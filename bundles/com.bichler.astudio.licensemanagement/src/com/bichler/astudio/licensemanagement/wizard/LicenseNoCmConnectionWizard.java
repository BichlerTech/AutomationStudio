package com.bichler.astudio.licensemanagement.wizard;

import org.eclipse.jface.wizard.Wizard;

import com.bichler.astudio.licensemanagement.LicManActivator;
import com.bichler.astudio.licensemanagement.wizard.page.LicenseNoCmConnectionPage;
import com.bichler.astudio.utils.internationalization.CustomString;
import com.wibu.cm.CodeMeter;
import com.wibu.cm.CodeMeter.CMACCESS2;

public class LicenseNoCmConnectionWizard extends Wizard {

	private LicenseNoCmConnectionPage pageOne;
	private long hcmse = 0l;
	private long firmCode = 0l;
	private long productCode = 0l;

	public LicenseNoCmConnectionWizard(long firmCode, long productCode) {
		setWindowTitle(
				CustomString.getString(LicManActivator.getDefault().RESOURCE_BUNDLE, "form.license"));
		
		this.firmCode = firmCode;
		this.productCode = productCode;
	}

	@Override
	public void addPages() {
		this.pageOne = new LicenseNoCmConnectionPage(this.firmCode, this.productCode);
		addPage(pageOne);
	}

	@Override
	public boolean performFinish() {
		// access license
		CMACCESS2 cmacc = new CMACCESS2();
		// cmacc.ctrl = CodeMeter.CM_ACCESS_CONVENIENT;
		cmacc.firmCode = this.firmCode;
		cmacc.productCode = this.productCode;
		this.hcmse = CodeMeter.cmAccess2(CodeMeter.CM_ACCESS_LOCAL, cmacc);
		if (0 == hcmse) {
			// TODO: LOG
			System.out.println("CmAccess() failed with error: " + CodeMeter.cmGetLastErrorText());
			return false;
		}

		return true;
	}

	public long getHCMSE() {
		return this.hcmse;
	}

}
