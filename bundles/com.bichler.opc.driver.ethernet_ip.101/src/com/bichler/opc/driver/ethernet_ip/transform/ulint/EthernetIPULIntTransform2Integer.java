package com.bichler.opc.driver.ethernet_ip.transform.ulint;

import org.opcfoundation.ua.builtintypes.UnsignedLong;

import com.bichler.opc.comdrv.utils.ValueOutOfRangeException;
import com.bichler.opc.driver.ethernet_ip.transform.EthernetIPTransform2Integer;

import etherip.types.CIPData;
import etherip.types.CIPData.Type;

public class EthernetIPULIntTransform2Integer extends EthernetIPTransform2Integer {

	@Override
	public void transToDevice(CIPData data, Object value, int index) throws IndexOutOfBoundsException, Exception {
		int val = (Integer) value;

		if (val < 0)
			throw new ValueOutOfRangeException("Value from opc ('" + val + "') is out of plc range ('" +UnsignedLong.MIN_VALUE + "|"+UnsignedLong.MAX_VALUE+"')!");

		data.set(index, ((Integer) value).shortValue());
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
