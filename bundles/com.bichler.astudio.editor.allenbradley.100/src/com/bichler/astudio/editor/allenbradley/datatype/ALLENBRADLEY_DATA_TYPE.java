package com.bichler.astudio.editor.allenbradley.datatype;

public enum ALLENBRADLEY_DATA_TYPE {
	BOOL, SINT, INT, DINT, LINT, USINT, UINT, UDINT, ULINT, REAL, LREAL, ITIME, TIME, FTIME, LTIME, DATE, TIME_OF_DAY, DATE_AND_TIME, STRING, STRING2, STRINGN, SHORT_STRING, STRINGI, BYTE, WORD, DWORD, LWORD, EPATH, ENGUNIT, UNDEFINED, COMPLEX;

	public static ALLENBRADLEY_DATA_TYPE getTypeFromString(String datatype) {
	  ALLENBRADLEY_DATA_TYPE[] items = values();

		for (ALLENBRADLEY_DATA_TYPE item : items) {
			if (datatype.startsWith(item.name())) {
				return item;
			}
		}
		return UNDEFINED;
	}

	public static String[] getNumericTypes() {
		String[] types = new String[11];
		types[0] = ALLENBRADLEY_DATA_TYPE.SINT.name();
		types[1] = ALLENBRADLEY_DATA_TYPE.INT.name();
		types[2] = ALLENBRADLEY_DATA_TYPE.DINT.name();
		types[3] = ALLENBRADLEY_DATA_TYPE.LINT.name();
		types[4] = ALLENBRADLEY_DATA_TYPE.USINT.name();
		types[5] = ALLENBRADLEY_DATA_TYPE.UINT.name();
		types[6] = ALLENBRADLEY_DATA_TYPE.UDINT.name();
		types[7] = ALLENBRADLEY_DATA_TYPE.ULINT.name();
		types[8] = ALLENBRADLEY_DATA_TYPE.REAL.name();
		types[9] = ALLENBRADLEY_DATA_TYPE.LREAL.name();
		types[10] = ALLENBRADLEY_DATA_TYPE.BYTE.name();
		return types;
	}
}
