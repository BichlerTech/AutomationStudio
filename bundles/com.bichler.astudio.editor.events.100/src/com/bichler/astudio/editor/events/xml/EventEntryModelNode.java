package com.bichler.astudio.editor.events.xml;

import java.util.ArrayList;
import java.util.List;

import org.opcfoundation.ua.builtintypes.NodeId;

import com.bichler.astudio.opcua.properties.driver.IDriverNode;
import com.hbsoft.driver.opc.events.dp.AbstractEventModel;

public class EventEntryModelNode implements IDriverNode
{
  private AbstractEventModel item = null;
  private List<EventEntryModelNode> children = null;
  private EventEntryModelNode parent = null;
  private EVENTENTRYTYPE type = EVENTENTRYTYPE.DATAPOINT;

  EventEntryModelNode(AbstractEventModel item)
  {
    this.children = new ArrayList<>();
    this.item = item;
  }

  @Override
  public String getDname()
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String getDesc()
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String getDtype()
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public NodeId getNId()
  {
    return null;
  }


  @Override
  public boolean isValid()
  {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public String getBrowsepath()
  {
    return null;
  }

  public AbstractEventModel getItem()
  {
    return this.item;
  }

  public void addChild(EventEntryModelNode child)
  {
    child.setParent(this);
    this.children.add(child);
  }

  public EventEntryModelNode[] getChildren()
  {
    return this.children.toArray(new EventEntryModelNode[0]);
  }

  public List<EventEntryModelNode> getChildrenList()
  {
    return this.children;
  }

  public EventEntryModelNode getParent()
  {
    return this.parent;
  }

  private void setParent(EventEntryModelNode parent)
  {
    this.parent = parent;
  }

  public static EventEntryModelNode loadFromDP(AbstractEventModel item, EVENTENTRYTYPE entryType)
  {
    // AbstractEventModel[] c2a = item.getChildren();
    EventEntryModelNode newNode = new EventEntryModelNode(item);
    newNode.setType(entryType);
    // for(AbstractEventModel child : c2a){
    //
    // }
    return newNode;
  }

  void setType(EVENTENTRYTYPE type)
  {
    this.type = type;
  }

  public EVENTENTRYTYPE getType()
  {
    return this.type;
  }

  public boolean isFirstChild()
  {
    if (this.parent == null)
    {
      return true;
    }
    EventEntryModelNode[] siblings = this.parent.getChildren();
    if (siblings.length == 0)
    {
      return true; // throw exception
    }
    EventEntryModelNode first = siblings[0];
    return first == this;
  }
}
