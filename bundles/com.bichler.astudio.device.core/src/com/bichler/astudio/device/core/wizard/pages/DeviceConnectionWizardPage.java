package com.bichler.astudio.device.core.wizard.pages;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.osgi.service.prefs.BackingStoreException;

import com.bichler.astudio.connections.enums.ConnectionType;
import com.bichler.astudio.core.user.util.UserUtils;
import com.bichler.astudio.device.core.DevCoreActivator;
import com.bichler.astudio.device.core.preference.DevicePreferenceManager;
import com.bichler.astudio.filesystem.DataHubFileSystem;
import com.bichler.astudio.filesystem.IFileSystem;
import com.bichler.astudio.filesystem.SimpleFileSystem;
import com.bichler.astudio.filesystem.SshFileSystem;
import com.bichler.astudio.images.common.CommonImagesActivator;
import com.bichler.astudio.utils.internationalization.CustomString;
import com.bichler.astudio.utils.ui.swt.NumericText;

public class DeviceConnectionWizardPage extends WizardPage {

	private static final String DATAHUB_SEPERATOR = "/";

	private Combo cmb_type;
	private Text txt_host;
	private Text txt_username;

	private List<IFileSystem> selectedFilesystem = null;
	// private IFileSystem filesystem = null;

	private Label lblBgcolor;
	private Text txt_password;
	private Text txt_rootpath;
	private Label lblOpcuarootpath;
	private Text txt_connectionName;
	private Text txt_timeout;
	private Button btnOpenDirectoryDialog;
	private Label lblFileSeparator;
	private Text txt_fileseparator;
	private Button btnCheckConnection;

	private boolean isNew;
	private boolean isscan;
	private String[] existingDevices = new String[0];
	private boolean onlyDataHubDevices = false;

	private boolean permissionSimpleFilesystem;

	/**
	 * private int connectionType = 0; private String connectionName = ""; private
	 * int connectionTimeout = 1000; private String hostName = ""; private String
	 * userName = ""; private String password = ""; private String javaPath = "";
	 * private String javaArg = "-jar"; private String rootPath = "";
	 */

