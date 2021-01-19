package com.bichler.opc.driver.siemens.communication;

public enum SiemensDataErrorCode {
	NO_ERROR(0xFF), NO_DATA_AVAILABLE(0x000A), EXPECT_RESPONSE(0x0000);
	private final byte code;

	private SiemensDataErrorCode(int code) {
		this.code = (byte) code;
	}

	public byte getCode() {
		return this.code;
	}

	public static SiemensDataErrorCode fromValue(byte code) {
		switch ((int) code) {
		case 0xFF:
			return NO_ERROR;
		case 0x000A:
			return NO_DATA_AVAILABLE;
		case 0x0000:
			return EXPECT_RESPONSE;
		}
		return NO_ERROR;
	}

	public static String strerror(SiemensDataErrorCode code) {
		switch (code) {
		case NO_ERROR:
			return "OK, no error!";
		case NO_DATA_AVAILABLE:
			return "No data on address available!";
		case EXPECT_RESPONSE:
			return "CPU expect response.";
		default:
			return "No message defined for code: " + code.getCode() + "!";
		}
	}
}
