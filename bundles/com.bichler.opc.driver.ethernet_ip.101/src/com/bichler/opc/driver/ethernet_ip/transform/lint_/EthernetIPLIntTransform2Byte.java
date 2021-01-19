package com.bichler.opc.driver.ethernet_ip.transform.lint_;

import com.bichler.opc.driver.ethernet_ip.transform.EthernetIPTransform2Byte;

import etherip.types.CIPData;
import etherip.types.CIPData.Type;

public class EthernetIPLIntTransform2Byte extends EthernetIPTransform2Byte {

	@Override
	public void transToDevice(CIPData data, Object value, int index) throws IndexOutOfBoundsException, Exception {
		data.set(index, (Byte) value);
		return;
	}

	@Override
	public CIPData createCipData(int array) {
		try {
			return new CIPData(Type.LINT, array);
		} catch (Exception e) {
			return null;
		}
	}
}
