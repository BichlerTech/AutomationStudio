package com.bichler.opc.driver.xml_da.transform.boolean_;

import org.opcfoundation.ua.builtintypes.Variant;

import com.bichler.opc.comdrv.utils.ValueOutOfRangeException;
import com.bichler.opc.driver.xml_da.transform.XML_DA_Transformation;

/**
 * this class transforms from Boolean (XML-DA) to Long (OPC UA) and vice versa
 * 
 * @company HB-Softsolution e.U.
 * @author hannes bichler
 * @since 18.12.2013
 * @version 1.0
 * 
 */
public class XML_DA_TransformBoolean2Long implements XML_DA_Transformation {
	@Override
	public Object transToIntern(Object value) throws ClassCastException, ValueOutOfRangeException {
		if ((Boolean) value)
			return 1;
		return 0;
	}

	@Override
	public Boolean transToDevice(Variant value) {
		if (value.longValue() == 1)
			return true;
		return false;
	}

	@Override
	public Object transToDevice(Object value) {
		if ((Long) value == 1)
			return true;
		return false;
	}

	public Object[] createInternArray(int arraylenght) {
		return new Long[arraylenght];
	}
}