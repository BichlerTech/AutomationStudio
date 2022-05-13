package com.bichler.opc.driver.ethernet_ip.transform.lreal;

import com.bichler.opc.comdrv.utils.ValueOutOfRangeException;
import com.bichler.opc.driver.ethernet_ip.transform.EthernetIPTransform2Double;

import etherip.types.CIPData;
import etherip.types.CIPData.Type;

public class EthernetIPLRealTransform2Double extends EthernetIPTransform2Double {

	@Override
	public void transToDevice(CIPData data, Object value, int index) throws IndexOutOfBoundsException, Exception {
		double val = ((Double) value).doubleValue();

		if (val > Double.MAX_VALUE)
			throw new ValueOutOfRangeException("Value from opc ('" + val + "') is out of plc range ('" + Double.MIN_VALUE + "|"+Double.MAX_VALUE+"')!");

		data.set(index, new Double((double) val));
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
