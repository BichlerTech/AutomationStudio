package com.bichler.opc.driver.siemens.dp;

import com.bichler.opc.driver.siemens.communication.SiemensDataKind;
import com.bichler.opc.driver.siemens.communication.SiemensDataType;
import com.bichler.opc.driver.siemens.transform.int_.SiemensIntTransform2Integer;

public class SiemensIntItem extends SiemensDPItem {
	public SiemensIntItem() {
		this.length = 16; // length in bit
		this.transform = new SiemensIntTransform2Integer();
		this.maxReadByteCount = 111;
		this.dataKind = SiemensDataKind.INT;
		this.dataType = SiemensDataType.INT;
	}

	@Override
	public SiemensIntItem clone(SiemensDPItem clone) {
		return (SiemensIntItem) super.clone(new SiemensIntItem());
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

	@Override
	protected byte[] createDRVValueArray() {
		if (this.internArray == null) {
			return null;
		}
		byte[] temp = new byte[2 * arraylength];
		int index = 0;
		for (int i = 0; i < arraylength; i++) {
			temp[index] = internArray[i][0];
			temp[index + 1] = internArray[i][1];
			index += 2;
		}
		return temp;
	}
}
