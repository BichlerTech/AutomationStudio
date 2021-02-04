package com.bichler.astudio.device.opcua.handler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.handlers.IHandlerService;
import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.common.NamespaceTable;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.BrowseDescription;
import org.opcfoundation.ua.core.BrowseDirection;
import org.opcfoundation.ua.core.BrowseResult;
import org.opcfoundation.ua.core.BrowseResultMask;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.core.ReferenceDescription;
import org.opcfoundation.ua.core.ReferenceNode;
import org.xml.sax.SAXException;

import com.bichler.astudio.device.opcua.DeviceActivator;
import com.bichler.astudio.device.opcua.options.EthernetUploadOption;
import com.bichler.astudio.device.opcua.options.SendOptions;
import com.bichler.astudio.filesystem.IFileSystem;
import com.bichler.astudio.log.ASLogActivator;
import com.bichler.astudio.opcua.OPCUAActivator;
import com.bichler.astudio.opcua.addressspace.model.binary.AddressSpaceNodeModelFactory;
import com.bichler.astudio.opcua.addressspace.model.binary.AddressSpaceNodeModelFactoryC;
import com.bichler.astudio.opcua.addressspace.model.binary.CompileFactory;
import com.bichler.astudio.opcua.addressspace.model.nosql.userauthority.NoSqlUtil;
import com.bichler.astudio.opcua.addressspace.model.tool.OPCProgressMonitor;
import com.bichler.astudio.opcua.constants.OPCUAConstants;
import com.bichler.astudio.opcua.events.OPCUADPReaderParameter;
import com.bichler.astudio.opcua.events.OPCUADPWriterParameter;
import com.bichler.astudio.opcua.handlers.events.AbstractOPCUADPReaderHandler;
import com.bichler.astudio.opcua.handlers.events.AbstractOPCUADPWriterHandler;
import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.opcua.nodes.OPCUAServerDriverModelNode;
import com.bichler.astudio.opcua.nodes.OPCUAServerModuleModelNode;
import com.bichler.astudio.opcua.widget.model.AdvancedConfigurationNode;
import com.bichler.astudio.opcua.widget.model.AdvancedConfigurationNodeParser;
import com.bichler.astudio.opcua.widget.model.AdvancedRootConfigurationNode;
import com.bichler.astudio.opcua.widget.model.AdvancedSectionType;
import com.bichler.astudio.utils.internationalization.CustomString;

import opc.sdk.core.node.Node;
import opc.sdk.core.node.UAVariableNode;
import opc.sdk.core.node.UAVariableTypeNode;

import com.bichler.astudio.device.core.transfer.AbstractUploadHandler;

public abstract class AbstractOPCCompileHandler extends AbstractUploadHandler {
	protected static final String SERVERS = "servers";
	protected static final String SERVERNAME_HBSOFT = "HBSServer";
	protected static final String SERVERNAME_BTECH = "BTECH-Server";
	protected boolean finished = true;

	public static final String PARAMETER_MODULE_NODE = "compilemodule";

	protected abstract void copy(IProgressMonitor monitor, IFileSystem localfileSystem, String localPath,
			IFileSystem targetFileSystem, String servername, Object[] namespaces2export, Object[] drivers,
			AtomicInteger aDrvId, SendOptions options, EthernetUploadOption ethernetOptions,
			boolean isUploadNewCertificate) throws IOException;

