package com.bichler.opc.driver.ethernet_ip.transform.usint;

import org.opcfoundation.ua.builtintypes.UnsignedByte;

import com.bichler.opc.comdrv.utils.ValueOutOfRangeException;
import com.bichler.opc.driver.ethernet_ip.transform.EthernetIPTransform2Double;

import etherip.types.CIPData;
import etherip.types.CIPData.Type;

public class EthernetIPUSIntTransform2Double extends EthernetIPTransform2Double {

	@Override
	public void transToDevice(CIPData data, Object value, int index) throws IndexOutOfBoundsException, Exception {
		double val = (Double) value;

		if (val > UnsignedByte.L_MAX_VALUE || val < 0)
			throw new ValueOutOfRangeException("Value from opc ('" + val + "') is out of plc range ('" +UnsignedByte.MIN_VALUE + "|"+UnsignedByte.MAX_VALUE+"')!");

		data.set(index, ((Double) value).shortValue());
		return;
	}

	@Override
	public CIPData createCipData(int array) {
		try {
			return new CIPData(Type.USINT, array);
		} catch (Exception e) {
			return null;
		}
	}
}
