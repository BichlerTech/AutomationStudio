package com.bichler.opc.driver.ethernet_ip.transform.real;

import com.bichler.opc.comdrv.utils.ValueOutOfRangeException;
import com.bichler.opc.driver.ethernet_ip.transform.EthernetIPTransform2Double;

import etherip.types.CIPData;
import etherip.types.CIPData.Type;

public class EthernetIPRealTransform2Double extends EthernetIPTransform2Double {

	@Override
	public void transToDevice(CIPData data, Object value, int index) throws IndexOutOfBoundsException, Exception {
		double val = ((Double) value).doubleValue();

		if (val > Float.MAX_VALUE)
			throw new ValueOutOfRangeException("Value from OPC UA is out of plc range!");

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
