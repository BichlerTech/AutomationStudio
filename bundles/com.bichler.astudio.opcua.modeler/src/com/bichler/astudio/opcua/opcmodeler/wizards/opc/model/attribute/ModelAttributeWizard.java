package com.bichler.astudio.opcua.opcmodeler.wizards.opc.model.attribute;

import opc.sdk.core.node.Node;

import org.eclipse.jface.wizard.Wizard;
import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;

import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.utils.internationalization.CustomString;
import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.opcua.opcmodeler.editor.node.models.change.ModelTreeDef;
import com.bichler.astudio.opcua.opcmodeler.editor.node.models.change.ModelTypDef;

public class ModelAttributeWizard extends Wizard {
	private ModelTypDef typeDef;
	private ModelAttributeElementPage pageOne;
	private Object value2update;
	private UnsignedInteger attributeId;

	public ModelAttributeWizard() {
		setWindowTitle(
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "wizard.modelchange.attribute.title"));
	}

	@Override
	public void addPages() {
		this.pageOne = new ModelAttributeElementPage();
		this.pageOne.setTypeDef(this.typeDef);
		addPage(pageOne);
	}

	@Override
	public boolean performFinish() {
		Object changedNodes = this.pageOne.getInput();
		for (ModelTreeDef def : (ModelTreeDef[]) changedNodes) {
			ExpandedNodeId nodeId2change = def.getNodeId();
			Node node2change = ServerInstance.getNode(nodeId2change);
			// change
			node2change.write(attributeId, value2update);
		}
		return true;
	}

	public void setTypeDef(ModelTypDef ptd) {
		this.typeDef = ptd;
	}

	public void setValue2update(Object value2update) {
		this.value2update = value2update;
	}

	public void setAttributeId(UnsignedInteger attributeId) {
		this.attributeId = attributeId;
	}
}
