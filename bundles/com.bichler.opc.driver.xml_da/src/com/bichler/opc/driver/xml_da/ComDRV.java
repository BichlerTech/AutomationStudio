package com.bichler.opc.driver.xml_da;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.common.NamespaceTable;
import org.opcfoundation.ua.core.Attributes;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.TimestampsToReturn;

import com.bichler.opc.comdrv.ComCommunicationStates;
import com.bichler.opc.comdrv.ComDRVManager;
import com.bichler.opc.comdrv.ExtendedComDRV;
import com.bichler.opc.comdrv.importer.Com_DeviceConfigNode;
import com.bichler.opc.comdrv.importer.Com_DeviceConfigNode.DeviceNode;
import com.bichler.opc.comdrv.importer.Com_StartConfigNode;
import com.bichler.opc.comdrv.utils.ComStatusUtils;
import com.bichler.opc.driver.xml_da.dp.XML_DA_DPItem;
import com.bichler.opc.driver.xml_da.transform.XML_DA_MAPPING_TYPE;
import com.bichler.opc.driver.xml_da.transform.XML_DA_TransformFactory;

public class ComDRV extends ExtendedComDRV {
	public static String VERSIONID = "1.0.0";
	// public static String BUNDLEID = "com.hbsoft.xml_da";
	private XML_DA_ResourceManager manager = null;
	private ComStatusUtils utils = new ComStatusUtils();
	private long lastwatchdogwrite = 0;

	/**
	 * constructor
	 */
	public ComDRV() {
		Logger.getLogger(getClass().getName()).log(Level.INFO, "load driver for opc ua xml da!");
		this.manager = new XML_DA_ResourceManager();
		this.manager.setManager(ComDRVManager.getDRVManager().getResourceManager());
		this.manager.setDrvManager(ComDRVManager.getDRVManager());
		this.device = new XML_DA_Device();
		getDevice().setResouceManager(this.manager);
		this.manager.setDevice((XML_DA_Device) device);
	}

	/**
	 * initialize function initialize all datapoints, and handlers
	 */
	@Override
	public void initialize() {
		// now load all dp mappings
		// this.loadDPs();
		// now set all listeners
		this.initializeHandler();
	}

	@Override
	public XML_DA_Device getDevice() {
		return (XML_DA_Device) this.device;
	}

	@Override
	public void checkCommunication() {
	}

