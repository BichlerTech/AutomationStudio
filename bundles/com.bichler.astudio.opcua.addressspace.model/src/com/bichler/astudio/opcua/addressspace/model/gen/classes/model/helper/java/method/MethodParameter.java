package com.bichler.astudio.opcua.addressspace.model.gen.classes.model.helper.java.method;

public class MethodParameter
{
  public static final String DEFAULT_NAME_VALUE = "value";
  private String type;
  private String name;

  public MethodParameter(String type, String name)
  {
    this.type = type;
    this.name = name;
  }

  @Override
  public String toString()
  {
    return type + " " + name;
  }
}
