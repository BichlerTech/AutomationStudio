package com.bichler.astudio.opcua.addressspace.model.gen.classes.output.java;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import opc.sdk.core.classes.ua.base.BaseMethodGen;
import opc.sdk.core.classes.ua.base.BaseObjectTypeGen;
import opc.sdk.core.classes.ua.base.BaseReferenceTypeGen;
import opc.sdk.core.classes.ua.base.BaseVariableTypeGen;
import opc.sdk.core.node.Node;
import opc.sdk.server.core.UAServerApplicationInstance;

import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.QualifiedName;

import com.bichler.astudio.opcua.addressspace.model.gen.classes.model.BaseGen;
import com.bichler.astudio.opcua.addressspace.model.gen.classes.model.helper.java.ConstructorGenHelper;
import com.bichler.astudio.opcua.addressspace.model.gen.classes.model.helper.java.DocumentGenHelper;
import com.bichler.astudio.opcua.addressspace.model.gen.classes.model.helper.java.InfoContext;
import com.bichler.astudio.opcua.addressspace.model.gen.classes.model.helper.java.MethodGenHelper;
import com.bichler.astudio.opcua.addressspace.model.gen.classes.model.helper.java.ToStringGenHelper;
import com.bichler.astudio.opcua.addressspace.model.gen.classes.model.helper.java.VariableGenHelper;
import com.bichler.astudio.opcua.addressspace.model.gen.classes.model.helper.java.method.MethodParameter;
import com.bichler.astudio.opcua.addressspace.model.gen.classes.output.ConstantTemplate;
import com.bichler.astudio.opcua.addressspace.model.gen.classes.output.InstanceGen;
import com.bichler.astudio.opcua.addressspace.model.gen.classes.output.InstanceTemplate;

public class JavaTemplate extends InstanceTemplate
{
  private UAServerApplicationInstance server;
  // package
  private String packageName;
  // imports
  private List<String> imports;
  // class start declaration
  private DocumentGenHelper classDeclarationHeader;
  // variables
  private List<VariableGenHelper> variables;
  // constructors
  private List<ConstructorGenHelper> constructors;
  // methods
  private List<MethodGenHelper> methods;

  /**
   * Java model to generate.
   * 
   * @param server
   */
  public JavaTemplate(UAServerApplicationInstance server)
  {
    this.server = server;
    this.imports = new ArrayList<>();
    this.variables = new ArrayList<>();
    this.constructors = new ArrayList<>();
    this.methods = new ArrayList<>();
  }

  @Override
  public void buildStructure(InfoContext context, InstanceGen instanceGen, ConstantTemplate constants)
  {
    this.packageName = instanceGen.getInstance2Generate().getPackage();
    context.setPackageName(this.packageName);
    createClassDeclaration(instanceGen);
    createConstructors(instanceGen);
    createIdentifierDefinition(instanceGen, constants);
    createInstanceVariables(instanceGen);
    createMethods(context, instanceGen);
  }

  @Override
  public void output(OutputStream out) throws IOException
  {
    BufferedWriter fos = new BufferedWriter(new OutputStreamWriter(out));
    streamPackage(fos);
    streamImports(fos);
    streamHeader(fos);
    streamVariables(fos);
    streamConstructors(fos);
    streamMethods(fos);
    streamEnd(fos);
  }

  /**
   * Creates the start class section and extends from its required parent.
   * 
   * @param instanceGen
   */
  private void createClassDeclaration(InstanceGen instanceGen)
  {
    String className = instanceGen.getInstance2Generate().getNode().getBrowseName().toString();
    String superType = "";
    NodeId supertypeId = this.server.getServerInstance().getTypeTable()
        .findSuperType(instanceGen.getInstance2Generate().getNode().getNodeId());
    Node supertypeNode = this.server.getServerInstance().getAddressSpaceManager().getNodeById(supertypeId);
    if (supertypeNode != null)
    {
      superType = supertypeNode.getBrowseName().toString();
    }
    else
    {
      // TODO hook into opc ua class model
      switch (instanceGen.getInstance2Generate().getNode().getNodeClass())
      {
      case ObjectType:
        superType = BaseObjectTypeGen.class.getName();
        break;
      case VariableType:
        superType = BaseVariableTypeGen.class.getName();
        break;
      case ReferenceType:
        superType = BaseReferenceTypeGen.class.getName();
        break;
      default:
        break;
      }
    }
    this.classDeclarationHeader = JavaGeneratorUtil.constuctJavaClassStart(className, superType);
  }

  private void createConstructors(InstanceGen instanceGen)
  {
    Node node = instanceGen.getInstance2Generate().getNode();
    ConstructorGenHelper helper = new ConstructorGenHelper(node.getBrowseName().toString());
    this.constructors.add(helper);
  }

