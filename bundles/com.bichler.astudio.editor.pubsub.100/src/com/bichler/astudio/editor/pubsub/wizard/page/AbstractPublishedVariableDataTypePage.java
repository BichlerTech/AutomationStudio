package com.bichler.astudio.editor.pubsub.wizard.page;

import java.util.Deque;

import org.eclipse.jface.dialogs.Dialog;
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
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.QualifiedName;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.Attributes;
import org.opcfoundation.ua.core.DeadbandType;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.NodeClass;

import com.bichler.astudio.editor.pubsub.dialog.QualifiedNameArrayDialog;
import com.bichler.astudio.editor.pubsub.dialog.VariantDialog;
import com.bichler.astudio.editor.pubsub.wizard.core.WrapperPublishedVariableParameter;
import com.bichler.astudio.opcua.components.ui.BrowsePathElement;
import com.bichler.astudio.opcua.components.ui.dialogs.OPCUABrowseUtils;
import com.bichler.astudio.opcua.components.ui.dialogs.OPCUANodeDialog;
import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;

import opc.sdk.core.utils.NumericRange;

public abstract class AbstractPublishedVariableDataTypePage extends AbstractDetailWizardPage {

	private Text txt_publishedVariable;
	private Button btn_PublishedVariable;
	private Text txt_attributeId;
	private Combo cmb_deadbandType;
	private Text txt_samplingIntervalHint;
	private Text txt_deadbandValue;
	private Text txt_indexRange;
	private Text txt_substituteValue;
	private Text txt_metadataProperties;
	private Button btn_substitueValue;
	private Button btn_metadataProperties;

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

		this.txt_publishedVariable = new Text(container, SWT.BORDER | SWT.READ_ONLY);
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

		this.cmb_deadbandType = new Combo(container, SWT.READ_ONLY);
		cmb_deadbandType.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		for (DeadbandType deadband : DeadbandType.values()) {
			cmb_deadbandType.add(deadband.name());
		}

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

		this.btn_metadataProperties = new Button(container, SWT.PUSH);
		btn_metadataProperties.setText(" ");

		setDefaultValue();
		setHandler();

		if (getElement() instanceof WrapperPublishedVariableParameter) {
			WrapperPublishedVariableParameter element = (WrapperPublishedVariableParameter) getElement();
			UnsignedInteger deadbandType = element.getDeadbandType();
			DeadbandType deadband = DeadbandType.valueOf(deadbandType);
			switch (deadband) {
			case None:
				this.cmb_deadbandType.select(0);
				break;
			case Absolute:
				this.cmb_deadbandType.select(1);
				break;
			case Percent:
				this.cmb_deadbandType.select(2);
				break;
			}
		} else {
			this.cmb_deadbandType.select(0);
		}

