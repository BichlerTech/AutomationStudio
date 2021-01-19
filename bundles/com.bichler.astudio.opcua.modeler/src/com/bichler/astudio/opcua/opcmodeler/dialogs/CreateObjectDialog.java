package com.bichler.astudio.opcua.opcmodeler.dialogs;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.StatusDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.wb.swt.SWTResourceManager;
import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.QualifiedName;
import org.opcfoundation.ua.builtintypes.UnsignedByte;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.core.AttributeWriteMask;
import org.opcfoundation.ua.core.NodeAttributes;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.core.ObjectAttributes;
import org.opcfoundation.ua.core.ReferenceNode;

import com.bichler.astudio.utils.internationalization.CustomString;
import com.bichler.astudio.utils.ui.swt.CheckBoxButton;
import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.opcua.modeller.controls.CometCombo;
import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.opcua.opcmodeler.commands.DialogResult;
import com.bichler.astudio.opcua.opcmodeler.editor.node.DesignerFormToolkit;
import com.bichler.astudio.opcua.opcmodeler.editor.node.validation.errors.NodeIdTextFieldQuickFixProvider;
import com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.nodes.BrowserModelNode;
import com.richclientgui.toolbox.validation.ValidatingField;

import opc.sdk.core.enums.EventNotifiers;
import opc.sdk.core.node.Node;
import opc.sdk.core.node.NodeIdUtil;
import opc.sdk.core.node.ObjectTypeNode;

public class CreateObjectDialog extends StatusDialog {
	@Override
	protected void cancelPressed() {
		// TODO Auto-generated method stub
		// CometLogger.getLogger().setLog(Activator.getDefault().getLog());
		// ICometLogger logger = CometLogger.getLogger();
		// logger.setLogLevel(ICometLogger.LOG1 | ICometLogger.CANCEL1);
		// logger.setDebug(ICometLogger.OUTPUT_STREAM);
		Logger.getLogger(getClass().getName()).log(Level.INFO, this.selectedParent.getBrowseName().getName() + " ("
				+ this.selectedParent.getNodeId() + "): create child Object canceled");
		super.cancelPressed();
	}

	// private Map<String, NodeId> objectTypes = null;
	private DialogResult result = null;
	private TreeViewer treeViewer = null;
	private TableViewer tableViewer = null;
	private ValidatingField<String> txt_browseName = null;
	private ValidatingField<String> txt_description = null;
	private ValidatingField<String> txt_displayName = null;
	private ValidatingField<String> txt_nodeId = null;
	private ValidatingField<UnsignedInteger> txt_writeMask = null;
	private ValidatingField<UnsignedInteger> txt_userWriteMask = null;
	// private Label lb_error = null;
	private CheckBoxButton[] cb_supportEvents = null;
	private Node selectedParent = null;
	private ValidatingField<Integer> txt_namespaceBrowseName = null;
	private ValidatingField<Locale> combo_localeDescription = null;
	private ValidatingField<Locale> combo_localeDisplayname = null;
	private CometCombo combo_nodeId = null;
	private DesignerFormToolkit controllCreationToolkit = null;
	private FormToolkit formToolkit = new FormToolkit(Display.getDefault());

