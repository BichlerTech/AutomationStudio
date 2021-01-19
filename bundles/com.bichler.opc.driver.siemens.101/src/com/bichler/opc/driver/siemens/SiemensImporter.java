package com.bichler.opc.driver.siemens;

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

import com.bichler.opc.comdrv.ComResourceManager;
import com.bichler.opc.comdrv.importer.Com_Importer;
import com.bichler.opc.driver.siemens.communication.SiemensAreaCode;
import com.bichler.opc.driver.siemens.communication.SiemensDataType;
import com.bichler.opc.driver.siemens.dp.SiemensBooleanItem;
import com.bichler.opc.driver.siemens.dp.SiemensByteItem;
import com.bichler.opc.driver.siemens.dp.SiemensCharItem;
import com.bichler.opc.driver.siemens.dp.SiemensDIntItem;
import com.bichler.opc.driver.siemens.dp.SiemensDPItem;
import com.bichler.opc.driver.siemens.dp.SiemensDWordItem;
import com.bichler.opc.driver.siemens.dp.SiemensDateAndTimeItem;
import com.bichler.opc.driver.siemens.dp.SiemensDateItem;
import com.bichler.opc.driver.siemens.dp.SiemensIntItem;
import com.bichler.opc.driver.siemens.dp.SiemensRealItem;
import com.bichler.opc.driver.siemens.dp.SiemensStringCharItem;
import com.bichler.opc.driver.siemens.dp.SiemensStringItem;
import com.bichler.opc.driver.siemens.dp.SiemensTimeItem;
import com.bichler.opc.driver.siemens.dp.SiemensTimeOfDayItem;
import com.bichler.opc.driver.siemens.dp.SiemensTimerItem;
import com.bichler.opc.driver.siemens.dp.SiemensWordItem;
import com.bichler.opc.driver.siemens.transform.SIEMENS_MAPPING_TYPE;

public class SiemensImporter extends Com_Importer {
	public SiemensImporter() {
	}

