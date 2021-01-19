package com.bichler.opc.driver.xml_da.transform.unsignedlong;

import org.opcfoundation.ua.builtintypes.UnsignedLong;
import org.opcfoundation.ua.builtintypes.Variant;

import com.bichler.opc.comdrv.utils.ValueOutOfRangeException;
import com.bichler.opc.driver.xml_da.transform.XML_DA_Transformation;

/**
 * this class transforms from UnsignedLong (XML-DA) to Float (OPC UA) and vice
 * versa
 * 
 * @company HB-Softsolution e.U.
 * @author hannes bichler
 * @since 18.12.2013
 * @version 1.0
 * 
 */
public class XML_DA_TransformUnsignedLong2Float implements XML_DA_Transformation {
	@Override
	public Object transToIntern(Object value) throws ClassCastException, ValueOutOfRangeException {
		long in = ((UnsignedLong) value).longValue();
		if (in > Float.MAX_VALUE || in < Float.MIN_VALUE) {
			throw new ValueOutOfRangeException("Couldn't transfor XML-DA value to OPC UA Value, only values between "
					+ Float.MIN_VALUE + " and " + Float.MAX_VALUE + " are allowed, but we got, " + in + ".");
		}
		return (float) in;
	}

	@Override
	public UnsignedLong transToDevice(Variant value) {
		return new UnsignedLong(value.longValue());
	}

	@Override
	public UnsignedLong transToDevice(Object value) {
		return new UnsignedLong(((Float) value).longValue());
	}

	public Object[] createInternArray(int arraylenght) {
		return new Float[arraylenght];
	}
}
