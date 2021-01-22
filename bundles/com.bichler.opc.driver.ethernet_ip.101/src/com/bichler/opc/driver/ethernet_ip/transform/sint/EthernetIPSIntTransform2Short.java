package com.bichler.opc.driver.ethernet_ip.transform.sint;

import com.bichler.opc.comdrv.utils.ValueOutOfRangeException;
import com.bichler.opc.driver.ethernet_ip.transform.EthernetIPTransform2Short;

import etherip.types.CIPData;
import etherip.types.CIPData.Type;

public class EthernetIPSIntTransform2Short extends EthernetIPTransform2Short {

	@Override
	public void transToDevice(CIPData data, Object value, int index) throws IndexOutOfBoundsException, Exception {
		short val = (Short) value;

		if (val > Byte.MAX_VALUE || val < Byte.MIN_VALUE)
			throw new ValueOutOfRangeException("Value from OPC UA is out of plc range!");

		data.set(index, ((Short) value).byteValue());
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