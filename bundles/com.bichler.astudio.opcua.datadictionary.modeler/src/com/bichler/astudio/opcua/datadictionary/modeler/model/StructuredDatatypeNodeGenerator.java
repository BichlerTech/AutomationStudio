package com.bichler.astudio.opcua.datadictionary.modeler.model;

import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.common.NamespaceTable;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.AddNodesItem;
import org.opcfoundation.ua.core.AddReferencesItem;
import org.opcfoundation.ua.core.IdType;
import org.opcfoundation.ua.core.Identifiers;

import com.bichler.astudio.opcua.datadictionary.base.model.AbstractDataDictionaryHelper;
import com.bichler.astudio.opcua.datadictionary.base.model.AbstractStructuredDatatypeNodeGenerator;

import opc.sdk.core.node.Node;
import opc.sdk.core.node.NodeIdMode;
import opc.sdk.server.core.OPCInternalServer;

public class StructuredDatatypeNodeGenerator extends AbstractStructuredDatatypeNodeGenerator {

	private OPCInternalServer opcServer;

	public StructuredDatatypeNodeGenerator(OPCInternalServer opcServer) {
		super();
		this.opcServer = opcServer;
	}
	
	@Override
	public ExpandedNodeId findBinarySchemaId(int namespaceIndex) {
		Node binarySchema = getNode(Identifiers.OPCBinarySchema_TypeSystem); // OpcUtils.getNode(this.opcServer,
																				// Identifiers.OPCBinarySchema_TypeSystem);
		ExpandedNodeId[] targets = binarySchema.findTargets(Identifiers.HasComponent, false);

		for (ExpandedNodeId target : targets) {
			Node targetNode = getNode(target); // OpcUtils.getNode(this.opcServer, target);
			if (targetNode == null) {
				continue;
			}
			if (targetNode.getNodeId().getNamespaceIndex() == namespaceIndex) {
				return target;
			}
		}

		return ExpandedNodeId.NULL;
	}

	@Override
	protected void addNode(AddNodesItem[] nodes, boolean includeChildren) throws ServiceResultException {
		OpcUtil.addNode(this.opcServer, nodes, includeChildren);
	}

	@Override
	protected void addReferences(AddReferencesItem[] references) throws ServiceResultException {
		OpcUtil.addReference(this.opcServer, references);
	}

	@Override
	protected AbstractDataDictionaryHelper createDataDictionaryHelper() {
		return new DataDictionaryHelper(this.opcServer);
	}

	@Override
	protected NamespaceTable getNamespaceTable() {
		return this.opcServer.getNamespaceUris();
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
	protected NodeId getNextNodeId(int namespaceIndex) {
		return this.opcServer.getAddressSpaceManager().getNodeFactory().showNextNodeId(namespaceIndex, IdType.Numeric,
				NodeIdMode.CONTINUE);
	}
}
