package com.bichler.opc.driver.ethernet_ip.transform.string;

import org.opcfoundation.ua.builtintypes.UnsignedInteger;

import com.bichler.opc.comdrv.utils.ValueOutOfRangeException;
import com.bichler.opc.driver.ethernet_ip.transform.EthernetIPTransform2UnsignedInteger;

import etherip.types.CIPData;
import etherip.types.CIPData.Type;

public class EthernetIPStringTransform2UnsignedInteger extends EthernetIPTransform2UnsignedInteger {

	@Override
	public void transToDevice(CIPData data, Object value, int index) throws IndexOutOfBoundsException, Exception {
		data.set(index, ((UnsignedInteger) value).longValue());
		return;
	}

	@Override
	public CIPData createCipData(int array) {
		try {
			return new CIPData(Type.LINT, array);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public Object transToIntern(CIPData value, int index) throws ValueOutOfRangeException {
		UnsignedInteger val = null;
		try {
			val = UnsignedInteger.parseUnsignedInteger(value.getString());
		} catch (Exception e) {
			throw new ValueOutOfRangeException("Value from plc is out of OPC UA range!");
		}
		return val;
	}
}
