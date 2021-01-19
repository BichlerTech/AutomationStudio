package com.bichler.opcua.statemachine.transform.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.DataType;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.ParameterDirectionKind;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.StateMachine;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.internal.impl.ParameterImpl;
import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.ExtensionObject;
import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.QualifiedName;
import org.opcfoundation.ua.builtintypes.UnsignedByte;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.common.NamespaceTable;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.AccessLevel;
import org.opcfoundation.ua.core.Argument;
import org.opcfoundation.ua.core.DataTypeAttributes;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.MethodAttributes;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.core.ObjectAttributes;
import org.opcfoundation.ua.core.ObjectTypeAttributes;
import org.opcfoundation.ua.core.ReferenceNode;
import org.opcfoundation.ua.core.VariableAttributes;
import org.opcfoundation.ua.encoding.EncoderContext;
import org.opcfoundation.ua.encoding.EncodingException;

import com.bichler.opcua.statemachine.addressspace.importer.StatemachineNodesetImporter;
import com.bichler.opcua.statemachine.transform.AbstractStateMachineToOpcTransformer;

import opc.sdk.core.enums.ValueRanks;
import opc.sdk.core.node.DataTypeNode;
import opc.sdk.core.node.DefaultNodeFactory;
import opc.sdk.core.node.MethodNode;
import opc.sdk.core.node.Node;
import opc.sdk.core.node.ObjectNode;
import opc.sdk.core.node.ObjectTypeNode;
import opc.sdk.core.node.VariableNode;

public class ClassTemplateFactory {

	public static String concatNodeIdPrefix(String nodeIdPrefix, Node nodeToAppend) {
		return concatNodeIdPrefix(nodeIdPrefix, nodeToAppend.getBrowseName().getName());
	}

	public static String concatNodeIdPrefix(String nodeIdPrefix, String append) {
		String appender = (nodeIdPrefix.isEmpty()) ? "" : ".";
		// TODO: Name type conventions about OpcUa nodes
		String newId = nodeIdPrefix + appender + append;
		return newId.replaceAll("<", "").replaceAll(">", "");
	}

	public static void copyOpcUaReferences(StatemachineNodesetImporter importer, List<Node> nodes,
			List<Node> createdNodes, List<Node> temporary, Map<NodeId, NodeId> mapping) {
		// check mapping
		if (mapping.isEmpty()) {
			return;
		}

		List<Node> all = new ArrayList<>();
		all.addAll(nodes);
		all.addAll(createdNodes);
		all.addAll(temporary);

		for (Entry<NodeId, NodeId> entry : mapping.entrySet()) {
			NodeId key = entry.getKey();
			NodeId value = entry.getValue();

			Node nodeKey = find(importer, key, all);
			Node nodeValue = find(importer, value, all);

			List<ReferenceNode> references = new ArrayList<>();
			for (ReferenceNode reference : nodeValue.getReferences()) {
				references.add(reference);
			}

			for (ReferenceNode reference : nodeKey.getReferences()) {
				// skip hierachical references
				if (importer.isHierachicalReference(reference.getReferenceTypeId())) {
					continue;
				}
				// skip typedefinition reference
				if (Identifiers.HasTypeDefinition.equals(reference.getReferenceTypeId())) {
					continue;
				}

				ExpandedNodeId targetId = reference.getTargetId();
				try {
					NodeId nodeId = importer.getNamespaceTable().toNodeId(targetId);
					// change target id mapping
					if (mapping.containsKey(nodeId)) {
						NodeId mappedId = mapping.get(nodeId);
						ReferenceNode mappedReference = new ReferenceNode(reference.getReferenceTypeId(),
								reference.getIsInverse(), importer.getNamespaceTable().toExpandedNodeId(mappedId));
						references.add(mappedReference);
					}
					// use current target id
					else {
						references.add(reference);
					}
				} catch (ServiceResultException e) {
					e.printStackTrace();
				}

			}

			nodeValue.setReferences(references.toArray(new ReferenceNode[0]));
		}
	}

