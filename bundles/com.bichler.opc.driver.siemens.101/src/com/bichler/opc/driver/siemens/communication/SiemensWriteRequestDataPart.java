package com.bichler.opc.driver.siemens.communication;

public class SiemensWriteRequestDataPart extends SiemensWriteDataPart {
	private byte unknown1 = 0;
	private byte following = 0;
	private byte unknown2 = 0;
	private SiemensDataType datatype = SiemensDataType.UNKNOWN;
	private int length = 0;
	private int dpNumber = 0;
	private SiemensAreaCode areacode = SiemensAreaCode.UNKNOWN;
	private int startAddress = 0;

	public byte getUnknown1() {
		return unknown1;
	}

	public void setUnknown1(byte unknown1) {
		this.unknown1 = unknown1;
	}

	public byte getFollowing() {
		return following;
	}

	public void setFollowing(byte following) {
		this.following = following;
	}

	public byte getUnknown2() {
		return unknown2;
	}

	public void setUnknown2(byte unknown2) {
		this.unknown2 = unknown2;
	}

	public SiemensDataType getDatatype() {
		return datatype;
	}

	public void setDatatype(SiemensDataType datatype) {
		this.datatype = datatype;
	}

	public void setDatatype(byte datatype) {
		this.datatype = SiemensDataType.fromValue(datatype);
	}

	public int getLength() {
		return length;
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

	public int getDpNumber() {
		return dpNumber;
	}

	public void setDpNumber(int dpNumber) {
		this.dpNumber = dpNumber;
	}

	public void setDpNumber(byte[] dpNumber) {
		int i = dpNumber[0];
		int k = dpNumber[1];
		if (i < 0)
			i += 256;
		if (k < 0)
			k += 256;
		this.dpNumber = (256 * i + k);
	}

	public SiemensAreaCode getAreacode() {
		return areacode;
	}

	public void setAreacode(SiemensAreaCode areacode) {
		this.areacode = areacode;
	}

	public void setAreacode(byte areacode) {
		this.areacode = SiemensAreaCode.fromCode(areacode);
	}

	public int getStartAddress() {
		return startAddress;
	}

	public void setStartAddress(int startAddress) {
		this.startAddress = startAddress;
	}

	public void setStartAddress(byte[] startAddress) {
		if (startAddress == null || startAddress.length != 3)
			return;
		int i = startAddress[0];
		int j = startAddress[1];
		int k = startAddress[2];
		if (i < 0)
			i += 256;
		if (j < 0)
			j += 256;
		if (k < 0)
			k += 256;
		this.startAddress = k + 256 * (256 * i + j);
	}
}
