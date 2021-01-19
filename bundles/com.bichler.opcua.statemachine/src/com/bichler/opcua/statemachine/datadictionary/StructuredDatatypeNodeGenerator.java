package com.bichler.opcua.statemachine.datadictionary;

import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.common.NamespaceTable;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.AddNodesItem;
import org.opcfoundation.ua.core.AddReferencesItem;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.ReferenceNode;

import com.bichler.astudio.opcua.datadictionary.base.model.AbstractDataDictionaryHelper;
import com.bichler.astudio.opcua.datadictionary.base.model.AbstractStructuredDatatypeNodeGenerator;
import com.bichler.opcua.statemachine.addressspace.importer.StatemachineNodesetImporter;
import com.bichler.opcua.statemachine.transform.AbstractStateMachineToOpcTransformer;

import opc.sdk.core.node.Node;

public class StructuredDatatypeNodeGenerator extends AbstractStructuredDatatypeNodeGenerator {

	private StatemachineNodesetImporter importer;
	private AbstractStateMachineToOpcTransformer transformer;

	public StructuredDatatypeNodeGenerator(AbstractStateMachineToOpcTransformer transformer,
			StatemachineNodesetImporter importer) {
		super();
		this.importer = importer;
		this.transformer = transformer;
	}
	
	public ExpandedNodeId findBinarySchemaId(int namespaceIndex) {
		// check nodes
		for (Node node : this.importer.getStructuredDatatypeNodesItemList().values()) {
			// for hierachical reference to OPCBinarySchema
			for (ReferenceNode reference : node.getReferences()) {
				if (!this.importer.isInverseHierachical(reference)) {
					continue;
				}

				if (!this.importer.isHierachicalReference(reference.getReferenceTypeId())) {
					continue;
				}

				if (!this.importer.getNamespaceTable().toExpandedNodeId(Identifiers.OPCBinarySchema_TypeSystem)
						.equals(reference.getTargetId())) {
					continue;
				}

				NodeId id = node.getNodeId();
				int nsIndex = id.getNamespaceIndex();
				if (namespaceIndex == nsIndex) {
					return this.importer.getNamespaceTable().toExpandedNodeId(node.getNodeId());
				}
			}
		}

		return ExpandedNodeId.NULL;
	}

	@Override
	protected void addNode(AddNodesItem[] nodes, boolean includeChildren) throws ServiceResultException {
		DatatypeUtils.addNode(this.transformer, this.importer, nodes, includeChildren);
	}

	@Override
	protected void addReferences(AddReferencesItem[] references) throws ServiceResultException {
		DatatypeUtils.addReferences(importer, references);
	}

	@Override
	protected AbstractDataDictionaryHelper createDataDictionaryHelper() {
		return new DataDictionaryHelper(this.importer);
	}

	@Override
	protected NamespaceTable getNamespaceTable() {
		return this.importer.getNamespaceTable();
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
	protected NodeId getNextNodeId(int namespaceIndex) {

		// TODO:
//		return this.opcServer.getAddressSpaceManager().getNodeFactory().showNextNodeId(namespaceIndex, IdType.Numeric,
//				NodeIdMode.CONTINUE);
		return this.transformer.getNextNumericNodeId(namespaceIndex);
	}
}
