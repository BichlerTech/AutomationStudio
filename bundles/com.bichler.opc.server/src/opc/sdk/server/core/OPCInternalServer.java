package opc.sdk.server.core;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.opcfoundation.ua.application.Application;
import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.DateTime;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.common.NamespaceTable;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.Attributes;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.WriteValue;

import com.bichler.opc.comdrv.ComDRVManager;
import com.bichler.opc.comdrv.ComResourceManager;
import com.bichler.opc.comdrv.IOPCInternalServer;

import opc.sdk.core.enums.RequestType;
import opc.sdk.core.language.LanguageItem;
import opc.sdk.core.node.Node;
import opc.sdk.server.IAddressSpaceConstants;
import opc.sdk.server.ILanguageServer;
import opc.sdk.server.core.managers.IOPCManager;
import opc.sdk.server.core.managers.OPCAddressSpaceManager;
import opc.sdk.server.core.managers.OPCDiscoveryManager;
import opc.sdk.server.core.managers.OPCHistoryManager;
import opc.sdk.server.core.managers.OPCMasterManager;
import opc.sdk.server.core.managers.OPCProfileManager;
import opc.sdk.server.core.managers.OPCServiceManager;
import opc.sdk.server.core.managers.OPCSessionManager;
import opc.sdk.server.core.managers.OPCSubscriptionManager;
import opc.sdk.server.core.managers.OPCUserAuthentificationManager;
import opc.sdk.server.language.LanguageManager;
import opc.sdk.server.service.node.UAServerObjectNode;
import opc.sdk.server.service.node.UAServerVariableNode;

public class OPCInternalServer extends OPCServer implements ILanguageServer, IOPCInternalServer {
	/** server timer */
	// private Timer serverTimer = new Timer();
	/** server threadpool */
	private ScheduledExecutorService threadPool = null;
	// private Timer threadpool = null;
	/** server session manager */
	private OPCSessionManager sessionManager;
	/** server subscription manager */
	private OPCSubscriptionManager subscriptionManager;
	/** server service manager */
	private OPCProfileManager serviceManager;
	/** server addressspace manager */
	private OPCAddressSpaceManager addressspaceManager;
	/** all internal managers */
	private List<IOPCManager> opcManagers = new ArrayList<>();
	/** master manager to execute all opc ua services */
	private OPCMasterManager internMaster = null;
	/** language manager */
	private LanguageManager languageManager = null;
	/** discovery manager */
	private OPCDiscoveryManager discoveryManager = null;
	/** history manager */
	private OPCHistoryManager historyManager = null;
	/** user authority manager */
	private OPCUserAuthentificationManager userManager = null;
	private UAServerApplicationInstance uaInstance;
	private ComResourceManager driverResMgr;
	private boolean useDiscoveryServer = false;
	private boolean useUserAuthentification = false;

	/** server configuration */
	public boolean isUseUserAuthentification() {
		return useUserAuthentification;
	}

	public void setUseUserAuthentification(boolean useUserAuthentification) {
		this.useUserAuthentification = useUserAuthentification;
	}

	// private OPCValidationFramework validationFramework;
	/**
	 * OPC UA internal server instance handling incoming requests for endpoints.
	 * 
	 * @param uaServerApplicationInstance
	 * 
	 * @param application
	 */
	public OPCInternalServer(UAServerApplicationInstance uaInstance, Application application) {
		super(application);
		this.uaInstance = uaInstance;
		// driver resource manager
		this.driverResMgr = ComDRVManager.getDRVManager().getResourceManager();
		// session manager
		this.sessionManager = new OPCSessionManager(this);
		this.opcManagers.add(this.sessionManager);
		// / subscription manager
		this.subscriptionManager = new OPCSubscriptionManager(this);
		this.opcManagers.add(this.subscriptionManager);
		//
		// HistoryConfiguration historyConfig = this.application
		// .get
		//
		// HistoryManager hManager = new SimpleSqlHistoryManager(
		// historyConfig.getDrvName(), historyConfig.getDatabaseUrl(),
		// historyConfig.getDatabase(), historyConfig.getUser(),
		// historyConfig.getPw());
		// setHistoryManager(-1, hManager);
		// history
		this.historyManager = new OPCHistoryManager(this);
		this.opcManagers.add(this.historyManager);
		// opc address space manager
		this.addressspaceManager = new OPCAddressSpaceManager(this);
		this.opcManagers.add(addressspaceManager);
		// internationalization
		this.languageManager = new LanguageManager(this.addressspaceManager);
		this.opcManagers.add(this.languageManager);
		// discovery
		this.discoveryManager = new OPCDiscoveryManager(this);
		this.opcManagers.add(this.discoveryManager);
		// history
		// this.historyManager = new OPCHistoryManager(this);
		// this.opcManagers.add(this.historyManager);
		// opc user service manager
		this.userManager = new OPCUserAuthentificationManager(this);
		this.opcManagers.add(this.userManager);
		// opc service profile manager
		this.serviceManager = new OPCProfileManager(this);
		this.opcManagers.add(this.serviceManager);
	}

