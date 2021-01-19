package com.bichler.opcua.statemachine.transform.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.CallEvent;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Generalization;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.ParameterDirectionKind;
import org.eclipse.uml2.uml.Pseudostate;
import org.eclipse.uml2.uml.PseudostateKind;
import org.eclipse.uml2.uml.State;
import org.eclipse.uml2.uml.StateMachine;
import org.eclipse.uml2.uml.Transition;
import org.eclipse.uml2.uml.Trigger;
import org.eclipse.uml2.uml.Vertex;
import org.eclipse.uml2.uml.internal.impl.ClassifierImpl;
import org.eclipse.uml2.uml.internal.impl.ParameterImpl;
import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.ExtensionObject;
import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.QualifiedName;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.AccessLevel;
import org.opcfoundation.ua.core.Argument;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.MethodAttributes;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.core.ObjectTypeAttributes;
import org.opcfoundation.ua.core.VariableAttributes;
import org.opcfoundation.ua.encoding.EncoderContext;
import org.opcfoundation.ua.encoding.EncodingException;

import com.bichler.opcua.statemachine.addressspace.importer.StatemachineNodesetImporter;
import com.bichler.opcua.statemachine.exception.StatemachineException;
import com.bichler.opcua.statemachine.transform.AbstractStateMachineToOpcTransformer;
import com.bichler.opcua.statemachine.transform.IStateMachineExtractor;
import com.bichler.opcua.statemachine.transform.StateMachineMappingContext;

import opc.sdk.core.enums.ValueRanks;
import opc.sdk.core.node.DefaultNodeFactory;
import opc.sdk.core.node.Node;
import opc.sdk.core.node.ObjectTypeNode;

public class StateMachineTemplate {

	private StateMachineMappingContext mappingContext = new StateMachineMappingContext();
	private StateMachine statemachine = null;
	private Map<String, Transition> transitions = new HashMap<>();
	private Map<String, Vertex> states = new HashMap<>();
	private Map<Transition, List<Trigger>> triggers = new HashMap<>();
	private Map<String, CallEvent> triggerEvents = new HashMap<>();
	private Map<Transition, Classifier> effects = new HashMap<>();

	// private NodeId subtype = NodeId.NULL;

	public StateMachineTemplate(StateMachine statemachine, Map<String, Transition> transitions,
			Map<String, Vertex> states, Map<Transition, List<Trigger>> triggers,
			Map<String, CallEvent> allTriggerEvents, Map<Transition, Classifier> effects) {

		this.statemachine = statemachine;
		this.transitions = transitions;
		this.states = states;
		this.triggers = triggers;
		this.effects = effects;

		Map<String, CallEvent> callEvents = new HashMap<String, CallEvent>();

		for (Entry<Transition, List<Trigger>> triggerList : this.triggers.entrySet()) {
			List<Trigger> value = triggerList.getValue();
			for (Trigger trigger : value) {
				if (trigger.getEvent() == null) {
					Logger.getLogger(getClass().getName()).log(Level.INFO,
							"Statemachine <" + statemachine.getName() + "> does not define a CallEvent for a trigger "
									+ (trigger.getName() == null ? "" : trigger.getName()));
					continue;
				}
				if (!(trigger.getEvent() instanceof CallEvent)) {
					Logger.getLogger(getClass().getName()).log(Level.INFO,
							"Skip an event for Statemachine <" + statemachine.getName()
									+ "> other than CallEvent for a trigger "
									+ (trigger.getName() == null ? "" : trigger.getName()));
					continue;
				}

				String name = IStateMachineExtractor.encapsulateEventName((CallEvent) trigger.getEvent());
				if (name == null) {
					Logger.getLogger(getClass().getName()).log(Level.INFO,
							"Statemachine <" + statemachine.getName()
									+ "> does not define a CallEvent or Operation name for a trigger "
									+ (trigger.getName() == null ? "" : trigger.getName()));
					continue;
				}

				if (this.triggerEvents.containsKey(name)) {
					continue;
				}

				CallEvent event = allTriggerEvents.get(name);
				if (event == null) {
					Logger.getLogger(getClass().getName()).log(Level.INFO,
							"Statemachine <" + statemachine.getName() + "> does not define an event for a trigger "
									+ (trigger.getName() == null ? "" : trigger.getName()));
					continue;
				}
				if (event.getOperation() == null) {
					Logger.getLogger(getClass().getName()).log(Level.INFO, "Statemachine <" + statemachine.getName()
							+ "> does not define an optional Operation for CallEvent " + name);
				}

				callEvents.put(name, event);
			}
		}

		this.triggerEvents = callEvents;
		this.mappingContext.setTriggerMappings(this.triggers);
		this.mappingContext.setEffectMappings(this.effects);
	}

