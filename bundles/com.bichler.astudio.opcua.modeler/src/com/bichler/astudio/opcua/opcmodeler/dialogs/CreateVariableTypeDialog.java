package com.bichler.astudio.opcua.opcmodeler.dialogs;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.StatusDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeSelection;
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
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
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
import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.DateTime;
import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.QualifiedName;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.core.AttributeWriteMask;
import org.opcfoundation.ua.core.NodeAttributes;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.core.ReferenceNode;
import org.opcfoundation.ua.core.VariableTypeAttributes;

import com.bichler.astudio.utils.internationalization.CustomString;
import com.bichler.astudio.utils.ui.swt.CheckBoxButton;
import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.opcua.modeller.controls.CometCombo;
import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.opcua.opcmodeler.commands.DialogResult;
import com.bichler.astudio.opcua.opcmodeler.editor.node.DesignerFormToolkit;
import com.bichler.astudio.opcua.opcmodeler.editor.node.validation.converters.ArrayDimStringConverter;
import com.bichler.astudio.opcua.opcmodeler.editor.node.validation.converters.VariantStringConverter;
import com.bichler.astudio.opcua.opcmodeler.editor.node.validation.errors.ArrayDimQuickFixProvider;
import com.bichler.astudio.opcua.opcmodeler.editor.node.validation.errors.NodeIdTextFieldQuickFixProvider;
import com.bichler.astudio.opcua.opcmodeler.editor.node.validation.fields.ArrayDimFieldValidator;
import com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.nodes.BrowserModelNode;
import com.richclientgui.toolbox.validation.ValidatingField;

import opc.sdk.core.enums.BuiltinType;
import opc.sdk.core.enums.ValueRanks;
import opc.sdk.core.node.DataTypeNode;
import opc.sdk.core.node.Node;
import opc.sdk.core.node.NodeIdUtil;
import opc.sdk.core.types.TypeInfo;

public class CreateVariableTypeDialog extends StatusDialog {
	private DialogResult result = null;
	private Node selectedParent = null;
	private ValidatingField<String> txt_browseName = null;
	private ValidatingField<String> txt_description = null;
	private ValidatingField<String> txt_displayName = null;
	private ValidatingField<String> txt_nodeId = null;
	private ValidatingField<UnsignedInteger> txt_writeMask = null;
	private ValidatingField<UnsignedInteger> txt_userWriteMask = null;
	// private Combo combo_dataType = null;
	private TreeViewer datatypeTreeViewer = null;
	private CometCombo combo_valueType = null;
	private ValidatingField<Variant> txt_value = null;
	private VariantStringConverter txt_value_converter = null;
	private CometCombo combo_valueRank = null;
	private ValidatingField<UnsignedInteger[]> txt_arrayDimension = null;
	private ArrayDimFieldValidator<UnsignedInteger[]> arrayDimensionValidator = null;
	private ArrayDimQuickFixProvider<UnsignedInteger[]> arrayDimensionQuickFix = null;
	// private CometCombo combo_isAbstract = null;
	private CheckBoxButton cbt_isAbstract = null;
	private TableViewer referencetableViewer = null;
	private ValidatingField<Integer> txt_namespaceBrowseName = null;
	private ValidatingField<Locale> combo_localeDescription = null;
	private ValidatingField<Locale> combo_localeDisplayname = null;
	private CometCombo combo_nodeId = null;
	private DesignerFormToolkit controllCreationToolkit = null;
	private ValueRanks valueRank = ValueRanks.Scalar;
	private BuiltinType valueType = BuiltinType.Null;
	private NodeId dataType = null;
	private DataValue value = null;
	private Map<ValueRanks, UnsignedInteger[]> cache_arraydimension = new HashMap<>();
	private Map<ValueRanks, Map<NodeId, Variant>> cache_value = new HashMap<>();
	private FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private TreeItem dataTypeItem = null;