	public CreateObjectDialog(Shell parentShell, Node selectedParent) {
		super(parentShell);
		this.selectedParent = selectedParent;
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
		newShell.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "opc.message.extendobject"));
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		// parent.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		super.createButtonsForButtonBar(parent);
		// getButton(OK).setText("Next");
		validate();
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
				"NodeEditorObjectPart.frm_mainForm.text"));
		scrolledForm.getBody().setLayout(new GridLayout(1, false));
		scrolledForm.getBody().setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		createSectionBasic(scrolledForm);
		createTypeSection(scrolledForm);
		createExtendedSection(scrolledForm);
		createReferenceSection(scrolledForm);
		
		scrolledForm.reflow(true);
		// Group nodeGroup = new Group(composite, SWT.SHADOW_ETCHED_IN);
		// nodeGroup.setText("Node Attributes");
		// nodeGroup.setLayout(new GridLayout(3, true));
		// this.lb_error = new Label(scrolledForm.getBody(), SWT.NONE);
		// this.lb_error.setForeground(new Color(null, 255, 0, 0));
		// this.lb_error.setVisible(false);
		// GridDataFactory.fillDefaults().span(1, 1).align(SWT.FILL,
		// SWT.FILL).applyTo(this.lb_error);
		return composite;
	}

	@Override
	protected boolean isResizable() {
		return true;
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
		this.controllCreationToolkit.createLabel(composite, CustomString
				.getString(Activator.getDefault().RESOURCE_BUNDLE, "CreateVariableDialog.lbl_browseName.text"));
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
		GridData data = (GridData) this.txt_browseName.getControl().getLayoutData();
		data.grabExcessHorizontalSpace = true;
		data.horizontalAlignment = SWT.FILL;
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
		// Label * Description
		this.controllCreationToolkit.createLabel(composite, CustomString
				.getString(Activator.getDefault().RESOURCE_BUNDLE, "CreateVariableDialog.lbl_description.text"));
		// Combo * Description
		this.combo_localeDescription = this.controllCreationToolkit.createComboLocale(composite, null);
		((Combo) this.combo_localeDescription.getControl()).addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				validate();
			}
		});
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
		((Combo) this.combo_localeDisplayname.getControl()).addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				validate();
			}
		});
		// Text * Displayname
		this.txt_displayName = this.controllCreationToolkit.createTextString(composite);
		((Text) this.txt_displayName.getControl()).addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				validate();
			}
		});
		GridDataFactory.fillDefaults().span(1, 1).grab(true, false).align(SWT.FILL, SWT.CENTER)
				.applyTo(this.txt_displayName.getControl());
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
				// validate();
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
				WriteMaskDialog writemaskDialog = new WriteMaskDialog(getShell(), NodeClass.Object,
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
				WriteMaskDialog writemaskDialog = new WriteMaskDialog(getShell(), NodeClass.Object,
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

	private void createTypeSection(ScrolledForm scrolledForm) {
		Section section = formToolkit.createSection(scrolledForm.getBody(), Section.TWISTIE | Section.TITLE_BAR);
		section.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		formToolkit.paintBordersFor(section);
		section.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "form.typedefinition"));
		section.setExpanded(true);
		Composite composite = formToolkit.createComposite(section, SWT.NONE);
		formToolkit.paintBordersFor(composite);
		section.setClient(composite);
		composite.setLayout(new GridLayout(3, false));
		// Label * Base Object Type
		Label lb_baseObjectType = this.controllCreationToolkit.createLabel(composite, CustomString
				.getString(Activator.getDefault().RESOURCE_BUNDLE, "NodeEditorObjectTypePart.frm_mainForm.text"));
		GridDataFactory.fillDefaults().span(1, 50).applyTo(lb_baseObjectType);
		// Treeviewer * Object Type
		this.treeViewer = this.controllCreationToolkit.createObjectTypeTreeViewer(composite);
		this.treeViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				ObjectTypeNode objectType = (ObjectTypeNode) ((BrowserModelNode) ((TreeSelection) event.getSelection())
						.getFirstElement()).getNode();
				if (objectType != null) {
					if (objectType.getIsAbstract()) {
						Status status = (Status) getStatus();
						status = new Status(IStatus.ERROR, status.getPlugin(), CustomString
								.getString(Activator.getDefault().RESOURCE_BUNDLE, "form.error.abstracttype"));
						updateStatus(status);
					} else {
						Status status = (Status) getStatus();
						status = new Status(IStatus.OK, status.getPlugin(), "");
						updateStatus(status);
					}
				}
			}
		});
		this.treeViewer.getTree().select(this.treeViewer.getTree().getItem(0));
		GridDataFactory.fillDefaults().span(2, 50).grab(true, false).align(SWT.FILL, SWT.FILL).minSize(SWT.DEFAULT, 150)
				.hint(SWT.DEFAULT, 150).applyTo(this.treeViewer.getControl());
	}

	private void createExtendedSection(ScrolledForm scrolledForm) {
		Section section = formToolkit.createSection(scrolledForm.getBody(), Section.TWISTIE | Section.TITLE_BAR);
		section.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		formToolkit.paintBordersFor(section);
		section.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "form.attributes.object"));
		section.setExpanded(true);
		Composite composite = formToolkit.createComposite(section, SWT.NONE);
		formToolkit.paintBordersFor(composite);
		section.setClient(composite);
		composite.setLayout(new GridLayout(3, false));
		// Label * SupportsEvents
		Label lbl1 = this.controllCreationToolkit.createLabel(composite,
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "form.support.events"));
		GridData data = (GridData) lbl1.getLayoutData();
		data.verticalAlignment = SWT.CENTER;
		// CheckBoxField * SupportsEvents
		this.cb_supportEvents = this.controllCreationToolkit.createCheckSupportEvents(composite);
		for (CheckBoxButton cb : this.cb_supportEvents) {
			cb.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseUp(MouseEvent e) {
					validate();
				}
			});
		}
	}

	private void createReferenceSection(ScrolledForm scrolledForm) {
		Section section = formToolkit.createSection(scrolledForm.getBody(), Section.TWISTIE | Section.TITLE_BAR);
		section.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		formToolkit.paintBordersFor(section);
		section.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
				"NodeEditorPart.tblclmn_reference.text"));
		section.setExpanded(true);
		Composite composite = formToolkit.createComposite(section, SWT.NONE);
		formToolkit.paintBordersFor(composite);
		section.setClient(composite);
		composite.setLayout(new GridLayout(1, false));
		// Label * Reference
