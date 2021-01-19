package com.bichler.astudio.opcua.datadictionary.base.model;

import java.util.ArrayList;
import java.util.List;

import org.opcfoundation.ua.builtintypes.ByteString;
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
import org.opcfoundation.ua.core.AddNodesItem;
import org.opcfoundation.ua.core.AddReferencesItem;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.core.ObjectAttributes;
import org.opcfoundation.ua.core.VariableAttributes;
import org.opcfoundation.ua.encoding.EncoderContext;
import org.w3c.dom.Document;

import opc.sdk.core.enums.ValueRanks;
import opc.sdk.core.node.DataTypeNode;
import opc.sdk.core.node.Node;

public abstract class AbstractStructuredDatatypeNodeGenerator {

	private static final String DEFAULT_BINARY = "Default Binary";

	public AbstractStructuredDatatypeNodeGenerator() {

	}

	public ExpandedNodeId createNodeBinarySchema(int namespaceIndex, String binarySchemaName)
			throws ServiceResultException {

		NodeId nextNodeId = getNextNodeId(namespaceIndex);
//				this.opcServer.getAddressSpaceManager().getNodeFactory().showNextNodeId(namespaceIndex,
//				IdType.Numeric, NodeIdMode.CONTINUE);

		ExpandedNodeId requestedNewNodeId = getNamespaceTable().toExpandedNodeId(nextNodeId); // this.opcServer.getNamespaceUris().toExpandedNodeId(nextNodeId);

		ByteString value = createEmptyDataDictionaryValue(namespaceIndex);

		AddNodesItem item = new AddNodesItem();
		item.setBrowseName(new QualifiedName(binarySchemaName));
		item.setNodeClass(NodeClass.Variable);
		item.setParentNodeId(getNamespaceTable().toExpandedNodeId(Identifiers.OPCBinarySchema_TypeSystem));
		item.setReferenceTypeId(Identifiers.HasComponent);
		item.setTypeDefinition(getNamespaceTable().toExpandedNodeId(Identifiers.DataTypeDictionaryType));
		item.setRequestedNewNodeId(requestedNewNodeId);

		VariableAttributes nodeAttributes = new VariableAttributes();
		nodeAttributes.setAccessLevel(AccessLevel.getMask(AccessLevel.CurrentRead));
		nodeAttributes.setArrayDimensions(new UnsignedInteger[0]);
		nodeAttributes.setDataType(Identifiers.ByteString);
		nodeAttributes.setDescription(LocalizedText.EMPTY);
		nodeAttributes.setDisplayName(new LocalizedText(binarySchemaName));
		nodeAttributes.setHistorizing(false);
		nodeAttributes.setMinimumSamplingInterval(0.0);
		nodeAttributes.setUserAccessLevel(AccessLevel.getMask(AccessLevel.CurrentRead));
		nodeAttributes.setUserWriteMask(UnsignedInteger.ZERO);
		nodeAttributes.setValue(new Variant(value));
		nodeAttributes.setValueRank(ValueRanks.Scalar.getValue());
		nodeAttributes.setWriteMask(UnsignedInteger.ZERO);

		item.setNodeAttributes(ExtensionObject.binaryEncode(nodeAttributes, EncoderContext.getDefaultInstance()));
		List<AddNodesItem> addNodes = new ArrayList<>();
		addNodes.add(item);

		// OpcUtils.addNode(this.opcServer, addNodes.toArray(new AddNodesItem[0]),
		// true);
		addNode(addNodes.toArray(new AddNodesItem[0]), true);
		return requestedNewNodeId;
	}

