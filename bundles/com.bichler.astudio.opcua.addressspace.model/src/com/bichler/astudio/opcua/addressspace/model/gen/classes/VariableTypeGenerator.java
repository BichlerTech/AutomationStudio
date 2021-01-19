package com.bichler.astudio.opcua.addressspace.model.gen.classes;

import opc.sdk.core.node.Node;
import opc.sdk.core.node.VariableNode;
import opc.sdk.core.node.VariableTypeNode;
import opc.sdk.server.core.UAServerApplicationInstance;

import org.opcfoundation.ua.core.NodeClass;

import com.bichler.astudio.opcua.addressspace.model.gen.classes.model.BaseGen;
import com.bichler.astudio.opcua.addressspace.model.gen.classes.model.VariableGen;
import com.bichler.astudio.opcua.addressspace.model.gen.classes.model.VariableTypeGen;
import com.bichler.astudio.opcua.addressspace.model.gen.classes.output.InstanceGen;
import com.bichler.astudio.opcua.addressspace.model.gen.classes.output.java.JavaVariableInstanceGen;

public class VariableTypeGenerator extends OPCGenerator
{
  public VariableTypeGenerator(UAServerApplicationInstance server)
  {
    super(server);
  }

  @Override
  public BaseGen genModel(NodeClass nodeClass, Node node)
  {
    BaseGen gen = null;
    switch (nodeClass)
    {
    // root init variable
    case VariableType:
      gen = new VariableTypeGen((VariableTypeNode) node);
      break;
    // -----------
    case Variable:
      gen = new VariableGen((VariableNode) node);
      break;
    default:
      break;
    }
    return gen;
  }

  @Override
  public boolean isChildAllowed(NodeClass nodeClass)
  {
    switch (nodeClass)
    {
    case Variable:
      return true;
    default:
      return false;
    }
  }

  @Override
  public InstanceGen createInstanceGenerator(BaseGen gen)
  {
    return new JavaVariableInstanceGen(gen, getServer());
  }
}
