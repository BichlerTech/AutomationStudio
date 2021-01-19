package com.bichler.opcua.statemachine.datadictionary;

import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.NodeId;

import com.bichler.astudio.opcua.datadictionary.base.model.AbstractDataDictionaryHelper;
import com.bichler.opcua.statemachine.addressspace.importer.StatemachineNodesetImporter;

import opc.sdk.core.node.Node;

public class DataDictionaryHelper extends AbstractDataDictionaryHelper {

	private StatemachineNodesetImporter importer;

	public DataDictionaryHelper(StatemachineNodesetImporter importer) {
		super();
		this.importer = importer;
	}

	@Override
	protected Node getNode(ExpandedNodeId nodeId) {
		return DatatypeUtils.getNode(this.importer, nodeId);
	}

	@Override
	protected Node getNode(NodeId nodeId) {
		return DatatypeUtils.getNode(this.importer, nodeId);
	}

	@Override
	protected boolean isTypeOf(NodeId type, NodeId typeOf) {
		return DatatypeUtils.isTypeOf(this.importer, type, typeOf);
	}
}
