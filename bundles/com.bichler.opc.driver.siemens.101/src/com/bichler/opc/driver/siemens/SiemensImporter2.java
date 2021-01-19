package com.bichler.opc.driver.siemens;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
import com.bichler.opc.comdrv.utils.ComStatusUtils;
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
import com.bichler.opc.driver.siemens.dp.SiemensLRealItem;
import com.bichler.opc.driver.siemens.dp.SiemensRealItem;
import com.bichler.opc.driver.siemens.dp.SiemensStringCharItem;
import com.bichler.opc.driver.siemens.dp.SiemensStringItem;
import com.bichler.opc.driver.siemens.dp.SiemensTimeItem;
import com.bichler.opc.driver.siemens.dp.SiemensTimeOfDayItem;
import com.bichler.opc.driver.siemens.dp.SiemensTimerItem;
import com.bichler.opc.driver.siemens.dp.SiemensWordItem;
import com.bichler.opc.driver.siemens.transform.SIEMENS_MAPPING_TYPE;

public class SiemensImporter2 extends Com_Importer {
	private Logger logger = Logger.getLogger(getClass().getName());

	public SiemensImporter2() {
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
		} catch (ParserConfigurationException | SAXException | IOException e) {
			logger.log(Level.SEVERE, e.getMessage());
		}
		return dps;
	}

	public List<SiemensConsumptionItem> loadConsumption(InputStream stream, /*
																			 * NamespaceTable uris,
																			 */
			ComResourceManager cometResourceManager) {
		List<SiemensConsumptionItem> dps = new ArrayList<>();
		SAXParserFactory factory = SAXParserFactory.newInstance();
		try {
			SAXParser parser = factory.newSAXParser();
			ConsumptionParser consparser = new ConsumptionParser(dps, /* uris, */
					cometResourceManager);
			Reader reader = new InputStreamReader(stream, "UTF-8");
			InputSource is = new InputSource(reader);
			is.setEncoding("UTF-8");
			parser.parse(is, consparser);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			logger.log(Level.SEVERE, e.getMessage());
		}
		return dps;
	}

	class ConsumptionParser extends DefaultHandler {
		private List<SiemensConsumptionItem> cons = null;
		private NodeId nodeid = null;
		private String symbolname = "";
		private boolean active = false;
		private long cycletime = -1000000;
		private String address = "";
		private float index = 0;
		private SiemensDPItem actNode = null;
		private SiemensConsumptionItem con = null;
		private SIEMENS_MAPPING_TYPE mapping;
		private ComResourceManager cometResourceManager;
		private ComStatusUtils utils = null;
		private long watchdog = 0;
		private long timeout = 10000000000L;
		private String trigger = "";

		public ConsumptionParser(List<SiemensConsumptionItem> cons/*
																	 * , NamespaceTable uris
																	 */, ComResourceManager cometResourceManager) {
			this.cons = cons;
			this.cometResourceManager = cometResourceManager;
			this.utils = new ComStatusUtils();
		}

		@Override
		public void endElement(String uri, String localName, String name) throws SAXException {
		}

		@Override
		public void startElement(String uri, String localname, String name, Attributes attrs) throws SAXException {
			// update watchdog after 10 seconds
			if (watchdog + timeout < System.nanoTime()) {
				watchdog = System.nanoTime();
//        utils.updateWatchdog();
//        utils.updateServerWatchdog();
			}
			if (name.compareTo("consumption") == 0) {
				this.con = new SiemensConsumptionItem();
				this.cons.add(this.con);
				try {
					address = attrs.getValue("db");
					this.con.setDb(Integer.parseInt(address));
				} catch (NumberFormatException ex) {
					logger.log(Level.SEVERE, ex.getMessage());
				}
				try {
					this.con.setStartaddress(Double.parseDouble(attrs.getValue("consumptionaddress")));
				} catch (NumberFormatException ex) {
					logger.log(Level.SEVERE, ex.getMessage());
				}
				try {
					this.con.setLength(Double.parseDouble(attrs.getValue("consumptionlength")));
				} catch (NumberFormatException ex) {
					logger.log(Level.SEVERE, ex.getMessage());
				}
			} else if (name.compareTo("node") == 0) {
				symbolname = attrs.getValue("config");
				// parse nodeid namespace
				nodeid = NodeId.parseNodeId("ns=0;" + attrs.getValue("confignodeid"));
				mapping = SIEMENS_MAPPING_TYPE.valueOf(attrs.getValue("mapping"));
				// get startindex of node
				try {
					index = Float.parseFloat(attrs.getValue("index"));
				} catch (NumberFormatException ex) {
				}
				active = Boolean.parseBoolean(attrs.getValue("isactive"));
				try {
					cycletime = Long.parseLong(attrs.getValue("cycletime")) * 1000000L;
				} catch (NumberFormatException ex) {
				}
				trigger = attrs.getValue("trigger");
				if (trigger == null)
					trigger = "";
				SiemensDataType dt = SiemensDataType.UNKNOWN;
				int arraylength = 1;
				try {
					dt = SiemensDataType.valueOf(attrs.getValue("datatype"));
				} catch (IllegalArgumentException ex) {
					// now it is possible to have an array
					String datatype = attrs.getValue("datatype");
					if (datatype.contains("[") && datatype.contains("]")) {
						// now extract array length
						String d = datatype.substring(0, datatype.indexOf("[")).trim();
						String arraycount = datatype.substring(datatype.indexOf("["));
						arraycount = arraycount.replace("[", "").replaceAll("]", "").trim();
						try {
							// try to parse int to integer
							arraylength = Integer.parseInt(arraycount);
							dt = SiemensDataType.valueOf(d.toUpperCase());
						} catch (NumberFormatException nex) {
							logger.log(Level.SEVERE, nex.getMessage());
						}
					}
				}
				if (nodeid == null) {
					return;
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
				case LREAL:
					actNode = new SiemensLRealItem();
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
					logger.log(Level.SEVERE, "No valid Siemens Datatype found for Nodeid: " + nodeid);
					break;
				}
				if (actNode == null) {
					return;
				}
				actNode.setManager(cometResourceManager);
				actNode.setArraylength(arraylength);
				actNode.setDataType(dt);
				actNode.setNodeId(nodeid);
				actNode.setSymbolname(symbolname);
				actNode.setActive(active);
				actNode.setCycletime(cycletime);
				actNode.setAddressType(SiemensAreaCode.DB);
				actNode.setAddress(address);
				actNode.setIndex(index);
				actNode.setMapping(mapping);
				actNode.setTriggerNode(trigger);
				this.con.getItems().add(actNode);
			}
		}
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
		private ComStatusUtils utils = null;
		private long watchdog = 0;
		private long timeout = 10000000000L;

		public DatapointsParser(List<SiemensDPItem> nodes, NamespaceTable uris,
				ComResourceManager cometResourceManager) {
			this.nodes = nodes;
			this.uris = uris;
			this.cometResourceManager = cometResourceManager;
			this.utils = new ComStatusUtils();
		}

		@Override
		public void endElement(String uri, String localName, String name) throws SAXException {
		}

		@Override
		public void startElement(String uri, String localname, String name, Attributes attrs) throws SAXException {
			// update watchdog after 10 seconds
			if (watchdog + timeout < System.nanoTime()) {
				watchdog = System.nanoTime();
//        utils.updateWatchdog();
//        utils.updateServerWatchdog();
			}
			if (name.compareTo("dp") == 0) {
				// actNode = new SiemensDPItem();
			} else if (name.compareTo("nodeid") == 0) {
				try {
					items = attrs.getValue("value").split(";");
					if (items != null && items.length == 2) {
						int nsindex = uris.getIndex(items[0].replace("ns=", ""));
						if (nsindex != -1) {
							nodeid = NodeId.parseNodeId("ns=" + nsindex + ";" + items[1]);
						} else {
							nodeid = null;
						}
					}
				} catch (NumberFormatException ex) {
					logger.log(Level.SEVERE, ex.getMessage());
				}
			} else if (name.compareTo("symbolname") == 0) {
				symbolname = attrs.getValue("value");
			} else if (name.compareTo("isactive") == 0) {
				active = Boolean.parseBoolean(attrs.getValue("value"));
			} else if (name.compareTo("cycletime") == 0) {
				try {
					cycletime = Long.parseLong(attrs.getValue("value")) * 1000000;
				} catch (NumberFormatException ex) {
					logger.log(Level.SEVERE, ex.getMessage());
				}
			} else if (name.compareTo("addresstype") == 0) {
				try {
					addresstype = SiemensAreaCode.valueOf(attrs.getValue("value"));
				} catch (NumberFormatException ex) {
					logger.log(Level.SEVERE, ex.getMessage());
				}
			} else if (name.compareTo("address") == 0) {
				address = attrs.getValue("value");
			} else if (name.compareTo("mapping") == 0) {
				mapping = SIEMENS_MAPPING_TYPE.valueOf(attrs.getValue("value"));
			} else if (name.compareTo("index") == 0) {
				try {
					index = Float.parseFloat(attrs.getValue("value"));
				} catch (NumberFormatException ex) {
					logger.log(Level.SEVERE, ex.getMessage());
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
							dt = SiemensDataType.valueOf(d.toUpperCase());
						} catch (NumberFormatException nex) {
							logger.log(Level.SEVERE, nex.getMessage());
						}
					}
				}
				if (nodeid == null) {
					return;
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
				case LREAL:
					actNode = new SiemensLRealItem();
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
					logger.log(Level.SEVERE, "No valid Siemens Datatype found for Nodeid: " + nodeid);
					break;
				}
				if (actNode == null) {
					return;
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
			} else if (name.compareTo("description") == 0 && nodeid != null) {
				actNode.setDescription(attrs.getValue("value"));
			}
		}
	}

	public List<SiemensDPItem> loadDeviceMapping(InputStream stream, NamespaceTable uris, Object[] obj,
			Map<Object, SiemensDPItem> meteridmapping) {
		List<SiemensDPItem> dps = new ArrayList<>();
		SAXParserFactory factory = SAXParserFactory.newInstance();
		try {
			SAXParser parser = factory.newSAXParser();
			DeviceMapper mapper = new DeviceMapper(dps, uris, obj, meteridmapping);
			Reader reader = new InputStreamReader(stream, "UTF-8");
			InputSource is = new InputSource(reader);
			is.setEncoding("UTF-8");
			parser.parse(is, mapper);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			logger.log(Level.SEVERE, e.getMessage());
		}
		return dps;
	}

	class DeviceMapper extends DefaultHandler {
		private int db = 0;
		private double index = 0.0;
		private int moduleCount = 4;
		private int enabledCount = 1;
		private int addOnCount = 0;
		private int groupCount = 0;
		private int meterCount = 0;
		private boolean isactive = false;
		private Object[] configs = null;
		private int dummy = 1;
		Map<Object, SiemensDPItem> meteridmapping = null;
		private int deviceindex = 0;
		private NamespaceTable uris = null;
		private List<SiemensDPItem> nodes;

		public DeviceMapper(List<SiemensDPItem> nodes, NamespaceTable uris, Object[] configs,
				Map<Object, SiemensDPItem> meteridmapping) {
			this.nodes = nodes;
			this.uris = uris;
			this.configs = configs;
			this.meteridmapping = meteridmapping;
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
					if (index % 2 == 1)
						dummy = 1;
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
					// now find index of device in structure
					for (int i = 0; i < this.configs.length; i++) {
						if (this.configs[i] instanceof Integer) {
							deviceindex = (Integer) this.configs[i];
							// if we found a 0 so no device will follow
							if (deviceindex == 0) {
								break;
							}
							if (deviceindex == value) {
								// now we found device number
								NodeId id = null;
								// add enabled flag
								int enabledindex = this.uris.getIndex(attrs.getValue("enablens"));
								if (enabledindex != -1) {
									id = NodeId.parseNodeId("ns=" + enabledindex + ";" + attrs.getValue("enableid"));
									if (id != null) {
										// create dp to read bytes to internal
										// array
										// changed from int to dint
										SiemensBooleanItem enabled = new SiemensBooleanItem();
										enabled.setActive(isactive);
										enabled.setDPAddress(db);
										enabled.setIndex((float) index + ((moduleCount + enabledCount + dummy
												+ addOnCount * 4 + groupCount * 4 + meterCount * 4) * i) + moduleCount);
										enabled.setArraylength(1);
										enabled.setCycletime(60000000);
										enabled.setNodeId(id);
										enabled.setAddressType(SiemensAreaCode.DB);
										enabled.setMapping(SIEMENS_MAPPING_TYPE.SCALAR);
										nodes.add(enabled);
									}
								}
								int addonindex = this.uris.getIndex(attrs.getValue("addonns"));
								if (addonindex != -1) {
									id = NodeId.parseNodeId("ns=" + addonindex + ";" + attrs.getValue("addonid"));
									if (id != null) {
										// create dp to read bytes to internal
										// array
										// changed from int to dint
										SiemensDIntItem addon = new SiemensDIntItem();
										addon.setActive(isactive);
										addon.setDPAddress(db);
										addon.setIndex(
												(float) index
														+ ((moduleCount + enabledCount + dummy + addOnCount * 4
																+ groupCount * 4 + meterCount * 4) * i)
														+ moduleCount + enabledCount + dummy);
										addon.setArraylength(addOnCount);
										addon.setCycletime(-1000000);
										addon.setNodeId(id);
										addon.setAddressType(SiemensAreaCode.DB);
										addon.setMapping(SIEMENS_MAPPING_TYPE.ARRAY_ARRAY);
										nodes.add(addon);
									}
								}
								int groupindex = this.uris.getIndex(attrs.getValue("groupns"));
								if (groupindex != -1) {
									id = NodeId.parseNodeId("ns=" + groupindex + ";" + attrs.getValue("groupid"));
									if (id != null) {
										// create dp to read bytes to internal
										// array
										// changed from int to dint
										SiemensDIntItem addon = new SiemensDIntItem();
										addon.setActive(isactive);
										addon.setDPAddress(db);
										addon.setIndex((float) index
												+ ((moduleCount + enabledCount + dummy + addOnCount * 4 + groupCount * 4
														+ meterCount * 4) * i)
												+ moduleCount + enabledCount + dummy + addOnCount * 4);
										addon.setArraylength(groupCount);
										addon.setCycletime(-1000000);
										addon.setNodeId(id);
										addon.setAddressType(SiemensAreaCode.DB);
										addon.setMapping(SIEMENS_MAPPING_TYPE.ARRAY_ARRAY);
										nodes.add(addon);
									}
								}
								int meterindex = this.uris.getIndex(attrs.getValue("meterns"));
								if (meterindex != -1) {
									id = NodeId.parseNodeId("ns=" + meterindex + ";" + attrs.getValue("meterid"));
									if (id != null) {
										// create dp to read bytes to internal
										// array
										SiemensDIntItem meterid = new SiemensDIntItem();
										meterid.setActive(isactive);
										meterid.setDPAddress(db);
										meterid.setIndex((float) index
												+ ((moduleCount + enabledCount + dummy + addOnCount * 4 + groupCount * 4
														+ meterCount * 4) * i)
												+ moduleCount + enabledCount + dummy + addOnCount * 4 + groupCount * 4);
										meterid.setArraylength(meterCount);
										meterid.setCycletime(-1000000);
										meterid.setNodeId(id);
										meterid.setAddressType(SiemensAreaCode.DB);
										meterid.setMapping(SIEMENS_MAPPING_TYPE.ARRAY_ARRAY);
										nodes.add(meterid);
										this.meteridmapping.put(deviceindex, meterid);
									}
								}
								break;
							}
						}
					}
				}
			}
		}
	}
}
