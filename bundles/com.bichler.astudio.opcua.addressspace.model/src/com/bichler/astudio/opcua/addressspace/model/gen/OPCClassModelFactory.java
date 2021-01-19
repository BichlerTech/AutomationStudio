package com.bichler.astudio.opcua.addressspace.model.gen;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Map;
import java.util.Map.Entry;

import opc.sdk.core.node.Node;
import opc.sdk.core.types.TypeInfo;
import opc.sdk.core.types.TypeTable;
import opc.sdk.server.core.UAServerApplicationInstance;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.opcfoundation.ua.builtintypes.NodeId;

import com.bichler.astudio.opcua.addressspace.model.gen.classes.ObjectTypeGenerator;
import com.bichler.astudio.opcua.addressspace.model.gen.classes.VariableTypeGenerator;
import com.bichler.astudio.opcua.addressspace.model.gen.classes.model.BaseGen;
import com.bichler.astudio.opcua.addressspace.model.gen.classes.output.ConstantTemplate;
import com.bichler.astudio.opcua.addressspace.model.gen.classes.output.java.JavaConstantTemplate;

public class OPCClassModelFactory
{
  private final String EXTENSION = ".java";
  private UAServerApplicationInstance server;
  private ObjectTypeGenerator objectTypeGenerator = null;
  private VariableTypeGenerator variableTypeGenerator = null;
  /** configuration for export */
  private String constantsName = "IdentifierGen";
  private String packageName = "opc.sdk.core.classes";

  /**
   * Write all classes to the given output destination
   * 
   * @param server
   */
  public OPCClassModelFactory(UAServerApplicationInstance server)
  {
    Assert.isNotNull(server, "Invalid OPC server should not null!");
    this.objectTypeGenerator = new ObjectTypeGenerator(server);
    this.variableTypeGenerator = new VariableTypeGenerator(server);
    this.server = server;
  }

  public void writeClasses(String destination, String packageName)
  {
    IPath dest = new Path(destination);
    TypeTable typeTable = server.getServerInstance().getTypeTable();
    Map<NodeId, TypeInfo> allTypes = typeTable.getAllTypes();
    JavaConstantTemplate constants = new JavaConstantTemplate();
    constants.setName(this.constantsName);
    for (NodeId nodeId : allTypes.keySet())
    {
      writeCustomClass(dest, packageName, nodeId, constants);
    }
    writeCustomConstants(dest, constants);
  }

  private void writeCustomClass(IPath destination, String packageName, NodeId nodeId, ConstantTemplate constants)
  {
    Node customNode = server.getServerInstance().getAddressSpaceManager().getNodeById(nodeId);
    String name = customNode.getBrowseName().toString();
    IPath genName = destination.append(name + EXTENSION);
    // create file
    File destFile = genName.toFile();
    // remove old file
    if (destFile.exists())
    {
      destFile.delete();
    }
    FileOutputStream fos = null;
    try
    {
      fos = new FileOutputStream(destFile);
      doCreate(fos, packageName, customNode, name, constants);
    }
    catch (FileNotFoundException e)
    {
      e.printStackTrace();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    finally
    {
      if (fos != null)
      {
        try
        {
          fos.close();
        }
        catch (IOException e)
        {
          e.printStackTrace();
        }
      }
    }
    // TODO: REMOVE only testcase
    switch (customNode.getNodeClass())
    {
    case Object:
    case ObjectType:
    case Variable:
    case VariableType:
    case Method:
      break;
    default:
      destFile.delete();
      break;
    }
  }

  private void writeCustomConstants(IPath destination, ConstantTemplate constants)
  {
    IPath genDest = destination.append(constantsName + EXTENSION);
    File destFile = genDest.toFile();
    // remove old file
    if (destFile.exists())
    {
      destFile.delete();
    }
    // create new file
    if (!destFile.exists())
    {
      try
      {
        destFile.createNewFile();
      }
      catch (IOException e)
      {
        e.printStackTrace();
      }
    }
    BufferedWriter bfos = null;
    try
    {
      FileOutputStream fos = new FileOutputStream(destFile);
      bfos = new BufferedWriter(new OutputStreamWriter(fos));
      // write package
      bfos.write("package " + packageName + ";");
      bfos.newLine();
      // write imports
      bfos.write("import org.opcfoundation.ua.builtintypes.NodeId;");
      bfos.newLine();
      bfos.write("import org.opcfoundation.ua.builtintypes.UnsignedInteger;");
      bfos.newLine();
      bfos.write("import opc.sdk.core.node.NodeIdUtil;");
      bfos.newLine();
      // write class declaration
      bfos.write("public class " + this.constantsName + " {");
      bfos.newLine();
      // write constants
      for (Entry<NodeId, String> entry : constants.entrySet())
      {
        NodeId nodeId = entry.getKey();
        String name = entry.getValue();
        bfos.write("public static final NodeId " + name + " = init(" + nodeId.getNamespaceIndex() + ","
            + nodeId.getValue() + ");");
      }
      bfos.newLine();
      // write init method
      bfos.write("static NodeId init(Integer index, Object value){");
      bfos.newLine();
      bfos.write("return NodeIdUtil.createNodeId(index, value);");
      bfos.newLine();
      bfos.write("}");
      // end class
      bfos.write("}");
    }
    catch (FileNotFoundException e)
    {
      e.printStackTrace();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    finally
    {
      if (bfos != null)
      {
        try
        {
          bfos.close();
        }
        catch (IOException e)
        {
          e.printStackTrace();
        }
      }
    }
  }

  private void doCreate(OutputStream out, String packageName, Node customNode, String className,
      ConstantTemplate constants) throws IOException
  {
    if (customNode == null)
    {
      return;
    }
    switch (customNode.getNodeClass())
    {
    case DataType:
      doCreateDataType(out, packageName, customNode, className, constants);
      break;
    case ObjectType:
      doCreateObjectType(out, packageName, customNode, className, constants);
      break;
    case VariableType:
      doCreateVariableType(out, packageName, customNode, className, constants);
      break;
    case ReferenceType:
      doCreateReferenceType(out, packageName, customNode, className, constants);
      break;
    default:
      break;
    }
  }

  private void doCreateDataType(OutputStream fos, String packageName, Node customNode, String className,
      ConstantTemplate constants) throws IOException
  {
  }

  private void doCreateObjectType(OutputStream out, String packageName, Node customNode, String className,
      ConstantTemplate constants) throws IOException
  {
    BaseGen model = this.objectTypeGenerator.build(customNode, className, constants);
    // set package name
    model.setPackage(packageName);
    this.objectTypeGenerator.generate(out, model, className, constants);
  }

  private void doCreateVariableType(OutputStream fos, String packageName, Node customNode, String className,
      ConstantTemplate constants) throws IOException
  {
    BaseGen model = this.variableTypeGenerator.build(customNode, className, constants);
    // set package name
    model.setPackage(packageName);
    this.variableTypeGenerator.generate(fos, model, className, constants);
  }

  private void doCreateReferenceType(OutputStream fos, String packageName, Node customNode, String className,
      ConstantTemplate constants) throws IOException
  {
  }

  public void setConstantsName(String constantsName)
  {
    this.constantsName = constantsName;
  }

  public void setPackageName(String packageName)
  {
    this.packageName = packageName;
  }
}
