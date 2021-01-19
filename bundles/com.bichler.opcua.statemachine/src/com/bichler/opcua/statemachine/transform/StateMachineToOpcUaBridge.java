package com.bichler.opcua.statemachine.transform;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.Behavior;
import org.eclipse.uml2.uml.BehavioralFeature;
import org.eclipse.uml2.uml.CallEvent;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.FunctionBehavior;
import org.eclipse.uml2.uml.Generalization;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.PackageableElement;
import org.eclipse.uml2.uml.Pseudostate;
import org.eclipse.uml2.uml.PseudostateKind;
import org.eclipse.uml2.uml.Region;
import org.eclipse.uml2.uml.State;
import org.eclipse.uml2.uml.StateMachine;
import org.eclipse.uml2.uml.Transition;
import org.eclipse.uml2.uml.Trigger;
import org.eclipse.uml2.uml.Vertex;
import org.eclipse.uml2.uml.internal.impl.ClassifierImpl;
import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.ExtensionObject;
import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.QualifiedName;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.core.ObjectTypeAttributes;
import org.opcfoundation.ua.encoding.EncoderContext;

import com.bichler.opcua.statemachine.addressspace.importer.StatemachineNodesetImporter;
import com.bichler.opcua.statemachine.exception.StatemachineException;
import com.bichler.opcua.statemachine.transform.model.StateMachineTemplate;

import opc.sdk.core.node.DefaultNodeFactory;
import opc.sdk.core.node.Node;
import opc.sdk.core.node.ObjectTypeNode;

public class StateMachineToOpcUaBridge implements IStateMachineExtractor {

	public StateMachineToOpcUaBridge() {
	}

	@Override
	public Node findStatemachineType(AbstractStateMachineToOpcTransformer transformer, StatemachineNodesetImporter importer,
			Model model, StateMachine statemachine, /* int index, */ List<Node> additionalStatemachineTypeNodes) {

		Map<String, Classifier> allInherits = new HashMap<>();
		// package
		for (int i = 0; i < model.getPackagedElements().size(); i++) {
			PackageableElement statemachinePackage = model.getPackagedElements().get(i);
			if (!(statemachinePackage instanceof Package)) {
				continue;
			}
			// collect all UML classes that inherits
			for (PackageableElement element : ((Package) statemachinePackage).getPackagedElements()) {
				// no class object
				if (!(element instanceof Classifier)) {
					continue;
				}
				// type hierachy
				Map<String, Classifier> inherits = IStateMachineExtractor.collectTypeStructure(importer,
						(Classifier) element);
				allInherits.putAll(inherits);
			}
		}

		// find supertype of the statemachine
		String supertypeName = IStateMachineExtractor.findSupertype(importer, allInherits, statemachine);
		if (supertypeName == null) {
			Logger.getLogger(getClass().getName()).log(Level.INFO, "Cannot find UML statemachine <"
					+ statemachine.getName() + "> type definition, using default OPC UA type <FiniteStateMachineType>");
			return importer.getNodesItemById(Identifiers.FiniteStateMachineType);
		}

		Node supertypeNode = importer.getNodeByBrowsename(supertypeName);
		if (supertypeNode == null) {
			try {
				NodeId nodeId = importer.getNamespaceTable().toNodeId(rekCreateTypeStructure(transformer, importer,
						statemachine, allInherits.get(supertypeName), /* index, */ additionalStatemachineTypeNodes));

				for (Node node : additionalStatemachineTypeNodes) {
					if (node.getNodeId().equals(nodeId)) {
						return node;
					}
				}
			} catch (StatemachineException e) {
				Logger.getLogger(getClass().getName()).log(Level.INFO, e.getMessage());
			} catch (ServiceResultException e) {
				Logger.getLogger(getClass().getName()).log(Level.INFO, e.getMessage());
			}
			return importer.getNodesItemById(Identifiers.FiniteStateMachineType);
		}

		return supertypeNode;
	}

