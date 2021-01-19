package com.bichler.astudio.opcua.addressspace.model.binary;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.QualifiedName;
import org.opcfoundation.ua.common.NamespaceTable;
import org.opcfoundation.ua.core.IdType;

import com.bichler.astudio.opcua.libs.OPCLibsActivator;

@Deprecated
public class ComplexTypeModelFactory extends CompileFactory
{
  public static final String DEFAULT_PACKAGE = "com.hbsoft.opc.complextypes";
  private static final String DEFAULT_IMPORTS = "import org.opcfoundation.ua.builtintypes.NodeId;\nimport org.opcfoundation.ua.builtintypes.UnsignedInteger;\nimport org.opcfoundation.ua.common.NamespaceTable;\nimport com.hbsoft.comdrv.ComDRVManager;\nimport opc.sdk.ua.classes.*;\n";
  // private static final String INIT_START = "public void init(){";
  // private static final String INIT_END = "}\n}\n";
  private static final String MAINCLASSNAME = "Cmplx";
  private StringBuilder imports;

  public ComplexTypeModelFactory()
  {
    this.imports = new StringBuilder();
    this.imports.append(DEFAULT_IMPORTS);
  }

  public ComplexModelTemplate createComplexObject(NamespaceTable nsTable, QualifiedName nodename, ExpandedNodeId nodeId,
      QualifiedName typename)
  {
    // Only create available complex types
    try
    {
      ClassLoader cl = OPCLibsActivator.getDefault().getClass().getClassLoader();
      // Field f = ClassLoader.class.getDeclaredField("classes");
      // f.setAccessible(true);
      // List<Class> classes = new ArrayList<>((Vector<Class>) f.get(cl));
      // for (Class c : classes) {
      // if (c.getCanonicalName() == null) {
      // continue;
      // }
      //
      // if (c.getCanonicalName().startsWith("opc")) {
      // System.out.println(c.getCanonicalName() + " Klasse: " +
      // c.getName());
      //
      // }
      //
      // }
      // Class<?> c4n2 =
      cl.loadClass("opc.sdk.ua.classes." + typename.getName());
    }
    catch (Exception e)
    {
      return null;
    }
    String uri = nsTable.getUri(nodeId.getNamespaceIndex());
    StringBuilder model = new StringBuilder();
    model.append("{\n");
    model.append("NamespaceTable nsTable = ComDRVManager.getDRVManager().getServer().getNamespaceUris();\n");
    model.append("int nsIndex = nsTable.getIndex(\"" + uri + "\");\n");
    IdType idType = nodeId.getIdType();
    switch (idType)
    {
    case Guid:
      break;
    case Numeric:
      model.append("NodeId nodeId = new NodeId(nsIndex, " + ((Number) nodeId.getValue()).intValue() + ");\n");
      break;
    case Opaque:
      break;
    case String:
      break;
    }
    model.append(typename.getName() + " " + nodename.getName() + " = new " + typename.getName() + "(null);\n");
    model.append(nodename.getName() + ".setNodeId(nodeId);\n");
    model.append("ComDRVManager.getDRVManager().getBaseManager().addNodeState(nodeId, " + nodename.getName() + ");\n");
    model.append("}\n");
    ComplexModelTemplate template = new ComplexModelTemplate(nodeId, uri, nodename.getName(), typename.getName());
    return template;
  }
  // TODO:
  // public void addComplexType() {
  //
  // }
  // public String end() {
  // StringBuilder builder = new StringBuilder();
  // builder.append("package " + DEFAULT_PACKAGE + ";\n\n");
  // builder.append(this.imports.toString());
  // builder.append("\n\n");
  // builder.append("public class " + MAINCLASSNAME + "{\n");
  // builder.append(INIT_START + "\n");
  // // builder.append(main.toString());
  // builder.append(INIT_END);
  // return builder.toString();
  // }

