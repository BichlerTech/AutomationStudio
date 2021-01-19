package com.bichler.astudio.opcua.navigation.views.provider;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;

import com.bichler.astudio.navigation.nodes.StudioModelNode;

public class OPCModelContentProvider implements ITreeContentProvider {

	private TreeViewer viewer;

	public OPCModelContentProvider() {
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	/**
	 * return the root element for comet unified studio
	 */
	@Override
	public Object[] getElements(Object inputElement) {
//		System.out.println();
//		return new Object[]{inputElement};
		return getChildren(inputElement);
	}

	/**
	 * Get all children of selected tree node.
	 * 
	 */
	@Override
	public Object[] getChildren(Object parentElement) {
		if (parentElement != null && parentElement instanceof StudioModelNode) {
			return ((StudioModelNode) parentElement).getChildren();
		}

		return new Object[0];
	}

	@Override
	public Object getParent(Object element) {
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		return this.getChildren(element).length > 0;
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

	}

}
