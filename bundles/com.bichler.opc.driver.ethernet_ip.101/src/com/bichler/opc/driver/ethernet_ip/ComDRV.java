package com.bichler.opc.driver.ethernet_ip;

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
import com.bichler.opc.comdrv.importer.Com_DeviceConfigNode;
import com.bichler.opc.comdrv.importer.Com_DeviceConfigNode.DeviceNode;
import com.bichler.opc.comdrv.importer.Com_StartConfigNode;
import com.bichler.opc.comdrv.importer.Com_TriggerDpItem;
import com.bichler.opc.comdrv.utils.ComStatusUtils;
import com.bichler.opc.driver.ethernet_ip.dp.EthernetIPDPItem;
import com.bichler.opc.driver.ethernet_ip.transform.ETHERNETIP_MAPPING_TYPE;
import com.bichler.opc.driver.ethernet_ip.transform.EthernetIPTransformFactory;

public class ComDRV extends ExtendedComDRV {
	private ComStatusUtils utils = new ComStatusUtils();
	private ComEthernetIPResourceManager manager;
	private Logger logger = Logger.getLogger(getClass().getName());

	public ComDRV() {
		this.manager = new ComEthernetIPResourceManager();
		this.manager.setManager(ComDRVManager.getDRVManager().getResourceManager());
	}

