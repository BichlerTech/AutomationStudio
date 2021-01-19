package com.bichler.opc.driver.ethernet_ip.transform.boolean_;

import com.bichler.opc.comdrv.utils.ValueOutOfRangeException;
import com.bichler.opc.driver.ethernet_ip.transform.EthernetIPTransform2Float;

import etherip.types.CIPData;
import etherip.types.CIPData.Type;

public class EthernetIPBooleanTransform2Float extends EthernetIPTransform2Float {

	@Override
	public CIPData createCipData(int array) {
		try {
			return new CIPData(Type.BOOL, array);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public void transToDevice(CIPData data, Object value, int index) throws IndexOutOfBoundsException, Exception {
		float val = (Float) value;

		if (val == 1)
			data.set(index, 0xFF);
		else
			data.set(index, 0);
		return;
	}

	@Override
	public Object transToIntern(CIPData value, int index) throws ValueOutOfRangeException {
		Number val = null;
		try {
			val = value.getNumber(index);
		} catch (Exception e) {
			throw new ValueOutOfRangeException("Value from plc is out of OPC UA range!");
		}

		if (val.floatValue() > Float.MAX_VALUE)
			throw new ValueOutOfRangeException("Value from plc is out of OPC UA range!");

		if (val.floatValue() == -1)
			return 1;
		return val.intValue();
	}
}
