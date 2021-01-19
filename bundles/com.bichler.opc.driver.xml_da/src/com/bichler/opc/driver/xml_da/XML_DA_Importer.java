package com.bichler.opc.driver.xml_da;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.common.NamespaceTable;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.bichler.opc.driver.xml_da.dp.XML_DA_BooleanItem;
import com.bichler.opc.driver.xml_da.dp.XML_DA_ByteItem;
import com.bichler.opc.driver.xml_da.dp.XML_DA_DATATYPE;
import com.bichler.opc.driver.xml_da.dp.XML_DA_DPItem;
import com.bichler.opc.driver.xml_da.dp.XML_DA_DoubleItem;
import com.bichler.opc.driver.xml_da.dp.XML_DA_FloatItem;
import com.bichler.opc.driver.xml_da.dp.XML_DA_IntegerItem;
import com.bichler.opc.driver.xml_da.dp.XML_DA_LongItem;
import com.bichler.opc.driver.xml_da.dp.XML_DA_ShortItem;
import com.bichler.opc.driver.xml_da.dp.XML_DA_StringItem;
import com.bichler.opc.driver.xml_da.dp.XML_DA_TriggerDpItem;
import com.bichler.opc.driver.xml_da.dp.XML_DA_UnsignedByteItem;
import com.bichler.opc.driver.xml_da.dp.XML_DA_UnsignedIntItem;
import com.bichler.opc.driver.xml_da.dp.XML_DA_UnsignedLongItem;
import com.bichler.opc.driver.xml_da.dp.XML_DA_UnsignedShortItem;
import com.bichler.opc.driver.xml_da.transform.XML_DA_MAPPING_TYPE;

public class XML_DA_Importer {
	public XML_DA_Importer() {
	}