	public static List<Node> copyOpcUaStructure(AbstractStateMachineToOpcTransformer transformer,
			StatemachineNodesetImporter importer, Classifier classifier, String nodeIdPrefix, NodeId parentId,
			Node nodeToCopy, Map<NodeId, NodeId> mapping) {
		List<Node> nodes = new ArrayList<Node>();

		ReferenceNode[] references = nodeToCopy.getReferences();
		for (ReferenceNode reference : references) {
			// only hierachical references
			if (!importer.isHierachicalReference(reference.getReferenceTypeId())) {
				continue;
			}
			// only inverse references
			if (reference.getIsInverse()) {
				continue;
			}
			// find target to copy
			ExpandedNodeId targetId = reference.getTargetId();

			Node node = importer.getNodesItemById(targetId);
			Node copy = copyNode(transformer, importer, importer.getNamespaceTable(), classifier, nodeIdPrefix,
					parentId, reference.getReferenceTypeId(), node, mapping);
			if (copy == null) {
				// ObjectType, VariableType, DataType, ReferenceType
				continue;
			}

			nodes.add(copy);

			List<Node> structure = copyOpcUaStructure(transformer, importer, classifier,
					nodeIdPrefix + "." + copy.getBrowseName().getName(), copy.getNodeId(), node, mapping);
			nodes.addAll(structure);
		}

		return nodes;
	}

	public static List<Node> createMethod(AbstractStateMachineToOpcTransformer transformer,
			StatemachineNodesetImporter importer, Classifier classifier, String nodeIdPrefix, Operation operation,
			NodeId parentNodeId) {

		List<Node> nodes = new ArrayList<>();
		// method with input/output arguments

		if (operation.getName() != null) {
			DefaultNodeFactory nodeFactory = new DefaultNodeFactory();
			String operationName = operation.getName();

			QualifiedName browsename = new QualifiedName(operationName);
			MethodAttributes encodeable = new MethodAttributes();
			encodeable.setDescription(LocalizedText.NULL);// new LocalizedText("", Locale.ENGLISH));
			encodeable.setDisplayName(new LocalizedText(operationName, Locale.ENGLISH));
			encodeable.setExecutable(false);
			encodeable.setUserExecutable(false);
			encodeable.setUserWriteMask(UnsignedInteger.ZERO);
			encodeable.setWriteMask(UnsignedInteger.ZERO);

			ExtensionObject attributes = null;
			try {
				attributes = ExtensionObject.binaryEncode(encodeable, EncoderContext.getDefaultInstance());

				NodeClass nodeClass = NodeClass.Method;
				ExpandedNodeId parentId = importer.getNamespaceTable().toExpandedNodeId(parentNodeId);
				NodeId referenceType = Identifiers.HasComponent;
				ExpandedNodeId requestedNodeId = importer.getNamespaceTable()
						.toExpandedNodeId(transformer.getNewNodeIdWithPrefix(importer.getNamespaceTable(), classifier,
								nodeIdPrefix, operation.getName()));
				ExpandedNodeId typeDefinition = null;

				Node newNode = nodeFactory.createNode(importer.getNamespaceTable(), browsename, attributes, nodeClass,
						parentId, referenceType, requestedNodeId, typeDefinition);
				nodes.add(newNode);

				String concatId = ClassTemplateFactory.concatNodeIdPrefix(nodeIdPrefix, operation.getName());

				List<Node> args = createMethodArgumentNode(transformer, importer, browsename, requestedNodeId,
						classifier, operation, concatId);
				nodes.addAll(args);
			} catch (ServiceResultException e) {
				Logger.getLogger(ClassTemplateFactory.class.getName()).log(Level.SEVERE,
						"Cannot encode method node from operation " + browsename.getName());
			}
		}
		return nodes;
	}

	public static DataTypeNode createDataType(AbstractStateMachineToOpcTransformer transformer,
			StatemachineNodesetImporter importer, Classifier classifier, NodeId parentType) {
		DefaultNodeFactory nodeFactory = new DefaultNodeFactory();
		try {
			QualifiedName browseName = new QualifiedName(classifier.getName());
			DataTypeAttributes dta = new DataTypeAttributes();
			dta.setDescription(new LocalizedText("", Locale.ENGLISH));
			dta.setDisplayName(new LocalizedText(classifier.getName(), Locale.ENGLISH));
			dta.setIsAbstract(false);
			dta.setUserWriteMask(UnsignedInteger.ZERO);
			dta.setWriteMask(UnsignedInteger.ZERO);
			ExtensionObject attributes = ExtensionObject.binaryEncode(dta, EncoderContext.getDefaultInstance());
			NodeClass nodeClass = NodeClass.DataType;
			ExpandedNodeId parentId = importer.getNamespaceTable().toExpandedNodeId(parentType);
			NodeId referenceType = Identifiers.HasSubtype;

			ExpandedNodeId requestedNewId = importer.getNamespaceTable()
					.toExpandedNodeId(transformer.getNewNodeIdForObjectType(importer.getNamespaceTable(), classifier));

			return (DataTypeNode) nodeFactory.createNode(importer.getNamespaceTable(), browseName, attributes,
					nodeClass, parentId, referenceType, requestedNewId, null);
		} catch (ServiceResultException e) {
			Logger.getLogger(ClassTemplateFactory.class.getName()).log(Level.SEVERE,
					"Cannot create OPC UA objecttype " + classifier.getName());
		}
		return null;
	}

