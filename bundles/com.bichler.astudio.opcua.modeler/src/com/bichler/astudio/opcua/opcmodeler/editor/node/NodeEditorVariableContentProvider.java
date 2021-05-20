package com.bichler.astudio.opcua.opcmodeler.editor.node;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TreeItem;
import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.UnsignedByte;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.common.NamespaceTable;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.AccessLevel;
import org.opcfoundation.ua.core.Attributes;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.ReferenceNode;
import org.opcfoundation.ua.core.WriteValue;

import com.bichler.astudio.utils.internationalization.CustomString;
import com.bichler.astudio.utils.ui.swt.CheckBoxButton;
import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.opcua.modeller.controls.CometCombo;
import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.opcua.opcmodeler.editor.node.validation.converters.VariantStringConverter;
import com.bichler.astudio.opcua.opcmodeler.editor.node.validation.errors.ArrayDimQuickFixProvider;
import com.bichler.astudio.opcua.opcmodeler.editor.node.validation.fields.ArrayDimFieldValidator;
import com.richclientgui.toolbox.validation.ValidatingField;

import opc.sdk.core.enums.BuiltinType;
import opc.sdk.core.enums.ValueRanks;
import opc.sdk.core.node.Node;
import opc.sdk.core.node.UAVariableNode;
import opc.sdk.core.node.UAVariableTypeNode;
import opc.sdk.core.node.VariableNode;
import opc.sdk.core.node.VariableTypeNode;
import opc.sdk.core.types.TypeInfo;

public class NodeEditorVariableContentProvider extends NodeEditorVariableTypeContentProvider {
	/**
	 * variables for variable and variable types use
	 */
	private ValidatingField<Double> txt_minimumSamplingInterval = null;
	private CometCombo combo_dataType = null;
	private CheckBoxButton[] btn_accessLevel = null;
	private CheckBoxButton[] btn_userAccessLevel = null;
	private CheckBoxButton btn_historizing = null;
	private UAVariableTypeNode variableType = null;
	private Map<ValueRanks, UnsignedInteger[]> cache_arraydimension = new HashMap<>();
	private Map<ValueRanks, Map<NodeId, Variant>> cache_value = new HashMap<>();

	// private Node selectedParent = null;
	public NodeEditorVariableContentProvider(NodeEditorPart nodeEditorPart /**
																			 * , Node selectedParent
																			 */
	) {
		super(nodeEditorPart);
		// this.selectedParent = selectedParent;
	}

