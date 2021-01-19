package com.bichler.astudio.utils.constants;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.osgi.framework.Bundle;

public class HBStudioUtils
{
  /** common name of sections */
  public static final String GENERAL_SECTION = "general";
//  public static final String OPC_SECTION = "opc_ua_server";
//  public static final String HMI_SECTION = "hmi_server";
//  public static final String IEC_SECTION = "iec_projects";
//  public static final String TSN_SECTION = "tsn_projects";
  /** common name of folders */
  public static final String NAME_SERVERS = "servers";
  public static final String NAME_RUNTIME = "runtime";
  public static final String NAME_DRIVERS = "drivers";
  public static final String NAME_OPCUA = "opcua";

  public static Map<String, Map<String, String>> createWorkspaceRuntime(String workspaceRootPath)
  {
    // common properties
    Map<String, Map<String, String>> properties = new HashMap<>();
    // comet studio bundle
//    Bundle bundle = ASActivator.getDefault().getBundle();
    try
    {
      // installation directory of comet studio (not used)
//      File rootFolder = findCometStudioFiles(bundle, "");
//      String rootDirectory = rootFolder.toURI().getPath();
      // config path (currently HMI color bar)
//      File configFolder = findCometStudioFiles(bundle, "config");
//      String configDirectory = configFolder.toURI().getPath();
      // general properties
      Map<String, String> generalproperties = new HashMap<>();
//      generalproperties.put(StudioConstants.StudioFolder, rootDirectory);
//      generalproperties.put(StudioConstants.ConfigFolder, configDirectory);
      properties.put(GENERAL_SECTION, generalproperties);
      // check opcua structure
//      IPath path = new Path(workspaceRootPath);
    //  IPath opcPath = path.append(OPC_SECTION);
    //  boolean goodOPC = checkSection(opcPath);
      // opc ua properties
//      Map<String, String> opcproperties = new HashMap<>();
//      opcproperties.put(OPCUAConstants.OPCUARuntime, opcPath.toOSString());
//      opcproperties.put(OPCUAConstants.CometOPCUAServersPath, "servers");
//      opcproperties.put(OPCUAConstants.CometOPCUARuntimePath, "runtime");
//      opcproperties.put(OPCUAConstants.CometOPCUADriversFolder, "drivers");
//      opcproperties.put(OPCUAConstants.CometOPCUADriverConfigFile, "driver.com");
    //  properties.put(OPC_SECTION, opcproperties);
    //  if (!goodOPC)
      {
//        createSection(OPC_SECTION, opcPath);
      }
      // config path (currently HMI color bar)
//      File svgEditorFolder = findCometStudioFiles(HMIActivator.getDefault().getBundle(), "svgEditor");
//      String svgEditorDirectory = "";
//      if (svgEditorFolder != null)
//        svgEditorFolder.toURI().getPath();
      // check hmi structure
     // IPath hmiPath = path.append(HMI_SECTION);
//      Map<String, String> hmiproperties = new HashMap<>();
//      hmiproperties.put(HMIConstants.HMIRuntime, hmiPath.toOSString());
//      hmiproperties.put(HMIConstants.CometHMIRuntimePath, "runtime");
//      hmiproperties.put(HMIConstants.CometHMIServersPath, "servers");
//      hmiproperties.put(HMIConstants.HMI2DEditorPath, svgEditorDirectory);
//      hmiproperties.put(HMIConstants.HMI2DEditorFile, "svg-editor.html");
    //  properties.put(HMI_SECTION, hmiproperties);
    //  boolean goodHMI = checkSection(hmiPath);
    //  if (!goodHMI)
      {
//        createSection(HMI_SECTION, hmiPath);
      }
      // check iec structure
   //   IPath iecPath = path.append(IEC_SECTION);
//      Map<String, String> iecproperties = new HashMap<>();
//      iecproperties.put(IEC61131Constants.IECRuntime, iecPath.toOSString());
//      iecproperties.put(IEC61131Constants.IECRuntimePath, "runtime");
//      iecproperties.put(IEC61131Constants.IECProjectsPath, "projects");
   //   properties.put(IEC_SECTION, iecproperties);
  //    boolean goodIEC = checkSection(iecPath);
  //    if (!goodIEC)
      {
//        createSection(IEC_SECTION, iecPath);
      }
      // check tsn structure
  //    IPath tsnPath = path.append(TSN_SECTION);
//      Map<String, String> tsnproperties = new HashMap<>();
//      tsnproperties.put(TSNConstants.TSNRuntime, tsnPath.toOSString());
//      tsnproperties.put(TSNConstants.TSNRuntimePath, "runtime");
//      tsnproperties.put(TSNConstants.TSNProjectsPath, "projects");
  //    properties.put(TSN_SECTION, tsnproperties);
  //    boolean goodTSN = checkSection(tsnPath);
  //    if (!goodTSN)
      {
//        createSection(TSN_SECTION, tsnPath);
      }
    }
//    catch (IOException e)
//    {
//      e.printStackTrace();
//    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return properties;
  }

  public static void initWorkspacePreferences(Map<String, Map<String, String>> csWorkspacePreferences)
  {
//    for (Entry<String, Map<String, String>> entry : csWorkspacePreferences.entrySet())
//    {
//      String category = entry.getKey();
//      IPreferenceStore store = null;
//      if (GENERAL_SECTION.equals(category))
//      {
//        store = ComponentsActivator.getDefault().getPreferenceStore();
//      }
//      else if (OPC_SECTION.equals(category))
//      {
//        store = OPCUAActivator.getDefault().getPreferenceStore();
//      }
//      else if (HMI_SECTION.equals(category))
//      {
//        store = HMIActivator.getDefault().getPreferenceStore();
//      }
//      else if (IEC_SECTION.equals(category))
//      {
//        store = IEC61131Activator.getDefault().getPreferenceStore();
//      }
//      else if (TSN_SECTION.equals(category))
//      {
//        store = TSNActivator.getDefault().getPreferenceStore();
//      }
//      fillPreferenceStore(store, entry.getValue());
//    }
  }



  public static File findCometStudioFiles(Bundle bundle, String relativePath) throws IOException
  {
    URL url = FileLocator.find(bundle, Path.ROOT.append(relativePath), null);
    URL relativeUrl = FileLocator.toFileURL(url);
    String[] subDirs = relativeUrl.getPath().replaceAll("\\\\", "/").split(File.separator.replaceAll("\\\\", "/"));
    List<String> subdirs = new ArrayList<>();
    for (String dir : subDirs)
    {
      if (dir.isEmpty())
        continue;
      if (dir.compareTo("..") == 0)
        subdirs.remove(subdirs.size() - 1);
      else
        subdirs.add(dir);
    }
    IPath path = new Path(subdirs.get(0) + File.separator);
    for (String entry : subdirs)
    {
      path = path.append(entry);
    }
    File folder = new File(path.toString());
    folder.exists();
    // if (relativeUrl != null)
    // folder = new File(relativeUrl.getFile());
    return folder;
  }
}
