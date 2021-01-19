package com.bichler.astudio.editor.pubsub.wizard;

import org.eclipse.jface.wizard.Wizard;

import com.bichler.astudio.editor.pubsub.nodes.PubSubEntryModelNode;

public abstract class AbstractPubSubWizard extends Wizard {

	private boolean flagDirty = false;
	private PubSubEntryModelNode element = null;
	
	protected AbstractPubSubWizard(PubSubEntryModelNode element) {
		super();
		this.element = element;
	}
	
	public boolean isDirty() {
		return this.flagDirty;
	}

	void setIfDirty(Object left, Object right) {
		boolean same = true;

		if (left == null) {
			same = right == null;
		} else {
			same = left.equals(right);
		}
		if (!same) {
			this.flagDirty = true;
		}
	}

	public PubSubEntryModelNode getElement() {
		return this.element;
	}

	public void setElement(PubSubEntryModelNode element) {
		this.element = element;
	}
	
}
