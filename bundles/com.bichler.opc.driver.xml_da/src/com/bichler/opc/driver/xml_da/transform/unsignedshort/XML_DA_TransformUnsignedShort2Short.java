package com.bichler.opc.driver.xml_da.transform.unsignedshort;

import org.opcfoundation.ua.builtintypes.UnsignedShort;
import org.opcfoundation.ua.builtintypes.Variant;

import com.bichler.opc.comdrv.utils.ValueOutOfRangeException;
import com.bichler.opc.driver.xml_da.transform.XML_DA_Transformation;

/**
 * this class transforms from UnsignedShort (XML-DA) to Short (OPC UA) and vice
 * versa
 * 
 * @company HB-Softsolution e.U.
 * @author hannes bichler
 * @since 18.12.2013
 * @version 1.0
 * 
 */
public class XML_DA_TransformUnsignedShort2Short implements XML_DA_Transformation {
	@Override
	public Object transToIntern(Object value) throws ClassCastException, ValueOutOfRangeException {
		int in = ((UnsignedShort) value).intValue();
		if (in > Short.MAX_VALUE || in < Short.MIN_VALUE) {
			throw new ValueOutOfRangeException("Couldn't transfor XML-DA value to OPC UA Value, only values between "
					+ Short.MIN_VALUE + " and " + Short.MAX_VALUE + " are allowed, but we got, " + in + ".");
		}
		return (short) in;
	}

	@Override
	public UnsignedShort transToDevice(Variant value) {
		return new UnsignedShort(value.shortValue());
	}

	@Override
	public UnsignedShort transToDevice(Object value) {
		return new UnsignedShort(((Short) value).shortValue());
	}

	public Object[] createInternArray(int arraylenght) {
		return new Short[arraylenght];
	}
}
