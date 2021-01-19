package com.bichler.astudio.device.opcua.handler;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

//import org.eclipse.core.runtime.IPath;
//import org.eclipse.core.runtime.Path;
import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.common.NamespaceTable;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.BrowseDescription;
import org.opcfoundation.ua.core.BrowseDirection;
import org.opcfoundation.ua.core.BrowseResult;
import org.opcfoundation.ua.core.BrowseResultMask;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.core.ReferenceDescription;
import org.opcfoundation.ua.core.ReferenceNode;
import org.xml.sax.SAXException;

import com.bichler.astudio.filesystem.IFileSystem;
import com.bichler.astudio.opcua.addressspace.model.binary.AddressSpaceNodeModelFactory;
import com.bichler.astudio.opcua.addressspace.model.binary.CompileFactory;
import com.bichler.astudio.opcua.nodes.OPCUAServerDriverModelNode;
import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.opcua.widget.model.AdvancedConfigurationNode;
import com.bichler.astudio.opcua.widget.model.AdvancedConfigurationNodeParser;
import com.bichler.astudio.opcua.widget.model.AdvancedRootConfigurationNode;
import com.bichler.astudio.opcua.widget.model.AdvancedSectionType;

import opc.sdk.core.node.Node;
import opc.sdk.core.node.UAVariableNode;
import opc.sdk.core.node.UAVariableTypeNode;

public class CompileHandlerUtil {

	private static final Logger logger = Logger.getLogger(CompileHandler.class.getName());
	
	public static NamespaceTable createNamespaceTableToExport(NamespaceTable serverNS, Set<Integer> collection) {
		NamespaceTable exportTable = new NamespaceTable();
		for (Integer i : collection) {
			String uri = serverNS.getUri(i);
			exportTable.add(uri);
		}
		return exportTable;
	}
	
	public static void fetchNamespaceIndexFromNode(Node node, NamespaceTable nsTable, Set<Integer> collection) {

		NodeId nodeId = node.getNodeId();
		matchIndizes(collection, nodeId);
		NodeClass nodeClass = node.getNodeClass();

		NodeId dataTypeId = null;
		switch (nodeClass) {
		case Variable:
			dataTypeId = ((UAVariableNode) node).getDataType();
			matchIndizes(collection, dataTypeId);
			break;
		case VariableType:
			dataTypeId = ((UAVariableTypeNode) node).getDataType();
			matchIndizes(collection, dataTypeId);
			break;
		default:
			break;
		}

		ReferenceNode[] references = node.getReferences();
		if (references != null) {
			for (ReferenceNode reference : references) {
				NodeId refTypId = reference.getReferenceTypeId();
				matchIndizes(collection, refTypId);

				ExpandedNodeId targetId = reference.getTargetId();
				try {
					NodeId targetNodeId = nsTable.toNodeId(targetId);
					matchIndizes(collection, targetNodeId);
				} catch (ServiceResultException e) {
					logger.log(Level.SEVERE, e.getMessage());
				}
			}
		}

	}

	private static void matchIndizes(Set<Integer> collection, NodeId nodeId) {
		if (NodeId.isNull(nodeId)) {
			return;
		}

		Integer index = nodeId.getNamespaceIndex();
		collection.add(index);
	}
	
	public static void fetchDeviceModel(Node node, Map<Integer, List<Node>> allNodes, List<NodeId> already, NamespaceTable nsTable,
			Set<Integer> collection) {
		// add node
		if (already.contains(node.getNodeId())) {
			return;
		}
		List<Node> nodes = allNodes.get(node.getNodeId().getNamespaceIndex());
		if(nodes == null)
		{
			nodes = new ArrayList<Node>();
			allNodes.put(node.getNodeId().getNamespaceIndex(), nodes);
		}
		nodes.add(node);
		already.add(node.getNodeId());

		fetchNamespaceIndexFromNode(node, nsTable, collection);

		BrowseDescription[] nodesToBrowse = new BrowseDescription[1];
		nodesToBrowse[0] = new BrowseDescription();
		nodesToBrowse[0].setBrowseDirection(BrowseDirection.Forward);
		nodesToBrowse[0].setIncludeSubtypes(true);
		nodesToBrowse[0].setNodeClassMask(NodeClass.ALL);
		nodesToBrowse[0].setNodeId(node.getNodeId());
		nodesToBrowse[0].setReferenceTypeId(Identifiers.HierarchicalReferences);
		nodesToBrowse[0].setResultMask(BrowseResultMask.ALL);

		try {
			BrowseResult[] results = ServerInstance.getInstance().getServerInstance().getMaster().browse(nodesToBrowse,
					UnsignedInteger.ZERO, null, null);
			if (results != null) {
				for (ReferenceDescription result : results[0].getReferences()) {
					ExpandedNodeId targetId = result.getNodeId();
					Node target = ServerInstance.getInstance().getServerInstance().getAddressSpaceManager()
							.getNodeById(targetId);
					if (target == null) {
						continue;
					}
					// fetch
					fetchDeviceModel(target, allNodes, already, nsTable, collection);
				}
			}
		} catch (ServiceResultException e) {
			logger.log(Level.SEVERE, e.getMessage());
		}
	}
	
