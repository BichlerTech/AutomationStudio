package com.bichler.astudio.opcua.components.ui;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.handlers.IHandlerService;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.core.IdType;

import com.bichler.astudio.components.ui.file.filter.XMLFileFilter;
import com.bichler.astudio.opcua.components.addressspace.DefinitionBean;
import com.bichler.astudio.opcua.components.addressspace.DefinitionFieldBean;
import com.bichler.astudio.utils.opcua.OpenServerParameter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import opc.sdk.server.core.UAServerApplicationInstance;

public class Studio_ResourceManager {
	public static final String OPCUASERVERJARNAME = "OPC_Server.jar";
	private static String studioConfigFolder = "";
	private static String infoModellerDokuPath = null;
	private static String infoModellerPath = null;
	private static IProgressMonitor currentProgressMonitor;
	private static String servername = null;
	// hashmap with all node infos for documentation
	public static HashMap<NodeId, String> NODE_INFOS = new HashMap<>();
	public static HashMap<NodeId, ArrayList<String>> NODE_EXTENSIONS = new HashMap<>();
	public static HashMap<NodeId, DefinitionBean> DATATYPE_DEFINITIONS = new HashMap<>();
	public static HashMap<String, HashMap<String, String>> INFORMATIONMODEL_DETAILS = new HashMap<>();
	public static HashMap<String, HashMap<NodeId, String>> NODE_CUSTOMATTRIBUTES = new HashMap<>();