	public static ObjectTypeNode createObjectType(AbstractStateMachineToOpcTransformer transformer,
			StatemachineNodesetImporter importer, Classifier classifier, NodeId parentType) {
		DefaultNodeFactory nodeFactory = new DefaultNodeFactory();
		try {
			QualifiedName browseName = new QualifiedName(classifier.getName());
			ObjectTypeAttributes ota = new ObjectTypeAttributes();
			ota.setDescription(new LocalizedText("", Locale.ENGLISH));
			ota.setDisplayName(new LocalizedText(classifier.getName(), Locale.ENGLISH));
			ota.setIsAbstract(false);
			ota.setUserWriteMask(UnsignedInteger.ZERO);
			ota.setWriteMask(UnsignedInteger.ZERO);
			ExtensionObject attributes = ExtensionObject.binaryEncode(ota, EncoderContext.getDefaultInstance());
			NodeClass nodeClass = NodeClass.ObjectType;
			ExpandedNodeId parentId = importer.getNamespaceTable().toExpandedNodeId(parentType);
			NodeId referenceType = Identifiers.HasSubtype;

			ExpandedNodeId requestedNewId = importer.getNamespaceTable()
					.toExpandedNodeId(transformer.getNewNodeIdForObjectType(importer.getNamespaceTable(), classifier));

			return (ObjectTypeNode) nodeFactory.createNode(importer.getNamespaceTable(), browseName, attributes,
					nodeClass, parentId, referenceType, requestedNewId, null);
		} catch (ServiceResultException e) {
			Logger.getLogger(ClassTemplateFactory.class.getName()).log(Level.SEVERE,
					"Cannot create OPC UA objecttype " + classifier.getName());
		}
		return null;
	}

	public static ObjectNode createObjectFolder(AbstractStateMachineToOpcTransformer transformer,
			StatemachineNodesetImporter importer, Classifier typedClassifier, Node parent,
			Property aggregationComposite, String nodeIdPrefix) {

		String name = aggregationComposite.getType().getName();

		DefaultNodeFactory nodeFactory = new DefaultNodeFactory();
		try {
			QualifiedName browseName = new QualifiedName(name);
			ObjectAttributes oa = new ObjectAttributes();
			oa.setDescription(new LocalizedText("", Locale.ENGLISH));
			oa.setDisplayName(new LocalizedText(name, Locale.ENGLISH));
			oa.setEventNotifier(UnsignedByte.ZERO);
			oa.setUserWriteMask(UnsignedInteger.ZERO);
			oa.setWriteMask(UnsignedInteger.ZERO);

			ExtensionObject attributes = ExtensionObject.binaryEncode(oa, EncoderContext.getDefaultInstance());
			NodeClass nodeClass = NodeClass.Object;
			ExpandedNodeId parentId = importer.getNamespaceTable().toExpandedNodeId(parent.getNodeId());
			NodeId referenceType = Identifiers.HasComponent;

			ExpandedNodeId requestedNewId = importer.getNamespaceTable().toExpandedNodeId(transformer
					.getNewNodeIdWithPrefix(importer.getNamespaceTable(), typedClassifier, nodeIdPrefix, name));

			ObjectNode objNode = (ObjectNode) nodeFactory.createNode(importer.getNamespaceTable(), browseName,
					attributes, nodeClass, parentId, referenceType, requestedNewId,
					importer.getNamespaceTable().toExpandedNodeId(Identifiers.FolderType));

			addModellingRule(importer, objNode, aggregationComposite);

			return objNode;
		} catch (ServiceResultException e) {
			Logger.getLogger(ClassTemplateFactory.class.getName()).log(Level.SEVERE,
					"Cannot create OPC UA foldertype " + typedClassifier.getName());
		}
		return null;
	}

