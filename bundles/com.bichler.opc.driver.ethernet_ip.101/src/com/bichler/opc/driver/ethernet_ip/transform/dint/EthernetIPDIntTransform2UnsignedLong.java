package com.bichler.opc.driver.ethernet_ip.transform.dint;

import org.opcfoundation.ua.builtintypes.UnsignedLong;

import com.bichler.opc.comdrv.utils.ValueOutOfRangeException;
import com.bichler.opc.driver.ethernet_ip.transform.EthernetIPTransform2UnsignedLong;

import etherip.types.CIPData;
import etherip.types.CIPData.Type;

public class EthernetIPDIntTransform2UnsignedLong extends EthernetIPTransform2UnsignedLong {

	@Override
	public void transToDevice(CIPData data, Object value, int index) throws IndexOutOfBoundsException, Exception {
		long val = ((UnsignedLong) value).longValue();

		if (val > Integer.MAX_VALUE || val < Integer.MIN_VALUE)
			throw new ValueOutOfRangeException("Value from OPC UA is out of plc range!");

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