	/**
	 * set driver information to actual server only valid if we have one plc driver
	 * connected to a server
	 */
	private void fillServerinfo() {
		DateTime builddate = null;
		String revision = "0.1";
		String manufacturer = "Bichler Technologies GmbH";
		String productname = "OPC UA Server for Ethernet-IP";
		String productUri = "";
		String softwareversion = "1.0.1";
		String driverinfopath = this.drvManager.getResourceManager().getRuntimeDir() + "drivers" + File.separator
				+ "rockwell" + File.separator + "drv.info";
		File drvinfo = new File(driverinfopath);
		String line = "";
		if (drvinfo.exists()) {
			try (FileReader freader = new FileReader(drvinfo); BufferedReader reader = new BufferedReader(freader);) {
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
				logger.log(Level.SEVERE, e.getMessage(), e);
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
		ComEthernetIPDevice dev = new ComEthernetIPDevice();
		this.manager.setDevice((ComEthernetIPDevice) dev);
		dev.setDeviceAddress(address);
		dev.setDrvId(drvId);
		dev.setManager(this.manager.getManager());
		dev.setUtils(utils);
		this.device = dev;
		this.manager.setDevice(dev);
	}

	@Override
	public void setDrvProperties(String drvProperties) {
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

	private void initializePLCAddress() {
		File conffile = new File(ComResourceManager.CONFIGPATH);
		if (conffile.exists()) {
			try (FileReader freader = new FileReader(conffile); BufferedReader reader = new BufferedReader(freader);) {
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
				logger.log(Level.SEVERE, e.getMessage(), e);
			}
		}
	}

	@Override
	public void initialize() {
		this.initializeHandler();
		this.initializePLCAddress();
		this.fillServerinfo();
	}

	/**
	 * 
	 */
	private void initializeHandler() {
		// don't send any read requests to the device
		EthernetIPWriteHandler write = new EthernetIPWriteHandler();
		write.setManager(manager);
		ComDRVManager.getDRVManager().addWriteListener((long) this.drvId, write);
	}

	@Override
	public void checkCommunication() {
	}

	@Override
	public ComEthernetIPDevice getDevice() {
		return (ComEthernetIPDevice) this.device;
	}

	private boolean validateMapping(EthernetIPDPItem dp) {
		EthernetIPTransformFactory factory = new EthernetIPTransformFactory();
		DataValue response = this.drvManager.readFromDriver(dp.getNodeId(), Attributes.Value, null, null, (long) drvId,
				0.0, TimestampsToReturn.Both);
		boolean valid = false;
		/* check the namespace object */
		if (response != null && response.getValue() != null && response.getValue().getValue() != null) {
			if (response.getValue().isArray() && dp.getMapping() == ETHERNETIP_MAPPING_TYPE.ARRAY_ARRAY) {
				if (((Object[]) response.getValue().getValue()).length == dp.getArrayLength()) {
					valid = true;
					dp.fixArrayLength();
				} else if (response.getValue().isArray() && dp.getMapping() == ETHERNETIP_MAPPING_TYPE.ALARM) {
					// attention, we only validate if it is an array, but no length
					valid = true;
				} else {
					if (((Object[]) response.getValue().getValue()).length > dp.getArrayLength()) {
						logger.log(Level.WARNING, "Intern array is larger than plc array! Node: " + dp.getNodeId() + " "
								+ dp.getTagname());
					} else {
						logger.log(Level.WARNING, "Intern array is smaller than plc array! Node: " + dp.getNodeId()
								+ " " + dp.getTagname());
					}
				}
			} else if (!response.getValue().isArray() && dp.getMapping() == ETHERNETIP_MAPPING_TYPE.SCALAR) {
				valid = true;
			} else {
				logger.log(Level.WARNING, "Node not added to schedule, because mapping is invalid! Nodeid: "
						+ dp.getNodeId() + " SymbolName: " + dp.getTagname());
			}
		} else if (response != null && (response.getStatusCode().getValue().equals(StatusCodes.Bad_NodeIdInvalid)
				|| response.getStatusCode().getValue().equals(StatusCodes.Bad_NodeIdUnknown))) {
			logger.log(Level.WARNING,
					"Node not added to schedule, because node did not exist in address space! Nodeid: " + dp.getNodeId()
							+ " SymbolName: " + dp.getTagname());
			return false;
		} else {
			logger.log(Level.WARNING, "Node not added to schedule, because opc ua value = null! Nodeid: "
					+ dp.getNodeId() + " SymbolName: " + dp.getTagname());
			return false;
		}
		if (valid) {
			factory.createTransform(dp, response.getValue().getCompositeClass());
		}
		return valid;
	}

	/**
	 * // create read request and waits for each response, if we had timeout so //
	 * return false
	 * 
	 * @param preds
	 * @return
	 */
	private boolean doWorkStartup(List<EthernetIPDPItem> preds) {
		// first check connection
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
		for (EthernetIPDPItem pred : preds) {
			try {
				DataValue val = ((ComEthernetIPDevice) this.device).read(pred);
				if (val != null) {
					this.drvManager.writeFromDriver(new NodeId[] { pred.getNodeId() },
							new UnsignedInteger[] { Attributes.Value }, new String[] { null }, new DataValue[] { val },
							new Long[] { this.drvId });
				}
			} catch (Exception e) {
				logger.log(Level.SEVERE, "Startup was not successfully because of communication exception.");
			}
		}
		return true;
	}

	@Override
	public boolean doStartup() {
		// try to load startup config,
		// try to load device config
		// try to load counter config
		super.doStartup();
		utils.updateWatchdog();
//    utils.updateServerWatchdog();
		ComEthernetIPImporter importer = new ComEthernetIPImporter();
		// load all data points to startup device configuration
		File dpfile = new File("drivers/" + this.getDriverName() + "/datapoints.com");
		List<EthernetIPDPItem> dps = new ArrayList<>();
		List<EthernetIPDPItem> predps = new ArrayList<>();
		FileInputStream input = null;
		if (dpfile.exists()) {
			// load all datapoints for predefined datapoint check
			if (this.drvManager.getResourceManager().isActivatedebug()
					&& (this.drvManager.getResourceManager().getDebug()
							& this.drvManager.getResourceManager().DEBUG_STARTUP) == this.drvManager
									.getResourceManager().DEBUG_STARTUP) {
				logger.log(Level.INFO, "Importing node to driver datapoint mappings started!");
			}
			try {
				input = new FileInputStream(dpfile);
				dps = importer.loadDPs(input, getNamespaceTable(), this.drvManager.getResourceManager());
				input.close();
			} catch (IOException e) {
				java.util.logging.Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
			}
			if (this.drvManager.getResourceManager().isActivatedebug()
					&& (this.drvManager.getResourceManager().getDebug()
							& this.drvManager.getResourceManager().DEBUG_STARTUP) == this.drvManager
									.getResourceManager().DEBUG_STARTUP) {
				logger.log(Level.INFO, "Importing node to driver datapoint mappings finished!");
			}
			// did we have any startconfignodes to validate ?
			if (getStartConfigNodes() != null && !getStartConfigNodes().isEmpty()) {
				for (Com_StartConfigNode node : getStartConfigNodes()) {
					if (node.getIndex() < 0)
						continue;
					// we can not have index lower than 0
					for (EthernetIPDPItem dp : dps) {
						if (dp.getNodeId().equals(node.getConfigNodeId())) {
							if (this.validateMapping(dp))
								predps.add(dp);
						}
					}
				}
			}
			// did we have any device config to validate ?
			if (getDeviceConfigNode() != null && getDeviceConfigNode().isActive()) {
				for (EthernetIPDPItem dp : dps) {
					if (dp.getNodeId().equals(getDeviceConfigNode().getConfigNodeId())) {
						if (this.validateMapping(dp))
							predps.add(dp);
					}
				}
			}
			if (getCounterConfigNodes() != null && getCounterConfigNodes().size() > 0) {
				Com_CounterConfigGroup counter = getCounterConfigNodes().get(0);
				for (EthernetIPDPItem dp : dps) {
					if (dp.getNodeId().equals(counter.getConfigNodeId())) {
						if (this.validateMapping(dp))
							predps.add(dp);
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
						logger.log(Level.SEVERE, e.getMessage(), e);
					}
				}
			}
			DataValue response = null;
			// try to add additional model files per device
			if (getDeviceConfigNode() != null) {
				Com_DeviceConfigNode dev = getDeviceConfigNode();
				response = this.drvManager.readFromDriver(dev.getConfigNodeId(), Attributes.Value, null, null,
						(long) drvId, 0.0, TimestampsToReturn.Both);
				if (response != null && response.getValue() != null && response.getValue().getValue() != null
						&& response.getValue().getValue() instanceof Object[]) {
					Object[] obj = (Object[]) response.getValue().getValue();
					// first set all nodes visible = false
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
									try {
										input = new FileInputStream(map);
										dps.addAll(importer.loadDPs(input, getNamespaceTable(),
												this.drvManager.getResourceManager()));
									} catch (FileNotFoundException e) {
										logger.log(Level.SEVERE, e.getMessage(), e);
									}
								}
							}
						}
					}
				}
			}
			this.createScheduleList(dps);
			manager.setTriggerList(new HashMap<String, Com_TriggerDpItem>());
			this.createTriggerList(manager.getTriggerList(), getNamespaceTable());
			this.createTriggers();
		}
		return true;
	}

	private void createScheduleList(List<EthernetIPDPItem> dps) {
		Map<Long, List<EthernetIPDPItem>> nodes = this.manager.getNodesByInterval();
		Map<NodeId, EthernetIPDPItem> items = this.manager.getDpItems();
		if (nodes == null) {
			nodes = new HashMap<>();
			this.manager.setNodesByInterval(nodes);
		}
		if (items == null) {
			items = new HashMap<>();
			this.manager.setDpItems(items);
		}
		for (EthernetIPDPItem dp : dps) {
			if (dp.isActive()) {
				DataValue response = this.drvManager.readFromDriver(dp.getNodeId(), Attributes.DisplayName, null, null,
						(long) drvId, 0.0, TimestampsToReturn.Both);
				if (response != null && response.getValue() != null && response.getValue().getValue() != null) {
					dp.setDisplayName(((LocalizedText) response.getValue().getValue()).getText());
				}
				List<EthernetIPDPItem> cycle = nodes.get(dp.getCycletime());
				if (validateMapping(dp)) {
					if (cycle == null) {
						cycle = new ArrayList<>();
						nodes.put(dp.getCycletime(), cycle);
					}
					cycle.add(dp);
					items.put(dp.getNodeId(), dp);
					// set datapoint write synchron
					this.drvManager.getResourceManager().getDriverConnection().setDriverWriteConnected(dp.getNodeId(),
							true, ComDRVManager.SYNCWRITE, (long) this.drvId);
				}
			}
		}
	}

	private void createTriggers() {
		List<String> trigs2remove = new ArrayList<>();
		// remove all not activated triggers
		for (Com_TriggerDpItem trigg : manager.getTriggerList().values()) {
			if (!trigg.isActive())
				trigs2remove.add(trigg.getTriggerID());
		}
		for (String trig : trigs2remove) {
			manager.getTriggerList().remove(trig);
		}
		List<NodeId> possibleTNIDs = new ArrayList<NodeId>();
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
		for (EthernetIPDPItem item : manager.getDpItems().values()) {
			String nid = item.getTriggerNode();
			if (nid != null) {
				tn = manager.getTriggerList().get(nid);
				List<EthernetIPDPItem> list = null;
				if (tn != null) {
					tn.getNodesToRead().add(item.getNodeId());
					list = manager.getTriggeritems().get(tn.getNodeId());
				}
				if (list == null) {
					list = new ArrayList<>();
					manager.getTriggeritems().put(tn.getNodeId(), list);
				}
				list.add(item);
			}
		}
	}

	@Override
	public boolean doWork() {
		int errorcount = 0;
		int successcount = 0;
		long intervalstart = System.nanoTime();
		if (lastwatchdogwrite + (1000L * 1000000L * 10L) < System.nanoTime()) {
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
		try {
			/**
			 * first try to read all trigger items
			 */
			if (this.manager.getTrigger2Read() != null && this.manager.getTrigger2Read().size() > 0) {
				EthernetIPDPItem item = null;
				while ((item = this.manager.getTrigger2Read().poll()) != null) {
					try {
						DataValue val = ((ComEthernetIPDevice) this.device).read(item);
						if (val != null) {
							if (!val.equals(item.getOldDataValue())) {
								long drvi = this.drvId;
								if (manager.getPossibleTriggerNIDs() != null
										&& manager.getPossibleTriggerNIDs().contains(item.getNodeId())) {
									drvi = -1;
								}
								item.setOldDataValue(val);
								StatusCode[] statuscode = this.drvManager.writeFromDriver(
										new NodeId[] { item.getNodeId() }, new UnsignedInteger[] { Attributes.Value },
										new String[] { null }, new DataValue[] { val }, new Long[] { drvi });
								if (statuscode == null || statuscode.length == 0) {
									errorcount++;
									if (this.drvManager.getResourceManager().isActivatedebug()) {
										logger.log(Level.WARNING, "Can not set value to opc ua node: "
												+ item.getNodeId() + " couldn't get error code from internal space! ");
									}
								} else if (statuscode[0].isBad()) {
									errorcount++;
									if (this.drvManager.getResourceManager().isActivatedebug()) {
										logger.log(Level.WARNING, "Can not set value to opc ua node: "
												+ item.getNodeId() + " code: " + statuscode[0].getDescription());
									}
								} else
									successcount++;
							}
						} else
							errorcount++;
					} catch (Exception e) {
						errorcount++;
					}
				}
				/**
				 * remove all trigger items
				 */
				this.manager.getTrigger2Read().clear();
			}
			for (Long key : this.manager.getNodesByInterval().keySet()) {
				for (EthernetIPDPItem item : this.manager.getNodesByInterval().get(key)) {
					if (item.getLastRead() + key < System.nanoTime()) {
						try {
							item.setLastRead(System.nanoTime());
							DataValue val = ((ComEthernetIPDevice) this.device).read(item);
							if (val != null) {
								if (!val.equals(item.getOldDataValue())) {
									item.setOldDataValue(val);
									long drvi = this.drvId;
									if (manager.getPossibleTriggerNIDs() != null
											&& manager.getPossibleTriggerNIDs().contains(item.getNodeId())) {
										drvi = -1;
									}
									StatusCode[] statuscode = this.drvManager.writeFromDriver(
											new NodeId[] { item.getNodeId() },
											new UnsignedInteger[] { Attributes.Value }, new String[] { null },
											new DataValue[] { val }, new Long[] { drvi });
									if (statuscode == null || statuscode.length == 0) {
										errorcount++;
										if (this.drvManager.getResourceManager().isActivatedebug()) {
											logger.log(Level.WARNING,
													"Can not set value to opc ua node: " + item.getNodeId()
															+ " couldn't get error code from internal space! ");
										}
									} else if (statuscode[0].isBad()) {
										errorcount++;
										if (this.drvManager.getResourceManager().isActivatedebug()) {
											logger.log(Level.WARNING, "Can not set value to opc ua node: "
													+ item.getNodeId() + " code: " + statuscode[0].getDescription());
										}
									} else
										successcount++;
								}
							} else
								errorcount++;
						} catch (Exception e) {
							errorcount++;
						}
					}
				}
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
		if (this.drvManager.getResourceManager().isActivatedebug() && (this.drvManager.getResourceManager().getDebug()
				& this.drvManager.getResourceManager().DEBUG_READINTERVAL) > 0) {
			if (successcount + errorcount > 0) {
				logger.log(Level.INFO,
						"Readcycle finished: Datapoints(total|success|failure): " + (successcount + errorcount) + "|"
								+ successcount + "|" + errorcount + " in: "
								+ (System.nanoTime() - intervalstart) / 1000000L + " milliseconds.");
			}
		}
		if (isfirstrun) {
			// now remove init datapoints
			if (this.manager.getNodesByInterval() != null) {
				this.manager.getNodesByInterval().remove(-1000000L);
			}
			isfirstrun = false;
			logger.log(Level.INFO, "---------------------- Allen Bradley Driver started! -------------------------");
			// here we set running flag to server state
			this.drvManager.writeFromDriver(new NodeId[] { Identifiers.Server_ServerStatus_State },
					new UnsignedInteger[] { Attributes.Value }, new String[] { null },
					new DataValue[] { new DataValue(new Variant(ServerState.Running)) }, new Long[] { drvId });
			getDevice().setInitState(false);
			utils.writeLEDStatus(ComStatusUtils.RUNNING);
			utils.updateWatchdog();
		}
		return true;
	}

	private boolean isfirstrun = true;
	private long lastwatchdogwrite = 0;

	@Override
	public boolean doFinish() {
		return true;
	}

	public ComEthernetIPResourceManager getManager() {
		return manager;
	}

	public void setManager(ComEthernetIPResourceManager manager) {
		this.manager = manager;
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
