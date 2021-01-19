package com.bichler.astudio.opcua.dnd;

import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.Attributes;
import org.opcfoundation.ua.core.ReadValueId;
import org.opcfoundation.ua.core.TimestampsToReturn;

import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;

public abstract class OPCUADropTarget extends DropTargetAdapter {

	public OPCUADropTarget() {
	}

	@Override
	public void drop(DropTargetEvent event) {
		Object data = event.data;

		if (data instanceof String) {
			NodeId nodeId = NodeId.parseNodeId((String) data);
			ReadValueId[] nodesToRead = new ReadValueId[1];
			nodesToRead[0] = new ReadValueId();
			nodesToRead[0].setAttributeId(Attributes.DisplayName);
			nodesToRead[0].setNodeId(nodeId);
			String name = "";
			try {
				DataValue[] result = ServerInstance.getInstance().getServerInstance().getMaster().read(nodesToRead, 0.0,
						TimestampsToReturn.Both, null, null);

				if (result != null && result.length > 0 && result[0] != null) {
					name = ((LocalizedText) result[0].getValue().getValue()).getText();
				}
			} catch (ServiceResultException e) {
				e.printStackTrace();
			}

			setDropValues(nodeId, name);
		}
		super.drop(event);
	}

	public abstract void setDropValues(NodeId nodeId, String name);

	@Override
	public void dropAccept(DropTargetEvent event) {
		super.dropAccept(event);
	}

	@Override
	public void dragEnter(DropTargetEvent event) {
		event.detail = DND.DROP_COPY;
	}

}
