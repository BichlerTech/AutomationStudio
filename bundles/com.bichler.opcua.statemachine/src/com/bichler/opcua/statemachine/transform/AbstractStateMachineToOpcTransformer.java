package com.bichler.opcua.statemachine.transform;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.uml2.uml.CallEvent;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Region;
import org.eclipse.uml2.uml.StateMachine;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.internal.resource.UMLResourceFactoryImpl;
import org.eclipse.uml2.uml.resource.UMLResource;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.common.NamespaceTable;
import org.opcfoundation.ua.transport.tcp.io.SequenceNumber;

import com.bichler.opcua.statemachine.addressspace.ModelV2Exporter;
import com.bichler.opcua.statemachine.addressspace.importer.StatemachineNodesetImporter;
import com.bichler.opcua.statemachine.datadictionary.StructuredDataTypeManager;
import com.bichler.opcua.statemachine.exception.StatemachineException;
import com.bichler.opcua.statemachine.transform.model.ClassTemplate;
import com.bichler.opcua.statemachine.transform.model.ClassTemplateFactory;
import com.bichler.opcua.statemachine.transform.model.StateMachineTemplate;

import opc.sdk.core.context.StringTable;
import opc.sdk.core.node.Node;

public abstract class AbstractStateMachineToOpcTransformer {

	private IStateMachineExtractor statemachineBridge = null;
	private ClassToOpcUaBridge classBridge = null;

	private Set<String> symbolicIds = null;

	/** Numeric ids for OPC UA nodes */
	private Map<Integer, SequenceNumber> dynamicIds = new HashMap<>();
	/** Flag using symbolic ids */
	private boolean flagSymbolicIds;

	public AbstractStateMachineToOpcTransformer(boolean flagSymbolicIds) {
		this.statemachineBridge = new StateMachineToOpcUaBridge();
		this.classBridge = new ClassToOpcUaBridge();
		// string symbolic name ids
		this.flagSymbolicIds = flagSymbolicIds;

		this.symbolicIds = new HashSet<String>();
	}

	public void transform(File resourceFile, File output) throws StatemachineException {
		// number of transformed models
		int count = 0;
		try {
			// initialize OPC UA default nodes and types
			StatemachineNodesetImporter importer = new StatemachineNodesetImporter();
			importer.importNodeSet(loadDefaultOpcUaModelFiles());
			importer.importClassModel(loadUMLDefaultOpcUaClassFiles());

			boolean exist = resourceFile.exists();
			if (!exist) {
				throw new StatemachineException("UML file " + resourceFile.getName() + " does not exist");
			}

			ResourceSet resource = new ResourceSetImpl();
			resource.getResourceFactoryRegistry().getExtensionToFactoryMap().put(UMLResource.FILE_EXTENSION,
					new UMLResourceFactoryImpl());
			resource.getPackageRegistry().put(UMLPackage.eNS_URI, UMLPackage.eINSTANCE.eClass());
			URI uml_model_path = URI.createFileURI(resourceFile.getAbsolutePath());
			UMLResource res = (UMLResource) resource.getResource(uml_model_path, true);
			Model model = (Model) res.getContents().get(0);

			// find all statemachines from the model
			Map<String, StateMachineTemplate[]> statemachineTemplates = this.statemachineBridge.transform(model);

			if (statemachineTemplates.isEmpty()) {
				throw new StatemachineException(
						"An UML package with a statemachine diagram does not exist in file " + resourceFile.getName());
			}

			// collect all nodes to export
			List<Node> all = new ArrayList<>();
			for (Entry<String, StateMachineTemplate[]> templateSet : statemachineTemplates.entrySet()) {
				if (templateSet.getValue().length <= 0) {
					Logger.getLogger(getClass().getName()).log(Level.INFO,
							"UML package " + templateSet.getKey() + " does not contain a valid statemachine");
					continue;
				}

				// add namespace to table
				/* int index = */importer.getNamespaceTable().add(templateSet.getKey());
				// creates the OPC UA nodes for a given statemachine
				Map<StateMachineTemplate, List<Node>> statemachines = transformStatemachine(model, importer, /*
																												 * index,
																												 */
						templateSet.getValue());

				if (statemachines.isEmpty()) {
					Logger.getLogger(getClass().getName()).log(Level.INFO,
							"There is no UML statemachine diagram in file " + resourceFile.getName() + " for namespace "
									+ templateSet.getKey());
					continue;
				}

				for (Entry<StateMachineTemplate, List<Node>> entries : statemachines.entrySet()) {
					all.addAll(entries.getValue());
				}
			}

			Map<String, ClassTemplate[]> classmodelTemplates = this.classBridge.transform(model, importer, all);
			for (Entry<String, ClassTemplate[]> templates : classmodelTemplates.entrySet()) {
				Map<ClassTemplate, List<Node>> classes = transformClass(model, importer, templates.getValue(), all);

				if (classes.isEmpty()) {
					Logger.getLogger(getClass().getName()).log(Level.INFO, "There is no UML class diagram in file "
							+ resourceFile.getName() + " for namespace " + templates.getKey());
					continue;
				}

				for (Entry<ClassTemplate, List<Node>> entries : classes.entrySet()) {
					all.addAll(entries.getValue());
				}
			}

			all.addAll(importer.getStructuredDatatypeNodesItemList().values());

			// exports the OPC UA statemachine nodes to a nodeset.xml
			NamespaceTable nsTable = importer.getNamespaceTable();
			StringTable serverUri = new StringTable();
			ModelV2Exporter exporter = new ModelV2Exporter(nsTable, serverUri);

			FileOutputStream fos = null;
			try {
				// create a target output file if not exist
				if (output.exists()) {
					output.delete();
				}
				output.createNewFile();

				fos = new FileOutputStream(output);

				// write nodes to target file
				exporter.writeNodes(fos, importer, nsTable, all);
				count++;
			} catch (IOException e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage());
				throw new StatemachineException(e);
			} finally {
				if (fos != null) {
					try {
						fos.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

		} catch (StatemachineException ex) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, ex.getMessage());
			throw ex;
		}

		if (count <= 0) {
			throw new StatemachineException("There is no valid UML statemachine to transform to OPC UA");
		}
	}

