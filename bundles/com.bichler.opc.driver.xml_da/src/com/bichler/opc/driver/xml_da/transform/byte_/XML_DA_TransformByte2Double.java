package com.bichler.opc.driver.xml_da.transform.byte_;

import org.opcfoundation.ua.builtintypes.Variant;

import com.bichler.opc.comdrv.utils.ValueOutOfRangeException;
import com.bichler.opc.driver.xml_da.transform.XML_DA_Transformation;

/**
 * this class transforms from Byte (XML-DA) to Double (OPC UA) and vice versa
 * 
 * @company HB-Softsolution e.U.
 * @author hannes bichler
 * @since 18.12.2013
 * @version 1.0
 * 
 */
public class XML_DA_TransformByte2Double implements XML_DA_Transformation {
	@Override
	public Object transToIntern(Object value) throws ClassCastException, ValueOutOfRangeException {
		return ((Byte) value).doubleValue();
	}

	@Override
	public Byte transToDevice(Variant value) {
		double d = value.doubleValue();
		d = Math.min(d, Byte.MAX_VALUE);
		d = Math.max(d, Byte.MIN_VALUE);
		return (byte) d;
	}

	@Override
	public Byte transToDevice(Object value) {
		double d = ((Double) value).doubleValue();
		d = Math.min(d, Byte.MAX_VALUE);
		d = Math.max(d, Byte.MIN_VALUE);
		return (byte) d;
	}

	public Object[] createInternArray(int arraylenght) {
		return new Double[arraylenght];
	}
}
