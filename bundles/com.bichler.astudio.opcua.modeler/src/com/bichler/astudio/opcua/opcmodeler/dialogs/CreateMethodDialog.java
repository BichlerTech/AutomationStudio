package com.bichler.astudio.opcua.opcmodeler.dialogs;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import opc.sdk.core.node.Node;
import opc.sdk.core.node.NodeIdUtil;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
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
import org.opcfoundation.ua.core.MethodAttributes;
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

public class CreateMethodDialog extends Dialog {
	private DialogResult result = null;
	private TableViewer tableViewer = null;
	private Node selectedParent = null;
	private FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private ValidatingField<String> txt_browseName = null;
	private ValidatingField<String> txt_description = null;
	private ValidatingField<String> txt_displayName = null;
	private ValidatingField<String> txt_nodeId = null;
	private ValidatingField<UnsignedInteger> txt_writeMask = null;
	private ValidatingField<UnsignedInteger> txt_userWriteMask = null;
	private CometCombo combo_executeable = null;
	private CometCombo combo_userExecuteable = null;
	private ValidatingField<Integer> txt_namespaceBrowseName = null;
	private ValidatingField<Locale> combo_localeDescription = null;
	private ValidatingField<Locale> combo_localeDisplayname = null;
	private CometCombo combo_nodeId = null;
	private DesignerFormToolkit controllCreationToolkit = null;

	public CreateMethodDialog(Shell parentShell, Node selectedNode) {
		super(parentShell);
		this.selectedParent = selectedNode;
		this.controllCreationToolkit = new DesignerFormToolkit();
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
				"NodeEditorMethodPart.frm_mainForm.text"));
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		super.createButtonsForButtonBar(parent);
		validate();
	}

	@Override
	protected Control createDialogArea(final Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);
		composite.setLayout(new FillLayout(SWT.HORIZONTAL));
		ScrolledForm scrolledForm = formToolkit.createScrolledForm(composite);
		formToolkit.paintBordersFor(scrolledForm);
		scrolledForm.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
				"NodeEditorMethodPart.frm_mainForm.text"));
		scrolledForm.getBody().setLayout(new GridLayout(1, false));
		createSectionBasic(scrolledForm);
		createExtendedSection(scrolledForm);
		createReferenceSection(scrolledForm);
		
		scrolledForm.reflow(true);
		// Group nodeGroup = new Group(composite, SWT.SHADOW_ETCHED_IN);
		// nodeGroup.setText("Node Attributes");
		// nodeGroup.setLayout(new GridLayout(3, true));
		return composite;
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
				WriteMaskDialog writemaskDialog = new WriteMaskDialog(getShell(), NodeClass.Method,
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
				WriteMaskDialog writemaskDialog = new WriteMaskDialog(getShell(), NodeClass.Method,
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
		section.setText("Methoden Attribute");
		section.setExpanded(true);
		Composite composite = formToolkit.createComposite(section, SWT.NONE);
		formToolkit.paintBordersFor(composite);
		section.setClient(composite);
		composite.setLayout(new GridLayout(3, false));
		// Label * Executeable
		this.controllCreationToolkit.createLabel(composite, CustomString
				.getString(Activator.getDefault().RESOURCE_BUNDLE, "NodeEditorMethodPart.lbl_executeable.text"));
		// Combo * Executeable
		this.combo_executeable = this.controllCreationToolkit.createComboBoolean(composite);
		GridDataFactory.fillDefaults().span(2, 1).grab(true, false).align(SWT.FILL, SWT.CENTER)
				.applyTo(this.combo_executeable);
		// Label * UserExecuteable
		this.controllCreationToolkit.createLabel(composite, CustomString
				.getString(Activator.getDefault().RESOURCE_BUNDLE, "NodeEditorMethodPart.lbl_userExecuteable.text"));
		this.combo_userExecuteable = this.controllCreationToolkit.createComboBoolean(composite);
		GridDataFactory.fillDefaults().span(2, 1).grab(true, false).align(SWT.FILL, SWT.CENTER)
				.applyTo(this.combo_userExecuteable);
	}

	@Override
	protected boolean isResizable() {
		return true;
	}

	private void createReferenceSection(ScrolledForm scrolledForm) {
		Section section = formToolkit.createSection(scrolledForm.getBody(), Section.TWISTIE | Section.TITLE_BAR);
		section.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		formToolkit.paintBordersFor(section);
		section.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
				"66: CreateVariableDialog.lbl_references.text = References "));
		section.setExpanded(true);
		Composite composite = formToolkit.createComposite(section, SWT.NONE);
		formToolkit.paintBordersFor(composite);
		section.setClient(composite);
		composite.setLayout(new GridLayout(1, false));
		// Label * Reference
