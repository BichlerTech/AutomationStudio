package com.bichler.opc.driver.xml_da.transform.unsignedbyte;

import org.opcfoundation.ua.builtintypes.UnsignedByte;
import org.opcfoundation.ua.builtintypes.Variant;

import com.bichler.opc.comdrv.utils.ValueOutOfRangeException;
import com.bichler.opc.driver.xml_da.transform.XML_DA_Transformation;

/**
 * this class transforms from UnsignedShort (XML-DA) to UnsignedByte (OPC UA)
 * and vice versa
 * 
 * @company HB-Softsolution e.U.
 * @author hannes bichler
 * @since 18.12.2013
 * @version 1.0
 * 
 */
public class XML_DA_TransformUnsignedByte2UnsignedByte implements XML_DA_Transformation {
	@Override
	public Object transToIntern(Object value) throws ClassCastException, ValueOutOfRangeException {
		int in = ((org.apache.axis.types.UnsignedByte) value).intValue();
		return new UnsignedByte(in);
	}

	@Override
	public org.apache.axis.types.UnsignedByte transToDevice(Variant value) {
		return new org.apache.axis.types.UnsignedByte(value.intValue());
	}

	@Override
	public org.apache.axis.types.UnsignedByte transToDevice(Object value) {
		return new org.apache.axis.types.UnsignedByte(((UnsignedByte) value).intValue());
	}

	public Object[] createInternArray(int arraylenght) {
		return new UnsignedByte[arraylenght];
	}
}
