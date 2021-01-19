package com.bichler.astudio.opcua.opcmodeler.wizards;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.wb.swt.SWTResourceManager;
import org.opcfoundation.ua.builtintypes.NodeId;

import com.bichler.astudio.opcua.modeller.controls.CometCombo;
import com.bichler.astudio.opcua.opcmodeler.editor.node.DesignerFormToolkit;
import com.bichler.astudio.opcua.opcmodeler.editor.node.validation.errors.NodeIdTextFieldQuickFixProvider;
import com.richclientgui.toolbox.validation.ValidatingField;

import opc.sdk.core.node.Node;

public class CopyWizardNamespacePage extends WizardPage {
	private FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private DesignerFormToolkit controllCreationToolkit = null;
	private ScrolledForm scrolledFrom;
	private Node selectedParent;
	private CometCombo combo_nodeId;
	private CopyWizardDetailPage detailPage;
	private Button cb_parent;
	private boolean useSameIds;

	/**
	 * Create the wizard.
	 */
	public CopyWizardNamespacePage(Node selectedParent) {
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
		scrolledFrom.getBody().setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		scrolledFrom.setBusy(false);
		// scrolledFrom.getBody().setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		scrolledFrom.setExpandHorizontal(true);
		scrolledFrom.setExpandVertical(true);
		createSectionGeneral(scrolledFrom.getBody());
		fill();
	}

	private void createSectionGeneral(Composite container) {
		container.setLayout(new GridLayout(1, false));
		// Combo * NodeId
		this.combo_nodeId = this.controllCreationToolkit.createComboNodeId(container,
				this.selectedParent.getNodeId().getNamespaceIndex());
		this.combo_nodeId.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		this.combo_nodeId.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				detailPage.getTxtNodeId().setContents("");
				new NodeIdTextFieldQuickFixProvider<NodeId>(combo_nodeId).doQuickFix(detailPage.getTxtNodeId());
				detailPage.getTxtNodeId().validate();
			}
		});
		this.cb_parent = new Button(container, SWT.CHECK);
		this.cb_parent.setText("Copy with same nodeids to a different namespace");
		this.cb_parent.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		this.cb_parent.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				useSameIds = ((Button) e.getSource()).getSelection();
				detailPage.setUseSameIds(useSameIds);
			}
		});
	}

	private void fill() {
	}

	@Override
	public boolean isPageComplete() {
		return true;
	}

	protected CometCombo getComboNamespace() {
		return this.combo_nodeId;
	}

	protected void setDetailPage(CopyWizardDetailPage detailPage) {
		this.detailPage = detailPage;
	}
}
