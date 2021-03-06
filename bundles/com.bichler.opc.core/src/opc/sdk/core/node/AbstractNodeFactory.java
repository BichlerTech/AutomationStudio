package opc.sdk.core.node;

import java.util.ArrayList;
import java.util.List;

import org.opcfoundation.ua.builtintypes.DataValue;
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
import org.opcfoundation.ua.core.Attributes;
import org.opcfoundation.ua.core.DataTypeAttributes;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.MethodAttributes;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.core.ObjectAttributes;
import org.opcfoundation.ua.core.ObjectTypeAttributes;
import org.opcfoundation.ua.core.ReadValueId;
import org.opcfoundation.ua.core.ReferenceNode;
import org.opcfoundation.ua.core.ReferenceTypeAttributes;
import org.opcfoundation.ua.core.VariableAttributes;
import org.opcfoundation.ua.core.VariableTypeAttributes;
import org.opcfoundation.ua.core.ViewAttributes;
import org.opcfoundation.ua.encoding.EncoderContext;
import org.opcfoundation.ua.encoding.IEncodeable;

import opc.sdk.core.utils.Utils;

/**
 * Factory to create nodes.
 * 
 * @author Thomas Z&ouml;bauer
 * 
 */
public abstract class AbstractNodeFactory implements NodeIdGenerator {
	public AbstractNodeFactory() {
	}

	/**
	 * Returns all required ReadValueId objects to read a node from the server.
	 * 
	 * @param nodeIds
	 * @param nodeClasses
	 * @return
	 */
	public ReadValueId[] buildReadValueIdForNodes(NodeId nodeId, NodeClass nodeClass) {
		ReadValueId[] readvalueIds = null;
		UnsignedInteger[] attributes = null;
		switch (nodeClass) {
		case Object:
			attributes = Utils.OBJECTATTRIBUTES;
			break;
		case ObjectType:
			attributes = Utils.OBJECTTYPEATTRIBUTES;
			break;
		case Variable:
			attributes = Utils.VARIABLEATTRIBUTES;
			break;
		case VariableType:
			attributes = Utils.VARIABLETYPEATTRIBUTES;
			break;
		case DataType:
			attributes = Utils.DATATYPEATTRIBUTES;
			break;
		case Method:
			attributes = Utils.METHODATTRIBUTES;
			break;
		case ReferenceType:
			attributes = Utils.REFERENCETYPEATTRIBUTES;
			break;
		case View:
			attributes = Utils.VIEWATTRIBUTES;
			break;
		default:
			attributes = new UnsignedInteger[0];
		}
		readvalueIds = new ReadValueId[attributes.length];
		for (int i = 0; i < attributes.length; i++) {
			/** result */
			ReadValueId readValueId = new ReadValueId();
			/** nodeid */
			readValueId.setNodeId(nodeId);
			/** attributes */
			readValueId.setAttributeId(attributes[i]);
			readvalueIds[i] = readValueId;
		}
		return readvalueIds;
	}

	/**
	 * Create a node from the given datavalues result.
	 * 
	 * @param NodeId
	 * @param NodeClass
	 * @param Results
	 * 
	 * @return Node with the attributes of results.
	 * @throws ServiceResultException
	 */
	public Node createNode(NamespaceTable namespaceTable, NodeId nodeId, NodeClass nodeClass, DataValue[] results)
			throws ServiceResultException {
		// get required attributes
		// -1 because array starts with zero and attributes with 1
		Node node = null;
		if (results != null && results.length > 0) {
			Variant value = results[Attributes.BrowseName.intValue() - 1].getValue();
			QualifiedName browseName = null;
			if (value != null && !value.isEmpty()) {
				browseName = (QualifiedName) value.getValue();
			}
			ExtensionObject nodeAttributes = NodeAttributeFactory.createNodeAttributes(nodeClass, results);
			if (nodeAttributes != null) {
				node = createNode(namespaceTable, browseName, nodeAttributes, nodeClass, null, null,
						namespaceTable.toExpandedNodeId(nodeId), null);
				// node = createNode(browseName, nodeAttributes, nodeClass, null, null,
				// NamespaceTable.getDefaultInstance().toExpandedNodeId(nodeId), null);
			}
		}
		return node;
	}

