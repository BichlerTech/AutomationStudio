package com.bichler.astudio.opcua.opcmodeler.wizards.create.page;

import java.util.Locale;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.wizard.WizardPage;
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
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.QualifiedName;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.core.AttributeWriteMask;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.core.ReferenceNode;

import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.opcua.modeller.controls.CometCombo;
import com.bichler.astudio.opcua.opcmodeler.dialogs.SelectComponentOfDialog;
import com.bichler.astudio.opcua.opcmodeler.dialogs.WriteMaskDialog;
import com.bichler.astudio.opcua.opcmodeler.editor.node.DesignerFormToolkit;
import com.bichler.astudio.opcua.opcmodeler.editor.node.validation.errors.NodeIdTextFieldQuickFixProvider;
import com.richclientgui.toolbox.validation.ValidatingField;

import opc.sdk.core.node.Node;
import opc.sdk.core.node.NodeIdUtil;

public class GeneralCreatePage extends WizardPage {
	private FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private DesignerFormToolkit controllCreationToolkit = null;
	private ValidatingField<Integer> txt_namespaceBrowseName;
	private ValidatingField<String> txt_browseName;
	private ValidatingField<Locale> combo_localeDescription;
	private ValidatingField<String> txt_description;
	private ValidatingField<Locale> combo_localeDisplayname;
	private ValidatingField<String> txt_displayName;
	private CometCombo combo_nodeId;
	private ValidatingField<String> txt_nodeId;
	private ValidatingField<UnsignedInteger> txt_writeMask;
	private ValidatingField<UnsignedInteger> txt_userWriteMask;
	private ScrolledForm scrolledFrom;
	private TableViewer tableViewer;
	private Node selectedParent;

	/**
	 * Create the wizard.
	 */
	public GeneralCreatePage(Node selectedParent) {
		super("generalcreatePage");
		setTitle("Neue Methode");
		setDescription("Erstellt eine neue Methode im OPC UA Informationsmodell");
		this.controllCreationToolkit = new DesignerFormToolkit();
		this.selectedParent = selectedParent;
	}

	/**
	 * Create contents of the wizard.
	 * 
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		container.setLayout(new FillLayout(SWT.HORIZONTAL));
		setControl(container);
		scrolledFrom = formToolkit.createScrolledForm(container);
		scrolledFrom.setExpandHorizontal(true);
		scrolledFrom.setExpandVertical(true);
		scrolledFrom.getBody().setLayout(new GridLayout(1, false));
		createSectionGeneral(scrolledFrom.getBody());
		createSectionReferences(scrolledFrom.getBody());
	}

	private void createSectionReferences(Composite container) {
		Section section = formToolkit.createSection(container, Section.TWISTIE | Section.TITLE_BAR);
		section.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		formToolkit.paintBordersFor(section);
		section.setText("References");
		section.setExpanded(true);
		Composite composite = formToolkit.createComposite(section, SWT.NONE);
		formToolkit.paintBordersFor(composite);
		composite.setLayout(new GridLayout(1, false));
		section.setClient(composite);
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

	private void createSectionGeneral(Composite container) {
		Section section = formToolkit.createSection(container, Section.TWISTIE | Section.TITLE_BAR);
		section.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		formToolkit.paintBordersFor(section);
		section.setText("OPC UA Attribute");
		section.setExpanded(true);
		Composite composite = formToolkit.createComposite(section, SWT.NONE);
		formToolkit.paintBordersFor(composite);
		composite.setLayout(new GridLayout(3, false));
		section.setClient(composite);
		// Label * Browsename
		this.controllCreationToolkit.createLabel(composite, "Browsename");
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
//				txt_description.setContents(txt_browseName.getContents());
				txt_displayName.setContents(txt_browseName.getContents());
				validate();
			}
		});
		GridData data = (GridData) this.txt_browseName.getControl().getLayoutData();
		data.grabExcessHorizontalSpace = true;
		data.horizontalAlignment = SWT.FILL;
		// Label * Description
		this.controllCreationToolkit.createLabel(composite, "Description");
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
		this.controllCreationToolkit.createLabel(composite, "Displayname");
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
		this.controllCreationToolkit.createLabel(composite, "NodeId");
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
		this.controllCreationToolkit.createLabel(composite, "WriteMask");
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
		this.controllCreationToolkit.createLabel(composite, "UserWriteMask");
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

	@Override
	public boolean isPageComplete() {
		boolean validBrowseName = this.txt_browseName.isValid();
		boolean validDescription = this.txt_description.isValid();
		boolean validDisplayName = this.txt_displayName.isValid();
		boolean validNodeId = this.txt_nodeId.isValid();
		boolean validWriteMask = this.txt_writeMask.isValid();
		boolean validUserWriteMask = this.txt_userWriteMask.isValid();
		boolean validBrowseNameIndex = this.txt_namespaceBrowseName.isValid();
		boolean isValid = validBrowseName && validDescription && validDisplayName && validNodeId && validWriteMask
				&& validUserWriteMask && validBrowseNameIndex;
		return isValid;
	}

	private void validate() {
		boolean isComplete = isPageComplete();
		setPageComplete(isComplete);
	}

	public QualifiedName getBrowseName() {
		return new QualifiedName(this.txt_namespaceBrowseName.getContents(), this.txt_browseName.getContents());
	}

	public LocalizedText getDescriptionText() {
		return new LocalizedText(this.txt_description.getContents(), this.combo_localeDescription.getContents());
	}

	public LocalizedText getDisplayname() {
		return new LocalizedText(this.txt_displayName.getContents(), this.combo_localeDisplayname.getContents());
	}

	public NodeId getNodeId() {
		Object identifier = NodeIdUtil.createIdentifierFromString(this.txt_nodeId.getContents());
		return NodeIdUtil.createNodeId(ServerInstance.getInstance().getServerInstance().getNamespaceUris()
				.getIndex(this.combo_nodeId.getText()), identifier);
	}

	public UnsignedInteger getWriteMask() {
		return this.txt_writeMask.getContents();
	}

	public UnsignedInteger getUserWriteMask() {
		return this.txt_userWriteMask.getContents();
	}

	public ReferenceNode getReferenceNode() {
		TableItem c = this.tableViewer.getTable().getItems()[0];
		ReferenceNode data = (ReferenceNode) c.getData();
		return data;
	}
}
