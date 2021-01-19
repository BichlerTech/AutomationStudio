package com.bichler.astudio.editor.pubsub.wizard.page;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.bichler.astudio.editor.pubsub.wizard.core.WrapperPublishedVariableParameter;

public abstract class AbstractPublishedVariableDataTypePage extends AbstractDetailWizardPage {

	private Text txt_publishedVariable;
	private Button btn_PublishedVariable;
	private Text txt_attributeId;
	private Text txt_deadbandType;
	private Text txt_samplingIntervalHint;
	private Text txt_deadbandValue;
	private Text txt_indexRange;
	private Text txt_substituteValue;
	private Text txt_metadataProperties;
	private Button btn_substitueValue;

	private WrapperPublishedVariableParameter model;

	public AbstractPublishedVariableDataTypePage() {
		super("publishedvariabledatatypepage");
		setTitle("Published variable data type");
		setDescription("Properties of a published variable data type");
	}

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);

		setControl(container);
		container.setLayout(new GridLayout(3, false));

		Label lblPublishedvariable = new Label(container, SWT.NONE);
		lblPublishedvariable.setText("PublishedVariable");

		this.txt_publishedVariable = new Text(container, SWT.BORDER);
		txt_publishedVariable.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		this.btn_PublishedVariable = new Button(container, SWT.PUSH);
		btn_PublishedVariable.setText(" ");

		Label lblAttributeid = new Label(container, SWT.NONE);
		lblAttributeid.setText("AttributeId");

		this.txt_attributeId = new Text(container, SWT.BORDER);
		txt_attributeId.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		new Label(container, SWT.NONE);

		Label lblSamplingintervalhint = new Label(container, SWT.NONE);
		lblSamplingintervalhint.setText("SamplingIntervalHint");

		this.txt_samplingIntervalHint = new Text(container, SWT.BORDER);
		txt_samplingIntervalHint.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		new Label(container, SWT.NONE);

		Label lblDeadbandtype = new Label(container, SWT.NONE);
		lblDeadbandtype.setText("DeadbandType");

		this.txt_deadbandType = new Text(container, SWT.BORDER);
		txt_deadbandType.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		new Label(container, SWT.NONE);

		Label lblDeadbandvalue = new Label(container, SWT.NONE);
		lblDeadbandvalue.setText("DeadbandValue");

		this.txt_deadbandValue = new Text(container, SWT.BORDER);
		txt_deadbandValue.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		new Label(container, SWT.NONE);

		Label lblIndexrange = new Label(container, SWT.NONE);
		lblIndexrange.setText("IndexRange");

		this.txt_indexRange = new Text(container, SWT.BORDER);
		txt_indexRange.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		new Label(container, SWT.NONE);

		Label lblSubstitutevalue = new Label(container, SWT.NONE);
		lblSubstitutevalue.setText("SubstituteValue");

		this.txt_substituteValue = new Text(container, SWT.BORDER);
		txt_substituteValue.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		this.btn_substitueValue = new Button(container, SWT.PUSH);
		btn_substitueValue.setText(" ");

		Label lblMetadataproperties = new Label(container, SWT.NONE);
		lblMetadataproperties.setText("MetadataProperties");

		this.txt_metadataProperties = new Text(container, SWT.BORDER);
		txt_metadataProperties.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		new Label(container, SWT.NONE);

		setDefaultValue();
		setHandler();

		// createApply(container);

	}

	public WrapperPublishedVariableParameter getPublishedVariableDataType() {
		return this.model;
	}

	private void setDefaultValue() {
		Object element = getElement();
		if (getElement() instanceof WrapperPublishedVariableParameter) {
			WrapperPublishedVariableParameter publishedVariableDataType = (WrapperPublishedVariableParameter) element;
			if (publishedVariableDataType == null) {
				return;
			}

			this.model = publishedVariableDataType.clone();

			if (publishedVariableDataType.getPublishedVariable() != null) {
				this.txt_publishedVariable.setText(publishedVariableDataType.getPublishedVariable().toString());
			}

			if (publishedVariableDataType.getAttributeId() != null) {
				this.txt_attributeId.setText(publishedVariableDataType.getAttributeId().toString());
			}

			if (publishedVariableDataType.getDeadbandType() != null) {
				this.txt_deadbandType.setText(publishedVariableDataType.getDeadbandType().toString());
			}

			if (publishedVariableDataType.getDeadbandValue() != null) {
				this.txt_deadbandValue.setText(publishedVariableDataType.getDeadbandType().toString());
			}

			if (publishedVariableDataType.getIndexRange() != null) {
				this.txt_indexRange.setText(publishedVariableDataType.getIndexRange().toString());
			}

			if (publishedVariableDataType.getMetaDataProperties() != null) {
				this.txt_metadataProperties.setText(publishedVariableDataType.getMetaDataProperties().toString());
			}

			if (publishedVariableDataType.getSamplingIntervalHint() != null) {
				this.txt_samplingIntervalHint.setText(publishedVariableDataType.getSamplingIntervalHint().toString());
			}

			if (publishedVariableDataType.getSubstituteValue() != null) {
				this.txt_substituteValue.setText(publishedVariableDataType.getSubstituteValue().toString());
			}
		}
	}

	private void setHandler() {
		this.txt_publishedVariable.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				boolean isComplete = isPageComplete();
				setPageComplete(isComplete);
			}
		});
	}
	
	abstract Object getElement();

}
