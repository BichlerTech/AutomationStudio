package com.bichler.opc.driver.ethernet_ip.transform.ulint;

import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import com.bichler.opc.driver.ethernet_ip.transform.EthernetIPTransform2UnsignedInteger;
import etherip.types.CIPData;
import etherip.types.CIPData.Type;

public class EthernetIPULIntTransform2UnsignedInteger extends EthernetIPTransform2UnsignedInteger {

	@Override
	public void transToDevice(CIPData data, Object value, int index) throws IndexOutOfBoundsException, Exception {
		data.set(index, ((UnsignedInteger) value).shortValue());
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
