package com.bichler.opc.driver.ethernet_ip.transform;

import java.math.BigInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.opcfoundation.ua.builtintypes.UnsignedLong;
import org.opcfoundation.ua.builtintypes.Variant;
import com.bichler.opc.comdrv.utils.ValueOutOfRangeException;
import etherip.types.CIPData;

public abstract class EthernetIPTransform2Long implements EthernetIPTransformation {
	protected Logger logger = Logger.getLogger(getClass().getName());

	@Override
	public Object[] createInternArray(int arrayLength) {
		return new Long[arrayLength];
	}

	@Override
	public Variant getDefaultValue() {
		return new Variant(Long.MIN_VALUE);
	}

	@Override
	public Object transToIntern(CIPData value, int index) throws ValueOutOfRangeException {
		Number val = null;
		try {
			val = value.getNumber(0);
		} catch (Exception e) {
			logger.log(Level.SEVERE, "{0} | transform index {1}",
					new String[] { e.getMessage(), Integer.toString(index) });
			return 0l;
		}
		if (val instanceof UnsignedLong) {
			BigInteger big = ((UnsignedLong) val).bigIntegerValue();
			int greater = big.compareTo(BigInteger.valueOf(Long.MAX_VALUE));
			int lower = big.compareTo(BigInteger.valueOf(Long.MIN_VALUE));
			if (greater == 1 || lower == -1)
				throw new ValueOutOfRangeException("Value from plc ('" + val + "') is out of opc range ('"+Long.MIN_VALUE+"|" + Long.MAX_VALUE + "')!");
			return (UnsignedLong) val;
		} else if( val instanceof Float) {
			float tempVal = val.floatValue();
			if (tempVal > (float)Long.MAX_VALUE || tempVal < (float)Long.MIN_VALUE)
				throw new ValueOutOfRangeException("Value from plc ('" + val + "') is out of opc range ('"+Long.MIN_VALUE+"|" + Long.MAX_VALUE + "')!");
			return (long)tempVal;
		} else if( val instanceof Double) {
			double tempVal = val.doubleValue();
			if (tempVal > (double)Long.MAX_VALUE || tempVal < (double)Long.MIN_VALUE)
				throw new ValueOutOfRangeException("Value from plc ('" + val + "') is out of opc range ('"+Long.MIN_VALUE+"|" + Long.MAX_VALUE + "')!");
			return (long)tempVal;
		}
		else {
			long tempVal = val.longValue();
			if (tempVal > Long.MAX_VALUE || tempVal < Long.MIN_VALUE)
				throw new ValueOutOfRangeException("Value from plc ('" + val + "') is out of opc range ('"+Long.MIN_VALUE+"|" + Long.MAX_VALUE + "')!");

			logger.log(Level.FINE, "Transform to Long - value: " + val);
			return tempVal;
		}
	}
}
