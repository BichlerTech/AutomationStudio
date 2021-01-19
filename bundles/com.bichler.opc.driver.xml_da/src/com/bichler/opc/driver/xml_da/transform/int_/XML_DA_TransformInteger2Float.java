package com.bichler.opc.driver.xml_da.transform.int_;

import org.opcfoundation.ua.builtintypes.Variant;

import com.bichler.opc.comdrv.utils.ValueOutOfRangeException;
import com.bichler.opc.driver.xml_da.transform.XML_DA_Transformation;

/**
 * this class transforms from Integer (XML-DA) to Float (OPC UA) and vice versa
 * 
 * @company HB-Softsolution e.U.
 * @author hannes bichler
 * @since 18.12.2013
 * @version 1.0
 * 
 */
public class XML_DA_TransformInteger2Float implements XML_DA_Transformation {
	@Override
	public Object transToIntern(Object value) throws ClassCastException, ValueOutOfRangeException {
		return ((Integer) value).floatValue();
	}

	@Override
	public Integer transToDevice(Variant value) {
		float in = value.floatValue();
		in = Math.min(in, Integer.MAX_VALUE);
		in = Math.max(in, Integer.MIN_VALUE);
		return (int) in;
	}

	@Override
	public Integer transToDevice(Object value) {
		float in = ((Float) value).floatValue();
		in = Math.min(in, Integer.MAX_VALUE);
		in = Math.max(in, Integer.MIN_VALUE);
		return (int) in;
	}

	public Object[] createInternArray(int arraylenght) {
		return new Float[arraylenght];
	}
}
