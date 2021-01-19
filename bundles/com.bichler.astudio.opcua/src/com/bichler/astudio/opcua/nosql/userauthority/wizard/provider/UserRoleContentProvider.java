package com.bichler.astudio.opcua.nosql.userauthority.wizard.provider;

import java.util.HashMap;
import java.util.Map;

import opc.sdk.core.node.user.DBAuthority;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.opcfoundation.ua.builtintypes.NodeId;

import com.bichler.astudio.opcua.nosql.userauthority.wizard.model.AbstractOPCUserModel;

public class UserRoleContentProvider implements ITreeContentProvider {

	private Map<NodeId, DBAuthority> mapping = new HashMap<>();

	public UserRoleContentProvider(Map<NodeId, DBAuthority> mapping) {
		this.mapping = mapping;
	}

	@Override
	public void dispose() {

	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

	}

	@Override
	public Object[] getElements(Object inputElement) {
		return getChildren(inputElement);
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof AbstractOPCUserModel) {
			((AbstractOPCUserModel) parentElement).fetchChildren(false);
			((AbstractOPCUserModel) parentElement).mapChildren(this.mapping);
			return ((AbstractOPCUserModel) parentElement).getChildren();
		}

		return new Object[0];
	}

	@Override
	public Object getParent(Object element) {
		if (element instanceof AbstractOPCUserModel) {
			return ((AbstractOPCUserModel) element).getParent();
		}

		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		return getChildren(element).length > 0;
	}

}
