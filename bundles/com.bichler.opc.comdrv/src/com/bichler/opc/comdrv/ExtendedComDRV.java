package com.bichler.opc.comdrv;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.common.NamespaceTable;
import org.opcfoundation.ua.core.Attributes;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.TimestampsToReturn;

import com.bichler.opc.comdrv.importer.Com_CounterConfigGroup;
import com.bichler.opc.comdrv.importer.Com_DeviceConfigNode;
import com.bichler.opc.comdrv.importer.Com_Importer;
import com.bichler.opc.comdrv.importer.Com_StartConfigNode;
import com.bichler.opc.comdrv.importer.Com_TriggerDpItem;
import com.bichler.opc.comdrv.importer.Com_DeviceConfigNode.DeviceNode;

public abstract class ExtendedComDRV extends ComDRV {
	private List<Com_StartConfigNode> startConfigNodes = new ArrayList<Com_StartConfigNode>();
	private Com_DeviceConfigNode deviceConfigNode = new Com_DeviceConfigNode();
	private List<Com_CounterConfigGroup> counterConfigNodes = new ArrayList<Com_CounterConfigGroup>();
	private NamespaceTable namespaceTable;
	private Logger logger = Logger.getLogger(getClass().getName());

	@Override
	public void setDrvReconnectTimeout(String timeout) {
		// should be implemented in concrete object
	}

	@Override
	public void checkCommunication() {
		// should be implemented in concrete object
	}

	@Override
	public boolean doWork() {
		// should be implemented in concrete objectb
		return false;
	}

	@Override
	public boolean doFinish() {
		// should be implemented in concrete object
		return false;
	}

	/**
	 * default constructor, only call super constructor
	 */
	public ExtendedComDRV() {
		super();
	}

	public boolean renewNamespaceTable() {
		DataValue response = ComDRVManager.getDRVManager().readFromDriver(Identifiers.Server_NamespaceArray,
				Attributes.Value, null, null, null, 0.0, TimestampsToReturn.Neither);
		// check the namespace object
		if (response == null || response.getValue() == null) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE,
					"Read namespaces from internal server not possible!");
			return false;
		}
		// get whole namespaces from internal server to generate the right
		// name space index for each datapoint
		String[] namespaces = (String[]) response.getValue().getValue();
		if (namespaces == null) {
			logger.log(Level.SEVERE, "Read namespaces from internal server not possible!");
			return false;
		}
		namespaceTable = NamespaceTable.createFromArray(namespaces);
		return true;
	}

	/**
	 * Startup function is called before we start normal working process. We can do
	 * some initializations here.
	 */
	@Override
	public boolean doStartup() {
		if (this.drvManager.getResourceManager().isActivatedebug() && (this.drvManager.getResourceManager().getDebug()
				& this.drvManager.getResourceManager().DEBUG_STARTUP) == this.drvManager
						.getResourceManager().DEBUG_STARTUP) {
			logger.log(Level.INFO, "Read start configuration from file: startconfignodes.com");
		}
		FileInputStream input = null;
		try {
			/**
			 * first we create namespace table to map namespace uri to index
			 */
			if (!this.renewNamespaceTable()) {
				return false;
			}
			/**
			 * 
			 * now we start to import start config from startconfignodes.com file
			 * 
			 */
			File startconfig = new File("drivers/" + this.getDriverName() + "/startconfignodes.com");
			Com_Importer importer = new Com_Importer();
			if (startconfig.exists()) {
				input = new FileInputStream(startconfig);
				this.startConfigNodes = importer.loadStartConfigNodes(input, namespaceTable);
			} else {
				if (this.drvManager.getResourceManager().isActivatedebug()
						&& (this.drvManager.getResourceManager().getDebug()
								& this.drvManager.getResourceManager().DEBUG_STARTUP) == this.drvManager
										.getResourceManager().DEBUG_STARTUP) {
					logger.log(Level.INFO, "No start configuration found, so all nodes will be published!");
				}
			}
			/**
			 * 
			 * now we start to import start config from startconfignodes.com file
			 * 
			 */
			File deviceconfig = new File("drivers/" + this.getDriverName() + "/deviceconfig.com");
			if (deviceconfig.exists()) {
				input = new FileInputStream(deviceconfig);
				this.deviceConfigNode = importer.loadDeviceConfig(input, namespaceTable);
				// set all devices invisible
				if (this.deviceConfigNode != null) {
					List<NodeId> ids = new ArrayList<>();
					// first set all nodes visible = false
					for (DeviceNode dev : this.deviceConfigNode.getDevices().values()) {
						ids.add(dev.getDeviceNodeId());
					}
					// set device false
					drvManager.setNodeVisible(ids.toArray(new NodeId[ids.size()]), false);
				}
			} else {
				if (this.drvManager.getResourceManager().isActivatedebug()
						&& (this.drvManager.getResourceManager().getDebug()
								& this.drvManager.getResourceManager().DEBUG_STARTUP) == this.drvManager
										.getResourceManager().DEBUG_STARTUP) {
					logger.log(Level.INFO, "No device configuration found, so all devices will be published!");
				}
			}
			File counterfile = new File("drivers/" + this.getDriverName() + "/counter.com");
			if (counterfile.exists()) {
				input = new FileInputStream(counterfile);
				counterConfigNodes = importer.loadCounterConfig(input, namespaceTable);
			} else {
				if (this.drvManager.getResourceManager().isActivatedebug()
						&& (this.drvManager.getResourceManager().getDebug()
								& this.drvManager.getResourceManager().DEBUG_STARTUP) == this.drvManager
										.getResourceManager().DEBUG_STARTUP) {
					logger.log(Level.INFO,
							"No counter configuration found, so all counters of each device will be published!");
				}
			}
		} catch (FileNotFoundException e) {
			logger.log(Level.SEVERE, e.getMessage());
		}
		return true;
	}

	public void createTriggerList(Map<String, Com_TriggerDpItem> map, NamespaceTable uris) {
		if (map != null) {
			// first create scedule list for all cyclic dps
			Com_Importer importer = new Com_Importer();
			InputStream input;
			try {
				input = new FileInputStream("drivers/" + this.getDriverName() + "/triggernodes.com");
				List<Com_TriggerDpItem> dps = importer.loadTriggerDPs(input, uris);
				if (dps != null) {
					for (Com_TriggerDpItem dp : dps) {
						map.put(dp.getTriggerID(), dp);
					}
				}
			} catch (IOException ex) {
				logger.log(Level.SEVERE, ex.getMessage());
			}
			return;
		}
	}

	public List<Com_StartConfigNode> getStartConfigNodes() {
		return startConfigNodes;
	}

	public void setStartConfigNodes(List<Com_StartConfigNode> startConfigNodes) {
		this.startConfigNodes = startConfigNodes;
	}

	public List<Com_CounterConfigGroup> getCounterConfigNodes() {
		return counterConfigNodes;
	}

	public void setCounterConfigNodes(List<Com_CounterConfigGroup> counterConfigNodes) {
		this.counterConfigNodes = counterConfigNodes;
	}

	public Com_DeviceConfigNode getDeviceConfigNode() {
		return deviceConfigNode;
	}

	public void setDeviceConfigNodes(Com_DeviceConfigNode deviceConfigNode) {
		this.deviceConfigNode = deviceConfigNode;
	}

	public NamespaceTable getNamespaceTable() {
		return namespaceTable;
	}

	public void setNamespaceTable(NamespaceTable namespaceTable) {
		this.namespaceTable = namespaceTable;
	}
}
