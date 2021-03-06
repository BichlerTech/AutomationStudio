package com.bichler.opc.comdrv.importer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.common.NamespaceTable;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.bichler.opc.comdrv.importer.Com_DeviceConfigNode.DeviceNode;

public class Com_Importer {
	public List<Com_StartConfigNode> loadStartConfigNodes(InputStream stream, NamespaceTable uris) {
		List<Com_StartConfigNode> dps = new ArrayList<Com_StartConfigNode>();
		SAXParserFactory factory = SAXParserFactory.newInstance();
		try {
			SAXParser parser = factory.newSAXParser();
			Com_Importer.StartConfigParser dpparser = new StartConfigParser(dps, uris);
			parser.parse(stream, dpparser);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dps;
	}

	class StartConfigParser extends DefaultHandler {
		private List<Com_StartConfigNode> nodes;
		private NamespaceTable uris;

		// private boolean isactive = false;
		public StartConfigParser(List<Com_StartConfigNode> nodes, NamespaceTable uris) {
			this.nodes = nodes;
			this.uris = uris;
		}

		@Override
		public void endElement(String uri, String localName, String name) throws SAXException {
		}

		@Override
		public void startElement(String uri, String localname, String name, Attributes attrs) throws SAXException {
			if (name.compareTo("main") == 0) {
				// isactive = Boolean.parseBoolean(attrs.getValue("isactive"));
			} else if (name.compareTo("node") == 0) {
				Com_StartConfigNode config = new Com_StartConfigNode();
				config.setActive(Boolean.parseBoolean(attrs.getValue("isactive")));
				config.setDeviceNS(attrs.getValue("devicens"));
				config.setDeviceID(attrs.getValue("deviceid"));
				config.setConfignodeidNS(attrs.getValue("confignodeidns"));
				config.setConfignodeID(attrs.getValue("confignodeid"));
				config.setIndex(Integer.parseInt(attrs.getValue("index")));
				config.setValue(attrs.getValue("value"));
				config.setConfignodeName(attrs.getValue("confignodename"));
				config.setDeviceName(attrs.getValue("devicename"));
				int nsindex = uris.getIndex(config.getDeviceNS());
				NodeId nodeid = NodeId.parseNodeId("ns=" + nsindex + ";" + config.getDeviceID());
				config.setDeviceNodeId(nodeid);
				nsindex = uris.getIndex(config.getConfignodeidNS());
				nodeid = NodeId.parseNodeId("ns=" + nsindex + ";" + config.getConfignodeID());
				config.setConfigNodeId(nodeid);
				this.nodes.add(config);
			}
		}
	}

	public Com_DeviceConfigNode loadDeviceConfig(InputStream stream, NamespaceTable uris) {
		Com_DeviceConfigNode dps = new Com_DeviceConfigNode();
		SAXParserFactory factory = SAXParserFactory.newInstance();
		try {
			SAXParser parser = factory.newSAXParser();
			Com_Importer.DeviceConfigParser dpparser = new DeviceConfigParser(dps, uris);
			parser.parse(stream, dpparser);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dps;
	}

	class DeviceConfigParser extends DefaultHandler {
		private Com_DeviceConfigNode node;
		private NamespaceTable uris;
		private int index = 1;

		public DeviceConfigParser(Com_DeviceConfigNode node, NamespaceTable uris) {
			this.node = node;
			this.uris = uris;
		}

		@Override
		public void endElement(String uri, String localName, String name) throws SAXException {
		}

		@Override
		public void startElement(String uri, String localname, String name, Attributes attrs) throws SAXException {
			if (name.compareTo("main") == 0) {
				node.setActive(Boolean.parseBoolean(attrs.getValue("isactive")));
				node.setConfignodeName(attrs.getValue("config"));
				node.setConfignodeidNS(attrs.getValue("confignodeidns"));
				node.setConfignodeID(attrs.getValue("confignodeid"));
				int nsindex = uris.getIndex(node.getConfignodeidNS());
				if (nsindex >= 0) {
					NodeId nodeid = NodeId.parseNodeId("ns=" + nsindex + ";" + node.getConfignodeID());
					node.setConfigNodeId(nodeid);
				}
			} else if (name.compareTo("node") == 0) {
				// if (!Boolean.parseBoolean(attrs.getValue("isactive")))
				// return;
				DeviceNode dev = new Com_DeviceConfigNode().new DeviceNode();
				dev.setDeviceNS(attrs.getValue("devicens"));
				dev.setDeviceID(attrs.getValue("deviceid"));
				dev.setValue(Integer.parseInt(attrs.getValue("value")));
				dev.setDeviceName(attrs.getValue("devicename"));
				dev.setFilename("device_" + index + ".jar");
				dev.setMappingFile("datapoints_" + index + ".com");
				index++;
				int nsindex = uris.getIndex(dev.getDeviceNS());
				if (nsindex >= 0) {
					NodeId nodeid = NodeId.parseNodeId("ns=" + nsindex + ";" + dev.getDeviceID());
					dev.setDeviceNodeId(nodeid);
				}
				node.getDevices().put(dev.getValue(), dev);
			}
		}
	}

	public List<Com_CounterConfigGroup> loadCounterConfig(InputStream stream, NamespaceTable uris) {
		List<Com_CounterConfigGroup> groups = new ArrayList<Com_CounterConfigGroup>();
		SAXParserFactory factory = SAXParserFactory.newInstance();
		try {
			SAXParser parser = factory.newSAXParser();
			CounterConfigParser dpparser = new CounterConfigParser(groups, uris);
			parser.parse(stream, dpparser);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return groups;
	}

	class CounterConfigParser extends DefaultHandler {
		private NamespaceTable uris;
		private List<Com_CounterConfigGroup> groups = null;
		private boolean isactive = false;
		private NodeId configid = null;
		private String confignodename = "";
		private String confignodeidns = "";
		private String confignodeid = "";
		Com_CounterConfigGroup group = null;

		public CounterConfigParser(List<Com_CounterConfigGroup> groups, NamespaceTable uris) {
			this.groups = groups;
			this.uris = uris;
		}

		@Override
		public void endElement(String uri, String localName, String name) throws SAXException {
		}

		@Override
		public void startElement(String uri, String localname, String name, Attributes attrs) throws SAXException {
			if (name.compareTo("groups") == 0) {
				isactive = Boolean.parseBoolean(attrs.getValue("isactive"));
				confignodeidns = attrs.getValue("confignodeidns");
				confignodeid = attrs.getValue("confignodeid");
				// counter.setIndex(Integer.parseInt(attrs.getValue("index")));
				confignodename = attrs.getValue("confignodename");
				int nsindex = uris.getIndex(confignodeidns);
				if (nsindex >= 0) {
					configid = NodeId.parseNodeId("ns=" + nsindex + ";" + confignodeid);
				}
			} else if (name.compareTo("group") == 0) {
				if (configid != null) {
					group = new Com_CounterConfigGroup();
					group.setActive(isactive);
					group.setConfigNodeId(configid);
					group.setConfignodeName(confignodename);
					group.setConfignodeidNS(confignodeidns);
					group.setConfignodeID(confignodeid);
					this.groups.add(group);
				}
			} else if (name.compareTo("device") == 0) {
				if (group != null) {
					group.getCounterlist().add(new Com_CounterConfigNode());
				}
			} else if (name.compareTo("counter") == 0) {
				if (group != null) {
					List<Com_CounterConfigNode> cg = group.getCounterlist();
					Com_CounterConfigNode node = cg.get(cg.size() - 1);
					String ns = attrs.getValue("confignodeidns");
					String id = attrs.getValue("confignodeid");
					int nsindex = uris.getIndex(ns);
					if (nsindex >= 0) {
						NodeId nodeid = NodeId.parseNodeId("ns=" + nsindex + ";" + id);
						if (nodeid != null) {
							if (node != null) {
								node.getCounterlist().add(nodeid);
							}
						}
					}
				}
			}
		}
	}

	/**
	 * creates and returns a list with all dps which trigger an read for other
	 * datapoints
	 * 
	 * @param stream
	 * @param uris
	 * @return
	 */
	public List<Com_TriggerDpItem> loadTriggerDPs(InputStream stream, NamespaceTable uris) {
		List<Com_TriggerDpItem> dps = new ArrayList<Com_TriggerDpItem>();
		// List<NodeId> possibleNIDs = new ArrayList<NodeId>();
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
			String line = "";
			String[] items = null;
			while ((line = reader.readLine()) != null) {
				try {
					items = line.split("\t");
					if (items != null && items.length == 4) {
						// we also need to get the namespace index from server
						String[] nitems = items[0].split(";");
						if (nitems != null && nitems.length == 2) {
							// now create node to tigger
							Com_TriggerDpItem node = new Com_TriggerDpItem();
							node.setNodesToRead(new ArrayList<NodeId>());
							int nsindex = uris.getIndex(nitems[0].replace("ns=", ""));
							if (nsindex != -1) {
								node.setNodeId(NodeId.parseNodeId("ns=" + nsindex + ";" + nitems[1]));
								// possibleNIDs.add(node.getNodeId());
								node.setActive(Boolean.parseBoolean(items[1]));
								try {
									node.setIndex(Integer.parseInt(items[2]));
								} catch (NumberFormatException ex) {
									node.setIndex(-1);
								}
								node.setTriggerID(items[3]);
								dps.add(node);
							}
						}
					}
				} catch (Exception ex) {
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dps;
	}
}
