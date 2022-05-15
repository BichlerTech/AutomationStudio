package com.bichler.opc.driver.ethernet_ip.transform;

import java.math.BigInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.opcfoundation.ua.builtintypes.UnsignedLong;
import org.opcfoundation.ua.builtintypes.Variant;
import com.bichler.opc.comdrv.utils.ValueOutOfRangeException;
import etherip.types.CIPData;

public abstract class EthernetIPTransform2Integer implements EthernetIPTransformation {
	protected Logger logger = Logger.getLogger(getClass().getName());

	@Override
	public Object[] createInternArray(int arrayLength) {
		return new Integer[arrayLength];
	}

	@Override
	public Variant getDefaultValue() {
		return new Variant(Integer.MIN_VALUE);
	}

	@Override
	public Object transToIntern(CIPData value, int index) throws ValueOutOfRangeException {
		Number val = null;
		try {
			val = value.getNumber(index);
		} catch (Exception e) {
			logger.log(Level.SEVERE, "{0} | transform index {1}",
					new String[] { e.getMessage(), Integer.toString(index) });
			return 0.0;
		}
		if (val instanceof UnsignedLong) {
			BigInteger big = ((UnsignedLong) val).bigIntegerValue();
			int greater = big.compareTo(BigInteger.valueOf(Integer.MAX_VALUE));
			int lower = big.compareTo(BigInteger.valueOf(Integer.MIN_VALUE));
			if (greater == 1 || lower == -1)
				throw new ValueOutOfRangeException("Value from plc ('" + val + "') is out of opc range ('"+Integer.MIN_VALUE+"|" + Integer.MAX_VALUE + "')!");
			return (UnsignedLong) val;
		} else {
			long tempVal = val.longValue();
			if (tempVal > Integer.MAX_VALUE || tempVal < Integer.MIN_VALUE)
				throw new ValueOutOfRangeException("Value from plc ('" + val + "') is out of opc range ('"+Integer.MIN_VALUE+"|" + Integer.MAX_VALUE + "')!");

			logger.log(Level.FINE, "Transform to Integer - value: " + val);
			return val.intValue();
		}
	}
}
