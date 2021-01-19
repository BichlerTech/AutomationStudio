package com.bichler.astudio.opcua.opcmodeler.singletons;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.progress.IProgressService;
import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.StatusCode;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.common.NamespaceTable;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.AddNodesItem;
import org.opcfoundation.ua.core.AddNodesResult;
import org.opcfoundation.ua.core.AddReferencesItem;
import org.opcfoundation.ua.core.BrowseDescription;
import org.opcfoundation.ua.core.BrowseDirection;
import org.opcfoundation.ua.core.BrowseResult;
import org.opcfoundation.ua.core.BrowseResultMask;
import org.opcfoundation.ua.core.DeleteNodesItem;
import org.opcfoundation.ua.core.DeleteReferencesItem;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.core.ReadValueId;
import org.opcfoundation.ua.core.TimestampsToReturn;
import org.w3c.dom.Document;

import com.bichler.astudio.licensemanagement.LicManActivator;
import com.bichler.astudio.licensemanagement.exception.ASStudioLicenseException;
import com.bichler.astudio.opcua.addressspace.model.tool.OPCProgressMonitor;
import com.bichler.astudio.opcua.addressspace.xml.exporter.ModelV2Exporter;
import com.bichler.astudio.opcua.components.ui.Studio_ResourceManager;
import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.opcua.opcmodeler.commands.NamespaceTableChangeParameter;
import com.bichler.astudio.opcua.opcmodeler.constants.DesignerConstants;
import com.bichler.astudio.opcua.opcmodeler.dialogs.utils.MCPreferenceStoreUtil;
import com.bichler.astudio.opcua.opcmodeler.exporter.InformationModelParser;
import com.bichler.astudio.opcua.opcmodeler.exporter.OPCXMLWriter;
import com.bichler.astudio.opcua.opcmodeler.language.LanguageCSVReader;
import com.bichler.astudio.opcua.opcmodeler.language.LanguageCSVWriter;
import com.bichler.astudio.opcua.opcmodeler.singletons.type.ServerModelTypeMapper;
import com.bichler.astudio.opcua.opcmodeler.singletons.type.ServerTypeModel;
import com.bichler.astudio.opcua.opcmodeler.utils.extern.DesignerUtils;
import com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.ModelBrowserView;
import com.bichler.astudio.utils.internationalization.CustomString;

import opc.sdk.core.application.ApplicationConfiguration;
import opc.sdk.core.language.LanguageItem;
import opc.sdk.core.newApplication.NamespaceItem;
import opc.sdk.core.node.Node;
import opc.sdk.core.persistence.xml.SaxNodeWriter;
import opc.sdk.server.core.OPCInternalServer;
import opc.sdk.server.core.UAServerApplicationInstance;
import opc.server.hbserver.OPCUADriverServer;

public final class ServerInstance {
	/**
	 * Current running opc ua server instance
	 */
	private static UAServerApplicationInstance server = null;
	/**
	 * Type mapper
	 */
	private static ServerModelTypeMapper typeMapper = new ServerModelTypeMapper();

	public static AddNodesResult[] addNode(AddNodesItem[] nodes, boolean includeChildren)
			throws ServiceResultException {
		try {
			LicManActivator.getDefault().getLicenseManager().getLicense().validateAddNodes(nodes.length, false);
		} catch (ASStudioLicenseException e) {
			// prevents to add nodes
			Logger.getLogger(ServerInstance.class.getName()).log(Level.SEVERE, null, e);
			throw new ServiceResultException(e);
		}
		Map<ExpandedNodeId, AddNodesItem> mappedNodes = new HashMap<>();
		for (AddNodesItem item : nodes) {
			mappedNodes.put(item.getRequestedNewNodeId(), item);
		}
		return server.getServerInstance().getMaster().addNodes(nodes, mappedNodes, includeChildren, null, false);
	}
	
	public static StatusCode[] addReferences(AddReferencesItem[] references) throws ServiceResultException {
		return server.getServerInstance().getMaster().addReferences(references, null);
	}
	