	/**
	 * Creates a node with its AddNodes service parameters.
	 * 
	 * @return Node
	 */
	public Node createNode(NamespaceTable namespaceTable, QualifiedName browseName, ExtensionObject nodesAttribute,
			NodeClass nodeClass, ExpandedNodeId parentNodeId, NodeId referenceTypeId, ExpandedNodeId requestedNodeId,
			ExpandedNodeId typeDefinition) throws ServiceResultException {
		Node created = null;
		IEncodeable decodedAttributes = null;
		List<ReferenceNode> referenceNodes = new ArrayList<ReferenceNode>();
		switch (nodeClass) {
		case Object:
			decodedAttributes = nodesAttribute.decode(EncoderContext.getDefaultInstance());
			// ROOT
			if (!(parentNodeId == null))
				referenceNodes = createObjectReferenceNodes(referenceTypeId, parentNodeId, typeDefinition);
			else {
				ReferenceNode referenceTypeNode = this.createReferenceNode(Identifiers.HasTypeDefinition,
						new Boolean(false), typeDefinition);
				referenceNodes.add(referenceTypeNode);
			}
			created = this.createObjectNode(namespaceTable.toNodeId(requestedNodeId), browseName,
					((ObjectAttributes) decodedAttributes).getDescription(),
					((ObjectAttributes) decodedAttributes).getDisplayName(), new Object(), nodeClass,
					referenceNodes.toArray(new ReferenceNode[referenceNodes.size()]),
					((ObjectAttributes) decodedAttributes).getUserWriteMask(),
					((ObjectAttributes) decodedAttributes).getWriteMask(),
					((ObjectAttributes) decodedAttributes).getEventNotifier());
			break;
		case ObjectType:
			decodedAttributes = nodesAttribute.decode(EncoderContext.getDefaultInstance());
			referenceNodes = createTypeReferenceNodes(referenceTypeId, parentNodeId);
			created = this.createObjectTypeNode(namespaceTable.toNodeId(requestedNodeId), browseName,
					((ObjectTypeAttributes) decodedAttributes).getDescription(),
					((ObjectTypeAttributes) decodedAttributes).getDisplayName(), new Object()/* handle */, nodeClass,
					referenceNodes.toArray(new ReferenceNode[referenceNodes.size()]),
					((ObjectTypeAttributes) decodedAttributes).getUserWriteMask(),
					((ObjectTypeAttributes) decodedAttributes).getWriteMask(),
					((ObjectTypeAttributes) decodedAttributes).getIsAbstract());
			break;
		case Variable:
			decodedAttributes = nodesAttribute.decode(EncoderContext.getDefaultInstance());
			referenceNodes = createVariableReferenceNodes(referenceTypeId, parentNodeId, typeDefinition);
			created = this.createVariableNode(namespaceTable.toNodeId(requestedNodeId), browseName,
					((VariableAttributes) decodedAttributes).getDescription(),
					((VariableAttributes) decodedAttributes).getDisplayName(), new Object()/* handle */, nodeClass,
					referenceNodes.toArray(new ReferenceNode[referenceNodes.size()]),
					((VariableAttributes) decodedAttributes).getUserWriteMask(),
					((VariableAttributes) decodedAttributes).getWriteMask(),
					((VariableAttributes) decodedAttributes).getValue(),
					((VariableAttributes) decodedAttributes).getDataType(),
					((VariableAttributes) decodedAttributes).getValueRank(),
					((VariableAttributes) decodedAttributes).getArrayDimensions(),
					((VariableAttributes) decodedAttributes).getAccessLevel(),
					((VariableAttributes) decodedAttributes).getUserAccessLevel(),
					((VariableAttributes) decodedAttributes).getMinimumSamplingInterval(),
					((VariableAttributes) decodedAttributes).getHistorizing());
			break;
		case VariableType:
			decodedAttributes = nodesAttribute.decode(EncoderContext.getDefaultInstance());
			referenceNodes = createTypeReferenceNodes(referenceTypeId, parentNodeId);
			created = this.createVariableTypeNode(namespaceTable.toNodeId(requestedNodeId), browseName,
					((VariableTypeAttributes) decodedAttributes).getDescription(),
					((VariableTypeAttributes) decodedAttributes).getDisplayName(), new Object()/**/, nodeClass,
					referenceNodes.toArray(new ReferenceNode[referenceNodes.size()]),
					((VariableTypeAttributes) decodedAttributes).getUserWriteMask(),
					((VariableTypeAttributes) decodedAttributes).getWriteMask(),
					((VariableTypeAttributes) decodedAttributes).getValue(),
					((VariableTypeAttributes) decodedAttributes).getDataType(),
					((VariableTypeAttributes) decodedAttributes).getValueRank(),
					((VariableTypeAttributes) decodedAttributes).getArrayDimensions(),
					((VariableTypeAttributes) decodedAttributes).getIsAbstract());
			break;
		case DataType:
			decodedAttributes = nodesAttribute.decode(EncoderContext.getDefaultInstance());
			referenceNodes = createTypeReferenceNodes(referenceTypeId, parentNodeId);
			created = this.createDataTypeNode(namespaceTable.toNodeId(requestedNodeId), browseName,
					((DataTypeAttributes) decodedAttributes).getDescription(),
					((DataTypeAttributes) decodedAttributes).getDisplayName(), new Object()/**/, nodeClass,
					referenceNodes.toArray(new ReferenceNode[referenceNodes.size()]),
					((DataTypeAttributes) decodedAttributes).getUserWriteMask(),
					((DataTypeAttributes) decodedAttributes).getWriteMask(),
					((DataTypeAttributes) decodedAttributes).getIsAbstract());
			break;
		case ReferenceType:
			decodedAttributes = nodesAttribute.decode(EncoderContext.getDefaultInstance());
			referenceNodes = createTypeReferenceNodes(referenceTypeId, parentNodeId);
			created = this.createReferenceTypeNode(namespaceTable.toNodeId(requestedNodeId), browseName,
					((ReferenceTypeAttributes) decodedAttributes).getDescription(),
					((ReferenceTypeAttributes) decodedAttributes).getDisplayName(), new Object()/* handle */, nodeClass,
					referenceNodes.toArray(new ReferenceNode[referenceNodes.size()]),
					((ReferenceTypeAttributes) decodedAttributes).getUserWriteMask(),
					((ReferenceTypeAttributes) decodedAttributes).getWriteMask(),
					((ReferenceTypeAttributes) decodedAttributes).getIsAbstract(),
					((ReferenceTypeAttributes) decodedAttributes).getSymmetric(),
					((ReferenceTypeAttributes) decodedAttributes).getInverseName());
			break;
		case Method:
			decodedAttributes = nodesAttribute.decode(EncoderContext.getDefaultInstance());
			referenceNodes = createMethodReferenceNodes(referenceTypeId, parentNodeId);
			created = this.createMethodNode(namespaceTable.toNodeId(requestedNodeId), browseName,
					((MethodAttributes) decodedAttributes).getDescription(),
					((MethodAttributes) decodedAttributes).getDisplayName(), new Object()/**/, nodeClass,
					referenceNodes.toArray(new ReferenceNode[referenceNodes.size()]),
					((MethodAttributes) decodedAttributes).getUserWriteMask(),
					((MethodAttributes) decodedAttributes).getWriteMask(),
					((MethodAttributes) decodedAttributes).getExecutable(),
					((MethodAttributes) decodedAttributes).getUserExecutable());
			break;
		case View:
			decodedAttributes = nodesAttribute.decode(EncoderContext.getDefaultInstance());
			referenceNodes = createViewReferenceNodes();
			created = this.createViewNode(namespaceTable.toNodeId(requestedNodeId), browseName,
					((ViewAttributes) decodedAttributes).getDescription(),
					((ViewAttributes) decodedAttributes).getDisplayName(), new Object(), nodeClass,
					referenceNodes.toArray(new ReferenceNode[referenceNodes.size()]),
					((ViewAttributes) decodedAttributes).getUserWriteMask(),
					((ViewAttributes) decodedAttributes).getWriteMask(),
					((ViewAttributes) decodedAttributes).getContainsNoLoops(),
					((ViewAttributes) decodedAttributes).getEventNotifier());
			break;
		}
		onCreate(created);
		return created;
	}

