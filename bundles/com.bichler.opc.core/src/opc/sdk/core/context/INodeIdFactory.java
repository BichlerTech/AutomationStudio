package opc.sdk.core.context;

import opc.sdk.core.node.Node;

import org.opcfoundation.ua.builtintypes.NodeId;

/**
 * Interface of a nodeid factory.
 * 
 * @author Thomas Z&ouml;bauer
 * 
 */
public interface INodeIdFactory {
	/**
	 * Creates a new NodeId for the specified node.
	 * 
	 * @param context
	 * @param node
	 * @return
	 */
	NodeId newNodeId(Node node);
}