	public static ObjectNode createObject(AbstractStateMachineToOpcTransformer transformer,
			StatemachineNodesetImporter importer, Classifier typedClassifier, Node parent, Property aggregationShared,
			Node objTypeNode, String nodeIdPrefix) {

		String name = toSharedName(aggregationShared);

		DefaultNodeFactory nodeFactory = new DefaultNodeFactory();
		try {
			QualifiedName browseName = new QualifiedName(name);
			ObjectAttributes oa = new ObjectAttributes();
			oa.setDescription(new LocalizedText("", Locale.ENGLISH));
			oa.setDisplayName(new LocalizedText(name, Locale.ENGLISH));
			oa.setEventNotifier(UnsignedByte.ZERO);
			oa.setUserWriteMask(UnsignedInteger.ZERO);
			oa.setWriteMask(UnsignedInteger.ZERO);

			ExtensionObject attributes = ExtensionObject.binaryEncode(oa, EncoderContext.getDefaultInstance());
			NodeClass nodeClass = NodeClass.Object;
			ExpandedNodeId parentId = importer.getNamespaceTable().toExpandedNodeId(parent.getNodeId());
			NodeId referenceType = Identifiers.HasComponent;

			String associationName = aggregationShared.getAssociation().getName();
			if (associationName != null) {
				Node refNode = importer.getNodeByBrowsename(associationName);
				if (refNode != null) {
					referenceType = refNode.getNodeId();
				} else {
					Logger.getLogger(ClassTemplateFactory.class.getName()).log(Level.SEVERE,
							"Cannot find an OPC UA reference type " + associationName + " for OPC UA object " + name
									+ " ");
				}
			}

			ExpandedNodeId requestedNewId = importer.getNamespaceTable().toExpandedNodeId(transformer
					.getNewNodeIdWithPrefix(importer.getNamespaceTable(), typedClassifier, nodeIdPrefix, name));

			ObjectNode objNode = (ObjectNode) nodeFactory.createNode(importer.getNamespaceTable(), browseName,
					attributes, nodeClass, parentId, referenceType, requestedNewId,
					importer.getNamespaceTable().toExpandedNodeId(objTypeNode.getNodeId()));

			addModellingRule(importer, objNode, aggregationShared);

			return objNode;
		} catch (ServiceResultException e) {
			Logger.getLogger(ClassTemplateFactory.class.getName()).log(Level.SEVERE,
					"Cannot create OPC UA object " + typedClassifier.getName());
		}

		return null;
	}

	public static List<Node> createStructure(AbstractStateMachineToOpcTransformer transformer,
			StatemachineNodesetImporter importer, ClassTemplate[] templates, List<Node> createdNodes,
			List<Node> temporary, String nodeIdPrefix, Classifier classifier, ClassTemplate structTemplate,
			ObjectNode nodeObject, Map<NodeId, NodeId> mapping) {

		List<Node> nodes = structTemplate.toOpcUaNodes(transformer, importer, templates, createdNodes, temporary, false,
				nodeIdPrefix, structTemplate.getClassifier(), nodeObject, mapping);

		return nodes;
	}

	public static ClassTemplate createTemplate(StatemachineNodesetImporter importer, Classifier classifier,
			NodeId referencedId) {
		UaClassType type = UaClassType.ObjectType;

		ClassTemplate template = null;

		EList<EObject> stereotypes = classifier.getStereotypeApplications();
		// stereotype
		if (!stereotypes.isEmpty()) {
			EClass stereotypeClass = stereotypes.get(0).eClass();
			String name = stereotypeClass.getName();
			Stereotypes stereotype = Stereotypes.valueOf(name);

			switch (stereotype) {
			case Instance:
				type = UaClassType.Object;
				break;
			default:
				break;
			}
		}

		if (classifier instanceof DataType) {
			type = UaClassType.DataType;
		}

		switch (type) {
		case Object:
			template = new ObjectClassTemplate(classifier, referencedId);
			break;
		case ObjectType:
			if (classifier instanceof StateMachine) {
				template = new StatemachineTypeClassTemplate(classifier, referencedId);
			} else {
				template = new ObjectTypeClassTemplate(classifier, referencedId);
			}
			break;
		case DataType:
			template = new DataTypeClassTemplate(classifier, referencedId);
//			Classifier supertype = template.getSupertype();
//			if(supertype == null) {
//				Map<String, DataType> defaultTypeSet = importer.getTypeClassIdMapping().get("OpcFoundationUA");
//				DataType structure = defaultTypeSet.get("_jR2mCyUN2n85b2jdyPNR1k");
//				template.setSupertype(structure);
//			}
			break;
		}

		return template;
	}

