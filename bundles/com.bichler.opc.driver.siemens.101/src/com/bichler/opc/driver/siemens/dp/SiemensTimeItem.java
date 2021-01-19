package com.bichler.opc.driver.siemens.dp;

public class SiemensTimeItem extends SiemensDPItem {
	public SiemensTimeItem() {
		this.length = 4; // length in bit
	}

	@Override
	public SiemensTimeItem clone(SiemensDPItem clone) {
		return (SiemensTimeItem) super.clone(new SiemensTimeItem());
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
