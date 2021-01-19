package opc.sdk.server.service.node;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import opc.sdk.core.node.DefaultNodeFactory;
import opc.sdk.core.node.Node;
import opc.sdk.server.core.managers.OPCAddressSpaceManager;
import opc.sdk.server.core.managers.OPCHistoryManager;

import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.QualifiedName;
import org.opcfoundation.ua.builtintypes.UnsignedByte;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.core.IdType;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.core.ReferenceNode;

public class OPCServerNodeFactory extends DefaultNodeFactory {
	private OPCAddressSpaceManager addressSpaceManager;
	private OPCHistoryManager historyManager;

	public OPCServerNodeFactory(OPCAddressSpaceManager addressSpaceManager, OPCHistoryManager historyManager) {
		this.addressSpaceManager = addressSpaceManager;
		this.historyManager = historyManager;
	}

	@Override
	protected Map<IdType, List<? extends Object>> findNodeIds(int namespaceIndex) {
		Map<IdType, List<? extends Object>> gapes = super.findNodeIds(namespaceIndex);
		NodeId[] availableNodeIds = this.addressSpaceManager.getAllNodeIds(namespaceIndex);
		List<UnsignedInteger> l1 = new ArrayList<UnsignedInteger>();
		gapes.put(IdType.Numeric, l1);
		List<UUID> l2 = new ArrayList<UUID>();
		gapes.put(IdType.Guid, l2);
		List<byte[]> l3 = new ArrayList<byte[]>();
		gapes.put(IdType.Opaque, l3);
		List<String> l4 = new ArrayList<String>();
		gapes.put(IdType.String, l4);
		for (NodeId id : availableNodeIds) {
			switch (id.getIdType()) {
			case Numeric:
				l1.add((UnsignedInteger) id.getValue());
				break;
			case Guid:
				l2.add((UUID) id.getValue());
				break;
			case Opaque:
				l3.add((byte[]) id.getValue());
				break;
			case String:
				l4.add((String) id.getValue());
				break;
			}
		}
		return gapes;
	}

	@Override
	protected Node createVariableNode(NodeId nodeId, QualifiedName browseName, LocalizedText description,
			LocalizedText displayName, Object handle, NodeClass nodeClass, ReferenceNode[] references,
			UnsignedInteger userWriteMask, UnsignedInteger writeMask, Variant value, NodeId dataType, Integer valueRank,
			UnsignedInteger[] arrayDimensions, UnsignedByte accessLevel, UnsignedByte userAccessLevel,
			Double minimumSamplingInterval, Boolean historizing) {
		Node variableNode = new UAServerVariableNode(nodeId, nodeClass, browseName, displayName, description, writeMask,
				userWriteMask, references, value, dataType, valueRank, arrayDimensions, accessLevel, userAccessLevel,
				minimumSamplingInterval, historizing);
		// if(variableNode.getNodeId().getNamespaceIndex() != 0){
		// System.out.println(variableNode);
		// }
		if (this.historyManager != null) {
			((UAServerVariableNode) variableNode).setHistory(this.historyManager.getHistory());
		}
		return variableNode;
	}
}
