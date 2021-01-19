package com.bichler.opc.driver.base.handler;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.StatusCode;
import org.opcfoundation.ua.core.Identifiers;

import com.bichler.opc.comdrv.IWriteListener;

public class BaseWriteHandler extends AbstractBaseHandler implements IWriteListener {
	private static final Logger LOGGER = Logger.getLogger(BaseWriteHandler.class.getName());

	public BaseWriteHandler() {
		super();
	}

	@Override
	public boolean prepareWrite(NodeId nodeId) {
		return false;
	}

	@Override
	public StatusCode asyncWriteValue(NodeId nodeId, DataValue value, DataValue oldValue, long senderState) {
		return null;
	}

	/**
	 * not used in this modbus because we can not send an message direct, before the
	 * server ask us.
	 */
	@Override
	public StatusCode syncWriteValue(NodeId nodeId, DataValue value, DataValue oldValue, long senderState) {
		// server state
		if (Identifiers.Server_ServerStatus_State.equals(nodeId)) {
			handleServerStatus(oldValue, value);
		}
		return null;
	}

	private void handleServerStatus(DataValue oldValue, DataValue newValue) {
		LOGGER.log(Level.INFO, "OldState: {0} - NewValue: {1}",
				new Object[] { oldValue.getValue().getValue(), newValue.getValue().getValue() });
	}

	@Override
	public StatusCode asyncWriteValue(NodeId nodeId, DataValue value, DataValue oldValue, String indexRange,
			long senderState) {
		return null;
	}

	/**
	 * not used in this modbus because we can not send an message direct, before the
	 * server ask us.
	 */
	@Override
	public StatusCode syncWriteValue(NodeId nodeId, DataValue value, DataValue oldValue, String indexRange,
			long senderState) {
		if (Identifiers.Server_ServerStatus_State.equals(nodeId)) {
			handleServerStatus(oldValue, value);
		}
		return null;
	}
}
