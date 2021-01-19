package com.bichler.astudio.opcua.datadictionary.modeler.model;

import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.NodeId;

import com.bichler.astudio.opcua.datadictionary.base.model.AbstractDataDictionaryHelper;

import opc.sdk.core.node.Node;
import opc.sdk.server.core.OPCInternalServer;

public class DataDictionaryHelper extends AbstractDataDictionaryHelper {

	private OPCInternalServer opcServer;

	public DataDictionaryHelper(OPCInternalServer opcServer) {
		super();
		this.opcServer = opcServer;
	}

	@Override
	protected Node getNode(ExpandedNodeId nodeId) {
		return OpcUtil.getNode(this.opcServer, nodeId);
	}

	@Override
	protected Node getNode(NodeId nodeId) {
		return OpcUtil.getNode(this.opcServer, nodeId);
	}

	@Override
	protected boolean isTypeOf(NodeId type, NodeId typeOf) {
		return OpcUtil.isTypeOf(this.opcServer, type, typeOf);
	}
}
