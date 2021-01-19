package com.bichler.astudio.editor.aggregated.wizard;

import opc.sdk.core.node.NodeIdUtil;

import org.eclipse.swt.widgets.Composite;
import org.opcfoundation.ua.builtintypes.NodeId;

import com.bichler.astudio.opcua.modeller.controls.CometCombo;
import com.bichler.astudio.opcua.opcmodeler.editor.node.DesignerFormToolkit;
import com.bichler.astudio.opcua.opcmodeler.wizards.opc.nodeid.AbstractNodeIdWizardPage;
import com.bichler.astudio.utils.internationalization.CustomString;
import com.richclientgui.toolbox.validation.IQuickFixProvider;
import com.richclientgui.toolbox.validation.ValidatingField;
import com.bichler.astudio.editor.aggregated.AggregatedActivator;

public class NodeIdWizardPage extends AbstractNodeIdWizardPage {

	/**
	 * Create the wizard.
	 */
	public NodeIdWizardPage(NodeId id) {
		super("nodeId", id);
		setTitle(CustomString.getString(AggregatedActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.editor.aggregated.propertyview.wizard.nodeid"));
		setDescription(CustomString.getString(AggregatedActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.editor.aggregated.propertyview.wizard.nodeid.title"));
	}

	public NodeIdTypeFieldValidator<String> getNodeIdTypeFieldValidator() {
		return new NodeIdTypeFieldValidator<String>();
	}

	@Override
	protected CometCombo createComboNodeId(Composite parent) {
		DesignerFormToolkit tk = new DesignerFormToolkit();
		return tk.createCometCombo(parent);
	}

	@Override
	public IQuickFixProvider<String> getQuickFixProvider() {
		return new NodeIdFieldQuickFixProvider<String>();
	}

	@Override
	protected void validateNodeIdValueText() {

		if (selectedType != null) {
			String txtId = txtNodeId.getContents();
			String txtIndex = comboNodeId.getText();

			Object idValue = null;
			try {
				idValue = Integer.parseInt(txtId);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}

			if (idValue == null) {
				idValue = txtId;
			}
			try {
				int nsIndex = Integer.parseInt(txtIndex);
				NodeId nodeId = NodeIdUtil.createNodeId(nsIndex, idValue);
				// nodeId = (NodeId) txt_nodeId.getControl().getData(
				// selectedType.name());
				if (!NodeId.isNull(nodeId)) {
					setValue(nodeId);
				}

			} catch (NumberFormatException nfe) {
				// nfe.printStackTrace();
			} catch (IllegalArgumentException iae) {

			}
			txtNodeId.validate();
		}
	}

	class NodeIdFieldQuickFixProvider<T> implements IQuickFixProvider<String> {

		public NodeIdFieldQuickFixProvider() {

		}

		@Override
		public boolean doQuickFix(ValidatingField<String> value) {
			return true;
		}

		@Override
		public String getQuickFixMenuText() {
			return CustomString.getString(AggregatedActivator.getDefault().RESOURCE_BUNDLE,
					"com.bichler.astudio.editor.aggregated.propertyview.wizard.nodeid.quickfix.valid");
		}

		@Override
		public boolean hasQuickFix(String value) {
			/*
			 * if (value != null) { return false; }
			 */
			return true;
		}
	}
}
