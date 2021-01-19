package com.hbsoft.studio.editor.events.xml;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.common.NamespaceTable;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.hbsoft.studio.opcua.driver.datatype.XML_DA_DATATYPE;

public class EventsImporter {

	public EventsImporter() {

	}

	public List<EventDpItem> loadDPs(InputStream stream, NamespaceTable uris) {
		try {
			InputStreamReader isr = new InputStreamReader(stream, "UTF-8");
			InputSource is = new InputSource(isr);
			return loadDPs(is, uris);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<EventDpItem> loadDPs(InputSource stream, NamespaceTable uris) {
		List<EventDpItem> dps = new ArrayList<>();
		SAXParserFactory factory = SAXParserFactory.newInstance();
		try {
			SAXParser parser = factory.newSAXParser();
			DatapointsParser dpparser = new DatapointsParser(dps, uris);
			parser.parse(stream, dpparser);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return dps;
	}

	class DatapointsParser extends DefaultHandler {

		private NamespaceTable uris = null;

		private List<EventDpItem> nodes = null;
		private List<EventDpSyntax> syntax = null;

		private EventDpItem current = null;

		private NodeId nodeid = null;
		private String operation = null;
		private NodeId sourceId = null;
		private DataValue value = null;
		private String message = null;
		private String reference = null;

		private String description = null;
		boolean isSyntax = false;

		// private EVENTS_MAPPING_TYPE mapping = EVENTS_MAPPING_TYPE.SCALAR;

		public DatapointsParser(List<EventDpItem> nodes, NamespaceTable uris) {
			this.nodes = nodes;
			this.uris = uris;
		}

		private void initDP() {
			this.syntax = new ArrayList<EventDpSyntax>();

			this.nodeid = NodeId.NULL;
			// this.browsename = "";
			// this.active = false;
			// this.cycletime = -1;
			// this.resourcename = "";
			// this.varname = "";
			this.operation = "";
			this.sourceId = NodeId.NULL;
			this.description = null;
			this.value = null;
			this.reference = null;

			this.current = new EventDpItem();
			// this.actNode = null;
			// this.items = null;
			// this. mapping = EVENTS_MAPPING_TYPE.SCALAR;
		}

		private void initSyntax() {
			this.isSyntax = true;

			this.nodeid = NodeId.NULL;
			this.operation = "";
			this.sourceId = NodeId.NULL;
			this.description = null;
			this.value = null;
			this.reference = EventReference.Beginn.name();
		}

		@Override
		public void endElement(String uri, String localName, String name)
				throws SAXException {
			if (name.compareTo("dp") == 0) {
				for (EventDpSyntax child : this.syntax) {
					this.current.addChild(child);
				}

				this.nodes.add(this.current);
			}

			else if (name.compareTo("syntax") == 0) {
				EventDpSyntax newItem = new EventDpSyntax();
				newItem.setSourceId(this.nodeid);
				newItem.setOperation(this.operation);
				newItem.setValue(this.value);
				newItem.setVariableSource(this.sourceId);
				newItem.setMessage(this.message);
				newItem.setReference(EventReference.valueOf(this.reference));

				this.syntax.add(newItem);
				this.isSyntax = false;
			}
		}

		@Override
		public void startElement(String uri, String localname, String name,
				Attributes attrs) throws SAXException {

			if (name.compareTo("dp") == 0) {
				// reset node attributes to parse
				initDP();

			} else if (name.compareTo("syntax") == 0) {
				// nothing to do
				initSyntax();

			}

			else if (name.compareTo("nodeid") == 0) {
				try {
					String[] items = attrs.getValue("value").split(";");
					if (items != null && items.length == 2) {
						int nsindex = uris
								.getIndex(items[0].replace("ns=", ""));
						if (nsindex != -1) {
							nodeid = NodeId.parseNodeId("ns=" + nsindex + ";"
									+ items[1]);
							// actNode.setNodeId(NodeId.decode("ns=" + nsindex +
							// ";" + items[1]));
						}
					}
				} catch (NumberFormatException ex) {
					// TODO logg numberformatexception
				}

				if (!isSyntax) {
					this.current.setSourceId(this.nodeid);
				}
			}
			// else if (name.compareTo("symbolname") == 0) {
			// browsename = attrs.getValue("value");
			// // actNode.setSymbolname(attrs.getValue("value"));
			// // actNode.setBrowsename(attrs.getValue("value"));
			// }
			// else if (name.compareTo("isactive") == 0) {
			// active = Boolean.parseBoolean(attrs.getValue("value"));
			// actNode.setActive(Boolean.parseBoolean(attrs.getValue("value")));
			// }
			// else if (name.compareTo("cycletime") == 0) {
			// try {
			// cycletime = Integer.parseInt(attrs.getValue("value"));
			// // actNode.setCycletime(Integer.parseInt(attrs
			// // .getValue("value")));
			// } catch (NumberFormatException ex) {
			// // TODO log numberformatexception
			// }
			// }

			// else if (name.compareTo("resourceName") == 0) {
			// resourcename = attrs.getValue("value");
			// }

			// else if (name.compareTo("varName") == 0) {
			// varname = attrs.getValue("value");
			// }
			// else if (name.compareTo("mapping") == 0) {
			// try {
			// String val = attrs.getValue("value");
			// mapping = EVENTS_MAPPING_TYPE.valueOf(val);
			// } catch (IllegalArgumentException ex) {
			// // TODO log argument exception
			// ex.printStackTrace();
			// mapping = EVENTS_MAPPING_TYPE.SCALAR;
			// }
			// }
			// else if (name.compareTo("datatype") == 0) {
			// XML_DA_DATATYPE dt = XML_DA_DATATYPE.ANY;
			// int arraylength = 0;
			// try {
			// String dtvalue = attrs.getValue("value");
			// dt = XML_DA_DATATYPE.valueOf(dtvalue);
			// } catch (IllegalArgumentException ex) {
			// // now it is possible to have an array
			// String datatype = attrs.getValue("value");
			// if (datatype.contains("[") && datatype.contains("]")) {
			// // now extract array length
			// String d = datatype.substring(0, datatype.indexOf("["))
			// .trim();
			// String arraycount = datatype.substring(datatype
			// .indexOf("["));
			// arraycount = arraycount.replace("[", "")
			// .replaceAll("]", "").trim();
			//
			// try {
			// // try to parse int to integer
			// arraylength = Integer.parseInt(arraycount);
			// dt = XML_DA_DATATYPE.valueOf(d);
			// } catch (NumberFormatException nex) {
			//
			// }
			// }
			// }
			// // TODO:
			//
			// switch (dt) {
			// // case BOOLEAN:
			// // actNode = new XML_DA_BooleanItem();
			// // break;
			// // case BYTE:
			// // actNode = new XML_DA_ByteItem();
			// // break;
			// // case SHORT:
			// // actNode = new XML_DA_ShortItem();
			// // break;
			// // case INT:
			// // actNode = new XML_DA_IntegerItem();
			// // break;
			// // case LONG:
			// // actNode = new XML_DA_LongItem();
			// // break;
			// // case FLOAT:
			// // actNode = new XML_DA_FloatItem();
			// // break;
			// // case DOUBLE:
			// // actNode = new XML_DA_DoubleItem();
			// // break;
			// // case UNSIGNEDSHORT:
			// // actNode = new XML_DA_UnsignedShortItem();
			// // break;
			// // case UNSIGNEDINT:
			// // actNode = new XML_DA_UnsignedIntItem();
			// // break;
			// // case UNSIGNEDLONG:
			// // actNode = new XML_DA_UnsignedLongItem();
			// // break;
			// default:
			// actNode = new Events_DPItem();
			// actNode.setDataType(dt);
			// break;
			// }
			//
			// // if we have an arrays
			// if (mapping == EVENTS_MAPPING_TYPE.SCALAR_ARRAY
			// || mapping == EVENTS_MAPPING_TYPE.ARRAY_ARRAY) {
			// actNode.setArraylength(arraylength);
			// }
			// actNode.setDataType(dt);
			// actNode.setNodeId(nodeid);
			// actNode.setSymbolname(browsename);
			// actNode.setBrowsename(browsename);
			// actNode.setActive(active);
			// actNode.setCycletime(cycletime);
			// actNode.setResourceName(resourcename);
			// actNode.setVarName(varname);
			// actNode.setMapping(mapping);
			//
			// this.nodes.add(actNode);
			// }

			else if (name.compareTo("operation") == 0) {
				this.operation = attrs.getValue("value");
			} else if (name.compareTo("value") == 0) {
				this.value = new DataValue();
				// attrs.getValue("value");
			} else if (name.compareTo("source") == 0) {
				try {
					String[] items = attrs.getValue("value").split(";");
					if (items != null && items.length == 2) {
						int nsindex = uris
								.getIndex(items[0].replace("ns=", ""));
						if (nsindex != -1) {
							sourceId = NodeId.parseNodeId("ns=" + nsindex + ";"
									+ items[1]);
							// actNode.setNodeId(NodeId.decode("ns=" + nsindex +
							// ";" + items[1]));
						}
					}
				} catch (NumberFormatException ex) {
					// TODO logg numberformatexception
				}

				// String ns = attrs.getValue("ns"), id =
				// attrs.getValue("value");
				// NodeId nodeId = NodeId.parseNodeId("ns=" + uris.getIndex(ns)
				// + ";" + id);
				// this.source = nodeId;
			} else if (name.compareTo("description") == 0) {
				this.description = attrs.getValue("value");
			} else if (name.compareTo("message") == 0) {
				this.message = attrs.getValue("value");
			} else if (name.compareTo("reference") == 0) {
				this.reference = attrs.getValue("value");
			}
		}

	}
}
