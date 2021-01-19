package com.bichler.astudio.editor.siemens.dialog;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import com.bichler.astudio.editor.siemens.SiemensActivator;
import com.bichler.astudio.utils.internationalization.CustomString;

public class SiemensModelImportDialog extends Dialog {
	private Label lbl_question;
	private Button btnOk;
	private Button btnAppend;
	private Button btnCancel;
	private DialogResults result;

	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 */
	public SiemensModelImportDialog(Shell parentShell) {
		super(parentShell);
	}

	/**
	 * Create contents of the dialog.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new FillLayout(SWT.HORIZONTAL));
		this.lbl_question = new Label(container, SWT.NONE);
		// lbl_question.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
		// true, 1, 1));
		lbl_question.setText(CustomString.getString(SiemensActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.editor.siemens.wizard.import.dialog.message.description"));
		return container;
	}

	/**
	 * Create contents of the button bar.
	 * 
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		this.btnOk = createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		this.btnAppend = createButton(parent, IDialogConstants.BACK_ID,
				CustomString.getString(SiemensActivator.getDefault().RESOURCE_BUNDLE, "siemens.wizard.append"), false);
		this.btnCancel = createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(450, 300);
	}

	@Override
	protected void buttonPressed(int buttonId) {
		switch (buttonId) {
		case IDialogConstants.OK_ID:
			this.result = DialogResults.OK;
			break;
		case IDialogConstants.CANCEL_ID:
			this.result = DialogResults.Cancel;
			break;
		default:
			this.result = DialogResults.Append;
			okPressed();
			break;
		}
		super.buttonPressed(buttonId);
	}

	public DialogResults getResult() {
		return this.result;
	}
}
