package com.bichler.opc.driver.xml_da.transform.byte_;

import org.opcfoundation.ua.builtintypes.Variant;

import com.bichler.opc.comdrv.utils.ValueOutOfRangeException;
import com.bichler.opc.driver.xml_da.transform.XML_DA_Transformation;

/**
 * this class transforms from Byte (XML-DA) to Byte (OPC UA) and vice versa
 * 
 * @company HB-Softsolution e.U.
 * @author hannes bichler
 * @since 18.12.2013
 * @version 1.0
 * 
 */
public class XML_DA_TransformByte2Byte implements XML_DA_Transformation {
	@Override
	public Object transToIntern(Object value) throws ClassCastException, ValueOutOfRangeException {
		return value;
	}

	@Override
	public Byte transToDevice(Variant value) {
		return value.byteValue();
	}

	@Override
	public Byte transToDevice(Object value) {
		return (Byte) value;
	}

	public Object[] createInternArray(int arraylenght) {
		return new Byte[arraylenght];
	}
}
