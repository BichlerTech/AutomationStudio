package com.bichler.opc.driver.calculation.targets;

import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.NodeId;

public class CalculationTarget {
	private NodeId targetId = null;
	private StringBuffer target = new StringBuffer();

	public StringBuffer getTarget() {
		return target;
	}

	public void setTarget(StringBuffer target) {
		this.target = target;
	}

	protected String valueFromNode = "";
	private boolean loggedError = false;

	public String getValueFromNode() {
		return valueFromNode;
	}

	public void setValueFromNode(String valueFromNode) {
		this.valueFromNode = valueFromNode;
	}

	public NodeId getTargetId() {
		return targetId;
	}

	public void setTargetId(NodeId targetId) {
		this.targetId = targetId;
	}

	public DataValue getTargetValue(Object val, int index) {
		return null;
	}

	public void setLoggedError(boolean value) {
		this.loggedError = value;
	}

	public boolean getLoggedError() {
		return this.loggedError;
	}
}
