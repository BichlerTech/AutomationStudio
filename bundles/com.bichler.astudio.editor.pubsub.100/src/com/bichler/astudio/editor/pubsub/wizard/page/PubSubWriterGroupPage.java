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
import org.opcfoundation.ua.builtintypes.ExtensionObject;
import org.opcfoundation.ua.core.KeyValuePair;
import org.opcfoundation.ua.core.MessageSecurityMode;
import org.opcfoundation.ua.core.WriterGroupMessageDataType;
import org.opcfoundation.ua.core.WriterGroupTransportDataType;

import com.bichler.astudio.editor.pubsub.nodes.PubSubEncodingType;
import com.bichler.astudio.editor.pubsub.nodes.PubSubEntryModelNode;
import com.bichler.astudio.editor.pubsub.nodes.PubSubRTLevel;
import com.bichler.astudio.editor.pubsub.nodes.PubSubWriterGroup;
import com.bichler.astudio.editor.pubsub.wizard.PubSubWriterGroupWizard;
import com.bichler.astudio.editor.pubsub.wizard.core.WrapperWriterGroupTransport;

public class PubSubWriterGroupPage extends AbstractMainWizardPage {
	private Text txt_name;
	private Text txt_writerGroupId;
	private Text txt_publishingInterval;
	private Text txt_keepAliveTime;
	private Text txt_priority;

	//private Text txt_groupPropertiesSize;
	private Text txt_messageSettings;
	private Text txt_transportSettings;
	private Text txt_groupProperties;
	private Text txt_maxEncapsulationDataSetMessageCount;

	private Button btn_checkConfigurationFrozen;
	private Button btn_checkEnabled;
	private Combo cmb_encodingType;
	private Combo cmb_securityMode;
	private Combo cmb_rtLevel;
	
	private Button btn_messageSettings;
	private Button btn_transportSettings;
	private Button btn_groupProperties;

