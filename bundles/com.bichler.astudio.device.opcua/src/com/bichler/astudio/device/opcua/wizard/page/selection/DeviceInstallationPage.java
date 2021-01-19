package com.bichler.astudio.device.opcua.wizard.page.selection;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Text;

import com.bichler.astudio.device.opcua.DeviceActivator;
import com.bichler.astudio.utils.internationalization.CustomString;

public class DeviceInstallationPage extends WizardPage {

	private Button cbInternal;
	private Button cbExternal;
	private Group groupExternal;
	private Text txtExternalPath;
	private Button btnOpenExternal;
	private boolean isInternalRuntime = true;
	protected String externalPath;

	/**
	 * Create the wizard.
	 */
	public DeviceInstallationPage() {
		super("wizardPage");
		setTitle(CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.device.opcua.handler.target.install.monitor.title"));
		setDescription(CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.device.opcua.handler.wizard.deviceinstall.page.message"));
	}

	/**
	 * Create contents of the wizard.
	 * 
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);

		setControl(container);
		container.setLayout(new GridLayout(1, false));

		createPart(container);

		setHandler();

		init();
	}

	private void init() {
		this.cbInternal.notifyListeners(SWT.Selection, new Event());
	}

	private void createPart(Composite container) {
		this.cbInternal = new Button(container, SWT.RADIO);
		cbInternal.setSelection(true);
		cbInternal.setText(CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.device.opcua.handler.wizard.deviceinstall.page.cbintern"));

		this.cbExternal = new Button(container, SWT.RADIO);
		cbExternal.setText(CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.device.opcua.handler.wizard.deviceinstall.page.cbextern"));

		this.groupExternal = new Group(container, SWT.NONE);
		groupExternal.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		groupExternal.setLayout(new GridLayout(3, false));

		Label lblPfad = new Label(groupExternal, SWT.NONE);
		lblPfad.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPfad.setText(CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.device.opcua.simulation.form.application.path"));

		txtExternalPath = new Text(groupExternal, SWT.BORDER);
		txtExternalPath.setEditable(false);
		txtExternalPath.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		btnOpenExternal = new Button(groupExternal, SWT.NONE);
		btnOpenExternal.setText(CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.device.opcua.handler.wizard.deviceinstall.page.browse"));

	}

	private void setHandler() {

		this.cbInternal.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				// disable group
				groupExternal.setVisible(false);
				groupExternal.setEnabled(false);
				isInternalRuntime = true;
			}

		});

		this.cbExternal.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				// enable group
				groupExternal.setVisible(true);
				groupExternal.setEnabled(true);
				isInternalRuntime = false;
			}
		});

		this.btnOpenExternal.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog dialog = new FileDialog(getShell(), SWT.OPEN);
				dialog.setFilterExtensions(new String[] { "*.zip" });
				String open = dialog.open();
				if (open != null) {
					txtExternalPath.setEditable(true);
					txtExternalPath.setText(open);
					txtExternalPath.setEditable(false);
					externalPath = open;
				}
			}
		});
	}

	public boolean isInternalRuntime() {
		return this.isInternalRuntime;
	}

	public String getExternalPath() {
		return this.externalPath;
	}

}
