package com.bichler.astudio.opcua.opcmodeler.editor.node;

import java.util.Calendar;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.opcfoundation.ua.builtintypes.BuiltinsMap;
import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.DateTime;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.StatusCode;
import org.opcfoundation.ua.builtintypes.UnsignedByte;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.AccessLevel;
import org.opcfoundation.ua.core.Attributes;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.WriteValue;

import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.core.user.util.UserUtils;
import com.bichler.astudio.opcua.modeller.controls.CometCombo;
import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.opcua.opcmodeler.dialogs.DateTimeDialog;
import com.bichler.astudio.opcua.opcmodeler.dialogs.NodeValueDialog;
import com.bichler.astudio.opcua.opcmodeler.editor.node.validation.converters.ArrayDimStringConverter;
import com.bichler.astudio.opcua.opcmodeler.editor.node.validation.converters.VariantStringConverter;
import com.bichler.astudio.opcua.opcmodeler.editor.node.validation.fields.ArrayDimFieldValidator;
import com.bichler.astudio.opcua.opcmodeler.editor.node.validation.fields.DoubleFieldValidator;
import com.bichler.astudio.opcua.opcmodeler.utils.extern.DesignerUtils;
import com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.nodes.BrowserModelNode;
import com.bichler.astudio.utils.internationalization.CustomString;
import com.bichler.astudio.utils.ui.swt.CheckBoxButton;
import com.richclientgui.toolbox.validation.ValidatingField;

import opc.sdk.core.enums.BuiltinType;
import opc.sdk.core.enums.ValueRanks;
import opc.sdk.core.node.NamingRuleType;
import opc.sdk.core.node.Node;
import opc.sdk.core.node.UAVariableNode;
import opc.sdk.core.node.VariableNode;
import opc.sdk.core.node.VariableTypeNode;
import opc.sdk.core.types.TypeInfo;

public class NodeEditorVariablePart extends NodeEditorExtensionPart {
	public static final String ID = "com.hbsoft.designer.editor.node.NodeEditorVariablePart"; //$NON-NLS-1$
	// protected ValueRanks valueRank = ValueRanks.Scalar;
	private Text txt_arrayDimensions;
	// private Text txt_value;
	private CometCombo cmb_valueType;
	private CometCombo cmb_valueRank;
	private TreeViewer tv_variableType;
	private CometCombo cmb_modellingRule;
	private Label lbl_modellingRule;
	private Label lbl_arrayDimensions;
	private Label lbl_valueRank;
	private Label lbl_value;
	private Label lbl_dataType;
	private CheckBoxButton btn_historizing = null;
	private CheckBoxButton btn_currentRead = null;
	private CheckBoxButton btn_currentWrite = null;
	private CheckBoxButton btn_historyRead = null;
	private CheckBoxButton btn_historyWrite;
	private CheckBoxButton btn_semanticChange;
	private CheckBoxButton btn_statusWrite;
	private CheckBoxButton btn_timestampWrite;
	private CheckBoxButton btn_userCurrentRead;
	private CheckBoxButton btn_userCurrentWrite;
	private CheckBoxButton btn_userHistoryRead;
	private CheckBoxButton btn_userHistoryWrite;
	private CheckBoxButton btn_userSemanticChange = null;
	private CheckBoxButton btn_userStatusWrite;
	private CheckBoxButton btn_userTimestampWrite;
	private DoubleFieldValidator samplingIntvalidator = null;
	private CometCombo cmb_dataType;
	private Text txt_samplingInt;
	private boolean showModellingRule = false;
	private Composite typeViewerComposite;
	private ControlDecoration cd_arrayDimensions;
	private ArrayDimFieldValidator<UnsignedInteger[]> arrayDimValidator;
	private ValidatingField<Variant> txt_value;
	private VariantStringConverter txt_value_converter;
	private ValueRanks valueRank = ValueRanks.Any;
	private BuiltinType builtinType = BuiltinType.Null;
	private Map<ValueRanks, UnsignedInteger[]> cache_arraydimension = new HashMap<>();
	private Map<ValueRanks, Map<NodeId, Variant>> cache_value = new HashMap<>();


	public NodeEditorVariablePart() {
		super();
	}

	/**
	 * Create contents of the editor part.
	 * 
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent) {
		super.createPartControl(parent);
		frm_mainForm.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
				"NodeEditorVariablePart.frm_mainForm.text") + getEditorInput().getName());
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		this.setFocus();
		if (this.valid()) {
			super.doSave(monitor);
			if (monitor.isCanceled()) {
				return;
			}
			UAVariableNode node = (UAVariableNode) getEditorInput().getNode().getNode();
			/** will we need to save modelling rule ? */
			if (showModellingRule) {
				if (this.cmb_modellingRule.getText().compareTo(CustomString
						.getString(Activator.getDefault().RESOURCE_BUNDLE, "ModellingRule.norule.text")) == 0) {
					/** if we previously hab an rule so we need to delete it */
					this.controllCreationToolkit.deleteModellingRule(node);
				} else {
					this.controllCreationToolkit.setModellingRule(node,
							NamingRuleType.valueOf(this.cmb_modellingRule.getText()));
				}
			}
			int accessLevel = 0;
			if (this.btn_currentRead != null && this.btn_currentRead.isChecked()) {
				accessLevel += AccessLevel.CurrentRead.getValue();
			}
			if (this.btn_currentWrite  != null && this.btn_currentWrite.isChecked()) {
				accessLevel += AccessLevel.CurrentWrite.getValue();
			}
			if (this.btn_historyRead  != null && this.btn_historyRead.isChecked()) {
				accessLevel += AccessLevel.HistoryRead.getValue();
			}
			if (this.btn_historyWrite  != null && this.btn_historyWrite.isChecked()) {
				accessLevel += AccessLevel.HistoryWrite.getValue();
			}
			if (this.btn_semanticChange  != null && this.btn_semanticChange.isChecked()) {
				accessLevel += AccessLevel.SemanticChange.getValue();
			}
			if (this.btn_statusWrite  != null && this.btn_statusWrite.isChecked()) {
				accessLevel += AccessLevel.StatusWrite.getValue();
			}
			if (this.btn_timestampWrite  != null && this.btn_timestampWrite.isChecked()) {
				accessLevel += AccessLevel.TimestampWrite.getValue();
			}
			node.setAccessLevel(new UnsignedByte(accessLevel));
			
