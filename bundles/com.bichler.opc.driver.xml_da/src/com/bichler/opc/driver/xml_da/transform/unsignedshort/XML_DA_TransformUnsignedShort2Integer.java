package com.bichler.opc.driver.xml_da.transform.unsignedshort;

import org.opcfoundation.ua.builtintypes.UnsignedShort;
import org.opcfoundation.ua.builtintypes.Variant;

import com.bichler.opc.comdrv.utils.ValueOutOfRangeException;
import com.bichler.opc.driver.xml_da.transform.XML_DA_Transformation;

/**
 * this class transforms from UnsignedShort (XML-DA) to Integer (OPC UA) and
 * vice versa
 * 
 * @company HB-Softsolution e.U.
 * @author hannes bichler
 * @since 18.12.2013
 * @version 1.0
 * 
 */
public class XML_DA_TransformUnsignedShort2Integer implements XML_DA_Transformation {
	@Override
	public Object transToIntern(Object value) throws ClassCastException, ValueOutOfRangeException {
		return ((UnsignedShort) value).intValue();
	}

	@Override
	public UnsignedShort transToDevice(Variant value) {
		return new UnsignedShort(value.intValue());
	}

	@Override
	public UnsignedShort transToDevice(Object value) {
		return new UnsignedShort(((Integer) value).intValue());
	}

	public Object[] createInternArray(int arraylenght) {
		return new Integer[arraylenght];
	}
}
