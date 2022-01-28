package com.bichler.astudio.editor.allenbradley.xml;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.graphics.Image;
import org.opcfoundation.ua.builtintypes.NodeId;

import com.bichler.astudio.editor.allenbradley.datatype.ALLENBRADLEY_DATA_TYPE;
import com.bichler.astudio.opcua.properties.driver.IDriverNode;
import com.bichler.astudio.opcua.widget.NodeToTrigger;

public class AllenBradleyEntryModelNode implements IDriverNode
{

  private String version = "v.1.0.1";
  private String buildDate = "01.01.2012";
  private String license = "";
  private NodeId nodeId = null;
  private int cycletime = 1000;
  private String displayname = "";
  private String browsepath = "";
  private int arrayLength = 0;
  private String address = "";
  private ALLENBRADLEY_MAPPING_TYPE mapping = ALLENBRADLEY_MAPPING_TYPE.SCALAR;
  private NodeToTrigger trigger = null;
  private String fileDir = "";
  private boolean active = true;
  private String dataType = ALLENBRADLEY_DATA_TYPE.UNDEFINED.name();
  private String description = "";
  private Image labelImage = null;
  private String symbolname = "";
  private List<AllenBradleyEntryModelNode> children = null;
  private boolean valid = true;

  public AllenBradleyEntryModelNode getChild(String key)
  {
    for (AllenBradleyEntryModelNode node : children)
    {
      if (node.getSymbolName().compareTo(key) == 0)
      {
        return node;
      }
    }
    return null;
  }

  public AllenBradleyEntryModelNode()
  {
    this.setChildren(new ArrayList<AllenBradleyEntryModelNode>());
  }

  public static AllenBradleyEntryModelNode loadFromDP(AllenBradleyDPItem dp)
  {
    AllenBradleyEntryModelNode node = new AllenBradleyEntryModelNode();
    node.setActive(dp.isActive());
    node.setAddress("" + dp.getDBAddress());
    node.setIndex(dp.getIndex());
    node.setCycletime(dp.getCycletime());
    node.setDataType(dp.getDataType());
    node.setDescription(dp.getDescription());
    node.setMapping(dp.getMapping());
    NodeToTrigger t = new NodeToTrigger();
    t.triggerName = dp.getTriggerNode();
    node.setTrigger(t);
    node.setNodeId(dp.getNodeId());
    node.setSymbolName(dp.getSymbolname());
    return node;
  }

  public String getLicense()
  {
    return license;
  }

  public void setLicense(String license)
  {
    this.license = license;
  }

  public String getVersion()
  {
    return version;
  }

  public void setVersion(String version)
  {
    this.version = version;
  }

  public String getBuildDate()
  {
    return buildDate;
  }

  public void setBuildDate(String buildDate)
  {
    this.buildDate = buildDate;
  }

  /**
   * public String getImportPath() { return importPath; }
   * 
   * public void setImportPath(String importPath) { this.importPath = importPath;
   * }
   */
  public List<AllenBradleyEntryModelNode> getChildren()
  {
    return children;
  }

  public void setChildren(List<AllenBradleyEntryModelNode> children)
  {
    this.children = children;
  }

  public NodeId getNodeId()
  {
    return nodeId;
  }

  public void setNodeId(NodeId nodeId)
  {
    this.nodeId = nodeId;
  }

  public String getAddress()
  {
    return address;
  }

  public void setAddress(String address)
  {
    this.address = address;
  }

  /**
   * public boolean isHistorical() { return historical; }
   * 
   * public void setHistorical(boolean historical) { this.historical = historical;
   * }
   */
  public int getCycletime()
  {
    return cycletime;
  }

  public void setCycletime(int cycletime)
  {
    this.cycletime = cycletime;
  }

  public String getFileDir()
  {
    return fileDir;
  }

  public void setFileDir(String fileDir)
  {
    this.fileDir = fileDir;
  }

  public String getDisplayname()
  {
    return displayname;
  }

  public void setDisplayname(String displayname)
  {
    this.displayname = displayname;
  }

  public String getBrowsepath()
  {
    return browsepath;
  }

  public void setBrowsepath(String browsepath)
  {
    this.browsepath = browsepath;
  }

  public NodeToTrigger getTrigger()
  {
    return trigger;
  }

  public void setTrigger(NodeToTrigger trigger)
  {
    this.trigger = trigger;
  }

  public void setArrayRange(int[] rangeResults)
  {
    int min = rangeResults[0];
    int max = rangeResults[1];
    this.arrayLength = max - min + 1;
  }

  public int getArrayLength()
  {
    return this.arrayLength;
  }

  public boolean isActive()
  {
    return active;
  }

  public void setActive(boolean active)
  {
    this.active = active;
  }

  public String getSymbolName()
  {
    return symbolname;
  }

  public void setSymbolName(String datapoint)
  {
    this.symbolname = datapoint;
  }

  public Image getLabelImage()
  {
    return labelImage;
  }

  public void setLabelImage(Image labelImage)
  {
    this.labelImage = labelImage;
  }

  // public float getIndex() {
  // return index;
  // }
  // public void setIndex(float index) {
  // this.index = index;
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
  // public void setAddressType(SiemensAreaCode addressType) {
  // this.addressType = addressType;
  // }

  // public SiemensAreaCode getAddressType() {
  // return this.addressType;
  // }
  public ALLENBRADLEY_MAPPING_TYPE getMapping()
  {
    return this.mapping;
  }

  public void setMapping(ALLENBRADLEY_MAPPING_TYPE valueRank)
  {
    this.mapping = valueRank;
  }

  public float calculateEndIndex()
  {
    // allenbradley_DATA_TYPE type = allenbradley_DATA_TYPE
    // .getTypeFromString(this.dataType);
    //
    // return type.getIndexLength() + this.index;
    return -1;
  }
  private AllenBradleyEntryModelNode neighbor = null;
  private String indexNew = "";
  private String datatypeNew = "";
  // private String tag;
  // private String scope;
  // private String specifier;
  // private String attributes;
  private int index;

  public void setNeighbor(AllenBradleyEntryModelNode neighbor)
  {
    this.neighbor = neighbor;
  }

  protected AllenBradleyEntryModelNode getNeighbor()
  {
    return neighbor;
  }

  public String getIndexNew()
  {
    return this.indexNew;
  }

  public int getIndex()
  {
    return this.index;
  }

  public void setIndex(int index)
  {
    this.index = index;
  }

  public void setIndexNew(String index)
  {
    this.indexNew = index;
  }

  public String getDataTypeNew()
  {
    return this.datatypeNew;
  }

  public void setDataTypeNew(String datatypeNew)
  {
    this.datatypeNew = datatypeNew;
  }

  @Override
  public String getDname()
  {
    return getDisplayname();
  }

  @Override
  public String getDesc()
  {
    return getDescription();
  }

  @Override
  public String getDtype()
  {
    return getDataType();
  }

  @Override
  public NodeId getNId()
  {
    return getNodeId();
  }

  public String getItemPath()
  {
    return "";
  }

  public String getItemName()
  {
    return this.displayname;
  }

  @Override
  public boolean isValid()
  {
    return this.valid;
  }
}
