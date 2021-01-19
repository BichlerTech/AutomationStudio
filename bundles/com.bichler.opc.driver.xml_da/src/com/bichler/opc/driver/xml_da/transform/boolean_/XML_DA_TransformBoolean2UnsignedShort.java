package com.bichler.opc.driver.xml_da.transform.boolean_;

import org.opcfoundation.ua.builtintypes.UnsignedShort;
import org.opcfoundation.ua.builtintypes.Variant;

import com.bichler.opc.comdrv.utils.ValueOutOfRangeException;
import com.bichler.opc.driver.xml_da.transform.XML_DA_Transformation;

/**
 * this class transforms from Boolean (XML-DA) to UnsignedShort (OPC UA) and
 * vice versa
 * 
 * @company HB-Softsolution e.U.
 * @author hannes bichler
 * @since 18.12.2013
 * @version 1.0
 * 
 */
public class XML_DA_TransformBoolean2UnsignedShort implements XML_DA_Transformation {
	@Override
	public Object transToIntern(Object value) throws ClassCastException, ValueOutOfRangeException {
		if ((Boolean) value)
			return new UnsignedShort(1);
		return new UnsignedShort(0);
	}

	@Override
	public Boolean transToDevice(Variant value) {
		if (value.intValue() == 1)
			return true;
		return false;
	}

	@Override
	public Boolean transToDevice(Object value) {
		if ((Integer) value == 1)
			return true;
		return false;
	}

	public Object[] createInternArray(int arraylenght) {
		return new UnsignedShort[arraylenght];
	}
}
