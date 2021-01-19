package com.bichler.astudio.editor.allenbradley.xml;

import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.NodeId;

import com.bichler.opc.comdrv.ComDP;
import com.bichler.opc.comdrv.utils.ValueOutOfRangeException;

// import com.hbsoft.comet.siemens.communication.SiemensAreaCode;
// import com.hbsoft.comet.siemens.communication.SiemensDataKind;

public class AllenBradleyDPItem extends ComDP
{
  DataValue dv = new DataValue();

  @Override
  public void setAddress(String addr)
  {
    try
    {
      this.address = Integer.parseInt(addr);
    }
    catch (NumberFormatException ex)
    {
      // TODO log db address wrong format
      ex.printStackTrace();
    }
  }

  public int getDBAddress()
  {
    return this.address;
  }
  private int id = 0;
  // protected byte[] internArray = null;
  private boolean active = true;
  private int cycletime = 1000;
  private String symbolname = "";
  private String browsename = "";
  // protected SiemensAreaCode addressType = SiemensAreaCode.UNKNOWN;
  protected String dataType = "";
  // protected SiemensDataKind dataKind = SiemensDataKind.UNKNOWN;
  protected int address = 0;
  protected int index = 0;
  private String description = "";
  private boolean isFolder = false;
  private int parentId = -1;
  private String triggerNode = "";
  /**
   * the length of an array for the first dimension
   */
  // protected int arraylength = 1;
  protected AllenBradleyTransformation transform;
  /**
   * the size of that element in byte
   */
  protected int length = 0;
  private byte[] defaultSingleReadRequest = null;
  private byte[] defaultSingleWriteRequest = null;
  private long lastread;
  protected ALLENBRADLEY_MAPPING_TYPE mapping = ALLENBRADLEY_MAPPING_TYPE.SCALAR;
  protected Object[] array = null;

  public int getId()
  {
    return id;
  }

  public void setId(int id)
  {
    this.id = id;
  }

  public boolean isActive()
  {
    return active;
  }

  public void setActive(boolean active)
  {
    this.active = active;
  }

  public int getCycletime()
  {
    return cycletime;
  }

  public void setCycletime(int cycletime)
  {
    this.cycletime = cycletime;
  }

  public String getSymbolname()
  {
    return symbolname;
  }

  public void setSymbolname(String symbolname)
  {
    this.symbolname = symbolname;
  }

  public String getBrowsename()
  {
    return browsename;
  }

  public void setBrowsename(String browsename)
  {
    this.browsename = browsename;
  }

  // public SiemensAreaCode getAddressType() {
  // return addressType;
  // }
  //
  // public void setAddressType(SiemensAreaCode addressType) {
  // this.addressType = addressType;
  // }
  public String getDataType()
  {
    return dataType;
  }

  public void setDataType(String dataType)
  {
    this.dataType = dataType;
  }

  public String getDescription()
  {
    return description;
  }

  public void setDescription(String description)
  {
    this.description = description;
  }

  public boolean isFolder()
  {
    return isFolder;
  }

  public void setFolder(boolean isFolder)
  {
    this.isFolder = isFolder;
  }

  public int getParentId()
  {
    return parentId;
  }

  public void setParentId(int parentId)
  {
    this.parentId = parentId;
  }

  /**
   * public boolean isHistorical() { return historical; } public void
   * setHistorical(boolean historical) { this.historical = historical; }
   */
  public NodeId getNodeId()
  {
    return nodeId;
  }

  public void setNodeId(NodeId nodeId)
  {
    this.nodeId = nodeId;
  }

  public String getTriggerNode()
  {
    return triggerNode;
  }

  public void setTriggerNode(String triggerNode)
  {
    this.triggerNode = triggerNode;
  }

  public byte[] getDefaultSingleReadRequest()
  {
    return defaultSingleReadRequest;
  }

  public void setDefaultSingleReadRequest(byte[] defaultSingleReadRequest)
  {
    this.defaultSingleReadRequest = defaultSingleReadRequest;
  }

