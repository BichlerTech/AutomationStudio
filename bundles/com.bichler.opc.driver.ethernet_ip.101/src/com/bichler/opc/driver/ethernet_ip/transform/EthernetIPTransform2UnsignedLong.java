package com.bichler.opc.driver.ethernet_ip.transform;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.opcfoundation.ua.builtintypes.UnsignedLong;
import org.opcfoundation.ua.builtintypes.Variant;
import com.bichler.opc.comdrv.utils.ValueOutOfRangeException;
import etherip.types.CIPData;

public abstract class EthernetIPTransform2UnsignedLong implements EthernetIPTransformation {
	protected Logger logger = Logger.getLogger(getClass().getName());

	@Override
	public Object[] createInternArray(int arrayLength) {
		return new UnsignedLong[arrayLength];
	}

	@Override
	public Variant getDefaultValue() {
		return new Variant(UnsignedLong.MIN_VALUE);
	}

	@Override
	public Object transToIntern(CIPData value, int index) throws ValueOutOfRangeException {
		Number val = null;
		try {
			val = value.getNumber(index);
		} catch (Exception e) {
			logger.log(Level.SEVERE, "{0} | transform index {1}",
					new String[] { e.getMessage(), Integer.toString(index) });
			return new UnsignedLong(0);
		}
		if (val instanceof UnsignedLong)
			return val;
		else if( val instanceof Float) {
			float tempVal = val.floatValue();
			if (tempVal > (float)Long.MAX_VALUE || tempVal < (float)Long.MIN_VALUE)
				throw new ValueOutOfRangeException("Value from plc ('" + val + "') is out of opc range ('0|" + UnsignedLong.MAX_VALUE + "')!");
			return new UnsignedLong((long)tempVal);
		} else if( val instanceof Double) {
			double tempVal = val.doubleValue();
			if (tempVal > (double)Long.MAX_VALUE || tempVal < (double)Long.MIN_VALUE)
				throw new ValueOutOfRangeException("Value from plc ('" + val + "') is out of opc range ('0|" + UnsignedLong.MAX_VALUE + "')!");
			return new UnsignedLong((long)tempVal);
		}
		else {
			long tempVal = val.longValue();
			if (tempVal > Long.MAX_VALUE || tempVal < UnsignedLong.MIN_VALUE.longValue())
				throw new ValueOutOfRangeException("Value from plc ('" + val + "') is out of opc range ('0|" + UnsignedLong.MAX_VALUE + "')!");
			logger.log(Level.FINE, "Transform to UnsignedInteger - value: " + val);
			return new UnsignedLong(tempVal);
		}
	}
}
