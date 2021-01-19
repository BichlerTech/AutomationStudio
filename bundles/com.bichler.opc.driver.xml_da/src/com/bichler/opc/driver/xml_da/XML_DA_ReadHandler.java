package com.bichler.opc.driver.xml_da;

import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.StatusCode;

import com.bichler.opc.comdrv.IReadListener;

public class XML_DA_ReadHandler implements IReadListener {
	private XML_DA_ResourceManager manager = null;

	@Override
	public boolean prepareRead(NodeId nodeId) {
		return false;
	}

	@Override
	public StatusCode asyncReadValue(NodeId nodeId, long senderState) {
		return null;
	}

	public XML_DA_ResourceManager getManager() {
		return manager;
	}

	public void setManager(XML_DA_ResourceManager manager) {
		this.manager = manager;
	}

	@Override
	public DataValue syncReadValue(NodeId nodeId, long senderState) {
		return null;
	}
}