			int userAccessLevel = 0;
			if (this.btn_userCurrentRead  != null && this.btn_userCurrentRead.isChecked()) {
				userAccessLevel += AccessLevel.CurrentRead.getValue();
			}
			if (this.btn_userCurrentWrite  != null && this.btn_userCurrentWrite.isChecked()) {
				userAccessLevel += AccessLevel.CurrentWrite.getValue();
			}
			if (this.btn_userHistoryRead != null && this.btn_userHistoryRead.isChecked()) {
				userAccessLevel += AccessLevel.HistoryRead.getValue();
			}
			if (this.btn_userHistoryWrite != null && this.btn_userHistoryWrite.isChecked()) {
				userAccessLevel += AccessLevel.HistoryWrite.getValue();
			}
			if (this.btn_userSemanticChange  != null && this.btn_userSemanticChange.isChecked()) {
				userAccessLevel += AccessLevel.SemanticChange.getValue();
			}
			if (this.btn_userStatusWrite  != null && this.btn_userStatusWrite.isChecked()) {
				userAccessLevel += AccessLevel.StatusWrite.getValue();
			}
			if (this.btn_userTimestampWrite  != null && this.btn_userTimestampWrite.isChecked()) {
				userAccessLevel += AccessLevel.TimestampWrite.getValue();
			}
			node.setUserAccessLevel(new UnsignedByte(userAccessLevel));

			boolean isHistorizing = false;
			if (this.btn_historizing != null) {
				isHistorizing = this.btn_historizing.isChecked();
			}
			node.setHistorizing(isHistorizing);

