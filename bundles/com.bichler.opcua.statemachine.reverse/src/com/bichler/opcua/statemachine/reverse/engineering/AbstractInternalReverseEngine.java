package com.bichler.opcua.statemachine.reverse.engineering;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.common.NamespaceTable;

import com.bichler.opcua.statemachine.addressspace.importer.ResourceType;
import com.bichler.opcua.statemachine.addressspace.importer.StatemachineNodesetImporter;
import com.bichler.opcua.statemachine.exception.StatemachineException;

import opc.sdk.core.node.Node;

public abstract class AbstractInternalReverseEngine extends AbstractReverseEngine {

	private StatemachineNodesetImporter importer;

	public AbstractInternalReverseEngine() throws StatemachineException {
		super();
		this.importer = new StatemachineNodesetImporter();
		setup();
	}
	
	public void addExternalIdSet(Map<String, Map<NodeId, String>> mapping) {
		for (Entry<String, Map<NodeId, String>> map : mapping.entrySet()) {
			
			for (Entry<NodeId, String> entry : map.getValue().entrySet()) {
				addUMLStaticId(entry.getKey(), entry.getValue(), map.getKey());
			}
		}
	}
	
	public Map<String, Map<NodeId, String>> fetchExternalIdsFromUML(File...files) {
		return this.importer.mapNodeIdToUML(files);
	}
	
	public String[] generateNamespacesToExport() {
		NamespaceTable nsTable = getNamespaceTable();
		List<String> namespacesToExport = new ArrayList<>();
		for (int i = 0; i < nsTable.size(); i++) {
			String uri = nsTable.getUri(i);
			if (NamespaceTable.OPCUA_NAMESPACE.equals(uri)) {
				// add null instead of default OPC UA Namespace to skip nodes
				namespacesToExport.add(null);
				continue;
			}
			namespacesToExport.add(uri);
		}
		return namespacesToExport.toArray(new String[0]);
	}
	
	@Override
	public NamespaceTable getNamespaceTable() {
		return this.importer.getNamespaceTable();
	}

	@Override
	public Node getNodeById(NodeId nodeId) {
		return getNodeById(nodeIdToExpandedNodeId(nodeId));
	}

	@Override
	public Node getNodeById(ExpandedNodeId nodeId) {
		return this.importer.getNodesItemList().get(nodeId);
	}
	
	@Override
	public String getResource(String modelname, NodeId nodeId) {
		String resourceId = "";
		try {
			resourceId = this.importer.getResource(modelname, nodeId, ResourceType.Generalization);
		} catch (StatemachineException e) {
			e.printStackTrace();
		}
		return resourceId;
	}
	
	public Map<ExpandedNodeId, Node> importOpcUaNodeset(File...file) throws StatemachineException {
		this.importer.importNodeSet(file);
		return this.importer.getNodesItemList();
	}
	
	@Override
	public void loadUMLResources(File...files) {
		this.importer.loadUMLResource(files);
	}	
	
	@Override
	public ExpandedNodeId nodeIdToExpandedNodeId(NodeId nodeId) {
		return this.importer.getNamespaceTable().toExpandedNodeId(nodeId);
	}

	private Map<ExpandedNodeId, Node> setup() throws StatemachineException {
		Logger.getLogger(getClass().getName()).log(Level.INFO, "Start UML reverse engineering");
		this.importer.importNodeSet(loadDefaultOpcUaModelFiles());
		return this.importer.getNodesItemList();
	}
	
	public abstract File[] loadDefaultOpcUaModelFiles();
	public abstract File[] loadUMLDefaultOpcUaClassFiles();
}
