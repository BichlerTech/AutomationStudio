package com.bichler.opc.driver.ethernet_ip.transform;

import java.math.BigInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opcfoundation.ua.builtintypes.UnsignedByte;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.builtintypes.UnsignedLong;
import org.opcfoundation.ua.builtintypes.Variant;
import com.bichler.opc.comdrv.utils.ValueOutOfRangeException;
import etherip.types.CIPData;

public abstract class EthernetIPTransform2UnsignedByte implements EthernetIPTransformation {
	protected Logger logger = Logger.getLogger(getClass().getName());

	@Override
	public Object[] createInternArray(int arrayLength) {
		return new UnsignedByte[arrayLength];
	}

	@Override
	public Variant getDefaultValue() {
		return new Variant(UnsignedByte.MIN_VALUE);
	}

	@Override
	public Object transToIntern(CIPData value, int index) throws ValueOutOfRangeException {
		Number val = null;
		try {
			val = value.getNumber(index);
		} catch (Exception e) {
			logger.log(Level.SEVERE, "{0} | transform index {1}",
					new String[] { e.getMessage(), Integer.toString(index) });
			return new UnsignedByte(0);
		}
		if (val instanceof UnsignedLong) {
			BigInteger big = ((UnsignedLong) val).bigIntegerValue();
			int greater = big.compareTo(BigInteger.valueOf(UnsignedInteger.L_MAX_VALUE));
			int lower = big.compareTo(BigInteger.valueOf(UnsignedInteger.L_MIN_VALUE));
			if (greater == 1 || lower == -1)
				throw new ValueOutOfRangeException("Value from plc ('" + val + "') is out of opc range ('0|" + UnsignedByte.MAX_VALUE + "')!");
			return (UnsignedLong) val;
		} else {
			long tempVal = val.longValue();
			if (tempVal > UnsignedByte.MAX_VALUE.intValue() || tempVal < UnsignedByte.MIN_VALUE.intValue())
				throw new ValueOutOfRangeException("Value from plc ('" + val + "') is out of opc range ('0|" + UnsignedByte.MAX_VALUE + "')!");
			logger.log(Level.FINE, "Transform to UnsignedByte - value: " + val);
			return new UnsignedByte(tempVal);
		}
	}
}
