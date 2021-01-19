package com.bichler.opcua.statemachine.datadictionary;

import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.ReferenceNode;

import com.bichler.astudio.opcua.datadictionary.base.model.AbstractDataDictionaryHelper;
import com.bichler.astudio.opcua.datadictionary.base.model.AbstractStructuredDataTypeManager;
import com.bichler.astudio.opcua.datadictionary.base.model.AbstractStructuredDatatypeNodeGenerator;
import com.bichler.opcua.statemachine.addressspace.importer.StatemachineNodesetImporter;
import com.bichler.opcua.statemachine.transform.AbstractStateMachineToOpcTransformer;

import opc.sdk.core.node.DataTypeNode;
import opc.sdk.core.node.Node;

public class StructuredDataTypeManager extends AbstractStructuredDataTypeManager {

	private StatemachineNodesetImporter importer;
	private AbstractStateMachineToOpcTransformer transformer;

	public StructuredDataTypeManager(AbstractStateMachineToOpcTransformer transformer,
			StatemachineNodesetImporter importer) {
		super();
		this.importer = importer;
		this.transformer = transformer;
		setNodeGenerator(createStructuredDatatypeNodeGenerator());
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

	@Override
	protected AbstractDataDictionaryHelper createDataDictionaryHelper() {
		return new DataDictionaryHelper(this.importer);
	}

	@Override
	protected AbstractStructuredDatatypeNodeGenerator createStructuredDatatypeNodeGenerator() {
		return new StructuredDatatypeNodeGenerator(this.transformer, this.importer);
	}

	@Override
	protected String askForTypeDictionaryName(String defaultName) {
		return defaultName;
	}

	@Override
	protected ExpandedNodeId findBinarySchemaId(DataTypeNode datatypeNode) {
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
				if (datatypeNode.getNodeId().getNamespaceIndex() == nsIndex) {
					return this.importer.getNamespaceTable().toExpandedNodeId(node.getNodeId());
				}
			}
		}

		return ExpandedNodeId.NULL;
	}

}