			String text = this.cmb_valueRank.getText();
			if (text != null && !text.isEmpty()) {
				node.setValueRank(ValueRanks.valueOf(this.cmb_valueRank.getText()).getValue());
			} else {
				node.setValueRank(ValueRanks.Any.getValue());
			}
			String arrayDim = ((Text) this.cd_arrayDimensions.getControl()).getText();
			ArrayDimStringConverter converter = new ArrayDimStringConverter();
			node.setArrayDimensions(converter.convertFromString(arrayDim));
			HashMap<?, ?> datatypes = (HashMap<?, ?>) this.cmb_dataType.getData();
			int index = this.cmb_dataType.getSelectionIndex();
			int count = 0;
			for (Entry<?, ?> value : datatypes.entrySet()) {
				// add datatype
				if (count == index) {
					NodeId newDataTypeId = (NodeId) value.getKey();
					node.setDataType(newDataTypeId);
					break;
				}
				count++;
			}
			Variant value = this.txt_value.getContents();
			// Variant value = createValue();
			if (value != null) {
				DataValue datavalue = new DataValue(value, StatusCode.GOOD, DateTime.currentTime(),
						DateTime.currentTime());
				// node.writeValue(Attributes.Value, datavalue);
				try {
					ServerInstance.getInstance().getServerInstance().getMaster().write(
							new WriteValue[] { new WriteValue(node.getNodeId(), Attributes.Value, null, datavalue) },
							true, new Long[] { -1l }, null);
				} catch (ServiceResultException e) {
					Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
				}
			} else {
				node.setValue(Variant.NULL);
			}
			String txtMinSampInt = this.txt_samplingInt.getText();
			double minSamplingInterval = txtMinSampInt != null ? Double.parseDouble(txtMinSampInt) : 1000;
			node.setMinimumSamplingInterval(minSamplingInterval);
			this.frm_mainForm.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
					"NodeEditorVariablePart.frm_mainForm.text") + getEditorInput().getName());
			doSaveFinish();
		}
	}

	@Override
	public void dispose() {
		super.dispose();
		Node node = getEditorInput().getNode().getNode();
		this.controllCreationToolkit.log(Status.INFO,
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "NodeEditorVariablePart.close.info"),
				node);
	}

	@Override
	protected void createExtendedSection(Composite extended) {
		GridLayout gl_composite_2 = new GridLayout(3, false);
		gl_composite_2.horizontalSpacing = 10;
		extended.setLayout(gl_composite_2);
		Label lbl_variableType = new Label(extended, SWT.NONE);
		GridData gd_lbl_variableType = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lbl_variableType.widthHint = 220;
		lbl_variableType.setLayoutData(gd_lbl_variableType);
		lbl_variableType.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
				"NodeEditorVariablePart.lbl_variableType.text"));
		formToolkit.adapt(lbl_variableType, true, true);
		this.typeViewerComposite = new Composite(extended, SWT.NONE);
		GridData gd = new GridData(SWT.FILL, SWT.FILL, false, false, 2, 1);
		gd.heightHint = 200;
		typeViewerComposite.setLayoutData(gd);
		typeViewerComposite.setLayout(new FillLayout());
		tv_variableType = new TreeViewer(typeViewerComposite, SWT.BORDER);
		Tree tb_dataType = tv_variableType.getTree();
		// tb_dataType.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
		// true,
		// 2, 1));
		formToolkit.paintBordersFor(tb_dataType);
		lbl_dataType = new Label(extended, SWT.NONE);
		GridData gd_lbl_dataType = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lbl_dataType.widthHint = 220;
		lbl_dataType.setLayoutData(gd_lbl_dataType);
		lbl_dataType.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
				"NodeEditorVariablePart.lbl_dataType.text"));
		formToolkit.adapt(lbl_dataType, true, true);
		cmb_dataType = new CometCombo(extended, SWT.READ_ONLY);
		GridData gd_cmb_dataType = new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1);
		gd_cmb_dataType.widthHint = 250;
		cmb_dataType.setLayoutData(gd_cmb_dataType);
		formToolkit.adapt(cmb_dataType);
		formToolkit.paintBordersFor(cmb_dataType);
		lbl_valueRank = new Label(extended, SWT.NONE);
		GridData gd_lbl_valueRank = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_lbl_valueRank.widthHint = 220;
		lbl_valueRank.setLayoutData(gd_lbl_valueRank);
		lbl_valueRank.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
				"NodeEditorVariablePart.lbl_valueRank.text"));
		formToolkit.adapt(lbl_valueRank, true, true);
		cmb_valueRank = new CometCombo(extended, SWT.READ_ONLY);
		GridData gd_cmb_valueRank = new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1);
		gd_cmb_valueRank.widthHint = 250;
		cmb_valueRank.setLayoutData(gd_cmb_valueRank);
		formToolkit.adapt(cmb_valueRank);
		formToolkit.paintBordersFor(cmb_valueRank);
		lbl_arrayDimensions = new Label(extended, SWT.NONE);
		GridData gd_lbl_arrayDimensions = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_lbl_arrayDimensions.widthHint = 220;
		lbl_arrayDimensions.setLayoutData(gd_lbl_arrayDimensions);
		lbl_arrayDimensions.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
				"NodeEditorVariablePart.lbl_arrayDimensions.text"));
		formToolkit.adapt(lbl_arrayDimensions, true, true);
		txt_arrayDimensions = new Text(extended, SWT.BORDER);
		GridData gd_txt_arrayDimensions = new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1);
		gd_txt_arrayDimensions.widthHint = 250;
		txt_arrayDimensions.setLayoutData(gd_txt_arrayDimensions);
		formToolkit.adapt(txt_arrayDimensions, true, true);
		cd_arrayDimensions = new ControlDecoration(txt_arrayDimensions, SWT.RIGHT | SWT.TOP);
		cd_arrayDimensions.setDescriptionText("Some Description");
		lbl_value = new Label(extended, SWT.NONE);
		GridData gd_lbl_value = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_lbl_value.widthHint = 220;
		lbl_value.setLayoutData(gd_lbl_value);
		lbl_value.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
				"NodeEditorVariablePart.lbl_value.text"));
		formToolkit.adapt(lbl_value, true, true);
		cmb_valueType = new CometCombo(extended, SWT.READ_ONLY);
		GridData gd_cmb_valueType = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_cmb_valueType.widthHint = 250;
		cmb_valueType.setLayoutData(gd_cmb_valueType);
		formToolkit.adapt(cmb_valueType);
		formToolkit.paintBordersFor(cmb_valueType);
		this.txt_value_converter = new VariantStringConverter("", this.cmb_valueType, this.cmb_valueRank, true);
		this.txt_value = this.controllCreationToolkit.createTextValue(extended, this.txt_value_converter, cmb_valueType,
				cmb_valueRank, this.txt_arrayDimensions, this.cache_value);
		Text textFieldValue = (Text) this.txt_value.getControl();
		GridData gd_txt_value = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_txt_value.widthHint = 200;
		textFieldValue.setLayoutData(gd_txt_value);
		formToolkit.adapt(textFieldValue, true, true);
		/** will we need it ? */
		if (!DesignerUtils.isChildOf(getEditorInput().getNode().getNode().getNodeId(), Identifiers.ObjectsFolder)) {
			showModellingRule = true;
			lbl_modellingRule = new Label(extended, SWT.NONE);
			GridData gd_lbl_modellingRule = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
			gd_lbl_modellingRule.widthHint = 220;
			lbl_modellingRule.setLayoutData(gd_lbl_modellingRule);
			lbl_modellingRule.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
					"NodeEditorVariablePart.lbl_modellingRule.text"));
			formToolkit.adapt(lbl_modellingRule, true, true);
			cmb_modellingRule = new CometCombo(extended, SWT.READ_ONLY);
			GridData gd_cmb_modellingRule = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
			gd_cmb_modellingRule.widthHint = 250;
			cmb_modellingRule.setLayoutData(gd_cmb_modellingRule);
			formToolkit.adapt(cmb_modellingRule);
			formToolkit.paintBordersFor(cmb_modellingRule);
			new Label(extended, SWT.NONE);
		}

		boolean adminControl = UserUtils.testUserRights(1);

		Label lbl_accessLevel = new Label(extended, SWT.NONE);
		GridData gd_lbl_accessLevel = new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 1);
		gd_lbl_accessLevel.widthHint = 220;
		lbl_accessLevel.setLayoutData(gd_lbl_accessLevel);
		lbl_accessLevel.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
				"NodeEditorVariablePart.lbl_accessLevel.text"));
		formToolkit.adapt(lbl_accessLevel, true, true);
		Group gp_accessLevel = new Group(extended, SWT.NONE);
		GridData gd_gp_accessLevel = new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1);
		// gd_gp_accessLevel.widthHint = 433;
		gp_accessLevel.setLayoutData(gd_gp_accessLevel);
		{
			btn_currentRead = new CheckBoxButton(gp_accessLevel, SWT.NONE);
			btn_currentRead.setTouchEnabled(true);
			btn_currentRead.setBounds(5, 3, 180, 25);
			btn_currentRead.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
					"CreateVariableDialog.btn_currentRead.text")); //$NON-NLS-1$
		}
		{
			btn_currentWrite = new CheckBoxButton(gp_accessLevel, SWT.NONE);
			btn_currentWrite.setTouchEnabled(true);
			btn_currentWrite.setBounds(185, 3, 140, 25);
			btn_currentWrite.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
					"CreateVariableDialog.btn_currentWrite.text")); //$NON-NLS-1$
		}
		if (adminControl) {
			btn_historyRead = new CheckBoxButton(gp_accessLevel, SWT.NONE);
			btn_historyRead.setTouchEnabled(true);
			btn_historyRead.setBounds(325, 3, 140, 25);
			btn_historyRead.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
					"CreateVariableDialog.btn_historyRead.text")); //$NON-NLS-1$
		}
		if (adminControl) {
			btn_historyWrite = new CheckBoxButton(gp_accessLevel, SWT.NONE);
			btn_historyWrite.setTouchEnabled(true);
			btn_historyWrite.setBounds(465, 3, 180, 25);
			btn_historyWrite.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
					"CreateVariableDialog.btn_historyWrite.text")); //$NON-NLS-1$
		}
		if (adminControl) {
			btn_semanticChange = new CheckBoxButton(gp_accessLevel, SWT.NONE);
			btn_semanticChange.setTouchEnabled(true);
			btn_semanticChange.setBounds(5, 30, 180, 25);
			btn_semanticChange.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
					"CreateVariableDialog.btn_semanticChange.text")); //$NON-NLS-1$
		}
		if (adminControl) {
			btn_statusWrite = new CheckBoxButton(gp_accessLevel, SWT.NONE);
			btn_statusWrite.setTouchEnabled(true);
			btn_statusWrite.setBounds(185, 30, 140, 25);
			btn_statusWrite.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
					"CreateVairableDialog.Accesslevel.StatusWrite")); //$NON-NLS-1$
		}
		if (adminControl) {
			btn_timestampWrite = new CheckBoxButton(gp_accessLevel, SWT.NONE);
			btn_timestampWrite.setTouchEnabled(true);
			btn_timestampWrite.setBounds(325, 30, 140, 25);
			btn_timestampWrite.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
					"CreateVairableDialog.Accesslevel.TimestampWrite")); //$NON-NLS-1$
		}
		
		
		Label lbl_userAccessLevel = new Label(extended, SWT.NONE);
		lbl_userAccessLevel.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
				"NodeEditorVariablePart.lbl_userAccessLevel.text"));
		GridData gd_lbl_userAccessLevel = new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 1);
		gd_lbl_userAccessLevel.widthHint = 220;
		lbl_userAccessLevel.setLayoutData(gd_lbl_userAccessLevel);
		formToolkit.adapt(lbl_userAccessLevel, true, true);
		Group gp_userAccessLevel = new Group(extended, SWT.NONE);
		GridData gd_gp_userAccessLevel = new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1);
		// gd_gp_userAccessLevel.widthHint = 459;
		gp_userAccessLevel.setLayoutData(gd_gp_userAccessLevel);
		{
			btn_userCurrentRead = new CheckBoxButton(gp_userAccessLevel, SWT.NONE);
			btn_userCurrentRead.setTouchEnabled(true);
			btn_userCurrentRead.setBounds(5, 3, 180, 25);
			btn_userCurrentRead.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
					"CreateVariableDialog.btn_userCurrentRead.text")); //$NON-NLS-1$
		}
		{
			btn_userCurrentWrite = new CheckBoxButton(gp_userAccessLevel, SWT.NONE);
			btn_userCurrentWrite.setTouchEnabled(true);
			btn_userCurrentWrite.setBounds(185, 3, 140, 25);
			btn_userCurrentWrite.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
					"CreateVariableDialog.btn_userCurrentWrite.text")); //$NON-NLS-1$
		}
		if (adminControl) {
			btn_userHistoryRead = new CheckBoxButton(gp_userAccessLevel, SWT.NONE);
			btn_userHistoryRead.setTouchEnabled(true);
			btn_userHistoryRead.setBounds(325, 3, 140, 25);
			btn_userHistoryRead.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
					"CreateVariableDialog.btn_historyRead.text")); //$NON-NLS-1$
		}
		if (adminControl) {
			btn_userHistoryWrite = new CheckBoxButton(gp_userAccessLevel, SWT.NONE);
			btn_userHistoryWrite.setTouchEnabled(true);
			btn_userHistoryWrite.setBounds(465, 3, 180, 25);
			btn_userHistoryWrite.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
					"CreateVariableDialog.btn_historyWrite.text")); //$NON-NLS-1$
		}
		if (adminControl) {
			btn_userSemanticChange = new CheckBoxButton(gp_userAccessLevel, SWT.NONE);
			btn_userSemanticChange.setTouchEnabled(true);
			btn_userSemanticChange.setBounds(5, 30, 180, 25);
			btn_userSemanticChange.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
					"CreateVariableDialog.btn_semanticChange.text")); //$NON-NLS-1$
		}
		
		if (adminControl) {
			btn_userStatusWrite = new CheckBoxButton(gp_userAccessLevel, SWT.NONE);
			btn_userStatusWrite.setTouchEnabled(true);
			btn_userStatusWrite.setBounds(185, 30, 140, 25);
			btn_userStatusWrite.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
					"CreateVairableDialog.Accesslevel.StatusWrite")); //$NON-NLS-1$
		}
		if (adminControl) {
			btn_userTimestampWrite = new CheckBoxButton(gp_userAccessLevel, SWT.NONE);
			btn_userTimestampWrite.setTouchEnabled(true);
			btn_userTimestampWrite.setBounds(325, 30, 140, 25);
			btn_userTimestampWrite.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
					"CreateVairableDialog.Accesslevel.TimestampWrite")); //$NON-NLS-1$
		}
		
		
		{
			Label lbl_minSamplInt = new Label(extended, SWT.NONE);
			lbl_minSamplInt.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
					"CreateVariableDialog.lbl_minSamplInt.text"));
			GridData gd_lbl_minSamplInt = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
			gd_lbl_minSamplInt.widthHint = 220;
			lbl_minSamplInt.setLayoutData(gd_lbl_minSamplInt);
			formToolkit.adapt(lbl_minSamplInt, true, true);
			txt_samplingInt = new Text(extended, SWT.BORDER);
			GridData gd_txt_samplingInt = new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1);
			gd_txt_samplingInt.widthHint = 250;
			txt_samplingInt.setLayoutData(gd_txt_samplingInt);
			formToolkit.adapt(txt_samplingInt, true, true);
		}
		if (adminControl) {
			{
				Label lbl_historizing = new Label(extended, SWT.NONE);
				lbl_historizing.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
						"CreateVariableDialog.lbl_historizing.text"));
				GridData gd_lbl_historizing = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
				gd_lbl_historizing.widthHint = 220;
				lbl_historizing.setLayoutData(gd_lbl_historizing);
				formToolkit.adapt(lbl_historizing, true, true);
			}
			{
				btn_historizing = new CheckBoxButton(extended, SWT.NONE);
				btn_historizing.setLeftMargin(8);
				btn_historizing.setTouchEnabled(true);
				// btn_historizing.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,"CreateVariableDialog.btn_historizing.text"));
				// //$NON-NLS-1$
			}
			new Label(extended, SWT.NONE);
		}

		lbl_writeMask = new Label(extended, SWT.NONE);
		GridData gd_lbl_writeMask = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lbl_writeMask.widthHint = 220;
		lbl_writeMask.setLayoutData(gd_lbl_writeMask);
		lbl_writeMask.setText(
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "NodeEditorPart.lbl_writeMask.text"));
		formToolkit.adapt(lbl_writeMask, true, true);
		txt_writeMask = new Text(extended, SWT.BORDER | SWT.READ_ONLY);
		GridData gd_txt_writeMask = new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1);
		gd_txt_writeMask.widthHint = 250;
		txt_writeMask.setLayoutData(gd_txt_writeMask);
		formToolkit.adapt(txt_writeMask, true, true);
		lbl_userWriteMask = new Label(extended, SWT.NONE);
		GridData gd_lbl_userWriteMask = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lbl_userWriteMask.widthHint = 220;
		lbl_userWriteMask.setLayoutData(gd_lbl_userWriteMask);
		lbl_userWriteMask.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
				"NodeEditorPart.lbl_userWriteMask.text"));
		formToolkit.adapt(lbl_userWriteMask, true, true);
		txt_userWriteMask = new Text(extended, SWT.BORDER | SWT.READ_ONLY);
		GridData gd_txt_userWriteMask = new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1);
		gd_txt_userWriteMask.widthHint = 250;
		txt_userWriteMask.setLayoutData(gd_txt_userWriteMask);
		formToolkit.adapt(txt_userWriteMask, true, true);
	}

	@Override
	protected void setHandlers() {
		super.setHandlers();
		if (lbl_dataType != null) {
			this.controllCreationToolkit.setDirtyListener(lbl_dataType, cmb_dataType, this);
		}
		if (btn_currentRead != null) {
			this.controllCreationToolkit.setDirtyListener(null, this.btn_currentRead, this);
		}
		if (btn_currentWrite != null) {
			this.controllCreationToolkit.setDirtyListener(null, this.btn_currentWrite, this);
		}
		if (btn_historyRead != null) {
			this.controllCreationToolkit.setDirtyListener(null, this.btn_historyRead, this);
		}
		if (btn_historyWrite != null) {
			this.controllCreationToolkit.setDirtyListener(null, this.btn_historyWrite, this);
		}
		if (btn_semanticChange != null) {
			this.controllCreationToolkit.setDirtyListener(null, this.btn_semanticChange, this);
		}
		if (btn_userCurrentRead != null) {
			this.controllCreationToolkit.setDirtyListener(null, this.btn_userCurrentRead, this);
		}
		if (btn_userCurrentWrite != null) {
			this.controllCreationToolkit.setDirtyListener(null, this.btn_userCurrentWrite, this);
		}
		if (btn_userHistoryRead != null) {
			this.controllCreationToolkit.setDirtyListener(null, this.btn_userHistoryRead, this);
		}
		if (btn_userHistoryWrite != null) {
			this.controllCreationToolkit.setDirtyListener(null, this.btn_userHistoryWrite, this);
		}
		if (btn_userSemanticChange != null) {
			this.controllCreationToolkit.setDirtyListener(null, this.btn_userSemanticChange, this);
		}
		if (btn_historizing != null) {
			this.controllCreationToolkit.setDirtyListener(null, this.btn_historizing, this);
		}
		if (showModellingRule) {
			this.controllCreationToolkit.setDirtyListener(lbl_modellingRule, cmb_modellingRule, this);
		}
		this.controllCreationToolkit.setDirtyListener(lbl_valueRank, cmb_valueRank, this);
		this.controllCreationToolkit.setDirtyListener(lbl_value, cmb_valueType, this);
		// this.controllCreationToolkit.setDirtyListener(txt_value, this);
		((Text) txt_value.getControl()).addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				String txtValueType = cmb_valueType.getText();
				if (!txtValueType.isEmpty()) {
					BuiltinType valueType = BuiltinType.valueOf(txtValueType);
					switch (valueType) {
					// case Enumeration:
					// // e.doit = false;
					// break;
					default:
						break;
					}
				}
			}
		});
		((Text) txt_value.getControl()).addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				String txtValueType = cmb_valueType.getText();
				if (!txtValueType.isEmpty()) {
					BuiltinType valueType = BuiltinType.valueOf(txtValueType);
					switch (valueType) {
					// case Enumeration:
					// return;
					default:
						break;
					}
				}
				setDirty(true);
				txt_value.validate();
			}
		});
		/**
		 * Value Handler
		 */
		this.cmb_dataType.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				fillComboValueTypeFromDataTypeId();
			}
		});
		this.txt_arrayDimensions.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				validate();
			}
		});
		Text text = (Text) this.txt_value.getControl();
		text.addMouseListener(new MouseAdapter() {
			@SuppressWarnings("unchecked")
			@Override
			public void mouseUp(MouseEvent e) {
				String txtValueType = cmb_valueType.getText();
				if (txtValueType.isEmpty()) {
					return;
				}
				if (txtValueType.compareTo(BuiltinType.ExtensionObject.name()) == 0) {
					// try to get value type from datatype combo
					txtValueType = cmb_dataType.getText();
					// TODO:
				} else {

					BuiltinType valueType = BuiltinType.valueOf(txtValueType);
					boolean openSingle = false;
					switch (valueType) {
					case Null:
						return;
					case DateTime:
						openSingle = true;
						break;
					case ByteString:
						openSingle = true;
						break;
					case Enumeration:
						openSingle = true;
						break;
					default:
						openSingle = false;
						break;
					}

					String vRank = cmb_valueRank.getText();
					if (vRank.isEmpty()) {
						return;
					}
					ValueRanks valueRank = ValueRanks.valueOf(vRank);
					if (!openSingle && valueRank == ValueRanks.ScalarOrOneDimension) {
						return;
					}
					if (!openSingle && valueRank == ValueRanks.Any) {
						return;
					}
					if (!openSingle && valueRank == ValueRanks.Scalar) {
						return;
					}

					switch (valueRank) {
					case ScalarOrOneDimension:
					case Any:
					case Scalar:
						switch (valueType) {
						case Null:
							return;
						case DateTime:
							DateTimeDialog dialog = new DateTimeDialog(txt_value.getControl().getShell());
							int open = dialog.open();
							if (Dialog.OK == open) {
								Calendar dt = dialog.getDateTime();
								DateTime date = new DateTime(dt);
								Variant variant = new Variant(date);
								// value.setValue(variant);
								txt_value_converter.setValue(variant);
								txt_value.setContents(variant);
								cmb_valueType.setData(cmb_valueType.getText(), variant);
							}
							return;
						case Enumeration:
							return;
						case Boolean:
							break;
						case Byte:
							break;
						case ByteString:
							break;
						case DataValue:
							break;
						case DiagnosticInfo:
							break;
						case Double:
							break;
						case ExpandedNodeId:
							break;
						case ExtensionObject:
							break;
						case Float:
							break;
						case Guid:
							break;
						case Int16:
							break;
						case Int32:
							break;
						case Int64:
							break;
						case Integer:
							break;
						case LocalizedText:
							break;
						case NodeId:
							break;
						case Number:
							break;
						case QualifiedName:
							break;
						case SByte:
							break;
						case StatusCode:
							break;
						case String:
							break;
						case UInt16:
							break;
						case UInt32:
							break;
						case UInt64:
							break;
						case UInteger:
							break;
						case Variant:
							break;
						case XmlElement:
							break;
						default:
							break;
						}
					default:
						switch (valueType) {
						case Null:
							return;
						default:
							Variant value = txt_value.getContents();
							// check the value type
							TypeInfo typeInfo = TypeInfo.construct(new DataValue(value),
									ServerInstance.getInstance().getServerInstance().getTypeTable());
							DataValue tableValue = new DataValue();
							if (value == null || value.getValue() == null
									|| !(typeInfo.getBuiltInsType().equals(valueType))) {
								if (value != null && BuiltinType.Int32.equals(typeInfo.getBuiltInsType())
										&& BuiltinType.Enumeration.equals(valueType)) {
									tableValue.setValue(value);
								} else {
									Object val = DesignerFormToolkit.createDefaultValue(valueType, valueRank);
									tableValue.setValue(new Variant(val));
								}
							} else {
								tableValue.setValue(value);
							}
							// array dimensions
							Text dimensions = txt_arrayDimensions;
							ArrayDimStringConverter converter = new ArrayDimStringConverter();
							UnsignedInteger[] dims = null;
							switch (valueRank) {
							case Scalar:
								switch (valueType) {
								case ByteString:
									dims = new UnsignedInteger[] { UnsignedInteger.ONE };
									break;
								case Enumeration:
									dims = converter.convertFromString(dimensions.getText());
								case Boolean:
									break;
								case Byte:
									break;
								case DataValue:
									break;
								case DateTime:
									break;
								case DiagnosticInfo:
									break;
								case Double:
									break;
								case ExpandedNodeId:
									break;
								case ExtensionObject:
									break;
								case Float:
									break;
								case Guid:
									break;
								case Int16:
									break;
								case Int32:
									break;
								case Int64:
									break;
								case Integer:
									break;
								case LocalizedText:
									break;
								case NodeId:
									break;
								case Null:
									break;
								case Number:
									break;
								case QualifiedName:
									break;
								case SByte:
									break;
								case StatusCode:
									break;
								case String:
									break;
								case UInt16:
									break;
								case UInt32:
									break;
								case UInt64:
									break;
								case UInteger:
									break;
								case Variant:
									break;
								case XmlElement:
									break;
								default:
									break;
								}
								break;
							default:
								dims = converter.convertFromString(dimensions.getText());
								break;
							}
							Map<NodeId, String> data = (Map<NodeId, String>) cmb_dataType.getData();
							String datavalue = cmb_dataType.getText();
							NodeId datatypeId = NodeId.NULL;
							for (Entry<NodeId, String> entry : data.entrySet()) {
								if (datavalue.equals(entry.getValue())) {
									datatypeId = entry.getKey();
									break;
								}
							}
							NodeValueDialog dialog = new NodeValueDialog(txt_value.getControl().getShell(), valueRank,
									dims, valueType, tableValue, datatypeId,
									getEditorInput().getNode().getNode().getNodeId());
							int ok = dialog.open();
							if (Dialog.OK == ok) {
								// set value
								DataValue newValue = dialog.getNewValue();
								setValueInput(newValue.getValue());
							}
							break;
						}
					}
				}
			}
		});
		final Image errorImage = FieldDecorationRegistry.getDefault()
				.getFieldDecoration(FieldDecorationRegistry.DEC_ERROR).getImage();
		final Image warningImage = FieldDecorationRegistry.getDefault()
				.getFieldDecoration(FieldDecorationRegistry.DEC_WARNING).getImage();
		// if the value rank is changing, validate the arraydimension
		this.cmb_valueRank.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				valueRank = ValueRanks.valueOf(cmb_valueRank.getText());
				arrayDimValidator.setValueRank(valueRank);
				ArrayDimStringConverter converter = new ArrayDimStringConverter();
				UnsignedInteger[] arrayDim = converter
						.convertFromString(((Text) cd_arrayDimensions.getControl()).getText());
				if (!arrayDimValidator.isValid(arrayDim)) {
					cd_arrayDimensions.setImage(errorImage);
					cd_arrayDimensions.setDescriptionText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
							"ArrayDimensionValidator.error"));
				} else if (arrayDimValidator.warningExist(arrayDim)) {
					cd_arrayDimensions.setImage(warningImage);
					cd_arrayDimensions.setDescriptionText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
							"ArrayDimensionValidator.error"));
				} else {
					cd_arrayDimensions.setImage(null);
					cd_arrayDimensions.setDescriptionText("");
				}
			}
		});
		cmb_dataType.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				HashMap<?, ?> datatypes = (HashMap<?, ?>) cmb_dataType.getData();
				int index = cmb_dataType.getSelectionIndex();
				NodeId newDataTypeId = null;
				int count = 0;
				for (Entry<?, ?> value : datatypes.entrySet()) {
					// add datatype
					if (count == index) {
						newDataTypeId = (NodeId) value.getKey();
						controllCreationToolkit.setComboValueType(cmb_valueType, newDataTypeId);
						break;
					}
					count++;
				}
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void setInputs() {
		super.setInputs();
		UAVariableNode node = (UAVariableNode) this.getEditorInput().getNode().getNode();
		this.controllCreationToolkit.log(Status.INFO,
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "NodeEditorVariablePart.open.info"),
				node);
		this.controllCreationToolkit.setVariableTypeTreeViewer(tv_variableType);
		NodeId variableTypeId = null;
		try {
			variableTypeId = ServerInstance.getInstance().getServerInstance().getNamespaceUris()
					.toNodeId(node.findTarget(Identifiers.HasTypeDefinition, false));
			/** get all tree items */
			this.tv_variableType.expandAll();
			TreeItem[] items = this.tv_variableType.getTree().getItems();
			TreeItem it = null;
			for (TreeItem item : items) {
				it = this.findTreeItem(item, variableTypeId);
				if (it != null) {
					// this.variableType = (UAVariableTypeNode)it.getData();
					this.tv_variableType.getTree().setSelection(it);
					break;
				}
			}
			// select combo
			this.controllCreationToolkit.setComboDataType(cmb_dataType, variableTypeId);
			HashMap<NodeId, String> datatypes = (HashMap<NodeId, String>) this.cmb_dataType.getData();
			if (datatypes != null) {
				String selection = datatypes.get(node.getDataType());
				for (int i = 0; i < this.cmb_dataType.getItemCount(); i++) {
					String item = this.cmb_dataType.getItem(i);
					if (item != null && item.equals(selection)) {
						this.cmb_dataType.select(i);
						break;
					}
				}
			}
		} catch (ServiceResultException e) {
			e.printStackTrace();
		}
		/** value rank */
		this.valueRank = ValueRanks.getValueRanks(node.getValueRank());
		this.controllCreationToolkit.setComboValueRank(this.cmb_valueRank, valueRank, this.getEditorInput().getNode());
		this.cmb_valueRank.setText(valueRank.name());
		/** array dimension */
		this.arrayDimValidator = new ArrayDimFieldValidator<UnsignedInteger[]>(cache_arraydimension);
		this.controllCreationToolkit.setTextArrayDimensions(this.cd_arrayDimensions, arrayDimValidator,
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "ArrayDimensionValidator.error"),
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "ArrayDimensionValidator.warning"),
				node.getValueRank(), node.getArrayDimensions(), this);
		/** value type */
		this.builtinType = BuiltinType.Null;
		if (!node.getValue().isEmpty()) {
			Class<? extends Object> clazz = node.getValue().getValue().getClass();
			// wrapp array
			if (clazz.isArray()) {
				clazz = clazz.getComponentType();
			}
			Integer id = null;
			if (Identifiers.ByteString.equals(node.getDataType())) {
				id = BuiltinType.ByteString.getValue();
			} else {
				id = BuiltinsMap.ID_MAP.get(clazz);
			}

			if (id != null) {
				this.builtinType = BuiltinType.getType(new NodeId(0, id));
			}
		}
		fillComboValueTypeFromDataTypeId();
		/** value type */
		this.cmb_valueType.setText(this.builtinType.name());
		/** value */
		this.txt_value_converter.setAfterEditorInput();
		Variant valu = ((VariableNode) node).getValue();
		/** data */
		// Object data = this.cmb_valueType.getData(this.builtinType.name());
		// if (data == null) {
		// this.cmb_valueType.setData(this.builtinType.name(), valu);
		// }
		setCachedValue(this.valueRank, this.builtinType, valu);
		setValueInput(valu);
		// if (valu != null && !valu.isEmpty()) {
		// String value = valu.getValue().toString();
		// this.controllCreationToolkit.setValue(this.txt_value, value);
		//
		// }
		if (showModellingRule) {
			this.controllCreationToolkit.setComboModellingRule(this.cmb_modellingRule, node);
		}
		UnsignedByte accessLevel = node.getAccessLevel();
		EnumSet<AccessLevel> accessLevelSet = AccessLevel.getSet(accessLevel.intValue());
		if (this.btn_currentRead != null && accessLevelSet.contains(AccessLevel.CurrentRead)) {
			this.btn_currentRead.setChecked(true);
		}
		if (this.btn_currentWrite != null && accessLevelSet.contains(AccessLevel.CurrentWrite)) {
			this.btn_currentWrite.setChecked(true);
		}
		if (this.btn_historyRead  != null && accessLevelSet.contains(AccessLevel.HistoryRead)) {
			this.btn_historyRead.setChecked(true);
		}
		if (this.btn_historyWrite  != null && accessLevelSet.contains(AccessLevel.HistoryWrite)) {
			this.btn_historyWrite.setChecked(true);
		}
		if (this.btn_semanticChange != null && accessLevelSet.contains(AccessLevel.SemanticChange)) {
			this.btn_semanticChange.setChecked(true);
		}
		UnsignedByte userAccessLevel = node.getUserAccessLevel();
		EnumSet<AccessLevel> userAccessLevelSet = AccessLevel.getSet(userAccessLevel.intValue());
		if (this.btn_userCurrentRead  != null && userAccessLevelSet.contains(AccessLevel.CurrentRead)) {
			this.btn_userCurrentRead.setChecked(true);
		}
		if (this.btn_userCurrentWrite  != null && userAccessLevelSet.contains(AccessLevel.CurrentWrite)) {
			this.btn_userCurrentWrite.setChecked(true);
		}
		if (this.btn_userHistoryRead  != null && userAccessLevelSet.contains(AccessLevel.HistoryRead)) {
			this.btn_userHistoryRead.setChecked(true);
		}
		if (this.btn_userHistoryWrite  != null && userAccessLevelSet.contains(AccessLevel.HistoryWrite)) {
			this.btn_userHistoryWrite.setChecked(true);
		}
		if (this.btn_userSemanticChange  != null && userAccessLevelSet.contains(AccessLevel.SemanticChange)) {
			this.btn_userSemanticChange.setChecked(true);
		}
		if(this.btn_historizing  != null) {
			this.btn_historizing.setChecked(node.getHistorizing());
		}
		// this.controllCreationToolkit.setVariableReferenceTable(tv_references, node);
		this.txt_samplingInt.setText(node.getMinimumSamplingInterval() + "");
		this.samplingIntvalidator = new DoubleFieldValidator();
		{
			ControlDecoration cd_minSampling = new ControlDecoration(txt_samplingInt, SWT.RIGHT | SWT.TOP);
			cd_minSampling.setDescriptionText("Minimum sampling interval for this variable node");
			this.controllCreationToolkit.setTextDoubleValidation(cd_minSampling, this.samplingIntvalidator, this);
		}
	}

	@Override
	protected void preDisableWidgets() {
		this.typeViewerComposite.setEnabled(false);
	}

	@Override
	protected boolean valid() {
		if (!super.valid()) {
			return false;
		}
		if (this.cd_arrayDimensions.isVisible()) {
			return false;
		}
		if (!this.txt_value.isValid()) {
			String tx = ((Text) this.txt_value.getControl()).getText();
			if (tx.isEmpty()) {
				return true;
			}
			return false;
		}
		return true;
	}

	@Override
	protected void validate() {
		super.validate();
		String vr = this.cmb_valueRank.getText();
		ValueRanks valueRank = ValueRanks.Any;
		try {
			valueRank = ValueRanks.valueOf(vr);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		// String txtType = this.cmb_valueType.getText();
		String type2 = this.cmb_dataType.getText();
		NodeId datatypeId = NodeId.NULL;
		for (Entry<NodeId, String> entry : ((Map<NodeId, String>) this.cmb_dataType.getData()).entrySet()) {
			if (type2.equals(entry.getValue())) {
				datatypeId = entry.getKey();
				break;
			}
		}
		// skip type if empty because it is notified when combo is removing its
		// data
		if (type2 != null && !type2.isEmpty()) {
			Variant data = getCachedValue(valueRank, datatypeId);
			if (data == null) {
				data = Variant.NULL;
			}
			setValueInput(data);
			this.txt_value.validate();
		}
	}

	/**
	 * Gets the current cached value in dialog
	 * 
	 * @param valueRank
	 * @param valueType
	 * @return
	 */
	private Variant getCachedValue(ValueRanks valueRank, NodeId datatype) {
		Map<NodeId, Variant> data = this.cache_value.get(valueRank);
		if (data == null) {
			data = new HashMap<>();
			data.put(datatype, Variant.NULL);
			this.cache_value.put(valueRank, data);
		}
		Variant variant = data.get(datatype);
		return variant;
	}

	private void fillComboValueTypeFromDataTypeId() {
		Map<NodeId, String> data = (Map<NodeId, String>) cmb_dataType.getData();
		String txt = cmb_dataType.getText();

		for (Entry<NodeId, String> entry : data.entrySet()) {
			if (txt.equals(entry.getValue())) {
				controllCreationToolkit.setComboValueType(cmb_valueType, entry.getKey());
				// controllCreationToolkit.setComboDataType(cmb_valueType, new NodeId(1,
				// 853));
				validate();
				break;
			}
		}
	}

	private TreeItem findTreeItem(TreeItem root, NodeId key) {
		Object data = null;
		if (root.getData() instanceof BrowserModelNode) {
			data = ((BrowserModelNode) root.getData()).getNode();
		} else {
			data = root.getData();
		}
		if (data instanceof VariableTypeNode && key.equals(((VariableTypeNode) data).getNodeId())) {
			return root;
		} else if (root.getItemCount() > 0) {
			TreeItem it = null;
			for (TreeItem item : root.getItems()) {
				it = findTreeItem(item, key);
				if (it != null) {
					return it;
				}
			}
		}
		return null;
	}

	/**
	 * Sets the current value to instance cache
	 * 
	 * @param valueRank
	 * @param valueType
	 * @param variant
	 */
	private void setCachedValue(ValueRanks valueRank, BuiltinType valueType, Variant variant) {
		Map<NodeId, Variant> values2use = this.cache_value.get(valueRank);
		if (values2use == null) {
			values2use = new HashMap<>();
			this.cache_value.put(valueRank, values2use);
		}
		if (variant == null) {
			variant = Variant.NULL;
		}
		values2use.put(valueType.getBuildinTypeId(), variant);
	}

	private void setValueInput(Variant value) {
		this.txt_value_converter.setValue(value);
		this.txt_value_converter.setDataType(this.cmb_valueType.getText());
		Map<NodeId, String> data = (Map<NodeId, String>) this.cmb_dataType.getData();
		String text = this.cmb_dataType.getText();
		NodeId datatypeId = NodeId.NULL;
		for (Entry<NodeId, String> entry : data.entrySet()) {
			if (text.equals(entry.getValue())) {
				datatypeId = entry.getKey();
				break;
			}
		}
		txt_value_converter.setDataTypeId(datatypeId);
		this.txt_value.setContents(value);
		if (value == null) {
			((Text) this.txt_value.getControl()).setText("");
		} /*
			 * else { if(!value.isEmpty() && this.txt_value.isValid()){
			 * ((Text)this.txt_value.getControl()).setText(value.getValue().toString()); } }
			 */
	}
}