	public ReferenceNode createReferenceNode(NodeId referenceType, Boolean isInverse, ExpandedNodeId targetId) {
		ReferenceNode referenceNode = new ReferenceNode(referenceType, isInverse, targetId);
		return referenceNode;
	}

	protected Node createDataTypeNode(NodeId nodeId, QualifiedName browseName, LocalizedText description,
			LocalizedText displayName, Object handle, NodeClass nodeClass, ReferenceNode[] references,
			UnsignedInteger userWriteMask, UnsignedInteger writeMask, Boolean isAbstract) {
		Node dataTypeNode = new UADataTypeNode(nodeId, nodeClass, browseName, displayName, description, writeMask,
				userWriteMask, references, isAbstract);
		return dataTypeNode;
	}

	protected Node createMethodNode(NodeId nodeId, QualifiedName browseName, LocalizedText description,
			LocalizedText displayName, Object handle, NodeClass nodeClass, ReferenceNode[] references,
			UnsignedInteger userWriteMask, UnsignedInteger writeMask, Boolean executeable, Boolean userexecuteable) {
		Node methodNode = new UAMethodNode(nodeId, nodeClass, browseName, displayName, description, writeMask,
				userWriteMask, references, executeable, userexecuteable);
		return methodNode;
	}

	protected Node createObjectNode(NodeId nodeId, QualifiedName browseName, LocalizedText description,
			LocalizedText displayName, Object handle, NodeClass nodeClass, ReferenceNode[] references,
			UnsignedInteger userWriteMask, UnsignedInteger writeMask, UnsignedByte eventNotifier) {
		Node objectNode = new UAObjectNode(nodeId, nodeClass, browseName, displayName, description, writeMask,
				userWriteMask, references, eventNotifier);
		return objectNode;
	}

