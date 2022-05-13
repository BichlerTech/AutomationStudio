package com.bichler.opc.driver.ethernet_ip.transform.boolean_;

import java.util.logging.Level;

import org.opcfoundation.ua.builtintypes.UnsignedByte;
import com.bichler.opc.comdrv.utils.ValueOutOfRangeException;
import com.bichler.opc.driver.ethernet_ip.transform.EthernetIPTransform2UnsignedByte;
import etherip.types.CIPData;
import etherip.types.CIPData.Type;

public class EthernetIPBooleanTransform2UnsignedByte extends EthernetIPTransform2UnsignedByte {

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
		short val = ((UnsignedByte) value).shortValue();

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

		if (val.longValue() > UnsignedByte.L_MAX_VALUE || val.longValue() < UnsignedByte.L_MIN_VALUE)
			throw new ValueOutOfRangeException("Value from plc is out of OPC UA range!");

		logger.log(Level.FINE, "Transform Bool to UnsignedByte - value: " + val.shortValue());
		if (val.longValue() == -1)
			return new UnsignedByte(1);
		return new UnsignedByte(val.shortValue());
	}
}
