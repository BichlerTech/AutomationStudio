package com.bichler.astudio.opcua.dnd;

import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.widgets.Widget;
import org.opcfoundation.ua.builtintypes.NodeId;

import com.bichler.astudio.opcua.driver.IOPCDriverConfigEditPart;
import com.bichler.astudio.opcua.widget.NodeToTrigger;

public class OPCUADropInTriggerNodesViewAdapter extends OPCUADropInViewAdapter {

	private IOPCDriverConfigEditPart editor;

	public OPCUADropInTriggerNodesViewAdapter(Viewer viewer, IOPCDriverConfigEditPart editor) {
		super(viewer);
		this.editor = editor;
	}

	public boolean setItemValues(Widget item, int index, NodeId nodeId, String name) {
		if (item.getData() instanceof NodeToTrigger) {
			NodeToTrigger node = (NodeToTrigger) item.getData();
			if (node != null) {
				// switch (index) {
				// case 1:
				node.nodeId = nodeId;
				node.displayname = name;
				// ((ColumnViewer) getViewer()).update(node, null);
				// break;
				// case 2:
				// node.setConfigId(nodeId);
				// node.setConfigNodeName(name);
				// ((ColumnViewer) getViewer()).update(node, null);
				// break;
				// }
				((ColumnViewer) getViewer()).update(node, null);
				this.editor.setDirty(true);
				return true;
			}
		}

		return false;
	}

	@Override
	public boolean validateDropInView(DropTargetEvent event) {
		// int index = getIndexFromTableItem(getCurrentEvent());
		//
		// switch (index) {
		// case 1:
		// case 2:
		// return true;
		// }
		return true;
	}

	@Override
	protected boolean setRemoteItemValues(Object node, String data) {

		// nothing to do
		return false;
	}

	@Override
	protected boolean setMoveItemValues(Object node, String data) {
		// TODO Auto-generated method stub
		return false;
	}
}
