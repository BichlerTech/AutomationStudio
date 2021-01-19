package com.bichler.astudio.opcua.opcmodeler.editor.node;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TreeItem;
import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.DateTime;
import org.opcfoundation.ua.builtintypes.DiagnosticInfo;
import org.opcfoundation.ua.builtintypes.Enumeration;
import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.ExtensionObject;
import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.QualifiedName;
import org.opcfoundation.ua.builtintypes.StatusCode;
import org.opcfoundation.ua.builtintypes.UnsignedByte;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.builtintypes.UnsignedLong;
import org.opcfoundation.ua.builtintypes.UnsignedShort;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.builtintypes.XmlElement;

import com.bichler.astudio.utils.internationalization.CustomString;
import com.bichler.astudio.utils.ui.swt.CheckBoxButton;
import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.opcua.modeller.controls.CometCombo;
import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.opcua.opcmodeler.dialogs.DateTimeDialog;
import com.bichler.astudio.opcua.opcmodeler.dialogs.NodeValueDialog;
import com.bichler.astudio.opcua.opcmodeler.editor.node.validation.converters.ArrayDimStringConverter;
import com.bichler.astudio.opcua.opcmodeler.editor.node.validation.converters.VariantStringConverter;
import com.bichler.astudio.opcua.opcmodeler.editor.node.validation.errors.ArrayDimQuickFixProvider;
import com.bichler.astudio.opcua.opcmodeler.editor.node.validation.fields.ArrayDimFieldValidator;
import com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.nodes.BrowserModelNode;
import com.richclientgui.toolbox.validation.ValidatingField;

import opc.sdk.core.enums.BuiltinType;
import opc.sdk.core.enums.ValueRanks;
import opc.sdk.core.node.DataTypeNode;
import opc.sdk.core.node.Node;
import opc.sdk.core.node.UAVariableTypeNode;
import opc.sdk.core.node.VariableNode;
import opc.sdk.core.node.VariableTypeNode;
import opc.sdk.core.types.TypeInfo;

public class NodeEditorVariableTypeContentProvider extends NodeEditorContentProvider {
	protected ValueRanks valueRank = ValueRanks.Scalar;
	protected TreeViewer treeViewer = null;
	protected TreeItem dataTypeItem = null;
	protected NodeId dataType = null;
	private BuiltinType valueType = BuiltinType.Null;
	protected DataValue value = null;
	protected DataValue scalar_or_onedimension_value = null;
	protected DataValue scalar_value = null;
	protected DataValue any_value = null;
	protected DataValue one_or_more_dimension_value = null;
	protected DataValue onedimension_value = null;
	protected DataValue twodimension_value = null;
	protected DataValue threedimension_value = null;
	protected DataValue fourdimension_value = null;
	protected DataValue fivedimension_value = null;
	protected CometCombo combo_valueType = null;
	protected CometCombo combo_valueRank = null;
	protected ArrayDimFieldValidator<UnsignedInteger[]> arrayDimensionValidator = null;
	protected ArrayDimQuickFixProvider<UnsignedInteger[]> arrayDimensionQuickFix = null;
	protected ValidatingField<UnsignedInteger[]> txt_arrayDimension = null;
	protected ValidatingField<Variant> txt_value = null;
	protected VariantStringConverter txt_value_converter = null;
	private CheckBoxButton cb_isAbstract = null;
	private Map<ValueRanks, Map<NodeId, Variant>> cache_value = new HashMap<>();
	private Map<ValueRanks, UnsignedInteger[]> cache_arraydimension = new HashMap<>();

	// private Boolean isAbstract = true;
	public NodeEditorVariableTypeContentProvider(NodeEditorPart nodeEditorPart) {
		super(nodeEditorPart);
	}

