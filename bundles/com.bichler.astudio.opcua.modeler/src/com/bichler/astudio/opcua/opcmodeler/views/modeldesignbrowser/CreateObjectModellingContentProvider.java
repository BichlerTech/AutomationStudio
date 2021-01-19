package com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser;

import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.core.NodeClass;

import com.bichler.astudio.opcua.opcmodeler.dialogs.utils.CreateNodeUtil;

public class CreateObjectModellingContentProvider implements ITreeContentProvider {
	private UnsignedInteger nodeClassFilter = null;

	// public CreateObjectModellingContentProvider(UnsignedInteger
	// nodeClassFilter) {
	// this.nodeClassFilter = nodeClassFilter;
	// }
	public CreateObjectModellingContentProvider() {
		this.nodeClassFilter = NodeClass.getMask(NodeClass.Variable, NodeClass.Object, NodeClass.Method);
	}

	public CreateObjectModellingContentProvider(List<NodeId> hierarchie) {
		this();
	}

	@Override
	public Object[] getElements(Object inputElement) {
		return getChildren(inputElement);
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		return CreateNodeUtil.getNodeChildrenForTreeViewer(parentElement, this.nodeClassFilter);
	}

	@Override
	public Object getParent(Object element) {
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		return getChildren(element).length > 0;
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

	@Override
	public void dispose() {
	}
}
