package com.bichler.opc.driver.xml_da.transform.unsignedint;

import org.apache.axis.types.UnsignedInt;
import org.opcfoundation.ua.builtintypes.Variant;

import com.bichler.opc.comdrv.utils.ValueOutOfRangeException;
import com.bichler.opc.driver.xml_da.transform.XML_DA_Transformation;

/**
 * this class transforms from UnsignedInt (XML-DA) to Boolean (OPC UA) and vice
 * versa
 * 
 * @company HB-Softsolution e.U.
 * @author hannes bichler
 * @since 18.12.2013
 * @version 1.0
 * 
 */
public class XML_DA_TransformUnsignedInt2Boolean implements XML_DA_Transformation {
	@Override
	public Object transToIntern(Object value) throws ClassCastException, ValueOutOfRangeException {
		long in = ((UnsignedInt) value).longValue();
		if (in == 1)
			return true;
		else if (in == 0)
			return false;
		throw new ValueOutOfRangeException(
				"Couldn't transfor XML-DA value to OPC UA Value, only 0 an 1 is allowed, but we got, " + in + ".");
	}

	@Override
	public UnsignedInt transToDevice(Variant value) {
		boolean val = value.booleanValue();
		if (val)
			return new UnsignedInt(1);
		return new UnsignedInt(0);
	}

	@Override
	public UnsignedInt transToDevice(Object value) {
		boolean val = ((Boolean) value).booleanValue();
		if (val)
			return new UnsignedInt(1);
		return new UnsignedInt(0);
	}

	public Object[] createInternArray(int arraylenght) {
		return new Boolean[arraylenght];
	}
}
