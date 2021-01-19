package com.bichler.opc.driver.xml_da.transform.short_;

import org.opcfoundation.ua.builtintypes.Variant;

import com.bichler.opc.comdrv.utils.ValueOutOfRangeException;
import com.bichler.opc.driver.xml_da.transform.XML_DA_Transformation;

/**
 * this class transforms from Short (XML-DA) to Long (OPC UA) and vice versa
 * 
 * @company HB-Softsolution e.U.
 * @author hannes bichler
 * @since 18.12.2013
 * @version 1.0
 * 
 */
public class XML_DA_TransformShort2Long implements XML_DA_Transformation {
	@Override
	public Object transToIntern(Object value) throws ClassCastException, ValueOutOfRangeException {
		return ((Short) value).longValue();
	}

	@Override
	public Short transToDevice(Variant value) {
		long in = value.longValue();
		in = Math.min(in, Short.MAX_VALUE);
		in = Math.max(in, Short.MIN_VALUE);
		return (short) in;
	}

	@Override
	public Short transToDevice(Object value) {
		long in = ((Long) value).longValue();
		in = Math.min(in, Short.MAX_VALUE);
		in = Math.max(in, Short.MIN_VALUE);
		return (short) in;
	}

	public Object[] createInternArray(int arraylenght) {
		return new Long[arraylenght];
	}
}
