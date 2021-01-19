package com.bichler.astudio.opcua.security.wizard;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import org.eclipse.jface.wizard.Wizard;
import org.opcfoundation.ua.transport.security.KeyPair;

import com.bichler.astudio.opcua.security.Activator;
import com.bichler.astudio.opcua.security.wizard.page.RenewCertificateWizardPage;
import com.bichler.astudio.utils.internationalization.CustomString;

public class RenewCertificateWizard extends Wizard {

	private RenewCertificateWizardPage pageRenew;
	private KeyPair keyPair;
	private String cn;
	private String organisation;
	private String applicationUri;
	private String algorithm;
	private int validDays;

	public RenewCertificateWizard(KeyPair keyPair) {
		setWindowTitle(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "wizard.renew.title"));
		this.keyPair = keyPair;
	}

	@Override
	public void addPages() {
		this.pageRenew = new RenewCertificateWizardPage(this.keyPair);
		addPage(this.pageRenew);
	}

	@Override
	public boolean performFinish() {
		this.cn = this.pageRenew.getCN();
		this.organisation = this.pageRenew.getOrganisation();
		this.applicationUri = this.pageRenew.getApplicationUri();
		Calendar dt_issue = this.pageRenew.getDateIssue();
		Calendar dt_expire = this.pageRenew.getDateExpire();
		this.algorithm = this.pageRenew.getAlgorithm();

		
		
		long difference = dt_expire.getTimeInMillis()
				- dt_issue.getTimeInMillis();

		
		
		this.validDays = (int)TimeUnit.DAYS.convert(difference, TimeUnit.MILLISECONDS);
		
//		this.validDays = (int) (difference / (1000 * 60 * 60 * 24));

		return true;
	}

	public String getAlgorithm() {
		return algorithm;
	}

	public String getApplicationUri() {
		return applicationUri;
	}

	public String getCN() {
		return cn;
	}

	public int getValidDays() {
		return this.validDays;
	}

	public String getOrganisation() {
		return organisation;
	}

}
