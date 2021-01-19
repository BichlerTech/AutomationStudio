package com.bichler.opc.driver.ethernet_ip;

public enum ComEthernetIPStatus {
	SUCCESS(0x0000), INVALID_UNSUPORTED_COMMAND(0x0001), INSUFF_MEMORY(0x0002), POORLY_INCORR_DATA(0x0003),
	INVALID_SESSION_HANDLE(0x0064), INVALID_LENGTH(0x0065), UNSUPP_PROTOCOL_VERSION(0x0069);

	private final int flag;

	private ComEthernetIPStatus(int flag) {
		this.flag = flag;
	}

	public int getValue() {
		return this.flag;
	}
}
