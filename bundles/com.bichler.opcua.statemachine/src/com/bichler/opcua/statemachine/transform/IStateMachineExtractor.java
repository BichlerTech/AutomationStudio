package com.bichler.opcua.statemachine.transform;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.CallEvent;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Generalization;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.StateMachine;
import org.eclipse.uml2.uml.Transition;
import org.eclipse.uml2.uml.internal.impl.ClassifierImpl;

import com.bichler.opcua.statemachine.addressspace.importer.StatemachineNodesetImporter;
import com.bichler.opcua.statemachine.exception.StatemachineException;
import com.bichler.opcua.statemachine.transform.model.StateMachineTemplate;

import opc.sdk.core.node.Node;

public interface IStateMachineExtractor {

	/**
	 * Transforms an UML model to a programmatic statemachine template model
	 * 
	 * @param model Statemachine UML model
	 * @return a programmatic KeyValuePair template to generate an OPC UA
	 *         nodeset.xml
	 * @throws StatemachineException
	 */
	public Map<String, StateMachineTemplate[]> transform(Model model) throws StatemachineException;

	/**
	 * Finds the subtype for the statemachine to generate.
	 * 
	 * @param stateMachineToOpcTransformer
	 * 
	 * @param importer
	 * 
	 * @param model                        Statemachine UML model
	 * @param additionalStatemachineTypes  Statemachine subtypes defined in UML
	 * @return NodeId of the statemachine subtype
	 */
	public Node findStatemachineType(AbstractStateMachineToOpcTransformer transformer, StatemachineNodesetImporter importer,
			Model model, StateMachine statemachine, List<Node> additionalStatemachineTypes);

	public static Map<String, Classifier> collectTypeStructure(StatemachineNodesetImporter importer, Classifier value) {
		Map<String, Classifier> inherits = new HashMap<>();

		Classifier iteration = value;
		while (!iteration.getGeneralizations().isEmpty()) {
			EList<Generalization> generalizations = iteration.getGeneralizations();
			Generalization generalization = generalizations.get(0);
			Classifier general = generalization.getGeneral();

			String typeName = importer.findTypenameFromResource((ClassifierImpl) general);
			Classifier typeClass = importer.findTypeFromResource((ClassifierImpl) general);
			inherits.put(typeName, typeClass);

			iteration = typeClass;
		}

		return inherits;
	}

	public static String findSupertype(StatemachineNodesetImporter importer, Map<String, Classifier> inherits,
			Classifier statemachine) {
		String supertypeName = null;
		Classifier element = statemachine;
		while (element.getGeneralizations() != null && !element.getGeneralizations().isEmpty()) {
			Generalization generalization = element.getGeneralizations().get(0);
			Classifier general = generalization.getGeneral();
			if (element.getGeneralizations().size() > 1) {
				Logger.getLogger(IStateMachineExtractor.class.getName()).log(Level.INFO,
						"Statemachine <" + statemachine.getName() + "> has multiple generalizations, using supertype "
								+ general.getName());
			}

			String supertype = importer.findTypenameFromResource((ClassifierImpl) general);
			if (inherits.containsKey(supertype)) {
				supertypeName = supertype;
				break;
			}
			element = general;
		}

		return supertypeName;
	}

	/**
	 * Prevents a NullPointerException when a CallEvent object has no
	 * Attribute:Name.
	 * 
	 * @param Event Object to use
	 * @return Name of the event or operation if null
	 */
	public static String encapsulateEventName(CallEvent event) {
		if (event.getName() != null) {
			return event.getName();
		}
		if (event.getOperation() != null && event.getOperation().getName() != null) {
			return event.getOperation().getName();
		}
		return null;
	}

	/**
	 * Prevents a NullPointerException when a Transition object has no
	 * Attribute:Name.
	 * 
	 * @param Element Object to use
	 * @return Name of element or empty string if null
	 */
	public static String encapsulateName(NamedElement element) {
		if (!(element instanceof Transition)) {
			return element.getName();
		}

		if (element.getName() != null) {
			return element.getName();
		}
		// Attribute:name is null
		String transitionName = ((Transition) element).getSource().getName() + "To"
				+ ((Transition) element).getTarget().getName();
		return transitionName;
	}
}
