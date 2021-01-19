package com.bichler.opc.driver.ethernet_ip.transform.dint;

import com.bichler.opc.driver.ethernet_ip.transform.EthernetIPTransform2Integer;

import etherip.types.CIPData;
import etherip.types.CIPData.Type;

public class EthernetIPDIntTransform2Integer extends EthernetIPTransform2Integer {

	@Override
	public void transToDevice(CIPData data, Object value, int index) throws IndexOutOfBoundsException, Exception {
		data.set(index, (Integer) value);
		return;
	}

	@Override
	public CIPData createCipData(int array) {
		try {
			return new CIPData(Type.DINT, array);
		} catch (Exception e) {
			return null;
		}
	}
}
