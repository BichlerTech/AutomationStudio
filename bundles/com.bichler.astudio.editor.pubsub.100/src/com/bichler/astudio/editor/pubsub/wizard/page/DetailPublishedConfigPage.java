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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.bichler.astudio.editor.pubsub.nodes.PubSubPublishedDataItemsTemplate;
import com.bichler.astudio.editor.pubsub.nodes.PubSubPublishedDataSetType;
import com.bichler.astudio.editor.pubsub.nodes.PubSubPublishedEventTemplate;
import com.bichler.astudio.editor.pubsub.wizard.PubSubPublishedDataSetWizard;
import com.bichler.astudio.editor.pubsub.wizard.core.WrapperDataSetMetaData;

public class DetailPublishedConfigPage extends AbstractDetailWizardPage {

	private Composite cmp_diTmpConfig;
	private Composite cmp_evtConfig;
	private Composite cmp_evtTmpConfig;
	// private Text txt_diVariablesToAddSize;
	private Text txt_diVariablesToAdd;
	private Text txt_evtEventNotifier;
	private Text txt_evtContentFilter;
	private Text txt_evttmpMetaData;
	private Text txt_evttmpEventnotifier;
	// private Control txt_evttmpFieldSize;
	private Text txt_evttmpSelectedFields;
	private Text txt_evttmpContentFilter;

	private Text txt_diName;
	private Text txt_diDescription;
	private Text txt_diFields;
	private Button btn_diFields;
	private Button btn_diVariablesToAdd;
	private PubSubPublishedDataSetType fieldType;

	private Text txt_diClassId;
	private Button btn_diClassId;
	private Text txt_diVersion;
	private Button btn_diVersion;

	private Text txt_Namespaces;
	private Button btn_diNamespaces;

	private Object model;
//	private Text txt_StructureDataTypes;
//	private Button btn_diStructureDataTypes;
//	private Text txt_EnumDataTypes;
//	private Button btn_diEnumDataTypes;
//	private Text txt_SimpleDataTypes;
//	private Button btn_diSimpleDataTypes;

	public DetailPublishedConfigPage() {
		super("configpage");
		setTitle("Config");
		setDescription("Properties of a config");
	}

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);

		setControl(container);
		container.setLayout(new GridLayout(1, false));

		this.cmp_diTmpConfig = new Composite(container, SWT.NONE);
		cmp_diTmpConfig.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		cmp_diTmpConfig.setLayout(new GridLayout(3, false));

		initDIConfig();

		this.cmp_evtConfig = new Composite(container, SWT.NONE);
		cmp_evtConfig.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		cmp_evtConfig.setLayout(new GridLayout(3, false));

		initEvtConfig();

		this.cmp_evtTmpConfig = new Composite(container, SWT.NONE);
		cmp_evtTmpConfig.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		cmp_evtTmpConfig.setLayout(new GridLayout(3, false));

		initEvtTmpConfig();

		createApply(container);
		
		setDefaultValues();
		setHandler();
	}

	public Object getConfig() {
		return this.model;
	}

	@Override
	public PubSubPublishedDataSetWizard getWizard() {
		return (PubSubPublishedDataSetWizard) super.getWizard();
	}

	@Override
	public boolean isPageComplete() {
		if (this.fieldType != null) {
			switch (this.fieldType) {
			case UA_PUBSUB_DATASET_PUBLISHEDITEMS:
			case UA_PUBSUB_DATASET_PUBLISHEDEVENTS:
			case UA_PUBSUB_DATASET_PUBLISHEDEVENTS_TEMPLATE:
				break;
			case UA_PUBSUB_DATASET_PUBLISHEDITEMS_TEMPLATE:
				if (this.txt_diName.getText().isEmpty()) {
					setErrorMessage("Name must be specified!");
					return false;
				} else if (this.txt_diDescription.getText().isEmpty()) {
					setErrorMessage("Description must be specified!");
					return false;
				}
				break;
			default:
				break;
			}
		}

		setErrorMessage(null);

		return super.isPageComplete();
	}

	public void setFieldType(PubSubPublishedDataSetType fieldType) {
		switch (fieldType) {
		case UA_PUBSUB_DATASET_PUBLISHEDEVENTS:
			this.cmp_diTmpConfig.setVisible(false);
			this.cmp_evtConfig.setVisible(true);
			this.cmp_evtTmpConfig.setVisible(false);

			((GridData) this.cmp_diTmpConfig.getLayoutData()).exclude = true;
			((GridData) this.cmp_evtConfig.getLayoutData()).exclude = false;
			((GridData) this.cmp_evtTmpConfig.getLayoutData()).exclude = true;
			break;
		case UA_PUBSUB_DATASET_PUBLISHEDEVENTS_TEMPLATE:
			this.cmp_diTmpConfig.setVisible(false);
			this.cmp_evtConfig.setVisible(false);
			this.cmp_evtTmpConfig.setVisible(true);

			((GridData) this.cmp_diTmpConfig.getLayoutData()).exclude = true;
			((GridData) this.cmp_evtConfig.getLayoutData()).exclude = true;
			((GridData) this.cmp_evtTmpConfig.getLayoutData()).exclude = false;
			break;
		case UA_PUBSUB_DATASET_PUBLISHEDITEMS_TEMPLATE:
			this.cmp_diTmpConfig.setVisible(true);
			this.cmp_evtConfig.setVisible(false);
			this.cmp_evtTmpConfig.setVisible(false);

			((GridData) this.cmp_diTmpConfig.getLayoutData()).exclude = false;
			((GridData) this.cmp_evtConfig.getLayoutData()).exclude = true;
			((GridData) this.cmp_evtTmpConfig.getLayoutData()).exclude = true;
			break;
		case UA_PUBSUB_DATASET_PUBLISHEDITEMS:
		default:
			break;
		}

		Composite parent = (Composite) getControl();
		parent.layout(true, true);

		this.fieldType = fieldType;

		setModelValue();
	}

