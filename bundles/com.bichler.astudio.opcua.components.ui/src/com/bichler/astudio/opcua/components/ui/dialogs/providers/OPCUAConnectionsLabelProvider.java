package com.bichler.astudio.opcua.components.ui.dialogs.providers;

import org.eclipse.jface.viewers.LabelProvider;
import org.opcfoundation.ua.builtintypes.NodeId;

import com.bichler.astudio.opcua.components.ui.serverbrowser.OPCUAServerModelNode;
import com.hbsoft.visu.connections.Comet_UA_Connection;

public class OPCUAConnectionsLabelProvider extends LabelProvider {

	@Override
	public String getText(Object element) {
		// TODO Auto-generated method stub
		if(element instanceof String) {
			return element.toString();
		}
		if(element instanceof Comet_UA_Connection) {
			return ((Comet_UA_Connection)element).getConnectionName();
		}
		if(element instanceof NodeId) {
			return ((NodeId)element).toString();
		}
		if(element instanceof OPCUAServerModelNode) {
			return ((OPCUAServerModelNode)element).getDisplayName();
		}
		return super.getText(element);
	}

}
