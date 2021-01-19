package com.bichler.opc.driver.siemens.dp;

import com.bichler.opc.driver.siemens.communication.SiemensDataKind;
import com.bichler.opc.driver.siemens.communication.SiemensDataType;

public class SiemensLRealItem extends SiemensDPItem {
	public SiemensLRealItem() {
		this.length = 4; // length in byte
		this.maxReadByteCount = 28;
		this.dataKind = SiemensDataKind.LREAL;
		this.dataType = SiemensDataType.LREAL;
	}

	@Override
	public SiemensLRealItem clone(SiemensDPItem clone) {
		return (SiemensLRealItem) super.clone(new SiemensLRealItem());
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
		byte[] temp = new byte[8 * arraylength];
		int index = 0;
		for (int i = 0; i < arraylength; i++) {
			temp[index] = internArray[i][0];
			temp[index + 1] = internArray[i][1];
			temp[index + 2] = internArray[i][2];
			temp[index + 3] = internArray[i][3];
			temp[index + 4] = internArray[i][4];
			temp[index + 5] = internArray[i][5];
			temp[index + 6] = internArray[i][6];
			temp[index + 7] = internArray[i][7];
			index += 8;
		}
		return temp;
	}

	@Override
	public void setArraylength(int arraylength) {
		this.arraylength = arraylength; // * 2;
		this.internArray = new byte[this.arraylength][getLength()];
	}

	/**
	 * gets the length of the array, if i > -1, we got a request from create message
	 * 
	 * @param i
	 * @return
	 */
	@Override
	public int getReadArraylength(int i) {
		if (i <= -1)
			return this.arraylength;
		int alength = this.arraylength * 2;
		// the array length is limited by maxBytecount
		if (maxReadByteCount * (i + 1) < alength * getLength()) {
			return maxReadByteCount / getLength();
		} else {
			return alength - maxReadByteCount / getLength() * i;
		}
	}

	/**
	 * gets the length of the array, if i > -1, we got a request from create message
	 * 
	 * @param i
	 * @return
	 */
	public int getWriteArraylength(int i) {
		int alength = this.arraylength * 2;
		if (i <= -1)
			return alength;
		// the array length is limited by maxBytecount
		if (maxWriteByteCount * (i + 1) < alength * getLength()) {
			return maxWriteByteCount / getLength();
		} else {
			return alength - maxWriteByteCount / getLength() * i;
		}
	}
}
