package com.bichler.opc.driver.ethernet_ip.transform.lreal;

import com.bichler.opc.driver.ethernet_ip.transform.EthernetIPTransform2Float;

import etherip.types.CIPData;
import etherip.types.CIPData.Type;

public class EthernetIPLRealTransform2Float extends EthernetIPTransform2Float {

	@Override
	public void transToDevice(CIPData data, Object value, int index) throws IndexOutOfBoundsException, Exception {
		data.set(index, (double) value);
		return;
	}

	@Override
	public CIPData createCipData(int array) {
		try {
			return new CIPData(Type.LREAL, array);
		} catch (Exception e) {
			return null;
		}
	}
}
