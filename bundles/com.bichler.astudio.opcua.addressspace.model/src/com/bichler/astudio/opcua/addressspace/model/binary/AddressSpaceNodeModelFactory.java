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
import java.util.logging.Level;
import java.util.logging.Logger;

import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.QualifiedName;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.common.NamespaceTable;
import org.opcfoundation.ua.common.ServerTable;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.IdType;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.core.ReferenceNode;
import org.opcfoundation.ua.encoding.EncoderContext;
import org.opcfoundation.ua.encoding.EncodingException;
import org.opcfoundation.ua.encoding.binary.BinaryEncoder;
import org.opcfoundation.ua.utils.StackUtils;

import com.bichler.astudio.filesystem.IFileSystem;
import com.bichler.astudio.opcua.addressspace.model.Activator;
import com.bichler.astudio.opcua.addressspace.model.nosql.userauthority.NoSqlUtil;
import com.bichler.astudio.utils.internationalization.CustomString;

import opc.sdk.core.application.operation.ICancleOperation;
import opc.sdk.core.node.Node;
import opc.sdk.core.node.UADataTypeNode;
import opc.sdk.core.node.UAMethodNode;
import opc.sdk.core.node.UAObjectNode;
import opc.sdk.core.node.UAObjectTypeNode;
import opc.sdk.core.node.UAReferenceTypeNode;
import opc.sdk.core.node.UAVariableTypeNode;
import opc.sdk.core.node.ViewNode;
import opc.sdk.server.service.node.UAServerVariableNode;

public class AddressSpaceNodeModelFactory {// extends AbstractCompileFactory {
	/**
	 * Constants
	 */
	public static final String METHOD_NAME_INIT = "init";
	public static final String PACKAGENAME = "com.hbsoft.opc.informationmodel";
	public static final String MAINCLASSNAME = "InformationModel";
	public static final String VALUECLASSNAME = "values";
	private static final int SPLIT_FILE = 25;
	/**
	 * max byte limits of java methods
	 */
	private static final String MODELCLASSNAME = "Model";
	private static final String METHOD_NAME_ADD = "add";
	private static final String VARIABLE_LIST_NODES2ADD = "nodes2add";
	private static final String REFERENCE_LIST_REFS2ADD = "references2add";
	private static final String METHOD_NAME_INITVALUES = "readValues";

	private Logger logger = Logger.getLogger(getClass().getName());

	protected static final String EXTENSION_JAR = "jar";
	protected static final String EXTENSION_CLASS = "class";
	protected static final String EXTENSION_JAVA = "java";
	protected static final String EXTENSION_VALUE = "v";
	protected static final int MAX_BYTE_LIMIT_METHOD = 60000;
	protected static final int MAX_BYTE_LIMIT_VALUE = Integer.MAX_VALUE;
	
	public AddressSpaceNodeModelFactory() {
		super();
	}

	class AuthorityHelper {
		boolean hasMethodIncreased = false;
		int byteCount = 0;
	}

	private AuthorityHelper writeAuthContent(BufferedWriter authout, Node node, NamespaceTable serverTable,
			int authMethodCount, int authByteCount) throws IOException {
		AuthorityHelper helper = new AuthorityHelper();
		int auth[][] = NoSqlUtil.readRolesFromNode2(node.getNodeId());
		if (auth != null && auth.length > 0) {
			String sep = "";
			String role = "";
			String au = "";
			for (int[] a : auth) {
				// check if the node has all rights
				if (a[1] == 16383)
					continue;
				role += sep + a[0];
				au += sep + a[1];
				sep = ",";
			}
			String ns = serverTable.getUri(node.getNodeId().getNamespaceIndex());
			String nodeid = node.getNodeId().toString();
			if (nodeid.contains(";")) {
				nodeid = nodeid.split(";")[1];
			}
			// calculate new bytecount
			String declaration = "server.getServerInstance().getUserAuthentifiationManager().addAuthority(\"" + ns
					+ "\", \"" + nodeid + "\", new int[]{" + role + "}, new int[]{" + au + "});";
			int byteCount = declaration.getBytes().length;
			helper.byteCount = byteCount;
			// authority limit exceeds
			if (authByteCount + byteCount >= MAX_BYTE_LIMIT_METHOD) {
				helper.hasMethodIncreased = true;
				// close auth method
				writeClassEnd(authout);
				authout.newLine();
				authout.newLine();
				// open new auth method
				writeAuthStart(authout, authMethodCount);
			}
			authout.write("server.getServerInstance().getUserAuthentifiationManager().addAuthority(\"" + ns + "\", \""
					+ nodeid + "\", new int[]{" + role + "}, new int[]{" + au + "});");
			authout.newLine();
			authout.flush();
		}
		return helper;
	}

