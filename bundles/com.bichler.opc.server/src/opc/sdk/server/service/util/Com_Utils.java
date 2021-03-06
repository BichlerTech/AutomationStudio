package opc.sdk.server.service.util;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.logging.Level;
import java.util.logging.Logger;

import opc.sdk.core.node.Node;
// import opc.sdk.server.core.OPCInternalServer;
import opc.sdk.server.core.OPCServer;

import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.BrowseDescription;
import org.opcfoundation.ua.core.BrowseDirection;
import org.opcfoundation.ua.core.BrowseResult;
import org.opcfoundation.ua.core.BrowseResultMask;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.NodeClass;

public class Com_Utils {
	/**
	 * get the whole browsepath from root to the required node, browse will only be
	 * performed on local address space, no connection to client required
	 * 
	 * @param id
	 * @param opc
	 * @param endnode
	 * @return
	 */
	public static Deque<String> getFullBrowsePath(NodeId id, OPCServer opc, NodeId endnode) {
		Deque<String> queue = new ArrayDeque<String>();
		if (id == null) {
			return queue;
		}
		// BrowsePathElement element = null;
		NodeId baseTypeDef = null;
		Node value = opc.getAddressSpaceManager().getNodeById(id);
		if (value == null) {
			// we couldn't find the selected node
			return queue;
		}
		// element = new BrowsePathElement();
		// if (value != null) {
		// element.setDisplayname(value.getDisplayName());
		// element.setBrowsename(value.getBrowseName());
		// }
		//
		// element.setId(id);
		// queue.add(element);
		while (!id.equals(endnode)) {
			BrowseDescription[] nodesToBrowse = { new BrowseDescription(id, BrowseDirection.Inverse,
					Identifiers.HierarchicalReferences, true, NodeClass.getMask(NodeClass.ALL),
					BrowseResultMask.getMask(Arrays.asList(BrowseResultMask.values()))) };
			try {
				BrowseResult references = opc.getAddressSpaceManager().getServer().getMaster().browse(nodesToBrowse,
						UnsignedInteger.ZERO, null, null)[0];
				if (references != null && references.getReferences() != null && references.getReferences().length > 0) {
					if (references.getReferences()[0] != null) {
						baseTypeDef = opc.getNamespaceUris().toNodeId(references.getReferences()[0].getNodeId());
						Node node = opc.getAddressSpaceManager().getNodeById(baseTypeDef);
						// element = new BrowsePathElement();
						// element.setDisplayname(node.getDisplayName());
						// element.setBrowsename(node.getBrowseName());
						// element.setId(baseTypeDef);
						queue.addFirst(node.getBrowseName().getName());
						id = baseTypeDef;
					}
				}
			} catch (ServiceResultException e) {
				Logger.getLogger(Com_Utils.class.getName()).log(Level.SEVERE, e.getMessage(), e);
			}
		}
		return queue;
	}
}
