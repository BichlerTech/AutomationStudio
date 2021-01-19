package com.bichler.opc.driver.siemens;

import org.opcfoundation.ua.builtintypes.DataValue;

import com.bichler.opc.driver.siemens.dp.SiemensDPItem;

public class SiemensWriteRequest {
	private SiemensDPItem dp = null;
	private DataValue value = null;
	private String indexRange = "";

	public SiemensDPItem getDp() {
		return dp;
	}

	public void setDp(SiemensDPItem dp) {
		this.dp = dp;
	}

	public DataValue getValue() {
		return value;
	}

	public void setValue(DataValue value) {
		this.value = value;
	}

	public String getIndexRange() {
		return indexRange;
	}

	public void setIndexRange(String indexRange) {
		this.indexRange = indexRange;
	}
}
