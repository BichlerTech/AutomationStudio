package com.bichler.astudio.opcua.opcmodeler.editor.node.validation.errors;

import java.util.ArrayList;

import org.eclipse.swt.widgets.Text;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.common.NamespaceTable;
import org.opcfoundation.ua.core.IdType;

import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.opcua.modeller.controls.CometCombo;
import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.opcua.opcmodeler.editor.node.NodeEditorPart;
import com.bichler.astudio.opcua.opcmodeler.utils.extern.DesignerUtils;
import com.bichler.astudio.utils.internationalization.CustomString;

import opc.sdk.core.node.NodeIdMode;

public class NodeIdQuickFixProvider {
	private CometCombo selectedNamespaceIndex = null;
	private Object callback;

	public NodeIdQuickFixProvider(CometCombo namespace, Object callback) {
		this.callback = callback;
		this.selectedNamespaceIndex = namespace;
	}

	public boolean doQuickFix(Text value) {
		NamespaceTable nsTable = ServerInstance.getInstance().getServerInstance().getNamespaceUris();
		int index = nsTable.getIndex(this.selectedNamespaceIndex.getText());
		NodeIdMode continueNodeId = DesignerUtils.getPreferenceContinueCreateNodeIds();
		NodeId nxtNodeId = ServerInstance.getInstance().getServerInstance().getAddressSpaceManager().getNodeFactory()
				.showNextNodeId(index, IdType.Numeric, continueNodeId);
		if (callback instanceof NodeEditorPart) {
			((NodeEditorPart) callback).setNodeIdInputs(nxtNodeId);
		}
		return true;
	}

	public String getQuickFixMenuText() {
		return CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "NodeIdQuickfix.text");
	}

	public boolean hasQuickFix(String value) {
		/*
		 * if (value != null) { return false; }
		 */
		return true;
	}
}
