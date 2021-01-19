package com.bichler.astudio.opcua.dnd;

import opc.sdk.core.node.Node;
import opc.sdk.core.node.UAObjectNode;
import opc.sdk.core.node.UAVariableNode;

import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.widgets.Widget;
import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.Attributes;
import org.opcfoundation.ua.core.ReadValueId;
import org.opcfoundation.ua.core.TimestampsToReturn;

import com.bichler.astudio.opcua.driver.IOPCDriverConfigEditPart;
import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.opcua.widget.model.AdvancedConfigurationNode;

public class OPCUADropInDeviceMappingViewAdapter extends OPCUADropInViewAdapter {

	private IOPCDriverConfigEditPart editor;

	public OPCUADropInDeviceMappingViewAdapter(Viewer viewer, IOPCDriverConfigEditPart editor) {
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
					isDirty = true;
					break;
				case 2:
					node.setEnableId(nodeId);
					node.setEnableName(name);
					isDirty = true;
					break;
				case 3:
					node.setAddonId(nodeId);
					node.setAddonName(name);
					isDirty = true;
					break;
				case 4:
					node.setGroupId(nodeId);
					node.setGroupName(name);
					isDirty = true;
					break;
				case 5:
					node.setMeterId(nodeId);
					node.setMeterName(name);
					isDirty = true;
					break;
				}
				((ColumnViewer) getViewer()).update(node, null);

				if (isDirty) {
					editor.setDirty(true);
				}
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
		case 3:
		case 4:
		case 5:
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
			boolean isRemote = ((String) data).startsWith("%remote%");
			if (!isRemote) {
				nodeId = NodeId.parseNodeId((String) data);

				Node node = ServerInstance.getInstance().getServerInstance().getAddressSpaceManager()
						.getNodeById(nodeId);

				if (index > 1 && index <= 5 && !(node instanceof UAVariableNode)) {
					return false;
				}
				if (index == 1 && !(node instanceof UAObjectNode)) {
					return false;
				}
				ReadValueId[] nodesToRead = new ReadValueId[1];
				nodesToRead[0] = new ReadValueId();
				nodesToRead[0].setAttributeId(Attributes.DisplayName);
				nodesToRead[0].setNodeId(nodeId);
				// String name = "";
				try {
					DataValue[] result = ServerInstance.getInstance().getServerInstance().getMaster().read(nodesToRead,
							0.0, TimestampsToReturn.Both, null, null);

					if (result != null && result.length > 0 && result[0] != null) {
						name = ((LocalizedText) result[0].getValue().getValue()).getText();
					}
					return setItemValues(item, index, nodeId, name);
				} catch (ServiceResultException e) {
					e.printStackTrace();
				}
			} else {
				String drop = ((String) data).replaceFirst("%remote%", "");
				return setRemoteItemValues(item.getData(), drop);
			}

		}
		return false;
	}

	@Override
	protected boolean setMoveItemValues(Object node, String data) {
		// TODO Auto-generated method stub
		return false;
	}
}
