package com.bichler.opc.driver.siemens.communication;

public class SiemensTPDUCCHeader extends SiemensTPDUHeader {
	private byte length = 0;
	private byte code = (byte) 0xD0;
	private byte[] target = null;
	private byte[] source = null;
	private byte class_ = 0;
	private byte tpduSize1 = 0;
	private byte following1 = 0;
	private byte tpduSize2 = 0;
	private byte callingTSAP_ID = 0;
	private byte following2 = 0;
	private byte unknown1 = 0;
	private byte TSAP_ID1 = 0;
	private byte following3 = 0;
	private byte unknown2 = 0;
	private byte TSAP_ID2 = 0;

	public byte getLength() {
		return length;
	}

	public void setLength(byte length) {
		this.length = length;
	}

	public byte getCode() {
		return code;
	}

	public void setCode(byte code) {
		this.code = code;
	}

	public byte[] getTarget() {
		return target;
	}

	public void setTarget(byte[] target) {
		this.target = target;
	}

	public byte[] getSource() {
		return source;
	}

	public void setSource(byte[] source) {
		this.source = source;
	}

	public byte getClass_() {
		return class_;
	}

	public void setClass_(byte class_) {
		this.class_ = class_;
	}

	public byte getTpduSize1() {
		return tpduSize1;
	}

	public void setTpduSize1(byte tpduSize1) {
		this.tpduSize1 = tpduSize1;
	}

	public byte getFollowing1() {
		return following1;
	}

	public void setFollowing1(byte following1) {
		this.following1 = following1;
	}

	public byte getTpduSize2() {
		return tpduSize2;
	}

	public void setTpduSize2(byte tpduSize2) {
		this.tpduSize2 = tpduSize2;
	}

	public byte getCallingTSAP_ID() {
		return callingTSAP_ID;
	}

	public void setCallingTSAP_ID(byte callingTSAP_ID) {
		this.callingTSAP_ID = callingTSAP_ID;
	}

	public byte getFollowing2() {
		return following2;
	}

	public void setFollowing2(byte following2) {
		this.following2 = following2;
	}

	public byte getUnknown1() {
		return unknown1;
	}

	public void setUnknown1(byte unknown1) {
		this.unknown1 = unknown1;
	}

	public byte getTSAP_ID1() {
		return TSAP_ID1;
	}

	public void setTSAP_ID1(byte tSAP_ID1) {
		TSAP_ID1 = tSAP_ID1;
	}

	public byte getFollowing3() {
		return following3;
	}

	public void setFollowing3(byte following3) {
		this.following3 = following3;
	}

	public byte getUnknown2() {
		return unknown2;
	}

	public void setUnknown2(byte unknown2) {
		this.unknown2 = unknown2;
	}

	public byte getTSAP_ID2() {
		return TSAP_ID2;
	}

	public void setTSAP_ID2(byte tSAP_ID2) {
		TSAP_ID2 = tSAP_ID2;
	}
}
