package com.bichler.opc.driver.ethernet_ip.transform.dint;

import com.bichler.opc.comdrv.utils.ValueOutOfRangeException;
import com.bichler.opc.driver.ethernet_ip.transform.EthernetIPTransform2Double;

import etherip.types.CIPData;
import etherip.types.CIPData.Type;

public class EthernetIPDIntTransform2Double extends EthernetIPTransform2Double {

	@Override
	public void transToDevice(CIPData data, Object value, int index) throws IndexOutOfBoundsException, Exception {
		double val = (Double) value;

		if (val > Integer.MAX_VALUE || val < Integer.MIN_VALUE)
			throw new ValueOutOfRangeException("Value from opc ('" + val + "') is out of plc range ('" + Integer.MIN_VALUE + "|"+Integer.MAX_VALUE+"')!");

		data.set(index, new Integer((byte) val));
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
