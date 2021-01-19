package com.bichler.opc.driver.xml_da.dp;

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
import com.bichler.opc.driver.xml_da.transform.XML_DA_MAPPING_TYPE;
import com.bichler.opc.driver.xml_da.transform.XML_DA_Transformation;

public class XML_DA_DPItem extends ComDP {
	// protected ICometLogger logger = CometLogger.getLogger();
	// public Logger getLogger() {
	// return this.manager.getLogger();
	// }
	// public void setLogger(ICometLogger logger) {
	// this.logger = logger;
	// }
	public Object[] getArray() {
		return array;
	}

	public void setArray(Object[] array) {
		this.array = array;
	}

	protected DataValue value = new DataValue();
	private int id = 0;
	private boolean active = true;
	private int cycletime = 1000;
	private String symbolname = "";
	private String displayname = "";
	protected XML_DA_DATATYPE dataType = XML_DA_DATATYPE.ANY;
	protected XML_DA_MAPPING_TYPE mapping = XML_DA_MAPPING_TYPE.SCALAR;
	protected String itemPath = "";
	protected String itemName = "";
	private String description = "";
	private boolean isFolder = false;
	private NodeId triggerNode = null;
	protected Object[] array = null;
	/**
	 * the length of an array for the first dimension
	 */
	protected int arraylength = 1;
	protected XML_DA_Transformation transform;
	/**
	 * the size of that element in byte
	 */
	protected int length = 0;
	private byte[] defaultSingleReadRequest = null;
	private byte[] defaultSingleWriteRequest = null;
	private long lastread;

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

	public int getCycletime() {
		return cycletime;
	}

	public void setCycletime(int cycletime) {
		this.cycletime = cycletime;
	}

	public String getSymbolname() {
		return symbolname;
	}

	public void setSymbolname(String symbolname) {
		this.symbolname = symbolname;
	}

	public String getDisplayname() {
		return displayname;
	}

	public void setDisplayname(String displayname) {
		this.displayname = displayname;
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

	public NodeId getTriggerNode() {
		return triggerNode;
	}

	public void setTriggerNode(NodeId triggerNode) {
		this.triggerNode = triggerNode;
	}

	public byte[] getDefaultSingleReadRequest() {
		return defaultSingleReadRequest;
	}

	public void setDefaultSingleReadRequest(byte[] defaultSingleReadRequest) {
		this.defaultSingleReadRequest = defaultSingleReadRequest;
	}

	public byte[] getDefaultSingleWriteRequest() {
		return defaultSingleWriteRequest;
	}

	public void setDefaultSingleWriteRequest(byte[] defaultSingleWriteRequest) {
		this.defaultSingleWriteRequest = defaultSingleWriteRequest;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public long getLastRead() {
		return this.lastread;
	}

	public void setLastRead(long lastread) {
		this.lastread = lastread;
	}

	public XML_DA_Transformation getTransform() {
		return transform;
	}

	public void setTransform(XML_DA_Transformation transform) {
		this.transform = transform;
	}

	@Override
	public DataValue drv2Prog(byte[] data) {
		if (this.transform == null)
			return null;
		this.value.setSourceTimestamp(new DateTime());
		return this.value;
	}

	public DataValue drv2Prog(Variant data) {
		if (this.transform == null)
			return null;
		this.value.setSourceTimestamp(new DateTime());
		return this.value;
	}

	/**
	 * 
	 * @param data
	 * @return
	 */
	public DataValue drv2Prog(Object data) {
		try {
			if (this.transform != null) {
				if (mapping == XML_DA_MAPPING_TYPE.SCALAR) {
					this.value.setValue(new Variant(this.transform.transToIntern(data)));
				} else if (mapping == XML_DA_MAPPING_TYPE.ARRAY_ARRAY) {
					Object[] values = (Object[]) data;
					// create array of intern elements
					// this.array
					// Object[] valtointern = createProgValueArray();
					for (int i = 0; i < values.length; i++) {
						this.array[i] = this.transform.transToIntern(values[i]);
					}
					this.value.setValue(new Variant(this.array));
				}
				this.value.setSourceTimestamp(new DateTime());
				this.value.setStatusCode(StatusCode.GOOD);
			}
		} catch (ClassCastException ex) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE,
					ex.getMessage() + " " + this.dpName/*
														 * , CometModuls.INT_DRV, ComDRV.BUNDLEID, ComDRV.VERSIONID
														 */);
		} catch (ValueOutOfRangeException ex) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE,
					ex.getMessage() + " " + this.dpName/*
														 * , CometModuls.INT_DRV, ComDRV.BUNDLEID, ComDRV.VERSIONID
														 */);
			this.value.setStatusCode(StatusCodes.Bad_OutOfRange);
		}
		return value;
	}

	protected Object createDRVValueArray() {
		return null;
	}

	@Override
	public byte[] prog2DRV(DataValue val) {
		if (this.transform == null)
			return null;
		this.transform.transToDevice(val.getValue());
		return null;
	}

	public Object prog2DRV(Variant val) {
		if (this.transform == null)
			return null;
		if (mapping == XML_DA_MAPPING_TYPE.SCALAR) {
			return this.transform.transToDevice(val.getValue());
		} else if (mapping == XML_DA_MAPPING_TYPE.ARRAY_ARRAY) {
			Object[] objects = (Object[]) val.getValue();
			// fill the whole array
			for (int i = 0; i < this.arraylength; i++) {
				this.array[i] = this.transform.transToDevice(objects[i]);
			}
			// now create the target specific array
			return this.createDRVValueArray();
		}
		return null;
	}

	public int getArraylength() {
		return arraylength;
	}

	public void setArraylength(int arraylength) {
		this.arraylength = arraylength;
	}

	public XML_DA_DATATYPE getDataType() {
		return dataType;
	}

	public void setDataType(XML_DA_DATATYPE dataType) {
		this.dataType = dataType;
	}

	public String getItemPath() {
		return itemPath;
	}

	public void setItemPath(String itemPath) {
		this.itemPath = itemPath;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	/**
	 * 
	 * @return
	 */
	public XML_DA_MAPPING_TYPE getMapping() {
		return mapping;
	}

	public void setMapping(XML_DA_MAPPING_TYPE mapping) {
		this.mapping = mapping;
	}

	public DataValue getValue() {
		return value;
	}

	public void setValue(DataValue value) {
		this.value = value;
	}
}
