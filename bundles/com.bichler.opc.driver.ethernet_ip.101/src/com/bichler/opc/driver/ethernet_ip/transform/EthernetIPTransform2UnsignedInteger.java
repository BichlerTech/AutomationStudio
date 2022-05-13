package com.bichler.opc.driver.ethernet_ip.transform;

import java.math.BigInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.builtintypes.UnsignedLong;
import org.opcfoundation.ua.builtintypes.Variant;
import com.bichler.opc.comdrv.utils.ValueOutOfRangeException;
import etherip.types.CIPData;

public abstract class EthernetIPTransform2UnsignedInteger implements EthernetIPTransformation {
	protected Logger logger = Logger.getLogger(getClass().getName());

	@Override
	public Object[] createInternArray(int arrayLength) {
		return new UnsignedInteger[arrayLength];
	}

	@Override
	public Variant getDefaultValue() {
		return new Variant(UnsignedInteger.MIN_VALUE);
	}

	@Override
	public Object transToIntern(CIPData value, int index) throws ValueOutOfRangeException {
		Number val = null;
		try {
			val = value.getNumber(index);
		} catch (Exception e) {
			logger.log(Level.SEVERE, "{0} | transform index {1}",
					new String[] { e.getMessage(), Integer.toString(index) });
			return new UnsignedInteger(0);
		}
		if (val instanceof UnsignedLong) {
			BigInteger big = ((UnsignedLong) val).bigIntegerValue();
			int greater = big.compareTo(BigInteger.valueOf(UnsignedInteger.L_MAX_VALUE));
			int lower = big.compareTo(BigInteger.valueOf(UnsignedInteger.L_MIN_VALUE));
			if (greater == 1 || lower == -1)
				throw new ValueOutOfRangeException("Value from plc ('" + val + "') is out of opc range ('0|" + UnsignedInteger.MAX_VALUE + "')!");
			return (UnsignedLong) val;
		} else {
			long tempVal = val.longValue();
			if (tempVal > UnsignedInteger.MAX_VALUE.longValue() || tempVal < UnsignedInteger.MIN_VALUE.longValue())
				throw new ValueOutOfRangeException("Value from plc ('" + val + "') is out of opc range ('0|" + UnsignedInteger.MAX_VALUE + "')!");
			logger.log(Level.FINE, "Transform to UnsignedInteger - value: " + val);
			return new UnsignedInteger(tempVal);
		}
	}
}
