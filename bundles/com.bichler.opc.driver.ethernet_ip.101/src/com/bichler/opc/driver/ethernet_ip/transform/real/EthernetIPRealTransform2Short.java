package com.bichler.opc.driver.ethernet_ip.transform.real;

import com.bichler.opc.driver.ethernet_ip.transform.EthernetIPTransform2Short;

import etherip.types.CIPData;
import etherip.types.CIPData.Type;

public class EthernetIPRealTransform2Short extends EthernetIPTransform2Short {

	@Override
	public void transToDevice(CIPData data, Object value, int index) throws IndexOutOfBoundsException, Exception {
		data.set(index, new Float(((Short) value).floatValue()));
		return;
	}

	@Override
	public CIPData createCipData(int array) {
		try {
			return new CIPData(Type.REAL, array);
		} catch (Exception e) {
			return null;
		}
	}
}
