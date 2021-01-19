package com.bichler.astudio.licensemanagement.wizard.page;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.ResourceManager;
import org.eclipse.swt.layout.GridData;
import org.eclipse.wb.swt.SWTResourceManager;

public class LicenseCategoryExceededWizardPage extends WizardPage {
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private String text;

	/**
	 * Create the wizard.
	 * 
	 * @param description
	 * @param title
	 * @param text
	 */
	public LicenseCategoryExceededWizardPage(String title, String description, String text) {
		super("licensecategroyexceededwizardpage");
		setTitle(title);
		setDescription(description);
		this.text = text;
	}

	/**
	 * Create contents of the wizard.
	 * 
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		setControl(container);
		container.setLayout(new GridLayout(2, false));

		Label lbl_img = new Label(container, SWT.NONE);
		lbl_img.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		lbl_img.setImage(
				ResourceManager.getPluginImage("com.bichler.astudio.licensemanagement", "icons/wizards/key.png"));
		formToolkit.adapt(lbl_img, true, true);

		Label lbl_text = formToolkit.createLabel(container, "license not good", SWT.WRAP);
		lbl_text.setBackground(SWTResourceManager.getColor(240, 240, 240));
		lbl_text.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, true, 1, 1));
		lbl_text.setText(this.text);
	}
	
	

	@Override
	public boolean isPageComplete() {
		return super.isPageComplete();
	}

}
