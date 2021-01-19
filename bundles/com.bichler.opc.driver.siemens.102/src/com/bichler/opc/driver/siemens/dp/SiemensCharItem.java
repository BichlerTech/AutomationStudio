package com.bichler.opc.driver.siemens.dp;

import com.bichler.opc.driver.siemens.communication.SiemensDataKind;
import com.bichler.opc.driver.siemens.communication.SiemensDataType;

public class SiemensCharItem extends SiemensDPItem {
	public SiemensCharItem() {
		this.length = 8; // length in byte
		this.dataType = SiemensDataType.CHAR;
		this.dataKind = SiemensDataKind.CHAR;
		// default bytecount
		this.maxReadByteCount = 221;
	}

	@Override
	public SiemensCharItem clone(SiemensDPItem clone) {
		return (SiemensCharItem) super.clone(new SiemensCharItem());
	}

	@Override
	public int getReadIndexInBit(int i) {
		if (i < 0)
			return ((int) this.index) * 8;
		return ((int) this.index) * 8 + maxReadByteCount * 8 * i;
	}

	@Override
	public int getWriteIndexInBit(int i) {
		if (i < 0)
			return ((int) this.index) * 8;
		return ((int) this.index) * 8 + maxWriteByteCount * 8 * i;
	}

	@Override
	public int getLength() {
		return length / 8;
	}

	@Override
	public int getLengthInBit() {
		return length;
	}
}
