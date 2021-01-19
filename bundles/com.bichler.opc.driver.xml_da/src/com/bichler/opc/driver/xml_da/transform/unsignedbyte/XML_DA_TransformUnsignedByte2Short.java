package com.bichler.opc.driver.xml_da.transform.unsignedbyte;

import org.opcfoundation.ua.builtintypes.UnsignedByte;
import org.opcfoundation.ua.builtintypes.Variant;

import com.bichler.opc.comdrv.utils.ValueOutOfRangeException;
import com.bichler.opc.driver.xml_da.transform.XML_DA_Transformation;

/**
 * this class transforms from UnsignedByte (XML-DA) to Short (OPC UA) and vice
 * versa
 * 
 * @company HB-Softsolution e.U.
 * @author hannes bichler
 * @since 18.12.2013
 * @version 1.0
 * 
 */
public class XML_DA_TransformUnsignedByte2Short implements XML_DA_Transformation {
	@Override
	public Object transToIntern(Object value) throws ClassCastException, ValueOutOfRangeException {
		return ((UnsignedByte) value).shortValue();
	}

	@Override
	public UnsignedByte transToDevice(Variant value) {
		return new UnsignedByte(value.shortValue());
	}

	@Override
	public UnsignedByte transToDevice(Object value) {
		return new UnsignedByte(((Short) value).shortValue());
	}

	public Object[] createInternArray(int arraylenght) {
		return new Short[arraylenght];
	}
}
