package com.bichler.astudio.opcua.security.wizard.page;

import java.security.cert.CertificateParsingException;
import java.util.Calendar;
import java.util.Date;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.DateTime;
import org.opcfoundation.ua.transport.security.KeyPair;
import org.opcfoundation.ua.utils.CertificateUtils;

import com.bichler.astudio.opcua.security.Activator;
import com.bichler.astudio.opcua.security.wizard.util.CertGenUtil;
import com.bichler.astudio.utils.internationalization.CustomString;

public class RenewCertificateWizardPage extends WizardPage {
	private Text txt_cn;
	private Text txt_organisation;
	private Text txt_applicationUri;
	private DateTime dt_expire;
	private DateTime dt_issue;
	private KeyPair keyPair;
	private Text txt_algorithm;
	private DateTime dt_newIssue;
	private DateTime dt_newExpire;

	/**
	 * Create the wizard.
	 * 
	 * @param keyPair
	 */
	public RenewCertificateWizardPage(KeyPair keyPair) {
		super("wizardPage");
		setTitle(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "wizard.renew.title"));
		setDescription(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "wizard.renew.description"));
		this.keyPair = keyPair;
	}

	/**
	 * Create contents of the wizard.
	 * 
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);

		setControl(container);
		container.setLayout(new FillLayout(SWT.HORIZONTAL));

		ScrolledComposite scrolledComposite = new ScrolledComposite(container,
				SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);

		Composite composite = new Composite(scrolledComposite, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));
		scrolledComposite.setContent(composite);

		Label lblCn = new Label(composite, SWT.NONE);
		lblCn.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false,
				1, 1));
		lblCn.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "wizard.renew.cn"));

		txt_cn = new Text(composite, SWT.BORDER);
		txt_cn.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));

		Label lblO = new Label(composite, SWT.NONE);
		lblO.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1,
				1));
		lblO.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "wizard.renew.o"));

		txt_organisation = new Text(composite, SWT.BORDER);
		txt_organisation.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true,
				false, 1, 1));

		Label lblApplicationUri = new Label(composite, SWT.NONE);
		lblApplicationUri.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER,
				false, false, 1, 1));
		lblApplicationUri.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "wizard.renew.applicationuri"));

		txt_applicationUri = new Text(composite, SWT.BORDER);
		txt_applicationUri.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true,
				false, 1, 1));

		Label lblAlgorithm = new Label(composite, SWT.NONE);
		lblAlgorithm.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		lblAlgorithm.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "wizard.renew.algorithm"));

		txt_algorithm = new Text(composite, SWT.BORDER);
		txt_algorithm.setEnabled(false);
		txt_algorithm.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true,
				false, 1, 1));

		Label lblOldDate = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
		lblOldDate.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
				false, 2, 1));
		lblOldDate.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "wizard.renew.olddate"));

		Label lblDateOfIssue = new Label(composite, SWT.NONE);
		lblDateOfIssue.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "wizard.renew.olddate.issue"));

		this.dt_issue = new DateTime(composite, SWT.BORDER | SWT.LONG);
		dt_issue.setEnabled(false);
		dt_issue.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false,
				1, 1));

		Label lblDateOfExpire = new Label(composite, SWT.NONE);
		lblDateOfExpire.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "wizard.renew.olddate.expire"));

		this.dt_expire = new DateTime(composite, SWT.BORDER | SWT.LONG);
		dt_expire.setEnabled(false);
		dt_expire.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
				false, 1, 1));

		Label label = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2,
				1));

		Label lblNewDate = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
		lblNewDate.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
				false, 2, 1));
		lblNewDate.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "wizard.renew.newdate"));

		Label lblNewDateOfIssue = new Label(composite, SWT.NONE);
		lblNewDateOfIssue.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "wizard.renew.newdate.issue"));

		this.dt_newIssue = new DateTime(composite, SWT.BORDER | SWT.LONG);
		dt_newIssue.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
				false, 1, 1));

		Label lblNewDateOfExpire = new Label(composite, SWT.NONE);
		lblNewDateOfExpire.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "wizard.renew.newdate.expire"));

		this.dt_newExpire = new DateTime(composite, SWT.BORDER | SWT.LONG);
		dt_newExpire.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
				false, 1, 1));

		fill();

	}

	private void fill() {
		String commonName = CertGenUtil.getValByAttributeTypeFromIssuerDN(
				this.keyPair.getCertificate(), CertGenUtil.ATTR_CN);
		String organisation = CertGenUtil.getValByAttributeTypeFromIssuerDN(
				this.keyPair.getCertificate(), CertGenUtil.ATTR_O);
		String applicationUri = "";
		try {
			applicationUri = CertificateUtils
					.getApplicationUriOfCertificate(this.keyPair
							.getCertificate());
		} catch (CertificateParsingException e) {
			e.printStackTrace();
		}
		// certificate attributes
		this.txt_cn.setText(commonName);
		this.txt_organisation.setText(organisation);
		this.txt_applicationUri.setText(applicationUri);
		this.txt_algorithm.setText(this.keyPair.getCertificate()
				.getCertificate().getSigAlgName());

		// old date expire
		// date issue
		Date date_issue = this.keyPair.getCertificate().getCertificate()
				.getNotBefore();
		Calendar calendar_issue = Calendar.getInstance();
		calendar_issue.setTime(date_issue);
		this.dt_issue.setDate(calendar_issue.get(Calendar.YEAR),
				calendar_issue.get(Calendar.MONTH),
				calendar_issue.get(Calendar.DAY_OF_MONTH));

		Date date_expire = this.keyPair.getCertificate().getCertificate()
				.getNotAfter();
		Calendar calendar_expire = Calendar.getInstance();
		calendar_expire.setTime(date_expire);
		this.dt_expire.setDate(calendar_expire.get(Calendar.YEAR),
				calendar_expire.get(Calendar.MONTH),
				calendar_expire.get(Calendar.DAY_OF_MONTH));

		// new date expire

		Calendar new_issue = Calendar.getInstance();
		this.dt_newIssue.setDate(new_issue.get(Calendar.YEAR),
				new_issue.get(Calendar.MONTH),
				new_issue.get(Calendar.DAY_OF_MONTH));

		Calendar new_expire = Calendar.getInstance();
		new_expire.add(Calendar.YEAR, 10);

		this.dt_newExpire.setDate(new_expire.get(Calendar.YEAR),
				new_expire.get(Calendar.MONTH),
				new_expire.get(Calendar.DAY_OF_MONTH));

	}

	@Override
	public boolean isPageComplete() {
		if (this.txt_cn.getText().isEmpty()) {
			return false;
		}
		if (this.txt_organisation.getText().isEmpty()) {
			return false;
		}

		if (this.txt_applicationUri.getText().isEmpty()) {
			return false;
		}

		if (this.txt_algorithm.getText().isEmpty()) {
			return false;
		}

		Calendar c1 = Calendar.getInstance();
		c1.set(this.dt_newIssue.getYear(), this.dt_newIssue.getMonth(),
				this.dt_newIssue.getDay());
		
		Calendar c2 = Calendar.getInstance();
		c2.set(this.dt_newExpire.getYear(), this.dt_newExpire.getMonth(),
				this.dt_newExpire.getDay());
		
		if(c1.compareTo(c2) >= 0){
			return false;
		}
		
		return true;
	}

	public String getCN() {
		return this.txt_cn.getText();
	}

	public Calendar getDateIssue() {
		Calendar c = Calendar.getInstance();
		c.set(this.dt_newIssue.getYear(), this.dt_newIssue.getMonth(),
				this.dt_newIssue.getDay());
		return c;
	}

	public Calendar getDateExpire() {
		Calendar c = Calendar.getInstance();
		c.set(this.dt_newExpire.getYear(), this.dt_newExpire.getMonth(),
				this.dt_newExpire.getDay());
		return c;
	}

	public String getAlgorithm() {
		return this.txt_algorithm.getText();
	}

	public String getApplicationUri() {
		return this.txt_applicationUri.getText();
	}

	public String getOrganisation() {
		return this.txt_organisation.getText();
	}

}
