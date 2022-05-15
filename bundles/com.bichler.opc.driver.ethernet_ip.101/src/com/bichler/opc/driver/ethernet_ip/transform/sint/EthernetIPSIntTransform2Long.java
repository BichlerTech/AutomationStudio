package com.bichler.opc.driver.ethernet_ip.transform.sint;

import com.bichler.opc.comdrv.utils.ValueOutOfRangeException;
import com.bichler.opc.driver.ethernet_ip.transform.EthernetIPTransform2Long;

import etherip.types.CIPData;
import etherip.types.CIPData.Type;

public class EthernetIPSIntTransform2Long extends EthernetIPTransform2Long {

	@Override
	public void transToDevice(CIPData data, Object value, int index) throws IndexOutOfBoundsException, Exception {
		long val = (Long) value;

		if (val > Byte.MAX_VALUE || val < Byte.MIN_VALUE)
			throw new ValueOutOfRangeException("Value from opc ('" + val + "') is out of plc range ('" + Byte.MIN_VALUE + "|"+Byte.MAX_VALUE+"')!");

		data.set(index, ((Long) value).byteValue());
		return;
	}

	@Override
	public CIPData createCipData(int array) {
		try {
			return new CIPData(Type.SINT, array);
		} catch (Exception e) {
			return null;
		}
	}
}
