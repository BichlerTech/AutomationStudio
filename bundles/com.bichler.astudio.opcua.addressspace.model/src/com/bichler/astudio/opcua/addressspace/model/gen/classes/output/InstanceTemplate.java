package com.bichler.astudio.opcua.addressspace.model.gen.classes.output;

import java.io.IOException;
import java.io.OutputStream;

import opc.sdk.core.node.Node;
import opc.sdk.server.core.UAServerApplicationInstance;

import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.QualifiedName;

import com.bichler.astudio.opcua.addressspace.model.gen.classes.model.helper.java.InfoContext;

public abstract class InstanceTemplate
{
  public abstract void buildStructure(InfoContext context, InstanceGen instanceGen, ConstantTemplate constants);

  public abstract void output(OutputStream out) throws IOException;

  protected QualifiedName getBrowsenameFromId(UAServerApplicationInstance server, ExpandedNodeId typeId)
  {
    if (!ExpandedNodeId.isNull(typeId))
    {
      Node typeNode = server.getServerInstance().getAddressSpaceManager().getNodeById(typeId);
      return typeNode.getBrowseName();
    }
    return QualifiedName.NULL;
  }
}