	protected void createContent(Node node, ScrolledComposite parent) {
		Composite composite = new Composite(parent, SWT.SHADOW_ETCHED_IN);
		parent.setContent(composite);
		GridLayoutFactory.fillDefaults().numColumns(1).applyTo(composite);
		Group nodeGroup = new Group(composite, SWT.NONE);
		/** first create all base node controls */
		createBaseNodeContent(nodeGroup, node);
		// Label * DataType
		Label lb_type = this.controllCreationToolkit.createLabel(nodeGroup, CustomString
				.getString(Activator.getDefault().RESOURCE_BUNDLE, "CreateVariableDialog.lbl_dataType.text"));
		GridDataFactory.fillDefaults().span(1, 20).applyTo(lb_type);
		// TreeViewer * DataType
		this.treeViewer = this.controllCreationToolkit.createDataTypeTreeViewer(nodeGroup);
		GridDataFactory.fillDefaults().span(2, 20).align(SWT.FILL, SWT.FILL).applyTo(this.treeViewer.getControl());
		// Label * Value Rank
		this.controllCreationToolkit.createLabel(nodeGroup, CustomString
				.getString(Activator.getDefault().RESOURCE_BUNDLE, "CreateVariableDialog.lbl_valueRank.text"));
		// Combo * Value Rank
		this.combo_valueRank = this.controllCreationToolkit.createComboValueRank(nodeGroup);
		this.arrayDimensionValidator = new ArrayDimFieldValidator<UnsignedInteger[]>(cache_arraydimension);
		// Label * Array Dimension
		this.controllCreationToolkit.createLabel(nodeGroup, CustomString.getString(
				Activator.getDefault().RESOURCE_BUNDLE, "NodeEditorVariableTypePart.lbl_arrayDimensions.text"));
		// Text * Array Dimension
		this.arrayDimensionQuickFix = new ArrayDimQuickFixProvider<UnsignedInteger[]>(ValueRanks.Any);
		this.txt_arrayDimension = this.controllCreationToolkit.createTextArrayDimensions(nodeGroup, this.valueRank,
				this.arrayDimensionValidator, this.arrayDimensionQuickFix);
		GridDataFactory.fillDefaults().span(2, 1).applyTo(this.txt_arrayDimension.getControl());
		// Label * Value
		this.controllCreationToolkit.createLabel(nodeGroup, CustomString
				.getString(Activator.getDefault().RESOURCE_BUNDLE, "NodeEditorVariableTypePart.lbl_value.text"));
		// Combo * ValueType
		this.combo_valueType = this.controllCreationToolkit.createComboValueType(nodeGroup);
		GridDataFactory.fillDefaults().applyTo(this.combo_valueType);
		// Text * Value
		this.txt_value_converter = new VariantStringConverter("", this.combo_valueType, this.combo_valueRank);
		this.txt_value = this.controllCreationToolkit.createTextValue(nodeGroup, this.txt_value_converter,
				this.combo_valueType, this.combo_valueRank, (Text) this.txt_arrayDimension.getControl(),
				this.cache_value);
		GridDataFactory.fillDefaults().applyTo(this.txt_value.getControl());
		// Label * Is Abstract
		this.controllCreationToolkit.createLabel(nodeGroup, CustomString
				.getString(Activator.getDefault().RESOURCE_BUNDLE, "NodeEditorDataTypePart.lbl_isAbstract.text"));
		// Combo * Is Abstract
		this.cb_isAbstract = new CheckBoxButton(nodeGroup, SWT.NONE); // this.controllCreationToolkit.createComboBoolean(nodeGroup);
		GridDataFactory.fillDefaults().span(2, 1).applyTo(this.cb_isAbstract);
		// Label * Reference
		this.controllCreationToolkit.createLabel(composite, CustomString
				.getString(Activator.getDefault().RESOURCE_BUNDLE, "NodeEditorPart.tblclmn_reference.text"));
		// Table * Reference
		this.tableViewer = this.controllCreationToolkit.createVariableTypeReferenceTable(composite, node,
				this.callBackEditor);
		createReferenceTableButtonSection(composite, node);
		createErrorLabel(composite);
		composite.layout();
		composite.setSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT, true));
		if (!isEditable(node)) {
			disableEditableSection(nodeGroup);
		}
	}

	protected void setNodeInput(Node node) {
		setBaseNodeInput(node);
		setReferenceTableNodeInput(node);
		setBaseVariableNodeInput(node);
		// fill datatype combo box
		Map<NodeId, String> dataTypes = this.controllCreationToolkit.fetchDataTypes();
		String dataTypeId = dataTypes.get(this.dataType);
		/** get all tree items */
		this.treeViewer.expandAll();
		TreeItem[] items = this.treeViewer.getTree().getItems();
		TreeItem it = null;
		for (TreeItem item : items) {
			it = this.findTreeItem(item, dataTypeId);
			if (it != null) {
				this.treeViewer.getTree().setSelection(it);
				break;
			}
		}
		this.treeViewer.addDoubleClickListener(new IDoubleClickListener() {
			@Override
			public void doubleClick(DoubleClickEvent event) {
				// change value type for correct value
				DataTypeNode node = (DataTypeNode) ((BrowserModelNode) ((TreeSelection) event.getSelection())
						.getFirstElement()).getNode();
				if (node == null) {
					// reset old selection because if we collapse and the expand
					// a node the selection is not show yet.
					if (dataTypeItem != null) {
						treeViewer.getTree().setSelection(dataTypeItem);
					}
					return;
				} else /**
						 * if(node.getIsAbstract()) { setError(StatusCode.BAD); /** TODO message Status
						 * status = (Status)getStatus(); status = new Status(IStatus.ERROR,
						 * status.getPlugin(), "Can not create variabletype from abstract datatype!" );
						 * updateStatus(status);
						 * 
						 * } else
						 */
				{
					/**
					 * TODO message Status status = (Status)getStatus(); status = new
					 * Status(IStatus.OK, status.getPlugin(), ""); updateStatus(status);
					 */
					dataType = node.getNodeId();
					dataTypeItem = treeViewer.getTree().getSelection()[0];
					callBackEditor.setDirty(true);
				}
			}
		});
		this.cb_isAbstract.setChecked(((VariableTypeNode) node).getIsAbstract());
		this.cb_isAbstract.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				callBackEditor.setDirty(true);
			}
		});
	}

	@Override
	public void doSave(Node node) {
		// save the default node attributes
		doSaveNodeBaseAttributes(node);
		// cast the node
		UAVariableTypeNode varTypeNode = (UAVariableTypeNode) node;
		// construct the new changed values
		UnsignedInteger[] arrayDimensions = createArrayDimension();
		Variant value = createValue();
		Integer valueRank = createValueRank();
		varTypeNode.setDataType(this.dataType);
		varTypeNode.setIsAbstract(this.cb_isAbstract.isChecked());
		varTypeNode.setArrayDimensions(arrayDimensions);
		varTypeNode.setValue(value);
		varTypeNode.setValueRank(valueRank);
	}

	private TreeItem findTreeItem(TreeItem root, String key) {
		if (root.getText().compareTo(key) == 0) {
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

	protected void setBaseVariableNodeInput(Node node) {
		// NodeId dataTypeId = null;
		UnsignedInteger[] arrayDimensions = null;
		// Integer valueRank = null;
		Variant val = null;
		/** init values */
		this.scalar_or_onedimension_value = new DataValue();
		this.any_value = new DataValue();
		this.scalar_value = new DataValue();
		this.one_or_more_dimension_value = new DataValue();
		this.onedimension_value = new DataValue();
		this.twodimension_value = new DataValue();
		this.threedimension_value = new DataValue();
		this.fourdimension_value = new DataValue();
		this.fivedimension_value = new DataValue();
		if (node instanceof VariableNode) {
			arrayDimensions = ((VariableNode) node).getArrayDimensions();
			this.dataType = ((VariableNode) node).getDataType();
			this.valueRank = ValueRanks.getValueRanks(((VariableNode) node).getValueRank());
			val = ((VariableNode) node).getValue();
		} else if (node instanceof VariableTypeNode) {
			this.dataType = ((VariableTypeNode) node).getDataType();
			arrayDimensions = ((VariableTypeNode) node).getArrayDimensions();
			this.valueRank = ValueRanks.getValueRanks(((VariableTypeNode) node).getValueRank());
			val = ((VariableTypeNode) node).getValue();
		}
		this.combo_valueRank.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				valueRank = ValueRanks.valueOf(combo_valueRank.getText());
				// TODO Auto-generated method stub
				arrayDimensionValidator.setValueRank(valueRank);
				txt_arrayDimension.validate();
				/** set quick fix */
				arrayDimensionQuickFix.setValueRank(valueRank);
				((Text) txt_value.getControl()).setText("");
				if (valueRank != ValueRanks.Scalar) {
					((Text) txt_value.getControl()).setText(
							CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "form.doubleclick"));
				}
				switch (ValueRanks.valueOf(combo_valueRank.getText())) {
				case ScalarOrOneDimension:
					value = scalar_or_onedimension_value;
					break;
				case Any:
					value = any_value;
					break;
				case Scalar:
					value = scalar_value;
					break;
				case OneOrMoreDimensions:
					value = one_or_more_dimension_value;
					break;
				case OneDimension:
					value = onedimension_value;
					break;
				case TwoDimensions:
					value = twodimension_value;
					break;
				case ThreeDimensions:
					value = threedimension_value;
					break;
				case FourDimensions:
					value = fourdimension_value;
					break;
				case FiveDimensions:
					value = fivedimension_value;
					break;
				default:
					break;
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
			}
		});
		// fill array dimension
		this.txt_arrayDimension.setContents(arrayDimensions);
		((Text) this.txt_arrayDimension.getControl()).addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				callBackEditor.setDirty(true);
			}
		});
		// fill value rank
		int index = 0;
		for (int i = 0; i < ValueRanks.values().length; i++) {
			if (ValueRanks.values()[i].equals(valueRank)) {
				index = i;
				break;
			}
		}
		// set the correct value rank values
		switch (valueRank) {
		case ScalarOrOneDimension:
			scalar_or_onedimension_value.setValue(val);
			value = scalar_or_onedimension_value;
			break;
		case Any:
			any_value.setValue(val);
			value = any_value;
			break;
		case Scalar:
			scalar_value.setValue(val);
			value = scalar_value;
			break;
		case OneOrMoreDimensions:
			one_or_more_dimension_value.setValue(val);
			value = one_or_more_dimension_value;
			break;
		case OneDimension:
			onedimension_value.setValue(val);
			value = onedimension_value;
			break;
		case TwoDimensions:
			twodimension_value.setValue(val);
			value = twodimension_value;
			break;
		case ThreeDimensions:
			threedimension_value.setValue(val);
			value = threedimension_value;
			break;
		case FourDimensions:
			fourdimension_value.setValue(val);
			value = fourdimension_value;
			break;
		case FiveDimensions:
			fivedimension_value.setValue(val);
			value = fivedimension_value;
			break;
		default:
			break;
		}
		this.value.setValue(val);
		this.combo_valueRank.select(index);
		this.arrayDimensionValidator.setValueRank(valueRank);
		this.txt_arrayDimension.validate();
		this.arrayDimensionQuickFix.setValueRank(valueRank);
		this.combo_valueRank.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				callBackEditor.setDirty(true);
			}
		});
		try {
			this.valueType = TypeInfo
					.construct(this.value, ServerInstance.getInstance().getServerInstance().getTypeTable())
					.getBuiltInsType();
			index = 0;
			for (int i = 0; i < this.combo_valueType.getItems().length; i++) {
				if (this.combo_valueType.getItem(i).equals(this.valueType.name())) {
					index = i;
					break;
				}
			}
		} catch (NullPointerException npe) {
			index = 0;
		}
		this.combo_valueType.select(index);
		this.combo_valueType.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				callBackEditor.setDirty(true);
			}
		});
		this.combo_valueType.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				valueType = BuiltinType.valueOf(((Combo) e.getSource()).getText());
				// check the value type
				TypeInfo typeInfo = TypeInfo.construct(value,
						ServerInstance.getInstance().getServerInstance().getTypeTable());
				if (value.getValue().getValue() == null || !(typeInfo.getBuiltInsType().equals(valueType))) {
					Object val = createDefaultValue(valueType, valueRank);
					value.setValue(new Variant(val));
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
			}
		});
		if (!this.combo_valueType.getItem(this.combo_valueType.getSelectionIndex()).equals(BuiltinType.Null.name())) {
			if (val.isArray()) {
				((Text) this.txt_value.getControl())
						.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "form.doubleclick"));
			} else {
				this.txt_value.setContents(val);
			}
		}
		((Text) this.txt_value.getControl()).addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				callBackEditor.setDirty(true);
			}
		});
		((Text) this.txt_value.getControl()).addMouseListener(new MouseListener() {
			@Override
			public void mouseUp(MouseEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void mouseDown(MouseEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
				// here we are
				// first we need to check if array Dimension is correct
				if (!txt_arrayDimension.isValid()) {
					return;
				}
				boolean openSingle = false;
				switch (valueType) {
				case Null:
					return;
				case DateTime:
					openSingle = true;
					break;
				default:
					openSingle = false;
					break;
				}
				// now we check if we have an scalar
				if (!openSingle && valueRank == ValueRanks.ScalarOrOneDimension
						&& ((Text) txt_value.getControl()).getText().compareTo(CustomString
								.getString(Activator.getDefault().RESOURCE_BUNDLE, "form.doubleclick")) != 0) {
					return;
				}
				if (!openSingle && valueRank == ValueRanks.Any && ((Text) txt_value.getControl()).getText().compareTo(
						CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "form.doubleclick")) != 0) {
					return;
				}
				if (!openSingle && valueRank == ValueRanks.Scalar) {
					return;
				}
				boolean single = false;
				if (openSingle) {
					switch (valueType) {
					case DateTime:
						single = true;
						DateTimeDialog dialog = new DateTimeDialog(txt_value.getControl().getShell());
						int open = dialog.open();
						if (Dialog.OK == open) {
							Calendar dt = dialog.getDateTime();
							DateTime date = new DateTime(dt);
							Variant variant = new Variant(date);
							value.setValue(variant);
							txt_value_converter.setValue(variant);
							txt_value.setContents(variant);
							combo_valueType.setData(combo_valueType.getText(), variant);
						}
						break;
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
				}
				if (!single) {
					Text dimensions = (Text) txt_arrayDimension.getControl();
					ArrayDimStringConverter converter = new ArrayDimStringConverter();
					UnsignedInteger[] dims = converter.convertFromString(dimensions.getText());
					// check the value type
					TypeInfo typeInfo = TypeInfo.construct(value,
							ServerInstance.getInstance().getServerInstance().getTypeTable());
					if (value.getValue().getValue() == null || !(typeInfo.getBuiltInsType().equals(valueType))) {
						Object val = createDefaultValue(valueType, valueRank);
						value.setValue(new Variant(val));
					}
					NodeValueDialog dialog = new NodeValueDialog(txt_value.getControl().getShell(), valueRank, dims,
							valueType, value, dataType, node.getNodeId());
					int open = dialog.open();
					if (open == Dialog.OK) {
						callBackEditor.setDirty(true);
					}
				}
			}
		});
	}

	/**
	 * Creates the correct value object appending on the value rank and value text
	 * 
	 * @return
	 */
	protected Variant createValue() {
		if (valueRank == ValueRanks.Scalar
				|| (valueRank == ValueRanks.ScalarOrOneDimension && ((Text) txt_value.getControl()).getText().compareTo(
						CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "form.doubleclick")) != 0)
				|| (valueRank == ValueRanks.Any && ((Text) txt_value.getControl()).getText().compareTo(
						CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "form.doubleclick")) != 0)) {
			return this.txt_value.getContents();
		} else {
			return this.value.getValue();
		}
	}

	protected Integer createValueRank() {
		return this.valueRank.getValue();
	}

	protected UnsignedInteger[] createArrayDimension() {
		return this.txt_arrayDimension.getContents();
	}

	protected Object createDefaultValue(BuiltinType valueType, ValueRanks valueRank) {
		Object val = null;
		switch (valueType) {
		case Boolean: // Boolean
			// fist check if we had already an array in our value
			if (valueRank == ValueRanks.TwoDimensions) { // two dimensions
				val = new Boolean[1][1];
				((Boolean[][]) val)[0][0] = false;
			} else {
				val = new Boolean[1];
				((Boolean[]) val)[0] = false;
			}
			return val;
		case SByte: // SBYTE
			if (valueRank == ValueRanks.TwoDimensions) { // two dimensions
				val = new Byte[1][1];
				((Byte[][]) val)[0][0] = (byte) 0;
			} else {
				val = new Byte[1];
				((Byte[]) val)[0] = (byte) 0;
			}
			return val;
		case Byte: // SBYTE
			if (valueRank == ValueRanks.TwoDimensions) { // two dimensions
				val = new UnsignedByte[1][1];
				((UnsignedByte[][]) val)[0][0] = new UnsignedByte(0);
			} else {
				val = new UnsignedByte[1];
				((UnsignedByte[]) val)[0] = new UnsignedByte(0);
			}
			return val;
		case Int16: // INT16
			if (valueRank == ValueRanks.TwoDimensions) { // two dimensions
				val = new Short[1][1];
				((Short[][]) val)[0][0] = (short) 0;
			} else {
				val = new Short[1];
				((Short[]) val)[0] = (short) 0;
			}
			return val;
		case UInt16: // UINT16
			if (valueRank == ValueRanks.TwoDimensions) { // two dimensions
				val = new UnsignedShort[1][1];
				((UnsignedShort[][]) val)[0][0] = new UnsignedShort(0);
			} else {
				val = new UnsignedShort[1];
				((UnsignedShort[]) val)[0] = new UnsignedShort(0);
			}
			return val;
		case Int32: // INT32
			if (valueRank == ValueRanks.TwoDimensions) { // two dimensions
				val = new Integer[1][1];
				((Integer[][]) val)[0][0] = 0;
			} else {
				val = new Integer[1];
				((Integer[]) val)[0] = 0;
			}
			return val;
		case UInt32: // UINT32
			if (valueRank == ValueRanks.TwoDimensions) { // two dimensions
				val = new UnsignedInteger[1][1];
				((UnsignedInteger[][]) val)[0][0] = new UnsignedInteger(0);
			} else {
				val = new UnsignedInteger[1];
				((UnsignedInteger[]) val)[0] = new UnsignedInteger(0);
			}
			return val;
		case Int64: // INT64
			if (valueRank == ValueRanks.TwoDimensions) { // two dimensions
				val = new Long[1][1];
				((Long[][]) val)[0][0] = (long) 0;
			} else {
				val = new Long[1];
				((Long[]) val)[0] = (long) 0;
			}
			return val;
		case UInt64: // UINT64
			if (valueRank == ValueRanks.TwoDimensions) { // two dimensions
				val = new UnsignedLong[1][1];
				((UnsignedLong[][]) val)[0][0] = new UnsignedLong(0);
			} else {
				val = new UnsignedLong[1];
				((UnsignedLong[]) val)[0] = new UnsignedLong(0);
			}
			return val;
		case Float: // Float
			if (valueRank == ValueRanks.TwoDimensions) { // two dimensions
				val = new Float[1][1];
				((Float[][]) val)[0][0] = (float) 0.0;
			} else {
				val = new Float[1];
				((Float[]) val)[0] = (float) 0.0;
			}
			return val;
		case Double: // Double
			if (valueRank == ValueRanks.TwoDimensions) { // two dimensions
				val = new Double[1][1];
				((Double[][]) val)[0][0] = 0.0;
			} else {
				val = new Double[1];
				((Double[]) val)[0] = 0.0;
			}
			return val;
		case String: // String
			if (valueRank == ValueRanks.TwoDimensions) { // two dimensions
				val = new String[1][1];
				((String[][]) val)[0][0] = "";
			} else {
				val = new String[1];
				((String[]) val)[0] = "";
			}
			return val;
		case DateTime: // DateTime
			if (valueRank == ValueRanks.TwoDimensions) { // two dimensions
				val = new DateTime[1][1];
				((DateTime[][]) val)[0][0] = DateTime.currentTime();
			} else {
				val = new DateTime[1];
				((DateTime[]) val)[0] = DateTime.currentTime();
			}
			return val;
		case Guid: // Guid
			if (valueRank == ValueRanks.TwoDimensions) { // two dimensions
				val = new UUID[1][1];
				((UUID[][]) val)[0][0] = UUID.randomUUID();
			} else {
				val = new UUID[1];
				((UUID[]) val)[0] = UUID.randomUUID();
			}
			return val;
		case ByteString: // ByteString
			if (valueRank == ValueRanks.TwoDimensions) { // two dimensions
				val = new Byte[1][1];
				((Byte[][]) val)[0][0] = (byte) 0;
			} else {
				val = new Byte[1];
				((Byte[]) val)[0] = (byte) 0;
			}
			return val;
		case XmlElement: // XMLElement
			if (valueRank == ValueRanks.TwoDimensions) { // two dimensions
				val = new XmlElement[1][1];
				((XmlElement[][]) val)[0][0] = new XmlElement("");
			} else {
				val = new XmlElement[1];
				((XmlElement[]) val)[0] = new XmlElement("");
			}
			return val;
		case NodeId: // NodeId
			if (valueRank == ValueRanks.TwoDimensions) { // two dimensions
				val = new NodeId[1][1];
				((NodeId[][]) val)[0][0] = new NodeId(0, 0);
			} else {
				val = new NodeId[1];
				((NodeId[]) val)[0] = new NodeId(0, 0);
			}
			return val;
		case ExpandedNodeId: // ExpandedNodeId
			if (valueRank == ValueRanks.TwoDimensions) { // two dimensions
				val = new ExpandedNodeId[1][1];
				((ExpandedNodeId[][]) val)[0][0] = new ExpandedNodeId(new UnsignedInteger(), 0, 0);
			} else {
				val = new ExpandedNodeId[1];
				((ExpandedNodeId[]) val)[0] = new ExpandedNodeId(new UnsignedInteger(), 0, 0);
			}
			return val;
		case StatusCode: // StatusCode
			if (valueRank == ValueRanks.TwoDimensions) { // two dimensions
				val = new StatusCode[1][1];
				((StatusCode[][]) val)[0][0] = StatusCode.GOOD;
			} else {
				val = new StatusCode[1];
				((StatusCode[]) val)[0] = StatusCode.GOOD;
			}
			return val;
		case QualifiedName: // QualifiedName
			if (valueRank == ValueRanks.TwoDimensions) { // two dimensions
				val = new QualifiedName[1][1];
				((QualifiedName[][]) val)[0][0] = new QualifiedName("");
			} else {
				val = new QualifiedName[1];
				((QualifiedName[]) val)[0] = new QualifiedName("");
			}
			return val;
		case LocalizedText: // LocalizedText
			if (valueRank == ValueRanks.TwoDimensions) { // two dimensions
				val = new LocalizedText[1][1];
				((LocalizedText[][]) val)[0][0] = new LocalizedText("", "");
			} else {
				val = new LocalizedText[1];
				((LocalizedText[]) val)[0] = new LocalizedText("", "");
			}
			return val;
		case ExtensionObject: // ExtensionObject
			if (valueRank == ValueRanks.TwoDimensions) { // two dimensions
				val = new ExtensionObject[1][1];
				((ExtensionObject[][]) val)[0][0] = new ExtensionObject(ExpandedNodeId.NULL);
			} else {
				val = new ExtensionObject[1];
				((ExtensionObject[]) val)[0] = new ExtensionObject(ExpandedNodeId.NULL);
			}
			return val;
		case DataValue: // DataValue
			if (valueRank == ValueRanks.TwoDimensions) { // two dimensions
				val = new DataValue[1][1];
				((DataValue[][]) val)[0][0] = new DataValue();
			} else {
				val = new DataValue[1];
				((DataValue[]) val)[0] = new DataValue();
			}
			return val;
		case Variant: // Variant
			if (valueRank == ValueRanks.TwoDimensions) { // two dimensions
				val = new Variant[1][1];
				((Variant[][]) val)[0][0] = new Variant(null);
			} else {
				val = new Variant[1];
				((Variant[]) val)[0] = new Variant(null);
			}
			return val;
		case DiagnosticInfo: // DiagnosticInfo
			if (valueRank == ValueRanks.TwoDimensions) { // two dimensions
				val = new DiagnosticInfo[1][1];
				((DiagnosticInfo[][]) val)[0][0] = new DiagnosticInfo();
			} else {
				val = new DiagnosticInfo[1];
				((DiagnosticInfo[]) val)[0] = new DiagnosticInfo();
			}
			return val;
		case Number: // Number
			if (valueRank == ValueRanks.TwoDimensions) { // two dimensions
				val = new Number[1][1];
				((Number[][]) val)[0][0] = null;
			} else {
				val = new Number[1];
				((Number[]) val)[0] = null;
			}
			return val;
		case Integer: // Integer
			if (valueRank == ValueRanks.TwoDimensions) { // two dimensions
				val = new Integer[1][1];
				((Integer[][]) val)[0][0] = 0;
			} else {
				val = new Integer[1];
				((Integer[]) val)[0] = 0;
			}
			return val;
		case UInteger: // UInteger
			if (valueRank == ValueRanks.TwoDimensions) { // two dimensions
				val = new UnsignedInteger[1][1];
				((UnsignedInteger[][]) val)[0][0] = new UnsignedInteger(0);
			} else {
				val = new UnsignedInteger[1];
				((UnsignedInteger[]) val)[0] = new UnsignedInteger(0);
			}
			return val;
		case Enumeration: // Enumeration
			if (valueRank == ValueRanks.TwoDimensions) { // two dimensions
				val = new Enumeration[1][1];
				((Enumeration[][]) val)[0][0] = null;
			} else {
				val = new Enumeration[1];
				((Enumeration[]) val)[0] = null;
			}
			return val;
		}
		return val;
	}

	@Override
	public boolean valid() {
		if (this.txt_value != null) {
			if (!this.txt_value.isValid()) {
				return false;
			}
		}
		if (this.txt_arrayDimension != null) {
			if (!this.txt_arrayDimension.isValid()) {
				return false;
			}
		}
		// now check the base elements
		return super.valid();
	}
}