	public static void blockExecute(IRunnableWithProgress runnable) {
		IWorkbench wb = PlatformUI.getWorkbench();
		IProgressService ps = wb.getProgressService();
		try {
			ps.busyCursorWhile(runnable);
		} catch (InvocationTargetException | InterruptedException e) {
			Logger.getLogger(ServerInstance.class.getName()).log(Level.SEVERE, null, e);
		}
	}
	
	public static BrowseResult[] browse(NodeId nodeId, NodeId refernceType, EnumSet<NodeClass> nodeClass,
			EnumSet<BrowseResultMask> browseResult, BrowseDirection direction, boolean includeSubtypes)
			throws ServiceResultException {
		BrowseDescription[] nodesToBrowse = new BrowseDescription[1];
		BrowseDescription nodeToBrowse = new BrowseDescription();
		nodeToBrowse.setBrowseDirection(direction);
		nodeToBrowse.setIncludeSubtypes(includeSubtypes);
		nodeToBrowse.setNodeClassMask(nodeClass);
		nodeToBrowse.setNodeId(nodeId);
		nodeToBrowse.setReferenceTypeId(refernceType);
		nodeToBrowse.setResultMask(browseResult);
		nodesToBrowse[0] = nodeToBrowse;
		return server.getServerInstance().getMaster().browse(nodesToBrowse, UnsignedInteger.ZERO, null, null);
	}

	public static UAServerApplicationInstance getInstance() {
		if (ServerInstance.server == null) {
			throw new IllegalStateException("OPC Designer has not been started!");
		}
		return ServerInstance.server;
	}

	public static void closeServer() {
		if (ServerInstance.server != null && ServerInstance.server.getServerInstance() != null) {
//      LicManActivator.getDefault().getLicenseManager().getLicense().stopOPC();
			ServerInstance.server.stop();
		}
		ServerInstance.server = null;
	}
	
	public static void deleteNodes(DeleteNodesItem[] nodes2delete) throws ServiceResultException {
		try {
			LicManActivator.getDefault().getLicenseManager().getLicense().validateDeleteNodes(nodes2delete.length,
					false);
		} catch (ASStudioLicenseException e1) {
			// should never happen
			Logger.getLogger(ServerInstance.class.getName()).log(Level.SEVERE, null, e1);
		}
		try {
			server.getServerInstance().getMaster().deleteNodes(nodes2delete, null);
			// refresh last nodeid
			Set<Integer> nsIndizes = new HashSet<>();
			for (DeleteNodesItem node2delete : nodes2delete) {
				nsIndizes.add(node2delete.getNodeId().getNamespaceIndex());
			}
			server.getServerInstance().getAddressSpaceManager().getNodeFactory()
					.onRemove(nsIndizes.toArray(new Integer[0]));
		} catch (ServiceResultException e) {
			Logger.getLogger(ServerInstance.class.getName()).log(Level.SEVERE, null, e);
		}
	}

	public static StatusCode[] deleteReference(DeleteReferencesItem[] deleteReferencesItems)
			throws ServiceResultException {
		return server.getServerInstance().getMaster().deleteReference(deleteReferencesItems, null);
	}
	
