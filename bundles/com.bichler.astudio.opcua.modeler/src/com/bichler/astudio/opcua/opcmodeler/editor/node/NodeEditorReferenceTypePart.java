package com.bichler.astudio.opcua.opcmodeler.editor.node;

import java.util.Locale;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.opcfoundation.ua.builtintypes.LocalizedText;

import com.bichler.astudio.utils.internationalization.CustomString;
import com.bichler.astudio.utils.ui.swt.CheckBoxButton;
import com.bichler.astudio.opcua.modeller.controls.CometCombo;
import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.opcua.opcmodeler.editor.node.language.ListenerInversenameLanguage;

import opc.sdk.core.node.Node;
import opc.sdk.core.node.ReferenceTypeNode;
import opc.sdk.core.node.UAReferenceTypeNode;

public class NodeEditorReferenceTypePart extends NodeEditorExtensionPart {
	public static final String ID = "com.hbsoft.designer.editor.node.NodeEditorReferenceTypePart"; //$NON-NLS-1$
	private CheckBoxButton cb_isAbstract;
	private CheckBoxButton cb_symmetric;
	private Text txt_inverseName;
	private CometCombo cmb_inverseName;
	private Label lbl_isAbstract;
	private Label lbl_symmetric;
	private Button btn_localeInversName;

