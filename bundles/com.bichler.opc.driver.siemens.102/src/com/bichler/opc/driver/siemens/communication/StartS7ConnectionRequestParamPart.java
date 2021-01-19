package com.bichler.opc.driver.siemens.communication;

public class StartS7ConnectionRequestParamPart extends SiemensParamPart {
	private byte unknown1 = 0;
	private byte unknown2 = 0;
	private byte unknown3 = 0;
	private byte unknown4 = 0;
	private byte unknown5 = 0;
	private byte offeredPDUSize = (byte) 0xF0;

	public byte getUnknown1() {
		return unknown1;
	}

	public void setUnknown1(byte unknown1) {
		this.unknown1 = unknown1;
	}

	public byte getUnknown2() {
		return unknown2;
	}

	public void setUnknown2(byte unknown2) {
		this.unknown2 = unknown2;
	}

	public byte getUnknown3() {
		return unknown3;
	}

	public void setUnknown3(byte unknown3) {
		this.unknown3 = unknown3;
	}

	public byte getUnknown4() {
		return unknown4;
	}

	public void setUnknown4(byte unknown4) {
		this.unknown4 = unknown4;
	}

	public byte getUnknown5() {
		return unknown5;
	}

	public void setUnknown5(byte unknown5) {
		this.unknown5 = unknown5;
	}

	public byte getOfferedPDUSize() {
		return offeredPDUSize;
	}

	public void setOfferedPDUSize(byte offeredPDUSize) {
		this.offeredPDUSize = offeredPDUSize;
	}

	public byte[] toBytes() {
		return new byte[] { code.getType(), unknown1, unknown2, unknown3, unknown4, unknown5, offeredPDUSize };
	}
}