	/**
	 * 
	 * @param namespaces
	 * @param newNsTable Edited namespace name table
	 * @param oldNsTable Original namespace table
	 */
	public static void doChangeNamespaceTable(NamespaceItem[] namespaces, NamespaceTable newNsTable,
			NamespaceTable oldNsTable) {
		// get active page
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		// current origin server namespace table
		NamespaceTable origin = NamespaceTable
				.createFromArray(getInstance().getServerInstance().getNamespaceUris().toArray());
		/**
		 * change opc ua server address spaces on namespace table
		 */
		Map<Integer, Integer> mapping = getInstance().getServerInstance().doChangeNamespaceTable(newNsTable,
				oldNsTable);
		Map<String, String> nsMapping = new HashMap<>();
		for (Entry<Integer, Integer> e : mapping.entrySet()) {
			// find name of orign namespace
			String nsOrigin = origin.getUri(e.getKey());
			// find name of new namespace
			String nsNew = newNsTable.getUri(e.getValue());
			nsMapping.put(nsOrigin, nsNew);
		}
		/**
		 * change typemapping
		 */
		getTypeModel().doChangeNamespaceTable(newNsTable, origin, mapping);
		// prepare command eclipse rcp framework
		ICommandService commandService = page.getWorkbenchWindow().getService(ICommandService.class);
		IHandlerService handlerService = page.getWorkbenchWindow().getService(IHandlerService.class);
		/**
		 * change opc node ids
		 */
		Command updateNamespaceTable2framework = commandService
				.getCommand(DesignerConstants.COMMAND_UPDATE_NAMESPACETABLE);
		// trigger
		NamespaceTableChangeParameter event = new NamespaceTableChangeParameter();
		event.setIndexMapping(mapping);
		event.setMapping(nsMapping);
		event.setOriginTable(origin);
		event.setNamespaceTable2change(newNsTable);
		// execute
		ExecutionEvent executionUpdateNamespaceTable2framework = handlerService
				.createExecutionEvent(updateNamespaceTable2framework, null);
		IEvaluationContext evalCtx = (IEvaluationContext) executionUpdateNamespaceTable2framework
				.getApplicationContext();
		evalCtx.getParent().addVariable(NamespaceTableChangeParameter.PARAMETER_ID, event);
		try {
			updateNamespaceTable2framework.executeWithChecks(executionUpdateNamespaceTable2framework);
		} catch (Exception ex) {
			Logger.getLogger(ServerInstance.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	public static Node getNode(ExpandedNodeId targetNodeId) {
		return server.getServerInstance().getAddressSpaceManager().getNodeById(targetNodeId);
	}

	public static Node getNode(NodeId nodeId) {
		return server.getServerInstance().getAddressSpaceManager().getNodeById(nodeId);
	}
	
	public static ServerTypeModel getTypeModel() {
		return ServerInstance.typeMapper.getServerModel(ServerInstance.getInstance());
	}

	public static void exportLanguageFiles(String path) throws IOException {
		if (path != null) {
			File file = new File(path);
			boolean createed = false;
			if (!file.exists()) {
				createed = file.createNewFile();
			}
			if (createed) {
				LanguageItem[] items = ServerInstance.getInstance().getServerInstance().getLanguageManager()
						.getLanguageInformation();
				LanguageCSVWriter writer = new LanguageCSVWriter();
				writer.write(file, items);
			}
		}
	}

	public static void exportModel2File2(IProgressMonitor monitor, String filePath, List<Integer> allowedNamespaces) {
		// create namespaces for the document
		List<String> namespace = allowedNamespaces(allowedNamespaces);
		// write the nodes
		OPCInternalServer sInstance = ServerInstance.getInstance().getServerInstance();
		// ServerInstance.getTypeModel();
		
		Map<Integer, List<Node>> nodesToWrite = allowedNodesFromNamespaces2(sInstance.getNamespaceUris(), namespace);

		ServerTypeModel typeModel = ServerInstance.typeMapper.getServerModel(ServerInstance.getInstance());
		
		ModelV2Exporter exporter = new ModelV2Exporter(sInstance);

		OPCProgressMonitor progress = new OPCProgressMonitor(monitor);
		exporter.setProgressMonitor(progress, 1);
		FileOutputStream fos = null;
		try {
			File file = new File(filePath);
			fos = new FileOutputStream(file);
			exporter.writeNodes(fos, namespace, typeModel, nodesToWrite);
			// write typemodel
			// exportTypesModel(filePath, namespace.toArray(new String[0]));
		} catch (IOException e) {
			Logger.getLogger(ServerInstance.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					Logger.getLogger(ServerInstance.class.getName()).log(Level.SEVERE, null, e);
				}
			}
		}
	}

	public static void exportModelFile2(IProgressMonitor monitor, String filePath, List<Integer> allowedNamespaces) {
		// create namespaces for the document
		List<String> namespace = allowedNamespaces(allowedNamespaces);
		// write the nodes
		List<Node> nodesToWrite = allowedNodesFromNamespaces(allowedNamespaces);

		OPCInternalServer sInstance = ServerInstance.getInstance().getServerInstance();
		SaxNodeWriter writer = new SaxNodeWriter(sInstance.getNamespaceUris(), sInstance.getServerUris(),
				sInstance.getTypeTable());
		OPCProgressMonitor progress = new OPCProgressMonitor(monitor);
		writer.setProgressMonitor(progress, 1);
		FileOutputStream fos = null;
		try {
			File file = new File(filePath);
			fos = new FileOutputStream(file);
			writer.writeNodes(fos, nodesToWrite.toArray(new Node[0]));
			// write typemodel
			exportTypesModel(filePath, namespace.toArray(new String[0]));
		} catch (IOException e) {
			Logger.getLogger(ServerInstance.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					Logger.getLogger(ServerInstance.class.getName()).log(Level.SEVERE, null, e);
				}
			}
		}
	}

	public static void exportModelClasses(IProgressMonitor monitor, String filePath, List<Integer> allowedNamespaces) {
		// create namespaces for the document
		List<String> namespace = new ArrayList<>();
		// adds the default namespace
		for (int i = 0; i < allowedNamespaces.size(); i++) {
			String nsUri = ServerInstance.getInstance().getServerInstance().getNamespaceUris()
					.getUri(allowedNamespaces.get(i));
			if (!namespace.contains(nsUri)) {
				namespace.add(nsUri);
			}
		}
		// write the nodes
		List<Node> nodesToWrite = new ArrayList<>();
		for (int i = 0; i < allowedNamespaces.size(); i++) {
			Node[] nodes2export = ServerInstance.getInstance().getServerInstance().getAddressSpaceManager()
					.getAllNodes(allowedNamespaces.get(i));
			for (Node n : nodes2export) {
				nodesToWrite.add(n);
			}
		}
		OPCInternalServer sInstance = ServerInstance.getInstance().getServerInstance();
		SaxNodeWriter writer = new SaxNodeWriter(sInstance.getNamespaceUris(), sInstance.getServerUris(),
				sInstance.getTypeTable());
		OPCProgressMonitor progress = new OPCProgressMonitor(monitor);
		writer.setProgressMonitor(progress, 1);
		FileOutputStream fos = null;
		try {
			File file = new File(filePath);
			fos = new FileOutputStream(file);
			writer.writeNodes(fos, nodesToWrite.toArray(new Node[0]));
			// write typemodel
			exportTypesModel(filePath, namespace.toArray(new String[0]));
		} catch (IOException e) {
			Logger.getLogger(ServerInstance.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					Logger.getLogger(ServerInstance.class.getName()).log(Level.SEVERE, null, e);
				}
			}
		}
	}