	public static void loadNodeCustomAttributes() {
		NODE_CUSTOMATTRIBUTES.clear();
		String param = Studio_ResourceManager.infoModellerPath;
		Path ncaPath = new Path(param);
		String nca = ncaPath.append("nodecustomattributes").toOSString();
		File ncaFile = new File(nca);
		if (!ncaFile.exists()) {
			try {
				ncaFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		try (FileReader reader = new FileReader(ncaFile)) {
			Gson GSON = new GsonBuilder().setPrettyPrinting().create();
			HashMap<String, AbstractMap<String, String>> nodes = GSON.fromJson(reader,
					NODE_CUSTOMATTRIBUTES.getClass());
			if (nodes == null) {
				nodes = new HashMap<>();
			}
			for (Entry<String, AbstractMap<String, String>> entry : nodes.entrySet()) {

				HashMap<NodeId, String> values = NODE_CUSTOMATTRIBUTES.get(entry.getKey());
				if (values == null) {
					values = new HashMap<NodeId, String>();
					NODE_CUSTOMATTRIBUTES.put(entry.getKey(), values);
				}

				for (Entry<String, String> valueEntry : entry.getValue().entrySet()) {
					values.put(NodeId.parseNodeId(valueEntry.getKey()), valueEntry.getValue());
				}
			}
		} catch (IOException e) {
			Logger.getLogger(Studio_ResourceManager.class.getName()).log(Level.SEVERE, e.getMessage(), e);
		}
	}

	public static void persistNodeCustomAttributes() {
		String param = Studio_ResourceManager.infoModellerPath;
		String infoPath = new Path(param).append("nodecustomattributes").toOSString();
		Gson GSON = new GsonBuilder().setPrettyPrinting().create();
		File infos = new File(infoPath);
		if (infos.exists()) {
			try (FileWriter writer = new FileWriter(infos);) {
				if (!infos.exists()) {
					infos.createNewFile();
				}

				GSON.toJson(NODE_CUSTOMATTRIBUTES, writer);
			} catch (IOException ex) {
				Logger.getLogger(Studio_ResourceManager.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
			}
		}
	}

	public static void loadInformationModelDetails() {
		INFORMATIONMODEL_DETAILS.clear();
		String param = Studio_ResourceManager.infoModellerPath;
		Path informationModelPath = new Path(param);
		String informationModelFile = informationModelPath.append("informationmodeldetails").toOSString();
		File infoModelFile = new File(informationModelFile);
		if (!infoModelFile.exists()) {
			try {
				infoModelFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		try (FileReader reader = new FileReader(infoModelFile)) {
			Gson GSON = new GsonBuilder().setPrettyPrinting().create();
			HashMap<String, HashMap<String, String>> nodes = GSON.fromJson(reader, INFORMATIONMODEL_DETAILS.getClass());
			if (nodes == null) {
				nodes = new HashMap<>();
			}
			for (Entry<String, HashMap<String, String>> entry : nodes.entrySet()) {
				INFORMATIONMODEL_DETAILS.put(entry.getKey(), new HashMap<String, String>(entry.getValue()));
			}
		} catch (IOException e) {
			Logger.getLogger(Studio_ResourceManager.class.getName()).log(Level.SEVERE, e.getMessage(), e);
		}
	}

	public static void persistInformationModelDetails() {
		String param = Studio_ResourceManager.infoModellerPath;
		String infoPath = new Path(param).append("informationmodeldetails").toOSString();
		Gson GSON = new GsonBuilder().setPrettyPrinting().create();
		File infos = new File(infoPath);
		if (infos.exists()) {
			try (FileWriter writer = new FileWriter(infos);) {
				if (!infos.exists()) {
					infos.createNewFile();
				}

				GSON.toJson(INFORMATIONMODEL_DETAILS, writer);
			} catch (IOException ex) {
				Logger.getLogger(Studio_ResourceManager.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
			}
		}
	}

	public static void loadDatatypeDefinitions() {
		DATATYPE_DEFINITIONS.clear();
		String param = Studio_ResourceManager.infoModellerPath;
		Path modelPath = new Path(param);
		String definitionFile = modelPath.append("definitions").toOSString();
		File extFile = new File(definitionFile);
		if (!extFile.exists()) {
			try {
				extFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		try (FileReader reader = new FileReader(extFile)) {
			GsonBuilder builder = new GsonBuilder();
			Gson GSON = builder.setPrettyPrinting().create();

			HashMap<String, DefinitionBean> nodes = GSON.fromJson(reader, DATATYPE_DEFINITIONS.getClass());
			if (nodes == null) {
				nodes = new HashMap<>();
			}
			for (Entry<String, DefinitionBean> entry : nodes.entrySet()) {
				DefinitionBean bean = parseDefBean((Map) entry.getValue());
				DATATYPE_DEFINITIONS.put(NodeId.parseNodeId(entry.getKey()), bean);
			}
		} catch (IOException e) {
			Logger.getLogger(Studio_ResourceManager.class.getName()).log(Level.SEVERE, e.getMessage(), e);
		}
	}

	private static DefinitionBean parseDefBean(Map<String, Object> value) {
		DefinitionBean defbean = new DefinitionBean();
		for (Entry<String, Object> entry : value.entrySet()) {
			String key = entry.getKey();

			switch (key) {
			case "definitionName":
				defbean.setDefinitionName((String) entry.getValue());
				break;
			case "fields":
				ArrayList<Map<String, Object>> values = (ArrayList) entry.getValue();
				// fields
				for (Map<String, Object> map : values) {
					// entries
					DefinitionFieldBean field = new DefinitionFieldBean();

					for (Entry<String, Object> fieldEntry : map.entrySet()) {
						switch (fieldEntry.getKey()) {
						case "name":
							field.setName((String) fieldEntry.getValue());
							break;
						case "datatype":
//							NodeId.parseNodeId(fieldEntry.getValue());
							Map<String, Object> id = (Map<String, Object>) fieldEntry.getValue();
							String idType = (String) id.get("type");
							Integer idNs = ((Double) id.get("namespaceIndex")).intValue();
						
							switch (idType) {
							case "Numeric":
								Integer idValue = ((Double) ((Map) id.get("value")).get("value")).intValue();
								field.setDatatype(new NodeId(idNs, idValue));
								break;
							case "String":
								field.setDatatype(new NodeId(idNs, (String) id.get("value")));
								break;
							default:
								break;
							}
							break;
						case "description":
							Object vv = fieldEntry.getValue();
							System.out.println("");
							break;
						case "valueRank":
							field.setValueRank(((Double) fieldEntry.getValue()).intValue());
							break;
						case "value":
							field.setValue(((Double) fieldEntry.getValue()).intValue());
							break;
						}
					}
					defbean.addField(field);
				}
				break;
			}
		}

		return defbean;
	}

	public static void persistDatatypeDefinitions() {
		String param = Studio_ResourceManager.infoModellerPath;
		String infoPath = new Path(param).append("definitions").toOSString();
		Gson GSON = new GsonBuilder().setPrettyPrinting().create();
		File infos = new File(infoPath);
		if (infos.exists()) {
			try (FileWriter writer = new FileWriter(infos);) {
				if (!infos.exists()) {
					infos.createNewFile();
				}

				GSON.toJson(DATATYPE_DEFINITIONS, writer);
			} catch (IOException ex) {
				Logger.getLogger(Studio_ResourceManager.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
			}

		}
	}

	public static void loadNodeExtensions() {
		NODE_EXTENSIONS.clear();
		String param = Studio_ResourceManager.infoModellerPath;
		Path extensionPath = new Path(param);
		String extensionFile = extensionPath.append("extensions").toOSString();
		File extFile = new File(extensionFile);
		if (!extFile.exists()) {
			try {
				extFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		try (FileReader reader = new FileReader(extFile)) {
			Gson GSON = new GsonBuilder().setPrettyPrinting().create();
			HashMap<String, ArrayList<String>> nodes = GSON.fromJson(reader, NODE_EXTENSIONS.getClass());
			if (nodes == null) {
				nodes = new HashMap<>();
			}
			for (Entry<String, ArrayList<String>> entry : nodes.entrySet()) {
				NODE_EXTENSIONS.put(NodeId.parseNodeId(entry.getKey()), entry.getValue());
			}
		} catch (IOException e) {
			Logger.getLogger(Studio_ResourceManager.class.getName()).log(Level.SEVERE, e.getMessage(), e);
		}
	}

	public static void persistNodeExtensions() {
		String param = Studio_ResourceManager.infoModellerPath;
		String infoPath = new Path(param).append("extensions").toOSString();
		Gson GSON = new GsonBuilder().setPrettyPrinting().create();
		File infos = new File(infoPath);
		if (infos.exists()) {
			try (FileWriter writer = new FileWriter(infos);) {
				if (!infos.exists()) {
					infos.createNewFile();
				}

				GSON.toJson(NODE_EXTENSIONS, writer);
			} catch (IOException ex) {
				Logger.getLogger(Studio_ResourceManager.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
			}

		}
	}

	public static void loadNodeInfos() {
		NODE_INFOS.clear();
		String param = Studio_ResourceManager.infoModellerDokuPath;
		Path docuPath = new Path(param);
		File docuFolder = new File(docuPath.toOSString());
		if (!docuFolder.exists()) {
			docuFolder.mkdir();
		}

		String infoPath = docuPath.append("infos.txt").toOSString();
		File infos = new File(infoPath);
		if (!infos.exists())
			try {
				infos.createNewFile();
			} catch (IOException e) {
				Logger.getLogger(Studio_ResourceManager.class.getName()).log(Level.SEVERE, e.getMessage(), e);
			}
		try (FileReader reader = new FileReader(infos);) {
			Gson GSON = new GsonBuilder().setPrettyPrinting().create();
			HashMap<String, String> nodes = GSON.fromJson(reader, NODE_INFOS.getClass());
			// create a map if null
			if (nodes == null) {
				nodes = new HashMap<>();
			}
			for (Entry<String, String> entry : nodes.entrySet()) {
				NODE_INFOS.put(NodeId.parseNodeId(entry.getKey()), entry.getValue());
			}

		} catch (IOException ex) {
			Logger.getLogger(Studio_ResourceManager.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
		}
	}

	public static void persistNodeInfos() {
		String param = Studio_ResourceManager.infoModellerDokuPath;
		String infoPath = new Path(param).append("infos.txt").toOSString();
		Gson GSON = new GsonBuilder().setPrettyPrinting().create();
		File infos = new File(infoPath);
		if (infos.exists()) {
			try (FileWriter writer = new FileWriter(infos);) {
				if (!infos.exists()) {
					infos.createNewFile();
				}

				GSON.toJson(NODE_INFOS, writer);

			} catch (IOException ex) {
				Logger.getLogger(Studio_ResourceManager.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
			}

		}
	}

	public static String getStudioConfigFolder() {
		return studioConfigFolder;
	}

	public static void setStudioConfigFolder(String studioConfigFolder) {
		Studio_ResourceManager.studioConfigFolder = studioConfigFolder;
	}

	public static String getInfoModellerResource() {
		return Studio_ResourceManager.infoModellerPath;
	}

	public static void setInfoModellerResource(String value) {
		Studio_ResourceManager.infoModellerPath = value;
	}

	public static void setInfoModellerDokuResource(String value) {
		Studio_ResourceManager.infoModellerDokuPath = value;
	}

	private static final Map<String, UAServerApplicationInstance> opcservers = new HashMap<String, UAServerApplicationInstance>();

	public static UAServerApplicationInstance getOrNewOPCUAServer(String serverName, String configuration,
			String certificates, String localized, URL parentModelFolder) throws ExecutionException {
		// create new instance
		IHandlerService handlerService = (IHandlerService) PlatformUI.getWorkbench().getService(IHandlerService.class);
		ICommandService commandService = (ICommandService) PlatformUI.getWorkbench().getService(ICommandService.class);
		Command cmdStartServer = commandService.getCommand("com.bichler.astudio.opcua.modeler.startServer");
		try {
			StringBuilder b = new StringBuilder();
			if (parentModelFolder != null) {
				File models = new File(FileLocator.toFileURL(parentModelFolder).toURI());
				for (File model : models.listFiles(new XMLFileFilter())) {
					if (!model.isHidden() && !model.getPath().endsWith("_types.xml")) {
						b.append(model.toPath().toString());
						b.append(";");
					}
				}
				b = b.deleteCharAt(b.length() - 1);
			}
			OpenServerParameter parameter = new OpenServerParameter();
			parameter.setServername(serverName);
			parameter.setConfiguration(configuration);
			parameter.setCertificates(certificates);
			parameter.setLocalization(localized);
			parameter.setModel(b.toString());
			ExecutionEvent event = handlerService.createExecutionEvent(cmdStartServer, null);
			IEvaluationContext ac = (IEvaluationContext) event.getApplicationContext();
			ac.getParent().addVariable(OpenServerParameter.PARAMETER_ID, parameter);
			try {
				cmdStartServer.executeWithChecks(event);
			} catch (ExecutionException e) {
				Logger.getLogger(Studio_ResourceManager.class.getName()).log(Level.SEVERE, e.getMessage());
				throw e;
			} catch (NotDefinedException | NotEnabledException | NotHandledException e) {
				Logger.getLogger(Studio_ResourceManager.class.getName()).log(Level.SEVERE, e.getMessage());
			}
		} catch (IOException | URISyntaxException e) {
			Logger.getLogger(Studio_ResourceManager.class.getName()).log(Level.SEVERE, e.getMessage());
		}
		return getOPCUAServerInstance(serverName);
	}

	public static UAServerApplicationInstance getOPCUAServerInstance(String serverName) {
		return opcservers.get(serverName);
	}

	public static UAServerApplicationInstance removeOPCUAServerInstance(String serverName) {
		return opcservers.remove(serverName);
	}

	public static void addOPCUAServer(String servername, UAServerApplicationInstance instance) {
		opcservers.put(servername, instance);
	}

	public static IProgressMonitor getProgressMonitor() {
		return currentProgressMonitor;
	}

	public static void setProgressMonitor(IProgressMonitor monitor) {
		currentProgressMonitor = monitor;
	}

	public static void setServerName(String serverName) {
		servername = serverName;
	}

	public static String getServerName() {
		return servername;
	}

	public static String getInfoModellerDokuPath() {
		return infoModellerDokuPath;
	}

	public static void setInfoModellerDokuPath(String infoModellerDokuPath) {
		Studio_ResourceManager.infoModellerDokuPath = infoModellerDokuPath;
	}
}
