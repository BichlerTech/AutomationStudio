package opc.sdk.core.node;

import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.DateTime;
import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.QualifiedName;
import org.opcfoundation.ua.builtintypes.ServiceResult;
import org.opcfoundation.ua.builtintypes.StatusCode;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.builtintypes.UnsignedShort;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.core.Attributes;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.core.ReferenceNode;

/**
 * Custom variabletype node.
 * 
 * @author Thomas Z&ouml;bauer
 * 
 */
public class UAVariableTypeNode extends VariableTypeNode {
	private StatusCode quality4value;
	private DateTime sourceTimeStamp4value;
	private UnsignedShort sourcePicoSeconds4value;

	public UAVariableTypeNode() {
		super();
	}

	public UAVariableTypeNode(NodeId NodeId, NodeClass NodeClass, QualifiedName BrowseName, LocalizedText DisplayName,
			LocalizedText Description, UnsignedInteger WriteMask, UnsignedInteger UserWriteMask,
			ReferenceNode[] References, Variant Value, NodeId DataType, Integer ValueRank,
			UnsignedInteger[] ArrayDimensions, Boolean IsAbstract) {
		super(NodeId, NodeClass, BrowseName, DisplayName, Description, WriteMask, UserWriteMask, References, Value,
				DataType, ValueRank, ArrayDimensions, IsAbstract);
	}

	public UAVariableTypeNode(NodeId NodeId, NodeClass NodeClass, QualifiedName BrowseName, LocalizedText DisplayName,
			LocalizedText Description, UnsignedInteger WriteMask, UnsignedInteger UserWriteMask,
			ReferenceNode[] References, DataValue Value, NodeId DataType, Integer ValueRank,
			UnsignedInteger[] ArrayDimensions, Boolean IsAbstract) {
		super(NodeId, NodeClass, BrowseName, DisplayName, Description, WriteMask, UserWriteMask, References,
				(Value != null) ? Value.getValue() : Variant.NULL, DataType, ValueRank, ArrayDimensions, IsAbstract);
		// write value and timestamps
		writeValue(Attributes.Value, Value);
	}

	@Override
	public ServiceResult readAttributeValue(UnsignedInteger attributeId, DataValue value, String[] locales) {
		if (Attributes.Value.equals(attributeId)) {
			if (this.quality4value == null) {
				this.quality4value = value.getStatusCode();
			}
			value.setStatusCode(this.quality4value);
			if (this.sourceTimeStamp4value == null) {
				this.sourceTimeStamp4value = value.getSourceTimestamp();
			}
			value.setSourceTimestamp(this.sourceTimeStamp4value);
			if (this.sourcePicoSeconds4value == null) {
				this.sourcePicoSeconds4value = value.getSourcePicoseconds();
			}
			value.setSourcePicoseconds(this.sourcePicoSeconds4value);
		}
		return super.readAttributeValue(attributeId, value, locales);
	}

	public ServiceResult writeValue(UnsignedInteger attributeId, DataValue value) {
		if (Attributes.Value.equals(attributeId)) {
			this.quality4value = value.getStatusCode();
			if (value.getSourceTimestamp() != null) {
				this.sourceTimeStamp4value = value.getSourceTimestamp();
			} else {
				this.sourceTimeStamp4value = DateTime.currentTime();
			}
			this.sourcePicoSeconds4value = value.getSourcePicoseconds();
		}
		return write(attributeId, value.getValue().getValue());
	}

	@Override
	public ServiceResult write(UnsignedInteger attributeId, Object value) {
		return super.write(attributeId, value);
	}
}
