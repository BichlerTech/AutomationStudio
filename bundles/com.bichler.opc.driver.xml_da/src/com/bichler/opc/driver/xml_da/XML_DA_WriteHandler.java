package com.bichler.opc.driver.xml_da;

import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.StatusCode;
import org.opcfoundation.ua.core.StatusCodes;

import com.bichler.opc.comdrv.IWriteListener;
import com.bichler.opc.driver.xml_da.dp.XML_DA_DPItem;

/**
 * 
 * @author hannes bichler
 * 
 */
public class XML_DA_WriteHandler implements IWriteListener {
	/**
	 * siemens resource manager with digital and pwm write queue
	 */
	private XML_DA_ResourceManager manager = null;

	/**
	 * prepares a node for write, here we set nowrite to all called nodes because we
	 * have previously set the async write flag to each writable node.
	 */
	@Override
	public boolean prepareWrite(NodeId nodeId) {
		/** we had already initiated all write flags for nodes of that driver */
		return false;
	}

	/**
	 * 
	 */
	@Override
	public StatusCode asyncWriteValue(NodeId nodeId, DataValue value, DataValue oldValue, long senderState) {
		return new StatusCode(StatusCodes.Bad_NodeIdUnknown);
	}

	/**
	 * synch write
	 */
	@Override
	public StatusCode syncWriteValue(NodeId nodeId, DataValue value, DataValue oldValue, long senderState) {
		if (this.manager.getDpItems().containsKey(nodeId)) {
			XML_DA_DPItem dp = this.manager.getDpItems().get(nodeId);
			if (dp != null) {
				Object obj = dp.prog2DRV(value.getValue());
				this.manager.getDevice().write(dp.getItemPath(), dp.getItemName(), obj);
			}
		}
		return null;
	}

	/**
	 * get the xml-da resource manager
	 * 
	 * @return xml-da resource manager
	 */
	public XML_DA_ResourceManager getManager() {
		return manager;
	}

	/**
	 * set the mini resource manager
	 * 
	 * @param mini resource manager
	 */
	public void setManager(XML_DA_ResourceManager manager) {
		this.manager = manager;
	}

	@Override
	public StatusCode asyncWriteValue(NodeId nodeId, DataValue value, DataValue oldValue, String indexRange,
			long senderState) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StatusCode syncWriteValue(NodeId nodeId, DataValue value, DataValue oldValue, String indexRange,
			long senderState) {
		// TODO Auto-generated method stub
		return null;
	}
}
