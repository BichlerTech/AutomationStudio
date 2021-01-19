package com.bichler.astudio.opcua.addressspace.model.gen.classes.output;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.opcfoundation.ua.builtintypes.NodeId;

public abstract class ConstantTemplate
{
  private Map<NodeId, String> constants = new HashMap<>();
  private String name;

  public ConstantTemplate()
  {
  }

  public Set<Entry<NodeId, String>> entrySet()
  {
    return this.constants.entrySet();
  }

  public String put(NodeId nodeId, String className)
  {
    return this.constants.put(nodeId, className);
  }

  public String get(NodeId key)
  {
    return this.constants.get(key);
  }

  public String getName()
  {
    return this.name;
  }

  public void setName(String name)
  {
    this.name = name;
  }
}
