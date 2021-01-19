package com.bichler.astudio.opcua.opcmodeler.editor.node;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeSelection;
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
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.opcfoundation.ua.builtintypes.BuiltinsMap;
import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.DateTime;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.StatusCode;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.core.Attributes;
import org.opcfoundation.ua.core.Identifiers;

import com.bichler.astudio.utils.internationalization.CustomString;
import com.bichler.astudio.utils.ui.swt.CheckBoxButton;
import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.opcua.modeller.controls.CometCombo;
import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.opcua.opcmodeler.dialogs.DateTimeDialog;
import com.bichler.astudio.opcua.opcmodeler.dialogs.NodeValueDialog;
import com.bichler.astudio.opcua.opcmodeler.editor.node.validation.converters.ArrayDimStringConverter;
import com.bichler.astudio.opcua.opcmodeler.editor.node.validation.converters.VariantStringConverter;
import com.bichler.astudio.opcua.opcmodeler.editor.node.validation.fields.ArrayDimFieldValidator;
import com.bichler.astudio.opcua.opcmodeler.utils.extern.DesignerUtils;
import com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.nodes.BrowserModelNode;
import com.richclientgui.toolbox.validation.ValidatingField;

import opc.sdk.core.enums.BuiltinType;
import opc.sdk.core.enums.ValueRanks;
import opc.sdk.core.node.DataTypeNode;
import opc.sdk.core.node.Node;
import opc.sdk.core.node.UAVariableTypeNode;
import opc.sdk.core.node.VariableTypeNode;
import opc.sdk.core.types.TypeInfo;

public class NodeEditorVariableTypePart extends NodeEditorExtensionPart {
	public static final String ID = "com.hbsoft.designer.editor.node.NodeEditorVariableTypePart"; //$NON-NLS-1$
	private CheckBoxButton cb_isAbstract;
	private Text txt_arrayDimensions;
	// private Text txt_value;
	private CometCombo cmb_valueType;
	private CometCombo cmb_valueRank;
	private TreeViewer tv_dataType;
	private Label lbl_isAbstract;
	private Label lbl_arrayDimensions;
	private Label lbl_valueRank;
	private Label lbl_value;
	private ControlDecoration cd_arrayDimensions;
	private ArrayDimFieldValidator<UnsignedInteger[]> arrayDimValidator;
	// protected ValueRanks valueRank = ValueRanks.Scalar;
	private boolean showModellingRule = false;
	private Composite typeViewerComposite;
	private VariantStringConverter txt_value_converter;
	private ValidatingField<Variant> txt_value;
	private Map<ValueRanks, UnsignedInteger[]> cache_arraydimension = new HashMap<>();
	private Map<ValueRanks, Map<NodeId, Variant>> cache_value = new HashMap<>();