	/**
	 * Transforms an UML template to an OPC UA information model structure.
	 * 
	 * @param transformer
	 * @param importer
	 * @param structureStatemachine
	 * @return
	 * @throws StatemachineException
	 */
	public List<Node> toOpcUaNodes(AbstractStateMachineToOpcTransformer transformer,
			StatemachineNodesetImporter importer, Node structureStatemachine) throws StatemachineException {

		// create opc ua type structure
		List<Node> nodes = new ArrayList<Node>();
		Node typeDef = createStatemachineType(transformer, importer, structureStatemachine);
		// add statemachine type node
		nodes.add(typeDef);

		// states
		Map<String, List<Node>> states = new HashMap<>();
		if (this.states.isEmpty()) {
			Logger.getLogger(getClass().getName()).log(Level.INFO,
					"Statemachine <" + this.statemachine.getName() + "> does not contain UML states");
		} else {
			infoIfInitialState();
		}

		for (Entry<String, Vertex> entry : this.states.entrySet()) {
			try {
				List<Node> state = createState(transformer, importer, typeDef, entry.getValue());
				states.put(entry.getKey(), state);
			} catch (ServiceResultException e) {
				e.printStackTrace();
			}
		}
		// add state nodes
		fillNodes(nodes, states);
		// transitions´
		Map<String, List<Node>> transitions = new HashMap<String, List<Node>>();
		if (this.transitions.isEmpty()) {
			Logger.getLogger(getClass().getName()).log(Level.INFO,
					"Statemachine <" + this.statemachine.getName() + "> does not contain UML transitions");
		}
		for (Entry<String, Transition> entry : this.transitions.entrySet()) {
			try {
				List<Node> transition = createTransition(transformer, importer, typeDef, entry.getValue());
				transitions.put(entry.getKey(), transition);
			} catch (ServiceResultException e) {
				e.printStackTrace();
			}
		}
		// add transition nodes
		fillNodes(nodes, transitions);
		// triggerEvents
		Map<String, List<Node>> triggerEvents = new HashMap<String, List<Node>>();
		if (this.triggerEvents.isEmpty()) {
			Logger.getLogger(getClass().getName()).log(Level.INFO,
					"Statemachine <" + this.statemachine.getName() + "> does not contain UML triggers");
		}
		for (Entry<String, CallEvent> entry : this.triggerEvents.entrySet()) {
			List<Node> events = createTriggerEvent(transformer, importer, typeDef, entry.getValue());
			triggerEvents.put(entry.getKey(), events);

		}
		// add trigger nodes
		fillNodes(nodes, triggerEvents);
		// effects
		Map<String, List<Node>> eventTypes = new HashMap<>();
		if (this.effects.isEmpty()) {
			Logger.getLogger(getClass().getName()).log(Level.INFO,
					"Statemachine <" + this.statemachine.getName() + "> does not contain UML effects");
		}
		for (Entry<Transition, Classifier> entry : this.effects.entrySet()) {
			List<Node> event = createEffectEvent(transformer, importer, typeDef, entry.getValue());
			eventTypes.put(entry.getValue().getName(), event);
		}
		// add effect nodes
		fillNodes(nodes, eventTypes);

		createDefaultStateMachineTransitionReferences(importer, nodes, typeDef);

		return nodes;
	}

	/**
	 * Returns the core statemachine
	 * 
	 * @return UML statemachine
	 */
	public StateMachine getStatemachine() {
		return this.statemachine;
	}

	private void infoIfInitialState() {
		boolean initialStateExist = false;
		for (Vertex state : this.states.values()) {
			if (state instanceof Pseudostate) {
				PseudostateKind kind = ((Pseudostate) state).getKind();
				if (kind == PseudostateKind.INITIAL_LITERAL) {
					initialStateExist = true;
					break;
				}
			}
		}
		if (!initialStateExist) {
			Logger.getLogger(getClass().getName()).log(Level.INFO,
					"Statemachine <" + this.statemachine.getName() + "> does not contain an UML initialstate");
		}
	}

