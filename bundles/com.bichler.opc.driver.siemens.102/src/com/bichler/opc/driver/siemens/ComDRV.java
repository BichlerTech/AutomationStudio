package com.bichler.opc.driver.siemens;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.DateTime;
import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.StatusCode;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.common.NamespaceTable;
import org.opcfoundation.ua.core.Attributes;
import org.opcfoundation.ua.core.BuildInfo;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.ServerState;
import org.opcfoundation.ua.core.StatusCodes;
import org.opcfoundation.ua.core.TimestampsToReturn;

import com.bichler.opc.comdrv.ComCommunicationStates;
import com.bichler.opc.comdrv.ComDRVManager;
import com.bichler.opc.comdrv.ComResourceManager;
import com.bichler.opc.comdrv.ExtendedComDRV;
import com.bichler.opc.comdrv.importer.Com_CounterConfigGroup;
import com.bichler.opc.comdrv.importer.Com_CounterConfigNode;
import com.bichler.opc.comdrv.importer.Com_DeviceConfigNode;
import com.bichler.opc.comdrv.importer.Com_DeviceConfigNode.DeviceNode;
import com.bichler.opc.comdrv.importer.Com_StartConfigNode;
import com.bichler.opc.comdrv.importer.Com_TriggerDpItem;
import com.bichler.opc.comdrv.utils.ComByteMessage;
import com.bichler.opc.comdrv.utils.ComStatusUtils;
import com.bichler.opc.driver.siemens.communication.SiemensDataErrorCode;
import com.bichler.opc.driver.siemens.communication.SiemensErrorCode;
import com.bichler.opc.driver.siemens.communication.SiemensPDUType;
import com.bichler.opc.driver.siemens.communication.SiemensReadDataPart;
import com.bichler.opc.driver.siemens.communication.SiemensReadParamPart;
import com.bichler.opc.driver.siemens.communication.SiemensReadResponseDataPart;
import com.bichler.opc.driver.siemens.communication.SiemensTPDUHeader;
import com.bichler.opc.driver.siemens.dp.SiemensDPItem;
import com.bichler.opc.driver.siemens.dp.SiemensDPPackages;
import com.bichler.opc.driver.siemens.dp.SiemensPackageList;
import com.bichler.opc.driver.siemens.dp.SiemensStringItem;
import com.bichler.opc.driver.siemens.transform.SIEMENS_MAPPING_TYPE;
import com.bichler.opc.driver.siemens.transform.SiemensTransformFactory;

public class ComDRV extends ExtendedComDRV {
	private final Logger logger = Logger.getLogger(getClass().getName());
	// version identifier for that siemens communication driver
	public static final String VERSIONID = "1.0.1";
	// bundle id for that siemens communication driver
	public static final String BUNDLEID = "com.hbsoft.driver.siemens";
	// manager which holds all required resources
	private SiemensResourceManager manager = null;
	private boolean deviceRemoval = false;
	private boolean isfirstrun = true;
	private ComStatusUtils utils = new ComStatusUtils();
	private boolean isstartup;
	private List<String> histories = new ArrayList<>();

	/**
	 * set driver information to actual server only valid if we have one plc driver
	 * connected to a server
	 */
	private void fillServerinfo() {
		DateTime builddate = null;
		String revision = "0.5";
		String manufacturer = "Bichler Technologies GmbH";
		String productname = "Siemens S7 OPC UA Server";
		String productUri = "";
		String softwareversion = "2.0";
		String driverinfopath = this.drvManager.getResourceManager().getRuntimeDir() + "drivers" + File.separator
				+ "siemens" + File.separator + "drv.info";
		File drvinfo = new File(driverinfopath);
		String line = "";
		if (drvinfo.exists()) {
			FileReader freader = null;
			BufferedReader reader = null;
			try {
				freader = new FileReader(drvinfo);
				reader = new BufferedReader(freader);
				while ((line = reader.readLine()) != null) {
					if (line.startsWith("##")) {
						continue;
					}
					if ("#drvweb".compareTo(line) == 0) {
						productUri = reader.readLine();
					} else if ("#drvversion".compareTo(line) == 0) {
						softwareversion = reader.readLine();
					} else if ("#drvbuilddate".compareTo(line) == 0) {
						try {
							builddate = DateTime.parseDateTime(reader.readLine());
						} catch (ParseException | IllegalArgumentException e) {
							logger.log(Level.SEVERE, e.getMessage());
						}
					} else if ("#drvrevision".compareTo(line) == 0) {
						revision = reader.readLine();
					}
				}
			} catch (IOException e) {
				logger.log(Level.SEVERE, e.getMessage());
			} finally {
				if (freader != null)
					try {
						freader.close();
					} catch (IOException e) {
						logger.log(Level.SEVERE, e.getMessage());
					}
				if (reader != null)
					try {
						reader.close();
					} catch (IOException e2) {
						logger.log(Level.SEVERE, e2.getMessage());
					}
			}
		}
		// fill build info
		BuildInfo info = new BuildInfo();
		info.setBuildDate(builddate);
		info.setBuildNumber(revision);
		info.setManufacturerName(manufacturer);
		info.setProductName(productname);
		info.setProductUri(productUri);
		info.setSoftwareVersion(softwareversion);
		drvManager.writeFromDriver(
				new NodeId[] { Identifiers.Server_ServerStatus_BuildInfo,
						Identifiers.Server_ServerStatus_BuildInfo_BuildDate,
						Identifiers.Server_ServerStatus_BuildInfo_BuildNumber,
						Identifiers.Server_ServerStatus_BuildInfo_ManufacturerName,
						Identifiers.Server_ServerStatus_BuildInfo_ProductName,
						Identifiers.Server_ServerStatus_BuildInfo_ProductUri,
						Identifiers.Server_ServerStatus_BuildInfo_SoftwareVersion },
				new UnsignedInteger[] { Attributes.Value, Attributes.Value, Attributes.Value, Attributes.Value,
						Attributes.Value, Attributes.Value, Attributes.Value },
				new String[] { null, null, null, null, null, null, null },
				new DataValue[] { new DataValue(new Variant(info)), new DataValue(new Variant(builddate)),
						new DataValue(new Variant(revision)), new DataValue(new Variant(manufacturer)),
						new DataValue(new Variant(productname)), new DataValue(new Variant(productUri)),
						new DataValue(new Variant(softwareversion)) },
				new Long[] { (long) this.drvId, (long) this.drvId, (long) this.drvId, (long) this.drvId,
						(long) this.drvId, (long) this.drvId, (long) this.drvId });
		// fill in start time
		drvManager.writeFromDriver(new NodeId[] { Identifiers.Server_ServerStatus_StartTime },
				new UnsignedInteger[] { Attributes.Value }, new String[] { null },
				new DataValue[] { new DataValue(new Variant(new DateTime())) }, new Long[] { (long) this.drvId });
	}

	/**
	 * at the moment only isotcp connection is defined
	 */
	@Override
	public void setDeviceAddress(String address) {
		String[] content = address.split(";");
		// minimum the communication type is already
		if (content != null && content.length > 1) {
			// get the communication type to the plc
			try {
				if (content[0] != null) {
					SiemensDeviceType type = SiemensDeviceType.valueOf(content[0]);
					switch (type) {
					case ISOTCPS7_300:
						if (content.length >= 5) {
							int rack = Integer.parseInt(content[3]);
							int slot = Integer.parseInt(content[4]);
							SiemensTCPISODevice device = new SiemensTCPISODevice();
							device.setDrvId(drvId);
							device.setManager(this.manager.getManager());
							device.setHost(content[1]);
							device.setPort(Integer.parseInt(content[2]));
							device.setSlot(slot);
							device.setRack(rack);
							device.setUtils(utils);
							this.device = device;
							this.manager.setDevice(device);
						}
						break;
					case ISOTCPS7_1500:
						if (content.length >= 5) {
							int rack = Integer.parseInt(content[3]);
							int slot = Integer.parseInt(content[4]);
							SiemensTCPISODevice device = new SiemensTCPISODevice();
							device.setDrvId(drvId);
							device.setManager(this.manager.getManager());
							device.setHost(content[1]);
							device.setPort(Integer.parseInt(content[2]));
							device.setSlot(slot);
							device.setRack(rack);
							device.setUtils(utils);
							this.device = device;
							this.manager.setDevice(device);
						}
						break;
					default:
					}
				}
			} catch (IllegalArgumentException ex) {
				logger.log(Level.SEVERE, ex.getMessage());
			}
		}
	}

