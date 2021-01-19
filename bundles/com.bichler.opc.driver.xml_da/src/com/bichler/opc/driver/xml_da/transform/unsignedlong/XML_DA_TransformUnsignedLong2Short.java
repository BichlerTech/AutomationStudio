package com.bichler.opc.driver.xml_da.transform.unsignedlong;

import org.opcfoundation.ua.builtintypes.UnsignedLong;
import org.opcfoundation.ua.builtintypes.Variant;

import com.bichler.opc.comdrv.utils.ValueOutOfRangeException;
import com.bichler.opc.driver.xml_da.transform.XML_DA_Transformation;

/**
 * this class transforms from UnsignedInt (XML-DA) to Short (OPC UA) and vice
 * versa
 * 
 * @company HB-Softsolution e.U.
 * @author hannes bichler
 * @since 18.12.2013
 * @version 1.0
 * 
 */
public class XML_DA_TransformUnsignedLong2Short implements XML_DA_Transformation {
	@Override
	public Object transToIntern(Object value) throws ClassCastException, ValueOutOfRangeException {
		long in = ((UnsignedLong) value).longValue();
		if (in > Short.MAX_VALUE || in < Short.MIN_VALUE) {
			throw new ValueOutOfRangeException("Couldn't transfor XML-DA value to OPC UA Value, only values between "
					+ Short.MIN_VALUE + " and " + Short.MAX_VALUE + " are allowed, but we got, " + in + ".");
		}
		return (short) in;
	}

	@Override
	public UnsignedLong transToDevice(Variant value) {
		return new UnsignedLong(value.shortValue());
	}

	@Override
	public UnsignedLong transToDevice(Object value) {
		return new UnsignedLong(((Short) value).shortValue());
	}

	public Object[] createInternArray(int arraylenght) {
		return new Short[arraylenght];
	}
}
