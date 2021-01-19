package com.bichler.opc.driver.xml_da.transform.long_;

import org.opcfoundation.ua.builtintypes.Variant;

import com.bichler.opc.comdrv.utils.ValueOutOfRangeException;
import com.bichler.opc.driver.xml_da.transform.XML_DA_Transformation;

/**
 * this class transforms from Long (XML-DA) to Boolean (OPC UA) and vice versa
 * 
 * @company HB-Softsolution e.U.
 * @author hannes bichler
 * @since 18.12.2013
 * @version 1.0
 * 
 */
public class XML_DA_TransformLong2Boolean implements XML_DA_Transformation {
	@Override
	public Object transToIntern(Object value) throws ClassCastException, ValueOutOfRangeException {
		long in = (Long) value;
		if (in == 1)
			return true;
		else if (in == 0)
			return false;
		throw new ValueOutOfRangeException(
				"Couldn't transfor XML-DA value to OPC UA Value, only 0 an 1 is allowed, but we got, " + in + ".");
	}

	@Override
	public Long transToDevice(Variant value) {
		boolean val = value.booleanValue();
		if (val)
			return 1L;
		return 0L;
	}

	@Override
	public Long transToDevice(Object value) {
		boolean val = ((Boolean) value).booleanValue();
		if (val)
			return 1L;
		return 0L;
	}

	public Object[] createInternArray(int arraylenght) {
		return new Boolean[arraylenght];
	}
}
