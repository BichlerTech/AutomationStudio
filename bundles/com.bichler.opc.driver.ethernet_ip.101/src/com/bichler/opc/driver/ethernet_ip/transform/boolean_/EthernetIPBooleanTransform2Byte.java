package com.bichler.opc.driver.ethernet_ip.transform.boolean_;

import java.util.logging.Level;
import org.opcfoundation.ua.builtintypes.Variant;
import com.bichler.opc.comdrv.utils.ValueOutOfRangeException;
import com.bichler.opc.driver.ethernet_ip.transform.EthernetIPTransform2Byte;
import etherip.types.CIPData;
import etherip.types.CIPData.Type;

public class EthernetIPBooleanTransform2Byte extends EthernetIPTransform2Byte {

	@Override
	public void transToDevice(CIPData data, Object value, int index) throws IndexOutOfBoundsException, Exception {
		boolean val = (Boolean) value;

		if (val)
			data.set(index, 0xFF);
		else
			data.set(index, 0);
		return;
	}

	@Override
	public CIPData createCipData(int array) {
		try {
			return new CIPData(Type.BOOL, array);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public Variant getDefaultValue() {
		return new Variant(Boolean.FALSE);
	}

	@Override
	public Object transToIntern(CIPData value, int index) throws ValueOutOfRangeException {
		Number val = null;
		try {
			val = value.getNumber(index);
		} catch (Exception e) {
			throw new ValueOutOfRangeException("Value from plc is out of OPC UA range!");
		}
		
		logger.log(Level.FINE, "Transform Bool to Byte - value: " + val.byteValue());
		if (val.byteValue() == -1)
			return (byte)1.0;
		return val.byteValue();
	}
}
