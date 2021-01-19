package com.bichler.astudio.opcua.components.ui.dialogs;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import opc.sdk.core.node.Node;
import opc.sdk.core.node.NodeIdUtil;
import opc.sdk.server.core.OPCInternalServer;

import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.DateTime;
import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.QualifiedName;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.NodeClass;

public class OPCTreeViewerItem implements Serializable,
	Externalizable {

	private String			serverName 		= "";
	private QualifiedName 	browseName 		= null;
	private String 			browsePath 		= "";
	private NodeId 			nodeId 			= null;
	private NodeId 			typeDefinition 	= null;
	private String 	displayName 	= null;
	private NodeClass 		nodeClass 		= null;
	private NodeId 			referencetypeId;

	private Node 			node 			= null;
	private Object 			value 			= null;
	private DateTime 		lastTimestampe 	= null;
	private ExpandedNodeId parentId = null;
	
	private OPCInternalServer server = null;

	public OPCInternalServer getServer() {
		return server;
	}

	public void setServer(OPCInternalServer server) {
		this.server = server;
	}

	private List<OPCTreeViewerItem> children = null;
	
	public OPCTreeViewerItem() {

	}
	
	public OPCTreeViewerItem(String displayname) {
		this.displayName = displayname;
	}
	
	public void setChildren(List<OPCTreeViewerItem> children) {
		Map<NodeId, OPCTreeViewerItem> distinctItems = new HashMap<NodeId, OPCTreeViewerItem>();

		ListIterator<OPCTreeViewerItem> iterator = children.listIterator();

		if (children != null && !children.isEmpty()) {

			while (iterator.hasNext()) {
				OPCTreeViewerItem item = iterator.next();

				NodeId id = item.getNodeId();

				if (Identifiers.ObjectNode.equals(id)) {
					continue;
				} else if (Identifiers.VariableNode.equals(id)) {
					continue;
				} else if (Identifiers.ObjectTypeNode.equals(id)) {
					continue;
				} else if (Identifiers.VariableTypeNode.equals(id)) {
					continue;
				} else if (Identifiers.MethodNode.equals(id)) {
					continue;
				} else if (Identifiers.ReferenceTypeNode.equals(id)) {
					continue;
				}

				if (!NodeId.isNull(id) && !distinctItems.containsKey(id)) {
					distinctItems.put(id, item);
				}
				iterator.remove();
			}
			children.addAll(distinctItems.values());
		}

		this.children = children;
	}

	public List<OPCTreeViewerItem> getChildren() {
		return this.children;
	}
	
	public boolean hasChildren() {
		if (this.children != null && this.children.size() > 0) {
			return true;
		}
		
		Node[] result = null;
		try {
			
			result = server.getAddressSpaceManager().findChildren(this.nodeId);
			
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

		if(result == null) {
			return false;
		}
		return (result.length > 0) ? true : false;
	}

	public String getDisplayName() {
		return this.displayName;
	}

	public void setDisplayName(String displayname) {
		this.displayName = displayname;
	}

	public NodeId getNodeId() {
		return nodeId;
	}

	public void setNodeId(NodeId nodeId) {
		this.nodeId = nodeId;
	}

	public NodeId getTypeDefinition() {
		return typeDefinition;
	}

	public void setTypeDefinition(NodeId typeDefinition) {
		this.typeDefinition = typeDefinition;
	}

	public NodeClass getNodeClass() {
		return nodeClass;
	}

	public void setNodeClass(NodeClass nodeClass) {
		this.nodeClass = nodeClass;
	}

	public void setBrowseName(QualifiedName browseName) {
		this.browseName = browseName;
	}

	public QualifiedName getBrowseName() {
		return this.browseName;
	}

	public NodeId getReferenceTypeId() {
		return this.referencetypeId;
	}

	public void setReferenceTypeId(NodeId referenceTypeId) {
		this.referencetypeId = referenceTypeId;
	}

	public void setNode(Node node) {
		this.node = node;
	}

	public Node getNode() {
		return this.node;
	}

	public Object getValue() {
		return this.value ;
	}

	public DateTime getTimestamp() {
		return this.lastTimestampe;
	}

	public void setValue(DataValue currentValue) {
		try{
		this.value = currentValue.getValue().getValue();
		}catch(NullPointerException npe){
			
		}
	}

	public void setTimestamp(DateTime sourceTimestamp) {
		this.lastTimestampe = sourceTimestamp;
	}
	
	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		/** Browsename */
		this.browseName = new QualifiedName(in.readInt(), in.readUTF());
		/** Displayname */
		this.displayName = in.readUTF();
		/** NodeId */
		this.nodeId = NodeIdUtil.createNodeId(in.readInt(), in.readObject());
		/** Nodeclass */
		int nodeClassValue = in.readInt();

		for (NodeClass n : NodeClass.ALL) {
			if (n.getValue() == nodeClassValue) {
				this.nodeClass = n;
				break;
			}
		}
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		/** QualifiedName Browsename */
		out.writeInt(this.browseName.getNamespaceIndex());
		out.writeUTF(this.browseName.getName());
		/** DisplayName */
		out.writeUTF(this.displayName);
		//if(this.displayName != null){
		// out.writeUTF(this.displayName.getLocaleId());
		//}
		//else{
		//	out.writeUTF("");
		//}
		/** NodeId */
		out.writeInt(this.nodeId.getNamespaceIndex());
		out.writeObject(this.nodeId.getValue());
		/** NodeClass */
		out.writeInt(this.nodeClass.getValue());
	}

	public String getBrowsePath() {
		return browsePath;
	}

	public void setBrowsePath(String browsePath) {
		this.browsePath = browsePath;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public ExpandedNodeId getParentId() {
		return parentId;
	}

	public void setParentId(ExpandedNodeId parentId) {
		this.parentId = parentId;
	}
}
