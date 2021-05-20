package com.bichler.astudio.opcua.opcmodeler.editor.node;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.UUID;

import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
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
import org.opcfoundation.ua.common.NamespaceTable;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.AccessLevel;
import org.opcfoundation.ua.core.AttributeWriteMask;
import org.opcfoundation.ua.core.BrowseDescription;
import org.opcfoundation.ua.core.BrowseDirection;
import org.opcfoundation.ua.core.BrowseResult;
import org.opcfoundation.ua.core.BrowseResultMask;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.core.ReferenceDescription;
import org.opcfoundation.ua.core.ReferenceNode;

import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.core.user.util.UserUtils;
import com.bichler.astudio.opcua.modeller.controls.CometCombo;
import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.opcua.opcmodeler.dialogs.ArrayDimensionAttributeDialog;
import com.bichler.astudio.opcua.opcmodeler.dialogs.WriteMaskDialog_;
import com.bichler.astudio.opcua.opcmodeler.dialogs.utils.ReferenceTableLabelProvider;
import com.bichler.astudio.opcua.opcmodeler.dialogs.utils.TypesReferenceTableContentProvider;
import com.bichler.astudio.opcua.opcmodeler.editor.node.validation.converters.ArrayDimStringConverter;
import com.bichler.astudio.opcua.opcmodeler.editor.node.validation.converters.BooleanStringConverter;
import com.bichler.astudio.opcua.opcmodeler.editor.node.validation.converters.ByteStringConverter;
import com.bichler.astudio.opcua.opcmodeler.editor.node.validation.converters.DateTimeStringConverter;
import com.bichler.astudio.opcua.opcmodeler.editor.node.validation.converters.GuidStringConverter;
import com.bichler.astudio.opcua.opcmodeler.editor.node.validation.converters.Int16StringConverter;
import com.bichler.astudio.opcua.opcmodeler.editor.node.validation.converters.Int32StringConverter;
import com.bichler.astudio.opcua.opcmodeler.editor.node.validation.converters.Int64StringConverter;
import com.bichler.astudio.opcua.opcmodeler.editor.node.validation.converters.LocaleStringConverter;
import com.bichler.astudio.opcua.opcmodeler.editor.node.validation.converters.LocalizedTextStringConverter;
import com.bichler.astudio.opcua.opcmodeler.editor.node.validation.converters.SByteStringConverter;
import com.bichler.astudio.opcua.opcmodeler.editor.node.validation.converters.UInt16StringConverter;
import com.bichler.astudio.opcua.opcmodeler.editor.node.validation.converters.UInt32StringConverter;
import com.bichler.astudio.opcua.opcmodeler.editor.node.validation.converters.UInt64StringConverter;
import com.bichler.astudio.opcua.opcmodeler.editor.node.validation.converters.UIntStringConverter;
import com.bichler.astudio.opcua.opcmodeler.editor.node.validation.converters.VariantStringConverter;
import com.bichler.astudio.opcua.opcmodeler.editor.node.validation.errors.ArrayDimQuickFixProvider;
import com.bichler.astudio.opcua.opcmodeler.editor.node.validation.errors.NodeIdQuickFixProvider;
import com.bichler.astudio.opcua.opcmodeler.editor.node.validation.errors.NodeIdTextFieldQuickFixProvider;
import com.bichler.astudio.opcua.opcmodeler.editor.node.validation.fields.ArrayDimFieldValidator;
import com.bichler.astudio.opcua.opcmodeler.editor.node.validation.fields.BooleanFieldValidator;
import com.bichler.astudio.opcua.opcmodeler.editor.node.validation.fields.ByteFieldValidator;
import com.bichler.astudio.opcua.opcmodeler.editor.node.validation.fields.DateTimeFieldValidator;
import com.bichler.astudio.opcua.opcmodeler.editor.node.validation.fields.DoubleFieldValidator;
import com.bichler.astudio.opcua.opcmodeler.editor.node.validation.fields.FloatFieldValidator;
import com.bichler.astudio.opcua.opcmodeler.editor.node.validation.fields.GuidFieldValidator;
import com.bichler.astudio.opcua.opcmodeler.editor.node.validation.fields.Int16FieldValidator;
import com.bichler.astudio.opcua.opcmodeler.editor.node.validation.fields.Int32FieldValidator;
import com.bichler.astudio.opcua.opcmodeler.editor.node.validation.fields.Int64FieldValidator;
import com.bichler.astudio.opcua.opcmodeler.editor.node.validation.fields.LocaleFieldValidator;
import com.bichler.astudio.opcua.opcmodeler.editor.node.validation.fields.LocalizedTextFieldValidator;
import com.bichler.astudio.opcua.opcmodeler.editor.node.validation.fields.NodeIdFieldValidator;
import com.bichler.astudio.opcua.opcmodeler.editor.node.validation.fields.SByteFieldValidator;
import com.bichler.astudio.opcua.opcmodeler.editor.node.validation.fields.TextFieldValidator;
import com.bichler.astudio.opcua.opcmodeler.editor.node.validation.fields.UInt16FieldValidator;
import com.bichler.astudio.opcua.opcmodeler.editor.node.validation.fields.UInt32FieldValidator;
import com.bichler.astudio.opcua.opcmodeler.editor.node.validation.fields.UInt64FieldValidator;
import com.bichler.astudio.opcua.opcmodeler.editor.node.validation.fields.VariantFieldValidator;
import com.bichler.astudio.opcua.opcmodeler.utils.extern.DesignerUtils;
import com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.ModelContentProvider;
import com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.ModelLabelProvider;
import com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.OPCUABrowserModelViewerComparator;
import com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.nodes.BrowserModelNode;
import com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.nodes.BrowserObjectTypeModelNode;
import com.bichler.astudio.opcua.opcmodeler.wizards.opc.reference.AddReferenceWizard;
import com.bichler.astudio.opcua.opcmodeler.wizards.opc.reference.DeleteReferenceWizard;
import com.bichler.astudio.opcua.opcmodeler.wizards.opc.reference.EditReferenceWizard;
import com.bichler.astudio.utils.internationalization.CustomString;
import com.bichler.astudio.utils.ui.swt.CheckBoxButton;
import com.richclientgui.toolbox.validation.ValidatingField;
import com.richclientgui.toolbox.validation.ValidationToolkit;
import com.richclientgui.toolbox.validation.converter.DoubleStringConverter;
import com.richclientgui.toolbox.validation.converter.FloatStringConverter;
import com.richclientgui.toolbox.validation.string.StringValidationToolkit;
import com.richclientgui.toolbox.validation.validator.IFieldValidator;

import opc.sdk.core.enums.BuiltinType;
import opc.sdk.core.enums.EventNotifiers;
import opc.sdk.core.enums.ValueRanks;
import opc.sdk.core.node.NamingRuleType;
import opc.sdk.core.node.Node;
import opc.sdk.core.node.ReferenceTypeNode;
import opc.sdk.core.node.UAVariableNode;
import opc.sdk.core.node.VariableTypeNode;

public class DesignerFormToolkit {
	private final int DECORATOR_POSITION = SWT.TOP | SWT.RIGHT;
	private final int DECORATOR_MARGIN_WIDTH = 1;
	// private final int DEFAULT_WIDTH_HINT = 150;
	protected final GridData gridData1 = new GridData(150, SWT.DEFAULT);
	protected final GridData gridData2 = new GridData(300, SWT.DEFAULT);
	protected final GridData gridData3 = new GridData(105, SWT.DEFAULT);
	private StringValidationToolkit strValToolkit = null;
	private ValidationToolkit<Locale> locValToolkit = null;
	private ValidationToolkit<UnsignedInteger[]> arrayDimValToolkit = null;
	private ValidationToolkit<String> nodeIdValToolkit = null;
	private ValidationToolkit<Boolean> booleanValToolkit = null;
	private ValidationToolkit<Byte> sbyteValToolkit = null;
	private ValidationToolkit<UnsignedByte> byteValToolkit = null;
	private ValidationToolkit<Short> int16ValToolkit = null;
	private ValidationToolkit<UnsignedShort> uint16ValToolkit = null;
	private ValidationToolkit<Integer> int32ValToolkit = null;
	private ValidationToolkit<UnsignedInteger> uint32ValToolkit = null;
	private ValidationToolkit<Long> int64ValToolkit = null;
	private ValidationToolkit<UnsignedLong> uint64ValToolkit = null;
	private ValidationToolkit<Float> floatValToolkit = null;
	private ValidationToolkit<Double> doubleValToolkit = null;
	private ValidationToolkit<DateTime> datetimeValToolkit = null;
	private ValidationToolkit<UUID> guidValToolkit = null;
	private ValidationToolkit<Integer> intValToolkit = null;
	private ValidationToolkit<UnsignedInteger> uIntValToolkit = null;
	private ValidationToolkit<LocalizedText> localizedTextValToolkit = null;
	private ValidationToolkit<Variant> variantValToolkit = null;
	private ILog log = null;// Activator.getDefault().getLog();

	public DesignerFormToolkit() {
		this.strValToolkit = new StringValidationToolkit(DECORATOR_POSITION, DECORATOR_MARGIN_WIDTH, true);
		this.locValToolkit = new ValidationToolkit<Locale>(new LocaleStringConverter(), DECORATOR_POSITION,
				DECORATOR_MARGIN_WIDTH, true);
		this.arrayDimValToolkit = new ValidationToolkit<UnsignedInteger[]>(new ArrayDimStringConverter(),
				DECORATOR_POSITION, DECORATOR_MARGIN_WIDTH, true);
		this.uIntValToolkit = new ValidationToolkit<UnsignedInteger>(new UIntStringConverter(), DECORATOR_POSITION,
				DECORATOR_MARGIN_WIDTH, true);
		this.nodeIdValToolkit = new StringValidationToolkit(DECORATOR_POSITION, DECORATOR_MARGIN_WIDTH, true);
		this.booleanValToolkit = new ValidationToolkit<Boolean>(new BooleanStringConverter(), DECORATOR_POSITION,
				DECORATOR_MARGIN_WIDTH, true);
		this.sbyteValToolkit = new ValidationToolkit<Byte>(new SByteStringConverter(), DECORATOR_POSITION,
				DECORATOR_MARGIN_WIDTH, true);
		this.byteValToolkit = new ValidationToolkit<UnsignedByte>(new ByteStringConverter(), DECORATOR_POSITION,
				DECORATOR_MARGIN_WIDTH, true);
		this.int16ValToolkit = new ValidationToolkit<Short>(new Int16StringConverter(), DECORATOR_POSITION,
				DECORATOR_MARGIN_WIDTH, true);
		this.uint16ValToolkit = new ValidationToolkit<UnsignedShort>(new UInt16StringConverter(), DECORATOR_POSITION,
				DECORATOR_MARGIN_WIDTH, true);
		this.int32ValToolkit = new ValidationToolkit<Integer>(new Int32StringConverter(), DECORATOR_POSITION,
				DECORATOR_MARGIN_WIDTH, true);
		this.uint32ValToolkit = new ValidationToolkit<UnsignedInteger>(new UInt32StringConverter(), DECORATOR_POSITION,
				DECORATOR_MARGIN_WIDTH, true);
		this.int64ValToolkit = new ValidationToolkit<Long>(new Int64StringConverter(), DECORATOR_POSITION,
				DECORATOR_MARGIN_WIDTH, true);
		this.uint64ValToolkit = new ValidationToolkit<UnsignedLong>(new UInt64StringConverter(), DECORATOR_POSITION,
				DECORATOR_MARGIN_WIDTH, true);
		this.floatValToolkit = new ValidationToolkit<Float>(new FloatStringConverter(), DECORATOR_POSITION,
				DECORATOR_MARGIN_WIDTH, true);
		this.doubleValToolkit = new ValidationToolkit<Double>(new DoubleStringConverter(), DECORATOR_POSITION,
				DECORATOR_MARGIN_WIDTH, true);
		this.datetimeValToolkit = new ValidationToolkit<DateTime>(new DateTimeStringConverter(), DECORATOR_POSITION,
				DECORATOR_MARGIN_WIDTH, true);
		this.guidValToolkit = new ValidationToolkit<UUID>(new GuidStringConverter(), DECORATOR_POSITION,
				DECORATOR_MARGIN_WIDTH, true);
		this.localizedTextValToolkit = new ValidationToolkit<LocalizedText>(new LocalizedTextStringConverter(),
				DECORATOR_POSITION, DECORATOR_MARGIN_WIDTH, true);
		// this.log = Activator.getDefault().getLog();
		// CometLogger.getLogger().setDebug(CometLogger.OUTPUT_STREAM);
		// CometLogger.getLogger().setLogLevel(
		// ICometLogger.LOG1 | ICometLogger.LOG2 | ICometLogger.LOG3);
		// CometLogger.getLogger().startLogging();
	}

	public Button createButtonPush(Composite parent, String display) {
		Button button = new Button(parent, SWT.PUSH);
		if (display != null && !display.isEmpty())
			button.setText(display);
		return button;
	}

