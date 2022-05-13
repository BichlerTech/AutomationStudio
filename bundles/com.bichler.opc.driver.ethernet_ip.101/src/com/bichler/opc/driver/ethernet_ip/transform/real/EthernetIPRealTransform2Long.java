package com.bichler.opc.driver.ethernet_ip.transform.real;

import com.bichler.opc.comdrv.utils.ValueOutOfRangeException;
import com.bichler.opc.driver.ethernet_ip.transform.EthernetIPTransform2Long;

import etherip.types.CIPData;
import etherip.types.CIPData.Type;

public class EthernetIPRealTransform2Long extends EthernetIPTransform2Long {

	@Override
	public void transToDevice(CIPData data, Object value, int index) throws IndexOutOfBoundsException, Exception {
		long val = ((Long) value).longValue();

		if (val > Float.MAX_VALUE)
			throw new ValueOutOfRangeException("Value from opc ('" + val + "') is out of plc range ('" + Float.MIN_VALUE + "|"+Float.MAX_VALUE+"')!");

		data.set(index, new Float((float) val));
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
