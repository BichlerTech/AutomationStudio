package com.bichler.opc.driver.xml_da.transform.unsignedbyte;

import org.opcfoundation.ua.builtintypes.UnsignedByte;
import org.opcfoundation.ua.builtintypes.Variant;

import com.bichler.opc.comdrv.utils.ValueOutOfRangeException;
import com.bichler.opc.driver.xml_da.transform.XML_DA_Transformation;

/**
 * this class transforms from UnsignedByte (XML-DA) to Boolean (OPC UA) and vice
 * versa
 * 
 * @company HB-Softsolution e.U.
 * @author hannes bichler
 * @since 18.12.2013
 * @version 1.0
 * 
 */
public class XML_DA_TransformUnsignedByte2Boolean implements XML_DA_Transformation {
	@Override
	public Object transToIntern(Object value) throws ClassCastException, ValueOutOfRangeException {
		UnsignedByte in = (UnsignedByte) value;
		if (in.intValue() == 1)
			return true;
		else if (in.intValue() == 0)
			return false;
		throw new ValueOutOfRangeException(
				"Couldn't transfor XML-DA value to OPC UA Value, only 0 an 1 is allowed, but we got, " + in.intValue()
						+ ".");
	}

	@Override
	public UnsignedByte transToDevice(Variant value) {
		boolean val = value.booleanValue();
		if (val)
			return new UnsignedByte(1);
		return new UnsignedByte(0);
	}

	@Override
	public UnsignedByte transToDevice(Object value) {
		boolean val = ((Boolean) value).booleanValue();
		if (val)
			return new UnsignedByte(1);
		return new UnsignedByte(0);
	}

	public Object[] createInternArray(int arraylenght) {
		return new Boolean[arraylenght];
	}
}
