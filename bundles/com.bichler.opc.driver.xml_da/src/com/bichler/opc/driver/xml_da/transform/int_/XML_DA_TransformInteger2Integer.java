package com.bichler.opc.driver.xml_da.transform.int_;

import org.opcfoundation.ua.builtintypes.Variant;

import com.bichler.opc.comdrv.utils.ValueOutOfRangeException;
import com.bichler.opc.driver.xml_da.transform.XML_DA_Transformation;

/**
 * this class transforms from Integer (XML-DA) to Integer (OPC UA) and vice
 * versa
 * 
 * @company HB-Softsolution e.U.
 * @author hannes bichler
 * @since 18.12.2013
 * @version 1.0
 * 
 */
public class XML_DA_TransformInteger2Integer implements XML_DA_Transformation {
	@Override
	public Object transToIntern(Object value) throws ClassCastException, ValueOutOfRangeException {
		return value;
	}

	@Override
	public Integer transToDevice(Variant value) {
		return value.intValue();
	}

	@Override
	public Integer transToDevice(Object value) {
		return (Integer) value;
	}

	public Object[] createInternArray(int arraylenght) {
		return new Integer[arraylenght];
	}
}