	public static VariableNode createVariable(AbstractStateMachineToOpcTransformer transformer,
			StatemachineNodesetImporter importer, Classifier typedClassifier, String nodeIdPrefix, Node parent,
			Property variable) {

		String name = variable.getName();
		NodeId datatypeId = toDatatype(importer, variable);
		NodeId variableTypeId = Identifiers.BaseDataVariableType;

		// variable stereotype -> OPC UA variabletype
		EList<EObject> stereoTypes = variable.getStereotypeApplications();
		if (!stereoTypes.isEmpty()) {
			EClass stereotypeClass = stereoTypes.get(0).eClass();
			String stereotypeName = stereotypeClass.getName();
			Stereotypes stereotype = Stereotypes.valueOf(stereotypeName);
			switch (stereotype) {
			case Property:
				variableTypeId = Identifiers.PropertyType;
				break;
			case Variable:
				variableTypeId = Identifiers.BaseDataVariableType;
				break;
			default:
				variableTypeId = Identifiers.BaseDataVariableType;
				break;
			}
		}

		DefaultNodeFactory nodeFactory = new DefaultNodeFactory();
		try {
			QualifiedName browseName = new QualifiedName(name);
			VariableAttributes va = new VariableAttributes();
			va.setDescription(new LocalizedText("", Locale.ENGLISH));
			va.setDisplayName(new LocalizedText(name, Locale.ENGLISH));
			va.setAccessLevel(AccessLevel.getMask(AccessLevel.ALL));
			va.setArrayDimensions(new UnsignedInteger[] {});
			va.setDataType(datatypeId);
			va.setHistorizing(false);
			va.setMinimumSamplingInterval(1000.0);
			va.setUserAccessLevel(AccessLevel.getMask(AccessLevel.ALL));
			va.setUserWriteMask(UnsignedInteger.ZERO);
			va.setValue(Variant.NULL);
			va.setValueRank(ValueRanks.Scalar.getValue());
			va.setWriteMask(UnsignedInteger.ZERO);

			ExtensionObject attributes = ExtensionObject.binaryEncode(va, EncoderContext.getDefaultInstance());
			NodeClass nodeClass = NodeClass.Variable;
			ExpandedNodeId parentId = importer.getNamespaceTable().toExpandedNodeId(parent.getNodeId());
			NodeId referenceType = Identifiers.HasComponent;

			ExpandedNodeId requestedNewId = importer.getNamespaceTable().toExpandedNodeId(transformer
					.getNewNodeIdWithPrefix(importer.getNamespaceTable(), typedClassifier, nodeIdPrefix, name));

			VariableNode varNode = (VariableNode) nodeFactory.createNode(importer.getNamespaceTable(), browseName,
					attributes, nodeClass, parentId, referenceType, requestedNewId,
					importer.getNamespaceTable().toExpandedNodeId(variableTypeId));

			addModellingRule(importer, varNode, variable);

			return varNode;
		} catch (ServiceResultException e) {
			Logger.getLogger(ClassTemplateFactory.class.getName()).log(Level.SEVERE,
					"Cannot create OPC UA variable " + typedClassifier.getName());
		}

		return null;
	}

	public static String withoutTypeName(String typeName) {
		String name = typeName;

		if (typeName.contains("Type")) {
			name = typeName.substring(0, typeName.length() - "Type".length());
		}
		return name;
	}

	protected static Node copyNode(AbstractStateMachineToOpcTransformer transformer,
			StatemachineNodesetImporter importer, NamespaceTable nsTable, Classifier classifier, String nodeIdPrefix,
			NodeId parentId, NodeId referenceTypeId, Node node, Map<NodeId, NodeId> mapping) {
		NodeClass nodeClass = node.getNodeClass();
		Node copy = null;
		switch (nodeClass) {
		case Object:
			copy = copyObject(transformer, importer, nsTable, classifier, nodeIdPrefix, parentId, referenceTypeId,
					(ObjectNode) node);
			break;
		case Variable:
			copy = copyVariable(transformer, importer, nsTable, classifier, nodeIdPrefix, parentId, referenceTypeId,
					(VariableNode) node);
			break;
		case Method:
			copy = copyMethod(transformer, importer, nsTable, classifier, nodeIdPrefix, parentId, referenceTypeId,
					(MethodNode) node);
			break;
		default:
			return null;
		}

		mapping.put(node.getNodeId(), copy.getNodeId());
		return copy;
	}

