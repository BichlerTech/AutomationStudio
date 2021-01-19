package com.bichler.astudio.opcua.handlers.events;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import opc.sdk.core.node.Node;
import org.eclipse.core.internal.preferences.Base64;
import org.eclipse.core.runtime.Path;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.common.NamespaceTable;
import org.opcfoundation.ua.core.IdType;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.bichler.astudio.filesystem.IFileSystem;
import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.opcua.widget.ConsumptionTemplate;
import com.bichler.astudio.opcua.widget.NodeToTrigger;
import com.bichler.astudio.opcua.widget.model.AbstractAdvanedParser;
import com.bichler.astudio.opcua.widget.model.AbstractConfigNode;
import com.bichler.astudio.opcua.widget.model.AdvancedConfigurationNode;
import com.bichler.astudio.opcua.widget.model.AdvancedConfigurationNodeParser;
import com.bichler.astudio.opcua.widget.model.AdvancedGroupNodeParser;
import com.bichler.astudio.opcua.widget.model.AdvancedRootConfigurationNode;
import com.bichler.astudio.opcua.widget.model.AdvancedSectionType;
import com.bichler.astudio.opcua.widget.model.DeviceConsumption;

public class AdvancedDriverPersister {

	public AdvancedDriverPersister() {

	}

	public void exportCounters(IFileSystem fs, NamespaceTable nsTable, String path,
			AdvancedRootConfigurationNode input) {
		String deviceGroups = new Path(path).append("counter.com").toOSString();
		try {
			if (fs.isFile(deviceGroups)) {
				fs.removeFile(deviceGroups);
			}

			if (input != null && input.getChildren() != null && !fs.isFile(deviceGroups)) {
				fs.addFile(deviceGroups);
			}

			if (fs.isFile(deviceGroups)) {
				OutputStream output = null;
				try {
					// get the namespace table
					// NamespaceTable uris = this.opcServer.getServerInstance()
					// .getNamespaceUris();

					StringBuffer buffer = new StringBuffer();
					buffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
					saveDeviceCounters(nsTable, buffer, input);
					output = fs.writeFile(deviceGroups);
					output.write(buffer.toString().getBytes());
					output.flush();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if (output != null) {
						try {
							output.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public void exportDeviceConfig(IFileSystem fs, NamespaceTable nsTable, String path,
			AdvancedRootConfigurationNode input) {
		String deviceconfigNodes = new Path(path).append("deviceconfig.com").toOSString();

		try {
			if (fs.isFile(deviceconfigNodes)) {
				fs.removeFile(deviceconfigNodes);
			}

			if (input != null && input.getChildren() != null && !fs.isFile(deviceconfigNodes)) {
				fs.addFile(deviceconfigNodes);
			}

			if (fs.isFile(deviceconfigNodes)) {
				// if(file.exists()) {
				OutputStream output = null;
				try {
					// get the namespace table
					// NamespaceTable uris =
					// this.opcServer.getServerInstance()
					// .getNamespaceUris();

					StringBuffer buffer = new StringBuffer();
					buffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
					saveDeviceConfig(nsTable, buffer, input);
					output = fs.writeFile(deviceconfigNodes);
					output.write(buffer.toString().getBytes());
					output.flush();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if (output != null) {
						try {
							output.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public void exportDeviceMapping(IFileSystem fs, NamespaceTable nsTable, String path,
			AdvancedRootConfigurationNode input) {
		String deviceMappings = new Path(path).append("devicemapping.com").toOSString();
		try {
			if (fs.isFile(deviceMappings)) {
				fs.removeFile(deviceMappings);
			}

			if (input != null && input.getChildren() != null && !fs.isFile(deviceMappings)) {
				fs.addFile(deviceMappings);
			}

			if (fs.isFile(deviceMappings)) {
				OutputStream output = null;
				try {
					// get the namespace table
					// NamespaceTable uris = this.opcServer.getServerInstance()
					// .getNamespaceUris();
					StringBuffer buffer = new StringBuffer();
					buffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
					saveDeviceMappings(nsTable, buffer, input);
					output = fs.writeFile(deviceMappings);
					output.write(buffer.toString().getBytes());
					output.flush();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if (output != null) {
						try {
							output.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public void exportStartConfig(IFileSystem fs, NamespaceTable nsTable, String path,
			AdvancedRootConfigurationNode input) {
		/**
		 * try to save trigger nodes
		 */
		String startconfigNodes = new Path(path).append("startconfignodes.com").toOSString();
		try {
			if (fs.isFile(startconfigNodes)) {
				fs.removeFile(startconfigNodes);
			}

			if (input != null && input.getChildren() != null && input.getChildren().length > 0
					&& !fs.isFile(startconfigNodes)) {
				fs.addFile(startconfigNodes);
			}

			if (fs.isFile(startconfigNodes)) {
				// if(file.exists()) {
				OutputStream output = null;
				try {
					// get the namespace table
					// NamespaceTable uris = this.opcServer.getServerInstance()
					// .getNamespaceUris();
					StringBuffer buffer = new StringBuffer();
					buffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
					saveStartConfig(nsTable, buffer, input);
					output = fs.writeFile(startconfigNodes);
					output.write(buffer.toString().getBytes());
					output.flush();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if (output != null) {
						try {
							output.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public void exportConsumptionConfig(IFileSystem fs, NamespaceTable nsTable, String path, String checked,
			List<ConsumptionTemplate> states) {

		String stateconfiguration = new Path(path).append("consumptionconfiguration.com").toOSString();
		try {
			if (fs.isFile(stateconfiguration)) {
				fs.removeFile(stateconfiguration);
			}

			if (states != null) {
				fs.addFile(stateconfiguration);
			}
			if (fs.isFile(stateconfiguration)) {
				// if(file.exists()) {
				OutputStream output = null;
				try {
					// get the namespace table
					// NamespaceTable uris = this.opcServer.getServerInstance()
					// .getNamespaceUris();
					StringBuffer buffer = new StringBuffer();
					buffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
					saveConsumptionConfig(nsTable, buffer, checked, states);
					output = fs.writeFile(stateconfiguration);
					output.write(buffer.toString().getBytes());
					output.flush();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if (output != null) {
						try {
							output.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public boolean exportTriggerNodes(NamespaceTable nsTable, IFileSystem filesystem, String path,
			List<NodeToTrigger> triggernodes) {
		// String trigger = new
		// Path(path).append("triggernodes.com").toOSString();

		if (triggernodes.isEmpty()) {
			return true;
		}

		try {
			if (!filesystem.isFile(path)) {
				filesystem.addFile(path);
			}

			if (filesystem.isFile(path)) {
				OutputStream output = null;
				try {
					output = filesystem.writeFile(path);
					for (NodeToTrigger n : triggernodes) {
						if (n.nodeId != null) {
							String[] idelements = n.nodeId.toString().split(";");
							String id = "";
							if (idelements != null) {
								if (idelements.length == 1) {
									id = idelements[0];
								} else if (idelements.length == 2) {
									id = idelements[1];
								}
							}
							String nsuri = nsTable.getUri(n.nodeId.getNamespaceIndex());
							String index = "" + n.index;
							output.write(("ns=" + nsuri + ";" + id + "\t" + n.active + "\t" + index + "\t"
									+ n.triggerName + "\n").getBytes());
						}
					}

					output.flush();
				} catch (IOException e) {
					e.printStackTrace();
					return false;
				} finally {
					if (output != null) {
						output.close();
					}
				}
			}
		} catch (IOException ex) {
			ex.printStackTrace();
			return false;
		}
		return true;
	}

	public DeviceConsumption importDeviceConsumption(IFileSystem filesystem, String path, DefaultHandler handler) {

		if (filesystem.isFile(path)) {
			InputStreamReader reader = null;
			try {
				InputStream input = filesystem.readFile(path);
				reader = new InputStreamReader(input, "UTF-8");

				InputSource source = new InputSource(reader);

				SAXParser parser = SAXParserFactory.newInstance().newSAXParser();

				parser.parse(source, handler);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			} catch (SAXException e) {
				e.printStackTrace();
			} finally {
				if (reader != null) {
					try {
						reader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return null;
	}

	public AdvancedRootConfigurationNode importAdvancedSettings(NamespaceTable nsTable, IFileSystem filesystem,
			String path, AbstractAdvanedParser handler, AdvancedSectionType sectionType) {

		AdvancedRootConfigurationNode root = new AdvancedRootConfigurationNode(sectionType);
		// if (handler.getRoot() == null) {
		handler.setRoot(root);
		// }
		if (filesystem.isFile(path)) {
			InputStream input = null;
			try {
				input = filesystem.readFile(path);

				SAXParser parser = SAXParserFactory.newInstance().newSAXParser();

				// AdvancedConfigurationNodeParser saxHandler = new
				// AdvancedConfigurationNodeParser(
				// nsTable, root, sectionType);
				parser.parse(input, handler);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			} catch (SAXException e) {
				e.printStackTrace();
			} finally {
				if (input != null) {
					try {
						input.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return root;
	}

	public List<NodeToTrigger> importTriggerNodes(NamespaceTable nsTable, IFileSystem filesystem, String triggerpath) {

		List<NodeToTrigger> triggernodes = new ArrayList<>();

		BufferedReader reader = null;
		try {
			// String triggerpath = new Path(path).append("triggernodes.com")
			// .toOSString();
			if (!filesystem.isFile(triggerpath)) {
				return triggernodes;
			}
			// read trigger nodes
			InputStream input = filesystem.readFile(triggerpath);
			reader = new BufferedReader(new InputStreamReader(input));

			String line = "";
			String[] items = null;

			while ((line = reader.readLine()) != null) {
				try {
					items = line.split("\t");
					if (items != null && items.length >= 3) {
						// we also need to get the namespace index from server

						String[] nitems = items[0].split(";");
						if (nitems != null && nitems.length == 2) {
							// now create node to tigger
							NodeToTrigger node = new NodeToTrigger();
							int nsindex = nsTable.getIndex(nitems[0].replace("ns=", ""));
							if (nsindex != -1) {
								node.nodeId = NodeId.parseNodeId("ns=" + nsindex + ";" + nitems[1]);
								node.active = Boolean.parseBoolean(items[1]);
								node.index = Integer.parseInt(items[2]);
								if (items.length > 3)
									node.triggerName = items[3];

								// set display name to trigger node
								Node n = ServerInstance.getInstance().getServerInstance().getAddressSpaceManager()
										.getNodeById(node.nodeId);

								if (n != null) {
									node.displayname = n.getDisplayName().getText();
								}
								triggernodes.add(node);
							}
						}
					}

				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return triggernodes;
	}

	void saveDeviceConfig(NamespaceTable nsTable, StringBuffer buffer, AdvancedRootConfigurationNode input) {

		if (input == null) {
			buffer.append("<" + AdvancedConfigurationNodeParser.StartTag + " />");
		} else {
			NodeId configId = input.getRefNodeId();
			String NS_configtag = "";
			String ID_configtag = "";
			if (!NodeId.isNull(configId)) {
				NS_configtag = nsTable.getUri(configId.getNamespaceIndex());
				ID_configtag = valueToString(configId.getIdType(), configId.getValue());
			}
			buffer.append("<" + AdvancedConfigurationNodeParser.StartTag + " "
					+ AdvancedConfigurationNodeParser.ConfigNodeTagName + "=\"" + input.getRefNodeName() + "\" "
					+ AdvancedConfigurationNodeParser.ConfigNodeIdTagNS + "=\"" + NS_configtag + "\" "
					+ AdvancedConfigurationNodeParser.ConfigNodeIdTagID + "=\"" + ID_configtag + "\" " + " "
					+ AdvancedConfigurationNodeParser.IsActiveTag + "=\"" + input.isActive().toString() + "\">\n");

			for (AdvancedConfigurationNode child : input.getChildren()) {
				NodeId deviceId = child.getDeviceId();
				String NS_devicetag = nsTable.getUri(deviceId.getNamespaceIndex());
				String ID_devicetag = valueToString(deviceId.getIdType(), deviceId.getValue());

				buffer.append("<" + AdvancedConfigurationNodeParser.NodeTag + " "
						+ AdvancedConfigurationNodeParser.IsActiveTag + "=\"" + child.isActive() + "\" "
						+ AdvancedConfigurationNodeParser.DeviceTagNS + "=\"" + NS_devicetag + "\" "
						+ AdvancedConfigurationNodeParser.DeviceTagID + "=\"" + ID_devicetag + "\" "
						+ AdvancedConfigurationNodeParser.DeviceTagName + "=\"" + child.getDeviceName() + "\" "
						+ AdvancedConfigurationNodeParser.ValueTag + "=\"" + child.getValue() + "\" " + "/>\n");
			}

			buffer.append("</" + AdvancedConfigurationNodeParser.StartTag + ">");
		}
	}

	void saveDeviceCounters(NamespaceTable nsTable, StringBuffer buffer, AdvancedRootConfigurationNode input) {
		if (input == null) {
			buffer.append("<" + AdvancedGroupNodeParser.RootTag + " />");
		} else {
			NodeId data_m = input.getRefNodeId();
			String txt_m = input.getRefNodeName();
			String NS_configtag = "";
			String ID_configtag = "";

			if (data_m != null) {
				NS_configtag = nsTable.getUri(data_m.getNamespaceIndex());
				ID_configtag = valueToString(data_m.getIdType(), data_m.getValue());
			}

			buffer.append("<" + AdvancedGroupNodeParser.RootTag + " "
					+ AdvancedConfigurationNodeParser.ConfigNodeIdTagNS + "=\"" + NS_configtag + "\" "
					+ AdvancedConfigurationNodeParser.ConfigNodeIdTagID + "=\"" + ID_configtag + "\" "
					+ AdvancedConfigurationNodeParser.ConfigNodeTagName + "=\"" + txt_m + "\" "
					+ AdvancedConfigurationNodeParser.IsActiveTag + "=\"" + input.isActive().toString() + "\" >\n");

			for (AdvancedConfigurationNode child : input.getChildren()) {
				saveGroup(nsTable, buffer, child);
			}

			buffer.append("</" + AdvancedGroupNodeParser.RootTag + ">");
		}
	}

	void saveDeviceMappings(NamespaceTable nsTable, StringBuffer buffer, AdvancedRootConfigurationNode root) {

		if (root == null) {
			buffer.append("<" + AdvancedConfigurationNodeParser.StartTag + " />\n");
		} else {

			String txt_1 = root.getDatablock();
			String txt_2 = root.getStartAddress();
			String txt_3 = root.getRangeAddon();
			String txt_4 = root.getRangeGroup();
			String txt_5 = root.getMeterId();

			buffer.append("<" + AdvancedConfigurationNodeParser.StartTag + " " + AdvancedConfigurationNodeParser.DB
					+ "=\"" + txt_1 + "\" " + AdvancedConfigurationNodeParser.Start + "=\"" + txt_2 + "\" "
					+ AdvancedConfigurationNodeParser.RangeAddon + "=\"" + txt_3 + "\" "
					+ AdvancedConfigurationNodeParser.RangeGroups + "=\"" + txt_4 + "\" "
					+ AdvancedConfigurationNodeParser.RangeMeter + "=\"" + txt_5 + "\" " + " "
					+ AdvancedConfigurationNodeParser.IsActiveTag + "=\"" + root.isActive().toString() + "\" >\n");

			for (AdvancedConfigurationNode child : root.getChildren()) {
				saveExtendedConfig(nsTable, buffer, child);
			}
			buffer.append("</" + AdvancedConfigurationNodeParser.StartTag + ">\n");
		}
	}

	void saveStartConfig(NamespaceTable table, StringBuffer buffer, AdvancedRootConfigurationNode input) {

		if (input == null) {
			buffer.append("<" + AdvancedConfigurationNodeParser.StartTag + " />");
		} else {
			buffer.append("<" + AdvancedConfigurationNodeParser.StartTag + " "
					+ AdvancedConfigurationNodeParser.IsActiveTag + "=\"" + input.isActive().toString() + "\">\n");

			for (AdvancedConfigurationNode child : input.getChildren()) {

				NodeId deviceId = child.getDeviceId();
				NodeId configId = child.getConfigNodeId();

				String NS_devicetag = table.getUri(deviceId.getNamespaceIndex());
				String ID_devicetag = valueToString(deviceId.getIdType(), deviceId.getValue());

				String NS_configtag = table.getUri(configId.getNamespaceIndex());
				String ID_configtag = valueToString(configId.getIdType(), configId.getValue());

				buffer.append("<" + AdvancedConfigurationNodeParser.NodeTag + " "
						+ AdvancedConfigurationNodeParser.IsActiveTag + "=\"" + child.isActive().toString() + "\" "
						+ AdvancedConfigurationNodeParser.DeviceTagNS + "=\"" + NS_devicetag + "\" "
						+ AdvancedConfigurationNodeParser.DeviceTagID + "=\"" + ID_devicetag + "\" "
						+ AdvancedConfigurationNodeParser.ConfigNodeIdTagNS + "=\"" + NS_configtag + "\" "
						+ AdvancedConfigurationNodeParser.ConfigNodeIdTagID + "=\"" + ID_configtag + "\" "
						+ AdvancedConfigurationNodeParser.IndexTag + "=\"" + child.getIndex() + "\" "
						+ AdvancedConfigurationNodeParser.ValueTag + "=\"" + child.getValue() + "\" "
						+ AdvancedConfigurationNodeParser.ConfigNodeTagName + "=\"" + child.getConfigNodeName() + "\" "
						+ AdvancedConfigurationNodeParser.DeviceTagName + "=\"" + child.getDeviceName() + "\" "
						+ "/>\n");
			}

			buffer.append("</" + AdvancedConfigurationNodeParser.StartTag + ">");
		}
	}

	void saveConsumptionConfig(NamespaceTable nsTable, StringBuffer buffer, String checked,
			List<ConsumptionTemplate> bufferstates) {

		if (bufferstates == null) {
			buffer.append("<" + AdvancedConfigurationNodeParser.StartTag + " "
					+ AdvancedConfigurationNodeParser.IsActiveTag + "=\"" + checked + "\" />");
		} else {
			// main
			buffer.append("<" + AdvancedConfigurationNodeParser.StartTag + " "
					+ AdvancedConfigurationNodeParser.IsActiveTag + "=\"" + checked + "\" >\n");
			for (ConsumptionTemplate state : bufferstates) {
				// state
				if (state.getItems().length > 0) {
					buffer.append("<" + AdvancedConfigurationNodeParser.ConsumptionTag + " "
							+ AdvancedConfigurationNodeParser.DB + "=\"" + state.getDB() + "\" "
							+ AdvancedConfigurationNodeParser.ConsumptionAddress + "=\"" + state.getStartAddress()
							+ "\" " + AdvancedConfigurationNodeParser.ConsumptionStructLength + "=\""
							+ state.getStructLength() + "\" " + AdvancedConfigurationNodeParser.Name + "=\""
							+ state.getCategory() + "\" >\n");

					for (AbstractConfigNode node : state.getItems()) {
						String name = ((AdvancedConfigurationNode) node).getConfigNodeName();
						NodeId nodeid = ((AdvancedConfigurationNode) node).getConfigNodeId();

						String nodeid_id = "";
						if (!NodeId.isNull(nodeid)) {
							String[] idelements = nodeid.toString().split(";");
							if (idelements != null) {
								if (idelements.length == 1) {
									nodeid_id = idelements[0];
								} else if (idelements.length == 2) {
									nodeid_id = idelements[1];
								}
							}
						}

						String nodeid_ns = nsTable.getUri(nodeid.getNamespaceIndex());
						String browsepath = ((AdvancedConfigurationNode) node).getBrowsepath();
						String devicename = ((AdvancedConfigurationNode) node).getDeviceName();
						String mapping = ((AdvancedConfigurationNode) node).getMapping().name();
						String datatype = ((AdvancedConfigurationNode) node).getDataType();
						String index = "" + ((AdvancedConfigurationNode) node).getRefStartAddress();
						String enabled = "" + ((AdvancedConfigurationNode) node).isEnable();
						String cycletime = "" + ((AdvancedConfigurationNode) node).getCycletime();
						String trigger = "";
						if (((AdvancedConfigurationNode) node).getTrigger() != null) {
							trigger = ((AdvancedConfigurationNode) node).getTrigger().triggerName;
						}
						buffer.append("<" + AdvancedConfigurationNodeParser.NodeTag + " "
						// + AdvancedConfigurationNodeParser.IsActiveTag
						// +
						// "=\""
						// + bufferstates[0] + "\" "
								+ AdvancedConfigurationNodeParser.ConfigNodeTagName + "=\"" + name + "\" "
								+ AdvancedConfigurationNodeParser.ConfigNodeIdTagNS + "=\"" + nodeid_ns + "\" "
								+ AdvancedConfigurationNodeParser.ConfigNodeIdTagID + "=\"" + nodeid_id + "\" "
								+ AdvancedConfigurationNodeParser.Browsepath + "=\"" + browsepath + "\" "
								+ AdvancedConfigurationNodeParser.DeviceTagName + "=\"" + devicename + "\" "
								+ AdvancedConfigurationNodeParser.Datatype + "=\"" + datatype + "\" "
								+ AdvancedConfigurationNodeParser.Mapping + "=\"" + mapping + "\" "
								+ AdvancedConfigurationNodeParser.IndexTag + "=\"" + index + "\" "
								+ AdvancedConfigurationNodeParser.IsActiveTag + "=\"" + enabled + "\" "
								+ AdvancedConfigurationNodeParser.CycleTime + "=\"" + cycletime + "\" ");
						if (trigger != null && !trigger.isEmpty()) {
							buffer.append(AdvancedConfigurationNodeParser.TriggerNode + "=\"" + trigger + "\" ");
						}
						buffer.append("/>\n");
					}
					buffer.append("</" + AdvancedConfigurationNodeParser.ConsumptionTag + ">\n");
				} else {
					buffer.append("<" + AdvancedConfigurationNodeParser.ConsumptionTag + " "
							+ AdvancedConfigurationNodeParser.DB + "=\"" + state.getDB() + "\" "
							+ AdvancedConfigurationNodeParser.ConsumptionAddress + "=\"" + state.getStartAddress()
							+ "\" " + AdvancedConfigurationNodeParser.ConsumptionStructLength + "=\""
							+ state.getStructLength() + "\" " + AdvancedConfigurationNodeParser.Name + "=\""
							+ state.getCategory() + "\" />\n");
				}

			}

			buffer.append("</" + AdvancedConfigurationNodeParser.StartTag + ">");

			// buffer.append("</" + AdvancedConfigurationNodeParser.StartTag +
			// ">");
		}
	}

	private void saveExtendedConfig(NamespaceTable nsTable, StringBuffer buffer, AdvancedConfigurationNode node) {

		/** fill configration */
		String NS_dev = "";
		String ID_dev = "";
		String deviceName = "";
		String NS_add = "";
		String ID_add = "";
		String addonName = "";
		String NS_group = "";
		String ID_group = "";
		String groupName = "";
		String NS_meter = "";
		String ID_meter = "";
		String meterNam = "";
		String enableNS = "";
		String enableID = "";
		String enableName = "";

		NodeId deviceId = node.getDeviceId();
		if (!NodeId.isNull(deviceId)) {
			NS_dev = nsTable.getUri(deviceId.getNamespaceIndex());
			ID_dev = valueToString(deviceId.getIdType(), deviceId.getValue());
			deviceName = node.getDeviceName();
		}

		NodeId enableId = node.getEnableId();
		if (!NodeId.isNull(enableId)) {
			enableNS = nsTable.getUri(enableId.getNamespaceIndex());
			enableID = valueToString(enableId.getIdType(), enableId.getValue());
			enableName = node.getEnableName();
		}

		NodeId addonId = node.getAddonId();
		if (!NodeId.isNull(addonId)) {
			NS_add = nsTable.getUri(addonId.getNamespaceIndex());
			ID_add = valueToString(addonId.getIdType(), addonId.getValue());
			addonName = node.getAddonName();
		}

		NodeId groupId = node.getGroupId();
		if (!NodeId.isNull(groupId)) {
			NS_group = nsTable.getUri(groupId.getNamespaceIndex());
			ID_group = valueToString(groupId.getIdType(), groupId.getValue());
			groupName = node.getGroupName();
		}

		NodeId meterId = node.getMeterId();
		if (!NodeId.isNull(meterId)) {
			NS_meter = nsTable.getUri(meterId.getNamespaceIndex());
			ID_meter = valueToString(meterId.getIdType(), meterId.getValue());
			meterNam = node.getMeterName();
		}

		// isenable = "" + node.isEnable();

		buffer.append("<" + AdvancedConfigurationNodeParser.NodeTag + " " + AdvancedConfigurationNodeParser.ValueTag
				+ "=\"" + node.getValue() + "\" " + AdvancedConfigurationNodeParser.DeviceTagNS + "=\"" + NS_dev + "\" "
				+ AdvancedConfigurationNodeParser.DeviceTagID + "=\"" + ID_dev + "\" "
				+ AdvancedConfigurationNodeParser.DeviceTagName + "=\"" + deviceName + "\" "
				+ AdvancedConfigurationNodeParser.EnableTagNS + "=\"" + enableNS + "\" "
				+ AdvancedConfigurationNodeParser.EnableTagID + "=\"" + enableID + "\" "
				+ AdvancedConfigurationNodeParser.EnableTagName + "=\"" + enableName + "\" "
				+ AdvancedConfigurationNodeParser.AddonTagNS + "=\"" + NS_add + "\" "
				+ AdvancedConfigurationNodeParser.AddonTagID + "=\"" + ID_add + "\" "
				+ AdvancedConfigurationNodeParser.AddonTagName + "=\"" + addonName + "\" "
				+ AdvancedConfigurationNodeParser.GroupTagNS + "=\"" + NS_group + "\" "
				+ AdvancedConfigurationNodeParser.GroupTagID + "=\"" + ID_group + "\" "
				+ AdvancedConfigurationNodeParser.GroupTagName + "=\"" + groupName + "\" "

				+ AdvancedConfigurationNodeParser.MeterTagNS + "=\"" + NS_meter + "\" "
				+ AdvancedConfigurationNodeParser.MeterTagID + "=\"" + ID_meter + "\" "
				+ AdvancedConfigurationNodeParser.MeterTagName + "=\"" + meterNam + "\" />\n");
	}

	private void saveGroup(NamespaceTable nsTable, StringBuffer buffer, AdvancedConfigurationNode child) {
		String grName = child.getGroupName();

		buffer.append("<" + AdvancedGroupNodeParser.GroupTag + " " + AdvancedGroupNodeParser.NameTag + "=\"" + grName
				+ "\" >\n");

		for (AdvancedConfigurationNode devChild : child.getChildren()) {
			saveDevice(nsTable, buffer, devChild);
		}

		buffer.append("</" + AdvancedGroupNodeParser.GroupTag + ">\n");
	}

	private void saveDevice(NamespaceTable nsTable, StringBuffer buffer, AdvancedConfigurationNode child) {
		String devName = child.getDeviceName();

		buffer.append("<" + AdvancedGroupNodeParser.DeviceTag + " " + AdvancedGroupNodeParser.NameTag + "=\"" + devName
				+ "\">\n");

		for (AdvancedConfigurationNode stateChild : child.getChildren()) {
			saveCounter(nsTable, buffer, stateChild);
		}

		buffer.append("</" + AdvancedGroupNodeParser.DeviceTag + ">\n");
	}

	private void saveCounter(NamespaceTable nsTable, StringBuffer buffer, AdvancedConfigurationNode child) {

		String counterName = child.getCounter();
		NodeId counterId = child.getDeviceId();

		String NS_configtag = nsTable.getUri(counterId.getNamespaceIndex());
		String ID_configtag = valueToString(counterId.getIdType(), counterId.getValue());

		buffer.append("<" + AdvancedGroupNodeParser.CounterTag + " " + AdvancedGroupNodeParser.NameTag + "=\""
				+ counterName + "\" " + AdvancedGroupNodeParser.ConfigNodeIdTagNS + "=\"" + NS_configtag + "\" "
				+ AdvancedGroupNodeParser.ConfigNodeIdTagID + "=\"" + ID_configtag + "\" />\n");
	}

	private static String valueToString(IdType type, Object value) {
		if (type == IdType.Numeric)
			return "i=" + value;
		if (type == IdType.String)
			return "s=" + value;
		if (type == IdType.Guid)
			return "g=" + value;
		if (type == IdType.Opaque) {
			if (value == null)
				return "b=null";
			return "b=" + new String(Base64.encode((byte[]) value));
		}
		return "error";
	}

}
