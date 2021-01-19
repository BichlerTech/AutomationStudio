package com.bichler.opcua.statemachine.transform.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.eclipse.uml2.uml.AggregationKind;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.uml2.uml.Type;
import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.ReferenceNode;

import com.bichler.opcua.statemachine.addressspace.importer.StatemachineNodesetImporter;
import com.bichler.opcua.statemachine.transform.AbstractStateMachineToOpcTransformer;

import opc.sdk.core.node.Node;
import opc.sdk.core.node.ObjectNode;
import opc.sdk.core.node.ObjectTypeNode;
import opc.sdk.core.node.VariableNode;

public class ObjectTypeClassTemplate extends ClassTemplate {

	private Node objType = null;

	private List<Property> folders;
	private List<Property> objects;
	private List<Property> variables;
	private List<Operation> operations;

	public ObjectTypeClassTemplate(Classifier classifier, NodeId nodeId) {
		super(classifier, nodeId);
	}

	@Override
	protected void initialize(EList<Property> attributes, EList<Operation> operations, EList<Stereotype> stereotypes) {
		this.folders = new ArrayList<>();
		this.objects = new ArrayList<>();
		this.variables = new ArrayList<>();
		this.operations = new ArrayList<>();
		// objecttypes
		// -hasSubtype
		this.supertype = generalToSupertype();

		// attributes
		// - objects and -variables
		if (attributes != null) {
			for (Property property : attributes) {
				AggregationKind aggregation = property.getAggregation();
				Association association = property.getAssociation();
				if (association != null) {
					switch (aggregation) {
					case COMPOSITE_LITERAL:
						this.folders.add(property);
						break;
					case SHARED_LITERAL:
						this.objects.add(property);
						break;
					default:
						break;
					}

				} else {
					this.variables.add(property);
				}
			}
		}

		for (Operation operation : operations) {
			this.operations.add(operation);
		}
	}

	public String getName() {
		return getClassifier().getName();
	}

//	public String getSuperttypeName() {
//		return this.supertypeName;
//	}
	@Override
	public List<Node> toOpcUaNodes(AbstractStateMachineToOpcTransformer transformer,
			StatemachineNodesetImporter importer, ClassTemplate[] templates, List<Node> createdNodes,
			List<Node> temporary, boolean addTypeNode, String nodeIdPrefix, Classifier supertype, Node parentObjType,
			Map<NodeId, NodeId> mapping) {

		List<Node> nodes = new ArrayList<>();

		// ObjectType
		if (supertype != null) {
			String newNodeIdPrefix = nodeIdPrefix;
			// add type node to collection
			if (addTypeNode && parentObjType != null) {
				nodes.add(parentObjType);
				newNodeIdPrefix = ClassTemplateFactory.concatNodeIdPrefix(nodeIdPrefix, parentObjType);
			}
			// create aggregations
			List<Node> aggregations = createAggregation(transformer, importer, templates, parentObjType,
					newNodeIdPrefix, createdNodes, temporary, this.objects, this.folders, this.variables,
					this.operations);
			nodes.addAll(aggregations);
		}
		// Class without classifier is a OPC UA FolderType
		else {
			// skip creation of them itself
		}

		return nodes;
	}

	@Override
	public List<Node> toOpcUaNodes(AbstractStateMachineToOpcTransformer transformer,
			StatemachineNodesetImporter importer, ClassTemplate[] templates, List<Node> createdNodes,
			List<Node> temporary, boolean addTypeNode, String nodeIdPrefix) {

		Map<NodeId, NodeId> mapping = new HashMap<>();

		return toOpcUaNodes(transformer, importer, templates, createdNodes, temporary, addTypeNode, nodeIdPrefix,
				this.supertype, this.objType, mapping);
	}

	@Override
	public void toOpcUaType(AbstractStateMachineToOpcTransformer transformer, StatemachineNodesetImporter importer,
			List<Node> createdNodes, List<Node> temporary) {
		// ObjectType
		if (this.supertype != null) {
			// Create OPC UA ObjectType node
			Node type = createClassType(transformer, importer, createdNodes, temporary);
			if (type == null) {
				type = findNode(importer, getClassifier(), createdNodes, temporary);
			}
			this.objType = type;
		}
	}

//	public Node getTypeNode() {
//		return this.objType;
//	}

	List<Node> createAggregation(AbstractStateMachineToOpcTransformer transformer, StatemachineNodesetImporter importer,
			ClassTemplate[] templates, Node parent, String nodeIdPrefix, List<Node> createdNodes, List<Node> temporary,
			List<Property> objects, List<Property> folders, List<Property> variables, List<Operation> operations) {
		List<Node> nodes = new ArrayList<>();
		// Aggregation composite - FolderType
		List<Node> composites = createAggregationComposite(transformer, importer, getClassifier(), nodeIdPrefix, parent,
				createdNodes, temporary, folders);
		nodes.addAll(composites);
		// Aggregation shared - Object createAggregationSharedts
		List<Node> shareds = createAggregationShared(transformer, importer, templates, getClassifier(), nodeIdPrefix,
				parent, createdNodes, temporary, objects);
		nodes.addAll(shareds);
		// Property - Variables
		List<Node> properties = createProperty(transformer, importer, getClassifier(), nodeIdPrefix, parent,
				createdNodes, temporary, variables);
		nodes.addAll(properties);
		// Operation - Methods
		List<Node> methods = createOperations(transformer, importer, getClassifier(), nodeIdPrefix, parent,
				createdNodes, temporary, operations);
		nodes.addAll(methods);
		return nodes;
	}

