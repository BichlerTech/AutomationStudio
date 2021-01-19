package com.bichler.opc.driver.ethernet_ip.transform.int_;

import com.bichler.opc.comdrv.utils.ValueOutOfRangeException;
import com.bichler.opc.driver.ethernet_ip.transform.EthernetIPTransform2Float;

import etherip.types.CIPData;
import etherip.types.CIPData.Type;

public class EthernetIPIntTransform2Float extends EthernetIPTransform2Float {

	@Override
	public void transToDevice(CIPData data, Object value, int index) throws IndexOutOfBoundsException, Exception {
		int val = ((Float) value).intValue();

		if (val > Short.MAX_VALUE || val < Short.MIN_VALUE)
			throw new ValueOutOfRangeException("Value from OPC UA is out of plc range!");

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
