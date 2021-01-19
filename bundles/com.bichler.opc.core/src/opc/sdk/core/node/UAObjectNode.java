package opc.sdk.core.node;

import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.QualifiedName;
import org.opcfoundation.ua.builtintypes.UnsignedByte;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.core.ReferenceNode;

/**
 * Custom object node.
 * 
 * @author Thomas Z&ouml;bauer
 * 
 */
public class UAObjectNode extends ObjectNode {
	public UAObjectNode() {
		super();
	}

	public UAObjectNode(NodeId NodeId, NodeClass NodeClass, QualifiedName BrowseName, LocalizedText DisplayName,
			LocalizedText Description, UnsignedInteger WriteMask, UnsignedInteger UserWriteMask,
			ReferenceNode[] References, UnsignedByte EventNotifier) {
		super(NodeId, NodeClass, BrowseName, DisplayName, Description, WriteMask, UserWriteMask, References,
				EventNotifier);
	}

	/**
	 * Its just a placeholder
	 */
	public void reportEvent() {
	}
}
