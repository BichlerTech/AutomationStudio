package com.bichler.opc.driver.ethernet_ip.transform;

import java.math.BigInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.opcfoundation.ua.builtintypes.UnsignedLong;
import org.opcfoundation.ua.builtintypes.Variant;
import com.bichler.opc.comdrv.utils.ValueOutOfRangeException;
import etherip.types.CIPData;

public abstract class EthernetIPTransform2Short implements EthernetIPTransformation {
	protected Logger logger = Logger.getLogger(getClass().getName());

	@Override
	public Object[] createInternArray(int arrayLength) {
		return new Short[arrayLength];
	}

	@Override
	public Variant getDefaultValue() {
		return new Variant(Short.MIN_VALUE);
	}

	@Override
	public Object transToIntern(CIPData value, int index) throws ValueOutOfRangeException {
		Number val = null;
		try {
			val = value.getNumber(index);
		} catch (Exception e) {
			logger.log(Level.SEVERE, "{0} | transform index {1}",
					new String[] { e.getMessage(), Integer.toString(index) });
			return (short) 0;
		}

		if (val instanceof UnsignedLong) {
			BigInteger big = ((UnsignedLong) val).bigIntegerValue();
			int greater = big.compareTo(BigInteger.valueOf(Short.MAX_VALUE));
			int lower = big.compareTo(BigInteger.valueOf(Short.MIN_VALUE));
			if (greater == 1 || lower == -1)
				throw new ValueOutOfRangeException("Value from plc ('" + val + "') is out of opc range ('"+Short.MIN_VALUE+"|" + Short.MAX_VALUE + "')!");
			return (UnsignedLong) val;
		} else {
			long tempVal = val.longValue();
			if (tempVal > Short.MAX_VALUE || tempVal < Short.MIN_VALUE)
				throw new ValueOutOfRangeException("Value from plc ('" + val + "') is out of opc range ('"+Short.MIN_VALUE+"|" + Short.MAX_VALUE + "')!");

			logger.log(Level.FINE, "Transform to Short - value: " + val);
			return val.shortValue();
		}
	}
}
