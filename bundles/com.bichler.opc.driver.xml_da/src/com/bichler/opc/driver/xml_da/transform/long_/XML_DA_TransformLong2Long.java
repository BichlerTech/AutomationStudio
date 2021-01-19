package com.bichler.opc.driver.xml_da.transform.long_;

import org.opcfoundation.ua.builtintypes.Variant;

import com.bichler.opc.comdrv.utils.ValueOutOfRangeException;
import com.bichler.opc.driver.xml_da.transform.XML_DA_Transformation;

/**
 * this class transforms from Long (XML-DA) to Integer (OPC UA) and vice versa
 * 
 * @company HB-Softsolution e.U.
 * @author hannes bichler
 * @since 18.12.2013
 * @version 1.0
 * 
 */
public class XML_DA_TransformLong2Long implements XML_DA_Transformation {
	@Override
	public Object transToIntern(Object value) throws ClassCastException, ValueOutOfRangeException {
		return value;
	}

	@Override
	public Long transToDevice(Variant value) {
		return value.longValue();
	}

	@Override
	public Long transToDevice(Object value) {
		return ((Long) value).longValue();
	}

	public Object[] createInternArray(int arraylenght) {
		return new Long[arraylenght];
	}
}
