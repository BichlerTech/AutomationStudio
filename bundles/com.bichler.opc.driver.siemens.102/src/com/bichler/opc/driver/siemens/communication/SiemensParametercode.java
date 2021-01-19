package com.bichler.opc.driver.siemens.communication;

public enum SiemensParametercode {
	OPEN_S7_CONNECTION(0xF0), SSL_DIAGNOSTICS(0x00), READ(0x04), WRITE(0x05), REQUEST_DOWNLOAD(0x1A),
	DOWNLOAD_BLOCK(0x1B), END_DOWNLOAD(0x1C), START_UPLOAD(0x1D), UPLOAD(0x1E), END_UPLOAD(0x1F), INSERT_BLOCK(0x28);
	private final int type;

	private SiemensParametercode(int type) {
		this.type = type;
	}

	public byte getType() {
		return (byte) type;
	}

	public static SiemensParametercode fromType(int type) {
		switch (type) {
		case 0xF0:
			return OPEN_S7_CONNECTION;
		case 0x00:
			return SSL_DIAGNOSTICS;
		case 0x04:
			return READ;
		case 0x05:
			return WRITE;
		case 0x1A:
			return REQUEST_DOWNLOAD;
		case 0x1B:
			return DOWNLOAD_BLOCK;
		case 0x1C:
			return END_DOWNLOAD;
		case 0x1D:
			return START_UPLOAD;
		case 0x1E:
			return UPLOAD;
		case 0x1F:
			return END_UPLOAD;
		case 0x28:
			return INSERT_BLOCK;
		}
		return OPEN_S7_CONNECTION;
	}
}
