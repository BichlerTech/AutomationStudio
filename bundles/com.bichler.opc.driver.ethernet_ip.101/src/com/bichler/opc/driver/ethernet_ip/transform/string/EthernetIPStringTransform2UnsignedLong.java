package com.bichler.opc.driver.ethernet_ip.transform.string;

import org.opcfoundation.ua.builtintypes.UnsignedLong;

import com.bichler.opc.comdrv.utils.ValueOutOfRangeException;
import com.bichler.opc.driver.ethernet_ip.transform.EthernetIPTransform2UnsignedLong;

import etherip.types.CIPData;
import etherip.types.CIPData.Type;

public class EthernetIPStringTransform2UnsignedLong extends EthernetIPTransform2UnsignedLong {

	@Override
	public void transToDevice(CIPData data, Object value, int index) throws IndexOutOfBoundsException, Exception {
		data.set(index, ((UnsignedLong) value).longValue());
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
		UnsignedLong val = null;
		try {
			val = UnsignedLong.parseUnsignedLong(value.getString());
		} catch (Exception e) {
			throw new ValueOutOfRangeException("Value from plc is out of OPC UA range!");
		}
		return val;
	}
}
