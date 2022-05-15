package com.bichler.opc.driver.ethernet_ip;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.common.NamespaceTable;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.bichler.opc.comdrv.ComResourceManager;
import com.bichler.opc.comdrv.importer.Com_Importer;
import com.bichler.opc.driver.ethernet_ip.dp.EthernetIPBooleanItem;
import com.bichler.opc.driver.ethernet_ip.dp.EthernetIPDIntItem;
import com.bichler.opc.driver.ethernet_ip.dp.EthernetIPDPItem;
import com.bichler.opc.driver.ethernet_ip.dp.EthernetIPDataType;
import com.bichler.opc.driver.ethernet_ip.dp.EthernetIPIntItem;
import com.bichler.opc.driver.ethernet_ip.dp.EthernetIPLIntItem;
import com.bichler.opc.driver.ethernet_ip.dp.EthernetIPLRealItem;
import com.bichler.opc.driver.ethernet_ip.dp.EthernetIPRealItem;
import com.bichler.opc.driver.ethernet_ip.dp.EthernetIPSIntItem;
import com.bichler.opc.driver.ethernet_ip.dp.EthernetIPStringItem;
import com.bichler.opc.driver.ethernet_ip.dp.EthernetIPUDIntItem;
import com.bichler.opc.driver.ethernet_ip.dp.EthernetIPUIntItem;
import com.bichler.opc.driver.ethernet_ip.dp.EthernetIPULIntItem;
import com.bichler.opc.driver.ethernet_ip.dp.EthernetIPUSIntItem;
import com.bichler.opc.driver.ethernet_ip.transform.ETHERNETIP_MAPPING_TYPE;

public class ComEthernetIPImporter extends Com_Importer {

	public ComEthernetIPImporter() {

	}

	public List<EthernetIPDPItem> loadDPs(InputStream stream, NamespaceTable uris,
			ComResourceManager cometResourceManager) {
		List<EthernetIPDPItem> dps = new ArrayList<>();
		SAXParserFactory factory = SAXParserFactory.newInstance();
		try {

			SAXParser parser = factory.newSAXParser();
			DatapointsParser dpparser = new DatapointsParser(dps, uris, cometResourceManager);
			Reader reader = new InputStreamReader(stream, "UTF-8");

			InputSource is = new InputSource(reader);
			is.setEncoding("UTF-8");

			parser.parse(is, dpparser);
		} catch (IOException | ParserConfigurationException | SAXException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
		}
		return dps;
	}

	class DatapointsParser extends DefaultHandler {

		private NodeId nodeid = null;
		private boolean active = false;
		private long cycletime = -1000000;
		// private EthernetIPAreaCode addresstype = EthernetIPAreaCode.UNKNOWN;
		private String tag = "";
		private EthernetIPDPItem actNode = null;
		private List<EthernetIPDPItem> nodes = null;
		private String[] items = null;
		private NamespaceTable uris = null;
		private ETHERNETIP_MAPPING_TYPE mapping;
		private ComResourceManager comResourceManager;

		public DatapointsParser(List<EthernetIPDPItem> nodes, NamespaceTable uris,
				ComResourceManager comResourceManager) {
			this.nodes = nodes;
			this.uris = uris;
			this.comResourceManager = comResourceManager;
		}

		@Override
		public void endElement(String uri, String localName, String name) throws SAXException {
		}

		@Override
		public void startElement(String uri, String localname, String name, Attributes attrs) throws SAXException {

			if (name.compareTo("dp") == 0) {
				// actNode = new EthernetIPDPItem();

			} else if (name.compareTo("nodeid") == 0) {
				try {
					items = attrs.getValue("value").split(";");
					if (items != null && items.length == 2) {
						int nsindex = uris.getIndex(items[0].replace("ns=", ""));
						if (nsindex != -1) {
							nodeid = NodeId.parseNodeId("ns=" + nsindex + ";" + items[1]);
						}
					}
				} catch (NumberFormatException ex) {
					// TODO logg numberformatexception
				}
			} else if (name.compareTo("symbolname") == 0) {
				tag = attrs.getValue("value");
				// actNode.setSymbolname(attrs.getValue("value"));
				// actNode.setBrowsename(attrs.getValue("value"));
			} else if (name.compareTo("isactive") == 0) {
				active = Boolean.parseBoolean(attrs.getValue("value"));
				// actNode.setActive(Boolean.parseBoolean(attrs.getValue("value")));
			} else if (name.compareTo("cycletime") == 0) {
				try {
					cycletime = Long.parseLong(attrs.getValue("value")) * 1000000;
					// actNode.setCycletime(Integer.parseInt(attrs
					// .getValue("value")));
				} catch (NumberFormatException ex) {
					// TODO log numberformatexception
				}
			} else if (name.compareTo("mapping") == 0) {
				mapping = ETHERNETIP_MAPPING_TYPE.valueOf(attrs.getValue("value"));
			} else if (name.compareTo("datatype") == 0) {
				EthernetIPDataType dt = EthernetIPDataType.UNKNOWN;
				int arraylength = 1;
				try {
					dt = EthernetIPDataType.valueOf(attrs.getValue("value"));
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
							dt = EthernetIPDataType.valueOf(d);
						} catch (NumberFormatException nex) {

						}
					}
				}
				switch (dt) {
				case BOOL:
					actNode = new EthernetIPBooleanItem();
					break;
				case SINT:
					actNode = new EthernetIPSIntItem();
					break;
				case INT:
					actNode = new EthernetIPIntItem();
					break;
				case DINT:
					actNode = new EthernetIPDIntItem();
					break;
				case LINT:
					actNode = new EthernetIPLIntItem();
					break;
				case USINT:
					actNode = new EthernetIPUSIntItem();
					break;
				case UINT:
					actNode = new EthernetIPUIntItem();
					break;
				case UDINT:
					actNode = new EthernetIPUDIntItem();
					break;
				case ULINT:
					actNode = new EthernetIPULIntItem();
					break;
				case REAL:
					actNode = new EthernetIPRealItem();
					break;
				case LREAL:
					actNode = new EthernetIPLRealItem();
					break;
				case STRING:
					actNode = new EthernetIPStringItem();
					break;
				default:
					actNode = new EthernetIPDPItem();
					break;
				}
				actNode.setManager(comResourceManager);
				actNode.setArraylength(arraylength);
				actNode.setNodeId(nodeid);
				actNode.setTagname(tag);
				actNode.setActive(active);
				actNode.setCycletime(cycletime);
				actNode.setMapping(mapping);
				this.nodes.add(actNode);
			} else if (name.compareTo("historical") == 0) {
				// not used at the moment
				// actNode.setHistorical(Boolean.parseBoolean(attrs
				// .getValue("value")));
			} else if (name.compareTo("trigger") == 0) {
				actNode.setTriggerNode(attrs.getValue("value"));
			} else if (name.compareTo("description") == 0) {
				actNode.setDescription(attrs.getValue("value"));
			}
		}
	}

}