	public NodeEditorReferenceTypePart() {
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
				"NodeEditorReferenceTypePart.frm_mainForm.text") + getEditorInput().getName());
	}

	@Override
	protected void setInputs() {
		super.setInputs();
		Node node = this.getEditorInput().getNode().getNode();
		this.controllCreationToolkit.log(Status.INFO,
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "NodeEditorReferenceTypePart.open.info"),
				node);
		String inverseNameLocale = ((ReferenceTypeNode) node).getInverseName().getLocaleId();
		String inverseName = ((ReferenceTypeNode) node).getInverseName().getText();
		inverseNameLocale = inverseNameLocale == null ? Locale.ENGLISH.toString() : inverseNameLocale;
		inverseName = inverseName == null ? "" : inverseName;
		this.cb_isAbstract.setChecked(((ReferenceTypeNode) node).getIsAbstract());
		this.cb_symmetric.setChecked(((ReferenceTypeNode) node).getSymmetric());
		this.controllCreationToolkit.setComboLocale(cmb_inverseName, null);
		this.cmb_inverseName.setText(inverseNameLocale);
		this.txt_inverseName.setText(inverseName);
		// this.controllCreationToolkit.setReferenceTypeReferenceTable(tv_references,
		// node);
	}

	@Override
	protected void setHandlers() {
		super.setHandlers();
		this.controllCreationToolkit.setDirtyListener(lbl_isAbstract, this.cb_isAbstract, this);
		this.controllCreationToolkit.setDirtyListener(lbl_symmetric, this.cb_symmetric, this);
		this.controllCreationToolkit.setDirtyListener(cmb_inverseName, txt_inverseName, this);
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		this.setFocus();
		if (this.valid()) {
			super.doSave(monitor);
			if (monitor.isCanceled()) {
				return;
			}
			UAReferenceTypeNode node = (UAReferenceTypeNode) getEditorInput().getNode().getNode();
			node.setIsAbstract(cb_isAbstract.isChecked());
			node.setInverseName(new LocalizedText(this.txt_inverseName.getText(), this.cmb_inverseName.getText()));
			node.setSymmetric(cb_symmetric.isChecked());
			doSaveFinish();
		}
	}

	@Override
	protected boolean valid() {
		if (this.cd_browseNameIndex.isVisible()) {
			getEditorSite().getActionBars().getStatusLineManager()
					.setErrorMessage(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "SaveProblem.error")
							+ " " + this.cd_browseNameIndex.getDescriptionText());
			return false;
		}
		if (this.cd_nodeId.isVisible()) {
			getEditorSite().getActionBars().getStatusLineManager()
					.setErrorMessage(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "SaveProblem.error")
							+ " " + this.cd_nodeId.getDescriptionText());
			return false;
		}
		return true;
		// getEditorSite().getActionBars().getStatusLineManager().setMessage("need
		// to save ");
	}

	@Override
	public void dispose() {
		super.dispose();
		Node node = getEditorInput().getNode().getNode();
		this.controllCreationToolkit.log(Status.INFO, CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
				"NodeEditorReferenceTypePart.close.info"), node);
	}

	@Override
	protected void createExtendedSection(Composite extended) {
		GridLayout gl_composite_2 = new GridLayout(4, false);
		gl_composite_2.horizontalSpacing = 10;
		extended.setLayout(gl_composite_2);
		lbl_isAbstract = new Label(extended, SWT.NONE);
		GridData gd_lbl_isAbstract = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lbl_isAbstract.widthHint = 170;
		lbl_isAbstract.setLayoutData(gd_lbl_isAbstract);
		lbl_isAbstract.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
				"NodeEditorReferenceTypePart.lbl_isAbstract.text"));
		formToolkit.adapt(lbl_isAbstract, true, true);
		cb_isAbstract = new CheckBoxButton(extended, SWT.NONE);
		GridData gd_cb_isAbstract = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_cb_isAbstract.widthHint = 250;
		cb_isAbstract.setLayoutData(gd_cb_isAbstract);
		cb_isAbstract.setTouchEnabled(true);
		// cb_isAbstract.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,"NodeEditorReferenceTypePart.cb_isAbstract.text"));
		cb_isAbstract.setLeftMargin(8);
		formToolkit.paintBordersFor(cb_isAbstract);
		new Label(extended, SWT.NONE);
		new Label(extended, SWT.NONE);
		lbl_symmetric = new Label(extended, SWT.NONE);
		GridData gd_lbl_symmetric = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lbl_symmetric.widthHint = 170;
		lbl_symmetric.setLayoutData(gd_lbl_symmetric);
		lbl_symmetric.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
				"NodeEditorReferenceTypePart.lbl_symmetric.text")); //$NON-NLS-1$
		formToolkit.adapt(lbl_symmetric, true, true);
		cb_symmetric = new CheckBoxButton(extended, SWT.NONE);
		GridData gd_cb_symmetric = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_cb_symmetric.widthHint = 250;
		cb_symmetric.setLayoutData(gd_cb_symmetric);
		cb_symmetric.setTouchEnabled(true);
		// cb_symmetric.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
		// "NodeEditorReferenceTypePart.cb_symmetric.text"));
		cb_symmetric.setLeftMargin(8);
		formToolkit.paintBordersFor(cb_symmetric);
		new Label(extended, SWT.NONE);
		new Label(extended, SWT.NONE);
		Label lbl_inverseName = new Label(extended, SWT.NONE);
		GridData gd_lbl_inverseName = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_lbl_inverseName.widthHint = 170;
		lbl_inverseName.setLayoutData(gd_lbl_inverseName);
		lbl_inverseName.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
				"NodeEditorReferenceTypePart.lbl_inverseName.text")); //$NON-NLS-1$
		formToolkit.adapt(lbl_inverseName, true, true);
		cmb_inverseName = new CometCombo(extended, SWT.READ_ONLY);
		cmb_inverseName.setTouchEnabled(true);
		GridData gd_cmb_inverseName = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_cmb_inverseName.widthHint = 50;
		cmb_inverseName.setLayoutData(gd_cmb_inverseName);
		formToolkit.adapt(cmb_inverseName);
		formToolkit.paintBordersFor(cmb_inverseName);
		cmb_inverseName.setText("<dynamic>");
		txt_inverseName = new Text(extended, SWT.BORDER);
		txt_inverseName.setText("<dynamic>");
		GridData gd_txt_inverseName = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_txt_inverseName.widthHint = 250;
		txt_inverseName.setLayoutData(gd_txt_inverseName);
		formToolkit.adapt(txt_inverseName, true, true);
		this.btn_localeInversName = new Button(extended, SWT.PUSH);
		GridData gd_btn_displayname = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_btn_displayname.widthHint = 50;
		btn_localeInversName.setLayoutData(gd_btn_displayname);
		formToolkit.adapt(btn_localeInversName, true, true);
		btn_localeInversName.setImage(this.img_book);
		btn_localeInversName
				.addSelectionListener(new ListenerInversenameLanguage(this.txt_nodeId, txt_inverseName, this));
		lbl_writeMask = new Label(extended, SWT.NONE);
		GridData gd_lbl_writeMask = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lbl_writeMask.widthHint = 170;
		lbl_writeMask.setLayoutData(gd_lbl_writeMask);
		lbl_writeMask.setText(
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "NodeEditorPart.lbl_writeMask.text"));
		formToolkit.adapt(lbl_writeMask, true, true);
		txt_writeMask = new Text(extended, SWT.BORDER | SWT.READ_ONLY);
		GridData gd_txt_writeMask = new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1);
		gd_txt_writeMask.widthHint = 250;
		txt_writeMask.setLayoutData(gd_txt_writeMask);
		formToolkit.adapt(txt_writeMask, true, true);
		lbl_userWriteMask = new Label(extended, SWT.NONE);
		GridData gd_lbl_userWriteMask = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lbl_userWriteMask.widthHint = 170;
		lbl_userWriteMask.setLayoutData(gd_lbl_userWriteMask);
		lbl_userWriteMask.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
				"NodeEditorPart.lbl_userWriteMask.text"));
		formToolkit.adapt(lbl_userWriteMask, true, true);
		txt_userWriteMask = new Text(extended, SWT.BORDER | SWT.READ_ONLY);
		GridData gd_txt_userWriteMask = new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1);
		gd_txt_userWriteMask.widthHint = 250;
		txt_userWriteMask.setLayoutData(gd_txt_userWriteMask);
		formToolkit.adapt(txt_userWriteMask, true, true);
	}

	@Override
	protected void preDisableWidgets() {
		// TODO Auto-generated method stub
	}
}
