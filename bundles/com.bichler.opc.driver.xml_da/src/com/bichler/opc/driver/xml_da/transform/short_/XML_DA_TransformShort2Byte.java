package com.bichler.opc.driver.xml_da.transform.short_;

import org.opcfoundation.ua.builtintypes.Variant;

import com.bichler.opc.comdrv.utils.ValueOutOfRangeException;
import com.bichler.opc.driver.xml_da.transform.XML_DA_Transformation;

/**
 * this class transforms from Short (XML-DA) to Byte (OPC UA) and vice versa
 * 
 * @company HB-Softsolution e.U.
 * @author hannes bichler
 * @since 18.12.2013
 * @version 1.0
 * 
 */
public class XML_DA_TransformShort2Byte implements XML_DA_Transformation {
	@Override
	public Object transToIntern(Object value) throws ClassCastException, ValueOutOfRangeException {
		short in = (Short) value;
		if (in > Byte.MAX_VALUE || in < Byte.MIN_VALUE) {
			throw new ValueOutOfRangeException("Couldn't transfor XML-DA value to OPC UA Value, only values between "
					+ Byte.MIN_VALUE + " and " + Byte.MAX_VALUE + " are allowed, but we got, " + in + ".");
		}
		return (byte) in;
	}

	@Override
	public Short transToDevice(Variant value) {
		return value.shortValue();
	}

	@Override
	public Short transToDevice(Object value) {
		return ((Byte) value).shortValue();
	}

	public Object[] createInternArray(int arraylenght) {
		return new Byte[arraylenght];
	}
}
