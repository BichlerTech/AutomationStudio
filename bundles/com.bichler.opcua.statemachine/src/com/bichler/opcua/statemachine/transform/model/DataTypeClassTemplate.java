package com.bichler.opcua.statemachine.transform.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.uml2.uml.Type;
import org.opcfoundation.ua.builtintypes.NodeId;

import com.bichler.astudio.opcua.components.addressspace.DefinitionBean;
import com.bichler.astudio.opcua.components.addressspace.DefinitionFieldBean;
import com.bichler.opcua.statemachine.addressspace.importer.StatemachineNodesetImporter;
import com.bichler.opcua.statemachine.datadictionary.StructuredDataTypeManager;
import com.bichler.opcua.statemachine.transform.AbstractStateMachineToOpcTransformer;

import opc.sdk.core.node.DataTypeNode;
import opc.sdk.core.node.Node;

public class DataTypeClassTemplate extends ClassTemplate {

	enum StructuredType {
		Structure, Enumeration;
	}

	private Node dataType;

	public DataTypeClassTemplate(Classifier classifier, NodeId nodeId) {
		super(classifier, nodeId);
	}

	@Override
	protected void initialize(EList<Property> attributes, EList<Operation> operations, EList<Stereotype> stereotypes) {
		this.supertype = generalToSupertype();
	}

	@Override
	Classifier generalToSupertype() {
		Classifier supertype = super.generalToSupertype();
		if (supertype == null) {
			Logger.getLogger(getClass().getName()).log(Level.INFO, "No supertype defined for datatype "
					+ getClassifier().getName());
		}
		return supertype;
	}

	@Override
	public List<Node> toOpcUaNodes(AbstractStateMachineToOpcTransformer transformer,
			StatemachineNodesetImporter importer, ClassTemplate[] templates, List<Node> createdNodes,
			List<Node> temporary, boolean addTypeNode, String nodeIdPrefix, Classifier supertype, Node parentDataType,
			Map<NodeId, NodeId> mapping) {

		List<Node> nodes = new ArrayList<>();

		if (supertype != null) {
			String newNodeIdPrefix = nodeIdPrefix;
			if (addTypeNode && parentDataType != null) {
				nodes.add(parentDataType);
				newNodeIdPrefix = ClassTemplateFactory.concatNodeIdPrefix(nodeIdPrefix, parentDataType);
			}
			boolean isStructure = false;
			boolean isEnumeration = false;

//		if (supertype == null) {
//			isStructure = true;
//		} else {
			isStructure = isSupertypeOf(supertype, StructuredType.Structure);
			isEnumeration = isSupertypeOf(supertype, StructuredType.Enumeration);
//		}

			// set up opc ua structure
			if (isStructure) {
				StructuredDataTypeManager manager = new StructuredDataTypeManager(transformer, importer);
				importer.getNodesItemList()
						.put(importer.getNamespaceTable().toExpandedNodeId(this.dataType.getNodeId()), this.dataType);
				manager.initOpcBinaryStructure((DataTypeNode) this.dataType);

				DefinitionBean definition = new DefinitionBean();
				definition.setDefinitionName(this.dataType.getBrowseName().getName());

				Classifier classifier = getClassifier();
				EList<Property> attributes = classifier.getAttributes();
				if (attributes != null) {
					for (Property property : attributes) {
						String name = property.getName();
						if(name == null) {
							Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Attribute name of an OPC UA datatype field cannot be empty");
							continue;
						}
						Type type = property.getType();
						if(type == null) {
							Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Attribute type of an OPC UA datatype field cannot be empty");
							continue;
						}
						
						Node datatype = findNode(importer, type.getName(), createdNodes, temporary);
						if (datatype == null) {
							Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Attribute type of an OPC UA datatype field is not from OPC UA datatype");
							continue;
						}
						NodeId datatypeId = datatype.getNodeId();
						DefinitionFieldBean field = new DefinitionFieldBean();
						field.setName(name);
						field.setDatatype(datatypeId);

						definition.addField(field);
					}
				}

				manager.writeOpcDataDictionary((DataTypeNode) this.dataType, definition);
				importer.getDatatypeDefinition().put(this.dataType.getNodeId(), definition);
//				Studio_ResourceManager.DATATYPE_DEFINITIONS.put(this.dataType.getNodeId(), definition.clone());
			} else if (isEnumeration) {
				StructuredDataTypeManager manager = new StructuredDataTypeManager(transformer, importer);
				importer.getNodesItemList()
						.put(importer.getNamespaceTable().toExpandedNodeId(this.dataType.getNodeId()), this.dataType);
				manager.initOpcEnumerationStructure((DataTypeNode) this.dataType);
			}
		}

		return nodes;
	}

	private NodeId findDatatypeId(StatemachineNodesetImporter importer, String name) {

		return NodeId.NULL;
	}

	@Override
	public List<Node> toOpcUaNodes(AbstractStateMachineToOpcTransformer transformer,
			StatemachineNodesetImporter importer, ClassTemplate[] templates, List<Node> createdNodes,
			List<Node> temporary, boolean addTypeNode, String nodeIdPrefix) {

		Map<NodeId, NodeId> mapping = new HashMap<>();

		return toOpcUaNodes(transformer, importer, templates, createdNodes, temporary, addTypeNode, nodeIdPrefix,
				this.supertype, this.dataType, mapping);
	}

	@Override
	public void toOpcUaType(AbstractStateMachineToOpcTransformer transformer, StatemachineNodesetImporter importer,
			List<Node> createdNodes, List<Node> temporary) {

		// DataType
		if (this.supertype != null) {
			// Create OPC UA ObjectType node
			Node type = createClassType(transformer, importer, createdNodes, temporary);
			if (type == null) {
				type = findNode(importer, getClassifier(), createdNodes, temporary);
			}
			this.dataType = type;
		}
		// null supertype is OPC UA Structure
//		else {
//			this.dataType = importer.getNodesItemById(Identifiers.Structure);
//		}
	}

	@Override
	protected Classifier getSupertype() {
		return this.supertype;
	}

	@Override
	Node createType(AbstractStateMachineToOpcTransformer transformer, StatemachineNodesetImporter importer,
			Classifier classifier, NodeId parentId) {

		return ClassTemplateFactory.createDataType(transformer, importer, classifier, parentId);
	}

	private boolean isSupertypeOf(Classifier type, StructuredType structuredType) {
		Classifier supertype = type;
		do {
			if (structuredType.name().equalsIgnoreCase(supertype.getName())) {
				return true;
			}

			if (!supertype.getGeneralizations().isEmpty()) {
				supertype = supertype.getGeneralizations().get(0).getGeneral();
			}
		} while (!supertype.getGeneralizations().isEmpty());

		return false;
	}
}