	private static void addModellingRule(StatemachineNodesetImporter importer, Node node, Property aggregation) {
		ExpandedNodeId modellingRule = ExpandedNodeId.NULL;

		int multiplicityLower = aggregation.getLower();
		int multiplicityUpper = aggregation.getUpper();

		switch (multiplicityLower) {
		// 0..
		case 0:
			switch (multiplicityUpper) {
			// *
			case -1:
				modellingRule = importer.getNamespaceTable()
						.toExpandedNodeId(Identifiers.ModellingRule_OptionalPlaceholder);
				break;
			// 1
			case 1:
				modellingRule = importer.getNamespaceTable().toExpandedNodeId(Identifiers.ModellingRule_Optional);
				break;
			}
			break;
		// 1..
		case 1:
			switch (multiplicityUpper) {
			// *
			case -1:
				modellingRule = importer.getNamespaceTable()
						.toExpandedNodeId(Identifiers.ModellingRule_MandatoryPlaceholder);
				break;
			// 1
			case 1:
				modellingRule = importer.getNamespaceTable().toExpandedNodeId(Identifiers.ModellingRule_Mandatory);
				break;
			}
			break;
		}
		// set modelling rule reference
		if (!ExpandedNodeId.isNull(modellingRule)) {
			ReferenceNode reference = new ReferenceNode();
			reference.setIsInverse(false);
			reference.setReferenceTypeId(Identifiers.HasModellingRule);
			reference.setTargetId(modellingRule);
			// add reference to node
			List<ReferenceNode> refList = new ArrayList<>();
			ReferenceNode[] references = node.getReferences();
			// add references to list
			for (ReferenceNode ref : references) {
				refList.add(ref);
			}
			// add new reference
			refList.add(reference);
			// set references to node
			node.setReferences(refList.toArray(new ReferenceNode[0]));
		}
	}

