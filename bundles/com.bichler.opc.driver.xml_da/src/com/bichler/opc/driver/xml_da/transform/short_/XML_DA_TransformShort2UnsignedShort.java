package com.bichler.opc.driver.xml_da.transform.short_;

import org.opcfoundation.ua.builtintypes.UnsignedShort;
import org.opcfoundation.ua.builtintypes.Variant;

import com.bichler.opc.comdrv.utils.ValueOutOfRangeException;
import com.bichler.opc.driver.xml_da.transform.XML_DA_Transformation;

/**
 * this class transforms from Short (XML-DA) to UnsignedShort (OPC UA) and vice
 * versa
 * 
 * @company HB-Softsolution e.U.
 * @author hannes bichler
 * @since 18.12.2013
 * @version 1.0
 * 
 */
public class XML_DA_TransformShort2UnsignedShort implements XML_DA_Transformation {
	@Override
	public Object transToIntern(Object value) throws ClassCastException, ValueOutOfRangeException {
		short in = ((Short) value).shortValue();
		if (in > UnsignedShort.L_MAX_VALUE || in < UnsignedShort.L_MIN_VALUE) {
			throw new ValueOutOfRangeException(
					"Couldn't transfor XML-DA value to OPC UA Value, only values between " + UnsignedShort.L_MIN_VALUE
							+ " and " + UnsignedShort.L_MAX_VALUE + " are allowed, but we got, " + in + ".");
		}
		return new UnsignedShort(in);
	}

	@Override
	public Short transToDevice(Variant value) {
		int in = value.intValue();
		in = Math.min(in, Short.MAX_VALUE);
		in = Math.max(in, Short.MIN_VALUE);
		return (short) in;
	}

	@Override
	public Short transToDevice(Object value) {
		int in = ((UnsignedShort) value).intValue();
		in = Math.min(in, Short.MAX_VALUE);
		in = Math.max(in, Short.MIN_VALUE);
		return (short) in;
	}

	public Object[] createInternArray(int arraylenght) {
		return new UnsignedShort[arraylenght];
	}
}
