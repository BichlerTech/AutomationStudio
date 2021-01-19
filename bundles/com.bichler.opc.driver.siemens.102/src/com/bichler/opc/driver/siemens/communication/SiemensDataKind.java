package com.bichler.opc.driver.siemens.communication;

public enum SiemensDataKind {
	UNKNOWN(0), BOOL(3), BYTE(4), WORD(4), DWORD(4), STRINGCHAR(4), TIMER(4), COUNTER(4), DATE(4), DATE_AND_TIME(4),
	TIME_OF_DAY(4), INT(5), DINT(5), TIME(5), REAL(7), LREAL(7), CHAR(9), STRING(9);
	private final int kind;

	private SiemensDataKind(int kind) {
		this.kind = kind;
	}

	public int getKind() {
		return this.kind;
	}

	public static SiemensDataKind fromValue(int kind) {
		switch (kind) {
		case 0:
			return UNKNOWN;
		case 3:
			return BOOL;
		case 4:
			return BYTE;
		case 5:
			return INT;
		case 7:
			return REAL;
		case 9:
			return CHAR;
		}
		return UNKNOWN;
	}
}
