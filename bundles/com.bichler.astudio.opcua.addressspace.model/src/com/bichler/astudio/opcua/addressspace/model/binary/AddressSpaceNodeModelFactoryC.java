package com.bichler.astudio.opcua.addressspace.model.binary;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.opcfoundation.ua.builtintypes.ByteString;
import org.opcfoundation.ua.builtintypes.DateTime;
import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.QualifiedName;
import org.opcfoundation.ua.builtintypes.StatusCode;
import org.opcfoundation.ua.builtintypes.UnsignedByte;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.builtintypes.UnsignedLong;
import org.opcfoundation.ua.builtintypes.UnsignedShort;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.common.NamespaceTable;
import org.opcfoundation.ua.common.ServerTable;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.Argument;
import org.opcfoundation.ua.core.BrowseDescription;
import org.opcfoundation.ua.core.BrowseDirection;
import org.opcfoundation.ua.core.BrowseResult;
import org.opcfoundation.ua.core.BrowseResultMask;
import org.opcfoundation.ua.core.EnumValueType;
import org.opcfoundation.ua.core.IdType;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.core.ReferenceDescription;
import org.opcfoundation.ua.core.ReferenceNode;
import org.opcfoundation.ua.encoding.EncoderContext;
import org.opcfoundation.ua.encoding.binary.BinaryEncoder;
import org.opcfoundation.ua.utils.StackUtils;

import com.bichler.astudio.filesystem.IFileSystem;
import com.bichler.astudio.opcua.addressspace.model.Activator;
import com.bichler.astudio.utils.internationalization.CustomString;

import opc.sdk.core.application.operation.ICancleOperation;
import opc.sdk.core.node.Node;
import opc.sdk.core.node.UADataTypeNode;
import opc.sdk.core.node.UAMethodNode;
import opc.sdk.core.node.UAObjectNode;
import opc.sdk.core.node.UAObjectTypeNode;
import opc.sdk.core.node.UAReferenceTypeNode;
import opc.sdk.core.node.UAVariableNode;
import opc.sdk.core.node.UAVariableTypeNode;
import opc.sdk.core.node.ViewNode;
import opc.sdk.server.core.OPCInternalServer;

public class AddressSpaceNodeModelFactoryC extends CompileFactory {
	/**
	 * Constants
	 */
	protected static final String EXTENSION_C = "c";
	protected static final String EXTENSION_H = "h";
	public static final String METHOD_NAME_INIT = "init";
	// public static final String PACKAGENAME = "com.hbsoft.opc.informationmodel";
	public static final String MAINCLASSNAME = "InformationModel";
	public static final String VALUECLASSNAME = "values";
	/**
	 * max byte limits of java methods
	 */
	private static final String MODELCLASSNAME = "model";
	private static final String STUBEXTENSION = "Stub";
	private static final String CALLBACKEXTENSION = "callback";

//	private static final String PUB_SUB_SOURCES = "PubSub";

	private Logger logger = Logger.getLogger(getClass().getName());
	private BufferedWriter outMainC;
//	private BufferedWriter outPubSubC;
	private OPCInternalServer serverInstance;
	private static int index = 0;

	public AddressSpaceNodeModelFactoryC() {
		super();
	}

	class AuthorityHelper {
		boolean hasMethodIncreased = false;
		int byteCount = 0;
	}

	/**
	 * 
	 * @param monitor
	 * @param file
	 * @param nsTable     namespacetable to export
	 * @param serverTable servernstable to map for export table
	 * @param nodes
	 * @throws IOException
	 */
	public File[] create(ICancleOperation monitor, String file, NamespaceTable nsTable, NamespaceTable serverTable,
			Map<Integer, List<Node>> allNodes, List<Integer> rList, IFileSystem filesystem) throws IOException {
		List<File> files = new ArrayList<>();
		File tmpFolder = new File(file);
		if (!tmpFolder.exists()) {
			tmpFolder.mkdir();
			files.add(tmpFolder);
		}
		IPath tmpPath = new Path(tmpFolder.getPath());
		tmpPath = tmpPath.append("compiled");
		File comp = tmpPath.toFile();
		if (!comp.exists())
			comp.mkdir();
		EncoderContext ctx = new EncoderContext(serverTable, ServerTable.createFromArray(new String[0]),
				StackUtils.getDefaultSerializer(), Integer.MAX_VALUE);

		BufferedWriter outC = null;
		BufferedWriter outStub = null;
		BufferedWriter outCallBack = null;
		BufferedWriter outCallBackH = null;
		BufferedWriter outStubH = null;
//		BufferedWriter outPubSub = null;
//		BufferedWriter outPubSubH = null;
		BufferedWriter authout = null;
		OutputStream valueOut = null;
		BinaryEncoder encoder = null;
		// close file because method limit exceeds
		try {
			String newMainModel = MODELCLASSNAME;
			File modelMainFileC = tmpPath.append(newMainModel + "." + EXTENSION_C).toFile();
			if (modelMainFileC.exists())
				modelMainFileC.delete();

			if (modelMainFileC.createNewFile())
				files.add(modelMainFileC);
			outMainC = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(modelMainFileC)));
			writeIncludeSectionC(outMainC, ""); // new function to export model for c

			for (Integer nsIndex : rList) {

				// only create import files for namespaces with nodes
				outMainC.write("int importModel_" + nsIndex + "();\n");
				// outMainC.write("int addNamespace_" + nsIndex + "(a_pServer);\n");
			}
			if (this.outMainC != null) {
				outMainC.write("\nvoid importModel()\n {\n");
				outMainC.flush();
			}
			// iterate over namespaces
			for (Integer nsIndex : rList) {

				String uri = nsTable.getUri(nsIndex);
				if (this.outMainC != null) {
					outMainC.write("\tUA_Server_addNamespace(g_pServer, \"" + uri + "\");\n");
				}
			}
			//index = 0;
			for (Integer nsIndex : rList) {
				Node[] nodes = allNodes.get(nsIndex).toArray(new Node[0]);
				// create file for namespace
				String newModel = MODELCLASSNAME + nsIndex;
				File modelFileC = tmpPath.append(newModel + "." + EXTENSION_C).toFile();
				if (modelFileC.exists())
					modelFileC.delete();

				if (modelFileC.createNewFile())
					files.add(modelFileC);

				String newModelCallBack = MODELCLASSNAME + CALLBACKEXTENSION + nsIndex;
				File newModelCallBackC = tmpPath.append(newModelCallBack + "." + EXTENSION_C).toFile();
				File newModelCallBackH = tmpPath.append(newModelCallBack + "." + EXTENSION_H).toFile();
				if (newModelCallBackC.exists())
					newModelCallBackC.delete();

				if (newModelCallBackC.createNewFile())
					files.add(newModelCallBackC);

				if (newModelCallBackH.exists())
					newModelCallBackH.delete();

				newModelCallBackH.createNewFile();

				String newModelStub = MODELCLASSNAME + STUBEXTENSION + nsIndex;
				File modelStubC = tmpPath.append(newModelStub + "." + EXTENSION_C).toFile();
				File modelStubH = tmpPath.append(newModelStub + "." + EXTENSION_H).toFile();
				if (modelStubC.exists())
					modelStubC.delete();

				if (modelStubC.createNewFile())
					files.add(modelStubC);

				if (modelStubC.exists())
					modelStubC.delete();

				modelStubC.createNewFile();
				// create buffered write

				outC = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(modelFileC)));
				writeIncludeSectionC(outC, "#include \"" + modelStubH.getName() + "\"\n" + "#include \""
						+ newModelCallBackH.getName() + "\"\n"); // new function to export
																	// model for c server

				writeClassStartC(outC, nsIndex); // new function to export model for c server
				outC.flush();

//				outPubSubC = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(modelPubSubC)));
//				writeIncludeSectionC(outPubSubC, ""); // new function to export model for c server
//				
//				
//				writePubSubStartC(outPubSubC, nsIndex);
//				outPubSubC.flush();

				/** fill callback files here */
				outCallBack = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(newModelCallBackC)));
				writeIncludeSectionC(outCallBack, ""); // new function to export model for c server
				outCallBack.flush();

				outStub = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(modelStubC)));
				writeIncludeSectionC(outStub, ""); // new function to export model for c server
				outStub.flush();

