package com.bichler.astudio.opcua.addressspace.model.gen.classes.model.helper.java;

import com.bichler.astudio.opcua.addressspace.model.gen.classes.model.helper.AbstractGenHelper;
import com.bichler.astudio.opcua.addressspace.model.gen.classes.model.helper.java.method.MethodParameter;
import com.bichler.astudio.opcua.addressspace.model.gen.classes.output.java.JavaGeneratorUtil;

public class MethodGenHelper extends AbstractGenHelper
{
  // annotation
  private boolean override = false;
  // method header
  private String modifier;
  private String returnType;
  private String name;
  private MethodParameter[] parameter;
  // method body
  private String body;

  /**
   * Method sceleton with <modifier> <returnType> <name> <parameters>
   * 
   * @param modifier
   * @param returnType
   * @param name
   */
  public MethodGenHelper(String modifier, String returnType, String name)
  {
    super();
    this.modifier = modifier;
    this.name = name;
    this.returnType = returnType;
    this.parameter = new MethodParameter[0];
  }

  /**
   * Method sceleton with <modifier> <returnType> <name> <parameters>
   * 
   * @param modifier
   * @param returnType
   * @param name
   * @param params
   */
  public MethodGenHelper(String modifier, String returnType, String name, MethodParameter... params)
  {
    this(modifier, returnType, name);
    this.parameter = params;
  }

  @Override
  public String genCode()
  {
    String parameterString = "";
    int count = this.parameter.length;
    for (int i = 0; i < count; i++)
    {
      MethodParameter param = this.parameter[i];
      parameterString += param.toString();
      if (i < count - 1)
      {
        parameterString += ", ";
      }
    }
    // method header
    String methodHeader = this.modifier + " " + this.returnType + " " + this.name + "(" + parameterString + ")";
    // construct method
    StringBuffer buffer = new StringBuffer();
    if (this.override)
    {
      buffer.append(JavaGeneratorUtil._Override + "\n");
    }
    buffer.append(methodHeader);
    buffer.append("{\n");
    buffer.append(body != null ? body : "");
    buffer.append("\n}\n\n");
    return buffer.toString();
  }

  public String getReturnType()
  {
    return returnType;
  }

  public void setReturnType(String returnType)
  {
    this.returnType = returnType;
  }

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public MethodParameter[] getParameter()
  {
    return parameter;
  }

  public void setParameter(MethodParameter[] parameter)
  {
    this.parameter = parameter;
  }

  public void setBody(String body)
  {
    this.body = body;
  }

  public void setOverride(boolean override)
  {
    this.override = override;
  }

  public boolean isOverride()
  {
    return this.override;
  }
}
