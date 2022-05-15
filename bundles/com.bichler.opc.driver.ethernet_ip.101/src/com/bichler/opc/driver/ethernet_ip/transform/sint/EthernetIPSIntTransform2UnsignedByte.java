package com.bichler.opc.driver.ethernet_ip.transform.sint;

import org.opcfoundation.ua.builtintypes.UnsignedByte;

import com.bichler.opc.comdrv.utils.ValueOutOfRangeException;
import com.bichler.opc.driver.ethernet_ip.transform.EthernetIPTransform2UnsignedByte;

import etherip.types.CIPData;
import etherip.types.CIPData.Type;

public class EthernetIPSIntTransform2UnsignedByte extends EthernetIPTransform2UnsignedByte {

	@Override
	public void transToDevice(CIPData data, Object value, int index) throws IndexOutOfBoundsException, Exception {
		short val = ((UnsignedByte) value).shortValue();

		if (val > Byte.MAX_VALUE || val < Byte.MIN_VALUE)
			throw new ValueOutOfRangeException("Value from opc ('" + val + "') is out of plc range ('" + Byte.MIN_VALUE + "|"+Byte.MAX_VALUE+"')!");

		data.set(index, ((UnsignedByte) value).byteValue());
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