  // public void writeMain(String file, String packagename, String output) {
  //
  // File folderCmplx = new Path(file).toFile();
  // if (!folderCmplx.exists()) {
  // folderCmplx.mkdir();
  // }
  //
  // IPath pathMain = new Path(file).append(MAINCLASSNAME + "." +
  // EXTENSION_JAVA);
  // File f = pathMain.toFile();
  // FileOutputStream fos = null;
  // try {
  // if (f.exists()) {
  // f.delete();
  // }
  //
  // f.createNewFile();
  // fos = new FileOutputStream(f);
  //
  // fos.write(output.getBytes());
  // fos.flush();
  // } catch (IOException e) {
  // e.printStackTrace();
  // } finally {
  // if (fos != null) {
  // try {
  // fos.close();
  // } catch (IOException e) {
  // e.printStackTrace();
  // }
  // }
  // }
  // }
  void writePackageAndImport(BufferedWriter fos) throws IOException
  {
    StringBuilder builder = new StringBuilder();
    builder.append("package " + DEFAULT_PACKAGE + ";\n\n");
    builder.append(this.imports.toString());
    fos.write(builder.toString());
    fos.newLine();
    fos.newLine();
    fos.flush();
  }

  void writeClassStart(BufferedWriter fos, String classname) throws IOException
  {
    fos.write("public class " + classname + "{");
    fos.newLine();
    fos.flush();
  }

  void writeInit(BufferedWriter fos, int methodCount) throws IOException
  {
    fos.write("public void init(){");
    fos.newLine();
    for (int i = 0; i < methodCount; i++)
    {
      fos.write("init" + i + "();");
      fos.newLine();
    }
    fos.write("}");
    fos.newLine();
    fos.flush();
  }

  void writeClassEnd(BufferedWriter fos) throws IOException
  {
    fos.write("}");
    fos.flush();
  }

  void writeComplexTypeModel(BufferedWriter fos, ComplexModelTemplate model) throws IOException
  {
    fos.write(model.toString());
    fos.newLine();
    fos.flush();
  }

  void writeMethodInitTypeStart(BufferedWriter fos, int methodCount) throws IOException
  {
    fos.write("private void init" + methodCount + "(){");
    fos.newLine();
    fos.flush();
  }

  void writeMethodInitTypeEnd(BufferedWriter fos) throws IOException
  {
    fos.write("}");
    fos.newLine();
    fos.newLine();
    fos.flush();
  }

  public void writeMain2(String file, String packagename, List<ComplexModelTemplate> objects2export)
  {
    File folderCmplx = new Path(file).toFile();
    if (!folderCmplx.exists())
    {
      folderCmplx.mkdir();
    }
    IPath pathMain = new Path(file).append(MAINCLASSNAME + "." + EXTENSION_JAVA);
    File f = pathMain.toFile();
    int methodCount = 0;
    BufferedWriter fos = null;
    try
    {
      if (f.exists())
      {
        f.delete();
      }
      f.createNewFile();
      fos = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f)));
      writePackageAndImport(fos);
      writeClassStart(fos, MAINCLASSNAME);
      int currentByteCount = 0;
      boolean isMethod = false;
      for (ComplexModelTemplate model : objects2export)
      {
        int modelByteCount = model.toString().getBytes().length;
        // chunk method
        if (currentByteCount + modelByteCount >= MAX_BYTE_LIMIT_METHOD)
        {
          // close method
          writeMethodInitTypeEnd(fos);
          // open new method
          isMethod = false;
        }
        // initialize method
        if (!isMethod)
        {
          writeMethodInitTypeStart(fos, methodCount);
          currentByteCount = 0;
          methodCount++;
          isMethod = true;
        }
        writeComplexTypeModel(fos, model);
        currentByteCount += modelByteCount;
      }
      // writeMethodInitTypeEnd(fos);
      writeInit(fos, methodCount);
      writeClassEnd(fos);
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
  }
}
