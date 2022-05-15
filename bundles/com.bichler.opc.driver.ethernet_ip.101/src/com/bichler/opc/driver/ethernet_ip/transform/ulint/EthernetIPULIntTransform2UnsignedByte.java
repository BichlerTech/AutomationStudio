package com.bichler.opc.driver.ethernet_ip.transform.ulint;

import org.opcfoundation.ua.builtintypes.UnsignedByte;
import com.bichler.opc.driver.ethernet_ip.transform.EthernetIPTransform2UnsignedByte;

import etherip.types.CIPData;
import etherip.types.CIPData.Type;

public class EthernetIPULIntTransform2UnsignedByte extends EthernetIPTransform2UnsignedByte {

	@Override
	public void transToDevice(CIPData data, Object value, int index) throws IndexOutOfBoundsException, Exception {
		data.set(index, ((UnsignedByte) value).shortValue());
		return;
	}

	@Override
	public CIPData createCipData(int array) {
		try {
			return new CIPData(Type.ULINT, array);
		} catch (Exception e) {
			return null;
		}
	}
}
