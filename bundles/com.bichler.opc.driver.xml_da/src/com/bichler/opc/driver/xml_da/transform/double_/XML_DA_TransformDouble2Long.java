package com.bichler.opc.driver.xml_da.transform.double_;

import org.opcfoundation.ua.builtintypes.Variant;

import com.bichler.opc.comdrv.utils.ValueOutOfRangeException;
import com.bichler.opc.driver.xml_da.transform.XML_DA_Transformation;

/**
 * this class transforms from Double (XML-DA) to Double (OPC UA) and vice versa
 * 
 * @company HB-Softsolution e.U.
 * @author hannes bichler
 * @since 18.12.2013
 * @version 1.0
 * 
 */
public class XML_DA_TransformDouble2Long implements XML_DA_Transformation {
	@Override
	public Object transToIntern(Object value) throws ClassCastException, ValueOutOfRangeException {
		return ((Double) value).longValue();
	}

	@Override
	public Double transToDevice(Variant value) {
		return value.doubleValue();
	}

	@Override
	public Double transToDevice(Object value) {
		return ((Long) value).doubleValue();
	}

	public Object[] createInternArray(int arraylenght) {
		return new Long[arraylenght];
	}
}
