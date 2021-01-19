package com.bichler.opc.driver.xml_da.transform.unsignedlong;

import org.opcfoundation.ua.builtintypes.UnsignedLong;
import org.opcfoundation.ua.builtintypes.Variant;

import com.bichler.opc.comdrv.utils.ValueOutOfRangeException;
import com.bichler.opc.driver.xml_da.transform.XML_DA_Transformation;

/**
 * this class transforms from UnsignedLong (XML-DA) to Double (OPC UA) and vice
 * versa
 * 
 * @company HB-Softsolution e.U.
 * @author hannes bichler
 * @since 18.12.2013
 * @version 1.0
 * 
 */
public class XML_DA_TransformUnsignedLong2Double implements XML_DA_Transformation {
	@Override
	public Object transToIntern(Object value) throws ClassCastException, ValueOutOfRangeException {
		return ((UnsignedLong) value).doubleValue();
	}

	@Override
	public UnsignedLong transToDevice(Variant value) {
		return new UnsignedLong(value.longValue());
	}

	@Override
	public UnsignedLong transToDevice(Object value) {
		return new UnsignedLong(((Double) value).longValue());
	}

	public Object[] createInternArray(int arraylenght) {
		return new Double[arraylenght];
	}
}
