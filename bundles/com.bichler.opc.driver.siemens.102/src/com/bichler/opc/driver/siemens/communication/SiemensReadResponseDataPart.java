package com.bichler.opc.driver.siemens.communication;

public class SiemensReadResponseDataPart extends SiemensReadDataPart {
	private byte dataErrorCode = 0;
	private SiemensDataKind dataKind = SiemensDataKind.UNKNOWN;
	private int length = 0;
	private byte[] data = null;

	public byte getDataErrorCode() {
		return dataErrorCode;
	}

	public void setDataErrorCode(byte dataErrorCode) {
		this.dataErrorCode = dataErrorCode;
	}

	public SiemensDataKind getDataKind() {
		return dataKind;
	}

	public void setDataKind(SiemensDataKind dataKind) {
		this.dataKind = dataKind;
	}

	public void setDataKind(byte dataType) {
		this.dataKind = SiemensDataKind.fromValue(dataType);
	}

	/**
	 * length in bit or byte depending on data type
	 * 
	 * @return
	 */
	public int getLength() {
		return length;
	}

	/**
	 * 
	 * @return
	 */
	public int getLengthinByte() {
		switch (this.dataKind) {
		case BOOL:
		case REAL:
		case CHAR:
		case STRING:
			return length;
		default:
			return length / 8;
		}
	}

	public void setLength(int length) {
		this.length = length;
	}

	public void setLength(byte[] length) {
		int i = length[0];
		int k = length[1];
		if (i < 0)
			i += 256;
		if (k < 0)
			k += 256;
		this.length = (256 * i + k);
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}
}
