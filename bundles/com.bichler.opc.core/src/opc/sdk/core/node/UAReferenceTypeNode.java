package opc.sdk.core.node;

import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.QualifiedName;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.core.ReferenceNode;

/**
 * Custom referencetype node.
 * 
 * @author Thomas Z&ouml;bauer
 *
 */
public class UAReferenceTypeNode extends ReferenceTypeNode {
	public UAReferenceTypeNode() {
		super();
	}

	public UAReferenceTypeNode(NodeId nodeId, NodeClass nodeClass, QualifiedName browseName, LocalizedText displayName,
			LocalizedText description, UnsignedInteger writeMask, UnsignedInteger userWriteMask,
			ReferenceNode[] references, Boolean isAbstract, Boolean symmetric, LocalizedText inverseName) {
		super(nodeId, nodeClass, browseName, displayName, description, writeMask, userWriteMask, references, isAbstract,
				symmetric, inverseName);
	}
}