//	protected PubSubPublishedDataSetType getFieldType() {
//		return this.fieldType;
//	}
	
	private void setDefaultValues() {
		
	}

	private void setModelValue() {
		Object config = getWizard().getElement().getConfig();
		if (config == null) {
			config = createNewConfig();
		}

		if (config instanceof PubSubPublishedDataItemsTemplate) {
			WrapperDataSetMetaData metadata = ((PubSubPublishedDataItemsTemplate) config).getMetaData();

			txt_diName.setText(metadata.getName());
			txt_diDescription.setText(metadata.getDescription().getText());
			txt_diClassId.setText(metadata.getDataSetClassId().toString());
			
//			txt_diVersion.setText(metadata.getConfigurationVersion().toString());
			
			// PublishedVariableDataType[] variableToAdd =
			// ((PubSubPublishedDataItemsTemplate) config)
			// .getVariablesToAdd();

			this.model = ((PubSubPublishedDataItemsTemplate) config).clone();
			
			getWizard().pageSeven.setDefaultValues();
		} else if (config instanceof PubSubPublishedEventTemplate) {

		}
	}

	private Object createNewConfig() {
		Object config = null;
		switch (this.fieldType) {
		case UA_PUBSUB_DATASET_PUBLISHEDITEMS_TEMPLATE:
			config = new PubSubPublishedDataItemsTemplate();
			((PubSubPublishedDataItemsTemplate) config).init();
			return config;
		default:
			break;
		}
		return null;
	}

	private void setHandler() {
		this.txt_diName.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				boolean isComplete = isPageComplete();
				setPageComplete(isComplete);
			}
		});

		this.txt_diDescription.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				boolean isComplete = isPageComplete();
				setPageComplete(isComplete);
			}
		});

		this.btn_diFields.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				getContainer().showPage(getWizard().pageSeven);
			}

		});

		this.btn_diClassId.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				txt_diClassId.setText(UUID.randomUUID().toString());
			}

		});

		this.btn_diVersion.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				getContainer().showPage(getWizard().pageFour);
			}

		});

		this.btn_diVariablesToAdd.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				getContainer().showPage(getWizard().pageSix);
			}

		});

		if (this.btn_apply != null) {
			this.btn_apply.addSelectionListener(new SelectionAdapter() {

				@Override
				public void widgetSelected(SelectionEvent e) {
					switch (fieldType) {
					case UA_PUBSUB_DATASET_PUBLISHEDITEMS_TEMPLATE:
//						model = new PubSubPublishedDataItemsTemplate();
//						((PubSubPublishedDataItemsTemplate) model).setMetaData(null);
//						((PubSubPublishedDataItemsTemplate) model).setVariablesToAdd(null);
						break;
					default:
						break;
					}
				}
			});
		}
	}

	private void initDIConfig() {
		Label lblName = new Label(this.cmp_diTmpConfig, SWT.NONE);
		lblName.setText("Name");

		txt_diName = new Text(this.cmp_diTmpConfig, SWT.BORDER);
		txt_diName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(cmp_diTmpConfig, SWT.NONE);

		Label lblDescription = new Label(this.cmp_diTmpConfig, SWT.NONE);
		lblDescription.setText("Description");

		txt_diDescription = new Text(this.cmp_diTmpConfig, SWT.BORDER);
		txt_diDescription.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(cmp_diTmpConfig, SWT.NONE);

		Label lblFields = new Label(this.cmp_diTmpConfig, SWT.NONE);
		lblFields.setText("Fields");

		txt_diFields = new Text(this.cmp_diTmpConfig, SWT.BORDER | SWT.READ_ONLY);
		txt_diFields.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		btn_diFields = new Button(this.cmp_diTmpConfig, SWT.PUSH);
		btn_diFields.setText(" ");

		Label lblClassId = new Label(this.cmp_diTmpConfig, SWT.NONE);
		lblClassId.setText("ClassId");

		txt_diClassId = new Text(this.cmp_diTmpConfig, SWT.BORDER | SWT.READ_ONLY);
		txt_diClassId.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		btn_diClassId = new Button(this.cmp_diTmpConfig, SWT.PUSH);
		btn_diClassId.setText(" ");

		Label lblVersion = new Label(this.cmp_diTmpConfig, SWT.NONE);
		lblVersion.setText("Version");

		txt_diVersion = new Text(this.cmp_diTmpConfig, SWT.BORDER | SWT.READ_ONLY);
		txt_diVersion.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		btn_diVersion = new Button(this.cmp_diTmpConfig, SWT.NONE);
		btn_diVersion.setText(" ");

		Label lblVariablesToAdd = new Label(this.cmp_diTmpConfig, SWT.NONE);
		lblVariablesToAdd.setText("variablestoadd");

		txt_diVariablesToAdd = new Text(this.cmp_diTmpConfig, SWT.BORDER | SWT.READ_ONLY);
		txt_diVariablesToAdd.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		btn_diVariablesToAdd = new Button(this.cmp_diTmpConfig, SWT.PUSH);
		btn_diVariablesToAdd.setText(" ");

		/*
		 * Label lblNamespaces = new Label(this.cmp_diTmpConfig, SWT.NONE);
		 * lblNamespaces.setText("Namespaces");
		 * 
		 * txt_Namespaces = new Text(this.cmp_diTmpConfig, SWT.BORDER);
		 * txt_Namespaces.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
		 * 1, 1));
		 * 
		 * btn_diNamespaces = new Button(this.cmp_diTmpConfig, SWT.PUSH);
		 * btn_diNamespaces.setText(" ");
		 * 
		 * Label lblStructureDataTypes = new Label(this.cmp_diTmpConfig, SWT.NONE);
		 * lblStructureDataTypes.setText("StructureDataTypes");
		 * 
		 * txt_StructureDataTypes = new Text(this.cmp_diTmpConfig, SWT.BORDER);
		 * txt_StructureDataTypes.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
		 * false, 1, 1));
		 * 
		 * btn_diStructureDataTypes = new Button(this.cmp_diTmpConfig, SWT.PUSH);
		 * btn_diStructureDataTypes.setText(" ");
		 * 
		 * Label lblEnumDataTypes = new Label(this.cmp_diTmpConfig, SWT.NONE);
		 * lblEnumDataTypes.setText("StructureDataTypes");
		 * 
		 * txt_EnumDataTypes = new Text(this.cmp_diTmpConfig, SWT.BORDER);
		 * txt_EnumDataTypes.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
		 * false, 1, 1));
		 * 
		 * btn_diEnumDataTypes = new Button(this.cmp_diTmpConfig, SWT.PUSH);
		 * btn_diEnumDataTypes.setText(" ");
		 * 
		 * Label lblSimpleDataTypes = new Label(this.cmp_diTmpConfig, SWT.NONE);
		 * lblSimpleDataTypes.setText("SimpleDataTypes");
		 * 
		 * txt_SimpleDataTypes = new Text(this.cmp_diTmpConfig, SWT.BORDER);
		 * txt_SimpleDataTypes.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
		 * false, 1, 1));
		 * 
		 * btn_diSimpleDataTypes = new Button(this.cmp_diTmpConfig, SWT.PUSH);
		 * btn_diSimpleDataTypes.setText(" ");
		 */
	}

	private void initEvtConfig() {
		Label lblNewLabel = new Label(this.cmp_evtConfig, SWT.NONE);
		lblNewLabel.setText("eventnotifier");

		txt_evtEventNotifier = new Text(this.cmp_evtConfig, SWT.BORDER);
		txt_evtEventNotifier.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(cmp_evtConfig, SWT.NONE);

		Label lblNewLabel1 = new Label(this.cmp_evtConfig, SWT.NONE);
		lblNewLabel1.setText("contentfilter");

		txt_evtContentFilter = new Text(this.cmp_evtConfig, SWT.BORDER);
		txt_evtContentFilter.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(cmp_evtConfig, SWT.NONE);
	}

	private void initEvtTmpConfig() {
		Label lblNewLabel = new Label(this.cmp_evtTmpConfig, SWT.NONE);
		lblNewLabel.setText("metaData");

		txt_evttmpMetaData = new Text(this.cmp_evtTmpConfig, SWT.BORDER);
		txt_evttmpMetaData.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(cmp_evtTmpConfig, SWT.NONE);

		Label lblNewLabel1 = new Label(this.cmp_evtTmpConfig, SWT.NONE);
		lblNewLabel1.setText("eventnotifier");

		txt_evttmpEventnotifier = new Text(this.cmp_evtTmpConfig, SWT.BORDER);
		txt_evttmpEventnotifier.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(cmp_evtTmpConfig, SWT.NONE);

		Label lblNewLabel3 = new Label(this.cmp_evtTmpConfig, SWT.NONE);
		lblNewLabel3.setText("selectedfields");

		txt_evttmpSelectedFields = new Text(this.cmp_evtTmpConfig, SWT.BORDER);
		txt_evttmpSelectedFields.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(cmp_evtTmpConfig, SWT.NONE);

		Label lblNewLabel4 = new Label(this.cmp_evtTmpConfig, SWT.NONE);
		lblNewLabel4.setText("contentfilter");

		txt_evttmpContentFilter = new Text(this.cmp_evtTmpConfig, SWT.BORDER);
		txt_evttmpContentFilter.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(cmp_evtTmpConfig, SWT.NONE);
	}
}