	@Override
	public Map<String, StateMachineTemplate[]> transform(Model model) throws StatemachineException {
		Map<String, Map<String, StateMachine>> statemachineModel = new LinkedHashMap<>();
		Map<String, CallEvent> triggerEvents = new HashMap<>();
		// extract statemachine information
		for (PackageableElement element : model.getPackagedElements()) {
			if (!(element instanceof Package)) {
				continue;
			}

			String namespaceURI = ((Package) element).getURI();
			if (namespaceURI == null) {
				throw new StatemachineException(
						"UML package <" + element.getName() + "> does not contain a required URI");
			}
			if (namespaceURI.isEmpty()) {
				throw new StatemachineException(
						"UML package <" + element.getName() + "> does not contain a required URI");
			}

			Map<String, StateMachine> statemachines = statemachineModel.get(namespaceURI);
			if (statemachines == null) {
				statemachines = new HashMap<>();
				statemachineModel.put(namespaceURI, statemachines);
			}

			for (PackageableElement modelElement : ((Package) element).getPackagedElements()) {
				if (modelElement instanceof StateMachine) {
					statemachines.put(modelElement.getName(), (StateMachine) modelElement);
				} else if (modelElement instanceof CallEvent) {
					String name = IStateMachineExtractor.encapsulateEventName((CallEvent) modelElement);
					triggerEvents.put(name, (CallEvent) modelElement);
				}
			}
		}

		Map<String, StateMachineTemplate[]> templatesToGenerate = new LinkedHashMap<>();
		for (Entry<String, Map<String, StateMachine>> modelToGenerate : statemachineModel.entrySet()) {
			String namespaceURI = modelToGenerate.getKey();
			// set up statemachine templates
			List<StateMachineTemplate> templates = new ArrayList<>();
			for (Map.Entry<String, StateMachine> element : modelToGenerate.getValue().entrySet()) {
				Map<String, Transition> transitions = findTransitionsFromStatemachine(element.getValue());
				Map<String, Vertex> states = findStatesFromStatemachine(element.getValue());
				Map<Transition, List<Trigger>> triggers = findTriggers(transitions.values().toArray(new Transition[0]));
				Map<Transition, Classifier> effects = findEffects(element.getKey(),
						transitions.values().toArray(new Transition[0]));

				StateMachineTemplate smt = new StateMachineTemplate(element.getValue(), transitions, states, triggers,
						triggerEvents, effects);
				templates.add(smt);
			}

			templatesToGenerate.put(namespaceURI, templates.toArray(new StateMachineTemplate[0]));
		}
		return templatesToGenerate;
	}

	/**
	 * Collect all UML effects from the model.
	 * 
	 * @param model
	 * 
	 * @param transitions
	 * @return
	 * @throws StatemachineException
	 */
	private Map<Transition, Classifier> findEffects(String statemachine, Transition[] transitions)
			throws StatemachineException {
		Map<Transition, Classifier> effects = new HashMap<>();

		for (Transition transition : transitions) {
			Behavior effect = transition.getEffect();
			if (effect == null) {
				continue;
			}

			if (!(effect instanceof FunctionBehavior)) {
				Logger.getLogger(getClass().getName()).log(Level.INFO,
						"Statemachine <" + statemachine + "> has a transition "
								+ (transition.getName() != null ? " <" + transition.getName() + "> " : "")
								+ "with an invalid effect type");
				continue;
			}

			BehavioralFeature behavialFeature = effect.getSpecification();
			if (behavialFeature == null) {
				Logger.getLogger(getClass().getName()).log(Level.INFO,
						"Statemachine <" + statemachine + "> does not define a BehavioralFeature on transition "
								+ (transition.getName() != null ? " <" + transition.getName() + ">" : "") + "for effect"
								+ (effect.getName() != null ? " <" + effect.getName() + ">" : ""));
				continue;
			}
			Classifier eventClass = (Classifier) behavialFeature.eContainer();
			effects.put(transition, eventClass);
		}

		return effects;
	}

	/**
	 * Collect all UML state objects from the model.
	 * 
	 * @param element Statemachine
	 * @return KeyValuePair of statenames and states
	 */
	private Map<String, Vertex> findStatesFromStatemachine(StateMachine element) {
		Map<String, Vertex> states = new HashMap<>();
		for (Region region : ((StateMachine) element).getRegions()) {
			EList<Vertex> subvertices = region.getSubvertices();

			for (Vertex vertex : subvertices) {
				// vertex connectionpoint, pseudostate, state
				if (vertex instanceof State) {
					states.put(vertex.getName(), vertex);
				} else if (vertex instanceof Pseudostate) {
					// Initial state
					PseudostateKind kind = ((Pseudostate) vertex).getKind();
					switch (kind) {
					case INITIAL_LITERAL:
						states.put(vertex.getName(), vertex);
						break;
					default:
						break;
					}
				}
			}
		}
		return states;
	}

