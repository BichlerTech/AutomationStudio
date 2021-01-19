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
package com.bichler.opc.comdrv;

import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.DateTime;
import org.opcfoundation.ua.builtintypes.NodeId;

import com.bichler.opc.comdrv.utils.ValueOutOfRangeException;

/**
 * this class is the base abstract class of all system data points with default
 * variables and functions here are also default implementations of value-change
 * and read and write functions
 * 
 * @version v1.0.0.1
 * @author hannes
 * @since 30.09.2009
 *
 */
public abstract class ComDP {
	/***************************************************************************
	 * 
	 * region protected fields
	 * 
	 **************************************************************************/
	/**
	 * name of this dp
	 */
	protected String dpName = "";
	/**
	 * name of the module this device belongs to
	 */
	protected String module = "";
	/**
	 * device address of this data point
	 */
	protected String address = "";
	/**
	 * time-stamp of last successful read of this data point from device or program
	 */
	protected DateTime readtimestamp = DateTime.currentTime();
	/**
	 * time-stamp of last successful write of this data point from device or program
	 */
	protected DateTime writetimestamp = DateTime.currentTime();
	protected NodeId nodeId = null;
	protected ComResourceManager manager = null;

	public ComResourceManager getManager() {
		return manager;
	}

	public void setManager(ComResourceManager manager) {
		this.manager = manager;
	}

	/**
	 * state of the data point to identify who has changed the value find all
	 * defined stated in Cometdata pointStates class
	 * 
	 * 0 -> initial 1 -> notification changed 2 -> scheduler changed 10 -> visu
	 * changed ....
	 * 
	 */
	protected long state = 0;

	/***************************************************************************
	 * 
	 * end region protected fields
	 * 
	 **************************************************************************/
	/***************************************************************************
	 * 
	 * region setter functions
	 * 
	 **************************************************************************/
	/**
	 * set name of the data point
	 * 
	 * @param name name of data point
	 */
	public void setdpName(String name) {
		this.dpName = name;
	}

	/**
	 * set address of the data point
	 * 
	 * @param addr address of data point
	 */
	public void setAddress(String addr) {
		this.address = addr;
	}

	/**
	 * set module name of the data point
	 * 
	 * @param module module name of data point
	 */
	public void setModuleName(String module) {
		this.module = module;
	}

	/**
	 * set time-stamp of data point
	 * 
	 * @param time time in milliseconds
	 */
	public void setWriteTimeStamp(DateTime time) {
		this.writetimestamp = time;
	}

	/**
	 * set write time-stamp from system time
	 */
	public void setWriteTimeStamp() {
		this.writetimestamp = DateTime.currentTime();
	}

	/**
	 * set time-stamp of data point
	 * 
	 * @param time time in milliseconds
	 */
	public void setReadTimeStamp(DateTime time) {
		this.readtimestamp = time;
	}

	/**
	 * set read time-stamp from system time
	 */
	public void setReadTimeStamp() {
		this.readtimestamp = DateTime.currentTime();
	}

	/**
	 * set the state of the data point -1 -> value read failure 0 -> initial 1 ->
	 * notification changed 2 -> scheduler changed 10 -> visu changed ...
	 * 
	 * @param state state to set
	 */
	public void setState(long state) {
		this.state = state;
	}

	/**
	 * Set the nodeid of this data point in the ua address space, (value of this
	 * data point).
	 * 
	 * @param nodeId
	 */
	public void setNodeId(NodeId nodeId) {
		this.nodeId = nodeId;
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
	 * get name of the data point
	 * 
	 * @return name of data point
	 */
	public String getdpName() {
		return this.dpName;
	}

	/**
	 * get address of the data point
	 * 
	 * @return address of data point
	 */
	public String getAddress() {
		return this.address;
	}

	/**
	 * get write time-stamp of data point
	 * 
	 * @return time-stamp in milliseconds
	 */
	public DateTime getWriteTimestamp() {
		return this.writetimestamp;
	}

	/**
	 * get read time-stamp of data point
	 * 
	 * @return time-stamp in milliseconds
	 */
	public DateTime getReadTimestamp() {
		return this.readtimestamp;
	}

	/**
	 * get the state of the data point
	 * 
	 * 0 -> initial 1 -> notification changed 2 -> scheduler changed 10 -> visu
	 * changed ...
	 * 
	 * @return state
	 */
	public long getState() {
		return this.state;
	}

	/**
	 * Set the nodeid of this data point in the ua address space, (value of this
	 * data point).
	 * 
	 * @param nodeId
	 */
	public NodeId getNodeId() {
		return this.nodeId;
	}

	/***************************************************************************
	 * 
	 * end region getter functions
	 * 
	 **************************************************************************/
	/***************************************************************************
	 * 
	 * region data transformations
	 * 
	 **************************************************************************/
	/**
	 * change value from driver value into program representation datatype must be
	 * changed and scale factor is calculated into value
	 * 
	 * @return value as ICometDataBuffer object
	 */
	public abstract DataValue drv2Prog(byte[] data);

	/**
	 * change from variant from device to driver datatype and update intern driver
	 * value
	 * 
	 * @param val value to change(convert)
	 * @return
	 */
	public abstract byte[] prog2DRV(DataValue val) throws ValueOutOfRangeException;
	/***************************************************************************
	 * 
	 * end region data transformations
	 * 
	 **************************************************************************/
}
