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
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.bichler.astudio.editor.pubsub.nodes.DataSetVariable;
import com.bichler.astudio.editor.pubsub.nodes.PubSubPublishedDataSetType;
import com.bichler.astudio.editor.pubsub.wizard.PubSubPublishedDataSetWizard;

public class PubSubPublishedDataSetPage extends AbstractMainWizardPage {

	private Text txt_config;
	private Text txt_name;
	private Button btn_checkConfigurationFrozen;
	private Combo cmb_type;
	private Button btn_config;

	public PubSubPublishedDataSetPage() {
		super("pubsubpublisheddatasetWizardPage");
		setTitle("PubSubPublishedDataSet");
		setDescription("Properties of a PubSubPublishedDataSet");
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

		Label lblType = new Label(container, SWT.NONE);
		lblType.setText("PublishedDataSet type:");

		this.cmb_type = new Combo(container, SWT.READ_ONLY);
		cmb_type.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));

		new Label(container, SWT.NONE);

		Label lblconfig = new Label(container, SWT.NONE);
		lblconfig.setText("Config:");

		this.txt_config = new Text(container, SWT.BORDER);
		txt_config.setEditable(false);
		txt_config.setEnabled(false);
		txt_config.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));

		this.btn_config = new Button(container, SWT.NONE);
		btn_config.setBounds(0, 0, 90, 30);
		btn_config.setText("  ");

		Label lblNewLabel = new Label(container, SWT.NONE);
		lblNewLabel.setText("ConfigurationFrozen:");

		this.btn_checkConfigurationFrozen = new Button(container, SWT.CHECK);
		btn_checkConfigurationFrozen.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));

		new Label(container, SWT.NONE);

		setDefaultValues();
		setHandler();

		this.cmb_type.notifyListeners(SWT.Modify, new Event());
	}

	/**
	 * Set default values to page controls
	 */
	private void setDefaultValues() {
		this.cmb_type.setItems(new String[] { PubSubPublishedDataSetType.UA_PUBSUB_DATASET_PUBLISHEDEVENTS.name(),
				PubSubPublishedDataSetType.UA_PUBSUB_DATASET_PUBLISHEDEVENTS_TEMPLATE.name(),
				PubSubPublishedDataSetType.UA_PUBSUB_DATASET_PUBLISHEDITEMS.name(),
				PubSubPublishedDataSetType.UA_PUBSUB_DATASET_PUBLISHEDITEMS_TEMPLATE.name() });

		if (getWizard().getElement().getPublishedDataSetType() != null) {
			String name = getWizard().getElement().getPublishedDataSetType().name();
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
			this.cmb_type.select(2);
		}

		if (getWizard().getElement().getName() != null) {
			this.txt_name.setText(getWizard().getElement().getName());
		}

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

		this.cmb_type.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {

				PubSubPublishedDataSetType fieldType = getPublishedDataSetFieldType();
				switch (fieldType) {
				case UA_PUBSUB_DATASET_PUBLISHEDITEMS:
					btn_config.setEnabled(false);
					break;
				default:
					btn_config.setEnabled(true);
					break;
				}

				boolean isPageComplete = isPageComplete();
				setPageComplete(isPageComplete);
			}
		});

		this.btn_config.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				getWizard().pageTwo.setFieldType(getPublishedDataSetFieldType());
				getContainer().showPage(getWizard().pageTwo);
			}

		});
		
	}

	@Override
	public PubSubPublishedDataSetWizard getWizard() {
		return (PubSubPublishedDataSetWizard) super.getWizard();
	}

	@Override
	public boolean isPageComplete() {
		// field name
		String name = this.txt_name.getText();
		if (name.isEmpty()) {
			setErrorMessage("Name must be specified!");
			return false;
		}

		setErrorMessage(null);
		return true;
	}

	public String getElementName() {
		return this.txt_name.getText();
	}

	public PubSubPublishedDataSetType getPublishedDataSetFieldType() {
		return PubSubPublishedDataSetType.valueOf(this.cmb_type.getText());
	}

	public DataSetVariable getConfig() {
		return null;
	}

	public Boolean isConfigurationFrozen() {
		return this.btn_checkConfigurationFrozen.getSelection();
	}

}
