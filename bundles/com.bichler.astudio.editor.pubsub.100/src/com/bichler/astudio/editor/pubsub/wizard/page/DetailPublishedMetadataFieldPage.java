package com.bichler.astudio.editor.pubsub.wizard.page;

import java.util.UUID;

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
import org.opcfoundation.ua.builtintypes.BuiltinsMap;
import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.builtintypes.UnsignedShort;
import org.opcfoundation.ua.utils.BijectionMap;

import com.bichler.astudio.editor.pubsub.nodes.PubSubPublishedDataItemsTemplate;
import com.bichler.astudio.editor.pubsub.wizard.PubSubPublishedDataSetWizard;
import com.bichler.astudio.editor.pubsub.wizard.core.WrapperFieldMetaData;

import opc.sdk.core.enums.BuiltinType;
import opc.sdk.core.enums.ValueRanks;

public class DetailPublishedMetadataFieldPage extends AbstractDetailWizardPage {
	private Text txtName;
	private Text txtDescription;
	private Text txtFieldFlag;
	private Text txtArrayDimensions;
	private Text txtMaxStringLength;
	private Text txtDatasetFieldId;
	private Text txtKeyValuePair;

	private Combo cmbBuiltinType;
	private Combo cmbDatatype;
	private Combo cmbValueRank;

	private Button btnDataSetFieldId;
	private Button btnKeyValuePair;

	private WrapperFieldMetaData model;
	private WrapperFieldMetaData originModel;
	private boolean isNew = false;

	public DetailPublishedMetadataFieldPage() {
		super("difieldpage");
		setTitle("Field");
		setDescription("Properties of a DataItem field");
	}

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);

		setControl(container);
		container.setLayout(new GridLayout(3, false));

		Label lblName = new Label(container, SWT.NONE);
		lblName.setText("Name:");

		txtName = new Text(container, SWT.BORDER);
		txtName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));

		Label lblDescription = new Label(container, SWT.NONE);
		lblDescription.setText("Description:");

		txtDescription = new Text(container, SWT.BORDER);
		txtDescription.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));

		Label lblFieldflags = new Label(container, SWT.NONE);
		lblFieldflags.setText("FieldFlags:");

		txtFieldFlag = new Text(container, SWT.BORDER);
		txtFieldFlag.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));

		Label lblBuiltintype = new Label(container, SWT.NONE);
		lblBuiltintype.setText("BuiltInType:");

		this.cmbBuiltinType = new Combo(container, SWT.DROP_DOWN | SWT.READ_ONLY);
		cmbBuiltinType.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));

		Label lblDatatype = new Label(container, SWT.NONE);
		lblDatatype.setText("Datatype:");

		this.cmbDatatype = new Combo(container, SWT.READ_ONLY);
		cmbDatatype.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));

		Label lblValuerank = new Label(container, SWT.NONE);
		lblValuerank.setText("ValueRank:");

		this.cmbValueRank = new Combo(container, SWT.READ_ONLY);
		cmbValueRank.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));

		Label lblArraydimension = new Label(container, SWT.NONE);
//		lblArraydimension.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblArraydimension.setText("ArrayDimension:");

		txtArrayDimensions = new Text(container, SWT.BORDER);
		txtArrayDimensions.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Button button = new Button(container, SWT.NONE);
		button.setText("  ");

		Label lblMaxstringlength = new Label(container, SWT.NONE);
