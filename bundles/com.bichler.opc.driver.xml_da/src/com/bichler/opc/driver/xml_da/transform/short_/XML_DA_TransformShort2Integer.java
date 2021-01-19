package com.bichler.opc.driver.xml_da.transform.short_;

import org.opcfoundation.ua.builtintypes.Variant;

import com.bichler.opc.comdrv.utils.ValueOutOfRangeException;
import com.bichler.opc.driver.xml_da.transform.XML_DA_Transformation;

/**
 * this class transforms from Short (XML-DA) to Integer (OPC UA) and vice versa
 * 
 * @company HB-Softsolution e.U.
 * @author hannes bichler
 * @since 18.12.2013
 * @version 1.0
 * 
 */
public class XML_DA_TransformShort2Integer implements XML_DA_Transformation {
	@Override
	public Object transToIntern(Object value) throws ClassCastException, ValueOutOfRangeException {
		return ((Short) value).intValue();
	}

	@Override
	public Short transToDevice(Variant value) {
		int in = value.intValue();
		in = Math.min(in, Short.MAX_VALUE);
		in = Math.max(in, Short.MIN_VALUE);
		return (short) in;
	}

	@Override
	public Short transToDevice(Object value) {
		int in = ((Integer) value).intValue();
		in = Math.min(in, Short.MAX_VALUE);
		in = Math.max(in, Short.MIN_VALUE);
		return (short) in;
	}

	public Object[] createInternArray(int arraylenght) {
		return new Integer[arraylenght];
	}
}
