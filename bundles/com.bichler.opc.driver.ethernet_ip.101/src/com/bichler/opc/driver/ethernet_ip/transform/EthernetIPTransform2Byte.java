package com.bichler.opc.driver.ethernet_ip.transform;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.opcfoundation.ua.builtintypes.Variant;
import com.bichler.opc.comdrv.utils.ValueOutOfRangeException;
import etherip.types.CIPData;

public abstract class EthernetIPTransform2Byte implements EthernetIPTransformation {

	protected Logger logger = Logger.getLogger(getClass().getName());
	
	@Override
	public Object[] createInternArray(int arrayLength) {
		return new Byte[arrayLength];
	}

	@Override
	public Variant getDefaultValue() {
		return new Variant(Byte.MIN_VALUE);
	}

	@Override
	public Object transToIntern(CIPData value, int index) throws ValueOutOfRangeException {
		Number val = null;
		try {
			val = value.getNumber(index);
		} catch (Exception e) {
			logger.log(Level.SEVERE, "{0} | transform index {1}",
					new String[] { e.getMessage(), Integer.toString(index) });
			return (byte)0;
		}

		if (val.intValue() > Byte.MAX_VALUE || val.intValue() < Byte.MIN_VALUE)
			throw new ValueOutOfRangeException("Value from plc is out of OPC UA range!");
		logger.log(Level.FINE, "Transform to Byte - value: " + val);
		return val.byteValue();
	}
}