	@Override
	public void setDrvProperties(String drvProperties) {
		if (drvProperties != null && !drvProperties.isEmpty()) {
			String[] props = drvProperties.split("=");
			if (props != null && props.length >= 2 && this.getDevice() != null) {
				try {
					this.getDevice().setMaxByteCount(Integer.parseInt(props[1]));
				} catch (NumberFormatException ex) {
					logger.log(Level.SEVERE, ex.getMessage());
				}
			}
		}
		super.setDrvProperties(drvProperties);
	}

	/**
	 * default constructor, create siemens resource manager, driver resource manager
	 * and and driver manager
	 */
	public ComDRV() {
		super();
		this.manager = new SiemensResourceManager();
		this.manager.setManager(ComDRVManager.getDRVManager().getResourceManager());
	}

	/**
	 * first connect to s7 plc and return true if connection could be established
	 * otherwise false
	 */
	@Override
	public boolean start() {
		// start driver connetion
		if (this.device != null) {
			this.device.connect();
			// send always true back, to go into the dostartup method
			// successfully
			return true;
		}
		return false;
	}

	@Override
	public void initialize() {
		// now set all listeners
		this.initializeHandler();
		this.initializePLCAddress();
		this.fillServerinfo();
	}

	private void initializePLCAddress() {
		File conffile = new File(ComResourceManager.CONFIGPATH);
		if (conffile.exists()) {
			BufferedReader reader = null;
			try {
				FileReader freader = new FileReader(conffile);
				reader = new BufferedReader(freader);
				String line = "";
				while ((line = reader.readLine()) != null) {
					if (line.startsWith("##")) {
						continue;
					}
					if ("#ethIPPLC".compareTo(line) == 0) {
						line = reader.readLine();
						this.device.setDeviceAddress(line);
					}
				}
			} catch (IOException e) {
				logger.log(Level.SEVERE, e.getMessage());
			} finally {
				if (reader != null) {
					try {
						reader.close();
					} catch (IOException e) {
						logger.log(Level.SEVERE, e.getMessage());
					}
				}
			}
		}
	}

	/**
	 * 
	 */
	private void initializeHandler() {
		// don't send any read requests to the device
		SiemensWriteHandler write = new SiemensWriteHandler();
		write.setManager(manager);
		ComDRVManager.getDRVManager().addWriteListener((long) this.drvId, write);
	}

	@Override
	public SiemensDevice getDevice() {
		return (SiemensDevice) this.device;
	}

	private boolean validateMapping(SiemensDPItem dp) {
		SiemensTransformFactory factory = new SiemensTransformFactory();
		DataValue response = this.drvManager.readFromDriver(dp.getNodeId(), Attributes.Value, null, null, (long) drvId,
				0.0, TimestampsToReturn.Both);
		boolean valid = false;
		/* check the namespace object */
		if (response != null && response.getValue() != null && response.getValue().getValue() != null) {
			// verify array length
			if (!response.getValue().isArray() && dp.getMapping() == SIEMENS_MAPPING_TYPE.SCALAR_ARRAY
					&& dp.getClass() == SiemensStringItem.class
					&& response.getValue().getCompositeClass().equals(String.class)) {
				valid = true;
			} else if (response.getValue().isArray() && dp.getMapping() == SIEMENS_MAPPING_TYPE.ARRAY_ARRAY) {
				if (((Object[]) response.getValue().getValue()).length == dp.getReadArraylength(-2))
					valid = true;
				else {
					if (((Object[]) response.getValue().getValue()).length > dp.getReadArraylength(-2)) {
						logger.log(Level.SEVERE, "Intern array is larger than plc array! Node: " + dp.getNodeId() + " "
								+ dp.getSymbolname());
					} else {
						logger.log(Level.SEVERE, "Intern array is smaller than plc array! Node: " + dp.getNodeId() + " "
								+ dp.getSymbolname());
					}
				}
			} else if (response.getValue().isArray() && (dp.getMapping() == SIEMENS_MAPPING_TYPE.ALARM)) {
				if (((Object[]) response.getValue().getValue()).length
						// * dp.getLength() == dp.getArraylength(-2))
						== dp.getReadArraylength(-2) * dp.getLengthInBit())
					valid = true;
			} else if (!response.getValue().isArray() && dp.getMapping() == SIEMENS_MAPPING_TYPE.SCALAR) {
				valid = true;
			} else {
				logger.log(Level.SEVERE, "Node not added to schedule, because mapping is invalid! Nodeid: "
						+ dp.getNodeId() + " SymbolName: " + dp.getSymbolname());
			}
		} else if (response != null && (response.getStatusCode().getValue().equals(StatusCodes.Bad_NodeIdInvalid)
				|| response.getStatusCode().getValue().equals(StatusCodes.Bad_NodeIdUnknown))) {
			logger.log(Level.SEVERE, "Node not added to schedule, because node did not exist in address space! Nodeid: "
					+ dp.getNodeId() + " SymbolName: " + dp.getSymbolname());
			return false;
		} else {
			logger.log(Level.SEVERE, "Node not added to schedule, because opc ua value = null! Nodeid: "
					+ dp.getNodeId() + " SymbolName: " + dp.getSymbolname());
			return false;
		}
		if (valid) {
			factory.createTransform(dp, response.getValue().getCompositeClass());
		}
		return valid;
	}

	private void createTriggerPackages() {
		List<String> trigs2remove = new ArrayList<>();
		// remove all not activated triggers
		for (Com_TriggerDpItem trigg : manager.getTriggerList().values()) {
			if (!trigg.isActive())
				trigs2remove.add(trigg.getTriggerID());
		}
		for (String trig : trigs2remove) {
			manager.getTriggerList().remove(trig);
		}
		List<NodeId> possibleTNIDs = new ArrayList<>();
		// add all possible trigger node ids
		for (Com_TriggerDpItem dp : manager.getTriggerList().values()) {
			if (!possibleTNIDs.contains(dp.getNodeId())) {
				possibleTNIDs.add(dp.getNodeId());
				this.drvManager.getResourceManager().getDriverConnection().setDriverWriteConnected(dp.getNodeId(), true,
						ComDRVManager.SYNCWRITE, (long) this.drvId);
			}
		}
		manager.setPossibleTriggerNIDs(possibleTNIDs);
		Com_TriggerDpItem tn = null;
		// iterate throug all
		for (SiemensDPItem item : manager.getDpItems().values()) {
			String nid = item.getTriggerNode();
			if (nid != null) {
				tn = manager.getTriggerList().get(nid);
				if (tn != null) {
					// tn.getNodesToRead().add(item.getNodeId());
				}
				// List<SiemensDPItem> list =
				// manager.getTriggeritems().get(tn.getNodeId());
				// if(list == null) {
				// list = new ArrayList<SiemensDPItem>();
				// manager.getTriggeritems().put(tn.getNodeId(), list);
				// }
				//
				// list.add(item);
			}
		}
		List<SiemensDPPackages> packages = null;
		SiemensDPPackages pack = new SiemensDPPackages();
		int responsebytecount = 0;
		int requestbytecount = 0;
		// add all trigger asyncwrite connected
		for (Com_TriggerDpItem trigg : manager.getTriggerList().values()) {
			// create readable packages
			this.manager.getManager().getDriverConnection().setDriverWriteConnected(trigg.getNodeId(), true,
					ComDRVManager.SYNCWRITE, (long) this.drvId);
			for (NodeId id : trigg.getNodesToRead()) {
				SiemensDPItem dp = manager.getDpItems().get(id);
				if (dp != null) {
					dp.setMaxReadByteCount(getDevice().getMaxByteCount());
					dp.setMaxWriteByteCount(getDevice().getMaxByteCount() - 12);
					if (this.manager.getTriggerpackages() == null) {
						this.manager.setTriggerpackages(new HashMap<String, List<SiemensDPPackages>>());
					}
					// packages for fast read
					packages = this.manager.getTriggerpackages().get(dp.getTriggerNode());
					if (packages == null) {
						pack = new SiemensDPPackages();
						packages = new ArrayList<>();
						this.manager.getTriggerpackages().put(dp.getTriggerNode(), packages);
						packages.add(pack);
					}
					DataValue response = this.drvManager.readFromDriver(dp.getNodeId(), Attributes.DisplayName, null,
							null, (long) drvId, 0.0, TimestampsToReturn.Both);
					if (response != null && response.getValue() != null && response.getValue().getValue() != null) {
						dp.setDisplayName(((LocalizedText) response.getValue().getValue()).getText());
					}
					// validate if siemens - opc ua datamapping is valid for
					// node
					if (this.validateMapping(dp)) {
						int overhead = 4;
						int resp = (dp.getLength() * dp.getReadArraylength(-1)) + overhead;
						// does resquest fit?
						if (responsebytecount + resp >= getDevice().getMaxByteCount() && !pack.getDps().isEmpty()
								|| requestbytecount + 12 >= getDevice().getMaxByteCount()) {
							pack.createReadMessage((SiemensTCPISODevice) this.getDevice());
							pack = new SiemensDPPackages();
							packages.add(pack);
							responsebytecount = 0;
							requestbytecount = 0;
						}
						pack.addDP(dp);
						responsebytecount += resp;
						requestbytecount += 12;
						if (resp >= getDevice().getMaxByteCount()) {
							pack.setSingleDP(true);
						}
						// add the actual created package
						// always create read message
						pack.createReadMessage((SiemensTCPISODevice) this.getDevice());
						// set datapoint write synchron
						this.manager.getManager().getDriverConnection().setDriverWriteConnected(dp.getNodeId(), true,
								ComDRVManager.SYNCWRITE, (long) this.drvId);
						// now set status code uncertain
						// TODO set uncertain in an other state
						// response.setSourceTimestamp(new DateTime());
						// response.setStatusCode(StatusCodes.Bad_WaitingForInitialData);
						// this.drvManager.writeFromDriver(
						// new NodeId[] { dp.getNodeId() },
						// new UnsignedInteger[] { Attributes.Value }, null,
						// new DataValue[] { response },
						// new Long[] { (long) this.drvId });
					} else {
						// do not log message, we logged a detailed message
						// earlier
						// this.drvManager
						// .getResourceManager()
						// .getLogger()
						// .log(Level.SEVERE, "OPC UA -- Siemens datamapping not right for
						// node: "
						// + dp.getNodeId()
						// + ": "
						// + dp.getDisplayName());
					}
				} else {
					logger.log(Level.SEVERE,
							"Datapoint to trigger not found in datapoint mapping list, or has a wrong mapping! Datapoint: "
									+ id);
				}
			}
		}
	}

