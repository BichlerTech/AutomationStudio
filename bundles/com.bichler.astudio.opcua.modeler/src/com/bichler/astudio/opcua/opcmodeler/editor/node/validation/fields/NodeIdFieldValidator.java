package com.bichler.astudio.opcua.opcmodeler.editor.node.validation.fields;

import org.eclipse.swt.widgets.Text;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.common.NamespaceTable;

import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.utils.internationalization.CustomString;
import com.bichler.astudio.opcua.modeller.controls.CometCombo;
import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.richclientgui.toolbox.validation.validator.IFieldValidator;

import opc.sdk.core.node.Node;
import opc.sdk.core.node.NodeIdUtil;

public class NodeIdFieldValidator<T> implements IFieldValidator<String> {
	private CometCombo namespaceIndex = null;
	private Text textValue;
	private NodeId id = null;

	public CometCombo getNamespaceIndex() {
		return namespaceIndex;
	}

	// public void setNamespaceIndex(CometCombo namespaceIndex) {
	// this.namespaceIndex = namespaceIndex;
	// }
	public void setTextfield(Text textValue) {
		this.textValue = textValue;
	}

	// private Map<Integer, Object> ids = null;
	public NodeIdFieldValidator(CometCombo combo_nodeIdNamespaceIndex, NodeId usedNodeId) {
		this.namespaceIndex = combo_nodeIdNamespaceIndex;
		this.id = usedNodeId;
	}

	@Override
	public String getErrorMessage() {
		return CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "NodeId.invalid.error");
	}

	@Override
	public String getWarningMessage() {
		return null;
	}

	@Override
	public boolean isValid(String nodeId) {
		// check if the input string is empty or null
		if (nodeId == null || nodeId.isEmpty()) {
			return false;
		}
		// TODO: CHECK IF THE NODEID IS OPC DEFINITION VALID (UNIQUE IN
		// ADDRESSSPACE)
		NamespaceTable namespaces = ServerInstance.getInstance().getServerInstance().getNamespaceUris();
		// AddressSpace addressSpace = ServerInstance.getInstance()
		// .getServerInstance().getAddressSpace();
		int index = namespaces.getIndex(this.namespaceIndex.getText());
		if (index == -1) {
			return false;
		}
		NodeId lookupNodeId = null;
		if (this.textValue != null && this.textValue.getData() != null) {
			lookupNodeId = (NodeId) this.textValue.getData();
		} else {
			Object identifier = NodeIdUtil.createIdentifierFromString(nodeId);
			lookupNodeId = NodeIdUtil.createNodeId(index, identifier);
		}
		Node result = ServerInstance.getNode(lookupNodeId);
		if (result != null) {
			if (this.id != null) {
				if ((lookupNodeId.equals(this.id))) {
					return true;
				}
			}
			return false;
		}
		return true;
	}

	@Override
	public boolean warningExist(String nodeId) {
		return false;
	}
}
