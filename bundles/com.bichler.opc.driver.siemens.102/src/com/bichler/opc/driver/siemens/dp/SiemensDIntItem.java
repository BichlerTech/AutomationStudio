package com.bichler.opc.driver.siemens.dp;

import org.opcfoundation.ua.builtintypes.DataValue;

import com.bichler.opc.driver.siemens.communication.SiemensDataKind;
import com.bichler.opc.driver.siemens.communication.SiemensDataType;
import com.bichler.opc.driver.siemens.transform.dint.SiemensDIntTransform2Integer;

public class SiemensDIntItem extends SiemensDPItem {
	public SiemensDIntItem() {
		this.length = 32; // length in byte
		this.transform = new SiemensDIntTransform2Integer();
		this.dataKind = SiemensDataKind.DINT;
		this.dataType = SiemensDataType.DINT;
		this.maxReadByteCount = 55;
	}

	@Override
	public SiemensDIntItem clone(SiemensDPItem clone) {
		return (SiemensDIntItem) super.clone(new SiemensDIntItem());
	}

	@Override
	public DataValue drv2Prog(byte[] data) {
		// TODO Auto-generated method stub
		return super.drv2Prog(data);
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