	/**
	 * creates the schedule list with all to schedule nodes sorted by interval, we
	 * also pack the datapoints into packagemessage, so we can request more than one
	 * datapoint at once
	 */
	private void createScheduleList(Map<NodeId, SiemensDPItem> map, List<SiemensDPItem> dps, NamespaceTable uris) {
		if (map != null) {
			// first create scedule list for all cyclic dps
			// SiemensImporter importer = new SiemensImporter();
			// InputStream input;
			List<SiemensDPItem> nodes = null;
			SiemensPackageList<SiemensDPPackages> packages = null;
			SiemensDPPackages pack = new SiemensDPPackages();
			int responsebytecount = 0;
			int requestbytecount = 0;
			if (dps != null) {
				if (this.manager.getNodesByInterval() == null) {
					this.manager.setNodesByInterval(new HashMap<Long, List<SiemensDPItem>>());
				}
				if (this.manager.getPackagesByInterval() == null) {
					this.manager.setPackagesByInterval(new HashMap<Long, SiemensPackageList<SiemensDPPackages>>());
				}
				for (SiemensDPItem dp : dps) {
					// update watchdog
					if (lastwatchdogwrite + (1000L * 1000000L * 10L) < System.nanoTime()) {
						// lastwatchdogwrite = System.currentTimeMillis();
						lastwatchdogwrite = System.nanoTime();
						/**
						 * now we finished run - so update watchdog state
						 */
						utils.updateWatchdog();
//            utils.updateServerWatchdog();
					}
					// set default read request to datapoint
					// now add dp to triggernode if required
					if (dp.getTriggerNode() != null && !dp.getTriggerNode().isEmpty()) {
						if (manager.getTriggerList().containsKey(dp.getTriggerNode())) {
							// this difficult line gets the triggerlist from
							// manager, then we get the triggeritem by
							// triggernodeid of datapoint, at least we add
							// the actual datapoint to that trigger node
							manager.getTriggerList().get(dp.getTriggerNode()).getNodesToRead().add(dp.getNodeId());
						} else {
							logger.log(Level.SEVERE, "Triggernode not found! NodeId: " + dp.getNodeId());
						}
					}
					/**
					 * add dp to schedule list
					 */
					if (dp.isActive()) {
						// validate if siemens - opc ua datamapping is valid for
						// node
						DataValue response = this.drvManager.readFromDriver(dp.getNodeId(), Attributes.DisplayName,
								null, null, (long) drvId, 0.0, TimestampsToReturn.Both);
						if (response != null && response.getValue() != null && response.getValue().getValue() != null) {
							dp.setDisplayName(((LocalizedText) response.getValue().getValue()).getText());
						}
						if (this.validateMapping(dp)) {
							map.put(dp.getNodeId(), dp);
							// set max byte count to read
							dp.setMaxReadByteCount(getDevice().getMaxByteCount());
							dp.setMaxWriteByteCount(getDevice().getMaxByteCount() - 12);
							nodes = this.manager.getNodesByInterval().get(dp.getCycletime());
							if (nodes == null) {
								nodes = new ArrayList<>();
								this.manager.getNodesByInterval().put(dp.getCycletime(), nodes);
							}
							// packages for fast read
							packages = this.manager.getPackagesByInterval().get(dp.getCycletime());
							if (packages == null) {
								pack = new SiemensDPPackages();
								packages = new SiemensPackageList<>();
								this.manager.getPackagesByInterval().put(new Long(dp.getCycletime()), packages);
								packages.add(pack);
							}
							pack = packages.get(packages.size() - 1);
							pack.createReadMessage((SiemensTCPISODevice) this.getDevice());
							requestbytecount = pack.getReadyReadMessage().length;
							responsebytecount = pack.getResponseCount();
							// validate if siemens - opc ua datamapping is valid
							// for
							// node
							int overhead = 4;
							int resp = (dp.getLength() * dp.getReadArraylength(-1)) + overhead;
							// does resquest fit?
							if ((responsebytecount + resp >= getDevice().getMaxByteCount() && !pack.getDps().isEmpty())
									|| requestbytecount + 12 >= getDevice().getMaxByteCount()) {
								pack = new SiemensDPPackages();
								packages.add(pack);
							}
							pack.addDP(dp);
							if (resp >= getDevice().getMaxByteCount()) {
								pack.setSingleDP(true);
							}
							nodes.add(dp);
							// set datapoint write synchron
							this.manager.getManager().getDriverConnection().setDriverWriteConnected(dp.getNodeId(),
									true, ComDRVManager.SYNCWRITE, (long) this.drvId);
							// now set status code uncertain
							response = this.drvManager.readFromDriver(dp.getNodeId(), Attributes.Value, null, null,
									(long) drvId, 0.0, TimestampsToReturn.Both);
							if (response != null) {
								response.setSourceTimestamp(new DateTime());
								response.setStatusCode(StatusCodes.Bad_WaitingForInitialData);
								this.drvManager.writeFromDriver(new NodeId[] { dp.getNodeId() },
										new UnsignedInteger[] { Attributes.Value }, null, new DataValue[] { response },
										new Long[] { (long) this.drvId });
							}
						} else {
							response = this.drvManager.readFromDriver(dp.getNodeId(), Attributes.Value, null, null,
									(long) drvId, 0.0, TimestampsToReturn.Both);
							if (response != null) {
								response.setSourceTimestamp(new DateTime());
								response.setStatusCode(StatusCodes.Bad_BoundNotSupported);
								this.drvManager.writeFromDriver(new NodeId[] { dp.getNodeId() },
										new UnsignedInteger[] { Attributes.Value }, null, new DataValue[] { response },
										new Long[] { (long) this.drvId });
							}
						}
					}
				}
			}
			// now create read messages for each package
			for (List<SiemensDPPackages> packs : this.manager.getPackagesByInterval().values()) {
				for (SiemensDPPackages p : packs) {
					p.createReadMessage((SiemensTCPISODevice) this.getDevice());
				}
			}
		}
		// remove all data points
		dps.clear();
	}