	public CheckBoxButton[] createCheckSupportEvents(Composite parent) {
		Group group_evtNotifiers = new Group(parent, SWT.SHADOW_ETCHED_IN);
		group_evtNotifiers.setLayout(new GridLayout(3, true));
		CheckBoxButton[] cb_supportEvents = new CheckBoxButton[3];
		int i = 0;
		for (EventNotifiers evtNotifiers : EventNotifiers.values()) {
			if (!evtNotifiers.equals(EventNotifiers.None)) {
				cb_supportEvents[i] = new CheckBoxButton(group_evtNotifiers, SWT.CHECK);
				cb_supportEvents[i].setText(evtNotifiers.name());
				GridDataFactory.fillDefaults().applyTo(cb_supportEvents[i]);
				i++;
			}
		}
		GridDataFactory.fillDefaults().span(2, 1).applyTo(group_evtNotifiers);
		return cb_supportEvents;
	}

	/**
	 * Creates a combobox with boolean items
	 * 
	 * @param parent
	 * @return
	 */
	public CometCombo createComboBoolean(Composite parent) {
		CometCombo combo = createComboBox(parent);
		combo.add(Boolean.TRUE.toString());
		combo.add(Boolean.FALSE.toString());
		combo.select(0);
		return combo;
	}

	public CometCombo createComboDataType(Composite parent) {
		CometCombo dataTypeSelection = new CometCombo(parent, SWT.DROP_DOWN);
		Map<NodeId, String> dataTypes = fetchDataTypes();
		for (String value : dataTypes.values()) {
			dataTypeSelection.add(value);
		}
		dataTypeSelection.select(0);
		dataTypeSelection.setData(dataTypes);
		return dataTypeSelection;
	}

	public CometCombo createComboBrowseName(Composite parent) {
		CometCombo combo = createComboBox(parent);
		return combo;
	}

	public CometCombo createComboNodeId(Composite parent, int parentIndex) {
		CometCombo combo = createNamespaceCombobox(parent, parentIndex);
		return combo;
	}

	public CometCombo createCometCombo(Composite parent) {
		CometCombo combo = createComboBox(parent);
		return combo;
	}

	public CometCombo createComboValueType(Composite parent) {
		CometCombo combo = new CometCombo(parent, SWT.DROP_DOWN);
		for (BuiltinType type : BuiltinType.values()) {
			combo.add(type.name());
		}
		combo.select(0);
		return combo;
	}

	public CometCombo createComboValueRank(Composite parent) {
		CometCombo combo = new CometCombo(parent, SWT.DROP_DOWN);
		GridDataFactory.fillDefaults().span(2, 1).applyTo(combo);
		for (ValueRanks v : ValueRanks.values()) {
			combo.add(v.toString());
			combo.setData(v.toString(), v);
		}
		combo.select(2);
		return combo;
	}

	public CometCombo createComboValueRank(Composite parent, Node selectedParent) {
		CometCombo combo = this.createComboValueRank(parent);
		int vr = ((VariableTypeNode) selectedParent).getValueRank();
		switch (vr) {
		case -3:
			combo.select(0);
			break;
		case -2:
			combo.select(1);
			break;
		case -1:
			combo.select(2);
			break;
		case -0:
			combo.select(3);
			break;
		case 1:
			combo.select(4);
			break;
		case 2:
			combo.select(5);
			break;
		default:
			combo.select(2);
			break;
		}
		return combo;
	}

	public CheckBoxButton[] createGroupAccessLevel(Composite parent) {
		Group group_accessLevel = new Group(parent, SWT.NONE);
		GridDataFactory.fillDefaults().span(2, 1).applyTo(group_accessLevel);
		group_accessLevel.setLayout(new GridLayout(4, true));
		
		List<CheckBoxButton> accessLevel = new ArrayList<>();
		boolean adminControl = UserUtils.testUserRights(1);
		
		for (AccessLevel accessLvl : AccessLevel.values()) {
			if(!adminControl && accessLvl.getValue() > 2) {
				continue;
			}
			
			CheckBoxButton checkBoxButton = new CheckBoxButton(group_accessLevel, SWT.CHECK);
			checkBoxButton.setText(accessLvl.name());
			GridDataFactory.fillDefaults().applyTo(checkBoxButton);
			accessLevel.add(checkBoxButton);
		}
		accessLevel.get(0).setChecked(true);
		accessLevel.get(1).setChecked(true);
		return accessLevel.toArray(new CheckBoxButton[0]);
	}

	public TableViewer createDataTypeReferenceTable(final Composite parent, Node input, NodeEditorPart callBackEditor) {
		TableViewer tableViewer = createReferenceTableSection(parent, callBackEditor);
		tableViewer.setContentProvider(
				new TypesReferenceTableContentProvider(Identifiers.BaseDataType, input.getNodeClass()));
		tableViewer.setLabelProvider(new ReferenceTableLabelProvider());
		tableViewer.setInput(input);
		return tableViewer;
	}

	public TableViewer createObjectTypeReferenceTable(final Composite parent, Node input,
			NodeEditorPart callBackEditor) {
		TableViewer tableViewer = createReferenceTableSection(parent, callBackEditor);
		tableViewer.setContentProvider(
				new TypesReferenceTableContentProvider(Identifiers.BaseObjectType, NodeClass.ObjectType));
		tableViewer.setLabelProvider(new ReferenceTableLabelProvider());
		tableViewer.setInput(input);
		return tableViewer;
	}

	public TableViewer createObjectReferenceTable(Composite parent, Node input, NodeEditorPart callBackEditor) {
		TableViewer tableViewer = createReferenceTableSection(parent, callBackEditor);
		tableViewer.setContentProvider(
				new TypesReferenceTableContentProvider(Identifiers.BaseObjectType, NodeClass.Object));
		tableViewer.setLabelProvider(new ReferenceTableLabelProvider());
		// tableViewer.setComparator(new ViewerComparator(){
		//
		// @Override
		// public int compare(Viewer viewer, Object e1, Object e2) {
		// // TODO Auto-generated method stub
		// return super.compare(viewer, e1, e2);
		// }
		//
		// });
		if (input != null) {
			tableViewer.setInput(input);
		}
		return tableViewer;
	}

	public TableViewer createVariableReferenceTable(Composite parent, Node input, NodeEditorPart callBackEditor) {
		TableViewer tableViewer = createReferenceTableSection(parent, callBackEditor);
		tableViewer.setContentProvider(
				new TypesReferenceTableContentProvider(Identifiers.BaseVariableType, NodeClass.Variable));
		tableViewer.setLabelProvider(new ReferenceTableLabelProvider());
		tableViewer.setInput(input);
		return tableViewer;
	}

	public TableViewer createVariableTypeReferenceTable(Composite parent, Node input, NodeEditorPart callBackEditor) {
		TableViewer tableViewer = createReferenceTableSection(parent, callBackEditor);
		tableViewer.setContentProvider(
				new TypesReferenceTableContentProvider(Identifiers.BaseVariableType, NodeClass.VariableType));
		tableViewer.setLabelProvider(new ReferenceTableLabelProvider());
		tableViewer.setInput(input);
		return tableViewer;
	}

	public TableViewer createReferenceTypeReferenceTable(Composite parent, Node input, NodeEditorPart callBackEditor) {
		TableViewer tableViewer = createReferenceTableSection(parent, callBackEditor);
		tableViewer.setContentProvider(
				new TypesReferenceTableContentProvider(Identifiers.References, input.getNodeClass()));
		tableViewer.setLabelProvider(new ReferenceTableLabelProvider());
		tableViewer.setInput(input);
		return tableViewer;
	}

	public TreeViewer createObjectTypeTreeViewer(Composite parent) {
		TreeViewer treeViewer = createTreeViewer(parent);
		treeViewer.setContentProvider(new ModelContentProvider(NodeClass.getMask(NodeClass.ObjectType)));
		treeViewer.setLabelProvider(new ModelLabelProvider());
		BrowserModelNode node = new BrowserObjectTypeModelNode(null);
		node.setNode(ServerInstance.getNode(Identifiers.ObjectTypesFolder));
		treeViewer.setComparator(new OPCUABrowserModelViewerComparator());
		treeViewer.setInput(node);
		return treeViewer;
	}

	public TreeViewer createVariableTypeTreeViewer(Composite parent) {
		TreeViewer treeViewer = createTreeViewer(parent);
		Tree tree = treeViewer.getTree();
		GridData gd = (GridData) tree.getLayoutData();
		gd.grabExcessVerticalSpace = true;
		tree.setLayoutData(gd);
		treeViewer.setContentProvider(new ModelContentProvider(NodeClass.getMask(NodeClass.VariableType)));
		treeViewer.setLabelProvider(new ModelLabelProvider());
		BrowserModelNode node = new BrowserModelNode(null);
		node.setNode(ServerInstance.getNode(Identifiers.VariableTypesFolder));
		treeViewer.setComparator(new OPCUABrowserModelViewerComparator());
		treeViewer.setInput(node);
		return treeViewer;
	}

	public TreeViewer createDataTypeTreeViewerFromParentType(Composite parent, Node superType) {
		NodeId id = ((VariableTypeNode) superType).getDataType();
		TreeViewer treeViewer = createTreeViewer(parent);
		treeViewer.setContentProvider(new ModelContentProvider(NodeClass.getMask(NodeClass.DataType), id));
		treeViewer.setLabelProvider(new ModelLabelProvider());
		BrowserModelNode node = new BrowserModelNode(null);
		node.setNode(ServerInstance.getNode(Identifiers.DataTypesFolder));
		treeViewer.setComparator(new OPCUABrowserModelViewerComparator());
		treeViewer.setInput(node);
		return treeViewer;
	}

	public TreeViewer createDataTypeTreeViewer(Composite parent) {
		TreeViewer treeViewer = createTreeViewer(parent);
		treeViewer.setContentProvider(new ModelContentProvider(NodeClass.getMask(NodeClass.DataType)));
		treeViewer.setLabelProvider(new ModelLabelProvider());
		BrowserModelNode node = new BrowserModelNode(null);
		node.setNode(ServerInstance.getNode(Identifiers.DataTypesFolder));
		treeViewer.setComparator(new OPCUABrowserModelViewerComparator());
		treeViewer.setInput(node);
		return treeViewer;
	}

	public Map<NodeId, String> fetchDataTypes() {
		// AddressSpace addressSpace = ServerInstance.getInstance()
		// .getServerInstance().getAddressSpace();
		Map<NodeId, String> dataTypes = DesignerUtils.fetchAllDatatypes();
		return dataTypes;
	}

	public Map<NodeId, String> fetchDataTypes(NodeId nodeid) {
		Map<NodeId, String> dataTypes = DesignerUtils.fetchAllDatatypes(nodeid);
		return dataTypes;
	}

	public Map<String, NodeId> fetchObjectTypes() {
		Map<String, NodeId> opjectTypes = DesignerUtils.fetchAllObjectTypes();
		return opjectTypes;
	}

	/**
	 * Creates a skeleton of a tree viewer
	 * 
	 * @param parent
	 * @return
	 */
	private TreeViewer createTreeViewer(Composite parent) {
		TreeViewer treeViewer = new TreeViewer(parent, SWT.BORDER);
		GridDataFactory.fillDefaults().span(2, 1).hint(593, 150).applyTo(treeViewer.getControl());
		return treeViewer;
	}

	/**
	 * Refreshes the display of the new nodeId text field with the last available
	 * numeric nodeId
	 */
	/*
	 * private void refreshNewNodeId(Combo browsename, Text nodeId) { int index =
	 * ServerInstance.getInstance().getServerInstance()
	 * .getNamespaceUris().getIndex(browsename.getText()); NodeId n =
	 * ServerInstance.getInstance().getServerInstance()
	 * .getAddressSpace().getNodeFactory().showNextNodeId(index);
	 * nodeId.setText(n.toString()); }
	 */
	private TableViewer createReferenceTableSection(final Composite parent, final NodeEditorPart callBackEditor) {
		Composite tableComposite = new Composite(parent, SWT.NONE);
		TableColumnLayout tableColumnLayout = new TableColumnLayout();
		tableComposite.setLayout(tableColumnLayout);
		final TableViewer tableViewer = new TableViewer(tableComposite,
				SWT.BORDER | SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL);
		String[] columnProperties = { "Reference", "IsInverse", "Target" };
		tableViewer.setColumnProperties(columnProperties);
		for (int i = 0; i < columnProperties.length; i++) {
			TableViewerColumn column = new TableViewerColumn(tableViewer, SWT.CENTER);
			column.getColumn().setText(columnProperties[i]);
			column.getColumn().setResizable(true);
			column.getColumn().setMoveable(true);
			tableColumnLayout.setColumnData(column.getColumn(), new ColumnWeightData(33));
		}
		Table table = tableViewer.getTable();
		table.setLayoutData(new GridData(GridData.FILL_BOTH));
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		// layout data for the table composite
		GridDataFactory.fillDefaults().span(2, 3).hint(400, 100).applyTo(tableComposite);
		return tableViewer;
	}

