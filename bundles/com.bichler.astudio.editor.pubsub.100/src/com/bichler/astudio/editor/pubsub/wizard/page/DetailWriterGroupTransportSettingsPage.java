package com.bichler.astudio.editor.pubsub.wizard.page;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.opcfoundation.ua.core.BrokerTransportQualityOfService;

import com.bichler.astudio.editor.pubsub.wizard.PubSubWriterGroupWizard;
import com.bichler.astudio.editor.pubsub.wizard.core.TransportSettingType;
import com.bichler.astudio.editor.pubsub.wizard.core.WrapperWriterGroupTransport;

public class DetailWriterGroupTransportSettingsPage extends AbstractDetailWizardPage {

	private Composite cmp_writergroup_transport_broker;
	private Composite cmp_writergroup_transport_datagram;

	private Combo cmb_type;
	private Text txt_queueName;
	private Text txt_resourceUri;
	private Text txt_authentificationProfileUri;
	private Combo cmb_requestedDeliveryGuarantee;
	private Text txt_messageRepeatCount;
	private Text txt_messageRepeatDelay;

	private WrapperWriterGroupTransport model = null;

	public DetailWriterGroupTransportSettingsPage() {
		super("transportsettingspage");
		setTitle("Transport settings");
		setDescription("Properties of a transport settings");
	}

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);

		setControl(container);
		container.setLayout(new GridLayout(1, false));

		this.cmb_type = new Combo(container, SWT.NONE);
		cmb_type.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		this.cmp_writergroup_transport_broker = new Composite(container, SWT.NONE);
		cmp_writergroup_transport_broker.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		cmp_writergroup_transport_broker.setLayout(new GridLayout(3, false));

		Label lblQueuename = new Label(cmp_writergroup_transport_broker, SWT.NONE);
		lblQueuename.setText("QueueName");

		txt_queueName = new Text(cmp_writergroup_transport_broker, SWT.BORDER);
		txt_queueName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		new Label(cmp_writergroup_transport_broker, SWT.NONE);

		Label lblResourceuri = new Label(cmp_writergroup_transport_broker, SWT.NONE);
		lblResourceuri.setText("ResourceUri");

		txt_resourceUri = new Text(cmp_writergroup_transport_broker, SWT.BORDER);
		txt_resourceUri.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		new Label(cmp_writergroup_transport_broker, SWT.NONE);

		Label lblAuthentificationprofileuri = new Label(cmp_writergroup_transport_broker, SWT.NONE);
		lblAuthentificationprofileuri.setBounds(0, 0, 70, 20);
		lblAuthentificationprofileuri.setText("AuthentificationProfileUri");

		txt_authentificationProfileUri = new Text(cmp_writergroup_transport_broker, SWT.BORDER);
		txt_authentificationProfileUri.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		new Label(cmp_writergroup_transport_broker, SWT.NONE);

		Label lblRequesteddeliveryguarantee = new Label(cmp_writergroup_transport_broker, SWT.NONE);
		lblRequesteddeliveryguarantee.setText("RequestedDeliveryGuarantee");

		cmb_requestedDeliveryGuarantee = new Combo(cmp_writergroup_transport_broker, SWT.NONE);
		cmb_requestedDeliveryGuarantee.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		new Label(cmp_writergroup_transport_broker, SWT.NONE);

		this.cmp_writergroup_transport_datagram = new Composite(container, SWT.NONE);
		cmp_writergroup_transport_datagram.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		cmp_writergroup_transport_datagram.setLayout(new GridLayout(3, false));

		Label lblMessageRepeatCount = new Label(cmp_writergroup_transport_datagram, SWT.NONE);
		lblMessageRepeatCount.setText("MessageRepeatCount");

		txt_messageRepeatCount = new Text(cmp_writergroup_transport_datagram, SWT.BORDER);
		txt_messageRepeatCount.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		new Label(cmp_writergroup_transport_datagram, SWT.NONE);

		Label lblMessagerepeatdelay = new Label(cmp_writergroup_transport_datagram, SWT.NONE);
		lblMessagerepeatdelay.setText("MessageRepeatDelay");

		txt_messageRepeatDelay = new Text(cmp_writergroup_transport_datagram, SWT.BORDER);
		txt_messageRepeatDelay.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		new Label(cmp_writergroup_transport_datagram, SWT.NONE);

		createApply(container);

		setDefaultValue();
		setHandler();
		// notify cmb type
		this.cmb_type.notifyListeners(SWT.Selection, new Event());
	}

	public WrapperWriterGroupTransport getTransportSettings() {
		return this.model;
	}

	@Override
	public PubSubWriterGroupWizard getWizard() {
		return (PubSubWriterGroupWizard) super.getWizard();
	}

	@Override
	public boolean isPageComplete() {
		TransportSettingType transportSettings = TransportSettingType.valueOf(cmb_type.getText());

		switch (transportSettings) {
		case Broker:
			if (this.txt_queueName.getText().isEmpty()) {
				setErrorMessage("Queuename must be specified!");
				return false;
			}

			if (this.txt_resourceUri.getText().isEmpty()) {
				setErrorMessage("ResourceUri must be specified!");
				return false;
			}

			if (this.txt_authentificationProfileUri.getText().isEmpty()) {
				setErrorMessage("AuthentificationProfileUri must be specified!");
				return false;
			}

			break;
		case Datagram:
			if (this.txt_messageRepeatCount.getText().isEmpty()) {
				setErrorMessage("MessageRepeatCount must be specified!");
				return false;
			}

			if (this.txt_messageRepeatDelay.getText().isEmpty()) {
				setErrorMessage("MessageRepeatDelay must be specified!");
				return false;
			}

			break;
		}

		setErrorMessage(null);
		return super.isPageComplete();
	}

	private void setDefaultValue() {
		// transport type
		String[] items = new String[TransportSettingType.values().length];
		for (int i = 0; i < TransportSettingType.values().length; i++) {
			items[i] = TransportSettingType.values()[i].name();
		}
		this.cmb_type.setItems(items);
		this.cmb_type.select(0);
		// broker transport quality of service
		String[] items2 = new String[BrokerTransportQualityOfService.values().length];
		for (int i = 0; i < BrokerTransportQualityOfService.values().length; i++) {
			items2[i] = BrokerTransportQualityOfService.values()[i].name();
		}
		this.cmb_requestedDeliveryGuarantee.setItems(items2);
		this.cmb_requestedDeliveryGuarantee.select(0);

		WrapperWriterGroupTransport transportSettings = getWizard().getElement().getTransportSettings();
		if (transportSettings != null) {
			TransportSettingType type = transportSettings.getType();
			switch (type) {
			case Datagram:
				this.cmb_type.select(1);
				break;
			case Broker:
			default:
				break;

			}
			
			this.txt_queueName.setText(transportSettings.getQueueName());
			this.txt_resourceUri.setText(transportSettings.getResourceUri());
			this.txt_authentificationProfileUri.setText(transportSettings.getAuthenticationProfileUri());
			this.cmb_requestedDeliveryGuarantee.select(transportSettings.getRequestedDeliveryGuarantee().getValue());
			this.txt_messageRepeatCount.setText(transportSettings.getMessageRepeatCount().toString());
			this.txt_messageRepeatCount.setText(transportSettings.getMessageRepeatDelay().toString());

			this.model = transportSettings.clone();
		}
	}

	private void setHandler() {
		this.txt_authentificationProfileUri.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				boolean complete = isPageComplete();
				setPageComplete(complete);
			}
		});

		this.txt_messageRepeatCount.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				boolean complete = isPageComplete();
				setPageComplete(complete);
			}
		});

		this.txt_messageRepeatDelay.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				boolean complete = isPageComplete();
				setPageComplete(complete);
			}
		});

		this.txt_queueName.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				boolean complete = isPageComplete();
				setPageComplete(complete);
			}
		});

		this.txt_resourceUri.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				boolean complete = isPageComplete();
				setPageComplete(complete);
			}
		});

		this.cmb_type.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				String value = cmb_type.getText();
				switch (TransportSettingType.valueOf(value)) {
				case Broker:
					cmp_writergroup_transport_broker.setVisible(true);
					((GridData) cmp_writergroup_transport_broker.getLayoutData()).exclude = false;
					cmp_writergroup_transport_datagram.setVisible(false);
					((GridData) cmp_writergroup_transport_datagram.getLayoutData()).exclude = true;
					break;
				case Datagram:
					cmp_writergroup_transport_broker.setVisible(false);
					((GridData) cmp_writergroup_transport_broker.getLayoutData()).exclude = true;
					cmp_writergroup_transport_datagram.setVisible(true);
					((GridData) cmp_writergroup_transport_datagram.getLayoutData()).exclude = false;
					break;
				}

				Composite parent = (Composite) getControl();
				parent.layout(true, true);
			}
		});
	}

}
