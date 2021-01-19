package com.bichler.opc.driver.siemens.dp;

import com.bichler.opc.driver.siemens.communication.SiemensDataKind;
import com.bichler.opc.driver.siemens.communication.SiemensDataType;

public class SiemensRealItem extends SiemensDPItem {
	public SiemensRealItem() {
		this.length = 4; // length in byte
		this.maxReadByteCount = 56;
		this.dataKind = SiemensDataKind.REAL;
		this.dataType = SiemensDataType.REAL;
	}

	@Override
	public SiemensRealItem clone(SiemensDPItem clone) {
		return (SiemensRealItem) super.clone(new SiemensRealItem());
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
	protected byte[] createDRVValueArray() {
		if (this.internArray == null) {
			return null;
		}
		byte[] temp = new byte[4 * arraylength];
		int index = 0;
		for (int i = 0; i < arraylength; i++) {
			temp[index] = internArray[i][0];
			temp[index + 1] = internArray[i][1];
			temp[index + 2] = internArray[i][2];
			temp[index + 3] = internArray[i][3];
			index += 4;
		}
		return temp;
	}
}