	public PubSubWriterGroupPage() {
		super("pubsubwritergroupWizardPage");
		setTitle("PubSubWriterGroup");
		setDescription("Properties of a PubSubWriterGroup");
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

		Label lbl_dataSetWriterGroupId = new Label(container, SWT.NONE);
		lbl_dataSetWriterGroupId.setText("DataSetWriterGroupId:");
		lbl_dataSetWriterGroupId.setLocation(0, 0);

		txt_writerGroupId = new Text(container, SWT.BORDER);
		txt_writerGroupId.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));

		new Label(container, SWT.NONE);

		Label lbl_publishingInterval = new Label(container, SWT.NONE);
		lbl_publishingInterval.setText("Publishing interval:");
		lbl_publishingInterval.setLocation(0, 0);

		txt_publishingInterval = new Text(container, SWT.BORDER);
		txt_publishingInterval.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));

		new Label(container, SWT.NONE);

		Label lbl_keepAliveTime = new Label(container, SWT.NONE);
		lbl_keepAliveTime.setText("KeepAlive time:");
		lbl_keepAliveTime.setLocation(0, 0);

		txt_keepAliveTime = new Text(container, SWT.BORDER);
		txt_keepAliveTime.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));

		new Label(container, SWT.NONE);

		Label lbl_priority = new Label(container, SWT.NONE);
		lbl_priority.setText("Priority:");
		lbl_priority.setLocation(0, 0);

		txt_priority = new Text(container, SWT.BORDER);
		txt_priority.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));

		new Label(container, SWT.NONE);

		Label lbl_messageSecurity = new Label(container, SWT.NONE);
		lbl_messageSecurity.setText("Message security:");
		lbl_messageSecurity.setLocation(0, 0);

		this.cmb_securityMode = new Combo(container, SWT.BORDER);
		cmb_securityMode.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));

		new Label(container, SWT.NONE);

		Label lblMessageSettings = new Label(container, SWT.NONE);
		lblMessageSettings.setText("Message settings:");

		txt_messageSettings = new Text(container, SWT.BORDER);
		txt_messageSettings.setEnabled(false);
		txt_messageSettings.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));

		this.btn_messageSettings = new Button(container, SWT.NONE);
		btn_messageSettings.setText("  ");

		Label lblTransportSettings = new Label(container, SWT.NONE);
		lblTransportSettings.setText("Transport settings:");

		txt_transportSettings = new Text(container, SWT.BORDER);
		txt_transportSettings.setEnabled(false);
		txt_transportSettings.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));

		this.btn_transportSettings = new Button(container, SWT.NONE);
		btn_transportSettings.setText("  ");

		// Label lblGroupPropertiesSize = new Label(container, SWT.NONE);
		// lblGroupPropertiesSize.setText("GroupPropertiesSize:");

		// txt_groupPropertiesSize = new Text(container, SWT.BORDER);
		// txt_groupPropertiesSize.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));

		// new Label(container, SWT.NONE);

		Label lbl_groupProperties = new Label(container, SWT.NONE);
		lbl_groupProperties.setText("Group properties:");

		txt_groupProperties = new Text(container, SWT.BORDER);
		txt_groupProperties.setEnabled(false);
		txt_groupProperties.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));

		this.btn_groupProperties = new Button(container, SWT.NONE);
		btn_groupProperties.setText("  ");

		Label lbl_encodingType = new Label(container, SWT.NONE);
		lbl_encodingType.setText("Encoding type:");
		lbl_encodingType.setLocation(0, 0);

		this.cmb_encodingType = new Combo(container, SWT.BORDER);
		cmb_encodingType.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));

		new Label(container, SWT.NONE);

		Label lbl_maxEncapsulationDataSetMessageCount = new Label(container, SWT.NONE);
		lbl_maxEncapsulationDataSetMessageCount.setText("MaxEncapsulationDataSetMessage count:");

		txt_maxEncapsulationDataSetMessageCount = new Text(container, SWT.BORDER);
		txt_maxEncapsulationDataSetMessageCount.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));

		new Label(container, SWT.NONE);

		Label lbl_rtLevel = new Label(container, SWT.NONE);
		lbl_rtLevel.setText("RT level:");
		lbl_rtLevel.setLocation(0, 0);

		this.cmb_rtLevel = new Combo(container, SWT.BORDER);
		cmb_rtLevel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));

		new Label(container, SWT.NONE);

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
		this.cmb_securityMode.setItems(new String[] { MessageSecurityMode.None.name(), MessageSecurityMode.Sign.name(),
				MessageSecurityMode.SignAndEncrypt.name(), MessageSecurityMode.None.name() });
		if (getWizard().getElement().getSecurityMode() != null) {
			String name = getWizard().getElement().getSecurityMode().name();
			int index = -1;
			for (int i = 0; i < this.cmb_securityMode.getItems().length; i++) {
				String type = this.cmb_securityMode.getItems()[i];
				if (type.equals(name)) {
					index = i;
					break;
				}
			}
			this.cmb_securityMode.select(index);
		} else {
			this.cmb_securityMode.select(0);
		}

		this.cmb_encodingType.setItems(new String[] { PubSubEncodingType.UA_PUBSUB_ENCODING_BINARY.name(),
				PubSubEncodingType.UA_PUBSUB_ENCODING_JSON.name(), PubSubEncodingType.UA_PUBSUB_ENCODING_UADP.name() });
		if (getWizard().getElement().getEncodingMimeType() != null) {
			String name = getWizard().getElement().getEncodingMimeType().name();
			int index = -1;
			for (int i = 0; i < this.cmb_encodingType.getItems().length; i++) {
				String type = this.cmb_encodingType.getItems()[i];
				if (type.equals(name)) {
					index = i;
					break;
				}
			}
			this.cmb_encodingType.select(index);
		} else {
			this.cmb_encodingType.select(0);
		}

		this.cmb_rtLevel.setItems(new String[] { PubSubRTLevel.UA_PUBSUB_RT_NONE.name(),
				PubSubRTLevel.UA_PUBSUB_RT_DETERMINISTIC.name(), PubSubRTLevel.UA_PUBSUB_RT_DIRECT_VALUE_ACCESS.name(),
				PubSubRTLevel.UA_PUBSUB_RT_FIXED_SIZE.name() });
		if (getWizard().getElement().getRtLevel() != null) {
			String name = getWizard().getElement().getRtLevel().name();
			int index = -1;
			for (int i = 0; i < this.cmb_rtLevel.getItems().length; i++) {
				String type = this.cmb_rtLevel.getItems()[i];
				if (type.equals(name)) {
					index = i;
					break;
				}
			}
			this.cmb_rtLevel.select(index);
		} else {
			this.cmb_rtLevel.select(0);
		}

		if (getWizard().getElement().getName() != null) {
			this.txt_name.setText(getWizard().getElement().getName());
		}

		if (getWizard().getElement().getEnabled() != null) {
			this.btn_checkEnabled.setSelection(getWizard().getElement().getEnabled());
		}

		if (getWizard().getElement().getWriterGroupId() != 0) {
			this.txt_writerGroupId.setText("" + getWizard().getElement().getWriterGroupId());
		}

		if (getWizard().getElement().getPublishingInterval() != null) {
			this.txt_publishingInterval.setText(getWizard().getElement().getPublishingInterval().toString());
		}

		if (getWizard().getElement().getKeepAliveTime() != null) {
			this.txt_keepAliveTime.setText(getWizard().getElement().getKeepAliveTime().toString());
		}

		if (getWizard().getElement().getPriority() != null) {
			this.txt_priority.setText(getWizard().getElement().getPriority().toString());
		}

