package com.bichler.opc.driver.ethernet_ip.transform.int_;

import com.bichler.opc.driver.ethernet_ip.transform.EthernetIPTransform2Boolean;

import etherip.types.CIPData;
import etherip.types.CIPData.Type;

public class EthernetIPIntTransform2Boolean extends EthernetIPTransform2Boolean {

	@Override
	public CIPData createCipData(int array) {
		try {
			return new CIPData(Type.INT, array);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public void transToDevice(CIPData data, Object value, int index) throws IndexOutOfBoundsException, Exception {
		if ((Boolean) value)
			data.set(index, 1);
		else
			data.set(index, 0);
		return;
	}
}
