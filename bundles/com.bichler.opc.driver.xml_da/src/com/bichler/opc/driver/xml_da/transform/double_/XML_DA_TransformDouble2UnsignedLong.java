package com.bichler.opc.driver.xml_da.transform.double_;

import org.opcfoundation.ua.builtintypes.UnsignedLong;
import org.opcfoundation.ua.builtintypes.Variant;

import com.bichler.opc.comdrv.utils.ValueOutOfRangeException;
import com.bichler.opc.driver.xml_da.transform.XML_DA_Transformation;

/**
 * this class transforms from Double (XML-DA) to UnsignedLong (OPC UA) and vice
 * versa
 * 
 * @company HB-Softsolution e.U.
 * @author hannes bichler
 * @since 18.12.2013
 * @version 1.0
 * 
 */
public class XML_DA_TransformDouble2UnsignedLong implements XML_DA_Transformation {
	@Override
	public Object transToIntern(Object value) throws ClassCastException, ValueOutOfRangeException {
		double in = ((Double) value).doubleValue();
		if (in < 0) {
			throw new ValueOutOfRangeException(
					"Couldn't transfor XML-DA value to OPC UA Value, no negative value allowed! Value: " + in + ".");
		}
		return new UnsignedLong((long) in);
	}

	@Override
	public Double transToDevice(Variant value) {
		return value.doubleValue();
	}

	@Override
	public Double transToDevice(Object value) {
		return ((UnsignedLong) value).doubleValue();
	}

	public Object[] createInternArray(int arraylenght) {
		return new UnsignedLong[arraylenght];
	}
}
