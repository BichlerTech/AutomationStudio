package com.bichler.opc.driver.xml_da.transform.string_;

import org.opcfoundation.ua.builtintypes.Variant;

import com.bichler.opc.comdrv.utils.ValueOutOfRangeException;
import com.bichler.opc.driver.xml_da.transform.XML_DA_Transformation;

/**
 * this class transforms from Short (XML-DA) to Boolean (OPC UA) and vice versa
 * 
 * @company HB-Softsolution e.U.
 * @author hannes bichler
 * @since 18.12.2013
 * @version 1.0
 * 
 */
public class XML_DA_TransformString2String implements XML_DA_Transformation {
	@Override
	public Object transToIntern(Object value) throws ClassCastException, ValueOutOfRangeException {
		return value;
	}

	@Override
	public String transToDevice(Variant value) {
		return value.getValue().toString();
	}

	@Override
	public String transToDevice(Object value) {
		return ((String) value);
	}

	public Object[] createInternArray(int arraylenght) {
		return new String[arraylenght];
	}
}
