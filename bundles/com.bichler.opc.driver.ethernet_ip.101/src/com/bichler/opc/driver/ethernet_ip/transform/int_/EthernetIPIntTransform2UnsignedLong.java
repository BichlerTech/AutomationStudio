package com.bichler.opc.driver.ethernet_ip.transform.int_;

import org.opcfoundation.ua.builtintypes.UnsignedLong;

import com.bichler.opc.comdrv.utils.ValueOutOfRangeException;
import com.bichler.opc.driver.ethernet_ip.transform.EthernetIPTransform2UnsignedLong;

import etherip.types.CIPData;
import etherip.types.CIPData.Type;

public class EthernetIPIntTransform2UnsignedLong extends EthernetIPTransform2UnsignedLong {

	@Override
	public void transToDevice(CIPData data, Object value, int index) throws IndexOutOfBoundsException, Exception {
		long val = ((UnsignedLong) value).longValue();

		if (val > Short.MAX_VALUE || val < Short.MIN_VALUE)
			throw new ValueOutOfRangeException("Value from opc ('" + val + "') is out of plc range ('" + Short.MIN_VALUE + "|"+Short.MAX_VALUE+"')!");

		data.set(index, new Short((short) val));
		return;
	}

	@Override
	public CIPData createCipData(int array) {
		try {
			return new CIPData(Type.INT, array);
		} catch (Exception e) {
			return null;
		}
	}
}