	public ExpandedNodeId createNodeDefaultBinary(NodeId datatypeId) throws ServiceResultException {
		// create default binary node for HasEncoding DataType Node and HasDescription
		// Variable Node
		NodeId nextNodeId = getNextNodeId(datatypeId.getNamespaceIndex());
//				this.opcServer.getAddressSpaceManager().getNodeFactory()
//				.showNextNodeId(datatypeId.getNamespaceIndex(), IdType.Numeric, NodeIdMode.CONTINUE);

		ExpandedNodeId requestedNewNodeId = getNamespaceTable().toExpandedNodeId(nextNodeId); // this.opcServer.getNamespaceUris().toExpandedNodeId(nextNodeId);

		AddNodesItem item = new AddNodesItem();
		item.setBrowseName(new QualifiedName("Default Binary"));
		item.setNodeClass(NodeClass.Object);
		item.setParentNodeId(null);
		item.setReferenceTypeId(null);
		item.setTypeDefinition(getNamespaceTable().toExpandedNodeId(Identifiers.DataTypeEncodingType));
		item.setRequestedNewNodeId(requestedNewNodeId);

		ObjectAttributes objectAttributes = new ObjectAttributes();
		objectAttributes.setDescription(LocalizedText.EMPTY);
		objectAttributes.setDisplayName(new LocalizedText("Default Binary"));
		objectAttributes.setEventNotifier(UnsignedByte.ZERO);
		objectAttributes.setUserWriteMask(UnsignedInteger.ZERO);
		objectAttributes.setWriteMask(UnsignedInteger.ZERO);
		item.setNodeAttributes(ExtensionObject.binaryEncode(objectAttributes, EncoderContext.getDefaultInstance()));

		List<AddNodesItem> addNodes = new ArrayList<>();
		addNodes.add(item);

//		NamespaceTable nsTable = getNamespaceTable(); // this.opcServer.getNamespaceUris();
//		Node decoded = DataDictionaryUtil.decodeNode(nsTable, item, new DefaultNodeFactory());
		// add node without parent

//		this.opcServer.getAddressSpaceManager().addNodes(nsTable, new Node[] { decoded });
		addNode(addNodes.toArray(new AddNodesItem[0]), false);

		List<AddReferencesItem> referencesToAdd = new ArrayList<>();
		// add references hasEncoding
		AddReferencesItem hasEncoding = new AddReferencesItem();
		hasEncoding.setIsForward(true);
		hasEncoding.setReferenceTypeId(Identifiers.HasEncoding);
		hasEncoding.setSourceNodeId(datatypeId);
		hasEncoding.setTargetNodeClass(NodeClass.Object);
		hasEncoding.setTargetNodeId(requestedNewNodeId);
		referencesToAdd.add(hasEncoding);
		// add references encodingOf
		AddReferencesItem encodingOf = new AddReferencesItem();
		encodingOf.setIsForward(false);
		encodingOf.setReferenceTypeId(Identifiers.HasEncoding);
		encodingOf.setSourceNodeId(getNamespaceTable().toNodeId(requestedNewNodeId));
		encodingOf.setTargetNodeClass(NodeClass.DataType);
		encodingOf.setTargetNodeId(getNamespaceTable().toExpandedNodeId(datatypeId));
		referencesToAdd.add(encodingOf);

		addReferences(referencesToAdd.toArray(new AddReferencesItem[0]));

		return requestedNewNodeId;
	}
	
	public ExpandedNodeId createNodeDataTypeDescription(int namespaceIndex, String datatypeName,
			ExpandedNodeId parentId) throws ServiceResultException {
		NodeId nextNodeId = getNextNodeId(namespaceIndex);
//				this.opcServer.getAddressSpaceManager().getNodeFactory().showNextNodeId(namespaceIndex,
//				IdType.Numeric, NodeIdMode.CONTINUE);

		ExpandedNodeId requestedNewNodeId = getNamespaceTable()
//				this.opcServer.getNamespaceUris()
				.toExpandedNodeId(nextNodeId);

		AddNodesItem item = new AddNodesItem();
		item.setBrowseName(new QualifiedName(datatypeName));
		item.setNodeClass(NodeClass.Variable);
		item.setParentNodeId(parentId);
		item.setReferenceTypeId(Identifiers.HasComponent);
		item.setTypeDefinition(getNamespaceTable()
// this.opcServer.getNamespaceUris()
				.toExpandedNodeId(Identifiers.DataTypeDescriptionType));
		item.setRequestedNewNodeId(requestedNewNodeId);

		VariableAttributes nodeAttributes = new VariableAttributes();
		nodeAttributes.setAccessLevel(AccessLevel.getMask(AccessLevel.CurrentRead));
		nodeAttributes.setArrayDimensions(new UnsignedInteger[0]);
		nodeAttributes.setDataType(Identifiers.String);
		nodeAttributes.setDescription(LocalizedText.EMPTY);
		nodeAttributes.setDisplayName(new LocalizedText(datatypeName));
		nodeAttributes.setHistorizing(false);
		nodeAttributes.setMinimumSamplingInterval(0.0);
		nodeAttributes.setUserAccessLevel(AccessLevel.getMask(AccessLevel.CurrentRead));
		nodeAttributes.setUserWriteMask(UnsignedInteger.ZERO);
		nodeAttributes.setValue(new Variant(datatypeName));
		nodeAttributes.setValueRank(ValueRanks.Scalar.getValue());
		nodeAttributes.setWriteMask(UnsignedInteger.ZERO);

		item.setNodeAttributes(ExtensionObject.binaryEncode(nodeAttributes, EncoderContext.getDefaultInstance()));
		List<AddNodesItem> addNodes = new ArrayList<>();
		addNodes.add(item);

		addNode(addNodes.toArray(new AddNodesItem[0]), true);

		return requestedNewNodeId;
	}