	private static Node copyMethod(AbstractStateMachineToOpcTransformer transformer,
			StatemachineNodesetImporter importer, NamespaceTable nsTable, Classifier classifier, String nodeIdPrefix,
			NodeId parentId, NodeId referenceTypeId, MethodNode copy) {

		DefaultNodeFactory nodeFactory = new DefaultNodeFactory();
		try {
			MethodAttributes ma = new MethodAttributes();
			ma.setDescription(copy.getDescription());
			ma.setDisplayName(copy.getDisplayName());
			ma.setUserWriteMask(copy.getUserWriteMask());
			ma.setWriteMask(copy.getWriteMask());
			ma.setExecutable(copy.getExecutable());
			ma.setUserExecutable(copy.getUserExecutable());

			ExtensionObject attributes = ExtensionObject.binaryEncode(ma, EncoderContext.getDefaultInstance());

			ExpandedNodeId requestedNewId = nsTable.toExpandedNodeId(transformer.getNewNodeIdWithPrefix(nsTable,
					classifier, nodeIdPrefix, copy.getBrowseName().getName()));

			Node node = nodeFactory.createNode(nsTable, copy.getBrowseName(), attributes, copy.getNodeClass(),
					nsTable.toExpandedNodeId(parentId), referenceTypeId, requestedNewId, null);

			return node;
		} catch (ServiceResultException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static Node copyObject(AbstractStateMachineToOpcTransformer transformer,
			StatemachineNodesetImporter importer, NamespaceTable nsTable, Classifier classifier, String nodeIdPrefix,
			NodeId parentId, NodeId referenceTypeId, ObjectNode copy) {

		DefaultNodeFactory nodeFactory = new DefaultNodeFactory();
		try {
			ExpandedNodeId typeDef = copy.findTarget(Identifiers.HasTypeDefinition, false);
			ObjectAttributes oa = new ObjectAttributes();
			oa.setDescription(copy.getDescription());
			oa.setDisplayName(copy.getDisplayName());
			oa.setUserWriteMask(copy.getUserWriteMask());
			oa.setWriteMask(copy.getWriteMask());
			oa.setEventNotifier(copy.getEventNotifier());

			ExtensionObject attributes = ExtensionObject.binaryEncode(oa, EncoderContext.getDefaultInstance());

			ExpandedNodeId requestedNewId = nsTable.toExpandedNodeId(transformer.getNewNodeIdWithPrefix(nsTable,
					classifier, nodeIdPrefix, copy.getBrowseName().getName()));

			Node node = nodeFactory.createNode(nsTable, copy.getBrowseName(), attributes, copy.getNodeClass(),
					nsTable.toExpandedNodeId(parentId), referenceTypeId, requestedNewId, typeDef);

			return node;
		} catch (ServiceResultException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static Node copyVariable(AbstractStateMachineToOpcTransformer transformer,
			StatemachineNodesetImporter importer, NamespaceTable nsTable, Classifier classifier, String nodeIdPrefix,
			NodeId parentId, NodeId referenceTypeId, VariableNode copy) {
		DefaultNodeFactory nodeFactory = new DefaultNodeFactory();
		try {
			ExpandedNodeId typeDef = copy.findTarget(Identifiers.HasTypeDefinition, false);
			VariableAttributes va = new VariableAttributes();
			va.setDescription(copy.getDescription());
			va.setDisplayName(copy.getDisplayName());
			va.setUserWriteMask(copy.getUserWriteMask());
			va.setWriteMask(copy.getWriteMask());

			va.setAccessLevel(copy.getAccessLevel());
			va.setArrayDimensions(copy.getArrayDimensions());
			va.setDataType(copy.getDataType());
			va.setHistorizing(copy.getHistorizing());
			va.setMinimumSamplingInterval(copy.getMinimumSamplingInterval());
			va.setValue(copy.getValue());
			va.setValueRank(copy.getValueRank());

			ExtensionObject attributes = ExtensionObject.binaryEncode(va, EncoderContext.getDefaultInstance());

			ExpandedNodeId requestedNewId = nsTable.toExpandedNodeId(transformer.getNewNodeIdWithPrefix(nsTable,
					classifier, nodeIdPrefix, copy.getBrowseName().getName()));

			Node node = nodeFactory.createNode(nsTable, copy.getBrowseName(), attributes, copy.getNodeClass(),
					nsTable.toExpandedNodeId(parentId), referenceTypeId, requestedNewId, typeDef);

			return node;
		} catch (ServiceResultException e) {
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * private static void copyReferences(StatemachineNodesetImporter importer, Node
	 * origin, Node target) { // refernces to copy ReferenceNode[] references =
	 * origin.getReferences();
	 * 
	 * // target references ReferenceNode[] targetReferences =
	 * target.getReferences(); List<ReferenceNode> newReferences = new
	 * ArrayList<>(); // add exisiting references for (ReferenceNode reference :
	 * targetReferences) { newReferences.add(reference); }
	 * 
	 * // copy references for (ReferenceNode reference : references) { if
	 * (importer.isHierachicalReference(reference.getReferenceTypeId())) { continue;
	 * }
	 * 
	 * if (Identifiers.HasTypeDefinition.equals(reference.getReferenceTypeId())) {
	 * continue; }
	 * 
	 * newReferences.add(reference); } }
	 */

	private static List<Node> createMethodArgumentNode(AbstractStateMachineToOpcTransformer transformer,
			StatemachineNodesetImporter importer, QualifiedName methodName, ExpandedNodeId parentId,
			Classifier classifier, Operation operation, String nodeIdPrefix) {

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
				Logger.getLogger(ClassTemplateFactory.class.getName()).log(Level.INFO,
						"A CallEvent " + methodName.getName() + " parameter has no name");
				continue;
			}

			if (parameter.getType() == null) {
				Logger.getLogger(ClassTemplateFactory.class.getName()).log(Level.INFO,
						"A CallEvent " + methodName.getName() + " parameter " + name + " has no type defined");
				continue;
			}
			
			String parameterTypeName = parameter.getType().getName();
			// find parameter name from imported UML ids
			if(parameterTypeName == null) {
				Type type = parameter.getType();
				String fragment = ((MinimalEObjectImpl)type).eProxyURI().fragment();
				Classifier class2obj = null;
				for (Map<String, Classifier> value : importer.getTypeClassIdMapping().values()) {					
					class2obj = value.get(fragment);
					if(class2obj != null) {
						break;
					}
				}
				
				if(class2obj != null) {
					parameterTypeName = class2obj.getName();
				}
			}
			
			Node datatypeNode = importer.getNodeByBrowsename(parameterTypeName);
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
					Logger.getLogger(ClassTemplateFactory.class.getName()).log(Level.INFO,
							"A CallEvent " + methodName.getName() + " parameter " + name
									+ " uses an unsupported direction " + direction.getName());
					break;
				}
			} catch (EncodingException e) {
				Logger.getLogger(ClassTemplateFactory.class.getName()).log(Level.SEVERE,
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
				// TODO:
				ExpandedNodeId requestedNodeId = importer.getNamespaceTable()
						.toExpandedNodeId(transformer.getNewNodeIdWithPrefix(importer.getNamespaceTable(), classifier,
								nodeIdPrefix, StatemachineNodesetImporter.INPUT_ARGUMENTS));

				DefaultNodeFactory nodeFactory = new DefaultNodeFactory();
				Node newNode = nodeFactory.createNode(importer.getNamespaceTable(), browsename, attributes, nodeClass,
						parentId, referenceType, requestedNodeId, typeDefinition);
//				((VariableNode) newNode).setValue(new Variant(inputArguments.toArray(new ExtensionObject[0])));
				args.add(newNode);
			} catch (ServiceResultException e) {
				Logger.getLogger(ClassTemplateFactory.class.getName()).log(Level.SEVERE,
						"Cannot create inputargument for operation " + operation.getName());
			}
		}

		if (!outputArguments.isEmpty()) {
			QualifiedName browsename = new QualifiedName(StatemachineNodesetImporter.OUTPUT_ARGUMENTS);
			encodeable.setDisplayName(new LocalizedText(StatemachineNodesetImporter.OUTPUT_ARGUMENTS, Locale.ENGLISH));
			encodeable.setValue(new Variant(outputArguments.toArray(new ExtensionObject[0])));
			try {
				attributes = ExtensionObject.binaryEncode(encodeable, EncoderContext.getDefaultInstance());
				// TODO:
				ExpandedNodeId requestedNodeId = importer.getNamespaceTable()
						.toExpandedNodeId(transformer.getNewNodeIdWithPrefix(importer.getNamespaceTable(), classifier,
								nodeIdPrefix, StatemachineNodesetImporter.OUTPUT_ARGUMENTS));

				DefaultNodeFactory nodeFactory = new DefaultNodeFactory();
				Node newNode = nodeFactory.createNode(importer.getNamespaceTable(), browsename, attributes, nodeClass,
						parentId, referenceType, requestedNodeId, typeDefinition);
//				((VariableNode) newNode).setValue(new Variant(outputArguments.toArray(new ExtensionObject[0])));
				args.add(newNode);
			} catch (ServiceResultException e) {
				Logger.getLogger(ClassTemplateFactory.class.getName()).log(Level.SEVERE,
						"Cannot create inputargument for operation " + operation.getName());
			}
		}

		return args;
	}

	private static NodeId toDatatype(StatemachineNodesetImporter importer, Property variable) {
		Type type = variable.getType();

		if (type == null) {
			Logger.getLogger(ClassTemplateFactory.class.getName()).log(Level.INFO, "Property variable "
					+ variable.getName() + " has no datatype defined. Datatype to be used is OPC UA BaseDataType.");
			return Identifiers.BaseDataType;
		}
		String typeName = type.getName();
		if (typeName == null) {
			String id = ((MinimalEObjectImpl) type).eProxyURI().fragment();
			for (Map<String, Classifier> value : importer.getTypeClassIdMapping().values()) {
				Classifier typedef = value.get(id);
				if (typedef != null) {
					typeName = typedef.getName();
					break;
				}
			}

		}

		Node datatype = importer.getNodeByBrowsename(typeName);
		if (datatype == null) {
			Logger.getLogger(ClassTemplateFactory.class.getName()).log(Level.INFO, "Property variable "
					+ variable.getName() + " has no datatype defined. Datatype to be used is OPC UA BaseDataType.");
			return Identifiers.BaseDataType;
		}

		if (!(datatype instanceof DataTypeNode)) {
			Logger.getLogger(ClassTemplateFactory.class.getName()).log(Level.INFO,
					"Property variable " + variable.getName() + " has no valid OPC UA datatype");
			return Identifiers.BaseDataType;
		}

		return datatype.getNodeId();
	}

	private static String toSharedName(Property aggregationShared) {
		int multiplicity = aggregationShared.getUpper();
		String typeName = aggregationShared.getType().getName();

		String name = withoutTypeName(typeName);

		switch (multiplicity) {
		// *
		case -1:
			name = "<" + name + ">";
			break;
		case 0:
		case 1:
			break;

		}

		return name;
	}

	private static Node find(StatemachineNodesetImporter importer, NodeId key, List<Node> nodes) {
		for (Node node : nodes) {
			NodeId nodeId = node.getNodeId();
			if (key.equals(nodeId)) {
				return node;
			}
		}

		Node node = importer.getNodesItemById(key);
		return node;
	}
}