	/**
	 * Finds all nodes starting from nodeid.
	 * 
	 * @param nodeId
	 *            NodeId to start
	 * @param dNs
	 * @param serverNS
	 * @return
	 */
	public static Map<Integer, List<Node>> findDeviceModel(NodeId nodeId, NamespaceTable nsTable, Set<Integer> collection) {

		Map<Integer, List<Node>> nodes = new HashMap<>();
		List<NodeId> already = new ArrayList<>();

		Node node = ServerInstance.getInstance().getServerInstance().getAddressSpaceManager().getNodeById(nodeId);

		if (node == null) {
			return new HashMap<Integer, List<Node>>();
		}

		fetchDeviceModel(node, nodes, already, nsTable, collection);

		return nodes;
	}
	
	public static void compileDevices(Object[] drivers, NamespaceTable namespaceTable, AddressSpaceNodeModelFactory mf) {
		// create driver jar
		for (Object driver : drivers) {
			IFileSystem filesystem = ((OPCUAServerDriverModelNode) driver).getFilesystem();
			String driverName = ((OPCUAServerDriverModelNode) driver).getDriverName();

			String rootPath = filesystem.getRootPath();
			Path driverPath = Paths.get(rootPath, "drivers", driverName); 
			//IPath driverPath = new Path(rootPath).append("drivers").append(driverName);

			Path deviceconfig = Paths.get(rootPath, "drivers", driverName, "deviceconfig.com");

			if (!filesystem.isFile(deviceconfig.toString())) {
				continue;
			}

			// read device config
			
			try(InputStream in = filesystem.readFile(deviceconfig.toString());) {
				
				SAXParser parser = SAXParserFactory.newInstance().newSAXParser();

				AdvancedRootConfigurationNode root = new AdvancedRootConfigurationNode(
						AdvancedSectionType.DeviceConfig);
				AdvancedConfigurationNodeParser saxHandler = new AdvancedConfigurationNodeParser(namespaceTable, root,
						AdvancedSectionType.DeviceConfig);
				parser.parse(in, saxHandler);

				AdvancedConfigurationNode[] children = root.getChildren();

				if (children.length > 0) {

					Path devicePath = Paths.get(driverPath.toString(), "devices");
					if (!filesystem.isDir(devicePath.toString())) {
						filesystem.addDir(devicePath.toString());
					}

					int fileIndex = 0;
					for (AdvancedConfigurationNode child : children) {
						fileIndex++;
						NodeId deviceId = child.getDeviceId();

						if (NodeId.isNull(deviceId)) {
							continue;
						}

						Path deviceFolder = Paths.get(devicePath.toString(), "device_" + fileIndex);
						if (!filesystem.isDir(deviceFolder.toString())) {
							filesystem.addDir(deviceFolder.toString());
						}

						// find all nodes for the device
						Set<Integer> dList = new HashSet<>();
						Map<Integer, List<Node>> nodes = CompileHandlerUtil.findDeviceModel(deviceId, namespaceTable, dList);
						if (nodes == null || nodes.isEmpty()) {
							continue;
						}

						NamespaceTable exportTable = CompileHandlerUtil.createNamespaceTableToExport(namespaceTable, dList);

						generateJar(deviceFolder.toString(), devicePath.toString(), "device_" + fileIndex, mf,
								exportTable, namespaceTable, nodes);
					}
				}
			} catch (IOException | ParserConfigurationException | SAXException e) {
				logger.log(Level.SEVERE, e.getMessage());
			}
		}
	}
	
	private static void generateJar(String destFolder, String jarPath, String jarName,
			AddressSpaceNodeModelFactory modelFactory, NamespaceTable nsTable, NamespaceTable serverTable,
			Map<Integer, List<Node>> nodes) {

		// TODO: TRY CATCH IS NOT WORKING CORRECT
		try {
			modelFactory.create(null, destFolder, nsTable, serverTable, nodes, false);
		} catch (IOException e) {
			logger.log(Level.SEVERE, e.getMessage());
		}

		try {
			CompileFactory.compile(null, destFolder, jarPath, jarName, AddressSpaceNodeModelFactory.PACKAGENAME);
		} catch (IOException e) {
			logger.log(Level.SEVERE, e.getMessage());
		}
	}
}
