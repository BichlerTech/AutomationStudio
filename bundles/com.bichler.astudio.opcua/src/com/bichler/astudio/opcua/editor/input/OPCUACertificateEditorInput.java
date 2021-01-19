package com.bichler.astudio.opcua.editor.input;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import com.bichler.astudio.opcua.nodes.security.AbstractOPCUACertificateModelNode;

public class OPCUACertificateEditorInput implements IEditorInput {

	private AbstractOPCUACertificateModelNode node = null;

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		return this.node == ((OPCUACertificateEditorInput) obj).node;
	}

	public OPCUACertificateEditorInput() {
	}

	@Override
	public Object getAdapter(@SuppressWarnings("rawtypes") Class adapter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean exists() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return node.getServerName();
	}

	@Override
	public IPersistableElement getPersistable() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getToolTipText() {
		// TODO Auto-generated method stub
		return "test";
	}

	public AbstractOPCUACertificateModelNode getNode() {
		return node;
	}

	public void setNode(AbstractOPCUACertificateModelNode node) {
		this.node = node;
	}

	public Object getCertificate() {
		return this.node.getCertificate();
	}
}
