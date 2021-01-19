package com.bichler.astudio.opcua.dnd;

import opc.sdk.core.node.Node;
import opc.sdk.core.node.UAVariableNode;

import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.Attributes;
import org.opcfoundation.ua.core.ReadValueId;
import org.opcfoundation.ua.core.TimestampsToReturn;

import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.opcua.widget.AdvancedDriverConfigWidget;

public class OPCUADropInVariableAdapter extends OPCUADropInDeviceConfigTextNodeAdapter {

	@Override
	public void drop(DropTargetEvent event) {

		Object data = event.data;

		if (data instanceof String) {
			NodeId nodeId = NodeId.NULL;
			String name = "";

			nodeId = NodeId.parseNodeId((String) data);

			Node node = ServerInstance.getInstance().getServerInstance().getAddressSpaceManager().getNodeById(nodeId);

			if (!(node instanceof UAVariableNode)) {
				event.detail = DND.DROP_NONE;
				return;
			}
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
				setDropValues(nodeId, name);
			} catch (ServiceResultException e) {
				e.printStackTrace();
			}

		}
		super.drop(event);
	}

	public OPCUADropInVariableAdapter(AdvancedDriverConfigWidget widget) {
		super(widget);
	}

	@Override
	public void dropAccept(DropTargetEvent event) {
		// event.detail = DND.DROP_NONE;
		super.dropAccept(event);
	}

}
