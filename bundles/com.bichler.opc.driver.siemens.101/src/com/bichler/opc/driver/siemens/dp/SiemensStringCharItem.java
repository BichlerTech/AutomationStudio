package com.bichler.opc.driver.siemens.dp;

import com.bichler.opc.driver.siemens.communication.SiemensDataKind;
import com.bichler.opc.driver.siemens.communication.SiemensDataType;

public class SiemensStringCharItem extends SiemensDPItem {
	public SiemensStringCharItem() {
		this.length = 1; // length in bit
		this.maxReadByteCount = 221;
		this.dataKind = SiemensDataKind.STRINGCHAR;
		this.dataType = SiemensDataType.STRINGCHAR;
	}

	@Override
	public SiemensStringCharItem clone(SiemensDPItem clone) {
		return (SiemensStringCharItem) super.clone(new SiemensStringCharItem());
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
}
