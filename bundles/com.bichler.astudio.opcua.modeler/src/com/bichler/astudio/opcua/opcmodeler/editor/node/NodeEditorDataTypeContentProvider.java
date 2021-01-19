package com.bichler.astudio.opcua.opcmodeler.editor.node;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.utils.internationalization.CustomString;
import com.bichler.astudio.utils.ui.swt.CheckBoxButton;

import opc.sdk.core.node.DataTypeNode;
import opc.sdk.core.node.Node;
import opc.sdk.core.node.UADataTypeNode;

public class NodeEditorDataTypeContentProvider extends NodeEditorContentProvider {
	private CheckBoxButton cb_isAbstract;

	public NodeEditorDataTypeContentProvider(NodeEditorPart nodeEditorPart) {
		super(nodeEditorPart);
	}

	@Override
	protected void createContent(Node node, ScrolledComposite parent) {
		Composite composite = new Composite(parent, SWT.SHADOW_ETCHED_IN);
		parent.setContent(composite);
		GridLayoutFactory.fillDefaults().numColumns(1).applyTo(composite);
		Group widgetGroup1 = new Group(composite, SWT.NONE);
		createBaseNodeContent(widgetGroup1, node);
		// Label * Is Abstract
		this.controllCreationToolkit.createLabel(widgetGroup1, CustomString
				.getString(Activator.getDefault().RESOURCE_BUNDLE, "NodeEditorDataTypePart.lbl_isAbstract.text"));
		// Combo * Is Abstract
		this.cb_isAbstract = new CheckBoxButton(widgetGroup1, SWT.NONE);
		GridDataFactory.fillDefaults().span(2, 1).applyTo(this.cb_isAbstract);
		this.cb_isAbstract.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				callBackEditor.setDirty(true);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
			}
		});
		// Label * Reference
		this.controllCreationToolkit.createLabel(composite,
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "NodeEditorPart.sctn_references.text"));
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
		this.cb_isAbstract.setChecked(((DataTypeNode) node).getIsAbstract());
	}

	@Override
	public void doSave(Node node) {
		// save the default node attributes
		doSaveNodeBaseAttributes(node);
		// cast the node
		UADataTypeNode dataTypeNode = (UADataTypeNode) node;
		// construct the new changed values
		dataTypeNode.setIsAbstract(this.cb_isAbstract.isChecked());
	}
}
