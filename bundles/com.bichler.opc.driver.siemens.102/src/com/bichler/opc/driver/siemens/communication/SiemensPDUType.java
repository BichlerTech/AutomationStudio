package com.bichler.opc.driver.siemens.communication;

public enum SiemensPDUType {
	REQUEST((byte) 1), UNKNOWN((byte) 2), RESPONSE((byte) 3), SZL_DIAGNOSE((byte) 7);
	private byte type;

	private SiemensPDUType(byte type) {
		this.type = type;
	}

	public byte getType() {
		return this.type;
	}

	public static SiemensPDUType fromValue(byte type) {
		switch (type) {
		case 1:
			return REQUEST;
		case 2:
			return UNKNOWN;
		case 3:
			return RESPONSE;
		case 7:
			return SZL_DIAGNOSE;
		}
		return UNKNOWN;
	}
}
