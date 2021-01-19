package com.bichler.opc.driver.ethernet_ip;

public enum ComEthernetIPCommands {
	NOP(0x0000), LISTSERVICES(0x0004), LISTIDENTITY(0x0063), LISTINTERFACES(0x0064), REGISTERSESSION(0x0065),
	UNREGISTERSESSION(0x0066), SENDRRDATA(0x006F), SENDUNITDATA(0x0070), INDICATESTATUS(0x0072);
	;

	private final int flag;

	private ComEthernetIPCommands(int flag) {
		this.flag = flag;
	}

	public int getValue() {
		return this.flag;
	}

	public static ComEthernetIPCommands forCode(int code) {
		for (ComEthernetIPCommands command : values())
			if (command.getValue() == code)
				return command;
		return null;
	}
}