	/**
	 * Language section
	 */
	@Override
	public void importLanguage(LanguageItem[] languages) {
		this.languageManager.importLanguage(languages);
	}

	public void importAdditionalModels() {
		List<String> models = getApplicationConfiguration().getServerInformationModels();
		for (String modelURI : models) {
			File modelFile = new File(modelURI);
			if (!modelFile.exists()) {
				continue;
			}
			try {
				this.uaInstance.importModel(null, modelURI);
			} catch (ServiceResultException e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
			}
		}
	}

	public void importAdditionalNodeSets() {
		List<String> nodesets = getApplicationConfiguration().getServerNodeSets();
		for (String modelURI : nodesets) {
			File modelFile = new File(modelURI);
			if (!modelFile.exists()) {
				continue;
			}
			try {
				this.uaInstance.importNodeSetModel(null, modelURI);
			} catch (ServiceResultException e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
			}
		}
	}

	/**
	 * Imports a jar model.
	 * 
	 * @param Path Path of the jar file
	 */
	public void importModel(String path) {
		URLClassLoader classLoader = null;
		try {
			// java url
			File jFile = new File(path);
			URL url = jFile.toURI().toURL();
			ClassLoader parentClassLoader = Thread.currentThread().getContextClassLoader();
			classLoader = new URLClassLoader(new URL[] { url }, parentClassLoader);
			// class name
			String classPath = IAddressSpaceConstants.PACKAGENAME + "." + IAddressSpaceConstants.MAINCLASSNAME;
			Class<?> c2 = Class.forName(classPath, true, classLoader);
			if (c2 == null) {
				return;
			}
			Object modelInstance = c2.newInstance();
			Method method = c2.getMethod(IAddressSpaceConstants.METHOD_NAME_INIT, UAServerApplicationInstance.class);
			method.invoke(modelInstance, this.uaInstance);
		} catch (MalformedURLException | ClassNotFoundException | NoSuchMethodException | SecurityException
				| IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| InstantiationException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
		}
		if (classLoader != null) {
			try {
				classLoader.close();
				classLoader = null;
				System.gc();
			} catch (IOException e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
			}
		}
	}

	public void setUserConfiguration(String userDB) {
		if (this.userManager != null)
			this.userManager.setUserDatabase(userDB);
	}

	public void initWorkspaceDefaults() {
		try {
			writeNamespaceTable();
			WriteValue write = new WriteValue();
			write = new WriteValue();
			write.setAttributeId(Attributes.Value);
			write.setNodeId(Identifiers.Server_ServerArray);
			write.setValue(new DataValue(new Variant(new String[] { getNamespaceUris().getUri(1) })));
			getMaster().write(new WriteValue[] { write }, true, null, null);
			write = new WriteValue();
			write.setAttributeId(Attributes.Value);
			write.setNodeId(Identifiers.Server_ServerStatus_CurrentTime);
			write.setValue(new DataValue(new Variant(DateTime.currentTime())));
			getMaster().write(new WriteValue[] { write }, true, null, null);
			// startSeverState();
		} catch (ServiceResultException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
		}
	}

	public void writeNamespaceTable() throws ServiceResultException {
		WriteValue write = new WriteValue();
		write = new WriteValue();
		write.setAttributeId(Attributes.Value);
		write.setNodeId(Identifiers.Server_NamespaceArray);
		write.setValue(new DataValue(new Variant(this.getNamespaceUris().toArray())));
		getMaster().write(new WriteValue[] { write }, true, null, null);
	}

	public void scheduleTask(Runnable task, int delay, long period) {
		this.threadPool.scheduleAtFixedRate(task, delay, period, TimeUnit.MILLISECONDS);
	}

