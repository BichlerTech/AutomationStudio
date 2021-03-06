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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.QualifiedName;
import org.opcfoundation.ua.builtintypes.StatusCode;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.AddNodesItem;
import org.opcfoundation.ua.core.AddReferencesItem;
import org.opcfoundation.ua.core.Attributes;
import org.opcfoundation.ua.core.BrowseDescription;
import org.opcfoundation.ua.core.BrowseDirection;
import org.opcfoundation.ua.core.BrowsePath;
import org.opcfoundation.ua.core.BrowsePathResult;
import org.opcfoundation.ua.core.BrowseResult;
import org.opcfoundation.ua.core.BrowseResultMask;
import org.opcfoundation.ua.core.CallMethodResult;
import org.opcfoundation.ua.core.EventFilter;
import org.opcfoundation.ua.core.HistoryReadDetails;
import org.opcfoundation.ua.core.HistoryReadResult;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.MonitoredItemCreateRequest;
import org.opcfoundation.ua.core.MonitoredItemModifyRequest;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.core.ReadEventDetails;
import org.opcfoundation.ua.core.RelativePath;
import org.opcfoundation.ua.core.RelativePathElement;
import org.opcfoundation.ua.core.StatusCodes;
import org.opcfoundation.ua.core.TimestampsToReturn;
import org.opcfoundation.ua.core.ViewDescription;

import opc.sdk.core.enums.ValueRanks;
import opc.sdk.core.node.Node;
import opc.sdk.core.types.TypeInfo;
import opc.sdk.core.utils.Utils;
import opc.sdk.ua.IOPCOperation;
import opc.sdk.ua.classes.BaseEventType;

/**
 * first initialize all communications and data points, then create all
 * notifications at last starts the schedule thread
 * 
 * @version v1.0.0.1
 * @author hannes
 * @since 30.09.2010
 */
public class ComDRVManager {
	public static final String VERSION = "v.1.0.1";
	public static final byte READNOTINIT = 0;
	public static final byte SYNCREAD = 1;
	public static final byte ASYNCREAD = 2;
	public static final byte NOREAD = 3;
	public static final byte WRITENOTINIT = 0;
	public static final byte SYNCWRITE = 1;
	public static final byte ASYNCWRITE = 2;
	public static final byte NOWRITE = 3;
	public static final byte HISTORYREADNOTINIT = 0;
	public static final byte HISTORYSYNCREAD = 1;
	public static final byte HISTORYASYNCREAD = 2;
	public static final byte HISTORYNOREAD = 3;
	protected ComResourceManager manager = null;
	protected static ComDRVManager drvManager = null;
	private Map<Long, ILoginListener> loginListeners = null;
	private Map<Long, IReadListener> readListeners = null;
	private Map<Long, IWriteListener> writeListeners = null;
	private Map<Long, IHistoryReadListener> historyReadListeners = null;
	private List<IMonitoringListener> monitoringListeners = null;
	private List<IMethodListener> methodListeners = null;
	private Map<NodeId, IMethodListener> methodListenersById = null;
	private Map<NodeId, Map<NodeId, IMethodListener>> methodListenersByIdId = null; // first id is object id, second is
																					// method id
	private List<IViewListener> viewListeners = null;
	private IOPCInternalServer server;
	private List<String> versions = new ArrayList<>();
	private List<String> histories = new ArrayList<>();
	/**
	 * added 24.11.2015 HB get driver satus flags from driver.com file
	 */
	private NodeId currentTime = Identifiers.Server_ServerStatus_CurrentTime;
	private NodeId secondsTillShutdown = Identifiers.Server_ServerStatus_SecondsTillShutdown;
	private NodeId serverState = Identifiers.Server_ServerStatus_State;
	private NodeId startTime = Identifiers.Server_ServerStatus_StartTime;
	private NodeId shutdownReason = Identifiers.Server_ServerStatus_ShutdownReason;
	private NodeId productUri = Identifiers.Server_ServerStatus_BuildInfo_ProductUri;
	private NodeId softwareVersion = Identifiers.Server_ServerStatus_BuildInfo_SoftwareVersion;
	private NodeId manufacturerName = Identifiers.Server_ServerStatus_BuildInfo_ManufacturerName;
	private NodeId buildInfo = Identifiers.Server_ServerStatus_BuildInfo;
	private NodeId buildDate = Identifiers.Server_ServerStatus_BuildInfo_BuildDate;
	private NodeId productName = Identifiers.Server_ServerStatus_BuildInfo_ProductName;
	private NodeId buildNumber = Identifiers.Server_ServerStatus_BuildInfo_BuildNumber;
	private boolean drvStatusFlag = false;
	private Logger logger = Logger.getLogger(getClass().getName());
	private ComDRV baseDriver;

	/***************************************************************************
	 * 
	 * region default constructor
	 * 
	 **************************************************************************/
	/**
	 * default constructor, with no effect
	 */
	protected ComDRVManager() {
		this.manager = new ComResourceManager();
		this.readListeners = new HashMap<>();
		this.loginListeners = new HashMap<>();
		this.writeListeners = new HashMap<>();
		this.monitoringListeners = new ArrayList<>();
		this.methodListeners = new ArrayList<>();
		this.methodListenersById = new HashMap<>();
		this.methodListenersByIdId = new HashMap<NodeId, Map<NodeId, IMethodListener>>();
		this.viewListeners = new ArrayList<>();
		/** read debug info if available */
		this.readDebug();
	}

	public IOPCInternalServer getServer() {
		return server;
	}

	public void setServer(IOPCInternalServer server) {
		this.server = server;
	}

	public NodeId getCurrentTime() {
		return currentTime;
	}

	public void setCurrentTime(NodeId currentTime) {
		this.currentTime = currentTime;
	}

	public NodeId getSecondsTillShutdown() {
		return secondsTillShutdown;
	}

	public void setSecondsTillShutdown(NodeId secondsTillShutdown) {
		this.secondsTillShutdown = secondsTillShutdown;
	}

	public NodeId getServerState() {
		return serverState;
	}

	public void setServerState(NodeId serverState) {
		this.serverState = serverState;
	}

	public NodeId getStartTime() {
		return startTime;
	}

	public void setStartTime(NodeId startTime) {
		this.startTime = startTime;
	}

	public NodeId getShutdownReason() {
		return shutdownReason;
	}

	public void setShutdownReason(NodeId shutdownReason) {
		this.shutdownReason = shutdownReason;
	}

	public NodeId getProductUri() {
		return productUri;
	}

	public void setProductUri(NodeId productUri) {
		this.productUri = productUri;
	}

	public NodeId getSoftwareVersion() {
		return softwareVersion;
	}

	public void setSoftwareVersion(NodeId softwareVersion) {
		this.softwareVersion = softwareVersion;
	}

	public NodeId getManufacturerName() {
		return manufacturerName;
	}

	public void setManufacturerName(NodeId manufacturerName) {
		this.manufacturerName = manufacturerName;
	}

	public NodeId getBuildDate() {
		return buildDate;
	}

	public void setBuildDate(NodeId buildDate) {
		this.buildDate = buildDate;
	}

	public NodeId getProductName() {
		return productName;
	}

	public void setProductName(NodeId productName) {
		this.productName = productName;
	}

	public NodeId getBuildNumber() {
		return buildNumber;
	}

	public void setBuildNumber(NodeId buildNumber) {
		this.buildNumber = buildNumber;
	}

	public boolean isDrvStatusFlag() {
		return drvStatusFlag;
	}

	public void setDrvStatusFlag(boolean drvStatusFlag) {
		this.drvStatusFlag = drvStatusFlag;
	}

	public NodeId getBuildInfo() {
		return buildInfo;
	}

	public void setBuildInfo(NodeId buildInfo) {
		this.buildInfo = buildInfo;
	}

	/***************************************************************************
	 * 
	 * region default constructor
	 * 
	 **************************************************************************/
	/**
	 * this function should be used to set an customer defined drivermanager. If you
	 * doesn't set one, the internal will be used, that will take all defined
	 * drivers from the comet studio internal system.
	 * 
	 * @param manager
	 */
	public static void setDRVManager(ComDRVManager manager) {
		drvManager = manager;
	}

	/**
	 * 
	 * @return
	 */
	public static ComDRVManager getDRVManager() {
		if (drvManager == null) {
			drvManager = new ComDRVManager();
		}
		return drvManager;
	}

	/**
	 * set the connection object to communicate with the ua server this connection
	 * must be set first of all
	 * 
	 * @param connection connection object to communicate with an external program
	 */
	public void connect(IComDriverConnection connection) {
		this.manager.setDriverConnection(connection);
	}

	public void createSession(IOPCServerSession serverSession) throws ServiceResultException {
		// do nothing at the moment, make an user implementation
	}

	/***************************************************************************
	 * 
	 * region license operation
	 * 
	 **************************************************************************/
	/**
	 * Check the license to use the general driver framework.
	 * 
	 * @return return true if license is OK otherwise false.
	 */
	public boolean checkLicense() {
		if (this.manager.isActivatedebug()
				&& (this.manager.getDebug() & this.manager.DEBUG_STARTUP) == this.manager.DEBUG_STARTUP) {
			logger.info("Check license for communication drivers!");
		}
		// open license file to check registered key
		File license = new File("lic.dat");
		// first check if file exists
		if (!license.exists()) {
			logger.log(Level.SEVERE, "No license for communication drivers found! ");
			return false;
		}
		try (FileReader reader = new FileReader(license); BufferedReader breader = new BufferedReader(reader);) {
			// now check file content
			// now read license string from file
			String lic = breader.readLine();
			// check license string
			if (lic.compareTo("lic ok!") == 0) {
				return true;
			}
		} catch (FileNotFoundException fnfe) {
			logger.log(Level.SEVERE, "No license for communication drivers found! ");
			return false;
		} catch (IOException ioe) {
			if (this.manager.isActivatedebug()
					&& (this.manager.getDebug() & this.manager.DEBUG_STARTUP) == this.manager.DEBUG_STARTUP) {
				logger.info("Could not read licence file! ");
			}
			return false;
		}
		return false;
	}

	public StatusCode[] removeNodes(NodeId[] nodeid, boolean deleteTargetReferences) {
		if (this.manager.getDriverConnection() != null) {
			return this.manager.getDriverConnection().removeNodes(nodeid, deleteTargetReferences);
		}
		return new StatusCode[] { new StatusCode(StatusCodes.Bad_NodeIdInvalid) };
	}

