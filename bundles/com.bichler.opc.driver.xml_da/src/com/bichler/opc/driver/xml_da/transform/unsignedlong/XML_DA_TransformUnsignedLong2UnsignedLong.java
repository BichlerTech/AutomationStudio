package com.bichler.opc.driver.xml_da.transform.unsignedlong;

import org.opcfoundation.ua.builtintypes.UnsignedLong;
import org.opcfoundation.ua.builtintypes.Variant;

import com.bichler.opc.comdrv.utils.ValueOutOfRangeException;
import com.bichler.opc.driver.xml_da.transform.XML_DA_Transformation;

/**
 * this class transforms from UnsignedLong (XML-DA) to UnsignedLong (OPC UA) and
 * vice versa
 * 
 * @company HB-Softsolution e.U.
 * @author hannes bichler
 * @since 18.12.2013
 * @version 1.0
 * 
 */
public class XML_DA_TransformUnsignedLong2UnsignedLong implements XML_DA_Transformation {
	@Override
	public Object transToIntern(Object value) throws ClassCastException, ValueOutOfRangeException {
		return new org.opcfoundation.ua.builtintypes.UnsignedLong(((UnsignedLong) value).longValue());
	}

	@Override
	public UnsignedLong transToDevice(Variant value) {
		long val = value.longValue();
		return new UnsignedLong(val);
	}

	@Override
	public UnsignedLong transToDevice(Object value) {
		long val = ((org.opcfoundation.ua.builtintypes.UnsignedLong) value).longValue();
		return new UnsignedLong(val);
	}

	public Object[] createInternArray(int arraylenght) {
		return new org.opcfoundation.ua.builtintypes.UnsignedLong[arraylenght];
	}
}
