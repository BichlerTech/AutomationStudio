package com.bichler.astudio.opcua.datadictionary.modeler.model;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.PlatformUI;
import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.core.Identifiers;

import com.bichler.astudio.opcua.datadictionary.base.model.AbstractDataDictionaryHelper;
import com.bichler.astudio.opcua.datadictionary.base.model.AbstractStructuredDataTypeManager;
import com.bichler.astudio.opcua.datadictionary.base.model.AbstractStructuredDatatypeNodeGenerator;

import opc.sdk.core.node.DataTypeNode;
import opc.sdk.core.node.Node;
import opc.sdk.server.core.OPCInternalServer;

public class StructuredDataTypeManager extends AbstractStructuredDataTypeManager{

	private OPCInternalServer opcServer;

	public StructuredDataTypeManager(OPCInternalServer opcServer) {
		super();
		this.opcServer = opcServer;
		setNodeGenerator(createStructuredDatatypeNodeGenerator());
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

	@Override
	protected AbstractDataDictionaryHelper createDataDictionaryHelper() {
		return new DataDictionaryHelper(this.opcServer);
	}

	@Override
	protected AbstractStructuredDatatypeNodeGenerator createStructuredDatatypeNodeGenerator() {
		return new StructuredDatatypeNodeGenerator(this.opcServer);
	}

	@Override
	protected String askForTypeDictionaryName(String defaultName) {
		boolean confirm = MessageDialog.openConfirm(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
				"Binary Schema", "Create OPC Binary schema nodes");
		if (!confirm) {
			return null;
		}

		InputDialog dlg = new InputDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
				"Binary Schema", "Binary Schema name", defaultName, null);
//		new IInputValidator() {
//
//			@Override
//			public String isValid(String newText) {
//				if (schemaNames.contains(newText)) {
//					return "Name already exist!";
//				}
//				return null;
//			}
//		}

		if (dlg.open() != Dialog.OK) {
			return null;
		}
		// VARIABLE - Opc.Ua.<Name>
		return dlg.getValue();
	}

	@Override
	protected ExpandedNodeId findBinarySchemaId(DataTypeNode datatypeNode) {
		Node binarySchema = getNode(Identifiers.OPCBinarySchema_TypeSystem); 

		ExpandedNodeId[] targets = binarySchema.findTargets(Identifiers.HasComponent, false);
		ExpandedNodeId binarySchemaId = ExpandedNodeId.NULL;
		
		for (ExpandedNodeId target : targets) {
			Node targetNode = getNode(target); 
			if (targetNode == null) {
				continue;
			}
			if (targetNode.getNodeId().getNamespaceIndex() == datatypeNode.getNodeId().getNamespaceIndex()) {
				binarySchemaId = target;
				break;
			}
		}
		return binarySchemaId;
	}

}
