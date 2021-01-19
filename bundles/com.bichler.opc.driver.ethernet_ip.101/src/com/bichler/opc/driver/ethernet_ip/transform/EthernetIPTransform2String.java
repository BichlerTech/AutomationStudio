package com.bichler.opc.driver.ethernet_ip.transform;

import com.bichler.opc.driver.ethernet_ip.transform.EthernetIPTransformation;

public abstract class EthernetIPTransform2String implements EthernetIPTransformation {

	@Override
	public Object[] createInternArray(int arrayLength) {
		return new String[arrayLength];
	}
}
