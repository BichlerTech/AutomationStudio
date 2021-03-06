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

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.*;

// import org.apache.log4j.Logger;
// import org.apache.log4j.PropertyConfigurator;
import org.opcfoundation.ua.builtintypes.NodeId;

/**
 * this resourceManager handles creation of new communications, connections and
 * data point and set their properties
 * 
 * 
 * @version v1.0.0.1
 * @author hannes
 * @since 30.09.2009
 */
public class ComResourceManager {
	/***************************************************************************
	 * 
	 * region private fields
	 * 
	 **************************************************************************/
	private String rootDir = "";
	private String workingDir = "";
	private String runtimeDir = "";
	public static String CONFIGPATH = "";
	/**
	 * all connected drivers to that manager
	 */
	private Map<Long, ComDRV> drivers = new HashMap<>();
	/**
	 * connection object to communicate with the Visu, TrendChart or other moduls
	 */
	private IComDriverConnection cometConnection = null;
	/**
	 * Scheduler object to run in loop and call all doWork functions for each
	 * driver.
	 */
	private CometScheduler scheduler = null;
	private int debug = 0;
	private boolean activatedebug = false;
	private long lastdebugread = 0;
	private List<NodeId> nids = new ArrayList<>();
	public final int DEBUG_STARTUP = 1;
	public final int DEBUG_READINTERVAL = 2;
	public final int DEBUG_READPACKAGE = 4;
	public final int DEBUG_READNODE = 8;
	public final int DEBUG_WRITE = 16;
	// list with all drivers ordered by name
	private List<ComDRV> orderedDrivers = new ArrayList<>();
	private Logger logger = Logger.getLogger(getClass().getName());

	/***************************************************************************
	 * 
	 * end region private fields
	 * 
	 **************************************************************************/
	public int getDebug() {
		return debug;
	}

	public void setDebug(int debug) {
		this.debug = debug;
	}

	public boolean isActivatedebug() {
		return activatedebug;
	}

	public void setActivatedebug(boolean activatedebug) {
		this.activatedebug = activatedebug;
	}

	public long getLastdebugread() {
		return lastdebugread;
	}

	public void setLastdebugread(long lastdebugread) {
		this.lastdebugread = lastdebugread;
	}

	public List<NodeId> getNids() {
		return nids;
	}

	public void setNids(List<NodeId> nids) {
		this.nids = nids;
	}

	/***************************************************************************
	 * 
	 * region add functions
	 * 
	 **************************************************************************/
	/**
	 * Add one driver to the driver list, if list is null so create it new.
	 * 
	 * @param driverId Id of driver to add.
	 * @param driver   Driver object to add.
	 */
	public void addDriver(long driverId, ComDRV driver) {
		this.drivers.put(driverId, driver);
	}

	/**
	 * add one communication to the communication device, it can be a ADS
	 * communication, an OPC UA communication or a default one
	 * 
	 * @param comname name for communication, for which it can be located
	 * @param commtyp type of communication, e.g. HBS_DEFAULT, BECKHOFF_ADS
	 * @param addr    address of communication
	 * @param port    port to which it is connected
	 */
	public void addDriver(String drvName, int drvId, int drvTypeId, String drvTypeName, String pack) {
		if (this.activatedebug && (this.debug & this.DEBUG_STARTUP) == this.DEBUG_STARTUP) {
			logger.log(Level.INFO, "Start creating a communication object for {0}", drvName);
		}
		// class binary object
		Class<?> cls = null;
		// initialize all communications not connected
		AComDevice dev = null;
		// generate a new driver
		ComDRV drv = null;
		// load dynamically communication library from char from driver folder
		/*
		 * Create the .jar class loader
		 */
		/* Load the class from the .jar file and resolve it. */
		try {
			cls = ClassLoader.getSystemClassLoader().loadClass(pack + ".CometDevice");
			dev = (AComDevice) cls.newInstance();
			cls = ClassLoader.getSystemClassLoader().loadClass(pack + ".CometDRV");
			drv = (ComDRV) cls.newInstance();
		} catch (ClassNotFoundException ex) {
			logger.log(Level.SEVERE, "Could not find class for " + drvName);
			return;
		} catch (IllegalAccessException iex) {
			logger.log(Level.SEVERE, "Illegal access, could not create object for " + drvName);
			return;
		} catch (InstantiationException instex) {
			logger.log(Level.SEVERE, "Could not instantiate object for " + drvName);
			return;
		}
		dev.setDeviceName(drvName);
		// set driver name
		drv.setDriverName(drvName);
		drv.setDriverTypeId(drvTypeId);
		// set device for this driver
		drv.setDevice(dev);
		// add driver to available driver list
		this.addDriver(drvId, drv);
	}

