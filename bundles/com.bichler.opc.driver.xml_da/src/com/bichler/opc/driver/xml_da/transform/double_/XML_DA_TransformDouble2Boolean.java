package com.bichler.opc.driver.xml_da.transform.double_;

import org.opcfoundation.ua.builtintypes.Variant;

import com.bichler.opc.comdrv.utils.ValueOutOfRangeException;
import com.bichler.opc.driver.xml_da.transform.XML_DA_Transformation;

/**
 * this class transforms from Double (XML-DA) to Boolean (OPC UA) and vice versa
 * 
 * @company HB-Softsolution e.U.
 * @author hannes bichler
 * @since 18.12.2013
 * @version 1.0
 * 
 */
public class XML_DA_TransformDouble2Boolean implements XML_DA_Transformation {
	@Override
	public Object transToIntern(Object value) throws ClassCastException, ValueOutOfRangeException {
		double in = ((Double) value).doubleValue();
		if (in == 1)
			return true;
		else if (in == 0)
			return false;
		throw new ValueOutOfRangeException(
				"Couldn't transfor XML-DA value to OPC UA Value, only 0 an 1 is allowed, but we got, " + in + ".");
	}

	@Override
	public Double transToDevice(Variant value) {
		if (value.booleanValue())
			return 1.0;
		return 0.0;
	}

	@Override
	public Double transToDevice(Object value) {
		if ((Boolean) value)
			return 1.0;
		return 0.0;
	}

	public Object[] createInternArray(int arraylenght) {
		return new Boolean[arraylenght];
	}
}