//		lblMaxstringlength.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblMaxstringlength.setText("MaxStringLength:");

		txtMaxStringLength = new Text(container, SWT.BORDER);
		txtMaxStringLength.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));

		Label lblDatasetfieldid = new Label(container, SWT.NONE);
		lblDatasetfieldid.setText("DatasetFieldId:");

		txtDatasetFieldId = new Text(container, SWT.BORDER);
		txtDatasetFieldId.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		this.btnDataSetFieldId = new Button(container, SWT.NONE);
		btnDataSetFieldId.setText("  ");

		Label lblKeyValuePair = new Label(container, SWT.NONE);
		lblKeyValuePair.setText("KeyValuePair:");

		txtKeyValuePair = new Text(container, SWT.BORDER);
		txtKeyValuePair.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		this.btnKeyValuePair = new Button(container, SWT.NONE);
		btnKeyValuePair.setText("  ");

		createApply(container);
	
		setDefaultValues();
		setHandler();
	}

	@Override
	public PubSubPublishedDataSetWizard getWizard() {
		return (PubSubPublishedDataSetWizard) super.getWizard();
	}

	@Override
	public boolean isPageComplete() {
		if (this.txtName.getText().isEmpty()) {
			setErrorMessage("Name must be specified!");
			return false;
		}
		if (this.txtDescription.getText().isEmpty()) {
			setErrorMessage("Description must be specified!");
			return false;
		}

		try {
			UnsignedShort.parseUnsignedShort(this.txtFieldFlag.getText());
		} catch (NumberFormatException nfe) {
			setErrorMessage("Type of field FieldFlag is an UnsignedShort!");
			return false;
		}

		try {
			UnsignedInteger.parseUnsignedInteger(this.txtArrayDimensions.getText());
		} catch (NumberFormatException nfe) {
			setErrorMessage("Type of field ArrayDimensions is an UnsignedInteger!");
			return false;
		}

		try {
			UnsignedInteger.parseUnsignedInteger(this.txtMaxStringLength.getText());
		} catch (NumberFormatException nfe) {
			setErrorMessage("Type of field MaxStringLength is an UnsignedInteger!");
			return false;
		}

		try {
			UUID.fromString(this.txtDatasetFieldId.getText());
		} catch (IllegalArgumentException nfe) {
			setErrorMessage("Type of field DatasetFieldId is an UUID!");
			return false;
		}

		setErrorMessage(null);

		return super.isPageComplete();
	}

	void setField(WrapperFieldMetaData field) {
		if (field == null) {
			this.model = new WrapperFieldMetaData();
			this.isNew = true;
		} else {
			this.model = field.clone();
			this.originModel = field;
			this.isNew = false;
		}
		setModelValues();
	}

	private void setDefaultValues() {
		for (BuiltinType type : BuiltinType.values()) {
			this.cmbBuiltinType.add(type.name());
			this.cmbDatatype.add(type.getBuildinTypeId().toString());
		}
		this.cmbBuiltinType.select(0);
		this.cmbDatatype.select(0);
		
		for(ValueRanks valueRank : ValueRanks.values()) {
			this.cmbValueRank.add(valueRank.name());
		}
		this.cmbValueRank.select(0);
	}
	
	private void setModelValues() {
		if (this.model != null) {
			this.txtName.setText(this.model.getName() != null ? this.model.getName() : "");
			this.txtDescription.setText(this.model.getName() != null ? this.model.getName() : "");
//			this.txtFieldFlag.setText(this.model.getName() != null ? this.model.getName() : "");
//			this.cmbBuiltinType
//			this.cmbDatatype
//			this.cmbValueRank
//			this.txtArrayDimensions.setText(this.model.getName() != null ? this.model.getName() : "");
			this.txtMaxStringLength
					.setText(this.model.getMaxStringLength() != null ? this.model.getMaxStringLength().toString() : "");
			this.txtDatasetFieldId
					.setText(this.model.getDataSetFieldId() != null ? this.model.getDataSetFieldId().toString() : "");

		} else if (getWizard().getElement().getConfig() instanceof PubSubPublishedDataItemsTemplate) {
//			PubSubPublishedDataItemsTemplate config = (PubSubPublishedDataItemsTemplate) getWizard().getElement()
//					.getConfig();
//			if (config != null) {
//				WrapperDataSetMetaData metadata = config.getMetaData();
//				if (metadata != null) {
//				if (config instanceof PubSubPublishedDataItemsTemplate) {
//					DataSetMetaDataType metadata = ((PubSubPublishedDataItemsTemplate) config).getMetaData();
//					txt_diName.setText(metadata.getName());
//					txt_diDescription.setText(metadata.getDescription().toString());
//					txt_diClassId.setText(metadata.getDataSetClassId().toString());
//					txt_diVersion.setText(metadata.getConfigurationVersion().toString());
//
//					this.model = ((PubSubPublishedDataItemsTemplate) config).clone();
//				} else if (config instanceof PubSubPublishedEventTemplate) {
//
//				}
//				}
//		}
		}

	}

	private void setHandler() {
		this.txtName.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				boolean isComplete = isPageComplete();
				setPageComplete(isComplete);
			}
		});
		
		this.txtDescription.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				boolean isComplete = isPageComplete();
				setPageComplete(isComplete);
			}
		});
		
		this.txtFieldFlag.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				boolean isComplete = isPageComplete();
				setPageComplete(isComplete);
			}
		});
		
		this.cmbBuiltinType.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				boolean isComplete = isPageComplete();
				setPageComplete(isComplete);
			}
		});
		
		this.txtArrayDimensions.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				boolean isComplete = isPageComplete();
				setPageComplete(isComplete);
			}
		});
		
		this.txtMaxStringLength.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				boolean isComplete = isPageComplete();
				setPageComplete(isComplete);
			}
		});
		
		
		
		this.btnKeyValuePair.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				getWizard().pageFive.setPropertiesFromElement(model);
				getContainer().showPage(getWizard().pageFive);
			}

		});
		
		this.btnDataSetFieldId.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				txtDatasetFieldId.setText(UUID.randomUUID().toString());
			}
			
		});

		if (this.btn_apply != null) {
			this.btn_apply.addSelectionListener(new SelectionAdapter() {

				@Override
				public void widgetSelected(SelectionEvent e) {
					Object config = getWizard().pageTwo.getConfig();
					if (config instanceof PubSubPublishedDataItemsTemplate) {
						if (isNew) {
							originModel = new WrapperFieldMetaData();
							model = originModel;
							isNew = false;
							applyField(model);
							((PubSubPublishedDataItemsTemplate) config).getMetaData().addField(model);

						} else {
							applyField(model);
							((PubSubPublishedDataItemsTemplate) config).getMetaData().setField(model, originModel);
						}

						getWizard().pageSeven.refreshList();
					}
				}
			});
		}
	}

	private void applyField(WrapperFieldMetaData model) {
		model.setName(this.txtName.getText());
		model.setDescription(new LocalizedText(this.txtDescription.getText()));
		model.setFieldFlags(UnsignedShort.parseUnsignedShort(this.txtFieldFlag.getText()));
		// model.setBuiltInType(UnsignedInteger.parseUnsignedInteger(this.cmbBuiltinType.getText());
//		model.setDataType(this.cmbDatatype.getData(this.cmbDatatype.getText()));
//		model.setValueRank(this.cmbValueRank.getData());
//		model.setArrayDimensions(this.txtArrayDimensions.getText());
		model.setMaxStringLength(UnsignedInteger.parseUnsignedInteger(this.txtMaxStringLength.getText()));
		model.setDataSetFieldId(UUID.fromString(this.txtDatasetFieldId.getText()));
	}

}
