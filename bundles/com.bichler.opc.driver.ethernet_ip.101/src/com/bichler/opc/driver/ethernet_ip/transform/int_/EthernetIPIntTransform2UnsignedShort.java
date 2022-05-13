package com.bichler.opc.driver.ethernet_ip.transform.int_;

import org.opcfoundation.ua.builtintypes.UnsignedShort;

import com.bichler.opc.comdrv.utils.ValueOutOfRangeException;
import com.bichler.opc.driver.ethernet_ip.transform.EthernetIPTransform2UnsignedShort;

import etherip.types.CIPData;
import etherip.types.CIPData.Type;

public class EthernetIPIntTransform2UnsignedShort extends EthernetIPTransform2UnsignedShort {

	@Override
	public void transToDevice(CIPData data, Object value, int index) throws IndexOutOfBoundsException, Exception {
		int val = ((UnsignedShort) value).intValue();

		if (val > UnsignedShort.L_MAX_VALUE || val < UnsignedShort.L_MIN_VALUE)
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
