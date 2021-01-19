package com.bichler.opc.driver.ethernet_ip.transform.dint;

import com.bichler.opc.driver.ethernet_ip.transform.EthernetIPTransform2Short;

import etherip.types.CIPData;
import etherip.types.CIPData.Type;

public class EthernetIPDIntTransform2Short extends EthernetIPTransform2Short {

	@Override
	public void transToDevice(CIPData data, Object value, int index) throws IndexOutOfBoundsException, Exception {
		data.set(index, new Integer((Short) value));
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
