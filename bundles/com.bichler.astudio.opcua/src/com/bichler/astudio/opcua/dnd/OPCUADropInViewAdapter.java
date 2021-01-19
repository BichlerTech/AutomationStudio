package com.bichler.astudio.opcua.dnd;

import java.util.Deque;
import java.util.EnumSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import opc.sdk.core.node.Node;

import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.jface.viewers.ViewerDropAdapter;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TransferData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Widget;
import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.Attributes;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.core.ReadValueId;
import org.opcfoundation.ua.core.TimestampsToReturn;

import com.bichler.astudio.opcua.components.ui.BrowsePathElement;
import com.bichler.astudio.opcua.components.ui.dialogs.OPCUABrowseUtils;
import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;

public abstract class OPCUADropInViewAdapter extends ViewerDropAdapter {

	private EnumSet<NodeClass> filter;

	public OPCUADropInViewAdapter(Viewer viewer) {
		super(viewer);

		this.filter = NodeClass.getSet(NodeClass.getMask(NodeClass.Variable));
	}

	// public OPCUADropInViewAdapter(Viewer viewer, EnumSet<NodeClass> filter) {
	// super(viewer);
	//
	// this.filter = filter;
	// }

	protected boolean performDropFilter(Node node, Widget item, int index) {
		return this.filter.contains(node.getNodeClass());
	}

	public abstract boolean setItemValues(Widget item, int index, NodeId nodeId, String name);

	public abstract boolean validateDropInView(DropTargetEvent event);

	@Override
	public boolean performDrop(Object data) {
		DropTargetEvent event = getCurrentEvent();

		int index = getIndexFromTableItem(event);
		Widget item = event.item;

		if (item == null) {
			return false;
		}

		if (data instanceof String) {
			NodeId nodeId = NodeId.NULL;
			String name = "";
			// remove
			boolean isRemote = ((String) data).startsWith("%remote%");
			if (isRemote) {
				String drop = ((String) data).replaceFirst("%remote%", "");
				return setRemoteItemValues(item.getData(), drop);
			}
			// move
			boolean ismove = ((String) data).startsWith("%move%");
			if (ismove) {
				String drop = ((String) data).replaceFirst("%move%", "");
				return setMoveItemValues(item.getData(), drop);
			}

			// nothing
			nodeId = NodeId.parseNodeId((String) data);
			Node node = ServerInstance.getInstance().getServerInstance().getAddressSpaceManager().getNodeById(nodeId);

			if (!performDropFilter(node, item, index)) {
				Logger.getLogger(getClass().getName()).log(Level.INFO,
						"Node type not supported, supported nodes are: " + this.filter);
				return false;
			}

			// if(!(node instanceof UAVariableNode)) {
			// return false;
			// }

			ReadValueId[] nodesToRead = new ReadValueId[1];
			nodesToRead[0] = new ReadValueId();
			nodesToRead[0].setAttributeId(Attributes.DisplayName);
			nodesToRead[0].setNodeId(nodeId);
			// String name = "";
			try {
				DataValue[] result = ServerInstance.getInstance().getServerInstance().getMaster().read(nodesToRead, 0.0,
						TimestampsToReturn.Both, null, null);

				if (result != null && result.length > 0 && result[0] != null) {
					name = ((LocalizedText) result[0].getValue().getValue()).getText();
				}
				return setItemValues(item, index, nodeId, name);
			} catch (ServiceResultException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	protected abstract boolean setRemoteItemValues(Object node, String data);

	protected abstract boolean setMoveItemValues(Object node, String data);

	@Override
	public boolean validateDrop(Object target, int operation, TransferData transferType) {

		DropTargetEvent event = getCurrentEvent();
		boolean isValid = false;
		if (target != null) {
			isValid = validateDropInView(event);
		}

		return isValid;
	}

	protected int getIndexFromTableItem(DropTargetEvent event) {
		Viewer viewer2use = getViewer();
		if (viewer2use instanceof ColumnViewer) {
			Point location = getViewer().getControl().toControl(event.x, event.y);
			ViewerCell cell = ((ColumnViewer) viewer2use).getCell(location);

			if (cell != null) {
				return cell.getColumnIndex();
			}
		}
		return -1;
	}

	@Override
	public void drop(DropTargetEvent event) {
		super.drop(event);
	}

	@Override
	public void dragEnter(DropTargetEvent event) {
		event.detail = DND.DROP_COPY;
		super.dragEnter(event);
	}

	protected static String getBrowsePath(NodeId nodeId, NodeId lastParentId) {
		Deque<BrowsePathElement> browsepathelems = OPCUABrowseUtils.getFullBrowsePath(nodeId,
				ServerInstance.getInstance().getServerInstance(), lastParentId);

		String browsepath = "";
		for (BrowsePathElement element : browsepathelems) {
			if (element.getId().equals(lastParentId)) {
				continue;
			}
			browsepath += "//" + element.getBrowsename().getName();
		}

		return browsepath;
	}

	protected static String getBrowsePath(NodeId nodeId) {
		return getBrowsePath(nodeId, Identifiers.ObjectsFolder);
	}

}
