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
import org.opcfoundation.ua.core.DataSetOrderingType;
import org.opcfoundation.ua.core.JsonWriterGroupMessageDataType;
import org.opcfoundation.ua.core.UadpWriterGroupMessageDataType;
import org.opcfoundation.ua.core.WriterGroupMessageDataType;

import com.bichler.astudio.editor.pubsub.wizard.PubSubWriterGroupWizard;
import com.bichler.astudio.editor.pubsub.wizard.core.MessageSettingType;
import com.bichler.astudio.editor.pubsub.wizard.core.WrapperWriterGroupMessage;

public class DetailWriterGroupMessageSettingsPage extends AbstractDetailWizardPage {

	private Composite cmp_writergroup_json;
	private Composite cmp_writergroup_uadp;

	private Combo cmb_type;
	private Combo cmb_datasetOrdering;

	private Text txt_groupVersion;
	private Text txt_networkMessageContentMask;
	private Text txt_sampleOffset;
	private Text txt_publishingOffset;
	private Text txt_jsonNetworkMessageContentMask;

	protected UnsignedInteger GroupVersion;
	protected DataSetOrderingType DataSetOrdering;
	protected UnsignedInteger NetworkMessageContentMask;
	protected Double SamplingOffset;
	protected Double[] PublishingOffset;

	private WrapperWriterGroupMessage model = null;

	public DetailWriterGroupMessageSettingsPage() {
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

		this.cmb_type = new Combo(container, SWT.NONE);
		cmb_type.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		this.cmp_writergroup_uadp = new Composite(container, SWT.NONE);
		cmp_writergroup_uadp.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		cmp_writergroup_uadp.setLayout(new GridLayout(3, false));

		Label lblGroupVersin = new Label(cmp_writergroup_uadp, SWT.NONE);
		lblGroupVersin.setBounds(0, 0, 70, 20);
		lblGroupVersin.setText("GroupVersion");

		this.txt_groupVersion = new Text(cmp_writergroup_uadp, SWT.BORDER);
		txt_groupVersion.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		new Label(cmp_writergroup_uadp, SWT.NONE);

		Label lblDatasetordering = new Label(cmp_writergroup_uadp, SWT.NONE);
		lblDatasetordering.setBounds(0, 0, 70, 20);
		lblDatasetordering.setText("DataSetOrdering");

		this.cmb_datasetOrdering = new Combo(cmp_writergroup_uadp, SWT.BORDER);
		cmb_datasetOrdering.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		new Label(cmp_writergroup_uadp, SWT.NONE);

		Label lblNetworkMessageContentMask = new Label(cmp_writergroup_uadp, SWT.NONE);
		lblNetworkMessageContentMask.setBounds(0, 0, 70, 20);
		lblNetworkMessageContentMask.setText("NetworkMessageContentMask");

		this.txt_networkMessageContentMask = new Text(cmp_writergroup_uadp, SWT.BORDER);
		txt_networkMessageContentMask.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		new Label(cmp_writergroup_uadp, SWT.NONE);

		Label lblSampleOffset = new Label(cmp_writergroup_uadp, SWT.NONE);
		lblSampleOffset.setBounds(0, 0, 70, 20);
		lblSampleOffset.setText("SampleOffset");

		this.txt_sampleOffset = new Text(cmp_writergroup_uadp, SWT.BORDER);
		txt_sampleOffset.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		new Label(cmp_writergroup_uadp, SWT.NONE);

		Label lblPublishingoffset = new Label(cmp_writergroup_uadp, SWT.NONE);
		lblPublishingoffset.setBounds(0, 0, 70, 20);
		lblPublishingoffset.setText("PublishingOffset");

		this.txt_publishingOffset = new Text(cmp_writergroup_uadp, SWT.BORDER);
		txt_publishingOffset.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		new Label(cmp_writergroup_uadp, SWT.NONE);

		this.cmp_writergroup_json = new Composite(container, SWT.NONE);
		cmp_writergroup_json.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		cmp_writergroup_json.setLayout(new GridLayout(3, false));

		Label lblNetworkmessagecontentmask = new Label(cmp_writergroup_json, SWT.NONE);
		lblNetworkmessagecontentmask.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		lblNetworkmessagecontentmask.setBounds(0, 0, 70, 20);
		lblNetworkmessagecontentmask.setText("NetworkMessageContentMask");

		this.txt_jsonNetworkMessageContentMask = new Text(cmp_writergroup_json, SWT.BORDER);
		txt_jsonNetworkMessageContentMask.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		new Label(cmp_writergroup_json, SWT.NONE);

		createApply(container);

		setDefaultValue();
		setHandler();

		this.cmb_type.notifyListeners(SWT.Selection, new Event());
	}

