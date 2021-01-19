package com.bichler.opc.driver.ethernet_ip.dp;

public enum EthernetIPDataType {
	UNKNOWN(0), BOOL(1), SINT(2), INT(3), DINT(4), LINT(8), BYTE(2), STRINGCHAR(2), DATE_AND_TIME(2), CHAR(3),
	STRING(3), WORD(4), TIMER(4), COUNTER(4), DATE(4),

	DWORD(6), TIME_OF_DAY(6), TIME(7), REAL(8);

	private final int type;

	private EthernetIPDataType(int type) {
		this.type = type;
	}

	public int getType() {
		return this.type;
	}

	public static EthernetIPDataType fromValue(int type) {
		switch (type) {
		case 0:
			return UNKNOWN;
		case 1:
			return BOOL;
		case 2:
			return BYTE;
		case 3:
			return CHAR;
		case 4:
			return WORD;
		case 5:
			return INT;
		case 6:
			return DWORD;
		case 7:
			return DINT;
		case 8:
			return LINT;
		}
		return UNKNOWN;
	}
}
