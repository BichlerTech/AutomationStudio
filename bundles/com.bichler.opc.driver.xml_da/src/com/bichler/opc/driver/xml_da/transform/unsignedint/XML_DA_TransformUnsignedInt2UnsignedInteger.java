package com.bichler.opc.driver.xml_da.transform.unsignedint;

import org.apache.axis.types.UnsignedInt;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.builtintypes.Variant;

import com.bichler.opc.comdrv.utils.ValueOutOfRangeException;
import com.bichler.opc.driver.xml_da.transform.XML_DA_Transformation;

/**
 * this class transforms from UnsignedInt (XML-DA) to UnsignedInteger (OPC UA)
 * and vice versa
 * 
 * @company HB-Softsolution e.U.
 * @author hannes bichler
 * @since 18.12.2013
 * @version 1.0
 * 
 */
public class XML_DA_TransformUnsignedInt2UnsignedInteger implements XML_DA_Transformation {
	@Override
	public Object transToIntern(Object value) throws ClassCastException, ValueOutOfRangeException {
		return new UnsignedInteger(((UnsignedInt) value).longValue());
	}

	@Override
	public UnsignedInt transToDevice(Variant value) {
		return new UnsignedInt(value.longValue());
	}

	@Override
	public UnsignedInt transToDevice(Object value) {
		return new UnsignedInt(((UnsignedInteger) value).longValue());
	}

	public Object[] createInternArray(int arraylenght) {
		return new UnsignedInteger[arraylenght];
	}
}