	/**
	 * Creates a default combobox with a default grid data
	 * 
	 * @param parent
	 * @return
	 */
	private CometCombo createComboBox(Composite parent) {
		CometCombo combo = new CometCombo(parent, SWT.DROP_DOWN);
		GridDataFactory.fillDefaults().applyTo(combo);
		return combo;
	}

	/**
	 * Creates a non - validated namespace combobox
	 * 
	 * @param parent
	 * @return
	 */
	private CometCombo createNamespaceCombobox(Composite parent, int parentIndex) {
		CometCombo combo = createComboBox(parent);
		String[] nsArray = ServerInstance.getInstance().getServerInstance().getNamespaceUris().toArray();
		for (int i = 0; i < nsArray.length; i++) {
			combo.add(nsArray[i]);
			combo.setData(nsArray[i], i);
		}
		if (parentIndex < nsArray.length && parentIndex > 0)
			combo.select(parentIndex);
		else if (nsArray.length > 1)
			combo.select(1);
		return combo;
	}

	/**
	 * Creates a combobox filed with java.util.Locale selections
	 * 
	 * @param parent
	 * @param localizedText
	 * @return
	 */
	public ValidatingField<Locale> createComboLocale(Composite parent, LocalizedText localizedText) {
		if (localizedText == null) {
			localizedText = new LocalizedText("", Locale.ENGLISH);
		}
		ValidatingField<Locale> combo = this.locValToolkit.createComboField(parent, new LocaleFieldValidator(), false,
				localizedText.getLocale(), Locale.getAvailableLocales());
		GridDataFactory.fillDefaults().applyTo(combo.getControl());
		return combo;
	}

	/**
	 * Creates a label with the given description text
	 * 
	 * @param parent
	 * @param description
	 * @return
	 */
	public Label createLabel(Composite parent, String description) {
		Label label = new Label(parent, SWT.NONE);
		label.setText(description + ": ");
		GridDataFactory.fillDefaults().applyTo(label);
		return label;
	}

	public ValidatingField<LocalizedText> createLocalizedText(Composite parent, LocalizedText value) {
		ValidatingField<LocalizedText> validating = localizedTextValToolkit.createTextField(parent,
				new LocalizedTextFieldValidator(), false, value);
		GridDataFactory.fillDefaults().applyTo(validating.getControl());
		return validating;
	}

	public ValidatingField<LocalizedText> createComboEnum(Composite parent, final NodeId datatypeId) {
		BrowseDescription nodesToBrowse = new BrowseDescription();
		nodesToBrowse.setBrowseDirection(BrowseDirection.Forward);
		nodesToBrowse.setIncludeSubtypes(true);
		nodesToBrowse.setNodeClassMask(NodeClass.getMask(NodeClass.ALL));
		nodesToBrowse.setNodeId(datatypeId);
		nodesToBrowse.setReferenceTypeId(Identifiers.HierarchicalReferences);
		nodesToBrowse.setResultMask(BrowseResultMask.getMask(BrowseResultMask.ALL));
		LocalizedText[] txt = new LocalizedText[0];
		try {
			BrowseResult[] result = ServerInstance.getInstance().getServerInstance().getMaster()
					.browse(new BrowseDescription[] { nodesToBrowse }, UnsignedInteger.ZERO, null, null);
			if (result != null && result.length > 0 && result[0].getReferences() != null) {
				for (ReferenceDescription refDesc : result[0].getReferences()) {
					ExpandedNodeId nodeId = refDesc.getNodeId();
					UAVariableNode valueNode = (UAVariableNode) ServerInstance.getNode(nodeId);
					txt = (LocalizedText[]) valueNode.getValue().getValue();
				}
			}
		} catch (ServiceResultException e) {
			e.printStackTrace();
		}
		ValidationToolkit<LocalizedText> enumValToolkit = new ValidationToolkit<LocalizedText>(
				new LocalizedTextStringConverter(), DECORATOR_POSITION, DECORATOR_MARGIN_WIDTH, true);
		ValidatingField<LocalizedText> validating = enumValToolkit.createCComboField(parent,
				new IFieldValidator<LocalizedText>() {
					@Override
					public String getErrorMessage() {
						return null;
					}

					@Override
					public String getWarningMessage() {
						return null;
					}

					@Override
					public boolean isValid(LocalizedText arg0) {
						return true;
					}

					@Override
					public boolean warningExist(LocalizedText arg0) {
						return false;
					}
				}, true, LocalizedText.EMPTY, txt);
		validating.getControl().setData(txt);
		return validating;
	}

	/**
	 * Creates an Boolean textfield with validation support.
	 * 
	 * @param parent
	 * @return
	 */
	public ValidatingField<Boolean> createTextBoolean(Composite parent, Boolean value) {
		ValidatingField<Boolean> validating = booleanValToolkit.createTextField(parent, new BooleanFieldValidator(),
				false, value);
		GridDataFactory.fillDefaults().applyTo(validating.getControl());
		return validating;
	}

	/**
	 * Creates an SByte textfield with validation support.
	 * 
	 * @param parent
	 * @return
	 */
	public ValidatingField<Byte> createTextSByte(Composite parent, Byte value) {
		ValidatingField<Byte> validating = sbyteValToolkit.createTextField(parent, new SByteFieldValidator(), false,
				value);
		GridDataFactory.fillDefaults().applyTo(validating.getControl());
		return validating;
	}

	/**
	 * Creates an Byte textfield with validation support.
	 * 
	 * @param parent
	 * @return
	 */
	public ValidatingField<UnsignedByte> createTextByte(Composite parent, UnsignedByte value) {
		ValidatingField<UnsignedByte> validating = byteValToolkit.createTextField(parent, new ByteFieldValidator(),
				false, value);
		GridDataFactory.fillDefaults().applyTo(validating.getControl());
		return validating;
	}

	/**
	 * Creates an Int16 textfield with validation support.
	 * 
	 * @param parent
	 * @return
	 */
	public ValidatingField<Short> createTextInt16(Composite parent, Short value) {
		ValidatingField<Short> text = int16ValToolkit.createTextField(parent, new Int16FieldValidator(), false, value);
		GridDataFactory.fillDefaults().applyTo(text.getControl());
		return text;
	}

	/**
	 * Creates an UInt16 textfield with validation support.
	 * 
	 * @param parent
	 * @return
	 */
	public ValidatingField<UnsignedShort> createTextUInt16(Composite parent, UnsignedShort value) {
		ValidatingField<UnsignedShort> text = uint16ValToolkit.createTextField(parent, new UInt16FieldValidator(),
				false, value);
		GridDataFactory.fillDefaults().applyTo(text.getControl());
		return text;
	}

	/**
	 * Creates an Int32 textfield with validation support.
	 * 
	 * @param parent
	 * @return
	 */
	public ValidatingField<Integer> createTextInt32(Composite parent, Integer value) {
		ValidatingField<Integer> text = this.int32ValToolkit.createTextField(parent, new Int32FieldValidator(), false,
				value);
		GridDataFactory.fillDefaults().applyTo(text.getControl());
		return text;
	}

	/**
	 * Creates an UInt32 textfield with validation support.
	 * 
	 * @param parent
	 * @return
	 */
	public ValidatingField<UnsignedInteger> createTextUInt32(Composite parent, UnsignedInteger value) {
		ValidatingField<UnsignedInteger> text = this.uint32ValToolkit.createTextField(parent,
				new UInt32FieldValidator(), false, value);
		GridDataFactory.fillDefaults().applyTo(text.getControl());
		return text;
	}

	/**
	 * Creates an Int64 textfield with validation support.
	 * 
	 * @param parent
	 * @return
	 */
	public ValidatingField<Long> createTextInt64(Composite parent, Long value) {
		ValidatingField<Long> text = this.int64ValToolkit.createTextField(parent, new Int64FieldValidator(), false,
				value);
		GridDataFactory.fillDefaults().applyTo(text.getControl());
		return text;
	}

	/**
	 * Creates an UInt64 textfield with validation support.
	 * 
	 * @param parent
	 * @return
	 */
	public ValidatingField<UnsignedLong> createTextUInt64(Composite parent, UnsignedLong value) {
		ValidatingField<UnsignedLong> text = this.uint64ValToolkit.createTextField(parent, new UInt64FieldValidator(),
				false, value);
		GridDataFactory.fillDefaults().applyTo(text.getControl());
		return text;
	}

	/**
	 * Creates an Float textfield with validation support.
	 * 
	 * @param parent
	 * @return
	 */
	public ValidatingField<Float> createTextFloat(Composite parent, Float value) {
		ValidatingField<Float> text = this.floatValToolkit.createTextField(parent, new FloatFieldValidator(), false,
				value);
		GridDataFactory.fillDefaults().applyTo(text.getControl());
		return text;
	}

	/**
	 * Creates an Double textfield with validation support.
	 * 
	 * @param parent
	 * @return
	 */
	public ValidatingField<Double> createTextDouble(Composite parent, Double value) {
		ValidatingField<Double> text = this.doubleValToolkit.createTextField(parent, new DoubleFieldValidator(), false,
				value);
		GridDataFactory.fillDefaults().applyTo(text.getControl());
		return text;
	}

	/**
	 * Creates an Datetime textfield with validation support.
	 * 
	 * @param parent
	 * @return
	 */
	public ValidatingField<DateTime> createTextDateTime(Composite parent, DateTime value) {
		ValidatingField<DateTime> text = this.datetimeValToolkit.createTextField(parent, new DateTimeFieldValidator(),
				false, value);
		GridDataFactory.fillDefaults().applyTo(text.getControl());
		return text;
	}

	/**
	 * Creates an Guid textfield with validation support.
	 * 
	 * @param parent
	 * @return
	 */
	public ValidatingField<UUID> createTextGuid(Composite parent, UUID value) {
		ValidatingField<UUID> text = this.guidValToolkit.createTextField(parent, new GuidFieldValidator(), false,
				value);
		GridDataFactory.fillDefaults().applyTo(text.getControl());
		return text;
	}

	/**
	 * Create a text field for a nodeId value (not namespaceindex, for this use
	 * createTextInteger())
	 * 
	 * @param parent
	 * @param usedNodeIds
	 * @param combo_nodeId
	 * @param defaultValue
	 * @return
	 */
	public ValidatingField<String> createTextNodeId(Composite parent, CometCombo combo_nodeIdNamespaceIndex,
			NodeId usedNodeId) {
		NodeIdFieldValidator<String> validator = new NodeIdFieldValidator<String>(combo_nodeIdNamespaceIndex,
				usedNodeId);
		ValidatingField<String> txtFieldValidation = this.nodeIdValToolkit.createTextField(parent, validator, false,
				"");
		NodeIdTextFieldQuickFixProvider<String> quickFix = new NodeIdTextFieldQuickFixProvider<String>(
				combo_nodeIdNamespaceIndex);
		txtFieldValidation.setQuickFixProvider(quickFix);
		Text txtField = (Text) txtFieldValidation.getControl();
		// validator.setTextfield(txtField);
		quickFix.doQuickFix(txtFieldValidation);
		GridDataFactory.fillDefaults().applyTo(txtField);
		return txtFieldValidation;
	}

	/**
	 * Creates an unsigned integer textfield. (E.g. writemask, userwritemask entry)
	 * 
	 * @param parent
	 * @return
	 */
	public ValidatingField<UnsignedInteger> createTextUnsignedInteger(Composite parent, UnsignedInteger defaultValue) {
		ValidatingField<UnsignedInteger> text = this.uIntValToolkit.createTextField(parent, new UInt32FieldValidator(),
				false, defaultValue);
		GridDataFactory.fillDefaults().applyTo(text.getControl());
		return text;
	}

