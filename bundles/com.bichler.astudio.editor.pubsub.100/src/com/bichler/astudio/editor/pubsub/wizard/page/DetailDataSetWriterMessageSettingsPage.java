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
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.builtintypes.UnsignedShort;

import com.bichler.astudio.editor.pubsub.wizard.PubSubDataSetWriterWizard;
import com.bichler.astudio.editor.pubsub.wizard.core.MessageSettingType;
import com.bichler.astudio.editor.pubsub.wizard.core.WrapperWriterDataSetMessage;

public class DetailDataSetWriterMessageSettingsPage extends AbstractDetailWizardPage {

	private Composite cmp_writerdataset_uadp;
	private Composite cmp_writerdataset_json;

	private Combo cmb_type;

	private WrapperWriterDataSetMessage model = null;

	private Text txt_contentMask;
	private Text txt_configuredSize;
	private Text txt_networkMessageNumber;
	private Text txt_datasetOffset;
	private Text txt_networkMessageContentMask;
	private Text txt_datasetMessageContentMask;

	public DetailDataSetWriterMessageSettingsPage() {
		super("messagesettingspage");
		setTitle("Message settings");
		setDescription("Properties of a message settings");
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

		this.cmp_writerdataset_uadp = new Composite(container, SWT.NONE);
		cmp_writerdataset_uadp.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		cmp_writerdataset_uadp.setLayout(new GridLayout(2, false));

		Label lblDataSetWriter = new Label(cmp_writerdataset_uadp, SWT.NONE);
		lblDataSetWriter.setText("DataSetWriterContentMask");

		this.txt_contentMask = new Text(cmp_writerdataset_uadp, SWT.BORDER);
		this.txt_contentMask.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblConfiguredSize = new Label(cmp_writerdataset_uadp, SWT.NONE);
		lblConfiguredSize.setText("ConfiguredSize");

		this.txt_configuredSize = new Text(cmp_writerdataset_uadp, SWT.BORDER);
		this.txt_configuredSize.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblNewLabel = new Label(cmp_writerdataset_uadp, SWT.NONE);
		lblNewLabel.setText("NetworkMessageNumber");

		this.txt_networkMessageNumber = new Text(cmp_writerdataset_uadp, SWT.BORDER);
		this.txt_networkMessageNumber.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblDatasetoffset = new Label(cmp_writerdataset_uadp, SWT.NONE);
		lblDatasetoffset.setText("DataSetOffset");

		this.txt_datasetOffset = new Text(cmp_writerdataset_uadp, SWT.BORDER);
		this.txt_datasetOffset.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		this.cmp_writerdataset_json = new Composite(container, SWT.NONE);
		cmp_writerdataset_json.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		cmp_writerdataset_json.setLayout(new GridLayout(2, false));

		Label lblWDSNetworkMessageContentMask = new Label(cmp_writerdataset_json, SWT.NONE);
		lblWDSNetworkMessageContentMask.setText("NetworkMessageContentMask");

		this.txt_networkMessageContentMask = new Text(cmp_writerdataset_json, SWT.BORDER);
		this.txt_networkMessageContentMask.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblDatasetmessagecontentmask = new Label(cmp_writerdataset_json, SWT.NONE);
		lblDatasetmessagecontentmask.setText("DataSetMessageContentMask");

		this.txt_datasetMessageContentMask = new Text(cmp_writerdataset_json, SWT.BORDER);
		this.txt_datasetMessageContentMask.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		createApply(container);

		setDefaultValue();
		setHandler();

		this.cmb_type.notifyListeners(SWT.Selection, new Event());
	}

	public WrapperWriterDataSetMessage getMessageSettings() {
		return this.model;
	}

	@Override
	public PubSubDataSetWriterWizard getWizard() {
		return (PubSubDataSetWriterWizard) super.getWizard();
	}

	@Override
	public boolean isPageComplete() {
//		IWizardPage cPage = getWizard().getContainer().getCurrentPage();
//		if(cPage != this) {
//			return true;
//		}

		String text = "";
		MessageSettingType type = MessageSettingType.valueOf(this.cmb_type.getText());
		switch (type) {
		case JSON:
			text = this.txt_datasetMessageContentMask.getText();
			if (text.isEmpty()) {
				setErrorMessage("MessageContentMask must be specified!");
				return false;
			}
			try {
				UnsignedInteger.parseUnsignedInteger(text);
			} catch (NumberFormatException nfe) {
				setErrorMessage("Type of field MessageContentMask is an UnsignedInteger!");
				return false;
			}

			text = this.txt_networkMessageContentMask.getText();
			if (text.isEmpty()) {
				setErrorMessage("Type of field NetworkMessageContentMask is an UnsignedInteger!");
				return false;
			}
			try {
				UnsignedInteger.parseUnsignedInteger(text);
			} catch (NumberFormatException nfe) {
				return false;
			}
			break;
		case UADP:
			text = this.txt_configuredSize.getText();
			if (text.isEmpty()) {
				setErrorMessage("ConfiguredSize must be specified!");
				return false;
			}
			try {
				UnsignedShort.parseUnsignedShort(text);
			} catch (NumberFormatException nfe) {
				setErrorMessage("Type of field ConfiguredSize is an UnsignedShort!");
				return false;
			}

			if (this.txt_contentMask.getText().isEmpty()) {
				setErrorMessage("ContentMask must be specified!");
				return false;
			}
			try {
				UnsignedInteger.parseUnsignedInteger(text);
			} catch (NumberFormatException nfe) {
				setErrorMessage("Type of field ContentMask is an UnsignedShort!");
				return false;
			}

			if (this.txt_datasetOffset.getText().isEmpty()) {
				setErrorMessage("DataOffset must be specified!");
				return false;
			}
			try {
				UnsignedShort.parseUnsignedShort(text);
			} catch (NumberFormatException nfe) {
				setErrorMessage("Type of field DatasetOffset is an UnsignedShort!");
				return false;
			}

			if (this.txt_networkMessageNumber.getText().isEmpty()) {
				setErrorMessage("NetworkMessageNumber must be specified!");
				return false;
			}
			try {
				UnsignedShort.parseUnsignedShort(text);
			} catch (NumberFormatException nfe) {
				setErrorMessage("Type of field NetworkMessageNumber is an UnsignedShort!");
				return false;
			}

			break;
		}

		setErrorMessage(null);
		
		return super.isPageComplete();
	}

