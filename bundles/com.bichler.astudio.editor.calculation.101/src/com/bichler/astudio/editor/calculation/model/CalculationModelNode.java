package com.bichler.astudio.editor.calculation.model;

import org.opcfoundation.ua.builtintypes.NodeId;

import com.bichler.opc.driver.calculation.CalculationDP;
import com.bichler.astudio.opcua.properties.driver.IDriverNode;

public class CalculationModelNode implements IDriverNode {

	private CalculationDP dp = null;
	private String name = null;
	private boolean valid = true;

	public CalculationModelNode() {
		this.dp = new CalculationDP();
		this.name = "";
	}

	public CalculationDP getDp() {
		return this.dp;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDP(CalculationDP dp) {
		this.dp = dp;
	}

	@Override
	public String getDname() {
		return this.name;
	}

	@Override
	public String getDesc() {
		return "";
	}

	@Override
	public String getDtype() {
		return "";
	}

	@Override
	public NodeId getNId() {
		return this.dp != null ? this.dp.getTarget().getTargetId()
				: NodeId.NULL;
	}

	@Override
	public boolean isValid() {
		return this.valid;
	}

	@Override
	public String getBrowsepath() {
		// NOT NEEDED
		return "";
	}
}