		createApply(container);
	}

	@Override
	public boolean isPageComplete() {
		if (this.txt_publishedVariable.getData() == null) {
			setErrorMessage("Published variable must be specified!");
			return false;
		}

		if (this.txt_attributeId.getText().isEmpty()) {
			setErrorMessage("AttributeId must be specified!");
			return false;
		} else {
			try {
				UnsignedInteger attrId = new UnsignedInteger(this.txt_attributeId.getText());
				if(attrId.longValue() < 1 && attrId.longValue() > 27) {
					setErrorMessage("AttributeId is not inside value range from 1 to 27!");
				}
			} catch (IllegalArgumentException e) {
				setErrorMessage("AttributeId must be a type of UnsignedInteger!");
			}
		}

		if (this.txt_samplingIntervalHint.getText().isEmpty()) {
			setErrorMessage("SamplingIntervalHint must be specified!");
			return false;
		}

		// deadband type and deadband value
		String deadbandType = this.cmb_deadbandType.getItem(this.cmb_deadbandType.getSelectionIndex());
		DeadbandType deadband = DeadbandType.valueOf(deadbandType);

		switch (deadband) {
		case None:
			break;
		default:
			if (this.txt_deadbandValue.getText().isEmpty()) {
				setErrorMessage("DeadbandValue must be specified!");
				return false;
			}

			Double deadbandVal = -1.0;
			try {
				deadbandVal = new Double(this.txt_deadbandValue.getText());
			} catch (NumberFormatException nfe) {
				setErrorMessage("DeadbandValue must be type of Double!");
				return false;
			}

			switch (deadband) {
			case Percent:
				if (deadbandVal < 0.0 && deadbandVal > 100.0) {
					setErrorMessage("Percent DeadbandValue is not inside value range from 0.0 to 100.0!");
					return false;
				}
				break;
			default:
				break;
			}

			break;
		}

		// index range

		if (!this.txt_indexRange.getText().isEmpty()) {
			String indexRange = this.txt_indexRange.getText();
			try {
				NumericRange.parse(indexRange);
			} catch (ServiceResultException e) {
				setErrorMessage("IndexRange is not valid!");
			}
		}

		setErrorMessage(null);
		return super.isPageComplete();
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

//			if (publishedVariableDataType.getDeadbandType() != null) {
//				this.cmb_deadbandType.setText(publishedVariableDataType.getDeadbandType().toString());
//			}

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

		this.btn_PublishedVariable.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				NodeId selection = (NodeId) txt_publishedVariable.getData();

				OPCUANodeDialog dialog = new OPCUANodeDialog(getShell());
				dialog.setFormTitle("Formtitle");
				dialog.setInternalServer(ServerInstance.getInstance().getServerInstance());
				dialog.setNodeClassFilter(NodeClass.Variable);

				if (!NodeId.isNull(selection)) {
//					Deque<BrowsePathElement> browsepath = OPCUABrowseUtils.getFullBrowsePath(selection,
//							ServerInstance.getInstance().getServerInstance(), Identifiers.ObjectsFolder);
//					dialog.setBrowsePath(browsepath);
					dialog.setSelectedNodeId(selection);
				}

				int open = dialog.open();
				if (open == Dialog.OK) {
					selection = dialog.getSelectedNodeId();
					txt_publishedVariable.setData(selection);
					Deque<BrowsePathElement> browsepath = dialog.getBrowsePath();
					txt_publishedVariable.setText(OPCUANodeDialog.toBrowsePath(browsepath));
				}
			}

		});

		this.txt_publishedVariable.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				boolean isComplete = isPageComplete();
				setPageComplete(isComplete);
			}
		});

		this.txt_attributeId.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				boolean isComplete = isPageComplete();
				setPageComplete(isComplete);
			}
		});

		this.txt_samplingIntervalHint.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				boolean isComplete = isPageComplete();
				setPageComplete(isComplete);
			}
		});

		this.cmb_deadbandType.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				String selection = cmb_deadbandType.getItem(cmb_deadbandType.getSelectionIndex());
				DeadbandType deadbandType = DeadbandType.valueOf(selection);
				switch (deadbandType) {
				case None:
					txt_deadbandValue.setEnabled(false);
					break;
				default:
					txt_deadbandValue.setEnabled(true);
					break;
				}

				boolean isComplete = isPageComplete();
				setPageComplete(isComplete);
			}
		});

		this.txt_deadbandValue.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				boolean isComplete = isPageComplete();
				setPageComplete(isComplete);
			}
		});

		this.btn_metadataProperties.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				QualifiedNameArrayDialog dialog = new QualifiedNameArrayDialog(getShell());
				if (Dialog.OK == dialog.open()) {

				}
			}
		});

		this.btn_substitueValue.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				VariantDialog dialog = new VariantDialog(getShell());
				if (Dialog.OK == dialog.open()) {

				}
			}
		});

		if (this.btn_apply != null) {
			this.btn_apply.addSelectionListener(new SelectionAdapter() {

				@Override
				public void widgetSelected(SelectionEvent e) {
					WrapperPublishedVariableParameter pvdt = new WrapperPublishedVariableParameter();

					pvdt.setAttributeId(getAttributeId());
					pvdt.setDeadbandType(getDeadbandType());
					pvdt.setDeadbandValue(getDeadbandValue());
					pvdt.setIndexRange(getIndexRange());
					pvdt.setMetaDataProperties(getMetadataProperties());
					pvdt.setPublishedVariable(getPublishedVariable());
					pvdt.setSamplingIntervalHint(getSamplingInterval());
					pvdt.setSubstituteValue(getSubstituteValue());

					model = pvdt;
				}

			});
		}
	}

	private UnsignedInteger getAttributeId() {
		return null;
	}

	private UnsignedInteger getDeadbandType() {
		return null;
	}

	private Double getDeadbandValue() {
		return null;
	}

	private String getIndexRange() {
		return null;
	}

	private QualifiedName[] getMetadataProperties() {
		return null;
	}

	private NodeId getPublishedVariable() {
		return null;
	}

	private Double getSamplingInterval() {
		return null;
	}

	private Variant getSubstituteValue() {
		return null;
	}

	abstract Object getElement();

}
