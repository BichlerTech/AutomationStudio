package com.bichler.opc.driver.ethernet_ip.transform.sint;

import org.opcfoundation.ua.builtintypes.UnsignedShort;

import com.bichler.opc.comdrv.utils.ValueOutOfRangeException;
import com.bichler.opc.driver.ethernet_ip.transform.EthernetIPTransform2UnsignedShort;

import etherip.types.CIPData;
import etherip.types.CIPData.Type;

public class EthernetIPSIntTransform2UnsignedShort extends EthernetIPTransform2UnsignedShort {

	@Override
	public void transToDevice(CIPData data, Object value, int index) throws IndexOutOfBoundsException, Exception {
		int val = ((UnsignedShort) value).intValue();

		if (val > Byte.MAX_VALUE || val < Byte.MIN_VALUE)
			throw new ValueOutOfRangeException("Value from OPC UA is out of plc range!");

		data.set(index, ((UnsignedShort) value).byteValue());
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