//		this.controllCreationToolkit.createLabel(composite, CustomString
//				.getString(Activator.getDefault().RESOURCE_BUNDLE, "CreateVariableDialog.lbl_references.text"));
		// Table * Reference
		this.tableViewer = this.controllCreationToolkit.createObjectReferenceTable(composite, this.selectedParent,
				null);
		/** we have to check if we create an object type */
		if (NodeClass.ObjectType.equals(this.selectedParent.getNodeClass())) {
			this.tableViewer.addDoubleClickListener(new IDoubleClickListener() {
				@Override
				public void doubleClick(DoubleClickEvent event) {
					// TODO Auto-generated method stub
					SelectComponentOfDialog organizedDialog = new SelectComponentOfDialog(getShell());
					organizedDialog.setNodeToUpdate(selectedParent);
					int open = organizedDialog.open();
					if (open == Dialog.OK) {
						ReferenceNode result = organizedDialog.getReference();
						tableViewer.setInput(new ReferenceNode[] { result });
						// TableItem item = tableViewer.getTable().getItem(0);
						// item.setData(new ReferenceNode[]{result});
						tableViewer.refresh();
					}
				}
			});
		}
		GridDataFactory.fillDefaults().grab(true, false).span(1, 1).align(SWT.FILL, SWT.FILL)
				.applyTo(this.tableViewer.getTable());
	}

	@Override
	protected void okPressed() {
		createResult();
		/** we have to check if object type is not abstract */
		// this.result.getReferenceTypeId();
		super.okPressed();
	}

	/**
	 * Creates a result for a object node
	 */
	private void createResult() throws NullPointerException {
		NodeId nodeId = null;
		int namespaceIndex = ServerInstance.getInstance().getServerInstance().getNamespaceUris()
				.getIndex(this.combo_nodeId.getText());
		Object identifier = NodeIdUtil.createIdentifierFromString(this.txt_nodeId.getContents());
		nodeId = NodeIdUtil.createNodeId(namespaceIndex, identifier);
		NodeClass nodeClass = NodeClass.Object;
		QualifiedName browseName = new QualifiedName(this.txt_namespaceBrowseName.getContents(),
				this.txt_browseName.getContents());
		LocalizedText displayName = new LocalizedText(txt_displayName.getContents(),
				combo_localeDisplayname.getContents());
		LocalizedText description = new LocalizedText(txt_description.getContents(),
				combo_localeDescription.getContents());
		UnsignedInteger writeMask = new UnsignedInteger(txt_writeMask.getContents());
		UnsignedInteger userWriteMask = new UnsignedInteger(txt_userWriteMask.getContents());
		Node node = new Node(nodeId, nodeClass, browseName, displayName, description, writeMask, userWriteMask, null);
		byte supportEventVal = 0;
		for (CheckBoxButton b : cb_supportEvents) {
			if (b.isChecked()) {
				supportEventVal += EventNotifiers.valueOf(b.getText()).getValue();
			}
		}
		UnsignedByte eventNotifier = new UnsignedByte(supportEventVal);
		NodeAttributes attributes = new ObjectAttributes(null, displayName, description, writeMask, userWriteMask,
				eventNotifier);
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
		Object element = ((ITreeSelection) this.treeViewer.getSelection()).getFirstElement();
		dialogResult.setType(((BrowserModelNode) element).getNode().getNodeId());
		dialogResult
				.setAdditionalReferences(additionalReferences.toArray(new ReferenceNode[additionalReferences.size()]));
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
