package com.bichler.astudio.editor.calculation.model;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.UnsignedByte;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.builtintypes.UnsignedLong;
import org.opcfoundation.ua.builtintypes.UnsignedShort;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.common.NamespaceTable;
import org.opcfoundation.ua.core.TimestampsToReturn;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.bichler.opc.driver.calculation.CalcEvent;
import com.bichler.opc.driver.calculation.CalculationDP;
import com.bichler.opc.driver.calculation.CalculationExpression;
import com.bichler.opc.driver.calculation.CalculationNode;
import com.bichler.opc.driver.calculation.CalculationResourceManager;
import com.bichler.opc.driver.calculation.targets.CalculationBooleanTarget;
import com.bichler.opc.driver.calculation.targets.CalculationByteTarget;
import com.bichler.opc.driver.calculation.targets.CalculationDoubleTarget;
import com.bichler.opc.driver.calculation.targets.CalculationFloatTarget;
import com.bichler.opc.driver.calculation.targets.CalculationIntTarget;
import com.bichler.opc.driver.calculation.targets.CalculationLongTarget;
import com.bichler.opc.driver.calculation.targets.CalculationShortTarget;
import com.bichler.opc.driver.calculation.targets.CalculationStringTarget;
import com.bichler.opc.driver.calculation.targets.CalculationTarget;
import com.bichler.opc.driver.calculation.targets.CalculationUnsignedByteTarget;
import com.bichler.opc.driver.calculation.targets.CalculationUnsignedIntTarget;
import com.bichler.opc.driver.calculation.targets.CalculationUnsignedLongTarget;
import com.bichler.opc.driver.calculation.targets.CalculationUnsignedShortTarget;
import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.opc.comdrv.ComDRVManager;
import opc.sdk.core.node.Node;
import opc.sdk.core.node.VariableNode;
import opc.sdk.core.node.VariableTypeNode;

public class CalculationDPEditorImporter {

	public CalculationDPEditorImporter() {

	}

