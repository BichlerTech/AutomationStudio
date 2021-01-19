package com.bichler.astudio.opcua.opcmodeler.dialogs;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.QualifiedName;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.core.AttributeWriteMask;
import org.opcfoundation.ua.core.DataTypeAttributes;
import org.opcfoundation.ua.core.NodeAttributes;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.core.ReferenceNode;

import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.utils.internationalization.CustomString;
import com.bichler.astudio.opcua.modeller.controls.CometCombo;
import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.opcua.opcmodeler.commands.DialogResult;
import com.bichler.astudio.opcua.opcmodeler.editor.node.DesignerFormToolkit;
import com.bichler.astudio.opcua.opcmodeler.editor.node.validation.errors.NodeIdTextFieldQuickFixProvider;
import com.richclientgui.toolbox.validation.ValidatingField;

import opc.sdk.core.node.Node;
import opc.sdk.core.node.NodeIdUtil;

public class CreateDataTypeDialog extends Dialog {
	private DialogResult result = null;
	private Node selectedParent = null;
	private TableViewer tableViewer = null;
	private DesignerFormToolkit controllCreationToolkit = null;
	private FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private ValidatingField<String> txt_browseName = null;
	private ValidatingField<String> txt_description = null;
	private ValidatingField<String> txt_displayName = null;
	private ValidatingField<String> txt_nodeId = null;
	private ValidatingField<UnsignedInteger> txt_writeMask = null;
	private ValidatingField<UnsignedInteger> txt_userWriteMask = null;
	private CometCombo combo_isAbstract = null;
	private ValidatingField<Integer> txt_namespaceBrowseName;
	private ValidatingField<Locale> combo_localeDescription;
	private ValidatingField<Locale> combo_localeDisplayname;
	private CometCombo combo_nodeId;

	// private ScrolledForm scrolledForm;
	// private Section sctnBasic;
	// private Composite container;
	public CreateDataTypeDialog(Shell parentShell, Node selectedNode) {
		super(parentShell);
		this.selectedParent = selectedNode;
		this.controllCreationToolkit = new DesignerFormToolkit();
	}

	public DialogResult getResult() {
		return this.result;
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		// parent.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		super.createButtonsForButtonBar(parent);
		validate();
	}

