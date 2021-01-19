package com.bichler.astudio.opcua.opcmodeler.dialogs;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.StatusDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.wb.swt.SWTResourceManager;
import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.DateTime;
import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.QualifiedName;
import org.opcfoundation.ua.builtintypes.UnsignedByte;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.core.AccessLevel;
import org.opcfoundation.ua.core.AttributeWriteMask;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.NodeAttributes;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.core.ReferenceNode;
import org.opcfoundation.ua.core.VariableAttributes;

import com.bichler.astudio.utils.internationalization.CustomString;
import com.bichler.astudio.utils.ui.swt.CheckBoxButton;
import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.core.user.util.UserUtils;
import com.bichler.astudio.opcua.modeller.controls.CometCombo;
import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.opcua.opcmodeler.commands.DialogResult;
import com.bichler.astudio.opcua.opcmodeler.editor.node.DesignerFormToolkit;
import com.bichler.astudio.opcua.opcmodeler.editor.node.validation.converters.ArrayDimStringConverter;
import com.bichler.astudio.opcua.opcmodeler.editor.node.validation.converters.VariantStringConverter;
import com.bichler.astudio.opcua.opcmodeler.editor.node.validation.errors.ArrayDimQuickFixProvider;
import com.bichler.astudio.opcua.opcmodeler.editor.node.validation.errors.NodeIdTextFieldQuickFixProvider;
import com.bichler.astudio.opcua.opcmodeler.editor.node.validation.fields.ArrayDimFieldValidator;
import com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.nodes.BrowserVariableTypeInternalModelNode;
import com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.nodes.BrowserVariableTypeModelNode;
import com.richclientgui.toolbox.validation.ValidatingField;

import opc.sdk.core.enums.BuiltinType;
import opc.sdk.core.enums.ValueRanks;
import opc.sdk.core.node.Node;
import opc.sdk.core.node.NodeIdUtil;
import opc.sdk.core.node.UAVariableTypeNode;
import opc.sdk.core.types.TypeInfo;

public class CreateVariableDialog extends StatusDialog {
	// designer form creators
	private DesignerFormToolkit controllCreationToolkit = null;
	private FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	// dialog result, for creating the node. used after okPressed()
	private DialogResult result = null;
	// variabletype tree viewer
	private TreeViewer variableTypeTreeViewer = null;
	// parent node
	private Node selectedParent = null;
	// reference table viewer
	private TableViewer referencetableViewer = null;
	// browsename attribute
	private ValidatingField<String> txt_browseName = null;
	// description attribute
	private ValidatingField<String> txt_description = null;
	// displayname attribute
	private ValidatingField<String> txt_displayName = null;
	// minimumsampling interval attribute
	private ValidatingField<Double> txt_minimumSamplingInterval = null;
	// arraydimension attribute
	private ValidatingField<UnsignedInteger[]> txt_arrayDimension = null;
	// nodeid attributes
	private ValidatingField<String> txt_nodeId = null;
	// writemask attribute
	private ValidatingField<UnsignedInteger> txt_writeMask = null;
	// userwritemask attribute
	private ValidatingField<UnsignedInteger> txt_userWriteMask = null;
	// value attribute
	private ValidatingField<Variant> txt_value = null;
	// access level attribute
	private CheckBoxButton[] combo_accessLevel = null;
	// user access level attribute
	private CheckBoxButton[] combo_userAccessLevel = null;;
	// datatype attribute
	private CometCombo combo_dataType = null;
	// valuetype attribute
	private CometCombo combo_valueType = null;
	// valuerank attribute
	private CometCombo combo_valueRank = null;
	// historizing attribute
	private CometCombo combo_historizing = null;
	// namespace index
	private ValidatingField<Integer> txt_namespaceBrowseName = null;
	// locale description
	private ValidatingField<Locale> combo_localeDescription = null;
	// locale displayname
	private ValidatingField<Locale> combo_localeDisplayname = null;
	// combo nodeid
	private CometCombo combo_nodeId = null;
	// error label
	// private Label lb_error;
	// array dimension validator
	private ArrayDimFieldValidator<UnsignedInteger[]> arrayDimensionValidator = null;
	// array dimension quickfix
	private ArrayDimQuickFixProvider<UnsignedInteger[]> arrayDimensionQuickFix = null;
	// variabletype
	private UAVariableTypeNode variableType = null;
	// current valuerank
	private ValueRanks valueRank = ValueRanks.Scalar;
	// current valuetype
	private BuiltinType valueType = BuiltinType.Null;
	// current value
	private DataValue value = null;
	// converter for values
	private VariantStringConverter txt_value_converter = null;
	// cache for values
	private Map<ValueRanks, Map<NodeId, Variant>> cache_value = new HashMap<>();
	// cache for array dimensions
	private Map<ValueRanks, UnsignedInteger[]> cache_arraydimension = new HashMap<>();

