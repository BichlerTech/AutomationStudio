package com.bichler.opc.driver.xml_da.transform.int_;

import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.builtintypes.Variant;

import com.bichler.opc.comdrv.utils.ValueOutOfRangeException;
import com.bichler.opc.driver.xml_da.transform.XML_DA_Transformation;

/**
 * this class transforms from Integer (XML-DA) to UnsignedInteger (OPC UA) and
 * vice versa
 * 
 * @company HB-Softsolution e.U.
 * @author hannes bichler
 * @since 18.12.2013
 * @version 1.0
 * 
 */
public class XML_DA_TransformInteger2UnsignedInteger implements XML_DA_Transformation {
	@Override
	public Object transToIntern(Object value) throws ClassCastException, ValueOutOfRangeException {
		int in = (Integer) value;
		if (in > UnsignedInteger.L_MAX_VALUE || in < UnsignedInteger.L_MIN_VALUE) {
			throw new ValueOutOfRangeException(
					"Couldn't transfor XML-DA value to OPC UA Value, only values between " + UnsignedInteger.L_MIN_VALUE
							+ " and " + UnsignedInteger.L_MAX_VALUE + " are allowed, but we got, " + in + ".");
		}
		return new UnsignedInteger(in);
	}

	@Override
	public Integer transToDevice(Variant value) {
		long in = value.longValue();
		in = Math.max(in, Integer.MIN_VALUE);
		in = Math.min(in, Integer.MAX_VALUE);
		return (int) in;
	}

	@Override
	public Integer transToDevice(Object value) {
		long in = ((UnsignedInteger) value).longValue();
		in = Math.max(in, Integer.MIN_VALUE);
		in = Math.min(in, Integer.MAX_VALUE);
		return (int) in;
	}

	public Object[] createInternArray(int arraylenght) {
		return new UnsignedInteger[arraylenght];
	}
}