	/**
	 * Prepares a node for a write request. We set flags to the node if it should be
	 * written or not.
	 * 
	 * @param userName username to verify.
	 * @return true if the nodeid was found and could be set otherwise false.
	 */
	public boolean loginUser(String userName, String passwd, long senderState) {
		boolean result = false;
		/**
		 * do we have a write handler registered ?
		 */
		if (this.loginListeners != null) {
			ILoginListener listener = null;
			for (Entry<Long, ILoginListener> entry : this.loginListeners.entrySet()) {
				if (entry.getKey() != senderState) {
					listener = entry.getValue();
					if (listener != null) {
						result = listener.loginUser(userName, passwd);
					}
				}
			}
		}
		return result;
	}

	/**
	 * Check the license to use a special communication driver.
	 * 
	 * @param drvId Id of driver to check the licence for.
	 * @return return true if license is OK otherwise false
	 */
	public boolean checkLicense(int drvId) {
		return true;
	}

	/***************************************************************************
	 * 
	 * region license operation
	 * 
	 **************************************************************************/
	/**
	 * Load all drivers inclusive its data points into driver framework. The driver
	 * configuration is in UA address space and starts with the node "DRIVERS".
	 * 
	 * @param driversNodeId The start nodeid of drivers configuration.
	 */
	public StatusCode loadDrivers() {
		String line = "";
		// load driver config file if exists
		File conffile = new File(ComResourceManager.CONFIGPATH);
		ArrayList<String> driverlist = new ArrayList<>();
		if (conffile.exists()) {
			try (FileReader freader = new FileReader(conffile); BufferedReader reader = new BufferedReader(freader);) {
				while ((line = reader.readLine()) != null) {
					if (line.startsWith("##")) {
						continue;
					}
					if ("#drvname".compareTo(line) == 0) {
						line = reader.readLine();
						String[] drivers = line.split(";");
						driverlist.addAll(Arrays.asList(drivers));
					}
				}
			} catch (IOException e) {
				logger.log(Level.SEVERE, e.getMessage());
			}
			driverlist.add("events");
		}
		// it is required that we load driver list in the correct order
		if (!driverlist.isEmpty()) {
		}
		// load all drivers from comet studio configuration
		String studioServers = this.manager.getRootDir() + this.manager.getWorkingDir() + "drivers/";
		String studioRuntime = this.manager.getRootDir() + this.manager.getRuntimeDir() + "drivers/";
		File driverspath = new File(studioServers);
		if (driverspath.exists() && driverspath.isDirectory()) {
			File[] drvs = driverspath.listFiles();
			String drvtype = "";
			String drvversion = "";
			ClassLoader currentThreadClassLoader = Thread.currentThread().getContextClassLoader();
			URLClassLoader loader = null;
			String config = "";
			String drvLibPath = "";
			File drvLibDir = null;
			File[] jars = null;
			Class<?> cls = null;
			long driverId = 0;
			for (File drv : drvs) {
				if (!driverlist.isEmpty() && !driverlist.contains(drv.getName())) {
					// we found a not configured driver
					continue;
				}
				if (drv.isDirectory() && drv.getName().compareTo(".") != 0 && drv.getName().compareTo("..") != 0) {
					config = drv.getAbsolutePath() + "/driver.com";
					driverId = 0;
					String deviceAddr = "";
					String drvNameSpace = "";
					boolean needSchedule = false;
					long scheduleNano = 1000000;
					String drvProperties = "";
					boolean redundante = false;
					String reconnectTimeout = "";
					String currenttime = "";
					String secondstillshutdown = "";
					String state = "";
					String starttime = "";
					String shutdownreason = "";
					String buildinfo = "";
					String buildDate = "";
					String pructUri = "";
					String pruductName = "";
					String softwareVersion = "";
					String manufName = "";
					String buildnr = "";
					try (FileReader freader = new FileReader(config);
							BufferedReader reader = new BufferedReader(freader);) {
						while ((line = reader.readLine()) != null) {
							if (line.startsWith("#") || line.isEmpty()) {
								continue;
							} else if (line.compareTo("drivertype") == 0) {
								// driver type definition found
								drvtype = reader.readLine();
							} else if (line.compareTo("driverversion") == 0) {
								// driver version definition found
								drvversion = reader.readLine();
							} else if (line.compareTo("driverid") == 0) {
								driverId = Long.parseLong(reader.readLine());
							} else if (line.compareTo("deviceaddress") == 0) {
								deviceAddr = reader.readLine();
							} else if (line.compareTo("drvns") == 0) {
								drvNameSpace = reader.readLine();
							} else if (line.compareTo("scancyclic") == 0) {
								String[] split = reader.readLine().split(";");
								if (split.length > 0) {
									needSchedule = Boolean.parseBoolean(split[0]);
								}
								if (split.length > 1) {
									try {
										scheduleNano = Long.parseLong(split[1]) * 1000000;
									} catch (NumberFormatException ex) {
										logger.log(Level.SEVERE, ex.getMessage());
									}
								}
							} else if (line.compareTo("drvproperties") == 0) {
								drvProperties = reader.readLine();
							} else if (line.compareTo("reconnecttimeout") == 0) {
								reconnectTimeout = reader.readLine();
							} else if (line.compareTo("redundancy") == 0) {
								String red = reader.readLine();
								if (red.compareTo("yes") == 0) {
									redundante = true;
								} else {
									redundante = false;
								}
							} else if (line.compareTo("drvstatusflag") == 0) {
								this.setDrvStatusFlag(Boolean.parseBoolean(reader.readLine()));
							} else if (line.compareTo("driverstatus") == 0) {
								try {
									if (this.isDrvStatusFlag()) {
										// read general driver node, consume on line
										String firstline = reader.readLine();
										for (int i = 0; i < 12; i++) {
											String cont = reader.readLine();
											String property = cont.split("=")[0];
											cont = cont.substring(cont.indexOf('=') + 1);
											switch (property) {
											case "CurrentTime":
												currenttime = cont;
												//
												// this.setCurrentTime(NodeId.parseNodeId(
												// "ns=" + ((ExtendedComDRV)
												// drvImpl).getNamespaceTable().getIndex(cont.split(";")[0])
												// + ";"
												// + cont.split(";")[1])); // CurrentTime
												break;
											case "SecondsTillShutdown":
												secondstillshutdown = cont;
												// this.setSecondsTillShutdown(NodeId.parseNodeId(
												// "ns=" + ((ExtendedComDRV)
												// drvImpl).getNamespaceTable().getIndex(cont.split(";")[0])
												// + ";"
												// + cont.split(";")[1])); // SecondsTillShutdown
												break;
											case "State":
												state = cont;
												// this.setServerState(NodeId.parseNodeId(
												// "ns=" + ((ExtendedComDRV)
												// drvImpl).getNamespaceTable().getIndex(cont.split(";")[0])
												// + ";"
												// + cont.split(";")[1])); // State
												break;
											case "StartTime":
												starttime = cont;
												// this.setStartTime(NodeId.parseNodeId(
												// "ns=" + ((ExtendedComDRV)
												// drvImpl).getNamespaceTable().getIndex(cont.split(";")[0])
												// + ";"
												// + cont.split(";")[1])); // StartTime
												break;
											case "ShutdownReason":
												shutdownreason = cont;
												// this.setShutdownReason(NodeId.parseNodeId(
												// "ns=" + ((ExtendedComDRV)
												// drvImpl).getNamespaceTable().getIndex(cont.split(";")[0])
												// + ";"
												// + cont.split(";")[1])); // ShutdownReason
												break;
											case "BuildInfo":
												buildinfo = cont;
												// this.setBuildInfo(NodeId.parseNodeId(
												// "ns=" + ((ExtendedComDRV)
												// drvImpl).getNamespaceTable().getIndex(cont.split(";")[0])
												// + ";"
												// + cont.split(";")[1])); // BuildInfo
												break;
											case "BuildDate":
												buildDate = cont;
												// this.setBuildDate(NodeId.parseNodeId(
												// "ns=" + ((ExtendedComDRV)
												// drvImpl).getNamespaceTable().getIndex(cont.split(";")[0])
												// + ";"
												// + cont.split(";")[1])); // BuildInfo
												break;
											case "ProductUri":
												pructUri = cont;
												// this.setProductName(NodeId.parseNodeId(
												// "ns=" + ((ExtendedComDRV)
												// drvImpl).getNamespaceTable().getIndex(cont.split(";")[0])
												// + ";"
												// + cont.split(";")[1])); // BuildInfo
												break;
											case "ProductName":
												pruductName = cont;
												// this.setProductName(NodeId.parseNodeId(
												// "ns=" + ((ExtendedComDRV)
												// drvImpl).getNamespaceTable().getIndex(cont.split(";")[0])
												// + ";"
												// + cont.split(";")[1])); // ProductUri
												break;
											case "SoftwareVersion":
												softwareVersion = cont;
												// this.setSoftwareVersion(NodeId.parseNodeId(
												// "ns=" + ((ExtendedComDRV)
												// drvImpl).getNamespaceTable().getIndex(cont.split(";")[0])
												// + ";"
												// + cont.split(";")[1])); // SoftwareVersion
												break;
											case "ManufacturerName":
												manufName = cont;
												// this.setManufacturerName(NodeId.parseNodeId(
												// "ns=" + ((ExtendedComDRV)
												// drvImpl).getNamespaceTable().getIndex(cont.split(";")[0])
												// + ";"
												// + cont.split(";")[1])); // ManufacturerName
												break;
											case "BuildNumber":
												buildnr = cont;
												// this.setBuildNumber(NodeId.parseNodeId(
												// "ns=" + ((ExtendedComDRV)
												// drvImpl).getNamespaceTable().getIndex(cont.split(";")[0])
												// + ";"
												// + cont.split(";")[1])); // BuildNumber
												break;
											default:
												break;
											}
										}
									}
								} catch (IllegalArgumentException ex) {
									logger.log(Level.SEVERE,
											"Driver status node could not be parsed from driver.com config name: "
													+ drv.getName());
								}
							}
						}
					} catch (FileNotFoundException e) {
						logger.log(Level.SEVERE,
								"File driver.com couldn't be found, so we can not init driver: " + drv.getName());
					} catch (IOException e) {
						logger.log(Level.SEVERE, e.getMessage());
					}
					// now try to load driver bundle
					drvLibPath = studioRuntime + drvtype + "/" + drvversion + "/lib";
					drvLibDir = new File(drvLibPath);
					if (drvLibDir.exists()) {
						jars = drvLibDir.listFiles();
						for (File jar : jars) {
							if (jar.getName().endsWith(".jar")) {
								// load only jar file into system.
								try {
									// class binary object
									loader = new URLClassLoader(new URL[] { jar.toURI().toURL() },
											currentThreadClassLoader);
									Thread.currentThread().setContextClassLoader(loader);
									cls = loader.loadClass(jar.getName().replace(".jar", "") + ".ComDRV");
									// add jars to classpath
									// System.setProperty("java.class.path",
									// System.getProperty("java.class.path") +
									// ";C:\\BTech\\Programmierung\\java\\automation_studio\\com.bichler.driver.observer\\lib\\gson-2.8.2.jar");
									// String classpath = System.getProperty("java.class.path");
									// String []elements = classpath.split(";");
									// System.out.println(classpath);
									// addToClasspath(loader,
									// "C:\\BTech\\Programmierung\\java\\automation_studio\\com.bichler.driver.observer\\lib\\gson-2.8.2.jar");
									ComDRV drvImpl = (ComDRV) cls.newInstance();
									drvImpl.setDriverName(drv.getName());
									drvImpl.setDriverId(driverId);
									this.manager.addDriver(driverId, drvImpl);
									this.manager.getOrderedDrivers().add(drvImpl);
									/** set all properties now */
									drvImpl.setDeviceAddress(deviceAddr);
									drvImpl.setDrvNameSpace(drvNameSpace);
									drvImpl.setNeedSchedule(needSchedule);
									drvImpl.setScheduleNano(scheduleNano);
									drvImpl.setDrvProperties(drvProperties);
									drvImpl.setRedundant(redundante);
									drvImpl.setDrvReconnectTimeout(reconnectTimeout);
									if (this.isDrvStatusFlag()) {
										((ExtendedComDRV) drvImpl).renewNamespaceTable();
										if (!currenttime.isEmpty()) {
											String[] content = currenttime.split(";");
											if (content != null && content.length == 2) {
												this.setCurrentTime(NodeId.parseNodeId(
														"ns=" + ((ExtendedComDRV) drvImpl).getNamespaceTable()
																.getIndex(content[0]) + ";" + content[1])); // CurrentTime
											}
										}
										if (!secondstillshutdown.isEmpty()) {
											String[] content = secondstillshutdown.split(";");
											if (content != null && content.length == 2) {
												this.setSecondsTillShutdown(NodeId.parseNodeId(
														"ns=" + ((ExtendedComDRV) drvImpl).getNamespaceTable()
																.getIndex(content[0]) + ";" + content[1])); // SecondsTillShutdown
											}
										}
										if (!state.isEmpty()) {
											String[] content = state.split(";");
											if (content != null && content.length == 2) {
												this.setServerState(NodeId.parseNodeId(
														"ns=" + ((ExtendedComDRV) drvImpl).getNamespaceTable()
																.getIndex(content[0]) + ";" + content[1])); // State
											}
										}
										if (!starttime.isEmpty()) {
											String[] content = starttime.split(";");
											if (content != null && content.length == 2) {
												this.setStartTime(NodeId.parseNodeId("ns=" + ((ExtendedComDRV) drvImpl)
														.getNamespaceTable().getIndex(content[0]) + ";" + content[1])); // StartTime
											}
										}
										if (!shutdownreason.isEmpty()) {
											String[] content = shutdownreason.split(";");
											if (content != null && content.length == 2) {
												this.setShutdownReason(NodeId.parseNodeId(
														"ns=" + ((ExtendedComDRV) drvImpl).getNamespaceTable()
																.getIndex(content[0]) + ";" + content[1])); // ShutdownReason
											}
										}
										if (!buildinfo.isEmpty()) {
											String[] content = buildinfo.split(";");
											if (content != null && content.length == 2) {
												this.setBuildInfo(NodeId.parseNodeId("ns=" + ((ExtendedComDRV) drvImpl)
														.getNamespaceTable().getIndex(content[0]) + ";" + content[1])); // BuildInfo
											}
										}
										if (!buildDate.isEmpty()) {
											String[] content = buildDate.split(";");
											if (content != null && content.length == 2) {
												this.setBuildDate(NodeId.parseNodeId("ns=" + ((ExtendedComDRV) drvImpl)
														.getNamespaceTable().getIndex(content[0]) + ";" + content[1])); // BuildInfo
											}
										}
										if (!pructUri.isEmpty()) {
											String[] content = pructUri.split(";");
											if (content != null && content.length == 2) {
												this.setProductName(NodeId.parseNodeId(
														"ns=" + ((ExtendedComDRV) drvImpl).getNamespaceTable()
																.getIndex(content[0]) + ";" + content[1])); // BuildInfo
											}
										}
										if (!pruductName.isEmpty()) {
											String[] content = pruductName.split(";");
											if (content != null && content.length == 2) {
												this.setProductName(NodeId.parseNodeId(
														"ns=" + ((ExtendedComDRV) drvImpl).getNamespaceTable()
																.getIndex(content[0]) + ";" + content[1])); // ProductUri
											}
										}
										if (!softwareVersion.isEmpty()) {
											String[] content = softwareVersion.split(";");
											if (content != null && content.length == 2) {
												this.setSoftwareVersion(NodeId.parseNodeId(
														"ns=" + ((ExtendedComDRV) drvImpl).getNamespaceTable()
																.getIndex(content[0]) + ";" + content[1])); // SoftwareVersion
											}
										}
										if (!manufName.isEmpty()) {
											String[] content = manufName.split(";");
											if (content != null && content.length == 2) {
												this.setManufacturerName(NodeId.parseNodeId(
														"ns=" + ((ExtendedComDRV) drvImpl).getNamespaceTable()
																.getIndex(content[0]) + ";" + content[1])); // ManufacturerName
											}
										}
										if (!buildnr.isEmpty()) {
											String[] content = buildnr.split(";");
											if (content != null && content.length == 2) {
												this.setBuildNumber(NodeId.parseNodeId(
														"ns=" + ((ExtendedComDRV) drvImpl).getNamespaceTable()
																.getIndex(content[0]) + ";" + content[1])); // BuildNumber
											}
										}
									}
								} catch (ClassNotFoundException | IllegalAccessException | InstantiationException
										| MalformedURLException ex) {
									logger.log(Level.SEVERE, ex.getMessage());
								}
							}
						}
					} else {
						logger.log(Level.SEVERE, "Can not start driver {0}, no runtime version {1} exists!",
								new String[] { drvtype, drvversion });
					}
				}
			}
			// this.baseDriver = new com.bichler.driver.opc.base.ComDRV();
			// baseDriver.setDriverName("OPC UA Basedriver");
			// baseDriver.setDriverId(this.manager.getDrivers().size());
			// baseDriver.initialize();
			for (ComDRV drv : this.manager.getOrderedDrivers()) {
				// call the initialize function of each driver
				drv.initialize();
			}
		}
		return StatusCode.GOOD;
	}

