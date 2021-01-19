package com.hbsoft.studio.editor.events.xml;

import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.DateTime;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.StatusCode;
import org.opcfoundation.ua.builtintypes.Variant;

import com.hbsoft.comdrv.utils.ValueOutOfRangeException;
import com.hbsoft.studio.opcua.properties.driver.IDriverNode;

public class EventDpSyntax extends AbstractEventModel implements IDriverNode {

	// private NodeId sourceId = NodeId.NULL;
	private String operation = "Change";
	private NodeId variable_source = NodeId.NULL;
	// private String displayname = "";

	private DataValue value = new DataValue();
	private String message = "";

	private EventReference reference = EventReference.Beginn;

	// protected byte[] intern = null;
	// private int id = 0;

	private boolean active = true;
	// private int cycletime = 1000;
	// private String symbolname = "";
	// private String browsename = "";
	// protected XML_DA_DATATYPE dataType = XML_DA_DATATYPE.ANY;
	// protected EVENTS_MAPPING_TYPE mapping = EVENTS_MAPPING_TYPE.SCALAR;
	// protected String resourceName = "";
	// protected String varName = "";
	// private String description = "";
	// private boolean isFolder = false;
	// private NodeId triggerNode = null;

	protected Object[] array = null;

	/**
	 * the length of an array for the first dimension
	 */
	protected int arraylength = 1;

	// protected XML_DA_Transformation transform;

	/**
	 * the size of that element in byte
	 */
	protected int length = 0;

	private byte[] defaultSingleReadRequest = null;
	private byte[] defaultSingleWriteRequest = null;
	private long lastread;

	private EventDpItem parent;

	// public int getId() {
	// return id;
	// }

	// public void setId(int id) {
	// this.id = id;
	// }