	public CreateVariableTypeDialog(Shell parentShell, Node selectedNode) {
		super(parentShell);
		this.controllCreationToolkit = new DesignerFormToolkit();
		this.selectedParent = selectedNode;
		this.value = new DataValue();
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
				"NodeEditorVariableTypePart.frm_mainForm.text"));
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		// parent.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		super.createButtonsForButtonBar(parent);
		validate();
	}

	@Override
	protected boolean isResizable() {
		return true;
	}

	@Override
	public void create() {
		super.create();
		fillEntries();
	}

	@Override
	protected Control createDialogArea(final Composite parent) {
		// parent.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		Composite composite = (Composite) super.createDialogArea(parent);
		composite.setLayout(new FillLayout(SWT.HORIZONTAL));
		Composite borderComposite = new Composite(composite, SWT.BORDER);
		borderComposite.setLayout(new FillLayout(SWT.HORIZONTAL));
		ScrolledForm scrolledForm = formToolkit.createScrolledForm(borderComposite);
		formToolkit.paintBordersFor(scrolledForm);
		scrolledForm.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
				"NodeEditorVariableTypePart.frm_mainForm.text"));
		scrolledForm.getBody().setLayout(new GridLayout(1, false));
		createSectionBasic(scrolledForm);
		createDataTypeSection(scrolledForm);
		createExtendedSection(scrolledForm);
		createReferenceSection(scrolledForm);
		
		scrolledForm.reflow(true);
		return composite;
	}

	private void fillEntries() {
		// select first item
		TreeItem[] items = this.datatypeTreeViewer.getTree().getItems();
		if (items != null) {
			for (TreeItem item : items) {
				this.datatypeTreeViewer.getTree().select(item);
				this.datatypeTreeViewer.getTree().notifyListeners(SWT.Selection, new Event());
				break;
			}
		}
	}

	private void createDataTypeSection(ScrolledForm scrolledForm) {
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
		// Label * DataType
		Label lb_type = this.controllCreationToolkit.createLabel(composite, CustomString
				.getString(Activator.getDefault().RESOURCE_BUNDLE, "NodeEditorVariableTypePart.lbl_dataType.text"));
		GridDataFactory.fillDefaults().span(1, 20).applyTo(lb_type);
		this.datatypeTreeViewer = this.controllCreationToolkit.createDataTypeTreeViewerFromParentType(composite,
				this.selectedParent);
		this.datatypeTreeViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				// TODO change value type for correct value
				// change value type for correct value
				DataTypeNode node = (DataTypeNode) ((BrowserModelNode) ((TreeSelection) event.getSelection())
						.getFirstElement()).getNode();
				if (node == null) {
					// reset old selection because if we collapse and
					// the expand a node the selection is not show yet.
					if (dataTypeItem != null) {
						datatypeTreeViewer.getTree().setSelection(dataTypeItem);
					}
					return;
				} else {
					Status status = (Status) getStatus();
					status = new Status(IStatus.OK, status.getPlugin(), "");
					updateStatus(status);
					dataType = node.getNodeId();
					dataTypeItem = datatypeTreeViewer.getTree().getSelection()[0];
					txt_value_converter.setDataType(node.getBrowseName().getName());
					txt_value_converter.setDataTypeId(node.getNodeId());
					/**
					 * Combobox value type
					 */
					controllCreationToolkit.setComboValueType(combo_valueType, dataType);
					selectComboValueType();
				}
			}
		});
		GridDataFactory.fillDefaults().span(2, 20).align(SWT.FILL, SWT.FILL).hint(SWT.DEFAULT, 150).grab(true, false)
				.applyTo(this.datatypeTreeViewer.getControl());
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
				WriteMaskDialog writemaskDialog = new WriteMaskDialog(getShell(), NodeClass.DataType,
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
				WriteMaskDialog writemaskDialog = new WriteMaskDialog(getShell(), NodeClass.VariableType,
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

	private void createExtendedSection(ScrolledForm scrolledForm) {
		Section section = formToolkit.createSection(scrolledForm.getBody(), Section.TWISTIE | Section.TITLE_BAR);
		section.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		formToolkit.paintBordersFor(section);
		section.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "form.attributes.variabletype"));
		section.setExpanded(true);
		Composite composite = formToolkit.createComposite(section, SWT.NONE);
		formToolkit.paintBordersFor(composite);
		section.setClient(composite);
		composite.setLayout(new GridLayout(3, false));
		// Label * Value Rank
		this.controllCreationToolkit.createLabel(composite, CustomString
				.getString(Activator.getDefault().RESOURCE_BUNDLE, "CreateVariableDialog.lbl_valueRank.text"));
		// Combo * Value Rank
		this.combo_valueRank = this.controllCreationToolkit.createComboValueRank(composite, this.selectedParent);
		GridDataFactory.fillDefaults().span(2, 1).align(SWT.FILL, SWT.CENTER).grab(true, false)
				.applyTo(this.combo_valueRank);
		this.valueRank = ValueRanks.valueOf(this.combo_valueRank.getText());
		this.combo_valueRank.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				selectComboValueRank();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
			}
		});
		// Label * Array Dimension
		this.controllCreationToolkit.createLabel(composite, CustomString.getString(
				Activator.getDefault().RESOURCE_BUNDLE, "NodeEditorVariableTypePart.lbl_arrayDimensions.text"));
		// Text * Array Dimension
		this.arrayDimensionQuickFix = new ArrayDimQuickFixProvider<UnsignedInteger[]>(ValueRanks.Any);
		this.arrayDimensionValidator = new ArrayDimFieldValidator<UnsignedInteger[]>(cache_arraydimension);
		this.arrayDimensionValidator
				.setValueRank((ValueRanks) this.combo_valueRank.getData(this.combo_valueRank.getText()));
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
				// valueType = BuiltinType.valueOf(((Combo) e.getSource())
				// .getText());
				//
				// // check the value type
				// TypeInfo typeInfo = TypeInfo.construct(value, ServerInstance
				// .getInstance().getServerInstance().getTypeTree());
				//
				// if (value.getValue().getValue() == null
				// || !(typeInfo.getBuiltInsType().equals(valueType))) {
				//
				// Object val = createDefaultValue(valueType, valueRank);
				// value.setValue(new Variant(val));
				// }
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
			}
		});
		// Text * Value
		this.txt_value_converter = new VariantStringConverter("", this.combo_valueType, this.combo_valueRank);
		this.txt_value_converter.setIsDialog(true);
		// this.txt_value = this.controllCreationToolkit.createTextValue(composite,
		// this.txt_value_converter,
		// this.combo_valueType, this.combo_valueRank, (Text)
		// this.txt_arrayDimension.getControl(),
		// this.cache_value);
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
				// String txtValueType = cmb_valueType.getText();
				// if (!txtValueType.isEmpty()) {
				//
				// BuiltinType valueType = BuiltinType.valueOf(txtValueType);
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
			public void mouseUp(MouseEvent e) {
				openValueDialogIfNeeded();
			}
		});
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER).grab(true, false)
				.applyTo(this.txt_value.getControl());
		// Label * Is Abstract
		this.controllCreationToolkit.createLabel(composite, CustomString
				.getString(Activator.getDefault().RESOURCE_BUNDLE, "NodeEditorReferenceTypePart.lbl_isAbstract.text"));
		{
			cbt_isAbstract = new CheckBoxButton(composite, SWT.NONE);
			cbt_isAbstract.setLeftMargin(8);
			cbt_isAbstract.setTouchEnabled(true);
			// cbt_isAbstract.setText(OPCDesignerString.getString("CreateVariableDialog.btn_historizing.text"));
			// //$NON-NLS-1$
		}
		// Combo * Is Abstract
		// this.combo_isAbstract = this.controllCreationToolkit
		// .createComboBoolean(nodeGroup);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER).grab(true, false).span(2, 1)
				.applyTo(this.cbt_isAbstract);
		new Label(composite, SWT.NONE);
		this.arrayDimensionValidator.setValueField(this.txt_value);
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
//				.getString(Activator.getDefault().RESOURCE_BUNDLE, "CreateVariableDialog.lbl_references.text"));
		// Table * Reference
		this.referencetableViewer = this.controllCreationToolkit.createVariableTypeReferenceTable(composite,
				this.selectedParent, null);
		GridDataFactory.fillDefaults().span(1, 1).align(SWT.FILL, SWT.FILL).grab(true, false).minSize(SWT.DEFAULT, 120)
				.hint(SWT.DEFAULT, 120).applyTo(this.referencetableViewer.getControl());
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
		if (!openSingle && valueType == BuiltinType.Null) {
			return;
		}
		// now we check if we have an scalar
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
			BrowserModelNode bo = (BrowserModelNode) ((IStructuredSelection) datatypeTreeViewer.getSelection())
					.getFirstElement();
			NodeValueDialog dialog2 = new NodeValueDialog(getShell(), valueRank, dims, valueType, value,
					bo.getNode().getNodeId(), this.selectedParent.getNodeId());
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
		Status status = (Status) getStatus();
		status = new Status(IStatus.OK, status.getPlugin(), "");
		updateStatus(status);
	}

	@Override
	protected void okPressed() {
		if (createResult()) {
			super.okPressed();
		} /** otherwise do nothing */
	}

	private boolean createResult() {
		NodeId nodeId = null;
		int namespaceIndex = ServerInstance.getInstance().getServerInstance().getNamespaceUris()
				.getIndex(this.combo_nodeId.getText());
		Object identifier = NodeIdUtil.createIdentifierFromString(this.txt_nodeId.getContents());
		nodeId = NodeIdUtil.createNodeId(namespaceIndex, identifier);
		NodeClass nodeClass = NodeClass.VariableType;
		namespaceIndex = txt_namespaceBrowseName.getContents();
		QualifiedName browseName = new QualifiedName(this.txt_namespaceBrowseName.getContents(),
				this.txt_browseName.getContents());
		LocalizedText displayName = new LocalizedText(txt_displayName.getContents(),
				this.combo_localeDisplayname.getContents());
		LocalizedText description = new LocalizedText(txt_description.getContents(),
				this.combo_localeDescription.getContents());
		UnsignedInteger writeMask = new UnsignedInteger(txt_writeMask.getContents());
		UnsignedInteger userWriteMask = new UnsignedInteger(txt_userWriteMask.getContents());
		Node node = new Node(nodeId, nodeClass, browseName, displayName, description, writeMask, userWriteMask, null);
		/** check the datatype if selected */
		if (this.dataType == null) {
			Status status = (Status) getStatus();
			status = new Status(IStatus.ERROR, status.getPlugin(),
					CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "message.error.datatype"));
			updateStatus(status);
			return false;
		}
		/**
		 * NodeId dataTypeKey = null; for (Entry<NodeId, String> dataType :
		 * this.controllCreationToolkit .fetchDataTypes().entrySet()) { if
		 * (dataType.getValue().equalsIgnoreCase(combo_dataType.getText())) {
		 * dataTypeKey = dataType.getKey(); break; } }
		 */
		UnsignedInteger[] arraydimension = txt_arrayDimension.getContents();
		Variant value = txt_value.getContents();
		if (valueRank == ValueRanks.Scalar
				|| (valueRank == ValueRanks.ScalarOrOneDimension
						&& ((Text) txt_value.getControl()).getText().compareTo("doubleclick") != 0)
				|| (valueRank == ValueRanks.Any
						&& ((Text) txt_value.getControl()).getText().compareTo("doubleclick") != 0)) {
			value = txt_value.getContents();
		} else {
			value = this.value.getValue();
		}
		boolean isAbstract = cbt_isAbstract.isChecked(); // new
		// Boolean(combo_isAbstract.getText());
		NodeAttributes attributes = new VariableTypeAttributes(null, displayName, description, writeMask, userWriteMask,
				value, dataType, this.valueRank.getValue(), arraydimension, isAbstract);
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
		dialogResult
				.setAdditionalReferences(additionalReferences.toArray(new ReferenceNode[additionalReferences.size()]));
		this.result = dialogResult;
		return true;
	}

	public DialogResult getResult() {
		return this.result;
	}

	private void selectComboValueRank() {
		valueRank = ValueRanks.valueOf(combo_valueRank.getText());
		arrayDimensionValidator.setValueRank(valueRank);
		txt_arrayDimension.validate();
		/** set quick fix */
		arrayDimensionQuickFix.setValueRank(valueRank);
		value = getCachedValue(valueRank, valueType);
		this.txt_value.setContents(value.getValue());
		validate();
	}

	private void selectComboValueType() {
		valueType = BuiltinType.valueOf(this.combo_valueType.getText());
		this.value = getCachedValue(valueRank, valueType);
		this.txt_value_converter.setValue(value.getValue());
		this.txt_value_converter.setDataType(this.combo_valueType.getText());
		BrowserModelNode element = (BrowserModelNode) ((IStructuredSelection) datatypeTreeViewer.getSelection())
				.getFirstElement();
		this.txt_value_converter.setDataTypeId(element.getNode().getNodeId());
		this.txt_value.setContents(value.getValue());
		if (value == null) {
			((Text) this.txt_value.getControl()).setText("");
		} else if (value.getValue() == null) {
			((Text) this.txt_value.getControl()).setText("");
		}
		validate();
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

	private void validate() {
		boolean validBrowseName = this.txt_browseName.isValid();
		boolean validDescription = this.txt_description.isValid();
		boolean validDisplayName = this.txt_displayName.isValid();
		boolean validNodeId = this.txt_nodeId.isValid();
		boolean validWriteMask = this.txt_writeMask.isValid();
		boolean validUserWriteMask = this.txt_userWriteMask.isValid();
		boolean validBrowseNameIndex = this.txt_namespaceBrowseName.isValid();
		boolean validArrayDimension = this.txt_arrayDimension.isValid();
		boolean validValue = this.txt_value.isValid();
		this.txt_value.validate();
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
				&& validUserWriteMask && validBrowseNameIndex && validArrayDimension && validValue;
		Button btnOK = getButton(OK);
		if (!isValid) {
			if (btnOK != null) {
				btnOK.setEnabled(false);
			}
		} else {
			if (btnOK != null) {
				btnOK.setEnabled(true);
			}
		}
	}
}