	class MyClassLoader extends URLClassLoader {
		/**
		 * @param urls, to carryforward the existing classpath.
		 */
		public MyClassLoader(URL[] urls) {
			super(urls);
		}

		@Override
		/**
		 * add ckasspath to the loader.
		 */
		public void addURL(URL url) {
			super.addURL(url);
		}
	}

	/**
	 * Hannes Bichler 05.01.2018 new function to read version mapping from config
	 * 
	 */
	public void initializeVersion() {
		File conffile = new File(ComResourceManager.CONFIGPATH);
		if (!conffile.exists())
			return;
		try (FileReader freader = new FileReader(conffile); BufferedReader reader = new BufferedReader(freader);) {
			String line = "";
			while ((line = reader.readLine()) != null) {
				if (line.startsWith("##")) {
					continue;
				}
				if ("#version".compareTo(line) == 0) {
					this.readVersion(reader);
				}
			}
		} catch (IOException e) {
			logger.log(Level.SEVERE, e.getMessage());
		}
	}

	private void readVersion(BufferedReader reader) throws IOException {
		String line = "";
		while ((line = reader.readLine()) != null) {
			if (line.isEmpty())
				return;
			this.versions.add(line);
		}
	}

	/**
	 * translates a browsepath as string into a nodeid if valid
	 * 
	 * @param server
	 * @param startNode
	 * @param browseindex
	 * @param path
	 * @return
	 */
	public static NodeId translatBrowsePath2NodeId(IOPCInternalServer server, NodeId startNode, int browseindex,
			String path) {
		NodeId id = null;
		try {
			List<RelativePathElement> elements = new ArrayList<>();
			String[] items = path.split("/");
			for (String item : items) {
				String[] britems = item.split(":");
				if (britems != null && britems.length >= 1) {
					if (britems.length == 2) {
						try {
							item = britems[1];
							browseindex = Integer.parseInt(britems[0]);
						} catch (Exception ex) {
							Logger.getLogger(ComDRVManager.class.getName()).log(Level.SEVERE, null, ex);
						}
					} else {
						item = britems[0];
					}
					RelativePathElement element = new RelativePathElement();
					element.setTargetName(new QualifiedName(browseindex, item));
					element.setIsInverse(false);
					element.setIncludeSubtypes(true);
					element.setReferenceTypeId(Identifiers.HierarchicalReferences);
					elements.add(element);
				}
			}
			BrowsePath browse = new BrowsePath();
			browse.setStartingNode(startNode);
			browse.setRelativePath(new RelativePath(elements.toArray(new RelativePathElement[0])));
			BrowsePathResult[] result = server.getMaster().translateBrowsePathsToNodeIds(new BrowsePath[] { browse },
					null);
			if (result != null && result.length > 0 && result[0].getStatusCode() == StatusCode.GOOD) {
				return server.getNamespaceUris().toNodeId(result[0].getTargets()[0].getTargetId());
			}
		} catch (ServiceResultException e) {
			Logger.getLogger(ComDRVManager.class.getName()).log(Level.SEVERE, null, e);
		}
		return id;
	}

