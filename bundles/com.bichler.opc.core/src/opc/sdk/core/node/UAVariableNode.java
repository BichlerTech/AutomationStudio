package opc.sdk.core.node;

import java.io.Serializable;

import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.DateTime;
import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.QualifiedName;
import org.opcfoundation.ua.builtintypes.ServiceResult;
import org.opcfoundation.ua.builtintypes.StatusCode;
import org.opcfoundation.ua.builtintypes.UnsignedByte;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.builtintypes.UnsignedShort;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.core.Attributes;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.core.ReferenceNode;

/**
 * Custom variable node.
 * 
 * @author Thomas Z&ouml;bauer
 * 
 */
public class UAVariableNode extends VariableNode implements Serializable {
	/**
	 * generated serial version uid
	 */
	private static final long serialVersionUID = -9209626292448910826L;
	/** Flag that indicates that the Node is used by a driver */
	/** normal version */
	private boolean driverConnected = true;
	private byte syncReadMask = 0;
	private byte syncWriteMask = 0;
	private StatusCode valueQuality;
	private DateTime valueSourceTS;
	private UnsignedShort valueSourcePS;
	private DateTime valueServerTS;
	// private UnsignedShort valueServerPS;
	/**
	 * Driver Connection IDs
	 */
	private long[] writeDrvIds = new long[0];

	/**
	 * UA Variable Node
	 */
	public UAVariableNode() {
		super();
	}

	/**
	 * UA Variable Node
	 * 
	 * @param NodeId
	 * @param NodeClass
	 * @param BrowseName
	 * @param DisplayName
	 * @param Description
	 * @param WriteMask
	 * @param UserWriteMask
	 * @param References
	 * @param Value
	 * @param DataType
	 * @param ValueRank
	 * @param ArrayDimensions
	 * @param AccessLevel
	 * @param UserAccessLevel
	 * @param MinimumSamplingInterval
	 * @param Historizing
	 */
	public UAVariableNode(NodeId NodeId, NodeClass NodeClass, QualifiedName BrowseName, LocalizedText DisplayName,
			LocalizedText Description, UnsignedInteger WriteMask, UnsignedInteger UserWriteMask,
			ReferenceNode[] References, Variant Value, NodeId DataType, Integer ValueRank,
			UnsignedInteger[] ArrayDimensions, UnsignedByte AccessLevel, UnsignedByte UserAccessLevel,
			Double MinimumSamplingInterval, Boolean Historizing) {
		super(NodeId, NodeClass, BrowseName, DisplayName, Description, WriteMask, UserWriteMask, References, Value,
				DataType, ValueRank, ArrayDimensions, AccessLevel, UserAccessLevel, MinimumSamplingInterval,
				Historizing);
	}

	public UAVariableNode(NodeId NodeId, NodeClass NodeClass, QualifiedName BrowseName, LocalizedText DisplayName,
			LocalizedText Description, UnsignedInteger WriteMask, UnsignedInteger UserWriteMask,
			ReferenceNode[] References, DataValue Value, NodeId DataType, Integer ValueRank,
			UnsignedInteger[] ArrayDimensions, UnsignedByte AccessLevel, UnsignedByte UserAccessLevel,
			Double MinimumSamplingInterval, Boolean Historizing) {
		super(NodeId, NodeClass, BrowseName, DisplayName, Description, WriteMask, UserWriteMask, References,
				(Value != null) ? Value.getValue() : Variant.NULL, DataType, ValueRank, ArrayDimensions, AccessLevel,
				UserAccessLevel, MinimumSamplingInterval, Historizing);
		// write value and timestamps
		writeValue(Attributes.Value, Value);
	}

	@Override
	public Variant read(UnsignedInteger attributeId, String[] locales) {
		if (attributeId.equals(Attributes.ArrayDimensions)) {
			return new Variant(getArrayDimensions());
		}
		return super.read(attributeId, locales);
	}