	List<Node> createAggregationComposite(AbstractStateMachineToOpcTransformer transformer,
			StatemachineNodesetImporter importer, Classifier classifier, String nodeIdPrefix, Node parent,
			List<Node> createdNodes, List<Node> temporary, List<Property> folders) {

		List<Node> nodes = new ArrayList<>();
		for (Property folder : folders) {
			ObjectNode nodeFolder = ClassTemplateFactory.createObjectFolder(transformer, importer, classifier, parent,
					folder, nodeIdPrefix);
			if (nodeFolder == null) {
				continue;
			}
			nodes.add(nodeFolder);

			// create properties of folder
		}

		return nodes;
	}

	List<Node> createAggregationShared(AbstractStateMachineToOpcTransformer transformer,
			StatemachineNodesetImporter importer, ClassTemplate[] templates, Classifier classifier, String nodeIdPrefix,
			Node parent, List<Node> createdNodes, List<Node> temporary, List<Property> objects) {

		List<Node> nodes = new ArrayList<>();
		for (Property object : objects) {
			Map<NodeId, NodeId> mapping = new HashMap<>();
			Type type = object.getType();
			Node objTypeNode = findNode(importer, type.getName(), createdNodes, temporary);
			// create OpcUa Object from OpcUa ObjectType
			ObjectNode nodeObject = ClassTemplateFactory.createObject(transformer, importer, classifier, parent, object,
					objTypeNode, nodeIdPrefix);

			String newNodeIdPrefix = nodeIdPrefix;
			if (nodeObject != null) {
				nodes.add(nodeObject);
				// add nodeId prefix
				newNodeIdPrefix = ClassTemplateFactory.concatNodeIdPrefix(nodeIdPrefix,
						nodeObject.getBrowseName().getName());
				mapping.put(objTypeNode.getNodeId(), nodeObject.getNodeId());
			}
			// UML structure for supertype nodes
			ClassTemplate structTemplate = null;
			String name = type.getName();
			do {
				structTemplate = findTemplateForStructure(name, templates);
				if (structTemplate == null) {
					break;
				}
				List<Node> structureNodes = ClassTemplateFactory.createStructure(transformer, importer, templates,
						createdNodes, temporary, newNodeIdPrefix, structTemplate.getSupertype(), structTemplate,
						nodeObject, mapping);
				nodes.addAll(structureNodes);
				name = structTemplate.getSupertype().getName();
				if(name== null) {
					//TODO:
					Classifier supertypeClassifier = structTemplate.getSupertype();
					String fragment = ((MinimalEObjectImpl)supertypeClassifier).eProxyURI().fragment();
					Classifier class2obj = null;
					for (Map<String, Classifier> value : importer.getTypeClassIdMapping().values()) {					
						class2obj = value.get(fragment);
						if(class2obj != null) {
							break;
						}
					}
					if(class2obj != null) {
						name = class2obj.getName();
					}
				}
			} while (structTemplate != null);
			// OPC UA structure
			Node hierachyNode = importer.getNodeByBrowsename(name);
			do {
				if (hierachyNode == null) {
					break;
				}
				// copy structure to parent node
				List<Node> opcUaNodes = ClassTemplateFactory.copyOpcUaStructure(transformer, importer, classifier,
						newNodeIdPrefix, nodeObject.getNodeId(), hierachyNode, mapping);
				nodes.addAll(opcUaNodes);

				// find supertype
				ReferenceNode[] references = hierachyNode.getReferences();
				hierachyNode = null;
				for (ReferenceNode reference : references) {
					if (!Identifiers.HasSubtype.equals(reference.getReferenceTypeId())) {
						continue;
					}
					if (!reference.getIsInverse()) {
						continue;
					}
					ExpandedNodeId targetId = reference.getTargetId();
					hierachyNode = importer.getNodesItemById(targetId);
					break;
				}

			} while (hierachyNode != null);
			// copy OPC UA refernces
			ClassTemplateFactory.copyOpcUaReferences(importer, nodes, createdNodes, temporary, mapping);
		}

		return nodes;
	}

	List<Node> createOperations(AbstractStateMachineToOpcTransformer transformer, StatemachineNodesetImporter importer,
			Classifier classifier, String nodeIdPrefix, Node parent, List<Node> createdNodes, List<Node> temporary,
			List<Operation> operations) {
		List<Node> nodes = new ArrayList<>();

		for (Operation operation : operations) {
			List<Node> methodNodes = ClassTemplateFactory.createMethod(transformer, importer, classifier, nodeIdPrefix,
					operation, parent.getNodeId());
			nodes.addAll(methodNodes);
		}

		return nodes;
	}

	List<Node> createProperty(AbstractStateMachineToOpcTransformer transformer, StatemachineNodesetImporter importer,
			Classifier classifier, String nodeIdPrefix, Node parent, List<Node> createdNodes, List<Node> temporary,
			List<Property> variables) {

		List<Node> nodes = new ArrayList<>();
		for (Property variable : variables) {
			VariableNode nodeVariable = ClassTemplateFactory.createVariable(transformer, importer, classifier,
					nodeIdPrefix, parent, variable);
			if (nodeVariable != null) {
				nodes.add(nodeVariable);
			}
		}

		return nodes;
	}

	private ClassTemplate findTemplateForStructure(String name, ClassTemplate[] templates) {
		for (ClassTemplate template : templates) {
			String structName = template.getClassifier().getName();

			if (structName.equals(name)) {
				return template;
			}
		}
		return null;
	}

	@Override
	protected Classifier getSupertype() {
		return this.supertype;
	}

	@Override
	Node createType(AbstractStateMachineToOpcTransformer transformer, StatemachineNodesetImporter importer,
			Classifier classifier, NodeId parentId) {

		return ClassTemplateFactory.createObjectType(transformer, importer, classifier, parentId);
	}
}