	public WrapperWriterGroupMessage getWriterGroupMessage() {
		return this.model;
	}

	@Override
	public PubSubWriterGroupWizard getWizard() {
		return (PubSubWriterGroupWizard) super.getWizard();
	}

	@Override
	public boolean isPageComplete() {
		switch (MessageSettingType.valueOf(cmb_type.getText())) {
		case JSON:
			String jsonNetwork = this.txt_jsonNetworkMessageContentMask.getText();
			if (jsonNetwork.isEmpty()) {
				setErrorMessage("JsonNetowrkMessageContentMask must be specified!");
				return false;
			}
			try {
				UnsignedInteger.parseUnsignedInteger(jsonNetwork);
			} catch (NumberFormatException e) {
				setErrorMessage("Type of field JsonNetworkContentMask is an UnsignedInteger!");
				return false;
			}
			break;
		case UADP:
			// field name
			String groupVersion = this.txt_groupVersion.getText();
			if (groupVersion.isEmpty()) {
				setErrorMessage("GroupVersion must be specified!");
				return false;
			}
			try {
				UnsignedInteger.parseUnsignedInteger(groupVersion);
			} catch (NumberFormatException e) {
				setErrorMessage("Type of field Groupversion is an UnsignedInteger!");
				return false;
			}

			String networkMessageContentMask = this.txt_networkMessageContentMask.getText();
			if (networkMessageContentMask.isEmpty()) {
				setErrorMessage("NetworkMessageContentMask must be specified!");
				return false;
			}
			try {
				UnsignedInteger.parseUnsignedInteger(networkMessageContentMask);
			} catch (NumberFormatException e) {
				setErrorMessage("Type of field NetworkMessageContentMask is an UnsignedInteger!");
				return false;
			}

			String sampleOffset = this.txt_sampleOffset.getText();
			if (sampleOffset.isEmpty()) {
				setErrorMessage("SampleOffset must be specified!");
				return false;
			}
			try {
				Double.parseDouble(sampleOffset);
			} catch (NumberFormatException e) {
				setErrorMessage("Type of field SampleOffset is a Double!");
				return false;
			}

			/*
			 * String publishingOffset = this.txt_publishingOffset.getText(); if
			 * (publishingOffset.isEmpty()) {
			 * setErrorMessage("PublishingOffset must be specified!"); return false; }
			 */
			// TODO: [DOUBLE],[DOUBLE],[DOUBLE],.....
			break;
		}

		setErrorMessage(null);
		
		return super.isPageComplete();
	}

