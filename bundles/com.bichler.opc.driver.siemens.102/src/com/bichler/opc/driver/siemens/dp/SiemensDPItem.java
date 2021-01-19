package com.bichler.opc.driver.siemens.dp;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.DateTime;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.StatusCode;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.core.StatusCodes;

import com.bichler.opc.comdrv.ComDP;
import com.bichler.opc.comdrv.utils.ComByteMessage;
import com.bichler.opc.comdrv.utils.ValueOutOfRangeException;
import com.bichler.opc.driver.siemens.communication.SiemensAreaCode;
import com.bichler.opc.driver.siemens.communication.SiemensDataKind;
import com.bichler.opc.driver.siemens.communication.SiemensDataType;
import com.bichler.opc.driver.siemens.transform.SIEMENS_MAPPING_TYPE;
import com.bichler.opc.driver.siemens.transform.SiemensTransformation;

public class SiemensDPItem extends ComDP {
	protected int maxReadByteCount = 0;
	protected int maxWriteByteCount = 0;
	/**
	 * data value to write value from plc cto internal opc ua
	 */
	protected DataValue olddv = new DataValue();
	protected String displayName;
	private int id = 0;
	protected byte[][] internArray = null;
	private boolean active = true;
	private long cycletime = 1000;
	private String symbolname = "";
	protected SiemensAreaCode addressType = SiemensAreaCode.UNKNOWN;
	protected SiemensDataType dataType = SiemensDataType.UNKNOWN;
	protected SiemensDataKind dataKind = SiemensDataKind.UNKNOWN;
	protected int address = 0;
	protected float index = 0;
	private String description = "";
	private boolean isFolder = false;
	private int parentId = -1;
	private String triggerNode = null;
	/**
	 * the length of an array for the first dimension
	 */
	protected int arraylength = 1;
	protected SiemensTransformation transform;
	/**
	 * the size of that element in byte
	 */
	protected int length = 0;
	// private byte[] defaultSingleReadRequest = null;
	// private byte[] defaultSingleWriteRequest = null;
	// private long lastread;
	protected SIEMENS_MAPPING_TYPE mapping = SIEMENS_MAPPING_TYPE.SCALAR;

	public SiemensDPItem clone(SiemensDPItem clone) {
		if (clone == null)
			clone = new SiemensDPItem();
		clone.setMaxReadByteCount(maxReadByteCount);
		clone.setMaxWriteByteCount(maxWriteByteCount);
		clone.setDisplayName(displayName);
		clone.setId(id);
		clone.setActive(active);
		clone.setCycletime(cycletime);
		clone.setSymbolname(symbolname);
		clone.setAddressType(addressType);
		clone.setDataType(dataType);
		clone.setDataKind(dataKind);
		clone.setAddress(address + "");
		clone.setIndex(index);
		clone.setDescription(description);
		clone.setFolder(isFolder);
		clone.setParentId(parentId);
		clone.setTriggerNode(triggerNode);
		clone.setArraylength(arraylength);
		clone.setTransform(transform);
		clone.setLength(length);
		clone.setMapping(mapping);
		clone.setManager(manager);
		return clone;
	}

	public int getMaxReadByteCount() {
		return maxReadByteCount;
	}

	public int getMaxWriteByteCount() {
		return maxWriteByteCount;
	}

	public void setMaxReadByteCount(int maxReadByteCount) {
		this.maxReadByteCount = maxReadByteCount;
	}

	// public DataValue getDataValue() {
	// return dv;
	// }
	public DataValue getOldDataValue() {
		return olddv;
	}

	// public DataValue setDataValue(DataValue dv) {
	// return this.dv = dv;
	// }
	public DataValue setOldDataValue(DataValue dv) {
		return olddv = dv;
	}

	@Override
	public void setAddress(String addr) {
		try {
			this.address = Integer.parseInt(addr);
		} catch (NumberFormatException ex) {
			// TODO log db address wrong format
			ex.printStackTrace();
		}
	}

