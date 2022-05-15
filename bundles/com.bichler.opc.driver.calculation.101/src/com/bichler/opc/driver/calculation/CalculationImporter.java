package com.bichler.opc.driver.calculation;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.UnsignedByte;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.builtintypes.UnsignedLong;
import org.opcfoundation.ua.builtintypes.UnsignedShort;
import org.opcfoundation.ua.common.NamespaceTable;
import org.opcfoundation.ua.core.TimestampsToReturn;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.bichler.opc.comdrv.ComDRVManager;
import com.bichler.opc.driver.calculation.targets.CalculationBooleanTarget;
import com.bichler.opc.driver.calculation.targets.CalculationSByteTarget;
import com.bichler.opc.driver.calculation.targets.CalculationDoubleTarget;
import com.bichler.opc.driver.calculation.targets.CalculationFloatTarget;
import com.bichler.opc.driver.calculation.targets.CalculationIntTarget;
import com.bichler.opc.driver.calculation.targets.CalculationLongTarget;
import com.bichler.opc.driver.calculation.targets.CalculationShortTarget;
import com.bichler.opc.driver.calculation.targets.CalculationStringTarget;
import com.bichler.opc.driver.calculation.targets.CalculationUnsignedByteTarget;
import com.bichler.opc.driver.calculation.targets.CalculationUnsignedIntTarget;
import com.bichler.opc.driver.calculation.targets.CalculationUnsignedLongTarget;
import com.bichler.opc.driver.calculation.targets.CalculationUnsignedShortTarget;