//		this.controllCreationToolkit.createLabel(composite, CustomString
//				.getString(Activator.getDefault().RESOURCE_BUNDLE, "NodeEditorPart.tblclmn_reference.text"));
		// Table * Reference
		this.tableViewer = this.controllCreationToolkit.createDataTypeReferenceTable(composite, this.selectedParent,
				null);
		/** we have to check if we create an object type */
		if (NodeClass.ObjectType.equals(this.selectedParent.getNodeClass())) {
			this.tableViewer.addDoubleClickListener(new IDoubleClickListener() {
				@Override
				public void doubleClick(DoubleClickEvent event) {
					SelectComponentOfDialog organizedDialog = new SelectComponentOfDialog(getShell());
					organizedDialog.setNodeToUpdate(selectedParent);
					int open = organizedDialog.open();
					if (open == Dialog.OK) {
						ReferenceNode result = organizedDialog.getReference();
						tableViewer.setInput(new ReferenceNode[] { result });
						tableViewer.refresh();
					}
				}
			});
		}
		GridDataFactory.fillDefaults().span(1, 1).grab(true, false).align(SWT.FILL, SWT.FILL)
				.applyTo(this.tableViewer.getControl());
	}

	@Override
	protected void okPressed() {
		createResult();
		super.okPressed();
	}

	private void createResult() {
		NodeId nodeId = null;
		int namespaceIndex = ServerInstance.getInstance().getServerInstance().getNamespaceUris()
				.getIndex(this.combo_nodeId.getText());
		Object identifier = NodeIdUtil.createIdentifierFromString(this.txt_nodeId.getContents());
		nodeId = NodeIdUtil.createNodeId(namespaceIndex, identifier);
		NodeClass nodeClass = NodeClass.Method;
		QualifiedName browseName = new QualifiedName(this.txt_namespaceBrowseName.getContents(),
				this.txt_browseName.getContents());
		LocalizedText displayName = new LocalizedText(txt_displayName.getContents(),
				this.combo_localeDisplayname.getContents());
		LocalizedText description = new LocalizedText(txt_description.getContents(),
				this.combo_localeDescription.getContents());
		UnsignedInteger writeMask = null;
		try {
			new UnsignedInteger(txt_writeMask.getContents());
		} catch (NumberFormatException nfe) {
			writeMask = new UnsignedInteger(0);
		}
		UnsignedInteger userWriteMask = null;
		try {
			userWriteMask = new UnsignedInteger(txt_userWriteMask.getContents());
		} catch (NumberFormatException nfe) {
			userWriteMask = new UnsignedInteger(0);
		}
		Node node = new Node(nodeId, nodeClass, browseName, displayName, description, writeMask, userWriteMask, null);
		boolean executeable = new Boolean(combo_executeable.getText());
		boolean userExecuteable = new Boolean(combo_userExecuteable.getText());
		NodeAttributes attributes = new MethodAttributes(null, displayName, description, writeMask, userWriteMask,
				executeable, userExecuteable);
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
		// TODO: Set type
		this.result = dialogResult;
	}

	public DialogResult getResult() {
		return this.result;
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
