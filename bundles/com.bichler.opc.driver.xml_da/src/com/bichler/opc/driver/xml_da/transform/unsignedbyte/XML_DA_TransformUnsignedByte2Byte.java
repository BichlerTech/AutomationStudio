package com.bichler.opc.driver.xml_da.transform.unsignedbyte;

import org.opcfoundation.ua.builtintypes.UnsignedByte;
import org.opcfoundation.ua.builtintypes.Variant;

import com.bichler.opc.comdrv.utils.ValueOutOfRangeException;
import com.bichler.opc.driver.xml_da.transform.XML_DA_Transformation;

/**
 * this class transforms from UnsignedByte (XML-DA) to Byte (OPC UA) and vice
 * versa
 * 
 * @company HB-Softsolution e.U.
 * @author hannes bichler
 * @since 18.12.2013
 * @version 1.0
 * 
 */
public class XML_DA_TransformUnsignedByte2Byte implements XML_DA_Transformation {
	@Override
	public Object transToIntern(Object value) throws ClassCastException, ValueOutOfRangeException {
		int in = ((UnsignedByte) value).intValue();
		if (in > Byte.MAX_VALUE || in < Byte.MIN_VALUE) {
			throw new ValueOutOfRangeException("Couldn't transfor XML-DA value to OPC UA Value, only values between "
					+ Byte.MIN_VALUE + " and " + Byte.MAX_VALUE + " are allowed, but we got, " + in + ".");
		}
		return (byte) in;
	}

	@Override
	public UnsignedByte transToDevice(Variant value) {
		return new UnsignedByte(value.byteValue());
	}

	@Override
	public UnsignedByte transToDevice(Object value) {
		return new UnsignedByte(((Byte) value).byteValue());
	}

	public Object[] createInternArray(int arraylenght) {
		return new Byte[arraylenght];
	}
}
