package com.bichler.astudio.opcua.addressspace.model.gen.classes.output.java;

import com.bichler.astudio.opcua.addressspace.model.gen.classes.model.helper.java.DocumentGenHelper;
import com.bichler.astudio.opcua.addressspace.model.gen.classes.model.helper.java.InfoContext;
import com.bichler.astudio.opcua.addressspace.model.gen.classes.model.helper.java.MethodGenHelper;
import com.bichler.astudio.opcua.addressspace.model.gen.classes.model.helper.java.VariableGenHelper;

public class JavaGeneratorUtil
{
  public static String _public = "public";
  public static String _private = "private";
  public static String _static = "static";
  public static String n_ID = "ID";
  public static String _Override = "@Override";

  public static DocumentGenHelper constuctJavaClassStart(String className, String superType)
  {
    DocumentGenHelper helper = new DocumentGenHelper(className, superType);
    return helper;
  }

  public static void methodGetter(MethodGenHelper method, VariableGenHelper variable)
  {
    String name = method.getName();
    // change method header
    String gettername = "get" + method.getName();
    if (variable.getGetterName() != null)
    {
      gettername = variable.getGetterName();
    }
    method.setName(gettername);
    // insert body
    String body = "return " + name + ";";
    method.setBody(body);
  }

  public static void methodSetter(MethodGenHelper method, VariableGenHelper variable, String value)
  {
    String name = method.getName();
    // change method header
    String setterName = "set" + method.getName();
    if (variable.getSetterName() != null)
    {
      setterName = variable.getSetterName();
    }
    method.setName(setterName);
    // insert body
    String body = "" + name + " = " + value + ";";
    method.setBody(body);
  }

  public static void methodGetValue(InfoContext context, MethodGenHelper method, String valueType)
  {
    String statementBegin = "getVariant() != null ? (" + valueType + ")getVariant().getValue() : null;";
    String statementEnd = "return " + statementBegin;
    method.setBody(statementEnd);
  }
}
