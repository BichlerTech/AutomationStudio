package com.bichler.astudio.opcua.addressspace.model.gen.classes.model.helper.java;

import com.bichler.astudio.opcua.addressspace.model.gen.classes.model.helper.AbstractGenHelper;

public class DocumentGenHelper extends AbstractGenHelper
{
  private String className;
  private String superType;

  public DocumentGenHelper(String className, String superType)
  {
    super();
    this.className = className;
    this.superType = superType;
  }

  @Override
  public String genCode()
  {
    StringBuffer buffer = new StringBuffer();
    buffer.append("public class " + className + " extends " + superType + " {");
    buffer.append("\n");
    return buffer.toString();
  }

  public String endClass()
  {
    StringBuffer buffer = new StringBuffer();
    buffer.append("}");
    return buffer.toString();
  }
}
