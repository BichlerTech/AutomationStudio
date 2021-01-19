package com.bichler.astudio.licensemanagement.wizard.page;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.ResourceManager;
import org.eclipse.wb.swt.SWTResourceManager;

import com.bichler.astudio.licensemanagement.LicManActivator;
import com.bichler.astudio.utils.internationalization.CustomString;
import com.wibu.CodeMeter.CodeMeter;
import com.wibu.CodeMeter.CodeMeter.CMACCESS2;

public class LicenseNoCmConnectionPage extends WizardPage {
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private long firmCode;
	private long productCode;

	/**
	 * Create the wizard.
	 */
	public LicenseNoCmConnectionPage(long firmCode, long productCode) {
		super("cmnoconnectionpage");
		setTitle(CustomString.getString(LicManActivator.getDefault().RESOURCE_BUNDLE, "form.lostcm.title"));
		setDescription(CustomString.getString(LicManActivator.getDefault().RESOURCE_BUNDLE, "form.lostcm.description"));

		this.firmCode = firmCode;
		this.productCode = productCode;
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
		lbl_img.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 2));
		lbl_img.setImage(ResourceManager.getPluginImage(LicManActivator.PLUGIN_ID, "icons/wizards/key.png"));
		formToolkit.adapt(lbl_img, true, true);

		Label lbl_text = formToolkit.createLabel(container, "", SWT.WRAP);
		lbl_text.setBackground(SWTResourceManager.getColor(240, 240, 240));
		lbl_text.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, true, 1, 1));
		lbl_text.setText(CustomString.getString(LicManActivator.getDefault().RESOURCE_BUNDLE, "form.lostcm.text"));
		lbl_text.setBackground(SWTResourceManager.getColor(240, 240, 240));
		lbl_text.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));

		Button btn_refresh = formToolkit.createButton(container, "", SWT.PUSH);
		btn_refresh.setImage(ResourceManager.getPluginImage(LicManActivator.PLUGIN_ID, "icons/refresh_24.png"));
		btn_refresh
				.setToolTipText(CustomString.getString(LicManActivator.getDefault().RESOURCE_BUNDLE, "form.refresh"));
		GridData gd_btn_refresh = new GridData(SWT.RIGHT, SWT.TOP, false, true, 1, 1);
		gd_btn_refresh.widthHint = 48;
		gd_btn_refresh.heightHint = 48;
		btn_refresh.setLayoutData(gd_btn_refresh);
		btn_refresh.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				CMACCESS2 cmacc = new CMACCESS2();
				cmacc.firmCode = firmCode;
				cmacc.productCode = productCode;

				long handle = CodeMeter.cmAccess2(CodeMeter.CM_ACCESS_LOCAL, cmacc);

				if (handle == 0) {
					setPageComplete(false);
				} else {
					setPageComplete(true);
					CodeMeter.cmRelease(handle);
				}
			}

		});

		setPageComplete(false);
	}
}
