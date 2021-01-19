package com.bichler.astudio.opcua.addressspace.model.gen.classes.model;

import opc.sdk.core.node.MethodNode;
import opc.sdk.server.core.UAServerApplicationInstance;

import org.opcfoundation.ua.builtintypes.ExpandedNodeId;

public class MethodGen extends BaseGen
{
  public MethodGen(MethodNode node)
  {
    super(node);
  }

  @Override
  public MethodNode getNode()
  {
    return (MethodNode) super.getNode();
  }

  @Override
  public ExpandedNodeId prepareAsVariable(UAServerApplicationInstance server)
  {
    return ExpandedNodeId.NULL;
  }
}
