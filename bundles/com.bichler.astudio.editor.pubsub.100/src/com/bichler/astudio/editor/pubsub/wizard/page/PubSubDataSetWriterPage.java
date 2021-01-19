package com.bichler.astudio.editor.pubsub.wizard.page;

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

import com.bichler.astudio.editor.pubsub.nodes.DataSetFieldContentMask;
import com.bichler.astudio.editor.pubsub.wizard.PubSubDataSetWriterWizard;

public class PubSubDataSetWriterPage extends AbstractMainWizardPage {
	private Text txt_name;
	private Text txt_dataSetWriterId;
	private Text txt_keyFrameCount;
	private Text txt_dataSetName;

	//private Text txt_dataSetWriterPropertiesSize;
	private Text txt_messageSettings;
	private Text txt_transportSettings;
	private Text txt_dataSetWriterProperties;

	private Button btn_checkConfigurationFrozen;
	private Combo cmb_dataSetFieldContentMask;
	private Button btn_messageSettings;
	private Button btn_transportSettings;
	private Button btn_dataSetWriterProperties;

	public PubSubDataSetWriterPage() {
		super("pubsubdatasetwriterWizardPage");
		setTitle("PubSubDataSetWriter");
		setDescription("Properties of a PubSubDataSetWriter");
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

		Label lbl_dataSetWriterId = new Label(container, SWT.NONE);
		lbl_dataSetWriterId.setText("DataSetWriterId:");
		lbl_dataSetWriterId.setLocation(0, 0);

		txt_dataSetWriterId = new Text(container, SWT.BORDER);
		txt_dataSetWriterId.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));

		new Label(container, SWT.NONE);

		Label lbl_dataSetFieldContentMask = new Label(container, SWT.NONE);
		lbl_dataSetFieldContentMask.setText("DataSetFieldContentMask:");
		lbl_dataSetFieldContentMask.setLocation(0, 0);

		this.cmb_dataSetFieldContentMask = new Combo(container, SWT.BORDER);
		cmb_dataSetFieldContentMask.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));

		new Label(container, SWT.NONE);

		Label lbl_keyFrameCount = new Label(container, SWT.NONE);
		lbl_keyFrameCount.setText("Key frame count:");
		lbl_keyFrameCount.setLocation(0, 0);

		txt_keyFrameCount = new Text(container, SWT.BORDER);
		txt_keyFrameCount.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));

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

		Label lbl_dataSetName = new Label(container, SWT.NONE);
		lbl_dataSetName.setText("DataSet name:");
		lbl_dataSetName.setLocation(0, 0);

		txt_dataSetName = new Text(container, SWT.BORDER);
		txt_dataSetName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));

		new Label(container, SWT.NONE);

		//Label lblDataSetWriterPropertiesSize = new Label(container, SWT.NONE);
		//lblDataSetWriterPropertiesSize.setText("DataSetWriterPropertiesSize:");

		// txt_dataSetWriterPropertiesSize = new Text(container, SWT.BORDER);
		// txt_dataSetWriterPropertiesSize.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));

		//new Label(container, SWT.NONE);

		Label lblDataSetWriterProperties = new Label(container, SWT.NONE);
		lblDataSetWriterProperties.setText("DataSetWriter properties:");

		txt_dataSetWriterProperties = new Text(container, SWT.BORDER);
		txt_dataSetWriterProperties.setEnabled(false);
		txt_dataSetWriterProperties.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));

		this.btn_dataSetWriterProperties = new Button(container, SWT.NONE);
		btn_dataSetWriterProperties.setText("  ");

		Label lblNewLabel = new Label(container, SWT.NONE);
		lblNewLabel.setText("ConfigurationFrozen:");

		this.btn_checkConfigurationFrozen = new Button(container, SWT.CHECK);
		btn_checkConfigurationFrozen.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));

		new Label(container, SWT.NONE);

		setDefaultValues();
		setHandler();
	}



	@Override
	public PubSubDataSetWriterWizard getWizard() {
		return (PubSubDataSetWriterWizard) super.getWizard();
	}

	@Override
	public boolean isPageComplete() {
		// field name
		String name = this.txt_name.getText();
		if (name.isEmpty()) {
			setErrorMessage("Name must be specified!");
			return false;
		}

		// field dataSetWriterId
		String dataSetWriterId = this.txt_dataSetWriterId.getText();
		if (dataSetWriterId.isEmpty()) {
			setErrorMessage("DataSetWriterId must be specified!");
			return false;
		}
		try {
			Integer.parseInt(dataSetWriterId);
		} catch (NumberFormatException e) {
			setErrorMessage("Type of field DataSetWriterId is an Int32!");
			return false;
		}
		// field transportprofileUri
		String keyFrameCount = this.txt_keyFrameCount.getText();
		if (keyFrameCount.isEmpty()) {
			setErrorMessage("KeyFrameCount must be specified!");
			return false;
		}
		try {
			Integer.parseInt(keyFrameCount);
		} catch (NumberFormatException e) {
			setErrorMessage("Type of field KeyFrameCount is an Int32!");
			return false;
		}

		// field datasetname
		String datasetname = this.txt_dataSetName.getText();
		if (datasetname.isEmpty()) {
			setErrorMessage("DataSet name must be specified!");
			return false;
		}

		// field connectionPropertiesSize
		/*String dataSetWriterPropertiesSize = this.txt_dataSetWriterPropertiesSize.getText();
		if (dataSetWriterPropertiesSize.isEmpty()) {
			setErrorMessage("DataSetWriterPropertiesSize must be specified!");
			return false;
		}
		try {
			Integer.parseInt(dataSetWriterPropertiesSize);
		} catch (NumberFormatException e) {
			setErrorMessage("Type of field DataSetWriterPropertiesSize is an Int32!");
			return false;
		}*/

		setErrorMessage(null);
		return true;
	}

	public String getElementName() {
		return this.txt_name.getText();
	}

	public Integer getDataSetWriterId() {
		String dataSetWriterId = this.txt_dataSetWriterId.getText();
		return Integer.parseInt(dataSetWriterId);
	}

	public DataSetFieldContentMask getDataSetFieldContentMask() {
		return DataSetFieldContentMask.valueOf(this.cmb_dataSetFieldContentMask.getText());
	}

	public Integer getKeyFrameCount() {
		return Integer.parseInt(this.txt_keyFrameCount.getText());
	}

	public ExtensionObject getMessageSettings() {
		return null;
	}

	public String getDataSetName() {
		return this.txt_dataSetName.getText();
	}

	/*public Integer getDataSetWriterPropertiesSize() {
		return Integer.parseInt(this.txt_dataSetWriterPropertiesSize.getText());
	}*/

	public KeyValuePair[] getDataSetWriterProperties() {
		return null;
	}

	public Boolean isConfigurationFrozen() {
		return this.btn_checkConfigurationFrozen.getSelection();
	}
	
	/**
	 * Set default values to page controls
	 */
	private void setDefaultValues() {
		this.cmb_dataSetFieldContentMask
				.setItems(new String[] { DataSetFieldContentMask.UA_DATASETFIELDCONTENTMASK_NONE.name(),
						DataSetFieldContentMask.UA_DATASETFIELDCONTENTMASK_STATUSCODE.name(),
						DataSetFieldContentMask.UA_DATASETFIELDCONTENTMASK_SOURCETIMESTAMP.name(),
						DataSetFieldContentMask.UA_DATASETFIELDCONTENTMASK_SERVERTIMESTAMP.name(),
						DataSetFieldContentMask.UA_DATASETFIELDCONTENTMASK_SOURCEPICOSECONDS.name(),
						DataSetFieldContentMask.UA_DATASETFIELDCONTENTMASK_SERVERPICOSECONDS.name(),
						DataSetFieldContentMask.UA_DATASETFIELDCONTENTMASK_RAWDATA.name(),
						DataSetFieldContentMask.__UA_DATASETFIELDCONTENTMASK_FORCE32BIT.name() });
		
		if (getWizard().getElement().getDataSetFieldContentMask() != null) {
			String name = getWizard().getElement().getDataSetFieldContentMask().name();
			int index = -1;
			for (int i = 0; i < this.cmb_dataSetFieldContentMask.getItems().length; i++) {
				String type = this.cmb_dataSetFieldContentMask.getItems()[i];
				if (type.equals(name)) {
					index = i;
					break;
				}
			}
			this.cmb_dataSetFieldContentMask.select(index);
		} else {
			this.cmb_dataSetFieldContentMask.select(1);
		}

		if (getWizard().getElement().getName() != null) {
			this.txt_name.setText(getWizard().getElement().getName());
		}

		if (getWizard().getElement().getDataSetWriterId() != 0) {
			this.txt_dataSetWriterId.setText("" + getWizard().getElement().getDataSetWriterId());
		}

		this.txt_keyFrameCount.setText("" + getWizard().getElement().getKeyFrameCount());

		if (getWizard().getElement().getDataSetName() != null) {
			this.txt_dataSetName.setText(getWizard().getElement().getDataSetName());
		}

//		this.txt_dataSetWriterPropertiesSize.setText("" + getWizard().getElement().getDataSetWriterPropertiesSize());

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

		this.txt_dataSetWriterId.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				boolean isPageComplete = isPageComplete();
				setPageComplete(isPageComplete);
			}
		});

		this.cmb_dataSetFieldContentMask.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				boolean isPageComplete = isPageComplete();
				setPageComplete(isPageComplete);
			}
		});

		this.txt_keyFrameCount.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				boolean isPageComplete = isPageComplete();
				setPageComplete(isPageComplete);
			}
		});

		this.txt_dataSetName.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				boolean isPageComplete = isPageComplete();
				setPageComplete(isPageComplete);
			}
		});

		/* this.txt_dataSetWriterPropertiesSize.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				boolean isPageComplete = isPageComplete();
				setPageComplete(isPageComplete);
			}
		});*/

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

		this.btn_dataSetWriterProperties.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				getContainer().showPage(getWizard().pageFour);
			}

		});
	}
}
