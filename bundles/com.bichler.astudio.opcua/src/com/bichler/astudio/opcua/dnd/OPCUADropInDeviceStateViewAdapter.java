package com.bichler.astudio.opcua.dnd;

import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.widgets.Widget;
import org.opcfoundation.ua.builtintypes.NodeId;

import com.bichler.astudio.opcua.driver.IOPCDriverConfigEditPart;
import com.bichler.astudio.opcua.widget.model.AbstractConfigNode;
import com.bichler.astudio.opcua.widget.model.AdvancedConfigurationNode;

public class OPCUADropInDeviceStateViewAdapter extends OPCUADropInViewAdapter {

	private IOPCDriverConfigEditPart editor;

	public OPCUADropInDeviceStateViewAdapter(Viewer viewer, IOPCDriverConfigEditPart editor) {
		super(viewer);
		this.editor = editor;
	}

	@Override
	public boolean setItemValues(Widget item, int index, NodeId nodeId, String name) {

		if (item.getData() instanceof AdvancedConfigurationNode) {
			AdvancedConfigurationNode node = (AdvancedConfigurationNode) item.getData();
			if (node != null) {
				boolean isDirty = false;
				switch (index) {
				case 0:
				case 1:
				case 2:
					node.setConfigNodeName(name);
					node.setConfigId(nodeId);
					String path = getBrowsePath(nodeId);
					node.setBrowsePath(path);
					// node.setDeviceId(nodeId);
					// node.setDeviceName(name);
					// ((ColumnViewer) getViewer()).update(node, null);
					isDirty = true;
					break;

				// case 2:
				// node.setConfigId(nodeId);
				// node.setConfigNodeName(name);
				// ((ColumnViewer) getViewer()).update(node, null);
				// isDirty = true;
				// break;
				}
				((ColumnViewer) getViewer()).update(node, null);
				if (isDirty) {
					this.editor.setDirty(true);
				}
				// this.editor.setDirty(true);
				return true;
			}
		}

		return false;
	}

	@Override
	public boolean validateDropInView(DropTargetEvent event) {
//		int index = 
		getIndexFromTableItem(event);
		Widget widget = event.item;
		Object data2 = widget.getData();

		if (data2 instanceof AbstractConfigNode) {
			return true;
		}

		//
		// switch (index) {
		// case 1:
		// case 2:
		// case 3:
		// break;
		// default:
		// break;
		// }
		// Object item = event.getSource();
		// Object data = event.data;
		return false;
	}

	@Override
	protected boolean setRemoteItemValues(Object node, String data) {
		if (data == null) {
			return false;
		}

		if (!(node instanceof AdvancedConfigurationNode)) {
			return false;
		}

		String[] datas = data.split("%d%");
		String name = datas[0];
		String datatype = datas[1];
		String address = datas[3];

		((AdvancedConfigurationNode) node).setDeviceName(name);
		((AdvancedConfigurationNode) node).setDataType(datatype);
		((AdvancedConfigurationNode) node).setRefStartAddress(address);
		((ColumnViewer) getViewer()).update(node, null);

		this.editor.setDirty(true);
		return true;
	}

	@Override
	protected boolean setMoveItemValues(Object node, String data) {
		// TODO Auto-generated method stub
		return false;
	}

}
