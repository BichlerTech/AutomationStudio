package com.bichler.astudio.opcua.opcmodeler.dialogs;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.wb.swt.SWTResourceManager;

import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.utils.internationalization.CustomString;

public class ConfirmDialog extends Dialog {
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private CLabel lbl_question;
	private String confirmQuestion = "";

	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 */
	public ConfirmDialog(Shell parentShell) {
		super(parentShell);
		setShellStyle(SWT.BORDER | SWT.TITLE | SWT.PRIMARY_MODAL);
	}

	/**
	 * Create contents of the dialog.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		parent.setTouchEnabled(true);
		parent.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		Composite area = (Composite) super.createDialogArea(parent);
		area.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		area.setLayout(new FillLayout(SWT.HORIZONTAL));
		Composite container = new Composite(area, SWT.NONE);
		container.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		container.setLayout(new FillLayout(SWT.HORIZONTAL));
		Form frm_mainForm = formToolkit.createForm(container);
		formToolkit.paintBordersFor(frm_mainForm);
		frm_mainForm.setText(
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "ConfirmDialog.frm_mainForm.caption"));
		formToolkit.decorateFormHeading(frm_mainForm);
		frm_mainForm.getBody().setLayout(new GridLayout(1, false));
		lbl_question = new CLabel(frm_mainForm.getBody(), SWT.NONE);
		lbl_question.setImage(
				SWTResourceManager.getImage(ConfirmDialog.class, "/org/eclipse/jface/dialogs/images/help.gif"));
		lbl_question.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, true, 1, 1));
		formToolkit.adapt(lbl_question, true, true);
		lbl_question.setText(this.confirmQuestion);
		return area;
	}

	/**
	 * Create contents of the button bar.
	 * 
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		parent.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		Button btn_yes = createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		btn_yes.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "ConfirmDialog.btn_yes.text"));
		Button btn_no = createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
		btn_no.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "ConfirmDialog.btn_no.text"));
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(450, 166);
	}

	public void setConfirmQuestion(String question) {
		this.confirmQuestion = question;
	}
}
