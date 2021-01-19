package com.bichler.opc.driver.xml_da.transform.float_;

import org.opcfoundation.ua.builtintypes.Variant;

import com.bichler.opc.comdrv.utils.ValueOutOfRangeException;
import com.bichler.opc.driver.xml_da.transform.XML_DA_Transformation;

/**
 * this class transforms from Float (XML-DA) to Integer (OPC UA) and vice versa
 * 
 * @company HB-Softsolution e.U.
 * @author hannes bichler
 * @since 18.12.2013
 * @version 1.0
 * 
 */
public class XML_DA_TransformFloat2Long implements XML_DA_Transformation {
	// TODO check value range
	@Override
	public Object transToIntern(Object value) throws ClassCastException, ValueOutOfRangeException {
		return ((Long) value).longValue();
	}

	@Override
	public Float transToDevice(Variant value) {
		return (float) value.longValue();
	}

	@Override
	public Float transToDevice(Object value) {
		return ((Long) value).floatValue();
	}

	public Object[] createInternArray(int arraylenght) {
		return new Long[arraylenght];
	}
}
