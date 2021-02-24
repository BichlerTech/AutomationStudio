package com.bichler.astudio.editor.pubsub.handler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.handlers.HandlerUtil;
import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.DateTime;
import org.opcfoundation.ua.builtintypes.ExtensionObject;
import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.QualifiedName;
import org.opcfoundation.ua.builtintypes.StatusCode;
import org.opcfoundation.ua.builtintypes.UnsignedByte;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.builtintypes.UnsignedShort;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.core.BrokerConnectionTransportDataType;
import org.opcfoundation.ua.core.BrokerDataSetWriterTransportDataType;
import org.opcfoundation.ua.core.BrokerTransportQualityOfService;
import org.opcfoundation.ua.core.BrokerWriterGroupTransportDataType;
import org.opcfoundation.ua.core.ConfigurationVersionDataType;
import org.opcfoundation.ua.core.ConnectionTransportDataType;
import org.opcfoundation.ua.core.ContentFilter;
import org.opcfoundation.ua.core.ContentFilterElement;
import org.opcfoundation.ua.core.DataSetMetaDataType;
import org.opcfoundation.ua.core.DataSetOrderingType;
import org.opcfoundation.ua.core.DataSetWriterMessageDataType;
import org.opcfoundation.ua.core.DataSetWriterTransportDataType;
import org.opcfoundation.ua.core.DatagramConnectionTransportDataType;
import org.opcfoundation.ua.core.DatagramWriterGroupTransportDataType;
import org.opcfoundation.ua.core.EnumDefinition;
import org.opcfoundation.ua.core.EnumDescription;
import org.opcfoundation.ua.core.EnumField;
import org.opcfoundation.ua.core.FieldMetaData;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.JsonDataSetWriterMessageDataType;
import org.opcfoundation.ua.core.JsonWriterGroupMessageDataType;
import org.opcfoundation.ua.core.KeyValuePair;
import org.opcfoundation.ua.core.MessageSecurityMode;
import org.opcfoundation.ua.core.NetworkAddressUrlDataType;
import org.opcfoundation.ua.core.PublishedEventsDataType;
import org.opcfoundation.ua.core.PublishedVariableDataType;
import org.opcfoundation.ua.core.SimpleTypeDescription;
import org.opcfoundation.ua.core.StructureDescription;
import org.opcfoundation.ua.core.UadpDataSetWriterMessageDataType;
import org.opcfoundation.ua.core.UadpWriterGroupMessageDataType;
import org.opcfoundation.ua.core.WriterGroupMessageDataType;
import org.opcfoundation.ua.core.WriterGroupTransportDataType;

import com.bichler.astudio.device.opcua.DeviceActivator;
import com.bichler.astudio.device.opcua.handler.AbstractOPCCompileHandler;
import com.bichler.astudio.editor.pubsub.PubSubActivator;
import com.bichler.astudio.editor.pubsub.nodes.DataSetFieldContentMask;
import com.bichler.astudio.editor.pubsub.nodes.DataSetVariable;
import com.bichler.astudio.editor.pubsub.nodes.PubSubConnection;
import com.bichler.astudio.editor.pubsub.nodes.PubSubDataSetField;
import com.bichler.astudio.editor.pubsub.nodes.PubSubDataSetWriter;
import com.bichler.astudio.editor.pubsub.nodes.PubSubEncodingType;
import com.bichler.astudio.editor.pubsub.nodes.PubSubEntryModelNode;
import com.bichler.astudio.editor.pubsub.nodes.PubSubModel;
import com.bichler.astudio.editor.pubsub.nodes.PubSubPublishedDataItemsTemplate;
import com.bichler.astudio.editor.pubsub.nodes.PubSubPublishedDataSet;
import com.bichler.astudio.editor.pubsub.nodes.PubSubPublishedDataSetType;
import com.bichler.astudio.editor.pubsub.nodes.PubSubPublishedEventTemplate;
import com.bichler.astudio.editor.pubsub.nodes.PubSubPublisherIdType;
import com.bichler.astudio.editor.pubsub.nodes.PubSubRTLevel;
import com.bichler.astudio.editor.pubsub.nodes.PubSubWriterGroup;
import com.bichler.astudio.editor.pubsub.wizard.core.MessageSettingType;
import com.bichler.astudio.editor.pubsub.wizard.core.TransportSettingType;
import com.bichler.astudio.editor.pubsub.wizard.core.WrapperConfigurationVersion;
import com.bichler.astudio.editor.pubsub.wizard.core.WrapperConnectionAddress;
import com.bichler.astudio.editor.pubsub.wizard.core.WrapperConnectionTransportSetting;
import com.bichler.astudio.editor.pubsub.wizard.core.WrapperDataSetMetaData;
import com.bichler.astudio.editor.pubsub.wizard.core.WrapperFieldMetaData;
import com.bichler.astudio.editor.pubsub.wizard.core.WrapperKeyValuePair;
import com.bichler.astudio.editor.pubsub.wizard.core.WrapperPublishedVariable;
import com.bichler.astudio.editor.pubsub.wizard.core.WrapperPublishedVariableParameter;
import com.bichler.astudio.editor.pubsub.wizard.core.WrapperStaticValue;
import com.bichler.astudio.editor.pubsub.wizard.core.WrapperWriterDataSetMessage;
import com.bichler.astudio.editor.pubsub.wizard.core.WrapperWriterDataSetTransport;
import com.bichler.astudio.editor.pubsub.wizard.core.WrapperWriterGroupMessage;
import com.bichler.astudio.editor.pubsub.wizard.core.WrapperWriterGroupTransport;
import com.bichler.astudio.navigation.nodes.StudioModelNode;
import com.bichler.astudio.opcua.addressspace.model.binary.AddressSpaceNodeModelFactoryC;
import com.bichler.astudio.opcua.handlers.AbstractOPCOpenDriverModelHandler;
import com.bichler.astudio.opcua.navigation.views.OPCNavigationView;
import com.bichler.astudio.opcua.nodes.OPCUAServerDriverModelNode;
import com.bichler.astudio.opcua.nodes.OPCUAServerDriversModelNode;
import com.bichler.astudio.opcua.nodes.OPCUAServerModuleModelNode;
import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import opc.sdk.core.enums.ValueRanks;
import opc.sdk.server.core.OPCInternalServer;

public class PubSubCompileHandler extends AbstractOPCOpenDriverModelHandler {
	public static final String ID = "com.bichler.astudio.editor.pubsub.1.0.0.compile";

	private static OPCInternalServer opcServer = null;
	private String tab = "\t";
	private String linefeed = "\n";

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IEvaluationContext evalCxt = (IEvaluationContext) event.getApplicationContext();
		OPCUAServerModuleModelNode node = (OPCUAServerModuleModelNode) evalCxt
				.getVariable(AbstractOPCCompileHandler.PARAMETER_MODULE_NODE);

		Logger.getLogger(getClass().getName()).log(Level.INFO, "Create pubsub config");

		String modulePath = new Path(node.getFilesystem().getRootPath()).append("modules").append(node.getModuleName())
				.toOSString();
		String makeFileFolder = modulePath;
		String exportFile = new Path(modulePath).append("export.c").toOSString();
		String configFile = new Path(modulePath).append("datapoints.com").toOSString();

