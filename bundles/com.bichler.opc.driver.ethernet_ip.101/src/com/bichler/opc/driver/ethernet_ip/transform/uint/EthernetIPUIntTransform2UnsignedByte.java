package com.bichler.opc.driver.ethernet_ip.transform.uint;

import org.opcfoundation.ua.builtintypes.UnsignedByte;
import org.opcfoundation.ua.builtintypes.UnsignedShort;

import com.bichler.opc.comdrv.utils.ValueOutOfRangeException;
import com.bichler.opc.driver.ethernet_ip.transform.EthernetIPTransform2UnsignedByte;

import etherip.types.CIPData;
import etherip.types.CIPData.Type;

public class EthernetIPUIntTransform2UnsignedByte extends EthernetIPTransform2UnsignedByte {

	@Override
	public void transToDevice(CIPData data, Object value, int index) throws IndexOutOfBoundsException, Exception {
		short val = ((UnsignedByte) value).shortValue();

		if (val > UnsignedShort.L_MAX_VALUE || val < 0)
			throw new ValueOutOfRangeException("Value from opc ('" + val + "') is out of plc range ('" +UnsignedShort.L_MIN_VALUE + "|"+UnsignedShort.L_MAX_VALUE+"')!");

		data.set(index, ((UnsignedByte) value).shortValue());
		return;
	}

	@Override
	public CIPData createCipData(int array) {
		try {
			return new CIPData(Type.UINT, array);
		} catch (Exception e) {
			return null;
		}
	}
}
