package com.bichler.astudio.opcua.opcmodeler.editor.node;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;
import org.opcfoundation.ua.builtintypes.LocalizedText;

import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.nodes.BrowserModelNode;
import com.bichler.astudio.utils.internationalization.CustomString;

public class NodeEditorInput implements IEditorInput {
	private BrowserModelNode node = null;

	public NodeEditorInput(BrowserModelNode node, boolean checkEquals) {
		super();
		this.node = node;
	}

	@Override
	public Object getAdapter(Class adapter) {
		// Editor-Input-Objekte können optional adaptierbar gestaltet werden
		return null;
	}

	@Override
	public boolean exists() {
		return false;
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		// Activator.getImageDescriptor("/icons/someicon.png");
		return null;
	}

	@Override
	public String getName() {
		String name = null;
		try {
			LocalizedText displayName = this.node.getNode().getDisplayName();
			if (displayName == null) {
				name = this.node.getNode().getNodeClass().name();
			} else {
				name = this.node.getNode().getDisplayName().getText();
			}
		} catch (NullPointerException npe) {
		}
		return name == null ? CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "form.closed") : name;
	}

	@Override
	public IPersistableElement getPersistable() {
		// getPersistable muss nur implementiert werden, wenn das
		// Editor-Inputobjekt
		// über mehrere Anwendungssessions hinweg gelten soll, z.B. wenn eine
		// "Zuletzt geöffnete Dokumente"-Liste verwendet wird.
		return null;
	}

	@Override
	public String getToolTipText() {
		return this.node.getNode().getNodeClass().toString() + " - " + getName();
	}

	// equals und hashCode müssen implementiert werden, damit nur ein
	// Editor für dasselbe Dokumente geöffnet werden kann
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		return (this.node == ((NodeEditorInput) obj).node);
	}

	@Override
	public int hashCode() {
		// if (checkEquals) {
		return super.hashCode();
		// } else {
		// return 0;
		// }
	}

	public BrowserModelNode getNode() {
		return this.node;
	}
}