	/**
	 * translates a nodeid to the browsepath, starting from own browsename to object
	 * folder or to nodeid of endnode
	 * 
	 * @param server
	 * @param nidToTranslate nodeid to find the browsepath for
	 * @param endnode        nodeid of end node or null. If null, browsepath will
	 *                       end at objectsfolder.
	 * @return
	 */
	public List<RelativePathElement> translateNodeIdToBrowsepath(IOPCInternalServer server, NodeId nidToTranslate,
			NodeId endnode) {
		List<RelativePathElement> path = new ArrayList<RelativePathElement>();
		RelativePathElement element;
		BrowseDescription browse = new BrowseDescription();
		browse.setBrowseDirection(BrowseDirection.Inverse);
		browse.setIncludeSubtypes(true);
		browse.setNodeClassMask(NodeClass.ALL);
		browse.setNodeId(nidToTranslate);
		browse.setResultMask(BrowseResultMask.BrowseName);
		browse.setReferenceTypeId(Identifiers.HierarchicalReferences);
		// first get browsename of node to translate
		Node translate = server.getAddressSpaceManager().getNodeById(nidToTranslate);
		if (translate == null)
			return path;
		element = new RelativePathElement();
		element.setIsInverse(true);
		element.setReferenceTypeId(Identifiers.HierarchicalReferences);
		element.setTargetName(translate.getBrowseName());
		path.add(element);
		try {
			boolean iterate = true;
			while (iterate) {
				BrowseResult[] result = server.getMaster().browse(new BrowseDescription[] { browse },
						new UnsignedInteger(1), null, null);
				if (result == null || result.length == 0 || result[0].getReferences() == null
						|| result[0].getReferences().length == 0) {
					return path;
				}
				element = new RelativePathElement();
				element.setIsInverse(true);
				element.setReferenceTypeId(result[0].getReferences()[0].getReferenceTypeId());
				element.setTargetName(result[0].getReferences()[0].getBrowseName());
				path.add(element);
				NodeId target = server.getNamespaceUris().toNodeId(result[0].getReferences()[0].getNodeId());
				if (target.compareTo(nidToTranslate) == 0 || target.compareTo(Identifiers.ObjectsFolder) == 0)
					return path;
				browse.setNodeId(target);
			}
		} catch (IllegalArgumentException | ServiceResultException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
		return path;
	}

	public void fillVersions() {
		for (String version : versions) {
			String[] vv = version.split("\\|\\|");
			if (vv != null && vv.length >= 2) {
				File versionFile = new File(vv[0]);
				if (versionFile.exists()) {
					NodeId nid = null;
					try {
						nid = NodeId.parseNodeId(vv[1]);
					} catch (IllegalArgumentException ex) {
						// do nothing here we try to get nodeid from namespaceindex
					}
					/**
					 * try to translate nodeid from namespace + id
					 */
					if (nid == null) {
						String[] namespaceid = vv[1].split(";");
						if (namespaceid != null && namespaceid.length == 2) {
							int nsindex = server.getNamespaceUris().getIndex(namespaceid[0]);
							if (nsindex >= 0) {
								try {
									nid = NodeId.parseNodeId("ns=" + nsindex + ";" + namespaceid[1]);
								} catch (IllegalArgumentException ex) {
									// do nothing, because....
								}
							}
						}
					}
					if (nid == null) {
						// now try to translate browsepath to nodeid
						nid = translatBrowsePath2NodeId(ComDRVManager.getDRVManager().getServer(),
								Identifiers.ObjectsFolder, 1, vv[1]);
					}
					if (nid != null) {
						try (FileReader freader = new FileReader(versionFile);
								BufferedReader reader = new BufferedReader(freader);) {
							String line = "";
							List<String> buffer = new ArrayList<>();
							while ((line = reader.readLine()) != null) {
								buffer.add(line);
							}
							if (buffer.size() > 1) {
								drvManager.writeFromDriver(new NodeId[] { nid },
										new UnsignedInteger[] { Attributes.ValueRank }, new String[] { null },
										new DataValue[] {
												new DataValue(new Variant(ValueRanks.OneDimension.getValue())) },
										new Long[] { (long) -1 });
								drvManager.writeFromDriver(new NodeId[] { nid },
										new UnsignedInteger[] { Attributes.ArrayDimensions }, new String[] { null },
										new DataValue[] { new DataValue(new Variant(
												new UnsignedInteger[] { new UnsignedInteger(buffer.size()) })) },
										new Long[] { (long) -1 });
								String[] items = buffer.toArray(new String[buffer.size()]);
								drvManager.writeFromDriver(new NodeId[] { nid },
										new UnsignedInteger[] { Attributes.Value }, new String[] { null },
										new DataValue[] { new DataValue(new Variant(items)) },
										new Long[] { (long) -1 });
							} else if (buffer.size() == 1) {
								drvManager.writeFromDriver(new NodeId[] { nid },
										new UnsignedInteger[] { Attributes.ValueRank }, new String[] { null },
										new DataValue[] { new DataValue(new Variant(ValueRanks.Scalar.getValue())) },
										new Long[] { (long) -1 });
								drvManager.writeFromDriver(new NodeId[] { nid },
										new UnsignedInteger[] { Attributes.ArrayDimensions }, new String[] { null },
										new DataValue[] { new DataValue(
												new Variant(new UnsignedInteger[] { new UnsignedInteger(0) })) },
										new Long[] { (long) -1 });
								drvManager.writeFromDriver(new NodeId[] { nid },
										new UnsignedInteger[] { Attributes.Value }, new String[] { null },
										new DataValue[] { new DataValue(new Variant(buffer.get(0))) },
										new Long[] { (long) -1 });
							}
						} catch (IOException e) {
							logger.log(Level.SEVERE, e.getMessage());
						}
					}
				}
			}
		}
	}

	public void fillHistories() {
		for (String history : histories) {
			String[] vv = history.split("\\|\\|");
			if (vv != null && vv.length >= 2) {
				File versionFile = new File(vv[0]);
				if (versionFile.exists()) {
					NodeId nid = null;
					try {
						nid = NodeId.parseNodeId(vv[1]);
					} catch (IllegalArgumentException ex) {
						// do nothing here
					}
					/**
					 * try to translate nodeid from namespace + id
					 */
					if (nid == null) {
						String[] namespaceid = vv[1].split(";");
						if (namespaceid != null && namespaceid.length == 2) {
							int nsindex = server.getNamespaceUris().getIndex(namespaceid[0]);
							if (nsindex >= 0) {
								try {
									nid = NodeId.parseNodeId("ns=" + nsindex + ";" + namespaceid[1]);
								} catch (IllegalArgumentException ex) {
									// do nothing, because....
								}
							}
						}
					}
					if (nid == null) {
						// now try to translate browsepath to nodeid
						nid = translatBrowsePath2NodeId(ComDRVManager.getDRVManager().getServer(),
								Identifiers.ObjectsFolder, 1, vv[1]);
					}
					if (nid != null) {
						try (FileReader freader = new FileReader(versionFile);
								BufferedReader reader = new BufferedReader(freader);) {
							String line = "";
							List<String> buffer = new ArrayList<>();
							while ((line = reader.readLine()) != null) {
								buffer.add(line);
							}
							if (buffer.size() > 1) {
								drvManager.writeFromDriver(new NodeId[] { nid },
										new UnsignedInteger[] { Attributes.ValueRank }, new String[] { null },
										new DataValue[] {
												new DataValue(new Variant(ValueRanks.OneDimension.getValue())) },
										new Long[] { (long) -1 });
								drvManager.writeFromDriver(new NodeId[] { nid },
										new UnsignedInteger[] { Attributes.ArrayDimensions }, new String[] { null },
										new DataValue[] { new DataValue(new Variant(
												new UnsignedInteger[] { new UnsignedInteger(buffer.size()) })) },
										new Long[] { (long) -1 });
								String[] items = buffer.toArray(new String[buffer.size()]);
								drvManager.writeFromDriver(new NodeId[] { nid },
										new UnsignedInteger[] { Attributes.Value }, new String[] { null },
										new DataValue[] { new DataValue(new Variant(items)) },
										new Long[] { (long) -1 });
							} else if (buffer.size() == 1) {
								drvManager.writeFromDriver(new NodeId[] { nid },
										new UnsignedInteger[] { Attributes.ValueRank }, new String[] { null },
										new DataValue[] { new DataValue(new Variant(ValueRanks.Scalar.getValue())) },
										new Long[] { (long) -1 });
								drvManager.writeFromDriver(new NodeId[] { nid },
										new UnsignedInteger[] { Attributes.ArrayDimensions }, new String[] { null },
										new DataValue[] { new DataValue(
												new Variant(new UnsignedInteger[] { new UnsignedInteger(0) })) },
										new Long[] { (long) -1 });
								drvManager.writeFromDriver(new NodeId[] { nid },
										new UnsignedInteger[] { Attributes.Value }, new String[] { null },
										new DataValue[] { new DataValue(new Variant(buffer.get(0))) },
										new Long[] { (long) -1 });
							}
						} catch (IOException e) {
							logger.log(Level.SEVERE, e.getMessage(), e);
						}
					}
				}
			}
		}
	}

	/**
	 * Hannes Bichler 05.01.2018 new function to read history mapping from config
	 * 
	 */
	public void initializeHistory() {
		File conffile = new File(ComResourceManager.CONFIGPATH);
		if (conffile.exists()) {
			try (FileReader freader = new FileReader(conffile); BufferedReader reader = new BufferedReader(freader);) {
				String line = "";
				while ((line = reader.readLine()) != null) {
					if (line.startsWith("##")) {
						continue;
					}
					if ("#history".compareTo(line) == 0) {
						while ((line = reader.readLine()) != null) {
							if (line.isEmpty())
								return;
							this.histories.add(line);
						}
					}
				}
			} catch (IOException e) {
				logger.log(Level.SEVERE, e.getMessage(), e);
			}
		}
	}

	public void readDebug() {
		// read logg activation
		File file = new File("debug.conf");
		if (file.exists()) {
			try (BufferedReader reader = new BufferedReader(new FileReader(file));) {
				String db = reader.readLine();
				String[] elems = null;
				if (db != null) {
					elems = db.split(" ");
					if (elems != null && elems.length > 0) {
						this.getResourceManager().setActivatedebug(Boolean.parseBoolean(elems[0]));
					}
				}
				db = reader.readLine();
				if (db != null) {
					elems = db.split(" ");
					if (elems != null && elems.length > 0) {
						this.getResourceManager().setDebug(Integer.parseInt(elems[0]));
					}
				}
				db = reader.readLine();
				if (db != null) {
					elems = db.split(" ");
					if (elems != null && elems.length > 0) {
						String nodeids = elems[0];
						String[] ids = nodeids.split("\\|\\|");
						this.getResourceManager().getNids().clear();
						if (ids != null) {
							for (String i : ids) {
								if (i != null && !i.isEmpty()) {
									try {
										NodeId id = NodeId.parseNodeId(i);
										this.getResourceManager().getNids().add(id);
									} catch (IllegalArgumentException ex) {
										logger.log(Level.SEVERE, "NodeId to debug has wrong format: " + i);
									}
								}
							}
						}
					}
				}
			} catch (IOException e) {
				logger.log(Level.SEVERE, e.getMessage());
			}
		}
	}

	/***************************************************************************
	 * 
	 * end region load all installed drivers from driver directory into class-loader
	 * as binary
	 * 
	 **************************************************************************/
	/**
	 * return driver state if it could be found, otherwise driver not found state
	 * 
	 * @param drvName name of desired driver
	 * 
	 * @return driver state, or driver not found value
	 */
	public long getDriverState(int drvId) {
		// get driver from which the state is requested
		ComDRV driver = this.manager.getDriver(drvId);
		// could driver be found ?
		if (driver == null) {
			return ComStates.DRVNOTFOUND;
		}
		// get state of the requested driver
		return driver.getState();
	}

	/****************************************************************************
	 * 
	 * region to add, load and start driver(s)
	 * 
	 ***************************************************************************/
	/**
	 * Starts all loaded drivers.
	 */
	public void startAllDrivers() {
		// get all drivers from resoucemanager
		List<ComDRV> drivers = this.manager.getOrderedDrivers();
		// are some drivers found
		if (drivers != null && !drivers.isEmpty()) {
			// iterate over all drivers to start
			for (ComDRV driver : drivers) {
				// now try to start that driver
				this.startDriver(driver.getDriverId());
			}
			/* now start the driver scheduler */
			this.manager.getScheduler().startschedule();
		}
	}

	/**
	 * load user configuration for node authorization
	 */
	public void loadUserConfig() {
		/**
		 * user information
		 */
		File usrfile = new File("users/userconfig.db");
		if (usrfile.exists()) {
			drvManager.getServer().setUserConfiguration(usrfile.getAbsolutePath());
			// now we have to starte the security manager
			drvManager.getServer().getUserAuthentifiationManager().start();
		}
	}

	/**
	 * starts loaded driver by name and returns result
	 * 
	 * @param drvName name of driver to start
	 * @return error state or OK
	 */
	private long startDriver(long drvId) {
		// here driver must exists
		final ComDRV driver = this.manager.getDriver(drvId);
		// driver exists ?
		if (driver != null) {
			// start the cycle scheduler
			if (driver.start()) {
				/* do some last initializations */
				if (!driver.doStartup()) {
					new Thread() {
						@Override
						public void run() {
							while (!driver.doStartup()) {
								// startup not possible, try it in one seconds
								// again
								try {
									Thread.sleep(1000);
								} catch (InterruptedException e) {
									logger.log(Level.SEVERE, e.getMessage());
								}
							}
						}
					}.start();
					driver.setState(ComDRVStates.STARTING);
				}
				// set driver to running state
				driver.setState(ComDRVStates.RUNNING);
				return ComStates.OK;
			} else {
				logger.log(Level.SEVERE, "Starting driver not possible: " + driver.drvName);
				return ComStates.COMCLOSED;
			}
		} else {
			logger.log(Level.SEVERE, "No right driver found with name: " + drvId);
		}
		return ComStates.DRVNOTFOUND;
	}

	/****************************************************************************
	 * 
	 * end region to add, load and start driver(s)
	 * 
	 ***************************************************************************/
	/****************************************************************************
	 * 
	 * region to stop, clear, remove and store driver(s)
	 * 
	 ***************************************************************************/
	/**
	 * Stop all defined drivers which where running and disconnect it from device.
	 */
	public void stopAllDrivers() {
		// get all drivers from resoucemanager
		Map<Long, ComDRV> drivers = this.manager.getDrivers();
		// are some drivers found
		if (drivers != null) {
			// get iterator for all driver keys
			Iterator<Long> keys = drivers.keySet().iterator();
			// key for each driver to stop
			long key = 0;
			// iterate over all drivers to stop
			while (keys.hasNext()) {
				key = keys.next();
				// now try to stop that driver
				this.stopDriver(key);
			}
			this.manager.getScheduler().stopschedule();
		}
	}

	/**
	 * Stop one running driver by id.
	 * 
	 * @param drvId : Id of driver to stop.
	 */
	public long stopDriver(long drvId) {
		// get driver by key
		ComDRV drv = this.manager.getDriver(drvId);
		// could driver be found ?
		if (drv == null) {
			logger.log(Level.SEVERE, "Driver to stop could not be found! ");
			return ComStates.DRVNOTFOUND;
		}
		if (this.manager.isActivatedebug()
				&& (this.manager.getDebug() & this.manager.DEBUG_STARTUP) == this.manager.DEBUG_STARTUP) {
			logger.info("Try to stop driver: " + drvId);
		}
		// now stop this driver
		if (drv.stop()) {
			drv.setState(ComDRVStates.STOPPED);
		}
		return ComStates.OK;
	}

	/**
	 * remove all configured drivers stop is not provided by this function, you have
	 * to stop all drivers previously
	 * 
	 */
	public void removeAllDrivers() {
		// are some drivers found
		if (this.manager.getDrivers() != null) {
			Long[] drvIds = this.manager.getDrivers().keySet().toArray(new Long[] {});
			// get all drivers from resoucemanager
			for (Long dr : drvIds) {
				removeDriver(dr);
			}
			this.manager.getDrivers().clear();
		}
	}

	/**
	 * remove configured driver by name, stop is not provided by this function, you
	 * have to stop that driver previously
	 * 
	 * @param name of driver to remove
	 * @return OK if no error occurs, otherwise error number
	 */
	public long removeDriver(long drvId) {
		// remove driver from list
		ComDRV driver = this.manager.removeDriver(drvId);
		if (driver == null) {
			return ComStates.DRVNOTFOUND;
		}
		/* do finish functionality e.g. remove all dps,... */
		driver.doFinish();
		return ComStates.OK;
	}

	/****************************************************************************
	 * 
	 * end region to stop, clear, remove and store driver(s)
	 * 
	 ***************************************************************************/
	/***************************************************************************
	 * 
	 * region data point list manipulation
	 * 
	 **************************************************************************/
	/**
	 * Initialize some data points from address space and add it to the defined
	 * driver.
	 * 
	 * @param drvId Id of driver to add the nodes.
	 * @param ids   All node ids of data points to load.
	 */
	public void initDPs(int drvId, List<NodeId> ids) {
		ComDRV drv = this.manager.getDriver(drvId);
		if (drv == null) {
			logger.log(Level.SEVERE, "Can not init data points for driver(null) number: " + drvId);
			return;
		}
		if (ids == null) {
			logger.log(Level.SEVERE, "Can not init data points(null) for driver number: " + drvId);
			return;
		}
		/* add all nodeids to the correct driver */
		for (NodeId nodeId : ids) {
			drv.getDevice().addDP(nodeId);
		}
	}

	/**
	 * Add one data point to the driver list defined by NodeId.
	 * 
	 * @param drvId  Id of driver to add data point for.
	 * @param nodeId Nodeid to configure data point to add
	 * 
	 * @return OK if no error occurs, otherwise error number
	 */
	public long addDP(int drvId, NodeId nodeId) {
		// no drivers connected?
		if (this.manager.getDrivers() == null) {
			logger.log(Level.SEVERE, "No driver could be found!");
			return ComStates.DRVNOTFOUND;
		}
		// get the desired driver from list
		ComDRV driver = this.manager.getDriver(drvId);
		if (driver == null) {
			logger.log(Level.SEVERE, "Driver with id: " + drvId + " could not be found!");
			return ComStates.DRVNOTFOUND;
		}
		driver.getDevice().addDP(nodeId);
		return ComStates.OK;
	}

	/**
	 * remove one data point from the driver list defined by name
	 * 
	 * @param drvId  id of driver to remove data point from
	 * @param nodeId id of data point to remove
	 * 
	 * @return OK if no error occurs, otherwise error number
	 */
	public long removeDP(int drvId, NodeId nodeId) {
		// no drivers connected?
		if (this.manager.getDrivers() == null) {
			logger.log(Level.INFO, "No driver could be found!");
			return ComStates.DRVNOTFOUND;
		}
		// get the desired driver from list
		ComDRV driver = this.manager.getDriver(drvId);
		if (driver == null) {
			// if not found return and don't add a new data point
			if (this.manager.isActivatedebug()
					&& (this.manager.getDebug() & this.manager.DEBUG_STARTUP) == this.manager.DEBUG_STARTUP) {
				logger.log(Level.INFO, "Driver: " + nodeId.toString() + " could not be found!");
			}
			return ComStates.DRVNOTFOUND;
		}
		driver.getDevice().removeDPFromList(nodeId);
		return ComStates.OK;
	}

	/***************************************************************************
	 * 
	 * end region data point list manipulation
	 * 
	 **************************************************************************/
	/**
	 * Get drivers from connected drivers list.
	 * 
	 * @return
	 */
	public Map<Long, ComDRV> getDrivers() {
		return this.manager.getDrivers();
	}

	/**
	 * get driver from all connected drivers if no driver with that name is
	 * connected null is returned
	 * 
	 * @param drvName
	 * @return
	 */
	public ComDRV getDriver(long drvId) {
		return this.manager.getDriver(drvId);
	}

	/**
	 * Write value to device and don't wait for response.
	 * 
	 * @deprecated use asyncWriteValue(NodeId nodeId, DataValue value, DataValue
	 *             oldValue, long[] drvIds, long senderState) instead
	 * @param nodeId
	 */
	@Deprecated
	public StatusCode asyncWriteValue(NodeId nodeId, DataValue value, DataValue oldValue, long senderState) {
		StatusCode result = StatusCode.GOOD;
		/**
		 * do we have a write handler registered ?
		 */
		if (this.writeListeners != null) {
			IWriteListener listener = null;
			for (long id : this.writeListeners.keySet()) {
				if (id != senderState) {
					listener = this.writeListeners.get(id);
					if (listener != null) {
						result = listener.asyncWriteValue(nodeId, value, oldValue, senderState);
						if (result != null && !StatusCode.GOOD.equals(result)) {
							return result;
						}
					}
				}
			}
		}
		return result;
	}

	/**
	 * Write value to device and don't wait for response.
	 * 
	 * @param nodeId
	 */
	public StatusCode asyncWriteValue(NodeId nodeId, DataValue value, DataValue oldValue, long[] drvIds,
			long senderState) {
		StatusCode result = StatusCode.GOOD;
		/**
		 * do we have a write handler registered ?
		 */
		if (this.writeListeners != null && drvIds != null) {
			IWriteListener listener = null;
			for (long drvid : drvIds) {
				if (drvid != senderState) {
					listener = this.writeListeners.get(drvid);
					if (listener != null) {
						result = listener.asyncWriteValue(nodeId, value, oldValue, senderState);
						if (result != null && !StatusCode.GOOD.equals(result)) {
							return result;
						}
					}
				}
			}
		}
		return result;
	}

	/**
	 * Write value to device and don't wait for response.
	 * 
	 * @deprecated asyncWriteValue(NodeId nodeId, DataValue value, DataValue
	 *             oldValue, String indexRange, long[] drvIds, long senderState)
	 *             instead
	 * @param nodeId
	 */
	@Deprecated
	public StatusCode asyncWriteValue(NodeId nodeId, DataValue value, DataValue oldValue, String indexRange,
			long senderState) {
		StatusCode result = StatusCode.GOOD;
		/**
		 * do we have a write handler registered ?
		 */
		if (this.writeListeners != null) {
			IWriteListener listener = null;
			for (long id : this.writeListeners.keySet()) {
				if (id != senderState) {
					listener = this.writeListeners.get(id);
					if (listener != null) {
						result = listener.asyncWriteValue(nodeId, value, oldValue, indexRange, senderState);
						if (result != null && !StatusCode.GOOD.equals(result)) {
							return result;
						}
					}
				}
			}
		}
		return result;
	}

	/**
	 * Write value to device and don't wait for response.
	 * 
	 * @param nodeId
	 */
	public StatusCode asyncWriteValue(NodeId nodeId, DataValue value, DataValue oldValue, String indexRange,
			long[] drvIds, long senderState) {
		StatusCode result = StatusCode.GOOD;
		/**
		 * do we have a write handler registered ?
		 */
		if (this.writeListeners != null && drvIds != null) {
			IWriteListener listener = null;
			for (long id : drvIds) {
				if (id != senderState) {
					listener = this.writeListeners.get(id);
					if (listener != null) {
						result = listener.asyncWriteValue(nodeId, value, oldValue, indexRange, senderState);
						if (result != null && !StatusCode.GOOD.equals(result)) {
							return result;
						}
					}
				}
			}
		}
		return result;
	}

	/**
	 * Write value to device and wait for response.
	 * 
	 * @deprecated syncWriteValue(NodeId nodeId, DataValue value, DataValue
	 *             oldValue, long[] drvIds, long senderState) instead
	 * 
	 * @param nodeId      Nodeid of data point to change value for.
	 * @param value       Value to write to device.
	 * @param senderState State of sender to set to the data point, to know who has
	 *                    written value.
	 * @return Driver not found or OK.
	 */
	@Deprecated
	public StatusCode syncWriteValue(NodeId nodeId, DataValue value, DataValue oldValue, long senderState) {
		StatusCode result = StatusCode.GOOD;
		/**
		 * do we have a write handler registered ?
		 */
		if (this.writeListeners != null) {
			IWriteListener listener = null;
			for (long id : this.writeListeners.keySet()) {
				if (id != senderState) {
					listener = this.writeListeners.get(id);
					if (listener != null) {
						result = listener.syncWriteValue(nodeId, value, oldValue, senderState);
						if (result != null && !StatusCode.GOOD.equals(result)) {
							return result;
						}
					}
				}
			}
		}
		return result;
	}

	/**
	 * Write value to device and wait for response.
	 * 
	 * @param nodeId      Nodeid of data point to change value for.
	 * @param value       Value to write to device.
	 * @param senderState State of sender to set to the data point, to know who has
	 *                    written value.
	 * @return Driver not found or OK.
	 */
	public StatusCode syncWriteValue(NodeId nodeId, DataValue value, DataValue oldValue, long[] drvIds,
			long senderState) {
		StatusCode result = StatusCode.GOOD;
		/**
		 * do we have a write handler registered ?
		 */
		if (this.writeListeners != null && drvIds != null) {
			IWriteListener listener = null;
			for (long drvid : drvIds) {
				if (drvid != senderState) {
					listener = this.writeListeners.get(drvid);
					if (listener != null) {
						result = listener.syncWriteValue(nodeId, value, oldValue, senderState);
						if (result != null && !StatusCode.GOOD.equals(result)) {
							return result;
						}
					}
				}
			}
		}
		return result;
	}

	/**
	 * Write value to device and wait for response.
	 * 
	 * @deprecated syncWriteValue(NodeId nodeId, DataValue value, DataValue
	 *             oldValue, String indexRange, long[] drvIds, long senderState)
	 *             instead
	 * @param nodeId      Nodeid of data point to change value for.
	 * @param value       Value to write to device.
	 * @param senderState State of sender to set to the data point, to know who has
	 *                    written value.
	 * @return Driver not found or OK.
	 */
	@Deprecated
	public StatusCode syncWriteValue(NodeId nodeId, DataValue value, DataValue oldValue, String indexRange,
			long senderState) {
		StatusCode result = StatusCode.GOOD;
		/**
		 * do we have a write handler registered ?
		 */
		if (this.writeListeners != null) {
			IWriteListener listener = null;
			for (long drvid : writeListeners.keySet()) {
				if (drvid != senderState) {
					listener = this.writeListeners.get(drvid);
					if (listener != null) {
						result = listener.syncWriteValue(nodeId, value, oldValue, indexRange, senderState);
						if (result != null && !StatusCode.GOOD.equals(result)) {
							return result;
						}
					}
				}
			}
		}
		return result;
	}

	/**
	 * Write value to device and wait for response.
	 * 
	 * @param nodeId      Nodeid of data point to change value for.
	 * @param value       Value to write to device.
	 * @param drvIds      ids of all drivers connecte to nodeid
	 * @param senderState State of sender to set to the data point, to know who has
	 *                    written value.
	 * @return Driver not found or OK.
	 */
	public StatusCode syncWriteValue(NodeId nodeId, DataValue value, DataValue oldValue, String indexRange,
			long[] drvIds, long senderState) {
		StatusCode result = StatusCode.GOOD;
		/**
		 * do we have a write handler registered ?
		 */
		if (this.writeListeners != null && drvIds != null) {
			IWriteListener listener = null;
			for (long drvid : drvIds) {
				if (drvid != senderState) {
					listener = this.writeListeners.get(drvid);
					if (listener != null) {
						result = listener.syncWriteValue(nodeId, value, oldValue, indexRange, senderState);
						if (result != null && !StatusCode.GOOD.equals(result)) {
							return result;
						}
					}
				}
			}
		}
		return result;
	}

	/**
	 * Start reading value from device and don't wait for response.
	 * 
	 * @deprecated use asyncReadValue(NodeId nodeId, long[] drvIds, long
	 *             senderState) instead
	 * 
	 * @param nodeId      Nodeid of data point to read value.
	 * @param senderState State of sender to set to the data point, to know who has
	 *                    red value.
	 * @return Driver not found or OK.
	 */
	@Deprecated
	public StatusCode asyncReadValue(NodeId nodeId, long senderState) {
		StatusCode result = null;
		/**
		 * do we have a read handler registered ?
		 */
		if (this.readListeners != null) {
			IReadListener listener = null;
			for (long drvid : readListeners.keySet()) {
				if (drvid != senderState) {
					listener = this.readListeners.get(drvid);
					if (listener != null) {
						result = listener.asyncReadValue(nodeId, senderState);
					}
				}
			}
		}
		return result;
	}

	/**
	 * Start reading value from device and don't wait for response.
	 * 
	 * @param nodeId      Nodeid of data point to read value.
	 * @param drvIds      ids of all drivers connecte to nodeid
	 * @param senderState State of sender to set to the data point, to know who has
	 *                    red value.
	 * @return Driver not found or OK.
	 */
	public StatusCode asyncReadValue(NodeId nodeId, long[] drvIds, long senderState) {
		StatusCode result = null;
		/**
		 * do we have a read handler registered ?
		 */
		if (this.readListeners != null && drvIds != null) {
			IReadListener listener = null;
			for (long drvid : drvIds) {
				if (drvid != senderState) {
					listener = this.readListeners.get(drvid);
					if (listener != null) {
						result = listener.asyncReadValue(nodeId, senderState);
					}
				}
			}
		}
		return result;
	}

	/**
	 * Start reading value from device and don't wait for response.
	 * 
	 * @param nodeId      Nodeid of data point to read value.
	 * @param senderState State of sender to set to the data point, to know who has
	 *                    red value.
	 * @return Driver not found or OK.
	 */
	@Deprecated
	public DataValue syncReadValue(NodeId nodeId, long senderState) {
		DataValue result = null;
		/**
		 * do we have a read handler registered ?
		 */
		if (this.readListeners != null) {
			IReadListener listener = null;
			for (long drvid : readListeners.keySet()) {
				if (drvid != senderState) {
					listener = this.readListeners.get(drvid);
					if (listener != null) {
						result = listener.syncReadValue(nodeId, senderState);
					}
				}
			}
		}
		return result;
	}

	/**
	 * Start reading value from device and wait for response.
	 * 
	 * @param nodeId      Nodeid of data point to read value.
	 * @param senderState State of sender to set to the data point, to know who has
	 *                    red value.
	 * @return Driver not found or OK.
	 */
	public DataValue syncReadValue(NodeId nodeId, long[] drvIds, long senderState) {
		DataValue result = null;
		/**
		 * do we have a read handler registered ?
		 */
		if (this.readListeners != null && drvIds != null) {
			IReadListener listener = null;
			for (long drvid : drvIds) {
				if (drvid != senderState) {
					listener = this.readListeners.get(drvid);
					if (listener != null) {
						result = listener.syncReadValue(nodeId, senderState);
					}
				}
			}
		}
		return result;
	}

	/**
	 * Start reading history values from device and wait for response.
	 * 
	 * @param nodeId      Nodeid of data point to read value.
	 * @param senderState State of sender to set to the data point, to know who has
	 *                    red value.
	 * @return Driver not found or OK.
	 */
	public HistoryReadResult syncHistReadValue(NodeId nodeId, HistoryReadDetails details, long[] drvIds,
			long senderState) {
		HistoryReadResult result = null;
		/**
		 * do we have a read handler registered ?
		 */
		if (this.readListeners != null && drvIds != null) {
			IHistoryReadListener listener = null;
			for (long drvid : drvIds) {
				if (drvid != senderState) {
					listener = this.historyReadListeners.get(drvid);
					if (listener != null) {
						result = listener.syncHistoryReadValue(nodeId, details, senderState);
					}
				}
			}
		}
		return result;
	}

	/**
	 * Start reading history values from device and wait for response.
	 * 
	 * @param nodeId      Nodeid of data point to read value.
	 * @param senderState State of sender to set to the data point, to know who has
	 *                    red value.
	 * @return Driver not found or OK.
	 */
	public StatusCode syncHistReadEventValue(NodeId nodeId, ReadEventDetails details, long[] drvIds,
			HistoryReadResult result, long senderState) {
		/**
		 * do we have a read handler registered ?
		 */
		if (this.readListeners != null && drvIds != null) {
			IHistoryReadListener listener = null;
			for (long drvid : drvIds) {
				if (drvid != senderState) {
					listener = this.historyReadListeners.get(drvid);
					if (listener != null) {
						result = listener.syncHistoryReadEventValue(nodeId, details, senderState);
					}
				}
			}
		}
		return StatusCode.GOOD;
	}

	/***************************************************************************
	 * 
	 * region for internal program communication
	 * 
	 **************************************************************************/
	/**
	 * Write a driver value to the ua address space. returns true if variable could
	 * be written otherwise false.
	 * 
	 * @deprecated use writeFromDriver([])
	 * 
	 * @param nodeId  Node id of ua variable node to write to device.
	 * @param value   DataValue with value to write.
	 * @param dpState State of the data point.
	 * @return true if write was ok otherwise false
	 */
	public StatusCode writeFromDriver(NodeId nodeId, UnsignedInteger attributeId, String indexRange, DataValue value,
			long dpState) {
		StatusCode[] result = writeFromDriver(new NodeId[] { nodeId }, new UnsignedInteger[] { attributeId },
				new String[] { indexRange }, new DataValue[] { value }, new Long[] { dpState });
		if (result != null && result.length > 0) {
			return result[0];
		}
		return null;
	}

	/***************************************************************************
	 * 
	 * region for inter program communication
	 * 
	 **************************************************************************/
	/**
	 * Write a driver value to the ua address space returns true if variables could
	 * be written otherwise false
	 * 
	 * @param nodeIds  Node id of data point to write.
	 * @param values   DataValues with values to write.
	 * @param dpStates States of the to write data points.
	 * @return true if write was ok otherwise false
	 */
	public StatusCode[] writeFromDriver(NodeId[] nodeIds, UnsignedInteger[] attributeIds, String[] indexRanges,
			DataValue[] values, Long[] dpStates) {
		IComDriverConnection connect = this.manager.getDriverConnection();
		if (connect != null) {
			return connect.writeFromDriver(nodeIds, attributeIds, indexRanges, values, dpStates);
		}
		return null;
	}

	/**
	 * Write a driver value to the ua address space returns true if variables could
	 * be written otherwise false
	 * 
	 * @param nodeIds  Node id of data point to write.
	 * @param values   DataValues with values to write.
	 * @param dpStates States of the to write data points.
	 * @return true if write was ok otherwise false
	 */
	public StatusCode[] fireEventFromDriver(NodeId[] nodeIds, BaseEventType[] eventstates, Long[] dpStates) {
		IComDriverConnection connect = this.manager.getDriverConnection();
		if (connect != null) {
			return connect.fireEventFromDriver(nodeIds, eventstates, dpStates);
		}
		return null;
	}

	public boolean filterEventFromDriver(NodeId nodeId, BaseEventType state, EventFilter filter) {
		return state.validateEvent(filter);
	}

	/**
	 * Read an attribute from opc ua.
	 * 
	 * @param nodeId
	 * @param id
	 * @return the read datavalue
	 */
	public DataValue readFromDriver(NodeId nodeId, UnsignedInteger attributeIds, String indexRange,
			QualifiedName dataEncoding, Long dpState, double maxAge, TimestampsToReturn timestampToReturn) {
		DataValue[] value = readFromDriver(new NodeId[] { nodeId }, new UnsignedInteger[] { attributeIds },
				new String[] { indexRange }, new QualifiedName[] { dataEncoding }, new Long[] { dpState }, maxAge,
				timestampToReturn);
		if (value != null && value.length > 0) {
			return value[0];
		}
		return null;
	}

	/**
	 * Read an attribute from opc ua.
	 * 
	 * @param nodeId
	 * @param id
	 * @return the read datavalue
	 */
	public DataValue[] readFromDriver(NodeId[] nodeId, UnsignedInteger[] attributeIds, String[] indexRanges,
			QualifiedName[] dataEncodings, Long[] dpState, double maxAge, TimestampsToReturn timestampToReturn) {
		IComDriverConnection connect = this.manager.getDriverConnection();
		if (connect != null) {
			return connect.readFromDriver(nodeId, attributeIds, indexRanges, dataEncodings, dpState, maxAge,
					timestampToReturn);
		}
		return null;
	}

	/**
	 * Read an attribute from opc ua.
	 * 
	 * @param nodeId
	 * @param id
	 * @return the read datavalue
	 */
	public void setNodeVisible(NodeId[] nodeId, boolean isVisible) {
		IComDriverConnection connect = this.manager.getDriverConnection();
		if (connect != null) {
			connect.setNodeVisible(nodeId, isVisible);
		}
	}

	/**
	 * get a list with all children of a node in the address space
	 * 
	 * @param nodeId Nodeid to get the node for.
	 * @return List with all children for that id.
	 */
	public Node[] getChildren(NodeId nodeId) {
		IComDriverConnection connect = manager.getDriverConnection();
		if (connect != null) {
			return connect.getChildren(nodeId);
		}
		return null;
	}

	public Node getNode(NodeId nodeId) {
		IComDriverConnection connect = manager.getDriverConnection();
		if (connect != null) {
			return connect.getNode(nodeId);
		}
		return null;
	}

	/**
	 * get the type info of the value of a variable node, only nodeids of variable
	 * nodes are allowed
	 * 
	 * @param nodeId Id of variable node to get the whole type info.
	 * @return Typeinfo of the value or null if it is node has no value.
	 */
	public TypeInfo getTypeInfo(NodeId nodeId) {
		IComDriverConnection connect = this.manager.getDriverConnection();
		if (connect != null) {
			return connect.getTypeInfo(nodeId);
		}
		return null;
	}

	/***************************************************************************
	 * 
	 * end region for inter program communication
	 * 
	 **************************************************************************/
	public ComResourceManager getResourceManager() {
		return this.manager;
	}

	/**
	 * Register a new node for the notification service.
	 * 
	 * @param node         The node to register for the service.
	 * @param itemToCreate Create request for a node with additional information.
	 * 
	 * @return GOOD if node could be registered, otherwise the corresponding code.
	 */
	public StatusCode registerEvent(Node node, MonitoredItemCreateRequest itemToCreate, IOPCServerSession session) {
		StatusCode result = null;
		/**
		 * do we have registered a monitoring listener?
		 */
		if (this.monitoringListeners != null) {
			for (IMonitoringListener listener : this.monitoringListeners) {
				result = listener.registerEvent(node, itemToCreate);
				if (result != null) {
					return result;
				}
			}
		}
		if (node != null && itemToCreate != null)
			logger.log(Level.INFO, "Register event for nodeid: " + node.getNodeId() + " | samplinginterval: "
					+ itemToCreate.getRequestedParameters().getSamplingInterval());
		return result;
	}

	/**
	 * Change a registered node for the notification service.
	 * 
	 * @param node         The node to change for the service.
	 * @param itemToChange Change request for a node with additional information.
	 * 
	 * @return GOOD if node could be changed, otherwise the corresponding code.
	 */
	public StatusCode changeEvent(Node node, MonitoredItemModifyRequest itemToChange) {
		StatusCode result = null;
		/**
		 * do we have registered a monitoring listener?
		 */
		if (this.monitoringListeners != null) {
			for (IMonitoringListener listener : this.monitoringListeners) {
				result = listener.changeEvent(node, itemToChange);
				if (result != null) {
					return result;
				}
			}
		}
		if (node != null && node.getNodeId() != null && itemToChange != null)
			logger.log(Level.INFO, "Change event for nodeid: " + node.getNodeId() + " | samplinginterval: "
					+ itemToChange.getRequestedParameters().getSamplingInterval());
		return result;
	}

	/**
	 * UnRegister an existing node from the notification service.
	 * 
	 * @param nodeId The nodeid to unregister a node from the service.
	 * 
	 * @return GOOD if node could be unregistered, otherwise the corresponding code.
	 */
	public StatusCode unregisterEvent(NodeId nodeId, IOPCServerSession session) {
		StatusCode result = null;
		/**
		 * do we have registered a monitoring listener?
		 */
		if (this.monitoringListeners != null) {
			for (IMonitoringListener listener : this.monitoringListeners) {
				result = listener.unregisterEvent(nodeId);
				if (result != null) {
					return result;
				}
			}
		}
		logger.log(Level.INFO, "Unregister event for nodeid: {0}", nodeId);
		return result;
	}

	/**
	 * Register a new node for the notification service.
	 * 
	 * @param node         The node to register for the service.
	 * @param itemToCreate Create request for a node with additional information.
	 * 
	 * @return GOOD if node could be registered, otherwise the corresponding code.
	 */
	public StatusCode registerNotification(Node node, MonitoredItemCreateRequest itemToCreate) {
		StatusCode result = null;
		/**
		 * do we have registered a monitoring listener?
		 */
		if (this.monitoringListeners != null) {
			for (IMonitoringListener listener : this.monitoringListeners) {
				result = listener.registerNotification(node, itemToCreate);
				if (result != null) {
					return result;
				}
			}
		}
		if (node != null && itemToCreate != null)
			logger.log(Level.INFO, "Register monitored item with nodeid: " + node.getNodeId() + " | samplinginterval: "
					+ itemToCreate.getRequestedParameters().getSamplingInterval());
		return result;
	}

	/**
	 * Change a registered node for the notification service.
	 * 
	 * @param node         The node to change for the service.
	 * @param itemToCreate Modify request for a node with additional information.
	 * 
	 * @return GOOD if node could be modified, otherwise the corresponding code.
	 */
	public StatusCode changeNotification(Node node, MonitoredItemModifyRequest itemToChange) {
		StatusCode result = null;
		/**
		 * do we have registered a monitoring listener?
		 */
		if (this.monitoringListeners != null) {
			for (IMonitoringListener listener : this.monitoringListeners) {
				result = listener.changeNotification(node, itemToChange);
				if (result != null) {
					return result;
				}
			}
		}
		if (node != null && itemToChange != null)
			logger.log(Level.INFO, "Modify monitored item with nodeid: {0} | samplinginterval: {1}",
					new String[] { node.getNodeId().toString(),
							Double.toString(itemToChange.getRequestedParameters().getSamplingInterval()) });
		return result;
	}

	/**
	 * UnRegister an existing node from the notification service.
	 * 
	 * @param nodeId The nodeid to unregister a node from the service.
	 * 
	 * @return GOOD if node could be unregistered, otherwise the corresponding code.
	 */
	public StatusCode unregisterNotification(NodeId nodeId) {
		StatusCode result = null;
		/**
		 * do we have registered a monitoring listener?
		 */
		if (this.monitoringListeners != null) {
			for (IMonitoringListener listener : this.monitoringListeners) {
				result = listener.unregisterNotification(nodeId);
				if (result != null) {
					return result;
				}
			}
		}
		logger.log(Level.INFO, "Unregister monitored item with nodeid: {0}", nodeId);
		return result;
	}

	/**
	 * Modify a already registered node with the new parameters.
	 * 
	 * @param nodeId       The nodeid to modify the notification for a node.
	 * @param itemToModify Modify request for a node with additional information.
	 * 
	 * @return GOOD if notification for a node could be modified, otherwise the
	 *         corresponding code.
	 */
	public StatusCode modifyNotification(NodeId nodeId, MonitoredItemModifyRequest itemToModify) {
		StatusCode result = null;
		/**
		 * do we have registered a monitoring listener?
		 */
		if (this.monitoringListeners != null) {
			for (IMonitoringListener listener : this.monitoringListeners) {
				result = listener.modifyNotification(nodeId, itemToModify);
				if (result != null) {
					return result;
				}
			}
		}
		if (itemToModify != null)
			logger.log(Level.INFO, "Modify monitored item with nodeid: {0} | samplinginterval: ", nodeId);
		return StatusCode.GOOD;
	}

	/** *************************************************** */
	/** new prepare functions */
	/**													 	*/
	/**
	 * Prepares a node for a read request. We set flags to the node if it should be
	 * red or not.
	 * 
	 */
	public void prepareRead(NodeId nodeId) {
		/**
		 * do we have registered a read listener?
		 */
		if (this.readListeners != null) {
			boolean couldset = false;
			for (IReadListener listener : this.readListeners.values()) {
				couldset = listener.prepareRead(nodeId);
				if (couldset) {
					return;
				}
			}
		}
	}

	/**
	 * Prepares a node for a write request. We set flags to the node if it should be
	 * written or not.
	 * 
	 */
	public void prepareWrite(NodeId nodeId) {
		/**
		 * do we have registered a write listener?
		 */
		if (this.writeListeners != null) {
			boolean couldset = false;
			for (IWriteListener listener : this.writeListeners.values()) {
				couldset = listener.prepareWrite(nodeId);
				if (couldset) {
					return;
				}
			}
		}
	}

	public boolean addNodes(AddNodesItem[] nodes) {
		if (this.manager.getDriverConnection() != null) {
			return this.manager.getDriverConnection().addNodes(nodes);
		}
		return false;
	}

	public boolean loadNodes(AddNodesItem[] nodes) {
		if (this.manager.getDriverConnection() != null) {
			return this.manager.getDriverConnection().loadNodes(nodes);
		}
		return false;
	}

	public Map<Long, IReadListener> getReadListener() {
		return readListeners;
	}

	public Map<Long, ILoginListener> getLoginListener() {
		return loginListeners;
	}

	public void addReadListener(Long drvNumber, IReadListener readListener) {
		this.readListeners.put(drvNumber, readListener);
	}

	public void addLoginListener(Long drvNumber, ILoginListener loginListener) {
		this.loginListeners.put(drvNumber, loginListener);
	}

	public void removeReadListener(Long drvId) {
		this.readListeners.remove(drvId);
	}

	public void removeLoginListener(Long drvId) {
		this.loginListeners.remove(drvId);
	}

	public Map<Long, IWriteListener> getWriteListeners() {
		return writeListeners;
	}

	public void addWriteListener(Long drvNumber, IWriteListener writeListener) {
		this.writeListeners.put(drvNumber, writeListener);
	}

	public void removeWriteListener(Long drvId) {
		this.writeListeners.remove(drvId);
	}

	public List<IMonitoringListener> getMonitoringListeners() {
		return monitoringListeners;
	}

	public void addMonitoringListener(IMonitoringListener monitoringListener) {
		this.monitoringListeners.add(monitoringListener);
	}

	public void removeMonitoringListener(IMonitoringListener monitoringListener) {
		this.monitoringListeners.remove(monitoringListener);
	}

	public List<IMethodListener> getMethodListeners() {
		return methodListeners;
	}

	public Map<NodeId, IMethodListener> getMethodListenersById() {
		return methodListenersById;
	}

	public void addMethodListener(IMethodListener methodListener) {
		this.methodListeners.add(methodListener);
	}

	public void addMethodListener(NodeId id, IMethodListener methodListener) {
		this.methodListenersById.put(id, methodListener);
	}

	public void addMethodListener(NodeId obj, NodeId method, IMethodListener methodListener) {
		Map<NodeId, IMethodListener> methods = null;
		if (methodListenersByIdId.containsKey(obj)) {
			methods = methodListenersByIdId.get(obj);
		}
		if (methods == null) {
			methods = new HashMap<NodeId, IMethodListener>();
			methods.put(method, methodListener);
		}
		methodListenersByIdId.put(obj, methods);
	}

	public void addMethodListener(String browseNames, IMethodListener methodListener) {
		String[] items = browseNames.split("/");
		List<QualifiedName> names = new ArrayList<>();
		if (items != null) {
			for (String qname : items) {
				String[] name = qname.split(":");
				if (name != null && name.length >= 1) {
					try {
						QualifiedName qual = new QualifiedName(Integer.parseInt(name[0]), name[1]);
						names.add(qual);
					} catch (NumberFormatException ex) {
						logger.log(Level.SEVERE, ex.getMessage());
						return;
					}
				}
			}
		}
		List<QualifiedName> parentNames = new ArrayList<>(names);
		if (parentNames.size() <= 0) {
			logger.log(Level.SEVERE, "No objectnode found for browsepath: " + browseNames);
			return;
		}
		parentNames.remove(parentNames.size() - 1); // remove last

		BrowsePath parentBrowsePath = new BrowsePath();
		parentBrowsePath.setStartingNode(Identifiers.ObjectsFolder);
		parentBrowsePath.setRelativePath(Utils.parseRelativePath(parentNames.toArray(new QualifiedName[0]),
				server.getTypeTable(), server.getNamespaceUris(), server.getNamespaceUris(), false));

		BrowsePath browsePath = new BrowsePath();
		browsePath.setStartingNode(Identifiers.ObjectsFolder);
		browsePath.setRelativePath(Utils.parseRelativePath(names.toArray(new QualifiedName[names.size()]),
				server.getTypeTable(), server.getNamespaceUris(), server.getNamespaceUris(), false));
		BrowsePath[] browsePaths = new BrowsePath[2];
		browsePaths[0] = parentBrowsePath;
		browsePaths[1] = browsePath;
		try {
			BrowsePathResult[] result = server.getMaster().translateBrowsePathsToNodeIds(browsePaths, null);
			if (result != null && result.length > 1) {
				this.addMethodListener(server.getNamespaceUris().toNodeId(result[0].getTargets()[0].getTargetId()),
						server.getNamespaceUris().toNodeId(result[1].getTargets()[0].getTargetId()), methodListener);
//        this.methodListenersById.put(server.getNamespaceUris().toNodeId(result[0].getTargets()[0].getTargetId()),
//            methodListener);
			} else {
				logger.log(Level.SEVERE, "No browsepaths found!");
			}
		} catch (ServiceResultException e) {
			logger.log(Level.SEVERE, e.getMessage());
		}
	}

	public void removeMethodListener(IMethodListener methodListener) {
		this.methodListeners.remove(methodListener);
	}

	public void removeMethodListener(NodeId id) {
		this.methodListenersById.remove(id);
	}

	public List<IViewListener> getBrowseListeners() {
		return viewListeners;
	}

	public void addBrowseListener(IViewListener browseListener) {
		this.viewListeners.add(browseListener);
	}

	public void removeBrowseListener(IViewListener browseListener) {
		this.viewListeners.remove(browseListener);
	}

	public CallMethodResult callMethod(IOPCOperation context, NodeId objId, NodeId methodId, Variant[] inputargs) {
		CallMethodResult result = null;
		/**
		 * do we have registered a method listener?
		 */
		if (this.methodListeners != null) {
			for (IMethodListener listener : this.methodListeners) {
				result = listener.callMethod(context, objId, methodId, inputargs);
				if (result != null) {
					return result;
				}
			}
		}
		if (this.methodListenersById != null && this.methodListenersById.containsKey(methodId)) {
			result = this.methodListenersById.get(methodId).callMethod(context, objId, methodId, inputargs);
		} else if (this.methodListenersByIdId != null && this.methodListenersByIdId.containsKey(objId)) {
			Map<NodeId, IMethodListener> meths = this.methodListenersByIdId.get(objId);
			if (meths != null && meths.containsKey(methodId)) {
				result = meths.get(methodId).callMethod(context, objId, methodId, inputargs);
			} else {
				result = new CallMethodResult();
				result.setStatusCode(new StatusCode(StatusCodes.Bad_NodeIdUnknown));
				Logger.getLogger(getClass().getName()).log(Level.WARNING,
						"No Method for Method-NodeId found! Object-NodeId: " + objId + " Method-NodeId: " + methodId);
			}
		} else {
			result = new CallMethodResult();
			result.setStatusCode(new StatusCode(StatusCodes.Bad_NodeIdUnknown));
			Logger.getLogger(getClass().getName()).log(Level.WARNING, "No Method for Object-NodeId found: " + objId);
		}
		return result;
	}

	public BrowseResult browse(BrowseDescription description, UnsignedInteger maxReferences, ViewDescription view) {
		BrowseResult result = null;
		/**
		 * do we have registered a view listener?
		 */
		if (this.viewListeners != null) {
			for (IViewListener listener : this.viewListeners) {
				result = listener.browse(description, maxReferences, view);
				if (result != null) {
					return result;
				}
			}
		}
		return result;
	}

	public Node getNodeFromUnderlyingSystem(NodeId nodeId) {
		Node node = null;
		/**
		 * do we have registered any view listener?
		 */
		if (this.viewListeners != null) {
			for (IViewListener listener : this.viewListeners) {
				node = listener.getNodeFromUnderlyingSystem(nodeId);
				if (node != null) {
					return node;
				}
			}
		}
		return node;
	}

	public boolean isNodeInUnderlyingSystem(NodeId nodeId) {
		/**
		 * do we have registered any view listener?
		 */
		if (this.viewListeners != null) {
			for (IViewListener listener : this.viewListeners) {
				if (listener.isNodeInUnderlyingSystem(nodeId)) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean addReferences(AddReferencesItem[] items) {
		if (this.manager.getDriverConnection() != null) {
			return this.manager.getDriverConnection().addReferences(items);
		}
		return false;
	}

	public void importModelFile(String path) {
		if (this.manager.getDriverConnection() != null) {
			this.manager.getDriverConnection().importModelFile(path);
		}
	}

	public ComDRV getBaseManager() {
		return this.baseDriver;
	}

	public void setBaseManager(ComDRV manager) {
		this.baseDriver = manager;
	}
}
