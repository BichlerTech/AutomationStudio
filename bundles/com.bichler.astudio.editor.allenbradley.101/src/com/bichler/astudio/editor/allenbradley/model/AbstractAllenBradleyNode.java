package com.bichler.astudio.editor.allenbradley.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.graphics.Image;

import com.bichler.astudio.editor.allenbradley.datatype.ALLENBRADLEY_DATA_TYPE;
import com.bichler.astudio.editor.allenbradley.datenbaustein.AllenBradleyDBResourceManager;
import com.bichler.astudio.editor.allenbradley.xml.AllenBradleyDPItem;
import com.bichler.astudio.editor.allenbradley.xml.AllenBradleyEntryModelNode;
import com.bichler.astudio.view.drivermodel.handler.util.AbstractDriverModelViewNode;

public abstract class AbstractAllenBradleyNode extends AbstractDriverModelViewNode
{
  private String dataType = ALLENBRADLEY_DATA_TYPE.UNDEFINED.name();
  private Image labelImage = null;
  private String name = "";
  private String address = "";
  private String description = "";
  private boolean active = true;
  private AbstractAllenBradleyNode parent = null;
  private List<AbstractAllenBradleyNode> members = new ArrayList<>();
  private List<AbstractAllenBradleyNode> dependancies = new ArrayList<>();

  protected AbstractAllenBradleyNode(AllenBradleyDBResourceManager structManager)
  {
    super(structManager);
  }

  @Override
  public String getText()
  {
    return getName();
  }

  @Override
  public AllenBradleyDBResourceManager getStructureManager()
  {
    return (AllenBradleyDBResourceManager) super.getStructureManager();
  }

  public boolean isActive()
  {
    return active;
  }

  public void setActive(boolean active)
  {
    this.active = active;
  }

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
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
  // return this.index;
  // }
  public String getDataType()
  {
    return dataType;
  }

  public void setDataType(String dataType)
  {
    this.dataType = dataType.replace("BIT", "BOOL");
  }

  public String getDescription()
  {
    return description;
  }

  public void setDescription(String description)
  {
    this.description = description;
  }

  public void addMember(AbstractAllenBradleyNode child)
  {
    child.setParent(this);
    this.members.add(child);
  }

  public void addDependancy(AbstractAllenBradleyNode child)
  {
    child.setParent(this);
    this.dependancies.add(child);
  }

  public AbstractAllenBradleyNode[] getDependancies()
  {
    return this.dependancies.toArray(new AbstractAllenBradleyNode[0]);
  }

  public AbstractAllenBradleyNode[] getMembers()
  {
    return this.members.toArray(new AbstractAllenBradleyNode[0]);
  }

  public AbstractAllenBradleyNode getParent()
  {
    return this.parent;
  }

  public abstract AbstractAllenBradleyNode cloneNode(boolean includeChildren);

  public void setActiveAll(boolean active)
  {
    setActive(active);
    for (AbstractAllenBradleyNode child : getMembers())
    {
      child.setActiveAll(active);
    }
  }

  /**
   * Clone nodes to treeviewer, include the address index
   * 
   * @param collection
   * @return
   */
  public AbstractAllenBradleyNode fillActiveAll(List<AbstractAllenBradleyNode> collection)
  {
    AbstractAllenBradleyNode cloned = this.cloneNode(false);
    if (isActive())
    {
      collection.add(cloned);
    }
    for (AbstractAllenBradleyNode child : getMembers())
    {
      child.fillActiveAll(collection);
    }
    return cloned;
  }

  public String getAddress()
  {
    return this.address;
  }

  public void setAddress(String address)
  {
    this.address = address;
  }
  // public String getSymbolName() {
  // return symbolName;
  // }

  // public void setSymbolName(String symbolName) {
  // this.symbolName = symbolName;
  // }
  protected void setParent(AbstractAllenBradleyNode parent)
  {
    this.parent = parent;
  }

  public static AbstractAllenBradleyNode loadFromDP(AllenBradleyDPItem dp)
  {
    AllenBradleyEntryModelNode node = new AllenBradleyEntryModelNode();
    node.setActive(dp.isActive());
    // node.setAddress(dp.getAddress());
    // node.setIndex(dp.getIndex());
    // node.setAddressType(dp.getAddressType());
    node.setCycletime(dp.getCycletime());
    node.setDataType(dp.getDataType());
    node.setDescription(dp.getDescription());
    // node.setDataType(dp.getDataType().name());
    node.setDescription(dp.getDescription());
    // node.setTrigger(dp.getTriggerNode());
    // node.setHistorical(dp.isHistorical());
    // node.setId(dp.getId());
    node.setNodeId(dp.getNodeId());
    node.setSymbolName(dp.getSymbolname());
    return null;
  }
}
