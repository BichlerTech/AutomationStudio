package com.bichler.opcua.statemachine.reverse.engineering;

import java.io.IOException;

import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.common.NamespaceTable;

import com.bichler.opcua.statemachine.exception.StatemachineException;

import opc.sdk.core.node.Node;

public interface IOPCServerEngine {
	
	public abstract Node getNodeById(ExpandedNodeId nodeId);

	public abstract Node getNodeById(NodeId nodeId);

	public abstract ExpandedNodeId nodeIdToExpandedNodeId(NodeId nodeId);
	
	public IReverseTransformationContext createTransformationContext(String directoryPath);
	
	public NamespaceTable getNamespaceTable();
	
	public void addUMLStaticId(NodeId nodeId, String umlId, String externalUMLName);
	
	public void transformTypesModel(IReverseTransformationContext context, String projectName, String[] UA_NAMESPACES) throws IOException, StatemachineException;
	
	public String getResource(String modelname, NodeId nodeId);
}