	/**
	 * Create OPC UA references for a statemachine
	 * 
	 * @param importer
	 * @param nodes
	 * @param typeDef
	 */
	private void createDefaultStateMachineTransitionReferences(StatemachineNodesetImporter importer, List<Node> nodes,
			Node typeDef) {
		this.mappingContext.createTransitionReferences(importer, nodes, typeDef);
	}

	private List<Node> createEffectEvent(AbstractStateMachineToOpcTransformer transformer,
			StatemachineNodesetImporter importer, /* int index, */ Node typeDef, Classifier value)
			throws StatemachineException {

		List<Node> values = new ArrayList<>();
		// generate event type
		rekCreateEventTypeStructure(transformer, importer, values, value);
		// map effect type
		Node last = null;
		if (values.size() > 0) {
			last = values.get(values.size() - 1);
			this.mappingContext.mapNamedElement(importer.getNamespaceTable().toExpandedNodeId(last.getNodeId()), value);
		}
		return values;
	}

	/**
	 * Creates an OPC UA state from an UML state
	 * 
	 * @param transformer
	 * @param importer
	 * @param index
	 * @param stateType
	 * @param state
	 * @return
	 * @throws ServiceResultException
	 * @throws StatemachineException
	 */
	private List<Node> createState(AbstractStateMachineToOpcTransformer transformer,
			StatemachineNodesetImporter importer, Node stateType, Vertex state)
			throws ServiceResultException, StatemachineException {

		NodeId type = getTypeFromState(state);

		Map<Node, List<Node>> structure = importer.fetchOpcNodeTypeStructure(type, Identifiers.StateType);
		List<Node> export = importer.extractStructure(this.mappingContext, transformer, this.statemachine, structure,
				stateType, state);

		return export;
	}

