package com.bichler.astudio.opcua.addressspace.model.gen.classes.model.helper.def;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Map;

import org.opcfoundation.ua.builtintypes.NodeId;

public class IdentifierVarGenHelper extends VarGenHelper
{
  /** node id declaration */
  private NodeId nodeId;

  public IdentifierVarGenHelper()
  {
  }

  public NodeId getNodeId()
  {
    return this.nodeId;
  }

  public void setNodeId(NodeId nodeId)
  {
    this.nodeId = nodeId;
  }

  @Override
  public void streamVarDeclaration(BufferedWriter fos, Map<NodeId, String> constants) throws IOException
  {
    String def = constants.get(this.nodeId);
    if (def != null)
    {
      // fos.write("public static final NodeId ID = "
      // + IdentifierGen.class.getName() + "." + def + ";");
    }
    else
    {
      fos.write("public static final NodeId ID = NodeId.parseNodeId(\"" + this.nodeId.toString() + "\");");
    }
    fos.newLine();
  }

  @Override
  public void streamVarGetterSetter(BufferedWriter fos) throws IOException
  {
    fos.write("@Override\npublic NodeId getTypeId(){return ID;}");
    fos.newLine();
    fos.newLine();
  }

  @Override
  public String getVarDef()
  {
    return "";
  }
}
