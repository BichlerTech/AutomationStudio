package com.bichler.opc.driver.xml_da.transform.double_;

import org.opcfoundation.ua.builtintypes.UnsignedShort;
import org.opcfoundation.ua.builtintypes.Variant;

import com.bichler.opc.comdrv.utils.ValueOutOfRangeException;
import com.bichler.opc.driver.xml_da.transform.XML_DA_Transformation;

/**
 * this class transforms from Double (XML-DA) to UnsignedShort (OPC UA) and vice
 * versa
 * 
 * @company HB-Softsolution e.U.
 * @author hannes bichler
 * @since 18.12.2013
 * @version 1.0
 * 
 */
public class XML_DA_TransformDouble2UnsignedShort implements XML_DA_Transformation {
	@Override
	public Object transToIntern(Object value) throws ClassCastException, ValueOutOfRangeException {
		double in = ((Double) value).doubleValue();
		if (in > UnsignedShort.L_MAX_VALUE || in < UnsignedShort.L_MIN_VALUE) {
			throw new ValueOutOfRangeException("Couldn't transfor XML-DA value to OPC UA Value, only values between "
					+ UnsignedShort.L_MIN_VALUE + " and " + UnsignedShort.L_MAX_VALUE + " are allowed, but we got, "
					+ ((Double) value).doubleValue() + ".");
		}
		return new UnsignedShort((int) in);
	}

	@Override
	public Double transToDevice(Variant value) {
		return value.doubleValue();
	}

	@Override
	public Double transToDevice(Object value) {
		return ((UnsignedShort) value).doubleValue();
	}

	public Object[] createInternArray(int arraylenght) {
		return new UnsignedShort[arraylenght];
	}
}
