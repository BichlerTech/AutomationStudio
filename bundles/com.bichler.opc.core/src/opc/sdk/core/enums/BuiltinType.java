package opc.sdk.core.enums;

import org.opcfoundation.ua.builtintypes.NodeId;

/**
 * Enum of OPC UA BuiltinTypes.
 * 
 * @author Thomas Z&ouml;bauer
 * 
 */
public enum BuiltinType {
	Null(0), Boolean(1), SByte(2), Byte(3), Int16(4), UInt16(5), Int32(6), UInt32(7), Int64(8), UInt64(9), Float(10),
	Double(11), String(12), DateTime(13), Guid(14), ByteString(15), XmlElement(16), NodeId(17), ExpandedNodeId(18),
	StatusCode(19), QualifiedName(20), LocalizedText(21), ExtensionObject(22), DataValue(23), Variant(24),
	DiagnosticInfo(25), Number(26), Integer(27), UInteger(28), Enumeration(29);

	int value = -1;

	private BuiltinType(int value) {
		this.value = value;
	}

	public int getValue() {
		return this.value;
	}

	/**
	 * Get the id for the type. Namespaceindex is 0.
	 * 
	 * @return id of the buildin type
	 */
	public NodeId getBuildinTypeId() {
		return new NodeId(0, this.value);
	}

	public static BuiltinType getType(NodeId result) {
		BuiltinType builtInType = null;
		for (int ii = 0; ii < values().length; ii++) {
			if (values()[ii].getBuildinTypeId().equals(result)) {
				builtInType = values()[ii];
				break;
			}
			// if (values()[ii].getValue() == ((UnsignedInteger)
			// result.getValue())
			// .intValue()) {
			// builtInType = values()[ii];
			// break;
			// }
		}
		return builtInType;
	}
}