	/**
	 * Create the wizard.
	 * 
	 * @param pageOne
	 * @param isnew
	 * 
	 * @wbp.parser.constructor
	 */
	public DeviceConnectionWizardPage(boolean isnew, boolean isscan,
			ScanForDeviceWizardPage pageOne /* , ScanForDeviceDNSWizardPage pageTwo */) {
		super("newdevice");

		setTitle(CustomString.getString(DevCoreActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.device.opcua.handler.wizard.deviceconnection.page.title"));
		setDescription(CustomString.getString(DevCoreActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.device.opcua.handler.wizard.deviceconnection.page.description"));
//		this.page_ScanForDeviceOne = pageOne;
		// this.page_ScanForDeviceTwo = pageTwo;

		// userrights for simple file system
		this.permissionSimpleFilesystem = UserUtils.testUserRights(1);
		initializeSelectedFilesystem();

		this.isNew = isnew;
		this.isscan = isscan;

		DevicePreferenceManager manager = new DevicePreferenceManager();
		try {
			this.existingDevices = manager.getRoot().childrenNames();
		} catch (BackingStoreException e) {
			e.printStackTrace();
		}

		DevicePreferenceManager devPrefManager = new DevicePreferenceManager();
		setOnlyDataHubDevices(devPrefManager.isOnlyDataHub());
	}

	/**
	 * Create contents of the wizard.
	 * 
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);

		setControl(container);
		container.setLayout(new GridLayout(3, false));

		Label lblServername = new Label(container, SWT.NONE);
		lblServername.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblServername.setText(CustomString.getString(DevCoreActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.device.opcua.property.type"));

		cmb_type = new Combo(container, SWT.READ_ONLY);
		initializeComboFilesystem();
		GridData gd_cmb_type = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_cmb_type.widthHint = 284;
		cmb_type.setLayoutData(gd_cmb_type);

		new Label(container, SWT.NONE);

		Label lblNewLabel_1 = new Label(container, SWT.NONE);
		lblNewLabel_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_1.setText(CustomString.getString(DevCoreActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.device.opcua.property.name"));

		txt_connectionName = new Text(container, SWT.BORDER);

		txt_connectionName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(container, SWT.NONE);

		Label lblNewLabel_2 = new Label(container, SWT.NONE);
		lblNewLabel_2.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_2.setText(CustomString.getString(DevCoreActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.device.opcua.property.timeout"));

		txt_timeout = new NumericText(container, SWT.BORDER);

		txt_timeout.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(container, SWT.NONE);

		Label lblNewLabel = new Label(container, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel.setText(CustomString.getString(DevCoreActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.device.opcua.property.host"));

		txt_host = new Text(container, SWT.BORDER);
		txt_host.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		// txt_host.setEnabled(false);
		new Label(container, SWT.NONE);

		Label lblHeight = new Label(container, SWT.NONE);
		lblHeight.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblHeight.setText(CustomString.getString(DevCoreActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.device.opcua.property.user"));
		txt_username = new Text(container, SWT.BORDER);
		// txt_username.setEnabled(false);

		txt_username.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		new Label(container, SWT.NONE);

		lblBgcolor = new Label(container, SWT.NONE);
		lblBgcolor.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblBgcolor.setText(CustomString.getString(DevCoreActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.device.opcua.property.password"));

		txt_password = new Text(container, SWT.BORDER | SWT.PASSWORD);
		// txt_password.setEnabled(false);
		txt_password.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(container, SWT.NONE);

		lblFileSeparator = new Label(container, SWT.NONE);
		lblFileSeparator.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblFileSeparator.setText(CustomString.getString(DevCoreActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.device.opcua.property.separator"));

		txt_fileseparator = new Text(container, SWT.BORDER);
		txt_fileseparator.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(container, SWT.NONE);

		lblOpcuarootpath = new Label(container, SWT.NONE);
		lblOpcuarootpath.setText(CustomString.getString(DevCoreActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.device.opcua.property.path"));
		lblOpcuarootpath.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

		txt_rootpath = new Text(container, SWT.BORDER);
		txt_rootpath.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		btnOpenDirectoryDialog = new Button(container, SWT.NONE);
		btnOpenDirectoryDialog.setToolTipText(CustomString.getString(DevCoreActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.device.opcua.handler.wizard.deviceinstall.page.browse"));
		btnOpenDirectoryDialog.setImage(CommonImagesActivator.getDefault()
				.getRegisteredImage(CommonImagesActivator.IMG_16, CommonImagesActivator.LOG));
		new Label(container, SWT.NONE);

		btnCheckConnection = new Button(container, SWT.NONE);
		btnCheckConnection.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 2, 1));
		btnCheckConnection.setToolTipText(CustomString.getString(DevCoreActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.device.opcua.property.test"));
		btnCheckConnection.setImage(CommonImagesActivator.getDefault().getRegisteredImage(CommonImagesActivator.IMG_16,
				CommonImagesActivator.DATACHANGE));

		setHandler();

		if (!this.onlyDataHubDevices) {
			cmb_type.select(0);
		} else {
			cmb_type.select(1);
			cmb_type.setEnabled(false);
		}
		this.cmb_type.notifyListeners(SWT.Selection, new Event());
		// select();
	}

	@Override
	public boolean isPageComplete() {

		String connectionname = this.txt_connectionName.getText();
		String host = this.txt_host.getText();
		setErrorMessage(null);

		// check for target host connection
		if (this.selectedFilesystem() instanceof SshFileSystem) {
			if (host == null) {
				setErrorMessage(CustomString.getString(DevCoreActivator.getDefault().RESOURCE_BUNDLE,
						"com.bichler.astudio.device.opcua.handler.wizard.deviceconnection.page.error.nohost"));
				return false;
			} else if (host.isEmpty()) {
				setErrorMessage(CustomString.getString(DevCoreActivator.getDefault().RESOURCE_BUNDLE,
						"com.bichler.astudio.device.opcua.handler.wizard.deviceconnection.page.error.nohost"));
				return false;
			}
		}

		if (connectionname == null) {
			setErrorMessage(CustomString.getString(DevCoreActivator.getDefault().RESOURCE_BUNDLE,
					"com.bichler.astudio.device.opcua.handler.wizard.deviceconnection.page.error.emptyhost"));
			return false;
		} else if (connectionname.isEmpty()) {
			setErrorMessage(CustomString.getString(DevCoreActivator.getDefault().RESOURCE_BUNDLE,
					"com.bichler.astudio.device.opcua.handler.wizard.deviceconnection.page.error.emptyhost"));
			return false;
		}

		if (this.isNew) {
			for (String name : this.existingDevices) {
				if (name.equals(connectionname)) {
					setErrorMessage(CustomString.getString(DevCoreActivator.getDefault().RESOURCE_BUNDLE,
							"com.bichler.astudio.device.opcua.handler.wizard.deviceconnection.page.error.samehost"));
					return false;
				}
			}
		}

		// if (timeout == null) {
		// return false;
		// } else if (timeout.isEmpty()) {
		// return false;
		// }

		return true;
	}

	public void setEdit() {

		setTitle(CustomString.getString(DevCoreActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.device.opcua.handler.wizard.deviceconnection.page.edithost.title"));
		setDescription(CustomString.getString(DevCoreActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.device.opcua.handler.wizard.deviceconnection.page.edithost.description"));

		fill();
	}

	@Override
	public void setVisible(boolean visible) {

		if (visible) {
			setValue();
		}

		super.setVisible(visible);
	}

	public IFileSystem getFilesystem() {
		return selectedFilesystem();
	}

	public void setFilesystem(IFileSystem filesystem) {
		if (filesystem instanceof SimpleFileSystem) {
			this.selectedFilesystem.set(0, filesystem);
			this.cmb_type.select(0);
		} else {
			this.selectedFilesystem.set(1, filesystem);
			this.cmb_type.select(1);
		}
	}

	private void fill() {
		if (selectedFilesystem() instanceof SimpleFileSystem) {
			this.cmb_type.select(0);
		} else {
			if (this.permissionSimpleFilesystem) {
				this.cmb_type.select(1);
			} else {
				this.cmb_type.select(0);
			}
		}
		// user permission with simple filesystem and dataHUB
		if (this.permissionSimpleFilesystem) {
			// datahub device
			if (this.cmb_type.getSelectionIndex() == 1) {
				this.txt_host.setEnabled(true);
				this.txt_username.setEnabled(true);
				this.txt_password.setEnabled(true);
				this.txt_fileseparator.setEnabled(false);
				this.txt_rootpath.setEnabled(false);
				this.btnOpenDirectoryDialog.setEnabled(false);
				this.btnCheckConnection.setEnabled(true);
			}
			// simple filesystem
			else {
				this.txt_host.setEnabled(false);
				this.txt_username.setEnabled(false);
				this.txt_password.setEnabled(false);
				this.txt_fileseparator.setEnabled(true);
				this.txt_rootpath.setEnabled(true);
				this.btnOpenDirectoryDialog.setEnabled(true);
				this.btnCheckConnection.setEnabled(false);
			}
		}
		// user permission no simple filesystem
		else {
			this.txt_host.setEnabled(true);
			this.txt_username.setEnabled(true);
			this.txt_password.setEnabled(true);
			this.txt_fileseparator.setEnabled(false);
			this.txt_rootpath.setEnabled(false);
			this.btnOpenDirectoryDialog.setEnabled(false);
			this.btnCheckConnection.setEnabled(true);
		}

		this.txt_connectionName.setText(selectedFilesystem().getConnectionName());
		this.txt_timeout.setText(selectedFilesystem().getTimeOut() + "");
		this.txt_host.setText(selectedFilesystem().getHostName());
		this.txt_username.setText(selectedFilesystem().getUser());
		this.txt_password.setText(selectedFilesystem().getPassword());
		this.txt_rootpath.setText(selectedFilesystem().getRootPath());
		this.txt_fileseparator.setText(selectedFilesystem().getTargetFileSeparator());

		this.txt_host.setData(selectedFilesystem().getHostName());
		this.txt_username.setData(selectedFilesystem().getUser());
		this.txt_password.setData(selectedFilesystem().getPassword());
	}

	private void initializeComboFilesystem() {
		if (this.permissionSimpleFilesystem) {
			cmb_type.setItems(new String[] { ConnectionType.Virtual_Device.getDescription(),
					ConnectionType.SSH.getDescription() });
		} else {
			cmb_type.setItems(new String[] { ConnectionType.SSH.getDescription() });
		}
	}

	private void initializeSelectedFilesystem() {
		if (this.permissionSimpleFilesystem) {
			this.selectedFilesystem = new ArrayList<IFileSystem>(2);
			this.selectedFilesystem.add(null);
			this.selectedFilesystem.add(null);
		} else {
			this.selectedFilesystem = new ArrayList<IFileSystem>(1);
			this.selectedFilesystem.add(null);
		}
	}

	private void setValue() {

		if (this.selectedFilesystem.get(0) == null) {
			IFileSystem fs = new SimpleFileSystem();
			this.selectedFilesystem.add(0, fs);
		}
		if (this.selectedFilesystem.get(1) == null) {
			IFileSystem fs = new DataHubFileSystem();
			fs.setTargetFileSeparator(DATAHUB_SEPERATOR);
			fs.setRootPath(DATAHUB_SEPERATOR);
			this.selectedFilesystem.add(1, fs);

			if (this.onlyDataHubDevices) {
				this.cmb_type.notifyListeners(SWT.Selection, new Event());
			} else if (!this.permissionSimpleFilesystem) {
				this.cmb_type.notifyListeners(SWT.Selection, new Event());
			}
		}

		if (this.isNew && this.isscan) {

//			IStructuredSelection selection = this.page_ScanForDeviceOne.getSelection();
//			if (selection == null) {
//				return;
//			}

			// BroadcastMessage e1 = null;

//			if (selection.getFirstElement() instanceof BroadcastMessage) {
//				e1 = (BroadcastMessage) selection.getFirstElement();
//			} else if (selection.getFirstElement() instanceof Entry) {
//				Entry<String, BroadcastEntry> entry = (Entry<String, BroadcastEntry>) selection.getFirstElement();
//				e1 = (BroadcastMessage) entry.getValue().getParent();
//			}

//			if (e1 == null) {
//				return;
//			}
//
//			this.filesystem = new DataHubFileSystem();
//			this.filesystem.setConnectionName(e1.getConnectionName());
//			// this.filesystem.setHMIRootPath(path);
//			this.filesystem.setHostName(e1.getHost());
//			// this.filesystem.setJavaArg(value);
//			this.filesystem.setUser(e1.getUsername());
//			this.filesystem.setPassword(e1.getPassword());
//			this.filesystem.setTargetFileSeparator(e1.getFileSeparator());
//			this.filesystem.setTimeOut(Integer.parseInt(e1.getConnectionTimeout()));
//			this.filesystem.setRootPath(e1.getRootpath());

//			fill();
		}
	}

	private void setHandler() {
		btnCheckConnection.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				boolean isConnected = selectedFilesystem().connect();

				if (isConnected) {
					MessageDialog.openInformation(getShell(),
							CustomString.getString(DevCoreActivator.getDefault().RESOURCE_BUNDLE,
									"com.bichler.astudio.device.opcua.handler.wizard.deviceconnection.page.connection"),
							CustomString.getString(DevCoreActivator.getDefault().RESOURCE_BUNDLE,
									"com.bichler.astudio.device.opcua.handler.wizard.deviceconnection.page.connection.success"));

				} else {
					MessageDialog.openWarning(getShell(),
							CustomString.getString(DevCoreActivator.getDefault().RESOURCE_BUNDLE,
									"com.bichler.astudio.device.opcua.handler.wizard.deviceconnection.page.connection"),
							CustomString.getString(DevCoreActivator.getDefault().RESOURCE_BUNDLE,
									"com.bichler.astudio.device.opcua.handler.wizard.deviceconnection.page.connection.fail")
									+ " " + selectedFilesystem().getHostName());
				}

			}

		});

		cmb_type.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				comboTypeChange();
			}
		});

		// disable change type when editing device connection properties
		if (!isNew) {
			cmb_type.setEnabled(false);
		}

		btnOpenDirectoryDialog.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				DirectoryDialog dialog = new DirectoryDialog(getShell());
				String path = dialog.open();
				if (path != null) {
					txt_rootpath.setText(path);
					selectedFilesystem().setRootPath(path);
					boolean isComplete = isPageComplete();
					setPageComplete(isComplete);
				}
			}
		});

		txt_connectionName.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				selectedFilesystem().setConnectionName(txt_connectionName.getText());

				boolean isComplete = isPageComplete();
				setPageComplete(isComplete);
			}
		});

		txt_timeout.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				try {
					selectedFilesystem().setTimeOut(Integer.parseInt(txt_timeout.getText()));
				}

				catch (Exception ex) {

				}
				boolean isComplete = isPageComplete();
				setPageComplete(isComplete);
			}
		});

		txt_host.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				selectedFilesystem().setHostName(txt_host.getText());

				boolean isComplete = isPageComplete();
				setPageComplete(isComplete);
			}
		});

		txt_password.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				selectedFilesystem().setPassword(txt_password.getText());

				boolean isComplete = isPageComplete();
				setPageComplete(isComplete);
			}
		});

		txt_fileseparator.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				selectedFilesystem().setTargetFileSeparator(txt_fileseparator.getText());
				boolean isComplete = isPageComplete();
				setPageComplete(isComplete);
			}
		});

		txt_username.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				selectedFilesystem().setUser(txt_username.getText());
				boolean isComplete = isPageComplete();
				setPageComplete(isComplete);
			}
		});

		txt_rootpath.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				selectedFilesystem().setRootPath(txt_rootpath.getText());
				boolean isComplete = isPageComplete();
				setPageComplete(isComplete);
			}
		});
	}

	private void comboTypeChange() {
		IFileSystem fs = selectedFilesystem();
		if (fs == null) {
			if (!this.onlyDataHubDevices) {
				txt_host.setEnabled(false);
				txt_username.setEnabled(false);
				txt_password.setEnabled(false);
			} else {
				txt_fileseparator.setEnabled(false);
				txt_rootpath.setEnabled(false);
				btnOpenDirectoryDialog.setEnabled(false);
			}
		}
		if (fs != null) {
			txt_connectionName.setText(fs.getConnectionName());
			txt_timeout.setText("" + fs.getTimeOut());
			txt_host.setText(fs.getHostName());
			txt_username.setText(fs.getUser());
			txt_password.setText(fs.getPassword());
			txt_fileseparator.setText(fs.getTargetFileSeparator());
			txt_rootpath.setText(fs.getRootPath());

			if (this.permissionSimpleFilesystem) {
				if (cmb_type.getSelectionIndex() == 0) {
					// change filesystem if required
					txt_host.setEnabled(false);
					txt_username.setEnabled(false);
					txt_password.setEnabled(false);
					txt_fileseparator.setEnabled(true);
					txt_rootpath.setEnabled(true);
					btnOpenDirectoryDialog.setEnabled(true);
				} else {
					txt_host.setEnabled(true);
					txt_username.setEnabled(true);
					txt_password.setEnabled(true);
					txt_fileseparator.setEnabled(false);
					txt_rootpath.setEnabled(false);
					btnOpenDirectoryDialog.setEnabled(false);
				}
			} else {
				txt_host.setEnabled(true);
				txt_username.setEnabled(true);
				txt_password.setEnabled(true);
				txt_fileseparator.setEnabled(false);
				txt_rootpath.setEnabled(false);
				btnOpenDirectoryDialog.setEnabled(false);
			}
		}
		boolean isComplete = isPageComplete();
		setPageComplete(isComplete);
	}

	private IFileSystem selectedFilesystem() {
		if (!this.permissionSimpleFilesystem) {
			if (this.selectedFilesystem.size() <= 1) {
				return null;
			}
			return this.selectedFilesystem.get(1);
		}
		return this.selectedFilesystem.get(this.cmb_type.getSelectionIndex());
	}

	private void setOnlyDataHubDevices(boolean onlyDataHubDevices) {
		this.onlyDataHubDevices = onlyDataHubDevices;
	}
}
