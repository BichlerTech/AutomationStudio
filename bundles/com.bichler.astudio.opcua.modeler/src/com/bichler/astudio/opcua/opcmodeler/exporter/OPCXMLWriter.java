package com.bichler.astudio.opcua.opcmodeler.exporter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.QualifiedName;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.core.ReferenceNode;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.opcua.opcmodeler.singletons.type.ServerTypeModel;

import opc.sdk.core.informationmodel.xml.NodeType;
import opc.sdk.core.node.DataTypeNode;
import opc.sdk.core.node.MethodNode;
import opc.sdk.core.node.Node;
import opc.sdk.core.node.NodeIdUtil;
import opc.sdk.core.node.ObjectNode;
import opc.sdk.core.node.ObjectTypeNode;
import opc.sdk.core.node.ReferenceTypeNode;
import opc.sdk.core.node.VariableNode;
import opc.sdk.core.node.VariableTypeNode;
import opc.sdk.core.types.TypeTable;
import opc.sdk.server.core.UAServerApplicationInstance;

/**
 * Information Model XML-Exporter.
 * 
 * @author Thomas Zöchbauer
 */
public class OPCXMLWriter implements XMLWriter {
	/**
	 * Root Element for all Nodes to write.
	 */
	private Element nodesStorage = null;
	/**
	 * Generic CoreServer instance.
	 */
	private UAServerApplicationInstance server = null;
	/**
	 * NamespaceIndizes to export.
	 */
	private List<Integer> allowedNamespaces = null;
	/** Namespaces as String to export */
	private List<String> namespaces = null;

	/**
	 * Document writer, which should export the whole model
	 */
	public OPCXMLWriter() {
	}

	/**
	 * Document writer, which exports a model with defined namespace indizes
	 * 
	 * @param CoreServer        CoreServerInstance
	 * @param Namespaces        AllowedNamespaces as String.
	 * @param AllowedNamespaces AllowedNamespaces as Index.
	 */
	public OPCXMLWriter(UAServerApplicationInstance coreServer, List<String> namespaces,
			List<Integer> allowedNamespaces) {
		this.server = coreServer;
		this.allowedNamespaces = allowedNamespaces;
		this.namespaces = namespaces;
	}

	/**
	 * Writes the header of the document. This includes the document, the namespaces
	 * and the server uris.
	 * 
	 * @param Document         Document to write.
	 * @param Namespaces       Namespaces to write.
	 * @param ServerNamespaces All Namespaces from the Server.
	 */
	public void writeModelDocument(Document document, String[] namespaceTable, String[] serverUriTable) {
		Element nodeSet = WriteUtil.writeBeginn(document);
		document.appendChild(nodeSet);
		// namespaces
		Element namespaceUris = WriteUtil.writeNamespaceUris(document, namespaceTable);
		nodeSet.appendChild(namespaceUris);
		// server uris
		Element serverUris = WriteUtil.writeServerUris(document, serverUriTable);
		nodeSet.appendChild(serverUris);
		// start tag of nodes (<nodes> </nodes>
		Element nodes = WriteUtil.writeNodes(document);
		nodeSet.appendChild(nodes);
		this.nodesStorage = nodes;
	}

	public void writeTypes(Document document, ServerTypeModel serverModel, String[] urisToExport) {
		HashMap<ExpandedNodeId, ExpandedNodeId> mapping = serverModel.getObjectMapping();
		List<Integer> nsIndizes = new ArrayList<>();
		for (String uri : urisToExport) {
			int index = ServerInstance.getInstance().getServerInstance().getNamespaceUris().getIndex(uri);
			nsIndizes.add(index);
		}
		for (Entry<ExpandedNodeId, ExpandedNodeId> entry : mapping.entrySet()) {
			ExpandedNodeId key = entry.getKey();
			ExpandedNodeId value = entry.getValue();
			if (ExpandedNodeId.isNull(key)) {
				continue;
			}
			boolean valid = nsIndizes.contains(key.getNamespaceIndex());
			if (!valid) {
				continue;
			}
			WriteUtil.writeTypeMappingNode(document, this.nodesStorage, key, value, nsIndizes);
		}
	}