	/**
	 * Collect all UML transition objects from the model.
	 * 
	 * @param element Statemachine
	 * @return KeyValuePair of transitionnames and transitions
	 */
	private Map<String, Transition> findTransitionsFromStatemachine(StateMachine element) {
		Map<String, Transition> transitions = new HashMap<>();

		for (Region region : ((StateMachine) element).getRegions()) {
			EList<Transition> rTransitions = region.getTransitions();

			for (Transition transition : rTransitions) {
				transitions.put(IStateMachineExtractor.encapsulateName(transition), transition);
			}
		}
		return transitions;
	}

	/**
	 * Collect all UML trigger objects from the model.
	 * 
	 * @param element Statemachine
	 * @return KeyValuePair of triggernames and triggers
	 */
	private Map<Transition, List<Trigger>> findTriggers(Transition[] transitions) {
		Map<Transition, List<Trigger>> triggers = new HashMap<>();

		for (Transition transition : transitions) {
			if (transition.getTriggers() == null) {
				continue;
			}

			List<Trigger> triggersForTransition = triggers.get(transition);
			if (triggersForTransition == null) {
				triggersForTransition = new ArrayList<Trigger>();
				triggers.put(transition, triggersForTransition);
			}

			for (Trigger trigger : transition.getTriggers()) {
				triggersForTransition.add(trigger);
			}
		}

		return triggers;
	}

	/**
	 * Link to method: StateMachineTemplate:rekCreateEventTypeStructure
	 * 
	 * @param transformer
	 * @param importer
	 * @param statemachine
	 * @param typeClass
	 * @param index
	 * @param nodes
	 * @return
	 * @throws StatemachineException
	 */
	private ExpandedNodeId rekCreateTypeStructure(AbstractStateMachineToOpcTransformer transformer,
			StatemachineNodesetImporter importer, StateMachine statemachine, Classifier typeClass, /* int index, */
			List<Node> nodes) throws StatemachineException {

		String superTypeName = typeClass.getName();
		if (superTypeName == null) {
			superTypeName = importer.findTypenameFromResource((ClassifierImpl) typeClass);
		}
		// find type node
		Node typeNode = importer.getNodeByBrowsename(superTypeName);
		if (typeNode != null) {
			return importer.getNamespaceTable().toExpandedNodeId(typeNode.getNodeId());
		}

		if (typeClass.getGeneralizations().isEmpty()) {
			throw new StatemachineException("Inconsistent typedefinition for supertype " + typeClass.getName()
					+ ", using default type FiniteStatemachineType");
		}

		// find supertype
		Generalization generalization = typeClass.getGeneralizations().get(0);
		Classifier general = generalization.getGeneral();
		// get parent nodeid
		ExpandedNodeId parentId = rekCreateTypeStructure(transformer, importer, statemachine, general,
				/* index, */ nodes);
		// generate custom type
		DefaultNodeFactory nodeFactory = new DefaultNodeFactory();
		try {
			QualifiedName browseName = new QualifiedName(typeClass.getName());
			ObjectTypeAttributes ota = new ObjectTypeAttributes();
			ota.setDescription(new LocalizedText("", Locale.ENGLISH));
			ota.setDisplayName(new LocalizedText(typeClass.getName(), Locale.ENGLISH));
			ota.setIsAbstract(false);
			ota.setUserWriteMask(UnsignedInteger.ZERO);
			ota.setWriteMask(UnsignedInteger.ZERO);
			ExtensionObject attributes = ExtensionObject.binaryEncode(ota, EncoderContext.getDefaultInstance());
			NodeClass nodeClass = NodeClass.ObjectType;
			NodeId referenceType = Identifiers.HasSubtype;

			ExpandedNodeId requestedNewId = importer.getNamespaceTable().toExpandedNodeId(
					transformer.getNewNodeId(importer.getNamespaceTable(), statemachine, /* index, */ typeClass, null));

			typeNode = (ObjectTypeNode) nodeFactory.createNode(importer.getNamespaceTable(), browseName, attributes,
					nodeClass, parentId, referenceType, requestedNewId, null);
			nodes.add(typeNode);

			return requestedNewId;
		} catch (ServiceResultException e) {
			throw new StatemachineException(e.getMessage());
		}
	}
}