//				outPubSubH = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(modelPubSubH)));
//				outPubSubH.write("#ifndef BTECH_" + newPubSub + "_H_\n");
//				outPubSubH.write("#define BTECH_" + newPubSub + "_H_\n");
//				writeIncludeSectionC(outPubSubH, ""); // new function to export model for c server
//				outPubSubH.flush();

				outCallBackH = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(newModelCallBackH)));
				outCallBackH.write("#ifndef BTECH_" + newModelCallBack + "_H_\n");
				outCallBackH.write("#define BTECH_" + newModelCallBack + "_H_\n");
				writeIncludeSectionC(outCallBackH, ""); // new function to export model for c server
				outCallBackH.flush();

				outStubH = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(modelStubH)));
				outStubH.write("#ifndef BTECH_" + newModelStub + "_H_\n");
				outStubH.write("#define BTECH_" + newModelStub + "_H_\n");
				writeIncludeSectionC(outStubH, ""); // new function to export model for c server
				outStubH.flush();
				// write start class
				for (Node node : nodes) {
					if (node.getNodeId().getNamespaceIndex() == nsIndex) {
						if (monitor != null) {
							if(monitor.isCanceled()) {
								monitor.done();
								return files.toArray(new File[0]);
							}
						}
						if (node.getNodeId().equals(NodeId.get(IdType.Numeric, 0, new UnsignedInteger(78)))) {
							UAObjectNode mandatory = new UAObjectNode();
							mandatory.setBrowseName(node.getBrowseName());
							mandatory.setDescription(node.getDescription());
							mandatory.setDisplayName(node.getDisplayName());
							mandatory.setNodeClass(node.getNodeClass());
							mandatory.setNodeId(node.getNodeId());
							mandatory.setUserWriteMask(node.getUserWriteMask());
							mandatory.setWriteMask(node.getWriteMask());
							mandatory.setEventNotifier(((UAObjectNode) node).getEventNotifier());
							List<ReferenceNode> references = new ArrayList<>();
							ReferenceNode refNode1 = new ReferenceNode();
							refNode1.setIsInverse(false);
							refNode1.setReferenceTypeId(new NodeId(0, new UnsignedInteger(40)));
							refNode1.setTargetId(new ExpandedNodeId(this.serverInstance.getNamespaceUris().getUri(0),
									new UnsignedInteger(77), this.serverInstance.getNamespaceUris()));
							references.add(refNode1);
							ReferenceNode refNode2 = new ReferenceNode();
							refNode2.setIsInverse(false);
							refNode2.setReferenceTypeId(new NodeId(0, new UnsignedInteger(46)));
							refNode2.setTargetId(new ExpandedNodeId(this.serverInstance.getNamespaceUris().getUri(0),
									new UnsignedInteger(112), this.serverInstance.getNamespaceUris()));
							references.add(refNode2);
							ReferenceNode refNode3 = new ReferenceNode();
							refNode3.setIsInverse(true);
							refNode3.setReferenceTypeId(new NodeId(0, new UnsignedInteger(50)));
							refNode3.setTargetId(new ExpandedNodeId(this.serverInstance.getNamespaceUris().getUri(0),
									new UnsignedInteger(112), this.serverInstance.getNamespaceUris()));
							references.add(refNode3);
							mandatory.setReferences(references.toArray(new ReferenceNode[0]));
							node = mandatory;
						}
						// Replace Mandatory and Optional ObjectNode with its default
						// :PP HACK
						else if (node.getNodeId().equals(NodeId.get(IdType.Numeric, 0, new UnsignedInteger(80)))) {
							UAObjectNode optional = new UAObjectNode();
							optional.setBrowseName(node.getBrowseName());
							optional.setDescription(node.getDescription());
							optional.setDisplayName(node.getDisplayName());
							optional.setNodeClass(node.getNodeClass());
							optional.setNodeId(node.getNodeId());
							optional.setUserWriteMask(node.getUserWriteMask());
							optional.setWriteMask(node.getWriteMask());
							optional.setEventNotifier(((UAObjectNode) node).getEventNotifier());
							List<ReferenceNode> references = new ArrayList<>();
							ReferenceNode refNode1 = new ReferenceNode();
							refNode1.setIsInverse(false);
							refNode1.setReferenceTypeId(new NodeId(0, new UnsignedInteger(40)));
							refNode1.setTargetId(new ExpandedNodeId(this.serverInstance.getNamespaceUris().getUri(0),
									new UnsignedInteger(77), this.serverInstance.getNamespaceUris()));
							references.add(refNode1);
							ReferenceNode refNode2 = new ReferenceNode();
							refNode2.setIsInverse(false);
							refNode2.setReferenceTypeId(new NodeId(0, new UnsignedInteger(46)));
							refNode2.setTargetId(new ExpandedNodeId(this.serverInstance.getNamespaceUris().getUri(0),
									new UnsignedInteger(112), this.serverInstance.getNamespaceUris()));
							references.add(refNode2);
							ReferenceNode refNode3 = new ReferenceNode();
							refNode3.setIsInverse(true);
							refNode3.setReferenceTypeId(new NodeId(0, new UnsignedInteger(50)));
							refNode3.setTargetId(new ExpandedNodeId(this.serverInstance.getNamespaceUris().getUri(0),
									new UnsignedInteger(112), this.serverInstance.getNamespaceUris()));
							references.add(refNode3);
							optional.setReferences(references.toArray(new ReferenceNode[0]));
							node = optional;
						}
						writeNodeC(outC, outStub, outStubH, outCallBack, outCallBackH, encoder, nsTable, serverTable,
								node);
					}
				}

				writeAddEndC(outC);
				outC.close();

				outStub.close();

				outCallBack.close();

				outCallBackH.write("#endif // BTECH_" + newModelCallBack + "_H_\n");
				outCallBackH.close();

				outStubH.write("#endif // BTECH_" + newModelCallBack + "_H_\n");
				outStubH.close();
			}

			if (this.outMainC != null) {
				outMainC.write("}\n");
				outMainC.flush();
			}

			// new main pub sub model
//			String newMainPubSub = PUB_SUB_SOURCES;
//			File modelMainPubSubFileC = tmpPath.append(newMainPubSub + "." + EXTENSION_C).toFile();
//			if (modelMainPubSubFileC.exists())
//				modelMainPubSubFileC.delete();

//			if (modelMainPubSubFileC.createNewFile())
//				files.add(modelMainPubSubFileC);
//			outPubSub = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(modelMainPubSubFileC)));
//			writeIncludeSectionC(outPubSub, "");
//			List<DevicePubSubMapping.UA_PubSubConnectionConfig> conns = new ArrayList<DevicePubSubMapping.UA_PubSubConnectionConfig>();
//			UA_PubSubConnectionConfig con = new DevicePubSubMapping().new UA_PubSubConnectionConfig();
//			con.setAddress(new Variant(""));
//			conns.add(con);
//			exportPubSubConnectionConfig(outPubSub, conns);

//			outPubSub.write("return 0;\n");
//			outPubSub.write("}\n");
//			outPubSub.flush();
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
			throw new IOException(
					CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "monitor.error.upload"), e);
		} finally {

			if (valueOut != null) {
				try {
					valueOut.close();
				} catch (IOException e) {
					logger.log(Level.SEVERE, e.getMessage());
				}
			}
			if (authout != null) {
				try {
					authout.close();
				} catch (IOException e) {
					logger.log(Level.SEVERE, e.getMessage());
				}
			}

//			if (outPubSubC != null) {
//				try {
//					outPubSubC.close();
//				} catch (IOException e) {
//					logger.log(Level.SEVERE, e.getMessage());
//				}
//			}
			if (outMainC != null) {
				try {
					outMainC.close();
				} catch (IOException e) {
					logger.log(Level.SEVERE, e.getMessage());
				}
			}
		}
		return files.toArray(new File[0]);
	}

	/**
	 * Ends an add method.
	 * 
	 * @param out
	 * @throws IOException
	 */
	void writeAddEndC(BufferedWriter outc) throws IOException {
		// out.write("server.importModel(nsTable, " + VARIABLE_LIST_NODES2ADD +
		// ".toArray(new Node[0]));");
		outc.newLine();
		outc.write("return 0;\n}");
		outc.newLine();
		outc.flush();
	}

	/**
	 * Writes an add method to insert opc ua nodes.
	 * 
	 * @param out
	 * @param count
	 * @return
	 * @throws IOException
	 */
	int writeAddStartC(BufferedWriter out, int count) throws IOException {
		int byteCount = 0;
		return byteCount;
	}

	/**
	 * Writes end of an class
	 * 
	 * @param out
	 * @throws IOException
	 */
	void writeClassEndC(BufferedWriter out) throws IOException {
		out.write("\treturn 0;\n}\n");
	}

	/*
	 * function writes out start of c information model
	 */
	void writeClassStartC(BufferedWriter out, int nsIndex) throws IOException {
		// add also import function to main start routine
		if (this.outMainC != null) {
			outMainC.write("\timportModel_" + nsIndex + "();\n");
			outMainC.flush();
		}
		// this is the main callback function which will be called from main startup
		out.write("int importModel_" + nsIndex + "()\n{\n");

		out.write("\n");
	}

	/*
	 * function writes out start of c information model
	 */
