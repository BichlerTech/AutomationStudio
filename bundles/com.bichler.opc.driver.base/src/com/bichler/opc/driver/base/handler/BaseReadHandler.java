package com.bichler.opc.driver.base.handler;

import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.StatusCode;
import org.opcfoundation.ua.builtintypes.Variant;

import com.bichler.opc.comdrv.IReadListener;

import opc.sdk.ua.classes.BaseNode;
import opc.sdk.ua.classes.BaseVariableType;

public class BaseReadHandler extends AbstractBaseHandler implements IReadListener {
	public BaseReadHandler() {
		super();
	}

	@Override
	public boolean prepareRead(NodeId nodeId) {
		return false;
	}

	@Override
	public StatusCode asyncReadValue(NodeId nodeId, long senderState) {
		return null;
	}

	@Override
	public DataValue syncReadValue(NodeId nodeId, long senderState) {
		BaseNode node = getManager().getNode(nodeId);
		if (!(node instanceof BaseVariableType)) {
			return null;
		}
		return new DataValue(new Variant(((BaseVariableType<?>) node).getValue()));
	}
}
