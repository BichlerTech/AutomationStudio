package com.bichler.astudio.opcua.addressspace.model.gen.classes.model.helper.java;

import com.bichler.astudio.opcua.addressspace.model.gen.classes.model.helper.AbstractGenHelper;
import com.bichler.astudio.opcua.addressspace.model.gen.classes.output.java.JavaGeneratorUtil;

public class VariableGenHelper extends AbstractGenHelper
{
  private String declaration;
  private String modifier;
  private String name;
  private String type;
  private boolean isGetter = false;
  private boolean isSetter = false;
  private String getterName;
  private String setterName;
  private boolean override;

  /**
   * 
   * @param type
   *          name of the type
   * @param name
   *          name of the variable
   */
  public VariableGenHelper(String type, String name)
  {
    super();
    this.type = type;
    this.name = name;
    this.modifier = JavaGeneratorUtil._private;
  }

  /**
   * 
   * @param modifier
   *          modifier of the variable
   * @param type
   * @param name
   */
  public VariableGenHelper(String modifier, String type, String name)
  {
    this(type, name);
    this.modifier = modifier;
  }

  public VariableGenHelper(String modifier, String type, String name, String declaratiom)
  {
    this(modifier, type, name);
    this.declaration = declaratiom;
  }

  @Override
  public String genCode()
  {
    return this.modifier + " " + this.type + " " + this.name
        + (this.declaration != null ? " = " + this.declaration : "") + ";\n";
  }

  public String getName()
  {
    return this.name;
  }

  public String getType()
  {
    return this.type;
  }

  public boolean isGetter()
  {
    return isGetter;
  }

  public void setGetter(boolean isGetter)
  {
    this.isGetter = isGetter;
  }

  public boolean isSetter()
  {
    return isSetter;
  }

  public void setSetter(boolean isSetter)
  {
    this.isSetter = isSetter;
  }

  public String getGetterName()
  {
    return this.getterName;
  }

  public String getSetterName()
  {
    return this.setterName;
  }

  public void setGetterName(String getterName)
  {
    this.getterName = getterName;
  }

  public void setSetterName(String setterName)
  {
    this.setterName = setterName;
  }

  public boolean isOverride()
  {
    return this.override;
  }

  public void setMethodsOverriden(boolean override)
  {
    this.override = override;
  }
}