	@Override
	public boolean doStartup() {
		super.doStartup();
		// this.logger.log1("Read device configuration", CometModuls.STR_DRV,
		// CometModuls.INT_DRV, BUNDLEID, VERSIONID);
		FileInputStream input = null;
		try {
			// we need to map the device configuration to
			/** first we add namespaceuri */
			DataValue response = this.manager.getDrvManager().readFromDriver(Identifiers.Server_NamespaceArray,
					Attributes.Value, null, null, (long) drvId, 0.0, TimestampsToReturn.Both);
			/* check the namespace object */
			if (response == null || response.getValue() == null) {
				return false;
			}
			// get whole namespaces from internal server to generate the right
			// name space index for each datapoint
			String[] namespaces = (String[]) response.getValue().getValue();
			NamespaceTable ns = NamespaceTable.createFromArray(namespaces);
			XML_DA_Importer importer = new XML_DA_Importer();
			List<XML_StartConfigNode> start = null;
			// if (new File("drivers/" + this.getDriverName() +
			// "/startconfignodes.com").exists())
			// {
			// input = new FileInputStream("drivers/" + this.getDriverName() +
			// "/startconfignodes.com");
			// start = importer.loadDeviceConfig(input, ns);
			// }
			// load all data points to startup device configuration
			input = new FileInputStream("drivers/" + this.getDriverName() + "/datapoints.com");
			List<XML_DA_DPItem> dps = importer.loadDPs(input, ns);
			List<XML_DA_DPItem> predps = new ArrayList<XML_DA_DPItem>();
			manager.setDpByHandle(new HashMap<String, List<XML_DA_DPItem>>());
			Map<String, List<XML_DA_DPItem>> dpbyhandle = manager.getDpByHandle();
			if (getStartConfigNodes() != null && !getStartConfigNodes().isEmpty()) {
				for (Com_StartConfigNode node : getStartConfigNodes()) {
					if (node.getIndex() < 0)
						continue;
					// we can not have index lower than 0
					for (XML_DA_DPItem dp : dps) {
						if (dp.getNodeId().equals(node.getConfigNodeId())) {
							// dp.setMaxReadByteCount(getDevice().getMaxByteCount());
							// dp.setMaxWriteByteCount(getDevice().getMaxByteCount() - 12);
							// if (this.validateMapping(dp))
							predps.add(dp);
						}
					}
				}
			}
			// did we have any device config to validate ?
			if (getDeviceConfigNode() != null && getDeviceConfigNode().isActive()) {
				for (XML_DA_DPItem dp : dps) {
					if (dp.getNodeId().equals(getDeviceConfigNode().getConfigNodeId())) {
						predps.add(dp);
						/**
						 * add displayname to node to print any messages for the datapoint
						 */
						response = this.manager.getDrvManager().readFromDriver(dp.getNodeId(), Attributes.DisplayName,
								null, null, (long) drvId, 0.0, TimestampsToReturn.Both);
						if (response != null && response.getValue() != null && response.getValue().getValue() != null) {
							dp.setDisplayname(((LocalizedText) response.getValue().getValue()).getText());
						}
						/** first we add namespaceuri */
						response = this.manager.getDrvManager().readFromDriver(dp.getNodeId(), Attributes.Value, null,
								null, (long) drvId, 0.0, TimestampsToReturn.Both);
						/* check the namespace object */
						if (response != null && response.getValue() != null && response.getValue().getValue() != null) {
							// did we get an array value
							if (response.getValue().isArray()) {
								Object[] array = (Object[]) response.getValue().getValue();
								dp.setArraylength(array.length);
							}
							// set value to internal
							XML_DA_TransformFactory.createTransform(dp, response.getValue().getCompositeClass());
							if (dp.getTransform() != null) {
								dp.setArray(dp.getTransform().createInternArray(dp.getArraylength()));
							}
						}
						// XML_DA_TransformFactory.createTransform(dp,
						// response.getValue().getCompositeClass());
						dpbyhandle.put(dp.getItemPath() + "/" + dp.getItemName(), predps);
					}
				}
			}
			/*
			 * if (start != null) { for (XML_StartConfigNode node : start) { if
			 * (node.getIndex() < 0) continue; // we can not have index lower than 0 for
			 * (XML_DA_DPItem dp : dps) { if (dp.getNodeId().equals(node.getConfigNodeId()))
			 * { predps.add(dp); } } } }
			 */
			// now read all nodes and send value to opc ua server
			// manager.getDpByHandle().put(predps.get(0).getItemPath() + "/" +
			// predps.get(0).getItemName(), predps);
			getDevice().readNodes(predps, -1);
			// now we should have the correct values in the opc ua address space
			// try to add additional model files per device
			if (getDeviceConfigNode() != null) {
				Com_DeviceConfigNode dev = getDeviceConfigNode();
				response = this.manager.getDrvManager().readFromDriver(dev.getConfigNodeId(), Attributes.Value, null,
						null, (long) drvId, 0.0, TimestampsToReturn.Both);
				if (response != null && response.getValue() != null && response.getValue().getValue() != null) {
					Object[] obj = (Object[]) response.getValue().getValue();
					for (Object o : obj) {
						if (o instanceof Integer) {
							if (((Integer) o) == 0)
								break;
							int i = (Integer) o;
							DeviceNode dn = dev.getDevices().get(i);
							if (dn != null) {
								// ICometDriverConnection c;
								File mod = new File("drivers/" + this.getDriverName() + "/devices/" + dn.getFilename());
								if (mod.exists()) {
									// update watchdog file
									utils.updateWatchdog();
									drvManager.importModelFile(mod.getPath());
									// now we need to update namespacetable
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
									dps.addAll(importer.loadDPs(input, getNamespaceTable()));
								}
							}
						}
					}
				}
				// if (obj[node.getIndex()].toString().compareTo(node.getValue()) != 0)
				// {
				// nodes.add(node.getDeviceNodeId());
				// this.logger.log1(
				// "Device to remove from address space found! Nodeid: "
				// + node.getConfigNodeId().toString(),
				// CometModuls.STR_DRV, CometModuls.INT_DRV,
				// BUNDLEID, VERSIONID);
				// }
			}
			// now remove all devices which doesn't are configured
			// this.removeNotRequiredNodes(nodes);
			manager.setDpItems(new HashMap<NodeId, XML_DA_DPItem>());
			// manager.addDpByHandle(new HashMap<String, List<XML_DA_DPItem>>());
			// now load all trigger and schedule nodes
			this.createTriggerList(manager.getTriggerList(), ns);
			this.createScheduleList(manager.getDpItems(), dps, ns);
		} catch (IOException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage());
		} finally {
			try {
				if (input != null)
					input.close();
			} catch (IOException e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage());
			}
		}
		utils.writeLEDStatus(ComStatusUtils.RUNNING);
		return true;
	}

	@Override
	public boolean doWork() {
		if (lastwatchdogwrite + (1000L * 1000000L * 10L) < System.nanoTime()) {
			// lastwatchdogwrite = System.currentTimeMillis();
			lastwatchdogwrite = System.nanoTime();
			/**
			 * now we finished run - so update watchdog state
			 */
			utils.updateWatchdog();
//      utils.updateServerWatchdog();
		}
		// test read scalar nodes
		boolean worked = false;
		XML_DA_Device device = this.getDevice();
		// --- first check if connection is ok, otherwise return false back
		if (device.getDeviceState() == ComCommunicationStates.CLOSED) {
			return false;
		}
		// read all cyclic data points
		worked = this.getDevice().readScalarNodes() || worked;
		worked = this.getDevice().readArrayNodes() || worked;
		return worked;
	}

	@Override
	public boolean doFinish() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean start() {
		this.device.connect();
		// getDevice().registerNodes();
		return true;
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

	@Override
	public void setDeviceAddress(String address) {
		String[] content = address.split(";");
		// 0.item url
		// 1.item username
		// 2.item password
		if (content != null && content.length >= 3 && device != null) {
			this.getDevice().setXml_da_server_url(content[0]);
			this.getDevice().setXml_da_username(content[1]);
			this.getDevice().setXml_da_password(content[2]);
			if (content.length >= 4) {
				try {
					this.getDevice().setXml_da_connecttimeout(Integer.parseInt(content[3]));
				} catch (NumberFormatException ex) {
					System.out.println("timeout has wrong format: " + content[3]);
				}
			}
		}
	}

	@Override
	public void setDrvProperties(String drvProperties) {
		if (drvProperties != null && !drvProperties.isEmpty()) {
			String[] props = drvProperties.split(";");
			if (props != null && props.length == 3) {
				String c1 = props[0].replace("max_dp_count", "");
				String c2 = props[1].replace("max_scalar_dp_count", "");
				String c3 = props[2].replace("max_array_dp_count", "");
				if (this.getDevice() != null) {
					try {
						this.getDevice().setMaxDPCount(Integer.parseInt(c1));
						this.getDevice().setMaxScalarDPCount(Integer.parseInt(c2));
						this.getDevice().setMaxArrayDPCount(Integer.parseInt(c3));
					} catch (NumberFormatException ex) {
					}
				}
			}
		}
		super.setDrvProperties(drvProperties);
	}

	private void createScheduleList(Map<NodeId, XML_DA_DPItem> map, List<XML_DA_DPItem> dps, NamespaceTable uris) {
		if (map != null) {
			Map<Long, XML_DA_ItemList<XML_DA_DPItem>> nmap = null;
			XML_DA_ItemList<XML_DA_DPItem> nodes = null;
			if (dps != null) {
				for (XML_DA_DPItem dp : dps) {
					/**
					 * add dp to schedule list
					 */
					if (dp.isActive()) {
						map.put(dp.getNodeId(), dp);
						List<XML_DA_DPItem> dph = manager.getDpByHandle()
								.get(dp.getItemPath() + "/" + dp.getItemName());
						if (dph == null) {
							dph = new ArrayList<>();
							manager.getDpByHandle().put(dp.getItemPath() + "/" + dp.getItemName(), dph);
						}
						dph.add(dp);
						// take scalar list
						if (dp.getMapping() == XML_DA_MAPPING_TYPE.SCALAR) {
							nmap = this.manager.getScalarNodes();
							if (nmap == null) {
								nmap = new HashMap<Long, XML_DA_ItemList<XML_DA_DPItem>>();
								this.manager.setScalarNodes(nmap);
							}
							nodes = nmap.get((long) dp.getCycletime());
							if (nodes == null) {
								nodes = new XML_DA_ItemList<>();
								nmap.put((long) dp.getCycletime(), nodes);
							}
						} else if (dp.getMapping() == XML_DA_MAPPING_TYPE.ARRAY_ARRAY
								|| dp.getMapping() == XML_DA_MAPPING_TYPE.ALARM_MAPPING) {
							nmap = this.manager.getArraynodes();
							if (nmap == null) {
								nmap = new HashMap<Long, XML_DA_ItemList<XML_DA_DPItem>>();
								this.manager.setArraynodes(nmap);
							}
							nodes = nmap.get((long) dp.getCycletime());
							if (nodes == null) {
								nodes = new XML_DA_ItemList<>();
								nmap.put((long) dp.getCycletime(), nodes);
							}
						}
						nodes.add(dp);
						/**
						 * add displayname to node to print any messages for the datapoint
						 */
						DataValue response = this.manager.getDrvManager().readFromDriver(dp.getNodeId(),
								Attributes.DisplayName, null, null, (long) drvId, 0.0, TimestampsToReturn.Both);
						if (response != null && response.getValue() != null && response.getValue().getValue() != null) {
							dp.setDisplayname(((LocalizedText) response.getValue().getValue()).getText());
						}
						/** first we add namespaceuri */
						response = this.manager.getDrvManager().readFromDriver(dp.getNodeId(), Attributes.Value, null,
								null, (long) drvId, 0.0, TimestampsToReturn.Both);
						/* check the namespace object */
						if (response != null && response.getValue() != null && response.getValue().getValue() != null) {
							// did we get an array value
							if (response.getValue().isArray()) {
								Object[] array = (Object[]) response.getValue().getValue();
								dp.setArraylength(array.length);
							}
							// set value to internal
							XML_DA_TransformFactory.createTransform(dp, response.getValue().getCompositeClass());
							if (dp.getTransform() != null) {
								dp.setArray(dp.getTransform().createInternArray(dp.getArraylength()));
							}
						}
						this.manager.getManager().getDriverConnection().setDriverReadConnected(dp.getNodeId(), true,
								ComDRVManager.ASYNCREAD, drvId);
						this.manager.getManager().getDriverConnection().setDriverWriteConnected(dp.getNodeId(), true,
								ComDRVManager.SYNCWRITE, drvId);
					}
					if (dp.getTriggerNode() != null) {
						if (manager.getTriggerList().containsKey(dp.getTriggerNode().toString())) {
							// this difficult line gets the triggerlist from
							// manager, then we get the triggeritem by
							// triggernodeid of datapoint, at least we add
							// the actual datapoint to that trigger node
							manager.getTriggerList().get(dp.getTriggerNode().toString()).getNodesToRead()
									.add(dp.getNodeId());
						}
					}
				}
			}
			return;
		}
	}

	private void initializeHandler() {
		XML_DA_ReadHandler read = new XML_DA_ReadHandler();
		read.setManager(manager);
		manager.getDrvManager().addReadListener((long) drvId, read);
		XML_DA_WriteHandler write = new XML_DA_WriteHandler();
		write.setManager(manager);
		manager.getDrvManager().addWriteListener((long) drvId, write);
	}
}