	/**
	 * NodeId generator
	 * 
	 * @param statemachine   using statemachine name for symbolic ids
	 * @param namespaceIndex namespace index for ids
	 * @param element        element to create a symbolic name for
	 * @param append         Appendix for symbolic ids (e.g. State->StateNumber
	 * @return newly generated OPC UA NodeId
	 */
	public NodeId getNewNodeId(NamespaceTable nsTable, StateMachine statemachine, NamedElement element, String append) {
		NodeId newId = null;

		String namespaceUri = loopNsUri(element);
		int namespaceIndex = nsTable.add(namespaceUri);
		// symbolic id
		if (this.flagSymbolicIds) {
			String symbolicName = buildSymbolicName(statemachine, element, append);
			symbolicName = putSymbolicIds(symbolicName);
			newId = new NodeId(namespaceIndex, symbolicName);
		}
		// numeric id
		else {
			// next numeric id
			SequenceNumber id = getNextId(namespaceIndex, false);
			newId = new NodeId(namespaceIndex, new UnsignedInteger(id.getCurrentSendSequenceNumber()));
			// increase count
			id.getNextSendSequencenumber();
			return newId;
		}

		return newId;
	}

	public NodeId getNewNodeIdWithPrefix(NamespaceTable nsTable, Classifier typedClassifier, String nodeIdPrefix,
			String newBrowseName) {

		NodeId newId = null;

		String namespaceUri = loopNsUri(typedClassifier);
		int namespaceIndex = nsTable.add(namespaceUri);
		// symbolic id
		if (this.flagSymbolicIds) {
			String symbolicName = ClassTemplateFactory.concatNodeIdPrefix(nodeIdPrefix, newBrowseName);
			symbolicName = putSymbolicIds(symbolicName);
			newId = new NodeId(namespaceIndex, symbolicName);
		}
		// numeric id
		else {
			// next numeric id
			SequenceNumber id = getNextId(namespaceIndex, false);
			newId = new NodeId(namespaceIndex, new UnsignedInteger(id.getCurrentSendSequenceNumber()));
			// increase count
			id.getNextSendSequencenumber();
			return newId;
		}

		return newId;
	}

	public NodeId getNewNodeIdForObjectType(NamespaceTable nsTable, Classifier typedClassifier) {
		NodeId newId = null;

		String namespaceUri = loopNsUri(typedClassifier);
		int namespaceIndex = nsTable.add(namespaceUri);
		// symbolic id
		if (this.flagSymbolicIds) {
			String symbolicName = buildSymbolicNameForObjectType(typedClassifier);
			symbolicName = putSymbolicIds(symbolicName);
			newId = new NodeId(namespaceIndex, symbolicName);
		}
		// numeric id
		else {
			// next numeric id
			SequenceNumber id = getNextId(namespaceIndex, false);
			newId = new NodeId(namespaceIndex, new UnsignedInteger(id.getCurrentSendSequenceNumber()));
			// increase count
			id.getNextSendSequencenumber();
			return newId;
		}

		return newId;
	}

	public NodeId getNextNumericNodeId(int namespaceIndex) {
		SequenceNumber id = getNextId(namespaceIndex, false);
		NodeId show = new NodeId(namespaceIndex, new UnsignedInteger(id.getCurrentSendSequenceNumber()));
		id.getNextSendSequencenumber();
		return show;
	}

//	public NodeId showNextNumericNodeId(int namespaceIndex) {
//		SequenceNumber id = getNextId(namespaceIndex, true);
//		NodeId show = new NodeId(namespaceIndex, new UnsignedInteger(id.getCurrentSendSequenceNumber()));
//		return show;
//	}

	public abstract File[] loadDefaultOpcUaModelFiles();

	public abstract File[] loadUMLDefaultOpcUaClassFiles();

