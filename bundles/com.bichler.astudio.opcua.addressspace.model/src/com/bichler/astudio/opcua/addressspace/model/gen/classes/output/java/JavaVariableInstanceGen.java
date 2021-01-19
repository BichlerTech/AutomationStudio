package com.bichler.astudio.opcua.addressspace.model.gen.classes.output.java;

import java.util.List;

import opc.sdk.core.node.Node;
import opc.sdk.core.node.VariableNode;
import opc.sdk.core.node.VariableTypeNode;
import opc.sdk.core.types.SDKBuiltinsMap;
import opc.sdk.core.types.TypeTable;
import opc.sdk.server.core.UAServerApplicationInstance;

import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.NodeClass;

import com.bichler.astudio.opcua.addressspace.model.gen.classes.model.BaseGen;
import com.bichler.astudio.opcua.addressspace.model.gen.classes.model.helper.java.InfoContext;
import com.bichler.astudio.opcua.addressspace.model.gen.classes.model.helper.java.MethodGenHelper;

public class JavaVariableInstanceGen extends BaseOpcGen
{
  public JavaVariableInstanceGen(BaseGen gen, UAServerApplicationInstance server)
  {
    super(gen, server);
  }

  @Override
  public void additionalMethods(InfoContext context, List<MethodGenHelper> methods)
  {
    String valueType = getValueType(context);
    MethodGenHelper method = new MethodGenHelper(JavaGeneratorUtil._public, valueType, "getValue");
    method.setOverride(true);
    JavaGeneratorUtil.methodGetValue(context, method, valueType);
    methods.add(method);
  }

  private String getValueType(InfoContext context)
  {
    // get id of type
    NodeId datatype = null;
    Node node = getInstance2Generate().getNode();
    NodeClass nodeClass = node.getNodeClass();
    switch (nodeClass)
    {
    case Variable:
      datatype = ((VariableNode) node).getDataType();
      break;
    case VariableType:
      datatype = ((VariableTypeNode) node).getDataType();
      break;
    default:
      datatype = NodeId.NULL;
      break;
    }
    // get class of type
    TypeTable typeTree = getServer().getServerInstance().getTypeTable();
    boolean isBaseDataType = typeTree.isTypeOf(datatype, Identifiers.BaseDataType);
    boolean isStructure = typeTree.isTypeOf(datatype, Identifiers.Structure);
    String datatypeName = "Object";
    if (isBaseDataType && isStructure)
    {
      Node datatypeNode = getServer().getServerInstance().getAddressSpaceManager().getNodeById(datatype);
      String packagename = context.getPackageName();
      if (!context.existClass(datatype))
      {
        packagename = "org.opcfoundation.ua.core";
      }
      datatypeName = packagename + "." + datatypeNode.getBrowseName().toString();
    }
    else if (isBaseDataType && !isStructure)
    {
      Class<?> builtinClass = SDKBuiltinsMap.ID_CLASS_MAP.getRight(datatype);
      if (builtinClass != null)
      {
        if (builtinClass.isArray())
        {
          // simple name of array
          datatypeName = builtinClass.getSimpleName();
        }
        else
        {
          datatypeName = builtinClass.getName();
        }
      }
      else
      {
        throw new IllegalStateException("Cannot find builtin type with ID: " + datatype.toString());
      }
    }
    return datatypeName;
  }
}
