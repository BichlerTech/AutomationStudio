// (c) Copyright 2010 HB-Softsolution e.U.
// ALL RIGHTS RESERVED.
//
// DISCLAIMER:
// This code is provided and developed by HB-Softsolution e.U.
// Distribution of this source code underlies the Warranty
// and Liability Disclaimers which appear in the
// HB-Softsolution e.U. license agreements.
//
// Authors: hannes bichler
// Company: HB-Softsolution e.U.
// Web: www.hb-softsoluiton.com
// contact: hannes.bichler@hb-softsolution.com
/**
 * @version v1.0.0.1
 * @since 05.05.2009
 * 
 * @company HB-Softsolution e.U.
 * @web www.hb-softsoluiton.com
 * 
 * @author hannes bichler
 * @contact hannes.bichler@hb-softsolution.com
 * 
 *          description: this class contains the implementation of a driver
 *          which communicate over the communication object with the device
 * 
 *          ---------------------------------------------------------------
 */
package com.bichler.opc.comdrv;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.StatusCode;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.core.Attributes;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.ServerState;

import opc.sdk.core.node.Node;

public abstract class ComDRV {
	public static final String VERSIONID = "1.0.1";
	public static final String BUNDLEID = "com.bichler.opc.comdrv";
	/***************************************************************************
	 * 
	 * region private fields
	 * 
	 **************************************************************************/
	protected int drvTypeId = 0;
	protected long drvId = 0;
	/**
	 * name of the driver
	 */
	protected String drvName = "";
	protected boolean needSchedule = false;
	protected long scheduleNano = 100000000L;
	protected long lastRun = 0;
	protected String drvProperties = "";
	protected String drvNameSpace = "";
	private Logger logger = Logger.getLogger(getClass().getName());
	/**
	 * Device device(driver)
	 */
	protected AComDevice device = null;
	/**
	 * state of this communication driver
	 */
	protected long state = ComDRVStates.STOPPED;
	protected ComDRVManager drvManager = ComDRVManager.getDRVManager();

	public abstract void setDrvReconnectTimeout(String timeout);

	/**
	 * Default constructor with no effect
	 */
	public ComDRV() {
	}

	public String getDrvNameSpace() {
		return drvNameSpace;
	}

	public void setDrvNameSpace(String drvNameSpace) {
		this.drvNameSpace = drvNameSpace;
	}

	public String getDrvProperties() {
		return drvProperties;
	}

	public void setDrvProperties(String drvProperties) {
		this.drvProperties = drvProperties;
	}

	public long getLastRun() {
		return lastRun;
	}

	public void setLastRun(long lastRun) {
		this.lastRun = lastRun;
	}

	public long getScheduleNano() {
		return scheduleNano;
	}

	public void setScheduleNano(long scheduleNano) {
		this.scheduleNano = scheduleNano;
	}

	protected boolean redundant = false;

	public boolean isRedundant() {
		return redundant;
	}

	public void setRedundant(boolean redundant) {
		this.redundant = redundant;
	}

	/**
	 * subtype of communication for special reads
	 */
	public boolean isNeedSchedule() {
		return needSchedule;
	}

	public void setNeedSchedule(boolean needSchedule) {
		this.needSchedule = needSchedule;
	}

	/***************************************************************************
	 * 
	 * end region private fields
	 * 
	 **************************************************************************/
	/***************************************************************************
	 * 
	 * region constructor
	 * 
	 **************************************************************************/
	/***************************************************************************
	 * 
	 * end region constructor
	 * 
	 **************************************************************************/
	/***************************************************************************
	 * 
	 * region setter functions
	 * 
	 **************************************************************************/
	public void setDeviceAddress(String address) {
		if (this.device != null) {
			this.device.setDeviceAddress(address);
		}
	}

	/**
	 * sets the id of the driver
	 * 
	 * @param drvTypeId driver type id of this driver
	 */
	public void setDriverTypeId(int drvTypeId) {
		this.drvTypeId = drvTypeId;
	}

	/**
	 * Initialize function which will be called from drivermanager on startup. Here
	 * we can add all listeners which we want to provide. Her we also need to set if
	 * that driver should be cyclic scheduled.
	 */
	public void initialize() {
	}

	/**
	 * sets the id of the driver
	 * 
	 * @param drvId id of this driver
	 */
	public void setDriverId(long drvId) {
		this.drvId = drvId;
	}

	/**
	 * sets the name of the driver
	 * 
	 * @param drvName name of this driver
	 */
	public void setDriverName(String drvName) {
		this.drvName = drvName;
	}

