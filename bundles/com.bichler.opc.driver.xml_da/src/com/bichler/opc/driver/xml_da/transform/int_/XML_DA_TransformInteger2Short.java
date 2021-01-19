package com.bichler.opc.driver.xml_da.transform.int_;

import org.opcfoundation.ua.builtintypes.Variant;

import com.bichler.opc.comdrv.utils.ValueOutOfRangeException;
import com.bichler.opc.driver.xml_da.transform.XML_DA_Transformation;

/**
 * this class transforms from Integer (XML-DA) to Short (OPC UA) and vice versa
 * 
 * @company HB-Softsolution e.U.
 * @author hannes bichler
 * @since 18.12.2013
 * @version 1.0
 * 
 */
public class XML_DA_TransformInteger2Short implements XML_DA_Transformation {
	@Override
	public Object transToIntern(Object value) throws ClassCastException, ValueOutOfRangeException {
		int in = (Integer) value;
		if (in > Short.MAX_VALUE || in < Short.MIN_VALUE) {
			throw new ValueOutOfRangeException("Couldn't transfor XML-DA value to OPC UA Value, only values between "
					+ Short.MIN_VALUE + " and " + Short.MAX_VALUE + " are allowed, but we got, " + in + ".");
		}
		return (short) in;
	}

	@Override
	public Integer transToDevice(Variant value) {
		return value.intValue();
	}

	@Override
	public Integer transToDevice(Object value) {
		return ((Short) value).intValue();
	}

	public Object[] createInternArray(int arraylenght) {
		return new Short[arraylenght];
	}
}
