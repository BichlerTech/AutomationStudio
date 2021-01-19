package com.bichler.opc.driver.siemens.communication;

public class SiemensParamPart {
	protected int dataCount = 1;
	protected SiemensParametercode code = null;

	public SiemensParametercode getCode() {
		return code;
	}

	public void setCode(SiemensParametercode code) {
		this.code = code;
	}

	public int getDataCount() {
		return dataCount;
	}

	public void setDataCount(byte dataCount) {
		this.dataCount = dataCount;
		if (this.dataCount < 0) {
			// change it to positive
			this.dataCount += 256;
		}
	}
}
