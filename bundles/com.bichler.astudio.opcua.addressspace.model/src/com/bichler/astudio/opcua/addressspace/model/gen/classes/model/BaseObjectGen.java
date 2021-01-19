package com.bichler.astudio.opcua.addressspace.model.gen.classes.model;

import opc.sdk.core.node.Node;
import opc.sdk.server.core.UAServerApplicationInstance;

import org.opcfoundation.ua.builtintypes.ExpandedNodeId;

public abstract class BaseObjectGen extends BaseGen
{
  public BaseObjectGen(Node node)
  {
    super(node);
  }

  @Override
  public ExpandedNodeId prepareAsVariable(UAServerApplicationInstance server)
  {
    return ExpandedNodeId.NULL;
  }
}