	protected Node createObjectTypeNode(NodeId nodeId, QualifiedName browseName, LocalizedText description,
			LocalizedText displayName, Object handle, NodeClass nodeClass, ReferenceNode[] references,
			UnsignedInteger userWriteMask, UnsignedInteger writeMask, Boolean isAbstract) {
		Node objectTypeNode = new UAObjectTypeNode(nodeId, nodeClass, browseName, displayName, description, writeMask,
				userWriteMask, references, isAbstract);
		return objectTypeNode;
	}

	protected Node createReferenceTypeNode(NodeId nodeId, QualifiedName browseName, LocalizedText description,
			LocalizedText displayName, Object handle, NodeClass nodeClass, ReferenceNode[] references,
			UnsignedInteger userWriteMask, UnsignedInteger writeMask, Boolean isAbstract, Boolean symmetric,
			LocalizedText inverseName) {
		Node referenceTypeNode = new UAReferenceTypeNode(nodeId, nodeClass, browseName, displayName, description,
				writeMask, userWriteMask, references, isAbstract, symmetric, inverseName);
		return referenceTypeNode;
	}

	protected Node createVariableNode(NodeId nodeId, QualifiedName browseName, LocalizedText description,
			LocalizedText displayName, Object handle, NodeClass nodeClass, ReferenceNode[] references,
			UnsignedInteger userWriteMask, UnsignedInteger writeMask, Variant value, NodeId dataType, Integer valueRank,
			UnsignedInteger[] arrayDimensions, UnsignedByte accessLevel, UnsignedByte userAccessLevel,
			Double minimumSamplingInterval, Boolean historizing) {
		Node variableNode = new UAVariableNode(nodeId, nodeClass, browseName, displayName, description, writeMask,
				userWriteMask, references, value, dataType, valueRank, arrayDimensions, accessLevel, userAccessLevel,
				minimumSamplingInterval, historizing);
		return variableNode;
	}