  private void createIdentifierDefinition(InstanceGen instanceGen, ConstantTemplate constants)
  {
    // TYPEID of the current class
    NodeId nodeId = instanceGen.getInstance2Generate().getNode().getNodeId();
    // value of the variable
    String declaration;
    if ((declaration = constants.get(nodeId)) != null)
    {
      declaration = constants.getName() + "." + declaration;
    }
    else
    {
      declaration = nodeId.toString();
    }
    VariableGenHelper idVariable = new VariableGenHelper(JavaGeneratorUtil._public + " " + JavaGeneratorUtil._static,
        NodeId.class.getName(), JavaGeneratorUtil.n_ID, declaration);
    idVariable.setGetter(true);
    idVariable.setGetterName("getTypeId");
    idVariable.setMethodsOverriden(true);
    this.variables.add(idVariable);
  }

  private void createMethods(InfoContext context, InstanceGen instanceGen)
  {
    instanceGen.additionalMethods(context, this.methods);
    // create getter setter for instance variables
    for (VariableGenHelper var : this.variables)
    {
      String name = var.getName();
      String type = var.getType();
      // is getter
      if (var.isGetter())
      {
        MethodGenHelper method = new MethodGenHelper(JavaGeneratorUtil._public, type, name);
        method.setOverride(var.isOverride());
        JavaGeneratorUtil.methodGetter(method, var);
        this.methods.add(method);
      }
      // is setter
      if (var.isSetter())
      {
        // parameter
        MethodParameter parameter = new MethodParameter(type, MethodParameter.DEFAULT_NAME_VALUE);
        MethodGenHelper method = new MethodGenHelper("public", "void", name, parameter);
        method.setOverride(var.isOverride());
        JavaGeneratorUtil.methodSetter(method, var, MethodParameter.DEFAULT_NAME_VALUE);
        this.methods.add(method);
      }
    }
    // create toString()
    createToString(instanceGen);
  }

  private void createToString(InstanceGen instanceGen)
  {
    ToStringGenHelper helper = new ToStringGenHelper(
        instanceGen.getInstance2Generate().getNode().getBrowseName().toString());
    this.methods.add(helper);
  }

  private void createInstanceVariables(InstanceGen instanceGen)
  {
    // instance variables of the current class
    List<BaseGen> children = instanceGen.getInstance2Generate().getChildren();
    for (BaseGen child : children)
    {
      VariableGenHelper helper = null;
      switch (child.getNode().getNodeClass())
      {
      case Method:
        helper = new VariableGenHelper(BaseMethodGen.class.getName(), child.getNode().getBrowseName().toString());
        break;
      default:
        // variable type id
        ExpandedNodeId typeId = child.prepareAsVariable(this.server);
        // variable declaration
        QualifiedName typeName = getBrowsenameFromId(this.server, typeId);
        helper = new VariableGenHelper(typeName.toString(), child.getNode().getBrowseName().toString());
        break;
      }
      // allow getter and setter for instance variable
      helper.setGetter(true);
      helper.setSetter(true);
      this.variables.add(helper);
    }
  }

  private void streamImports(BufferedWriter fos) throws IOException
  {
    for (String imports : this.imports)
    {
      fos.write(imports);
      fos.newLine();
    }
    fos.newLine();
    fos.flush();
  }

  private void streamPackage(BufferedWriter fos) throws IOException
  {
    fos.write("package " + this.packageName + ";");
    fos.newLine();
    fos.flush();
  }

  private void streamConstructors(BufferedWriter fos) throws IOException
  {
    for (ConstructorGenHelper constructor : this.constructors)
    {
      String help = constructor.genCode();
      fos.write(help);
    }
    fos.flush();
  }

  // private void streamIdentifiers(BufferedWriter fos) throws IOException {
  // String identifier = this.typeId.genCode();
  // fos.write(identifier);
  // }
  private void streamHeader(BufferedWriter fos) throws IOException
  {
    String classDeclaration = this.classDeclarationHeader.genCode();
    fos.write(classDeclaration);
    fos.flush();
  }

  private void streamEnd(BufferedWriter fos) throws IOException
  {
    String endClass = this.classDeclarationHeader.endClass();
    fos.write(endClass);
    fos.flush();
  }

  private void streamMethods(BufferedWriter fos) throws IOException
  {
    for (MethodGenHelper method : this.methods)
    {
      String output = method.genCode();
      fos.write(output);
    }
    fos.flush();
  }

  private void streamVariables(BufferedWriter fos) throws IOException
  {
    for (VariableGenHelper instance : this.variables)
    {
      String instanceVar = instance.genCode();
      fos.write(instanceVar);
      // instance.streamVarDeclaration(fos, constants);
    }
    fos.newLine();
    fos.flush();
  }
}
