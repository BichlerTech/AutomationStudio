package com.bichler.opc.driver.siemens.dp;

import com.bichler.opc.driver.siemens.communication.SiemensDataKind;
import com.bichler.opc.driver.siemens.communication.SiemensDataType;

public class SiemensCounterItem extends SiemensDPItem {
	public SiemensCounterItem() {
		this.length = 2; // length in bit
		this.dataKind = SiemensDataKind.COUNTER;
		this.dataType = SiemensDataType.COUNTER;
		// default bytecount
		this.maxReadByteCount = 221;
	}

	@Override
	public SiemensCounterItem clone(SiemensDPItem clone) {
		return (SiemensCounterItem) super.clone(new SiemensCounterItem());
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
