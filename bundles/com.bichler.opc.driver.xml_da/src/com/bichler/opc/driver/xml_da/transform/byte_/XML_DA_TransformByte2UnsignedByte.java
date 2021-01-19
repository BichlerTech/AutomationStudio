package com.bichler.opc.driver.xml_da.transform.byte_;

import org.opcfoundation.ua.builtintypes.UnsignedByte;
import org.opcfoundation.ua.builtintypes.Variant;

import com.bichler.opc.comdrv.utils.ValueOutOfRangeException;
import com.bichler.opc.driver.xml_da.transform.XML_DA_Transformation;

/**
 * this class transforms from Byte (XML-DA) to UnsignedByte (OPC UA) and vice
 * versa
 * 
 * @company HB-Softsolution e.U.
 * @author hannes bichler
 * @since 18.12.2013
 * @version 1.0
 * 
 */
public class XML_DA_TransformByte2UnsignedByte implements XML_DA_Transformation {
	@Override
	public Object transToIntern(Object value) throws ClassCastException, ValueOutOfRangeException {
		byte in = ((Byte) value).byteValue();
		if (in < 0) {
			throw new ValueOutOfRangeException(
					"Couldn't transfor XML-DA value to OPC UA Value, no negative value allowed! Value: " + in + ".");
		}
		return new UnsignedByte(in);
	}

	@Override
	public Byte transToDevice(Variant value) {
		int d = value.shortValue();
		d = Math.min(d, Byte.MAX_VALUE);
		d = Math.max(d, Byte.MIN_VALUE);
		return (byte) d;
	}

	@Override
	public Byte transToDevice(Object value) {
		int d = ((UnsignedByte) value).shortValue();
		d = Math.min(d, Byte.MAX_VALUE);
		d = Math.max(d, Byte.MIN_VALUE);
		return (byte) d;
	}

	public Object[] createInternArray(int arraylenght) {
		return new UnsignedByte[arraylenght];
	}
}