	/**
	 * Creates a browsename textfield
	 * 
	 * @param parent
	 * @return
	 */
	public ValidatingField<String> createTextBrowseName(Composite parent) {
		ValidatingField<String> txtFieldValidation = this.strValToolkit.createTextField(parent,
				new TextFieldValidator<String>(), false, "");
		Text txtField = (Text) txtFieldValidation.getControl();
		txtField.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				// TODO Auto-generated method stub
			}
		});
		GridDataFactory.fillDefaults().applyTo(txtField);
		return txtFieldValidation;
	}

	public ValidatingField<UnsignedInteger[]> createTextArrayDimensions(final Composite parent,
			final ValueRanks valueRank, ArrayDimFieldValidator<?> validator,
			ArrayDimQuickFixProvider<UnsignedInteger[]> quickFix) {
		final ValidatingField<UnsignedInteger[]> txtFieldValidation = this.arrayDimValToolkit.createTextField(parent,
				validator, false, null);
		Text txtField = (Text) txtFieldValidation.getControl();
		txtField.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				switch (valueRank) {
				case OneOrMoreDimensions:
					break;
				default:
					UnsignedInteger[] arrayDim = txtFieldValidation.getContents();
					// TODO: parameter usualy FALSE
					ArrayDimensionAttributeDialog dialog = new ArrayDimensionAttributeDialog(parent.getShell(),
							valueRank, arrayDim, false);
					int ok = dialog.open();
					if (ok == Dialog.OK) {
						UnsignedInteger[] dimensions = dialog.getArrayDimension();
						txtFieldValidation.setContents(dimensions);
						txtFieldValidation.validate();
					}
					break;
				}
			}
		});
		txtFieldValidation.setQuickFixProvider(quickFix);
		quickFix.doQuickFix(txtFieldValidation);
		GridDataFactory.fillDefaults().applyTo(txtField);
		return txtFieldValidation;
	}

	public ValidatingField<UnsignedInteger[]> createTextArrayDimensions(final Composite parent,
			final CometCombo combo_valueRank, final ArrayDimFieldValidator<?> validator,
			ArrayDimQuickFixProvider<UnsignedInteger[]> quickFix) {
		final ValidatingField<UnsignedInteger[]> txtFieldValidation = this.arrayDimValToolkit.createTextField(parent,
				validator, false, null);
		Text txtField = (Text) txtFieldValidation.getControl();
		txtField.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				ValueRanks valueRank = (ValueRanks) combo_valueRank.getData(combo_valueRank.getText());
				switch (valueRank) {
				case ScalarOrOneDimension:
				case Any:
				case Scalar:
				case OneOrMoreDimensions:
					break;
				case OneDimension:
				case TwoDimensions:
				case ThreeDimensions:
				case FourDimensions:
				case FiveDimensions:
					UnsignedInteger[] arrayDim = txtFieldValidation.getContents();
					ArrayDimensionAttributeDialog dialog = new ArrayDimensionAttributeDialog(parent.getShell(),
							valueRank, arrayDim, true);
					int ok = dialog.open();
					if (ok == Dialog.OK) {
						UnsignedInteger[] dimensions = dialog.getArrayDimension();
						txtFieldValidation.setContents(dimensions);
						txtFieldValidation.validate();
					}
					break;
				}
			}
		});
		txtField.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				/** validate field with its array dimension */
				ValidatingField<?> field = validator.getValueField();
				if (field != null) {
					field.validate();
				}
			}
		});
		txtFieldValidation.setQuickFixProvider(quickFix);
		quickFix.doQuickFix(txtFieldValidation);
		GridDataFactory.fillDefaults().applyTo(txtField);
		return txtFieldValidation;
	}

	/**
	 * Creates the value textfield (value entry), Which can use different
	 * validators.
	 * 
	 * @param parent
	 * @param valueType
	 * @param dataType
	 * @param valueType
	 * @param combo_valueRank
	 * @param valueRank
	 * @param arrayDimension
	 * @return
	 */
	public ValidatingField<Variant> createTextValue(Composite parent, VariantStringConverter converter,
			CometCombo valueType, CometCombo valueRank, Text arrayDimension,
			Map<ValueRanks, Map<NodeId, Variant>> valueCache) {
		this.variantValToolkit = new ValidationToolkit<Variant>(converter, DECORATOR_POSITION, DECORATOR_MARGIN_WIDTH,
				true);
		VariantFieldValidator validator = new VariantFieldValidator(valueType, valueRank, arrayDimension, valueCache);
		ValidatingField<Variant> valueText = variantValToolkit.createTextField(parent, validator, false, Variant.NULL);

		validator.setTextField((Text) valueText.getControl());
		GridDataFactory.fillDefaults().applyTo(valueText.getControl());
		return valueText;
	}

	public ValidatingField<String> createTextString(Composite parent) {
		TextFieldValidator<String> validator = new TextFieldValidator<String>();
		ValidatingField<String> stringField = strValToolkit.createTextField(parent, validator, false, "");
		Text txtField = (Text) stringField.getControl();
		validator.setParent(txtField);
		GridDataFactory.fillDefaults().applyTo(txtField);
		return stringField;
	}

	/****************************************************************************************/
	/** new setter functions for validation */
	/****************************************************************************************/
	/**
	 * Creates an Int32 textfield with validation support.
	 * 
	 * @param parent
	 * @return
	 */
	public void setTextInt32Validation(final ControlDecoration decoration, final Int32FieldValidator validator,
			final Object callback) {
		final Text element = (Text) decoration.getControl();
		final Image errorImage = FieldDecorationRegistry.getDefault()
				.getFieldDecoration(FieldDecorationRegistry.DEC_ERROR).getImage();
		final Image warningImage = FieldDecorationRegistry.getDefault()
				.getFieldDecoration(FieldDecorationRegistry.DEC_WARNING).getImage();
		final Int32StringConverter converter = new Int32StringConverter();
		element.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if (callback != null) {
					if (callback instanceof NodeEditorPart) {
						((NodeEditorPart) callback).setDirty(true);
					}
				}
				Integer value = converter.convertFromString(element.getText());
				if (!validator.isValid(value)) {
					decoration.setImage(errorImage);
					decoration.setDescriptionText(validator.getErrorMessage());
				} else if (validator.warningExist(value)) {
					decoration.setImage(warningImage);
					decoration.setDescriptionText(validator.getWarningMessage());
				} else {
					decoration.setImage(null);
					decoration.setDescriptionText("");
				}
			}
		});
		return;
	}

	/**
	 * Creates an Int32 textfield with validation support.
	 * 
	 * @param parent
	 * @return
	 */
	public void setTextInt32Validation(final ControlDecoration decoration, final Int32FieldValidator validator,
			final String error, final String warning, final Object callback) {
		final Text element = (Text) decoration.getControl();
		final Image errorImage = FieldDecorationRegistry.getDefault()
				.getFieldDecoration(FieldDecorationRegistry.DEC_ERROR).getImage();
		final Image warningImage = FieldDecorationRegistry.getDefault()
				.getFieldDecoration(FieldDecorationRegistry.DEC_WARNING).getImage();
		final Int32StringConverter converter = new Int32StringConverter();
		element.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if (callback != null) {
					if (callback instanceof NodeEditorPart) {
						((NodeEditorPart) callback).setDirty(true);
					}
				}
				Integer value = converter.convertFromString(element.getText());
				if (!validator.isValid(value)) {
					decoration.setImage(errorImage);
					decoration.setDescriptionText(error);
				} else if (validator.warningExist(value)) {
					decoration.setImage(warningImage);
					decoration.setDescriptionText(error);
				} else {
					decoration.setImage(null);
					decoration.setDescriptionText("");
				}
			}
		});
		return;
	}

	/**
	 * Creates an Int32 textfield with validation support.
	 * 
	 * @param parent
	 * @return
	 */
	public void setTextUInt32Validation(final ControlDecoration decoration, final UInt32FieldValidator validator,
			final String error, final String warning, final Object callback) {
		final Text element = (Text) decoration.getControl();
		final Image errorImage = FieldDecorationRegistry.getDefault()
				.getFieldDecoration(FieldDecorationRegistry.DEC_ERROR).getImage();
		final Image warningImage = FieldDecorationRegistry.getDefault()
				.getFieldDecoration(FieldDecorationRegistry.DEC_WARNING).getImage();
		final UInt32StringConverter converter = new UInt32StringConverter();
		element.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if (callback != null) {
					if (callback instanceof NodeEditorPart) {
						((NodeEditorPart) callback).setDirty(true);
					}
				}
				UnsignedInteger value = converter.convertFromString(element.getText());
				if (!validator.isValid(value)) {
					decoration.setImage(errorImage);
					decoration.setDescriptionText(error);
				} else if (validator.warningExist(value)) {
					decoration.setImage(warningImage);
					decoration.setDescriptionText(error);
				} else {
					decoration.setImage(null);
					decoration.setDescriptionText("");
				}
			}
		});
		return;
	}

	/**
	 * Creates an double textfield with validation support.
	 * 
	 * @param parent
	 * @return
	 */
	public void setTextDoubleValidation(final ControlDecoration decoration, final DoubleFieldValidator validator,
			final Object callback) {
		final Text element = (Text) decoration.getControl();
		final Image errorImage = FieldDecorationRegistry.getDefault()
				.getFieldDecoration(FieldDecorationRegistry.DEC_ERROR).getImage();
		final Image warningImage = FieldDecorationRegistry.getDefault()
				.getFieldDecoration(FieldDecorationRegistry.DEC_WARNING).getImage();
		final DoubleStringConverter converter = new DoubleStringConverter();
		element.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if (callback != null) {
					if (callback instanceof NodeEditorPart) {
						((NodeEditorPart) callback).setDirty(true);
					}
				}
				Double value = converter.convertFromString(element.getText());
				if (!validator.isValid(value)) {
					decoration.setImage(errorImage);
					decoration.setDescriptionText(validator.getErrorMessage());
				} else if (validator.warningExist(value)) {
					decoration.setImage(warningImage);
					decoration.setDescriptionText(validator.getWarningMessage());
				} else {
					decoration.setImage(null);
					decoration.setDescriptionText("");
				}
			}
		});
		return;
	}

	public void setTextStringValidation(final ControlDecoration decoration, String error, String warning,
			final Object callback) {
		final Text element = (Text) decoration.getControl();
		final Image errorImage = FieldDecorationRegistry.getDefault()
				.getFieldDecoration(FieldDecorationRegistry.DEC_ERROR).getImage();
		final Image warningImage = FieldDecorationRegistry.getDefault()
				.getFieldDecoration(FieldDecorationRegistry.DEC_WARNING).getImage();
		final TextFieldValidator<String> validator = new TextFieldValidator<String>();
		validator.setParent(element);
		validator.setErrorMessage(error);
		validator.setWarningMessage(warning);
		element.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if (callback != null) {
					if (callback instanceof NodeEditorPart) {
						((NodeEditorPart) callback).setDirty(true);
					}
				}
				if (!validator.isValid(element.getText())) {
					decoration.setImage(errorImage);
					decoration.setDescriptionText(validator.getErrorMessage());
				} else if (validator.warningExist(element.getText())) {
					decoration.setImage(warningImage);
					decoration.setDescriptionText(validator.getWarningMessage());
				} else {
					decoration.setImage(null);
					decoration.setDescriptionText("");
				}
			}
		});
		return;
	}

	public void setTextVariantValidation(final ControlDecoration decoration, String error, String warning,
			final Object callback) {
		final Text element = (Text) decoration.getControl();
		final Image errorImage = FieldDecorationRegistry.getDefault()
				.getFieldDecoration(FieldDecorationRegistry.DEC_ERROR).getImage();
		final Image warningImage = FieldDecorationRegistry.getDefault()
				.getFieldDecoration(FieldDecorationRegistry.DEC_WARNING).getImage();
		final TextFieldValidator<String> validator = new TextFieldValidator<String>();
		validator.setParent(element);
		validator.setErrorMessage(error);
		validator.setWarningMessage(warning);
		element.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if (callback != null) {
					if (callback instanceof NodeEditorPart) {
						((NodeEditorPart) callback).setDirty(true);
					}
				}
				if (!validator.isValid(element.getText())) {
					decoration.setImage(errorImage);
					decoration.setDescriptionText(validator.getErrorMessage());
				} else if (validator.warningExist(element.getText())) {
					decoration.setImage(warningImage);
					decoration.setDescriptionText(validator.getWarningMessage());
				} else {
					decoration.setImage(null);
					decoration.setDescriptionText("");
				}
			}
		});
		return;
	}

	/**
	 * Creates a combobox filed with java.util.Locale selections
	 * 
	 * @param parent
	 * @param localizedText
	 * @return
	 */
	public void setComboLocale(CometCombo combo, LocalizedText localizedText) {
		if (localizedText == null) {
			localizedText = new LocalizedText("en", Locale.ENGLISH);
		}
		for (Locale loc : Locale.getAvailableLocales()) {
			combo.add(loc.toString());
		}
		combo.setText(localizedText.getText());
		return;
	}

	/**
	 * Creates a combobox filed with java.util.Locale selections
	 * 
	 * @param parent
	 * @param localizedText
	 * @return
	 */
	public void setComboModellingRule(final CometCombo combo, Node node) {
		// keine regeln
		combo.add(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "ModellingRule.norule.text"));
		for (NamingRuleType type : NamingRuleType.values()) {
			combo.add(type.toString());
		}
		combo.select(0);
		if (node != null) {
			for (ReferenceNode ref : node.getReferences()) {
				if (ref.getReferenceTypeId().equals(Identifiers.HasModellingRule)) {
					try {
						NodeId target = ServerInstance.getInstance().getServerInstance().getNamespaceUris()
								.toNodeId(ref.getTargetId());

						if (target.equals(Identifiers.ModellingRule_Mandatory)) {
							// select mandatory
							combo.select(1);
							// combo.setText(NamingRuleType.Mandatory.name());
							break;
						} else if (target.equals(Identifiers.ModellingRule_Optional)) {
							combo.select(2);
							// select optional
							// combo.setText(NamingRuleType.Mandatory.name());
							break;
						} else if (target.equals(Identifiers.ModellingRule_ExposesItsArray)) {
							combo.select(3);
							// select optional
							// combo.setText(NamingRuleType.Mandatory.name());
							break;
						} else if (target.equals(Identifiers.ModellingRule_OptionalPlaceholder)) {
							combo.select(4);
							// select optional
							// combo.setText(NamingRuleType.Mandatory.name());
							break;
						} else if (target.equals(Identifiers.ModellingRule_MandatoryPlaceholder)) {
							combo.select(5);
							// select optional
							// combo.setText(NamingRuleType.Mandatory.name());
							break;
						}
					} catch (ServiceResultException e) {
						Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
					}
				}
			}
		}
		return;
	}

	public void deleteModellingRule(Node node) {
		List<ReferenceNode> refs = new ArrayList<ReferenceNode>();
		boolean needToUpdate = false;
		if (node != null) {
			for (ReferenceNode ref : node.getReferences()) {
				if (ref.getReferenceTypeId().equals(Identifiers.HasModellingRule)) {
					needToUpdate = true;
				} else {
					refs.add(ref);
				}
			}
			if (needToUpdate) {
				node.setReferences(refs.toArray(new ReferenceNode[refs.size()]));
			}
		}
	}

	private void removeReference(Node node, NodeId type, ExpandedNodeId target) {
		List<ReferenceNode> refs = new ArrayList<ReferenceNode>();
		if (node != null) {
			for (ReferenceNode ref : node.getReferences()) {
				if (!ref.getReferenceTypeId().equals(type) || !ref.getTargetId().equals(target)) {
					refs.add(ref);
				}
			}
		}
		node.setReferences(refs.toArray(new ReferenceNode[refs.size()]));
	}

	public void setModellingRule(Node node, NamingRuleType rule) {
		NamespaceTable nsTable = ServerInstance.getInstance().getServerInstance().getNamespaceUris();
		List<ReferenceNode> refs = new ArrayList<ReferenceNode>();
		// boolean needToUpdate = false;
		ReferenceNode foundRef = null;
		NodeId ruleId = null;
		if (node != null) {
			for (ReferenceNode ref : node.getReferences()) {
				if (ref.getReferenceTypeId().equals(Identifiers.HasModellingRule)) {
					// needToUpdate = true;
					foundRef = ref;
					// remove this node from modelling rule node
					Node n = ServerInstance.getNode(ref.getTargetId());
					removeReference(n, Identifiers.HasModellingRule,
							new ExpandedNodeId(node.getNodeId()));
				} else {
					refs.add(ref);
				}
			}
			switch (rule) {
			case Mandatory:
				ruleId = Identifiers.ModellingRule_Mandatory;
				break;
			case Optional:
				ruleId = Identifiers.ModellingRule_Optional;
				break;
			case ExposesItsArray:
				ruleId = Identifiers.ModellingRule_ExposesItsArray;
				break;
			case OptionalPlaceholder:
				ruleId = Identifiers.ModellingRule_OptionalPlaceholder;
				break;
			case MandatoryPlaceholder:
				ruleId = Identifiers.ModellingRule_MandatoryPlaceholder;
				break;
			}
			// if (needToUpdate) {
			// foundRef.setTargetId(new ExpandedNodeId(ruleId));
			// } else {
			// if
			foundRef = new ReferenceNode(Identifiers.HasModellingRule, false,
					new ExpandedNodeId(ruleId));
			refs.add(foundRef);
			// now add reference to modelling rule node
			// }
			node.setReferences(refs.toArray(new ReferenceNode[refs.size()]));
		}
	}

	public void setComboDataType(CometCombo combo) {
		Map<NodeId, String> dataTypes = fetchDataTypes();
		for (String value : dataTypes.values()) {
			combo.add(value);
		}
		if (combo.getItemCount() > 0) {
			combo.select(0);
		}
		combo.setData(dataTypes);
		return;
	}

	public void setValue(Text txt_value, String value) {
		txt_value.setText(value);
	}

	public void setComboDataType(CometCombo combo, NodeId varTypeId) {
		/**
		 * InternalServer server = ServerInstance.getInstance().getServerInstance();
		 * ReadValueId []ids = new ReadValueId[1]; ids[0] = new ReadValueId(varTypeId,
		 * Attributes.DataType, null, null);
		 * 
		 * DataValue[] vals; try { vals = server.getNodeManager().readLocal(server, ids,
		 * 0, TimestampsToReturn.Both);
		 * 
		 * if(vals.length > 0) { vals[0].getValue().getValue();
		 * 
		 * Map<NodeId, String> dataTypes = fetchDataTypes(varTypeId);
		 * 
		 * for (String value : dataTypes.values()) { combo.add(value); }
		 * 
		 * if(combo.getItemCount() > 0) { combo.select(0); } } } catch
		 * (ServiceResultException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 */
		Node node = ServerInstance.getNode(varTypeId);
		if (node != null) {
			NodeId dataTypeId = ((VariableTypeNode) node).getDataType();
			Map<NodeId, String> dataTypes = fetchDataTypes(((VariableTypeNode) node).getDataType());
			combo.removeAll();
			int index = 0;
			for (Entry<NodeId, String> value : dataTypes.entrySet()) {
				// add datatype
				combo.add(value.getValue());
				if (dataTypeId != null && dataTypeId.equals(value.getKey())) {
					combo.select(index);
				}
				index++;
			}
			combo.setData(dataTypes);
		}
		return;
	}

	/**
	 * Creates a non - validated namespace combobox
	 * 
	 * @param parent
	 * @return
	 */
	public void setNamespaceCombobox(CometCombo combo) {
		for (String s : ServerInstance.getInstance().getServerInstance().getNamespaceUris().toArray()) {
			if (s == null) {
				continue;
			}
			combo.add(s);
		}
		if (combo.getItemCount() > 1) {
			combo.select(1);
		}
		return;
	}

	private Menu createQuickFixMenu(final Text parent, final NodeIdQuickFixProvider provider) {
		Menu newMenu = new Menu(parent);
		MenuItem item = new MenuItem(newMenu, SWT.PUSH);
		item.setText(provider.getQuickFixMenuText());
		item.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent event) {
				provider.doQuickFix(parent);
				parent.notifyListeners(SWT.KeyUp, null);
			}

			public void widgetDefaultSelected(SelectionEvent event) {
			}
		});
		return newMenu;
	}

	/**
	 * Create a text field for a nodeId value (not namespaceindex, for this use
	 * createTextInteger())
	 * 
	 * @param parent
	 * @param usedNodeIds
	 * @param combo_nodeId
	 * @param defaultValue
	 * @return
	 */
	public void setTextNodeId(final ControlDecoration decoration, final Label label,
			final CometCombo nodeIdNamespaceIndex, final NodeIdFieldValidator<String> validator,
			final Object callback) {
		final Text element = (Text) decoration.getControl();
		final NodeIdQuickFixProvider provider = new NodeIdQuickFixProvider(nodeIdNamespaceIndex, callback);
		final Image errorQuickImage = FieldDecorationRegistry.getDefault()
				.getFieldDecoration(FieldDecorationRegistry.DEC_ERROR_QUICKFIX).getImage();
		final Image warningImage = FieldDecorationRegistry.getDefault()
				.getFieldDecoration(FieldDecorationRegistry.DEC_WARNING).getImage();
		final Menu quickfMen = createQuickFixMenu(element, provider);
		decoration.addMenuDetectListener(new MenuDetectListener() {
			public void menuDetected(MenuDetectEvent e) {
				quickfMen.setLocation(e.x, e.y);
				quickfMen.setVisible(true);
			}
		});
		element.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if (callback != null) {
					if (callback instanceof NodeEditorPart) {
						((NodeEditorPart) callback).setDirty(true);
					}
				}
				if (!validator.isValid(element.getText())) {
					decoration.setImage(errorQuickImage);
					decoration.setDescriptionText(validator.getErrorMessage());
				} else if (validator.warningExist(element.getText())) {
					decoration.setImage(warningImage);
					decoration.setDescriptionText(validator.getWarningMessage());
				}
				// OK
				else {
					decoration.setImage(null);
					decoration.setDescriptionText("");
				}
			}
		});
		return;
	}

	public void setComboValueRank(CometCombo cmb_valueRank, ValueRanks valueRank, BrowserModelNode node) {
		NodeClass nodeClass = node.getNode().getNodeClass();
		ValueRanks vRank = null;
		switch (nodeClass) {
		case VariableType:
			BrowserModelNode parent = node.getParent();
			Node parentNode = parent.getNode();
			if (parentNode != null) {
				NodeClass nc2 = parentNode.getNodeClass();
				switch (nc2) {
				case VariableType:
					vRank = ValueRanks.getValueRanks(((VariableTypeNode) parent.getNode()).getValueRank());
					break;
				// parent has no variable type class
				default:
					vRank = valueRank;
					break;
				}
			}
			break;
		case Variable:
			ExpandedNodeId typeDef = node.getNode().findTarget(Identifiers.HasTypeDefinition, false);
			Node varTypeNode = ServerInstance.getNode(typeDef);
			vRank = ValueRanks.getValueRanks(((VariableTypeNode) varTypeNode).getValueRank());
			break;
		default:
			// do nothing
			break;
		}
		setComboValueRank(cmb_valueRank, vRank);
		// select value rank
		for (int i = 0; i < cmb_valueRank.getItems().length; i++) {
			String item = cmb_valueRank.getItem(i);
			if (valueRank.toString().equals(item)) {
				cmb_valueRank.select(i);
				break;
			}
		}
	}

	public void setComboValueRank(CometCombo combo) {
		for (ValueRanks v : ValueRanks.values()) {
			combo.add(v.toString());
		}
		if (combo.getItemCount() > 2) {
			combo.select(2);
		}
		return;
	}

	public void setComboValueRank(CometCombo combo, ValueRanks rank) {
		List<ValueRanks> ranks = new ArrayList<ValueRanks>();
		switch (rank) {
		case ScalarOrOneDimension:
			ranks.add(ValueRanks.ScalarOrOneDimension);
			ranks.add(ValueRanks.Scalar);
			ranks.add(ValueRanks.OneDimension);
			break;
		case Any:
			for (ValueRanks v : ValueRanks.values()) {
				ranks.add(v);
			}
			break;
		case Scalar:
			ranks.add(ValueRanks.Scalar);
			break;
		case OneOrMoreDimensions:
			ranks.add(ValueRanks.OneOrMoreDimensions);
			ranks.add(ValueRanks.OneDimension);
			ranks.add(ValueRanks.TwoDimensions);
			ranks.add(ValueRanks.ThreeDimensions);
			ranks.add(ValueRanks.FourDimensions);
			ranks.add(ValueRanks.FiveDimensions);
			break;
		default:
			ranks.add(rank);
			break;
		}
		for (ValueRanks v : ranks) {
			combo.add(v.toString());
		}
		return;
	}

	public void setComboValueType(CometCombo combo) {
		for (BuiltinType type : BuiltinType.values()) {
			combo.add(type.name());
		}
		if (combo.getItemCount() > 0) {
			combo.select(0);
		}
		return;
	}

	public void setComboValueType(CometCombo combo, NodeId dataTypeId) {
		combo.removeAll();
		// set all types
		if (Identifiers.BaseDataType.equals(dataTypeId)) {
			setComboValueType(combo);
			return;
		}
		BuiltinType type = BuiltinType.getType(dataTypeId);
		if (type == null) {
			try {
				type = findRootBuiltinType(dataTypeId);
			} catch (ServiceResultException e) {
				e.printStackTrace();
			}
		}
		// reset type combo
		switch (type) {
		case Boolean:
			combo.add(BuiltinType.Boolean.name());
			break;
		case Byte:
			combo.add(BuiltinType.Byte.name());
			break;
		case ByteString:
			combo.add(BuiltinType.ByteString.name());
			break;
		case DataValue:
			combo.add(BuiltinType.DataValue.name());
			break;
		case DateTime:
			combo.add(BuiltinType.DateTime.name());
			break;
		case DiagnosticInfo:
			combo.add(BuiltinType.DiagnosticInfo.name());
			break;
		case Double:
			combo.add(BuiltinType.Double.name());
			break;
		case Enumeration:
			combo.add(BuiltinType.Enumeration.name());
			break;
		case ExpandedNodeId:
			combo.add(BuiltinType.ExpandedNodeId.name());
			break;
		case ExtensionObject:
			combo.add(BuiltinType.ExtensionObject.name());
			break;
		case Float:
			combo.add(BuiltinType.Float.name());
			break;
		case Guid:
			combo.add(BuiltinType.Guid.name());
			break;
		case Int16:
			combo.add(BuiltinType.Int16.name());
			break;
		case Int32:
			combo.add(BuiltinType.Int32.name());
			break;
		case Int64:
			combo.add(BuiltinType.Int64.name());
			break;
		case Integer:
			combo.add(BuiltinType.Integer.name());
			combo.add(BuiltinType.Int16.name());
			combo.add(BuiltinType.Int32.name());
			combo.add(BuiltinType.Int64.name());
			combo.add(BuiltinType.SByte.name());
			combo.add(BuiltinType.UInteger.name());
			combo.add(BuiltinType.UInt16.name());
			combo.add(BuiltinType.UInt32.name());
			combo.add(BuiltinType.UInt64.name());
			break;
		case LocalizedText:
			combo.add(BuiltinType.LocalizedText.name());
			break;
		case NodeId:
			combo.add(BuiltinType.NodeId.name());
			break;
		case Null:
			combo.add(BuiltinType.Null.name());
			break;
		case Number:
			// combo.add(BuiltinType.Number.name());
			combo.add(BuiltinType.Double.name());
			combo.add(BuiltinType.Float.name());
			combo.add(BuiltinType.Integer.name());
			combo.add(BuiltinType.Int16.name());
			combo.add(BuiltinType.Int32.name());
			combo.add(BuiltinType.Int64.name());
			combo.add(BuiltinType.SByte.name());
			combo.add(BuiltinType.UInteger.name());
			combo.add(BuiltinType.UInt16.name());
			combo.add(BuiltinType.UInt32.name());
			combo.add(BuiltinType.UInt64.name());
			break;
		case QualifiedName:
			combo.add(BuiltinType.QualifiedName.name());
			break;
		case SByte:
			combo.add(BuiltinType.SByte.name());
			break;
		case StatusCode:
			combo.add(BuiltinType.StatusCode.name());
			break;
		case String:
			combo.add(BuiltinType.String.name());
			break;
		case UInt16:
			combo.add(BuiltinType.UInt16.name());
			break;
		case UInt32:
			combo.add(BuiltinType.UInt32.name());
			break;
		case UInt64:
			combo.add(BuiltinType.UInt64.name());
			break;
		case UInteger:
			combo.add(BuiltinType.UInteger.name());
			combo.add(BuiltinType.UInt16.name());
			combo.add(BuiltinType.UInt32.name());
			combo.add(BuiltinType.UInt64.name());
			break;
		case Variant:
			combo.add(BuiltinType.Variant.name());
			break;
		case XmlElement:
			combo.add(BuiltinType.XmlElement.name());
			break;
		default:
			break;
		}
		combo.select(0);
		// return type;
	}

	private BuiltinType findRootBuiltinType(NodeId newDataTypeId) throws ServiceResultException {
		BrowseDescription[] nodesToBrowse = new BrowseDescription[1];
		nodesToBrowse[0] = new BrowseDescription(newDataTypeId, BrowseDirection.Inverse,
				Identifiers.HierarchicalReferences, true, NodeClass.getMask(NodeClass.DataType),
				BrowseResultMask.getMask(BrowseResultMask.ALL));
		BrowseResult[] result = ServerInstance.browse(newDataTypeId, Identifiers.HierarchicalReferences,
				NodeClass.getSet(NodeClass.DataType.getValue()), BrowseResultMask.ALL, BrowseDirection.Inverse, true);
		ExpandedNodeId id = result[0].getReferences()[0].getNodeId();
		NodeId parentDataTypeId = ServerInstance.getInstance().getServerInstance().getNamespaceUris().toNodeId(id);
		BuiltinType bt = BuiltinType.getType(parentDataTypeId);
		if (bt != null) {
			return bt;
		}
		return findRootBuiltinType(parentDataTypeId);
	}

	/**
	 * Creates the value textfield (value entry), Which can use different
	 * validators.
	 * 
	 * @param parent
	 * @param dataType
	 * @param valueType
	 * @param valueRank
	 * @return
	 */
	public void setTextValueValidation(final ControlDecoration decoration, final VariantStringConverter converter,
			final VariantFieldValidator validator, final Object callback) {
		final Text element = (Text) decoration.getControl();
		final Image errorImage = FieldDecorationRegistry.getDefault()
				.getFieldDecoration(FieldDecorationRegistry.DEC_ERROR).getImage();
		final Image warningImage = FieldDecorationRegistry.getDefault()
				.getFieldDecoration(FieldDecorationRegistry.DEC_WARNING).getImage();
		element.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if (callback != null) {
					if (callback instanceof NodeEditorPart) {
						((NodeEditorPart) callback).setDirty(true);
					}
				}
				Variant variant = converter.convertFromString(element.getText());
				if (!validator.isValid(variant)) {
					decoration.setImage(errorImage);
					decoration.setDescriptionText(validator.getErrorMessage());
				} else if (validator.warningExist(variant)) {
					decoration.setImage(warningImage);
					decoration.setDescriptionText(validator.getWarningMessage());
				} else {
					decoration.setImage(null);
					decoration.setDescriptionText("");
				}
			}
		});
		return;
	}

	public void setDataTypeReferenceTable(TableViewer tableViewer, Node input) {
		tableViewer.setContentProvider(
				new TypesReferenceTableContentProvider(Identifiers.BaseDataType, NodeClass.DataType));
		tableViewer.setLabelProvider(new ReferenceTableLabelProvider());
		tableViewer.setComparator(new ViewerComparator() {
			public int compare(Viewer viewer, Object e1, Object e2) {
				int cat1 = category(e1);
				int cat2 = category(e2);
				if (cat1 != cat2) {
					return cat1 - cat2;
				}
				String name1 = getLabel(viewer, e1);
				String name2 = getLabel(viewer, e2);
				// use the comparator to compare the strings
				return getComparator().compare(name1, name2);
			}

			private String getLabel(Viewer viewer, Object e1) {
				ReferenceTypeNode referenceTypeNode = (ReferenceTypeNode) ServerInstance
						.getNode(((ReferenceNode) e1).getReferenceTypeId());
				String display = null;
				if (((ReferenceNode) e1).getIsInverse() && !referenceTypeNode.getSymmetric()) {
					display = referenceTypeNode.getInverseName().getText();
				} else {
					display = referenceTypeNode.getBrowseName().getName();
				}
				return display;
			}
		});
		if (input != null) {
			tableViewer.setInput(input.getReferences());
		}
	}

	public void setObjectReferenceTable(TableViewer tableViewer, Node input) {
		tableViewer.setContentProvider(
				new TypesReferenceTableContentProvider(Identifiers.BaseObjectType, NodeClass.Object));
		tableViewer.setLabelProvider(new ReferenceTableLabelProvider());
		if (input != null) {
			tableViewer.setInput(input.getReferences());
		}
		return;
	}

	public void setObjectTypeReferenceTable(TableViewer tableViewer, Node input) {
		tableViewer.setContentProvider(
				new TypesReferenceTableContentProvider(Identifiers.BaseObjectType, NodeClass.ObjectType));
		tableViewer.setLabelProvider(new ReferenceTableLabelProvider());
		if (input != null) {
			tableViewer.setInput(input.getReferences());
		}
		return;
	}

	public void setMethodReferenceTable(TableViewer tableViewer, Node input) {
		tableViewer.setContentProvider(
				new TypesReferenceTableContentProvider(Identifiers.BaseObjectType, NodeClass.Method));
		tableViewer.setLabelProvider(new ReferenceTableLabelProvider());
		tableViewer.setInput(input);
		return;
	}

	public void setReferenceTypeReferenceTable(TableViewer tableViewer, Node input) {
		tableViewer.setContentProvider(
				new TypesReferenceTableContentProvider(Identifiers.BaseVariableType, NodeClass.ReferenceType));
		tableViewer.setLabelProvider(new ReferenceTableLabelProvider());
		tableViewer.setInput(input.getReferences());
		return;
	}

	public void setVariableReferenceTable(TableViewer tableViewer, Node input) {
		tableViewer.setContentProvider(
				new TypesReferenceTableContentProvider(Identifiers.BaseVariableType, NodeClass.Variable));
		tableViewer.setLabelProvider(new ReferenceTableLabelProvider());
		tableViewer.setInput(input.getReferences());
		return;
	}

	public void setVariableTypeReferenceTable(TableViewer tableViewer, Node input) {
		tableViewer.setContentProvider(
				new TypesReferenceTableContentProvider(Identifiers.BaseVariableType, NodeClass.VariableType));
		tableViewer.setLabelProvider(new ReferenceTableLabelProvider());
		tableViewer.setInput(input.getReferences());
		return;
	}

	public void setReferenceDBClick(final TableViewer references, final Object callback) {
		// only admins are allowed to edit OPC UA node references
		boolean isAdmin = UserUtils.testUserRights(1);
		if(!isAdmin) {
			return;
		}
		
		references.addDoubleClickListener(new IDoubleClickListener() {
			@Override
			public void doubleClick(DoubleClickEvent event) {
				editReferences(references, (NodeEditorPart) callback, false);
			}
		});
	}

	public void setAddReferenceButton(Button button, final TableViewer references, final Object callback) {
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent event) {
				addReference(references, (NodeEditorPart) callback);
			}
		});
	}

	public void setEditReferenceButton(Button button, final TableViewer references, final NodeEditorPart callback) {
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				editReferences(references, callback, true);
			}
		});
	}

	public void setDeleteReferenceButton(Button button, final TableViewer references, final Object callback) {
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent event) {
				deleteReference(references, (NodeEditorPart) callback);
			}
		});
	}

	public void setReferenceTableSelection(TableViewer references, final Button delete, final Button edit) {
		// Only admin rights allowed to edit OPC UA node references
		boolean isAdmin = UserUtils.testUserRights(1);
		if(!isAdmin) {
			return ;
		}
		
		delete.setEnabled(false);
		edit.setEnabled(false);
				
		references.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				if (!event.getSelection().isEmpty()) {
					edit.setEnabled(true);
				}
				/** Mandatory Enable */
				ReferenceNode selection = (ReferenceNode) ((StructuredSelection) event.getSelection())
						.getFirstElement();
				boolean isEnabled = checkEnableDeleteButton(selection);
				delete.setEnabled(isEnabled);
			}

			private boolean checkEnableDeleteButton(ReferenceNode referenceToDelete) {
				if (referenceToDelete != null) {
					boolean hasModellingRule = ServerInstance.getInstance().getServerInstance().getTypeTable()
							.isTypeOf(referenceToDelete.getReferenceTypeId(), Identifiers.HasModellingRule);
//					if (hasModellingRule) {
//						return false;
//					}
				}
				return true;
			}
		});
	}

	protected void addReference(TableViewer viewer, NodeEditorPart callback) {
		if (callback instanceof NodeEditorPart) {
			if (((NodeEditorPart) callback).getEditorInput().getNode().getNode().getNodeId().getNamespaceIndex() == 0) {
				/**
				 * do nothing if we have the ua internal name space index
				 */
				return;
			}
		}
		BrowserModelNode sourceBrowserNode = ((NodeEditorPart) callback).getEditorInput().getNode();
		NodeId sourceNodeId = sourceBrowserNode.getNode().getNodeId();
		AddReferenceWizard wizard = new AddReferenceWizard();
		wizard.setSourceId(sourceNodeId);
		WizardDialog dialog = new WizardDialog(viewer.getTable().getShell(), wizard);
		int open = dialog.open();
		if (WizardDialog.OK == open) {
			ExpandedNodeId targetId = wizard.getTargetId();
			DesignerUtils.refreshBrowserNode(sourceBrowserNode, true);
			List<ExpandedNodeId> targets = new ArrayList<>();
			targets.add(targetId);
			DesignerUtils.refresh(targets);
		}
	}

	protected void deleteReference(TableViewer viewer, NodeEditorPart callback) {
		if (callback instanceof NodeEditorPart) {
			if (((NodeEditorPart) callback).getEditorInput().getNode().getNode().getNodeId().getNamespaceIndex() == 0) {
				/**
				 * do nothing if we have the ua internal name space index
				 */
				return;
			}
		}
		/**
		 * Source Node
		 */
		BrowserModelNode node = ((NodeEditorPart) callback).getEditorInput().getNode();
		/**
		 * Reference to edit
		 */
		TableItem[] selections = viewer.getTable().getSelection();
		Object element = selections[0].getData();
		if (element instanceof ReferenceNode) {
			DeleteReferenceWizard wizard = new DeleteReferenceWizard();
			wizard.setSourceNode(node);
			try {
				wizard.setReferenceToEdit((ReferenceNode) element);
				WizardDialog dialog = new WizardDialog(viewer.getTable().getShell(), wizard);
				int result = dialog.open();
				if (WizardDialog.OK == result) {
					DesignerUtils.refreshBrowserNode(node, true);
					List<ExpandedNodeId> targets = new ArrayList<>();
					targets.add(((ReferenceNode) element).getTargetId());
					DesignerUtils.refresh(targets);
				}
			} catch (ServiceResultException e) {
				Logger.getLogger(getClass().getName()).log(Level.INFO, e.getMessage());
			}
		}
	}

	protected void editReferences(TableViewer viewer, NodeEditorPart callback, boolean editReference) {
		if (callback instanceof NodeEditorPart) {
			if (editReference && ((NodeEditorPart) callback).getEditorInput().getNode().getNode().getNodeId()
					.getNamespaceIndex() == 0) {
				/**
				 * do nothing if we have the ua internal name space index
				 */
				return;
			}
		}
		/**
		 * Source Node
		 */
		BrowserModelNode node = ((NodeEditorPart) callback).getEditorInput().getNode();
		/**
		 * Reference to edit
		 */
		TableItem[] selections = viewer.getTable().getSelection();
		Object element = selections[0].getData();
		if (element instanceof ReferenceNode) {
			// validate rules
			// ReferenceModel model = ReferenceUtil.initializeReferenceTypeTree(
			// Identifiers.References, Identifiers.HasSubtype, false);
			EditReferenceWizard wizard = new EditReferenceWizard();
			wizard.setSourceNode(node);
			try {
				wizard.setReferenceToEdit((ReferenceNode) element);
				WizardDialog dialog = new WizardDialog(viewer.getTable().getShell(), wizard);
				int open = dialog.open();
				if (WizardDialog.OK == open) {
					DesignerUtils.refreshBrowserNode(node, true);
					List<ExpandedNodeId> targets = new ArrayList<>();
					targets.add(((ReferenceNode) element).getTargetId());
					DesignerUtils.refresh(targets);
				}
			} catch (ServiceResultException e) {
				Logger.getLogger(getClass().getName()).log(Level.INFO, e.getMessage());
			}
		}
	}

	public void setDirtyListener(final Label label, final CheckBoxButton button, final Object callback) {
		String tempText = button.getText();
		if (label != null) {
			tempText = label.getText();
		}
		final String labelText = tempText;
		if (callback instanceof NodeEditorPart) {
			button.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					((NodeEditorPart) callback).setDirty(true);
					// log(Status.OK,
					// labelText
					// + " "
					// + OPCDesignerString
					// .getString("Toolkit.changeFrom.text")
					// + !button.isChecked()
					// + " "
					// + OPCDesignerString
					// .getString("Toolkit.changeTo.text")
					// + button.isChecked(),
					// ((NodeEditorPart) callback).getEditorInput()
					// .getNode().getNode());
				}
			});
		}
	}

	public void setDirtyListener(final Label label, final CometCombo combo, final Object callback) {
		if (callback instanceof NodeEditorPart) {
			combo.addModifyListener(new ModifyListener() {
				@Override
				public void modifyText(ModifyEvent e) {
					if (combo.getOldText().compareTo(combo.getText()) != 0) {
						((NodeEditorPart) callback).setDirty(true);
						((NodeEditorPart) callback).validate();
					}
				}
			});
		}
	}

	public void setDirtyListener(Text text, final NodeEditorPart callback) {
		if (callback instanceof NodeEditorPart) {
			text.addModifyListener(new ModifyListener() {
				@Override
				public void modifyText(ModifyEvent e) {
					((NodeEditorPart) callback).setDirty(true);
				}
			});
		}
	}

	public void setDirtyListener(final CometCombo combo, final Text text, final NodeEditorReferenceTypePart callback) {
		if (callback instanceof NodeEditorPart) {
			combo.addModifyListener(new ModifyListener() {
				@Override
				public void modifyText(ModifyEvent e) {
					if (combo.getOldText().compareTo(combo.getText()) != 0) {
						((NodeEditorPart) callback).setDirty(true);
					}
				}
			});
			text.addModifyListener(new ModifyListener() {
				@Override
				public void modifyText(ModifyEvent e) {
					((NodeEditorPart) callback).setDirty(true);
				}
			});
		}
	}

	public void setDirtyListener(final ValidatingField<Variant> text, final NodeEditorPart callback) {
		if (callback instanceof NodeEditorPart) {
			((Text) text.getControl()).addModifyListener(new ModifyListener() {
				@Override
				public void modifyText(ModifyEvent e) {
					((NodeEditorPart) callback).setDirty(true);
					text.validate();
				}
			});
		}
	}

	public void setWriteMask(final Label label, final Text text, final Object callback) {
		text.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				String oldval = text.getText();
				if (callback instanceof NodeEditorPart) {
					WriteMaskDialog_ writemaskDialog = new WriteMaskDialog_(
							((NodeEditorPart) callback).getEditorSite().getShell());// ,
					// node.getNodeClass(),
					// new UnsignedInteger(txt_writeMask.getText()));
					writemaskDialog.setNodeClass(
							((NodeEditorPart) callback).getEditorInput().getNode().getNode().getNodeClass());
					writemaskDialog.setMask(new UnsignedInteger(text.getText()));
					int open = writemaskDialog.open();
					if (open == Dialog.OK) {
						UnsignedInteger writeMask = AttributeWriteMask.getMask(writemaskDialog.getWritemask());
						text.setText("" + writeMask.intValue());
						if (!oldval.equals(text.getText())) {
							((NodeEditorPart) callback).setDirty(true);
							log(Status.OK,
									label.getText() + " "
											+ CustomString.getString(
													Activator.getDefault().RESOURCE_BUNDLE, "Toolkit.changeFrom.text")
											+ oldval + " "
											+ CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
													"Toolkit.changeTo.text")
											+ text.getText(),
									((NodeEditorPart) callback).getEditorInput().getNode().getNode());
						}
					}
				}
			}
		});
	}

	public void setObjectTypeTreeViewer(TreeViewer treeViewer, final Node node) {
		treeViewer.setContentProvider(new ModelContentProvider(NodeClass.getMask(NodeClass.ObjectType)));
		treeViewer.setLabelProvider(new ModelLabelProvider());
		BrowserModelNode browserNode = new BrowserObjectTypeModelNode(null);
		browserNode.setNode(ServerInstance.getNode(Identifiers.ObjectTypesFolder));
		treeViewer.setComparator(new OPCUABrowserModelViewerComparator());
		treeViewer.setInput(browserNode);
		return;
	}

	public void setVariableTypeTreeViewer(TreeViewer treeViewer) {
		treeViewer.setContentProvider(new ModelContentProvider(NodeClass.getMask(NodeClass.VariableType)));
		treeViewer.setLabelProvider(new ModelLabelProvider());
		BrowserModelNode node = new BrowserModelNode(null);
		node.setNode(ServerInstance.getNode(Identifiers.VariableTypesFolder));
		treeViewer.setComparator(new OPCUABrowserModelViewerComparator());
		treeViewer.setInput(node);
		return;
	}

	public void setDataTypeTreeViewer(TreeViewer treeViewer) {
		treeViewer.setContentProvider(new ModelContentProvider(NodeClass.getMask(NodeClass.DataType)));
		treeViewer.setLabelProvider(new ModelLabelProvider());
		BrowserModelNode node = new BrowserModelNode(null);
		node.setNode(ServerInstance.getNode(Identifiers.DataTypesFolder));
		treeViewer.setComparator(new OPCUABrowserModelViewerComparator());
		treeViewer.setInput(node);
	}

	public void log(int statusType, String message, Node node) {
		// if(this.log == null) {
		// Activator.getDefault().getLog().addLogListener(new CometLogWriter());
		// l.setLog(log);
		// l.initializeLog();
		// }
		// int[] sev = null;
		//
		// try {
		// sev[1] = 22;
		// } catch (Exception ex) {
		// CometLogger.getLogger().error1("test", "error 1", 1, "designer",
		// "", ex);
		// CometLogger.getLogger().error2("test", "error 2", 1, "designer",
		// "", ex);
		// CometLogger.getLogger().error3("test", "error 3", 1, "designer",
		// "", ex);
		// }
		//
		// CometLogger.getLogger().log1("test", "log 1", 1, "designer", "");
		// CometLogger.getLogger().log2("test", "log 2", 1, "designer", "");
		// CometLogger.getLogger().log3("test", "log 3", 1, "designer", "");
		/**
		 * CometLogger.getLogger().log1(node.getBrowseName().getName() + " (" +
		 * node.getNodeId() + "): " + message, CometModuls.STR_LOG, CometModuls.INT_LOG,
		 * Activator.getDefault().getBundle().getSymbolicName(),
		 * Activator.getDefault().getBundle().getVersion().getQualifier());
		 * CometLogger.getLogger().log2(node.getBrowseName().getName() + " (" +
		 * node.getNodeId() + "): " + message, CometModuls.STR_LOG, CometModuls.INT_LOG,
		 * Activator.getDefault().getBundle().getSymbolicName(),
		 * Activator.getDefault().getBundle().getVersion().getQualifier());
		 * CometLogger.getLogger().log3(node.getBrowseName().getName() + " (" +
		 * node.getNodeId() + "): " + message, CometModuls.STR_LOG, CometModuls.INT_LOG,
		 * Activator.getDefault().getBundle().getSymbolicName(),
		 * Activator.getDefault().getBundle().getVersion().getQualifier());
		 */
	}

	/**
	 * Creates an Int32 textfield with validation support.
	 * 
	 * @param valueRank
	 * 
	 * @param callback
	 * 
	 * @param parent
	 * @return
	 */
	public void setTextArrayDimensions(final ControlDecoration decoration,
			final ArrayDimFieldValidator<UnsignedInteger[]> validator, final String error, final String warning,
			Integer valueRank, UnsignedInteger[] arrayDimension, final NodeEditorPart callback) {

		final Image errorImage = FieldDecorationRegistry.getDefault()
				.getFieldDecoration(FieldDecorationRegistry.DEC_ERROR).getImage();
		final Image warningImage = FieldDecorationRegistry.getDefault()
				.getFieldDecoration(FieldDecorationRegistry.DEC_WARNING).getImage();
		final Text element = (Text) decoration.getControl();
		validator.setTextField(element);
		final ValueRanks rank = ValueRanks.getValueRanks(valueRank);
		validator.setValueRank(rank);

		final ArrayDimStringConverter converter = new ArrayDimStringConverter();
		element.setText(converter.convertToString(arrayDimension));
		element.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if (callback != null) {
					if (callback instanceof NodeEditorPart) {
						((NodeEditorPart) callback).setDirty(true);
					}
				}

				UnsignedInteger[] value = converter.convertFromString(element.getText());
				if (!validator.isValid(value)) {
					decoration.setImage(errorImage);
					decoration.setDescriptionText(error);
				} else if (validator.warningExist(value)) {
					decoration.setImage(warningImage);
					decoration.setDescriptionText(error);
				} else {
					decoration.setImage(null);
					decoration.setDescriptionText("");
				}
			}
		});
		element.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				ValueRanks ranks = validator.getValueRanks();
				switch (ranks) {
				case ScalarOrOneDimension:
				case Any:
				case Scalar:
				case OneOrMoreDimensions:
					break;
				case OneDimension:
				case TwoDimensions:
				case ThreeDimensions:
				case FourDimensions:
				case FiveDimensions:
					UnsignedInteger[] value = converter.convertFromString(element.getText());
					ArrayDimensionAttributeDialog dialog = new ArrayDimensionAttributeDialog(element.getShell(), ranks,
							value, true);
					int ok = dialog.open();
					if (ok == Dialog.OK) {
						UnsignedInteger[] dimensions = dialog.getArrayDimension();
						element.setText(converter.convertToString(dimensions));
						if (!validator.isValid(dimensions)) {
							decoration.setImage(errorImage);
							decoration.setDescriptionText(error);
						} else if (validator.warningExist(dimensions)) {
							decoration.setImage(warningImage);
							decoration.setDescriptionText(error);
						} else {
							decoration.setImage(null);
							decoration.setDescriptionText("");
						}
					}
					break;
				}
			}
		});
		decoration.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				super.widgetSelected(e);
			}
		});
		return;
	}

	public static Object createDefaultValue(BuiltinType valueType, ValueRanks valueRank) {

		NamespaceTable nsTable = ServerInstance.getInstance().getServerInstance().getNamespaceUris();

		Object val = null;
		switch (valueType) {
		case Boolean: // Boolean
			// fist check if we had already an array in our value
			if (valueRank == ValueRanks.TwoDimensions) { // two dimensions
				val = new Boolean[1][1];
				((Boolean[][]) val)[0][0] = false;
			} else if (valueRank == ValueRanks.OneDimension) {
				val = new Boolean[1];
				((Boolean[]) val)[0] = false;
			} else if (valueRank == ValueRanks.Scalar) {
				val = new Boolean(false);
			}
			return val;
		case SByte: // SBYTE
			if (valueRank == ValueRanks.TwoDimensions) { // two dimensions
				val = new Byte[1][1];
				((Byte[][]) val)[0][0] = (byte) 0;
			} else if (valueRank == ValueRanks.OneDimension) {
				val = new Byte[1];
				((Byte[]) val)[0] = (byte) 0;
			} else if (valueRank == ValueRanks.Scalar) {
				val = new Byte((byte) 0);
			}
			return val;
		case Byte: // SBYTE
			if (valueRank == ValueRanks.TwoDimensions) { // two dimensions
				val = new UnsignedByte[1][1];
				((UnsignedByte[][]) val)[0][0] = new UnsignedByte(0);
			} else if (valueRank == ValueRanks.OneDimension) {
				val = new UnsignedByte[1];
				((UnsignedByte[]) val)[0] = new UnsignedByte(0);
			} else if (valueRank == ValueRanks.Scalar) {
				val = new UnsignedByte(0);
			}
			return val;
		case Int16: // INT16
			if (valueRank == ValueRanks.TwoDimensions) { // two dimensions
				val = new Short[1][1];
				((Short[][]) val)[0][0] = (short) 0;
			} else if (valueRank == ValueRanks.OneDimension) {
				val = new Short[1];
				((Short[]) val)[0] = (short) 0;
			} else if (valueRank == ValueRanks.Scalar) {
				val = new Short((short) 0);
			}
			return val;
		case UInt16: // UINT16
			if (valueRank == ValueRanks.TwoDimensions) { // two dimensions
				val = new UnsignedShort[1][1];
				((UnsignedShort[][]) val)[0][0] = new UnsignedShort(0);
			} else if (valueRank == ValueRanks.OneDimension) {
				val = new UnsignedShort[1];
				((UnsignedShort[]) val)[0] = new UnsignedShort(0);
			} else if (valueRank == ValueRanks.Scalar) {
				val = new UnsignedShort(0);
			}
			return val;
		case Int32: // INT32
			if (valueRank == ValueRanks.TwoDimensions) { // two dimensions
				val = new Integer[1][1];
				((Integer[][]) val)[0][0] = 0;
			} else if (valueRank == ValueRanks.OneDimension) {
				val = new Integer[1];
				((Integer[]) val)[0] = 0;
			} else if (valueRank == ValueRanks.Scalar) {
				val = new Integer(0);
			}
			return val;
		case UInt32: // UINT32
			if (valueRank == ValueRanks.TwoDimensions) { // two dimensions
				val = new UnsignedInteger[1][1];
				((UnsignedInteger[][]) val)[0][0] = new UnsignedInteger(0);
			} else if (valueRank == ValueRanks.OneDimension) {
				val = new UnsignedInteger[1];
				((UnsignedInteger[]) val)[0] = new UnsignedInteger(0);
			} else if (valueRank == ValueRanks.Scalar) {
				val = new UnsignedInteger(0);
			}
			return val;
		case Int64: // INT64
			if (valueRank == ValueRanks.TwoDimensions) { // two dimensions
				val = new Long[1][1];
				((Long[][]) val)[0][0] = (long) 0;
			} else if (valueRank == ValueRanks.OneDimension) {
				val = new Long[1];
				((Long[]) val)[0] = (long) 0;
			} else if (valueRank == ValueRanks.Scalar) {
				val = new Long(0);
			}
			return val;
		case UInt64: // UINT64
			if (valueRank == ValueRanks.TwoDimensions) { // two dimensions
				val = new UnsignedLong[1][1];
				((UnsignedLong[][]) val)[0][0] = new UnsignedLong(0);
			} else if (valueRank == ValueRanks.OneDimension) {
				val = new UnsignedLong[1];
				((UnsignedLong[]) val)[0] = new UnsignedLong(0);
			} else if (valueRank == ValueRanks.Scalar) {
				val = new UnsignedLong(0);
			}
			return val;
		case Float: // Float
			if (valueRank == ValueRanks.TwoDimensions) { // two dimensions
				val = new Float[1][1];
				((Float[][]) val)[0][0] = (float) 0.0;
			} else if (valueRank == ValueRanks.OneDimension) {
				val = new Float[1];
				((Float[]) val)[0] = (float) 0.0;
			} else if (valueRank == ValueRanks.Scalar) {
				val = new Float(0);
			}
			return val;
		case Double: // Double
			if (valueRank == ValueRanks.TwoDimensions) { // two dimensions
				val = new Double[1][1];
				((Double[][]) val)[0][0] = 0.0;
			} else if (valueRank == ValueRanks.OneDimension) {
				val = new Double[1];
				((Double[]) val)[0] = 0.0;
			} else if (valueRank == ValueRanks.Scalar) {
				val = new Double(0);
			}
			return val;
		case String: // String
			if (valueRank == ValueRanks.TwoDimensions) { // two dimensions
				val = new String[1][1];
				((String[][]) val)[0][0] = "";
			} else if (valueRank == ValueRanks.OneDimension) {
				val = new String[1];
				((String[]) val)[0] = "";
			} else if (valueRank == ValueRanks.Scalar) {
				val = "";
			}
			return val;
		case DateTime: // DateTime
			if (valueRank == ValueRanks.TwoDimensions) { // two dimensions
				val = new DateTime[1][1];
				((DateTime[][]) val)[0][0] = DateTime.currentTime();
			} else if (valueRank == ValueRanks.OneDimension) {
				val = new DateTime[1];
				((DateTime[]) val)[0] = DateTime.currentTime();
			} else if (valueRank == ValueRanks.Scalar) {
				val = DateTime.currentTime();
			}
			return val;
		case Guid: // Guid
			if (valueRank == ValueRanks.TwoDimensions) { // two dimensions
				val = new UUID[1][1];
				((UUID[][]) val)[0][0] = UUID.randomUUID();
			} else if (valueRank == ValueRanks.OneDimension) {
				val = new UUID[1];
				((UUID[]) val)[0] = UUID.randomUUID();
			} else if (valueRank == ValueRanks.Scalar) {
				val = UUID.randomUUID();
			}
			return val;
		case ByteString: // ByteString
			if (valueRank == ValueRanks.TwoDimensions) { // two dimensions
				val = new byte[1][1][1];
				((byte[][][]) val)[0][0][0] = (byte) 0;
			} else if (valueRank == ValueRanks.OneDimension) {
				val = new byte[1][1];
				((byte[][]) val)[0][0] = (byte) 0;
			} else if (valueRank == ValueRanks.Scalar) {
				val = new byte[1];
				((byte[]) val)[0] = (byte) 0;
			}
			return val;
		case XmlElement: // XMLElement
			if (valueRank == ValueRanks.TwoDimensions) { // two dimensions
				val = new XmlElement[1][1];
				((XmlElement[][]) val)[0][0] = new XmlElement("");
			} else if (valueRank == ValueRanks.OneDimension) {
				val = new XmlElement[1];
				((XmlElement[]) val)[0] = new XmlElement("");
			} else if (valueRank == ValueRanks.Scalar) {
				val = new XmlElement(new byte[0]);
			}
			return val;
		case NodeId: // NodeId
			if (valueRank == ValueRanks.TwoDimensions) { // two dimensions
				val = new NodeId[1][1];
				((NodeId[][]) val)[0][0] = new NodeId(0, 0);
			} else if (valueRank == ValueRanks.OneDimension) {
				val = new NodeId[1];
				((NodeId[]) val)[0] = new NodeId(0, 0);
			} else if (valueRank == ValueRanks.Scalar) {
				val = NodeId.NULL;
			}
			return val;
		case ExpandedNodeId: // ExpandedNodeId
			if (valueRank == ValueRanks.TwoDimensions) { // two dimensions
				val = new ExpandedNodeId[1][1];
				((ExpandedNodeId[][]) val)[0][0] = new ExpandedNodeId(new UnsignedInteger(), 0, 0);
			} else if (valueRank == ValueRanks.OneDimension) {
				val = new ExpandedNodeId[1];
				((ExpandedNodeId[]) val)[0] = new ExpandedNodeId(new UnsignedInteger(), 0, 0);
			} else if (valueRank == ValueRanks.Scalar) {
				val = new UnsignedInteger(0);
			}
			return val;
		case StatusCode: // StatusCode
			if (valueRank == ValueRanks.TwoDimensions) { // two dimensions
				val = new StatusCode[1][1];
				((StatusCode[][]) val)[0][0] = StatusCode.GOOD;
			} else if (valueRank == ValueRanks.OneDimension) {
				val = new StatusCode[1];
				((StatusCode[]) val)[0] = StatusCode.GOOD;
			} else if (valueRank == ValueRanks.Scalar) {
				val = StatusCode.GOOD;
			}
			return val;
		case QualifiedName: // QualifiedName
			if (valueRank == ValueRanks.TwoDimensions) { // two dimensions
				val = new QualifiedName[1][1];
				((QualifiedName[][]) val)[0][0] = new QualifiedName("");
			} else if (valueRank == ValueRanks.OneDimension) {
				val = new QualifiedName[1];
				((QualifiedName[]) val)[0] = new QualifiedName("");
			} else if (valueRank == ValueRanks.Scalar) {
				val = new QualifiedName("");
			}
			return val;
		case LocalizedText: // LocalizedText
			if (valueRank == ValueRanks.TwoDimensions) { // two dimensions
				val = new LocalizedText[1][1];
				((LocalizedText[][]) val)[0][0] = new LocalizedText("", "");
			} else if (valueRank == ValueRanks.OneDimension) {
				val = new LocalizedText[1];
				((LocalizedText[]) val)[0] = new LocalizedText("", "");
			} else if (valueRank == ValueRanks.Scalar) {
				val = new LocalizedText("", "");
			}
			return val;
		case ExtensionObject: // ExtensionObject
			if (valueRank == ValueRanks.TwoDimensions) { // two dimensions
				val = new ExtensionObject[1][1];
				((ExtensionObject[][]) val)[0][0] = new ExtensionObject(
						new ExpandedNodeId(nsTable.getUri(Identifiers.Structure.getNamespaceIndex()),
								Identifiers.Structure.getValue(), nsTable));
			} else if (valueRank == ValueRanks.OneDimension) {
				val = new ExtensionObject[1];
				((ExtensionObject[]) val)[0] = new ExtensionObject(
						new ExpandedNodeId(Identifiers.Structure));
			} else if (valueRank == ValueRanks.Scalar) {
				val = new ExtensionObject(ExpandedNodeId.NULL);
			}
			return val;
		case DataValue: // DataValue
			if (valueRank == ValueRanks.TwoDimensions) { // two dimensions
				val = new DataValue[1][1];
				((DataValue[][]) val)[0][0] = new DataValue();
			} else if (valueRank == ValueRanks.OneDimension) {
				val = new DataValue[1];
				((DataValue[]) val)[0] = new DataValue();
			} else if (valueRank == ValueRanks.Scalar) {
				val = new DataValue();
			}
			return val;
		case Variant: // Variant
			if (valueRank == ValueRanks.TwoDimensions) { // two dimensions
				val = new Variant[1][1];
				((Variant[][]) val)[0][0] = new Variant(null);
			} else if (valueRank == ValueRanks.OneDimension) {
				val = new Variant[1];
				((Variant[]) val)[0] = new Variant(null);
			} else if (valueRank == ValueRanks.Scalar) {
				val = Variant.NULL;
			}
			return val;
		case DiagnosticInfo: // DiagnosticInfo
			if (valueRank == ValueRanks.TwoDimensions) { // two dimensions
				val = new DiagnosticInfo[1][1];
				((DiagnosticInfo[][]) val)[0][0] = new DiagnosticInfo();
			} else if (valueRank == ValueRanks.OneDimension) {
				val = new DiagnosticInfo[1];
				((DiagnosticInfo[]) val)[0] = new DiagnosticInfo();
			} else if (valueRank == ValueRanks.Scalar) {
				val = new DiagnosticInfo();
			}
			return val;
		case Number: // Number
			if (valueRank == ValueRanks.TwoDimensions) { // two dimensions
				val = new Number[1][1];
				((Number[][]) val)[0][0] = 0;
			} else if (valueRank == ValueRanks.OneDimension) {
				val = new Number[1];
				((Number[]) val)[0] = 0;
			} else if (valueRank == ValueRanks.Scalar) {
				val = 0;
			}
			return val;
		case Integer: // Integer
			if (valueRank == ValueRanks.TwoDimensions) { // two dimensions
				val = new Integer[1][1];
				((Integer[][]) val)[0][0] = 0;
			} else if (valueRank == ValueRanks.OneDimension) {
				val = new Integer[1];
				((Integer[]) val)[0] = 0;
			} else if (valueRank == ValueRanks.Scalar) {
				val = 0;
			}
			return val;
		case UInteger: // UInteger
			if (valueRank == ValueRanks.TwoDimensions) { // two dimensions
				val = new UnsignedInteger[1][1];
				((UnsignedInteger[][]) val)[0][0] = new UnsignedInteger(0);
			} else if (valueRank == ValueRanks.OneDimension) {
				val = new UnsignedInteger[1];
				((UnsignedInteger[]) val)[0] = new UnsignedInteger(0);
			} else if (valueRank == ValueRanks.Scalar) {
				val = new UnsignedInteger(0);
			}
			return val;
		case Enumeration: // Enumeration
			if (valueRank == ValueRanks.TwoDimensions) { // two dimensions
				val = new Enumeration[1][1];
				((Enumeration[][]) val)[0][0] = null;
			} else if (valueRank == ValueRanks.OneDimension) {
				val = new Enumeration[1];
				((Enumeration[]) val)[0] = null;
			} else if (valueRank == ValueRanks.Scalar) {
				val = 0;
			}
			return val;
		}
		return val;
	}
}