	@Override
	public OPCAddressSpaceManager getAddressSpaceManager() {
		return this.addressspaceManager;
	}

	@Override
	public OPCDiscoveryManager getDiscoveryManager() {
		return this.discoveryManager;
	}

	public OPCHistoryManager getHistoryManager() {
		return this.historyManager;
	}

	@Override
	public LanguageItem[] getLanguageInformation(NodeId[] nodeIds) {
		return this.languageManager.getLanguageInformation(nodeIds);
	}

	@Override
	public LanguageManager getLanguageManager() {
		return this.languageManager;
	}

	/**
	 * Master is used to execute opc ua services synchonized! Prefered way to do!
	 * 
	 * @return
	 */
	public OPCMasterManager getMaster() {
		return this.internMaster;
	}

	public void setMaster(OPCMasterManager internMaster) {
		this.internMaster = internMaster;
	}

	@Override
	public String[] getServerLocales() {
		return this.languageManager.getServerLocales();
	}

	@Override
	public OPCSessionManager getSessionManager() {
		return this.sessionManager;
	}

	@Override
	public OPCSubscriptionManager getSubscriptionManager() {
		return this.subscriptionManager;
	}

	@Override
	public OPCServiceManager getServiceManager() {
		return this.serviceManager;
	}

	@Override
	public OPCUserAuthentificationManager getUserAuthentifiationManager() {
		return this.userManager;
	}

	public void logService(RequestType type, String message) {
		// log publish response message
		if (getLogger().isActivatedebug()) {
			if ((getLogger().getDebug() & getLogger().DEBUG_READNODE) != 0) {
				Logger.getLogger(getClass().getName()).log(Level.INFO, "{0}: {1}",
						new Object[] { type.name(), message });
			}
		}
	}

	@Override
	public Map<Integer, Integer> doChangeNamespaceTable(NamespaceTable newMappingTable,
			NamespaceTable originMappingTable) {
		// change address space nodeids
		Map<Integer, Integer> mapping = this.addressspaceManager.changeNamespaceTable(originMappingTable,
				newMappingTable);
		// change nodeids in typetable
		getTypeTable().doChangeNamespaceTable(getNamespaceUris(), mapping);
		setNamespaceTable(newMappingTable);
		return mapping;
	}

	/**
	 * Incoming from jar loading
	 * 
	 * @param externNsTable
	 * @param nodes
	 */
	protected void importModel(NamespaceTable externNsTable, Node[] nodes) {
		if (this.historyManager != null) {
			for (Node node : nodes) {
				if (node instanceof UAServerVariableNode) {
					((UAServerVariableNode) node).setHistory(this.historyManager.getHistory());
				} else if (node instanceof UAServerObjectNode) {
					((UAServerObjectNode) node).setHistory(this.historyManager.getHistory());
				}
			}
		}
		this.addressspaceManager.addNodes(externNsTable, nodes);
	}

	@Override
	protected void startServer() {
		// close open threadpool
		if (this.threadPool != null) {
			this.threadPool.shutdownNow();
			this.threadPool = null;
		}
		// init theadpool
		this.threadPool = Executors.newScheduledThreadPool(1);
		for (IOPCManager manager : this.opcManagers) {
			if (manager instanceof OPCDiscoveryManager) {
				if (this.useDiscoveryServer) {
					manager.start();
				}
			} else
				manager.start();
		}
		super.startServer();
	}

	@Override
	protected void startServerNoRunning() {
		// close open threadpool
		if (this.threadPool != null) {
			this.threadPool.shutdownNow();
			this.threadPool = null;
		}
		// init threadpool
		this.threadPool = Executors.newScheduledThreadPool(1);
		for (IOPCManager manager : this.opcManagers) {
			manager.start();
		}
		super.startServerNoRunning();
	}

	@Override
	protected void stopServer() {
		super.stopServer();
		for (IOPCManager manager : this.opcManagers) {
			manager.stop();
		}
		if (this.threadPool != null) {
			this.threadPool.shutdownNow();
			this.threadPool = null;
		}
	}

	private ComResourceManager getLogger() {
		return this.driverResMgr;
	}

	@Override
	protected ScheduledExecutorService getThreadPool() {
		return this.threadPool;
	}

	public boolean isUseDiscoveryServer() {
		return useDiscoveryServer;
	}

	public void setUseDiscoveryServer(boolean useDiscoveryServer) {
		this.useDiscoveryServer = useDiscoveryServer;
	}
}