	public void setDPAddress(int addr) {
		this.address = addr;
	}

	public int getDBAddress() {
		return this.address;
	}

	public SIEMENS_MAPPING_TYPE getMapping() {
		return mapping;
	}

	public void setMapping(SIEMENS_MAPPING_TYPE mapping) {
		this.mapping = mapping;
	}

	protected Object[] array = null;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public long getCycletime() {
		return cycletime;
	}

	public void setCycletime(long cycletime) {
		this.cycletime = cycletime;
	}

	public String getSymbolname() {
		return symbolname;
	}

	public void setSymbolname(String symbolname) {
		this.symbolname = symbolname;
	}

	public SiemensAreaCode getAddressType() {
		return addressType;
	}

	public void setAddressType(SiemensAreaCode addressType) {
		this.addressType = addressType;
	}

	public SiemensDataType getDataType() {
		return dataType;
	}

	public void setDataType(SiemensDataType dataType) {
		this.dataType = dataType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isFolder() {
		return isFolder;
	}

	public void setFolder(boolean isFolder) {
		this.isFolder = isFolder;
	}

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	/**
	 * public boolean isHistorical() { return historical; } public void
	 * setHistorical(boolean historical) { this.historical = historical; }
	 */
	public NodeId getNodeId() {
		return nodeId;
	}

	public void setNodeId(NodeId nodeId) {
		this.nodeId = nodeId;
	}

	public String getTriggerNode() {
		return triggerNode;
	}

	public void setTriggerNode(String triggerNode) {
		this.triggerNode = triggerNode;
	}

	// public byte[] getDefaultSingleReadRequest() {
	// return defaultSingleReadRequest;
	// }
	//
	// public void setDefaultSingleReadRequest(byte[] defaultSingleReadRequest) {
	// this.defaultSingleReadRequest = defaultSingleReadRequest;
	// }
	//
	// public byte[] getDefaultSingleWriteRequest() {
	// return defaultSingleWriteRequest;
	// }
	//
	// public void setDefaultSingleWriteRequest(byte[] defaultSingleWriteRequest)
	// {
	// this.defaultSingleWriteRequest = defaultSingleWriteRequest;
	// }
	public int getLength() {
		return length;
	}

	public int getLengthInBit() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public float getIndex() {
		return index;
	}

	public int getReadIndexInBit(int index2) {
		return 0;
	}

	public int getWriteIndexInBit(int index2) {
		return 0;
	}

	public void setIndex(float index) {
		this.index = index;
	}

	// public long getLastRead() {
	// return this.lastread;
	// }
	//
	// public void setLastRead(long lastread) {
	// this.lastread = lastread;
	// }
	public SiemensTransformation getTransform() {
		return transform;
	}

	public void setTransform(SiemensTransformation transform) {
		this.transform = transform;
		if (this.transform != null) {
			// create internal array depending on transform
			this.array = transform.createInternArray(arraylength);
			if (array == null) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE,
						"Can not create array for Node: " + this.nodeId + " Displayname: " + this.displayName);
			}
		}
	}

	@Override
	public DataValue drv2Prog(byte[] data) {
		DataValue dv = new DataValue();
		// set timestamp that we did any work
		dv.setSourceTimestamp(new DateTime());
		try {
			if (this.transform == null) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE,
						"Couldn't transform plc value to opc ua value, no transfomation found for node: " + this.nodeId
								+ " - " + this.displayName);
				dv.setStatusCode(StatusCodes.Bad_DataEncodingInvalid);
				return dv;
			}
			/**
			 * now create message to delete read bytes
			 */
			ComByteMessage message = new ComByteMessage();
			message.setBuffer(data);
			if (mapping == SIEMENS_MAPPING_TYPE.SCALAR) {
				if (message.getBuffer().length == 0) {
					Logger.getLogger(getClass().getName()).log(Level.SEVERE, "We did not get any data for Node: "
							+ this.getNodeId() + " Displayname: " + this.displayName);
				} else {
					// we have an scalar transformation
					Object obj = this.transform.transToIntern(message);
					dv.setValue(new Variant(obj));
				}
			} else if (mapping == SIEMENS_MAPPING_TYPE.ARRAY_ARRAY) {
				this.array = this.transform.createInternArray(arraylength);
				// we have an array transformation
				for (int i = 0; i < this.arraylength; i++) {
					this.array[i] = this.transform.transToIntern(message);
				}
				dv.setValue(new Variant(this.array));
			}
			dv.setStatusCode(StatusCode.GOOD);
		} catch (ClassCastException ex) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE,
					"Couldn't cast Siemens value to OPC UA Value! Node: " + this.displayName);
			dv.setStatusCode(StatusCodes.Bad_TypeMismatch);
		} catch (ValueOutOfRangeException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage() + " Node: " + this.displayName);
		}
		return dv;
	}

	@Override
	public byte[] prog2DRV(DataValue val) throws ValueOutOfRangeException {
		if (this.transform == null)
			return null;
		if (mapping == SIEMENS_MAPPING_TYPE.SCALAR) {
			byte[] ret = this.transform.transToDevice(val.getValue().getValue());
			return ret;
		} else if (mapping == SIEMENS_MAPPING_TYPE.ARRAY_ARRAY) {
			Object[] objects = (Object[]) val.getValue().getValue();
			// fill the whole array
			for (int i = 0; i < this.arraylength; i++) {
				this.internArray[i] = this.transform.transToDevice(objects[i]);
			}
			// now create the target specific array
			// val.setStatusCode(this.transform.getStatusCode());
			return this.createDRVValueArray();
		}
		return null;
	}

	protected byte[] createDRVValueArray() {
		return null;
	}

	/**
	 * gets the length of the array, if i > -1, we got a request from create message
	 * 
	 * @param i
	 * @return
	 */
	public int getReadArraylength(int i) {
		if (i <= -1)
			return this.arraylength;
		// the array length is limited by maxBytecount
		if (maxReadByteCount * (i + 1) < this.arraylength * getLength()) {
			return maxReadByteCount / getLength();
		} else {
			return this.arraylength - maxReadByteCount / getLength() * i;
		}
	}

	/**
	 * gets the length of the array, if i > -1, we got a request from create message
	 * 
	 * @param i
	 * @return
	 */
	public int getWriteArraylength(int i) {
		if (i <= -1)
			return this.arraylength;
		// the array length is limited by maxBytecount
		if (maxWriteByteCount * (i + 1) < this.arraylength * getLength()) {
			return maxWriteByteCount / getLength();
		} else {
			return this.arraylength - maxWriteByteCount / getLength() * i;
		}
	}

	/**
	 * gets the length of the array, if i > -1, we got a request from create message
	 * 
	 * @param i
	 * @return
	 */
	// public int getWriteArraylengthinByte(int i) {
	// if (i <= -1)
	// return this.arraylength;
	//
	// // the array length is limited by maxBytecount
	// if (maxWriteByteCount * (i + 1) < this.arraylength * getLength()) {
	// return maxWriteByteCount / getLength();
	// } else {
	// return this.arraylength - maxWriteByteCount / getLength() * i;
	// }
	// }
	public void setArraylength(int arraylength) {
		this.arraylength = arraylength;
		this.internArray = new byte[arraylength][getLength()];
	}

	public SiemensDataKind getDataKind() {
		return dataKind;
	}

	public void setDataKind(SiemensDataKind dataKind) {
		this.dataKind = dataKind;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * get count how many messages will be requried to read the whole array
	 * 
	 * @return
	 */
	public int getReadMessageCount() {
		return this.arraylength * this.getLength() / maxReadByteCount + 1;
	}

	/**
	 * get byte count for read without message header
	 */
	public int getReadCount() {
		return 0;
	}

	public void setMaxWriteByteCount(int maxByteCount) {
		this.maxWriteByteCount = maxByteCount;
	}
}
