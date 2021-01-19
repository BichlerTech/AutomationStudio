package com.bichler.astudio.editor.pubsub.wizard.page;

import java.util.List;

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
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.opcfoundation.ua.core.KeyValuePair;

import com.bichler.astudio.editor.pubsub.nodes.PubSubConnection;
import com.bichler.astudio.editor.pubsub.nodes.PubSubEntryModelNode;
import com.bichler.astudio.editor.pubsub.nodes.PubSubPublisherIdType;
import com.bichler.astudio.editor.pubsub.wizard.PubSubConnectionWizard;
import com.bichler.astudio.editor.pubsub.wizard.PubSubDataSetFieldWizard;

public class PubSubConnectionPage extends AbstractMainWizardPage {
	private Text txt_name;
	private Text txt_publisherId;
	private Text txt_transportProfileUri;
	private Text txt_address;
	private Text txt_connectionsProperties;
	private Text txt_connectionTransportSettings;
	private Button btn_checkEnabled;
	private Button btn_checkConfigurationFrozen;
	private Combo cmb_publisherType;
	private Button btn_connectionProperties;
	private Button btn_connectionTransportSettings;
	private Button btn_address;

	public PubSubConnectionPage() {
		super("pubsubconnectionWizardPage");
		setTitle("PubSubConnection");
		setDescription("Properties of a PubSubConnection");
	}

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);

		setControl(container);
		container.setLayout(new GridLayout(4, false));

		Label lbl_name = new Label(container, SWT.NONE);
		lbl_name.setText("Name:");

		txt_name = new Text(container, SWT.BORDER);
		txt_name.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));

		new Label(container, SWT.NONE);

		Label lbl_enabled = new Label(container, SWT.NONE);
		lbl_enabled.setText("Enabled:");
		lbl_enabled.setLocation(0, 0);

		this.btn_checkEnabled = new Button(container, SWT.CHECK);
		btn_checkEnabled.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));
		btn_checkEnabled.setBounds(0, 0, 111, 20);

		new Label(container, SWT.NONE);

		Label lbl_publisherIdType = new Label(container, SWT.NONE);
		lbl_publisherIdType.setText("PublisherIdType:");
		lbl_publisherIdType.setLocation(0, 0);

		this.cmb_publisherType = new Combo(container, SWT.BORDER);
		cmb_publisherType.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));

		new Label(container, SWT.NONE);

		Label lbl_publisherId = new Label(container, SWT.NONE);
		lbl_publisherId.setText("PublisherId:");
		lbl_publisherId.setLocation(0, 0);

		txt_publisherId = new Text(container, SWT.BORDER);
		txt_publisherId.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));

		new Label(container, SWT.NONE);

		Label lblTransportprofileuri = new Label(container, SWT.NONE);
		lblTransportprofileuri.setText("TransportProfileUri:");

		txt_transportProfileUri = new Text(container, SWT.BORDER);
		txt_transportProfileUri.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));

		new Label(container, SWT.NONE);

		Label lbl_address = new Label(container, SWT.NONE);
		lbl_address.setText("Address:");
		lbl_address.setLocation(0, 0);

		txt_address = new Text(container, SWT.BORDER);
		txt_address.setEnabled(false);
		txt_address.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		txt_address.setBounds(0, 0, 78, 26);

		this.btn_address = new Button(container, SWT.NONE);
		btn_address.setBounds(0, 0, 90, 30);
		btn_address.setText("  ");

		Label lblConnectionProperties = new Label(container, SWT.NONE);
		lblConnectionProperties.setText("ConnectionsProperties:");

		txt_connectionsProperties = new Text(container, SWT.BORDER);
		txt_connectionsProperties.setEnabled(false);
		txt_connectionsProperties.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));

		this.btn_connectionProperties = new Button(container, SWT.NONE);
		btn_connectionProperties.setText("  ");

		Label lblConnectionTransportSettings = new Label(container, SWT.NONE);
		lblConnectionTransportSettings.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblConnectionTransportSettings.setText("ConnectionTransportSettings:");

		txt_connectionTransportSettings = new Text(container, SWT.BORDER);
		txt_connectionTransportSettings.setEnabled(false);
		txt_connectionTransportSettings.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));

		this.btn_connectionTransportSettings = new Button(container, SWT.NONE);
		btn_connectionTransportSettings.setText("  ");

		Label lblNewLabel = new Label(container, SWT.NONE);
		lblNewLabel.setText("ConfigurationFrozen:");

		this.btn_checkConfigurationFrozen = new Button(container, SWT.CHECK);
		btn_checkConfigurationFrozen.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));

		new Label(container, SWT.NONE);

		setDefaultValues();
		setHandler();
	}

	/**
	 * Set default values to page controls
	 */
	private void setDefaultValues() {
		this.cmb_publisherType.setItems(new String[] { PubSubPublisherIdType.UA_PUBSUB_PUBLISHERID_NUMERIC.name(),
				PubSubPublisherIdType.UA_PUBSUB_PUBLISHERID_STRING.name() });
		
		if (getWizard().getElement().getPublisherIdType() != null) {
			String name = getWizard().getElement().getPublisherIdType().name();
			int index = -1;
			for (int i = 0; i < this.cmb_publisherType.getItems().length; i++) {
				String type = this.cmb_publisherType.getItems()[i];
				if (type.equals(name)) {
					index = i;
					break;
				}
			}
			this.cmb_publisherType.select(index);
		} else {
			this.cmb_publisherType.select(1);
		}

		if (getWizard().getElement().getName() != null) {
			this.txt_name.setText(getWizard().getElement().getName());
		}

		if (getWizard().getElement().getEnabled() != null) {
			this.btn_checkEnabled.setSelection(getWizard().getElement().getEnabled());
		}

		if (getWizard().getElement().getPublisherId() != null) {
			this.txt_publisherId.setText(getWizard().getElement().getPublisherId().toString());
		}

		if (getWizard().getElement().getTransportProfileUri() != null) {
			this.txt_transportProfileUri.setText(getWizard().getElement().getTransportProfileUri());
		}

		// this.txt_connectionsPropertiesSize.setText("" + getWizard().getElement().getConnectionProperties().size());

		if (getWizard().getElement().getConfigurationFrozen() != null) {
			this.btn_checkConfigurationFrozen.setSelection(getWizard().getElement().getConfigurationFrozen());
		}
	}

	/**
	 * Sets handlers to page controls
	 */
	private void setHandler() {
		this.txt_name.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				boolean isPageComplete = isPageComplete();
				setPageComplete(isPageComplete);
			}
		});

		this.txt_publisherId.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				boolean isPageComplete = isPageComplete();
				setPageComplete(isPageComplete);
			}
		});

		this.cmb_publisherType.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				boolean isPageComplete = isPageComplete();
				setPageComplete(isPageComplete);
			}
		});

		this.txt_transportProfileUri.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				boolean isPageComplete = isPageComplete();
				setPageComplete(isPageComplete);
			}
		});

		/*this.txt_connectionsPropertiesSize.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				boolean isPageComplete = isPageComplete();
				setPageComplete(isPageComplete);
			}
		});*/

		this.btn_address.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				getContainer().showPage(getWizard().pageTwo);
			}

		});

		this.btn_connectionProperties.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				getContainer().showPage(getWizard().pageFour);
			}

		});
		
		this.btn_connectionTransportSettings.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				getContainer().showPage(getWizard().pageThree);
			}

		});
	}

	@Override
	public PubSubConnectionWizard getWizard() {
		return (PubSubConnectionWizard) super.getWizard();
	}

	@Override
	public boolean isPageComplete() {
		// field name
		String name = this.txt_name.getText();
		if (name.isEmpty()) {
			setErrorMessage("Name must be specified!");
			return false;
		}

		// field publisherId
		String publisherId = this.txt_publisherId.getText();
		if (publisherId.isEmpty()) {
			setErrorMessage("PublisherId must be specified!");
			return false;
		}
		// field publisherIdType
		try {
			PubSubPublisherIdType publisherType = PubSubPublisherIdType.valueOf(this.cmb_publisherType.getText());
			switch (publisherType) {
			case UA_PUBSUB_PUBLISHERID_NUMERIC:
				Integer.parseInt(publisherId);
				break;
			default:
				break;
			}
		} catch (NumberFormatException e) {
			setErrorMessage("Type of field PublisherId is an Int32!");
			return false;
		}
		// field transportprofileUri
		String transportProfileUri = this.txt_transportProfileUri.getText();
		if (transportProfileUri.isEmpty()) {
			setErrorMessage("TransportProfileUri must be specified!");
			return false;
		}
		// field address (disabled)
		// this.txt_address.getText();

		// field connectionPropertiesSize
		/*String connectionPropertiesSize = this.txt_connectionsPropertiesSize.getText();
		if (connectionPropertiesSize.isEmpty()) {
			setErrorMessage("ConnectionPropertiesSize must be specified!");
			return false;
		}
		try {
			Integer.parseInt(connectionPropertiesSize);
		} catch (NumberFormatException e) {
			setErrorMessage("Type of field ConnectionPropertiesSize is an Int32!");
			return false;
		}*/

		// this.txt_connectionTransportSettings.getText();
		setErrorMessage(null);
		return true;
	}

	public String getElementName() {
		return this.txt_name.getText();
	}

	public Boolean isEnabled() {
		return this.btn_checkEnabled.getSelection();
	}

	public PubSubPublisherIdType getPublisherIdType() {
		return PubSubPublisherIdType.valueOf(this.cmb_publisherType.getText());
	}

//  union { /* std: valid types UInt or String */
//  UA_UInt32 numeric;
//  UA_String string;
//} publisherId;
	/**
	 * PublisherId
	 * 
	 * @return an Integer or String
	 */
	public Object getPublisherId() {
		String publisherId = this.txt_publisherId.getText();
		PubSubPublisherIdType publisherType = PubSubPublisherIdType.valueOf(this.cmb_publisherType.getText());
		switch (publisherType) {
		case UA_PUBSUB_PUBLISHERID_NUMERIC:
			// Integer
			return Integer.parseInt(publisherId);
		default:
			// String
			return publisherId;
		}
	}

	public String getTransportProfileUri() {
		return this.txt_transportProfileUri.getText();
	}

	public Boolean isConfigurationFrozen() {
		return this.btn_checkConfigurationFrozen.getSelection();
	}

	public List<KeyValuePair> getConnectionProperties() {
		return null;
	}


}
