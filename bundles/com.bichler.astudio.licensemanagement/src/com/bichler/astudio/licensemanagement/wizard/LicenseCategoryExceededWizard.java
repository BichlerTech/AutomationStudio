package com.bichler.astudio.licensemanagement.wizard;

import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.graphics.Point;

import com.bichler.astudio.licensemanagement.LicManActivator;
import com.bichler.astudio.licensemanagement.wizard.page.LicenseCategoryExceededWizardPage;
import com.bichler.astudio.utils.internationalization.CustomString;

public class LicenseCategoryExceededWizard extends Wizard {

	private LicenseCategoryExceededWizardPage pageOne;

	public LicenseCategoryExceededWizard(String title, String description, String text) {
		setWindowTitle(CustomString.getString(LicManActivator.getDefault().RESOURCE_BUNDLE, "form.license"));
		this.pageOne = new LicenseCategoryExceededWizardPage(title, description, text);
	}

	@Override
	public void addPages() {
		addPage(this.pageOne);

		// Force the shell size
		Point size = getShell().computeSize(510, 540);
		getShell().setSize(size);
	}
	
	@Override
	public boolean performFinish() {
		return true;
	}
	
	

}
