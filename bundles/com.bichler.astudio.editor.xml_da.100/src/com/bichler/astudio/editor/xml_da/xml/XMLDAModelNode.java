package com.bichler.astudio.editor.xml_da.xml;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.graphics.Image;
import org.opcfoundation.ua.builtintypes.NodeId;

import com.bichler.astudio.editor.xml_da.driver.datatype.XML_DA_DATATYPE;
import com.bichler.astudio.opcua.properties.driver.IDriverNode;
import com.bichler.astudio.opcua.widget.NodeToTrigger;

public class XMLDAModelNode implements IDriverNode
{
  private String version = "v.1.0.1";
  private String buildDate = "01.01.2012";
  private String license = ""; // TODO not used now
  private NodeId nodeId = null;
  private int id = 0;
  private boolean active = true;
  private int cycletime = 1000;
  private String displayname = "";
  private String symbolname = "";
  private String browsepath = "";
  private String dataType = XML_DA_DATATYPE.ANY.name();
  private String itemPath = "";
  private String itemName = "";
  private XML_DA_MAPPING_TYPE mapping = XML_DA_MAPPING_TYPE.SCALAR;
  private String description = "";
  private NodeToTrigger trigger = null;
  private Image labelImage = null;
  private String fileDir = "";
  private boolean valid = true;
  private List<XMLDAModelNode> children = null;

  public XMLDAModelNode getChild(String key)
  {
    for (XMLDAModelNode node : children)
    {
      if (node.symbolname.compareTo(key) == 0)
      {
        return node;
      }
    }
    return null;
  }

  public XMLDAModelNode()
  {
    this.setChildren(new ArrayList<XMLDAModelNode>());
  }

  public static XMLDAModelNode loadFromDP(XML_DA_DPItem dp)
  {
    XMLDAModelNode node = new XMLDAModelNode();
    node.setActive(dp.isActive());
    node.setItemPath(dp.getItemPath());
    node.setItemName(dp.getItemName());
    node.setCycletime(dp.getCycletime());
    node.setDataType(dp.getDataType().name());
    node.setDescription(dp.getDescription());
    node.setId(dp.getId());
    node.setNodeId(dp.getNodeId());
    node.setSymbolName(dp.getSymbolname());
    NodeToTrigger t = new NodeToTrigger();
    t.triggerName = dp.getTriggerNode();
    node.setTrigger(t);
    node.setMapping(dp.getMapping());
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

  public String getSymbolName()
  {
    return symbolname;
  }

  public void setSymbolName(String datapoint)
  {
    this.symbolname = datapoint;
  }

  public String getDescription()
  {
    return description;
  }

  public void setDescription(String description)
  {
    this.description = description;
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
  public List<XMLDAModelNode> getChildren()
  {
    return children;
  }

  public void setChildren(List<XMLDAModelNode> children)
  {
    this.children = children;
  }

  public Image getLabelImage()
  {
    return labelImage;
  }

  public void setLabelImage(Image labelImage)
  {
    this.labelImage = labelImage;
  }

  public boolean isActive()
  {
    return active;
  }

  public void setActive(boolean active)
  {
    this.active = active;
  }

  public void setNodeId(NodeId nodeId)
  {
    this.nodeId = nodeId;
  }

  public String getDataType()
  {
    return dataType;
  }

  public void setDataType(String dataType)
  {
    this.dataType = dataType;
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

  public int getId()
  {
    return id;
  }

  public void setId(int id)
  {
    this.id = id;
  }

  public String getDisplayname()
  {
    return displayname;
  }

  public void setDisplayname(String displayname)
  {
    this.displayname = displayname;
  }

  @Override
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

  public String getItemPath()
  {
    return itemPath;
  }

  public void setItemPath(String itemPath)
  {
    this.itemPath = itemPath;
  }

  public String getItemName()
  {
    return itemName;
  }

  public void setItemName(String itemName)
  {
    this.itemName = itemName;
  }

  public XML_DA_MAPPING_TYPE getMapping()
  {
    return mapping;
  }

  public void setMapping(XML_DA_MAPPING_TYPE mapping)
  {
    this.mapping = mapping;
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
    return this.nodeId;
  }


  @Override
  public boolean isValid()
  {
    return this.valid;
  }
}
