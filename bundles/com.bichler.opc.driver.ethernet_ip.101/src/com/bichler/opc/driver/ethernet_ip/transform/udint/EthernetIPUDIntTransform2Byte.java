package com.bichler.opc.driver.ethernet_ip.transform.udint;

import com.bichler.opc.driver.ethernet_ip.transform.EthernetIPTransform2Byte;

import etherip.types.CIPData;
import etherip.types.CIPData.Type;

public class EthernetIPUDIntTransform2Byte extends EthernetIPTransform2Byte {

	@Override
	public void transToDevice(CIPData data, Object value, int index) throws IndexOutOfBoundsException, Exception {
		data.set(index, ((Short) value).shortValue());
		return;
	}

	@Override
	public CIPData createCipData(int array) {
		try {
			return new CIPData(Type.UDINT, array);
		} catch (Exception e) {
			return null;
		}
	}
}
