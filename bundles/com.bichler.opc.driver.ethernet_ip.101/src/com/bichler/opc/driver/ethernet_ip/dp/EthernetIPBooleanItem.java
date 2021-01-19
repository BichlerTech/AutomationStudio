package com.bichler.opc.driver.ethernet_ip.dp;

public class EthernetIPBooleanItem extends EthernetIPDPItem {

	private boolean fixed = false;

	@Override
	public void fixArrayLength() {
		this.fixed = true;

	}

	public int getArrayLength() {
		if (this.fixed) {
			int length = this.arraylength / 32;

			if (arraylength % 32 > 0)
				length++;

			return length;
		}
		return this.arraylength;
	}

	public EthernetIPBooleanItem() {
	}
}