public class CalculationImporter {
	public List<CalculationDP> loadDPs(InputStream stream, NamespaceTable uris, CalculationResourceManager manager,
			long drvId) {
		List<CalculationDP> dps = new ArrayList<CalculationDP>();
		SAXParserFactory factory = SAXParserFactory.newInstance();
		try {
			SAXParser parser = factory.newSAXParser();
			DatapointsParser dpparser = new CalculationImporter().new DatapointsParser(dps, uris, manager, drvId);
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
		long drvId = -1;
		/**
		 * timeout for cyclic calculation
		 */
		// private CalcEvent calcevent = CalcEvent.ONREAD;
		private CalculationDP actNode = null;
		private List<CalculationDP> nodes = null;
		private NamespaceTable uris = null;
		private CalculationResourceManager manager = null;

		public DatapointsParser(List<CalculationDP> nodes, NamespaceTable uris, CalculationResourceManager manager,
				long drvId) {
			this.nodes = nodes;
			this.uris = uris;
			this.manager = manager;
			this.drvId = drvId;
		}

		@Override
		public void endElement(String uri, String localName, String name) throws SAXException {
			if (actNode != null) {
				if (name.compareTo("calc") == 0) {
					// now add and semicolon to end a calculation instruction
					actNode.getScript().append("\n");
				}
			}
		}

		@Override
		public void startElement(String uri, String localname, String name, Attributes attrs) throws SAXException {
			/**
			 * get the calculation event for a calculation instruction
			 */
			if (name.compareTo("calcevent") == 0) {
				if (actNode != null) {
					actNode.setEvent(CalcEvent.valueOf(attrs.getValue("value")));
					if (actNode.getEvent() == CalcEvent.CYCLIC) {
						try {
							// set timeout to nanoseconds
							actNode.setTimeout(Long.parseLong(attrs.getValue("timeout")) * 1000000);
						} catch (NumberFormatException ex) {
							// TODO log number format exception
						}
					}
				}
			}
			/**
			 * get active flag
			 */
			else if (name.compareTo("active") == 0) {
				if (actNode != null) {
					actNode.setActive(Boolean.parseBoolean(attrs.getValue("value")));
				}
			}
			/**
			 * get the target node id
			 */
			else if (name.compareTo("target") == 0) {
				// get the target node id
				String targetNS = attrs.getValue("ns");
				String targetId = attrs.getValue("id");
				int arrayIndex = -1;
				try {
					arrayIndex = Integer.parseInt(attrs.getValue("index"));
				} catch (NumberFormatException ex) {
				}
				int index = uris.getIndex(targetNS);
				NodeId nodeid = null;
				try {
					nodeid = NodeId.parseNodeId("ns=" + index + ";" + targetId);
				} catch (IllegalArgumentException ex) {
					// TODO log exception
				}
				if (nodeid != null) {
					// now create the right calculation dp
					DataValue response = ComDRVManager.getDRVManager().readFromDriver(nodeid,
							org.opcfoundation.ua.core.Attributes.Value, null, null, null, 0.0,
							TimestampsToReturn.Neither);
					actNode = new CalculationDP();
					/**
					 * now we have the nodeid of that type, browse up to base type
					 */
					if (response == null) {
						Logger.getLogger(getClass().getName()).log(Level.SEVERE,
								"Can not create instruction for '{0}', because value response is null!", new Object[] {nodeid});
					} else if (response.getValue() == null) {
						Logger.getLogger(getClass().getName()).log(Level.SEVERE,
								"Can not create instruction for '{0}', because value is null!", new Object[] {nodeid});
					} else if (response.getValue().getCompositeClass() == null) {
						Logger.getLogger(getClass().getName()).log(Level.SEVERE,
								"Can not create instruction for '{0}', because composite class for value is null!",
								new Object[] {nodeid});
					} else {
						Class<?> type = response.getValue().getCompositeClass();
						if (type.equals(Boolean.class)) {
							actNode.setTarget(new CalculationBooleanTarget());
						} else if (type.equals(Integer.class)) {
							actNode.setTarget(new CalculationIntTarget());
						} else if (type.equals(UnsignedByte.class)) {
							actNode.setTarget(new CalculationUnsignedByteTarget());
						} else if (type.equals(byte[].class)) {
							actNode.setTarget(new CalculationSByteTarget());
						} else if (type.equals(Double.class)) {
							actNode.setTarget(new CalculationDoubleTarget());
						} else if (type.equals(Float.class)) {
							actNode.setTarget(new CalculationFloatTarget());
						} else if (type.equals(Short.class)) {
							actNode.setTarget(new CalculationShortTarget());
						} else if (type.equals(Integer.class)) {
							actNode.setTarget(new CalculationIntTarget());
						} else if (type.equals(Long.class)) {
							actNode.setTarget(new CalculationLongTarget());
						} else if (type.equals(Byte.class)) {
							actNode.setTarget(new CalculationSByteTarget());
						} else if (type.equals(String.class)) {
							actNode.setTarget(new CalculationStringTarget());
						} else if (type.equals(UnsignedByte.class)) {
							actNode.setTarget(new CalculationUnsignedByteTarget());
						} else if (type.equals(UnsignedShort.class)) {
							actNode.setTarget(new CalculationUnsignedShortTarget());
						} else if (type.equals(UnsignedInteger.class)) {
							actNode.setTarget(new CalculationUnsignedIntTarget());
						} else if (type.equals(UnsignedLong.class)) {
							actNode.setTarget(new CalculationUnsignedLongTarget());
						}
						nodes.add(actNode);
					}
					if (actNode != null) {
						actNode.setArrayindex(arrayIndex);
						/**
						 * set target string for evaluation
						 */
						actNode.getTarget().setTargetId(nodeid);
					}
				}
			}
			/**
			 * append operator to script
			 */
			else if (name.compareTo("operation") == 0) {
				if (actNode != null) {
					actNode.getScript()
							.append(attrs.getValue("value").replaceAll("\\$lower\\$", "<").replaceAll("\\$greater\\$", ">").replaceAll("&amp;", "&").replaceAll("&#39;", "'").replaceAll("&#34;", "\""));
					actNode.getCalculationExpressions().add(new CalculationExpression(
							attrs.getValue("value").replaceAll("\\$lower\\$", "<").replaceAll("\\$greater\\$", ">").replaceAll("&amp;", "&").replaceAll("&#39;", "'").replaceAll("&#34;", "\"")));
				}
			} else if (name.compareTo("node") == 0) {
				if (actNode != null) {
					try {
						String nodeNS = attrs.getValue("ns");
						String nodeid = attrs.getValue("id");
						int arrayIndex = -1;
						try {
							arrayIndex = Integer.parseInt(attrs.getValue("index"));
						} catch (NumberFormatException ex) {
						}
						int index = uris.getIndex(nodeNS);
						NodeId id = NodeId.parseNodeId("ns=" + index + ";" + nodeid);
						CalculationNode node = new CalculationNode(id.toString());
						actNode.getCalculationExpressions().add(node);
						String[] dt = getValueFromNode(id);
						if (arrayIndex > -1) {
							node.setArrayIndex(arrayIndex);
							actNode.getScript()
									.append("((ComDRVManager.getDRVManager().readFromDriver(NodeId.parseNodeId(\""
											+ id.toString() + "\"), Attributes.Value, \"" + arrayIndex
											+ "\", null, new Long(" + drvId
											+ "), new Double(0.0), TimestampsToReturn.Both).getValue().getValue())[0])"
											+ dt[1]);
						} else {
							actNode.getScript()
									.append("ComDRVManager.getDRVManager().readFromDriver(NodeId.parseNodeId(\""
											+ id.toString() + "\"), Attributes.Value, null, null, new Long(" + drvId
											+ "), new Double(0.0), TimestampsToReturn.Both).getValue()" + dt[1]);
						}
						if (actNode.getEvent() == CalcEvent.VALUECHANGE) {
							// add nodes to all events
							if (manager != null) {
								manager.addCalcInstructionValueChange(id, actNode);
							}
						}
					} catch (NumberFormatException ex) {
						// TODO logg numberformatexception
					}
				}
			}
		}

	}

	private String[] getValueFromNode(NodeId nodeid) {
		DataValue response = ComDRVManager.getDRVManager().readFromDriver(nodeid,
				org.opcfoundation.ua.core.Attributes.Value, null, null, null, 0.0, TimestampsToReturn.Neither);
		String[] ret = new String[] { "", "" };
		if (response != null) {
			/**
			 * now we have the nodeid of that type, browse up to base type
			 */
			if (response != null && response.getValue() != null && response.getValue().getCompositeClass() != null) {
				Class<?> type = response.getValue().getCompositeClass();
				if (type.equals(Boolean.class)) {
					ret[0] = "Boolean";
					ret[1] = ".booleanValue()";
				} else if (type.equals(Integer.class)) {
					ret[0] = "Integer";
					ret[1] = ".intValue()";
				} else if (type.equals(UnsignedByte.class)) {
					ret[0] = "Integer";
					ret[1] = ".intValue()";
				} else if (type.equals(byte[].class)) {
					ret[0] = "Integer";
					ret[1] = ".intValue()";
				} else if (type.equals(Double.class)) {
					ret[0] = "java.lang.Double";
					ret[1] = ".doubleValue()";
				} else if (type.equals(Float.class)) {
					ret[0] = "Float";
					ret[1] = ".floatValue()";
				} else if (type.equals(Short.class)) {
					ret[0] = "short";
					ret[1] = ".shortValue()";
				} else if (type.equals(Integer.class)) {
					ret[0] = "Integer";
					ret[1] = ".intValue()";
				} else if (type.equals(Long.class)) {
					ret[0] = "Long";
					ret[1] = ".longValue()";
				} else if (type.equals(Byte.class)) {
					ret[0] = "Byte";
					ret[1] = ".byteValue()";
				} else if (type.equals(String.class)) {
					ret[0] = "String";
					ret[1] = ".toString()";
				} else if (type.equals(UnsignedShort.class)) {
					ret[0] = "Integer";
					ret[1] = ".intValue()";
				} else if (type.equals(UnsignedInteger.class)) {
					ret[0] = "Long";
					ret[1] = ".longValue()";
				} else if (type.equals(UnsignedLong.class)) {
					ret[0] = "Long";
					ret[1] = ".longValue()";
				}
			}
		}
		return ret;
	}
}