	/**
	 * Append the array of nodes to the xml document
	 * 
	 * @param Document Document to write.
	 * @param Nodes    Nodes to write.
	 */
	@Override
	public void writeNodes(Document document, Node[] nodes) {
		for (Node node : nodes) {
			writeNode(document, node);
		}
	}

	/**
	 * Append the node to the XML Document.
	 * 
	 * @param Document Document to write.
	 * @param Node     Node to write.
	 */
	@Override
	public void writeNode(Document document, Node node) {
		// declaration of the attributes
		Object[] attributes = null;
		// select the nodeType
		NodeType nodeType = null;
		switch (node.getNodeClass()) {
		case Object:
			nodeType = NodeType.ObjectNode;
			attributes = new Object[1];
			attributes[0] = ((ObjectNode) node).getEventNotifier().toString();
			break;
		case ObjectType:
			nodeType = NodeType.ObjectTypeNode;
			attributes = new Object[1];
			attributes[0] = ((ObjectTypeNode) node).getIsAbstract().toString();
			break;
		case Variable:
			nodeType = NodeType.VariableNode;
			attributes = new Object[8];
			// write the value section. Value not null and not empty!
			if (((VariableNode) node).getValue() != null && !((VariableNode) node).getValue().isEmpty()) {
				attributes[0] = ((VariableNode) node).getValue();
			} else {
				attributes[0] = null;
			}
			attributes[1] = checkExportedNodeId(document, ((VariableNode) node).getDataType());
			attributes[2] = ((VariableNode) node).getValueRank().toString();
			try {
				UnsignedInteger[] dim = ((VariableNode) node).getArrayDimensions();
				if (dim.length == 0) {
					attributes[3] = "";
				} else {
					attributes[3] = "{";
					String comma = "";
					for (UnsignedInteger item : dim) {
						attributes[3] = attributes[3] + comma + item.toString();
						comma = ",";
					}
					attributes[3] = attributes[3] + "}";
				}
			} catch (NullPointerException npe) {
				// error array dim is null and null = 0;
				attributes[3] = "";
			}
			attributes[4] = ((VariableNode) node).getAccessLevel().toString();
			attributes[5] = ((VariableNode) node).getUserAccessLevel().toString();
			attributes[6] = ((VariableNode) node).getMinimumSamplingInterval().toString();
			attributes[7] = ((VariableNode) node).getHistorizing().toString();
			break;
		case VariableType:
			nodeType = NodeType.VariableTypeNode;
			attributes = new Object[5];
			if (((VariableTypeNode) node).getValue() != null && !((VariableTypeNode) node).getValue().isEmpty()) {
				attributes[0] = ((VariableTypeNode) node).getValue();
			} else {
				attributes[0] = null;
			}
			attributes[1] = checkExportedNodeId(document, ((VariableTypeNode) node).getDataType());
			attributes[2] = ((VariableTypeNode) node).getValueRank().toString();
			try {
				UnsignedInteger[] dim = ((VariableTypeNode) node).getArrayDimensions();
				if (dim.length == 0) {
					attributes[3] = "";
				} else {
					attributes[3] = "{";
					String comma = "";
					for (UnsignedInteger item : dim) {
						attributes[3] = attributes[3] + comma + item.toString();
						comma = ",";
					}
					attributes[3] = attributes[3] + "}";
				}
			} catch (NullPointerException npe) {
				// error array dim is null and null = 0;
				attributes[3] = "";
			}
			attributes[4] = ((VariableTypeNode) node).getIsAbstract().toString();
			break;
		case DataType:
			nodeType = NodeType.DataTypeNode;
			attributes = new String[1];
			attributes[0] = ((DataTypeNode) node).getIsAbstract().toString();
			break;
		case ReferenceType:
			nodeType = NodeType.ReferenceTypeNode;
			attributes = new Object[3];
			attributes[0] = ((ReferenceTypeNode) node).getIsAbstract().toString();
			attributes[1] = ((ReferenceTypeNode) node).getSymmetric().toString();
			attributes[2] = ((ReferenceTypeNode) node).getInverseName();
			break;
		case Method:
			nodeType = NodeType.MethodNode;
			attributes = new Object[2];
			attributes[0] = ((MethodNode) node).getExecutable().toString();
			attributes[1] = ((MethodNode) node).getUserExecutable().toString();
			break;
		case View:
			nodeType = NodeType.View;
			attributes = new String[1];
			break;
		case Unspecified:
			nodeType = NodeType.Unspecified;
			attributes = new String[1];
			break;
		}
		// build the reference nodes
		List<ReferenceNodeStructure> refNodeStructure = new ArrayList<ReferenceNodeStructure>();
		TypeTable typeTable = ServerInstance.getInstance().getServerInstance().getTypeTable();
		String refNodeID;
		String refTypeID;
		// Debug helper
		for (ReferenceNode ref : node.getReferences()) {
			/**
			 * here we should check if we have a hierarchical child reference
			 */
			if ((!ref.getIsInverse()
					&& typeTable.isTypeOf(ref.getReferenceTypeId(), Identifiers.HierarchicalReferences))
					|| ref.getTargetId() == null) {
				continue;
			}
			refNodeID = checkExportedExpandedNodeId(document, ref.getTargetId());
			new ExpandedNodeId(ref.getTargetId().getServerIndex(), ref.getTargetId().getNamespaceIndex(),
					ref.getTargetId().getValue());
			refTypeID = checkExportedNodeId(document, ref.getReferenceTypeId());
			try {
				refNodeStructure.add(new ReferenceNodeStructure(refTypeID, ref.getIsInverse().toString(), refNodeID));
			} catch (NullPointerException npe) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, npe);
			}
		}
		// add the node
		addNodeElement(document, nodeType, node.getNodeId().toString(), node.getNodeClass(), node.getBrowseName(),
				node.getDisplayName(), node.getDescription(), node.getWriteMask().toString(),
				node.getUserWriteMask().toString(),
				refNodeStructure.toArray(new ReferenceNodeStructure[refNodeStructure.size()]), attributes);
	}

	/**
	 * Appends a Node-Element to the Document.
	 * 
	 * @param Document       Document to write.
	 * @param NodeType       Type of the Node.
	 * @param NodeId         Id of the Node.
	 * @param NodeClass      NodeClass of the Node.
	 * @param Browsename     BrowseName of the Node.
	 * @param Displayname    DisplayName of the Node.
	 * @param Description    Description of the Node.
	 * @param WriteMask      WriteMaks of the Node.
	 * @param UserWriteMask  UserWriteMask of the Node.
	 * @param ReferenceNodes References of the Node.
	 * @param Attributes     Node specific Attributes, depends on its NodeType.
	 */
	protected void addNodeElement(Document document, NodeType nodeType, String nodeId, NodeClass nodeClass,
			QualifiedName browsename, LocalizedText displayname, LocalizedText description, String writeMask,
			String userWriteMask, ReferenceNodeStructure[] referenceNodes, Object... attributes) {
		BaseAttributes attribute;
		switch (nodeType) {
		case ObjectNode:
			attribute = new ObjectAttribute(attributes);
			writeObject(document, nodeType, nodeId, nodeClass, browsename, displayname, description, writeMask,
					userWriteMask, referenceNodes, (ObjectAttribute) attribute);
			break;
		case ObjectTypeNode:
			attribute = new ObjectTypeAttribute(attributes);
			writeObjectType(document, nodeType, nodeId, nodeClass, browsename, displayname, description, writeMask,
					userWriteMask, referenceNodes, (ObjectTypeAttribute) attribute);
			break;
		case VariableNode:
			attribute = new VariableAttribute(attributes);
			writeVariable(document, nodeType, nodeId, nodeClass, browsename, displayname, description, writeMask,
					userWriteMask, referenceNodes, (VariableAttribute) attribute);
			break;
		case VariableTypeNode:
			attribute = new VariableTypeAttribute(attributes);
			writeVariableType(document, nodeType, nodeId, nodeClass, browsename, displayname, description, writeMask,
					userWriteMask, referenceNodes, (VariableTypeAttribute) attribute);
			break;
		case MethodNode:
			attribute = new MethodAttribute(attributes);
			writeMethod(document, nodeType, nodeId, nodeClass, browsename, displayname, description, writeMask,
					userWriteMask, referenceNodes, (MethodAttribute) attribute);
			break;
		case DataTypeNode:
			attribute = new DataTypeAttribute(attributes);
			writeDataType(document, nodeType, nodeId, nodeClass, browsename, displayname, description, writeMask,
					userWriteMask, referenceNodes, (DataTypeAttribute) attribute);
			break;
		case ReferenceTypeNode:
			attribute = new ReferenceAttributes(attributes);
			writeReferenceType(document, nodeType, nodeId, nodeClass, browsename, displayname, description, writeMask,
					userWriteMask, referenceNodes, (ReferenceAttributes) attribute);
			break;
		default:
			break;
		}
	}

	/**
	 * Writes the attributes of a base node
	 * 
	 * @param Document       Document to write.
	 * @param ElementToUse   XML - Element to write.
	 * @param NodeType       Type of teh Node.
	 * @param NodeId         Id of the Node.
	 * @param NodeClass      NodeClass of the Node.
	 * @param Browsename     BrowseName of the Node.
	 * @param Displayname    DisplayName of the Node.
	 * @param Description    Description of the Node.
	 * @param WriteMask      WriteMask of the Node.
	 * @param UserWriteMask  UserWriteMask of the Node.
	 * @param ReferenceNodes References of the Node.
	 */
	private void writeBaseNodeAttributes(Document document, Element elementToWrite, NodeType nodeType, String nodeId,
			NodeClass nodeClass, QualifiedName browsename, LocalizedText displayname, LocalizedText description,
			String writeMask, String userWriteMask, ReferenceNodeStructure... referenceNodes) {
		NodeId dec = NodeId.parseNodeId(nodeId);
		if (dec.getNamespaceIndex() != 0) {
			nodeId = checkExportedNodeId(document, dec);
		}
		// Attributes of the Element (NodeId)
		Element nodeIdAttr = WriteUtil.writeNodeId(document, nodeId);
		elementToWrite.appendChild(nodeIdAttr);
		// Attributes of the Element (Browsename)
		Element nodeClassAttr = WriteUtil.writeNodeClass(document, nodeClass);
		elementToWrite.appendChild(nodeClassAttr);
		// Attributes of the Element (Browsename)
		Element browsenameAttr = WriteUtil.writeBrowsename(document, browsename);
		elementToWrite.appendChild(browsenameAttr);
		// Attributes of the Element (Displayname)
		Element displaynameAttr = WriteUtil.writeDisplayname(document, displayname);
		elementToWrite.appendChild(displaynameAttr);
		// Attributes of the Element (Description)
		Element descriptionAttr = WriteUtil.writeDescription(document, description);
		elementToWrite.appendChild(descriptionAttr);
		// Attributes of the Element (WriteMask)
		Element writeMaskAttr = WriteUtil.writeWriteMask(document, writeMask);
		elementToWrite.appendChild(writeMaskAttr);
		// Attributes of the Element (UserWriteMask)
		Element userWriteMaskAttr = WriteUtil.writeUserWriteMask(document, userWriteMask);
		elementToWrite.appendChild(userWriteMaskAttr);
		Element references = WriteUtil.writeReferences(document);
		elementToWrite.appendChild(references);
		for (ReferenceNodeStructure refNode : referenceNodes) {
			try {
				/**
				 * now no check is required, because we have added all necessary ns previously
				 * String checkedRefTypeId = checkExportedNodeId(refNode .getReferenceTypeId());
				 * String checkedTargetId = checkExportedNodeId(refNode .getTargetId());
				 */
				String checkedRefTypeId = refNode.getReferenceTypeId();
				String checkedTargetId = refNode.getTargetId();
				Element refNodeElement = WriteUtil.writeReferenceNode(document, checkedRefTypeId,
						refNode.getIsInverse(), checkedTargetId);
				references.appendChild(refNodeElement);
			} catch (NullPointerException npe) {
				npe.printStackTrace();
			}
		}
		this.nodesStorage.appendChild(elementToWrite);
	}

	/**
	 * Re-Naming NodeIds when exporting specified Namespaces.
	 * 
	 * @param NodeId NodeId to change.
	 * @return Changed NodeId, or the same if nothing is to change.
	 */
	private String checkExportedNodeId(Document document, NodeId checkToChange) {
		int index;
		/** get the namespace uri for the target */
		String ns = ServerInstance.getInstance().getServerInstance().getNamespaceUris()
				.getUri(checkToChange.getNamespaceIndex());
		if (!this.namespaces.contains(ns)) {
			this.namespaces.add(ns);
			WriteUtil.appendNamespaceUri(document, ns);
		}
		index = this.namespaces.indexOf(ns);
		checkToChange = NodeIdUtil.createNodeId(index, checkToChange.getValue());
		return checkToChange.toString();
	}

	/**
	 * Re-Naming NodeIds when exporting specified Namespaces.
	 * 
	 * @param NodeId NodeId to change.
	 * @return Changed NodeId, or the same if nothing is to change.
	 */
	private String checkExportedExpandedNodeId(Document document, ExpandedNodeId checkToChange) {
		int index;
		/** get the namespace uri for the target */
		String ns = ServerInstance.getInstance().getServerInstance().getNamespaceUris()
				.getUri(checkToChange.getNamespaceIndex());
		if (!this.namespaces.contains(ns)) {
			this.namespaces.add(ns);
			WriteUtil.appendNamespaceUri(document, ns);
		}
		index = this.namespaces.indexOf(ns);
		checkToChange = new ExpandedNodeId(checkToChange.getServerIndex(),
				NodeIdUtil.createNodeId(index, checkToChange.getValue()));
		return checkToChange.toString();
	}

	/**
	 * Write DataType Node Attributes.
	 * 
	 * @param Document       Document to write.
	 * @param NodeType       Type of teh Node.
	 * @param NodeId         Id of the Node.
	 * @param NodeClass      NodeClass of the Node.
	 * @param Browsename     BrowseName of the Node.
	 * @param Displayname    DisplayName of the Node.
	 * @param Description    Description of the Node.
	 * @param WriteMask      WriteMask of the Node.
	 * @param UserWriteMask  UserWriteMask of the Node.
	 * @param ReferenceNodes References of the Node.
	 * @param Attribute      Node specific Attributes, depends on its NodeType.
	 */
	private void writeDataType(Document document, NodeType nodeType, String nodeId, NodeClass nodeClass,
			QualifiedName browsename, LocalizedText displayname, LocalizedText description, String writeMask,
			String userWriteMask, ReferenceNodeStructure[] referenceNodes, DataTypeAttribute attribute) {
		String isAbstract = attribute.getIsAbstract();
		// Node Element
		Element dataType = WriteUtil.writeNodeHeader(document, nodeType);
		// write base attributes
		writeBaseNodeAttributes(document, dataType, nodeType, nodeId, nodeClass, browsename, displayname, description,
				writeMask, userWriteMask, referenceNodes);
		// Attributes of the Element (IsAbstract)
		Element executeableAttr = WriteUtil.writeExecutable(document, isAbstract);
		dataType.appendChild(executeableAttr);
	}

	/**
	 * Write Method Node Attributes.
	 * 
	 * @param Document       Document to write.
	 * @param NodeType       Type of teh Node.
	 * @param NodeId         Id of the Node.
	 * @param NodeClass      NodeClass of the Node.
	 * @param Browsename     BrowseName of the Node.
	 * @param Displayname    DisplayName of the Node.
	 * @param Description    Description of the Node.
	 * @param WriteMask      WriteMask of the Node.
	 * @param UserWriteMask  UserWriteMask of the Node.
	 * @param ReferenceNodes References of the Node.
	 * @param Attribute      Node specific Attributes, depends on its NodeType.
	 */
	private void writeMethod(Document document, NodeType nodeType, String nodeId, NodeClass nodeClass,
			QualifiedName browsename, LocalizedText displayname, LocalizedText description, String writeMask,
			String userWriteMask, ReferenceNodeStructure[] referenceNodes, MethodAttribute attribute) {
		String executeable = attribute.getExecuteable();
		String userExecuteable = attribute.getUserExecuteable();
		// Node Element
		Element method = WriteUtil.writeNodeHeader(document, nodeType);
		// write base attributes
		writeBaseNodeAttributes(document, method, nodeType, nodeId, nodeClass, browsename, displayname, description,
				writeMask, userWriteMask, referenceNodes);
		// Attributes of the Element (Executeable)
		Element executeableAttr = WriteUtil.writeExecutable(document, executeable);
		method.appendChild(executeableAttr);
		// Attributes of the Element (UserExecuteable)
		Element userExecuteableAttr = WriteUtil.writeUserExecutable(document, userExecuteable);
		method.appendChild(userExecuteableAttr);
	}

	/**
	 * Write Object Node Attributes.
	 * 
	 * @param Document       Document to write.
	 * @param NodeType       Type of teh Node.
	 * @param NodeId         Id of the Node.
	 * @param NodeClass      NodeClass of the Node.
	 * @param Browsename     BrowseName of the Node.
	 * @param Displayname    DisplayName of the Node.
	 * @param Description    Description of the Node.
	 * @param WriteMask      WriteMask of the Node.
	 * @param UserWriteMask  UserWriteMask of the Node.
	 * @param ReferenceNodes References of the Node.
	 * @param Attribute      Node specific Attributes, depends on its NodeType.
	 */
	private void writeObject(Document document, NodeType nodeType, String nodeId, NodeClass nodeClass,
			QualifiedName browsename, LocalizedText displayname, LocalizedText description, String writeMask,
			String userWriteMask, ReferenceNodeStructure[] referenceNodes, ObjectAttribute attribute) {
		String eventNotifier = attribute.getEventNotifier();
		// Node Element
		Element object = WriteUtil.writeNodeHeader(document, nodeType);
		// write base attributes
		writeBaseNodeAttributes(document, object, nodeType, nodeId, nodeClass, browsename, displayname, description,
				writeMask, userWriteMask, referenceNodes);
		// Attributes of the Element (EventNotifier)
		Element eventNotifierAttr = WriteUtil.writeEventNotifier(document, eventNotifier);
		object.appendChild(eventNotifierAttr);
	}

	/**
	 * Write ObjectType Node Attributes.
	 * 
	 * @param Document       Document to write.
	 * @param NodeType       Type of the Node.
	 * @param NodeId         Id of the Node.
	 * @param NodeClass      NodeClass of the Node.
	 * @param Browsename     BrowseName of the Node.
	 * @param Displayname    DisplayName of the Node.
	 * @param Description    Description of the Node.
	 * @param WriteMask      WriteMask of the Node.
	 * @param UserWriteMask  UserWriteMask of the Node.
	 * @param ReferenceNodes References of the Node.
	 * @param Attribute      Node specific Attributes, depends on its NodeType.
	 */
	private void writeObjectType(Document document, NodeType nodeType, String nodeId, NodeClass nodeClass,
			QualifiedName browsename, LocalizedText displayname, LocalizedText description, String writeMask,
			String userWriteMask, ReferenceNodeStructure[] referenceNodes, ObjectTypeAttribute attribute) {
		String isAbstract = attribute.getIsAbstract();
		// Node Element
		Element objectType = WriteUtil.writeNodeHeader(document, nodeType);
		// write base attributes
		writeBaseNodeAttributes(document, objectType, nodeType, nodeId, nodeClass, browsename, displayname, description,
				writeMask, userWriteMask, referenceNodes);
		// Attributes of the Element (IsAbstract)
		Element isAbstractAttr = WriteUtil.writeIsAbstract(document, isAbstract);
		objectType.appendChild(isAbstractAttr);
	}

	/**
	 * Write ReferenceType Node Attributes.
	 * 
	 * @param Document       Document to write.
	 * @param NodeType       Type of teh Node.
	 * @param NodeId         Id of the Node.
	 * @param NodeClass      NodeClass of the Node.
	 * @param Browsename     BrowseName of the Node.
	 * @param Displayname    DisplayName of the Node.
	 * @param Description    Description of the Node.
	 * @param WriteMask      WriteMask of the Node.
	 * @param UserWriteMask  UserWriteMask of the Node.
	 * @param ReferenceNodes References of the Node.
	 * @param Attribute      Node specific Attributes, depends on its NodeType.
	 */
	private void writeReferenceType(Document document, NodeType nodeType, String nodeId, NodeClass nodeClass,
			QualifiedName browsename, LocalizedText displayname, LocalizedText description, String writeMask,
			String userWriteMask, ReferenceNodeStructure[] referenceNodes, ReferenceAttributes attribute) {
		String isAbstract = attribute.getIsAbstract();
		String symmetric = attribute.getSymmetric();
		LocalizedText inversename = attribute.getInverseName();
		// Node Element
		Element referenceType = WriteUtil.writeNodeHeader(document, nodeType);
		// write base attributes
		writeBaseNodeAttributes(document, referenceType, nodeType, nodeId, nodeClass, browsename, displayname,
				description, writeMask, userWriteMask, referenceNodes);
		// Attributes of the Element (IsAbstract)
		Element isAbstractAttr = WriteUtil.writeIsAbstract(document, isAbstract);
		referenceType.appendChild(isAbstractAttr);
		// Attributes of the Element (Symmetric)
		Element symmetricAttr = WriteUtil.writeSymmetric(document, symmetric);
		referenceType.appendChild(symmetricAttr);
		// Attributes of the Element (Value)
		Element inversenameAttr = WriteUtil.writeInversename(document, inversename);
		referenceType.appendChild(inversenameAttr);
	}

	/**
	 * Write VariableType Node Attributes.
	 * 
	 * @param Document       Document to write.
	 * @param NodeType       Type of the Node.
	 * @param NodeId         Id of the Node.
	 * @param NodeClass      NodeClass of the Node.
	 * @param Browsename     BrowseName of the Node.
	 * @param Displayname    DisplayName of the Node.
	 * @param Description    Description of the Node.
	 * @param WriteMask      WriteMask of the Node.
	 * @param UserWriteMask  UserWriteMask of the Node.
	 * @param ReferenceNodes References of the Node.
	 * @param Attribute      Node specific Attributes, depends on its NodeType.
	 */
	private void writeVariableType(Document document, NodeType nodeType, String nodeId, NodeClass nodeClass,
			QualifiedName browsename, LocalizedText displayname, LocalizedText description, String writeMask,
			String userWriteMask, ReferenceNodeStructure[] refNodes, VariableTypeAttribute attribute) {
		Variant value = attribute.getValue();
		String dataType = attribute.getDataType();
		String valueRank = attribute.getValueRank();
		String arrayDimension = attribute.getArrayDimensions();
		String isAbstract = attribute.getIsAbstract();
		// Node Element
		Element variableType = WriteUtil.writeNodeHeader(document, nodeType);
		// write base attributes
		writeBaseNodeAttributes(document, variableType, nodeType, nodeId, nodeClass, browsename, displayname,
				description, writeMask, userWriteMask, refNodes);
		// Attributes of the Element (Value)
		TypeTable typeTree = this.server.getServerInstance().getTypeTable();
		// Attributes of the Element (DataType)
		Element dataTypeAttr = WriteUtil.writeDataType(document, dataType);
		variableType.appendChild(dataTypeAttr);
		// Attributes of the Element (ValueRank)
		Element valueRankAttr = WriteUtil.writeValueRank(document, valueRank);
		variableType.appendChild(valueRankAttr);
		// Attributes of the Element (ValueRank)
		Element arrayDimensionAttr = WriteUtil.writeArrayDimension(document, arrayDimension);
		variableType.appendChild(arrayDimensionAttr);
		Element valueAttr = WriteUtil.writeValue(document, dataType, typeTree, value);
		variableType.appendChild(valueAttr);
		// Attributes of the Element (IsAbstract)
		Element isAbstractAttr = WriteUtil.writeIsAbstract(document, isAbstract);
		variableType.appendChild(isAbstractAttr);
	}

	/**
	 * Write Variable Node Attributes.
	 * 
	 * @param Document       Document to write.
	 * @param NodeType       Type of the Node.
	 * @param NodeId         Id of the Node.
	 * @param NodeClass      NodeClass of the Node.
	 * @param Browsename     BrowseName of the Node.
	 * @param Displayname    DisplayName of the Node.
	 * @param Description    Description of the Node.
	 * @param WriteMask      WriteMask of the Node.
	 * @param UserWriteMask  UserWriteMask of the Node.
	 * @param ReferenceNodes References of the Node.
	 * @param Attribute      Node specific Attributes, depends on its NodeType.
	 */
	private void writeVariable(Document document, NodeType nodeType, String nodeId, NodeClass nodeClass,
			QualifiedName browsename, LocalizedText displayname, LocalizedText description, String writeMask,
			String userWriteMask, ReferenceNodeStructure[] refNodes, VariableAttribute attribute) {
		Variant value = attribute.getValue();
		String dataType = attribute.getDataType();
		String valueRank = attribute.getValueRank();
		String arrayDimension = attribute.getArrayDimension();
		String accessLevel = attribute.getAccessLevel();
		String userAccessLevel = attribute.getUserAccessLevel();
		String minimumSamplingInterval = attribute.getMinimumSamplingInterval();
		String historizing = attribute.getHistorizing();
		// Node Element
		Element variable = WriteUtil.writeNodeHeader(document, nodeType);
		// write base attributes
		writeBaseNodeAttributes(document, variable, nodeType, nodeId, nodeClass, browsename, displayname, description,
				writeMask, userWriteMask, refNodes);
		// Attributes/DataType of the Element (Value)
		TypeTable typeTree = this.server.getServerInstance().getTypeTable();
		// this.server.getServerInstance().
		// Attributes/DataType of the Element (DataType)
		Element dataTypeAttr = WriteUtil.writeDataType(document, dataType);
		variable.appendChild(dataTypeAttr);
		// Attributes of the Element (ValueRank)
		Element valueRankAttr = WriteUtil.writeValueRank(document, valueRank);
		variable.appendChild(valueRankAttr);
		// Attributes of the Element (ArrayDimensions)
		Element arrayDimensionsAttr = WriteUtil.writeArrayDimension(document, arrayDimension);
		variable.appendChild(arrayDimensionsAttr);
		Element valueAttr = WriteUtil.writeValue(document, dataType, typeTree, value);
		variable.appendChild(valueAttr);
		// Attributes of the Element (AccessLevel)
		Element accessLevelAttr = WriteUtil.writeAccessLevel(document, accessLevel);
		variable.appendChild(accessLevelAttr);
		// Attributes of the Element (UserAccessLevel)
		Element userAccessLevelAttr = WriteUtil.writeUserAccessLevel(document, userAccessLevel);
		variable.appendChild(userAccessLevelAttr);
		// Attributes of the Element (MinimumSamplingInterval)
		Element minimumSamplingIntervalAttr = WriteUtil.writeMinimumSamplingInterval(document, minimumSamplingInterval);
		variable.appendChild(minimumSamplingIntervalAttr);
		// Attributes of the Element (Historizing)
		Element historizingAttr = WriteUtil.writeHistorizing(document, historizing);
		variable.appendChild(historizingAttr);
	}
}
