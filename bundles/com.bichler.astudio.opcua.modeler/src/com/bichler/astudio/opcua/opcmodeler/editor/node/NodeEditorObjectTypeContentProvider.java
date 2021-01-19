package com.bichler.astudio.opcua.opcmodeler.editor.node;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

import com.bichler.astudio.utils.internationalization.CustomString;
import com.bichler.astudio.utils.ui.swt.CheckBoxButton;
import com.bichler.astudio.opcua.opcmodeler.Activator;

import opc.sdk.core.node.Node;
import opc.sdk.core.node.ObjectTypeNode;
import opc.sdk.core.node.UAObjectTypeNode;

public class NodeEditorObjectTypeContentProvider extends NodeEditorContentProvider {
	private CheckBoxButton cb_isAbstract = null;

	public NodeEditorObjectTypeContentProvider(NodeEditorPart nodeEditorPart) {
		super(nodeEditorPart);
	}

	@Override
	protected void createContent(Node node, ScrolledComposite parent) {
		Composite composite = new Composite(parent, SWT.SHADOW_ETCHED_IN);
		parent.setContent(composite);
		GridLayoutFactory.fillDefaults().numColumns(1).applyTo(composite);
		Group nodeGroup = new Group(composite, SWT.NONE);
		createBaseNodeContent(nodeGroup, node);
		// Label * Is Abstract
		this.controllCreationToolkit.createLabel(nodeGroup, CustomString
				.getString(Activator.getDefault().RESOURCE_BUNDLE, "NodeEditorDataTypePart.lbl_isAbstract.text"));
		// Combo * Is Abstract
		this.cb_isAbstract = new CheckBoxButton(nodeGroup, SWT.NONE);
		GridDataFactory.fillDefaults().span(2, 1).applyTo(this.cb_isAbstract);
		this.cb_isAbstract.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				callBackEditor.setDirty(true);
			}
		});
		// ---------------------------------------------------------//
		// Label * Reference
		this.controllCreationToolkit.createLabel(composite, CustomString
				.getString(Activator.getDefault().RESOURCE_BUNDLE, "NodeEditorPart.tblclmn_reference.text"));
		// Table * Reference
		this.tableViewer = this.controllCreationToolkit.createObjectTypeReferenceTable(composite, node,
				this.callBackEditor);
		createReferenceTableButtonSection(composite, node);
		createErrorLabel(composite);
		composite.setSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT, true));
		composite.layout();
		if (!isEditable(node)) {
			disableEditableSection(nodeGroup);
		}
	}

	@Override
	protected void setNodeInput(Node node) {
		setBaseNodeInput(node);
		setReferenceTableNodeInput(node);
		this.cb_isAbstract.setChecked(((ObjectTypeNode) node).getIsAbstract());
	}

	@Override
	public void doSave(Node node) {
		// save the default node attributes
		doSaveNodeBaseAttributes(node);
		// cast the node
		UAObjectTypeNode objTypeNode = (UAObjectTypeNode) node;
		// change the object type node
		objTypeNode.setIsAbstract(this.cb_isAbstract.isChecked());
	}
}
