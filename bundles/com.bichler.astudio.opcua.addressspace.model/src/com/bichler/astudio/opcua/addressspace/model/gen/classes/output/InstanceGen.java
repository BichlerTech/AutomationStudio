package com.bichler.astudio.opcua.addressspace.model.gen.classes.output;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import opc.sdk.core.classes.ua.base.BaseMethodGen;
import opc.sdk.server.core.UAServerApplicationInstance;

import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.QualifiedName;

import com.bichler.astudio.opcua.addressspace.model.gen.classes.model.BaseGen;
import com.bichler.astudio.opcua.addressspace.model.gen.classes.model.helper.def.IdentifierVarGenHelper;
import com.bichler.astudio.opcua.addressspace.model.gen.classes.model.helper.def.InstanceVarGenHelper;
import com.bichler.astudio.opcua.addressspace.model.gen.classes.model.helper.java.InfoContext;
import com.bichler.astudio.opcua.addressspace.model.gen.classes.model.helper.java.MethodGenHelper;

public abstract class InstanceGen
{
  /** opc ua information */
  private BaseGen gen = null;
  private UAServerApplicationInstance server;

  public InstanceGen(BaseGen gen, UAServerApplicationInstance server)
  {
    this.gen = gen;
    this.server = server;
  }

  protected UAServerApplicationInstance getServer()
  {
    return this.server;
  }

  public BaseGen getInstance2Generate()
  {
    return this.gen;
  }

  public abstract void additionalMethods(InfoContext context, List<MethodGenHelper> methods);

  /**
   * Build Model
   * 
   * @param constants
   *          **
   */
  public abstract InstanceTemplate prepare(InfoContext context, UAServerApplicationInstance server,
      ConstantTemplate constants);

  /**
   * Write model**
   * 
   * @param out
   * @param constants
   */
  public void output(OutputStream out, InstanceTemplate template) throws IOException
  {
    template.output(out);
  }

  public void genModel(BufferedWriter fos, Map<NodeId, String> constants) throws IOException
  {
    streamPackage(fos);
    streamImports(fos);
    streamHeader(fos);
    streamIdentifiers(fos, constants);
    streamInstances(fos, constants);
    streamConstructors(fos);
    streamGetSetInstances(fos);
    // TODO: Currently not needed
    // streamCloneSection(fos);
    streamGetSetIdentifiers(fos);
    streamToString(fos);
    streamEnd(fos);
  }

  /**
   * Prepare all information needed to generate a model file
   * 
   * @param typeTable
   */
  public void buildModel(UAServerApplicationInstance server)
  {
    List<String> imports = new ArrayList<>();
    prepareHeader(server, imports);
    prepareInstanceVariables(server, imports);
    prepareConstructors(imports);
    prepareClone(imports);
    prepareToString(imports);
    // TODO: Dynamic imports
    imports.add("import org.opcfoundation.ua.builtintypes.NodeId;");
    // this.genImports.addAll(imports);
  }

  private void prepareClone(List<String> imports)
  {
    // this.genClon = new ClonGenHelper();
  }

  private void prepareConstructors(List<String> imports)
  {
    // ConstructorGenHelper helper_none = new ConstructorGenHelper(this.gen);
    // ConstructorGenHelper helper_initialized = new ConstructorGenHelper(
    // this.gen);
    // this.genConstructors.add(helper_none);
    // this.genConstructors.add(helper_initialized);
  }

  private void prepareHeader(UAServerApplicationInstance server, List<String> imports)
  {
    // String className = this.gen.getNode().getBrowseName().toString();
    // String superType = "";
    // NodeId supertypeId = server.getServerInstance().getTypeTable()
    // .findSuperType(this.gen.getNode().getNodeId());
    // Node supertypeNode = server.getServerInstance()
    // .getAddressSpaceManager().getNodeById(supertypeId);
    // if (supertypeNode != null) {
    // superType = supertypeNode.getBrowseName().toString();
    // } else {
    // // hook into opc ua class model
    // switch (this.gen.getNode().getNodeClass()) {
    // case ObjectType:
    // superType = BaseObjectTypeGen.class.getName();
    // break;
    // case VariableType:
    // superType = BaseVariableTypeGen.class.getName();
    // break;
    // case ReferenceType:
    // superType = BaseReferenceTypeGen.class.getName();
    // break;
    // }
    // }
    // this.genHeader = "public class " + className + " extends " +
    // superType
    // + " {";
  }

  private void prepareToString(List<String> imports)
  {
    // this.genToString = new ToStringGenHelper();
  }

  private void prepareInstanceVariables(UAServerApplicationInstance server, List<String> imports)
  {
    // instances
    List<BaseGen> children = this.gen.getChildren();
    for (BaseGen child : children)
    {
      InstanceVarGenHelper helper = new InstanceVarGenHelper();
      switch (child.getNode().getNodeClass())
      {
      case Method:
        helper.setTypeId(server, ExpandedNodeId.NULL);
        helper.setType(new QualifiedName(BaseMethodGen.class.getName()));
        break;
      default:
        ExpandedNodeId typeId = child.prepareAsVariable(server);
        helper.setTypeId(server, typeId);
        break;
      }
      helper.setHelper(child);
      // this.genInstances.add(helper);
    }
    // identifiers
    NodeId nodeId = this.gen.getNode().getNodeId();
    IdentifierVarGenHelper helper = new IdentifierVarGenHelper();
    helper.setNodeId(nodeId);
    // this.genIdentifiers.add(helper);
  }

  void streamImports(BufferedWriter fos) throws IOException
  {
    // for (String imports : this.genImports) {
    // fos.write(imports);
    // fos.newLine();
    // }
    fos.newLine();
  }

  void streamPackage(BufferedWriter fos) throws IOException
  {
    fos.write("package opc.sdk.server.core.classes;");
    fos.newLine();
  }

  void streamToString(BufferedWriter fos) throws IOException
  {
    // this.genToString.streamToString(fos);
  }

  void streamGetSetIdentifiers(BufferedWriter fos) throws IOException
  {
    // for (VarGenHelper instance : this.genIdentifiers) {
    // instance.streamVarGetterSetter(fos);
    // }
  }

  void streamCloneSection(BufferedWriter fos) throws IOException
  {
    // this.genClon.streamClone(fos);
  }

  void streamGetSetInstances(BufferedWriter fos) throws IOException
  {
    // for (VarGenHelper instance : this.genInstances) {
    // instance.streamVarGetterSetter(fos);
    // }
  }

  void streamConstructors(BufferedWriter fos) throws IOException
  {
    // for (ConstructorGenHelper constructor : this.genConstructors) {
    // constructor.streamConstructor(fos);
    // }
  }

  void streamInstances(BufferedWriter fos, Map<NodeId, String> constants) throws IOException
  {
    // for (VarGenHelper instance : this.genInstances) {
    // instance.streamVarDeclaration(fos, constants);
    // }
  }

  void streamIdentifiers(BufferedWriter fos, Map<NodeId, String> constants) throws IOException
  {
    // for (VarGenHelper identifier : this.genIdentifiers) {
    // identifier.streamVarDeclaration(fos, constants);
    // }
  }

  void streamHeader(BufferedWriter fos) throws IOException
  {
    // fos.write(this.genHeader);
    // fos.newLine();
  }

  void streamEnd(BufferedWriter fos) throws IOException
  {
    fos.write("}");
  }
}
