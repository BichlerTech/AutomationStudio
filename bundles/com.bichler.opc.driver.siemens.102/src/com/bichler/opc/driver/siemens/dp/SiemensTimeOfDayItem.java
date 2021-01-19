package com.bichler.opc.driver.siemens.dp;

import com.bichler.opc.driver.siemens.communication.SiemensDataKind;
import com.bichler.opc.driver.siemens.communication.SiemensDataType;

public class SiemensTimeOfDayItem extends SiemensDPItem {
	public SiemensTimeOfDayItem() {
		this.length = 4; // length in bit
		this.dataKind = SiemensDataKind.TIME_OF_DAY;
		this.dataType = SiemensDataType.TIME_OF_DAY;
	}

	@Override
	public SiemensTimeOfDayItem clone(SiemensDPItem clone) {
		return (SiemensTimeOfDayItem) super.clone(new SiemensTimeOfDayItem());
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
