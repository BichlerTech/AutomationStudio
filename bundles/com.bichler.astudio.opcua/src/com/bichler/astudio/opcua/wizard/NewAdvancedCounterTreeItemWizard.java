package com.bichler.astudio.opcua.wizard;

import org.eclipse.jface.wizard.Wizard;

import com.bichler.astudio.opcua.widget.model.AdvancedConfigurationNode;
import com.bichler.astudio.opcua.widget.model.AdvancedRootConfigurationNode;
import com.bichler.astudio.opcua.widget.model.AdvancedSectionType;
import com.bichler.astudio.opcua.wizard.page.NewAdvancedTreeItemPage;
import com.bichler.astudio.opcua.wizard.page.NewAdvancedTreeItemPage.DevGrType;
import com.bichler.astudio.utils.internationalization.CustomString;
import com.bichler.astudio.opcua.OPCUAActivator;

public class NewAdvancedCounterTreeItemWizard extends Wizard {

	private NewAdvancedTreeItemPage page1;
	private AdvancedConfigurationNode selection;
	private AdvancedRootConfigurationNode root;
	private AdvancedConfigurationNode newChild;

	public NewAdvancedCounterTreeItemWizard() {
		setWindowTitle(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "wizard.countertree.title"));
	}

	@Override
	public void addPages() {
		this.page1 = new NewAdvancedTreeItemPage(this.root, this.selection);
		addPage(this.page1);
	}

	@Override
	public boolean performFinish() {
		DevGrType type = this.page1.getType();

		if (type == null) {
			return true;
		}

		this.newChild = new AdvancedConfigurationNode(AdvancedSectionType.Counter);

		switch (type) {
		// group
		case Group:
			newChild.setGroupName("...");
			newChild.setGroup(true);
			this.root.addChild(newChild);

			break;
		// device
		case Device:
			newChild.setDeviceName("...");
			newChild.setDevice(true);

			// add when device is selected
			if (this.selection.isDevice()) {
				// find parent and add
				Object parent = this.selection.getParent();

				if (parent instanceof AdvancedConfigurationNode) {
					((AdvancedConfigurationNode) parent).addChild(newChild);
				} else {
					throw new IllegalArgumentException("Cannot");
				}
			}
			// add when group is selected
			else if (this.selection.isGroup()) {
				// add device in the group
				this.selection.addChild(newChild);
			}

			break;
		// state
		case Counter:
			newChild.setCounter("...");
			newChild.setCounter(true);

			// add counter when device is selected
			if (this.selection.isDevice()) {
				this.selection.addChild(newChild);
			}

			else if (this.selection.isState()) {
				Object parent = this.selection.getParent();
				((AdvancedConfigurationNode) parent).addChild(newChild);
			}

			break;
		}

		return true;
	}

	public Object getResult() {
		return newChild;
	}

	public Object getResultParent() {
		return newChild.getParent();
	}

	public void setSelection(Object selection) {
		this.selection = (AdvancedConfigurationNode) selection;
	}

	public boolean setRoot(Object input) {
		if (input == null) {
			root = new AdvancedRootConfigurationNode(AdvancedSectionType.Counter);
			return true;
		} else {
			this.root = (AdvancedRootConfigurationNode) input;
			return false;
		}
	}

	public Object getRoot() {
		return this.root;
	}

}