	/**
	 * set device of the driver
	 * 
	 * @param com device object for this driver
	 */
	public void setDevice(AComDevice com) {
		this.device = com;
	}

	/**
	 * set the state of this driver
	 * 
	 * @param state state of this driver
	 */
	public void setState(long state) {
		this.state = state;
	}

	/***************************************************************************
	 * 
	 * end region setter functions
	 * 
	 **************************************************************************/
	/***************************************************************************
	 * 
	 * region getter functions
	 * 
	 **************************************************************************/
	/**
	 * gets the id of the driver type
	 * 
	 * @return id of this driver type
	 */
	public int getDriverTypeId() {
		return this.drvTypeId;
	}

	/**
	 * gets the id of the driver
	 * 
	 * @return id of this driver
	 */
	public long getDriverId() {
		return this.drvId;
	}

	/**
	 * gets the name of the driver
	 * 
	 * @return name of this driver
	 */
	public String getDriverName() {
		return this.drvName;
	}

	/**
	 * get state of the Device
	 * 
	 * @return actual Device state
	 */
	public long getDeviceState() {
		return this.device.getDeviceState();
	}

	/**
	 * get state of the driver
	 * 
	 * @return actual state of the driver
	 */
	public long getState() {
		return this.state;
	}

	/**
	 * get the Device of the driver
	 * 
	 * @return Device object of this driver
	 */
	public AComDevice getDevice() {
		return this.device;
	}

	/**
	 * get all children for nodeid, and add it to list, we don't add duplicate
	 * elements to prevent cyclic loops
	 * 
	 * @param nodes
	 * @param node
	 */
	public void fetchChildren(List<NodeId> nodes, NodeId node) {
		Node[] children = this.drvManager.getChildren(node);
		if (children == null || children.length == 0) {
			return;
		}
		for (Node child : children) {
			if (!nodes.contains(child.getNodeId())) {
				nodes.add(child.getNodeId());
				fetchChildren(nodes, child.getNodeId());
			}
		}
	}

	/**
	 * remove all defined nodes of the list incl. all children
	 * 
	 * @param nodes
	 */
	public void removeNotRequiredNodes(List<NodeId> nodes) {
		// get all children of each node
		List<NodeId> children = new ArrayList<>(nodes);
		for (NodeId elId : nodes) {
			this.fetchChildren(children, elId);
		}
		if (nodes != null && !nodes.isEmpty()) {
			StatusCode[] codes = this.drvManager.removeNodes(children.toArray(new NodeId[children.size()]), true);
			for (int i = 0; i < codes.length; i++) {
				StatusCode c = codes[i];
				if (c != null && c.isBad()) {
					logger.log(Level.SEVERE,
							"Couldn't remove element from opc ua address space! Nodeid: " + children.get(i));
				}
			}
		}
	}

	/**
	 * get the sub-type of the driver
	 * 
	 * @param sub -type of this driver
	 * 
	 *            public int getSubType() { return this.commsubtype; }
	 */
	/***************************************************************************
	 * 
	 * end region getter functions
	 * 
	 **************************************************************************/
	/***************************************************************************
	 * 
	 * region driver internals
	 * 
	 **************************************************************************/
	/**
	 * checks the state of the Device
	 */
	public abstract void checkCommunication();

	/**
	 * stop this running driver
	 * 
	 * @return always true
	 */
	public boolean stop() {
		return true;
	}

	/**
	 * starts this stopped driver
	 * 
	 * @return always true
	 */
	public boolean start() {
		this.drvManager.writeFromDriver(new NodeId[] { Identifiers.Server_ServerStatus_State },
				new UnsignedInteger[] { Attributes.Value }, null,
				new DataValue[] { new DataValue(new Variant(ServerState.Running)) }, new Long[] { this.drvId });
		return true;
	}

	/***************************************************************************
	 * 
	 * end region driver internals
	 * 
	 **************************************************************************/
	/***************************************************************************
	 * 
	 * end region write functions
	 * 
	 **************************************************************************/
	/**
	 * Startup function is called before we start normal working process. We can do
	 * some initializations here.
	 */
	public abstract boolean doStartup();

	/***************************************************************************
	 * work function will be called every xx interval we have to return true if we
	 * read or write any value otherwise false
	 **************************************************************************/
	public abstract boolean doWork();

	/**
	 * Will be called after shut down request for the driver to free all resources,
	 * if required by the underlying system
	 * 
	 * @return
	 */
	public abstract boolean doFinish();
}
