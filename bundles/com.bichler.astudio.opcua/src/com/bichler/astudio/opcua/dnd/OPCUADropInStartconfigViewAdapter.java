package com.bichler.astudio.opcua.dnd;

import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.widgets.Widget;
import org.opcfoundation.ua.builtintypes.NodeId;

import com.bichler.astudio.opcua.driver.IOPCDriverConfigEditPart;
import com.bichler.astudio.opcua.widget.model.AdvancedConfigurationNode;

public class OPCUADropInStartconfigViewAdapter extends OPCUADropInViewAdapter {

	private IOPCDriverConfigEditPart editor;

	public OPCUADropInStartconfigViewAdapter(Viewer viewer, IOPCDriverConfigEditPart editor) {
		super(viewer);
		this.editor = editor;
	}

	public boolean setItemValues(Widget item, int index, NodeId nodeId, String name) {
		if (item.getData() instanceof AdvancedConfigurationNode) {
			AdvancedConfigurationNode node = (AdvancedConfigurationNode) item.getData();
			if (node != null) {
				boolean isDirty = false;
				switch (index) {
				case 1:
					node.setDeviceId(nodeId);
					node.setDeviceName(name);
					((ColumnViewer) getViewer()).update(node, null);
					isDirty = true;
					break;
				case 2:
					node.setConfigId(nodeId);
					node.setConfigNodeName(name);
					((ColumnViewer) getViewer()).update(node, null);
					isDirty = true;
					break;
				}
				((ColumnViewer) getViewer()).update(node, null);
				if (isDirty) {
					this.editor.setDirty(true);
				}
//				this.editor.setDirty(true);
				return true;
			}
		}

		return false;
	}

	@Override
	public boolean validateDropInView(DropTargetEvent event) {
		int index = getIndexFromTableItem(getCurrentEvent());

		switch (index) {
		case 1:
		case 2:
			return true;
		}
		return false;
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