	private String buildSymbolicNameForObjectType(Classifier classifier) {
		return classifier.getName();
	}

	/**
	 * Build a symbolic name with the attribute name of the element. If the element
	 * is nested within other elements a '.' is a delimiter between names.
	 * 
	 * @param element
	 * 
	 * @return symbolic name for the element
	 */
	private String buildSymbolicName(StateMachine statemachine, NamedElement element, String append) {

		String symbolicname = (append != null) ? "." + append : "";
		// callevent nodes are under statemachine
		if (element instanceof CallEvent) {
			String eventName = IStateMachineExtractor.encapsulateEventName((CallEvent) element);
			symbolicname = statemachine.getName() + "." + eventName + symbolicname;
			return symbolicname;
		}
		NamedElement loopingElement = element;

		// return symbolic class/datatype name
		if (loopingElement instanceof Classifier) {
			return IStateMachineExtractor.encapsulateName(loopingElement);
		}

		EObject current = null;
		// build a symbolicname
		do {
			current = loopingElement;
			// skip region, it is not included in OPC UA
			if (loopingElement instanceof Region) {
				loopingElement = (NamedElement) loopingElement.eContainer();
				continue;
			}
			// add name to symbolicname
			symbolicname = IStateMachineExtractor.encapsulateName(loopingElement) + symbolicname;
			// add a delimiter if needed
			if (!(loopingElement instanceof StateMachine)) {
				symbolicname = "." + symbolicname;
			}
			loopingElement = (NamedElement) loopingElement.eContainer();
		} while (current != null && !(current instanceof StateMachine));

		return symbolicname;
	}

	/**
	 * Returns the next numeric id value.
	 * 
	 * @param namespaceIndex Next Id from a given namespace index
	 * @return next numeric value
	 */
	private SequenceNumber getNextId(int namespaceIndex, boolean show) {
		SequenceNumber sId = this.dynamicIds.get(namespaceIndex);
		if (sId == null) {
			sId = new SequenceNumber();
			if (!show) {
				this.dynamicIds.put(namespaceIndex, sId);
			}
		}
		return sId;
	}

	private String loopNsUri(EObject element) {
		if (element instanceof Package) {
			return ((Package) element).getURI();
		}

		return loopNsUri(element.eContainer());
	}

	private String putSymbolicIds(String symbolicName) {
		boolean contains = this.symbolicIds.contains(symbolicName);

		if (!contains) {
			this.symbolicIds.add(symbolicName);
		} else {
			int endIndex = symbolicName.lastIndexOf("_");
			int appendix = 1;
			if (endIndex >= 0) {
				String endppendix = symbolicName.substring(endIndex, symbolicName.length());
				appendix = Integer.parseInt(endppendix) + 1;
				symbolicName = symbolicName.substring(0, endIndex);
			}
			symbolicName = symbolicName += "_" + appendix;
		}

		return symbolicName;
	}

	private Map<ClassTemplate, List<Node>> transformClass(Model model, StatemachineNodesetImporter importer,
			ClassTemplate[] templates, List<Node> createdNodes) {

		List<Node> temporary = new ArrayList<>();
		Map<ClassTemplate, List<Node>> classes = new HashMap<>();
		// create OPC UA types - save @temporary
		for (ClassTemplate template : templates) {
			template.toOpcUaType(this, importer, createdNodes, temporary);
		}

		// create aggregation for types
		for (ClassTemplate template : templates) {
			List<Node> classNodes = template.toOpcUaNodes(this, importer, templates, createdNodes, temporary, true, "");
			classes.put(template, classNodes);
		}

		return classes;
	}

	/**
	 * Transforms a statemachine template to OPC UA nodes.
	 * 
	 * @param importer  Basic information about OPC UA information model (used for
	 *                  type generation e.g.: StatemachineType, StateType,
	 *                  TransitionType,..) and UML model ids for subtyping
	 * 
	 * @param templates Statemachine templates from the UML model
	 * 
	 * @param model     of the statemachine to transform
	 * 
	 * @return KeyValuePair from Statemachines and its transformed OPC UA Nodes
	 * @throws StatemachineException
	 */
	private Map<StateMachineTemplate, List<Node>> transformStatemachine(Model model,
			StatemachineNodesetImporter importer, StateMachineTemplate[] templates) throws StatemachineException {

		Map<StateMachineTemplate, List<Node>> statemachines = new HashMap<>();
		for (StateMachineTemplate template : templates) {
			// find the subtype which the statemachine is inherited from
			List<Node> additionalStatemachineTypes = new ArrayList<>();
			Node structureStatemachine = this.statemachineBridge.findStatemachineType(this, importer, model,
					template.getStatemachine(), /* index, */ additionalStatemachineTypes);
			// Create an OPC UA node structure for the template
			List<Node> statemachineNodes = template.toOpcUaNodes(this, importer, /* index, */ structureStatemachine);
			statemachineNodes.addAll(additionalStatemachineTypes);

			statemachines.put(template, statemachineNodes);
		}
		return statemachines;
	}

}
