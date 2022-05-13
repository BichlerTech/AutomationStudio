package com.bichler.opc.driver.ethernet_ip.transform.ulint;

import org.opcfoundation.ua.builtintypes.UnsignedLong;

import com.bichler.opc.comdrv.utils.ValueOutOfRangeException;
import com.bichler.opc.driver.ethernet_ip.transform.EthernetIPTransform2Double;

import etherip.types.CIPData;
import etherip.types.CIPData.Type;

public class EthernetIPULIntTransform2Double extends EthernetIPTransform2Double {

	@Override
	public void transToDevice(CIPData data, Object value, int index) throws IndexOutOfBoundsException, Exception {
		double val = (Double) value;

		if (val < 0)
			throw new ValueOutOfRangeException("Value from opc ('" + val + "') is out of plc range ('" +UnsignedLong.MIN_VALUE + "|"+UnsignedLong.MAX_VALUE+"')!");

		data.set(index, ((Double) value).shortValue());
		return;
	}

	@Override
	public CIPData createCipData(int array) {
		try {
			return new CIPData(Type.ULINT, array);
		} catch (Exception e) {
			return null;
		}
	}
}