	private void setDefaultValue() {
		// connection type
		String[] items = new String[MessageSettingType.values().length];
		for (int i = 0; i < MessageSettingType.values().length; i++) {
			items[i] = MessageSettingType.values()[i].name();
		}
		this.cmb_type.setItems(items);
		this.cmb_type.select(0);

		String[] items2 = new String[DataSetOrderingType.values().length];
		for (int i = 0; i < DataSetOrderingType.values().length; i++) {
			items2[i] = DataSetOrderingType.values()[i].name();
		}
		this.cmb_datasetOrdering.setItems(items2);
		this.cmb_datasetOrdering.select(0);

		if (getWizard().getElement().getMessageSettings() != null) {
			WrapperWriterGroupMessage setting = getWizard().getElement().getMessageSettings();

			UnsignedInteger groupVersion = setting.getGroupVersion();
			if (groupVersion != null) {
				txt_groupVersion.setText(groupVersion.toString());
			}

			UnsignedInteger networkMessageContentMask = setting.getNetworkMessageContentMask();
			if (groupVersion != null) {
				txt_networkMessageContentMask.setText(networkMessageContentMask.toString());
			}

			DataSetOrderingType datasetOrdering = setting.getDataSetOrdering();
			if (groupVersion != null) {
				cmb_datasetOrdering.select(datasetOrdering.getValue());
			}

			Double[] publishingOffset = setting.getPublishingOffset();
			if (groupVersion != null) {
				txt_publishingOffset.setText(publishingOffset.toString());
			}

			Double samplingOffset = setting.getSamplingOffset();
			if (groupVersion != null) {
				txt_sampleOffset.setText(samplingOffset.toString());
			}

			UnsignedInteger jsonNetworkMessageContentMask = setting.getJsonNetworkMessageContentMask();
			if (networkMessageContentMask != null) {
				txt_jsonNetworkMessageContentMask.setText(networkMessageContentMask.toString());
			}
			this.cmb_type.select(1);

			this.model = setting.clone();
		}
	}

	private void setHandler() {
		this.txt_groupVersion.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				boolean isPageComplete = isPageComplete();
				setPageComplete(isPageComplete);
			}
		});

		this.txt_networkMessageContentMask.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				boolean isPageComplete = isPageComplete();
				setPageComplete(isPageComplete);
			}
		});

		this.txt_publishingOffset.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				boolean isPageComplete = isPageComplete();
				setPageComplete(isPageComplete);
			}
		});

		this.txt_sampleOffset.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				boolean isPageComplete = isPageComplete();
				setPageComplete(isPageComplete);
			}
		});

		this.txt_jsonNetworkMessageContentMask.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				boolean isPageComplete = isPageComplete();
				setPageComplete(isPageComplete);
			}
		});

		this.cmb_type.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				String value = cmb_type.getText();
				switch (MessageSettingType.valueOf(value)) {
				case UADP:
					cmp_writergroup_uadp.setVisible(true);
					((GridData) cmp_writergroup_uadp.getLayoutData()).exclude = false;
					cmp_writergroup_json.setVisible(false);
					((GridData) cmp_writergroup_json.getLayoutData()).exclude = true;
					break;
				case JSON:
					cmp_writergroup_uadp.setVisible(false);
					((GridData) cmp_writergroup_uadp.getLayoutData()).exclude = true;
					cmp_writergroup_json.setVisible(true);
					((GridData) cmp_writergroup_json.getLayoutData()).exclude = false;
					break;
				}

				Composite parent = (Composite) getControl();
				parent.layout(true, true);

				boolean complete = isPageComplete();
				setPageComplete(complete);
			}
		});

		btn_apply.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				MessageSettingType value = MessageSettingType.valueOf(cmb_type.getText());
				model = new WrapperWriterGroupMessage();
				model.setType(value);

				switch (value) {
				case UADP:
					model.setDataSetOrdering(getDataSetOrdering());
					model.setGroupVersion(getGroupVersion());
					model.setNetworkMessageContentMask(getNetworkMessageContentMask());
					model.setPublishingOffset(getPublishingOffset());
					model.setSamplingOffset(getSamplingOffset());
					break;
				case JSON:
					model.setJsonNetworkMessageContentMask(getJsonNetworkMessageContentMask());
					break;
				}
			}
		});
	}

	private UnsignedInteger getGroupVersion() {
		return new UnsignedInteger(this.txt_groupVersion.getText());// new UnsignedInteger(this.txt);
	}

	private DataSetOrderingType getDataSetOrdering() {
		return DataSetOrderingType.valueOf(this.cmb_datasetOrdering.getText());
	}

	private UnsignedInteger getNetworkMessageContentMask() {
		return new UnsignedInteger(this.txt_networkMessageContentMask.getText());
	}

	private Double getSamplingOffset() {
		return new Double(this.txt_sampleOffset.getText());
	}

	private Double[] getPublishingOffset() {
		return new Double[0];
	}

	private UnsignedInteger getJsonNetworkMessageContentMask() {
		return new UnsignedInteger(this.txt_jsonNetworkMessageContentMask.getText());
	}
}