		opcServer = ServerInstance.getInstance().getServerInstance();
		if (opcServer == null) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE,
					"Can not export pubsub config, because OPCServer instance not available!");
			return null;
		}
		try (FileReader reader = new FileReader(configFile);
				BufferedWriter outC = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(exportFile)))) {
			Gson GSON = new GsonBuilder().setPrettyPrinting().create();

			PubSubModel model = GSON.fromJson(reader, PubSubModel.class);

			// generate demo values
			// loadDemo(model);

			writeIncludeSectionC(outC, "");
			writePubSubStartC(outC, 0);
			generateLocalVar(outC, model);
			generateConnections(outC, model);
			generatePublishedDataSets(outC, model);
			writerPubSubEndC(outC);
		} catch (IOException e) {
			e.printStackTrace();
		}
		writeMakefile(makeFileFolder, node.getModuleName(), new File[0], new String[] { "export.c" });
		return null;
	}

	private void loadDemo(PubSubModel model) {
		List<PubSubConnection> connections = new ArrayList<PubSubConnection>();
		PubSubConnection conn = new PubSubConnection();
		generateDemoConnection1(conn);
		connections.add(conn);
		conn = new PubSubConnection();
		generateDemoConnection2(conn);
		connections.add(conn);
		model.setConnections(connections);

		List<PubSubPublishedDataSet> datasets = new ArrayList<PubSubPublishedDataSet>();
		PubSubPublishedDataSet ds = new PubSubPublishedDataSet();

		generateDemoPublishedDataSet(ds);
		datasets.add(ds);

		model.setPublishedDS(datasets);
	}

	/**
	 * 
	 * @param event
	 * @param drivers
	 */
	private void findDrivers(ExecutionEvent event, List<OPCUAServerDriverModelNode> drivers) {
		IPerspectiveDescriptor perspective = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage()
				.getPerspective();
		String perspectiveID = perspective.getId();
		StudioModelNode selectedElement = null;

		switch (perspectiveID) {
		case "com.bichler.astudio.opcua.perspective":// OPCServerPerspective.ID:
			OPCNavigationView view = (OPCNavigationView) HandlerUtil.getActiveWorkbenchWindow(event).getActivePage()
					.findView(OPCNavigationView.ID);
			selectedElement = (StudioModelNode) view.getViewer().getInput();

			Object[] children = selectedElement.getChildren();
			for (Object child : children) {
				if (child instanceof OPCUAServerDriversModelNode) {
					for (Object obj : ((OPCUAServerDriversModelNode) child).getChildren()) {
						if (obj instanceof OPCUAServerDriverModelNode) {
							OPCUAServerDriverModelNode driver = (OPCUAServerDriverModelNode) obj;
							if (driver.getDriverType().compareTo(PubSubActivator.MODULE_TYPE) == 0
									&& driver.getDriverVersion().compareTo(PubSubActivator.MODULE_VERSION) == 0)
								drivers.add((OPCUAServerDriverModelNode) obj);
						}
					}
					break;
				}
			}
			break;
		default:
			IStructuredSelection selection = (IStructuredSelection) HandlerUtil.getCurrentSelection(event);
			selectedElement = (StudioModelNode) selection.getFirstElement();
			break;
		}
		return;
	}

	private void generateLocalVar(BufferedWriter out, PubSubEntryModelNode node) throws IOException {
		if (node instanceof PubSubModel) {
			PubSubModel model = (PubSubModel) node;
			for (PubSubConnection conn : model.getConnections())
				generateLocalVar(out, conn);
			for (PubSubPublishedDataSet ds : model.getPublishedDS())
				generateLocalVar(out, ds);
		} else if (node instanceof PubSubConnection) {
			PubSubConnection conn = (PubSubConnection) node;
			out.write("UA_NodeId connectionIdent_" + conn.getName() + ";" + linefeed);
			for (PubSubWriterGroup wg : conn.getChildren())
				generateLocalVar(out, wg);
		} else if (node instanceof PubSubWriterGroup) {
			PubSubWriterGroup wg = (PubSubWriterGroup) node;
			out.write("UA_NodeId writerGroupIdent_" + wg.getName() + ";" + linefeed);
			for (PubSubDataSetWriter dsw : wg.getChildren())
				generateLocalVar(out, dsw);
		} else if (node instanceof PubSubDataSetWriter) {
			PubSubDataSetWriter dsw = (PubSubDataSetWriter) node;
			out.write("UA_NodeId dataSetWriterIdent_" + dsw.getName() + ";" + linefeed);
		} else if (node instanceof PubSubPublishedDataSet) {
			PubSubPublishedDataSet pds = (PubSubPublishedDataSet) node;
			out.write("UA_NodeId pdsIdent_" + pds.getName() + ";" + linefeed);
		}
	}

	private void writeMakefile(String folder, String libName, File[] files, String[] extCFiles) {
		// reate compiled folder
		File compiled = new File(folder);
		try {
			if (!compiled.exists())
				compiled.createNewFile();
			// create make file
			File f = new File(folder + "\\makefile");

			if (!f.exists())
				f.createNewFile();

			try (FileWriter fw = new FileWriter(f)) {
				fw.write("################################################################################\n");
				fw.write("#  Automatically-generated file. Do not edit!\n");
				fw.write("################################################################################\n");
				// fw.write("\n-include sources.mk\n");
				// fw.write("-include objects.mk\n\n");
				fw.write("\n");
				fw.write("C_SRCS += ");
				for (File file : files) {
					fw.write(file.getName() + " ");// .replace(".iec", ".c") + " ");
				}
				for (String file : extCFiles) {
					fw.write(file + " ");// .replace(".iec", ".c") + " ");
				}
				fw.write("\n");
				fw.write("\n");
				fw.write("OBJS += ");
				for (File file : files) {
					fw.write(file.getName().replace(".c", ".o") + " ");
				}
				for (String file : extCFiles) {
					fw.write(file.replace(".c", ".o") + " ");
				}
				fw.write("\n");
				fw.write("\n");
				fw.write("C_DEPS += ");
				for (File file : files) {
					fw.write(file.getName().replace(".c", ".d") + " ");
				}
				for (String file : extCFiles) {
					fw.write(file.replace(".c", ".d") + " ");
				}
//				fw.write("\n\nLIBS := -lopen62541\n");
				fw.write("\n");
				fw.write("RM := del -rf\n");
				fw.write("\n");
				fw.write("# All Target\r\n");
				fw.write("all: lib" + libName + ".so\n");
				fw.write("\n");
				fw.write("lib" + libName + ".so: $(OBJS)\n\t");

//				URL linker = FileLocator.find(DeviceActivator.getDefault().getBundle(),
//						Path.ROOT.append("toolchain").append("compiler").append("gcc-linaro-arm-linux-gnueabihf-4.9.3")
//								.append("bin").append("arm-linux-gnueabihf-gcc.exe"),
//						null);
				File fCompiler = DeviceActivator.getDefault()
						.getToolchainFile(DeviceActivator.getDefault().getToolchain(), "compiler");
				File fToolchain = DeviceActivator.getDefault().getToolchainFile(fCompiler,
						"gcc-linaro-arm-linux-gnueabihf-4.9.3");
				File fBin = DeviceActivator.getDefault().getToolchainFile(fToolchain, "bin");
				File linkerFile = DeviceActivator.getDefault().getToolchainFile(fBin, "arm-linux-gnueabihf-gcc.exe");

//				URL open62541File = FileLocator.find(DeviceActivator.getDefault().getBundle(), Path.ROOT
//						.append("toolchain").append("btech_lib").append("open62541").append("build-arm").append("bin"),
//						null);
//				URL open62541FileFUri = FileLocator.toFileURL(open62541File);

				File fBtechLib = DeviceActivator.getDefault()
						.getToolchainFile(DeviceActivator.getDefault().getToolchain(), "btech_lib");
				File fOpen62541 = DeviceActivator.getDefault().getToolchainFile(fBtechLib, "open62541");
				File fBuildArm = DeviceActivator.getDefault().getToolchainFile(fOpen62541, "build-arm");
				File open62541F = DeviceActivator.getDefault().getToolchainFile(fBuildArm, "bin");

//				File open62541F = new File(open62541FileFUri.getFile());

//				URL linker2 = FileLocator.toFileURL(linker);
//				File linkerFile = new File(linker2.getFile());
				fw.write(" \"" + linkerFile.getAbsolutePath());
				fw.write("\" -mcpu=cortex-a7 -shared -o \"" + libName + ".so\" $(OBJS)");

				fw.write("\n");
				fw.write("\n");
				// for (File file : files) {
				fw.write("%.o : %.c\n");
//				URL compiler = FileLocator.find(DeviceActivator.getDefault().getBundle(),
//						Path.ROOT.append("toolchain").append("compiler").append("gcc-linaro-arm-linux-gnueabihf-4.9.3")
//								.append("bin").append("arm-linux-gnueabihf-gcc.exe"),
//						null);
//				URL compiler2 = FileLocator.toFileURL(compiler);
//
//				File compilerFile = new File(compiler2.getFile());
				File compilerFile = linkerFile;
				fw.write("\t\"" + compilerFile.getAbsolutePath() + "\"");

				// include files
				// open62541/include
				// open62541/deps
				// open62541/build-arm/src_generated
				// open62541/plugins/include
				// open62541/deps/ua-nodeset/AnsiC
				// open62541/arch
				
//				URL includes = FileLocator.find(DeviceActivator.getDefault().getBundle(),
//						Path.ROOT.append("toolchain").append("btech_lib").append("open62541").append("include"), null);
//				URL includes2 = FileLocator.toFileURL(includes);
//				File includesFolder = new File(includes2.getFile());
				File includesFolder = DeviceActivator.getDefault().getToolchainFile(fOpen62541, "include");
				
				fw.write(" -I\"" + includesFolder.getAbsolutePath() + "\"");

//				includes = FileLocator.find(DeviceActivator.getDefault().getBundle(),
//						Path.ROOT.append("toolchain").append("btech_lib").append("open62541").append("deps"), null);
//				includes2 = FileLocator.toFileURL(includes);
//				includesFolder = new File(includes2.getFile());
				File fOpen62541Deps = includesFolder = DeviceActivator.getDefault().getToolchainFile(fOpen62541,
						"deps");
				
				fw.write(" -I\"" + fOpen62541Deps.getAbsolutePath() + "\"");

//				includes = FileLocator.find(DeviceActivator.getDefault().getBundle(), Path.ROOT.append("toolchain")
//						.append("btech_lib").append("open62541").append("build-arm").append("src_generated"), null);
//				includes2 = FileLocator.toFileURL(includes);
//				includesFolder = new File(includes2.getFile());	
				includesFolder = DeviceActivator.getDefault().getToolchainFile(fBuildArm, "src_generated");
				
				fw.write(" -I\"" + includesFolder.getAbsolutePath() + "\"");

//				includes = FileLocator.find(DeviceActivator.getDefault().getBundle(), Path.ROOT.append("toolchain")
//						.append("btech_lib").append("open62541").append("plugins").append("include"), null);
//				includes2 = FileLocator.toFileURL(includes);
//				includesFolder = new File(includes2.getFile());
				
				File fOpen62541Plugins = DeviceActivator.getDefault().getToolchainFile(fOpen62541, "plugins");
				includesFolder = DeviceActivator.getDefault().getToolchainFile(fOpen62541Plugins, "include");
				
				fw.write(" -I\"" + includesFolder.getAbsolutePath() + "\"");

//				includes = FileLocator.find(DeviceActivator.getDefault().getBundle(), Path.ROOT.append("toolchain")
//						.append("btech_lib").append("open62541").append("deps").append("ua-nodeset").append("AnsiC"),
//						null);
//				includes2 = FileLocator.toFileURL(includes);
//				includesFolder = new File(includes2.getFile());
				
				File fOpen62541UANodeset = DeviceActivator.getDefault().getToolchainFile(fOpen62541Deps, "ua-nodeset");
				includesFolder = DeviceActivator.getDefault().getToolchainFile(fOpen62541UANodeset, "AnsiC");
				
				fw.write(" -I\"" + includesFolder.getAbsolutePath() + "\"");

//				includes = FileLocator.find(DeviceActivator.getDefault().getBundle(),
//						Path.ROOT.append("toolchain").append("btech_lib").append("open62541").append("arch"), null);
//				includes2 = FileLocator.toFileURL(includes);
//				includesFolder = new File(includes2.getFile());
				
				includesFolder = DeviceActivator.getDefault().getToolchainFile(fOpen62541, "arch");
				
				fw.write(" -I\"" + includesFolder.getAbsolutePath() + "\"");

//				includes = FileLocator.find(DeviceActivator.getDefault().getBundle(),
//						Path.ROOT.append("toolchain").append("btech_include"), null);
//				includes2 = FileLocator.toFileURL(includes);
//				includesFolder = new File(includes2.getFile());
				
				includesFolder = DeviceActivator.getDefault()
						.getToolchainFile(DeviceActivator.getDefault().getToolchain(), "btech_include");
				
				fw.write(" -I\"" + includesFolder.getAbsolutePath() + "\"");

				fw.write(
						" -O3 -Wall -c -fmessage-length=0  -fPIC -MMD -MP -MF\"$(@:%.o=%.d)\" -MT\"$(@)\" -o \"$@\" \"$<\"\n");
				fw.write("\n");
				// 
				fw.write("cleanSources:\n");
				fw.write("\t-$(RM) $(C_SRCS)\n\n");// btech_opcua_server.elf ");
				fw.write("cleanBinary:\n");
				fw.write("\t-$(RM) $(OBJS)$(C_DEPS) lib" + libName + ".so ");
				for (String f2del : extCFiles) {
					// fw.write(f2del + " ");
				}
				fw.write(linefeed);
			} catch (Exception e) {
			}
			// now remove also make file
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	private void generateConnections(BufferedWriter out, PubSubModel model) throws IOException {
		for (PubSubConnection conn : model.getConnections()) {
			generateConnectionSource(out, conn);

			generateWriterGroups(out, conn.getChildren(), conn.getName());
		}
	}

	private void generatePublishedDataSets(BufferedWriter out, PubSubModel model) throws IOException {
		for (PubSubPublishedDataSet dataSet : model.getPublishedDS()) {
			generatePublishedDataSetSource(out, dataSet);

			generatePubSubDataSetFields(out, dataSet);
		}
	}

	/**
	 * 
	 * @param out
	 * @param conConf
	 * @throws IOException
	 */
	private void generateConnectionSource(BufferedWriter out,
			PubSubConnection conConf/*
									 * , /* List<DevicePubSubMapping.UA_PubSubConnectionConfig> conConfig
									 */) throws IOException {
//		out.write("UA_NodeId connectionIdentifier_" + conConf.getName() + ";" + linefeed);
		out.write("UA_Server_addPubSubConnection (g_pServer," + linefeed);
		out.write("(UA_PubSubConnectionConfig[1])" + linefeed);
		out.write("{{" + linefeed);
		out.write(tab);
		AddressSpaceNodeModelFactoryC.helpString(out, conConf.getName());
		out.write("," + linefeed);
		out.write(tab);
		AddressSpaceNodeModelFactoryC.helpBooleanValue(out, conConf.getEnabled());
		out.write("," + linefeed);
		out.write(tab + conConf.getPublisherIdType().name() + "," + linefeed);
		out.write(tab);
		helpPublisherId(out, conConf.getPublisherId());
		out.write("," + linefeed);
		out.write(tab);
		AddressSpaceNodeModelFactoryC.helpString(out, conConf.getTransportProfileUri());
		out.write("," + linefeed);
		out.write(tab);
		AddressSpaceNodeModelFactoryC.helpVariantC(out, helpConnectionAddress(conConf.getAddress()),
				Identifiers.NetworkAddressUrlDataType, 1, opcServer.getNamespaceUris(), opcServer.getNamespaceUris());
		out.write("," + linefeed);
		out.write(tab);
		KeyValuePair[] kvp = null;
		if (conConf.getConnectionProperties() == null)
			out.write("0");
		else {
			out.write(Integer.toString(conConf.getConnectionProperties().length));
			kvp = WrapperKeyValuePair.unwrap(conConf.getConnectionProperties());
		}
		out.write("," + linefeed);

		helpKeyValuePairs(out, kvp);
		out.write("," + linefeed);
		out.write(tab);
		NodeId connTransp = null;
		switch (conConf.getConnectionTransportSettings().getType()) {
		case Broker:
			connTransp = Identifiers.BrokerConnectionTransportDataType;
			break;
		default:
			connTransp = Identifiers.DatagramConnectionTransportDataType;
			break;
		}
		AddressSpaceNodeModelFactoryC.helpVariantC(out,
				helpConnectionTransportSettings(conConf.getConnectionTransportSettings()), connTransp, 1,
				opcServer.getNamespaceUris(), opcServer.getNamespaceUris());
		out.write("}}, &connectionIdent_" + conConf.getName() + ");" + linefeed + linefeed);
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

	/*
	 * function writes start of c information model
	 */
	private void writePubSubStartC(BufferedWriter out, int nsIndex) throws IOException {
		// this is the main callback function which will be called from main startup
		out.write("int btech_ua_LoadModule()" + linefeed + "{" + linefeed);
	}

	private void writerPubSubEndC(BufferedWriter out) throws IOException {
		out.write("return 0;" + linefeed);
		out.write("}" + linefeed);
	}

	private void generateWriterGroups(BufferedWriter out, List<PubSubWriterGroup> writerGroups, String connName)
			throws IOException {
		for (PubSubWriterGroup group : writerGroups) {
			generateWriterGroupSource(out, group, connName);
			generateWriters(out, group.getChildren(), group.getName());
		}
	}

	/**
	 * typedef struct { UA_String name; UA_Boolean enabled; UA_UInt16 writerGroupId;
	 * UA_Duration publishingInterval; UA_Double keepAliveTime; UA_Byte priority;
	 * UA_MessageSecurityMode securityMode; UA_ExtensionObject transportSettings;
	 * UA_ExtensionObject messageSettings; size_t groupPropertiesSize;
	 * UA_KeyValuePair *groupProperties; UA_PubSubEncodingType encodingMimeType; /*
	 * non std. config parameter. maximum count of embedded DataSetMessage in one
	 * NetworkMessage * UA_UInt16 maxEncapsulatedDataSetMessageCount; /* This flag
	 * is 'read only' and is set internally based on the PubSub state. * UA_Boolean
	 * configurationFrozen; /* non std. field * UA_PubSubRTLevel rtLevel; }
	 * UA_WriterGroupConfig;
	 * 
	 * @param out
	 * @param writerGroup
	 * @throws IOException
	 */
	private void generateWriterGroupSource(BufferedWriter out, PubSubWriterGroup writerGroup, String connName)
			throws IOException {
//		out.write("UA_NodeId writerGroupIdent_" + writerGroup.getName() + ";" + linefeed);
		out.write("UA_Server_addWriterGroup(g_pServer, connectionIdent_" + connName + "," + linefeed);// ,
																										// &writerGroupConfig,
																										// &writerGroupIdent);
		out.write("(UA_WriterGroupConfig[1]) " + linefeed + "{{" + linefeed);
		out.write(tab);
		AddressSpaceNodeModelFactoryC.helpString(out, writerGroup.getName());
//		out.write("UA_STRING(\"" + writerGroup.getName() + "\"),\n"); // UA_String name;
		out.write("," + linefeed);
		AddressSpaceNodeModelFactoryC.helpBooleanValue(out, writerGroup.getEnabled());
		out.write("," + linefeed); // UA_Boolean enabled;
		out.write(tab + writerGroup.getWriterGroupId());
		out.write("," + linefeed); // UA_UInt16 writerGroupId;
		out.write(tab);
		AddressSpaceNodeModelFactoryC.helpDouble(out, writerGroup.getPublishingInterval());
		out.write("," + linefeed); // UA_Duration publishingInterval;
		out.write(tab);
		AddressSpaceNodeModelFactoryC.helpDouble(out, writerGroup.getKeepAliveTime());
		out.write("," + linefeed); // UA_Double keepAliveTime;
		AddressSpaceNodeModelFactoryC.helpByte(out, writerGroup.getPriority()); // UA_Byte priority;
		out.write("," + linefeed);
		out.write(tab);
		if (writerGroup.getTransportSettings() == null)
			out.write("0");
		else
			out.write(Integer.toString(writerGroup.getSecurityMode().getValue())); // UA_MessageSecurityMode
		out.write("," + linefeed); // securityMode;
		out.write(tab);
		helpWriterGroupTransportData(out, helpWriterGroupTransport(writerGroup.getTransportSettings()));
		out.write("," + linefeed);
//		out.write(tab + writerGroup.getTransportSettings() + "," + linefeed); // UA_ExtensionObject transportSettings;
		out.write(tab); // + writerGroup.getMessageSettings() + "," + linefeed); // UA_ExtensionObject
						// messageSettings;
		helpWriterGroupMessageData(out, helpWriterGroupMessage(writerGroup.getMessageSettings()));
		out.write("," + linefeed);
		if (writerGroup.getGroupProperties() == null)
			out.write(tab + "0");
		else
			out.write(tab + Integer.toString(writerGroup.getGroupProperties().length)); // size_t groupPropertiesSize;
		out.write("," + linefeed);
		out.write(tab);
		helpKeyValuePairs(out, WrapperKeyValuePair.unwrap(writerGroup.getGroupProperties()));
//		out.write(tab + writerGroup.getGroupProperties() + "," + linefeed); // UA_KeyValuePair *groupProperties;
		out.write("," + linefeed);
		if (writerGroup.getEncodingMimeType() == null)
			out.write(tab + "0");
		else
			out.write(tab + Integer.toString(writerGroup.getEncodingMimeType().ordinal())); // UA_PubSubEncodingType
																							// encodingMimeType;
		out.write("," + linefeed);
		/*
		 * non std. config parameter. maximum count of embedded DataSetMessage in one
		 * NetworkMessage
		 */
		out.write(tab + Integer.toString(writerGroup.getMaxEncapsulatedDataSetMessageCount())); // UA_UInt16
		out.write("," + linefeed);
		// maxEncapsulatedDataSetMessageCount;
		/* This flag is 'read only' and is set internally based on the PubSub state. */
		AddressSpaceNodeModelFactoryC.helpBooleanValue(out, writerGroup.getConfigurationFrozen());
		out.write(tab + "," + linefeed); // UA_Boolean configurationFrozen;
		/* non std. field */
		out.write(tab);
		if (writerGroup.getRtLevel() == null)
			out.write("0");
		else
			out.write(Integer.toString(writerGroup.getRtLevel().ordinal())); // UA_PubSubRTLevel rtLevel;
		out.write(linefeed);
		out.write(tab + "}}," + linefeed);
		out.write("&writerGroupIdent_" + writerGroup.getName() + ");");
		out.write(linefeed + linefeed);
	}

	private WriterGroupTransportDataType helpWriterGroupTransport(WrapperWriterGroupTransport transportSettings) {
		WriterGroupTransportDataType wgtdt = null;

		switch (transportSettings.getType()) {
		case Broker:
			wgtdt = new BrokerWriterGroupTransportDataType();
			((BrokerWriterGroupTransportDataType) wgtdt)
					.setAuthenticationProfileUri(transportSettings.getAuthenticationProfileUri());
			((BrokerWriterGroupTransportDataType) wgtdt).setQueueName(transportSettings.getQueueName());
			((BrokerWriterGroupTransportDataType) wgtdt)
					.setRequestedDeliveryGuarantee(transportSettings.getRequestedDeliveryGuarantee());
			((BrokerWriterGroupTransportDataType) wgtdt).setResourceUri(transportSettings.getResourceUri());
			break;
		case Datagram:
			wgtdt = new DatagramWriterGroupTransportDataType();
			((DatagramWriterGroupTransportDataType) wgtdt)
					.setMessageRepeatCount(transportSettings.getMessageRepeatCount());
			((DatagramWriterGroupTransportDataType) wgtdt)
					.setMessageRepeatDelay(transportSettings.getMessageRepeatDelay());
			break;
		}

		return wgtdt;
	}

	private WriterGroupMessageDataType helpWriterGroupMessage(WrapperWriterGroupMessage wrapper) {
		WriterGroupMessageDataType wgmdt = null;
		switch (wrapper.getType()) {
		case JSON:
			wgmdt = new JsonWriterGroupMessageDataType();
			((JsonWriterGroupMessageDataType) wgmdt)
					.setNetworkMessageContentMask(wrapper.getJsonNetworkMessageContentMask());
			break;
		case UADP:
			wgmdt = new UadpWriterGroupMessageDataType();
			((UadpWriterGroupMessageDataType) wgmdt).setDataSetOrdering(wrapper.getDataSetOrdering());
			((UadpWriterGroupMessageDataType) wgmdt).setGroupVersion(wrapper.getGroupVersion());
			((UadpWriterGroupMessageDataType) wgmdt)
					.setNetworkMessageContentMask(wrapper.getNetworkMessageContentMask());
			((UadpWriterGroupMessageDataType) wgmdt).setPublishingOffset(wrapper.getPublishingOffset());
			((UadpWriterGroupMessageDataType) wgmdt).setSamplingOffset(wrapper.getSamplingOffset());
			break;
		}
		return wgmdt;
	}

	private void generateWriters(BufferedWriter out, List<PubSubDataSetWriter> writers, String writerGroup)
			throws IOException {
		for (PubSubDataSetWriter writer : writers) {
			generateWriterSource(out, writer, writerGroup);
		}
	}

	/**
	 * typedef struct { UA_String name; UA_UInt16 dataSetWriterId;
	 * UA_DataSetFieldContentMask dataSetFieldContentMask; UA_UInt32 keyFrameCount;
	 * UA_ExtensionObject messageSettings; UA_ExtensionObject transportSettings;
	 * UA_String dataSetName; size_t dataSetWriterPropertiesSize; UA_KeyValuePair
	 * *dataSetWriterProperties; /* This flag is 'read only' and is set internally
	 * based on the PubSub state. * UA_Boolean configurationFrozen; }
	 * UA_DataSetWriterConfig;
	 * 
	 * @param out
	 * @param writer
	 * @throws IOException
	 */
	private void generateWriterSource(BufferedWriter out, PubSubDataSetWriter writer, String writerGroup)
			throws IOException {
//		out.write("UA_NodeId dataSetWriterIdent_" + writer.getName() + ";" + linefeed);
		out.write("UA_Server_addDataSetWriter(g_pServer, writerGroupIdent_" + writerGroup + ", ");
		out.write("pdsIdent_" + writer.getDataSetName() + ",");

		out.write("(UA_DataSetWriterConfig[1]){{" + linefeed);
		out.write(tab);
		AddressSpaceNodeModelFactoryC.helpString(out, writer.getName());
		out.write("," + linefeed);
		out.write(tab + writer.getDataSetWriterId() + "," + linefeed); // UA_UInt16 dataSetWriterId;
		out.write(tab);
		if (writer.getDataSetFieldContentMask() == null)
			out.write("0");
		else
			out.write(Integer.toString(writer.getDataSetFieldContentMask().ordinal())); // UA_DataSetFieldContentMask
		out.write("," + linefeed); // dataSetFieldContentMask;
		out.write(tab + Integer.toString(writer.getKeyFrameCount())); // UA_UInt32 keyFrameCount;
		out.write("," + linefeed);
		out.write(tab);
		helpDataSetWriterMessageData(out, helpDataSetWriterMessage(writer.getMessageSettings()));
		out.write("," + linefeed);
		out.write(tab);

		helpDataSetWriterTransportData(out, helpDataSetWriterTransport(writer.getTransportSettings()));
		out.write("," + linefeed);
		out.write(tab);
		AddressSpaceNodeModelFactoryC.helpString(out, writer.getDataSetName());
		out.write("," + linefeed);
		out.write(tab);
		if (writer.getDataSetWriterProperties() == null)
			out.write("0");
		else
			out.write(Integer.toString(writer.getDataSetWriterProperties().length)); // size_t
		out.write("," + linefeed); // dataSetWriterPropertiesSize;
		out.write(tab);
		helpKeyValuePairs(out, WrapperKeyValuePair.unwrap(writer.getDataSetWriterProperties()));
		out.write("," + linefeed);
		/* This flag is 'read only' and is set internally based on the PubSub state. */
		AddressSpaceNodeModelFactoryC.helpBooleanValue(out, writer.getConfigurationFrozen()); // UA_Boolean
																								// configurationFrozen;
		out.write(linefeed + "}}");
		out.write("," + linefeed);
		out.write(" &dataSetWriterIdent_" + writer.getName() + ");");
		out.write(linefeed + linefeed);
	}

	private DataSetWriterMessageDataType helpDataSetWriterMessage(WrapperWriterDataSetMessage messageSettings) {
		DataSetWriterMessageDataType value = null;
		switch (messageSettings.getType()) {
		case JSON:
			value = new JsonDataSetWriterMessageDataType(messageSettings.getNetworkMessageContentMask(),
					messageSettings.getDataSetMessageContentMask());
			break;
		case UADP:
		default:
			value = new UadpDataSetWriterMessageDataType(messageSettings.getDataSetMessageContentMask(),
					messageSettings.getConfiguredSize(), messageSettings.getNetworkMessageNumber(),
					messageSettings.getDataSetOffset());
			break;
		}

		return value;
	}

	private DataSetWriterTransportDataType helpDataSetWriterTransport(WrapperWriterDataSetTransport wrapper) {
		BrokerDataSetWriterTransportDataType type = new BrokerDataSetWriterTransportDataType();
		type.setAuthenticationProfileUri(wrapper.getAuthenticationProfileUri());
		type.setMetaDataQueueName(wrapper.getMetaDataQueueName());
		type.setMetaDataUpdateTime(wrapper.getMetaDataUpdateTime());
		type.setQueueName(wrapper.getQueueName());
		type.setRequestedDeliveryGuarantee(wrapper.getRequestedDeliveryGuarantee());
		type.setResourceUri(wrapper.getResourceUri());
		return type;
	}

	private void generatePublishedDataSetSource(BufferedWriter out, PubSubPublishedDataSet dataSet) throws IOException {
//		out.write("UA_NodeId " + dataSet.getName() + "_pdsIdentifier;" + linefeed);
		out.write("UA_Server_addPublishedDataSet(g_pServer, (UA_PublishedDataSetConfig[1]) {{" + linefeed);
		out.write(tab);
		AddressSpaceNodeModelFactoryC.helpString(out, dataSet.getName()); // UA_String name;
		out.write("," + linefeed);
		out.write(tab);
		if (dataSet.getPublishedDataSetType() == null)
			out.write("0");
		else
			out.write(Integer.toString(dataSet.getPublishedDataSetType().ordinal())); // UA_PublishedDataSetType
		out.write("," + linefeed); // publishedDataSetType;

		out.write(tab);
		generateConfsources(out, dataSet.getConfig());
		out.write("," + linefeed);
		/* This flag is 'read only' and is set internally based on the PubSub state. */
		AddressSpaceNodeModelFactoryC.helpBooleanValue(out, dataSet.getConfigurationFrozen()); // UA_Boolean
																								// configurationFrozen;
		out.write(linefeed);
		out.write("}}, &pdsIdent_" + dataSet.getName() + ");");
		out.write(linefeed + linefeed);
	}

	private void generateConfsources(BufferedWriter out, Object config) throws IOException {
		if (config instanceof PubSubPublishedDataItemsTemplate)
			generateItemsTemplateSource(out, (PubSubPublishedDataItemsTemplate) config);
		else if (config instanceof PublishedEventsDataType)
			generateEventsDataSource(out, (PublishedEventsDataType) config);
		else if (config instanceof PubSubPublishedEventTemplate)
			generateEventsTemplateSource(out, (PubSubPublishedEventTemplate) config);
		else
			out.write("NULL");
	}

	private void generateItemsTemplateSource(BufferedWriter out, PubSubPublishedDataItemsTemplate itemsTemplate)
			throws IOException {
		out.write(".config.itemsTemplate = "); // we can use only one variable of union definition
		out.write("{" + linefeed);
		generateDataSetMetaDataSource(out, helpDataSetMetaDataSource(itemsTemplate.getMetaData()));
		out.write("," + linefeed);
		if (itemsTemplate.getVariablesToAdd() == null)
			out.write("0");
		else
			out.write(Integer.toString(itemsTemplate.getVariablesToAdd().length)); // size_t variablesToAddSize
		out.write("," + linefeed);
		generatePublishedVariableDatas(out, helpVariablesToAdd(itemsTemplate.getVariablesToAdd())); // UA_PublishedVariableDataType
		// *variablesToAdd;
		out.write("}" + linefeed);
	}

	private PublishedVariableDataType[] helpVariablesToAdd(WrapperPublishedVariable[] variablesToAdd) {
		PublishedVariableDataType[] variables = new PublishedVariableDataType[variablesToAdd.length];
		for (int i = 0; i < variablesToAdd.length; i++) {
			variables[i] = new PublishedVariableDataType();
			variables[i].setAttributeId(variablesToAdd[i].getAttributeId());
			variables[i].setDeadbandType(variablesToAdd[i].getDeadbandType());
			variables[i].setDeadbandValue(variablesToAdd[i].getDeadbandValue());
			variables[i].setIndexRange(variablesToAdd[i].getIndexRange());
			variables[i].setMetaDataProperties(variablesToAdd[i].getMetaDataProperties());
			variables[i].setPublishedVariable(variablesToAdd[i].getPublishedVariable());
			variables[i].setSamplingIntervalHint(variablesToAdd[i].getSamplingIntervalHint());
			variables[i].setSubstituteValue(variablesToAdd[i].getSubstituteValue());
		}

		return variables;
	}

	// TODO: Test if EnumDataSetTypes, Namespaces, SimpleDataTypes and
	// StructureDataTypes are not needed
	private DataSetMetaDataType helpDataSetMetaDataSource(WrapperDataSetMetaData value) {
		DataSetMetaDataType metadata = new DataSetMetaDataType();
		metadata.setConfigurationVersion(helpConfigurationVersion(value.getConfigurationVersion()));
		metadata.setDataSetClassId(value.getDataSetClassId());
		metadata.setDescription(value.getDescription());
//		metadata.setEnumDataTypes(value.getEnumDataTypes());
		metadata.setFields(helpFields(value.getFields()));
		metadata.setName(value.getName());
//		metadata.setNamespaces(value.getNamespaces());
//		metadata.setSimpleDataTypes(value.getSimpleDataTypes());
//		metadata.setStructureDataTypes(value.getStructureDataTypes());
		return metadata;
	}

	private FieldMetaData[] helpFields(WrapperFieldMetaData[] fields) {
		FieldMetaData[] metadata = new FieldMetaData[fields.length];
		for (int i = 0; i < metadata.length; i++) {
			metadata[i] = new FieldMetaData();
			metadata[i].setArrayDimensions(fields[i].getArrayDimensions());
			metadata[i].setBuiltInType(fields[i].getBuiltInType());
			metadata[i].setDataSetFieldId(fields[i].getDataSetFieldId());
			metadata[i].setDataType(fields[i].getDataType());
			metadata[i].setDescription(fields[i].getDescription());
			metadata[i].setFieldFlags(fields[i].getFieldFlags());
			metadata[i].setMaxStringLength(fields[i].getMaxStringLength());
			metadata[i].setName(fields[i].getName());
			metadata[i].setProperties(WrapperKeyValuePair.unwrap(fields[i].getProperties()));
			metadata[i].setValueRank(fields[i].getValueRank());
		}

		return metadata;
	}

	private void generateEventsTemplateSource(BufferedWriter out, PubSubPublishedEventTemplate itemsTemplate)
			throws IOException {
//		out.write(".config.itemsTemplate = "); // we can use only one variable of union definition
//		out.write("{\n");
//		generateDataSetMetaDataSource(out, itemsTemplate.getMetaData());
//		out.write(",\n");
//		out.write(itemsTemplate.getVariablesToAdd().length + ",\n"); // size_t variablesToAddSize
//		generatePublishedVariableDatas(out, itemsTemplate.getVariablesToAdd()); // UA_PublishedVariableDataType
//																				// *variablesToAdd;
//		out.write("}\n");
//		    		union {
//		                /* The UA_PUBSUB_DATASET_PUBLISHEDITEMS has currently no additional members
//		                 * and thus no dedicated config structure.*/
//		                UA_PublishedDataItemsTemplateConfig itemsTemplate;
//		                UA_PublishedEventConfig event;
//		                UA_PublishedEventTemplateConfig eventTemplate;
//		            } config;
	}

	private void generatePublishedVariableDatas(BufferedWriter out, PublishedVariableDataType[] dataTypes)
			throws IOException {
		if (dataTypes == null) {
			out.write("NULL" + linefeed);
			return;
		}
		out.write("(UA_PublishedVariableDataType[" + dataTypes.length + "]) {" + linefeed);
		String comma = "";
		for (PublishedVariableDataType dt : dataTypes) {
			out.write(comma);
			generatePublishedVariableDataSource(out, dt);
			comma = ",";
		}
		out.write("}" + linefeed);
	}

	/**
	 * typedef struct { UA_NodeId eventNotfier; UA_ContentFilter filter; }
	 * UA_PublishedEventConfig;
	 * 
	 * @param out
	 * @param eventsData
	 * @throws IOException
	 */
	private void generateEventsDataSource(BufferedWriter out, PublishedEventsDataType eventsData) throws IOException {
		out.write(".config.event = ");
		out.write("{" + linefeed);
		AddressSpaceNodeModelFactoryC.helpNodeIdC(out, /* only selected namespaces */ opcServer.getNamespaceUris(),
				opcServer.getNamespaceUris(), eventsData.getEventNotifier());
		out.write("," + linefeed);
		generateContentFilterSource(out, eventsData.getFilter());
		eventsData.getFilter();
		out.write(linefeed);
		out.write("}" + linefeed);
	}

	/**
	 * typedef struct { size_t elementsSize; UA_ContentFilterElement *elements; }
	 * UA_ContentFilter;
	 * 
	 * @param out
	 * @param filter
	 * @throws IOException
	 */
	private void generateContentFilterSource(BufferedWriter out, ContentFilter filter) throws IOException {
		if (filter == null) {
			out.write("UA_CONTENTFILTER_NULL");
			out.write(linefeed);
			return;
		}
		out.write("{" + linefeed);
		if (filter.getElements() == null) {
			out.write("0");
			out.write("," + linefeed);
			out.write("NULL");
		} else {
			out.write(filter.getElements().length);
			out.write("," + linefeed);
			out.write("(UA_ContentFilterElement[" + filter.getElements().length + "])");
			out.write("{" + linefeed);
			for (ContentFilterElement element : filter.getElements()) {
				generateContentFilterElementSource(out, element);
			}
			out.write("\t}" + linefeed);
		}
		out.write("}" + linefeed);
	}

	/**
	 * typedef struct { UA_FilterOperator filterOperator; size_t filterOperandsSize;
	 * UA_ExtensionObject *filterOperands; } UA_ContentFilterElement;
	 * 
	 * @param out
	 * @param element
	 * @throws IOException
	 */
	private void generateContentFilterElementSource(BufferedWriter out, ContentFilterElement element)
			throws IOException {
		if (element == null) {
			out.write("UA_CONTENTFILTER_ELEMENT_NULL");
			out.write(linefeed);
			return;
		}

		out.write("{" + linefeed);
		if (element.getFilterOperator() == null)
			out.write("0");
		else
			out.write(Integer.toString(element.getFilterOperator().getValue()));
		out.write("," + linefeed);
		if (element.getFilterOperands() == null)
			out.write("0");
		else
			out.write(Integer.toString(element.getFilterOperands().length));
		out.write("," + linefeed);
		generateContentFilterOperands(out, element.getFilterOperands());
		out.write("\t\t}" + linefeed);
	}

	private void generateContentFilterOperands(BufferedWriter out, ExtensionObject[] operands) throws IOException {
		if (operands == null) {
			out.write("NULL");
			out.write(linefeed);
			return;
		}

		out.write("(UA_ExtensionObject[" + operands.length + "])");
		out.write(linefeed);
		out.write("\t\t\t{" + linefeed);
		for (ExtensionObject operand : operands) {
			generateContentFilterOperandSource(out, operand);
		}
		out.write("\t\t\t}" + linefeed);
	}

	private void generateContentFilterOperandSource(BufferedWriter out, ExtensionObject operand) throws IOException {
		out.write("\t\t\t{" + linefeed);
		out.write(tab + "UA_EXTENSIONOBJECT_DECODED");
		out.write(", " + linefeed); // set encoding
		out.write(".content.decoded = {" + linefeed);
		out.write("&UA_TYPES[UA_TYPES_FILTEROPERAND]");
		out.write("," + linefeed);

		out.write("(void *){" + linefeed);
		// TODO
		out.write("\t\t\t}" + linefeed);
		out.write("}" + linefeed);
	}

	/**
	 * typedef struct { size_t namespacesSize; UA_String *namespaces; size_t
	 * structureDataTypesSize; UA_StructureDescription *structureDataTypes; size_t
	 * enumDataTypesSize; UA_EnumDescription *enumDataTypes; size_t
	 * simpleDataTypesSize; UA_SimpleTypeDescription *simpleDataTypes; UA_String
	 * name; UA_LocalizedText description; size_t fieldsSize; UA_FieldMetaData
	 * *fields; UA_Guid dataSetClassId; UA_ConfigurationVersionDataType
	 * configurationVersion; } UA_DataSetMetaDataType;
	 * 
	 * @param out
	 * @param metaData
	 * @throws IOException
	 */
	private void generateDataSetMetaDataSource(BufferedWriter out, DataSetMetaDataType metaData) throws IOException {
		if (metaData == null) {
			out.write("UA_DATASETMETADATATYPE_NULL");
			return;
		}
		out.write("{" + linefeed); // UA_DataSetMetaDataType metaData
		if (metaData.getNamespaces() == null) {
			out.write("0"); // size_t namespacesSize;
		} else {
			out.write(Integer.toString(metaData.getNamespaces().length)); // size_t namespacesSize;
		}
		out.write("," + linefeed);
		AddressSpaceNodeModelFactoryC.helpStringArray(out, metaData.getNamespaces()); // UA_String *namespaces;
		out.write("," + linefeed);

		if (metaData.getStructureDataTypes() == null) {
			out.write("0");
		} else {
			out.write(Integer.toString(metaData.getStructureDataTypes().length)); // size_t structureDataTypesSize;

		}
		out.write("," + linefeed);
		generateStructureDescriptionSource(out, metaData.getStructureDataTypes()); // UA_StructureDescription
																					// *structureDataTypes;
		out.write("," + linefeed);
		if (metaData.getEnumDataTypes() == null) {
			out.write("0");
		} else {
			out.write(Integer.toString(metaData.getEnumDataTypes().length)); // size_t enumDataTypesSize;
		}
		out.write("," + linefeed);
		generateEnumDescriptionSource(out, metaData.getEnumDataTypes()); // UA_EnumDescription *enumDataTypes;
		out.write("," + linefeed);
		if (metaData.getSimpleDataTypes() == null) {
			out.write("0");
		} else {
			out.write(Integer.toString(metaData.getSimpleDataTypes().length)); // size_t simpleDataTypesSize;
		}
		out.write("," + linefeed);
		generateSimpleTypeDescriptionSource(out, metaData.getSimpleDataTypes()); // UA_SimpleTypeDescription
																					// *simpleDataTypes;
		out.write("," + linefeed);
		AddressSpaceNodeModelFactoryC.helpString(out, metaData.getName());
		out.write("," + linefeed);
//		out.write("UA_STRING(" + metaData.getName() + ")," + linefeed); // UA_String name;
		AddressSpaceNodeModelFactoryC.helpLocalizedTextC(out, metaData.getDescription()); // UA_LocalizedText
																							// description;
		out.write("," + linefeed);

		if (metaData.getFields() == null) {
			out.write("0");
		} else {
			out.write(Integer.toString(metaData.getFields().length)); // size_t fieldsSize;
		}
		out.write("," + linefeed);
		generateFieldMetaDataSource(out, metaData.getFields()); // UA_FieldMetaData *fields;
		out.write("," + linefeed);
		generateGuidSource(out, metaData.getDataSetClassId()); // UA_Guid dataSetClassId;
		out.write("," + linefeed);
		generateConfigurationVersionSource(out, metaData.getConfigurationVersion());
		out.write("}" + linefeed);
	}

	private void generateStructureDescriptionSource(BufferedWriter out, StructureDescription[] description)
			throws IOException {
		if (description == null)
			out.write("NULL");
		else
			out.write("");
	}

	/**
	 * typedef struct { UA_NodeId dataTypeId; UA_QualifiedName name;
	 * UA_EnumDefinition enumDefinition; UA_Byte builtInType; } UA_EnumDescription;
	 * 
	 * @param out
	 * @param enumDataTypes
	 * @throws IOException
	 */
	private void generateEnumDescriptionSource(BufferedWriter out, EnumDescription[] enumDataTypes) throws IOException {
		if (enumDataTypes == null)
			out.write("NULL");
		else {
			out.write("(UA_EnumDescription[" + enumDataTypes.length + "]){");
			String comma = "";
			for (EnumDescription desc : enumDataTypes) {
				out.write(comma);
				out.write("{" + linefeed);
				AddressSpaceNodeModelFactoryC.helpNodeIdC(out, opcServer.getNamespaceUris(),
						opcServer.getNamespaceUris(), desc.getDataTypeId());
				out.write("," + linefeed);
				AddressSpaceNodeModelFactoryC.helpQualifiedNameC(out, desc.getName());
				out.write("," + linefeed);
				generateEnumDefinitionSource(out, desc.getEnumDefinition());
				out.write("," + linefeed);
				if (desc.getBuiltInType() == null)
					out.write("0");
				else
					out.write(Integer.toString(desc.getBuiltInType().getValue()));
				out.write(linefeed);
				out.write("}" + linefeed);
				comma = ",";
			}
			out.write("}");
		}
	}

	/**
	 * typedef struct { size_t fieldsSize; UA_EnumField *fields; }
	 * UA_EnumDefinition;
	 * 
	 * @param out
	 * @param enumdef
	 * @throws IOException
	 */
	private void generateEnumDefinitionSource(BufferedWriter out, EnumDefinition enumdef) throws IOException {
		int length = 0;
		if (enumdef == null) {
			out.write("ENUMDEFINITION_NULL");
		} else {
			if (enumdef.getFields() != null)
				length = enumdef.getFields().length;

			out.write("{" + linefeed);
			out.write(length + "," + linefeed);
			if (enumdef.getFields() == null || enumdef.getFields().length == 0)
				out.write("NULL");
			else {
				out.write("(UA_Enumfield[" + enumdef.getFields().length + "]) {");
				for (EnumField field : enumdef.getFields()) {
					generateEnumFieldSource(out, field);
				}
				out.write("}" + linefeed);
			}

			out.write("}" + linefeed);
		}
	}

	/**
	 * UA_Int64 value; UA_LocalizedText displayName; UA_LocalizedText description;
	 * UA_String name;
	 * 
	 * @param out
	 * @param field
	 * @throws IOException
	 */
	private void generateEnumFieldSource(BufferedWriter out, EnumField field) throws IOException {
		long value = 0;
		LocalizedText displayName = null;
		LocalizedText description = null;
		String name = null;
		if (field != null) {
			value = field.getValue();
			displayName = field.getDisplayName();
			description = field.getDescription();
			name = field.getName();
		}
		out.write("{" + linefeed);
		out.write(value + "," + linefeed);
		AddressSpaceNodeModelFactoryC.helpLocalizedTextC(out, displayName);
		out.write("," + linefeed);
		AddressSpaceNodeModelFactoryC.helpLocalizedTextC(out, description);
		out.write("," + linefeed);
		AddressSpaceNodeModelFactoryC.helpString(out, name);
		out.write("}" + linefeed);
	}

	/**
	 * typedef struct { UA_NodeId dataTypeId; UA_QualifiedName name; UA_NodeId
	 * baseDataType; UA_Byte builtInType; } UA_SimpleTypeDescription;
	 */
	private void generateSimpleTypeDescriptionSource(BufferedWriter out, SimpleTypeDescription[] simpleTypeDesc)
			throws IOException {
		if (simpleTypeDesc == null)
			out.write("NULL");
		else {
			out.write("(UA_SimpleTypeDescription[" + simpleTypeDesc.length + "]) {");
			out.write(linefeed);
			String comma = "";
			for (SimpleTypeDescription st : simpleTypeDesc) {
				out.write(comma);
				out.write("{" + linefeed);
				AddressSpaceNodeModelFactoryC.helpNodeIdC(out, opcServer.getNamespaceUris(),
						opcServer.getNamespaceUris(), st.getDataTypeId());
				out.write("," + linefeed);
				AddressSpaceNodeModelFactoryC.helpQualifiedNameC(out, st.getName());
				out.write("," + linefeed);
				AddressSpaceNodeModelFactoryC.helpNodeIdC(out, opcServer.getNamespaceUris(),
						opcServer.getNamespaceUris(), st.getBaseDataType());
				out.write("," + linefeed);
				AddressSpaceNodeModelFactoryC.helpUnsignedByte(out, st.getBuiltInType());
				out.write(linefeed);
				out.write("}" + linefeed);
				comma = ",";
			}
			out.write("}");
			out.write(linefeed);
		}
	}

	/**
	 * typedef struct { unsigned int data1; unsigned short int data2; unsigned short
	 * int data3; unsigned char data4[8]; } UA_Guid;
	 * 
	 * @param out
	 * @param guid
	 * @throws IOException
	 */
	private void generateGuidSource(BufferedWriter out, UUID guid) throws IOException {
		if (guid == null)
			out.write("UA_GUID_NULL");
		else {
			out.write("{0,0,0, \"00000000\"}");
		}
	}

	/**
	 * typedef struct { UA_String name; UA_LocalizedText description;
	 * UA_DataSetFieldFlags fieldFlags; UA_Byte builtInType; UA_NodeId dataType;
	 * UA_Int32 valueRank; size_t arrayDimensionsSize; UA_UInt32 *arrayDimensions;
	 * UA_UInt32 maxStringLength; UA_Guid dataSetFieldId; size_t propertiesSize;
	 * UA_KeyValuePair *properties; } UA_FieldMetaData;
	 * 
	 * @param out
	 * @param fieldMetaData
	 * @throws IOException
	 */
	private void generateFieldMetaDataSource(BufferedWriter out, FieldMetaData[] fieldMetaData) throws IOException {
		if (fieldMetaData == null)
			out.write("NULL");
		else {
			out.write("(UA_FieldMetaData[" + fieldMetaData.length + "]) {" + linefeed);
			for (FieldMetaData meta : fieldMetaData) {
				out.write("{" + linefeed);
				AddressSpaceNodeModelFactoryC.helpString(out, meta.getName());
				out.write("," + linefeed);
				AddressSpaceNodeModelFactoryC.helpLocalizedTextC(out, meta.getDescription());
				out.write("," + linefeed);
				AddressSpaceNodeModelFactoryC.helpUnsignedShortC(out, meta.getFieldFlags());
				out.write("," + linefeed);
				AddressSpaceNodeModelFactoryC.helpUnsignedByteC(out, meta.getBuiltInType());
				out.write("," + linefeed);
				AddressSpaceNodeModelFactoryC.helpNodeIdC(out, opcServer.getNamespaceUris(),
						opcServer.getNamespaceUris(), meta.getDataType());
				out.write("," + linefeed);
				AddressSpaceNodeModelFactoryC.helpInteger(out, meta.getValueRank());
				out.write("," + linefeed);
				if (meta.getArrayDimensions() == null)
					out.write("0");
				else
					out.write(Integer.toString(meta.getArrayDimensions().length));
				out.write("," + linefeed);
				AddressSpaceNodeModelFactoryC.helpArrayDimensionsC(out, meta.getArrayDimensions(), meta.getValueRank(),
						1);
				out.write("," + linefeed);
				AddressSpaceNodeModelFactoryC.helpUnsignedInteger(out, meta.getMaxStringLength());
				out.write("," + linefeed);
				generateGuidSource(out, meta.getDataSetFieldId());
				out.write("," + linefeed);
				if (meta.getProperties() == null)
					out.write("0");
				else
					out.write(Integer.toString(meta.getProperties().length));
				out.write("," + linefeed);
				helpKeyValuePairs(out, meta.getProperties());
				out.write("}" + linefeed);
			}
			out.write("}" + linefeed);
		}
	}

	private void generateConfigurationVersionSource(BufferedWriter out, ConfigurationVersionDataType configVersion)
			throws IOException {
		if (configVersion == null) {
			out.write("UA_CCONFIGURATIONVERSIONDATATYPE_NULL");
		}
		out.write("{" + linefeed); // UA_ConfigurationVersionDataType configurationVersion;
		AddressSpaceNodeModelFactoryC.helpUnsignedInteger(out, configVersion.getMajorVersion());
		out.write("," + linefeed); // majorVersion
		AddressSpaceNodeModelFactoryC.helpUnsignedInteger(out, configVersion.getMinorVersion());
		out.write(linefeed); // minorVersion
		out.write("}" + linefeed);
	}

	private void generatePubSubDataSetFields(BufferedWriter out,
			PubSubPublishedDataSet dataSet /* List<PubSubDataSetField> fields */) throws IOException {
		for (PubSubDataSetField field : dataSet.getChildren()) {
			generateDataSetFieldSource(out, field, "pdsIdent_" + dataSet.getName());
		}
	}

	/**
	 * typedef struct { UA_DataSetFieldType dataSetFieldType; union { /* events need
	 * other config later * UA_DataSetVariableConfig variable; } field; /* This flag
	 * is 'read only' and is set internally based on the PubSub state. * UA_Boolean
	 * configurationFrozen; } UA_DataSetFieldConfig;
	 * 
	 * @param out
	 * @param field
	 * @param pdsIdentifier
	 * @throws IOException
	 */
	private void generateDataSetFieldSource(BufferedWriter out, PubSubDataSetField field, String pdsIdentifier)
			throws IOException {
		int fType = 0;
		DataSetVariable dsfield = null;
		Boolean conFrozen = null;
		if (field != null) {
			fType = field.getDataSetFieldType().ordinal();
			dsfield = field.getField();
			conFrozen = field.getConfigurationFrozen();
		}
		out.write("UA_DataSetFieldConfig field {" + linefeed);
		out.write(tab + fType);
		out.write("," + linefeed);
		out.write(tab + ".field = ");
		helpDataSetVariable(out, dsfield);
		out.write("," + linefeed);
		AddressSpaceNodeModelFactoryC.helpBooleanValue(out, conFrozen);
		out.write("};" + linefeed);
		/*
		 * typedef struct { UA_DataSetFieldType dataSetFieldType; union { /* events need
		 * other config later
		 */
		// UA_DataSetVariableConfig variable;

		out.write(
				"UA_Server_addDataSetField(g_pServer, publishedDataSetIdent, &dataSetFieldConfig, &dataSetFieldIdent);");
	}

	/**
	 * print a UA_DataSetVariableConfig struct in ansi c: typedef struct {
	 * UA_ConfigurationVersionDataType configurationVersion; UA_String
	 * fieldNameAlias; UA_Boolean promotedField; UA_PublishedVariableDataType
	 * publishParameters; // non std. field UA_Boolean staticValueSourceEnabled;
	 * UA_DataValue staticValueSource; } UA_DataSetVariableConfig;
	 * 
	 * @param out
	 * @param variable
	 * @throws IOException
	 */
	private void helpDataSetVariable(BufferedWriter out, DataSetVariable variable) throws IOException {
		ConfigurationVersionDataType cv = null;
		String falias = null;
		Boolean promField = null;
		Boolean statIN = null;
		Boolean statVSEnabled = null;
		DataValue statVS = null;
		PublishedVariableDataType pvdt = null;
		if (variable != null) {
			cv = helpConfigurationVersion(variable.getConfigurationVersion());
			falias = variable.getFieldNameAlias();
			promField = variable.getPromotedField();
			WrapperStaticValue staticVariable = variable.getStaticValue();
			
			statVSEnabled = staticVariable.isFieldSourceEnabled();
			statIN = staticVariable.isInformationModelNode();
			statVS = staticVariable.getStaticValueSource();
			pvdt = helpPublishParameters(variable.getPublishParameters());
		}
		out.write("{" + linefeed);
		helpConfigurationData(out, cv);
		out.write("," + linefeed);
		out.write(tab);
		AddressSpaceNodeModelFactoryC.helpString(out, falias);
		out.write("," + linefeed);
		out.write(tab);
		AddressSpaceNodeModelFactoryC.helpBooleanValue(out, promField);
		out.write("," + linefeed);
		out.write(tab);
		generatePublishedVariableDataSource(out, pvdt);
//		variable.getPublishParameters();
		out.write("," + linefeed);
		out.write(tab);
		AddressSpaceNodeModelFactoryC.helpBooleanValue(out, statVSEnabled);
		out.write("," + linefeed);
		out.write(tab);
		generateDataValueSource(out, statVS);
		out.write(linefeed);
		// variable.getStaticValueSource();
		out.write("}" + linefeed);
	}

	private PublishedVariableDataType helpPublishParameters(WrapperPublishedVariableParameter publishParameters) {
		PublishedVariableDataType pvdt = new PublishedVariableDataType();
		pvdt.setAttributeId(publishParameters.getAttributeId());
		pvdt.setDeadbandType(publishParameters.getDeadbandType());
		pvdt.setDeadbandValue(publishParameters.getDeadbandValue());
		pvdt.setIndexRange(publishParameters.getIndexRange());
		pvdt.setMetaDataProperties(publishParameters.getMetaDataProperties());
		pvdt.setPublishedVariable(publishParameters.getPublishedVariable());
		pvdt.setSamplingIntervalHint(publishParameters.getSamplingIntervalHint());
		pvdt.setSubstituteValue(publishParameters.getSubstituteValue());
		return pvdt;
	}

	private ConfigurationVersionDataType helpConfigurationVersion(WrapperConfigurationVersion version) {
		ConfigurationVersionDataType cv = new ConfigurationVersionDataType();
		cv.setMajorVersion(version.getMajorVersion());
		cv.setMinorVersion(version.getMinorVersion());
		return cv;
	}

	/**
	 * print a UA_DataValue struct in ansi c: typedef struct { UA_Variant value;
	 * UA_DateTime sourceTimestamp; UA_DateTime serverTimestamp; UA_UInt16
	 * sourcePicoseconds; UA_UInt16 serverPicoseconds; UA_StatusCode status;
	 * UA_Boolean hasValue : 1; UA_Boolean hasStatus : 1; UA_Boolean
	 * hasSourceTimestamp : 1; UA_Boolean hasServerTimestamp : 1; UA_Boolean
	 * hasSourcePicoseconds : 1; UA_Boolean hasServerPicoseconds : 1; }
	 * UA_DataValue;
	 *
	 * @param out
	 * @param value
	 */
	private void generateDataValueSource(BufferedWriter out, DataValue dvalue) throws IOException {
		out.write("{" + linefeed);
		Variant value = null;
		DateTime sourceTimeStamp = null;
		DateTime serverTimeStamp = null;
		UnsignedShort sourcePico = null;
		UnsignedShort serverPico = null;
		StatusCode status = null;
		Boolean hasValue = null;
		Boolean hasStatus = null;
		Boolean hasSourceTimestamp = null;
		Boolean hasServerTimestamp = null;
		Boolean hasSourcePicoseconds = null;
		Boolean hasServerPicoseconds = null;
		if (dvalue != null) {
			value = dvalue.getValue();
			sourceTimeStamp = dvalue.getSourceTimestamp();
			serverTimeStamp = dvalue.getServerTimestamp();
			sourcePico = dvalue.getSourcePicoseconds();
			serverPico = dvalue.getServerPicoseconds();
			status = dvalue.getStatusCode();
			hasValue = value != null;
			hasStatus = status != null;
			hasSourceTimestamp = sourceTimeStamp != null;
			hasServerTimestamp = serverTimeStamp != null;
			hasSourcePicoseconds = sourcePico != null;
			hasServerPicoseconds = serverPico != null;
		}
		// TODO: UNCOMMENT
//		AddressSpaceNodeModelFactoryC.helpVariantC(out, value, 1, opcServer.getNamespaceUris(),
//				opcServer.getNamespaceUris());
		out.write("," + linefeed);
		AddressSpaceNodeModelFactoryC.helpDateTimeC(out, sourceTimeStamp);
		out.write("," + linefeed);

		AddressSpaceNodeModelFactoryC.helpDateTimeC(out, serverTimeStamp);
		out.write("," + linefeed);

		AddressSpaceNodeModelFactoryC.helpUnsignedShortC(out, sourcePico);
		out.write("," + linefeed);

		AddressSpaceNodeModelFactoryC.helpUnsignedShortC(out, serverPico);
		out.write("," + linefeed);

		AddressSpaceNodeModelFactoryC.helpStatusCodeC(out, status);
		out.write("," + linefeed);

		AddressSpaceNodeModelFactoryC.helpBooleanValue(out, hasValue);
		out.write("," + linefeed);

		AddressSpaceNodeModelFactoryC.helpBooleanValue(out, hasStatus);
		out.write("," + linefeed);

		AddressSpaceNodeModelFactoryC.helpBooleanValue(out, hasSourceTimestamp);
		out.write("," + linefeed);

		AddressSpaceNodeModelFactoryC.helpBooleanValue(out, hasServerTimestamp);
		out.write("," + linefeed);

		AddressSpaceNodeModelFactoryC.helpBooleanValue(out, hasSourcePicoseconds);
		out.write("," + linefeed);

		AddressSpaceNodeModelFactoryC.helpBooleanValue(out, hasServerPicoseconds);
		out.write("," + linefeed);

		out.write("}" + linefeed);
	}

	/**
	 * print a UA_ConfigurationVersionDataType struct in ansi c: typedef struct {
	 * UA_UInt32 majorVersion; UA_UInt32 minorVersion; }
	 * UA_ConfigurationVersionDataType;
	 *
	 * like: { 0.0, 0.0 } or with real values { 10.0, 100.0 }
	 * 
	 * @state: do review
	 * 
	 * @param out
	 * @param configVersion
	 * @throws IOException
	 */
	private void helpConfigurationData(BufferedWriter out, ConfigurationVersionDataType configVersion)
			throws IOException {
		out.write("{" + linefeed);
		AddressSpaceNodeModelFactoryC.helpUnsignedInteger(out, configVersion.getMajorVersion());
		out.write("," + linefeed);
		AddressSpaceNodeModelFactoryC.helpUnsignedInteger(out, configVersion.getMinorVersion());
		out.write(linefeed);
		out.write("}" + linefeed);

	}

	/**
	 * print a UA_PublishedVariableDataType struct in ansi c: typedef struct {
	 * UA_NodeId publishedVariable; UA_UInt32 attributeId; UA_Double
	 * samplingIntervalHint; UA_UInt32 deadbandType; UA_Double deadbandValue;
	 * UA_String indexRange; UA_Variant substituteValue; size_t
	 * metaDataPropertiesSize; UA_QualifiedName *metaDataProperties; }
	 * UA_PublishedVariableDataType;
	 *
	 * like: { UA_NODEID_NULL, 0, 0.0, 0, 0.0, {0, (unsigned char *)""}, { NULL,
	 * UA_VARIANT_DATA, 0, NULL, 0, NULL }, 0, NULL }
	 *
	 */
	private void generatePublishedVariableDataSource(BufferedWriter out, PublishedVariableDataType variable)
			throws IOException {

		out.write("{" + linefeed);
		NodeId nid = null;
		UnsignedInteger attID = null;
		Double samplingHint = null;
		UnsignedInteger deadbandType = null;
		Double deadbandValue = null;
		String indexRange = null;
		Variant substituteValue = null;
		int metaDataPropertiesSize = 0;
		QualifiedName[] metaDataProperties = null;
		if (variable != null) {
			nid = variable.getPublishedVariable();
			attID = variable.getAttributeId();
			samplingHint = variable.getSamplingIntervalHint();
			deadbandType = variable.getDeadbandType();
			deadbandValue = variable.getDeadbandValue();
			indexRange = variable.getIndexRange();
			substituteValue = variable.getSubstituteValue();
			metaDataProperties = variable.getMetaDataProperties();
			if (metaDataProperties != null)
				metaDataPropertiesSize = metaDataProperties.length;
		}

		AddressSpaceNodeModelFactoryC.helpNodeIdC(out, opcServer.getNamespaceUris(), opcServer.getNamespaceUris(), nid);
		out.write("," + linefeed);
		AddressSpaceNodeModelFactoryC.helpUnsignedInteger(out, attID);
		out.write("," + linefeed);
		AddressSpaceNodeModelFactoryC.helpDouble(out, samplingHint);
		out.write("," + linefeed);

		AddressSpaceNodeModelFactoryC.helpUnsignedInteger(out, deadbandType);
		out.write("," + linefeed);

		AddressSpaceNodeModelFactoryC.helpDouble(out, deadbandValue);
		out.write("," + linefeed);

		AddressSpaceNodeModelFactoryC.helpString(out, indexRange);
		out.write("," + linefeed);

		// TODO: UNCOMMENT
//		AddressSpaceNodeModelFactoryC.helpVariantC(out, substituteValue, 1, opcServer.getNamespaceUris(),
//				opcServer.getNamespaceUris());
		out.write("," + linefeed);

		out.write(metaDataPropertiesSize + "," + linefeed);

		AddressSpaceNodeModelFactoryC.helpQualifiedNamesC(out, metaDataProperties);

		out.write("}" + linefeed);

	}

	/**
	 * Wraps connection address to OPC UA networkinterface datatype.
	 * 
	 * @param address wrapped object
	 * 
	 * @return variant with networkinterface datatype
	 */
	private Variant helpConnectionAddress(WrapperConnectionAddress address) {
		if (address == null) {
			return null;
		}

		NetworkAddressUrlDataType naudt = new NetworkAddressUrlDataType();
		naudt.setNetworkInterface(naudt.getNetworkInterface());
		naudt.setUrl(naudt.getUrl());
		return new Variant(naudt);
	}

	/**
	 * Wraps connection transport settings to OPC UA connectiontransportsetting
	 * datatype.
	 * 
	 * @param connectionTransportSettings wrapped object
	 * 
	 * @return variant with connectiontransportsetting
	 */
	private Variant helpConnectionTransportSettings(WrapperConnectionTransportSetting settings) {
		if (settings == null) {
			return null;
		}
		ConnectionTransportDataType con = null;

		switch (settings.getType()) {
		case Broker:
			con = new BrokerConnectionTransportDataType();
			((BrokerConnectionTransportDataType) con)
					.setAuthenticationProfileUri(settings.getAuthenticationProfileUri());
			((BrokerConnectionTransportDataType) con).setResourceUri(settings.getResourceUri());
			break;
		case Datagram:
			con = new DatagramConnectionTransportDataType();
			((DatagramConnectionTransportDataType) con).setDiscoveryAddress(settings.getDiscoveryAddress());
			break;

		}

		return new Variant(con);
	}

	/**
	 * print a publisherId in ansi c:
	 * 
	 * @param out
	 * @param publisherId
	 * @throws IOException
	 */
	private void helpPublisherId(BufferedWriter out, Object publisherId) throws IOException {
		if (publisherId instanceof String) {
			out.write(".publisherId.string = ");
			AddressSpaceNodeModelFactoryC.helpString(out, publisherId.toString());
		} else if (publisherId instanceof Integer) {
			out.write(".publisherId.numeric = ");
			AddressSpaceNodeModelFactoryC.helpInteger(out, (Integer) publisherId);
		}
	}

	/**
	 * print a UA_PublishedVariableDataType struct in ansi c: typedef struct {
	 * UA_QualifiedName key; UA_Variant value; } UA_KeyValuePair;
	 *
	 * @param out
	 * @param keys
	 * @throws IOException
	 */
	private void helpKeyValuePairs(BufferedWriter out, KeyValuePair[] keys) throws IOException {
		if (keys == null || keys.length == 0) {
			out.write("NULL");
			return;
		}
		String comma = "";
		out.write("(UA_KeyValuePair[" + keys.length + "]){" + linefeed);
		for (KeyValuePair keyv : keys) {
			out.write(comma);
			out.write("{" + linefeed);
			AddressSpaceNodeModelFactoryC.helpQualifiedNameC(out, keyv.getKey());
			out.write("," + linefeed);
			// TODO: UNCOMMENT
//			AddressSpaceNodeModelFactoryC.helpVariantC(out, keyv.getValue(), 1, opcServer.getNamespaceUris(),
//					opcServer.getNamespaceUris());
			out.write(linefeed);
			out.write(tab + "}" + linefeed);
			comma = ",";
		}
		out.write(tab + "}" + linefeed);
	}

	/**
	 * typedef struct { UA_ExtensionObjectEncoding encoding; union { struct {
	 * UA_NodeId typeId; /* The nodeid of the datatype * UA_ByteString body; /* The
	 * bytestring of the encoded data * } encoded; struct { const UA_DataType *type;
	 * void *data; } decoded; } content; } UA_ExtensionObject;
	 * 
	 * @param out
	 * @param obj
	 * @throws IOException
	 */
	private void helpWriterGroupTransportData(BufferedWriter out, WriterGroupTransportDataType obj) throws IOException {
		out.write("{" + linefeed);
		out.write(tab + "UA_EXTENSIONOBJECT_DECODED");
		out.write(", " + linefeed); // set encoding
		if (obj instanceof DatagramWriterGroupTransportDataType) {
			// not implemented yet
			// helpDataGramWriterGroupTransportDataSource(out,
			// (DatagramWriterGroupTransportDataType)obj);
		} else if (obj instanceof BrokerWriterGroupTransportDataType) {
			helpBrokerWriterGroupTransportDataSource(out, (BrokerWriterGroupTransportDataType) obj);
		}
		out.write("}" + linefeed);
	}

	/**
	 * typedef struct { UA_ExtensionObjectEncoding encoding; union { struct {
	 * UA_NodeId typeId; /* The nodeid of the datatype * UA_ByteString body; /* The
	 * bytestring of the encoded data * } encoded; struct { const UA_DataType *type;
	 * void *data; } decoded; } content; } UA_ExtensionObject;
	 * 
	 * @param out
	 * @param obj
	 * @throws IOException
	 */
	private void helpWriterGroupMessageData(BufferedWriter out, WriterGroupMessageDataType obj) throws IOException {
		if (obj == null) {
			out.write("NULL");
			return;
		}
		out.write("{" + linefeed);
		out.write(tab + "UA_EXTENSIONOBJECT_DECODED, " + linefeed); // set encoding

		if (obj instanceof UadpWriterGroupMessageDataType) {
			helpUadpWriterGroupMessageDataSource(out, (UadpWriterGroupMessageDataType) obj);
		} else if (obj instanceof JsonWriterGroupMessageDataType) {
			out.write("NULL");
			// not implemented yet
			helpJsonWriterGroupMessageDataSource(out, (JsonWriterGroupMessageDataType) obj);
		}
		out.write("}" + linefeed);
	}

	/**
	 * typedef struct { UA_UInt32 groupVersion; UA_DataSetOrderingType
	 * dataSetOrdering; UA_UadpNetworkMessageContentMask networkMessageContentMask;
	 * UA_Double samplingOffset; size_t publishingOffsetSize; UA_Double
	 * *publishingOffset; } UA_UadpWriterGroupMessageDataType;
	 * 
	 * @param out
	 * @param group
	 * @throws IOException
	 */
	private void helpUadpWriterGroupMessageDataSource(BufferedWriter out, UadpWriterGroupMessageDataType group)
			throws IOException {
		out.write(".content.decoded = {" + linefeed);
		out.write("&UA_TYPES[UA_TYPES_UADPWRITERGROUPMESSAGEDATATYPE]," + linefeed);
		out.write("(UA_UadpWriterGroupMessageDataType[1]){{" + linefeed);
		out.write(tab);
		AddressSpaceNodeModelFactoryC.helpUnsignedInteger(out, group.getGroupVersion());
		out.write("," + linefeed);
		if (group.getDataSetOrdering() == null)
			out.write("0");
		else
			out.write(tab + group.getDataSetOrdering().getValue());
		out.write("," + linefeed);
		AddressSpaceNodeModelFactoryC.helpUnsignedInteger(out, group.getNetworkMessageContentMask());
		out.write("," + linefeed);
		AddressSpaceNodeModelFactoryC.helpDouble(out, group.getSamplingOffset());
		out.write("," + linefeed);
		if (group.getPublishingOffset() == null)
			out.write("0");
		else
			out.write(Integer.toString(group.getPublishingOffset().length));
		out.write("," + linefeed);
		AddressSpaceNodeModelFactoryC.helpDoubleArray(out, group.getPublishingOffset());
		out.write(linefeed);
		out.write("}}" + linefeed);
		out.write("}" + linefeed);
	}

	/**
	 * TODO not yet implemented
	 * 
	 * @param out
	 * @param group
	 * @throws IOException
	 */
	private void helpJsonWriterGroupMessageDataSource(BufferedWriter out, JsonWriterGroupMessageDataType group)
			throws IOException {
		out.write(".content.decode = {");
		out.write("}" + linefeed);
	}

	/**
	 * typedef struct { UA_String queueName; UA_String resourceUri; UA_String
	 * authenticationProfileUri; UA_BrokerTransportQualityOfService
	 * requestedDeliveryGuarantee; } UA_BrokerWriterGroupTransportDataType;
	 * 
	 * @param out
	 * @param group
	 * @throws IOException
	 */
	private void helpBrokerWriterGroupTransportDataSource(BufferedWriter out, BrokerWriterGroupTransportDataType group)
			throws IOException {
		String queueName = null;
		String resourceUri = null;
		String authenticationProfileUri = null;
		BrokerTransportQualityOfService requestedDeliveryGuarantee = BrokerTransportQualityOfService.NotSpecified;

		if (group != null) {
			queueName = group.getQueueName();
			resourceUri = group.getResourceUri();
			authenticationProfileUri = group.getAuthenticationProfileUri();
			requestedDeliveryGuarantee = group.getRequestedDeliveryGuarantee();
		}

		out.write(".content.decoded = {" + linefeed);
		out.write("&UA_TYPES[UA_TYPES_BROKERWRITERGROUPTRANSPORTDATATYPE]," + linefeed);
		out.write("(UA_BrokerWriterGroupTransportDataType[1]){{" + linefeed);
		out.write(tab);
		AddressSpaceNodeModelFactoryC.helpString(out, queueName);
		out.write("," + linefeed);
		out.write(tab);
		AddressSpaceNodeModelFactoryC.helpString(out, resourceUri);
		out.write("," + linefeed);
		AddressSpaceNodeModelFactoryC.helpString(out, authenticationProfileUri);
		out.write("," + linefeed);
		out.write(tab);
		if (requestedDeliveryGuarantee == null)
			out.write("0");
		else
			out.write(Integer.toString(requestedDeliveryGuarantee.getValue()));
		out.write(linefeed);
		out.write("}}" + linefeed);
		out.write("}" + linefeed);
		out.write(linefeed);
	}

	/**
	 * 
	 * @param out
	 * @param obj
	 * @throws IOException
	 */
	private void helpDataSetWriterTransportData(BufferedWriter out, DataSetWriterTransportDataType obj)
			throws IOException {
		out.write("{" + linefeed);
		out.write(tab + "UA_EXTENSIONOBJECT_DECODED, " + linefeed); // set encoding
		if (obj instanceof BrokerDataSetWriterTransportDataType)
			helpBrokerDataSetWriterTransportData(out, (BrokerDataSetWriterTransportDataType) obj);
		out.write("}" + linefeed);
	}

	/**
	 * typedef struct { UA_String queueName; UA_String resourceUri; UA_String
	 * authenticationProfileUri; UA_BrokerTransportQualityOfService
	 * requestedDeliveryGuarantee; UA_String metaDataQueueName; UA_Double
	 * metaDataUpdateTime; } UA_BrokerDataSetWriterTransportDataType;
	 * 
	 * @param out
	 * @param obj
	 * @throws IOException
	 */
	private void helpBrokerDataSetWriterTransportData(BufferedWriter out, BrokerDataSetWriterTransportDataType obj)
			throws IOException {
		String queueName = null;
		String resourceUri = null;
		String authenticationProfileUri = null;
		BrokerTransportQualityOfService requestedDeliveryGuarantee = BrokerTransportQualityOfService.NotSpecified;
		String metaDataQueueName = null;
		Double metaDataUpdateTime = null;
		if (obj != null) {
			queueName = obj.getQueueName();
			resourceUri = obj.getResourceUri();
			authenticationProfileUri = obj.getAuthenticationProfileUri();
			requestedDeliveryGuarantee = obj.getRequestedDeliveryGuarantee();
			metaDataQueueName = obj.getMetaDataQueueName();
			metaDataUpdateTime = obj.getMetaDataUpdateTime();
		}

		out.write(".content.decoded = {" + linefeed);
		out.write("&UA_TYPES[UA_TYPES_BROKERDATASETWRITERTRANSPORTDATATYPE]," + linefeed);
		out.write("(UA_BrokerDataSetWriterTransportDataType[1]){{" + linefeed);
		out.write(tab);
		AddressSpaceNodeModelFactoryC.helpString(out, queueName);
		out.write("," + linefeed);
		AddressSpaceNodeModelFactoryC.helpString(out, resourceUri);
		out.write("," + linefeed);
		AddressSpaceNodeModelFactoryC.helpString(out, authenticationProfileUri);
		out.write("," + linefeed);
		out.write(tab);
		if (requestedDeliveryGuarantee == null)
			out.write("0");
		else
			out.write(Integer.toString(requestedDeliveryGuarantee.getValue()));
		out.write("," + linefeed);
		AddressSpaceNodeModelFactoryC.helpString(out, metaDataQueueName);
		out.write("," + linefeed);
		AddressSpaceNodeModelFactoryC.helpDouble(out, metaDataUpdateTime);
		out.write("}}" + linefeed);
		out.write("}" + linefeed);
		out.write(linefeed);
	}

	/**
	 * 
	 * @param out
	 * @param obj
	 * @throws IOException
	 */
	private void helpDataSetWriterMessageData(BufferedWriter out, DataSetWriterMessageDataType obj) throws IOException {
		out.write("{" + linefeed);
		out.write(tab + "UA_EXTENSIONOBJECT_DECODED, " + linefeed); // set encoding
		if (obj instanceof UadpDataSetWriterMessageDataType)
			helpUadpDataSetWriterMessageData(out, (UadpDataSetWriterMessageDataType) obj);
		else if (obj instanceof JsonDataSetWriterMessageDataType)
			helpJsonDataSetWriterMessageData(out, (JsonDataSetWriterMessageDataType) obj);
		else
			helpEmptyDataSetWriterMessageData(out);
		out.write("}" + linefeed);
	}

	/**
	 * typedef struct { UA_UadpDataSetMessageContentMask dataSetMessageContentMask;
	 * UA_UInt16 configuredSize; UA_UInt16 networkMessageNumber; UA_UInt16
	 * dataSetOffset; } UA_UadpDataSetWriterMessageDataType;
	 * 
	 * @param out
	 * @param obj
	 * @throws IOException
	 */
	private void helpUadpDataSetWriterMessageData(BufferedWriter out, UadpDataSetWriterMessageDataType obj)
			throws IOException {

		UnsignedInteger dataSetMessageContentMask = null;
		UnsignedShort configuredSize = null;
		UnsignedShort networkMessageNumber = null;
		UnsignedShort dataSetOffset = null;

		if (obj != null) {
			dataSetMessageContentMask = obj.getDataSetMessageContentMask();
			configuredSize = obj.getConfiguredSize();
			networkMessageNumber = obj.getNetworkMessageNumber();
			dataSetOffset = obj.getDataSetOffset();
		}
		out.write(".content.decoded = {" + linefeed);
		out.write("&UA_TYPES[UA_TYPES_UADPDATASETWRITERMESSAGEDATATYPE]," + linefeed);
		out.write("(UA_UadpDataSetWriterMessageDataType[1]){{" + linefeed);
		AddressSpaceNodeModelFactoryC.helpUnsignedInteger(out, dataSetMessageContentMask);
		out.write("," + linefeed);
		AddressSpaceNodeModelFactoryC.helpUnsignedShortC(out, configuredSize);
		out.write("," + linefeed);
		AddressSpaceNodeModelFactoryC.helpUnsignedShortC(out, networkMessageNumber);
		out.write("," + linefeed);
		AddressSpaceNodeModelFactoryC.helpUnsignedShortC(out, dataSetOffset);
		out.write(linefeed);
		out.write("}}" + linefeed);
		out.write("}" + linefeed);
		out.write(tab);
	}

	/**
	 * typedef struct { UA_UadpDataSetMessageContentMask dataSetMessageContentMask;
	 * UA_UInt16 configuredSize; UA_UInt16 networkMessageNumber; UA_UInt16
	 * dataSetOffset; } UA_UadpDataSetWriterMessageDataType;
	 * 
	 * @param out
	 * @param obj
	 * @throws IOException
	 */
	private void helpJsonDataSetWriterMessageData(BufferedWriter out, JsonDataSetWriterMessageDataType obj)
			throws IOException {

		UnsignedInteger dataSetMessageContentMask = null;

		if (obj != null) {
			dataSetMessageContentMask = obj.getDataSetMessageContentMask();
		}

		out.write(".content.decoded = {" + linefeed);
		out.write("&UA_TYPES[UA_TYPES_JSONDATASETWRITERMESSAGEDATATYPE]," + linefeed);
		out.write("(UA_UadpDataSetWriterMessageDataType[1]){{" + linefeed);
		AddressSpaceNodeModelFactoryC.helpUnsignedInteger(out, dataSetMessageContentMask);
		out.write(linefeed);
		out.write("}}" + linefeed);
		out.write("}" + linefeed);
		out.write(tab);
	}

	/**
	 * create an empty datasetwritermessage structure
	 * 
	 * @param out
	 * @throws IOException
	 */
	private void helpEmptyDataSetWriterMessageData(BufferedWriter out) throws IOException {
		out.write(".content.decoded = {" + linefeed);
		out.write("&UA_TYPES[UA_TYPES_JSONDATASETWRITERMESSAGEDATATYPE]," + linefeed);
		out.write("(UA_UadpDataSetWriterMessageDataType[1]){{" + linefeed);
		out.write("}}" + linefeed);
		out.write("}" + linefeed);
		out.write(tab);
	}

	/******************************************************************************************
	 * 
	 * start here all demo data
	 * 
	 * *****************************************************************************************
	 * 
	 * @param conn
	 */
	private void generateDemoConnection1(PubSubConnection conn) {

		WrapperConnectionAddress address = new WrapperConnectionAddress();
		address.setUrl("opc.udp://192.68.0.104:5555/");
		conn.setAddress(address);
		conn.setConfigurationFrozen(true);
		List<KeyValuePair> props = new ArrayList<KeyValuePair>();
		props.add(new KeyValuePair(new QualifiedName(22, "halihalo"), new Variant(555)));
		props.add(new KeyValuePair(new QualifiedName(1, "noch eins"),
				new Variant(new UnsignedInteger[] { new UnsignedInteger(22), new UnsignedInteger(33) })));
		conn.setConnectionProperties(WrapperKeyValuePair.wrap(props.toArray(new KeyValuePair[0])));

		WrapperConnectionTransportSetting settings = new WrapperConnectionTransportSetting();
		settings.setType(TransportSettingType.Broker);
		settings.setResourceUri("opc.udp://192.68.0.104:5555/");

		conn.setConnectionTransportSettings(settings);
		conn.setEnabled(true);
		conn.setName("my_test_connection_1");
		conn.setPublisherId("TODO");
		conn.setPublisherIdType(PubSubPublisherIdType.UA_PUBSUB_PUBLISHERID_STRING);
		conn.setTransportProfileUri("http://opcfoundation.org/UA-Profile/Transport/pubsub-udp-uadp");

		generateDemoWriterGroup(conn);
	}

	private void generateDemoConnection2(PubSubConnection conn) {

		WrapperConnectionAddress address = new WrapperConnectionAddress();
		address.setUrl("opc.udp://192.68.0.104:5556/");
		conn.setAddress(address);
		conn.setConfigurationFrozen(true);
		List<KeyValuePair> props = new ArrayList<KeyValuePair>();
		props.add(new KeyValuePair(new QualifiedName(22, "halihalo"), new Variant(555)));
		props.add(new KeyValuePair(new QualifiedName(1, "noch eins"),
				new Variant(new UnsignedInteger[] { new UnsignedInteger(22), new UnsignedInteger(33) })));

		conn.setConnectionProperties(WrapperKeyValuePair.wrap(props.toArray(new KeyValuePair[0])));

		WrapperConnectionTransportSetting settings = new WrapperConnectionTransportSetting();
		settings.setType(TransportSettingType.Broker);
		settings.setResourceUri("opc.udp://192.68.0.104:5556/");

		conn.setConnectionTransportSettings(settings);
		conn.setEnabled(true);
		conn.setName("my_test_connection_2");
		conn.setPublisherId(555);
		conn.setPublisherIdType(PubSubPublisherIdType.UA_PUBSUB_PUBLISHERID_NUMERIC);
		conn.setTransportProfileUri("http://opcfoundation.org/UA-Profile/Transport/pubsub-udp-uadp");
	}

	private void generateDemoWriterGroup(PubSubConnection conn) {
		PubSubWriterGroup wg = new PubSubWriterGroup();
		conn.getChildren().add(wg);
		wg.setConfigurationFrozen(true);
		wg.setEnabled(true);
		wg.setEncodingMimeType(PubSubEncodingType.UA_PUBSUB_ENCODING_BINARY);
		wg.setGroupProperties(WrapperKeyValuePair.wrap(new KeyValuePair[] {
				new KeyValuePair(new QualifiedName("ich"), new Variant(new UnsignedInteger(56))) }));
		wg.setKeepAliveTime(125.6);
		wg.setMaxEncapsulatedDataSetMessageCount(44);
		wg.setName("hannes");
		wg.setPriority((byte) 44);
		wg.setPublishingInterval(222.9);
		wg.setRtLevel(PubSubRTLevel.UA_PUBSUB_RT_DETERMINISTIC);
		wg.setSecurityMode(MessageSecurityMode.None);
		wg.setWriterGroupId(33);

		WrapperWriterGroupTransport wwgt = new WrapperWriterGroupTransport();
		wwgt.setType(TransportSettingType.Broker);
		wwgt.setAuthenticationProfileUri("AuthenticationProfileUri");
		wwgt.setQueueName("myqueue");
		wwgt.setRequestedDeliveryGuarantee(BrokerTransportQualityOfService.AtLeastOnce);
		wwgt.setResourceUri("my_ResourceUri");
		wg.setTransportSettings(wwgt);
		// BrokerWriterGroupTransportDataType wgdt = new
		// BrokerWriterGroupTransportDataType();
		// wgdt.setAuthenticationProfileUri("AuthenticationProfileUri");
		// wgdt.setQueueName("myqueue");
		// wgdt.setRequestedDeliveryGuarantee(BrokerTransportQualityOfService.AtLeastOnce);
		// wgdt.setResourceUri("my_ResourceUri");
		// wg.setTransportSettings(wgdt);
		WrapperWriterGroupMessage wwgm = new WrapperWriterGroupMessage();
		wwgm.setDataSetOrdering(DataSetOrderingType.AscendingWriterId);
		wwgm.setGroupVersion(new UnsignedInteger(55));
		wg.setMessageSettings(wwgm);
		// UadpWriterGroupMessageDataType wgmdt = new UadpWriterGroupMessageDataType();
		// wgmdt.setDataSetOrdering(DataSetOrderingType.AscendingWriterId);
		// wgmdt.setGroupVersion(new UnsignedInteger(55));
		// wg.setMessageSettings(wgmdt);
		generateDemoWriter(wg);
	}

	private void generateDemoWriter(PubSubWriterGroup writer) {
		PubSubDataSetWriter w = new PubSubDataSetWriter();
		writer.getChildren().add(w);
		w.setDataSetFieldContentMask(DataSetFieldContentMask.UA_DATASETFIELDCONTENTMASK_NONE);
		w.setConfigurationFrozen(true);
		w.setDataSetName("TestDataset");
		w.setDataSetWriterId(195);
		w.setDataSetWriterProperties(WrapperKeyValuePair
				.wrap(new KeyValuePair[] { new KeyValuePair(new QualifiedName("mama"), new Variant("meine")) }));

		w.setKeyFrameCount(122);
		w.setName("hannes");

		// UadpDataSetWriterMessageDataType msSett = new
		// UadpDataSetWriterMessageDataType();
		// w.setMessageSettings(msSett);
		WrapperWriterDataSetMessage wwdsm = new WrapperWriterDataSetMessage();
		wwdsm.setType(MessageSettingType.UADP);
		w.setMessageSettings(wwdsm);

		// BrokerDataSetWriterTransportDataType bwtrans = new
		// BrokerDataSetWriterTransportDataType();
		// w.setTransportSettings(bwtrans);
		WrapperWriterDataSetTransport wwdst = new WrapperWriterDataSetTransport();
		w.setTransportSettings(wwdst);
	}

	private void generateDemoPublishedDataSet(PubSubPublishedDataSet ds) {
		PubSubPublishedDataItemsTemplate items = new PubSubPublishedDataItemsTemplate();

//		DataSetMetaDataType meta = new DataSetMetaDataType();
//		meta.setConfigurationVersion(new ConfigurationVersionDataType(new UnsignedInteger(1), new UnsignedInteger(44)));
//		meta.setDataSetClassId(new UUID(44l, 55l));
//		meta.setDescription(new LocalizedText("testdescription"));
//		meta.setEnumDataTypes(new EnumDescription[] { new EnumDescription(new NodeId(2, "nanna"),
//				new QualifiedName("test"), new EnumDefinition(), new UnsignedByte(2)) });
		WrapperDataSetMetaData meta = new WrapperDataSetMetaData();
		WrapperConfigurationVersion wcv = new WrapperConfigurationVersion();
		wcv.setMinorVersion(new UnsignedInteger(1));
		wcv.setMajorVersion(new UnsignedInteger(44));

		meta.setConfigurationVersion(wcv);
		meta.setDataSetClassId(new UUID(44l, 55l));
		meta.setDescription(new LocalizedText("testdescription"));
		// TODO: Check if EnumDataTypes are not needed
//		meta.setEnumDataTypes(new EnumDescription[] { new EnumDescription(new NodeId(2, "nanna"),
//				new QualifiedName("test"), new EnumDefinition(), new UnsignedByte(2)) });

//		FieldMetaData metaData = new FieldMetaData();
//		metaData.setArrayDimensions(null);
//		metaData.setBuiltInType(new UnsignedByte(55));
//		metaData.setDataSetFieldId(new UUID(44l, 66l));
//		metaData.setDataType(new NodeId(3, 55));
//		metaData.setDescription(new LocalizedText("ttt"));
//		metaData.setFieldFlags(new UnsignedShort(6));
//		metaData.setMaxStringLength(new UnsignedInteger(44));
//		metaData.setName("name2");
//		metaData.setValueRank(ValueRanks.Scalar.getValue());
//		metaData.setProperties(new KeyValuePair[] {
//				new KeyValuePair(new QualifiedName("qualname"), new Variant(new UnsignedShort(33))) });

		WrapperFieldMetaData metaData = new WrapperFieldMetaData();
		metaData.setArrayDimensions(null);
		metaData.setBuiltInType(new UnsignedByte(55));
		metaData.setDataSetFieldId(new UUID(44l, 66l));
		metaData.setDataType(new NodeId(3, 55));
		metaData.setDescription(new LocalizedText("ttt"));
		metaData.setFieldFlags(new UnsignedShort(6));
		metaData.setMaxStringLength(new UnsignedInteger(44));
		metaData.setName("name2");
		metaData.setValueRank(ValueRanks.Scalar.getValue());

		metaData.setProperties(WrapperKeyValuePair.wrap(new KeyValuePair[] {
				new KeyValuePair(new QualifiedName("qualname"), new Variant(new UnsignedShort(33))) }));

		meta.setFields(new WrapperFieldMetaData[] { metaData });
		items.setMetaData(meta);
		items.setVariablesToAdd(new WrapperPublishedVariable[] { new WrapperPublishedVariable() });
		ds.setConfig(items);
		ds.setConfigurationFrozen(true);
		ds.setName("TestDataset");
		ds.setPublishedDataSetType(PubSubPublishedDataSetType.UA_PUBSUB_DATASET_PUBLISHEDITEMS_TEMPLATE);
	}
}
