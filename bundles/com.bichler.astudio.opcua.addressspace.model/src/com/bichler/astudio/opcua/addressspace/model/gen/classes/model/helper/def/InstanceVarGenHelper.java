package com.bichler.astudio.opcua.addressspace.model.gen.classes.model.helper.def;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Map;

import org.opcfoundation.ua.builtintypes.NodeId;

import com.bichler.astudio.opcua.addressspace.model.gen.classes.model.BaseGen;

public class InstanceVarGenHelper extends VarGenHelper
{
  /** opc ua instance declaration */
  private BaseGen helper;

  public InstanceVarGenHelper()
  {
  }

  public BaseGen getHelper()
  {
    return this.helper;
  }

  public void setHelper(BaseGen helper)
  {
    this.helper = helper;
  }

  @Override
  public void streamVarDeclaration(BufferedWriter fos, Map<NodeId, String> constants) throws IOException
  {
    fos.write("private " + getType() + " " + getVarDef() + ";");
    fos.newLine();
  }

  @Override
  public void streamVarGetterSetter(BufferedWriter fos) throws IOException
  {
    String typeDef = getType();
    String varDef = getVarDef();
    fos.write("public " + typeDef + " get" + varDef + "(){return " + varDef + ";}");
    fos.newLine();
    fos.newLine();
    fos.write("public void set" + varDef + "(" + typeDef + " value){this." + varDef + " = value;}");
    fos.newLine();
    fos.newLine();
  }

  @Override
  public String getVarDef()
  {
    return helper.getNode().getBrowseName().toString();
  }
}
