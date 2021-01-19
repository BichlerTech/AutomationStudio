package com.bichler.opc.driver.xml_da.transform.unsignedshort;

import org.opcfoundation.ua.builtintypes.UnsignedLong;
import org.opcfoundation.ua.builtintypes.UnsignedShort;
import org.opcfoundation.ua.builtintypes.Variant;

import com.bichler.opc.comdrv.utils.ValueOutOfRangeException;
import com.bichler.opc.driver.xml_da.transform.XML_DA_Transformation;

/**
 * this class transforms from UnsignedShort (XML-DA) to UnsignedLong (OPC UA)
 * and vice versa
 * 
 * @company HB-Softsolution e.U.
 * @author hannes bichler
 * @since 18.12.2013
 * @version 1.0
 * 
 */
public class XML_DA_TransformUnsignedShort2UnsignedLong implements XML_DA_Transformation {
	@Override
	public Object transToIntern(Object value) throws ClassCastException, ValueOutOfRangeException {
		return new UnsignedLong(((UnsignedShort) value).intValue());
	}

	@Override
	public UnsignedShort transToDevice(Variant value) {
		long in = value.longValue();
		in = Math.min(in, Integer.MAX_VALUE);
		in = Math.max(in, Integer.MIN_VALUE);
		return new UnsignedShort(in);
	}

	@Override
	public UnsignedShort transToDevice(Object value) {
		long in = ((UnsignedLong) value).longValue();
		in = Math.min(in, Integer.MAX_VALUE);
		in = Math.max(in, Integer.MIN_VALUE);
		return new UnsignedShort(in);
	}

	public Object[] createInternArray(int arraylenght) {
		return new UnsignedLong[arraylenght];
	}
}
