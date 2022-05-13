package com.bichler.opc.driver.ethernet_ip.transform.udint;

import org.opcfoundation.ua.builtintypes.UnsignedInteger;

import com.bichler.opc.comdrv.utils.ValueOutOfRangeException;
import com.bichler.opc.driver.ethernet_ip.transform.EthernetIPTransform2UnsignedInteger;

import etherip.types.CIPData;
import etherip.types.CIPData.Type;

public class EthernetIPUDIntTransform2UnsignedInteger extends EthernetIPTransform2UnsignedInteger {

	@Override
	public void transToDevice(CIPData data, Object value, int index) throws IndexOutOfBoundsException, Exception {
		long val = ((UnsignedInteger) value).longValue();

		if (val > UnsignedInteger.L_MAX_VALUE || val < 0)
			throw new ValueOutOfRangeException("Value from opc ('" + val + "') is out of plc range ('" +UnsignedInteger.L_MIN_VALUE + "|"+UnsignedInteger.L_MAX_VALUE+"')!");

		data.set(index, ((UnsignedInteger) value).longValue());
		return;
	}

	@Override
	public CIPData createCipData(int array) {
		try {
			return new CIPData(Type.UDINT, array);
		} catch (Exception e) {
			return null;
		}
	}
}