	protected void cleanupCompilationJars(IProgressMonitor monitor, IFileSystem filesystem, Object[] drivers,
			Object[] modules) {
		String rootPath = filesystem.getRootPath();
		// drivers / devices
		IPath driverPath = new Path(rootPath).append("drivers");
		IPath modulePath = new Path(rootPath).append("modules");
		for (Object driver : drivers) {
			String drvName = ((OPCUAServerDriverModelNode) driver).getDriverName();
			// ../<driver>/
			IPath dstAllDps = driverPath.append(drvName).append("datapointsDevices.com");
			if (filesystem.isFile(dstAllDps.toOSString())) {
				try {
					filesystem.removeFile(dstAllDps.toOSString());
				} catch (IOException e) {
					Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage());
				}
			}
			// ../<driver>/devices
			IPath dstDriver = driverPath.append(drvName).append("devices");
			if (filesystem.isDir(dstDriver.toOSString())) {
				try {
					filesystem.removeDir(dstDriver.toOSString(), true);
				} catch (IOException e) {
					Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage());
				}
			}
		}
		for (Object module : modules) {
			String modName = ((OPCUAServerModuleModelNode) module).getModuleName();
			// ../<driver>/
			IPath dstAllDps = modulePath.append(modName).append("datapointsDevices.com");
			if (filesystem.isFile(dstAllDps.toOSString())) {
				try {
					filesystem.removeFile(dstAllDps.toOSString());
				} catch (IOException e) {
					Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage());
				}
			}
			// ../<driver>/devices
			IPath dstModule = driverPath.append(modName).append("devices");
			if (filesystem.isDir(dstModule.toOSString())) {
				try {
					filesystem.removeDir(dstModule.toOSString(), true);
				} catch (IOException e) {
					Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage());
				}
			}
		}
		// information model
		IPath imFolder = new Path(filesystem.getRootPath()).append("informationmodel");
		File file = imFolder.toFile();
		if (file.exists()) {
			FileFilter filefilter = new FileFilter() {
				@Override
				public boolean accept(File file) {
					String extension = file.getName();
					if ("com.hbsoft.opc.informationmodel.jar".equals(extension)) {
						return true;
					}
					return false;
				}
			};
			// delete information model files
			deleteFiles(file, filefilter);
		}
	}

	protected void cleanupCompilation(IProgressMonitor monitor, IFileSystem filesystem, Object[] drivers,
			Object[] modules) {
		String rootPath = filesystem.getRootPath();
		// drivers
		IPath driverPath = new Path(rootPath).append("drivers");
		for (Object driver : drivers) {
			String drvName = ((OPCUAServerDriverModelNode) driver).getDriverName();
			IPath dstDriver = driverPath.append(drvName).append("devices");
			File file = dstDriver.toFile();
			if (!file.exists()) {
				continue;
			}
			FileFilter filefilter = new FileFilter() {
				@Override
				public boolean accept(File file) {
					String extension = new Path(file.getPath()).getFileExtension();
					if ("jar".equals(extension)) {
						return false;
					} else if ("com".equals(extension)) {
						return false;
					}
					return true;
				}
			};
			// delete information model files
			deleteFiles(file, filefilter);
		}
		// information model
		IPath imFolder = new Path(filesystem.getRootPath()).append("informationmodel");
		File file = imFolder.toFile();
		if (file.exists()) {
			FileFilter filefilter = new FileFilter() {
				@Override
				public boolean accept(File file) {
					IPreferenceStore store = OPCUAActivator.getDefault().getPreferenceStore();
					boolean doOPCUADoDeleteSources = store.getBoolean(OPCUAConstants.OPCUADoDeleteSources);

					String extension = new Path(file.getPath()).getFileExtension();
					if ("class".equals(extension)) {
						return true;
					} else if ("java".equals(extension) && doOPCUADoDeleteSources) {
						return true;
					} else if ("v".equals(extension)) {
						return true;
					}
					return false;
				}
			};
			// delete information model files
			deleteFiles(file, filefilter);
		}
	}

	protected void prepare(IProgressMonitor monitor, IWorkbenchWindow window, IFileSystem localfileSystem,
			Object[] namespaces2export, boolean isFullNsTable2export, Object[] drivers, Object[] modules)
			throws IOException {
		monitor.subTask(CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.device.opcua.handler.abstractcompile.monitor.createopcmodel") + "...");
		monitor.worked(1);
		// compile information model jar
		AddressSpaceNodeModelFactory mf = new AddressSpaceNodeModelFactory();
		// compile information model ansi c
		AddressSpaceNodeModelFactoryC mfc = new AddressSpaceNodeModelFactoryC();
		mfc.setServerInstance(ServerInstance.getInstance().getServerInstance());
		NamespaceTable serverNS = ServerInstance.getInstance().getServerInstance().getNamespaceUris();
		String imFolder = new Path(localfileSystem.getRootPath()).append("informationmodel").toOSString();
		/**
		 * cancel operation
		 */
		if (monitor.isCanceled()) {
			monitor.done();
			return;
		}
		String userdbpath = "";
		userdbpath = localfileSystem.getRootPath() + File.separator + "users" + File.separator + "userconfig.db";

		Connection con = null;
		try {
			con = NoSqlUtil.createConnection(userdbpath);
			// load all nodes autohority configuration
			NoSqlUtil.loadRolesforNodes(con, serverNS);
		} catch (SQLException | ClassNotFoundException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage());
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage());
				}
			}
		}

		IPreferenceStore store = OPCUAActivator.getDefault().getPreferenceStore();
		boolean doCompileJar = store.getBoolean(OPCUAConstants.OPCUADoCompileJar);
		if (doCompileJar) {
			// compiles information model
			compileInformationModel(monitor, imFolder, namespaces2export, serverNS, isFullNsTable2export, mf,
					localfileSystem);
		}

		boolean doCompileAnsiC = store.getBoolean(OPCUAConstants.OPCUADoCompileAnsiC);
		boolean isToolchain = DeviceActivator.getDefault().isToolchainInstalled();
		if (!isToolchain) {
			ASLogActivator.getDefault().getLogger().log(Level.INFO, "Cannot compile C-files, Toolchain not found!"
//					CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
//							"com.bichler.astudio.device.opcua.handler.abstractcompile.log.error.upload")
			);
		} else if (doCompileAnsiC) {
			// compiles c information model
			compileInformationModelC(monitor, imFolder, namespaces2export, serverNS, mfc, localfileSystem);
		}
		/**
		 * cancel operation, removes .java, .class, .jar files
		 */
		if (monitor.isCanceled()) {
			cleanupCompilation(monitor, localfileSystem, drivers, modules);
			cleanupCompilationJars(monitor, localfileSystem, drivers, modules);
			monitor.done();
			return;
		}
		// compile devices
		compileDevices(monitor, window, drivers, modules, namespaces2export, isFullNsTable2export, serverNS, mf);
		/**
		 * cancel operation, removes .java, .class, .jar files
		 */
		if (monitor.isCanceled()) {
			cleanupCompilation(monitor, localfileSystem, drivers, modules);
			cleanupCompilationJars(monitor, localfileSystem, drivers, modules);
			monitor.done();
			return;
		}
		// cleans up java and class files
		cleanupCompilation(monitor, localfileSystem, drivers, modules);
	}

	private void compileDevices(IProgressMonitor monitor, IWorkbenchWindow window, Object[] drivers, Object[] modules,
			Object[] namespaces2export, boolean fullNsTable2export, NamespaceTable namespaceTable,
			AddressSpaceNodeModelFactory mf) {
		monitor.subTask(CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.device.opcua.handler.abstractcompile.monitor.createdriver") + "...");
		monitor.worked(1);
		// create driver jar
		for (Object driver : drivers) {
			IFileSystem filesystem = ((OPCUAServerDriverModelNode) driver).getFilesystem();
			IFileSystem targetFilesystem = ((OPCUAServerDriverModelNode) driver).getTargetFilesystem();
			String driverName = ((OPCUAServerDriverModelNode) driver).getDriverName();
			String rootPath = filesystem.getRootPath();
			IPath driverPath = new Path(rootPath).append("drivers").append(driverName);
			// device config
			IPath deviceconfig = new Path(rootPath).append("drivers").append(driverName).append("deviceconfig.com");
			// read datapoints from driver
			IPath ddp = new Path(rootPath).append("drivers").append(driverName).append("datapoints.com");
			String type = ((OPCUAServerDriverModelNode) driver).getDriverType();
			String version = ((OPCUAServerDriverModelNode) driver).getDriverVersion();

			ICommandService commandService = window.getService(ICommandService.class);
			Command command = commandService
					.getCommand("com.bichler.astudio.editor." + type + "." + version + ".compile");

			if (command != null && command.isDefined()) {
				// call internal module compiler if exists
				IHandlerService handlerService = window.getService(IHandlerService.class);
				ExecutionEvent evt = handlerService.createExecutionEvent(command, null);
				try {
					command.executeWithChecks(evt);
				} catch (ExecutionException | NotDefinedException | NotEnabledException | NotHandledException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				// else call studio compile
				Map<NodeId, Object> result = readDatapointList(window, filesystem, ddp.toOSString(), type, version);
				// read device config
				InputStream deviceIn = null;
				if (filesystem.isFile(deviceconfig.toOSString())) {
					try {
						deviceIn = filesystem.readFile(deviceconfig.toOSString());
						SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
						AdvancedRootConfigurationNode root = new AdvancedRootConfigurationNode(
								AdvancedSectionType.DeviceConfig);
						AdvancedConfigurationNodeParser saxHandler = new AdvancedConfigurationNodeParser(namespaceTable,
								root, AdvancedSectionType.DeviceConfig);
						parser.parse(deviceIn, saxHandler);
						AdvancedConfigurationNode[] children = root.getChildren();
						if (children.length > 0) {
							IPath devicePath = driverPath.append("devices");
							if (!filesystem.isDir(devicePath.toOSString())) {
								filesystem.addDir(devicePath.toOSString());
							}
							int fileIndex = 0;
							for (AdvancedConfigurationNode child : children) {
								fileIndex++;
								NodeId deviceId = child.getDeviceId();
								monitor.subTask(CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
										"com.bichler.astudio.device.opcua.handler.abstractcompile.monitor.createdriver.device")
										+ "..." + " " + child.getDeviceName());
								monitor.worked(1);
								if (NodeId.isNull(deviceId)) {
									continue;
								}
								// device path
								IPath deviceFolder = devicePath.append("device_" + fileIndex);
								if (!filesystem.isDir(deviceFolder.toOSString())) {
									filesystem.addDir(deviceFolder.toOSString());
								}
								// find all nodes for the device
								List<Integer> dList = new ArrayList<>();
								Map<Integer, List<Node>> nodes = findDeviceModel(deviceId, namespaceTable, dList);
								// Map<Integer, List<Node>> allnodes = new HashMap<Integer, List<Node>>();
								// allnodes.put(key, value)
								if (nodes == null || nodes.isEmpty()) {
									continue;
								}
								writeDatapointList(window, filesystem, targetFilesystem, devicePath.toOSString(),
										"datapoints_" + fileIndex + ".com", type, result, nodes, version);
								NamespaceTable exportTable = AbstractOPCCompileHandlerUtil
										.createNamespaceTableToExport(namespaceTable, dList);
								// generate device jar
								generateJar(monitor, deviceFolder.toOSString(), devicePath.toOSString(),
										"device_" + fileIndex, mf, namespaceTable, exportTable, fullNsTable2export,
										nodes, filesystem);
								// generate datapoint sheet
								if (monitor.isCanceled()) {
									return;
								}
							}
						}
					} catch (IOException e) {
						ASLogActivator.getDefault().getLogger().log(Level.SEVERE,
								CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
										"com.bichler.astudio.device.opcua.handler.abstractcompile.log.error.upload"),
								e);
					} catch (ParserConfigurationException e) {
						ASLogActivator.getDefault().getLogger().log(Level.SEVERE,
								CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
										"com.bichler.astudio.device.opcua.handler.abstractcompile.log.error.upload"),
								e);
					} catch (SAXException e) {
						ASLogActivator.getDefault().getLogger().log(Level.SEVERE,
								CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
										"com.bichler.astudio.device.opcua.handler.abstractcompile.log.error.upload"),
								e);
					} finally {
						if (deviceIn != null) {
							try {
								deviceIn.close();
							} catch (IOException e) {
								ASLogActivator.getDefault().getLogger().log(Level.SEVERE, CustomString.getString(
										DeviceActivator.getDefault().RESOURCE_BUNDLE,
										"com.bichler.astudio.device.opcua.handler.abstractcompile.log.error.upload"),
										e);
							}
						}
					}
				}
				List<Integer> iList = AbstractOPCCompileHandlerUtil.getIndexFromNamespaceToExport(namespaceTable,
						namespaces2export);
				if (monitor.isCanceled()) {
					monitor.done();
					return;
				}
				Map<Integer, List<Node>> list = AbstractOPCCompileHandlerUtil
						.getNodesForNamespacetableToExport(namespaceTable, iList);
				// write datapoints end
				writeDatapointList(window, filesystem, targetFilesystem, driverPath.toOSString(),
						"datapointsDevices.com", type, result, list/* .values().toArray(new Node[0]) */, version);
			}
		}
		// create modules
		for (Object module : modules) {
			IFileSystem filesystem = ((OPCUAServerModuleModelNode) module).getFilesystem();
			IFileSystem targetFilesystem = ((OPCUAServerModuleModelNode) module).getTargetFilesystem();
			String moduleName = ((OPCUAServerModuleModelNode) module).getModuleName();
			String moduleType = ((OPCUAServerModuleModelNode) module).getModuleType();
			String moduleVersion = ((OPCUAServerModuleModelNode) module).getModuleVersion();
			String rootPath = filesystem.getRootPath();
			IPath modulePath = new Path(rootPath).append("modules").append(moduleName);
			// device config
			IPath deviceconfig = new Path(rootPath).append("modules").append(moduleName).append("deviceconfig.com");
			// read datapoints from driver
			IPath ddp = new Path(rootPath).append("modules").append(moduleName).append("datapoints.com");
			String type = ((OPCUAServerModuleModelNode) module).getModuleType();
			String version = ((OPCUAServerModuleModelNode) module).getModuleVersion();

			ICommandService commandService = window.getService(ICommandService.class);
			Command command = commandService
					.getCommand("com.bichler.astudio.editor." + type + "." + version + ".compile");

			if (command != null && command.isDefined()) {
				// call internal module compiler if exists
				IHandlerService handlerService = window.getService(IHandlerService.class);
				ExecutionEvent evt = handlerService.createExecutionEvent(command, null);

				IEvaluationContext evalCtx = (IEvaluationContext) evt.getApplicationContext();
				evalCtx.getParent().addVariable(PARAMETER_MODULE_NODE, module);

				try {
					command.executeWithChecks(evt);
				} catch (ExecutionException | NotDefinedException | NotEnabledException | NotHandledException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				// else call studio compile
				// no integrated compile function
			}
		}
	}

	private void writeDatapointList(IWorkbenchWindow window, IFileSystem filesystem, IFileSystem targetFilesystem,
			String parent, String file, String type, Map<NodeId, Object> datapoints, Map<Integer, List<Node>> nodes,
			String version) {
		OPCUADPWriterParameter trigger = new OPCUADPWriterParameter(parent, file);
		trigger.setFilesystem(filesystem);
//		trigger.setTargetFilesystem(targetFilesystem);
		trigger.setDatapointList(datapoints);
		trigger.setNodes(nodes);
		ICommandService commandService = window.getService(ICommandService.class);
		IHandlerService handlerService = window.getService(IHandlerService.class);
		String commandIdImport = AbstractOPCUADPWriterHandler.ID + type + "." + version + ".export";
		Command command = commandService.getCommand(commandIdImport);
		ExecutionEvent evt = handlerService.createExecutionEvent(command, null);
		IEvaluationContext evalCtx = (IEvaluationContext) evt.getApplicationContext();
		evalCtx.getParent().addVariable(OPCUADPWriterParameter.PARAMETER_ID, trigger);
		try {
			command.executeWithChecks(evt);
		} catch (Exception e) {
			ASLogActivator.getDefault().getLogger().log(Level.SEVERE,
					CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
							"com.bichler.astudio.device.opcua.handler.abstractcompile.log.error.upload.datapoints"),
					e);
		}
	}

	@SuppressWarnings("unchecked")
	private Map<NodeId, Object> readDatapointList(IWorkbenchWindow window, IFileSystem filesystem, String path,
			String type, String version) {
		OPCUADPReaderParameter reader = new OPCUADPReaderParameter(path);
		reader.setFilesystem(filesystem);
		ICommandService commandService = window.getService(ICommandService.class);
		IHandlerService handlerService = window.getService(IHandlerService.class);
		String commandIdImport = AbstractOPCUADPReaderHandler.ID + type + "." + version + ".import";
		Command command = commandService.getCommand(commandIdImport);
		ExecutionEvent evt = handlerService.createExecutionEvent(command, null);
		IEvaluationContext evalCtx = (IEvaluationContext) evt.getApplicationContext();
		evalCtx.getParent().addVariable(OPCUADPReaderParameter.PARAMETER_ID, reader);
		Map<NodeId, Object> result = new HashMap<>();
		try {
			result = (Map<NodeId, Object>) command.executeWithChecks(evt);
		} catch (ExecutionException | NotDefinedException | NotEnabledException | NotHandledException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage());
		}
		return result;
	}

	private void compileInformationModel(IProgressMonitor monitor, String file, Object[] namespaces2export,
			NamespaceTable nsTable, boolean fullNsTable2export, AddressSpaceNodeModelFactory modelFactory,
			IFileSystem filesystem) throws IOException {

		List<Integer> iList = AbstractOPCCompileHandlerUtil.getIndexFromNamespaceToExport(nsTable, namespaces2export);
		if (monitor.isCanceled()) {
			monitor.done();
			return;
		}
		Map<Integer, List<Node>> list = AbstractOPCCompileHandlerUtil.getNodesForNamespacetableToExport(nsTable, iList);
		if (monitor.isCanceled()) {
			monitor.done();
			return;
		}
		monitor.subTask(CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.device.opcua.handler.abstractcompile.monitor.createopcmodel") + "...");
		monitor.worked(1);
		NamespaceTable exportTable = AbstractOPCCompileHandlerUtil.createNamespaceTableToExport(nsTable, iList);
		if (monitor.isCanceled()) {
			monitor.done();
			return;
		}
		// generate complex type
		generateJar(monitor, file, file, AddressSpaceNodeModelFactory.PACKAGENAME, modelFactory, nsTable, exportTable,
				fullNsTable2export, list, filesystem);
	}

	private void compileInformationModelC(IProgressMonitor monitor, String file, Object[] namespaces2export,
			NamespaceTable nsTable, AddressSpaceNodeModelFactoryC modelFactory, IFileSystem filesystem)
			throws IOException {
		List<Integer> iList = AbstractOPCCompileHandlerUtil.getIndexFromNamespaceToExport(nsTable, namespaces2export);
		if (monitor.isCanceled()) {
			monitor.done();
			return;
		}
		List<Integer> rList = new ArrayList<Integer>();
		rList.addAll(iList);
		Map<Integer, List<Node>> list = AbstractOPCCompileHandlerUtil.getNodesForNamespacetableToExport(nsTable, rList);
		if (monitor.isCanceled()) {
			monitor.done();
			return;
		}
		monitor.subTask(CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.device.opcua.handler.abstractcompile.monitor.createopcmodel") + "...");
		monitor.worked(1);
		NamespaceTable exportTable = AbstractOPCCompileHandlerUtil.createNamespaceTableToExport(nsTable, iList);
		if (monitor.isCanceled()) {
			monitor.done();
			return;
		}

		// generate complex type
		generateExecute(monitor, file, file, AddressSpaceNodeModelFactory.PACKAGENAME, modelFactory, nsTable,
				exportTable, list, rList, filesystem);
	}

	private void deleteFiles(File file, FileFilter filefilter) {
		File[] files = file.listFiles(filefilter);
		if (files != null) {
			for (File file2remove : files) {
				if (file2remove.isDirectory()) {
					deleteFiles(file2remove, filefilter);
				}
				file2remove.delete();
			}
		}
	}

	/**
	 * Finds all nodes starting from nodeid.
	 * 
	 * @param nodeId   NodeId to start
	 * @param dNs
	 * @param serverNS
	 * @return
	 */
	private Map<Integer, List<Node>> findDeviceModel(NodeId nodeId, NamespaceTable nsTable, List<Integer> collection) {
		Map<Integer, List<Node>> nodes = new HashMap<Integer, List<Node>>();
		List<NodeId> already = new ArrayList<>();
		Node node = ServerInstance.getInstance().getServerInstance().getAddressSpaceManager().getNodeById(nodeId);
		if (node == null) {
			return new HashMap<Integer, List<Node>>();
		}
		fetchDeviceModel(node, nodes, already, nsTable, collection);
		return nodes;
	}

	private void fetchDeviceModel(Node node, Map<Integer, List<Node>> nodes, List<NodeId> already,
			NamespaceTable nsTable, List<Integer> collection) {

		List<Node> ns = null;
		// add node
		if (already.contains(node.getNodeId())) {
			return;
		}
		ns = nodes.get(node.getNodeId().getNamespaceIndex());
		if (ns == null) {
			ns = new ArrayList<Node>();
			nodes.put(node.getNodeId().getNamespaceIndex(), ns);
		}
		ns.add(node);
		already.add(node.getNodeId());
		AbstractOPCCompileHandlerUtil.fetchNamespaceIndexFromNode(node, nsTable, collection);
		BrowseDescription[] nodesToBrowse = new BrowseDescription[1];
		nodesToBrowse[0] = new BrowseDescription();
		nodesToBrowse[0].setBrowseDirection(BrowseDirection.Forward);
		nodesToBrowse[0].setIncludeSubtypes(true);
		nodesToBrowse[0].setNodeClassMask(NodeClass.ALL);
		nodesToBrowse[0].setNodeId(node.getNodeId());
		nodesToBrowse[0].setReferenceTypeId(Identifiers.HierarchicalReferences);
		nodesToBrowse[0].setResultMask(BrowseResultMask.ALL);
		try {
			BrowseResult[] results = ServerInstance.getInstance().getServerInstance().getMaster().browse(nodesToBrowse,
					UnsignedInteger.ZERO, null, null);
			if (results != null) {
				for (ReferenceDescription result : results[0].getReferences()) {
					ExpandedNodeId targetId = result.getNodeId();
					Node target = ServerInstance.getInstance().getServerInstance().getAddressSpaceManager()
							.getNodeById(targetId);
					if (target == null) {
						continue;
					}
					// fetch
					fetchDeviceModel(target, nodes, already, nsTable, collection);
				}
			}
		} catch (ServiceResultException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage());
		}
	}

	private void generateJar(IProgressMonitor monitor, String destFolder, String jarPath, String jarName,
			AddressSpaceNodeModelFactory modelFactory, NamespaceTable serverTable, NamespaceTable nsTable,
			boolean fullNsTable2export, Map<Integer, List<Node>> nodes, IFileSystem filesystem) throws IOException {

		/*
		 * if(fullNsTable2export) { nsTable = serverTable; }
		 */

		OPCProgressMonitor mon = new OPCProgressMonitor(monitor);
		modelFactory.create(mon, destFolder, nsTable, serverTable, nodes, fullNsTable2export);
		if (monitor.isCanceled()) {
			monitor.done();
			return;
		}
		CompileFactory.compile(new OPCProgressMonitor(monitor), destFolder, jarPath, jarName,
				AddressSpaceNodeModelFactory.PACKAGENAME);
		if (monitor.isCanceled()) {
			monitor.done();
			return;
		}
	}

	private void generateExecute(IProgressMonitor monitor, String destFolder, String jarPath, String jarName,
			AddressSpaceNodeModelFactoryC modelFactory, NamespaceTable serverTable, NamespaceTable nsTable,
			Map<Integer, List<Node>> nodes, List<Integer> rList, IFileSystem filesystem) throws IOException {

		File[] files = modelFactory.create(new OPCProgressMonitor(monitor), destFolder, nsTable, serverTable, nodes,
				rList, filesystem);

		String[] filesToDel = copyCFiles(filesystem);
		//
//		writeSourcesFile(destFolder, filesystem);
//		writeObjectsFile(destFolder, filesystem);
		// generate make file and compile
		writeMakefile(destFolder, filesystem, files, "gcc-linaro-arm-linux-gnueabihf-4.9.3", filesToDel);
		compileC(destFolder);
		if (monitor.isCanceled()) {
			monitor.done();
			return;
		}
	}

	private void compileC(String folder) {

		// now compile to device
		try {
			Runtime rt = Runtime.getRuntime();

			// final Map<String, String> env = new HashMap<String, String>(System.getenv());
//			URL makeFile = FileLocator.find(DeviceActivator.getDefault().getBundle(),
//					Path.ROOT.append("toolchain").append("btech_make").append("bin").append("make.exe"), null);
//			URL fileMakeFile = FileLocator.toFileURL(makeFile);
//			File includesFolder = new File(fileMakeFile.getFile());

			File fBtechMake = DeviceActivator.getDefault().getToolchainFile(DeviceActivator.getDefault().getToolchain(),
					"btech_make");
			File fBtechMakeBin = DeviceActivator.getDefault().getToolchainFile(fBtechMake, "bin");
			File fBtechMakeExe = DeviceActivator.getDefault().getToolchainFile(fBtechMakeBin, "make.exe");

			String makePath = fBtechMakeExe.getPath();
			Process pr = rt.exec("cmd /C " + makePath, null, new File(folder + "\\compiled"));
			BufferedReader input = new BufferedReader(new InputStreamReader(pr.getInputStream()));
			String line = null;
			while ((line = input.readLine()) != null) {
				System.out.println(line);
			}
			BufferedReader error = new BufferedReader(new InputStreamReader(pr.getErrorStream()));
			while ((line = error.readLine()) != null) {
				System.err.println(line);
			}

			int exitVal = pr.waitFor();
			if (exitVal > 0)
				System.out.println("compile exited with error code " + exitVal);

			// only delete all files if dodeletesources is not set
			IPreferenceStore store = OPCUAActivator.getDefault().getPreferenceStore();
			boolean doDeleteSources = store.getBoolean(OPCUAConstants.OPCUADoDeleteSources);
			if (doDeleteSources) {
				// now clean sources
				pr = rt.exec("cmd /C " + makePath + " cleanSources", null, new File(folder + "\\compiled"));
				input = new BufferedReader(new InputStreamReader(pr.getInputStream()));
				line = null;
				while ((line = input.readLine()) != null) {
					System.out.println(line);
				}
				error = new BufferedReader(new InputStreamReader(pr.getErrorStream()));
				while ((line = error.readLine()) != null) {
					System.err.println(line);
				}

				exitVal = pr.waitFor();
				if (exitVal > 0)
					System.out.println("clean exited with error code " + exitVal);

				// delte make file
//				File f = new File(folder + "\\compiled\\makefile");
//				if (f.exists())
//					f.delete();
			}
			// now clean binaries
			if (doDeleteSources) {
				pr = rt.exec("cmd /C " + makePath + " cleanBinary", null, new File(folder + "\\compiled"));
				input = new BufferedReader(new InputStreamReader(pr.getInputStream()));
				line = null;
				while ((line = input.readLine()) != null) {
					System.out.println(line);
				}
				error = new BufferedReader(new InputStreamReader(pr.getErrorStream()));
				while ((line = error.readLine()) != null) {
					System.err.println(line);
				}

				exitVal = pr.waitFor();
				if (exitVal > 0)
					System.out.println("clean exited with error code " + exitVal);
			}
			if (doDeleteSources) {
				// delte make file
				File f = new File(folder + "\\compiled\\makefile");
				if (f.exists())
					f.delete();
			}
		} catch (InterruptedException | IOException ex) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, ex.getMessage(), ex);
		}
	}

	private String[] copyCFiles(IFileSystem filesystem) {
		List<String> cfiles = new ArrayList<String>();
		// URL folder1 = FileLocator.find(DeviceActivator.getDefault().getBundle(),
		// Path.ROOT.append("toolchain").append("btech_src"), null);
		//
		// try {
		// Logger.getLogger(getClass().getName()).log(Level.INFO, "BTECH_SRC path:
		// "+folder1.toURI().toString());
		// } catch (URISyntaxException e1) {
		// e1.printStackTrace();
		// }
		//
		// URL folder2 = FileLocator.toFileURL(folder1);
		//
		// try {
		// Logger.getLogger(getClass().getName()).log(Level.INFO, "FileURL BTECH_SRC
		// path: "+folder2.toURI().toString());
		// } catch (URISyntaxException e1) {
		// e1.printStackTrace();
		// }
		// File folder3 = new File(folder2.getFile());
		File folder3 = DeviceActivator.getDefault().getToolchainFile(DeviceActivator.getDefault().getToolchain(),
				"btech_src");

		String folder = folder3.getAbsolutePath();
		try {
			cfiles.addAll(Arrays.asList(filesystem.listFiles(folder)));
		} catch (IOException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
			return cfiles.toArray(new String[0]);
		}

		InputStream in = null;
		OutputStream out = null;
		for (String file : cfiles) {
			try {
				// URL src = FileLocator.find(DeviceActivator.getDefault().getBundle(),
				// Path.ROOT.append("toolchain").append("btech_src").append(file), null);
				// URL src2 = FileLocator.toFileURL(src);
				//
				// try {
				// Logger.getLogger(getClass().getName()).log(Level.INFO, "FileURL BTECH_SRC
				// path: "+src2.toURI().toString());
				// } catch (URISyntaxException e1) {
				// e1.printStackTrace();
				// }
				//
				// File srcFile = new File(src2.getFile());
				File srcFile = DeviceActivator.getDefault().getToolchainFile(folder3, file);

				if (!filesystem.isFile(srcFile.getAbsolutePath())) {
					filesystem.addFile(srcFile.getAbsolutePath());
				}
				in = filesystem.readFile(srcFile.getAbsolutePath());
				if (!filesystem.isDir(filesystem.getRootPath() + "\\informationmodel\\compiled\\")) {
					filesystem.addDir(filesystem.getRootPath() + "\\informationmodel\\compiled\\");
				}
				out = filesystem.writeFile(filesystem.getRootPath() + "\\informationmodel\\compiled\\" + file);
				byte[] n = new byte[1024];
				int len = 0;
				while ((len = in.read(n)) > 0) {
					out.write(n, 0, len);
					out.flush();
				}

			} catch (IOException ex) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, ex.getMessage(), ex);
			} finally {
				try {
					in.close();
				} catch (IOException e) {
					Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
				}
				try {
					out.close();
				} catch (IOException e) {
					Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
				}
			}
		}

		return cfiles.toArray(new String[0]);
	}
	/*
	 * private void writeObjectsFile(String folder, IFileSystem filesystem) { //
	 * reate compiled folder File compiled = new File(folder + "\\compiled"); try {
	 * if (!compiled.exists()) compiled.createNewFile(); // create make file File f
	 * = new File(folder + "\\compiled\\objects.mk");
	 * 
	 * if (!f.exists()) f.createNewFile();
	 * 
	 * try (FileWriter fw = new FileWriter(f)) { fw.write(
	 * "################################################################################\n"
	 * ); fw.write("#  Automatically-generated file. Do not edit!\n"); fw.write(
	 * "################################################################################\n"
	 * ); fw.write("\n"); fw.write("USER_OBJS :=");
	 * fw.write("LIBS := -lopen62541\n"); fw.write("\n"); } catch (Exception e) { }
	 * // now remove also make file } catch (IOException e1) { // TODO
	 * Auto-generated catch block e1.printStackTrace(); } }
	 */

	/*
	 * private void writeSourcesFile(String folder, IFileSystem filesystem) { //
	 * reate compiled folder File compiled = new File(folder + "\\compiled"); try {
	 * if (!compiled.exists()) compiled.createNewFile(); // create make file File f
	 * = new File(folder + "\\compiled\\sources.mk");
	 * 
	 * if (!f.exists()) f.createNewFile();
	 * 
	 * try (FileWriter fw = new FileWriter(f)) { fw.write(
	 * "################################################################################\n"
	 * ); fw.write("#  Automatically-generated file. Do not edit!\n"); fw.write(
	 * "################################################################################\n"
	 * ); fw.write("\n"); fw.write("OBJ_SRCS := "); fw.write("ASM_SRCS := \n");
	 * fw.write("C_SRCS := \n"); fw.write("O_SRCS := \n");
	 * fw.write("S_UPPER_SRCS := \n"); fw.write("EXECUTABLES := \n");
	 * fw.write("OBJS := \n"); fw.write("C_DEPS := \n"); fw.write("\n");
	 * fw.write("# Every subdirectory with source files must be described here\n");
	 * fw.write("SUBDIRS := \\\n"); fw.write("src \\\n"); fw.write("\n"); } catch
	 * (Exception e) { } // now remove also make file } catch (IOException e1) { //
	 * TODO Auto-generated catch block e1.printStackTrace(); } }
	 */

	private void writeMakefile(String folder, IFileSystem filesystem, File[] files, String toolChain,
			String[] extCFiles) {
		// reate compiled folder
		File compiled = new File(folder + "\\compiled");
		try {
			if (!compiled.exists())
				compiled.createNewFile();
			// create make file
			File f = new File(folder + "\\compiled\\makefile");

			if (!f.exists())
				f.createNewFile();

			try (FileWriter fw = new FileWriter(f)) {
				fw.write("################################################################################\n");
				fw.write("#  Automatically-generated file. Do not edit!\n");
				fw.write("################################################################################\n");
				// fw.write("\n-include sources.mk\n");
				// fw.write("-include objects.mk\n\n");
				fw.write("\n");
				fw.write("C_SRCS += ");
				for (File file : files) {
					fw.write(file.getName() + " ");// .replace(".iec", ".c") + " ");
				}
				for (String file : extCFiles) {
					fw.write(file + " ");// .replace(".iec", ".c") + " ");
				}
				fw.write("\n");
				fw.write("\n");
				fw.write("OBJS += ");
				for (File file : files) {
					fw.write(file.getName().replace(".c", ".o") + " ");
				}
				for (String file : extCFiles) {
					fw.write(file.replace(".c", ".o") + " ");
				}
				fw.write("\n");
				fw.write("\n");
				fw.write("C_DEPS += ");
				for (File file : files) {
					fw.write(file.getName().replace(".c", ".d") + " ");
				}
				for (String file : extCFiles) {
					fw.write(file.replace(".c", ".d") + " ");
				}
				fw.write("\n\nLIBS := -lopen62541 -ldl\n");
				fw.write("\n");
				fw.write("RM := del -rf\n");
				fw.write("\n");
				fw.write("# All Target\r\n");
				fw.write("all: btech_opcua_server.elf\n");
				fw.write("\n");
				fw.write("btech_opcua_server.elf: $(OBJS)\n\t");

				File fCompiler = DeviceActivator.getDefault()
						.getToolchainFile(DeviceActivator.getDefault().getToolchain(), "compiler");
				File fToolchain = DeviceActivator.getDefault().getToolchainFile(fCompiler, toolChain);
				File fBin = DeviceActivator.getDefault().getToolchainFile(fToolchain, "bin");
				File linkerFile = DeviceActivator.getDefault().getToolchainFile(fBin, "arm-linux-gnueabihf-gcc.exe");

//				URL linker = FileLocator.find(DeviceActivator.getDefault().getBundle(), Path.ROOT.append("toolchain")
//						.append("compiler").append(toolChain).append("bin").append("arm-linux-gnueabihf-gcc.exe"),
//						null);

				File fBtechLib = DeviceActivator.getDefault()
						.getToolchainFile(DeviceActivator.getDefault().getToolchain(), "btech_lib");
				File fOpen62541 = DeviceActivator.getDefault().getToolchainFile(fBtechLib, "open62541");
				File fBuildArm = DeviceActivator.getDefault().getToolchainFile(fOpen62541, "build-arm");
				File open62541F = DeviceActivator.getDefault().getToolchainFile(fBuildArm, "bin");

//				URL open62541File = FileLocator.find(DeviceActivator.getDefault().getBundle(), Path.ROOT
//						.append("toolchain").append("btech_lib").append("open62541").append("build-arm").append("bin"),
//						null);
//				URL open62541FileFUri = FileLocator.toFileURL(open62541File);
//				File open62541F = new File(open62541FileFUri.getFile());

//				URL linker2 = FileLocator.toFileURL(linker);
//				File linkerFile = new File(linker2.getFile());
				fw.write(" " + linkerFile.getAbsolutePath());
				fw.write(" -L" + open62541F.getAbsolutePath());
				fw.write(
						" -mcpu=cortex-a7 -mthumb -O0 -fmessage-length=0 -fsigned-char -ffunction-sections -fdata-sections  -g3 -Xlinker --gc-sections -o \"btech_opcua_server.elf\" $(OBJS) $(LIBS)");

				fw.write("\n");
				fw.write("\n");
				// for (File file : files) {
				fw.write("%.o : %.c\n");

//				File fBtechLib = DeviceActivator.getDefault().getToolchainFile(DeviceActivator.getDefault().getToolchain(), "c");
				File fGccArm = DeviceActivator.getDefault().getToolchainFile(fCompiler,
						"gcc-linaro-arm-linux-gnueabihf-4.9.3");
				File fGCCArmBin = DeviceActivator.getDefault().getToolchainFile(fGccArm, "bin");
				File compilerFile = DeviceActivator.getDefault().getToolchainFile(fGCCArmBin,
						"arm-linux-gnueabihf-gcc.exe");

//				URL compiler = FileLocator.find(DeviceActivator.getDefault().getBundle(),
//						Path.ROOT.append("toolchain").append("compiler").append("gcc-linaro-arm-linux-gnueabihf-4.9.3")
//								.append("bin").append("arm-linux-gnueabihf-gcc.exe"),
//						null);
//				URL compiler2 = FileLocator.toFileURL(compiler);
//				File compilerFile = new File(compiler2.getFile());
				fw.write("\t" + compilerFile.getAbsolutePath());

				// include files
				// open62541/include
				// open62541/deps
				// open62541/build-arm/src_generated
				// open62541/plugins/include
				// open62541/deps/ua-nodeset/AnsiC
				// open62541/arch

				File includesFolder = DeviceActivator.getDefault().getToolchainFile(fOpen62541, "include");

//				URL includes = FileLocator.find(DeviceActivator.getDefault().getBundle(),
//						Path.ROOT.append("toolchain").append("btech_lib").append("open62541").append("include"), null);
//				URL includes2 = FileLocator.toFileURL(includes);
//				File includesFolder = new File(includes2.getFile());

				fw.write(" -I\"" + includesFolder.getAbsolutePath() + "\"");

				File fOpen62541Deps = includesFolder = DeviceActivator.getDefault().getToolchainFile(fOpen62541,
						"deps");
//				includes = FileLocator.find(DeviceActivator.getDefault().getBundle(),
//						Path.ROOT.append("toolchain").append("btech_lib").append("open62541").append("deps"), null);
//				includes2 = FileLocator.toFileURL(includes);
//				includesFolder = new File(includes2.getFile());
				fw.write(" -I\"" + includesFolder.getAbsolutePath() + "\"");

				includesFolder = DeviceActivator.getDefault().getToolchainFile(fBuildArm, "src_generated");

//				includes = FileLocator.find(DeviceActivator.getDefault().getBundle(), Path.ROOT.append("toolchain")
//						.append("btech_lib").append("open62541").append("build-arm").append("src_generated"), null);
//				includes2 = FileLocator.toFileURL(includes);
//				includesFolder = new File(includes2.getFile());
				fw.write(" -I\"" + includesFolder.getAbsolutePath() + "\"");

				File fOpen62541Plugins = DeviceActivator.getDefault().getToolchainFile(fOpen62541, "plugins");
				includesFolder = DeviceActivator.getDefault().getToolchainFile(fOpen62541Plugins, "include");

//				includes = FileLocator.find(DeviceActivator.getDefault().getBundle(), Path.ROOT.append("toolchain")
//						.append("btech_lib").append("open62541").append("plugins").append("include"), null);
//				includes2 = FileLocator.toFileURL(includes);
//				includesFolder = new File(includes2.getFile());
				fw.write(" -I\"" + includesFolder.getAbsolutePath() + "\"");

				File fOpen62541UANodeset = DeviceActivator.getDefault().getToolchainFile(fOpen62541Deps, "ua-nodeset");
				includesFolder = DeviceActivator.getDefault().getToolchainFile(fOpen62541UANodeset, "AnsiC");

//				includes = FileLocator.find(DeviceActivator.getDefault().getBundle(), Path.ROOT.append("toolchain")
//						.append("btech_lib").append("open62541").append("deps").append("ua-nodeset").append("AnsiC"),
//						null);
//				includes2 = FileLocator.toFileURL(includes);
//				includesFolder = new File(includes2.getFile());
				fw.write(" -I\"" + includesFolder.getAbsolutePath() + "\"");

				includesFolder = DeviceActivator.getDefault().getToolchainFile(fOpen62541, "arch");

//				includes = FileLocator.find(DeviceActivator.getDefault().getBundle(),
//						Path.ROOT.append("toolchain").append("btech_lib").append("open62541").append("arch"), null);
//				includes2 = FileLocator.toFileURL(includes);
//				includesFolder = new File(includes2.getFile());
				fw.write(" -I\"" + includesFolder.getAbsolutePath() + "\"");

				includesFolder = DeviceActivator.getDefault()
						.getToolchainFile(DeviceActivator.getDefault().getToolchain(), "btech_include");

//				includes = FileLocator.find(DeviceActivator.getDefault().getBundle(),
//						Path.ROOT.append("toolchain").append("btech_include"), null);
//				includes2 = FileLocator.toFileURL(includes);
//				includesFolder = new File(includes2.getFile());
				fw.write(" -I\"" + includesFolder.getAbsolutePath() + "\"");

				fw.write(
						" -O0 -g3 -Wall -c -fmessage-length=0 -MMD -MP -MF\"$(@:%.o=%.d)\" -MT\"$(@)\" -o \"$@\" \"$<\"\n");
				fw.write("\n");
				// }
				fw.write("cleanSources:\n");
				fw.write("\t-$(RM) $(C_SRCS)\n\n");// btech_opcua_server.elf ");
				fw.write("cleanBinary:\n");
				fw.write("\t-$(RM) $(OBJS)$(C_DEPS) btech_opcua_server.elf ");
				for (String f2del : extCFiles) {
					// fw.write(f2del + " ");
				}
				fw.write("\n");
			} catch (Exception e) {
			}
			// now remove also make file
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	/**
	 * **********
	 */
	@Override
	protected String titleJob() {
		return "com.bichler.astudio.device.core.monitor.upload.title";
	}

	@Override
	protected String descriptionJob() {
		return "com.bichler.astudio.device.website.install.monitor.upload";
	}
}