	@Override
	public ServiceResult readAttributeValue(UnsignedInteger attributeId, DataValue value, String[] locales) {
		if (Attributes.Value.equals(attributeId)) {
			// statuscode
			if (this.valueQuality != null) {
				value.setStatusCode(this.valueQuality);
			}
			// source timestamp
			if (this.valueSourceTS != null) {
				value.setSourceTimestamp(this.valueSourceTS);
			}
			// source picoseconds
			if (this.valueSourcePS != null) {
				value.setSourcePicoseconds(this.valueSourcePS);
			}
			// server timestamp
			if (this.valueServerTS != null) {
				value.setServerTimestamp(this.valueServerTS);
			}
			// else {
			// value.setServerTimestamp(DateTime.currentTime());
			// }
			// if (this.valueServerPS != null) {
			// value.setServerPicoseconds(this.valueServerPS);
			// }
		}
		return super.readAttributeValue(attributeId, value, locales);
	}

	public void setValueStatus(StatusCode code) {
		this.valueQuality = code;
	}

	public ServiceResult writeValue(UnsignedInteger attributeId, DataValue value) {
		if (Attributes.Value.equals(attributeId)) {
			// quality
			this.valueQuality = value.getStatusCode();
			// source
			if (value.getSourceTimestamp() != null) {
				this.valueSourceTS = value.getSourceTimestamp();
			} else {
				this.valueSourceTS = DateTime.currentTime();
			}
			// source picoseconds
			this.valueSourcePS = value.getSourcePicoseconds();
			// server timestamp
			if (value.getServerTimestamp() != null) {
				this.valueServerTS = value.getServerTimestamp();
			} else {
				this.valueServerTS = null;
			}
			// server picoseconds
			// this.valueServerPS = value.getServerPicoseconds();
		}
		return write(attributeId, value.getValue().getValue());
	}

	@Override
	public ServiceResult write(UnsignedInteger attributeId, Object value) {
		return super.write(attributeId, value);
	}

	@Override
	public boolean supportsAttribute(UnsignedInteger attributeId) {
		if (attributeId.equals(Attributes.ArrayDimensions)) {
			return true;
		}
		return super.supportsAttribute(attributeId);
	}

	/**
	 * Get the flag that marks this node to write to a driver.
	 * 
	 * @return TRUE if the node writes to a driver.
	 */
	public boolean getDriverConnected() {
		return this.driverConnected;
	}

	/**
	 * Setter to mark this node if it it forced to write to a driver.
	 * 
	 * @param Flag TRUE that the node writes to the driver, otherwise FALSE.
	 */
	public void setDriverConnected(boolean flag) {
		this.driverConnected = flag;
	}

	public byte getSyncReadMask() {
		return syncReadMask;
	}

	public void setSyncReadMask(byte syncReadMask) {
		this.syncReadMask = (byte) (this.syncReadMask | syncReadMask);
	}

	public byte getSyncWriteMask() {
		return syncWriteMask;
	}

	public void setSyncWriteMask(byte syncWriteMask) {
		this.syncWriteMask = (byte) (this.syncWriteMask | syncWriteMask);
	}

	public long[] getWriteDriverIds() {
		return this.writeDrvIds;
	}

	public void addWriteDriverId(long drvId) {
		long[] newDrvIds = new long[this.writeDrvIds.length + 1];
		boolean found = false;
		for (int i = 0; i < this.writeDrvIds.length; i++) {
			if (this.writeDrvIds[i] == drvId) {
				found = true;
				break;
			}
			newDrvIds[i] = this.writeDrvIds[i];
		}
		if (found) {
			return;
		}
		newDrvIds[this.writeDrvIds.length] = drvId;
		this.writeDrvIds = newDrvIds;
	}

	public void removeWriteDriverId(long drvId) {
		long[] newDrvIds = new long[this.writeDrvIds.length - 1];
		boolean found = false;
		int index = 0;
		for (int i = 0; i < this.writeDrvIds.length; i++) {
			if (this.writeDrvIds[i] == drvId) {
				found = true;
				continue;
			}
			// check if not found
			if (i >= newDrvIds.length && !found) {
				continue;
			}
			newDrvIds[index] = this.writeDrvIds[i];
			index++;
		}
		if (!found) {
			return;
		}
		// this.writeDrvIds = newDrvIds;
	}
}