  public byte[] getDefaultSingleWriteRequest()
  {
    return defaultSingleWriteRequest;
  }

  public void setDefaultSingleWriteRequest(byte[] defaultSingleWriteRequest)
  {
    this.defaultSingleWriteRequest = defaultSingleWriteRequest;
  }

  public int getLength()
  {
    return length;
  }

  public int getLengthInBit()
  {
    return length;
  }

  public void setLength(int length)
  {
    this.length = length;
  }

  public int getIndex()
  {
    return index;
  }

  public int getIndexInBit()
  {
    return 0;
  }

  public void setIndex(int index)
  {
    this.index = index;
  }

  public long getLastRead()
  {
    return this.lastread;
  }

  public void setLastRead(long lastread)
  {
    this.lastread = lastread;
  }

  public AllenBradleyTransformation getTransform()
  {
    return transform;
  }

  public void setTransform(AllenBradleyTransformation transform)
  {
    this.transform = transform;
  }

  // @Override
  // public DataValue drv2Prog(byte[] data) {
  // if (this.transform == null)
  // return null;
  //
  // /**
  // * now create message to delete read bytes
  // */
  // ComByteMessage message = new ComByteMessage();
  // message.setBuffer(data);
  // this.transform.setStatusCode(StatusCode.GOOD);
  //
  // Object obj = this.transform.transToIntern(message);
  // dv.setValue(new Variant(obj));
  // dv.setSourceTimestamp(new DateTime());
  // dv.setStatusCode(this.transform.getStatusCode());
  // return dv;
  // return this.transform.transToIntern(data);
  // }
  public DataValue drv2Prog(byte[] data)
  {
    return null;
  }

  // @Override
  // public byte[] prog2DRV(DataValue val) {
  // this.transform.setStatusCode(StatusCode.GOOD);
  //
  // if (this.transform == null)
  // return null;
  //
  // if (mapping == SIEMENS_MAPPING_TYPE.SCALAR) {
  // byte[] ret = this.transform
  // .transToDevice(val.getValue().getValue());
  // val.setStatusCode(this.transform.getStatusCode());
  // return ret;
  // } else if (mapping == SIEMENS_MAPPING_TYPE.ARRAY_ARRAY) {
  // Object[] objects = (Object[]) val.getValue().getValue();
  //
  // // fill the whole array
  // for (int i = 0; i < this.arraylength; i++) {
  // this.array[i] = this.transform.transToDevice(objects[i]);
  // }
  // // now create the target specific array
  // val.setStatusCode(this.transform.getStatusCode());
  // return this.createDRVValueArray();
  // }
  //
  // return null;
  // }
  protected byte[] createDRVValueArray()
  {
    return null;
  }
  // public int getArraylength() {
  // return arraylength;
  // }
  // public void setArraylength(int arraylength) {
  // this.arraylength = arraylength;
  // this.internArray = new byte[getLength() * arraylength];
  // }

  // public SiemensDataKind getDataKind() {
  // return dataKind;
  // }
  //
  // public void setDataKind(SiemensDataKind dataKind) {
  // this.dataKind = dataKind;
  // }
  public boolean verifyValueRange(DataValue value)
  {
    // check if we have an array to verify
    if (this.mapping == ALLENBRADLEY_MAPPING_TYPE.SCALAR)
    {
      value.getValue().getValue();
      // now verify range by transformation object
      return this.transform.verifyValueRange(value.getValue().getValue());
    }
    else if (this.mapping == ALLENBRADLEY_MAPPING_TYPE.ARRAY_ARRAY)
    {
    }
    return false;
  }

  public ALLENBRADLEY_MAPPING_TYPE getMapping()
  {
    return mapping;
  }

  public void setMapping(ALLENBRADLEY_MAPPING_TYPE mapping)
  {
    this.mapping = mapping;
  }

  @Override
  public byte[] prog2DRV(DataValue val) throws ValueOutOfRangeException
  {
    // TODO Auto-generated method stub
    return null;
  }
}