	@Override
	public void checkCommunication() {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean doStartup() {
		// try to load startup config,
		// try to load device config
		// try to load counter config
		super.doStartup();
		try {
			// update watchdog file
			utils.updateWatchdog();
//      utils.updateServerWatchdog();
			SiemensImporter2 importer = new SiemensImporter2();
			// load all data points to startup device configuration
			File dpfile = new File("drivers/" + this.getDriverName() + "/datapoints.com");
			List<SiemensDPItem> dps = new ArrayList<>();
			List<SiemensDPItem> predps = new ArrayList<>();
			FileInputStream input = null;
			List<SiemensConsumptionItem> cons = null;
			// meterid mapping
			Map<Object, SiemensDPItem> meteridmapping = new HashMap<>();
			if (dpfile.exists()) {
				// load all datapoints for predefined datapoint check
				if (this.drvManager.getResourceManager().isActivatedebug()
						&& (this.drvManager.getResourceManager().getDebug()
								& this.drvManager.getResourceManager().DEBUG_STARTUP) == this.drvManager
										.getResourceManager().DEBUG_STARTUP) {
					logger.info("Importing node to driver datapoint mappings started!");
				}
				try {
					input = new FileInputStream(dpfile);
					dps = importer.loadDPs(input, getNamespaceTable(), this.drvManager.getResourceManager());
				} catch (IOException e) {
					logger.log(Level.SEVERE, e.getMessage());
				} finally {
					if (input != null) {
						try {
							input.close();
						} catch (IOException e) {
							logger.log(Level.SEVERE, e.getMessage());
						}
					}
				}
				if (this.drvManager.getResourceManager().isActivatedebug()
						&& (this.drvManager.getResourceManager().getDebug()
								& this.drvManager.getResourceManager().DEBUG_STARTUP) == this.drvManager
										.getResourceManager().DEBUG_STARTUP) {
					logger.info("Importing node to driver datapoint mappings finished!");
				}
				// did we have any startconfignodes to validate ?
				if (getStartConfigNodes() != null && !getStartConfigNodes().isEmpty()) {
					for (Com_StartConfigNode node : getStartConfigNodes()) {
						if (node.getIndex() < 0)
							continue;
						// we can not have index lower than 0
						for (SiemensDPItem dp : dps) {
							if (dp.getNodeId().equals(node.getConfigNodeId())) {
								dp.setMaxReadByteCount(getDevice().getMaxByteCount());
								dp.setMaxWriteByteCount(getDevice().getMaxByteCount() - 12);
								if (this.validateMapping(dp))
									predps.add(dp);
							}
						}
					}
				}
				// did we have any device config to validate ?
				if (getDeviceConfigNode() != null && getDeviceConfigNode().isActive()) {
					for (SiemensDPItem dp : dps) {
						if (dp.getNodeId().equals(getDeviceConfigNode().getConfigNodeId())) {
							dp.setMaxReadByteCount(getDevice().getMaxByteCount());
							dp.setMaxWriteByteCount(getDevice().getMaxByteCount() - 12);
							if (this.validateMapping(dp))
								predps.add(dp);
						}
					}
				}
				if (getCounterConfigNodes() != null && getCounterConfigNodes().size() > 0) {
					Com_CounterConfigGroup counter = getCounterConfigNodes().get(0);
					for (SiemensDPItem dp : dps) {
						if (dp.getNodeId().equals(counter.getConfigNodeId())) {
							dp.setMaxReadByteCount(getDevice().getMaxByteCount());
							dp.setMaxWriteByteCount(getDevice().getMaxByteCount() - 12);
							if (this.validateMapping(dp))
								predps.add(dp);
						}
					}
				}
			}
			// now read all nodes and send value to opc ua server
			// TODO check if we have a communication, if not we will wait until
			// we get a
			// communication
			if (!predps.isEmpty()) {
				// TODO log message that we are in starting mode
				while (!this.doWorkStartup(predps)) {
					// try to reconnect to
					try {
						Thread.sleep(device.getReconnectTimeout());
					} catch (InterruptedException e) {
						logger.log(Level.SEVERE, e.getMessage());
					}
				}
				// return false;
			}
			// now we should have the correct values in the opc ua address space
			List<NodeId> nodes = new ArrayList<>();
			DataValue response = null;
			for (Com_StartConfigNode node : getStartConfigNodes()) {
				response = this.drvManager.readFromDriver(node.getConfigNodeId(), Attributes.Value, null, null,
						(long) drvId, 0.0, TimestampsToReturn.Both);
				if (response != null && response.getValue() != null && response.getValue().getValue() != null) {
					Object[] obj = (Object[]) response.getValue().getValue();
					if (obj[node.getIndex()].toString().compareTo(node.getValue()) == 0) {
						if (this.drvManager.getResourceManager().isActivatedebug()
								&& (this.drvManager.getResourceManager().getDebug()
										& this.drvManager.getResourceManager().DEBUG_STARTUP) == this.drvManager
												.getResourceManager().DEBUG_STARTUP) {
							logger.info("Device to activate in address space found! Nodeid: "
									+ node.getDeviceNodeId().toString());
						}
					} else {
						nodes.add(node.getDeviceNodeId());
						if (this.drvManager.getResourceManager().isActivatedebug()
								&& (this.drvManager.getResourceManager().getDebug()
										& this.drvManager.getResourceManager().DEBUG_STARTUP) == this.drvManager
												.getResourceManager().DEBUG_STARTUP) {
							logger.info("Device to remove from address space found! Nodeid: "
									+ node.getDeviceNodeId().toString());
						}
					}
				}
			}
			// now we should have the correct values in the opc ua address space
			// to maintenance devices
			// we use this only the device extended is activated
			if (deviceRemoval) {
				if (getDeviceConfigNode() != null) {
					Com_DeviceConfigNode dev = getDeviceConfigNode();
					response = this.drvManager.readFromDriver(dev.getConfigNodeId(), Attributes.Value, null, null,
							(long) drvId, 0.0, TimestampsToReturn.Both);
					if (response != null && response.getValue() != null && response.getValue().getValue() != null
							&& response.getValue().getValue() instanceof Object[]) {
						Object[] obj = (Object[]) response.getValue().getValue();
						List<NodeId> ids = new ArrayList<>();
						// first set all nodes visible = false
						for (Object o : obj) {
							if (o instanceof Integer) {
								if (((Integer) o) == 0)
									break;
								int i = (Integer) o;
								DeviceNode dn = dev.getDevices().remove(i);
								if (dn != null) {
									ids.add(dn.getDeviceNodeId());
								}
							}
							// dev.getDevices()
						}
						// set device false
						drvManager.setNodeVisible(ids.toArray(new NodeId[ids.size()]), true);
					}
					// now add all nodes to delete
					for (DeviceNode devn : getDeviceConfigNode().getDevices().values()) {
						nodes.add(devn.getDeviceNodeId());
					}
				}
			}
			// try to add additional model files per device
			if (getDeviceConfigNode() != null) {
				Com_DeviceConfigNode dev = getDeviceConfigNode();
				response = this.drvManager.readFromDriver(dev.getConfigNodeId(), Attributes.Value, null, null,
						(long) drvId, 0.0, TimestampsToReturn.Both);
				if (response != null && response.getValue() != null && response.getValue().getValue() != null
						&& response.getValue().getValue() instanceof Object[]) {
					Object[] obj = (Object[]) response.getValue().getValue();
					for (Object o : obj) {
						if (o instanceof Integer) {
							if (((Integer) o) == 0)
								break;
							int i = (Integer) o;
							DeviceNode dn = dev.getDevices().get(i);
							if (dn != null) {
								File mod = new File("drivers/" + this.getDriverName() + "/devices/" + dn.getFilename());
								if (mod.exists()) {
									// update watchdog file
									utils.updateWatchdog();
									drvManager.importModelFile(mod.getPath());
									// now we neet to update namespacetable
									this.renewNamespaceTable();
								} else {
								}
								// import device specific data mapping
								File map = new File(
										"drivers/" + this.getDriverName() + "/devices/" + dn.getMappingFile());
								if (map.exists()) {
									// update watchdog file
									utils.updateWatchdog();
									input = new FileInputStream(map);
									dps.addAll(importer.loadDPs(input, getNamespaceTable(),
											this.drvManager.getResourceManager()));
								}
							}
						}
					}
					// add also device mapping for addon, group, meter
					File dmappingfile = new File("drivers/" + this.getDriverName() + "/devicemapping.com");
					if (dmappingfile.exists()) {
						input = new FileInputStream(dmappingfile);
						dps.addAll(importer.loadDeviceMapping(input, getNamespaceTable(), obj, meteridmapping));
						try {
							input.close();
						} catch (IOException e) {
							logger.log(Level.SEVERE, e.getMessage());
						}
					}
				}
			}
			// also load zaehler mapping
			dpfile = new File("drivers/" + this.getDriverName() + "/consumptionconfiguration.com");
			if (dpfile.exists()) {
				// load all datapoints for predefined datapoint check
				if (this.drvManager.getResourceManager().isActivatedebug()
						&& (this.drvManager.getResourceManager().getDebug()
								& this.drvManager.getResourceManager().DEBUG_STARTUP) == this.drvManager
										.getResourceManager().DEBUG_STARTUP) {
					logger.info("Importing consumption datapoint mapping started!");
				}
				input = new FileInputStream(dpfile);
				cons = importer.loadConsumption(input, // getNamespaceTable(),
						this.drvManager.getResourceManager());
				try {
					input.close();
				} catch (IOException e) {
					logger.log(Level.SEVERE, e.getMessage());
				}
				if (this.drvManager.getResourceManager().isActivatedebug()
						&& (this.drvManager.getResourceManager().getDebug()
								& this.drvManager.getResourceManager().DEBUG_STARTUP) == this.drvManager
										.getResourceManager().DEBUG_STARTUP) {
					logger.info("Importing consumption datapoint mapping finished!");
				}
			}
			int index = 0;
			// now we should have the correct values in the opc ua address space
			for (Com_CounterConfigGroup group : getCounterConfigNodes()) {
				response = this.drvManager.readFromDriver(group.getConfigNodeId(), Attributes.Value, null, null,
						(long) drvId, 0.0, TimestampsToReturn.Both);
				if (response != null && response.getValue() != null && response.getValue().getValue() != null
						&& response.getValue().getValue() instanceof Object[]) {
					Object[] obj = (Object[]) response.getValue().getValue();
					Object ob = obj[index];
					index++;
					// first cast object to String
					try {
						// create count all counter
						int val = Integer.parseInt(ob.toString());
						for (Com_CounterConfigNode dev : group.getCounterlist()) {
							for (int i = val; i < dev.getCounterlist().size(); i++) {
								nodes.add(dev.getCounterlist().get(i));
							}
						}
					} catch (NumberFormatException ex) {
						logger.log(Level.SEVERE,
								"Can not cast node value to Integer! Nodeid: " + group.getConfigNodeId().toString());
					}
				}
			}
			// now remove all devices which doesn't are configured
			this.removeNotRequiredNodes(nodes);
			manager.setTriggerList(new HashMap<String, Com_TriggerDpItem>());
			manager.setDpItems(new HashMap<NodeId, SiemensDPItem>());
			this.createTriggerList(manager.getTriggerList(), getNamespaceTable());
			if (this.drvManager.getResourceManager().isActivatedebug()
					&& (this.drvManager.getResourceManager().getDebug()
							& this.drvManager.getResourceManager().DEBUG_STARTUP) == this.drvManager
									.getResourceManager().DEBUG_STARTUP) {
				logger.info("Start create schedule list for general dps dp-count: " + dps.size());
			}
			this.createScheduleList(manager.getDpItems(), dps, getNamespaceTable());
			if (this.drvManager.getResourceManager().isActivatedebug()
					&& (this.drvManager.getResourceManager().getDebug()
							& this.drvManager.getResourceManager().DEBUG_STARTUP) == this.drvManager
									.getResourceManager().DEBUG_STARTUP) {
				logger.info("Finished create schedule list for general dps!");
			}
			isstartup = true;
			// now run dowork to read all elements the first time
			doWork();
			isstartup = false;
			// isfirstrun = true;
			// now we should have also the consumption configuration or each
			// device found
			// now add all consumption nodes
			if (cons != null) {
				if (getDeviceConfigNode() != null) {
					Com_DeviceConfigNode dev = getDeviceConfigNode();
					response = this.drvManager.readFromDriver(dev.getConfigNodeId(), Attributes.Value, null, null,
							(long) drvId, 0.0, TimestampsToReturn.Both);
					if (response != null && response.getValue() != null && response.getValue().getValue() != null
							&& response.getValue().getValue() instanceof Object[]) {
						Object[] obj = (Object[]) response.getValue().getValue();
						// add also device mapping for addon, group, meter
						// List<NodeId> ids = new ArrayList<NodeId>();
						// first set all nodes visible = false
						for (Object o : obj) {
							if (o instanceof Integer) {
								if (((Integer) o) == 0)
									break;
								int i = (Integer) o;
								// get meter-id for each type
								SiemensDPItem meterid = meteridmapping.get(i);
								if (meterid == null) {
									continue;
								}
								// read meterid value from intern address space
								response = this.drvManager.readFromDriver(meterid.getNodeId(), Attributes.Value, null,
										null, (long) drvId, 0.0, TimestampsToReturn.Both);
								if (response != null && response.getValue() != null
										&& response.getValue().getValue() != null
										&& response.getValue().getValue() instanceof Object[]) {
									// if response is ok to we found an added
									// device
									Object[] meter = (Object[]) response.getValue().getValue();
									//
									if (meter.length == cons.size()) {
										for (int j = 0; j < meter.length; j++) {
											// create consumption mapping for
											// electrical -> index 0
											if (meter[j] != null && meter[j] instanceof Integer) {
												int m_e = (Integer) meter[j];
												// if meterid greater-equals 0
												// so we
												// have an activated meter found
												if (m_e >= 0) {
													List<SiemensDPItem> els = cons.get(j).getItems();
													// add mapping for each node
													for (SiemensDPItem el : els) {
														if (el.getNodeId() == null) {
															continue;
														}
														SiemensDPItem newitem = el.clone(null);
														newitem.setIndex(
																el.getIndex() + m_e * (float) cons.get(j).getLength());
														newitem.setNodeId(NodeId.parseNodeId(
																"ns=" + meterid.getNodeId().getNamespaceIndex() + ";"
																		+ el.getNodeId().toString()));
														// add displayname
														response = this.drvManager.readFromDriver(newitem.getNodeId(),
																Attributes.DisplayName, null, null, (long) drvId, 0.0,
																TimestampsToReturn.Both);
														if (response != null && response.getValue() != null
																&& response.getValue().getValue() != null) {
															newitem.setDisplayName(
																	((LocalizedText) response.getValue().getValue())
																			.getText());
														}
														dps.add(newitem);
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
				if (this.drvManager.getResourceManager().isActivatedebug()
						&& (this.drvManager.getResourceManager().getDebug()
								& this.drvManager.getResourceManager().DEBUG_STARTUP) == this.drvManager
										.getResourceManager().DEBUG_STARTUP) {
					logger.info("Start create schedule list for consumption dps dp-count: " + dps.size());
				}
				// create additional schedule list
				this.createScheduleList(manager.getDpItems(), dps, getNamespaceTable());
				if (this.drvManager.getResourceManager().isActivatedebug()
						&& (this.drvManager.getResourceManager().getDebug()
								& this.drvManager.getResourceManager().DEBUG_STARTUP) == this.drvManager
										.getResourceManager().DEBUG_STARTUP) {
					logger.info("Finished create schedule list for general dps!");
				}
			}
			this.createTriggerPackages();
		} catch (FileNotFoundException e) {
			if (this.drvManager.getResourceManager().isActivatedebug()
					&& (this.drvManager.getResourceManager().getDebug()
							& this.drvManager.getResourceManager().DEBUG_STARTUP) == this.drvManager
									.getResourceManager().DEBUG_STARTUP) {
				logger.info("No startconfiguration found, so all devices are published!");
			}
		}
		return true;
	}

	/**
	 * // create read request and waits for each response, if we had timeout so //
	 * return false
	 * 
	 * @param preds
	 * @return
	 */
	private boolean doWorkStartup(List<SiemensDPItem> preds) {
		// first check connection
		SiemensTCPISODevice sdevice = (SiemensTCPISODevice) device;
		if (device.getDeviceState() == ComCommunicationStates.CLOSED) {
			// device state is closed, so try to connect to plc
			device.connect();
			// if we couldn't connect, so send false back.
			if (device.getDeviceState() == ComCommunicationStates.CLOSED) {
				utils.writeLEDStatus(ComStatusUtils.STARTING);
				utils.updateWatchdog();
//        utils.updateServerWatchdog();
				return false;
			}
		}
		for (SiemensDPItem pred : preds) {
			SiemensDPPackages pack = new SiemensDPPackages();
			pack.addDP(pred);
			pack.createReadMessage(sdevice);
			List<SiemensTPDUHeader> tpdulist = sdevice.readPackage(pack);
			// if we have an
			if (tpdulist != null && tpdulist.size() > 0) {
				// we got a response
				if (pack.getMessageCount() > 1) {
					// response is only a big array
					ComByteMessage ms = new ComByteMessage();
					for (SiemensTPDUHeader tpdu : tpdulist) {
						if (tpdu != null && tpdu.getPdu() != null) {
							// we only accept responses at that
							// time
							if (tpdu.getPdu().getType() != SiemensPDUType.RESPONSE)
								continue;
							if (tpdu.getPdu().getErrorcode() != 0) {
								logger.log(Level.SEVERE, "Error code from message: " + SiemensErrorCode
										.strerror(SiemensErrorCode.fromValue(tpdu.getPdu().getErrorcode())));
								return false;
							}
							if (tpdu.getPdu().getParam() instanceof SiemensReadParamPart) {
								// get all dataparts
								// from response
								// int index = 0;
								for (SiemensReadDataPart d : ((SiemensReadParamPart) tpdu.getPdu().getParam())
										.getDataParts()) {
									// is it a response?
									if (d instanceof SiemensReadResponseDataPart) {
										if (((SiemensReadResponseDataPart) d)
												.getDataErrorCode() != SiemensDataErrorCode.NO_ERROR.getCode()) {
											logger.log(Level.SEVERE, "No Data available on address for config node: "
													+ pack.getDps().get(0).getNodeId());
											// StatusCode[] statuscode =
											drvManager.writeFromDriver(
													new NodeId[] { pack.getDps().get(0).getNodeId() },
													new UnsignedInteger[] { Attributes.Value }, null,
													new DataValue[] { new DataValue(
															new StatusCode(StatusCodes.Bad_NoDataAvailable)) },
													new Long[] { (long) this.drvId });
											continue;
										}
										ms.addBuffer(((SiemensReadResponseDataPart) d).getData());
									}
								}
							}
						}
					}
					SiemensDPItem dpoint = pack.getDps().get(0);
					DataValue dv = dpoint.drv2Prog(ms.getBuffer());
					StatusCode[] statuscode = drvManager.writeFromDriver(new NodeId[] { dpoint.getNodeId() },
							new UnsignedInteger[] { Attributes.Value }, null, new DataValue[] { dv },
							new Long[] { (long) this.drvId });
					if (statuscode == null || statuscode.length == 0) {
						logger.log(Level.SEVERE, "Can not set value to opc ua node: " + dpoint.getNodeId()
								+ " couldn't get error code from internal space! ");
					} else if (statuscode[0].isBad()) {
						logger.log(Level.SEVERE, "Can not set value to opc ua node: " + dpoint.getNodeId() + " code: "
								+ statuscode[0].getDescription());
					}
				} else {
					// we have datapoints which are not divided into
					// many messages
					for (SiemensTPDUHeader tpdu : tpdulist) {
						if (tpdu != null && tpdu.getPdu() != null) {
							// we only accept responses at that
							// time
							if (tpdu.getPdu().getType() != SiemensPDUType.RESPONSE)
								continue;
							if (tpdu.getPdu().getErrorcode() != 0) {
								logger.log(Level.SEVERE, "Error code from message: " + SiemensErrorCode
										.strerror(SiemensErrorCode.fromValue(tpdu.getPdu().getErrorcode())));
								return false;
							}
							if (tpdu.getPdu().getParam() instanceof SiemensReadParamPart) {
								// get all dataparts
								// from response
								int index = 0;
								List<NodeId> ids = new ArrayList<>();
								List<DataValue> Vals = new ArrayList<>();
								List<Long> drvids = new ArrayList<>();
								List<UnsignedInteger> attrs = new ArrayList<>();
								List<String> indr = new ArrayList<>();
								for (SiemensReadDataPart d : ((SiemensReadParamPart) tpdu.getPdu().getParam())
										.getDataParts()) {
									// is it a response?
									if (d instanceof SiemensReadResponseDataPart) {
										SiemensDPItem dpoint = pack.getDps().get(index);
										index++;
										byte[] data = ((SiemensReadResponseDataPart) d).getData();
										// verify data length
										if (data.length == 0) {
											logger.log(Level.SEVERE, "Wrong data length in response for node: "
													+ dpoint.getDisplayName());
										} else {
											DataValue dv = dpoint.drv2Prog(data);
											if (manager.getTriggerList().containsKey(dpoint.getNodeId())) {
												drvids.add((long) -1);
											} else {
												drvids.add((long) drvId);
											}
											ids.add(dpoint.getNodeId());
											Vals.add(dv);
											attrs.add(Attributes.Value);
											indr.add(null);
										}
									}
								}
								if (Vals.size() > 0) {
									StatusCode[] statuscode = drvManager.writeFromDriver(
											ids.toArray(new NodeId[ids.size()]),
											attrs.toArray(new UnsignedInteger[attrs.size()]),
											indr.toArray(new String[indr.size()]),
											Vals.toArray(new DataValue[Vals.size()]),
											drvids.toArray(new Long[drvids.size()]));
									if (statuscode == null || statuscode.length == 0) {
										StringBuffer str = new StringBuffer();
										for (NodeId i : ids) {
											str.append(i.toString() + " ");
										}
										logger.log(Level.SEVERE, "Can not set value to opc ua node: " + str.toString()
												+ " couldn't get error code from internal space! ");
									} else {
										for (int i = 0; i < statuscode.length; i++) {
											if (statuscode[i].isBad()) {
												logger.log(Level.SEVERE,
														"can not set value to opc ua node: "
																+ pack.getDps().get(i).getNodeId() + " code: "
																+ statuscode[i].getDescription());
											}
										}
									}
								}
							}
						}
					}
				}
			} else {
				logger.log(Level.SEVERE, "Connect to plc close during initialization, try it againt: PLC: "
						+ ((SiemensTCPISODevice) device).getHost());
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean doWork() {
		int errorcount = 0;
		int successcount = 0;
		long intervalstart = System.nanoTime();
		boolean worked = false;
		long packageinterval = 0;
		SiemensTCPISODevice device = (SiemensTCPISODevice) this.getDevice();
		// this watchdog update musst be done every 10 seconds
		// if(lastwatchdogwrite + (1000 * 10) > System.currentTimeMillis()) {
		if (lastwatchdogwrite + (1000L * 1000000L * 10L) < System.nanoTime()) {
			// lastwatchdogwrite = System.currentTimeMillis();
			lastwatchdogwrite = System.nanoTime();
			/**
			 * now we finished run - so update watchdog state
			 */
			utils.updateWatchdog();
//      utils.updateServerWatchdog();
		}
		// --- first check if connection is ok, otherwise return false back
		if (device.getDeviceState() == ComCommunicationStates.CLOSED) {
			return false;
		}
		// int readbytecount = 0;
		if (this.manager != null) {
			// synchronized (this.manager.getTriggers2read()) {
			SiemensDPPackages tpack = null;
			while ((tpack = this.manager.getTriggers2read().poll()) != null) {
				// for (SiemensDPPackages pack :
				// this.manager.getTriggers2read()) {
				// get starttime of package read
				// packageinterval = System.currentTimeMillis();
				packageinterval = System.nanoTime();
				// now read value from driver
				List<SiemensTPDUHeader> tpdulist = device.readPackage(tpack);
				if (tpdulist != null && !tpdulist.isEmpty()) {
					// we got a response
					if (tpack.isSingleDP()) {
						// response is only a big array
						ComByteMessage ms = new ComByteMessage();
						for (SiemensTPDUHeader tpdu : tpdulist) {
							if (tpdu != null && tpdu.getPdu() != null) {
								// we only accept responses at that
								// time
								if (tpdu.getPdu().getType() != SiemensPDUType.RESPONSE)
									continue;
								if (tpdu.getPdu().getErrorcode() != 0) {
									if (this.drvManager.getResourceManager().isActivatedebug()) {
										logger.log(Level.SEVERE, "Error code from message: " + SiemensErrorCode
												.strerror(SiemensErrorCode.fromValue(tpdu.getPdu().getErrorcode())));
									}
									continue;
								}
								if (tpdu.getPdu().getParam() instanceof SiemensReadParamPart) {
									// get all dataparts
									// from response
									// int index = 0;
									for (SiemensReadDataPart d : ((SiemensReadParamPart) tpdu.getPdu().getParam())
											.getDataParts()) {
										// is it a response?
										if (d instanceof SiemensReadResponseDataPart) {
											ms.addBuffer(((SiemensReadResponseDataPart) d).getData());
										}
									}
								}
							}
						}
						worked = true;
						SiemensDPItem dpoint = tpack.getDps().get(0);
						DataValue dv = dpoint.drv2Prog(ms.getBuffer());
						StatusCode[] statuscode = drvManager.writeFromDriver(new NodeId[] { dpoint.getNodeId() },
								new UnsignedInteger[] { Attributes.Value }, new String[] { null },
								new DataValue[] { dv }, new Long[] { (long) this.drvId });
						if (statuscode == null || statuscode.length == 0) {
							if (this.drvManager.getResourceManager().isActivatedebug()) {
								logger.log(Level.SEVERE, "Can not set value to opc ua node: " + dpoint.getNodeId()
										+ " couldn't get error code from internal space! ");
							}
						} else if (statuscode[0].isBad()) {
							if (this.drvManager.getResourceManager().isActivatedebug()) {
								logger.log(Level.SEVERE, "Can not set value to opc ua node: " + dpoint.getNodeId()
										+ " code: " + statuscode[0].getDescription());
							}
						}
					} else {
						// we have datapoints which are not divided into
						// many messages
						for (SiemensTPDUHeader tpdu : tpdulist) {
							if (tpdu != null && tpdu.getPdu() != null) {
								// we only accept responses at that
								// time
								if (tpdu.getPdu().getType() != SiemensPDUType.RESPONSE)
									continue;
								if (tpdu.getPdu().getErrorcode() != 0) {
									if (this.drvManager.getResourceManager().isActivatedebug()) {
										logger.log(Level.SEVERE, "Error code from message: " + SiemensErrorCode
												.strerror(SiemensErrorCode.fromValue(tpdu.getPdu().getErrorcode())));
									}
									continue;
								}
								if (tpdu.getPdu().getParam() instanceof SiemensReadParamPart) {
									// get all dataparts
									// from response
									worked = true;
									int index = 0;
									List<NodeId> ids = new ArrayList<NodeId>();
									List<DataValue> Vals = new ArrayList<DataValue>();
									List<Long> drvids = new ArrayList<Long>();
									List<UnsignedInteger> attrs = new ArrayList<UnsignedInteger>();
									List<String> indr = new ArrayList<String>();
									for (SiemensReadDataPart d : ((SiemensReadParamPart) tpdu.getPdu().getParam())
											.getDataParts()) {
										// is it a response?
										if (d instanceof SiemensReadResponseDataPart) {
											SiemensDPItem dpoint = tpack.getDps().get(index);
											index++;
											byte[] data = ((SiemensReadResponseDataPart) d).getData();
											// verify data length
											if (data.length == 0) {
												if (this.drvManager.getResourceManager().isActivatedebug()) {
													logger.log(Level.SEVERE, "Wrong data length response for node: "
															+ dpoint.getDisplayName());
												}
											} else {
												DataValue dv = dpoint.drv2Prog(data);
												if (manager.getTriggerList().containsKey(dpoint.getNodeId())) {
													drvids.add((long) -1);
												} else {
													drvids.add((long) drvId);
												}
												ids.add(dpoint.getNodeId());
												Vals.add(dv);
												attrs.add(Attributes.Value);
												indr.add(null);
											}
										}
									}
									if (Vals.size() > 0) {
										StatusCode[] statuscode = drvManager.writeFromDriver(
												ids.toArray(new NodeId[ids.size()]),
												attrs.toArray(new UnsignedInteger[attrs.size()]),
												indr.toArray(new String[indr.size()]),
												Vals.toArray(new DataValue[Vals.size()]),
												drvids.toArray(new Long[drvids.size()]));
										if (statuscode == null || statuscode.length == 0) {
											if (this.drvManager.getResourceManager().isActivatedebug()) {
												logger.log(Level.SEVERE, "Can not set value to opc ua node: "
														+ " couldn't get error code from internal space! ");
											}
										} else {
											for (int i = 0; i < statuscode.length; i++) {
												if (statuscode[i].isBad()) {
													if (this.drvManager.getResourceManager().isActivatedebug()) {
														logger.log(Level.SEVERE,
																"Can not set value to opc ua node: "
																		+ tpack.getDps().get(i).getNodeId() + " code: "
																		+ statuscode[i].getDescription());
													}
												}
											}
										}
									}
								}
							}
						}
					}
					if (this.drvManager.getResourceManager().isActivatedebug()
							&& (this.drvManager.getResourceManager().getDebug()
									& this.drvManager.getResourceManager().DEBUG_READPACKAGE) == this.drvManager
											.getResourceManager().DEBUG_READPACKAGE) {
						StringBuffer logg = new StringBuffer();
						for (SiemensDPItem dp : tpack.getDps()) {
							logg.append(dp.getDisplayName() + " ");
						}
						logger.info("Reading trigger variables: " + logg.toString() + " in:"
						// + (System.currentTimeMillis() -
						// packageinterval)
								+ (System.nanoTime() - packageinterval) / 1000000L + " milliseconds!");
					}
				}
				// }
			}
			if (this.manager.getPackagesByInterval() != null) {
				// first get interval of package
				for (Long key : this.manager.getPackagesByInterval().keySet()) {
					SiemensPackageList<SiemensDPPackages> packs = this.manager.getPackagesByInterval().get(key);
					// now did we have an expired interval
					// if (packs.getLastRead() + key <
					// System.currentTimeMillis()) {
					if (packs.getLastRead() + key < System.nanoTime()) {
						// packs.setLastRead(System.currentTimeMillis());
						packs.setLastRead(System.nanoTime());
						/**
						 * iterate through each data point in the interval list
						 */
						for (SiemensDPPackages pack : packs) {
							if (lastwatchdogwrite + (1000L * 1000000L * 10L) < System.nanoTime()) {
								// lastwatchdogwrite =
								// System.currentTimeMillis();
								lastwatchdogwrite = System.nanoTime();
								/**
								 * now we finished run - so update watchdog state
								 */
								utils.updateWatchdog();
							}
							// get starttime of package read
							// packageinterval = System.currentTimeMillis();
							packageinterval = System.nanoTime();
							// --------------------- add debug info
							// ------------------------------
							// variablecount += pack.getDps().size();
							List<SiemensTPDUHeader> tpdulist = device.readPackage(pack);
							if (tpdulist != null && tpdulist.size() > 0) {
								// we got a response
								if (pack.isSingleDP()) {
									// response is only a big array
									ComByteMessage ms = new ComByteMessage();
									for (SiemensTPDUHeader tpdu : tpdulist) {
										if (tpdu != null && tpdu.getPdu() != null) {
											// we only accept responses at that
											// time
											if (tpdu.getPdu().getType() != SiemensPDUType.RESPONSE)
												continue;
											if (tpdu.getPdu().getErrorcode() != 0) {
												if (this.drvManager.getResourceManager().isActivatedebug()) {
													logger.log(Level.SEVERE,
															"Error code from message: "
																	+ SiemensErrorCode.strerror(SiemensErrorCode
																			.fromValue(tpdu.getPdu().getErrorcode())));
												}
												continue;
											}
											if (tpdu.getPdu().getParam() instanceof SiemensReadParamPart) {
												// get all dataparts
												// from response
												// int index = 0;
												for (SiemensReadDataPart d : ((SiemensReadParamPart) tpdu.getPdu()
														.getParam()).getDataParts()) {
													// is it a response?
													if (d instanceof SiemensReadResponseDataPart) {
														ms.addBuffer(((SiemensReadResponseDataPart) d).getData());
													}
												}
											}
										}
									}
									worked = true;
									SiemensDPItem dpoint = pack.getDps().get(0);
									if (this.drvManager.getResourceManager().isActivatedebug() && (this.drvManager
											.getResourceManager().getDebug()
											& this.drvManager.getResourceManager().DEBUG_READNODE) == this.drvManager
													.getResourceManager().DEBUG_READNODE) {
										// try to find datapoint
										// for (SiemensDPItem item :
										// pack.getDps()) {
										if (this.drvManager.getResourceManager().getNids()
												.contains(dpoint.getNodeId())) {
											logger.info("Read node from plc! Nodeid: " + dpoint.getNodeId() + " in "
													+ (System.nanoTime() - packageinterval) / 1000000L
													+ " milliseconds!");
										}
									}
									// }
									DataValue dv = dpoint.drv2Prog(ms.getBuffer());
									List<Long> drvids = new ArrayList<Long>();
									if (manager.getTriggerList().containsKey(dpoint.getNodeId())) {
										drvids.add((long) -1);
									} else {
										drvids.add((long) drvId);
									}
									// packageinterval = System
									// .currentTimeMillis();
									long writetoserver = System.nanoTime();
									StatusCode[] statuscode = drvManager.writeFromDriver(
											new NodeId[] { dpoint.getNodeId() },
											new UnsignedInteger[] { Attributes.Value }, new String[] { null },
											new DataValue[] { dv }, drvids.toArray(new Long[drvids.size()]));
									if (statuscode == null || statuscode.length == 0) {
										if (this.drvManager.getResourceManager().isActivatedebug()) {
											logger.log(Level.SEVERE,
													"Can not set value to opc ua node: " + dpoint.getNodeId()
															+ " couldn't get error code from internal space! ");
										}
									} else if (statuscode[0].isBad()) {
										if (this.drvManager.getResourceManager().isActivatedebug()) {
											logger.log(Level.SEVERE, "Can not set value to opc ua node: "
													+ dpoint.getNodeId() + " code: " + statuscode[0].getDescription());
										}
									} else {
										if (this.drvManager.getResourceManager().isActivatedebug()
												&& (this.drvManager.getResourceManager().getDebug() & this.drvManager
														.getResourceManager().DEBUG_READNODE) == this.drvManager
																.getResourceManager().DEBUG_READNODE
												&& this.drvManager.getResourceManager().getNids()
														.contains(dpoint.getNodeId())) {
											logger.info("Write value to opc ua server! NodeId: " + dpoint.getNodeId()
													+ " in: " + (System.nanoTime() - writetoserver) / 1000000L
													+ " milliseconds, value: " + dv.toString());
										}
										successcount++;
									}
								} else {
									// we have datapoints which are not divided
									// into
									// many messages
									for (SiemensTPDUHeader tpdu : tpdulist) {
										if (tpdu != null && tpdu.getPdu() != null) {
											// we only accept responses at that
											// time
											if (tpdu.getPdu().getType() != SiemensPDUType.RESPONSE)
												continue;
											if (tpdu.getPdu().getErrorcode() != 0) {
												if (this.drvManager.getResourceManager().isActivatedebug()) {
													logger.log(Level.SEVERE,
															"Error code from message: "
																	+ SiemensErrorCode.strerror(SiemensErrorCode
																			.fromValue(tpdu.getPdu().getErrorcode())));
												}
												errorcount += pack.getDps().size();
												break; // because, here we can
												// get only one message
											}
											if (tpdu.getPdu().getParam() instanceof SiemensReadParamPart) {
												// get all dataparts
												// from response
												worked = true;
												int index = 0;
												List<NodeId> ids = new ArrayList<NodeId>();
												List<DataValue> Vals = new ArrayList<DataValue>();
												List<Long> drvids = new ArrayList<Long>();
												List<UnsignedInteger> attrs = new ArrayList<UnsignedInteger>();
												List<String> indr = new ArrayList<String>();
												// normally we do not need an
												// loop because we can have only
												// one item at once
												for (SiemensReadDataPart d : ((SiemensReadParamPart) tpdu.getPdu()
														.getParam()).getDataParts()) {
													// is it a response?
													if (d instanceof SiemensReadResponseDataPart) {
														SiemensDPItem dpoint = pack.getDps().get(index);
														index++;
														// verify data error
														if (((SiemensReadResponseDataPart) d)
																.getDataErrorCode() != SiemensDataErrorCode.NO_ERROR
																		.getCode()) {
															if (this.drvManager.getResourceManager()
																	.isActivatedebug()) {
																logger.log(Level.SEVERE, "Node: " + dpoint.getNodeId()
																		+ " - " + dpoint.getDisplayName() + "  "
																		+ SiemensDataErrorCode.strerror(
																				SiemensDataErrorCode.fromValue(
																						((SiemensReadResponseDataPart) d)
																								.getDataErrorCode())));
															}
															errorcount++;
															continue;
														}
														byte[] data = ((SiemensReadResponseDataPart) d).getData();
														// verify data length
														if (data.length == 0) {
															logger.log(Level.SEVERE,
																	"Wrong data length response for node: "
																			+ dpoint.getDisplayName());
															errorcount++;
														} else {
															DataValue dv = dpoint.drv2Prog(data);
															// check if we have
															// an
															// value change
															if (!dpoint.getOldDataValue().getValue()
																	.equals(dv.getValue())) {
																successcount++;
																if (manager.getPossibleTriggerNIDs() != null
																		&& manager.getPossibleTriggerNIDs()
																				.contains(dpoint.getNodeId())) {
																	drvids.add((long) -1);
																} else {
																	drvids.add((long) drvId);
																}
																ids.add(dpoint.getNodeId());
																Vals.add(dv);
																attrs.add(Attributes.Value);
																indr.add(null);
																dpoint.setOldDataValue(dv);
																if (this.drvManager.getResourceManager()
																		.isActivatedebug()
																		&& (this.drvManager.getResourceManager()
																				.getDebug()
																				& this.drvManager
																						.getResourceManager().DEBUG_READNODE) == this.drvManager
																								.getResourceManager().DEBUG_READNODE) {
																	if (this.drvManager.getResourceManager().getNids()
																			.contains(dpoint.getNodeId())) {
																		logger.info("Read node from plc! Nodeid: "
																				+ dpoint.getNodeId() + " in "
																				+ (System.nanoTime() - packageinterval)
																						/ 1000000L
																				+ " milliseconds!");
																	}
																}
															}
														}
													}
												}
												if (Vals.size() > 0) {
													long writeserverinterval = System.nanoTime();
													StatusCode[] statuscode = drvManager.writeFromDriver(
															ids.toArray(new NodeId[ids.size()]),
															attrs.toArray(new UnsignedInteger[attrs.size()]),
															indr.toArray(new String[indr.size()]),
															Vals.toArray(new DataValue[Vals.size()]),
															drvids.toArray(new Long[drvids.size()]));
													if (statuscode == null || statuscode.length == 0) {
														if (this.drvManager.getResourceManager().isActivatedebug()) {
															logger.log(Level.SEVERE,
																	"Can not set value to opc ua node: "
																			+ " couldn't get error code from internal space! ");
														}
													} else {
														for (int i = 0; i < statuscode.length; i++) {
															if (statuscode[i].isBad()) {
																if (this.drvManager.getResourceManager()
																		.isActivatedebug()) {
																	logger.log(Level.SEVERE,
																			"Can not set value to opc ua node: "
																					+ pack.getDps().get(i).getNodeId()
																					+ " code: "
																					+ statuscode[i].getDescription());
																}
															} else {
																// is this node
																// to
																// monitor
																if (this.drvManager.getResourceManager()
																		.isActivatedebug()
																		&& (this.drvManager.getResourceManager()
																				.getDebug()
																				& this.drvManager
																						.getResourceManager().DEBUG_READNODE) == this.drvManager
																								.getResourceManager().DEBUG_READNODE
																		&& this.drvManager.getResourceManager()
																				.getNids().contains(ids.get(i))) {
																	logger.info("Write value to opc ua server! NodeId: "
																			+ ids.get(i) + " in: "
																			+ (System.nanoTime() - writeserverinterval)
																			+ " nano seconds, value: "
																			+ Vals.get(i).toString());
																}
																successcount++;
															}
														}
													}
												}
											}
										}
									}
								}
							}
							if (this.drvManager.getResourceManager().isActivatedebug()
									&& (this.drvManager.getResourceManager().getDebug()
											& this.drvManager.getResourceManager().DEBUG_READPACKAGE) == this.drvManager
													.getResourceManager().DEBUG_READPACKAGE) {
								StringBuffer logg = new StringBuffer();
								for (SiemensDPItem dp : pack.getDps()) {
									logg.append(dp.getDisplayName() + " ");
								}
								logger.info("Reading variables: " + logg.toString() + " in:"
										+ (System.nanoTime() - packageinterval) / 1000000L + " milliseconds!");
							}
						}
					}
				}
			}
		}
		if (isfirstrun) {
			// now remove init packages
			if (this.manager.getPackagesByInterval() != null) {
				this.manager.getPackagesByInterval().remove(-1000000L);
			}
			if (!isstartup) {
				logger.info("---------------------- Siemens Driver started! -------------------------");
				// here we set running flag to server state
				this.drvManager.writeFromDriver(new NodeId[] { Identifiers.Server_ServerStatus_State },
						new UnsignedInteger[] { Attributes.Value }, new String[] { null },
						new DataValue[] { new DataValue(new Variant(ServerState.Running)) }, new Long[] { drvId });
				((SiemensTCPISODevice) this.device).setInitState(false);
				utils.writeLEDStatus(ComStatusUtils.RUNNING);
				utils.updateWatchdog();
				isfirstrun = false;
			}
		}
		if (this.drvManager.getResourceManager().isActivatedebug() && (this.drvManager.getResourceManager().getDebug()
				& this.drvManager.getResourceManager().DEBUG_READINTERVAL) > 0) {
			if (successcount + errorcount > 0) {
				logger.info("Readcycle finished: Datapoints(total|success|failure): " + (successcount + errorcount)
						+ "|" + successcount + "|" + errorcount + " in: "
						+ (System.nanoTime() - intervalstart) / 1000000L + " milliseconds.");
			}
		}
		// is live-trigger OK after a defined timeout and if device is connected
		// if(lastlivetriggercheck + livetriggertimeout <
		// System.currentTimeMillis()) {
		if (lastlivetriggercheck + livetriggertimeout < System.nanoTime()) {
			// lastlivetriggercheck = System.currentTimeMillis();
			lastlivetriggercheck = System.nanoTime();
			// if()
		}
		return worked;
	}

	private long lastwatchdogwrite = 0;
	private long livetriggertimeout = 1000L * 60L * 1000000;
	private long lastlivetriggercheck = 0;

	//
	// private long branchtimeout = 1000L * 60L * 60L * 1000000;
	// private long lastbranchrun = 0;
	@Override
	public boolean doFinish() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setDrvReconnectTimeout(String timeout) {
		if (this.device != null) {
			try {
				this.device.setReconnectTimeout(Long.parseLong(timeout));
			} catch (IllegalArgumentException ex) {
				// deactivate reconnect
				this.device.setReconnectTimeout(-1);
			}
		}
	}
}
