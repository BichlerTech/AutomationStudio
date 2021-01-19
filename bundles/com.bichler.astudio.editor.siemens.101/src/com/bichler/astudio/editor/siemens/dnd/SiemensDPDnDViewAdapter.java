package com.bichler.astudio.editor.siemens.dnd;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Widget;
import org.opcfoundation.ua.builtintypes.NodeId;

import com.bichler.astudio.editor.siemens.driver.SiemensDriverDragSupport;
import com.bichler.astudio.editor.siemens.xml.SiemensEntryModelNode;
import com.bichler.astudio.opcua.dnd.OPCUADropInViewAdapter;
import com.bichler.astudio.opcua.driver.IOPCDataPointEditPart;

public class SiemensDPDnDViewAdapter extends OPCUADropInViewAdapter {
	private IOPCDataPointEditPart editor;

	public SiemensDPDnDViewAdapter(Viewer viewer, IOPCDataPointEditPart editor) {
		super(viewer);
		this.editor = editor;
	}

	@Override
	public boolean setItemValues(Widget item, int index, NodeId nodeId, String name) {
		SiemensEntryModelNode dp = (SiemensEntryModelNode) ((TableItem) item).getData();
		dp.setNodeId(nodeId);
		dp.setDisplayname(name);
		String browsepath = getBrowsePath(dp.getNId());
		dp.setBrowsepath(browsepath);
		((TableViewer) getViewer()).update(dp, null);
		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				editor.setDirty(true);
			}
		});
		return true;
	}

	@Override
	protected boolean setRemoteItemValues(Object node, String data) {
		SiemensEntryModelNode dp = (SiemensEntryModelNode) node;
		String[] attributes = SiemensDriverDragSupport.convertTextToAttributes(data);
		SiemensDriverDragSupport.setAttributesToNode(dp, attributes);
		((TableViewer) getViewer()).update(dp, null);
		this.editor.setDirty(true);
		return true;
	}

	@Override
	public boolean validateDropInView(DropTargetEvent event) {
		return true;
	}

	@Override
	protected boolean setMoveItemValues(Object node, String data) {
		return false;
	}
}