	/***************************************************************************
	 * 
	 * end region add functions
	 * 
	 **************************************************************************/
	/***************************************************************************
	 * 
	 * region getter functions
	 * 
	 **************************************************************************/
	/**
	 * Get the scheduler for doWork function call.
	 * 
	 * @return Scheduler object.
	 */
	public CometScheduler getScheduler() {
		if (this.scheduler == null) {
			this.scheduler = new CometScheduler();
		}
		return this.scheduler;
	}

	/**
	 * @return the s_drivers
	 */
	public Map<Long, ComDRV> getDrivers() {
		return this.drivers;
	}

	public List<ComDRV> getOrderedDrivers() {
		return this.orderedDrivers;
	}

	public void setOrderedDrivers(List<ComDRV> drivers) {
		this.orderedDrivers = drivers;
	}

	/**
	 * get the desired driver from list, if it could not be found, return null
	 * 
	 * @param driverName name of required driver
	 * @return return the right driver or null if it couldn't be found
	 */
	public ComDRV getDriver(long driverId) {
		return this.drivers.get(driverId);
	}

	/**
	 * @return the s_cometConnection
	 */
	public IComDriverConnection getDriverConnection() {
		return this.cometConnection;
	}

	/***************************************************************************
	 * 
	 * end region getter functions
	 * 
	 **************************************************************************/
	/***************************************************************************
	 * 
	 * region setter functions
	 * 
	 **************************************************************************/
	/**
	 * Set the scheduler for doWork function call.
	 * 
	 * @param scheduler Scheduler object to set.
	 */
	public void setSceduler(CometScheduler scheduler) {
		this.scheduler = scheduler;
	}

	/**
	 * set a list of drivers
	 * 
	 * @param drivers the driver list to set
	 */
	public void setDrivers(Map<Long, ComDRV> drivers) {
		this.drivers = drivers;
	}

	/**
	 * sets the connection interface for this driver to communication with the
	 * device
	 * 
	 * @param connection connection interface to device
	 */
	public void setDriverConnection(IComDriverConnection connection) {
		this.cometConnection = connection;
	}

	/***************************************************************************
	 * 
	 * end region setter functions
	 * 
	 **************************************************************************/
	/***************************************************************************
	 * 
	 * region remove functions
	 * 
	 **************************************************************************/
	/**
	 * remove driver from list by name
	 * 
	 * @param driverName name of driver to remove
	 * @return driver object to remove, if not found return null
	 */
	public ComDRV removeDriver(long drvId) {
		return this.drivers.remove(drvId);
	}

	/***************************************************************************
	 * 
	 * end region remove functions
	 * 
	 **************************************************************************/
	public String getWorkingDir() {
		return workingDir;
	}

	public void setWorkingDir(String workingDir) {
		if (!workingDir.endsWith(File.separator)) {
			workingDir += File.separator;
		}
		this.workingDir = workingDir;
	}

	public String getRuntimeDir() {
		return runtimeDir;
	}

	public void setRuntimeDir(String runtimeDir) {
		if (!runtimeDir.endsWith(File.separator)) {
			runtimeDir += File.separator;
		}
		this.runtimeDir = runtimeDir;
	}

	public String getRootDir() {
		return rootDir;
	}

	public void setRootDir(String rootDir) {
		if (!rootDir.endsWith(File.separator)) {
			rootDir += File.separator;
		}
		this.rootDir = rootDir;
	}
}