	public EventDpSyntax() {
		super();
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	// public int getCycletime() {
	// return cycletime;
	// }
	//
	// public void setCycletime(int cycletime) {
	// this.cycletime = cycletime;
	// }

	// public String getSymbolname() {
	// return symbolname;
	// }
	//
	// public void setSymbolname(String symbolname) {
	// this.symbolname = symbolname;
	// }
	//
	// public String getBrowsename() {
	// return browsename;
	// }
	//
	// public void setBrowsename(String browsename) {
	// this.browsename = browsename;
	// }
	//
	// public String getDescription() {
	// return description;
	// }
	//
	// public void setDescription(String description) {
	// this.description = description;
	// }
	//
	// public boolean isFolder() {
	// return isFolder;
	// }
	//
	// public void setFolder(boolean isFolder) {
	// this.isFolder = isFolder;
	// }

	/**
	 * public boolean isHistorical() { return historical; } public void
	 * setHistorical(boolean historical) { this.historical = historical; }
	 */
	// public NodeId getNodeId() {
	// return nodeId;
	// }

	// public void setNodeId(NodeId nodeId) {
	// this.nodeId = nodeId;
	// }

	// public NodeId getTriggerNode() {
	// return triggerNode;
	// }
	//
	// public void setTriggerNode(NodeId triggerNode) {
	// this.triggerNode = triggerNode;
	// }

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

	public DataValue drv2Prog(byte[] data) {
		// if (this.transform == null)
		// return null;
		this.value.setSourceTimestamp(new DateTime());
		return this.value;
	}

	public DataValue drv2Prog(Variant data) {
		// if (this.transform == null)
		// return null;
		this.value.setSourceTimestamp(new DateTime());
		return this.value;
	}

	/**
	 * 
	 * @param data
	 * @return
	 * @throws ValueOutOfRangeException
	 * @throws ClassCastException
	 */
	public DataValue drv2Prog(Object data) throws ClassCastException,
			ValueOutOfRangeException {
		// if (this.transform != null) {
		// if (mapping == EVENTS_MAPPING_TYPE.SCALAR) {
		// this.value.setValue(new Variant(this.transform
		// .transToIntern(data)));
		// } else if (mapping == EVENTS_MAPPING_TYPE.ARRAY_ARRAY) {
		// Object[] values = (Object[]) data;
		// create array of intern elements
		// Object[] valtointern = createProgValueArray();
		// for (int i = 0; i < values.length; i++) {
		// valtointern[i] = this.transform.transToIntern(values[i]);
		// }
		// this.value.setValue(new Variant(valtointern));
		// }
		this.value.setSourceTimestamp(new DateTime());
		this.value.setStatusCode(StatusCode.GOOD);
		// }

		return value;
	}

	protected Object[] createProgValueArray() {
		return null;
	}

	protected Object createDRVValueArray() {
		return null;
	}

	// @Override
	// public byte[] prog2DRV(DataValue val) {
	// // if (this.transform == null) {
	// // // return null;
	// // return new byte[0];
	// // }
	// // this.transform.transToDevice(val);
	// return new byte[0];
	// // return null;
	// }

	public Object prog2DRV(Variant val) {
		// if (this.transform == null)
		// return null;

		// if (mapping == EVENTS_MAPPING_TYPE.SCALAR) {
		// return this.transform.transToDevice(new DataValue(val));
		// } else if (mapping == EVENTS_MAPPING_TYPE.ARRAY_ARRAY) {
		// Object[] objects = (Object[]) val.getValue();

		// fill the whole array
		// for (int i = 0; i < this.arraylength; i++) {
		// this.array[i] = this.transform.transToDevice(new
		// DataValue(new Variant(objects[i])));;
		// }
		// now create the target specific array
		// return this.createDRVValueArray();
		// }
		return null;
	}

	public int getArraylength() {
		return arraylength;
	}

	public void setArraylength(int arraylength) {
		this.arraylength = arraylength;
	}

	// public XML_DA_DATATYPE getDataType() {
	// return dataType;
	// }

	// public void setDataType(XML_DA_DATATYPE dataType) {
	// this.dataType = dataType;
	// }

	// public String getResourceName() {
	// return resourceName;
	// }

	// public void setResourceName(String resourceName) {
	// this.resourceName = resourceName;
	// }

	// public String getVarName() {
	// return varName;
	// }

	// public void setVarName(String varName) {
	// this.varName = varName;
	// }

	// public EVENTS_MAPPING_TYPE getMapping() {
	// return mapping;
	// }

	// public void setMapping(EVENTS_MAPPING_TYPE mapping) {
	// this.mapping = mapping;
	// }

	public DataValue getValue() {
		return value;
	}

	public void setValue(DataValue value) {
		this.value = value;
	}

	// public byte[] getIntern() {
	// return intern;
	// }

	// public void setIntern(byte[] intern) {
	// this.intern = intern;
	// }

	public Object[] getChildren() {
		return new Object[0];
	}

	// public NodeId getSourceId() {
	// return sourceId;
	// }
	//
	// public void setSourceId(NodeId sourceId) {
	// this.sourceId = sourceId;
	// }

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public NodeId getVariableSource() {
		return variable_source;
	}

	public void setVariableSource(NodeId variable_source) {
		this.variable_source = variable_source;
	}

	@Override
	public String getDname() {
		return null;
	}

	@Override
	public String getDesc() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDtype() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NodeId getNId() {
		return this.getSourceId();
	}

	@Override
	public boolean isValid() {
		return false;
	}

	@Override
	public String getBrowsepath() {
		return this.browsepath;
	}

	// public String getDisplayname() {
	// return displayname;
	// }
	//
	// public void setDisplayname(String displayname) {
	// this.displayname = displayname;
	// }

	public EventDpItem getParent() {
		return this.parent;
	}

	public void setParent(EventDpItem parent) {
		this.parent = parent;
	}

	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public EventReference getReference() {
		return reference;
	}

	public void setReference(EventReference reference) {
		this.reference = reference;
	}

}