	public void createReferencesHasDescription(ExpandedNodeId defaultBinary, ExpandedNodeId datatypeDescription)
			throws ServiceResultException {

		Node defBinNode = getNode(defaultBinary); // OpcUtils.getNode(this.opcServer, defaultBinary);

		ExpandedNodeId[] hasDescriptionTarget = defBinNode.findTargets(Identifiers.HasDescription, false);
		boolean hasDescriptionReference = hasDescriptionTarget.length > 0 ? true : false;

		if (!hasDescriptionReference) {
			List<AddReferencesItem> referencesToAdd = new ArrayList<>();
			// add hasDescription
			AddReferencesItem hasDescription = new AddReferencesItem();
			hasDescription.setIsForward(true);
			hasDescription.setReferenceTypeId(Identifiers.HasDescription);
			hasDescription.setSourceNodeId(getNamespaceTable().
//					this.opcServer.getNamespaceUris().
					toNodeId(defaultBinary));
			hasDescription.setTargetNodeClass(NodeClass.Variable);
			hasDescription.setTargetNodeId(datatypeDescription);
			referencesToAdd.add(hasDescription);
			// add descriptionOf
			AddReferencesItem descriptionOf = new AddReferencesItem();
			descriptionOf.setIsForward(false);
			descriptionOf.setReferenceTypeId(Identifiers.HasDescription);
			descriptionOf.setSourceNodeId(getNamespaceTable().
			// this.opcServer.getNamespaceUris().
					toNodeId(datatypeDescription));
			descriptionOf.setTargetNodeClass(NodeClass.Object);
			descriptionOf.setTargetNodeId(defaultBinary);
			referencesToAdd.add(descriptionOf);

			addReferences(referencesToAdd.toArray(new AddReferencesItem[0]));
//			this.opcServer.getMaster().addReferences(referencesToAdd.toArray(new AddReferencesItem[0]), null);
		}
	}

	public ExpandedNodeId findDefaultBinaryEncoding(DataTypeNode node) {
		ExpandedNodeId[] targetEncodings = node.findTargets(Identifiers.HasEncoding, false);
		for (ExpandedNodeId target : targetEncodings) {
			Node targetNode = getNode(target); // OpcUtils.getNode(this.opcServer, target);
			if (targetNode.getBrowseName().getName().compareTo(DEFAULT_BINARY) == 0) {
				return target;
			}
		}

		return ExpandedNodeId.NULL;
	}

	public List<String> findBinarySchemas() {
		List<String> schemas = new ArrayList<>();

		Node binarySchema = getNode(Identifiers.OPCBinarySchema_TypeSystem); // OpcUtils.getNode(this.opcServer,
																				// Identifiers.OPCBinarySchema_TypeSystem);
		ExpandedNodeId[] targets = binarySchema.findTargets(Identifiers.HasComponent, false);
		for (ExpandedNodeId target : targets) {
			Node targetNode = getNode(target); // OpcUtils.getNode(this.opcServer, target);
			if (targetNode == null) {
				continue;
			}
			schemas.add(targetNode.getBrowseName().getName());
		}
		return schemas;
	}

	public abstract ExpandedNodeId findBinarySchemaId(int namespaceIndex);

	public ExpandedNodeId findDatatypeDescription(ExpandedNodeId binarySchemaId, String datatypeName) {
		Node binarySchemaNode = getNode(binarySchemaId); // OpcUtils.getNode(this.opcServer, binarySchemaId);
		ExpandedNodeId[] targets = binarySchemaNode.findTargets(Identifiers.HasComponent, false);
		for (ExpandedNodeId target : targets) {
			Node targetNode = getNode(target); // OpcUtils.getNode(this.opcServer, target);

			String targetName = targetNode.getBrowseName().getName();
			if (targetName.equals(datatypeName)) {
				return target;
			}

		}
		return ExpandedNodeId.NULL;
	}

	ByteString createEmptyDataDictionaryValue(int nsIndex) {
		String namespaceUri = getNamespaceTable().getUri(nsIndex); // this.opcServer.getNamespaceUris().getUri(nsIndex);

		AbstractDataDictionaryHelper ddHelper = createDataDictionaryHelper(); // new
																				// AbstractDataDictionaryHelper(this.opcServer);
		Document document = ddHelper.createEmptyDataDictionary(namespaceUri);

		String uaStructure = ddHelper.prettyPrint(document);
		if (uaStructure.isEmpty()) {
			return ByteString.EMPTY;
		}
		return ddHelper.encodeBase64(uaStructure);
	}

	protected abstract void addNode(AddNodesItem[] nodes, boolean includeChildren) throws ServiceResultException;

	protected abstract void addReferences(AddReferencesItem[] references) throws ServiceResultException;

	protected abstract AbstractDataDictionaryHelper createDataDictionaryHelper();

	protected abstract NamespaceTable getNamespaceTable();

	protected abstract Node getNode(ExpandedNodeId nodeId);

	protected abstract Node getNode(NodeId nodeId);

	protected abstract NodeId getNextNodeId(int namespaceIndex);
}
