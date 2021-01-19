package com.bichler.astudio.opcua.addressspace.model.gen.classes;

import opc.sdk.core.node.MethodNode;
import opc.sdk.core.node.Node;
import opc.sdk.core.node.ObjectNode;
import opc.sdk.core.node.ObjectTypeNode;
import opc.sdk.core.node.VariableNode;
import opc.sdk.server.core.UAServerApplicationInstance;

import org.opcfoundation.ua.core.NodeClass;

import com.bichler.astudio.opcua.addressspace.model.gen.classes.model.BaseGen;
import com.bichler.astudio.opcua.addressspace.model.gen.classes.model.MethodGen;
import com.bichler.astudio.opcua.addressspace.model.gen.classes.model.ObjectGen;
import com.bichler.astudio.opcua.addressspace.model.gen.classes.model.ObjectTypeGen;
import com.bichler.astudio.opcua.addressspace.model.gen.classes.model.VariableGen;
import com.bichler.astudio.opcua.addressspace.model.gen.classes.output.InstanceGen;
import com.bichler.astudio.opcua.addressspace.model.gen.classes.output.java.JavaObjectInstanceGen;

public class ObjectTypeGenerator extends OPCGenerator
{
  public ObjectTypeGenerator(UAServerApplicationInstance server)
  {
    super(server);
  }

  @Override
  public BaseGen genModel(NodeClass nodeClass, Node node)
  {
    BaseGen gen = null;
    switch (nodeClass)
    {
    // root init object
    case ObjectType:
      gen = new ObjectTypeGen((ObjectTypeNode) node);
      break;
    // -----------
    case Object:
      gen = new ObjectGen((ObjectNode) node);
      break;
    case Variable:
      gen = new VariableGen((VariableNode) node);
      break;
    case Method:
      gen = new MethodGen((MethodNode) node);
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
    case Object:
    case Variable:
    case Method:
      return true;
    default:
      return false;
    }
  }

  @Override
  public InstanceGen createInstanceGenerator(BaseGen gen)
  {
    return new JavaObjectInstanceGen(gen, getServer());
  }
}
