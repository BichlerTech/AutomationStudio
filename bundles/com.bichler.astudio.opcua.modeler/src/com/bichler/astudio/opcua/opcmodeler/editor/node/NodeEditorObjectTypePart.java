package com.bichler.astudio.opcua.opcmodeler.editor.node;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.bichler.astudio.utils.internationalization.CustomString;
import com.bichler.astudio.utils.ui.swt.CheckBoxButton;
import com.bichler.astudio.opcua.opcmodeler.Activator;

import opc.sdk.core.node.Node;
import opc.sdk.core.node.ObjectTypeNode;
import opc.sdk.core.node.UAObjectTypeNode;

public class NodeEditorObjectTypePart extends NodeEditorExtensionPart {
	public static final String ID = "com.hbsoft.designer.editor.node.NodeEditorObjectTypePart"; //$NON-NLS-1$
	private CheckBoxButton cb_isAbstract;
	private Label lbl_isAbstract;

	public NodeEditorObjectTypePart() {
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
		frm_mainForm.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
				"NodeEditorObjectTypePart.frm_mainForm.text") + getEditorInput().getName());
	}

	@Override
	protected void setInputs() {
		super.setInputs();
		Node node = this.getEditorInput().getNode().getNode();
		this.controllCreationToolkit.log(Status.INFO,
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "NodeEditorObjectTypePart.open.info"),
				node);
		this.cb_isAbstract.setChecked(((ObjectTypeNode) node).getIsAbstract());
		// this.controllCreationToolkit.setObjectTypeReferenceTable(tv_references,
		// node);
	}

	@Override
	protected void setHandlers() {
		super.setHandlers();
		this.controllCreationToolkit.setDirtyListener(lbl_isAbstract, this.cb_isAbstract, this);
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		this.setFocus();
		if (this.valid()) {
			super.doSave(monitor);
			if (monitor.isCanceled()) {
				return;
			}
			UAObjectTypeNode node = (UAObjectTypeNode) getEditorInput().getNode().getNode();
			// construct the new changed values
			node.setIsAbstract(this.cb_isAbstract.isChecked());
			this.frm_mainForm.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
					"NodeEditorObjectTypePart.frm_mainForm.text") + getEditorInput().getName());
			doSaveFinish();
		}
	}

	@Override
	public void dispose() {
		super.dispose();
		Node node = getEditorInput().getNode().getNode();
		this.controllCreationToolkit.log(Status.INFO,
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "NodeEditorObjectTypePart.close.info"),
				node);
	}

	@Override
	protected void createExtendedSection(Composite extended) {
		lbl_isAbstract = new Label(extended, SWT.NONE);
		GridData gd_lbl_isAbstract = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lbl_isAbstract.widthHint = 220;
		lbl_isAbstract.setLayoutData(gd_lbl_isAbstract);
		lbl_isAbstract.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
				"NodeEditorObjectTypePart.lbl_isAbstract.text"));
		formToolkit.adapt(lbl_isAbstract, true, true);
		cb_isAbstract = new CheckBoxButton(extended, SWT.NONE);
		cb_isAbstract.setTouchEnabled(true);
		// cb_isAbstract.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,"NodeEditorObjectTypePart.cb_isAbstract.text"));
		cb_isAbstract.setLeftMargin(8);
		formToolkit.paintBordersFor(cb_isAbstract);
		lbl_writeMask = new Label(extended, SWT.NONE);
		GridData gd_lbl_writeMask = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lbl_writeMask.widthHint = 220;
		lbl_writeMask.setLayoutData(gd_lbl_writeMask);
		lbl_writeMask.setText(
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "NodeEditorPart.lbl_writeMask.text"));
		formToolkit.adapt(lbl_writeMask, true, true);
		txt_writeMask = new Text(extended, SWT.BORDER | SWT.READ_ONLY);
		GridData gd_txt_writeMask = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_txt_writeMask.widthHint = 250;
		txt_writeMask.setLayoutData(gd_txt_writeMask);
		formToolkit.adapt(txt_writeMask, true, true);
		lbl_userWriteMask = new Label(extended, SWT.NONE);
		GridData gd_lbl_userWriteMask = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lbl_userWriteMask.widthHint = 220;
		lbl_userWriteMask.setLayoutData(gd_lbl_userWriteMask);
		lbl_userWriteMask.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
				"NodeEditorPart.lbl_userWriteMask.text"));
		formToolkit.adapt(lbl_userWriteMask, true, true);
		txt_userWriteMask = new Text(extended, SWT.BORDER | SWT.READ_ONLY);
		GridData gd_txt_userWriteMask = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_txt_userWriteMask.widthHint = 250;
		txt_userWriteMask.setLayoutData(gd_txt_userWriteMask);
		formToolkit.adapt(txt_userWriteMask, true, true);
	}

	@Override
	protected void preDisableWidgets() {
		// TODO Auto-generated method stub
	}
}
