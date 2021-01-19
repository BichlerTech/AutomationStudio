package com.bichler.astudio.device.opcua.wizard.page.connection;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import com.bichler.astudio.device.opcua.DeviceActivator;
import com.bichler.astudio.device.opcua.options.EthernetUploadOption;
import com.bichler.astudio.device.opcua.options.UploadOption;
import com.bichler.astudio.utils.internationalization.CustomString;

public class DeviceEthernetWizardPage extends WizardPage {

	private UploadOption mode = UploadOption.Mode_Default;
	
	public DeviceEthernetWizardPage() {
		super("device ethernet page");
		setTitle(CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE, "upload.wizard.ethernet.title"));
		setDescription(CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
				"upload.wizard.ethernet.description"));
	}

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		setControl(container);
		container.setLayout(new FillLayout(SWT.VERTICAL));

		Button btn_defaultMode = new Button(container, SWT.RADIO);
		btn_defaultMode.setSelection(true);
		btn_defaultMode.setText(
				CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE, "upload.wizard.ethernet.default"));
		btn_defaultMode.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				mode = UploadOption.Mode_Default;
			}

		});

		Button btn_singleMode = new Button(container, SWT.RADIO);
		btn_singleMode.setText(
				CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE, "upload.wizard.ethernet.single"));
		btn_singleMode.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				mode = UploadOption.Mode_Single;
			}

		});
	}

	public EthernetUploadOption getEthernetOptions() {
		EthernetUploadOption euo = new EthernetUploadOption();
		euo.upload = this.mode;
		return euo;
	}
}