	public CreateVariableDialog(Shell parentShell, Node selectedNode) {
		super(parentShell);
		this.selectedParent = selectedNode;
		this.controllCreationToolkit = new DesignerFormToolkit();
		this.value = new DataValue();
	}

	@Override
	public void create() {
		super.create();
		// initialize status
		initDatatypeSelection();
	}

	@Override
	protected boolean isResizable() {
		return true;
	}

	/**
	 * Configures the Shell.
	 * 
	 * @param NewShell Shell to configure.
	 */
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
				"NodeEditorVariablePart.frm_mainForm.text"));
		// Point size = newShell.getSize();
		// size.y = 850;
		// newShell.setSize(size);
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		// parent.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		super.createButtonsForButtonBar(parent);
		validate();
	}

	@Override
	protected Control createDialogArea(final Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);
		composite.setLayout(new FillLayout(SWT.HORIZONTAL));
		Composite borderComposite = new Composite(composite, SWT.BORDER);
		borderComposite.setLayout(new FillLayout(SWT.HORIZONTAL));
		ScrolledForm scrolledForm = formToolkit.createScrolledForm(borderComposite);
		scrolledForm.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		scrolledForm.getBody().setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		formToolkit.paintBordersFor(scrolledForm);
		scrolledForm.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
				"NodeEditorVariablePart.frm_mainForm.text"));
		scrolledForm.getBody().setLayout(new GridLayout(1, false));
		createSectionBasic(scrolledForm);
		createTypeSection(scrolledForm);
		createExtendedSection(scrolledForm);
		createReferenceSection(scrolledForm);
		
		scrolledForm.reflow(true);
		return composite;
	}

	private void initDatatypeSelection() {
		// select first item
		TreeItem[] items = this.variableTypeTreeViewer.getTree().getItems();
		if (items != null) {
			for (TreeItem item : items) {
				this.variableTypeTreeViewer.getTree().select(item);
				this.variableTypeTreeViewer.getTree().notifyListeners(SWT.Selection, new Event());
				break;
			}
		}
	}

	private void createTypeSection(ScrolledForm scrolledForm) {
		Section section = formToolkit.createSection(scrolledForm.getBody(), Section.TWISTIE | Section.TITLE_BAR);
		section.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		formToolkit.paintBordersFor(section);
		section.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
				"NodeEditorVariableTypePart.lbl_dataType.text"));
		section.setExpanded(true);
		Composite composite = formToolkit.createComposite(section, SWT.NONE);
		formToolkit.paintBordersFor(composite);
		section.setClient(composite);
		composite.setLayout(new GridLayout(3, false));
		// Label * Type
		Label lb_type = this.controllCreationToolkit.createLabel(composite, CustomString
				.getString(Activator.getDefault().RESOURCE_BUNDLE, "NodeEditorVariableTypePart.frm_mainForm.text"));
		GridDataFactory.fillDefaults().span(1, 10).applyTo(lb_type);
		// TreeViewer * Type
		this.variableTypeTreeViewer = this.controllCreationToolkit.createVariableTypeTreeViewer(composite);
		this.variableTypeTreeViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				Object element = ((StructuredSelection) event.getSelection()).getFirstElement();
				if (element instanceof BrowserVariableTypeModelNode) {
					variableType = (UAVariableTypeNode) ((BrowserVariableTypeModelNode) element).getNode();
				} else if (element instanceof BrowserVariableTypeInternalModelNode) {
					variableType = (UAVariableTypeNode) ((BrowserVariableTypeInternalModelNode) element).getNode();
				} else {

					throw new IllegalArgumentException();
				}
				if (variableType != null) {
					// validation is required
					// set combo datatype
					controllCreationToolkit.setComboDataType(combo_dataType, variableType.getNodeId());
					if (!variableType.getIsAbstract()) {
						// select ValueRank
						int index = 0;
						valueRank = ValueRanks.getValueRanks(variableType.getValueRank());

						arrayDimensionQuickFix.setValueRank(valueRank);
						arrayDimensionValidator.setValueRank(valueRank);
						updateValueRankCombo();
						// set ArrayDimension
						if (variableType.getArrayDimensions() != null) {
							if (variableType.getArrayDimensions().length > 0
									&& variableType.getArrayDimensions()[0] == null) {
								txt_arrayDimension.setContents(new UnsignedInteger[0]);
							} else {
								int vvr = variableType.getValueRank();
								if (vvr <= 0) {
									txt_arrayDimension.setContents(new UnsignedInteger[0]);
								} else {
									txt_arrayDimension.setContents(variableType.getArrayDimensions());
								}

							}
						} else {
							txt_arrayDimension.setContents(new UnsignedInteger[0]);
						}
						controllCreationToolkit.setComboValueType(combo_valueType, variableType.getDataType());
						selectComboValueType();
						valueType = BuiltinType.valueOf(combo_valueType.getText());
						setCachedValue(valueRank, valueType, variableType.getValue());
						if (valueType == null) {
							valueType = BuiltinType.Null;
						}
						boolean isCorrectType = NodeClass.ObjectType.equals(selectedParent.getNodeClass())
								|| NodeClass.VariableType.equals(selectedParent.getNodeClass());
						txt_value_converter.setValue(value.getValue());
						txt_value.setContents(value.getValue());
						if (isCorrectType) {
							boolean isProperty = ServerInstance.getInstance().getServerInstance().getTypeTable()
									.isTypeOf(variableType.getNodeId(), Identifiers.PropertyType);
							boolean isVariableType = ServerInstance.getInstance().getServerInstance().getTypeTable()
									.isTypeOf(variableType.getNodeId(), Identifiers.BaseVariableType);
							if (isProperty) {
								ReferenceNode elementToUpdate = (ReferenceNode) referencetableViewer.getElementAt(0);
								elementToUpdate.setReferenceTypeId(Identifiers.HasProperty);
								referencetableViewer.update(elementToUpdate, null);
							} else {
								if (isVariableType) {
									ReferenceNode elementToUpdate = (ReferenceNode) referencetableViewer
											.getElementAt(0);
									elementToUpdate.setReferenceTypeId(Identifiers.HasComponent);
									referencetableViewer.update(elementToUpdate, null);
								}
							}
						}
					}
					validate();
				}
			}

			private void updateValueRankCombo() {
				combo_valueRank.removeAll();
				controllCreationToolkit.setComboValueRank(combo_valueRank, valueRank);
				for (int i = 0; i < combo_valueRank.getItems().length; i++) {
					if (valueRank.name().equals(combo_valueRank.getItem(i))) {
						combo_valueRank.select(i);
						break;
					}
				}
			}
		});
		GridDataFactory.fillDefaults().span(2, 10).align(SWT.FILL, SWT.FILL).grab(true, false).minSize(SWT.DEFAULT, 120)
				.hint(SWT.DEFAULT, 150).applyTo(this.variableTypeTreeViewer.getControl());
	}

	private void createReferenceSection(ScrolledForm scrolledForm) {
		Section section = formToolkit.createSection(scrolledForm.getBody(), Section.TWISTIE | Section.TITLE_BAR);
		section.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		formToolkit.paintBordersFor(section);
		section.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
				"CreateVariableDialog.lbl_references.text"));
		section.setExpanded(true);
		Composite composite = formToolkit.createComposite(section, SWT.NONE);
		formToolkit.paintBordersFor(composite);
		section.setClient(composite);
		composite.setLayout(new GridLayout(1, false));
		// Label * Reference
