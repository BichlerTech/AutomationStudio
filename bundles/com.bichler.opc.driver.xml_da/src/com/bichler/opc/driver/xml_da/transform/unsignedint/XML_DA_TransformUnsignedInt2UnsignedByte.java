package com.bichler.opc.driver.xml_da.transform.unsignedint;

import org.apache.axis.types.UnsignedInt;
import org.opcfoundation.ua.builtintypes.UnsignedByte;
import org.opcfoundation.ua.builtintypes.Variant;

import com.bichler.opc.comdrv.utils.ValueOutOfRangeException;
import com.bichler.opc.driver.xml_da.transform.XML_DA_Transformation;

/**
 * this class transforms from UnsignedInt (XML-DA) to UnsignedByte (OPC UA) and
 * vice versa
 * 
 * @company HB-Softsolution e.U.
 * @author hannes bichler
 * @since 18.12.2013
 * @version 1.0
 * 
 */
public class XML_DA_TransformUnsignedInt2UnsignedByte implements XML_DA_Transformation {
	@Override
	public Object transToIntern(Object value) throws ClassCastException, ValueOutOfRangeException {
		long in = ((UnsignedInt) value).longValue();
		if (in > UnsignedByte.L_MAX_VALUE || in < UnsignedByte.L_MIN_VALUE) {
			throw new ValueOutOfRangeException(
					"Couldn't transfor XML-DA value to OPC UA Value, only values between " + UnsignedByte.L_MIN_VALUE
							+ " and " + UnsignedByte.L_MAX_VALUE + " are allowed, but we got, " + in + ".");
		}
		return new UnsignedByte(in);
	}

	@Override
	public UnsignedInt transToDevice(Variant value) {
		return new UnsignedInt(value.intValue());
	}

	@Override
	public UnsignedInt transToDevice(Object value) {
		return new UnsignedInt(((UnsignedByte) value).intValue());
	}

	public Object[] createInternArray(int arraylenght) {
		return new UnsignedByte[arraylenght];
	}
}
