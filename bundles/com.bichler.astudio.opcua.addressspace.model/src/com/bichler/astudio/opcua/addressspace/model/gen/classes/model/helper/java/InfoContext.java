package com.bichler.astudio.opcua.addressspace.model.gen.classes.model.helper.java;

import java.util.HashMap;
import java.util.Map;

import org.opcfoundation.ua.builtintypes.NodeId;

public class InfoContext
{
  private String packageName;
  private Map<NodeId, String> classes2generate;

  public InfoContext()
  {
    this.classes2generate = new HashMap<>();
  }

  public void addClass(NodeId id, String name)
  {
    this.classes2generate.put(id, name);
  }

  public boolean existClass(NodeId id)
  {
    return this.classes2generate.containsKey(id);
  }

  public String removeClass(NodeId id)
  {
    return this.classes2generate.remove(id);
  }

  public String getPackageName()
  {
    return packageName;
  }

  public void setPackageName(String packageName)
  {
    this.packageName = packageName;
  }
}
