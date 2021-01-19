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

import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.DateTime;
import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.QualifiedName;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.common.ServiceFaultException;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.BrowseDirection;
import org.opcfoundation.ua.core.BrowseResultMask;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.core.ReferenceDescription;

import com.bichler.astudio.opcua.components.ui.serverbrowser.providers.UAServerModelNode;


public class OPCRemoteTreeViewerItem implements Serializable,
	Externalizable {

	/**
	* Generated Serial Id
	*/
	private static final long serialVersionUID = -4072032806184092022L;

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
	
	private UAServerModelNode server = null;

	private List<OPCRemoteTreeViewerItem> children = null;
	
	public OPCRemoteTreeViewerItem() {

	}
	
	public OPCRemoteTreeViewerItem(String displayname) {
		this.displayName = displayname;
	}
	
	public void setChildren(List<OPCRemoteTreeViewerItem> children) {
		Map<NodeId, OPCRemoteTreeViewerItem> distinctItems = new HashMap<NodeId, OPCRemoteTreeViewerItem>();

		ListIterator<OPCRemoteTreeViewerItem> iterator = children.listIterator();

		if (children != null && !children.isEmpty()) {

			while (iterator.hasNext()) {
				OPCRemoteTreeViewerItem item = iterator.next();

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

	public List<OPCRemoteTreeViewerItem> getChildren() {
		return this.children;
	}
	
	public boolean hasChildren() {
		if (this.children != null && this.children.size() > 0) {
			return true;
		}
		
		ReferenceDescription[] result = null;
		try {
			
			result = server.getDevice().getUaclient().browse(server.getDevice().getUaclient().getActiveSession(),
					this.nodeId,
					BrowseDirection.Forward, true,
					NodeClass.getMask(NodeClass.ALL),
					Identifiers.HierarchicalReferences,
					BrowseResultMask.getMask(BrowseResultMask.ALL),
					new UnsignedInteger(), null, false).getReferences();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServiceFaultException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServiceResultException e) {
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

	public UAServerModelNode getServer() {
		return server;
	}

	public void setServer(UAServerModelNode server) {
		this.server = server;
	}
}
