package com.bichler.opc.driver.xml_da.transform.float_;

import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.builtintypes.Variant;

import com.bichler.opc.comdrv.utils.ValueOutOfRangeException;
import com.bichler.opc.driver.xml_da.transform.XML_DA_Transformation;

/**
 * this class transforms from Double (XML-DA) to UnsignedInteger (OPC UA) and
 * vice versa
 * 
 * @company HB-Softsolution e.U.
 * @author hannes bichler
 * @since 18.12.2013
 * @version 1.0
 * 
 */
public class XML_DA_TransformFloat2UnsignedInteger implements XML_DA_Transformation {
	@Override
	public Object transToIntern(Object value) throws ClassCastException, ValueOutOfRangeException {
		float in = (Float) value;
		if (in > UnsignedInteger.L_MAX_VALUE || in < UnsignedInteger.L_MIN_VALUE) {
			throw new ValueOutOfRangeException(
					"Couldn't transfor XML-DA value to OPC UA Value, only values between " + UnsignedInteger.L_MIN_VALUE
							+ " and " + UnsignedInteger.L_MAX_VALUE + " are allowed, but we got, " + in + ".");
		}
		return new UnsignedInteger((int) in);
	}

	@Override
	public Float transToDevice(Variant value) {
		long in = value.longValue();
		in = (long) Math.max(in, Float.MIN_VALUE);
		in = (long) Math.min(in, Float.MAX_VALUE);
		return (float) in;
	}

	@Override
	public Float transToDevice(Object value) {
		long in = ((UnsignedInteger) value).longValue();
		in = (long) Math.max(in, Float.MIN_VALUE);
		in = (long) Math.min(in, Float.MAX_VALUE);
		return (float) in;
	}

	public Object[] createInternArray(int arraylenght) {
		return new UnsignedInteger[arraylenght];
	}
}
