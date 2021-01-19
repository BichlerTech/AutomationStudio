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
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.opcfoundation.ua.core.BrokerTransportQualityOfService;

import com.bichler.astudio.editor.pubsub.wizard.PubSubDataSetWriterWizard;
import com.bichler.astudio.editor.pubsub.wizard.core.WrapperWriterDataSetTransport;

public class DetailDataSetWriterTransportSettingsPage extends AbstractDetailWizardPage {

	private Composite cmp_writerdataset_transport_broker;
	private Text txt_queueName;
	private Text txt_resourceUri;
	private Text txt_authentificationProfileUri;
	private Combo cmb_requestedDeliveryGuarantee;
	private Text txt_metadataQueueName;
	private Text txt_metadataUpdateTime;

	private WrapperWriterDataSetTransport model;

	public DetailDataSetWriterTransportSettingsPage() {
		super("transportsettingspage");
		setTitle("Transport settings");
		setDescription("Properties of a transport settings");
	}

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);

		setControl(container);
		container.setLayout(new GridLayout(1, false));

		this.cmp_writerdataset_transport_broker = new Composite(container, SWT.NONE);
		cmp_writerdataset_transport_broker.setLayout(new GridLayout(2, false));
		cmp_writerdataset_transport_broker.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblQueuename = new Label(cmp_writerdataset_transport_broker, SWT.NONE);
		lblQueuename.setText("QueueName");

		txt_queueName = new Text(cmp_writerdataset_transport_broker, SWT.BORDER);
		txt_queueName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblResourceuri = new Label(cmp_writerdataset_transport_broker, SWT.NONE);
		lblResourceuri.setText("ResourceUri");

		txt_resourceUri = new Text(cmp_writerdataset_transport_broker, SWT.BORDER);
		txt_resourceUri.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblAuthentificationprofileuri = new Label(cmp_writerdataset_transport_broker, SWT.NONE);
		lblAuthentificationprofileuri.setText("AuthentificationProfileUri");

		txt_authentificationProfileUri = new Text(cmp_writerdataset_transport_broker, SWT.BORDER);
		txt_authentificationProfileUri.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblRequesteddeliveryguarantee = new Label(cmp_writerdataset_transport_broker, SWT.NONE);
		lblRequesteddeliveryguarantee.setText("RequestedDeliveryGuarantee");

		cmb_requestedDeliveryGuarantee = new Combo(cmp_writerdataset_transport_broker, SWT.READ_ONLY);
		cmb_requestedDeliveryGuarantee.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblMetadataqueuename = new Label(cmp_writerdataset_transport_broker, SWT.NONE);
		lblMetadataqueuename.setText("MetadataQueueName");

		txt_metadataQueueName = new Text(cmp_writerdataset_transport_broker, SWT.BORDER);
		txt_metadataQueueName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblMetadataupdatetime = new Label(cmp_writerdataset_transport_broker, SWT.NONE);
		lblMetadataupdatetime.setText("MetadataUpdateTime");

		txt_metadataUpdateTime = new Text(cmp_writerdataset_transport_broker, SWT.BORDER);
		txt_metadataUpdateTime.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		createApply(container);

		setDefaultValue();
		setHandler();
	}

	@Override
	public boolean isPageComplete() {
		if (this.txt_queueName.getText().isEmpty()) {
			setErrorMessage("QueueName must be specified!");
		} else if (this.txt_resourceUri.getText().isEmpty()) {
			setErrorMessage("ResourceUri must be specified!");
		} else if (this.txt_authentificationProfileUri.getText().isEmpty()) {
			setErrorMessage("AuthentificationProfileUri must be specified!");
		} else if (this.txt_metadataQueueName.getText().isEmpty()) {
			setErrorMessage("Metadata QueueName must be specified!");
		} else if (this.txt_metadataUpdateTime.getText().isEmpty()) {
			setErrorMessage("Metadata Updatetime must be specified!");
		} else if (!this.txt_metadataUpdateTime.getText().isEmpty()) {
			try {
				Double.parseDouble(this.txt_metadataUpdateTime.getText());
			} catch (NumberFormatException nfe) {
				setErrorMessage("Type of field Metadata UpdateTime is a Double!");
			}
		}
		return super.isPageComplete();
	}

	public WrapperWriterDataSetTransport getTransportSettings() {
		return this.model;
	}

	@Override
	public PubSubDataSetWriterWizard getWizard() {
		return (PubSubDataSetWriterWizard) super.getWizard();
	}

	private void setDefaultValue() {
		WrapperWriterDataSetTransport transportSettings = getWizard().getElement().getTransportSettings();
		if (transportSettings != null) {
			if (transportSettings.getAuthenticationProfileUri() != null) {
				txt_authentificationProfileUri.setText(transportSettings.getAuthenticationProfileUri());
			}
			if (transportSettings.getMetaDataQueueName() != null) {
				txt_metadataQueueName.setText(transportSettings.getMetaDataQueueName());
			}
			if (transportSettings.getMetaDataUpdateTime() != null) {
				txt_metadataUpdateTime.setText(transportSettings.getMetaDataUpdateTime().toString());
			}
			if (transportSettings.getQueueName() != null) {
				txt_queueName.setText(transportSettings.getQueueName());
			}
			if (transportSettings.getResourceUri() != null) {
				txt_resourceUri.setText(transportSettings.getResourceUri());
			}
			if (transportSettings.getRequestedDeliveryGuarantee() != null) {
				cmb_requestedDeliveryGuarantee.select(transportSettings.getRequestedDeliveryGuarantee().getValue());
			}

			this.model = transportSettings.clone();
		}
	}

	private void setHandler() {
		txt_queueName.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				boolean complete = isPageComplete();
				setPageComplete(complete);
			}
		});

		txt_resourceUri.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				boolean complete = isPageComplete();
				setPageComplete(complete);
			}
		});

		txt_authentificationProfileUri.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				boolean complete = isPageComplete();
				setPageComplete(complete);
			}
		});

		txt_metadataUpdateTime.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				boolean complete = isPageComplete();
				setPageComplete(complete);
			}
		});

		txt_metadataQueueName.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				boolean complete = isPageComplete();
				setPageComplete(complete);
			}
		});

		btn_apply.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				model = new WrapperWriterDataSetTransport();
				model.setAuthenticationProfileUri(getAuthenticationProfileUri());
				model.setMetaDataQueueName(getMetaDataQueueName());
				model.setMetaDataUpdateTime(getMetaDataUpdateTime());
				model.setQueueName(getQueueName());
				model.setRequestedDeliveryGuarantee(getRequestedDeliveryGuarantee());
				model.setResourceUri(getResourceUri());
				//model = new BrokerDataSetWriterTransportDataType(getQueueName(), getResourceUri(),
				//		getAuthenticationProfileUri(), getRequestedDeliveryGuarantee(), getMetaDataQueueName(),
				//		getMetaDataUpdateTime());
			}
		});
	}

	private Double getMetaDataUpdateTime() {
		return new Double(this.txt_metadataUpdateTime.getText());
	}

	private String getMetaDataQueueName() {
		return this.txt_metadataQueueName.getText();
	}

	private BrokerTransportQualityOfService getRequestedDeliveryGuarantee() {
		return BrokerTransportQualityOfService.valueOf(this.cmb_requestedDeliveryGuarantee.getSelectionIndex());
	}

	private String getAuthenticationProfileUri() {
		return this.txt_authentificationProfileUri.getText();
	}

	private String getResourceUri() {
		return this.txt_resourceUri.getText();
	}

	private String getQueueName() {
		return this.txt_queueName.getText();
	}
}