	private void setDefaultValue() {
		String[] items = new String[MessageSettingType.values().length];
		for (int i = 0; i < MessageSettingType.values().length; i++) {
			items[i] = MessageSettingType.values()[i].name();
		}
		this.cmb_type.setItems(items);
		this.cmb_type.select(0);

		WrapperWriterDataSetMessage messageSettings = getWizard().getElement().getMessageSettings();
		if (messageSettings != null) {
			// if (messageSettings instanceof UadpDataSetWriterMessageDataType) {
			this.txt_configuredSize.setText(messageSettings.getConfiguredSize().toString());
			this.txt_contentMask.setText(messageSettings.getDataSetMessageContentMask().toString());
			this.txt_datasetOffset.setText(messageSettings.getDataSetOffset().toString());
			this.txt_networkMessageNumber.setText(messageSettings.getNetworkMessageNumber().toString());
			// } else if (messageSettings instanceof JsonDataSetWriterMessageDataType) {
			this.txt_datasetMessageContentMask.setText(messageSettings.getDataSetMessageContentMask().toString());
			this.txt_networkMessageContentMask.setText(messageSettings.getNetworkMessageContentMask().toString());
			this.cmb_type.select(1);
			// }

			this.model = messageSettings.clone();
		}
	}

	private void setHandler() {
		this.cmb_type.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				String value = cmb_type.getText();
				switch (MessageSettingType.valueOf(value)) {
				case UADP:
					cmp_writerdataset_uadp.setVisible(true);
					((GridData) cmp_writerdataset_uadp.getLayoutData()).exclude = false;
					cmp_writerdataset_json.setVisible(false);
					((GridData) cmp_writerdataset_json.getLayoutData()).exclude = true;
					break;
				case JSON:
					cmp_writerdataset_uadp.setVisible(false);
					((GridData) cmp_writerdataset_uadp.getLayoutData()).exclude = true;
					cmp_writerdataset_json.setVisible(true);
					((GridData) cmp_writerdataset_json.getLayoutData()).exclude = false;
					break;
				}

				Composite parent = (Composite) getControl();
				parent.layout(true, true);

				boolean complete = isPageComplete();
				setPageComplete(complete);
			}
		});

		txt_configuredSize.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				boolean complete = isPageComplete();
				setPageComplete(complete);
			}
		});

		txt_contentMask.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				boolean complete = isPageComplete();
				setPageComplete(complete);
			}
		});

		txt_networkMessageNumber.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				boolean complete = isPageComplete();
				setPageComplete(complete);
			}
		});

		txt_datasetOffset.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				boolean complete = isPageComplete();
				setPageComplete(complete);
			}
		});

		txt_networkMessageContentMask.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				boolean complete = isPageComplete();
				setPageComplete(complete);
			}
		});

		txt_datasetMessageContentMask.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				boolean complete = isPageComplete();
				setPageComplete(complete);
			}
		});

		btn_apply.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				MessageSettingType value = MessageSettingType.valueOf(cmb_type.getText());
				model = new WrapperWriterDataSetMessage();
				model.setConfiguredSize(getConfiguredSize());
				model.setDataSetOffset(getDataSetOffset());
				model.setDataSetMessageContentMask(getMessageContentMask());
				model.setNetworkMessageNumber(getNetworkMessageNumber());

				model.setNetworkMessageContentMask(getNetworkMessageContentMask());
				model.setJsonDataSetMessageContentMask(getDataSetMessageContentMask());

//				switch (MessageSettingType.valueOf(cmb_type.getText())) {
//				case UADP:
//					model = new UadpDataSetWriterMessageDataType(getMessageContentMask(), getConfiguredSize(),
//							getNetworkMessageNumber(), getDataSetOffset());
//					break;
//				case JSON:
//					model = new JsonDataSetWriterMessageDataType(getNetworkMessageContentMask(),
//							getDataSetMessageContentMask());
//					break;
//				}
			}
		});
	}

	private UnsignedInteger getDataSetMessageContentMask() {
		return new UnsignedInteger(this.txt_datasetMessageContentMask.getText());
	}

	private UnsignedInteger getNetworkMessageContentMask() {
		return new UnsignedInteger(this.txt_networkMessageContentMask.getText());
	}

	private UnsignedShort getDataSetOffset() {
		return new UnsignedShort(this.txt_datasetOffset.getText());
	}

	private UnsignedShort getNetworkMessageNumber() {
		return new UnsignedShort(this.txt_networkMessageNumber.getText());
	}

	private UnsignedShort getConfiguredSize() {
		return new UnsignedShort(this.txt_configuredSize.getText());
	}

	private UnsignedInteger getMessageContentMask() {
		return new UnsignedInteger(this.txt_contentMask.getText());
	}

}
