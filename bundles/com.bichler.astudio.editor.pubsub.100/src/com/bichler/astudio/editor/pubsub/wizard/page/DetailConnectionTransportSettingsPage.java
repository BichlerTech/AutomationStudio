package com.bichler.astudio.editor.pubsub.wizard.page;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.ExtensionObject;

import com.bichler.astudio.editor.pubsub.wizard.PubSubConnectionWizard;
import com.bichler.astudio.editor.pubsub.wizard.core.TransportSettingType;
import com.bichler.astudio.editor.pubsub.wizard.core.WrapperConnectionTransportSetting;

public class DetailConnectionTransportSettingsPage extends AbstractDetailWizardPage {

	private Text txt_resourceUri;
	private Text txt_authentificationProfileUri;
	private Text txt_discoveryAddress;
	private Button btn_discoveryAddress;
	private Combo cmb_type;
	private Composite cmp_contransport_broker;
	private Composite cmp_contransport_datagram;

	private WrapperConnectionTransportSetting model = null;

	public DetailConnectionTransportSettingsPage() {
		super("connectiontransportsettingspage");
		setTitle("Connection transport settings");
		setDescription("Properties of connection transport settings");
	}

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);

		setControl(container);
		container.setLayout(new GridLayout(2, false));

		Label lblType = new Label(container, SWT.NONE);
		lblType.setText("Type");

		this.cmb_type = new Combo(container, SWT.READ_ONLY);
		cmb_type.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		this.cmp_contransport_broker = new Composite(container, SWT.NONE);
		cmp_contransport_broker.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		cmp_contransport_broker.setLayout(new GridLayout(3, false));

		Label lblResourceUri = new Label(cmp_contransport_broker, SWT.NONE);
		lblResourceUri.setBounds(0, 0, 70, 20);
		lblResourceUri.setText("ResourceUri");

		this.txt_resourceUri = new Text(cmp_contransport_broker, SWT.BORDER);
		txt_resourceUri.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		new Label(cmp_contransport_broker, SWT.NONE);

		Label lblAuthentificationprofileuri = new Label(cmp_contransport_broker, SWT.NONE);
		lblAuthentificationprofileuri.setBounds(0, 0, 70, 20);
		lblAuthentificationprofileuri.setText("AuthentificationProfileUri");

		this.txt_authentificationProfileUri = new Text(cmp_contransport_broker, SWT.BORDER);
		txt_authentificationProfileUri.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		new Label(cmp_contransport_broker, SWT.NONE);

		this.cmp_contransport_datagram = new Composite(container, SWT.NONE);
		cmp_contransport_datagram.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		cmp_contransport_datagram.setLayout(new GridLayout(3, false));

		Label lblDiscoveryAddress = new Label(cmp_contransport_datagram, SWT.NONE);
		lblDiscoveryAddress.setBounds(0, 0, 70, 20);
		lblDiscoveryAddress.setText("DiscoveryAddress");

		this.txt_discoveryAddress = new Text(cmp_contransport_datagram, SWT.BORDER);
		txt_discoveryAddress.setEnabled(false);
		txt_discoveryAddress.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		this.btn_discoveryAddress = new Button(cmp_contransport_datagram, SWT.PUSH);
		btn_discoveryAddress.setText(" ");

		createApply(container);

		setDefaultValues();
		setHandler();

		// notify cmb type
		this.cmb_type.notifyListeners(SWT.Selection, new Event());
	}

	@Override
	public PubSubConnectionWizard getWizard() {
		return (PubSubConnectionWizard) super.getWizard();
	}

	public WrapperConnectionTransportSetting getConnectionTransportSettings() {
		/*
		 * if(this.model == null) { return null; }
		 */
		// return new Variant(this.model);
		return this.model;
	}

	private void setDefaultValues() {
		// connection type
		String[] items = new String[TransportSettingType.values().length];
		for (int i = 0; i < TransportSettingType.values().length; i++) {
			items[i] = TransportSettingType.values()[i].name();
		}
		this.cmb_type.setItems(items);
		this.cmb_type.select(0);

		if (getWizard().getElement().getConnectionTransportSettings() != null) {
			// connection
			WrapperConnectionTransportSetting transportSettings = getWizard().getElement()
					.getConnectionTransportSettings();

			this.model = transportSettings.clone();

			switch (this.model.getType()) {
			case Broker:
				String authentificationProfileUri = transportSettings.getAuthenticationProfileUri();
				String resourceUri = transportSettings.getResourceUri();
				// authentificationprofile uri
				if (authentificationProfileUri != null) {
					this.txt_authentificationProfileUri.setText(authentificationProfileUri);
				}
				// resource uri
				if (resourceUri != null) {
					this.txt_resourceUri.setText(resourceUri);
				}
				break;
			case Datagram:
				ExtensionObject discoveryAddress = transportSettings.getDiscoveryAddress();

				this.cmb_type.select(1);
				break;
			}
		}
	}

	private void setHandler() {
		this.cmb_type.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				String value = cmb_type.getText();
				switch (TransportSettingType.valueOf(value)) {
				case Broker:
					cmp_contransport_broker.setVisible(true);
					((GridData) cmp_contransport_broker.getLayoutData()).exclude = false;
					cmp_contransport_datagram.setVisible(false);
					((GridData) cmp_contransport_datagram.getLayoutData()).exclude = true;
					break;
				case Datagram:
					cmp_contransport_broker.setVisible(false);
					((GridData) cmp_contransport_broker.getLayoutData()).exclude = true;
					cmp_contransport_datagram.setVisible(true);
					((GridData) cmp_contransport_datagram.getLayoutData()).exclude = false;
					break;
				}

				Composite parent = (Composite) getControl();
				parent.layout(true, true);
			}

		});

		this.btn_discoveryAddress.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

			}
		});
		if (this.btn_apply != null) {
			this.btn_apply.addSelectionListener(new SelectionAdapter() {

				@Override
				public void widgetSelected(SelectionEvent e) {
					String value = cmb_type.getText();
					WrapperConnectionTransportSetting cts = new WrapperConnectionTransportSetting();

					switch (TransportSettingType.valueOf(value)) {
					case Broker:
						cts.setType(TransportSettingType.Broker);
						cts.setResourceUri(txt_resourceUri.getText());
						cts.setAuthenticationProfileUri(txt_authentificationProfileUri.getText());
						break;
					case Datagram:
						cts.setType(TransportSettingType.Datagram);
						cts.setDiscoveryAddress(getDiscoveryAddress());
						break;
					}
					model = cts;
				}

			});
		}
	}

	private ExtensionObject getDiscoveryAddress() {
		return new ExtensionObject(ExpandedNodeId.NULL);
	}
}
