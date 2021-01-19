package com.bichler.astudio.opcua.opcmodeler.editor.node;

import opc.sdk.core.node.NamingRuleType;
import opc.sdk.core.node.Node;
import opc.sdk.core.node.UAMethodNode;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.opcfoundation.ua.core.Identifiers;

import com.bichler.astudio.opcua.modeller.controls.CometCombo;
import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.opcua.opcmodeler.utils.extern.DesignerUtils;
import com.bichler.astudio.utils.internationalization.CustomString;
import com.bichler.astudio.utils.ui.swt.CheckBoxButton;

public class NodeEditorMethodPart extends NodeEditorExtensionPart {
	public static final String ID = "com.hbsoft.designer.editor.node.NodeEditorMethodPart"; //$NON-NLS-1$
	// private FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	// private DesignerFormToolkit controllCreationToolkit = new
	// DesignerFormToolkit();
	private CheckBoxButton cb_executeable;
	private CheckBoxButton cb_userExecuteable;
	private Label lbl_executeable;
	private Label lbl_userExecuteable;
	private Label lbl_user;
	private Label lbl_system;
	private Label lbl_dummy;
	private boolean showModellingRule;
	private CometCombo cmb_modellingRule;
	private Label lbl_modellingRule;

	public NodeEditorMethodPart() {
		super();
	}