	/**
	 * Creates an OPC UA statemachine type from an UML statemachine
	 * 
	 * @param transformer
	 * @param importer
	 * @param index
	 * @param parentType
	 * @return
	 */
	private ObjectTypeNode createStatemachineType(AbstractStateMachineToOpcTransformer transformer,
			StatemachineNodesetImporter importer, Node parentType) {

		DefaultNodeFactory nodeFactory = new DefaultNodeFactory();
		try {
			QualifiedName browseName = new QualifiedName(this.statemachine.getName());
			ObjectTypeAttributes ota = new ObjectTypeAttributes();
			ota.setDescription(new LocalizedText("", Locale.ENGLISH));
			ota.setDisplayName(new LocalizedText(this.statemachine.getName(), Locale.ENGLISH));
			ota.setIsAbstract(false);
			ota.setUserWriteMask(UnsignedInteger.ZERO);
			ota.setWriteMask(UnsignedInteger.ZERO);
			ExtensionObject attributes = ExtensionObject.binaryEncode(ota, EncoderContext.getDefaultInstance());
			NodeClass nodeClass = NodeClass.ObjectType;
			ExpandedNodeId parentId = importer.getNamespaceTable().toExpandedNodeId(parentType.getNodeId());
			NodeId referenceType = Identifiers.HasSubtype;

			ExpandedNodeId requestedNewId = importer.getNamespaceTable().toExpandedNodeId(
					transformer.getNewNodeId(importer.getNamespaceTable(), this.statemachine, this.statemachine, null));

			return (ObjectTypeNode) nodeFactory.createNode(importer.getNamespaceTable(), browseName, attributes,
					nodeClass, parentId, referenceType, requestedNewId, null);

		} catch (ServiceResultException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Creates an OPC UA transition from an UML transition between two states
	 * 
	 * @param transformer
	 * @param importer
	 * @param index
	 * @param transitionType
	 * @param transition
	 * @return
	 * @throws ServiceResultException
	 */
	private List<Node> createTransition(AbstractStateMachineToOpcTransformer transformer,
			StatemachineNodesetImporter importer, Node transitionType, Transition transition)
			throws ServiceResultException {
		NodeId type = Identifiers.TransitionType;

		Map<Node, List<Node>> structure = importer.fetchOpcNodeTypeStructure(type, Identifiers.TransitionType);
		List<Node> export = importer.extractStructure(this.mappingContext, transformer, this.statemachine, structure,
				transitionType, transition);

		return export;
	}

	/**
	 * Creates an OPC UA method from an UML trigger (On transition)
	 * 
	 * @param transformer
	 * @param importer
	 * @param index
	 * @param parent
	 * @param triggerEvent
	 * @return
	 */
	private List<Node> createTriggerEvent(AbstractStateMachineToOpcTransformer transformer,
			StatemachineNodesetImporter importer, Node parent, CallEvent triggerEvent) {

		
		
		DefaultNodeFactory nodeFactory = new DefaultNodeFactory();
		List<Node> triggers = new ArrayList<>();

		QualifiedName browsename = new QualifiedName(IStateMachineExtractor.encapsulateEventName(triggerEvent));
		MethodAttributes encodeable = new MethodAttributes();
		encodeable.setDescription(LocalizedText.NULL);// new LocalizedText("", Locale.ENGLISH));
		encodeable.setDisplayName(
				new LocalizedText(IStateMachineExtractor.encapsulateEventName(triggerEvent), Locale.ENGLISH));
		encodeable.setExecutable(false);
		encodeable.setUserExecutable(false);
		encodeable.setUserWriteMask(UnsignedInteger.ZERO);
		encodeable.setWriteMask(UnsignedInteger.ZERO);

		ExtensionObject attributes = null;
		try {
			attributes = ExtensionObject.binaryEncode(encodeable, EncoderContext.getDefaultInstance());

			NodeClass nodeClass = NodeClass.Method;
			ExpandedNodeId parentId = importer.getNamespaceTable().toExpandedNodeId(parent.getNodeId());
			NodeId referenceType = Identifiers.HasComponent;
			ExpandedNodeId requestedNodeId = importer.getNamespaceTable().toExpandedNodeId(
					transformer.getNewNodeId(importer.getNamespaceTable(), this.statemachine, triggerEvent, null));
			ExpandedNodeId typeDefinition = null;

		
			Node newNode = nodeFactory.createNode(importer.getNamespaceTable(), browsename, attributes, nodeClass,
					parentId, referenceType, requestedNodeId, typeDefinition);
			triggers.add(newNode);

			this.mappingContext.mapNamedElement(requestedNodeId, triggerEvent);

			if (triggerEvent != null && triggerEvent.getOperation() != null) {
				List<Node> args = createMethodArgumentNode(transformer, importer, browsename, requestedNodeId,
						triggerEvent, triggerEvent.getOperation());
				triggers.addAll(args);
			}
		} catch (ServiceResultException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE,
					"Cannot encode method node from operation " + browsename.getName());
		}

		return triggers;
	}

	private List<Node> createMethodArgumentNode(AbstractStateMachineToOpcTransformer transformer,
			StatemachineNodesetImporter importer, QualifiedName methodName, ExpandedNodeId parentId,
			CallEvent triggerEvent, Operation operation) {
		List<Node> args = new ArrayList<Node>();
		EList<Parameter> ownedParameters = operation.getOwnedParameters();

		if (ownedParameters.isEmpty()) {
			return args;
		}

		List<ExtensionObject> inputArguments = new ArrayList<>();
		List<ExtensionObject> outputArguments = new ArrayList<>();
		for (Parameter parameter : ownedParameters) {
			ParameterDirectionKind direction = ((ParameterImpl) parameter).getDirection();

			String name = parameter.getName();
			if (name == null) {
				Logger.getLogger(getClass().getName()).log(Level.INFO,
						"A CallEvent " + methodName.getName() + " parameter has no name");
				continue;
			}

			if (parameter.getType() == null) {
				Logger.getLogger(getClass().getName()).log(Level.INFO,
						"A CallEvent " + methodName.getName() + " parameter " + name + " has no type defined");
				continue;
			}

			Node datatypeNode = importer.getNodeByBrowsename(parameter.getType().getName());
			NodeId datatypeId = datatypeNode.getNodeId();

			Integer valueRank = -1;
			UnsignedInteger[] arrayDimensions = new UnsignedInteger[0];
			LocalizedText description = LocalizedText.NULL;
			Argument arg = null;
			try {
				switch (direction) {
				case IN_LITERAL:
					arg = new Argument(name, datatypeId, valueRank, arrayDimensions, description);
					inputArguments.add(ExtensionObject.binaryEncode(arg, EncoderContext.getDefaultInstance()));
					break;
				case OUT_LITERAL:
					arg = new Argument(name, datatypeId, valueRank, arrayDimensions, description);
					outputArguments.add(ExtensionObject.binaryEncode(arg, EncoderContext.getDefaultInstance()));
					break;
				default:
					Logger.getLogger(getClass().getName()).log(Level.INFO, "A CallEvent " + methodName.getName()
							+ " parameter " + name + " uses an unsupported direction " + direction.getName());
					break;
				}
			} catch (EncodingException e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE,
						"Cannot encode argument from operation " + operation.getName());
			}
		}

		VariableAttributes encodeable = new VariableAttributes();
		encodeable.setDescription(LocalizedText.NULL);// new LocalizedText("", Locale.ENGLISH));
		encodeable.setUserWriteMask(UnsignedInteger.ZERO);
		encodeable.setWriteMask(UnsignedInteger.ZERO);
		encodeable.setAccessLevel(AccessLevel.getMask(AccessLevel.READONLY));
		encodeable.setArrayDimensions(new UnsignedInteger[0]);
		encodeable.setDataType(Identifiers.Argument);
		encodeable.setHistorizing(false);
		encodeable.setMinimumSamplingInterval(0.0);
		encodeable.setUserAccessLevel(AccessLevel.getMask(AccessLevel.READONLY));
		encodeable.setValueRank(ValueRanks.OneDimension.getValue());

		ExpandedNodeId typeDefinition = importer.getNamespaceTable().toExpandedNodeId(Identifiers.PropertyType);
		NodeClass nodeClass = NodeClass.Variable;
		NodeId referenceType = Identifiers.HasProperty;

		ExtensionObject attributes = null;

		if (!inputArguments.isEmpty()) {
			QualifiedName browsename = new QualifiedName(StatemachineNodesetImporter.INPUT_ARGUMENTS);
			encodeable.setDisplayName(new LocalizedText(StatemachineNodesetImporter.INPUT_ARGUMENTS, Locale.ENGLISH));
			encodeable.setValue(new Variant(inputArguments.toArray(new ExtensionObject[0])));
			try {
				attributes = ExtensionObject.binaryEncode(encodeable, EncoderContext.getDefaultInstance());
				ExpandedNodeId requestedNodeId = importer.getNamespaceTable()
						.toExpandedNodeId(transformer.getNewNodeId(importer.getNamespaceTable(), this.statemachine,
								triggerEvent, StatemachineNodesetImporter.INPUT_ARGUMENTS));

				DefaultNodeFactory nodeFactory = new DefaultNodeFactory();
				Node newNode = nodeFactory.createNode(importer.getNamespaceTable(), browsename, attributes, nodeClass,
						parentId, referenceType, requestedNodeId, typeDefinition);
//				((VariableNode) newNode).setValue(new Variant(inputArguments.toArray(new ExtensionObject[0])));
				args.add(newNode);
			} catch (ServiceResultException e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE,
						"Cannot create inputargument for operation " + operation.getName());
			}
		}

