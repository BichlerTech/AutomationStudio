package com.bichler.astudio.editor.allenbradley.model;

import java.util.ArrayList;
import java.util.List;

public class AllenBradleyRootStartConfigurationNode
{
  private List<AllenBradleyStartConfigurationNode> children = new ArrayList<>();
  private boolean isActive = false;

  public AllenBradleyRootStartConfigurationNode()
  {
  }

  public AllenBradleyStartConfigurationNode[] getChildren()
  {
    return children.toArray(new AllenBradleyStartConfigurationNode[0]);
  }

  public void addChild(AllenBradleyStartConfigurationNode child)
  {
    this.children.add(child);
  }

  public Boolean isActive()
  {
    return isActive;
  }

  public void setActive(boolean isActive)
  {
    this.isActive = isActive;
  }

  public void removeChild(AllenBradleyStartConfigurationNode node)
  {
    this.children.remove(node);
  }
}
