package com.bichler.opc.driver.xml_da.transform.unsignedbyte;

import org.opcfoundation.ua.builtintypes.UnsignedByte;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.builtintypes.Variant;

import com.bichler.opc.comdrv.utils.ValueOutOfRangeException;
import com.bichler.opc.driver.xml_da.transform.XML_DA_Transformation;

/**
 * this class transforms from UnsignedByte (XML-DA) to UnsignedInteger (OPC UA)
 * and vice versa
 * 
 * @company HB-Softsolution e.U.
 * @author hannes bichler
 * @since 18.12.2013
 * @version 1.0
 * 
 */
public class XML_DA_TransformUnsignedByte2UnsignedInteger implements XML_DA_Transformation {
	@Override
	public Object transToIntern(Object value) throws ClassCastException, ValueOutOfRangeException {
		return new UnsignedInteger(((UnsignedByte) value).intValue());
	}

	@Override
	public UnsignedByte transToDevice(Variant value) {
		long in = value.longValue();
		in = Math.min(in, Integer.MAX_VALUE);
		in = Math.max(in, Integer.MIN_VALUE);
		return new UnsignedByte(in);
	}

	@Override
	public UnsignedByte transToDevice(Object value) {
		long in = ((UnsignedInteger) value).longValue();
		in = Math.min(in, Integer.MAX_VALUE);
		in = Math.max(in, Integer.MIN_VALUE);
		return new UnsignedByte(in);
	}

	public Object[] createInternArray(int arraylenght) {
		return new UnsignedInteger[arraylenght];
	}
}
