package com.bichler.astudio.opcua.opcmodeler.preferences;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.wb.swt.SWTResourceManager;

import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.utils.internationalization.CustomString;

public class LicensePreferencePage extends PreferencePage implements IWorkbenchPreferencePage {
	public static final String ID = "com.hbsoft.designer.preferences.licensepreferencepage";
	// private final FormToolkit formToolkit = new
	// FormToolkit(Display.getDefault());
	private Text txt_company;
	private Text txp_address;
	private Text txt_zip;
	private Text txt_city;
	private Text txt_country;
	private Text txt_name;
	private Text text_6;
	private Text txt_phone;
	private Text txt_mobile;
	private Text txt_fax;
	private Text txt_email;
	private Text txt_key;
	private Text txt_machineKey;

	/**
	 * Create the preference page.
	 */
	public LicensePreferencePage() {
		setTitle(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "form.license"));
	}

	/**
	 * Create contents of the preference page.
	 * 
	 * @param parent
	 */
	@Override
	public Control createContents(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		GridLayout gl_container = new GridLayout(2, false);
		gl_container.verticalSpacing = 2;
		container.setLayout(gl_container);
		Label lblCompanyInfo = new Label(container, SWT.NONE);
		lblCompanyInfo.setFont(SWTResourceManager.getFont("Lucida Grande", 11, SWT.BOLD));
		lblCompanyInfo.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "form.companyinfo"));
		new Label(container, SWT.NONE);
		Label label = new Label(container, SWT.SEPARATOR | SWT.HORIZONTAL);
		GridData gd_label = new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1);
		gd_label.heightHint = 6;
		label.setLayoutData(gd_label);
		Label lblNewLabel = new Label(container, SWT.NONE);
		GridData gd_lblNewLabel = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblNewLabel.widthHint = 120;
		lblNewLabel.setLayoutData(gd_lblNewLabel);
		lblNewLabel.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "form.company"));
		txt_company = new Text(container, SWT.BORDER);
		txt_company.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		Label lblAddress = new Label(container, SWT.NONE);
		lblAddress.setText(
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "OmronTcpTypePart.lbl_varAddress.text"));
		txp_address = new Text(container, SWT.BORDER);
		txp_address.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		Label lblZip = new Label(container, SWT.NONE);
		lblZip.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "form.zip"));
		txt_zip = new Text(container, SWT.BORDER);
		txt_zip.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		Label lblCity = new Label(container, SWT.NONE);
		lblCity.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "form.city"));
		txt_city = new Text(container, SWT.BORDER);
		txt_city.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		Label lblCountry = new Label(container, SWT.NONE);
		lblCountry.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "form.country"));
		txt_country = new Text(container, SWT.BORDER);
		txt_country.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		Label lblContactPerson = new Label(container, SWT.NONE);
		lblContactPerson.setFont(SWTResourceManager.getFont("Lucida Grande", 11, SWT.BOLD));
		lblContactPerson.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "form.contactperson"));
		new Label(container, SWT.NONE);
		Label label_1 = new Label(container, SWT.SEPARATOR | SWT.HORIZONTAL);
		label_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));
		Label lblName = new Label(container, SWT.NONE);
		lblName.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "form.surename"));
		txt_name = new Text(container, SWT.BORDER);
		txt_name.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		Label lblVorname = new Label(container, SWT.NONE);
		lblVorname.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "form.name"));
		text_6 = new Text(container, SWT.BORDER);
		text_6.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		Label lblPhone = new Label(container, SWT.NONE);
		lblPhone.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "form.phone"));
		txt_phone = new Text(container, SWT.BORDER);
		txt_phone.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		Label lblMobile = new Label(container, SWT.NONE);
		lblMobile.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "form.phone"));
		txt_mobile = new Text(container, SWT.BORDER);
		txt_mobile.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		Label lblFax = new Label(container, SWT.NONE);
		lblFax.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "form.fax"));
		txt_fax = new Text(container, SWT.BORDER);
		txt_fax.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		Label lblEmail = new Label(container, SWT.NONE);
		lblEmail.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "form.email"));
		txt_email = new Text(container, SWT.BORDER);
		txt_email.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		Label lblNewLabel_1 = new Label(container, SWT.NONE);
		lblNewLabel_1.setFont(SWTResourceManager.getFont("Lucida Grande", 11, SWT.BOLD));
		lblNewLabel_1.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "form.key"));
		new Label(container, SWT.NONE);
		Label label_2 = new Label(container, SWT.SEPARATOR | SWT.HORIZONTAL);
		label_2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));
		Label lbl_maschinekey = new Label(container, SWT.NONE);
		lbl_maschinekey.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "form.machinekey"));
		txt_machineKey = new Text(container, SWT.BORDER | SWT.READ_ONLY);
		txt_machineKey.setEnabled(false);
		txt_machineKey.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		Label lbl_key = new Label(container, SWT.NONE);
		lbl_key.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "form.key"));
		txt_key = new Text(container, SWT.BORDER);
		txt_key.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		this.fill();
		return container;
	}

	private void fill() {
		/** check if license data were inserted */
		if (true) {
			this.txt_company.setText("HB-Softsolution e.U.");
			this.txt_company.setEnabled(false);
			// this.txt
		}
	}

	/**
	 * Initialize the preference page.
	 */
	public void init(IWorkbench workbench) {
	}

	@Override
	protected void performApply() {
		super.performApply();
	}

	@Override
	public boolean performCancel() {
		return super.performCancel();
	}

	@Override
	public boolean performOk() {
		return super.performOk();
	}
}
