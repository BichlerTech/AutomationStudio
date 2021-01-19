package com.bichler.astudio.opcua.components.ui.dialogs.providers;

import java.util.HashMap;
import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.hbsoft.visu.connections.Comet_UA_Connection;
import com.hbsoft.visu.connections.Comet_UA_Ethernet_Connection;
import com.hbsoft.visu.connections.Comet_UA_OPC_Connection;

public class OPCUAConnectionsContentProvider implements ITreeContentProvider {

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// TODO Auto-generated method stub
		
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Object[] getElements(Object inputElement) {
		String []children = new String[0];
		if(inputElement instanceof HashMap) {
			return ((HashMap<String, Comet_UA_Connection>) inputElement).values().toArray();
		}
		else if(inputElement instanceof List) {
			return ((List) inputElement).toArray();
		}
		return children;
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		
		if(parentElement instanceof Comet_UA_OPC_Connection) {
			return ((Comet_UA_OPC_Connection)parentElement).getNodeId2DP().keySet().toArray();
		}
		if(parentElement instanceof Comet_UA_Ethernet_Connection) {
		//	return ((Comet_UA_Ethernet_Connection)parentElement).getNodeId2DP().keySet().toArray();
		
		}
		// TODO Auto-generated method stub
		return new String[0];
	}

	@Override
	public Object getParent(Object element) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		// TODO Auto-generated method stub
		return getChildren(element).length > 0;
	}

}
