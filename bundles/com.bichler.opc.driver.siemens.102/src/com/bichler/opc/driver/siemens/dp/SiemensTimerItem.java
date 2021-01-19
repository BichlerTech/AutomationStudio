package com.bichler.opc.driver.siemens.dp;

public class SiemensTimerItem extends SiemensDPItem {
	public SiemensTimerItem() {
		this.length = 2; // length in bit
	}

	@Override
	public SiemensTimerItem clone(SiemensDPItem clone) {
		return (SiemensTimerItem) super.clone(new SiemensTimerItem());
	}
}
