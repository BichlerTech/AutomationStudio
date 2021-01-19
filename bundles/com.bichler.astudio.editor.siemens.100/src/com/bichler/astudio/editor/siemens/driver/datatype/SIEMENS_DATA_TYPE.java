package com.bichler.astudio.editor.siemens.driver.datatype;

public enum SIEMENS_DATA_TYPE {
	BOOL(1), BYTE(1), STRINGCHAR(1), DATE_AND_TIME(8), CHAR(1), STRING(2), WORD(2), TIMER(2), COUNTER(2), DATE(8),
	INT(2), REAL(4), DWORD(4), TIME_OF_DAY(4), DINT(4), TIME(4), UNDEFINED(0), ARRAY(0), STRUCT(0);
	private int length = 0;

	SIEMENS_DATA_TYPE(int length) {
		this.length = length;
	}

	public int getIndexLength() {
		return this.length;
	}

	public static SIEMENS_DATA_TYPE getTypeFromString(String datatype) {
		SIEMENS_DATA_TYPE[] items = values();
		for (SIEMENS_DATA_TYPE item : items) {
			if (datatype.startsWith(item.name())) {
				return item;
			}
		}
		return UNDEFINED;
	}

	public static String[] getNumericTypes() {
		String[] types = new String[4];
		types[0] = SIEMENS_DATA_TYPE.BYTE.name();
		types[1] = SIEMENS_DATA_TYPE.INT.name();
		types[2] = SIEMENS_DATA_TYPE.REAL.name();
		types[3] = SIEMENS_DATA_TYPE.DINT.name();
		return types;
	}
}
