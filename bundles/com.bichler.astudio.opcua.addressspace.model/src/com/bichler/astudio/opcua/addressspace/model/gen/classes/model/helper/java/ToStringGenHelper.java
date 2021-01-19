package com.bichler.astudio.opcua.addressspace.model.gen.classes.model.helper.java;

import org.opcfoundation.ua.utils.ObjectUtils;

public class ToStringGenHelper extends MethodGenHelper
{
  private String className;

  public ToStringGenHelper(String className)
  {
    super("", "", "");
    this.className = className;
  }

  @Override
  public String genCode()
  {
    StringBuffer buffer = new StringBuffer();
    buffer.append("public String toString(){");
    buffer.append("\n");
    buffer.append("return \"" + this.className + ":\"+ " + ObjectUtils.class.getName() + ".printFieldsDeep(this); ");
    buffer.append("\n");
    buffer.append("}");
    buffer.append("\n");
    buffer.append("\n");
    return buffer.toString();
  }
}
