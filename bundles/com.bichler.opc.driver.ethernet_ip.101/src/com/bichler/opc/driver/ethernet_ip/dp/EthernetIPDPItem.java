package com.bichler.opc.driver.ethernet_ip.dp;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.DateTime;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.StatusCode;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.core.StatusCodes;

import com.bichler.opc.comdrv.ComDP;
import com.bichler.opc.comdrv.utils.ValueOutOfRangeException;
import com.bichler.opc.driver.ethernet_ip.transform.ETHERNETIP_MAPPING_TYPE;
import com.bichler.opc.driver.ethernet_ip.transform.EthernetIPTransformation;

import etherip.types.CIPData;

public class EthernetIPDPItem extends ComDP {
	private Logger logger = Logger.getLogger(getClass().getName());
	/**
	 * data value to write value from plc to internal opc ua
	 */
	DataValue olddv = new DataValue();

	public DataValue getOldDataValue() {
		return olddv;
	}

	public DataValue setOldDataValue(DataValue dv) {
		return olddv = dv;
	}

	@Override
	public void setAddress(String addr) {
		this.address = addr;
	}

	protected String displayName;
	private int id = 0;
	private boolean active = true;
	private long cycletime = 1000;
	private String tag = "";
	private String description = "";
	private int parentId = -1;
	private String triggerNode = null;
	/**
	 * the length of an array for the first dimension
	 */
	protected int arraylength = 1;
	protected EthernetIPTransformation transform;
	private long lastread;
	protected ETHERNETIP_MAPPING_TYPE mapping = ETHERNETIP_MAPPING_TYPE.SCALAR;

	public ETHERNETIP_MAPPING_TYPE getMapping() {
		return mapping;
	}

	public void setMapping(ETHERNETIP_MAPPING_TYPE mapping) {
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

	public String getTagname() {
		return tag;
	}

	public void setTagname(String tag) {
		this.tag = tag;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public long getLastRead() {
		return this.lastread;
	}

	public void setLastRead(long lastread) {
		this.lastread = lastread;
	}

	public EthernetIPTransformation getTransform() {
		return transform;
	}

	public void setTransform(EthernetIPTransformation transform) {
		this.transform = transform;
		if (this.transform != null) {
			// create internal array depending on transform
			this.array = transform.createInternArray(arraylength);
			if (array == null) {
				logger.log(Level.WARNING,
						"Can not create array for Node: " + this.nodeId + " Displayname: " + this.displayName);
			}
		}
	}

	public DataValue drv2Prog(CIPData data) {
		DataValue dv = new DataValue();
		// set timestamp that we did any work
		dv.setSourceTimestamp(new DateTime());
		try {
			if (this.transform == null) {
				logger.log(Level.WARNING,
						"Couldn't transform plc value to opc ua value, no transfomation found for node: " + this.nodeId
								+ " - " + this.displayName);
				dv.setStatusCode(StatusCodes.Bad_DataEncodingInvalid);
				return dv;
			}
			if (mapping == ETHERNETIP_MAPPING_TYPE.SCALAR) {
				if (data == null) {
					logger.log(Level.WARNING, "We did not get any data for Node: " + this.getNodeId() + " Displayname: "
							+ this.displayName);
				} else {
					// we have an scalar transformation
					Object obj = this.transform.transToIntern(data, 0);
					dv.setValue(new Variant(obj));
				}
			} else if (mapping == ETHERNETIP_MAPPING_TYPE.ARRAY_ARRAY) {
				this.array = this.transform.createInternArray(arraylength);
				// we have an array transformation
				for (int i = 0; i < this.arraylength; i++) {
					this.array[i] = this.transform.transToIntern(data, i);
				}
				dv.setValue(new Variant(this.array));
			}
			dv.setStatusCode(StatusCode.GOOD);
		} catch (ClassCastException ex) {
			logger.log(Level.SEVERE, "Couldn't cast Ethernet IP value to OPC UA Value! Node: " + this.displayName);
			dv.setStatusCode(StatusCodes.Bad_TypeMismatch);
		} catch (ValueOutOfRangeException e) {				
			logger.log(Level.SEVERE, e.getMessage() + " | Opc-Node: '" + this.nodeId + " - " + this.displayName + "' | AB-Tag: '"+ this.tag + "'");
			dv.setStatusCode(StatusCodes.Bad_OutOfRange);
			dv.setValue(this.transform.getDefaultValue());
		}
		return dv;
	}

	@Override
	public DataValue drv2Prog(byte[] data) {
		DataValue dv = new DataValue();
		// set timestamp that we did any work
		dv.setSourceTimestamp(new DateTime());
		return dv;
	}

	@Override
	public byte[] prog2DRV(DataValue val) throws ValueOutOfRangeException {
		return null;
	}

	public CIPData prog2DRV(Variant val) throws Exception {
		CIPData data = null;
		if (this.transform == null)
			return null;
		data = this.transform.createCipData(this.arraylength);
		if (mapping == ETHERNETIP_MAPPING_TYPE.SCALAR) {
			this.transform.transToDevice(data, val.getValue(), 0);
		} else if (mapping == ETHERNETIP_MAPPING_TYPE.ARRAY_ARRAY) {
			Object[] objects = (Object[]) val.getValue();
			// fill the whole array
			for (int i = 0; i < this.arraylength; i++) {
				this.transform.transToDevice(data, objects[i], i);
			}
		}
		return data;
	}

	protected byte[] createDRVValueArray() {
		return null;
	}

	public void setArraylength(int arraylength) {
		this.arraylength = arraylength;
		// this.internArray = new byte[arraylength][getLength()];
	}

	public int getArrayLength() {
		return this.arraylength;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * get byte count for read without message header
	 */
	public int getReadCount() {
		return 0;
	}

	public void fixArrayLength() {
		// do nothing, only in boolean we fix the length.
	}
}
