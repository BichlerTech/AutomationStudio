package opc.sdk.core.node;

import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.QualifiedName;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.core.ReferenceNode;

/**
 * Custom datatype node.
 * 
 * @author Thomas Z&ouml;bauer
 * 
 */
public class UADataTypeNode extends DataTypeNode {
	public UADataTypeNode() {
		super();
	}

	public UADataTypeNode(NodeId NodeId, NodeClass NodeClass, QualifiedName BrowseName, LocalizedText DisplayName,
			LocalizedText Description, UnsignedInteger WriteMask, UnsignedInteger UserWriteMask,
			ReferenceNode[] References, Boolean IsAbstract) {
		super(NodeId, NodeClass, BrowseName, DisplayName, Description, WriteMask, UserWriteMask, References,
				IsAbstract);
	}
}
