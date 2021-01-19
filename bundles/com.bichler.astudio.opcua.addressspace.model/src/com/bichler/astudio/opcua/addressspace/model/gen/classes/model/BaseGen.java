package com.bichler.astudio.opcua.addressspace.model.gen.classes.model;

import java.util.ArrayList;
import java.util.List;

import org.opcfoundation.ua.builtintypes.ExpandedNodeId;

import opc.sdk.core.node.Node;
import opc.sdk.server.core.UAServerApplicationInstance;

public abstract class BaseGen
{
  private String packageName = "com.hbsoft.opc";
  /** Model defined instance variables */
  private BaseGen parent = null;
  private List<BaseGen> children = null;
  private Node node;

  public BaseGen(Node node)
  {
    this.children = new ArrayList<>();
    this.node = node;
  }

  public void addChild(BaseGen child)
  {
    child.setParent(this);
    this.children.add(child);
  }

  private void setParent(BaseGen parent)
  {
    this.parent = parent;
  }

  public List<BaseGen> getChildren()
  {
    return this.children;
  }

  public BaseGen getParent()
  {
    return this.parent;
  }

  public Node getNode()
  {
    return this.node;
  }

  public abstract ExpandedNodeId prepareAsVariable(UAServerApplicationInstance server);

  public String getPackage()
  {
    return this.packageName;
  }

  public void setPackage(String packageName)
  {
    this.packageName = packageName;
  }
}
