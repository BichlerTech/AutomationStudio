package com.bichler.astudio.opcua.opcmodeler.editor.node;

import opc.sdk.core.node.Node;
import opc.sdk.core.node.ReferenceTypeNode;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;
import org.opcfoundation.ua.builtintypes.LocalizedText;

import com.bichler.astudio.opcua.modeller.controls.CometCombo;
import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.utils.internationalization.CustomString;
import com.richclientgui.toolbox.validation.ValidatingField;

public class NodeEditorReferenceTypeContentProvider extends NodeEditorContentProvider {
	private CometCombo combo_isAbstract = null;
	private Boolean isAbstract = true;
	private CometCombo combo_symmetric = null;
	private Boolean symmetric = false;
	private ValidatingField<String> txt_inverseName = null;

	public NodeEditorReferenceTypeContentProvider(NodeEditorPart nodeEditorPart) {
		super(nodeEditorPart);
	}

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
		this.combo_isAbstract = this.controllCreationToolkit.createComboBoolean(widgetGroup1);
		GridDataFactory.fillDefaults().span(2, 1).applyTo(this.combo_isAbstract);
		// Label * Symmetric
		this.controllCreationToolkit.createLabel(widgetGroup1, CustomString
				.getString(Activator.getDefault().RESOURCE_BUNDLE, "NodeEditorReferenceTypePart.lbl_symmetric.text"));
		// Combo * Symmetric
		this.combo_symmetric = this.controllCreationToolkit.createComboBoolean(widgetGroup1);
		GridDataFactory.fillDefaults().span(2, 1).applyTo(combo_symmetric);
		// Label * InverseName
		this.controllCreationToolkit.createLabel(widgetGroup1, CustomString
				.getString(Activator.getDefault().RESOURCE_BUNDLE, "NodeEditorReferenceTypePart.lbl_inverseName.text"));
		this.txt_inverseName = this.controllCreationToolkit.createTextString(widgetGroup1);
		GridDataFactory.fillDefaults().span(2, 1).applyTo(this.txt_inverseName.getControl());
		// ---------------------------------------------------------//
		// Label * Reference
		this.controllCreationToolkit.createLabel(composite,
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "NodeEditorPart.sctn_references.text"));
		// Table * Reference
		this.tableViewer = this.controllCreationToolkit.createReferenceTypeReferenceTable(composite, node,
				this.callBackEditor);
		createReferenceTableButtonSection(composite, node);
		createErrorLabel(composite);
		composite.layout();
		composite.setSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT, true));
		if (!isEditable(node)) {
			disableEditableSection(widgetGroup1);
		}
	}

	protected void setNodeInput(Node node) {
		setBaseNodeInput(node);
		setReferenceTableNodeInput(node);
		this.isAbstract = ((ReferenceTypeNode) node).getIsAbstract();
		int index = 0;
		for (int i = 0; i < this.combo_isAbstract.getItems().length; i++) {
			String item = this.combo_isAbstract.getItems()[i];
			if (item.equalsIgnoreCase(isAbstract.toString())) {
				index = i;
				break;
			}
		}
		this.combo_isAbstract.select(index);
		this.combo_isAbstract.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				callBackEditor.setDirty(true);
			}
		});
		this.combo_isAbstract.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				isAbstract = new Boolean(((Combo) e.getSource()).getText());
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
			}
		});
		Boolean isSymmetric = ((ReferenceTypeNode) node).getSymmetric();
		index = 0;
		for (int i = 0; i < this.combo_symmetric.getItems().length; i++) {
			String item = this.combo_symmetric.getItems()[i];
			if (item.equalsIgnoreCase(isSymmetric.toString())) {
				index = i;
				break;
			}
		}
		this.combo_symmetric.select(index);
		this.combo_symmetric.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				callBackEditor.setDirty(true);
			}
		});
		this.combo_symmetric.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				symmetric = new Boolean(((Combo) e.getSource()).getText());
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
			}
		});
		String inverseName = ((ReferenceTypeNode) node).getInverseName().getText();
		if (inverseName == null) {
			inverseName = "";
		}
		this.txt_inverseName.setContents(inverseName);
		((Text) this.txt_inverseName.getControl()).addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				callBackEditor.setDirty(true);
			}
		});
	}

	/**
	 * Saves a reference type node
	 * 
	 * @param node
	 */
	public void doSave(Node node) {
		// save the default node attributes
		doSaveNodeBaseAttributes(node);
		// cast the node
		ReferenceTypeNode referenceTypeNode = (ReferenceTypeNode) node;
		// construct the new changed values
		LocalizedText inverseName = createInverseName(referenceTypeNode);
		referenceTypeNode.setInverseName(inverseName);
		referenceTypeNode.setIsAbstract(this.isAbstract);
		referenceTypeNode.setSymmetric(this.symmetric);
	}

	private LocalizedText createInverseName(ReferenceTypeNode node) {
		String text = this.txt_inverseName.getContents();
		String localeId = node.getInverseName().getLocaleId();
		LocalizedText inverseName = new LocalizedText(text, localeId);
		return inverseName;
	}

	public boolean valid() {
		if (this.txt_inverseName != null) {
			if (!this.txt_inverseName.isValid()) {
				return false;
			}
		}
		return super.valid();
	}
}
