package com.bichler.astudio.device.opcua.wizard.page.selection;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

import com.bichler.astudio.device.core.preference.DevicePreferenceConstants;
import com.bichler.astudio.device.core.preference.DevicePreferenceManager;
import com.bichler.astudio.device.opcua.DeviceActivator;
import com.bichler.astudio.connections.enums.ConnectionType;
import com.bichler.astudio.core.user.util.UserUtils;
import com.bichler.astudio.filesystem.IFileSystem;
import com.bichler.astudio.utils.internationalization.CustomString;
import com.bichler.astudio.utils.ui.swt.NumericText;

public class DeviceSelectionWizardPage extends WizardPage {
	private Text txtConnectionName;
	private Text txtHost;
	private Text txtUser;
	private Text txtPassword;
	private Text txtSeparator;
	private Text txtPath;
	private NumericText txtTimeout;
	private Combo cmbFilesystemType;
	private Combo cmbTargetDevice;
	private DevicePreferenceManager devicemanager;
	private Preferences input;

	private String targetDeviceName;

	/**
	 * Create the wizard.
	 */
	public DeviceSelectionWizardPage() {
		super("target");

		setTitle(CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.device.opcua.handler.wizard.deviceselection.page.titel"));
		setDescription(CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.device.opcua.handler.wizard.deviceselection.page.description"));
		this.devicemanager = new DevicePreferenceManager();
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

		Label lblDevice = new Label(container, SWT.NONE);
		lblDevice.setText(CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.device.opcua.property.device"));

		this.cmbTargetDevice = new Combo(container, SWT.NONE);
		cmbTargetDevice.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

		Composite composite = new Composite(container, SWT.BORDER);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 2));
		composite.setLayout(new GridLayout(2, false));
		composite.setEnabled(false);

		Label label = new Label(composite, SWT.NONE);
		label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label.setText(CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.device.opcua.property.type"));

		this.cmbFilesystemType = new Combo(composite, SWT.READ_ONLY);
		cmbFilesystemType.setItems(
				new String[] { ConnectionType.Virtual_Device.getDescription(), ConnectionType.SSH.getDescription() });
		GridData gd_combo = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_combo.widthHint = 284;
		cmbFilesystemType.setLayoutData(gd_combo);
		cmbFilesystemType.select(0);

		Label label_1 = new Label(composite, SWT.NONE);
		label_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label_1.setText(CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.device.opcua.property.name"));

		txtConnectionName = new Text(composite, SWT.BORDER);
		txtConnectionName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label label_3 = new Label(composite, SWT.NONE);
		label_3.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label_3.setText(CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.device.opcua.property.timeout"));

		this.txtTimeout = new NumericText(composite, SWT.BORDER);
		txtTimeout.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label label_5 = new Label(composite, SWT.NONE);
		label_5.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label_5.setText(CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.device.opcua.property.host"));

		txtHost = new Text(composite, SWT.BORDER);
		txtHost.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label label_7 = new Label(composite, SWT.NONE);
		label_7.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label_7.setText(CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.device.opcua.property.user"));

		txtUser = new Text(composite, SWT.BORDER);
		txtUser.setEnabled(false);
		txtUser.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label label_9 = new Label(composite, SWT.NONE);
		label_9.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label_9.setText(CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.device.opcua.property.password"));

		txtPassword = new Text(composite, SWT.BORDER | SWT.PASSWORD);
		txtPassword.setEnabled(false);
		txtPassword.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label label_11 = new Label(composite, SWT.NONE);
		label_11.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label_11.setText(CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.device.opcua.property.separator"));

		txtSeparator = new Text(composite, SWT.BORDER);
		txtSeparator.setEnabled(false);
		txtSeparator.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label label_13 = new Label(composite, SWT.NONE);
		label_13.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label_13.setText(CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.device.opcua.property.path"));

		txtPath = new Text(composite, SWT.BORDER);
		txtPath.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		// this.btn_defaultCombox = new Button(container, SWT.CHECK);
		// btn_defaultCombox.setText("ComBox Upload");
		// btn_defaultCombox.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER,
		// false, false, 2, 1));

		setHandler();

		setInput();

		select();
	}

	public IFileSystem getTargetFileSystem() {
		IFileSystem targetDevice = fillTargetDevice();
		return targetDevice;
	}

	// public boolean isComboxUpload() {
	// return this.comboxUpload;
	// }

	private void setInput() {
		boolean allowSimpleFilesystem = UserUtils.testUserRights(1);

		this.input = this.devicemanager.getRoot();
		try {
			String[] children = this.input.childrenNames();
			for (String child : children) {

				Preferences prefDevice = this.input.node(child);
				int filetype = prefDevice.getInt(DevicePreferenceConstants.PREFERENCE_DEVICE_FILETYPE, -1);
				if (!allowSimpleFilesystem && DevicePreferenceConstants.PREFERENCE_TYPE_SIMPLE == filetype) {
					continue;
				}

				this.cmbTargetDevice.add(child);
			}
		} catch (BackingStoreException e) {
			e.printStackTrace();
		}
	}

	private void setHandler() {
		this.cmbTargetDevice.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				String targetDevice = ((Combo) e.getSource()).getText();
				targetDeviceName = targetDevice;
				fillTargetDeviceInformation(targetDevice);
			}
		});

		// this.btn_defaultCombox.addSelectionListener(new SelectionAdapter() {
		//
		// @Override
		// public void widgetSelected(SelectionEvent e) {
		// comboxUpload = ((Button) e.getSource()).getSelection();
		// }
		//
		// });
	}

	private void select() {
		this.cmbTargetDevice.select(0);
		// int index = this.cmbTargetDevice.getSelectionIndex();
		this.cmbTargetDevice.notifyListeners(SWT.Selection, null);

	}

	private IFileSystem fillTargetDevice() {
		IFileSystem targetFileSystem = this.devicemanager.getFilesystem(this.targetDeviceName);

		return targetFileSystem;
	}

	protected void fillTargetDeviceInformation(String targetDevice) {
		Preferences root = this.devicemanager.getRoot();
		Preferences device = root.node(targetDevice);

		int fileType = device.getInt(DevicePreferenceConstants.PREFERENCE_DEVICE_FILETYPE, 0);
		String host = device.get(DevicePreferenceConstants.PREFERENCE_DEVICE_HOST, "");
		int timeout = device.getInt(DevicePreferenceConstants.PREFERENCE_DEVICE_TIMEOUT, 0);
		String user = device.get(DevicePreferenceConstants.PREFERENCE_DEVICE_USER, "");
		String password = device.get(DevicePreferenceConstants.PREFERENCE_DEVICE_PASSWORD, "");
		String fileseparator = device.get(DevicePreferenceConstants.PREFERENCE_DEVICE_FILESEPARATOR, "");
		String rootPath = device.get(DevicePreferenceConstants.PREFERENCE_DEVICE_ROOTPATH, "");

		switch (fileType) {
		case 0:
			cmbFilesystemType.select(1);
			break;
		case 1:
			cmbFilesystemType.select(0);
			break;
		}
		txtConnectionName.setText(targetDevice);
		txtHost.setText(host);
		txtTimeout.setText("" + timeout);
		txtUser.setText(user);
		txtPassword.setText(password);
		txtSeparator.setText(fileseparator);
		txtPath.setText(rootPath);
	}

}
