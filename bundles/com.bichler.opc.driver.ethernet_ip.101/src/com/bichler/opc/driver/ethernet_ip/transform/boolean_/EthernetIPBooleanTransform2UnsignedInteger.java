package com.bichler.opc.driver.ethernet_ip.transform.boolean_;

import java.util.logging.Level;

import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import com.bichler.opc.comdrv.utils.ValueOutOfRangeException;
import com.bichler.opc.driver.ethernet_ip.transform.EthernetIPTransform2UnsignedInteger;
import etherip.types.CIPData;
import etherip.types.CIPData.Type;

public class EthernetIPBooleanTransform2UnsignedInteger extends EthernetIPTransform2UnsignedInteger {

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
		long val = ((UnsignedInteger) value).longValue();

		if (val == 1)
			data.set(index, 0xFF);
		else if(val == 0)
			data.set(index, 0);
		else 
			throw new ValueOutOfRangeException("Value from opc ('" + val + "') is out of plc range ('0|1')!");
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

		if (val.longValue() > UnsignedInteger.L_MAX_VALUE || val.longValue() < UnsignedInteger.L_MIN_VALUE)
			throw new ValueOutOfRangeException("Value from plc is out of OPC UA range!");

		logger.log(Level.FINE, "Transform Bool to UnsignedInteger - value: " + val.shortValue());
		if (val.longValue() == -1)
			return new UnsignedInteger(1);
		return new UnsignedInteger(val.longValue());
	}
}
