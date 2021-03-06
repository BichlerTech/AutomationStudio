// (c) Copyright 2010 HB-Softsolution e.U.
// ALL RIGHTS RESERVED.
//
// DISCLAIMER:
// This code is provided and developed by HB-Softsolution e.U.
// Distribution of this source code underlies the Warranty
// and Liability Disclaimers which appear in the printed
// HB-Softsolution e.U. license agreements.
//
// Author:
// hannes bichler
// Company:
// HB-Softsolution e.U.
// Web:
// www.hb-softsoluiton.com
// contact
// hannes.bichler@hb-softsolution.com
//
//
package com.bichler.opc.comdrv;

import java.util.HashMap;
import java.util.Map;

import org.opcfoundation.ua.builtintypes.NodeId;

/**
 * This class contains base class of a communication device implementation. Here
 * are some base variables and functions defined.
 * 
 * @author hannes
 * @version v1.0.0.1
 * @since 05.05.2010
 *
 */
public abstract class AComDevice {
	/***************************************************************************
	 * 
	 * region protected fields fields only visible in subclasses
	 * 
	 **************************************************************************/
	/**
	 * name of the Device e.g. OPC_UA_1, Beckhoff_ADS_2
	 */
	protected String deviceName = "";
	/**
	 * address of the Device here you can set also complex address strings
	 */
	protected String deviceAddress = null;
	/**
	 * state of the driver
	 * 
	 * possible states defined in each specific driver 0 = closed as default
	 */
	protected long deviceState = ComCommunicationStates.CLOSED;
	/**
	 * timeout between reconnect should be done;
	 */
	protected long reconnectTimeout = 1000;
	protected long lastReconnect = 0;

	public long getReconnectTimeout() {
		return reconnectTimeout;
	}

	public void setReconnectTimeout(long reconnectTimeout) {
		this.reconnectTimeout = reconnectTimeout;
	}

	public long getLastReconnect() {
		return lastReconnect;
	}

	public void setLastReconnect(long lastReconnect) {
		this.lastReconnect = lastReconnect;
	}

	/**
	 * state of the driver as string
	 * 
	 * possible states defined in each specific driver
	 */
	protected String deviceStateStr = "";
	protected IComDPManager dpManager = null;
	/**
	 * list with all data points connected to the driver
	 */
	protected HashMap<NodeId, ComDP> dpList = new HashMap<>();

	/***************************************************************************
	 * 
	 * end region protected fields fields only visible in subclasses
	 * 
	 **************************************************************************/
	/***************************************************************************
	 * 
	 * region constructor
	 * 
	 **************************************************************************/
	/**
	 * default constructor with no effect
	 */
	public AComDevice() {
		// implement constructor in each sub class
	}

	/***************************************************************************
	 * 
	 * end region constructor
	 * 
	 **************************************************************************/
	/**
	 * Add one data point to actual data point-list.
	 * 
	 * @param nodeId NodeId of data point to add.
	 * @return True if data point could be added correct,otherwise false;
	 */
	public abstract boolean addDP(NodeId nodeId);

	/**
	 * Add one data point to actual data point-list.
	 * 
	 * @param nodeId     NodeId of data point to add.
	 * @param additional Additional information for the data point, you can use it
	 *                   driver specific.
	 */
	public abstract ComDP addDP(NodeId nodeId, Object additional);

	/**
	 * Add all data points to actual data point-list.
	 * 
	 * @param nodeId     NodeId of data point to add.
	 * @param additional Additional information for the data point, you can use it
	 *                   driver specific.
	 */
	public abstract boolean addDPs(NodeId nodeId, Object additional);

	/***************************************************************************
	 * 
	 * region setter functions
	 * 
	 **************************************************************************/
	/**
	 * Set the name of the Device.
	 * 
	 * @param name Name to set.
	 */
	public void setDeviceName(String name) {
		this.deviceName = name;
	}

	/**
	 * Set the address of the Device.
	 * 
	 * @param address Address to set.
	 * @return true if it is a valid address otherwise false
	 */
	public boolean setDeviceAddress(String address) {
		this.deviceAddress = address;
		return false;
	}

	/**
	 * Set state of the Device, e.g. running, stopped, ...
	 * 
	 * @param state Value indicate the state of the driver.
	 */
	public void setDeviceState(int state) {
		this.deviceState = state;
	}

	/**
	 * Set state of the Device as string.
	 * 
	 * @param string indicate the state of the driver
	 */
	public void setDeviceStateStr(String state) {
		this.deviceStateStr = state;
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
	 * get name of the Device
	 * 
	 * @return name of Device
	 */
	public String getDeviceName() {
		return this.deviceName;
	}

	/**
	 * get state of the Device
	 * 
	 * @return value indicate that the driver is connected or not
	 */
	public long getDeviceState() {
		return this.deviceState;
	}

	/**
	 * Set state of the Device as string
	 * 
	 * @return string indicate the state of the driver
	 */
	public String getDeviceStateStr() {
		return this.deviceStateStr;
	}

	/**
	 * get list with all data points configured in that driver
	 * 
	 * @return list with all data points connected
	 */
	public Map<NodeId, ComDP> getDPList() {
		return this.dpList;
	}

	/***************************************************************************
	 * 
	 * end region getter functions
	 * 
	 **************************************************************************/
	/***************************************************************************
	 * 
	 * region connect functions
	 * 
	 **************************************************************************/
	/**
	 * Donnect Device to periphery device.
	 * 
	 * @return result of Device connect
	 */
	public abstract long connect();

	/**
	 * Disconnect Device from device
	 */
	public void disconnect() {
	}

	/**
	 * Disconnect Device from device
	 */
	public long reconnect() {
		return -1;
	}

	/**
	 * Check communication for its state e.g closed, open, ...
	 */
	public abstract void checkCommunication();

	/***************************************************************************
	 * 
	 * end region connect functions
	 * 
	 **************************************************************************/
	/***************************************************************************
	 * 
	 * region data point list manipulation
	 * 
	 **************************************************************************/
	/**
	 * Removes one data point from actual data point-list.
	 * 
	 * @param nodeId ID of data point to remove
	 * @return return the data point which should be removed or null if no data
	 *         point could be found
	 */
	public ComDP removeDPFromList(NodeId nodeId) {
		// if not initialized generate a new one
		if (this.dpList == null) {
			return null;
		}
		// add data point to actual dplist
		return this.dpList.remove(nodeId);
	}

	/***************************************************************************
	 * 
	 * end region data point list manipulation
	 * 
	 **************************************************************************/
	/***************************************************************************
	 * 
	 * region read functions
	 * 
	 **************************************************************************/
	/**
	 * Store read request to write it later via doWork function to ua Server.
	 * 
	 * @param data point to read from communication
	 */
	public abstract void storeReadReq(ComDP dp/* , long senderState */);

	/***************************************************************************
	 * 
	 * end region read functions
	 * 
	 **************************************************************************/
	/***************************************************************************
	 * 
	 * region write functions
	 * 
	 **************************************************************************/
	public abstract byte[] read();

	public abstract int write(byte[] data);
}