	public List<CalculationDP> loadDPs(InputStream stream, NamespaceTable uris, CalculationResourceManager manager,
			long drvId) {

		List<CalculationDP> dps = new ArrayList<>();
		SAXParserFactory factory = SAXParserFactory.newInstance();
		try {

			SAXParser parser = factory.newSAXParser();
			DatapointsParser dpparser = new CalculationDPEditorImporter().new DatapointsParser(dps, uris, manager,
					drvId);
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

	// public enum CalcEvent {
	// ONREAD, CYCLIC, VALUECHANGE;
	// }

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
					// actNode.getScript().append(actNode.getTarget().getCreateDV()
					// + actNode.getTarget());
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
					Node node = ServerInstance.getInstance().getServerInstance().getAddressSpaceManager()
							.getNodeById(nodeid);

					// now create the right calculation dp

					Variant value = Variant.NULL;
					if (node instanceof VariableNode) {
						value = ((VariableNode) node).getValue();
					} else if (node instanceof VariableTypeNode) {
						value = ((VariableTypeNode) node).getValue();
					}

					// DataValue response = CometDRVManager.getDRVManager()
					// .readFromDriver(nodeid,
					// org.opcfoundation.ua.core.Attributes.Value,
					// null, null, null, 0.0,
					// TimestampsToReturn.Neither);

					if (value != null) {
						/**
						 * now we have the nodeid of that type, browse up to
						 * base type
						 */
						if (value != null && value.getCompositeClass() != null) {
							Class<?> type = value.getCompositeClass();
							actNode = new CalculationDP();
							if (type.equals(Boolean.class)) {
								actNode.setTarget(new CalculationBooleanTarget());
							} else if (type.equals(Integer.class)) {
								actNode.setTarget(new CalculationIntTarget());
							} else if (type.equals(UnsignedByte.class)) {
								actNode.setTarget(new CalculationUnsignedByteTarget());
							} else if (type.equals(byte[].class)) {
								actNode.setTarget(new CalculationByteTarget());
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
								actNode.setTarget(new CalculationByteTarget());
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
						// no variable with valid value
						else {
							actNode = new CalculationDP();
							actNode.setTarget(new CalculationTarget());
							actNode.getTarget().setTargetId(nodeid);
							actNode.setArrayindex(arrayIndex);
							nodes.add(actNode);
						}
					}

					if (actNode != null) {
						actNode.setArrayindex(arrayIndex);
						/**
						 * set target string for evaluation
						 */
						actNode.getTarget().getTarget()
								.append("CometDRVManager.getDRVManager().writeFromDriver(NodeId.parseNodeId(\"ns="
										+ index + ";" + targetId + "\"),Attributes.Value, null, null, val, new Long("
										+ drvId + "));");

						// actNode.getScript().append("importPackage(java.lang);\nvar
						// value;\n");
						// // + actNode.getTarget().getValueString());
						actNode.getScript().append(
								"importPackage(org.opcfoundation.ua.builtintypes);\nimportPackage(java.lang);\nimportPackage(com.hbsoft.comdrv);\nimportPackage(org.opcfoundation.ua.core);\nvar value;\n"); // +
						// actNode.getTarget().getValueString());
						actNode.getTarget().setTargetId(nodeid);
					}
				}
				// empty nodeid
				else {
					actNode = new CalculationDP();
					actNode.setTarget(new CalculationTarget());
					nodes.add(actNode);
				}
			}

			/**
			 * append operator to script
			 */
			else if (name.compareTo("operation") == 0) {
				if (actNode != null) {
					actNode.getScript()
							.append(attrs.getValue("value").replace("$lower$", "<").replace("$greater$", ">"));
					actNode.getCalculationExpressions().add(new CalculationExpression(
							attrs.getValue("value").replace("$lower$", "<").replace("$greater$", ">")));
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
						NodeId id = NodeId.NULL;
						CalculationNode node = null;
						try {
							id = NodeId.parseNodeId("ns=" + index + ";" + nodeid);
							node = new CalculationNode(id.toString());
						} catch (IllegalArgumentException iae) {
							iae.printStackTrace();
							node = new CalculationNode("");
						}

						actNode.getCalculationExpressions().add(node);
						String[] dt = getValueFromNode(id);
						if (arrayIndex > -1) {
							node.setArrayIndex(arrayIndex);

							actNode.getScript()
									.append("((CometDRVManager.getDRVManager().readFromDriver(NodeId.parseNodeId(\""
											+ id.toString() + "\"), Attributes.Value, \"" + arrayIndex
											+ "\", null, new Long(" + drvId
											+ "), new Double(0.0), TimestampsToReturn.Both).getValue().getValue())[0])"
											+ dt[1]);
							// Object obj = ((Boolean))
							// Object obj =
							// ((Boolean)((Object[])CometDRVManager.getDRVManager().readFromDriver(id,
							// org.opcfoundation.ua.core.Attributes.Value,
							// null, null, new Long(drvId), 0.0,
							// TimestampsToReturn.Both).getValue().getValue())[arrayIndex]).booleanValue();
							// actNode.getScript()
							// .append("(("+dt[0]+")((Object)CometDRVManager.getDRVManager().readFromDriver(NodeId.parseNodeId(\""
							// + id.toString()
							// + "\"), Attributes.Value, null, null, new Long("
							// + drvId
							// +
							// "), new Double(0.0),
							// TimestampsToReturn.Both).getValue().getValue())["+arrayIndex+"])"
							// + dt[1]);
						} else {
							actNode.getScript()
									.append("CometDRVManager.getDRVManager().readFromDriver(NodeId.parseNodeId(\""
											+ id.toString() + "\"), Attributes.Value, null, null, new Long(" + drvId
											+ "), new Double(0.0), TimestampsToReturn.Both).getValue()" + dt[1]);
						}
						if (actNode.getEvent() == CalcEvent.VALUECHANGE) {
							// add nodes to all events
							if (manager != null) {
								manager.getCalcInstructionsValueChange().put(id, actNode);
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
					ret[0] = "Integer";
					ret[1] = ".intValue()";
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
					ret[1] = ".getValue()";
				} else if (type.equals(UnsignedShort.class)) {
					ret[0] = "Integer";
					ret[1] = ".intValue()";
				} else if (type.equals(UnsignedInteger.class)) {
					ret[0] = "Integer";
					ret[1] = ".intValue()";
				} else if (type.equals(UnsignedLong.class)) {
					ret[0] = "Long";
					ret[1] = ".longValue()";
				}
			}
		}
		return ret;
	}

}
