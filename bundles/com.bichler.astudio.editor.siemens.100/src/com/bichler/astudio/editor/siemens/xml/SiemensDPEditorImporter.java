package com.bichler.astudio.editor.siemens.xml;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
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

import com.bichler.opc.comdrv.importer.Com_TriggerDpItem;
import com.bichler.opc.driver.siemens.communication.SiemensAreaCode;

public class SiemensDPEditorImporter {
	public SiemensDPEditorImporter() {
	}

	public List<SiemensDPItem> loadDPs(InputStream stream, NamespaceTable uris) {
		try {
			InputStreamReader isr = new InputStreamReader(stream, "UTF-8");
			InputSource is = new InputSource(isr);
			return loadDPs(is, uris);
		} catch (UnsupportedEncodingException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage());
		}
		return null;
	}

	private List<SiemensDPItem> loadDPs(InputSource stream, NamespaceTable uris) {
		List<SiemensDPItem> dps = new ArrayList<>();
		SAXParserFactory factory = SAXParserFactory.newInstance();
		try {
			SAXParser parser = factory.newSAXParser();
			DatapointsParser dpparser = new DatapointsParser(dps, uris);
			parser.parse(stream, dpparser);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage());
		}
		return dps;
	}

	class DatapointsParser extends DefaultHandler {
		private NodeId nodeid = null;
		private NodeId rootid = null;
		private String browsename = "";
		private boolean active = false;
		private boolean simulate = false;
		private int cycletime = -1;
		private SiemensAreaCode addresstype = SiemensAreaCode.UNKNOWN;
		private String address = "";
		private float index = 0;
		private SiemensDPItem actNode = null;
		private List<SiemensDPItem> nodes = null;
		private String[] items = null;
		private NamespaceTable uris = null;
		private SIEMENS_MAPPING_TYPE mapping = SIEMENS_MAPPING_TYPE.SCALAR;

		public DatapointsParser(List<SiemensDPItem> nodes, NamespaceTable uris) {
			this.nodes = nodes;
			this.uris = uris;
		}

		@Override
		public void endElement(String uri, String localName, String name) throws SAXException {
			if (name.compareTo("dp") == 0) {
				this.nodeid = null;
				this.browsename = null;
				this.active = false;
				this.simulate = false;
				this.cycletime = -1;
				this.addresstype = SiemensAreaCode.UNKNOWN;
				this.address = "";
				this.index = 0;
				this.mapping = SIEMENS_MAPPING_TYPE.SCALAR;
				this.actNode = null;
				this.rootid = null;
			}
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
			} else if (name.compareTo("rootid") == 0) {
				try {
					items = attrs.getValue("value").split(";");
					if (items != null && items.length == 2) {
						int nsindex = uris.getIndex(items[0].replace("ns=", ""));
						if (nsindex != -1) {
							rootid = NodeId.parseNodeId("ns=" + nsindex + ";" + items[1]);
							// actNode.setNodeId(NodeId.decode("ns=" + nsindex +
							// ";" + items[1]));
						}
					}
				} catch (NumberFormatException ex) {
					// TODO logg numberformatexception
				}
			} else if (name.compareTo("symbolname") == 0) {
				browsename = attrs.getValue("value");
				// actNode.setSymbolname(attrs.getValue("value"));
				// actNode.setBrowsename(attrs.getValue("value"));
			} else if (name.compareTo("isactive") == 0) {
				active = Boolean.parseBoolean(attrs.getValue("value"));
				// actNode.setActive(Boolean.parseBoolean(attrs.getValue("value")));
			} else if (name.compareTo("issimulate") == 0) {
				simulate = Boolean.parseBoolean(attrs.getValue("value"));
				// actNode.setActive(Boolean.parseBoolean(attrs.getValue("value")));
			} else if (name.compareTo("cycletime") == 0) {
				try {
					cycletime = Integer.parseInt(attrs.getValue("value"));
					// actNode.setCycletime(Integer.parseInt(attrs
					// .getValue("value")));
				} catch (NumberFormatException ex) {
					// TODO log numberformatexception
				}
			} else if (name.compareTo("addresstype") == 0) {
				try {
					addresstype = SiemensAreaCode.valueOf(attrs.getValue("value"));
				} catch (NumberFormatException ex) {
				}
			} else if (name.compareTo("mapping") == 0) {
				try {
					mapping = SIEMENS_MAPPING_TYPE.valueOf(attrs.getValue("value"));
				} catch (NumberFormatException nfe) {
					// TODO:
					nfe.printStackTrace();
				}
			} else if (name.compareTo("address") == 0) {
				address = attrs.getValue("value");
			} else if (name.compareTo("index") == 0) {
				try {
					index = Float.parseFloat(attrs.getValue("value"));
				} catch (NumberFormatException ex) {
				}
			} else if (name.compareTo("datatype") == 0) {
				String datatype = attrs.getValue("value");
				actNode = new SiemensDPItem();
				actNode.setDataType(datatype);
				actNode.setNodeId(nodeid);
				actNode.setSymbolname(browsename);
				actNode.setBrowsename(browsename);
				actNode.setActive(active);
				actNode.setCycletime(cycletime);
				actNode.setAddressType(addresstype);
				actNode.setAddress(address);
				actNode.setIndex(index);
				actNode.setMapping(mapping);
				actNode.setRootId(rootid);
				actNode.setSimulate(simulate);
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
							Com_TriggerDpItem node = new Com_TriggerDpItem();
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
}
