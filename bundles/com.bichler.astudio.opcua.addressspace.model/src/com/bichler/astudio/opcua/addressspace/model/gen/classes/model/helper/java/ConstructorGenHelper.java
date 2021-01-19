package com.bichler.astudio.opcua.addressspace.model.gen.classes.model.helper.java;

import com.bichler.astudio.opcua.addressspace.model.gen.classes.model.helper.AbstractGenHelper;

public class ConstructorGenHelper extends AbstractGenHelper
{
  private String constructorName;

  public ConstructorGenHelper(String constructorName)
  {
    super();
    this.constructorName = constructorName;
  }

  @Override
  public String genCode()
  {
    StringBuffer buffer = new StringBuffer();
    buffer.append("public " + this.constructorName + "(){");
    buffer.append("\n");
    buffer.append("super();");
    buffer.append("\n");
    buffer.append("}");
    buffer.append("\n");
    buffer.append("\n");
    return buffer.toString();
  }
}
