package com.bichler.astudio.opcua.opcmodeler.editor.node;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

import com.bichler.astudio.utils.internationalization.CustomString;
import com.bichler.astudio.utils.ui.swt.CheckBoxButton;
import com.bichler.astudio.opcua.opcmodeler.Activator;

import opc.sdk.core.node.MethodNode;
import opc.sdk.core.node.Node;
import opc.sdk.core.node.UAMethodNode;

public class NodeEditorMethodContentProvider extends NodeEditorContentProvider {
	// private Boolean executeable = false;
	// private Boolean userExecuteable = false;
	private CheckBoxButton cb_executeable = null;
	private CheckBoxButton cb_userExecuteable = null;

	public NodeEditorMethodContentProvider(NodeEditorPart nodeEditorPart) {
		super(nodeEditorPart);
	}

	@Override
	protected void createContent(Node node, ScrolledComposite parent) {
		Composite composite = new Composite(parent, SWT.SHADOW_ETCHED_IN);
		parent.setContent(composite);
		GridLayoutFactory.fillDefaults().numColumns(1).applyTo(composite);
		Group widgetGroup1 = new Group(composite, SWT.NONE);
		createBaseNodeContent(widgetGroup1, node);
		// Label * Executeable
		this.controllCreationToolkit.createLabel(widgetGroup1, CustomString
				.getString(Activator.getDefault().RESOURCE_BUNDLE, "NodeEditorMethodPart.lbl_executeable.text"));
		// Combo * Executeable
		this.cb_executeable = new CheckBoxButton(widgetGroup1, SWT.NONE);
		GridDataFactory.fillDefaults().span(2, 1).applyTo(this.cb_executeable);
		this.cb_executeable.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				callBackEditor.setDirty(true);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
			}
		});
		// Label * UserExecuteable
		this.controllCreationToolkit.createLabel(widgetGroup1, CustomString
				.getString(Activator.getDefault().RESOURCE_BUNDLE, "NodeEditorMethodPart.lbl_userExecuteable.text"));
		this.cb_userExecuteable = new CheckBoxButton(widgetGroup1, SWT.NONE);
		GridDataFactory.fillDefaults().span(2, 1).applyTo(this.cb_userExecuteable);
		this.cb_userExecuteable.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				callBackEditor.setDirty(true);
			}
		});
		// Label * Reference
		this.controllCreationToolkit.createLabel(composite, CustomString
				.getString(Activator.getDefault().RESOURCE_BUNDLE, "CreateVariableDialog.lbl_references.text"));
		// Table * Reference
		this.tableViewer = this.controllCreationToolkit.createDataTypeReferenceTable(composite, node,
				this.callBackEditor);
		createReferenceTableButtonSection(composite, node);
		createErrorLabel(composite);
		composite.layout();
		composite.setSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT, true));
		if (!isEditable(node)) {
			disableEditableSection(widgetGroup1);
		}
	}

	@Override
	protected void setNodeInput(Node node) {
		setBaseNodeInput(node);
		setReferenceTableNodeInput(node);
		this.cb_userExecuteable.setChecked(((MethodNode) node).getExecutable());
		this.cb_userExecuteable.setChecked(((MethodNode) node).getUserExecutable());
	}

	@Override
	public void doSave(Node node) {
		// save the default node attributes
		doSaveNodeBaseAttributes(node);
		// cast the node
		UAMethodNode methodNode = (UAMethodNode) node;
		methodNode.setExecutable(this.cb_executeable.isChecked());
		methodNode.setUserExecutable(this.cb_userExecuteable.isChecked());
	}
}
