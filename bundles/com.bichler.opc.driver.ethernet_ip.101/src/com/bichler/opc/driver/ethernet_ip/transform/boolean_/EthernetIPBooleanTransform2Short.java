package com.bichler.opc.driver.ethernet_ip.transform.boolean_;

import java.util.logging.Level;

import com.bichler.opc.comdrv.utils.ValueOutOfRangeException;
import com.bichler.opc.driver.ethernet_ip.transform.EthernetIPTransform2Short;
import etherip.types.CIPData;
import etherip.types.CIPData.Type;

public class EthernetIPBooleanTransform2Short extends EthernetIPTransform2Short {

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
		short val = (Short) value;

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

		if (val.intValue() > Short.MAX_VALUE || val.intValue() < Short.MIN_VALUE)
			throw new ValueOutOfRangeException("Value from plc is out of OPC UA range!");

		logger.log(Level.FINE, "Transform Bool to Short - value: " + val.shortValue());
		if (val.shortValue() == -1)
			return (short)1;
		return val.shortValue();
	}
}
