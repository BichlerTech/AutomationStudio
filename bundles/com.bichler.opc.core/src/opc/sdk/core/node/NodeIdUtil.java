package opc.sdk.core.node;

import java.util.UUID;

import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.StatusCode;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.common.NamespaceTable;
import org.opcfoundation.ua.core.IdType;
import org.opcfoundation.ua.core.StatusCodes;

/**
 * Util to create nodeids.
 * 
 * @author Thomas Z&ouml;bauer
 * 
 */
public class NodeIdUtil {
	public static NodeId create(Object identifier, String namespaceUri, NamespaceTable namespaceUris) {
		int index = -1;
		if (namespaceUris != null) {
			index = namespaceUris.getIndex(namespaceUri);
		}
		if (index < 0) {
			// throw new ServiceResultException(StatusCodes.Bad_NodeIdInvalid,
			// "NamespaceUri "+ namespaceUri +" is not in the namespace table."
			// );
		}
		return createNodeId(index, identifier);
	}

	/**
	 * Creates a new NodeId
	 * 
	 * @param index
	 * @param identifier
	 * @return
	 */
	public static NodeId createNodeId(int index, Object identifier) {
		if (identifier instanceof Integer) {
			return new NodeId(index, (Integer) identifier);
		} else if (identifier instanceof String) {
			return new NodeId(index, (String) identifier);
		} else if (identifier instanceof UnsignedInteger) {
			return new NodeId(index, (UnsignedInteger) identifier);
		} else if (identifier instanceof byte[]) {
			return new NodeId(index, (byte[]) identifier);
		} else if (identifier instanceof UUID) {
			return new NodeId(index, (UUID) identifier);
		}
		return NodeId.NULL;
	}

	public static Object createIdentifierFromString(String identifierString) {
		Object identifier = null;
		boolean error = false;
		// is unsigned integer
		try {
			identifier = new UnsignedInteger(identifierString);
		} catch (NumberFormatException e) {
			error = true;
		}
		// let it as string
		if (error) {
			identifier = identifierString;
		}
		return identifier;
	}

	public static StatusCode validate(NodeId nodeId) {
		if (NodeId.isNull(nodeId)) {
			return new StatusCode(StatusCodes.Bad_NodeIdInvalid);
		}
		if (IdType.String.equals(nodeId.getIdType())) {
			String value = (String) nodeId.getValue();
			if (value.isEmpty()) {
				return new StatusCode(StatusCodes.Bad_NodeIdInvalid);
			}
		}
		/*
		 * else if (IdType.Guid.equals(nodeId.getIdType())) { UUID value = (UUID)
		 * nodeId.getValue(); return; }
		 */else if (IdType.Numeric.equals(nodeId.getIdType())) {
			UnsignedInteger value = (UnsignedInteger) nodeId.getValue();
			if (value.longValue() <= 0) {
				return new StatusCode(StatusCodes.Bad_NodeIdInvalid);
			}
		}
		return StatusCode.GOOD;
	}
}