	protected void createContent(Node node, ScrolledComposite parent) {
		Composite composite = new Composite(parent, SWT.SHADOW_ETCHED_IN);
		parent.setContent(composite);
		GridLayoutFactory.fillDefaults().numColumns(1).applyTo(composite);
		Group nodeGroup = new Group(composite, SWT.NONE);
		/** first create all base node controls */
		createBaseNodeContent(nodeGroup, node);
		// Label * Type
		Label lb_type = this.controllCreationToolkit.createLabel(nodeGroup,
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "editor.variabletype"));
		GridDataFactory.fillDefaults().span(1, 20).applyTo(lb_type);
		// TreeViewer * VariableType
		this.treeViewer = this.controllCreationToolkit.createVariableTypeTreeViewer(nodeGroup);
		GridDataFactory.fillDefaults().span(2, 20).align(SWT.FILL, SWT.FILL).applyTo(this.treeViewer.getControl());
		// Label * DataType
		this.controllCreationToolkit.createLabel(nodeGroup,
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "editor.variabletype"));
		// Combo * DataType
		this.combo_dataType = this.controllCreationToolkit.createComboDataType(nodeGroup);
		GridDataFactory.fillDefaults().span(2, 1).applyTo(this.combo_dataType);
		this.combo_dataType.setEnabled(false);
		// Label * Value Rank
		this.controllCreationToolkit.createLabel(nodeGroup, CustomString
				.getString(Activator.getDefault().RESOURCE_BUNDLE, "CreateVariableDialog.lbl_valueRank.text"));
		// Combo * Value Rank
		this.combo_valueRank = this.controllCreationToolkit.createComboValueRank(nodeGroup);
		this.combo_valueRank.setEnabled(false);
		this.arrayDimensionValidator = new ArrayDimFieldValidator<UnsignedInteger[]>(cache_arraydimension);
		// Label * Array Dimension
		this.controllCreationToolkit.createLabel(nodeGroup, CustomString.getString(
				Activator.getDefault().RESOURCE_BUNDLE, "NodeEditorVariableTypePart.lbl_arrayDimensions.text"));
		// Text * Array Dimension
		this.arrayDimensionQuickFix = new ArrayDimQuickFixProvider<UnsignedInteger[]>(ValueRanks.Any);
		this.txt_arrayDimension = this.controllCreationToolkit.createTextArrayDimensions(nodeGroup,
				this.combo_valueRank, this.arrayDimensionValidator, this.arrayDimensionQuickFix);
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
		// Label * Access Level
		this.controllCreationToolkit.createLabel(nodeGroup, CustomString
				.getString(Activator.getDefault().RESOURCE_BUNDLE, "NodeEditorVariablePart.lbl_accessLevel.text"));
		// Group * Access Level
		this.btn_accessLevel = this.controllCreationToolkit.createGroupAccessLevel(nodeGroup);
		// Label * Useraccess Level
		this.controllCreationToolkit.createLabel(nodeGroup, CustomString
				.getString(Activator.getDefault().RESOURCE_BUNDLE, "NodeEditorVariablePart.lbl_userAccessLevel.text"));
		// Group * UserAccess Level
		this.btn_userAccessLevel = this.controllCreationToolkit.createGroupAccessLevel(nodeGroup);
		// Label * Minimumsamplinginterval
		Label lb_minimumsamplingInterval = this.controllCreationToolkit.createLabel(nodeGroup,
				"MinimumSampling\nInterval");
		lb_minimumsamplingInterval.setLayoutData(this.controllCreationToolkit.gridData3);
		this.txt_minimumSamplingInterval = this.controllCreationToolkit.createTextDouble(nodeGroup, new Double(1000.0));
		GridDataFactory.fillDefaults().span(2, 1).applyTo(this.txt_minimumSamplingInterval.getControl());
		// Label * Historizing
		this.controllCreationToolkit.createLabel(nodeGroup, CustomString
				.getString(Activator.getDefault().RESOURCE_BUNDLE, "CreateVariableDialog.btn_historizing.text"));
		// checkbox * Historizing
		this.btn_historizing = new CheckBoxButton(nodeGroup, SWT.NONE);
		GridDataFactory.fillDefaults().span(2, 1).applyTo(this.btn_historizing);
		// Label * Reference
		this.controllCreationToolkit.createLabel(composite, CustomString
				.getString(Activator.getDefault().RESOURCE_BUNDLE, "CreateVariableDialog.lbl_references.text"));
		// Table * Reference
		this.tableViewer = this.controllCreationToolkit.createVariableReferenceTable(composite, node,
				this.callBackEditor);
		createReferenceTableButtonSection(composite, node);
		createErrorLabel(composite);
		composite.layout();
		composite.setSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT, true));
		if (!isEditable(node)) {
			disableEditableSection(nodeGroup);
		}
	}

	@Override
	protected void setNodeInput(Node node) {
		setBaseNodeInput(node);
		// setNodeTypeInput(node);
		setReferenceTableNodeInput(node);
		setBaseVariableNodeInput(node);
		// NodeId dataTypeId = null;
		NodeId variableTypeId = null;
		/** get data type and variable type of that node */
		if (node instanceof VariableNode) {
			this.dataType = ((VariableNode) node).getDataType();
			// variableTypeId = ((VariableNode) node).getTypeId();
			try {
				variableTypeId = ServerInstance.getInstance().getServerInstance().getNamespaceUris()
						.toNodeId(node.findTarget(Identifiers.HasTypeDefinition, false));
			} catch (ServiceResultException e1) {
				e1.printStackTrace();
			}
		}
		/** get all tree items */
		this.treeViewer.expandAll();
		TreeItem[] items = this.treeViewer.getTree().getItems();
		TreeItem it = null;
		for (TreeItem item : items) {
			it = this.findTreeItem(item, variableTypeId);
			if (it != null) {
				this.variableType = (UAVariableTypeNode) it.getData();
				this.treeViewer.getTree().setSelection(it);
				break;
			}
		}
		this.treeViewer.addDoubleClickListener(new IDoubleClickListener() {
			@Override
			public void doubleClick(DoubleClickEvent event) {
				variableType = (UAVariableTypeNode) ((TreeSelection) event.getSelection()).getFirstElement();
				if (variableType != null) {
					// validation is required
					if (variableType.getIsAbstract()) {
						// Status status = (Status)getStatus();
						// status = new Status(IStatus.ERROR,
						// status.getPlugin(),
						// "Can not create variable from abstract type!");
						// updateStatus(status);
					} else {
						// Status status = (Status)getStatus();
						// status = new Status(IStatus.OK, status.getPlugin(),
						// "");
						// updateStatus(status);
						// remove value from txt field
						// txt_value.setContents(Variant.NULL);
						// combo_valueType
						// select Datatype
						Map<NodeId, String> dataTypes = controllCreationToolkit.fetchDataTypes();
						String dataType = dataTypes.get(variableType.getDataType());
						int index = 0;
						for (int i = 0; i < combo_dataType.getItemCount(); i++) {
							if (dataType.equalsIgnoreCase(combo_dataType.getItem(i))) {
								index = i;
								break;
							}
						}
						combo_dataType.select(index);
						// select ValueRank
						index = 0;
						valueRank = ValueRanks.getValueRanks(variableType.getValueRank());
						switch (valueRank) {
						case ScalarOrOneDimension:
							scalar_or_onedimension_value.setValue(variableType.getValue());
							value = scalar_or_onedimension_value;
							break;
						case Any:
							any_value.setValue(variableType.getValue());
							value = any_value;
							break;
						case Scalar:
							scalar_value.setValue(variableType.getValue());
							value = scalar_value;
							break;
						case OneOrMoreDimensions:
							one_or_more_dimension_value.setValue(variableType.getValue());
							value = one_or_more_dimension_value;
							break;
						case OneDimension:
							onedimension_value.setValue(variableType.getValue());
							value = onedimension_value;
							break;
						case TwoDimensions:
							twodimension_value.setValue(variableType.getValue());
							value = twodimension_value;
							break;
						case ThreeDimensions:
							threedimension_value.setValue(variableType.getValue());
							value = threedimension_value;
							break;
						case FourDimensions:
							fourdimension_value.setValue(variableType.getValue());
							value = fourdimension_value;
							break;
						case FiveDimensions:
							fivedimension_value.setValue(variableType.getValue());
							value = fivedimension_value;
							break;
						default:
							break;
						}
						arrayDimensionQuickFix.setValueRank(valueRank);
						arrayDimensionValidator.setValueRank(valueRank);
						for (int i = 0; i < ValueRanks.values().length; i++) {
							if (ValueRanks.values()[i].equals(valueRank)) {
								index = i;
								break;
							}
						}
						combo_valueRank.select(index);
						// set ArrayDimension
						if (variableType.getArrayDimensions() != null) {
							if (variableType.getArrayDimensions().length > 0
									&& variableType.getArrayDimensions()[0] == null) {
								txt_arrayDimension.setContents(new UnsignedInteger[0]);
							} else {
								txt_arrayDimension.setContents(variableType.getArrayDimensions());
							}
						} else {
							txt_arrayDimension.setContents(new UnsignedInteger[0]);
						}
						// select ValueType
						try {
							BuiltinType valueType = TypeInfo
									.construct(new DataValue(variableType.getValue()),
											ServerInstance.getInstance().getServerInstance().getTypeTable())
									.getBuiltInsType();
							index = 0;
							for (int i = 0; i < combo_valueType.getItems().length; i++) {
								if (combo_valueType.getItem(i).equals(valueType.name())) {
									index = i;
									break;
								}
							}
						} catch (NullPointerException npe) {
							index = 0;
						}
						combo_valueType.select(index);
						/**  */
						boolean isCorrectType = true;
						/**
						 * NodeClass.ObjectType .equals(selectedParent.getNodeClass()) ||
						 * NodeClass.VariableType.equals(selectedParent .getNodeClass());
						 */
						((Text) txt_value.getControl()).setText("");
						if (valueRank != ValueRanks.Scalar) {
							((Text) txt_value.getControl()).setText(
									CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "form.doubleclick"));
							// txt_value.setContents(Variant.NULL);
						} else {
							txt_value.setContents(value.getValue());
						}
						if (isCorrectType) {
							boolean isProperty = ServerInstance.getInstance().getServerInstance().getTypeTable()
									.isTypeOf(variableType.getNodeId(), Identifiers.PropertyType);
							boolean isVariableType = ServerInstance.getInstance().getServerInstance().getTypeTable()
									.isTypeOf(variableType.getNodeId(), Identifiers.BaseVariableType);
							if (isProperty) {
								ReferenceNode elementToUpdate = (ReferenceNode) tableViewer.getElementAt(0);
								elementToUpdate.setReferenceTypeId(Identifiers.HasProperty);
								tableViewer.update(elementToUpdate, null);
							} else {
								if (isVariableType) {
									ReferenceNode elementToUpdate = (ReferenceNode) tableViewer.getElementAt(0);
									elementToUpdate.setReferenceTypeId(Identifiers.HasComponent);
									tableViewer.update(elementToUpdate, null);
								}
							}
						}
					}
				}
			}
		});
		// fill datatype combo box
		Map<NodeId, String> dataTypes = this.controllCreationToolkit.fetchDataTypes();
		String dataType = dataTypes.get(this.dataType);
		int index = 0;
		for (int i = 0; i < this.combo_dataType.getItemCount(); i++) {
			if (dataType.equalsIgnoreCase(this.combo_dataType.getItem(i))) {
				index = i;
				break;
			}
		}
		this.combo_dataType.select(index);
		this.combo_dataType.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				callBackEditor.setDirty(true);
			}
		});
		UnsignedByte accessLevel = ((VariableNode) node).getAccessLevel();
		EnumSet<AccessLevel> setA = AccessLevel.getSet(accessLevel.intValue());
		for (AccessLevel access : setA) {
			for (int i = 0; i < this.btn_accessLevel.length; i++) {
				String data = this.btn_accessLevel[i].getText();
				AccessLevel value = AccessLevel.valueOf(data);
				if (access.equals(value)) {
					this.btn_accessLevel[i].setChecked(true);
				}
			}
		}
		for (int i = 0; i < this.btn_accessLevel.length; i++) {
			CheckBoxButton b = this.btn_accessLevel[i];
			b.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					callBackEditor.setDirty(true);
				}
			});
		}
		UnsignedByte userAccessLevel = ((VariableNode) node).getUserAccessLevel();
		EnumSet<AccessLevel> setUA = AccessLevel.getSet(userAccessLevel.intValue());
		for (AccessLevel access : setUA) {
			for (int i = 0; i < this.btn_userAccessLevel.length; i++) {
				String data = this.btn_userAccessLevel[i].getText();
				AccessLevel value = AccessLevel.valueOf(data);
				if (access.equals(value)) {
					this.btn_userAccessLevel[i].setChecked(true);
				}
			}
		}
		for (int i = 0; i < this.btn_userAccessLevel.length; i++) {
			CheckBoxButton b = this.btn_userAccessLevel[i];
			b.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					callBackEditor.setDirty(true);
				}
			});
		}
		this.btn_historizing.setChecked(((VariableNode) node).getHistorizing());
		this.btn_historizing.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				callBackEditor.setDirty(true);
			}
		});
		Double minimumSamplingInterval = ((VariableNode) node).getMinimumSamplingInterval();
		this.txt_minimumSamplingInterval.setContents(minimumSamplingInterval);
		((Text) this.txt_minimumSamplingInterval.getControl()).addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				callBackEditor.setDirty(true);
			}
		});
	}

	@Override
	public void doSave(Node node) {
		NamespaceTable nsTable = ServerInstance.getInstance().getServerInstance().getNamespaceUris();

		// save the default node attributes
		doSaveNodeBaseAttributes(node);
		// cast the node
		UAVariableNode varNode = (UAVariableNode) node;
		// construct the new changed values
		this.variableType.getNodeId();
		UnsignedByte accessLevel = createAccessLevel();
		UnsignedInteger[] arrayDimensions = createArrayDimension();
		Double minimumSamplingInterval = createMinimumSamplingInterval();
		UnsignedByte userAccessLevel = createtUserAccessLevel();
		Variant value = createValue();
		Integer valueRank = createValueRank();
		varNode.setDataType(this.dataType);
		varNode.setAccessLevel(accessLevel);
		varNode.setArrayDimensions(arrayDimensions);
		varNode.setHistorizing(this.btn_historizing.isChecked());
		varNode.setMinimumSamplingInterval(minimumSamplingInterval);
		varNode.setUserAccessLevel(userAccessLevel);
		varNode.setValueRank(valueRank);
		// varNode.setValue(value)
		try {
			ServerInstance.getInstance().getServerInstance().getMaster().write(
					new WriteValue[] { new WriteValue(node.getNodeId(), Attributes.Value, null, new DataValue(value)) },
					true, new Long[] { -1l }, null);
		} catch (ServiceResultException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/** get the variable type reference and update it */
		ReferenceNode[] nodes = varNode.getReferences();
		for (ReferenceNode n : nodes) {
			if (Identifiers.HasTypeDefinition.equals(n.getReferenceTypeId())) {
				n.setTargetId(new ExpandedNodeId(this.variableType.getNodeId()));
				// int i = 0;
			}
		}
	}

	private Double createMinimumSamplingInterval() {
		Double minimumSamplingInterval = new Double(this.txt_minimumSamplingInterval.getContents());
		return minimumSamplingInterval;
	}

	private UnsignedByte createAccessLevel() {
		List<AccessLevel> accessLevelList = new ArrayList<AccessLevel>();
		for (int i = 0; i < this.btn_accessLevel.length; i++) {
			CheckBoxButton accessLvl = this.btn_accessLevel[i];
			if (accessLvl.isChecked()) {
				String data = btn_accessLevel[i].getText();
				AccessLevel value = AccessLevel.valueOf(data);
				accessLevelList.add(value);
			}
		}
		UnsignedByte mask = AccessLevel.getMask(accessLevelList.toArray(new AccessLevel[accessLevelList.size()]));
		return mask;
	}

	private UnsignedByte createtUserAccessLevel() {
		List<AccessLevel> accessLevelList = new ArrayList<AccessLevel>();
		for (int i = 0; i < this.btn_userAccessLevel.length; i++) {
			CheckBoxButton userAccessLvl = this.btn_userAccessLevel[i];
			if (userAccessLvl.isChecked()) {
				String data = btn_userAccessLevel[i].getText();
				AccessLevel value = AccessLevel.valueOf(data);
				accessLevelList.add(value);
			}
		}
		UnsignedByte mask = AccessLevel.getMask(accessLevelList.toArray(new AccessLevel[accessLevelList.size()]));
		return mask;
	}

	private TreeItem findTreeItem(TreeItem root, NodeId key) {
		if (root.getData() instanceof VariableTypeNode && key.equals(((VariableTypeNode) root.getData()).getNodeId())) {
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

	public boolean valid() {
		if (this.txt_minimumSamplingInterval != null) {
			if (!this.txt_minimumSamplingInterval.isValid()) {
				return false;
			}
		}
		return super.valid();
	}
}
