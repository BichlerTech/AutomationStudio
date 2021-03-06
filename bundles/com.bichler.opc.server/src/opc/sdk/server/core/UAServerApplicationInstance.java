package opc.sdk.server.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import opc.sdk.core.application.OPCEntry;
import opc.sdk.core.application.operation.ICancleOperation;
import opc.sdk.core.node.Node;
import opc.sdk.core.persistence.xml.SAXNodeReader;
import opc.sdk.core.persistence.xml.SAXNodeSetReader;

import org.opcfoundation.ua.application.Application;
import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.StatusCode;
import org.opcfoundation.ua.common.NamespaceTable;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.AddNodesItem;
import org.opcfoundation.ua.core.AddNodesResult;
import org.opcfoundation.ua.core.AddReferencesItem;
import org.opcfoundation.ua.transport.security.CertificateValidator;

/**
 * Generic OPC Server. The core server class.
 * 
 * @author Thomas Z&ouml;chbauer
 * 
 */
public class UAServerApplicationInstance {
	/**
	 * Internal Server instance
	 */
	private OPCInternalServer server = null;

	/**
	 * Application Configuration
	 */
	public UAServerApplicationInstance() {
		Application application = new Application();
		application.getOpctcpSettings().setCertificateValidator(CertificateValidator.ALLOW_ALL);
		application.getHttpsSettings().setCertificateValidator(CertificateValidator.ALLOW_ALL);
		this.server = new OPCInternalServer(this, application);
	}

	public void start() {
		this.server.startServer();
	}

	public void startNoRunning() {
		this.server.startServerNoRunning();
	}

	public void stop() {
		this.server.stopServer();
	}

	/**
	 * Returns the Internal Server instance.
	 * 
	 * @return server.
	 */
	public OPCInternalServer getServerInstance() {
		return this.server;
	}

	/**
	 * Imports a xml information model.
	 * 
	 * @param Monitor Monitor to cancle import
	 * @param Path    Path of the xml file
	 * @return
	 * @throws ServiceResultException
	 */
	public int importModel(ICancleOperation monitor, String path) throws ServiceResultException {
		SAXNodeReader parser = new SAXNodeReader(this.server.getNamespaceUris(), this.server.getServerUris(),
				this.server.getTypeTable());
		parser.setProgressMonitor(monitor);
		OPCEntry<List<AddNodesItem>, List<AddReferencesItem>> nodes = parser.createNodes(path);
		Map<ExpandedNodeId, AddNodesItem> mappednodes = new HashMap<>();
		if (nodes == null) {
			return 0;
		} else {
			for (AddNodesItem item : nodes.getKey()) {
				mappednodes.put(item.getRequestedNewNodeId(), item);
			}
		}
		List<AddNodesItem> key = nodes.getKey();
		List<AddReferencesItem> value = nodes.getValue();
		AddNodesItem[] nodesToAdd = key.toArray(new AddNodesItem[0]);
		AddReferencesItem[] referencesToadd = value.toArray(new AddReferencesItem[0]);
		key.clear();
		value.clear();
		AddNodesResult[] nodesresult = this.server.getMaster().addNodes(nodesToAdd, mappednodes, false, null, false);
		int count = nodesToAdd.length;
		if (nodesresult.length != count) {
			Logger.getLogger(getClass().getName()).log(Level.WARNING,
					"Result from add nodes service ({0}) differs from node count ({1})!",
					new String[] { Integer.toString(nodesresult.length), Integer.toString(count) });
		}
		StatusCode[] addRefResult = this.server.getMaster().addReferences(referencesToadd, null);
		int refCount = referencesToadd.length;
		if (addRefResult.length != refCount) {
			Logger.getLogger(getClass().getName()).log(Level.WARNING,
					"Result from add reference service ({0}) differs from reference count ({1})!",
					new String[] { Integer.toString(addRefResult.length), Integer.toString(refCount) });
		}
		try {
			this.server.writeNamespaceTable();
		} catch (ServiceResultException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
		}
		return count;
	}

	/**
	 * Imports a xml information model.
	 * 
	 * @param Monitor Monitor to cancle import
	 * @param Path    Path of the xml file
	 * @return
	 * @throws ServiceResultException
	 */
	public int importNodeSetModel(ICancleOperation monitor, String path) throws ServiceResultException {
		SAXNodeSetReader parser = new SAXNodeSetReader(this.server.getNamespaceUris(), this.server.getServerUris(),
				this.server.getTypeTable());
		parser.setProgressMonitor(monitor);
		OPCEntry<List<AddNodesItem>, List<AddReferencesItem>> nodes = parser.createNodes(path);
		Map<ExpandedNodeId, AddNodesItem> mappednodes = new HashMap<>();
		if (nodes == null) {
			return 0;
		} else {
			for (AddNodesItem item : nodes.getKey()) {
				mappednodes.put(item.getRequestedNewNodeId(), item);
			}
		}
		List<AddNodesItem> key = nodes.getKey();
		List<AddReferencesItem> value = nodes.getValue();
		AddNodesItem[] nodesToAdd = key.toArray(new AddNodesItem[0]);
		AddReferencesItem[] referencesToadd = value.toArray(new AddReferencesItem[0]);
		key.clear();
		value.clear();
		this.server.getMaster().addNodes(nodesToAdd, mappednodes, false, null, false);
		int count = nodesToAdd.length;
		this.server.getMaster().addReferences(referencesToadd, null);
		try {
			this.server.writeNamespaceTable();
		} catch (ServiceResultException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
		}
		return count;
	}

	public void importModel(NamespaceTable externNsTable, Node[] nodes) {
		this.server.importModel(externNsTable, nodes);
	}

	public void initWorkspaceDefaults() {
		this.server.initWorkspaceDefaults();
	}
}
