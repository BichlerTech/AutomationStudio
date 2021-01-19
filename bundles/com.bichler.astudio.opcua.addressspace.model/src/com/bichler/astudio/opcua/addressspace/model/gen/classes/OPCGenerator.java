package com.bichler.astudio.opcua.addressspace.model.gen.classes;

import java.io.IOException;
import java.io.OutputStream;

import opc.sdk.core.node.Node;
import opc.sdk.server.core.UAServerApplicationInstance;

import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.core.NodeClass;

import com.bichler.astudio.opcua.addressspace.model.gen.classes.model.BaseGen;
import com.bichler.astudio.opcua.addressspace.model.gen.classes.model.helper.java.InfoContext;
import com.bichler.astudio.opcua.addressspace.model.gen.classes.output.ConstantTemplate;
import com.bichler.astudio.opcua.addressspace.model.gen.classes.output.InstanceGen;
import com.bichler.astudio.opcua.addressspace.model.gen.classes.output.InstanceTemplate;

public abstract class OPCGenerator
{
  private UAServerApplicationInstance server = null;

  public OPCGenerator(UAServerApplicationInstance server)
  {
    this.server = server;
  }

  /**
   * Generates an OPC generator model tree.
   * 
   * @param node
   * @param className
   * @param constants
   * 
   */
  public BaseGen build(Node node, String className, ConstantTemplate constants)
  {
    // constant id for class type
    registerTypeInConstants(constants, node.getNodeId(), className);
    BaseGen root = genModel(node.getNodeClass(), node);
    generateChildren(root, node);
    return root;
  }

  public void generate(OutputStream out, BaseGen model, String className, ConstantTemplate constants) throws IOException
  {
    InfoContext context = preGenerate();
    InstanceGen instance = createInstanceGenerator(model);
    // build model
    InstanceTemplate outModel = instance.prepare(context, this.server, constants);
    // output model
    instance.output(out, outModel);
  }

  protected UAServerApplicationInstance getServer()
  {
    return this.server;
  }

  private InfoContext preGenerate()
  {
    return new InfoContext();
  }

  private void registerTypeInConstants(ConstantTemplate constants, NodeId nodeId, String className)
  {
    constants.put(nodeId, className);
  }

  /**
   * Add child to parent.
   * 
   * @param parent
   *          parent
   * @param nodeClass
   *          child node class
   * @param node
   *          child node
   * @return
   */
  private BaseGen gen(BaseGen parent, NodeClass nodeClass, Node node)
  {
    BaseGen model = genModel(nodeClass, node);
    parent.addChild(model);
    return model;
  }

  private void generateChildren(BaseGen parent, Node node)
  {
    // get node id
    NodeId nodeId = node.getNodeId();
    // find children
    Node[] children = this.server.getServerInstance().getAddressSpaceManager().findChildren(nodeId);
    // append children
    for (Node child : children)
    {
      if (isChildAllowed(child.getNodeClass()))
      {
        // generate model
        BaseGen current = gen(parent, child.getNodeClass(), child);
        generateChildren(current, child);
      }
    }
  }

  public abstract InstanceGen createInstanceGenerator(BaseGen gen);

  public abstract boolean isChildAllowed(NodeClass nodeClass);

  public abstract BaseGen genModel(NodeClass nodeClass, Node node);
}
