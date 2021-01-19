package opc.sdk.ua.constants;

public enum HistoryUpdateQueryMode {
	INSERT(1), REPLACE(2), INSERTREPLACE(3), DELETE(4);

	private int value;

	private HistoryUpdateQueryMode(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public static HistoryUpdateQueryMode valueOf(int mode) {
		for (HistoryUpdateQueryMode value : values()) {
			if (value.getValue() == mode) {
				return value;
			}
		}
		return null;
	}
}