		if (!outputArguments.isEmpty()) {
			QualifiedName browsename = new QualifiedName(StatemachineNodesetImporter.OUTPUT_ARGUMENTS);
			encodeable.setDisplayName(new LocalizedText(StatemachineNodesetImporter.OUTPUT_ARGUMENTS, Locale.ENGLISH));
			encodeable.setValue(new Variant(outputArguments.toArray(new ExtensionObject[0])));
			try {
				attributes = ExtensionObject.binaryEncode(encodeable, EncoderContext.getDefaultInstance());
				ExpandedNodeId requestedNodeId = importer.getNamespaceTable()
						.toExpandedNodeId(transformer.getNewNodeId(importer.getNamespaceTable(), this.statemachine,
								triggerEvent, StatemachineNodesetImporter.OUTPUT_ARGUMENTS));

				DefaultNodeFactory nodeFactory = new DefaultNodeFactory();
				Node newNode = nodeFactory.createNode(importer.getNamespaceTable(), browsename, attributes, nodeClass,
						parentId, referenceType, requestedNodeId, typeDefinition);
//				((VariableNode) newNode).setValue(new Variant(outputArguments.toArray(new ExtensionObject[0])));
				args.add(newNode);
			} catch (ServiceResultException e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE,
						"Cannot create inputargument for operation " + operation.getName());
			}
		}

		return args;
	}

	/**
	 * Collect all nodes to a list
	 * 
	 * @param nodes   Collected nodes
	 * @param inserts Map of nodes to add
	 */
	private void fillNodes(List<Node> nodes, Map<String, List<Node>> inserts) {
		for (Entry<String, List<Node>> insert : inserts.entrySet()) {
			nodes.addAll(insert.getValue());
		}
	}

	/**
	 * Returns a type for a state.
	 * 
	 * @param state UML state model
	 * @return an OPC UA StateType, InitialStateType or choiceStateType
	 * @throws StatemachineException
	 */
	private NodeId getTypeFromState(Vertex state) throws StatemachineException {
		if (state instanceof State) {
			return Identifiers.StateType;
		} else if (state instanceof Pseudostate) {
			PseudostateKind kind = ((Pseudostate) state).getKind();
			switch (kind) {
			case INITIAL_LITERAL:
				return Identifiers.InitialStateType;
			case CHOICE_LITERAL:
				return Identifiers.ChoiceStateType;
			default:
				break;
			}
		}

		// default type is state
		throw new StatemachineException(
				"Type of " + state.getClass() + " is not handled in this current version of statemachine generator!");
	}

	/**
	 * Link to method: StateMachineToOpcUaBridge:rekCreateTypeStructure. Create OPC
	 * UA event type structure.
	 * 
	 * @param transformer
	 * @param importer
	 * @param nodes
	 * @param inherits
	 * @param eventType
	 * @param index
	 * @return
	 * @throws StatemachineException
	 */
	private ExpandedNodeId rekCreateEventTypeStructure(AbstractStateMachineToOpcTransformer transformer,
			StatemachineNodesetImporter importer, List<Node> nodes, Classifier eventType) throws StatemachineException {

		String eventTypeName = eventType.getName();
		if (eventTypeName == null) {
			eventTypeName = importer.findTypenameFromResource((ClassifierImpl) eventType);
		}

		// find type node
		Node typeNode = importer.getNodeByBrowsename(eventTypeName);
		if (typeNode != null) {
			if (nodes.isEmpty()) {
				nodes.add(typeNode);

			}

			return importer.getNamespaceTable().toExpandedNodeId(typeNode.getNodeId());
		}
		// missing supertype
		if (eventType.getGeneralizations().isEmpty()) {
			throw new StatemachineException("Could not find the EventType - " + eventType.getName());
		}
		// find supertype
		Generalization generalization = eventType.getGeneralizations().get(0);
		Classifier general = generalization.getGeneral();
		// get parent nodeid
		ExpandedNodeId parentId = rekCreateEventTypeStructure(transformer, importer, nodes, general);
		// generate custom type
		DefaultNodeFactory nodeFactory = new DefaultNodeFactory();
		try {
			QualifiedName browseName = new QualifiedName(eventType.getName());
			ObjectTypeAttributes ota = new ObjectTypeAttributes();
			ota.setDescription(new LocalizedText("", Locale.ENGLISH));
			ota.setDisplayName(new LocalizedText(eventType.getName(), Locale.ENGLISH));
			ota.setIsAbstract(false);
			ota.setUserWriteMask(UnsignedInteger.ZERO);
			ota.setWriteMask(UnsignedInteger.ZERO);
			ExtensionObject attributes = ExtensionObject.binaryEncode(ota, EncoderContext.getDefaultInstance());
			NodeClass nodeClass = NodeClass.ObjectType;
			NodeId referenceType = Identifiers.HasSubtype;

			ExpandedNodeId requestedNewId = importer.getNamespaceTable().toExpandedNodeId(
					transformer.getNewNodeId(importer.getNamespaceTable(), this.statemachine, eventType, null));

			typeNode = (ObjectTypeNode) nodeFactory.createNode(importer.getNamespaceTable(), browseName, attributes,
					nodeClass, parentId, referenceType, requestedNewId, null);
			nodes.add(typeNode);

			return requestedNewId;
		} catch (ServiceResultException e) {
			throw new StatemachineException(e.getMessage());
		}
	}
}