	public NodeEditorVariableTypePart() {
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
				"NodeEditorVariableTypePart.frm_mainForm.text") + getEditorInput().getName());
	}

	@Override
	protected void setInputs() {
		super.setInputs();
		VariableTypeNode node = (VariableTypeNode) this.getEditorInput().getNode().getNode();
		this.controllCreationToolkit.log(Status.INFO,
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "NodeEditorVariableTypePart.open.info"),
				node);
		/** vRank */
		ValueRanks vRank = ValueRanks.getValueRanks(((VariableTypeNode) node).getValueRank());
		this.controllCreationToolkit.setComboValueRank(this.cmb_valueRank, vRank, this.getEditorInput().getNode());
		this.cmb_valueRank.setText(vRank.name());
		/** array dimension */
		this.arrayDimValidator = new ArrayDimFieldValidator<UnsignedInteger[]>(cache_arraydimension);
		this.controllCreationToolkit.setTextArrayDimensions(this.cd_arrayDimensions, arrayDimValidator,
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "ArrayDimensionValidator.error"),
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "ArrayDimensionValidator.warning"),
				node.getValueRank(), node.getArrayDimensions(), this);
		/** isAbstract */
		this.cb_isAbstract.setChecked(((VariableTypeNode) node).getIsAbstract());
		/** dataType */
		this.controllCreationToolkit.setDataTypeTreeViewer(this.tv_dataType);
		NodeId dataTypeId = node.getDataType();
		/** get all tree items */
		this.tv_dataType.expandAll();
		TreeItem[] items = this.tv_dataType.getTree().getItems();
		TreeItem it = null;
		for (TreeItem item : items) {
			it = this.findTreeItem(item, dataTypeId);
			if (it != null) {
				// this.variableType = (UAVariableTypeNode)it.getData();
				this.tv_dataType.getTree().setSelection(it);
				break;
			}
		}
		/** reference */
		// this.controllCreationToolkit.setVariableTypeReferenceTable(tv_references,
		// node);
		/** value */
		this.txt_value_converter.setAfterEditorInput();
		Variant valu = node.getValue();
		/** combo valuetype */
		this.controllCreationToolkit.setComboValueType(this.cmb_valueType, node.getDataType());
		BuiltinType type = BuiltinType.Null;

		if (valu != null && !valu.isEmpty()) {
			if (Identifiers.ByteString.equals(node.getDataType())) {
				type = BuiltinType.ByteString;
			} else {
				Class<? extends Object> c = valu.getCompositeClass();
				if (c != null) {
					type = BuiltinType.getType(new NodeId(0, BuiltinsMap.ID_MAP.get(c)));
				}
			}
			this.cmb_valueType.setText(type.name());
			// Object data = this.cmb_valueType.getData(type.name());
			// if (data == null) {
			// this.cmb_valueType.setData(type.name(), valu);
			// }
		}
		setCachedValue(vRank, type, valu);
		setValueInput(valu);
	}

	private void setValueInput(Variant value) {
		this.txt_value_converter.setValue(value);
		this.txt_value_converter.setDataType(this.cmb_valueType.getText());
		BrowserModelNode element = (BrowserModelNode) ((IStructuredSelection) tv_dataType.getSelection())
				.getFirstElement();
		this.txt_value_converter.setDataTypeId(element.getNode().getNodeId());
		this.txt_value.setContents(value);
		if (value == null) {
			((Text) this.txt_value.getControl()).setText("");
		}
	}

	private TreeItem findTreeItem(TreeItem root, NodeId key) {
		Object data = null;
		if (root.getData() instanceof BrowserModelNode) {
			data = ((BrowserModelNode) root.getData()).getNode();
		} else {
			data = root.getData();
		}
		if (data instanceof DataTypeNode && key.equals(((DataTypeNode) data).getNodeId())) {
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

	private void selectComboValueType() {
		// BuiltinType valueType =
		// BuiltinType.valueOf(this.cmb_valueType.getText());
		Variant value = (Variant) this.cmb_valueType.getData(this.cmb_valueType.getText());
		// this.value.setValue(value);
		this.txt_value_converter.setValue(value);
		this.txt_value.setContents(value);
		if (value == null) {
			((Text) this.txt_value.getControl()).setText("");
		}
		/**
		 * When changing value type
		 */
		validate();
	}

	private TreeItem dataTypeItem;

	@Override
	protected void setHandlers() {
		super.setHandlers();
		this.controllCreationToolkit.setDirtyListener(lbl_isAbstract, cb_isAbstract, this);
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
					case Enumeration:
						e.doit = false;
						break;
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
					case Enumeration:
						return;
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
		tv_dataType.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				setDirty(true);
				DataTypeNode node = (DataTypeNode) ((BrowserModelNode) ((TreeSelection) event.getSelection())
						.getFirstElement()).getNode();
				if (node == null) {
					// reset old selection because if we collapse and
					// the expand a node the selection is not show yet.
					if (dataTypeItem != null) {
						tv_dataType.getTree().setSelection(dataTypeItem);
					}
					return;
				} else {
					dataTypeItem = tv_dataType.getTree().getSelection()[0];
					txt_value_converter.setDataType(node.getBrowseName().getName());
					txt_value_converter.setDataTypeId(node.getNodeId());
					/**
					 * Combobox value type
					 */
					controllCreationToolkit.setComboValueType(cmb_valueType, node.getNodeId());
					selectComboValueType();
				}
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
			@Override
			public void mouseUp(MouseEvent e) {
				String txtValueType = cmb_valueType.getText();
				if (txtValueType.isEmpty()) {
					return;
				}
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
					case Enumeration:
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
						Text dimensions = txt_arrayDimensions;
						ArrayDimStringConverter converter = new ArrayDimStringConverter();
						UnsignedInteger[] dims = converter.convertFromString(dimensions.getText());
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
								// TODO: CHECK IF NEEDED
								tableValue.setValue(new Variant(val));
							}
						} else {
							tableValue.setValue(value);
						}
						BrowserModelNode element = (BrowserModelNode) ((IStructuredSelection) tv_dataType
								.getSelection()).getFirstElement();
						NodeValueDialog dialog = new NodeValueDialog(txt_value.getControl().getShell(), valueRank, dims,
								valueType, tableValue, element.getNode().getNodeId(),
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
		});
		/**
		 * ------------------------
		 */
		final Image errorImage = FieldDecorationRegistry.getDefault()
				.getFieldDecoration(FieldDecorationRegistry.DEC_ERROR).getImage();
		final Image warningImage = FieldDecorationRegistry.getDefault()
				.getFieldDecoration(FieldDecorationRegistry.DEC_WARNING).getImage();
		// if the value rank is changing, validate the arraydimension
		this.cmb_valueRank.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				arrayDimValidator.setValueRank(ValueRanks.valueOf(cmb_valueRank.getText()));
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
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		this.setFocus();
		if (this.valid()) {
			super.doSave(monitor);
			if (monitor.isCanceled()) {
				return;
			}
			UAVariableTypeNode node = (UAVariableTypeNode) getEditorInput().getNode().getNode();
			node.setValueRank(ValueRanks.valueOf(cmb_valueRank.getText()).getValue());
			String arrayDim = ((Text) this.cd_arrayDimensions.getControl()).getText();
			ArrayDimStringConverter converter = new ArrayDimStringConverter();
			node.setArrayDimensions(converter.convertFromString(arrayDim));
			node.setIsAbstract(this.cb_isAbstract.isChecked());
			/**
			 * Datatype id
			 */
			NodeId oldDataTypeId = node.getDataType();
			NodeId dataTypeId = createDataTypeId();
			node.setDataType(dataTypeId);
			// datatype change to all affected nodes
			if (!dataTypeId.equals(oldDataTypeId)) {
				DesignerUtils.askModelChangeAttribute(getEditorInput().getNode(), Attributes.DataType);
			}
			/**
			 * Value
			 */
			Variant value = this.txt_value.getContents();
			if (value != null) {
				DataValue datavalue = new DataValue(value, StatusCode.GOOD, DateTime.currentTime(),
						DateTime.currentTime());
				node.writeValue(Attributes.Value, datavalue);
			} else {
				node.setValue(Variant.NULL);
			}
			//
			this.frm_mainForm.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
					"NodeEditorVariableTypePart.frm_mainForm.text") + getEditorInput().getName());
			doSaveFinish();
		}
	}

	private NodeId createDataTypeId() {
		IStructuredSelection selection = (IStructuredSelection) this.tv_dataType.getSelection();
		BrowserModelNode node = (BrowserModelNode) selection.getFirstElement();
		return node.getNode().getNodeId();
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
		ValueRanks valueRank = ValueRanks.valueOf(this.cmb_valueRank.getText());
		String txtType = this.cmb_valueType.getText();
		// skip type if empty because it is notified when combo is removing its
		// data
		if (txtType != null && !txtType.isEmpty()) {
			BuiltinType type = BuiltinType.valueOf(txtType);
			Variant data = getCachedValue(valueRank, type);
			setValueInput(data);
			this.txt_value.validate();
		}
	}

	@Override
	public void dispose() {
		super.dispose();
		Node node = getEditorInput().getNode().getNode();
		this.controllCreationToolkit.log(Status.INFO,
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "NodeEditorVariableTypePart.close.info"),
				node);
	}

	@Override
	protected void createExtendedSection(Composite extended) {
		GridLayout gl_composite_2 = new GridLayout(3, false);
		gl_composite_2.horizontalSpacing = 10;
		extended.setLayout(gl_composite_2);
		Label lbl_dataType = new Label(extended, SWT.NONE);
		GridData gd_lbl_dataType = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
		gd_lbl_dataType.widthHint = 220;
		lbl_dataType.setLayoutData(gd_lbl_dataType);
		lbl_dataType.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
				"NodeEditorVariableTypePart.lbl_dataType.text"));
		formToolkit.adapt(lbl_dataType, true, true);
		this.typeViewerComposite = new Composite(extended, SWT.NONE);
		GridData tvgd = new GridData(SWT.FILL, SWT.FILL, false, false, 2, 1);
		tvgd.heightHint = 200;
		typeViewerComposite.setLayoutData(tvgd);
		typeViewerComposite.setLayout(new FillLayout());
		tv_dataType = new TreeViewer(typeViewerComposite, SWT.BORDER);
		Tree tb_dataType = tv_dataType.getTree();
		formToolkit.paintBordersFor(tb_dataType);
		lbl_valueRank = new Label(extended, SWT.NONE);
		GridData gd_lbl_valueRank = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_lbl_valueRank.widthHint = 220;
		lbl_valueRank.setLayoutData(gd_lbl_valueRank);
		lbl_valueRank.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
				"NodeEditorVariableTypePart.lbl_valueRank.text"));
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
				"NodeEditorVariableTypePart.lbl_arrayDimensions.text"));
		formToolkit.adapt(lbl_arrayDimensions, true, true);
		txt_arrayDimensions = new Text(extended, SWT.BORDER);
		GridData gd_txt_arrayDimensions = new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1);
		gd_txt_arrayDimensions.widthHint = 250;
		txt_arrayDimensions.setLayoutData(gd_txt_arrayDimensions);
		formToolkit.adapt(txt_arrayDimensions, true, true);
		cd_arrayDimensions = new ControlDecoration(txt_arrayDimensions, SWT.RIGHT | SWT.TOP);
		cd_arrayDimensions.setDescriptionText(
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "form.tooltip.arraydimension"));
		lbl_value = new Label(extended, SWT.NONE);
		GridData gd_lbl_value = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_lbl_value.widthHint = 220;
		lbl_value.setLayoutData(gd_lbl_value);
		lbl_value.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
				"NodeEditorVariableTypePart.lbl_value.text"));
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
		Text txtv = (Text) this.txt_value.getControl();
		GridData gd_txt_value = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_txt_value.widthHint = 200;
		txtv.setLayoutData(gd_txt_value);
		formToolkit.adapt(txtv, true, true);
		lbl_isAbstract = new Label(extended, SWT.NONE);
		GridData gd_lbl_isAbstract = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lbl_isAbstract.widthHint = 220;
		lbl_isAbstract.setLayoutData(gd_lbl_isAbstract);
		lbl_isAbstract.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
				"NodeEditorVariableTypePart.lbl_isAbstract.text"));
		formToolkit.adapt(lbl_isAbstract, true, true);
		cb_isAbstract = new CheckBoxButton(extended, SWT.NONE);
		GridData gd_cb_isAbstract = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_cb_isAbstract.widthHint = 250;
		cb_isAbstract.setLayoutData(gd_cb_isAbstract);
		cb_isAbstract.setTouchEnabled(true);
		// cb_isAbstract.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
		// "NodeEditorVariableTypePart.cb_isAbstract.text"));
		cb_isAbstract.setLeftMargin(8);
		formToolkit.paintBordersFor(cb_isAbstract);
		new Label(extended, SWT.NONE);
		lbl_writeMask = new Label(extended, SWT.NONE);
		GridData gd_lbl_writeMask = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lbl_writeMask.widthHint = 220;
		lbl_writeMask.setLayoutData(gd_lbl_writeMask);
		lbl_writeMask.setText(
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "NodeEditorPart.lbl_writeMask.text"));
		formToolkit.adapt(lbl_writeMask, true, true);
		txt_writeMask = new Text(extended, SWT.BORDER | SWT.READ_ONLY);
		GridData gd_txt_writeMask = new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1);
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
		GridData gd_txt_userWriteMask = new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1);
		gd_txt_userWriteMask.widthHint = 250;
		txt_userWriteMask.setLayoutData(gd_txt_userWriteMask);
		formToolkit.adapt(txt_userWriteMask, true, true);
	}

	/**
	 * Gets the current cached value in dialog
	 * 
	 * @param valueRank
	 * @param valueType
	 * @return
	 */
	private Variant getCachedValue(ValueRanks valueRank, BuiltinType valueType) {
		Map<NodeId, Variant> data = this.cache_value.get(valueRank);
		if (data == null) {
			data = new HashMap<>();
			data.put(valueType.getBuildinTypeId(), Variant.NULL);
			this.cache_value.put(valueRank, data);
		}
		Variant variant = data.get(valueType.getBuildinTypeId());
		return variant;
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

	@Override
	protected void preDisableWidgets() {
		// this.typeViewerComposite.setEnabled(false);
	}
}
