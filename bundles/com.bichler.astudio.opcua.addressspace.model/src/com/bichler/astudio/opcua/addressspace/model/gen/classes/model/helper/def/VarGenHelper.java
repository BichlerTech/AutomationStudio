package com.bichler.astudio.opcua.addressspace.model.gen.classes.model.helper.def;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Map;

import opc.sdk.core.node.Node;
import opc.sdk.server.core.UAServerApplicationInstance;

import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.QualifiedName;

public abstract class VarGenHelper
{
  /** opc ua instance declaration */
  private ExpandedNodeId typeId;
  private QualifiedName type;

  public VarGenHelper()
  {
  }

  public abstract void streamVarDeclaration(BufferedWriter fos, Map<NodeId, String> constants) throws IOException;

  public abstract void streamVarGetterSetter(BufferedWriter fos) throws IOException;

  public ExpandedNodeId getTypeId()
  {
    return this.typeId;
  }

  public String getType()
  {
    if (QualifiedName.isNull(this.type))
    {
      return "";
    }
    return this.type.toString();
  }

  public void setTypeId(UAServerApplicationInstance server, ExpandedNodeId typeId)
  {
    this.typeId = typeId;
    if (!ExpandedNodeId.isNull(typeId))
    {
      Node typeNode = server.getServerInstance().getAddressSpaceManager().getNodeById(typeId);
      setType(typeNode.getBrowseName());
    }
  }

  public void setType(QualifiedName type)
  {
    this.type = type;
  }

  public abstract String getVarDef();
}
