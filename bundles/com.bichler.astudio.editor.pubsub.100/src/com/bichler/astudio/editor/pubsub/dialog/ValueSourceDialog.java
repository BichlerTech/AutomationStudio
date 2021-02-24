package com.bichler.astudio.editor.pubsub.dialog;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.bichler.astudio.editor.pubsub.wizard.core.WrapperStaticValue;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.layout.GridData;

public class ValueSourceDialog extends Dialog {

	private WrapperStaticValue value = null;
	private Combo cmbFieldSourceEnabled;
	private Combo cmbInformationModelNode;
	private Text txtStaticValueSource;
	private Button btnStaticValueSource;
	
	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public ValueSourceDialog(Shell parentShell) {
		super(parentShell);
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new GridLayout(3, false));
		
		Label lblFieldSourceEnabled = new Label(container, SWT.NONE);
		lblFieldSourceEnabled.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblFieldSourceEnabled.setText("FieldSourceEnabled:");
		
		this. cmbFieldSourceEnabled = new Combo(container, SWT.NONE);
		cmbFieldSourceEnabled.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		new Label(container, SWT.NONE);
		
		Label lblInformationmodelnode = new Label(container, SWT.NONE);
		lblInformationmodelnode.setText("InformationModelNode:");
		
		this. cmbInformationModelNode = new Combo(container, SWT.NONE);
		cmbInformationModelNode.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		new Label(container, SWT.NONE);
		
		Label lblStaticValueSource = new Label(container, SWT.NONE);
		lblStaticValueSource.setText("StaticValueSource:");

		this.txtStaticValueSource = new Text(container, SWT.BORDER);
		txtStaticValueSource.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		this.btnStaticValueSource = new Button(container, SWT.PUSH);
		this.btnStaticValueSource.setText(" ");
		
		return container;
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(450, 300);
	}

	public WrapperStaticValue getStaticValue() {
		if(value == null) {
			return new WrapperStaticValue();
		}
		return this.value;
	}

	public void setStaticValue(WrapperStaticValue value) {
		this.value = value;
	}

}
