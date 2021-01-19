package com.bichler.opc.driver.xml_da.transform.float_;

import org.opcfoundation.ua.builtintypes.Variant;

import com.bichler.opc.comdrv.utils.ValueOutOfRangeException;
import com.bichler.opc.driver.xml_da.transform.XML_DA_Transformation;

/**
 * this class transforms from Float (XML-DA) to Double (OPC UA) and vice versa
 * 
 * @company HB-Softsolution e.U.
 * @author hannes bichler
 * @since 18.12.2013
 * @version 1.0
 * 
 */
public class XML_DA_TransformFloat2Float implements XML_DA_Transformation {
	@Override
	public Object transToIntern(Object value) throws ClassCastException, ValueOutOfRangeException {
		return value;
	}

	@Override
	public Float transToDevice(Variant value) {
		float in = value.floatValue();
		in = Math.max(in, Float.MIN_VALUE);
		in = Math.min(in, Float.MAX_VALUE);
		return (float) in;
	}

	@Override
	public Float transToDevice(Object value) {
		float in = ((Float) value).floatValue();
		in = Math.max(in, Float.MIN_VALUE);
		in = Math.min(in, Float.MAX_VALUE);
		return (float) in;
	}

	public Object[] createInternArray(int arraylenght) {
		return new Float[arraylenght];
	}
}
