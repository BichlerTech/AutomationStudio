package com.bichler.opc.driver.ethernet_ip.transform;

import org.opcfoundation.ua.builtintypes.DateTime;

public abstract class EthernetIPTransform2DateTime implements EthernetIPTransformation {

	@Override
	public Object[] createInternArray(int arrayLength) {
		return new DateTime[arrayLength];
	}
}