//		this.txt_groupPropertiesSize.setText("" + getWizard().getElement().getGroupPropertiesSize());

		this.txt_maxEncapsulationDataSetMessageCount
				.setText("" + getWizard().getElement().getMaxEncapsulatedDataSetMessageCount());

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

		this.txt_writerGroupId.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				boolean isPageComplete = isPageComplete();
				setPageComplete(isPageComplete);
			}
		});

		this.txt_publishingInterval.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				boolean isPageComplete = isPageComplete();
				setPageComplete(isPageComplete);
			}
		});

		this.txt_keepAliveTime.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				boolean isPageComplete = isPageComplete();
				setPageComplete(isPageComplete);
			}
		});

		this.txt_priority.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				boolean isPageComplete = isPageComplete();
				setPageComplete(isPageComplete);
			}
		});

		this.txt_maxEncapsulationDataSetMessageCount.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				boolean isPageComplete = isPageComplete();
				setPageComplete(isPageComplete);
			}
		});

		this.btn_messageSettings.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				getContainer().showPage(getWizard().pageTwo);
			}

		});

		this.btn_transportSettings.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				getContainer().showPage(getWizard().pageThree);
			}

		});

		this.btn_groupProperties.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				getContainer().showPage(getWizard().pageFour);
			}

		});
		
		this.btn_checkEnabled.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				boolean isPageComplete = isPageComplete();
				setPageComplete(isPageComplete);
			}
			
			
		});
		
		this.btn_checkConfigurationFrozen.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				boolean isPageComplete = isPageComplete();
				setPageComplete(isPageComplete);
			}
			
		});

	}

	@Override
	public PubSubWriterGroupWizard getWizard() {
		return (PubSubWriterGroupWizard) super.getWizard();
	}

	@Override
	public boolean isPageComplete() {
		// field name
		String name = this.txt_name.getText();
		if (name.isEmpty()) {
			setErrorMessage("Name must be specified!");
			return false;
		}

		// field dataSetWriterGroupId
		String dataSetWriterGroupId = this.txt_writerGroupId.getText();
		if (dataSetWriterGroupId.isEmpty()) {
			setErrorMessage("DataSetWriterGroupId must be specified!");
			return false;
		}
		try {
			Integer.parseInt(dataSetWriterGroupId);
		} catch (NumberFormatException e) {
			setErrorMessage("Type of field DataSetWriterGroupId is an Int32!");
			return false;
		}
		// field transportprofileUri
		String publishingInterval = this.txt_publishingInterval.getText();
		if (publishingInterval.isEmpty()) {
			setErrorMessage("PublishingInterval must be specified!");
			return false;
		}
		try {
			Double.parseDouble(publishingInterval);
		} catch (NumberFormatException e) {
			setErrorMessage("Type of field PublishingInterval is a Double!");
			return false;
		}

		// field keepAliveTime
		String keepAliveTime = this.txt_keepAliveTime.getText();
		if (keepAliveTime.isEmpty()) {
			setErrorMessage("KeepAliveTime must be specified!");
			return false;
		}
		try {
			Double.parseDouble(keepAliveTime);
		} catch (NumberFormatException e) {
			setErrorMessage("Type of field KeepAliveTime is a Double!");
			return false;
		}

		// field priority
		String priority = this.txt_priority.getText();
		if (priority.isEmpty()) {
			setErrorMessage("Priority must be specified!");
			return false;
		}
		try {
			Byte.parseByte(priority);
		} catch (NumberFormatException e) {
			setErrorMessage("Type of field Priority is a Byte!");
			return false;
		}
	
		// field maxEncapsulationDataSetMessageSize
		String maxEncapsulationDataSetMessageSize = this.txt_maxEncapsulationDataSetMessageCount.getText();
		if (maxEncapsulationDataSetMessageSize.isEmpty()) {
			setErrorMessage("MaxEncapsulationDataSetMessageCount must be specified!");
			return false;
		}
		try {
			Integer.parseInt(maxEncapsulationDataSetMessageSize);
		} catch (NumberFormatException e) {
			setErrorMessage("Type of field MaxEncapsulationDataSetMessageCount is an Int32!");
			return false;
		}

		setErrorMessage(null);
		return true;
	}

	public String getElementName() {
		return this.txt_name.getText();
	}

	public Integer getWriterGroupId() {
		String writerGroupId = this.txt_writerGroupId.getText();
		return Integer.parseInt(writerGroupId);
	}

	public MessageSecurityMode getSecurityMode() {
		return MessageSecurityMode.valueOf(this.cmb_securityMode.getText());
	}

	public PubSubEncodingType getEncodingType() {
		return PubSubEncodingType.valueOf(this.cmb_encodingType.getText());
	}

	public Double getPublishingInterval() {
		return Double.parseDouble(this.txt_publishingInterval.getText());
	}

	public Double getKeepAlivetime() {
		return Double.parseDouble(this.txt_keepAliveTime.getText());
	}

	public Byte getPriority() {
		return Byte.parseByte(this.txt_priority.getText());
	}

/*	public Integer getGroupPropertiesSize() {
		return -1;
	}*/

	public Integer getMaxEncapsulationDataSetMessageCount() {
		return Integer.parseInt(this.txt_maxEncapsulationDataSetMessageCount.getText());
	}

	public List<KeyValuePair> getGroupProperties() {
		return null;
	}

	public PubSubRTLevel getRTLevel() {
		return PubSubRTLevel.valueOf(this.cmb_rtLevel.getText());
	}

	public Boolean isConfigurationFrozen() {
		return this.btn_checkConfigurationFrozen.getSelection();
	}

	public Boolean isEnabled() {
		return this.btn_checkEnabled.getSelection();
	}
}