//		this.controllCreationToolkit.createLabel(composite, CustomString
//				.getString(Activator.getDefault().RESOURCE_BUNDLE, "NodeEditorPart.tblclmn_reference.text"));
		// Table * Reference
		this.referencetableViewer = this.controllCreationToolkit.createVariableReferenceTable(composite,
				this.selectedParent, null);
		/** we have to check if we create an object type */
		if (NodeClass.ObjectType.equals(this.selectedParent.getNodeClass())) {
			this.referencetableViewer.addDoubleClickListener(new IDoubleClickListener() {
				@Override
				public void doubleClick(DoubleClickEvent event) {
					SelectPropertyOfDialog propertyOfDialog = new SelectPropertyOfDialog(getShell());
					propertyOfDialog.setNodeToUpdate(selectedParent);
					int open = propertyOfDialog.open();
					if (open == Dialog.OK) {
						ReferenceNode result = propertyOfDialog.getReference();
						referencetableViewer.setInput(new ReferenceNode[] { result });
					}
				}
			});
		}
	}

	private void createExtendedSection(ScrolledForm scrolledForm) {
		Section section = formToolkit.createSection(scrolledForm.getBody(), Section.TWISTIE | Section.TITLE_BAR);
		section.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		formToolkit.paintBordersFor(section);
		section.setText("Variable Attribute");
		section.setExpanded(true);
		Composite composite = formToolkit.createComposite(section, SWT.NONE);
		formToolkit.paintBordersFor(composite);
		section.setClient(composite);
		composite.setLayout(new GridLayout(3, false));
		// Label * DataType
		this.controllCreationToolkit.createLabel(composite, CustomString
				.getString(Activator.getDefault().RESOURCE_BUNDLE, "NodeEditorDataTypePart.frm_mainForm.text"));
		// Combo * DataType
		this.combo_dataType = this.controllCreationToolkit.createComboDataType(composite);
		GridDataFactory.fillDefaults().span(2, 1).align(SWT.FILL, SWT.CENTER).grab(true, false)
				.applyTo(this.combo_dataType);
		combo_dataType.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Map<NodeId, String> data = (Map<NodeId, String>) combo_dataType.getData();
				String txt = combo_dataType.getText();
				for (Entry<NodeId, String> entry : data.entrySet()) {
					if (txt.equals(entry.getValue())) {
						controllCreationToolkit.setComboValueType(combo_valueType, entry.getKey());
						selectComboValueType();
						break;
					}
				}
			}
		});
		// this.combo_dataType.setEnabled(false);
		// Label * Value Rank
		this.controllCreationToolkit.createLabel(composite, CustomString
				.getString(Activator.getDefault().RESOURCE_BUNDLE, "NodeEditorVariableTypePart.lbl_valueRank.text"));
		// Combo * Value Rank
		this.combo_valueRank = this.controllCreationToolkit.createComboValueRank(composite);
		GridData data = (GridData) this.combo_valueRank.getLayoutData();
		data.grabExcessHorizontalSpace = true;
		data.horizontalAlignment = SWT.FILL;
		// this.combo_valueRank.setEnabled(false);
		this.combo_valueRank.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				selectComboValueRank();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		// Label * Array Dimension
		this.controllCreationToolkit.createLabel(composite, CustomString.getString(
				Activator.getDefault().RESOURCE_BUNDLE, "NodeEditorVariableTypePart.lbl_arrayDimensions.text"));
		// Text * Array Dimension
		this.arrayDimensionQuickFix = new ArrayDimQuickFixProvider<UnsignedInteger[]>(valueRank);
		this.arrayDimensionValidator = new ArrayDimFieldValidator<UnsignedInteger[]>(this.cache_arraydimension);
		this.arrayDimensionValidator.setValueRank(valueRank);
		this.txt_arrayDimension = this.controllCreationToolkit.createTextArrayDimensions(composite,
				this.combo_valueRank, this.arrayDimensionValidator, this.arrayDimensionQuickFix);
		this.arrayDimensionValidator.setArrayDimField(this.txt_arrayDimension);
		((Text) this.txt_arrayDimension.getControl()).addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				validate();
			}
		});
		GridDataFactory.fillDefaults().span(2, 1).align(SWT.FILL, SWT.CENTER).grab(true, false)
				.applyTo(this.txt_arrayDimension.getControl());
		// this.txt_arrayDimension.getControl().setEnabled(false);
		// Label * Value
		this.controllCreationToolkit.createLabel(composite, CustomString
				.getString(Activator.getDefault().RESOURCE_BUNDLE, "NodeEditorVariableTypePart.lbl_value.text"));
		// Combo * ValueType
		this.combo_valueType = this.controllCreationToolkit.createComboValueType(composite);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER).grab(true, false).applyTo(this.combo_valueType);
		this.combo_valueType.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				selectComboValueType();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
			}
		});
		// Text * Value
		this.txt_value_converter = new VariantStringConverter("", this.combo_valueType, this.combo_valueRank);
		this.txt_value_converter.setIsDialog(true);
		this.txt_value = this.controllCreationToolkit.createTextValue(composite, this.txt_value_converter,
				this.combo_valueType, this.combo_valueRank, (Text) this.txt_arrayDimension.getControl(),
				this.cache_value);
		((Text) this.txt_value.getControl()).addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				switch (valueType) {
				case Enumeration:
					break;
				default:
					validate();
					break;
				}
			}
		});
		((Text) this.txt_value.getControl()).addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				switch (valueType) {
				case Enumeration:
					e.doit = false;
					break;
				default:
					break;
				}
				// }
			}
		});
		((Text) this.txt_value.getControl()).addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				openValueDialogIfNeeded();
			}
		});
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER).grab(true, false)
				.applyTo(this.txt_value.getControl());
		// Label * Access Level
		this.controllCreationToolkit.createLabel(composite, CustomString
				.getString(Activator.getDefault().RESOURCE_BUNDLE, "NodeEditorVariablePart.lbl_accessLevel.text"));
		// Group * Access Level
		this.combo_accessLevel = this.controllCreationToolkit.createGroupAccessLevel(composite);
		// Label * Useraccess Level
		this.controllCreationToolkit.createLabel(composite, CustomString
				.getString(Activator.getDefault().RESOURCE_BUNDLE, "NodeEditorVariablePart.lbl_userAccessLevel.text"));
		// Group * UserAccess Level
		this.combo_userAccessLevel = this.controllCreationToolkit.createGroupAccessLevel(composite);
		// data = combo_userAccessLevel.
		// Label * Minimumsamplinginterval
		this.controllCreationToolkit.createLabel(composite, CustomString
				.getString(Activator.getDefault().RESOURCE_BUNDLE, "CreateVariableDialog.lbl_minSamplInt.text"));
		this.txt_minimumSamplingInterval = this.controllCreationToolkit.createTextDouble(composite, new Double(1000.0));
		((Text) this.txt_minimumSamplingInterval.getControl()).addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				validate();
			}
		});
		GridDataFactory.fillDefaults().span(2, 1).align(SWT.FILL, SWT.CENTER).grab(true, false)
				.applyTo(this.txt_minimumSamplingInterval.getControl());
		// Label * Historizing
		this.controllCreationToolkit.createLabel(composite, CustomString
				.getString(Activator.getDefault().RESOURCE_BUNDLE, "CreateVariableDialog.btn_historizing.text"));

		boolean adminRights = UserUtils.testUserRights(1);
		if (adminRights) {
			// Combo * Historizing
			this.combo_historizing = this.controllCreationToolkit.createComboBoolean(composite);
			// select FALSE
			this.combo_historizing.select(1);
			GridDataFactory.fillDefaults().span(2, 1).align(SWT.FILL, SWT.CENTER).grab(true, false)
					.applyTo(this.combo_historizing);
		}
		this.arrayDimensionValidator.setValueField(this.txt_value);
	}

	/**
	 * Gets the current cached value in dialog
	 * 
	 * @param valueRank
	 * @param valueType
	 * @return
	 */
	private DataValue getCachedValue(ValueRanks valueRank, BuiltinType valueType) {
		Map<NodeId, Variant> data = this.cache_value.get(valueRank);
		if (data == null) {
			data = new HashMap<>();
			data.put(valueType.getBuildinTypeId(), Variant.NULL);
			this.cache_value.put(valueRank, data);
		}
		Variant variant = data.get(valueType.getBuildinTypeId());
		return new DataValue(variant);
	}

	private UnsignedInteger[] getCachedArrayDimension(ValueRanks valueRank) {
		UnsignedInteger[] arrayDim = null;
		switch (valueRank) {
		case Any:
		case OneOrMoreDimensions:
		case Scalar:
		case ScalarOrOneDimension:
			break;
		default:
			arrayDim = this.cache_arraydimension.get(valueRank);
			break;
		}
		return arrayDim;
	}

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
		this.value.setValue(variant);
	}

	private void createSectionBasic(ScrolledForm scrolledForm) {
		Section section = formToolkit.createSection(scrolledForm.getBody(), Section.TWISTIE | Section.TITLE_BAR);
		section.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		formToolkit.paintBordersFor(section);
		section.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "form.opc.attribute"));
		section.setExpanded(true);
		Composite composite = formToolkit.createComposite(section, SWT.NONE);
		formToolkit.paintBordersFor(composite);
		section.setClient(composite);
		composite.setLayout(new GridLayout(3, false));
		// Label * Browsename
		this.controllCreationToolkit.createLabel(composite,
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "CREATEVARIABLEDIALOG_"));
		// Text * NamespaceIndex
		this.txt_namespaceBrowseName = this.controllCreationToolkit.createTextInt32(composite, 0);
		((Text) this.txt_namespaceBrowseName.getControl()).addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				validate();
			}
		});
		// Text * Browsename
		this.txt_browseName = this.controllCreationToolkit.createTextBrowseName(composite);
		((Text) this.txt_browseName.getControl()).addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				validate();
				if (true) {
					String bn = txt_browseName.getContents();
					txt_description.setContents(bn);
					txt_displayName.setContents(bn);
				}
			}
		});
		GridData data = (GridData) this.txt_browseName.getControl().getLayoutData();
		data.grabExcessHorizontalSpace = true;
		data.horizontalAlignment = SWT.FILL;
		// Label * Description
		this.controllCreationToolkit.createLabel(composite, CustomString
				.getString(Activator.getDefault().RESOURCE_BUNDLE, "CreateVariableDialog.lbl_description.text"));
		// Combo * Description
		this.combo_localeDescription = this.controllCreationToolkit.createComboLocale(composite, null);
		// Text * Description
		this.txt_description = this.controllCreationToolkit.createTextString(composite);
		((Text) this.txt_description.getControl()).addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				validate();
			}
		});
		GridDataFactory.fillDefaults().span(1, 1).grab(true, false).align(SWT.FILL, SWT.CENTER)
				.applyTo(this.txt_description.getControl());
		// Label * Displayname
		this.controllCreationToolkit.createLabel(composite, CustomString
				.getString(Activator.getDefault().RESOURCE_BUNDLE, "CreateVariableDialog.lbl_displayName.text"));
		// Combo * Displayname
		this.combo_localeDisplayname = this.controllCreationToolkit.createComboLocale(composite, null);
		// Text * Displayname
		this.txt_displayName = this.controllCreationToolkit.createTextString(composite);
		((Text) this.txt_displayName.getControl()).addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				validate();
			}
		});
		GridDataFactory.fillDefaults().span(1, 1).grab(true, false).align(SWT.FILL, SWT.CENTER)
				.applyTo(this.txt_description.getControl());
		// Label * NodeId
		this.controllCreationToolkit.createLabel(composite,
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "CreateVariableDialog.lbl_nodeId.text"));
		// Combo * NodeId
		this.combo_nodeId = this.controllCreationToolkit.createComboNodeId(composite,
				this.selectedParent.getNodeId().getNamespaceIndex());
		this.combo_nodeId.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				txt_nodeId.setContents("");
				new NodeIdTextFieldQuickFixProvider<NodeId>(combo_nodeId).doQuickFix(txt_nodeId);
				txt_nodeId.validate();
			}
		});
		// Text * NodeId
		this.txt_nodeId = this.controllCreationToolkit.createTextNodeId(composite, this.combo_nodeId, null);
		((Text) this.txt_nodeId.getControl()).addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				validate();
			}
		});
		data = (GridData) this.txt_nodeId.getControl().getLayoutData();
		data.grabExcessHorizontalSpace = true;
		data.horizontalAlignment = SWT.FILL;
		// Label * WriteMask
		this.controllCreationToolkit.createLabel(composite, CustomString
				.getString(Activator.getDefault().RESOURCE_BUNDLE, "CreateVariableDialog.lbl_writeMask.text"));
		// Text * WriteMask
		this.txt_writeMask = this.controllCreationToolkit.createTextUnsignedInteger(composite, new UnsignedInteger());
		((Text) this.txt_writeMask.getControl()).addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				validate();
			}
		});
		((Text) this.txt_writeMask.getControl()).addKeyListener(new KeyListener() {
			@Override
			public void keyReleased(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
				e.doit = false;
			}
		});
		((Text) this.txt_writeMask.getControl()).addMouseListener(new MouseListener() {
			@Override
			public void mouseUp(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
				WriteMaskDialog writemaskDialog = new WriteMaskDialog(getShell(), NodeClass.Variable,
						txt_writeMask.getContents());
				int open = writemaskDialog.open();
				if (open == Dialog.OK) {
					UnsignedInteger writeMask = AttributeWriteMask.getMask(writemaskDialog.getWritemask());
					txt_writeMask.setContents(writeMask);
				}
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}
		});
		GridDataFactory.fillDefaults().span(2, 1).grab(true, false).applyTo(this.txt_writeMask.getControl());
		// Label * UserWriteMask
		this.controllCreationToolkit.createLabel(composite, CustomString
				.getString(Activator.getDefault().RESOURCE_BUNDLE, "CreateVariableDialog.lbl_userWriteMask.text"));
		// Text * UserWriteMask
		this.txt_userWriteMask = this.controllCreationToolkit.createTextUnsignedInteger(composite,
				new UnsignedInteger());
		((Text) this.txt_userWriteMask.getControl()).addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				validate();
			}
		});
		((Text) this.txt_userWriteMask.getControl()).addKeyListener(new KeyListener() {
			@Override
			public void keyReleased(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
				e.doit = false;
			}
		});
		((Text) this.txt_userWriteMask.getControl()).addMouseListener(new MouseListener() {
			@Override
			public void mouseUp(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
				WriteMaskDialog writemaskDialog = new WriteMaskDialog(getShell(), NodeClass.Variable,
						txt_userWriteMask.getContents());
				int open = writemaskDialog.open();
				if (open == Dialog.OK) {
					UnsignedInteger writeMask = AttributeWriteMask.getMask(writemaskDialog.getWritemask());
					txt_userWriteMask.setContents(writeMask);
				}
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}
		});
		GridDataFactory.fillDefaults().span(2, 1).grab(true, false).applyTo(this.txt_userWriteMask.getControl());
	}

	private void openValueDialogIfNeeded() {
		// first we need to check if array Dimension is correct
		if (!txt_arrayDimension.isValid()) {
			Status status = (Status) getStatus();
			status = new Status(IStatus.ERROR, status.getPlugin(),
					CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "message.error.arraydim"));
			updateStatus(status);
			return;
		}
		/**
		 * Skip null value
		 */
		if (valueType == BuiltinType.Null) {
			// Status status = (Status) getStatus();
			// status = new Status(IStatus.ERROR, status.getPlugin(),
			// "Valuetype is not valid!");
			// updateStatus(status);
			return;
		}
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
		// now we check if we have an scalar
		if (!openSingle && valueRank == ValueRanks.ScalarOrOneDimension) {
			return;
		} else if (!openSingle && valueRank == ValueRanks.Any) {
			return;
		} else if (!openSingle && valueRank == ValueRanks.Scalar) {
			return;
		}
		switch (valueRank) {
		case ScalarOrOneDimension:
		case Any:
		case Scalar:
			switch (valueType) {
			case DateTime:
				DateTimeDialog dialog = new DateTimeDialog(getParentShell());
				int open = dialog.open();
				if (Dialog.OK == open) {
					Calendar dt = dialog.getDateTime();
					DateTime date = new DateTime(dt);
					Variant variant = new Variant(date);
					this.value.setValue(variant);
					this.txt_value_converter.setValue(variant);
					this.txt_value.setContents(variant);
					this.combo_valueType.setData(this.combo_valueType.getText(), variant);
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
		default:
			Text dimensions = (Text) txt_arrayDimension.getControl();
			ArrayDimStringConverter converter = new ArrayDimStringConverter();
			UnsignedInteger[] dims = converter.convertFromString(dimensions.getText());
			// check the value type
			TypeInfo typeInfo = TypeInfo.construct(value,
					ServerInstance.getInstance().getServerInstance().getTypeTable());
			if (value.getValue().getValue() == null || !(typeInfo.getBuiltInsType().equals(valueType))) {
				if (!value.getValue().isEmpty() && typeInfo.getBuiltInsType().equals(BuiltinType.Int32)
						&& valueType.equals(BuiltinType.Enumeration)) {
					// skip enum
				} else {
					Object val = DesignerFormToolkit.createDefaultValue(valueType, valueRank);
					// TODO: CHECK IF NEEDED
					value.setValue(new Variant(val));
				}
			}
			// }
			Map<NodeId, String> data = (Map<NodeId, String>) this.combo_dataType.getData();
			NodeId datatypeId = NodeId.NULL;
			for (Entry<NodeId, String> e : data.entrySet()) {
				if (e.getValue().equals(this.combo_dataType.getText())) {
					datatypeId = e.getKey();
					break;
				}
			}
			// this.variableType.getNodeId();
			NodeValueDialog dialog2 = new NodeValueDialog(getShell(), valueRank, dims, valueType, value, datatypeId,
					this.selectedParent.getNodeId());
			int ok = dialog2.open();
			if (Dialog.OK == ok) {
				// set value
				DataValue newValue = dialog2.getNewValue();
				this.value.setValue(newValue.getValue());
				this.txt_value_converter.setValue(newValue.getValue());
				this.txt_value.setContents(newValue.getValue());
				this.combo_valueType.setData(this.combo_valueType.getText(), newValue.getValue());
			}
			break;
		}
	}

	private void selectComboValueRank() {
		valueRank = ValueRanks.valueOf(combo_valueRank.getText());
		arrayDimensionValidator.setValueRank(valueRank);
		// UnsignedInteger[] arrayDim = getCachedArrayDimension(valueRank);
		// if (arrayDim == null) {
		// arrayDim = new UnsignedInteger[0];
		// }
		// this.txt_arrayDimension.setContents(arrayDim);
		// arrayDimensionValidator.setArrayDimValue(arrayDim);
		txt_arrayDimension.validate();
		arrayDimensionQuickFix.setValueRank(valueRank);
		/** set quick fix */
		value = getCachedValue(valueRank, valueType);
		this.txt_value.setContents(value.getValue());
		validate();
	}

	private void selectComboValueType() {
		valueType = BuiltinType.valueOf(this.combo_valueType.getText());
		this.value = getCachedValue(valueRank, valueType);
		// Variant value = (Variant) this.combo_valueType
		// .getData(this.combo_valueType.getText());
		// this.value.setValue(value);
		this.txt_value_converter.setValue(value.getValue());
		this.txt_value_converter.setDataType(this.combo_valueType.getText());
		Map<NodeId, String> data = (Map<NodeId, String>) this.combo_dataType.getData();
		NodeId datatypeId = NodeId.NULL;
		for (Entry<NodeId, String> e : data.entrySet()) {
			if (e.getValue().equals(this.combo_dataType.getText())) {
				datatypeId = e.getKey();
				break;
			}
		}
		this.txt_value_converter.setDataTypeId(datatypeId);
		this.txt_value.setContents(value.getValue());
		if (value == null) {
			((Text) this.txt_value.getControl()).setText("");
		} else if (value.getValue().isEmpty()) {
			((Text) this.txt_value.getControl()).setText("");
		}
		validate();
	}

	@Override
	protected void okPressed() {
		createResult();
		super.okPressed();
	}

	private void createResult() throws NullPointerException {
		NodeId nodeId = null;
		int namespaceIndex = ServerInstance.getInstance().getServerInstance().getNamespaceUris()
				.getIndex(this.combo_nodeId.getText());
		Object identifier = NodeIdUtil.createIdentifierFromString(this.txt_nodeId.getContents());
		nodeId = NodeIdUtil.createNodeId(namespaceIndex, identifier);
		NodeClass nodeClass = NodeClass.Variable;
		namespaceIndex = ServerInstance.getInstance().getServerInstance().getNamespaceUris()
				.getIndex(combo_nodeId.getText());
		QualifiedName browseName = new QualifiedName(this.txt_namespaceBrowseName.getContents(),
				this.txt_browseName.getContents());
		LocalizedText displayName = new LocalizedText(txt_displayName.getContents(),
				this.combo_localeDisplayname.getContents());
		LocalizedText description = new LocalizedText(txt_description.getContents(),
				this.combo_localeDescription.getContents());
		UnsignedInteger writeMask = new UnsignedInteger(txt_writeMask.getContents());
		UnsignedInteger userWriteMask = new UnsignedInteger(txt_userWriteMask.getContents());
		Node node = new Node(nodeId, nodeClass, browseName, displayName, description, writeMask, userWriteMask, null);
		NodeId dataTypeKey = null;
		for (Entry<NodeId, String> dataType : this.controllCreationToolkit.fetchDataTypes().entrySet()) {
			if (dataType.getValue().equalsIgnoreCase(combo_dataType.getText())) {
				dataTypeKey = dataType.getKey();
				break;
			}
		}
		NodeId dataType = dataTypeKey;
		Integer valueRank = ValueRanks.valueOf(combo_valueRank.getText()).getValue();
		UnsignedInteger[] arrayDimension = txt_arrayDimension.getContents();
		int accessLvl = 0;
		for (CheckBoxButton b : combo_accessLevel) {
			if (b.isChecked()) {
				accessLvl += AccessLevel.valueOf(b.getText()).getValue();
			}
		}
		UnsignedByte accessLevel = new UnsignedByte(accessLvl);
		int userAccessLvl = 0;
		for (CheckBoxButton b : combo_userAccessLevel) {
			if (b.isChecked()) {
				userAccessLvl += AccessLevel.valueOf(b.getText()).getValue();
			}
		}
		UnsignedByte userAccessLevel = new UnsignedByte(userAccessLvl);
		Double minimumSamplingInterval = new Double(txt_minimumSamplingInterval.getContents());
		Boolean historizing = false;
		if (combo_historizing != null) {
			historizing = new Boolean(combo_historizing.getText());
		}
		Variant value = null;
		// did we have an scalar or an array
		if (this.valueRank == ValueRanks.Scalar
				|| (this.valueRank == ValueRanks.ScalarOrOneDimension
						&& ((Text) txt_value.getControl()).getText().compareTo("doubleclick") != 0)
				|| (this.valueRank == ValueRanks.Any
						&& ((Text) txt_value.getControl()).getText().compareTo("doubleclick") != 0)) {
			value = this.txt_value.getContents();
		} else {
			value = this.value.getValue();
		}
		NodeAttributes attributes = new VariableAttributes(null, displayName, description, writeMask, userWriteMask,
				value, dataType, valueRank, arrayDimension, accessLevel, userAccessLevel, minimumSamplingInterval,
				historizing);
		TableItem c = this.referencetableViewer.getTable().getItems()[0];
		ReferenceNode data = (ReferenceNode) c.getData();
		ExpandedNodeId parentNodeId = data.getTargetId();
		NodeId referenceTypeId = data.getReferenceTypeId();
		List<ReferenceNode> additionalReferences = new ArrayList<ReferenceNode>();
		for (int i = 1; i < this.referencetableViewer.getTable().getItemCount(); i++) {
			c = this.referencetableViewer.getTable().getItem(i);
			data = (ReferenceNode) c.getData();
			additionalReferences.add(data);
		}
		DialogResult dialogResult = new DialogResult();
		dialogResult.setNodeResult(node);
		dialogResult.setNodeAttributes(attributes);
		dialogResult.setParentNodeId(parentNodeId);
		dialogResult.setReferenceTypeId(referenceTypeId);
		dialogResult.setType(this.variableType.getNodeId());
		dialogResult
				.setAdditionalReferences(additionalReferences.toArray(new ReferenceNode[additionalReferences.size()]));
		this.result = dialogResult;
	}

	public DialogResult getResult() {
		return this.result;
	}

	private boolean validate() {
		boolean validBrowseName = this.txt_browseName.isValid();
		boolean validDescription = this.txt_description.isValid();
		boolean validDisplayName = this.txt_displayName.isValid();
		boolean validNodeId = this.txt_nodeId.isValid();
		boolean validWriteMask = this.txt_writeMask.isValid();
		boolean validUserWriteMask = this.txt_userWriteMask.isValid();
		boolean validBrowseNameIndex = this.txt_namespaceBrowseName.isValid();
		boolean validValue = this.txt_value.isValid();
		boolean validMinimumSamplingInterval = this.txt_minimumSamplingInterval.isValid();
		boolean validArrayDimension = this.txt_arrayDimension.isValid();
		this.txt_value.validate();
		boolean isAbstractType = false;
		if (this.variableType != null && this.variableType.getIsAbstract()) {
			Status status = (Status) getStatus();
			status = new Status(IStatus.ERROR, status.getPlugin(),
					CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "form.error.variable.abstracttype"));
			updateStatus(status);
			isAbstractType = true;
		} else if (this.variableType != null && !this.variableType.getIsAbstract()) {
			Status status = (Status) getStatus();
			status = new Status(IStatus.OK, status.getPlugin(), "");
			updateStatus(status);
			isAbstractType = false;
		}
		// no valid value
		if (!validValue) {
			// skip empty controls
			if (((Text) this.txt_value.getControl()).getText().isEmpty()) {
				validValue = true;
			}
		}
		// set new value
		else {
			Variant content = this.txt_value.getContents();
			this.value.setValue(content);
		}
		boolean isValid = validBrowseName && validDescription && validDisplayName && validNodeId && validWriteMask
				&& validUserWriteMask && validBrowseNameIndex && validValue && validMinimumSamplingInterval
				&& validArrayDimension;
		if (!isValid || isAbstractType) {
			getButton(OK).setEnabled(false);
		} else {
			getButton(OK).setEnabled(true);
		}
		return isValid;
	}
}
