package com.bichler.astudio.opcua.opcmodeler.wizards;

import java.util.Locale;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.wb.swt.SWTResourceManager;
import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.QualifiedName;

import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.opcua.opcmodeler.editor.node.DesignerFormToolkit;
import com.richclientgui.toolbox.validation.ValidatingField;

import opc.sdk.core.node.Node;
import opc.sdk.core.node.NodeIdUtil;

public class CopyWizardDetailPage extends WizardPage {
	private FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private DesignerFormToolkit controllCreationToolkit = null;
	// private ValidatingField<UnsignedInteger> txt_writeMask;
	// private ValidatingField<UnsignedInteger> txt_userWriteMask;
	private ScrolledForm scrolledFrom;
	// private TableViewer tableViewer;
	// private Node selectedParent;
	private ValidatingField<Integer> txt_namespaceBrowseName;
	private ValidatingField<String> txt_browseName;
	private ValidatingField<Locale> combo_localeDescription;
	private ValidatingField<String> txt_description;
	private ValidatingField<Locale> combo_localeDisplayname;
	private ValidatingField<String> txt_displayName;
	// private CometCombo combo_Namespace;
	private ValidatingField<String> txt_nodeId;
	private CopyWizardNamespacePage namespacePage;
	private NodeId node2copy;
	private boolean useIdsFromSource;

	/**
	 * Create the wizard.
	 * 
	 * @param cometCombo
	 */
	public CopyWizardDetailPage(NodeId node2copy, CopyWizardNamespacePage namespacePage) {
		super("generalcreatePage");
		setTitle("Neue Methode");
		setDescription("Erstellt eine neue Methode im OPC UA Informationsmodell");
		this.controllCreationToolkit = new DesignerFormToolkit();
		this.node2copy = node2copy;
		// this.selectedParent = selectedParent;
		this.namespacePage = namespacePage;
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
		scrolledFrom.getBody().setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		scrolledFrom.setBusy(false);
		scrolledFrom.setExpandHorizontal(true);
		scrolledFrom.setExpandVertical(true);
		createSectionGeneral(scrolledFrom.getBody());
		fill();
		// createSectionReferences(scrolledFrom.getBody());
	}

	private void createSectionGeneral(Composite container) {
		scrolledFrom.getBody().setLayout(new GridLayout(1, false));
		Composite composite = new Composite(container, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		formToolkit.paintBordersFor(composite);
		composite.setLayout(new GridLayout(3, false));
		// section.setClient(composite);
		// Label * Browsename
		Label lbl = this.controllCreationToolkit.createLabel(composite, "Browsename");
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
			}
		});
		GridData data = (GridData) this.txt_browseName.getControl().getLayoutData();
		data.grabExcessHorizontalSpace = true;
		data.horizontalAlignment = SWT.FILL;
		// Label * Description
		lbl = this.controllCreationToolkit.createLabel(composite, "Description");
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
		lbl = this.controllCreationToolkit.createLabel(composite, "Displayname");
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
		lbl = this.controllCreationToolkit.createLabel(composite, "NodeId");
		// Text * NodeId
		this.txt_nodeId = this.controllCreationToolkit.createTextNodeId(composite,
				this.namespacePage.getComboNamespace(), null);
		((Text) this.txt_nodeId.getControl()).addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				validate();
			}
		});
		data = (GridData) this.txt_nodeId.getControl().getLayoutData();
		data.grabExcessHorizontalSpace = true;
		data.horizontalAlignment = SWT.FILL;
		data.horizontalSpan = 2;
	}

	private void fill() {
		if (!NodeId.isNull(this.node2copy)) {
			Node node = ServerInstance.getNode(this.node2copy);
			QualifiedName bn = node.getBrowseName();
			LocalizedText dn = node.getDisplayName();
			LocalizedText desc = node.getDescription();
			this.txt_namespaceBrowseName.setContents(bn.getNamespaceIndex());
			this.txt_browseName.setContents(bn.getName());
			this.combo_localeDescription.setContents(desc.getLocale());
			this.combo_localeDisplayname.setContents(dn.getLocale());
			if (desc != null && desc.getText() != null) {
				txt_description.setContents(desc.getText());
			}
			if (dn != null && dn.getText() != null) {
				txt_displayName.setContents(dn.getText());
			}
		}
	}

	private void validate() {
		boolean isComplete = isPageComplete();
		setPageComplete(isComplete);
	}

	@Override
	public boolean isPageComplete() {
		boolean validBrowseName = this.txt_browseName.isValid();
		boolean validDescription = this.txt_description.isValid();
		boolean validDisplayName = this.txt_displayName.isValid();
		boolean validNodeId = this.txt_nodeId.isValid();
		boolean validBrowseNameIndex = this.txt_namespaceBrowseName.isValid();
		int nsIndex = ServerInstance.getInstance().getServerInstance().getNamespaceUris()
				.getIndex(this.namespacePage.getComboNamespace().getText());
		boolean isValid = false;
		if (this.useIdsFromSource && this.node2copy.getNamespaceIndex() == nsIndex) {
			return isValid;
		}
		isValid = validBrowseName && validDescription && validDisplayName && validNodeId && validBrowseNameIndex;
		return isValid;
	}

	protected QualifiedName getBrowseName() {
		return new QualifiedName(this.txt_namespaceBrowseName.getContents(), this.txt_browseName.getContents());
	}

	protected LocalizedText getDescriptionText() {
		return new LocalizedText(this.txt_description.getContents(), this.combo_localeDescription.getContents());
	}

	protected LocalizedText getDisplayname() {
		return new LocalizedText(this.txt_displayName.getContents(), this.combo_localeDisplayname.getContents());
	}

	protected NodeId getNodeId() {
		Object identifier = null;
		if (this.useIdsFromSource) {
			identifier = this.node2copy.getValue();
		} else {
			identifier = NodeIdUtil.createIdentifierFromString(this.txt_nodeId.getContents());
		}
		return NodeIdUtil.createNodeId(ServerInstance.getInstance().getServerInstance().getNamespaceUris()
				.getIndex(this.namespacePage.getComboNamespace().getText()), identifier);
	}

	protected ValidatingField<String> getTxtNodeId() {
		return this.txt_nodeId;
	}

	protected void setUseSameIds(boolean useIdsFromSource) {
		this.useIdsFromSource = useIdsFromSource;
		this.txt_nodeId.getControl().setEnabled(!useIdsFromSource);
	}

	public boolean isUseSameIds() {
		return this.useIdsFromSource;
	}
}
