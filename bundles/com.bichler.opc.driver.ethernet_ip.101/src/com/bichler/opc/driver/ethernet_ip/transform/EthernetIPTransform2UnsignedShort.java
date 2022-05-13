package com.bichler.opc.driver.ethernet_ip.transform;

import java.math.BigInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.opcfoundation.ua.builtintypes.UnsignedLong;
import org.opcfoundation.ua.builtintypes.UnsignedShort;
import org.opcfoundation.ua.builtintypes.Variant;
import com.bichler.opc.comdrv.utils.ValueOutOfRangeException;

import etherip.types.CIPData;

public abstract class EthernetIPTransform2UnsignedShort implements EthernetIPTransformation {
	private Logger logger = Logger.getLogger(getClass().getName());

	@Override
	public Object[] createInternArray(int arrayLength) {
		return new UnsignedShort[arrayLength];
	}

	@Override
	public Variant getDefaultValue() {
		return new Variant(UnsignedShort.MIN_VALUE);
	}

	@Override
	public Object transToIntern(CIPData value, int index) throws ValueOutOfRangeException {
		Number val = null;
		try {
			val = value.getNumber(index);
		} catch (Exception e) {
			logger.log(Level.SEVERE, "{0} | transform index {1}",
					new String[] { e.getMessage(), Integer.toString(index) });
			return new UnsignedShort(0);
		}

		if (val instanceof UnsignedLong) {
			BigInteger big = ((UnsignedLong) val).bigIntegerValue();
			int greater = big.compareTo(BigInteger.valueOf(UnsignedShort.L_MAX_VALUE));
			int lower = big.compareTo(BigInteger.valueOf(UnsignedShort.L_MIN_VALUE));
			if(greater == 1 || lower == -1)
				throw new ValueOutOfRangeException("Value from plc ('" + val + "') is out of opc range ('0|" + UnsignedShort.MAX_VALUE + "')!");
			return (UnsignedLong) val;
		} else {
			long tempVal = val.longValue();
			if (tempVal > UnsignedShort.L_MAX_VALUE || tempVal < UnsignedShort.L_MIN_VALUE)
				throw new ValueOutOfRangeException("Value from plc ('" + tempVal + "') is out of opc range ('0|" + UnsignedShort.MAX_VALUE + "')!");
			logger.log(Level.FINE, "Transform to UnsignedShort - value: " + val);
			return new UnsignedShort(tempVal);
		}
	}
}