	/**
	 * Create contents of the editor part.
	 * 
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent) {
		super.createPartControl(parent);
		frm_mainForm.setText(
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "NodeEditorMethodPart.frm_mainForm.text")
						+ " " + getEditorInput().getName());
	}

	@Override
	protected void setInputs() {
		super.setInputs();
		Node node = this.getEditorInput().getNode().getNode();
		this.controllCreationToolkit.log(Status.INFO,
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "NodeEditorMethodPart.open.info"), node);
		if (showModellingRule) {
			this.controllCreationToolkit.setComboModellingRule(cmb_modellingRule, node);
		}
		this.cb_executeable.setChecked(((UAMethodNode) node).getExecutable());
		this.cb_userExecuteable.setChecked(((UAMethodNode) node).getUserExecutable());
		// this.controllCreationToolkit.setMethodReferenceTable(tv_references, node);
	}

	@Override
	protected void setHandlers() {
		super.setHandlers();
		if (showModellingRule) {
			this.controllCreationToolkit.setDirtyListener(lbl_modellingRule, cmb_modellingRule, this);
		}
		this.controllCreationToolkit.setDirtyListener(lbl_executeable, this.cb_executeable, this);
		this.controllCreationToolkit.setDirtyListener(lbl_userExecuteable, this.cb_userExecuteable, this);
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		this.setFocus();
		if (this.valid()) {
			super.doSave(monitor);
			if (monitor.isCanceled()) {
				return;
			}
			Node node = getEditorInput().getNode().getNode();
			// cast the node
			UAMethodNode methodNode = (UAMethodNode) node;
			// construct the new changed values
			methodNode.setExecutable(this.cb_executeable.isChecked());
			methodNode.setUserExecutable(this.cb_userExecuteable.isChecked());
			/** will we need to save modelling rule ? */
			if (showModellingRule) {
				if (this.cmb_modellingRule.getText().compareTo(CustomString
						.getString(Activator.getDefault().RESOURCE_BUNDLE, "ModellingRule.norule.text")) == 0) {
					/** if we previously hab an rule so we need to delete it */
					this.controllCreationToolkit.deleteModellingRule(node);
				} else {
					this.controllCreationToolkit.setModellingRule(node,
							NamingRuleType.valueOf(this.cmb_modellingRule.getText()));
				}
			}
			this.frm_mainForm.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
					"NodeEditorMethodPart.frm_mainForm.text") + getEditorInput().getName());
			doSaveFinish();
		}
	}

	@Override
	public void dispose() {
		super.dispose();
		Node node = getEditorInput().getNode().getNode();
		this.controllCreationToolkit.log(Status.INFO,
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "NodeEditorMethodPart.close.info"),
				node);
	}

	@Override
	protected void createExtendedSection(Composite extended) {
		GridLayout gl_composite_2 = new GridLayout(3, false);
		gl_composite_2.verticalSpacing = 3;
		gl_composite_2.horizontalSpacing = 10;
		extended.setLayout(gl_composite_2);
		lbl_dummy = new Label(extended, SWT.NONE);
		GridData gd_lbl_dummy = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lbl_dummy.widthHint = 120;
		lbl_dummy.setLayoutData(gd_lbl_dummy);
		lbl_system = new Label(extended, SWT.NONE);
		GridData gd_lbl_system = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_lbl_system.widthHint = 50;
		lbl_system.setLayoutData(gd_lbl_system);
		lbl_system.setText(
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "NodeEditorMethodPart.lbl_system.text"));
		formToolkit.adapt(lbl_system, true, true);
		lbl_user = new Label(extended, SWT.NONE);
		GridData gd_lbl_user = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_lbl_user.widthHint = 50;
		lbl_user.setLayoutData(gd_lbl_user);
		lbl_user.setText(
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "NodeEditorMethodPart.lbl_user.text"));
		formToolkit.adapt(lbl_user, true, true);
		// ---------------------------------------------
		lbl_executeable = new Label(extended, SWT.NONE);
		GridData gd_lbl_executeable = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lbl_executeable.widthHint = 120;
		lbl_executeable.setLayoutData(gd_lbl_executeable);
		// lbl_executeable.setText(CustomString.getString("NodeEditorMethodPart.lbl_executeable.text"));
		lbl_executeable.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
				"NodeEditorMethodPart.lbl_executeable.text"));
		formToolkit.adapt(lbl_executeable, true, true);
		cb_executeable = new CheckBoxButton(extended, SWT.NONE);
		GridData gd_cb_executeable = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_cb_executeable.widthHint = 50;
		cb_executeable.setLayoutData(gd_cb_executeable);
		cb_executeable.setTouchEnabled(true);
		// cb_executeable.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
		// "NodeEditorMethodPart.cb_executeable.text"));
		cb_executeable.setLeftMargin(8);
		formToolkit.paintBordersFor(cb_executeable);
		cb_userExecuteable = new CheckBoxButton(extended, SWT.NONE);
		GridData gd_cb_userExecuteable = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_cb_userExecuteable.widthHint = 50;
		cb_userExecuteable.setLayoutData(gd_cb_userExecuteable);
		cb_userExecuteable.setTouchEnabled(true);
		// cb_userExecuteable.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
		// "NodeEditorMethodPart.cb_userExecuteable.text"));
		cb_userExecuteable.setLeftMargin(8);
		formToolkit.paintBordersFor(cb_userExecuteable);
		// -------------------------------------
		lbl_writeMask = new Label(extended, SWT.NONE);
		lbl_writeMask.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
		lbl_writeMask.setText(
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "NodeEditorPart.lbl_writeMask.text"));
		formToolkit.adapt(lbl_writeMask, true, true);
		txt_writeMask = new Text(extended, SWT.BORDER);
		GridData gd_txt_writeMask = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
		gd_txt_writeMask.widthHint = 250;
		txt_writeMask.setLayoutData(gd_txt_writeMask);
		formToolkit.adapt(txt_writeMask, true, true);
		lbl_userWriteMask = new Label(extended, SWT.NONE);
		lbl_userWriteMask.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
		lbl_userWriteMask.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
				"NodeEditorPart.lbl_userWriteMask.text"));
		formToolkit.adapt(lbl_userWriteMask, true, true);
		txt_userWriteMask = new Text(extended, SWT.BORDER);
		GridData gd_txt_userWriteMask = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
		gd_txt_userWriteMask.widthHint = 250;
		txt_userWriteMask.setLayoutData(gd_txt_userWriteMask);
		formToolkit.adapt(txt_userWriteMask, true, true);
		/** will we need it ? */
		if (!DesignerUtils.isChildOf(getEditorInput().getNode().getNode().getNodeId(), Identifiers.ObjectsFolder)) {
			showModellingRule = true;
			lbl_modellingRule = new Label(extended, SWT.NONE);
			GridData gd_lbl_modellingRule = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
			gd_lbl_modellingRule.widthHint = 220;
			lbl_modellingRule.setLayoutData(gd_lbl_modellingRule);
			lbl_modellingRule.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
					"NodeEditorObjectPart.lbl_modellingRule.text")); //$NON-NLS-1$
			formToolkit.adapt(lbl_modellingRule, true, true);
			cmb_modellingRule = new CometCombo(extended, SWT.READ_ONLY);
			GridData gd_cmb_modellingRule = new GridData(SWT.LEFT, SWT.CENTER, true, false, 2, 1);
			gd_cmb_modellingRule.widthHint = 250;
			cmb_modellingRule.setLayoutData(gd_cmb_modellingRule);
			formToolkit.adapt(cmb_modellingRule);
			formToolkit.paintBordersFor(cmb_modellingRule);
		}
	}

	@Override
	protected void preDisableWidgets() {
		// TODO Auto-generated method stub
	}
}