	/**
	 * Configures the Shell.
	 * 
	 * @param NewShell Shell to configure.
	 */
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "opc.message.extenddatatype"));
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
				"NodeEditorVariableTypePart.lbl_dataType.text"));
		scrolledForm.getBody().setLayout(new GridLayout(1, false));
		createSectionBasic(scrolledForm);
		createExtendedSection(scrolledForm);
		createReferenceSection(scrolledForm);
		// setMaxSizeHandler(parent);
		scrolledForm.reflow(true);
		return composite;
	}

	@Override
	protected boolean isResizable() {
		return true;
	}

	@Override
	protected void okPressed() {
		createResult();
		super.okPressed();
	}

	private void setMaxSizeHandler(Composite parent) {
		final Point pMax = parent.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		getShell().addListener(SWT.Resize, new Listener() {
			@Override
			public void handleEvent(Event event) {
				Control control = ((Control) event.widget);
				Point size = control.getSize();
				if (size.y > pMax.y) {
					size.y = pMax.y;
					control.setSize(size.x, size.y);
				}
			}
		});
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
		this.tableViewer = this.controllCreationToolkit.createDataTypeReferenceTable(composite, this.selectedParent,
				null);
		GridDataFactory.fillDefaults().span(1, 1).grab(true, false).align(SWT.FILL, SWT.FILL).minSize(SWT.DEFAULT, 120)
				.hint(SWT.DEFAULT, 120).applyTo(this.tableViewer.getControl());
	}

	private void createExtendedSection(ScrolledForm scrolledForm) {
		Section section = formToolkit.createSection(scrolledForm.getBody(), Section.TWISTIE | Section.TITLE_BAR);
		section.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		formToolkit.paintBordersFor(section);
		section.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "form.datatype.attribute"));
		section.setExpanded(true);
		Composite composite = formToolkit.createComposite(section, SWT.NONE);
		formToolkit.paintBordersFor(composite);
		section.setClient(composite);
		composite.setLayout(new GridLayout(3, false));
		// Label * Is Abstract
		this.controllCreationToolkit.createLabel(composite, CustomString
				.getString(Activator.getDefault().RESOURCE_BUNDLE, "NodeEditorVariableTypePart.lbl_isAbstract.text"));
		// Combo * Is Abstract
		this.combo_isAbstract = this.controllCreationToolkit.createComboBoolean(composite);
		GridDataFactory.fillDefaults().span(2, 1).grab(true, false).align(SWT.FILL, SWT.CENTER)
				.applyTo(this.combo_isAbstract);
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
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "NodeEditorPart.lbl_browseName.text"));
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
				//
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
		this.controllCreationToolkit.createLabel(composite,
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "NodeEditorPart.lbl_description.text"));
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
		this.controllCreationToolkit.createLabel(composite,
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "NodeEditorPart.lbl_displayName.text"));
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
				WriteMaskDialog writemaskDialog = new WriteMaskDialog(getShell(), NodeClass.DataType,
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

	/**
	 * Creates a result for a datatype node
	 */
	private void createResult() {
		NodeId nodeId = null;
		int namespaceIndex = ServerInstance.getInstance().getServerInstance().getNamespaceUris()
				.getIndex(this.combo_nodeId.getText());
		Object identifier = NodeIdUtil.createIdentifierFromString(this.txt_nodeId.getContents());
		nodeId = NodeIdUtil.createNodeId(namespaceIndex, identifier);
		NodeClass nodeClass = NodeClass.DataType;
		namespaceIndex = this.txt_namespaceBrowseName.getContents();
		QualifiedName browseName = new QualifiedName(this.txt_namespaceBrowseName.getContents(),
				this.txt_browseName.getContents());
		LocalizedText displayName = new LocalizedText(txt_displayName.getContents(),
				this.combo_localeDisplayname.getContents());
		LocalizedText description = new LocalizedText(txt_description.getContents(),
				this.combo_localeDescription.getContents());
		UnsignedInteger writeMask = txt_writeMask.getContents();
		UnsignedInteger userWriteMask = txt_userWriteMask.getContents();
		Node node = new Node(nodeId, nodeClass, browseName, displayName, description, writeMask, userWriteMask, null);
		boolean isAbstract = new Boolean(combo_isAbstract.getText());
		NodeAttributes attributes = new DataTypeAttributes(null, displayName, description, writeMask, userWriteMask,
				isAbstract);
		TableItem c = this.tableViewer.getTable().getItems()[0];
		ReferenceNode data = (ReferenceNode) c.getData();
		ExpandedNodeId parentNodeId = data.getTargetId();
		NodeId referenceTypeId = data.getReferenceTypeId();
		List<ReferenceNode> additionalReferences = new ArrayList<ReferenceNode>();
		for (int i = 1; i < this.tableViewer.getTable().getItemCount(); i++) {
			c = this.tableViewer.getTable().getItem(i);
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
	}

	private void validate() {
		boolean validBrowseName = this.txt_browseName.isValid();
		boolean validDescription = this.txt_description.isValid();
		boolean validDisplayName = this.txt_displayName.isValid();
		boolean validNodeId = this.txt_nodeId.isValid();
		boolean validWriteMask = this.txt_writeMask.isValid();
		boolean validUserWriteMask = this.txt_userWriteMask.isValid();
		boolean validBrowseNameIndex = this.txt_namespaceBrowseName.isValid();
		boolean isValid = validBrowseName && validDescription && validDisplayName && validNodeId && validWriteMask
				&& validUserWriteMask && validBrowseNameIndex;
		if (!isValid) {
			getButton(OK).setEnabled(false);
		} else {
			getButton(OK).setEnabled(true);
		}
	}
}
