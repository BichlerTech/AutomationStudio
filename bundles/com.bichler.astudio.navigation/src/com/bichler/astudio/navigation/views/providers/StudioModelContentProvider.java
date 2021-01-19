package com.bichler.astudio.navigation.views.providers;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.bichler.astudio.navigation.nodes.StudioModelNode;

public class StudioModelContentProvider implements ITreeContentProvider {

	public StudioModelContentProvider() {
	}

	@Override
	public void dispose() {

	}

	/**
	 * return the root element for comet unified studio
	 */
	@Override
	public Object[] getElements(Object inputElement) {

		// return getChildren(inputElement);
		return getChildren(inputElement);
	}

	/**
	 * Get all children of selected tree node.
	 * 
	 */
	@Override
	public Object[] getChildren(Object parentElement) {
		if (parentElement != null && parentElement instanceof StudioModelNode) {
			boolean isShow = ((StudioModelNode) parentElement)
					.showChildren(this);

			if (isShow) {
				return ((StudioModelNode) parentElement).getChildren();
			}
		}

		return new Object[0];
	}

	@Override
	public Object getParent(Object element) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		return this.getChildren(element).length > 0;
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// TODO Auto-generated method stub

	}
}