	public static void exportModelFile(IProgressMonitor monitor, String filePath, List<Integer> allowedNamespaces) {
		Logger.getLogger(ServerInstance.class.getName()).info(
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "form.savemodel") + " " + filePath);
		// create namespaces for the document
		List<String> namespace = new ArrayList<String>(allowedNamespaces.size() + 1);
		// adds the default namespace
		namespace.add(ServerInstance.getInstance().getServerInstance().getNamespaceUris().getUri(0));
		String namesp;
		for (int i = 0; i < allowedNamespaces.size(); i++) {
			namesp = ServerInstance.getInstance().getServerInstance().getNamespaceUris()
					.getUri(allowedNamespaces.get(i));
			if (!namespace.contains(namesp)) {
				namespace.add(namesp);
			}
		}
		// write the nodes
		List<Node> nodesToWrite = new ArrayList<Node>();
		for (int i = 0; i < allowedNamespaces.size(); i++) {
			Node[] nodes2export = ServerInstance.getInstance().getServerInstance().getAddressSpaceManager()
					.getAllNodes(allowedNamespaces.get(i));
			for (Node n : nodes2export) {
				nodesToWrite.add(n);
			}
		}
		/**
		 * export model file
		 */
		InformationModelParser parser = InformationModelParser.newInstance();
		// server uris
		List<String> serverUris = ServerInstance.getInstance().getServerInstance().getServerUris().getStrings();
		// nodes for opc model
		Node[] nodes = nodesToWrite.toArray(new Node[nodesToWrite.size()]);
		// prepare writer
		OPCXMLWriter writer = new OPCXMLWriter(ServerInstance.getInstance(), namespace, allowedNamespaces);
		// create document instance
		Document opcDocument = parser.createDocument();
		// fill opc model
		writer.writeModelDocument(opcDocument, namespace.toArray(new String[0]), serverUris.toArray(new String[0]));
		writer.writeNodes(opcDocument, nodes);
		// write opc model
		parser.writeDocument(opcDocument, filePath);
		typeMapper.exportModelFile(filePath, parser, writer, serverUris.toArray(new String[0]),
				namespace.toArray(new String[0]));
	}

	public static void exportTypesModel(String filepath, String[] urisToExport) {
		InformationModelParser parser = InformationModelParser.newInstance();
		List<String> serverUris = ServerInstance.getInstance().getServerInstance().getServerUris().getStrings();
		typeMapper.exportModelFile(filepath, parser, serverUris.toArray(new String[0]), urisToExport);
	}
	
	

	public static void importLanguageFiles(String path) {
		if (path != null) {
			File file = new File(path);
			if (file != null && file.exists() && file.isDirectory()) {
				File[] csvFiles = file.listFiles();
				for (File csvFile : csvFiles) {
					String extension = new Path(csvFile.getName()).getFileExtension();
					if (!csvFile.isFile() && !".csv".equals(extension)) {
						continue;
					}
					LanguageCSVReader parser = new LanguageCSVReader();
					LanguageItem[] items2add = parser.read(csvFile);
					ServerInstance.getInstance().getServerInstance().importLanguage(items2add);
				}
			}
		}
	}



	public static boolean importModelFile(IProgressMonitor monitor, URL... urls) throws ASStudioLicenseException {
		if (urls == null) {
			return false;
		}
		List<String> models2load = new ArrayList<>();
		for (URL url : urls) {
			URL url2 = FileLocator.find(url);
			if (url2 != null) {
				url = url2;
			}
			String file = url.getFile();
			try {
				file = URLDecoder.decode(file, "utf-8");
				models2load.add(file);
			} catch (UnsupportedEncodingException e) {
				Logger.getLogger(ServerInstance.class.getName()).log(Level.SEVERE, null, e);
			}
		}
		String[] modelFiles = models2load.toArray(new String[0]);
		for (String path : modelFiles) {
			if (checkMonitorChancled(monitor)) {
				break;
			}
			String extension = new Path(path).getFileExtension();
			if (extension.compareTo("xml") == 0) {
				try {
					OPCProgressMonitor monitor2 = new OPCProgressMonitor(monitor);
					int nodeCount = getInstance().importModel(monitor2, path);
					int namespaceCount = getInstance().getServerInstance().getNamespaceUris().size();
					// initial count
					LicManActivator.getDefault().getLicenseManager().getLicense().startOPC(nodeCount, namespaceCount);
				} catch (ServiceResultException e) {
					Logger.getLogger(ServerInstance.class.getName()).log(Level.SEVERE, null, e);
				}
			} else if (extension.compareTo("jar") == 0) {
				ServerInstance.getInstance().getServerInstance().importModel(path);
				break;
			}
		}
		if (checkMonitorChancled(monitor)) {
			return true;
		}
		/**
		 * types mapping
		 */
		typeMapper.importModelFile(ServerInstance.getInstance(), urls);
		if (checkMonitorChancled(monitor)) {
			return true;
		}
		return false;
	}

	public static DataValue[] read(ReadValueId[] id) {
		try {
			return server.getServerInstance().getMaster().read(id, 0.0, TimestampsToReturn.Both, null, null);
		} catch (ServiceResultException e) {
			Logger.getLogger(ServerInstance.class.getName()).log(Level.SEVERE, null, e);
		}
		return new DataValue[0];
	}
	
	/**
	 * Designer PRODUCT Start
	 * 
	 * Starts server with designer specific default values
	 * 
	 * @param urls
	 * @param certs
	 * @param config
	 * @param servername
	 * @return
	 */
	public static void resetServer(final String projectRootPath) {
		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				MCPreferenceStoreUtil
						.preWindowSaveInformationModel(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
				ServerInstance.closeServer();
				ServerInstance.startServer(projectRootPath);
				if (projectRootPath != null && !projectRootPath.isEmpty()) {
					Studio_ResourceManager.addOPCUAServer(new Path(projectRootPath).lastSegment(),
							ServerInstance.getInstance());
				}
				ModelBrowserView part = DesignerUtils.refreshBrowserAll();
				if (part != null) {
					part.setDirty(true);
				}
			}
		});
	}

	public static void resetServer() {
		MCPreferenceStoreUtil
				.preWindowSaveInformationModel(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
		resetServer("");
	}
	
	public static void startServer(UAServerApplicationInstance server) {
		ServerInstance.server = server;
	}

	/**
	 * Starts server with HB Studio
	 * 
	 * @param Serverconfig     Server configuration file
	 * @param certificateStore Server certificate store file
	 * @param models           Server information model files
	 * @return
	 * @throws ASStudioLicenseException
	 */
	public static UAServerApplicationInstance startServer(String servername, String serverconfig,
			String certificateStore, String localization, URL... models) throws ASStudioLicenseException {
		UAServerApplicationInstance server = new OPCUADriverServer();
		// declares instance as this ServerInstance SINGLETON
		startServer(server);
		// initialize OPC UA server
		boolean isCancled = startServerConfiguration(server, serverconfig, certificateStore, localization, models);
		if (isCancled) {
			return null;
		}
		return server;
	}

	public static UAServerApplicationInstance startServer(String projectRootPath) {
		UAServerApplicationInstance server2 = new UAServerApplicationInstance();
		startServer(server2);
		try {
			if (projectRootPath == null) {
				projectRootPath = "";
			}
			IPath projectPath = new Path(projectRootPath);
			File serverConfigFile;
			List<URL> modelURLs = new ArrayList<>();
			URL projectsFolder;
			if (projectRootPath.isEmpty()) {
				/**
				 * Project path
				 */
				URL projectUrl = FileLocator.find(Activator.getDefault().getBundle(), projectPath, null);
				projectsFolder = FileLocator.toFileURL(projectUrl);
				/**
				 * Section of initializing the opc server
				 */
				// bundle path of the server config file
				IPath path = Path.ROOT.append("configurations");
				// finds the url of the serverconfig xml
				URL serverConfigUrl = FileLocator.find(Activator.getDefault().getBundle(),
						path.append("serverconfig.xml"), null);
				//
				// converts the url to a file url
				if (serverConfigUrl != null) {
					serverConfigUrl = FileLocator.toFileURL(serverConfigUrl);
				}
				// creates the file
				serverConfigFile = new File(serverConfigUrl.getFile());
				URL predefinedModelUrl = FileLocator.find(Activator.getDefault().getBundle(),
						path.append("predefinedStandartModel.xml"), null);
				if (predefinedModelUrl != null) {
					predefinedModelUrl = FileLocator.toFileURL(predefinedModelUrl);
				}
				URL predefinedDiplcModel = FileLocator.find(Activator.getDefault().getBundle(),
						path.append("DI-PlcOpen.xml"), null);
				if (predefinedDiplcModel != null) {
					predefinedDiplcModel = FileLocator.toFileURL(predefinedDiplcModel);
				}
				modelURLs.add(predefinedModelUrl);
				modelURLs.add(predefinedDiplcModel);
			} else {
				/**
				 * Project path
				 */
				projectsFolder = projectPath.toFile().toURI().toURL();
				/**
				 * Section of initializing the opc server
				 */
				// bundle path of the server config file
				// finds the url of the serverconfig xml
				IPath serverConfigPath = projectPath.append("serverconfig").append("server.config.com");
				// converts the url to a file url
				// creates the file
				serverConfigFile = serverConfigPath.toFile();
				/**
				 * remove /servers/<project> from path and get default model
				 */
				URL predefinedModelUrl = projectPath.removeLastSegments(2).append("runtime").append("opcua").append("")
						.append("nodeset.xml").toFile().toURI().toURL();
				modelURLs.add(predefinedModelUrl);
			}
			startServerConfiguration(server, serverConfigFile.getPath(), projectsFolder.getFile(), null,
					modelURLs.toArray(new URL[0]));
		} catch (Exception e) {
			Logger.getLogger(ServerInstance.class.getName()).log(Level.SEVERE, null, e);
		}
		return server;
	}
	
	private static List<Node> allowedNodesFromNamespaces(List<Integer> allowedNamespaces) {
		List<Node> nodesToWrite = new ArrayList<>();
		for (int i = 0; i < allowedNamespaces.size(); i++) {
			Node[] nodes2export = ServerInstance.getInstance().getServerInstance().getAddressSpaceManager()
					.getAllNodes(allowedNamespaces.get(i));
			for (Node n : nodes2export) {
				nodesToWrite.add(n);
			}
		}
		return nodesToWrite;
	}

	private static Map<Integer, List<Node>> allowedNodesFromNamespaces2(NamespaceTable namespaceTable,
			List<String> namespace) {
		Map<Integer, List<Node>> nodes = new HashMap<Integer, List<Node>>();

		for (int i = 0; i < namespace.size(); i++) {
			int index = namespaceTable.getIndex(namespace.get(i));
			Node[] nodes2export = ServerInstance.getInstance().getServerInstance().getAddressSpaceManager()
					.getAllNodes(index);

			if(nodes2export.length == 0) {
				continue;
			}
			
			List<Node> nodesToWrite = nodes.get(index);
			if (nodesToWrite == null) {
				nodesToWrite = new ArrayList<>();
				nodes.put(index, nodesToWrite);
			}

			for (Node n : nodes2export) {
				nodesToWrite.add(n);
			}
			
			
//			nodesToWrite.sort(new Comparator<Node>() {
//
//				@Override
//				public int compare(Node node1, Node node2) {
//					return node1.getNodeId().compareTo(node2.getNodeId());
//				}
//				
//			});
		}
		return nodes;
	}

	private static List<String> allowedNamespaces(List<Integer> allowedNamespaces) {
		// create namespaces for the document
		List<String> namespace = new ArrayList<>();
		// adds the default namespace
		for (int i = 0; i < allowedNamespaces.size(); i++) {
			String nsUri = ServerInstance.getInstance().getServerInstance().getNamespaceUris()
					.getUri(allowedNamespaces.get(i));
			if (!NamespaceTable.OPCUA_NAMESPACE.equalsIgnoreCase(nsUri) && !namespace.contains(nsUri)) {
				namespace.add(nsUri);
			}
		}

		return namespace;
	}
	
	private static boolean checkMonitorChancled(IProgressMonitor monitor) {
		if (monitor != null && monitor.isCanceled()) {
			return true;
		}
		return false;
	}
	
	private static boolean handleMonitor(IProgressMonitor monitor, String subTask) {
		if (monitor != null && monitor.isCanceled()) {
			return true;
		}
		if (monitor != null) {
			monitor.subTask(subTask);
		}
		return false;
	}
	
	/**
	 * Creates a new OPC Server Instance
	 * 
	 * @param server
	 * @param serverconfig
	 * @param certificateStore
	 * @param localization
	 * @param models
	 * @throws ASStudioLicenseException
	 */
	private static boolean startServerConfiguration(final UAServerApplicationInstance server, String serverconfig,
			String certificateStore, String localization, URL... models) throws ASStudioLicenseException {
		IProgressMonitor monitor = Studio_ResourceManager.getProgressMonitor();
		boolean isCanceled = false;
		try {
			isCanceled = handleMonitor(monitor,
					CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "form.loadconfig") + "...");
			if (isCanceled) {
				return isCanceled;
			}
			// initializes the server configuration file
			ApplicationConfiguration configuration = new ApplicationConfiguration(new FileInputStream(serverconfig));
			server.getServerInstance().setServerConfiguration(configuration);
			// initializing of server multilanguage files
			importLanguageFiles(localization);
			// certificate store
			isCanceled = handleMonitor(monitor,
					CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "form.loadcert") + "...");
			if (isCanceled) {
				return isCanceled;
			}
			server.getServerInstance().loadOrCreateServerCertificateStore(certificateStore, false, "", "", "");
			// inforamtion model
			isCanceled = handleMonitor(monitor,
					CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "form.loadinformationmodel")
							+ "...");
			if (isCanceled) {
				return isCanceled;
			}
			// server url
			// TODO:
			// server.getServerInstance().getNamespaceUris().add(1,
			// "urn:localhost:UASDK:AS-Server");

			isCanceled = importModelFile(monitor, models);
		} catch (IOException e) {
			Logger.getLogger(ServerInstance.class.getName()).log(Level.SEVERE, null, e);
		}
		return isCanceled;
	}
}
