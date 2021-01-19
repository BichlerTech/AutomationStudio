package com.bichler.astudio.opcua.opcmodeler.editor.node.validation.errors;

import java.util.ArrayList;

import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.common.NamespaceTable;
import org.opcfoundation.ua.core.IdType;

import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.opcua.modeller.controls.CometCombo;
import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.opcua.opcmodeler.commands.handler.extern.NamespaceHandler;
import com.bichler.astudio.opcua.opcmodeler.utils.extern.DesignerUtils;
import com.bichler.astudio.opcua.opcmodeler.wizards.opc.namespace.OPCNamesspaceWizard;
import com.bichler.astudio.utils.internationalization.CustomString;
import com.richclientgui.toolbox.validation.IQuickFixProvider;
import com.richclientgui.toolbox.validation.ValidatingField;

import opc.sdk.core.node.NodeIdMode;

public class NodeIdTextFieldQuickFixProvider<T> implements IQuickFixProvider<String> {
	private CometCombo selectedNamespaceIndex = null;

	public NodeIdTextFieldQuickFixProvider(CometCombo namespace) {
		this.selectedNamespaceIndex = namespace;
	}

	@Override
	public boolean doQuickFix(ValidatingField<String> value) {
		NamespaceTable nsTable = ServerInstance.getInstance().getServerInstance().getNamespaceUris();
		NodeIdMode continueNodeId = DesignerUtils.getPreferenceContinueCreateNodeIds();
		int index = nsTable.getIndex(this.selectedNamespaceIndex.getText());
		// we have no other namespaces then the default opc ua namespace
		if (index == -1) {
			// open namespace dialog
			OPCNamesspaceWizard wizard = new OPCNamesspaceWizard();
			WizardDialog dialog = new WizardDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
					wizard);
			int open = dialog.open();
			if (WizardDialog.OK == open) {
				NamespaceHandler.updateNamespaces(wizard);
				nsTable = ServerInstance.getInstance().getServerInstance().getNamespaceUris();
				if (nsTable.size() > 1)
					index = 1;
			}
		}
		NodeId nxtNodeId = ServerInstance.getInstance().getServerInstance().getAddressSpaceManager().getNodeFactory()
				.showNextNodeId(index, IdType.Numeric, continueNodeId);
		value.setContents(nxtNodeId.getValue().toString());
		return true;
	}

	@Override
	public String getQuickFixMenuText() {
		return CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "NodeIdQuickfix.text");
	}

	@Override
	public boolean hasQuickFix(String value) {
		/*
		 * if (value != null) { return false; }
		 */
		return true;
	}
}
