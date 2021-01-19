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

import com.bichler.astudio.editor.pubsub.nodes.DataSetFieldType;
import com.bichler.astudio.editor.pubsub.wizard.PubSubDataSetFieldWizard;

public class PubSubDataSetFieldPage extends AbstractMainWizardPage {

	private Text txt_dataSet;
	private Button btn_checkConfigurationFrozen;
	private Combo cmb_type;
	private Button btn_dataSet;

	public PubSubDataSetFieldPage() {
		super("pubsubdatasetWizardPage");
		setTitle("PubSubDataSet");
		setDescription("Properties of a PubSubDataSet");
	}

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);

		setControl(container);
		container.setLayout(new GridLayout(4, false));

		Label lblType = new Label(container, SWT.NONE);
		lblType.setText("DataSetField type:");

		this.cmb_type = new Combo(container, SWT.NONE);
		cmb_type.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));

		new Label(container, SWT.NONE);

		Label lblDataSet = new Label(container, SWT.NONE);
		lblDataSet.setText("DataSet:");

		this.txt_dataSet = new Text(container, SWT.BORDER);
		txt_dataSet.setEnabled(false);
		txt_dataSet.setEditable(false);
		txt_dataSet.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));

		this.btn_dataSet = new Button(container, SWT.NONE);
		btn_dataSet.setBounds(0, 0, 90, 30);
		btn_dataSet.setText("  ");

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
		this.cmb_type.setItems(new String[] { DataSetFieldType.UA_PUBSUB_DATASETFIELD_EVENT.name(),
				DataSetFieldType.UA_PUBSUB_DATASETFIELD_VARIABLE.name() });
		if (getWizard().getElement().getDataSetFieldType() != null) {
			String name = getWizard().getElement().getDataSetFieldType().name();
			int index = -1;
			for (int i = 0; i < this.cmb_type.getItems().length; i++) {
				String type = this.cmb_type.getItems()[i];
				if (type.equals(name)) {
					index = i;
					break;
				}
			}
			this.cmb_type.select(index);
		} else {
			this.cmb_type.select(1);
		}

		if (getWizard().getElement().getConfigurationFrozen() != null) {
			this.btn_checkConfigurationFrozen.setSelection(getWizard().getElement().getConfigurationFrozen());
		}

	}

	/**
	 * Sets handlers to page controls
	 */
	private void setHandler() {
		this.cmb_type.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				switch (getDataSetFieldType()) {
				case UA_PUBSUB_DATASETFIELD_EVENT:
					btn_dataSet.setEnabled(false);
					break;
				case UA_PUBSUB_DATASETFIELD_VARIABLE:
					btn_dataSet.setEnabled(true);
					break;
				default:
					break;
				}

				boolean isPageComplete = isPageComplete();
				setPageComplete(isPageComplete);
			}
		});

		this.btn_dataSet.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				getWizard().pageTwo.setElement();
				getContainer().showPage(getWizard().pageTwo);
			}

		});

		/**
		 * TODO: DataSetVariable
		 */
	}

	@Override
	public PubSubDataSetFieldWizard getWizard() {
		return (PubSubDataSetFieldWizard) super.getWizard();
	}

	@Override
	public boolean isPageComplete() {
		// field dataSetFieldType
		try {
			DataSetFieldType publisherType = DataSetFieldType.valueOf(this.cmb_type.getText());
			switch (publisherType) {
			case UA_PUBSUB_DATASETFIELD_EVENT:
				break;
			case UA_PUBSUB_DATASETFIELD_VARIABLE:
				break;
			default:
				break;
			}
		} catch (NumberFormatException e) {
			setErrorMessage("Type of field must be a DataSetField type!");
			return false;
		}

		setErrorMessage(null);
		return true;
	}

	public DataSetFieldType getDataSetFieldType() {
		return DataSetFieldType.valueOf(this.cmb_type.getText());
	}

	public Boolean isConfigurationFrozen() {
		return this.btn_checkConfigurationFrozen.getSelection();
	}

}