	public List<SiemensDPItem> loadDPs(InputStream stream, NamespaceTable uris,
			ComResourceManager cometResourceManager) {
		List<SiemensDPItem> dps = new ArrayList<>();
		SAXParserFactory factory = SAXParserFactory.newInstance();
		try {
			SAXParser parser = factory.newSAXParser();
			DatapointsParser dpparser = new DatapointsParser(dps, uris, cometResourceManager);
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
		private boolean active = false;
		private long cycletime = -1000000;
		private SiemensAreaCode addresstype = SiemensAreaCode.UNKNOWN;
		private String address = "";
		private float index = 0;
		private SiemensDPItem actNode = null;
		private List<SiemensDPItem> nodes = null;
		private String[] items = null;
		private NamespaceTable uris = null;
		private SIEMENS_MAPPING_TYPE mapping;
		private ComResourceManager cometResourceManager;

		public DatapointsParser(List<SiemensDPItem> nodes, NamespaceTable uris,
				ComResourceManager cometResourceManager) {
			this.nodes = nodes;
			this.uris = uris;
			this.cometResourceManager = cometResourceManager;
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
					cycletime = Long.parseLong(attrs.getValue("value")) * 1000000;
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
			} else if (name.compareTo("address") == 0) {
				address = attrs.getValue("value");
			} else if (name.compareTo("mapping") == 0) {
				mapping = SIEMENS_MAPPING_TYPE.valueOf(attrs.getValue("value"));
			} else if (name.compareTo("index") == 0) {
				try {
					index = Float.parseFloat(attrs.getValue("value"));
				} catch (NumberFormatException ex) {
				}
			} else if (name.compareTo("datatype") == 0) {
				SiemensDataType dt = SiemensDataType.UNKNOWN;
				int arraylength = 1;
				try {
					dt = SiemensDataType.valueOf(attrs.getValue("value"));
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
							dt = SiemensDataType.valueOf(d);
						} catch (NumberFormatException nex) {
						}
					}
				}
				switch (dt) {
				case BOOL:
					actNode = new SiemensBooleanItem();
					break;
				case BYTE:
					actNode = new SiemensByteItem();
					break;
				case CHAR:
					actNode = new SiemensCharItem();
					break;
				case DINT:
					actNode = new SiemensDIntItem();
					break;
				case DWORD:
					actNode = new SiemensDWordItem();
					break;
				case DATE:
					actNode = new SiemensDateItem();
					break;
				case DATE_AND_TIME:
					actNode = new SiemensDateAndTimeItem();
					break;
				case INT:
					actNode = new SiemensIntItem();
					break;
				case REAL:
					actNode = new SiemensRealItem();
					break;
				case STRINGCHAR:
					actNode = new SiemensStringCharItem();
					break;
				case STRING:
					actNode = new SiemensStringItem();
					break;
				case TIME:
					actNode = new SiemensTimeItem();
					break;
				case TIME_OF_DAY:
					actNode = new SiemensTimeOfDayItem();
					break;
				case TIMER:
					actNode = new SiemensTimerItem();
					break;
				case WORD:
					actNode = new SiemensWordItem();
					break;
				default:
					actNode = new SiemensDPItem();
					break;
				}
				actNode.setManager(cometResourceManager);
				actNode.setArraylength(arraylength);
				actNode.setDataType(dt);
				actNode.setNodeId(nodeid);
				actNode.setSymbolname(symbolname);
				actNode.setActive(active);
				actNode.setCycletime(cycletime);
				actNode.setAddressType(addresstype);
				actNode.setAddress(address);
				actNode.setIndex(index);
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

	public List<SiemensDPItem> loadDeviceMapping(InputStream stream, NamespaceTable uris) {
		List<SiemensDPItem> dps = new ArrayList<SiemensDPItem>();
		SAXParserFactory factory = SAXParserFactory.newInstance();
		try {
			SAXParser parser = factory.newSAXParser();
			DeviceMapper mapper = new DeviceMapper(dps, uris);
			Reader reader = new InputStreamReader(stream, "UTF-8");
			InputSource is = new InputSource(reader);
			is.setEncoding("UTF-8");
			parser.parse(is, mapper);
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

	class DeviceMapper extends DefaultHandler {
		private int db = 0;
		private double index = 0.0;
		private int moduleCount = 4;
		private int addOnCount = 0;
		private int groupCount = 0;
		private int meterCount = 0;
		private boolean isactive = false;
		private NamespaceTable uris = null;
		private List<SiemensDPItem> nodes;

		public DeviceMapper(List<SiemensDPItem> nodes, NamespaceTable uris) {
			this.nodes = nodes;
			this.uris = uris;
		}

		@Override
		public void startElement(String uri, String localname, String name, Attributes attrs) throws SAXException {
			if (name.compareTo("main") == 0) {
				try {
					db = Integer.parseInt(attrs.getValue("db"));
				} catch (NumberFormatException ex) {
				}
				try {
					index = Double.parseDouble(attrs.getValue("start"));
				} catch (NumberFormatException ex) {
				}
				try {
					addOnCount = Integer.parseInt(attrs.getValue("raddon"));
				} catch (NumberFormatException ex) {
				}
				try {
					groupCount = Integer.parseInt(attrs.getValue("rgroups"));
				} catch (NumberFormatException ex) {
				}
				try {
					meterCount = Integer.parseInt(attrs.getValue("rmeter"));
				} catch (NumberFormatException ex) {
				}
				try {
					isactive = Boolean.parseBoolean(attrs.getValue("isactive"));
				} catch (NumberFormatException ex) {
				}
			} else if (name.compareTo("node") == 0) {
				int value = -1;
				try {
					value = Integer.parseInt(attrs.getValue("value"));
				} catch (NumberFormatException ex) {
				}
				if (value != -1) {
					int addonindex = this.uris.getIndex(attrs.getValue("addonns"));
					NodeId id = null;
					if (addonindex != -1) {
						id = NodeId.parseNodeId("ns=" + addonindex + ";" + attrs.getValue("addonid"));
						if (id != null) {
							// create dp to read bytes to internal array
							SiemensByteItem addon = new SiemensByteItem();
							addon.setActive(isactive);
							addon.setDPAddress(db);
							addon.setIndex((float) index + moduleCount);
							addon.setArraylength(addOnCount);
							addon.setCycletime(-1000000);
							addon.setNodeId(id);
							addon.setAddressType(SiemensAreaCode.DB);
							addon.setMapping(SIEMENS_MAPPING_TYPE.ARRAY_ARRAY);
							nodes.add(addon);
						}
					}
					index += addOnCount + moduleCount;
					int groupindex = this.uris.getIndex(attrs.getValue("groupns"));
					if (groupindex != -1) {
						id = NodeId.parseNodeId("ns=" + groupindex + ";" + attrs.getValue("groupid"));
						if (id != null) {
							// create dp to read bytes to internal array
							SiemensByteItem addon = new SiemensByteItem();
							addon.setActive(isactive);
							addon.setDPAddress(db);
							addon.setIndex((float) index);
							addon.setArraylength(groupCount);
							addon.setCycletime(-1000000);
							addon.setNodeId(id);
							addon.setAddressType(SiemensAreaCode.DB);
							addon.setMapping(SIEMENS_MAPPING_TYPE.ARRAY_ARRAY);
							nodes.add(addon);
						}
					}
					index += groupCount;
					int meterindex = this.uris.getIndex(attrs.getValue("meterns"));
					if (meterindex != -1) {
						id = NodeId.parseNodeId("ns=" + meterindex + ";" + attrs.getValue("meterid"));
						if (id != null) {
							// create dp to read bytes to internal array
							SiemensByteItem addon = new SiemensByteItem();
							addon.setActive(isactive);
							addon.setDPAddress(db);
							addon.setIndex((float) index);
							addon.setArraylength(meterCount);
							addon.setCycletime(-1000000);
							addon.setNodeId(id);
							addon.setAddressType(SiemensAreaCode.DB);
							addon.setMapping(SIEMENS_MAPPING_TYPE.ARRAY_ARRAY);
							nodes.add(addon);
						}
					}
					index += meterCount;
				}
			}
		}
	}
}
