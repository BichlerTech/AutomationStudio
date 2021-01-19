package com.bichler.astudio.opcua.addressspace.model.binary;

import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.core.IdType;

public class ComplexModelTemplate
{
  private ExpandedNodeId nodeId;
  private String uri;
  private String nodename;
  private String typename;

  protected ComplexModelTemplate(ExpandedNodeId nodeId, String uri, String string, String string2)
  {
    this.nodeId = nodeId;
    this.uri = uri;
    this.nodename = string;
    this.typename = string2;
  }

  @Override
  public String toString()
  {
    StringBuilder model = new StringBuilder();
    model.append("{\n");
    model.append("NamespaceTable nsTable = ComDRVManager.getDRVManager().getServer().getNamespaceUris();\n");
    model.append("int nsIndex = nsTable.getIndex(\"" + this.uri + "\");\n");
    IdType idType = nodeId.getIdType();
    switch (idType)
    {
    case Guid:
      break;
    case Numeric:
      model.append("NodeId nodeId = new NodeId(nsIndex, " + ((Number) nodeId.getValue()).intValue() + ");\n");
      break;
    case Opaque:
      break;
    case String:
      break;
    }
    model.append(this.typename + " " + this.nodename + " = new " + this.typename + "(null);\n");
    model.append(this.nodename + ".setNodeId(nodeId);\n");
    model.append("ComDRVManager.getDRVManager().getBaseManager().addNodeState(nodeId, " + this.nodename + ");\n");
    model.append("}");
    return model.toString();
  }
}
