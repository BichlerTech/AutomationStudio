package com.bichler.opc.driver.xml_da.transform.byte_;

import org.opcfoundation.ua.builtintypes.Variant;

import com.bichler.opc.comdrv.utils.ValueOutOfRangeException;
import com.bichler.opc.driver.xml_da.transform.XML_DA_Transformation;

/**
 * this class transforms from Byte (XML-DA) to Boolean (OPC UA) and vice versa
 * 
 * @company HB-Softsolution e.U.
 * @author hannes bichler
 * @since 18.12.2013
 * @version 1.0
 * 
 */
public class XML_DA_TransformByte2Boolean implements XML_DA_Transformation {
	@Override
	public Object transToIntern(Object value) throws ClassCastException, ValueOutOfRangeException {
		if ((Byte) value == 1)
			return true;
		else if ((Byte) value == 0)
			return false;
		throw new ValueOutOfRangeException(
				"Couldn't transfor XML-DA value to OPC UA Value, only 0 an 1 is allowed, but we got, "
						+ ((Byte) value).byteValue() + ".");
	}

	@Override
	public Byte transToDevice(Variant value) {
		if (value.booleanValue())
			return 1;
		return 0;
	}

	@Override
	public Byte transToDevice(Object value) {
		if ((Boolean) value)
			return 1;
		return 0;
	}

	public Object[] createInternArray(int arraylenght) {
		return new Boolean[arraylenght];
	}
}
