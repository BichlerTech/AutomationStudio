package com.bichler.astudio.licensemanagement.dialog;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.wb.swt.ResourceManager;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.wb.swt.SWTResourceManager;

import com.bichler.astudio.licensemanagement.LicManActivator;
import com.bichler.astudio.utils.internationalization.CustomString;

public class EvaluationLicenseDialog extends Dialog {

	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 */
	public EvaluationLicenseDialog(Shell parentShell) {
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
		container.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.BOLD));
		GridLayout gridLayout = (GridLayout) container.getLayout();
		gridLayout.numColumns = 2;
		
				Label lblHeader = new Label(container, SWT.NONE);
				lblHeader.setFont(SWTResourceManager.getFont("Segoe UI", 16, SWT.BOLD));
				lblHeader.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
				
						lblHeader.setText(CustomString.getString(LicManActivator.getDefault().RESOURCE_BUNDLE, "Evaluation"));
		new Label(container, SWT.NONE);

		Label lblNewLabel = new Label(container, SWT.NONE);
		lblNewLabel.setImage(
				ResourceManager.getPluginImage("com.bichler.astudio.licensemanagement", "icons/wizards/key.png"));

		Label lblDescription = new Label(container, SWT.WRAP);
		lblDescription.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, false, 1, 1));
		lblDescription.setText(CustomString.getString(LicManActivator.getDefault().RESOURCE_BUNDLE, "dialog.eval.description"));

		return container;
	}

	/**
	 * Create contents of the button bar.
	 * 
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(493, 370);
	}

}
