package com.bichler.opc.driver.siemens.dp;

import com.bichler.opc.driver.siemens.communication.SiemensDataKind;
import com.bichler.opc.driver.siemens.communication.SiemensDataType;

public class SiemensDateItem extends SiemensDPItem {
	public SiemensDateItem() {
		this.length = 32; // length in bit
		this.dataKind = SiemensDataKind.DATE;
		this.dataType = SiemensDataType.DATE;
	}

	@Override
	public SiemensDateItem clone(SiemensDPItem clone) {
		return (SiemensDateItem) super.clone(new SiemensDateItem());
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

	/**
	 * gets the length of the array, if i > -1, we got a request from create message
	 * 
	 * @param i
	 * @return
	 */
	public int getReadArraylength(int i) {
		if (i <= -1)
			return this.arraylength * getLength();
		// the array length is limited by maxBytecount
		if (maxReadByteCount * (i + 1) < this.arraylength * getLength()) {
			return maxReadByteCount / getLength();
		} else {
			return this.arraylength * getLength() - maxReadByteCount / getLength() * i;
		}
	}

	/**
	 * gets the length of the array, if i > -1, we got a request from create message
	 * 
	 * @param i
	 * @return
	 */
	public int getWriteArraylength(int i) {
		if (i <= -1)
			return this.arraylength * getLength();
		// the array length is limited by maxBytecount
		if (maxWriteByteCount * (i + 1) < this.arraylength * getLength()) {
			return maxWriteByteCount / getLength();
		} else {
			return this.arraylength * getLength() - maxWriteByteCount / getLength() * i;
		}
	}
}