	protected Node createVariableTypeNode(NodeId nodeId, QualifiedName browseName, LocalizedText description,
			LocalizedText displayName, Object handle, NodeClass nodeClass, ReferenceNode[] references,
			UnsignedInteger userWriteMask, UnsignedInteger writeMask, Variant value, NodeId dataType, Integer valueRank,
			UnsignedInteger[] arrayDimensions, Boolean isAbstract) {
		Node variableTypeNode = new UAVariableTypeNode(nodeId, nodeClass, browseName, displayName, description,
				writeMask, userWriteMask, references, value, dataType, valueRank, arrayDimensions, isAbstract);
		return variableTypeNode;
	}

	protected Node createViewNode(NodeId nodeId, QualifiedName browseName, LocalizedText description,
			LocalizedText displayName, Object handle, NodeClass nodeClass, ReferenceNode[] references,
			UnsignedInteger userWriteMask, UnsignedInteger writeMask, Boolean containsNoLoops,
			UnsignedByte eventNotifier) {
		Node viewNode = new ViewNode(nodeId, nodeClass, browseName, displayName, description, writeMask, userWriteMask,
				references, containsNoLoops, eventNotifier);
		return viewNode;
	}

	private List<ReferenceNode> createViewReferenceNodes() {
		List<ReferenceNode> referenceNodes = new ArrayList<ReferenceNode>();
		return referenceNodes;
	}

	private List<ReferenceNode> createObjectReferenceNodes(NodeId referenceTypeId, ExpandedNodeId parentNodeId,
			ExpandedNodeId typeDefinition) {
		List<ReferenceNode> referenceNodes = new ArrayList<ReferenceNode>();
		referenceNodes.add(new ReferenceNode(referenceTypeId, true, parentNodeId));
		referenceNodes.add(new ReferenceNode(Identifiers.HasTypeDefinition, false, typeDefinition));
		return referenceNodes;
	}

	private List<ReferenceNode> createVariableReferenceNodes(NodeId referenceTypeId, ExpandedNodeId parentNodeId,
			ExpandedNodeId typeDefinition) {
		List<ReferenceNode> referenceNodes = new ArrayList<ReferenceNode>();
		referenceNodes.add(new ReferenceNode(referenceTypeId, true, parentNodeId));
		referenceNodes.add(new ReferenceNode(Identifiers.HasTypeDefinition, false, typeDefinition));
		return referenceNodes;
	}

	private List<ReferenceNode> createMethodReferenceNodes(NodeId referenceTypeId, ExpandedNodeId parentNodeId) {
		List<ReferenceNode> referenceNodes = new ArrayList<ReferenceNode>();
		referenceNodes.add(new ReferenceNode(referenceTypeId, true, parentNodeId));
		return referenceNodes;
	}

	private List<ReferenceNode> createTypeReferenceNodes(NodeId referenceTypeId, ExpandedNodeId parentNodeId) {
		List<ReferenceNode> referenceNodes = new ArrayList<ReferenceNode>();
		referenceNodes.add(new ReferenceNode(referenceTypeId, true, parentNodeId));
		return referenceNodes;
	}

	abstract void onCreate(Node created);

	public abstract void onRemove(Integer[] indizes2refresh);
}
