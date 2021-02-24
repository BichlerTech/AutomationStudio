package com.bichler.astudio.editor.pubsub.wizard.page;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.opcfoundation.ua.builtintypes.DataValue;

import com.bichler.astudio.editor.pubsub.dialog.ValueSourceDialog;
import com.bichler.astudio.editor.pubsub.nodes.DataSetVariable;
import com.bichler.astudio.editor.pubsub.wizard.PubSubDataSetFieldWizard;
import com.bichler.astudio.editor.pubsub.wizard.core.WrapperConfigurationVersion;
import com.bichler.astudio.editor.pubsub.wizard.core.WrapperPublishedVariableParameter;
import com.bichler.astudio.editor.pubsub.wizard.core.WrapperStaticValue;

public class DetailDataSetVariablePage extends AbstractDetailWizardPage {

//	private Text txt_staticValueSource;
	private Text txt_version;
	private Text txt_fieldnameAlias;
	private Text txt_publishParameters;
	private Text txt_valueSource;
	
//	private Button btn_checkStaticValueSource;
	private Button btn_checkPromotedField;

	private Button btn_version;
	private Button btn_publishParameters;
//	private Button btn_staticValueSource;
	private Button btn_valueSource;
	
	private ValueSourceDialog staticValueDialog = null;
	
	private DataSetVariable model = null;
	

	public DetailDataSetVariablePage() {
		super("datasetvariablepage");
		setTitle("DataSet variable");
		setDescription("Properties of a DataSet variable");
		this.staticValueDialog = new ValueSourceDialog(getShell());
	}

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);

		setControl(container);
		container.setLayout(new GridLayout(3, false));

		Label lblConfigurationVersion = new Label(container, SWT.NONE);
		lblConfigurationVersion.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblConfigurationVersion.setText("Configuration version:");

		txt_version = new Text(container, SWT.BORDER);
		txt_version.setEnabled(false);
		txt_version.setEditable(false);
		txt_version.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		this.btn_version = new Button(container, SWT.NONE);
		btn_version.setText("  ");

		Label lblFieldaliasName = new Label(container, SWT.NONE);
		lblFieldaliasName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblFieldaliasName.setText("Fieldname alias:");

		txt_fieldnameAlias = new Text(container, SWT.BORDER);
		txt_fieldnameAlias.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		new Label(container, SWT.NONE);

		Label lblPromotionField = new Label(container, SWT.NONE);
		lblPromotionField.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPromotionField.setText("Promoted field:");

		btn_checkPromotedField = new Button(container, SWT.CHECK);
		btn_checkPromotedField.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(container, SWT.NONE);

		Label lblNewLabel = new Label(container, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel.setText("Publish parameters:");

		txt_publishParameters = new Text(container, SWT.BORDER);
		txt_publishParameters.setEnabled(false);
		txt_publishParameters.setEditable(false);
		txt_publishParameters.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		this.btn_publishParameters = new Button(container, SWT.NONE);
		btn_publishParameters.setText("  ");

//		Label lblStaticValueSource = new Label(container, SWT.NONE);
//		lblStaticValueSource.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
//		lblStaticValueSource.setText("Static value source enabled:");
//
//		btn_checkStaticValueSource = new Button(container, SWT.CHECK);
//		btn_checkStaticValueSource.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
//		new Label(container, SWT.NONE);
//
//		Label lblSourceValue = new Label(container, SWT.NONE);
//		lblSourceValue.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
//		lblSourceValue.setText("Static value source:");
//
//		txt_staticValueSource = new Text(container, SWT.BORDER);
//		txt_staticValueSource.setEnabled(false);
//		txt_staticValueSource.setEditable(false);
//		txt_staticValueSource.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
//
//		this.btn_staticValueSource = new Button(container, SWT.NONE);
//		btn_staticValueSource.setText("  ");

		Label lblValueSource = new Label(container, SWT.NONE);
		lblValueSource.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblValueSource.setText("Value source:");
	
		txt_valueSource = new Text(container, SWT.BORDER);
		txt_valueSource.setEnabled(false);
		txt_valueSource.setEditable(false);
		txt_valueSource.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		this.btn_valueSource = new Button(container, SWT.NONE);
		btn_valueSource.setText("  ");
		
		createApply(container);
			
		setHandler();
	}


	public DataSetVariable getField() {
		return this.model;
	}

	@Override
	public PubSubDataSetFieldWizard getWizard() {
		return (PubSubDataSetFieldWizard) super.getWizard();
	}
	
	@Override
	public boolean isPageComplete() {
		String fieldAliasName = txt_fieldnameAlias.getText();
		if(fieldAliasName.isEmpty()) {
			setErrorMessage("FieldAliasName must be specified!");
			return false;
		}
		
		setErrorMessage(null);
		return super.isPageComplete();
	}
	
	public void setElement() {
		setDefaultValues();
	}
	
	private void setHandler() {
		this.txt_fieldnameAlias.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				boolean complete = isPageComplete();
				setPageComplete(complete);
				
			}
		});

		this.btn_version.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				getContainer().showPage(getWizard().pageThree);
			}

		});

		this.btn_publishParameters.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				getContainer().showPage(getWizard().pageFour);
			}

		});
		
		this.btn_valueSource.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				int open = staticValueDialog.open();
				if(open == Dialog.OK) {
					WrapperStaticValue value = staticValueDialog.getStaticValue();
					model.setStaticValue(value);
				}
			}
		});
		
		btn_apply.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				model = new DataSetVariable();
				
				model.setConfigurationVersion(getConfigurationVersion());
				model.setFieldNameAlias(getFieldNameAlias());
				model.setPromotedField(getPromotedField());
				model.setPublishParameters(getPublishParameters());
				model.setStaticValue(getStaticValue());
			}

		});
	}

	private void setDefaultValues() {
		DataSetVariable datasetVariable = getWizard().getElement().getField();

		if (datasetVariable != null) {			
			if (datasetVariable.getFieldNameAlias() != null) {
				this.txt_fieldnameAlias.setText(datasetVariable.getFieldNameAlias());
			}
			if (datasetVariable.getPromotedField() != null) {
				this.btn_checkPromotedField.setSelection(datasetVariable.getPromotedField());
			}
			
			this.staticValueDialog.setStaticValue(datasetVariable.getStaticValue());
			
			this.model = datasetVariable.clone();
		}
	}

	private WrapperConfigurationVersion getConfigurationVersion() {
		return getWizard().pageThree.getVersion();
	}

	private String getFieldNameAlias() {
		return this.txt_fieldnameAlias.getText();
	}

	private Boolean getPromotedField() {
		return btn_checkPromotedField.getSelection();
	}

	private WrapperPublishedVariableParameter getPublishParameters() {
		return getWizard().pageFour.getPublishedVariableDataType();
	}

	private WrapperStaticValue getStaticValue() {
		return this.staticValueDialog.getStaticValue();
	}
	
}