//	void writePubSubStartC(BufferedWriter out, int nsIndex) throws IOException {
//		// this is the main callback function which will be called from main startup
//		out.write("int btech_ua_pubSubLoadConfig_" + nsIndex + "()\n{\n");
//
//		out.write("\n");
//	}

	void writeNodeC(BufferedWriter out, BufferedWriter outStub, BufferedWriter outStubH, BufferedWriter outCallBack,
			BufferedWriter outCallBackH, BinaryEncoder encoder, NamespaceTable nsTable, NamespaceTable serverTable,
			Node node) throws IOException {
		if (node == null) {
			return;
		}
		// references
		writeReferenceNodesC(out, nsTable, serverTable, node.getReferences());
		// node
		NodeClass nodeClass = node.getNodeClass();
		switch (nodeClass) {
		case Object:
			writeNodeObjectC(out, nsTable, serverTable, (UAObjectNode) node);
			break;
		case ObjectType:
			writeNodeObjectTypeC(out, nsTable, serverTable, (UAObjectTypeNode) node);
			break;
		case DataType:
			writeNodeDataTypeC(out, nsTable, serverTable, (UADataTypeNode) node);
			break;
		case Method:
			writeNodeMethodC(out, outStub, outStubH, nsTable, serverTable, (UAMethodNode) node);
			break;
		case ReferenceType:
			writeNodeReferenceTypeC(out, nsTable, serverTable, (UAReferenceTypeNode) node);
			break;
		case Variable:
			writeNodeVariableC(out, outCallBack, outCallBackH, nsTable, serverTable, (UAVariableNode) node);
			break;
		case VariableType:
			writeNodeVariableTypeC(out, nsTable, serverTable, (UAVariableTypeNode) node);
			break;
		case View:
			writeNodeViewC(out, nsTable, serverTable, (ViewNode) node);
			break;
		default:
			break;
		}
		return;
	}

	/**
	 * function writes out all necessary includes
	 * 
	 * @param out
	 * @throws IOException
	 */
	void writeIncludeSectionC(BufferedWriter out, String additional) throws IOException {
		out.write("#include <btech_interface.h>\n");
		out.write(additional);
	}

	public static int helpUnsignedInteger(BufferedWriter out, UnsignedInteger value) throws IOException {
		if (value == null)
			out.write("0");
		else
			out.write(Integer.toString(value.intValue()));
		return 0;
	}

	public static int helpDouble(BufferedWriter out, Double value) throws IOException {
		if (value == null)
			out.write("0.0");
		else
			out.write(Double.toString(value.doubleValue()));
		return 0;
	}

	public static int helpByte(BufferedWriter out, Byte value) throws IOException {
		if (value == null)
			out.write("0");
		else
			out.write(Byte.toString(value.byteValue()));
		return 0;
	}

	public static int helpUnsignedByte(BufferedWriter out, UnsignedByte value) throws IOException {
		if (value == null)
			out.write("0");
		else
			out.write(Integer.toString(value.intValue()));
		return 0;
	}

	public static int helpString(BufferedWriter out, String value) throws IOException {
		if (value == null)
			out.write("UA_STRING_NULL");
		else
			out.write("{" + value.length() + ", (unsigned char *)\"" + value + "\"}");
		return 0;
	}

	public static int helpInteger(BufferedWriter out, Integer value) throws IOException {
		if (value == null)
			out.write("0");
		else
			out.write(Integer.toString(value.intValue()));
		return 0;
	}

	public static int helpStringArray(BufferedWriter out, String[] value) throws IOException {
		int length = 0;
		String comma = "";
		if (value == null) {
			out.write("NULL");
			return 0;
		} else {
			length = value.length;
		}
		out.write("(UA_STRING[" + length + "]){");
		if (value != null) {
			for (String val : value) {
				out.write(comma);
				out.write("{" + val.length() + ", (unsigned char *)\"" + val + "\"}");
				comma = ",";
			}
		}
		out.write("}");
		return 0;
	}

	public static int helpDoubleArray(BufferedWriter out, Double[] value) throws IOException {
		int length = 0;
		String comma = "";
		if (value != null)
			length = value.length;
		out.write("(UA_Double[" + length + "]){");
		if (value != null) {
			for (Double val : value) {
				out.write(comma);
				out.write(val + "\n");
				comma = ",";
			}
		}
		out.write("}");
		return 0;
	}

	public static int helpLocalizedTextC(BufferedWriter out, LocalizedText text) throws IOException {
		String name = text.getText() == null ? "" : text.getText().replace("\n", " ");
		String localeId = text.getLocaleId() == null ? "" : text.getLocaleId();
		out.write("{{" + localeId.length() + ", (unsigned char *)\"" + localeId + "\"},{" + name.length()
				+ ", (unsigned char *)\"" + name + "\"}}");
		return 2 + localeId.length() + name.length();
	}

	/**
	 * writes a string to create a opc ua nodeid for string, numeric, bytestring and
	 * guid
	 * 
	 * @param nsTable
	 * @param serverTable
	 * @param nodeId
	 * @return
	 * @throws IOException
	 */
	public static int helpNodeIdC(BufferedWriter out, NamespaceTable nsTable, NamespaceTable serverTable, NodeId nodeId)
			throws IOException {
		if (nodeId == null) {
			out.write("UA_NODEID_NULL");
			return 0;
		}
		String uri = serverTable.getUri(nodeId.getNamespaceIndex());
		int index = nsTable.getIndex(uri);
		// add " quote to string
		Object nIdValue = nodeId.getValue();
		if (nIdValue instanceof String) {
			out.write("{" + index + ", 3, .identifier.string = {" + nIdValue.toString().length()
					+ ", (unsigned char *)\"" + nIdValue.toString() + "\" } }");
		} else if (nIdValue instanceof Integer) {
			out.write("{" + index + ", 0, .identifier.numeric = " + nIdValue.toString() + " }");
		} else if (nIdValue instanceof UnsignedInteger) {
			out.write("{" + index + ", 0, .identifier.numeric = " + nIdValue.toString() + "}");
		} else if (nIdValue instanceof UUID) {
			out.write("{" + index + ", 4, .identifier.guid = " + nIdValue.toString() + "}");
		} else if (nIdValue instanceof byte[]) {
			out.write("{" + index + ", 5, .identifier.bytestring = " + nIdValue.toString() + "}");
		} else {
			out.write("UA_NODEID_NULL");
		}
		return 24;
	}

	public static int helpCreateVariantDataC(BufferedWriter out, Variant value, NodeId dataType, NamespaceTable nsTable,
			NamespaceTable serverTable) throws IOException {
		if (value == null) {
			out.write("NULL");
			return 0;
		}
		if (!value.isArray()) {
			if (value.getCompositeClass() == Boolean.class) {
				out.write("(UA_Boolean[1]){" + ((Boolean) value.getValue()).booleanValue() + "}");
			} else if (value.getCompositeClass() == Byte.class) {
				out.write("(UA_SByte[1]){" + ((Byte) value.getValue()).intValue() + "}");
			} else if (value.getCompositeClass() == UnsignedByte.class) {
				out.write("(UA_Byte[1]){" + ((UnsignedByte) value.getValue()).intValue() + "}");
			} else if (value.getCompositeClass() == Short.class) {
				out.write("(UA_Int16[1]){" + ((Short) value.getValue()).intValue() + "}");
			} else if (value.getCompositeClass() == UnsignedShort.class) {
				out.write("(UA_UInt16[1]){" + ((UnsignedShort) value.getValue()).intValue() + "}");
			} else if (value.getCompositeClass() == Integer.class) {
				out.write("(UA_Int32[1]){" + ((Integer) value.getValue()).intValue() + "}");
			} else if (value.getCompositeClass() == UnsignedInteger.class) {
				out.write("(UA_UInt32[1]){" + ((UnsignedInteger) value.getValue()).longValue() + "}");
			} else if (value.getCompositeClass() == Long.class) {
				out.write("(UA_Int64[1]){" + ((Long) value.getValue()).longValue() + "}");
			} else if (value.getCompositeClass() == UnsignedLong.class) {
				out.write("(UA_UInt64[1]){" + ((UnsignedLong) value.getValue()).longValue() + "}");
			} else if (value.getCompositeClass() == Float.class) {
				out.write("(UA_Float[1]){" + ((Float) value.getValue()).floatValue() + "}");
			} else if (value.getCompositeClass() == Double.class) {
				out.write("(UA_Double[1]){" + ((Double) value.getValue()).doubleValue() + "}");
			} else if (value.getCompositeClass() == DateTime.class) {
				out.write("(UA_DateTime[1]){" + ((DateTime) value.getValue()).getValue() + "}");
			} else if (value.getCompositeClass() == String.class) {
				out.write("(UA_String[1]){{" + value.getValue().toString().length() + ", (unsigned char *)\""
						+ value.getValue().toString() + "\"}}");
			} else if (value.getCompositeClass() == ByteString.class) {
				out.write("(UA_ByteString[1]){{" + value.getValue().toString().length() + ", (unsigned char *)\""
						+ value.getValue().toString() + "\"}}");
			} else if (value.getCompositeClass() == LocalizedText.class) {
				out.write("(UA_LocalizedText[1]){");
				helpLocalizedTextC(out, (LocalizedText) value.getValue());
				out.write("}\n");
			} else if (value.getCompositeClass() == NodeId.class) {
				out.write("(UA_NodeId[1]){");
				helpNodeIdC(out, nsTable, serverTable, (NodeId) value.getValue());
				out.write("}\n");
			} else {
				// create value from datatype
				helpCreateValuefromDatatype(out, dataType);
			}
//				out.write(AddressSpaceNodeModelFactoryC.class.getName()
//						+ " line: 520 \nTODO add datatype serialization " + value.getCompositeClass() + "\n");
		} else {
			String comma = "";
			if (value.getCompositeClass() == Boolean.class) {
				out.write("(UA_Boolean[" + value.getArrayDimensions()[0] + "]){");
			} else if (value.getCompositeClass() == Byte.class) {
				out.write("(UA_SByte[" + value.getArrayDimensions()[0] + "]){");
			} else if (value.getCompositeClass() == UnsignedByte.class) {
				out.write("(UA_Byte[" + value.getArrayDimensions()[0] + "]){");
			} else if (value.getCompositeClass() == Short.class) {
				out.write("(UA_Int16[" + value.getArrayDimensions()[0] + "]){");
			} else if (value.getCompositeClass() == UnsignedShort.class) {
				out.write("(UA_UInt16[" + value.getArrayDimensions()[0] + "]){");
			} else if (value.getCompositeClass() == Integer.class) {
				out.write("(UA_Int32[" + value.getArrayDimensions()[0] + "]){");
			} else if (value.getCompositeClass() == UnsignedInteger.class) {
				out.write("(UA_UInt32[" + value.getArrayDimensions()[0] + "]){");
			} else if (value.getCompositeClass() == Long.class) {
				out.write("(UA_Int64[" + value.getArrayDimensions()[0] + "]){");
			} else if (value.getCompositeClass() == UnsignedLong.class) {
				out.write("(UA_UInt64[" + value.getArrayDimensions()[0] + "]){");
			} else if (value.getCompositeClass() == Float.class) {
				out.write("(UA_Float[" + value.getArrayDimensions()[0] + "]){");
			} else if (value.getCompositeClass() == Double.class) {
				out.write("(UA_Double[" + value.getArrayDimensions()[0] + "]){");
			} else if (value.getCompositeClass() == String.class) {
				out.write("(UA_String[" + value.getArrayDimensions()[0] + "]){");
			} else if (value.getCompositeClass() == ByteString.class) {
				out.write("(UA_ByteString[" + value.getArrayDimensions()[0] + "]){");
			} else if (value.getCompositeClass() == LocalizedText.class) {
				out.write("(UA_LocalizedText[" + value.getArrayDimensions()[0] + "]){");
			} else if (value.getCompositeClass() == EnumValueType.class) {
				out.write("(UA_EnumValueType[" + value.getArrayDimensions()[0] + "]){");
			} else
				out.write(AddressSpaceNodeModelFactoryC.class.getName()
						+ " line: 544 \nTODO add datatype serialization " + value.getCompositeClass() + "\n");

			Object bval[] = (Object[]) value.getValue();
			if (value.getCompositeClass() == String.class || value.getCompositeClass() == ByteString.class) {
				for (int i = 0; i < value.getArrayDimensions()[0]; i++) {
					out.write(comma + "{" + bval[i].toString().length() + ", (unsigned char *)\"" + bval[i].toString()
							+ "\"}");
					comma = ",";
				}
			} else if (value.getCompositeClass() == EnumValueType.class) {
				for (int i = 0; i < value.getArrayDimensions()[0]; i++) {
					out.write(comma + "{");
					out.write(((EnumValueType[]) value.getValue())[i].getValue().toString());
					out.write(", ");
					helpLocalizedTextC(out, ((EnumValueType[]) value.getValue())[i].getDisplayName());
					out.write(", ");
					helpLocalizedTextC(out, ((EnumValueType[]) value.getValue())[i].getDescription());
					out.write("}");
					comma = ",";
				}
			} else if (value.getCompositeClass() == LocalizedText.class) {
				for (int i = 0; i < value.getArrayDimensions()[0]; i++) {
					LocalizedText loc = (LocalizedText) bval[i];
					if (loc == null)
						loc = LocalizedText.EMPTY;
					String id = loc.getLocaleId();
					String text = loc.getText();
					if (id == null)
						id = "";
					if (text == null)
						text = "";

					out.write(comma);
					helpLocalizedTextC(out, loc);
					// out.write(comma + "{{" + id.length() + "(unsigned char*)\"" + id + "\"},{" +
					// text.length() + ", (unsigned char *)\"" + text + "\"}}");
					comma = ",";
				}
			} else {
				for (int i = 0; i < value.getArrayDimensions()[0]; i++) {
					out.write(comma + bval[i].toString());
					comma = ",";
				}
			}
			out.write("}");
		}
		return 1;
	}
	
	private static int helpCreateValuefromDatatype(BufferedWriter out, NodeId dataType) throws IOException {
		if(dataType.compareTo(Identifiers.UInt16) == 0)
			out.write("(UA_UInt16[1]){0}");
		else if(dataType.compareTo(Identifiers.Int16) == 0)
			out.write("(UA_Int16[1]){0}");
		else if(dataType.compareTo(Identifiers.UInt32) == 0)
			out.write("(UA_UInt32[1]){0}");
		else if(dataType.compareTo(Identifiers.Int32) == 0)
			out.write("(UA_Int32[1]){0}");
		else if(dataType.compareTo(Identifiers.UInt64) == 0)
			out.write("(UA_UInt64[1]){0}");
		else if(dataType.compareTo(Identifiers.Int64) == 0)
			out.write("(UA_Int64[1]){0}");
		else if(dataType.compareTo(Identifiers.Double) == 0)
			out.write("(UA_Double[1]){0}");
		else if(dataType.compareTo(Identifiers.Float) == 0)
			out.write("(UA_Float[1]){0}");
		else if(dataType.compareTo(Identifiers.String) == 0)
			out.write("(UA_String[1]){{0, (unsigned char *)\"\"}}");
		else if(dataType.compareTo(Identifiers.Boolean) == 0)
			out.write("(UA_Boolean[1]){false}");
		else if(dataType.compareTo(Identifiers.Byte) == 0)
			out.write("(UA_SByte[1]){0}");
		else if(dataType.compareTo(Identifiers.SByte) == 0)
			out.write("(UA_SByte[1]){0}");
		else
			out.write("NULL");
		return 1;
	}

	public static int helpArrayDimensionsC(BufferedWriter out, Variant value,
			/* int[] arrayDimensions, */ int valueRank, int index) throws IOException {
		int length = 0;
		if (value != null && value.getValue() != null && value.getArrayDimensions() != null && valueRank > -1) {
			length = value.getArrayDimensions().length;
		}
		String comma = "";
		String help = "";
		switch (length) {
		case 0:
			out.write("NULL");
			// do not write pre string
			return 1;
		default:
			for (int i = 0; i < length; i++) {
				help += comma + value.getArrayDimensions()[i];
				comma = ",";
			}
			out.write("(UA_UInt32[" + length + "]){" + help + "}");
			return length;
		}
	}

	public static int helpArrayDimensionsC(BufferedWriter out, UnsignedInteger[] value,
			/* int[] arrayDimensions, */ int valueRank, int index) throws IOException {
		int length = 0;
		if (value != null && valueRank > -1) {
			length = value.length;
		}
		String comma = "";
		String help = "";
		switch (length) {
		case 0:
			out.write("NULL");
			// do not write pre string
			return 1;
		default:
			for (int i = 0; i < length; i++) {
				help += comma + value[i].intValue();
				comma = ",";
			}
			out.write("(UA_UInt32[" + length + "]){" + help + "}");
			return length;
		}
	}

	private static int helpCreateArrayDimensionsC(BufferedWriter out, UnsignedInteger[] arrayDimensions, int valueRank,
			int index) throws IOException {
		int length = 0;
		if (arrayDimensions != null && valueRank > -1) {
			length = arrayDimensions.length;
		}
		String comma = "";
		String help = "";
		switch (length) {
		case 0:
			// do not write pre string
			out.write("NULL");
			return 1;
		default:
			for (int i = 0; i < length; i++) {
				help += comma + arrayDimensions[i];
				comma = ",";
			}
			out.write("(UA_UInt32[" + length + "]){" + help + "}");
			return length;
		}
	}

	private int helpMemSize(BufferedWriter out, int size) throws IOException {
		out.write("" + size);
		return 1;
	}

	public static int helpQualifiedNameC(BufferedWriter out, QualifiedName text) throws IOException {
		if (text == null) {
			out.write("NULL");
			return 0;
		} else {
			out.write("{" + text.getNamespaceIndex() + ", { " + text.getName().length() + ", (unsigned char *)\""
					+ text.getName() + "\"}}");
			return 2 + text.getName().length();
		}
	}

	public static int helpQualifiedNamesC(BufferedWriter out, QualifiedName[] text) throws IOException {
		if (text == null) {
			out.write("NULL");
			return 0;
		} else {
			String comma = "";
			out.write("UA_QualifiedName[]{");
			for (QualifiedName qn : text) {
				out.write(comma);
				out.write("{" + qn.getNamespaceIndex() + ", { " + qn.getName().length() + ", (unsigned char *)\""
						+ qn.getName() + "\"}}");
			}
			out.write("}");
			return 1;
		}
	}

	/**********************************************************************************************************
	 * functions to export an whole variant
	 * 
	 * @throws IOException
	 **********************************************************************************************************/
	public static int helpVariantC(BufferedWriter out, Variant value, NodeId dataType, Integer valueRank, NamespaceTable nsTable,
			NamespaceTable serverTable) throws IOException {
		int memcount = 0;
		out.write("{");
		memcount += helpDataTypeC(out, dataType,nsTable, serverTable);

		out.write(", ");
		memcount += helpStorageTypeC(out);
		out.write(",");
		memcount += helpArrayLengthC(out, value/* .getArrayDimensions() */, valueRank); // we can only create one dim
																						// arrays at the moment
		out.write(", ");
		helpCreateVariantDataC(out, value, dataType, nsTable, serverTable);
		out.write(",\n");
		memcount += helpArrayDimensionsSizeC(out, value /* .getArrayDimensions() */, valueRank);
		out.write(", ");
		memcount += helpArrayDimensionsC(out, value /* .getArrayDimensions() */, valueRank, index);

		out.write("}\n");

		return memcount;
	}

	/*
	 * storage type is type UA_DataType * -> 8 Byte
	 */
	public static int helpDataTypeC(BufferedWriter out, NodeId dataType, NamespaceTable nsTable, NamespaceTable serverTable) throws IOException {
		if (dataType == null) {
			out.write("NULL");
			return 0;
		}
		if (dataType.equals(Identifiers.Boolean))
			out.write("&UA_TYPES[UA_TYPES_BOOLEAN]");
		else if (dataType.equals(Identifiers.SByte))
			out.write("&UA_TYPES[UA_TYPES_SBYTE]");
		else if (dataType.equals(Identifiers.Byte))
			out.write("&UA_TYPES[UA_TYPES_BYTE]");
		else if (dataType.equals(Identifiers.Int16))
			out.write("&UA_TYPES[UA_TYPES_INT16]");
		else if (dataType.equals(Identifiers.UInt16))
			out.write("&UA_TYPES[UA_TYPES_UINT16]");
		else if (dataType.equals(Identifiers.Int32))
			out.write("&UA_TYPES[UA_TYPES_INT32]");
		else if (dataType.equals(Identifiers.UInt32))
			out.write("&UA_TYPES[UA_TYPES_UINT32]");
		else if (dataType.equals(Identifiers.Int64))
			out.write("&UA_TYPES[UA_TYPES_INT64]");
		else if (dataType.equals(Identifiers.UInt64))
			out.write("&UA_TYPES[UA_TYPES_UINT64]");
		else if (dataType.equals(Identifiers.Float))
			out.write("&UA_TYPES[UA_TYPES_FLOAT]");
		else if (dataType.equals(Identifiers.Double))
			out.write("&UA_TYPES[UA_TYPES_DOUBLE]");
		else if (dataType.equals(Identifiers.String))
			out.write("&UA_TYPES[UA_TYPES_STRING]");
		else if (dataType.equals(Identifiers.DateTime))
			out.write("&UA_TYPES[UA_TYPES_DATETIME]");
		else if (dataType.equals(Identifiers.Guid))
			out.write("&UA_TYPES[UA_TYPES_GUID]");
		else if (dataType.equals(Identifiers.ByteString))
			out.write("&UA_TYPES[UA_TYPES_BYTESTRING]");
		else if (dataType.equals(Identifiers.LocalizedText))
			out.write("&UA_TYPES[UA_TYPES_LOCALIZEDTEXT]");
		else if (dataType.equals(Identifiers.EnumValueType))
			out.write("&UA_TYPES[UA_TYPES_ENUMVALUETYPE]");
		else if (dataType.equals(Identifiers.Image))
			out.write("&UA_TYPES[UA_TYPES_BYTESTRING]");
		else {
//			Node node = server
//			// we have an custom datatype
//			out.write("&{\n"); 
//			helpNodeIdC(out, nsTable, serverTable, dataType);
//					///"    {0, UA_NODEIDTYPE_NUMERIC, {1}}, /* .typeId */\r\n" + 
//			out.write("    {0, UA_NODEIDTYPE_NUMERIC, {0}}, /* .binaryEncodingId */\n");
//			out.write("    sizeof(UA_Boolean), /* .memSize */\n");
//			out.write("    UA_TYPES_BOOLEAN, /* .typeIndex */\n");
//			out.write("    UA_DATATYPEKIND_BOOLEAN, /* .typeKind */\n"); 
//			out.write("    true, /* .pointerFree */\n");
//			out.write("    true, /* .overlayable */\r\n");
//			out.write("    0, /* .membersSize */\n");
//			out.write("    Boolean_members  /* .members */\n");
//			out.write("    UA_TYPENAME(\"Boolean\") /* .typeName */\n"); 
//			out.write("}");
			out.write("&UA_TYPES[UA_TYPES_BYTESTRING]");
		}
		return 8;
	}

	/*
	 * storage type is type UA_VARIANT_DATA -> 4 Byte
	 */
	public static int helpStorageTypeC(BufferedWriter out) throws IOException {
		out.write("UA_VARIANT_DATA");
		return 4;
	}

	public static int helpUnsignedShortC(BufferedWriter out, UnsignedShort value) throws IOException {
		if (value == null)
			out.write("0");
		else
			out.write(Integer.toString(value.intValue()));

		return 0;
	}

	public static int helpUnsignedByteC(BufferedWriter out, UnsignedByte value) throws IOException {
		if (value == null)
			out.write("0");
		else
			out.write(Integer.toString(value.intValue()));

		return 0;
	}

	public static int helpStatusCodeC(BufferedWriter out, StatusCode value) throws IOException {
		if (value == null)
			out.write("0");
		else
			out.write(Integer.toString(value.getValueAsIntBits()));

		return 0;
	}

	public static int helpBooleanValue(BufferedWriter out, Boolean value) throws IOException {
		if (value == null || value == false)
			out.write("0");
		else
			out.write("1");

		return 0;
	}

	public static int helpDateTimeC(BufferedWriter out, DateTime value) throws IOException {
		if (value == null)
			out.write("0");
		else
			out.write("" + value.getTimeInMillis());

		return 0;
	}

	/*
	 * arraylength is type size_t -> 8 Byte
	 */
	private static int helpArrayLengthC(BufferedWriter out, Variant value, Integer valueRank) throws IOException {
		if (value != null && value.getValue() != null && value.getArrayDimensions() != null
				&& value.getArrayDimensions().length > 0 && valueRank >= 0)
			out.write("" + value.getArrayDimensions()[0]);
		else
			out.write("0");
		return 8;
	}

	private int helpValueRankC(BufferedWriter out, int valueRank) throws IOException {
		out.write("" + valueRank);
		return 1;
	}

	private static int helpArrayDimensionsSizeC(BufferedWriter out, Variant value,
			/* int[] arrayDimensions, */ Integer valueRank) throws IOException {
		if (value != null && value.getValue() != null && value.getArrayDimensions() != null && valueRank != null
				&& valueRank >= 0)
			out.write("" + value.getArrayDimensions().length);
		else
			out.write("0");
		return 1;
	}

	private int helpArrayDimensionsSizeC(BufferedWriter out, UnsignedInteger[] arrayDimensions, Integer valueRank)
			throws IOException {
		if (arrayDimensions != null && valueRank >= 0)
			out.write("" + arrayDimensions.length);
		else
			out.write("0");
		return 1;
	}

	private int helpSpecifiedAttributesC(BufferedWriter out, int attributes) throws IOException {
		out.write("" + attributes);
		return 1;
	}

	private int helpAccessLevel(BufferedWriter out, UnsignedByte level) throws IOException {
		if (level == null)
			out.write("0");
		else
			out.write("" + level.intValue());
		return 1;
	}

	private int helpSamplingIntervalC(BufferedWriter out, Double interval) throws IOException {
		if (interval == null)
			out.write("0.0");
		else
			out.write("" + interval.floatValue());
		return 1;
	}

	private int helpHistorizing(BufferedWriter out, Boolean historizing) throws IOException {
		if (historizing == null)
			out.write("false");
		else
			out.write("" + historizing);
		return 1;
	}

	private int helpExecutable(BufferedWriter out, Boolean executable) throws IOException {
		if (executable == null)
			out.write("false");
		else
			out.write("" + executable);
		return 1;
	}

	private int helpWriteMask(BufferedWriter out, UnsignedInteger mask) throws IOException {
		if (mask == null)
			out.write("0");
		else
			out.write("" + mask.intValue());
		return 1;
	}

	private int helpIsAbstractC(BufferedWriter out, Boolean isAbstract) throws IOException {
		if (isAbstract == null)
			out.write("false");
		else
			out.write("" + isAbstract);
		return 1;
	}

	private int helpEventNotifierC(BufferedWriter out, UnsignedByte notifier) throws IOException {
		if (notifier == null)
			out.write("0");
		else
			out.write("" + notifier.byteValue());
		return 1;
	}

	private void writeNodeDataTypeC(BufferedWriter out, NamespaceTable nsTable, NamespaceTable serverTable,
			UADataTypeNode node) throws IOException {
		NodeId parentNode = null;
		NodeId referenceType = null;
		boolean ishierarchical = false;
		// find parent nodeid
		for (ReferenceNode ref : node.getReferences()) {
			if (ref.getIsInverse()) {
				ishierarchical = checkIsReferenceType(serverTable, ref.getReferenceTypeId(),
						Identifiers.HierarchicalReferences);
				if (ishierarchical) {
					try {
						parentNode = serverTable.toNodeId(ref.getTargetId());
						referenceType = ref.getReferenceTypeId();
					} catch (ServiceResultException e) {
						logger.log(Level.SEVERE, e.getMessage());
					}
				}
			}
		}

		// create data variable
		// helpPreCreateDataC(out, node.getValue(), index);
		// create array dim variable
		// helpPreCreateArrayDimensionsC(out, node.getArrayDimensions(),
		// node.getValueRank(), index);

		out.write("btech_addDataTypeNodeStruct add_" + index + " = ");
		out.write("{");
		// first write memsize
		helpMemSize(out, 0);
		out.write(",");
		helpNodeIdC(out, nsTable, serverTable, node.getNodeId());
		out.write(",");
		helpNodeIdC(out, nsTable, serverTable, parentNode);
		out.write(",");
		helpNodeIdC(out, nsTable, serverTable, referenceType);
		out.write(",");
		helpQualifiedNameC(out, node.getBrowseName());
		out.write(",");
//				helpNodeIdC(out, nsTable, serverTable, typeDefinition);
//				out.write(",");
		// attributes
		out.write("{");
		helpSpecifiedAttributesC(out, 0);
		out.write(",");
		helpLocalizedTextC(out, node.getDisplayName());
		out.write(",");
		helpLocalizedTextC(out, node.getDescription());
		out.write(",");
		helpWriteMask(out, node.getWriteMask());
		out.write(",");
		helpWriteMask(out, node.getUserWriteMask());
		out.write(",");
		helpIsAbstractC(out, node.getIsAbstract());
		out.write("},");
		// out.write(",\n");
		out.write("NULL,NULL};\n");

		out.write("btech_ua_server_addDataTypeNode_Init(&add_" + index + ");\n");

		index++;
		out.flush();

	}

	private void writeNodeMethodC(BufferedWriter out, BufferedWriter outStub, BufferedWriter outStubH,
			NamespaceTable nsTable, NamespaceTable serverTable, UAMethodNode node) throws IOException {
		NodeId parentNode = null;
		NodeId referenceType = null;
//		NodeId typeDefinition = null;
		int inputArgsCount = 0;
		Argument inputArgs[] = null;
		int outputArgsCount = 0;
		Argument outputArgs[] = null;
		boolean ishierarchical = false;
		int inputIndex = 0;
		int outputIndex = 0;

		// find parent nodeid
		out.write("\n\t/* *******************************************************************");
		out.write("\n\t *  start create mehode node code for " + node.getBrowseName());
		out.write("\n\t * *******************************************************************/\n");

		// find parent nodeid
		for (ReferenceNode ref : node.getReferences()) {
			if (ref.getIsInverse()) {
				ishierarchical = checkIsReferenceType(serverTable, ref.getReferenceTypeId(),
						Identifiers.HierarchicalReferences);
				if (ishierarchical) {
					try {
						parentNode = serverTable.toNodeId(ref.getTargetId());
						referenceType = ref.getReferenceTypeId();
					} catch (ServiceResultException e) {
						logger.log(Level.SEVERE, e.getMessage());
					}
				}
			} else {
				Node tmp = serverInstance.getAddressSpaceManager().getNodeById(ref.getTargetId());
				if (tmp instanceof UAVariableNode) {
					UAVariableNode var = (UAVariableNode) tmp;
					if (var.getValue() != null && var.getValue().getValue() != null) {
						if (var.getValue().getCompositeClass() == Argument.class) {
							if (var.getBrowseName().getName().compareTo("InputArguments") == 0) {
								inputArgs = (Argument[]) var.getValue().getValue();
							} else if (var.getBrowseName().getName().compareTo("OutputArguments") == 0) {
								outputArgs = (Argument[]) var.getValue().getValue();
							}
						}
					}
				}
			}
		}

		if (inputArgs != null && inputArgs.length > 0) {
			inputIndex = index;
			writeMethodArgumentsC(out, inputArgs, nsTable, serverTable);
			inputArgsCount = inputArgs.length;
		}
		if (outputArgs != null && outputArgs.length > 0) {
			outputIndex = index;
			writeMethodArgumentsC(out, outputArgs, nsTable, serverTable);
			outputArgsCount = outputArgs.length;
		}

		outStubH.write("UA_StatusCode btech_Method_" + index
				+ ("(UA_Server *server,\r\n" + "  const UA_NodeId *sessionId, void *sessionHandle,\r\n"
						+ "  const UA_NodeId *methodId, void *methodContext,\r\n"
						+ "  const UA_NodeId *objectId, void *objectContext,\r\n"
						+ "  size_t inputSize, const UA_Variant *input,\r\n"
						+ "  size_t outputSize, UA_Variant *output);\n"));

		// create method stub for implementation
		outStub.write("\n\t/* *******************************************************************");
		outStub.write("\n\t *  start method stub code for " + node.getBrowseName());
		outStub.write("\n\t * *******************************************************************/\n");
		outStub.write("UA_StatusCode btech_Method_" + index
				+ ("(UA_Server *server,\r\n" + "  const UA_NodeId *sessionId, void *sessionHandle,\r\n"
						+ "  const UA_NodeId *methodId, void *methodContext,\r\n"
						+ "  const UA_NodeId *objectId, void *objectContext,\r\n"
						+ "  size_t inputSize, const UA_Variant *input,\r\n"
						+ "  size_t outputSize, UA_Variant *output)"));
		outStub.write("{\n");
		outStub.write("\treturn UA_STATUSCODE_GOOD;\n");
		outStub.write("}");

		out.write("\n\tbtech_addMethodNodeStruct add_" + index + " = ");
		out.write("{");
		// first write memsize
		helpMemSize(out, 0);
		out.write(",");
		helpNodeIdC(out, nsTable, serverTable, node.getNodeId());
		out.write(",");
		helpNodeIdC(out, nsTable, serverTable, parentNode);
		out.write(",");
		helpNodeIdC(out, nsTable, serverTable, referenceType);
		out.write(",");
		helpQualifiedNameC(out, node.getBrowseName());
		out.write(",");

		// attributes
		out.write("{");
		helpSpecifiedAttributesC(out, 0);
		out.write(",");
		helpLocalizedTextC(out, node.getDisplayName());
		out.write(",");
		helpLocalizedTextC(out, node.getDescription());
		out.write(",");
		helpWriteMask(out, node.getWriteMask());
		out.write(",");
		helpWriteMask(out, node.getUserWriteMask());
		out.write(",");
		helpExecutable(out, node.getExecutable());
		out.write(",");
		helpExecutable(out, node.getUserExecutable());
		out.write("},");

		out.write("btech_Method_" + index);
		out.write(",");

		out.write(inputArgsCount + ",");
		if (inputIndex > 0)
			out.write("argument" + inputIndex);
		else
			out.write("NULL");
		out.write(",");
		out.write(outputArgsCount + ",");
		if (outputIndex > 0)
			out.write("argument" + outputIndex);
		else
			out.write("NULL");
		out.write(",");
		out.write("NULL,NULL};\n");

		out.write("btech_ua_server_addMethodNode_Init(&add_" + index + ");\n");
		index++;

		out.flush();
		outStub.flush();
		outStubH.flush();
	}

	private int writeMethodArgumentsC(BufferedWriter out, Argument args[], NamespaceTable nsTable,
			NamespaceTable serverTable) throws IOException {
		int count = 0;
		// first create arguments
		/*
		 * UA_String name; UA_NodeId dataType; UA_Int32 valueRank; size_t
		 * arrayDimensionsSize; UA_UInt32 *arrayDimensions; UA_LocalizedText
		 * description;
		 */

		out.write("UA_Argument argument" + index + "[] = {\n");
		int i = 0;
		String comma = "";
		for (i = 0; i < args.length; i++) {

			// helpPreCreateArrayDimensionsC(out, args[i].getArrayDimensions(),
			// args[i].getValueRank(), index + i);

			// out.write(" UA_Argument_init(&(inputArgument[\" + args.length + \"]));\n");
			out.write(comma);
			out.write("{");
			out.write("{ " + args[i].getName().length() + ", (unsigned char *)\"" + args[i].getName() + "\"}");
			out.write(", ");
//			UA_STRING(\"" + args[i].getName() +"\"), ");
//			out.write("    inputArgument.dataType = ");
			helpNodeIdC(out, nsTable, serverTable, args[i].getDataType());
//			helpDataTypeC(out, args[i].getDataType());
			out.write(", ");
//			out.write("    inputArgument.valueRank = ");
			helpValueRankC(out, args[i].getValueRank());
			out.write(",");
//			out.write("    inputArgument.arrayDimensionsSize = ");
			helpArrayDimensionsSizeC(out, args[i].getArrayDimensions(), args[i].getValueRank());
			out.write(",");
//			out.write("    inputArgument.arrayDimensions = ");
			helpCreateArrayDimensionsC(out, args[i].getArrayDimensions(), args[i].getValueRank(), index + i);
//			helpArrayDimensionC(out, args[i].getArrayDimensions(), args[i].getValueRank(), index + i);
			out.write(",");
//			out.write("    inputArgument[0].description = ");
			helpLocalizedTextC(out, args[i].getDescription());
			out.write("}");
			comma = ",";
		}
		out.write("};\n");
		index = index + i;

		return count;
	}

	private void writeNodeObjectC(BufferedWriter out, NamespaceTable nsTable, NamespaceTable serverTable,
			UAObjectNode node) throws IOException {

		NodeId parentNode = null;
		NodeId referenceType = null;
		NodeId typeDefinition = null;
		boolean ishierarchical = false;
		// find parent nodeid
		for (ReferenceNode ref : node.getReferences()) {
			if (ref.getIsInverse()) {
				ishierarchical = checkIsReferenceType(serverTable, ref.getReferenceTypeId(),
						Identifiers.HierarchicalReferences);
				if (ishierarchical) {
					try {
						parentNode = serverTable.toNodeId(ref.getTargetId());
						referenceType = ref.getReferenceTypeId();
					} catch (ServiceResultException e) {
						logger.log(Level.SEVERE, e.getMessage());
					}
				}
			} else if (ref.getReferenceTypeId().compareTo(Identifiers.HasTypeDefinition) == 0) {
				try {
					typeDefinition = nsTable.toNodeId(ref.getTargetId());
				} catch (ServiceResultException e) {
					logger.log(Level.SEVERE, e.getMessage());
				}
			}
		}

		out.write("btech_addObjectNodeStruct add_" + index + " = ");
		out.write("{");
		// first write memsize
		helpMemSize(out, 0);
		out.write(",");
		helpNodeIdC(out, nsTable, serverTable, node.getNodeId());
		out.write(",");
		helpNodeIdC(out, nsTable, serverTable, parentNode);
		out.write(",");
		helpNodeIdC(out, nsTable, serverTable, referenceType);
		out.write(",");
		helpQualifiedNameC(out, node.getBrowseName());
		out.write(",");
		helpNodeIdC(out, nsTable, serverTable, typeDefinition);
		out.write(",");
		// attributes
		out.write("{");
		helpSpecifiedAttributesC(out, 0);
		out.write(",");
		helpLocalizedTextC(out, node.getDisplayName());
		out.write(",");
		helpLocalizedTextC(out, node.getDescription());
		out.write(",");
		helpWriteMask(out, node.getWriteMask());
		out.write(",");
		helpWriteMask(out, node.getUserWriteMask());
		out.write(",");
		helpEventNotifierC(out, node.getEventNotifier());
		out.write("},");
		out.write("NULL,NULL};\n");

		out.write("btech_ua_server_addObjectNode_Init(&add_" + index + ");\n");
		index++;
		out.flush();
	}

	private void writeNodeObjectTypeC(BufferedWriter out, NamespaceTable nsTable, NamespaceTable serverTable,
			UAObjectTypeNode node) throws IOException {

		NodeId parentNode = null;
		NodeId referenceType = null;
		boolean ishierarchical = false;

		// find parent nodeid
		for (ReferenceNode ref : node.getReferences()) {
			if (ref.getIsInverse()) {
				ishierarchical = checkIsReferenceType(serverTable, ref.getReferenceTypeId(),
						Identifiers.HierarchicalReferences);
				if (ishierarchical) {
					try {
						parentNode = serverTable.toNodeId(ref.getTargetId());
						referenceType = ref.getReferenceTypeId();
					} catch (ServiceResultException e) {
						logger.log(Level.SEVERE, e.getMessage());
					}
				}
			}
		}

		out.write("btech_addObjectTypeNodeStruct add_" + index + " = ");
		out.write("{");
		// first write memsize
		helpMemSize(out, 0);
		out.write(",");
		helpNodeIdC(out, nsTable, serverTable, node.getNodeId());
		out.write(",");
		helpNodeIdC(out, nsTable, serverTable, parentNode);
		out.write(",");
		helpNodeIdC(out, nsTable, serverTable, referenceType);
		out.write(",");
		helpQualifiedNameC(out, node.getBrowseName());
		// attributes
		out.write(",{");
		helpSpecifiedAttributesC(out, 0);
		out.write(",");
		helpLocalizedTextC(out, node.getDisplayName());
		out.write(",");
		helpLocalizedTextC(out, node.getDescription());
		out.write(",");
		helpWriteMask(out, node.getWriteMask());
		out.write(",");
		helpWriteMask(out, node.getUserWriteMask());
		out.write(",");
		helpIsAbstractC(out, node.getIsAbstract());
		out.write("},");
		// out.write(",\n");
		out.write("NULL,NULL};\n");

		out.write("btech_ua_server_addObjectTypeNode_Init(&add_" + index + ");\n");
		index++;
		out.flush();
	}

	private void writeNodeReferenceTypeC(BufferedWriter out, NamespaceTable nsTable, NamespaceTable serverTable,
			UAReferenceTypeNode node) throws IOException {

		NodeId parentNode = null;
		NodeId referenceType = null;
		boolean ishierarchical = false;
		// find parent nodeid
		for (ReferenceNode ref : node.getReferences()) {
			if (ref.getIsInverse()) {
				ishierarchical = checkIsReferenceType(serverTable, ref.getReferenceTypeId(),
						Identifiers.HierarchicalReferences);
				if (ishierarchical) {
					try {
						parentNode = serverTable.toNodeId(ref.getTargetId());
						referenceType = ref.getReferenceTypeId();
					} catch (ServiceResultException e) {
						logger.log(Level.SEVERE, e.getMessage());
					}
				}
			}
		}

		// reference type node
		out.write("btech_addReferenceTypeNodeStruct add_" + index + " = ");

		out.write("{");
		// first write memsize
		helpMemSize(out, 0);
		out.write(",");
		helpNodeIdC(out, nsTable, serverTable, node.getNodeId());
		out.write(",");
		helpNodeIdC(out, nsTable, serverTable, parentNode);
		out.write(",");
		helpNodeIdC(out, nsTable, serverTable, referenceType);
		out.write(",");
		helpQualifiedNameC(out, node.getBrowseName());
		// attributes
		out.write(",{");
		helpSpecifiedAttributesC(out, 0);
		out.write(",");
		helpLocalizedTextC(out, node.getDisplayName());
		out.write(",");
		helpLocalizedTextC(out, node.getDescription());
		out.write(",");
		helpWriteMask(out, node.getWriteMask());
		out.write(",");
		helpWriteMask(out, node.getUserWriteMask());
		out.write(",");
		helpIsAbstractC(out, node.getIsAbstract());
		out.write("},");
		// out.write(",\n");
		out.write("NULL,NULL};\n");

		out.write("btech_ua_server_addReferenceTypeNode_Init(&add_" + index + ");\n");
		index++;
		out.flush();
	}

	private void writeNodeVariableC(BufferedWriter out, BufferedWriter outCallBack, BufferedWriter outCallBackH,
			NamespaceTable nsTable, NamespaceTable serverTable, UAVariableNode node) throws IOException {
		NodeId dataType = node.getDataType();
		NodeId parentNode = null;
		NodeId referenceType = null;
		NodeId typeDefinition = null;
		boolean ishierarchical = false;
		if (dataType.compareTo(new NodeId(0, 0)) == 0)
			dataType = Identifiers.BaseDataType;

		if (node.getBrowseName().getName().compareTo("OutputArguments") == 0
				|| node.getBrowseName().getName().compareTo("InputArguments") == 0)
			return;

		// find parent nodeid
		for (ReferenceNode ref : node.getReferences()) {
			// is inverse and hierarchical

			if (ref.getIsInverse()) {
				ishierarchical = checkIsReferenceType(serverTable, ref.getReferenceTypeId(),
						Identifiers.HierarchicalReferences);
				if (ishierarchical) {
					try {
						parentNode = serverTable.toNodeId(ref.getTargetId());
						referenceType = ref.getReferenceTypeId();
					} catch (ServiceResultException e) {
						logger.log(Level.SEVERE, e.getMessage());
					}
				}
			} else if (ref.getReferenceTypeId().compareTo(Identifiers.HasTypeDefinition) == 0) {
				try {
					typeDefinition = nsTable.toNodeId(ref.getTargetId());
				} catch (ServiceResultException e) {
					logger.log(Level.SEVERE, e.getMessage());
				}
			}
		}

		out.write("btech_addVariableNodeStruct add_" + index + " = ");
		out.write("{");
		// first write memsize
		helpMemSize(out, 0);
		out.write(",");
		helpNodeIdC(out, nsTable, serverTable, node.getNodeId());
		out.write(",");
		helpNodeIdC(out, nsTable, serverTable, parentNode);
		out.write(",");
		helpNodeIdC(out, nsTable, serverTable, referenceType);
		out.write(",");
		helpQualifiedNameC(out, node.getBrowseName());
		out.write(",");
		helpNodeIdC(out, nsTable, serverTable, typeDefinition);
		out.write(",");
		// attributes
		out.write("{");
		helpSpecifiedAttributesC(out, 0);
		out.write(",");
		helpLocalizedTextC(out, node.getDisplayName());
		out.write(",");
		helpLocalizedTextC(out, node.getDescription());
		out.write(",");
		helpWriteMask(out, node.getWriteMask());
		out.write(",");
		helpWriteMask(out, node.getUserWriteMask());
		out.write(",");
		helpVariantC(out, node.getValue(), node.getDataType(), node.getValueRank(), nsTable, serverTable);
		out.write(",");
		helpNodeIdC(out, nsTable, serverTable, node.getDataType());
		out.write(",");
		helpValueRankC(out, node.getValueRank());
		out.write(",");
		helpArrayDimensionsSizeC(out, node.getArrayDimensions(), node.getValueRank());
		out.write(", ");
		helpCreateArrayDimensionsC(out, node.getArrayDimensions(), node.getValueRank(), index);
		// helpArrayDimensionC(out, node.getArrayDimensions(), node.getValueRank(),
		// index);
		out.write(",");
		helpAccessLevel(out, node.getAccessLevel());
		out.write(",");
		helpAccessLevel(out, node.getUserAccessLevel());
		out.write(",");
		helpSamplingIntervalC(out, node.getMinimumSamplingInterval());
		out.write(",");
		helpHistorizing(out, node.getHistorizing());
		out.write("},");
		// out.write(",\n");
		out.write("NULL,NULL};\n");

		out.write("// add callback\n");
		out.write("add_" + index + ".addNodeCallback = addValueCallbackToVariable_" + index + ";\n");
		out.write("\n");

		out.write("btech_ua_server_addVariableNode_Init(&add_" + index + ");\n");

		// write callback functions
		outCallBack.write("/* callbacks for node: \n");
		outCallBack.write("* 	NodeId: " + node.getNodeId() + " \n");
		outCallBack.write("* BrowseName: " + node.getBrowseName() + " \n");
		outCallBack.write("* DisplayName: " + node.getDisplayName() + " \n");
		outCallBack.write("* Description: " + node.getDescription() + " */\n");
		outCallBack.write("static void\n");
		outCallBack.write("beforeReadCallbackToVariable_" + index + "(UA_Server *server,\n");
		outCallBack.write("               const UA_NodeId *sessionId, void *sessionContext,\n");
		outCallBack.write("               const UA_NodeId *nodeid, void *nodeContext,\n");
		outCallBack.write("               const UA_NumericRange *range, const UA_DataValue *data)");
		outCallBack.write("{\n");
		outCallBack.write("/* method stub, add implementation here */\n");
		outCallBack.write("}\n\n");
		outCallBack.write("static void\n");
		outCallBack.write("afterWriteCallbackToVariable_" + index + "(UA_Server *server,\n");
		outCallBack.write("               const UA_NodeId *sessionId, void *sessionContext,\n");
		outCallBack.write("               const UA_NodeId *nodeId, void *nodeContext,\n");
		outCallBack.write("               const UA_NumericRange *range, const UA_DataValue *data)\n");
		outCallBack.write("{\n");
		outCallBack.write("/* method stub, add implementation here */\n");
		outCallBack.write("}\n\n");

		outCallBack.write("UA_StatusCode\n");
		outCallBack.write("addValueCallbackToVariable_" + index + "(void) {\n");

		outCallBack.write("UA_NodeId currentNodeId = ");
		helpNodeIdC(outCallBack, nsTable, serverTable, node.getNodeId());
		outCallBack.write(";\n");
		outCallBack.write("UA_ValueCallback callback;\n");
		outCallBack.write("    callback.onRead = beforeReadCallbackToVariable_" + index + ";\n");
		outCallBack.write("    callback.onWrite = afterWriteCallbackToVariable_" + index + ";\n");
		outCallBack.write("    UA_Server_setVariableNode_valueCallback(g_pServer, currentNodeId, callback);\n");
		outCallBack.write("return UA_STATUSCODE_BADNOTIMPLEMENTED;");
		outCallBack.write("}\n");

		/** create callback header */
		outCallBackH.write("UA_StatusCode\n");
		outCallBackH.write("addValueCallbackToVariable_" + index + "(void);\n");
		index++;
		out.flush();
	}

	private void writeNodeVariableTypeC(BufferedWriter out, NamespaceTable nsTable, NamespaceTable serverTable,
			UAVariableTypeNode node) throws IOException {
		NodeId dataType = node.getDataType();
		NodeId parentNode = null;
		NodeId referenceType = null;
		boolean ishierarchical = false;
		if (dataType.compareTo(new NodeId(0, 0)) == 0)
			dataType = Identifiers.BaseDataType;

		// find parent nodeid
		for (ReferenceNode ref : node.getReferences()) {
			if (ref.getIsInverse()) {
				ishierarchical = checkIsReferenceType(serverTable, ref.getReferenceTypeId(),
						Identifiers.HierarchicalReferences);
				if (ishierarchical) {
					try {
						parentNode = serverTable.toNodeId(ref.getTargetId());
						referenceType = ref.getReferenceTypeId();
					} catch (ServiceResultException e) {
						logger.log(Level.SEVERE, e.getMessage());
					}
				}
			}
		}

		// create data variable
		// helpPreCreateDataC(out, node.getValue(), nsTable, serverTable, index);
		// create array dim variable
		// helpPreCreateArrayDimensionsC(out, node.getArrayDimensions(),
		// node.getValueRank(), index);

		out.write("btech_addVariableTypeNodeStruct add_" + index + " = ");
		out.write("{");
		// first write memsize
		helpMemSize(out, 0);
		out.write(",");
		helpNodeIdC(out, nsTable, serverTable, node.getNodeId());
		out.write(",");
		helpNodeIdC(out, nsTable, serverTable, parentNode);
		out.write(",");
		helpNodeIdC(out, nsTable, serverTable, referenceType);
		out.write(",");
		helpQualifiedNameC(out, node.getBrowseName());
		// attributes
		out.write(",{");
		helpSpecifiedAttributesC(out, 0);
		out.write(",");
		helpLocalizedTextC(out, node.getDisplayName());
		out.write(",");
		helpLocalizedTextC(out, node.getDescription());
		out.write(",");
		helpWriteMask(out, node.getWriteMask());
		out.write(",");
		helpWriteMask(out, node.getUserWriteMask());
		out.write(",");
		helpVariantC(out, node.getValue(), node.getDataType(), node.getValueRank(), nsTable, serverTable);
		out.write(",");
		helpNodeIdC(out, nsTable, serverTable, node.getDataType());
		out.write(",");
		helpValueRankC(out, node.getValueRank());
		out.write(",");
		helpArrayDimensionsSizeC(out, node.getArrayDimensions(), node.getValueRank());
		out.write(",");
		helpCreateArrayDimensionsC(out, node.getArrayDimensions(), node.getValueRank(), index);
//		helpArrayDimensionC(out, node.getArrayDimensions(), node.getValueRank(), index);
		out.write(",");
		helpIsAbstractC(out, node.getIsAbstract());
		out.write("},");
		// out.write(",\n");
		out.write("NULL,NULL};\n");

		out.write("btech_ua_server_addVariableTypeNode_Init(&add_" + index + ");\n");

		index++;
		out.flush();
	}

	private void writeNodeViewC(BufferedWriter out, NamespaceTable nsTable, NamespaceTable serverTable, ViewNode node)
			throws IOException {
		// view node
		out.write("btech_addReferenceNodeStruct add_" + index + " = ");
		out.write("{");
		out.write("},");
		// out.write(",\n");
		out.write("NULL,NULL};\n");

		out.write("btech_ua_server_addVariableTypeNode_Init(&add_" + index + ");\n");

		index++;
		out.flush();

		out.write("\tbtech_ua_server_addReference(\n\t\t" + "a_pServer,\n\t"
				+ helpNodeIdC(out, nsTable, serverTable, node.getNodeId()) + ",\n\t\t" + " UA_NodeClass.valueOf("
				+ node.getNodeClass().getValue() + ") ,\n\t\t" + helpQualifiedNameC(out, node.getBrowseName())
				+ " ,\n\t\t" + helpLocalizedTextC(out, node.getDisplayName()) + " ,\n\t\t"
				+ helpLocalizedTextC(out, node.getDescription()) + ",\n\t\t" + " new UnsignedInteger("
				+ node.getWriteMask() + ") ,\n\t\t" + " new UnsignedInteger(" + node.getUserWriteMask() + ") ,\n\t\t"
				+ "references2add.toArray(new ReferenceNode[0]) ,\n\t\t" + node.getContainsNoLoops()
				+ ", \n\t\t new UnsignedByte(" + node.getEventNotifier() + ")));".replace("\r", ""));
		out.newLine();
		out.flush();
	}

	private void writeReferenceNodesC(BufferedWriter out, NamespaceTable nsTable, NamespaceTable serverTable,
			ReferenceNode[] references) throws IOException {
		if (references == null) {
			return;
		}
	}

	public void setServerInstance(OPCInternalServer serverInstance) {
		this.serverInstance = serverInstance;
	}

	private boolean checkIsReferenceType(NamespaceTable nsTable, NodeId refTypeId, NodeId lookupId) {
		BrowseDescription[] nodesToBrowse = new BrowseDescription[1];
		BrowseDescription nodeToBrowse = new BrowseDescription();
		nodeToBrowse.setBrowseDirection(BrowseDirection.Inverse);
		nodeToBrowse.setIncludeSubtypes(true);
		nodeToBrowse.setNodeClassMask(NodeClass.ReferenceType);
		nodeToBrowse.setNodeId(refTypeId);
		nodeToBrowse.setReferenceTypeId(lookupId);
		nodeToBrowse.setResultMask(BrowseResultMask.ALL);
		nodesToBrowse[0] = nodeToBrowse;
		try {
			BrowseResult[] result = this.serverInstance.getMaster().browse(nodesToBrowse, UnsignedInteger.ONE, null,
					null);

//	    		  refTypeId, lookupId,
//	          NodeClass.getSet(NodeClass.ReferenceType.getValue()), BrowseResultMask.ALL, BrowseDirection.Inverse, true);
			// BrowseResult[] result = ServerInstance.getInstance()
			// .getServerInstance().getProfileManager()
			// .browse(nodesToBrowse, UnsignedInteger.ZERO, null);
			if (result != null && result.length > 0 & result[0].getReferences() != null) {
				for (ReferenceDescription refDesc : result[0].getReferences()) {
					NodeId refId = nsTable.toNodeId(refDesc.getNodeId());
					if (Identifiers.HierarchicalReferences.equals(refId)) {
						return true;
					}
					return checkIsReferenceType(nsTable, refId, lookupId);
				}
			} else {
				return false;
			}
		} catch (ServiceResultException e) {
			e.printStackTrace();
		}
		return false;
	}
}
