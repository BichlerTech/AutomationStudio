package com.bichler.astudio.opcua;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;

import com.bichler.astudio.opcua.opcmodeler.singletons.type.INamespaceTableChange;
import com.bichler.astudio.opcua.opcmodeler.commands.NamespaceTableChangeParameter;

public abstract class AbstractOPCDriverConfigSelectionEditorPart extends AbstractOPCConfigDriverViewLinkEditorPart
		implements ISelectionProvider, INamespaceTableChange {

	protected ISelectionChangedListener listener;
	protected Object currentSelection;

	public AbstractOPCDriverConfigSelectionEditorPart() {

	}

	protected void setSelectionBO(Object selection) {
		this.currentSelection = selection;
		selectionChanged();
	}

	private void selectionChanged() {
		if (listener != null)
			listener.selectionChanged(new SelectionChangedEvent(this, getSelection()));
	}

	@Override
	public void onNamespaceChange(NamespaceTableChangeParameter trigger) {

	}

}