	private void writeAuthStart(BufferedWriter out, int methodCount) throws IOException {
		out.write("private void authAdd" + methodCount + "(UAServerApplicationInstance server){");
		out.newLine();
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
			Map<Integer, List<Node>> allNodes, boolean isFullServerTable) throws IOException {

		List<File> files = new ArrayList<>();
		File tmpFolder = new File(file);
		if (!tmpFolder.exists()) {
			tmpFolder.mkdir();
			files.add(tmpFolder);
		}
	
		EncoderContext ctx = new EncoderContext(serverTable, ServerTable.createFromArray(new String[0]),
				StackUtils.getDefaultSerializer(), Integer.MAX_VALUE);
		int classByteCount = 0;
		int classValueCount = 0;
		int classFileCount = -1;
		int classMethodCount = 0;
		int authByteCount = 0;
		int authMethodCount = 0;
		boolean hasFileStarted = false;
		boolean hasMethodStarted = false;
		BufferedWriter out = null;
		BufferedWriter authout = null;
		OutputStream valueOut = null;
		BinaryEncoder encoder = null;
		// close file because method limit exceeds
		try {

			List<Node> nodeList = new ArrayList<Node>();
			for (List<Node> n : allNodes.values()) {
				nodeList.addAll(n);
			}
			Node[] nodes = nodeList.toArray(new Node[0]);
			// iterate to split files
			for (int i = 0; i < nodes.length; i++) {
				if (monitor != null && monitor.isCanceled()) {
					monitor.done();
					return files.toArray(new File[0]);
				}
				NodeChunk nodeChunk = new NodeChunk();
				// open new model file
				if (!hasFileStarted) {
					// creates class
					classFileCount++;
					String newModel = MODELCLASSNAME + classFileCount;
					
					File modelFile = new File(tmpFolder.getPath(), newModel + "." + EXTENSION_JAVA);

					File authFile = new File(tmpFolder.getPath(), newModel + "_auth." + EXTENSION_JAVA);
					String newValue = VALUECLASSNAME + classFileCount;
					File valueFile = new File(tmpFolder.getPath(), newValue + "." + EXTENSION_VALUE);

					if (modelFile.exists()) {
						modelFile.delete();
					}

					if (modelFile.createNewFile()) {
						files.add(modelFile);
					}

					if (authFile.exists()) {
						authFile.delete();
					}
					if (authFile.createNewFile())
						files.add(authFile);

					if (valueFile.exists()) {
						valueFile.delete();
					}

					if (valueFile.createNewFile()) {
						files.add(valueFile);
					}

					// write class
					out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(modelFile)));

					valueOut = new FileOutputStream(valueFile);
					encoder = new BinaryEncoder(valueOut);
					encoder.setEncoderContext(ctx);
					classByteCount = 0;
					writePackageSection(out);

					writeClassStart(out, newModel);
					out.flush();

					// write authority
					authByteCount = 0;
					authMethodCount = 0;

					authout = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(authFile)));
					writePackageSectionAuth(authout);
					writeClassStart(authout, newModel + "_auth");
					writeAuthStart(authout, authMethodCount);
					authMethodCount++;
					hasFileStarted = true;

				}
				// init method header size
				if (!hasMethodStarted) {
					classByteCount += 127;
					String declaration = "server.importModel(nsTable, " + VARIABLE_LIST_NODES2ADD
							+ ".toArray(new Node[0]));\n}\n";
					classByteCount += declaration.getBytes().length;
				}
				// Replace Mandatory and Optional ObjectNode with its default
				// :PP HACK
				if (nodes[i].getNodeId().equals(NodeId.get(IdType.Numeric, 0, new UnsignedInteger(78)))) {
					UAObjectNode mandatory = new UAObjectNode();
					mandatory.setBrowseName(nodes[i].getBrowseName());
					mandatory.setDescription(nodes[i].getDescription());
					mandatory.setDisplayName(nodes[i].getDisplayName());
					mandatory.setNodeClass(nodes[i].getNodeClass());
					mandatory.setNodeId(nodes[i].getNodeId());
					mandatory.setUserWriteMask(nodes[i].getUserWriteMask());
					mandatory.setWriteMask(nodes[i].getWriteMask());
					mandatory.setEventNotifier(((UAObjectNode) nodes[i]).getEventNotifier());
					List<ReferenceNode> references = new ArrayList<>();
					ReferenceNode refNode1 = new ReferenceNode();
					refNode1.setIsInverse(false);
					refNode1.setReferenceTypeId(new NodeId(0, new UnsignedInteger(40)));

					refNode1.setTargetId(new ExpandedNodeId(new UnsignedInteger(0), nsTable.getUri(0),
							new UnsignedInteger(77), nsTable));
					references.add(refNode1);
					ReferenceNode refNode2 = new ReferenceNode();
					refNode2.setIsInverse(false);
					refNode2.setReferenceTypeId(new NodeId(0, new UnsignedInteger(46)));
					refNode2.setTargetId(new ExpandedNodeId(new UnsignedInteger(0), nsTable.getUri(0),
							new UnsignedInteger(112), nsTable));
					references.add(refNode2);
					ReferenceNode refNode3 = new ReferenceNode();
					refNode3.setIsInverse(true);
					refNode3.setReferenceTypeId(new NodeId(0, new UnsignedInteger(50)));
					refNode3.setTargetId(new ExpandedNodeId(new UnsignedInteger(0), nsTable.getUri(0),
							new UnsignedInteger(112), nsTable));
					references.add(refNode3);
					mandatory.setReferences(references.toArray(new ReferenceNode[0]));
					nodes[i] = mandatory;
				}
				// Replace Mandatory and Optional ObjectNode with its default
				// :PP HACK
				else if (nodes[i].getNodeId().equals(NodeId.get(IdType.Numeric, 0, new UnsignedInteger(80)))) {
					UAObjectNode optional = new UAObjectNode();
					optional.setBrowseName(nodes[i].getBrowseName());
					optional.setDescription(nodes[i].getDescription());
					optional.setDisplayName(nodes[i].getDisplayName());
					optional.setNodeClass(nodes[i].getNodeClass());
					optional.setNodeId(nodes[i].getNodeId());
					optional.setUserWriteMask(nodes[i].getUserWriteMask());
					optional.setWriteMask(nodes[i].getWriteMask());
					optional.setEventNotifier(((UAObjectNode) nodes[i]).getEventNotifier());
					List<ReferenceNode> references = new ArrayList<>();
					ReferenceNode refNode1 = new ReferenceNode();
					refNode1.setIsInverse(false);
					refNode1.setReferenceTypeId(new NodeId(0, new UnsignedInteger(40)));
					refNode1.setTargetId(new ExpandedNodeId(new UnsignedInteger(0), nsTable.getUri(0),
							new UnsignedInteger(77), nsTable));
					references.add(refNode1);
					ReferenceNode refNode2 = new ReferenceNode();
					refNode2.setIsInverse(false);
					refNode2.setReferenceTypeId(new NodeId(0, new UnsignedInteger(46)));
					refNode2.setTargetId(new ExpandedNodeId(new UnsignedInteger(0), nsTable.getUri(0),
							new UnsignedInteger(112), nsTable));
					references.add(refNode2);
					ReferenceNode refNode3 = new ReferenceNode();
					refNode3.setIsInverse(true);
					refNode3.setReferenceTypeId(new NodeId(0, new UnsignedInteger(50)));
					refNode3.setTargetId(new ExpandedNodeId(new UnsignedInteger(0), nsTable.getUri(0),
							new UnsignedInteger(112), nsTable));
					references.add(refNode3);
					optional.setReferences(references.toArray(new ReferenceNode[0]));
					nodes[i] = optional;
				}
				do {
					int current = 0;

					current = calculateNodeByteLength(classValueCount, nsTable, serverTable, nodes[i], nodeChunk,
							classByteCount, isFullServerTable);

					// next byte count
					int nextByteCount = classByteCount + current;
					// max byte count reached, close method
					if (hasMethodStarted && nextByteCount >= MAX_BYTE_LIMIT_METHOD) {

						writeAddEnd(out);

						hasMethodStarted = false;
					}
					// if method limit is reached, start new file
					if (!hasMethodStarted && classMethodCount >= SPLIT_FILE && !nodeChunk.chunk) {

						writeInitializeSection(out, nsTable, serverTable, isFullServerTable, classFileCount,
								classMethodCount);
						// end class
						writeClassEnd(out);

						out.flush();
						out.close();

						valueOut.flush();
						valueOut.close();
						writeAuthInitializeSection(authout, authMethodCount);
						writeClassEnd(authout);
						authout.flush();
						authout.close();

						encoder = null;
						classMethodCount = 0;
						classValueCount = 0;
						hasFileStarted = false;
						// continue new file
						classFileCount++;
						String newModel = MODELCLASSNAME + classFileCount;
						
						File modelFile = new File(tmpFolder.getPath(), newModel + "." + EXTENSION_JAVA);

						String newValue = VALUECLASSNAME + classFileCount;
						File valueFile = new File(tmpFolder.getPath(),newValue + "." + EXTENSION_VALUE);
						File authFile = new File(tmpFolder.getPath(), newModel + "_auth." + EXTENSION_JAVA);

						if (modelFile.exists()) {
							modelFile.delete();
						}
						if (modelFile.createNewFile())
							files.add(modelFile);

						if (authFile.exists()) {
							authFile.delete();
						}
						if (authFile.createNewFile())
							files.add(authFile);
						if (valueFile.exists()) {
							valueFile.delete();
						}
						if (valueFile.createNewFile())
							files.add(valueFile);
						out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(modelFile)));

						valueOut = new FileOutputStream(valueFile);
						encoder = new BinaryEncoder(valueOut);
						encoder.setEncoderContext(ctx);
						classByteCount = 0;

						writePackageSection(out);

						writeClassStart(out, newModel);

						out.flush();

						// authority
						authByteCount = 0;
						authMethodCount = 0;
						authout = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(authFile)));

						writePackageSectionAuth(authout);
						writeClassStart(authout, newModel + "_auth");
						writeAuthStart(authout, authMethodCount);

						authMethodCount++;
						hasFileStarted = true;
					}
					// open method
					if (!hasMethodStarted) {
						classByteCount = 0;
						classByteCount += writeAddStart(out, classMethodCount);
						classMethodCount++;
						hasMethodStarted = true;
					}
					// write node
					int increaseValue = 0;
					if (!nodeChunk.chunk) {
						increaseValue = writeNode(out, encoder, nsTable, serverTable, nodes[i], classValueCount,
								nodeChunk, isFullServerTable);
					} else {
						increaseValue = chunkNode(out, encoder, nsTable, serverTable, nodes[i], classValueCount,
								nodeChunk, isFullServerTable);
					}
					AuthorityHelper authHelper = writeAuthContent(authout, nodes[i], serverTable, authMethodCount,
							authByteCount);
					authByteCount += authHelper.byteCount;
					if (authHelper.hasMethodIncreased) {
						authMethodCount++;
						authByteCount = 0;
					}

					classByteCount += current;
					classValueCount += increaseValue;

				} while (nodeChunk.chunk);
			}
			if (hasMethodStarted) {

				writeAddEnd(out);
				out.flush();

			}
			if (hasFileStarted) {

				writeInitializeSection(out, nsTable, serverTable, isFullServerTable, classFileCount, classMethodCount);
				// do not use this function
				writeClassEnd(out);
				out.flush();

				writeAuthInitializeSection(authout, authMethodCount);
				// writeClassEnd(authout);
				writeClassEnd(authout);
				authout.flush();
				valueOut.flush();

			}

			if (out != null) {
				out.close();
			}
			if (authout != null) {
				authout.close();
			}

			String newModel = MAINCLASSNAME + "." + EXTENSION_JAVA;
			File modelFile = new File(tmpFolder.getPath(), newModel);
			if (modelFile.exists()) {
				modelFile.delete();
			}
			modelFile.createNewFile();
			files.add(modelFile);
			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(modelFile)));
			writeInformationModelMain(out, classFileCount);
			out.flush();
		} catch (Exception e) {
			throw new IOException(
					CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "monitor.error.upload"), e);
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					logger.log(Level.SEVERE, e.getMessage());
				}
			}
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
		}
		return files.toArray(new File[0]);
	}

	private void writeAuthInitializeSection(BufferedWriter out, int methodCount) throws IOException {
		writeClassEnd(out);
		out.newLine();
		out.write("public void init(UAServerApplicationInstance server) {");
		out.newLine();
		for (int i = 0; i < methodCount; i++) {
			out.write("authAdd" + i + "(server);");
			out.newLine();
		}
		writeClassEnd(out);
	}

	void writeInitializeSection(BufferedWriter out, NamespaceTable nsTable, NamespaceTable serverTable,
			boolean isFullServerTable, int fileCount, int methodCount) throws IOException {
		// init values
		writeInitialzeValuesSection(out, fileCount);
		// init model
		out.write("protected void " + METHOD_NAME_INIT + "(UAServerApplicationInstance server){");
		out.newLine();
		writeInitialNsTable(out, nsTable, serverTable, isFullServerTable);
		writeInitialValues(out, fileCount);
		writeInitialReferences(out);
		for (int i = 0; i < methodCount; i++) {
			out.write(METHOD_NAME_ADD + i + "(nsTable, server, values, references2add);");
			out.newLine();
		}
		out.write("}");
		out.newLine();
	}

	private void writeInitialReferences(BufferedWriter out) throws IOException {
		out.write("List<ReferenceNode> references2add = new ArrayList<>();");
		out.newLine();
	}

	void writeInitialzeValuesSection(BufferedWriter out, int fileCount) throws IOException {
		EncoderContext.getDefaultInstance();
		ServerTable.createFromArray(new String[0]);
		// init model
		out.write("protected List<DataValue> " + METHOD_NAME_INITVALUES + "(){");
		out.newLine();
		out.write("List<DataValue> values = new ArrayList<>();");
		out.newLine();
		out.write("InputStream is = this.getClass().getClassLoader().getResourceAsStream(\"" + VALUECLASSNAME
				+ fileCount + "." + EXTENSION_VALUE + "\");");
		out.newLine();
		out.write("BinaryDecoder decoder = new BinaryDecoder(is, " + MAX_BYTE_LIMIT_VALUE + ");");
		out.newLine();
		out.write("EncoderContext ctx = EncoderContext.getDefaultInstance();");
		out.newLine();
		out.write("decoder.setEncoderContext(ctx);");
		out.newLine();
		out.write("long position = 0;");
		out.newLine();
		out.write("long lastPosition = 0;");
		out.newLine();
		out.write("long lastSize = 0;");
		out.newLine();
		out.write("try{");
		out.newLine();
		out.newLine();
		out.write(" while (true) {");
		out.newLine();
		out.write("long size = values.size();");
		out.newLine();
		out.write("try {");
		out.newLine();
		out.write("DataValue v = decoder.getDataValue(null);");
		out.newLine();
		out.write("position = decoder.getReadable().position();");
		out.newLine();
		out.write("values.add(v);");
		out.newLine();
		out.write("} catch (DecodingException e) {");
		out.newLine();
		out.write("if (lastSize == size) {");
		out.newLine();
		out.write("try {");
		out.newLine();
		out.write("is.close();");
		out.newLine();
		out.write("} catch (IOException e2) {}");
		out.newLine();
		out.write("break;");
		out.newLine();
		out.write("}");
		out.newLine();
		out.write("try {");
		out.newLine();
		out.write("is.close();");
		out.newLine();
		out.write("} catch (IOException e2) {}");
		out.newLine();
		out.write("is = this.getClass().getClassLoader().getResourceAsStream(\"" + VALUECLASSNAME + fileCount + "."
				+ EXTENSION_VALUE + "\");");
		out.newLine();
		out.write("lastPosition += position;");
		out.newLine();
		out.write("is.skip(lastPosition);");
		out.newLine();
		out.write("decoder = new BinaryDecoder(is, " + MAX_BYTE_LIMIT_VALUE + ");");
		out.newLine();
		out.write("decoder.setEncoderContext(ctx);");
		out.newLine();
		out.write("lastSize = values.size();");
		out.newLine();
		out.write("}");
		out.newLine();
		out.write("}");
		out.write("}catch(IOException e3){e3.printStackTrace();}");
		out.newLine();
		out.newLine();
		out.write("return values;");
		out.newLine();
		out.write("}");
		out.newLine();
		out.write("");
		out.newLine();
	}

	/**
	 * Write initialize opc ua values.
	 * 
	 * @param out
	 * @param fileCount
	 * @throws IOException
	 */
	private void writeInitialValues(BufferedWriter out, int fileCount) throws IOException {
		out.write("List<DataValue> values = " + METHOD_NAME_INITVALUES + "();");
		out.newLine();
	}

	/**
	 * Initializes the namespace table for the model to import.
	 * 
	 * @param out
	 * @param nsTable
	 * @throws IOException
	 */
	void writeInitialNsTable(BufferedWriter out, NamespaceTable nsTable, NamespaceTable serverTable,
			boolean isFullServerTable) throws IOException {
		NamespaceTable nsTable2use = nsTable;
		if (isFullServerTable) {
			nsTable2use = serverTable;
		}

		String[] nsUris = nsTable2use.toArray();
		out.write("NamespaceTable nsTable = new NamespaceTable();");
		out.newLine();
		for (String nsUri : nsUris) {
			int index = nsTable2use.getIndex(nsUri);
			out.write("nsTable.add(" + index + ", \"" + nsUri + "\".replace(\"hostname\", InetAddress.getLocalHost().getHostName()));");
			out.newLine();
		}
		out.newLine();
	}

	/**
	 * Ends an add method.
	 * 
	 * @param out
	 * @throws IOException
	 */
	void writeAddEnd(BufferedWriter out) throws IOException {
		out.write("server.importModel(nsTable, " + VARIABLE_LIST_NODES2ADD + ".toArray(new Node[0]));");
		out.newLine();
		out.write("}");
		out.newLine();
	}

	/**
	 * Writes an add method to insert opc ua nodes.
	 * 
	 * @param out
	 * @param count
	 * @return
	 * @throws IOException
	 */
	int writeAddStart(BufferedWriter out, int count) throws IOException {
		int byteCount = 0;
		String declaration = "private void add" + count
				+ "(NamespaceTable nsTable, UAServerApplicationInstance server, List<DataValue> values, List<ReferenceNode> references2add){\n";
		out.write("private void add" + count
				+ "(NamespaceTable nsTable, UAServerApplicationInstance server, List<DataValue> values,  List<ReferenceNode> references2add){");
		out.newLine();
		byteCount += declaration.getBytes().length;
		declaration = "List<Node> " + VARIABLE_LIST_NODES2ADD + " = new ArrayList<>();\n";
		out.write("List<Node> " + VARIABLE_LIST_NODES2ADD + " = new ArrayList<>();");
		out.newLine();
		byteCount += declaration.getBytes().length;
		return byteCount;
	}

	/**
	 * Writes end of an class
	 * 
	 * @param out
	 * @throws IOException
	 */
	void writeClassEnd(BufferedWriter out) throws IOException {
		out.write("}");
	}

	void writeClassStart(BufferedWriter out, String className) throws IOException {
		out.write("public class " + className + " {");
		out.newLine();
	}

	void writeInformationModelMain(BufferedWriter out, int fileCount) throws IOException {
		out.write("package " + PACKAGENAME + ";");
		out.newLine();
		out.write("import opc.sdk.server.core.UAServerApplicationInstance;");
		out.newLine();
		out.write("public class " + MAINCLASSNAME + " {");
		out.newLine();
		out.write("public void " + METHOD_NAME_INIT + "(UAServerApplicationInstance server){");
		out.newLine();
		for (int i = 0; i <= fileCount; i++) {
			out.write("new " + MODELCLASSNAME + i + "()." + METHOD_NAME_INIT + "(server);");
			out.newLine();
			out.write("new " + MODELCLASSNAME + i + "_auth()." + METHOD_NAME_INIT + "(server);");
			out.newLine();
		}
		out.write("}");
		out.newLine();
		out.write("}");
		out.flush();
	}

	private int chunkNode(BufferedWriter out, BinaryEncoder encoder, NamespaceTable nsTable, NamespaceTable serverTable,
			Node node, int valueCount, NodeChunk chunkFactory, boolean isFullServerTable) throws IOException {
		int increaseValue = 0;
		if (node == null) {
			return increaseValue;
		}
		out.write("{");
		out.newLine();
		// references
		writeReferenceNodes(out, nsTable, serverTable, node.getReferences(), chunkFactory, isFullServerTable);
		out.write("}");
		out.newLine();
		return increaseValue;
	}

	int writeNode(BufferedWriter out, BinaryEncoder encoder, NamespaceTable nsTable, NamespaceTable serverTable,
			Node node, int valueCount, NodeChunk chunkFactory, boolean isFullServerTable) throws IOException {
		int increaseValue = 0;
		if (node == null) {
			return increaseValue;
		}
		out.write("{");
		out.newLine();
		// references

		writeReferenceNodes(out, nsTable, serverTable, node.getReferences(), chunkFactory, isFullServerTable);
		// node
		NodeClass nodeClass = node.getNodeClass();
		boolean isWrite = false;
		switch (nodeClass) {
		case Object:
			writeNodeObject(out, nsTable, serverTable, isFullServerTable, (UAObjectNode) node);
			break;
		case ObjectType:
			writeNodeObjectType(out, nsTable, serverTable, isFullServerTable, (UAObjectTypeNode) node);
			break;
		case DataType:
			writeNodeDataType(out, nsTable, serverTable, isFullServerTable, (UADataTypeNode) node);
			break;
		case Method:
			writeNodeMethod(out, nsTable, serverTable, isFullServerTable, (UAMethodNode) node);
			break;
		case ReferenceType:
			writeNodeReferenceType(out, nsTable, serverTable, isFullServerTable, (UAReferenceTypeNode) node);
			break;
		case Variable:
			DataValue dv1 = new DataValue();
			((UAServerVariableNode) node).readAttributeValue(org.opcfoundation.ua.core.Attributes.Value, dv1, null);
			isWrite = writeValue(encoder, node, dv1);
			writeNodeVariable(out, nsTable, serverTable, isFullServerTable, (UAServerVariableNode) node, valueCount);
			if (isWrite) {
				increaseValue++;
			}
			break;
		case VariableType:
			DataValue dv2 = new DataValue();
			((UAVariableTypeNode) node).readAttributeValue(org.opcfoundation.ua.core.Attributes.Value, dv2, null);
			isWrite = writeValue(encoder, node, dv2);
			writeNodeVariableType(out, nsTable, serverTable, isFullServerTable, (UAVariableTypeNode) node, valueCount);
			if (isWrite) {
				increaseValue++;
			}
			break;
		case View:
			writeNodeView(out, nsTable, serverTable, isFullServerTable, (ViewNode) node);
			break;
		default:
			break;
		}
		out.write("references2add.clear();");
		out.newLine();
		out.write("}");
		out.newLine();
		chunkFactory.chunk = false;
		chunkFactory.beginChunkReferenceIndex = 0;
		chunkFactory.nextChunkReferenceIndex = 0;
		return increaseValue;
	}

	private boolean writeValue(BinaryEncoder encoder, Node node, DataValue value) {
		// skip server array
		if (Identifiers.Server_NamespaceArray.equals(node.getNodeId())) {
			return false;
		}
		if (!value.isNull()) // && value.getValue().isArray() && value.getValue().getCompositeClass() ==
								// EnumValueType.class)
		{
			DataValue value2encode = new DataValue(value.getValue(), value.getStatusCode());
			try {
				encoder.putDataValue(null, value2encode);
				encoder.getOutput().flush();
				return true;
			} catch (EncodingException | IOException e) {
				logger.log(Level.SEVERE,
						CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "monitor.error.upload"), e);
			}
		}
		return false;
	}

	void writePackageSectionAuth(BufferedWriter out) throws IOException {
		out.write("package " + PACKAGENAME + ";");
		out.newLine();
		// out.write("import java.util.List;");
		// out.newLine();
		// out.write("import java.io.InputStream;");
		// out.newLine();
		// out.write("import org.opcfoundation.ua.encoding.EncoderContext;");
		// out.newLine();
		// out.write("import org.opcfoundation.ua.encoding.binary.BinaryDecoder;");
		// out.newLine();
		// out.write("import org.opcfoundation.ua.common.ServerTable;");
		// out.newLine();
		// out.write("import org.opcfoundation.ua.utils.StackUtils;");
		// out.newLine();
		// out.write("import org.opcfoundation.ua.encoding.DecodingException;");
		// out.newLine();
		// out.write("import org.opcfoundation.ua.builtintypes.DataValue;");
		// out.newLine();
		// out.write("import java.util.ArrayList;");
		// out.newLine();
		// out.write("import java.io.IOException;");
		// out.newLine();
		// out.write("import org.opcfoundation.ua.builtintypes.QualifiedName;");
		// out.newLine();
		// out.write("import org.opcfoundation.ua.builtintypes.LocalizedText;\n");
		// out.newLine();
		// out.write("import org.opcfoundation.ua.builtintypes.Variant;");
		// out.newLine();
		// out.write("import org.opcfoundation.ua.core.NodeClass;");
		// out.newLine();
		// out.write("import org.opcfoundation.ua.core.ReferenceNode;");
		// out.newLine();
		// out.write("import org.opcfoundation.ua.builtintypes.ExpandedNodeId;");
		// out.newLine();
		// out.write("import org.opcfoundation.ua.builtintypes.UnsignedInteger;");
		// out.newLine();
		// out.write("import org.opcfoundation.ua.builtintypes.UnsignedByte;");
		// out.newLine();
		// out.write("import org.opcfoundation.ua.common.NamespaceTable;");
		// out.newLine();
		out.write("import opc.sdk.server.core.UAServerApplicationInstance;");
		out.newLine();
		// out.write("import opc.sdk.core.node.NodeIdUtil;");
		// out.newLine();
		// out.write("import opc.sdk.core.node.Node;");
		// out.newLine();
		// out.write("import opc.sdk.core.node.UADataTypeNode;");
		// out.newLine();
		// out.write("import opc.sdk.core.node.UAMethodNode;");
		// out.newLine();
		// out.write("import opc.sdk.core.node.UAObjectNode;");
		// out.newLine();
		// out.write("import opc.sdk.core.node.UAObjectTypeNode;");
		// out.newLine();
		// out.write("import opc.sdk.core.node.UAReferenceTypeNode;");
		// out.newLine();
		// out.write("import opc.sdk.core.node.UAVariableTypeNode;");
		// out.newLine();
		// out.write("import opc.sdk.server.service.node.UAServerVariableNode;");
		// out.newLine();
		// out.write("import opc.sdk.core.node.ViewNode;");
		// out.newLine();
		out.flush();
	}

	/**
	 * function writes out all necessary includes
	 * 
	 * @param out
	 * @throws IOException
	 */
	void writeIncludeSection(BufferedWriter out) throws IOException {
		out.write("/*\n");
		out.write("*\n");
		out.write("* model.c\n");
		out.write("*\n");
		out.write("*Created on: 20.11.2018\n");
		out.write("*Author: hannes\n");
		out.write("*/\n\n");
		out.write("#include open62541.h\n");
	}

	void writePackageSection(BufferedWriter out) throws IOException {
		out.write("package " + PACKAGENAME + ";");
		out.newLine();
		out.write("import java.net.InetAddress;");
		out.newLine();
		out.write("import java.util.List;");
		out.newLine();
		out.write("import java.io.InputStream;");
		out.newLine();
		out.write("import org.opcfoundation.ua.encoding.EncoderContext;");
		out.newLine();
		out.write("import org.opcfoundation.ua.encoding.binary.BinaryDecoder;");
		out.newLine();
		// out.write("import org.opcfoundation.ua.common.ServerTable;");
		// out.newLine();
		// out.write("import org.opcfoundation.ua.utils.StackUtils;");
		// out.newLine();
		out.write("import org.opcfoundation.ua.encoding.DecodingException;");
		out.newLine();
		out.write("import org.opcfoundation.ua.builtintypes.DataValue;");
		out.newLine();
		out.write("import java.util.ArrayList;");
		out.newLine();
		out.write("import java.io.IOException;");
		out.newLine();
		out.write("import org.opcfoundation.ua.builtintypes.QualifiedName;");
		out.newLine();
		out.write("import org.opcfoundation.ua.builtintypes.LocalizedText;\n");
		out.newLine();
		out.write("import org.opcfoundation.ua.builtintypes.Variant;");
		out.newLine();
		out.write("import org.opcfoundation.ua.core.NodeClass;");
		out.newLine();
		out.write("import org.opcfoundation.ua.core.ReferenceNode;");
		out.newLine();
		out.write("import org.opcfoundation.ua.builtintypes.ExpandedNodeId;");
		out.newLine();
		out.write("import org.opcfoundation.ua.builtintypes.UnsignedInteger;");
		out.newLine();
		out.write("import org.opcfoundation.ua.builtintypes.UnsignedByte;");
		out.newLine();
		out.write("import org.opcfoundation.ua.common.NamespaceTable;");
		out.newLine();
		out.write("import opc.sdk.server.core.UAServerApplicationInstance;");
		out.newLine();
		out.write("import opc.sdk.core.node.NodeIdUtil;");
		out.newLine();
		out.write("import opc.sdk.core.node.Node;");
		out.newLine();
		out.write("import opc.sdk.core.node.UADataTypeNode;");
		out.newLine();
		out.write("import opc.sdk.core.node.UAMethodNode;");
		out.newLine();
		out.write("import opc.sdk.core.node.UAObjectNode;");
		out.newLine();
		out.write("import opc.sdk.core.node.UAObjectTypeNode;");
		out.newLine();
		out.write("import opc.sdk.core.node.UAReferenceTypeNode;");
		out.newLine();
		out.write("import opc.sdk.core.node.UAVariableTypeNode;");
		out.newLine();
		out.write("import opc.sdk.server.service.node.UAServerVariableNode;");
		out.newLine();
		out.write("import opc.sdk.server.service.node.UAServerObjectNode;");
		out.newLine();
		// out.write("import opc.sdk.core.node.ViewNode;");
		// out.newLine();
		out.flush();
	}

	private int calculateNodeByteLength(int valueCount, NamespaceTable nsTable, NamespaceTable serverTable, Node node,
			NodeChunk chunk, int currentbyte, boolean isFullServerTable) {
		if (node == null) {
			return 0;
		}
		int chunkBytecount = currentbyte;
		int byteCount = 0;
		String declaration = "{\n";
		byteCount += declaration.getBytes().length;
		// calculate node
		NodeClass nodeClass = node.getNodeClass();
		switch (nodeClass) {
		case Object:
			byteCount += calculateNodeObject(nsTable, serverTable, isFullServerTable, (UAObjectNode) node);
			break;
		case ObjectType:
			byteCount += calculateNodeObjectType(nsTable, serverTable, isFullServerTable, (UAObjectTypeNode) node);
			break;
		case DataType:
			byteCount += calculateNodeDataType(nsTable, serverTable, isFullServerTable, (UADataTypeNode) node);
			break;
		case Method:
			byteCount += calculateNodeMethod(nsTable, serverTable, isFullServerTable, (UAMethodNode) node);
			break;
		case ReferenceType:
			byteCount += calculateNodeReferenceType(nsTable, serverTable, isFullServerTable,
					(UAReferenceTypeNode) node);
			break;
		case Variable:
			byteCount += calculateNodeVariable(valueCount, nsTable, serverTable, isFullServerTable,
					(UAServerVariableNode) node);
			break;
		case VariableType:
			byteCount += calculateNodeVariableType(valueCount, nsTable, serverTable, isFullServerTable,
					(UAVariableTypeNode) node);
			break;
		case View:
			byteCount += calculateNodeView(nsTable, serverTable, isFullServerTable, (ViewNode) node);
			break;
		default:
			break;
		}
		// calculate references
		chunkBytecount += byteCount;
		byteCount += calculateReferenceNode(nsTable, serverTable, node.getReferences(), chunk, chunkBytecount,
				isFullServerTable);
		declaration = "}\n";
		byteCount += declaration.getBytes().length;
		return byteCount;
	}

	private int calculateNodeView(NamespaceTable nsTable, NamespaceTable serverTable, boolean isFullServerTable,
			ViewNode node) {
		int byteCount = 0;
		String declaration = VARIABLE_LIST_NODES2ADD + ".add(new ViewNode("
				+ helpNodeId(nsTable, serverTable, isFullServerTable, node.getNodeId()) + "," + " NodeClass.valueOf("
				+ node.getNodeClass().getValue() + ") ," + helpQualifiedName(node.getBrowseName()) + " ,"
				+ helpLocalizedText(node.getDisplayName()) + " ," + helpLocalizedText(node.getDescription()) + ","
				+ " new UnsignedInteger(" + node.getWriteMask() + ") ," + " new UnsignedInteger("
				+ node.getUserWriteMask() + ") ," + "references2add.toArray(new ReferenceNode[0]) ,"
				+ node.getContainsNoLoops() + ", new UnsignedByte(" + node.getEventNotifier() + ")));\n";
		byteCount = declaration.getBytes().length;
		return byteCount;
	}

	private int calculateNodeVariableType(int valueCount, NamespaceTable nsTable, NamespaceTable serverTable,
			boolean isFullServerTable, UAVariableTypeNode node) {
		int byteCount = 0;
		String declaration = VARIABLE_LIST_NODES2ADD + ".add(new UAVariableTypeNode("
				+ helpNodeId(nsTable, serverTable, isFullServerTable, node.getNodeId()) + "," + " NodeClass.valueOf("
				+ node.getNodeClass().getValue() + ") ," + helpQualifiedName(node.getBrowseName()) + " ,"
				+ helpLocalizedText(node.getDisplayName()) + " ," + helpLocalizedText(node.getDescription()) + ","
				+ " new UnsignedInteger(" + node.getWriteMask() + ") ," + " new UnsignedInteger("
				+ node.getUserWriteMask() + ") ," + "references2add.toArray(new ReferenceNode[0]) ,"
				+ helpValue(valueCount, node.getValue()) + ","
				+ helpNodeId(nsTable, serverTable, isFullServerTable, node.getDataType()) + "," + node.getValueRank()
				+ "," + helpArrayDimension(node.getValueRank(), node.getArrayDimensions()) + "," + node.getIsAbstract() + "));\n";
		byteCount = declaration.getBytes().length;
		return byteCount;
	}

	private int calculateNodeVariable(int valueCount, NamespaceTable nsTable, NamespaceTable serverTable,
			boolean isFullServerTable, UAServerVariableNode node) {
		int byteCount = 0;
		// variable node
		String declaration = VARIABLE_LIST_NODES2ADD + ".add(new UAServerVariableNode("
				+ helpNodeId(nsTable, serverTable, isFullServerTable, node.getNodeId()) + "," + " NodeClass.valueOf("
				+ node.getNodeClass().getValue() + ") ," + helpQualifiedName(node.getBrowseName()) + " ,"
				+ helpLocalizedText(node.getDisplayName()) + " ," + helpLocalizedText(node.getDescription()) + ","
				+ " new UnsignedInteger(" + node.getWriteMask() + ") ," + " new UnsignedInteger("
				+ node.getUserWriteMask() + ") ," + "references2add.toArray(new ReferenceNode[0]) ,"
				+ helpValue(valueCount, node.getValue()) + ","
				+ helpNodeId(nsTable, serverTable, isFullServerTable, node.getDataType()) + "," + node.getValueRank()
				+ "," + helpArrayDimension(node.getValueRank(), node.getArrayDimensions()) + "," + "new UnsignedByte("
				+ node.getAccessLevel() + ")," + "new UnsignedByte(" + node.getUserAccessLevel() + "),"
				+ node.getMinimumSamplingInterval() + "," + node.getHistorizing() + "));\n";
		byteCount = declaration.getBytes().length;
		return byteCount;
	}

	private int calculateNodeReferenceType(NamespaceTable nsTable, NamespaceTable serverTable,
			boolean isFullServerTable, UAReferenceTypeNode node) {
		// reference type node
		int byteCount = 0;
		String declaration = VARIABLE_LIST_NODES2ADD + ".add(new UAReferenceTypeNode("
				+ helpNodeId(nsTable, serverTable, isFullServerTable, node.getNodeId()) + "," + " NodeClass.valueOf("
				+ node.getNodeClass().getValue() + ") ," + helpQualifiedName(node.getBrowseName()) + " ,"
				+ helpLocalizedText(node.getDisplayName()) + " ," + helpLocalizedText(node.getDescription()) + ","
				+ " new UnsignedInteger(" + node.getWriteMask() + ") ," + " new UnsignedInteger("
				+ node.getUserWriteMask() + ") ," + "references2add.toArray(new ReferenceNode[0]) ,"
				+ node.getIsAbstract() + "," + node.getSymmetric() + "," + helpLocalizedText(node.getInverseName())
				+ "));\n";
		byteCount = declaration.getBytes().length;
		return byteCount;
	}

	private int calculateNodeMethod(NamespaceTable nsTable, NamespaceTable serverTable, boolean isFullServerTable,
			UAMethodNode node) {
		int byteCount = 0;
		String declaration = VARIABLE_LIST_NODES2ADD + ".add(new UAMethodNode("
				+ helpNodeId(nsTable, serverTable, isFullServerTable, node.getNodeId()) + "," + " NodeClass.valueOf("
				+ node.getNodeClass().getValue() + ") ," + helpQualifiedName(node.getBrowseName()) + " ,"
				+ helpLocalizedText(node.getDisplayName()) + " ," + helpLocalizedText(node.getDescription()) + ","
				+ " new UnsignedInteger(" + node.getWriteMask() + ") ," + " new UnsignedInteger("
				+ node.getUserWriteMask() + ") ," + "references2add.toArray(new ReferenceNode[0]) ,"
				+ node.getExecutable() + "," + node.getUserExecutable() + "));\n";
		byteCount = declaration.getBytes().length;
		return byteCount;
	}

	private int calculateNodeDataType(NamespaceTable nsTable, NamespaceTable serverTable, boolean isFullServerTable,
			UADataTypeNode node) {
		int byteCount = 0;
		String declaration = VARIABLE_LIST_NODES2ADD + ".add(new UADataTypeNode("
				+ helpNodeId(nsTable, serverTable, isFullServerTable, node.getNodeId()) + "," + " NodeClass.valueOf("
				+ node.getNodeClass().getValue() + ") ," + helpQualifiedName(node.getBrowseName()) + " ,"
				+ helpLocalizedText(node.getDisplayName()) + " ," + helpLocalizedText(node.getDescription()) + ","
				+ " new UnsignedInteger(" + node.getWriteMask() + ") ," + " new UnsignedInteger("
				+ node.getUserWriteMask() + ") ," + "references2add.toArray(new ReferenceNode[0]) ,"
				+ node.getIsAbstract() + "));\n";
		byteCount = declaration.getBytes().length;
		return byteCount;
	}

	private int calculateNodeObjectType(NamespaceTable nsTable, NamespaceTable serverTable, boolean isFullServerTable,
			UAObjectTypeNode node) {
		// object type node
		int byteCount = 0;
		String declaration = VARIABLE_LIST_NODES2ADD + ".add(new UAObjectTypeNode("
				+ helpNodeId(nsTable, serverTable, isFullServerTable, node.getNodeId()) + "," + " NodeClass.valueOf("
				+ node.getNodeClass().getValue() + ") ," + helpQualifiedName(node.getBrowseName()) + " ,"
				+ helpLocalizedText(node.getDisplayName()) + " ," + helpLocalizedText(node.getDescription()) + ","
				+ " new UnsignedInteger(" + node.getWriteMask() + ") ," + " new UnsignedInteger("
				+ node.getUserWriteMask() + ") ," + "references2add.toArray(new ReferenceNode[0]) ,"
				+ node.getIsAbstract() + "));\n";
		byteCount = declaration.getBytes().length;
		return byteCount;
	}

	private int calculateNodeObject(NamespaceTable nsTable, NamespaceTable serverTable, boolean isFullServerTable,
			UAObjectNode node) {
		// object node
		int byteCount = 0;
		String declaration = VARIABLE_LIST_NODES2ADD + ".add(new UAObjectNode("
				+ helpNodeId(nsTable, serverTable, isFullServerTable, node.getNodeId()) + "," + " NodeClass.valueOf("
				+ node.getNodeClass().getValue() + ") ," + helpQualifiedName(node.getBrowseName()) + " ,"
				+ helpLocalizedText(node.getDisplayName()) + " ," + helpLocalizedText(node.getDescription()) + ","
				+ " new UnsignedInteger(" + node.getWriteMask() + ") ," + " new UnsignedInteger("
				+ node.getUserWriteMask() + ") ," + "references2add.toArray(new ReferenceNode[0]) ,"
				+ "new UnsignedByte(" + node.getEventNotifier() + ")" + "));\n";
		byteCount += declaration.getBytes().length;
		return byteCount;
	}

	private int calculateReferenceNode(NamespaceTable nsTable, NamespaceTable serverTable, ReferenceNode[] references,
			NodeChunk chunk, int bytecount, boolean isFullServerTable) {
		if (references == null) {
			return 0;
		}
		int byteCount = 0;
		String declaration = "";

		// TODO: CHUNK REFERENCES
		for (int i = chunk.beginChunkReferenceIndex; i < references.length; i++) {
			ReferenceNode refNode = references[i];
			// something error
			if (NodeId.isNull(refNode.getReferenceTypeId())) {
				continue;
			}
			// skip target reference nodes which are not allowed in the
			// namespace table
			boolean allowedReference = checkReferenceNodeAllowed(nsTable, serverTable, isFullServerTable, refNode);
			if (!allowedReference) {
				continue;
			}
			declaration = REFERENCE_LIST_REFS2ADD + ".add(new ReferenceNode("
					+ helpNodeId(nsTable, serverTable, isFullServerTable, refNode.getReferenceTypeId()) + ", "
					+ refNode.getIsInverse().toString() + ","
					+ helpExpandedNodeId(nsTable, serverTable, isFullServerTable, refNode.getTargetId()) + "));\n";
			byteCount += declaration.getBytes().length;
			if (byteCount < MAX_BYTE_LIMIT_METHOD) {
				chunk.nextChunkReferenceIndex = i;
				chunk.chunk = false;
			}
			// chunk references
			else {
				chunk.chunk = true;
				break;
			}
		}
		return byteCount;
	}

	private boolean checkReferenceNodeAllowed(NamespaceTable nsTable, NamespaceTable serverTable,
			boolean isFullServerTable, ReferenceNode node) {
		boolean allowed = false;
		if (ExpandedNodeId.isNull(node.getTargetId())) {
			return allowed;
		}

		try {
			NodeId targetNodeId = serverTable.toNodeId(node.getTargetId());
			String uri = serverTable.getUri(targetNodeId.getNamespaceIndex());
			int index = nsTable.getIndex(uri);
			if (index > -1) {
				allowed = true;
			}
		} catch (ServiceResultException e) {
			logger.log(Level.SEVERE, e.getMessage());
		}
		return allowed;
	}

	private String helpArrayDimension(int valueRank, UnsignedInteger[] arrayDimensions) {
		int length = -1;
		if (valueRank != -1) { // scalar -> always ignore arrayDimensions
			if (arrayDimensions != null) {
				length = arrayDimensions.length;
			}
		}
		String help = "";
		switch (length) {
		case -1:
			help = "null";
			break;
		case 0:
			help = "new UnsignedInteger[" + length + "]";
			break;
		default:
			help = "new UnsignedInteger[]{";
			String comma = "";
			for (int i = 0; i < length; i++) {
				help += comma + "new UnsignedInteger(" + arrayDimensions[i] + ")";
				comma = ",";
			}
			help += "}";
			break;
		}
		return help;
	}

	private String helpExpandedNodeId(NamespaceTable nsTable, NamespaceTable serverTable, boolean isFullServerTable,
			ExpandedNodeId nodeId) {
		NamespaceTable idTable = nsTable;
		if (isFullServerTable) {
			idTable = serverTable;
		}

		try {
			NodeId expId = serverTable.toNodeId(nodeId);
			String uri = serverTable.getUri(expId.getNamespaceIndex());
			int index = idTable.getIndex(uri);
			// add " quote to string
			Object nIdValue = nodeId.getValue();
			if (nIdValue instanceof String) {
				nIdValue = "\"" + nIdValue + "\"";
			}
			String help = "new ExpandedNodeId(new UnsignedInteger(" + nodeId.getServerIndex() + "),\"" + uri + "\".replace(\"hostname\", InetAddress.getLocalHost().getHostName()),"
					+ nIdValue + ", nsTable)";
			return help;
		} catch (ServiceResultException e) {
			logger.log(Level.SEVERE, "Error, upload Files to Combox!", e);
		}
		return "ExpandedNodeId.NULL";
	}

	private String helpLocalizedText(LocalizedText text) {
		String name = text.getText() == null ? "" : text.getText().replace("\n", " ");
		String localeId = text.getLocaleId() == null ? "" : text.getLocaleId();
		String help = " new LocalizedText(\"" + name + "\",\"" + localeId + "\")";
		return help;
	}

	private String helpNodeId(NamespaceTable nsTable, NamespaceTable serverTable, boolean isFullServerTable,
			NodeId nodeId) {
		NamespaceTable idTable = nsTable;
		if (isFullServerTable) {
			idTable = serverTable;
		}

		String uri = serverTable.getUri(nodeId.getNamespaceIndex());
		int index = idTable.getIndex(uri);
		// add " quote to string
		Object nIdValue = nodeId.getValue();
		if (nIdValue instanceof String) {
			nIdValue = "\"" + nIdValue + "\"";
		}
		String help = "NodeIdUtil.createNodeId(" + index + "," + nIdValue + ") ";
		return help;
	}

	private String helpQualifiedName(QualifiedName text) {
		String help = null;
		if (text == null) {
			help = "new QualifiedName(\"\")";
		} else {
			help = "new QualifiedName(" + text.getNamespaceIndex() + ",\"" + text.getName().replace("\n", " ") + "\")";
		}
		return help;
	}

	private String helpValue(int valueCount, Variant value) {
		String help = "";
		if (value == null || value.isEmpty()) {
			help = "Variant.NULL";
		} else {
			help = "values.get(" + valueCount + ")";
		}
		return help;
	}

	private void writeNodeDataType(BufferedWriter out, NamespaceTable nsTable, NamespaceTable serverTable,
			boolean isFullServerTable, UADataTypeNode node) throws IOException {
		// new UADataTypeNode(NodeId, NodeClass, BrowseName, DisplayName,
		// Description, WriteMask, UserWriteMask, References, IsAbstract)
		out.write(VARIABLE_LIST_NODES2ADD + ".add(new UADataTypeNode("
				+ helpNodeId(nsTable, serverTable, isFullServerTable, node.getNodeId()) + "," + " NodeClass.valueOf("
				+ node.getNodeClass().getValue() + ") ," + helpQualifiedName(node.getBrowseName()) + " ,"
				+ helpLocalizedText(node.getDisplayName()) + " ," + helpLocalizedText(node.getDescription()) + ","
				+ " new UnsignedInteger(" + node.getWriteMask() + ") ," + " new UnsignedInteger("
				+ node.getUserWriteMask() + ") ," + "references2add.toArray(new ReferenceNode[0]) ,"
				+ node.getIsAbstract() + "));".replace("\r", ""));
		out.newLine();
	}

	private void writeNodeMethod(BufferedWriter out, NamespaceTable nsTable, NamespaceTable serverTable,
			boolean isFullServerTable, UAMethodNode node) throws IOException {
		out.write(VARIABLE_LIST_NODES2ADD + ".add(new UAMethodNode("
				+ helpNodeId(nsTable, serverTable, isFullServerTable, node.getNodeId()) + "," + " NodeClass.valueOf("
				+ node.getNodeClass().getValue() + ") ," + helpQualifiedName(node.getBrowseName()) + " ,"
				+ helpLocalizedText(node.getDisplayName()) + " ," + helpLocalizedText(node.getDescription()) + ","
				+ " new UnsignedInteger(" + node.getWriteMask() + ") ," + " new UnsignedInteger("
				+ node.getUserWriteMask() + ") ," + "references2add.toArray(new ReferenceNode[0]) ,"
				+ node.getExecutable() + "," + node.getUserExecutable() + "));".replace("\r", ""));
		out.newLine();
	}

	private void writeNodeObject(BufferedWriter out, NamespaceTable nsTable, NamespaceTable serverTable,
			boolean isFullServerTable, UAObjectNode node) throws IOException {
		// object node
		out.write(VARIABLE_LIST_NODES2ADD + ".add(new UAServerObjectNode("
				+ helpNodeId(nsTable, serverTable, isFullServerTable, node.getNodeId()) + "," + " NodeClass.valueOf("
				+ node.getNodeClass().getValue() + ") ," + helpQualifiedName(node.getBrowseName()) + " ,"
				+ helpLocalizedText(node.getDisplayName()) + " ," + helpLocalizedText(node.getDescription()) + ","
				+ " new UnsignedInteger(" + node.getWriteMask() + ") ," + " new UnsignedInteger("
				+ node.getUserWriteMask() + ") ," + "references2add.toArray(new ReferenceNode[0]) ,"
				+ "new UnsignedByte(" + node.getEventNotifier() + ")" + "));".replace("\r", ""));
		out.newLine();
	}

	private void writeNodeObjectType(BufferedWriter out, NamespaceTable nsTable, NamespaceTable serverTable,
			boolean isFullServerTable, UAObjectTypeNode node) throws IOException {
		// object type node
		out.write(VARIABLE_LIST_NODES2ADD + ".add(new UAObjectTypeNode("
				+ helpNodeId(nsTable, serverTable, isFullServerTable, node.getNodeId()) + "," + " NodeClass.valueOf("
				+ node.getNodeClass().getValue() + ") ," + helpQualifiedName(node.getBrowseName()) + " ,"
				+ helpLocalizedText(node.getDisplayName()) + " ," + helpLocalizedText(node.getDescription()) + ","
				+ " new UnsignedInteger(" + node.getWriteMask() + ") ," + " new UnsignedInteger("
				+ node.getUserWriteMask() + ") ," + "references2add.toArray(new ReferenceNode[0]) ,"
				+ node.getIsAbstract() + "));".replace("\r", ""));
		out.newLine();
	}

	private void writeNodeReferenceType(BufferedWriter out, NamespaceTable nsTable, NamespaceTable serverTable,
			boolean isFullServerTable, UAReferenceTypeNode node) throws IOException {
		// reference type node
		out.write(VARIABLE_LIST_NODES2ADD + ".add(new UAReferenceTypeNode("
				+ helpNodeId(nsTable, serverTable, isFullServerTable, node.getNodeId()) + "," + " NodeClass.valueOf("
				+ node.getNodeClass().getValue() + ") ," + helpQualifiedName(node.getBrowseName()) + " ,"
				+ helpLocalizedText(node.getDisplayName()) + " ," + helpLocalizedText(node.getDescription()) + ","
				+ " new UnsignedInteger(" + node.getWriteMask() + ") ," + " new UnsignedInteger("
				+ node.getUserWriteMask() + ") ," + "references2add.toArray(new ReferenceNode[0]) ,"
				+ node.getIsAbstract() + "," + node.getSymmetric() + "," + helpLocalizedText(node.getInverseName())
				+ "));".replace("\r", ""));
		out.newLine();
	}

	private void writeNodeVariable(BufferedWriter out, NamespaceTable nsTable, NamespaceTable serverTable,
			boolean isFullServerTable, UAServerVariableNode node, int valueCount) throws IOException {
		NodeId dataType = node.getDataType();
		if (dataType.compareTo(new NodeId(0, 0)) == 0)
			dataType = Identifiers.BaseDataType;
		// variable node
		out.write(VARIABLE_LIST_NODES2ADD + ".add(new UAServerVariableNode("
				+ helpNodeId(nsTable, serverTable, isFullServerTable, node.getNodeId()) + "," + " NodeClass.valueOf("
				+ node.getNodeClass().getValue() + ") ," + helpQualifiedName(node.getBrowseName()) + " ,"
				+ helpLocalizedText(node.getDisplayName()) + " ," + helpLocalizedText(node.getDescription()) + ","
				+ " new UnsignedInteger(" + node.getWriteMask() + ") ," + " new UnsignedInteger("
				+ node.getUserWriteMask() + ") ," + "references2add.toArray(new ReferenceNode[0]) ,"
				+ helpValue(valueCount, node.getValue()) + ","
				+ helpNodeId(nsTable, serverTable, isFullServerTable, dataType) + "," + node.getValueRank() + ","
				+ helpArrayDimension(node.getValueRank(), node.getArrayDimensions()) + "," + "new UnsignedByte(" + node.getAccessLevel()
				+ ")," + "new UnsignedByte(" + node.getUserAccessLevel() + ")," + node.getMinimumSamplingInterval()
				+ "," + node.getHistorizing() + "));".replace("\r", ""));
		out.newLine();
	}

	private void writeNodeVariableType(BufferedWriter out, NamespaceTable nsTable, NamespaceTable serverTable,
			boolean isFullServerTable, UAVariableTypeNode node, int valueCount) throws IOException {
		NodeId dataType = node.getDataType();
		if (dataType.compareTo(new NodeId(0, 0)) == 0)
			dataType = Identifiers.BaseDataType;
		// variable type
		out.write(VARIABLE_LIST_NODES2ADD + ".add(new UAVariableTypeNode("
				+ helpNodeId(nsTable, serverTable, isFullServerTable, node.getNodeId()) + "," + " NodeClass.valueOf("
				+ node.getNodeClass().getValue() + ") ," + helpQualifiedName(node.getBrowseName()) + " ,"
				+ helpLocalizedText(node.getDisplayName()) + " ," + helpLocalizedText(node.getDescription()) + ","
				+ " new UnsignedInteger(" + node.getWriteMask() + ") ," + " new UnsignedInteger("
				+ node.getUserWriteMask() + ") ," + "references2add.toArray(new ReferenceNode[0]) ,"
				+ helpValue(valueCount, node.getValue()) + ","
				+ helpNodeId(nsTable, serverTable, isFullServerTable, dataType) + "," + node.getValueRank() + ","
				+ helpArrayDimension(node.getValueRank(), node.getArrayDimensions()) + "," + node.getIsAbstract() + "));".replace("\r", ""));
		out.newLine();
	}

	private void writeNodeView(BufferedWriter out, NamespaceTable nsTable, NamespaceTable serverTable,
			boolean isFullServerTable, ViewNode node) throws IOException {
		// view node
		out.write(VARIABLE_LIST_NODES2ADD + ".add(new ViewNode("
				+ helpNodeId(nsTable, serverTable, isFullServerTable, node.getNodeId()) + "," + " NodeClass.valueOf("
				+ node.getNodeClass().getValue() + ") ," + helpQualifiedName(node.getBrowseName()) + " ,"
				+ helpLocalizedText(node.getDisplayName()) + " ," + helpLocalizedText(node.getDescription()) + ","
				+ " new UnsignedInteger(" + node.getWriteMask() + ") ," + " new UnsignedInteger("
				+ node.getUserWriteMask() + ") ," + "references2add.toArray(new ReferenceNode[0]) ,"
				+ node.getContainsNoLoops() + ", new UnsignedByte(" + node.getEventNotifier()
				+ ")));".replace("\r", ""));
		out.newLine();
	}

	private void writeReferenceNodes(BufferedWriter out, NamespaceTable nsTable, NamespaceTable serverTable,
			ReferenceNode[] references, NodeChunk chunkFactory, boolean isFullServerTable) throws IOException {
		if (references == null) {
			return;
		}
		// references
		for (int i = chunkFactory.beginChunkReferenceIndex; i < chunkFactory.nextChunkReferenceIndex + 1; i++) {
			ReferenceNode refNode = references[i];
			// something error
			if (NodeId.isNull(refNode.getReferenceTypeId())) {
				continue;
			}
			// skip target reference nodes which are not allowed in the
			// namespace table

			boolean allowedReference = checkReferenceNodeAllowed(nsTable, serverTable, isFullServerTable, refNode);
			if (!allowedReference) {
				continue;
			}
			out.write(REFERENCE_LIST_REFS2ADD + ".add(new ReferenceNode("
					+ helpNodeId(nsTable, serverTable, isFullServerTable, refNode.getReferenceTypeId()) + ", "
					+ refNode.getIsInverse().toString() + ","
					+ helpExpandedNodeId(nsTable, serverTable, isFullServerTable, refNode.getTargetId())
					+ "));".replace("\r", ""));
			out.newLine();
		}
		chunkFactory.beginChunkReferenceIndex = chunkFactory.nextChunkReferenceIndex + 1;
	}

	class NodeChunk {
		int beginChunkReferenceIndex = 0;
		int nextChunkReferenceIndex = 0;
		boolean chunk = false;
	}
}
