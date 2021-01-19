package com.bichler.opc.driver.siemens;

import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.StatusCode;

import com.bichler.opc.comdrv.IReadListener;
import com.bichler.opc.driver.siemens.dp.SiemensDPItem;

public class SiemensReadHandler implements IReadListener {
	private SiemensResourceManager manager = null;

	@Override
	public boolean prepareRead(NodeId nodeId) {
		return false;
	}

	@Override
	public StatusCode asyncReadValue(NodeId nodeId, long senderState) {
		// check if we have an readable nodeid
		if (this.manager.getDpItems().containsKey(nodeId)) {
			// we have a read possible nodeid found
			SiemensDPItem dp = this.manager.getDpItems().get(nodeId);
			// create read request bytes
			// synchronized (manager.getReadListLock()) {
			manager.getDevice().storeReadReq(dp);
			// add datapoint exclusively into read list
			// manager.getReadList().add(dp.getDefaultSingleReadRequest());
			// }
		}
		return null;
	}

	public SiemensResourceManager getManager() {
		return manager;
	}

	public void setManager(SiemensResourceManager manager) {
		this.manager = manager;
	}

	@Override
	public DataValue syncReadValue(NodeId nodeId, long senderState) {
		// TODO Auto-generated method stub
		return null;
	}
}