	/**
	 * creates and returns a list with all dps which trigger an read for other
	 * datapoints
	 * 
	 * @param stream
	 * @param uris
	 * @return
	 */
	public List<XML_DA_TriggerDpItem> loadTriggerDPs(InputStream stream, NamespaceTable uris) {
		List<XML_DA_TriggerDpItem> dps = new ArrayList<XML_DA_TriggerDpItem>();
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
			String line = "";
			String[] items = null;
			while ((line = reader.readLine()) != null) {
				try {
					items = line.split("\t");
					if (items != null && items.length == 2) {
						// we also need to get the namespace index from server
						String[] nitems = items[0].split(";");
						if (nitems != null && nitems.length == 2) {
							// now create node to tigger
							XML_DA_TriggerDpItem node = new XML_DA_TriggerDpItem();
							node.setNodesToRead(new ArrayList<NodeId>());
							int nsindex = uris.getIndex(nitems[0].replace("ns=", ""));
							if (nsindex != -1) {
								node.setNodeId(NodeId.parseNodeId("ns=" + nsindex + ";" + nitems[1]));
								node.setActive(Boolean.parseBoolean(items[1]));
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

	public List<XML_StartConfigNode> loadDeviceConfig(InputStream stream, NamespaceTable uris) {
		List<XML_StartConfigNode> dps = new ArrayList<XML_StartConfigNode>();
		SAXParserFactory factory = SAXParserFactory.newInstance();
		try {
			SAXParser parser = factory.newSAXParser();
			StartConfigParser dpparser = new StartConfigParser(dps, uris);
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
		private List<XML_StartConfigNode> nodes;
		private NamespaceTable uris;

		public StartConfigParser(List<XML_StartConfigNode> nodes, NamespaceTable uris) {
			this.nodes = nodes;
			this.uris = uris;
		}

		@Override
		public void endElement(String uri, String localName, String name) throws SAXException {
		}

		@Override
		public void startElement(String uri, String localname, String name, Attributes attrs) throws SAXException {
			if (name.compareTo("node") == 0) {
				XML_StartConfigNode config = new XML_StartConfigNode();
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

	public List<XML_DA_DPItem> loadDPs(InputStream stream, NamespaceTable uris) {
		List<XML_DA_DPItem> dps = new ArrayList<XML_DA_DPItem>();
		SAXParserFactory factory = SAXParserFactory.newInstance();
		try {
			SAXParser parser = factory.newSAXParser();
			DatapointsParser dpparser = new DatapointsParser(dps, uris);
			Reader reader = new InputStreamReader(stream, "UTF-8");
			InputSource is = new InputSource(reader);
			is.setEncoding("UTF-8");
			parser.parse(is, dpparser);
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

	class DatapointsParser extends DefaultHandler {
		private NodeId nodeid = null;
		private String symbolname = "";
		private String displayname = "";
		private boolean active = false;
		private int cycletime = -1;
		private String itempath = "";
		private String itemname = "";
		private XML_DA_DPItem actNode = null;
		private List<XML_DA_DPItem> nodes = null;
		private String[] items = null;
		private NamespaceTable uris = null;
		private XML_DA_MAPPING_TYPE mapping = XML_DA_MAPPING_TYPE.SCALAR;

		public DatapointsParser(List<XML_DA_DPItem> nodes, NamespaceTable uris) {
			this.nodes = nodes;
			this.uris = uris;
		}

		@Override
		public void endElement(String uri, String localName, String name) throws SAXException {
		}

		@Override
		public void startElement(String uri, String localname, String name, Attributes attrs) throws SAXException {
			if (name.compareTo("dp") == 0) {
				// actNode = new SiemensDPItem();
			} else if (name.compareTo("nodeid") == 0) {
				try {
					items = attrs.getValue("value").split(";");
					if (items != null && items.length == 2) {
						int nsindex = uris.getIndex(items[0].replace("ns=", ""));
						if (nsindex != -1) {
							nodeid = NodeId.parseNodeId("ns=" + nsindex + ";" + items[1]);
							// actNode.setNodeId(NodeId.decode("ns=" + nsindex +
							// ";" + items[1]));
						}
					}
				} catch (NumberFormatException ex) {
					// TODO logg numberformatexception
				}
			} else if (name.compareTo("symbolname") == 0) {
				symbolname = attrs.getValue("value");
				// actNode.setSymbolname(attrs.getValue("value"));
				// actNode.setBrowsename(attrs.getValue("value"));
			} else if (name.compareTo("isactive") == 0) {
				active = Boolean.parseBoolean(attrs.getValue("value"));
				// actNode.setActive(Boolean.parseBoolean(attrs.getValue("value")));
			} else if (name.compareTo("cycletime") == 0) {
				try {
					cycletime = Integer.parseInt(attrs.getValue("value"));
					// actNode.setCycletime(Integer.parseInt(attrs
					// .getValue("value")));
				} catch (NumberFormatException ex) {
					// TODO log numberformatexception
				}
			} else if (name.compareTo("itempath") == 0) {
				itempath = attrs.getValue("value");
			} else if (name.compareTo("itemname") == 0) {
				itemname = attrs.getValue("value");
			} else if (name.compareTo("mapping") == 0) {
				try {
					mapping = XML_DA_MAPPING_TYPE.valueOf(attrs.getValue("value"));
				} catch (IllegalArgumentException ex) {
					// TODO log argument exception
					ex.printStackTrace();
				}
			} else if (name.compareTo("datatype") == 0) {
				XML_DA_DATATYPE dt = XML_DA_DATATYPE.ANY;
				int arraylength = 0;
				try {
					dt = XML_DA_DATATYPE.valueOf(attrs.getValue("value"));
				} catch (IllegalArgumentException ex) {
					// now it is possible to have an array
					String datatype = attrs.getValue("value");
					if (datatype.contains("[") && datatype.contains("]")) {
						// now extract array length
						String d = datatype.substring(0, datatype.indexOf("[")).trim();
						String arraycount = datatype.substring(datatype.indexOf("["));
						arraycount = arraycount.replace("[", "").replaceAll("]", "").trim();
						try {
							// try to parse int to integer
							arraylength = Integer.parseInt(arraycount);
							dt = XML_DA_DATATYPE.valueOf(d);
						} catch (NumberFormatException nex) {
						}
					}
				}
				switch (dt) {
				case BOOL:
					actNode = new XML_DA_BooleanItem();
					break;
				case BYTE:
					actNode = new XML_DA_ByteItem();
					break;
				case SHORT:
					actNode = new XML_DA_ShortItem();
					break;
				case INT:
					actNode = new XML_DA_IntegerItem();
					break;
				case LONG:
					actNode = new XML_DA_LongItem();
					break;
				case REAL:
					actNode = new XML_DA_FloatItem();
					break;
				case DOUBLE:
					actNode = new XML_DA_DoubleItem();
					break;
				case UNSIGNEDBYTE:
					actNode = new XML_DA_UnsignedByteItem();
					break;
				case UNSIGNEDSHORT:
					actNode = new XML_DA_UnsignedShortItem();
					break;
				case UNSIGNEDINT:
					actNode = new XML_DA_UnsignedIntItem();
					break;
				case UNSIGNEDLONG:
					actNode = new XML_DA_UnsignedLongItem();
					break;
				case STRING:
					actNode = new XML_DA_StringItem();
					break;
				default:
					actNode = new XML_DA_DPItem();
					break;
				}
				// if we have an arrays
				if (mapping == XML_DA_MAPPING_TYPE.SCALAR_ARRAY || mapping == XML_DA_MAPPING_TYPE.ARRAY_ARRAY)
					actNode.setArraylength(arraylength);
				actNode.setDataType(dt);
				actNode.setNodeId(nodeid);
				actNode.setSymbolname(symbolname);
				actNode.setDisplayname(displayname);
				actNode.setActive(active);
				actNode.setCycletime(cycletime);
				actNode.setItemPath(itempath);
				actNode.setItemName(itemname);
				actNode.setMapping(mapping);
				nodes.add(actNode);
				// actNode.getLogger().log1(
				// "Datapoint cretated Nodeid: " + actNode.getNodeId()
				// + " type: "
				// + actNode.getClass().getSimpleName(),
				// CometModuls.STR_DRV, CometModuls.INT_DRV,
				// CometDRV.BUNDLEID, CometDRV.VERSIONID);
			} else if (name.compareTo("description") == 0) {
				actNode.setDescription(attrs.getValue("value"));
			}
		}
	}
}
