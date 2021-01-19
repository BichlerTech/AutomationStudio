package com.bichler.opc.driver.siemens.communication;

public enum SiemensAreaCode {
	UNKNOWN(0), SYSINFO(3), // System info of 200 family
	SYSTEMFLAGS(5), // System flags of 200 family
	ANALOGINPUTS200(6), // analog inputs of 200 family
	ANALOGOUTPUTS200(7), // analog outputs of 200 family
	P(0x80), // Peripheral I/O
	INPUTS(0x81), OUTPUTS(0x82), FLAGS(0x83), DB(0x84), // data blocks
	DI(0x85), // instance data blocks
	LOCAL(0x86), // not tested
	V(0x87), // local of caller
	COUNTER(28), // S7 counters
	TIMER(29), // S7 timers
	COUNTER200(30), // IEC counters (200 family)
	TIMER200(31); // IEC timers (200 family)
	private final int code;

	private SiemensAreaCode(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public static SiemensAreaCode fromCode(byte val) {
		switch (val) {
		case 0:
			return UNKNOWN;
		case 3:
			return SYSINFO;
		case 5:
			return SYSTEMFLAGS;
		case 6:
			return ANALOGINPUTS200;
		case 7:
			return ANALOGOUTPUTS200;
		case 28:
			return COUNTER;
		case 29:
			return TIMER;
		case 30:
			return COUNTER200;
		case 31:
			return TIMER200;
		case (byte) 0x80:
			return P;
		case (byte) 0x81:
			return INPUTS;
		case (byte) 0x82:
			return OUTPUTS;
		case (byte) 0x83:
			return FLAGS;
		case (byte) 0x84:
			return DB;
		case (byte) 0x85:
			return DI;
		case (byte) 0x86:
			return LOCAL;
		case (byte) 0x87:
			return V;
		}
		return UNKNOWN;
	}
}
