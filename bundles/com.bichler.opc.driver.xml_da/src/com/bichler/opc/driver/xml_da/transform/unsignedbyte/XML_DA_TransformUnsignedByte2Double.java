package com.bichler.opc.driver.xml_da.transform.unsignedbyte;

import org.opcfoundation.ua.builtintypes.UnsignedByte;
import org.opcfoundation.ua.builtintypes.Variant;

import com.bichler.opc.comdrv.utils.ValueOutOfRangeException;
import com.bichler.opc.driver.xml_da.transform.XML_DA_Transformation;

/**
 * this class transforms from UnsignedByte (XML-DA) to Double (OPC UA) and vice
 * versa
 * 
 * @company HB-Softsolution e.U.
 * @author hannes bichler
 * @since 18.12.2013
 * @version 1.0
 * 
 */
public class XML_DA_TransformUnsignedByte2Double implements XML_DA_Transformation {
	@Override
	public Object transToIntern(Object value) throws ClassCastException, ValueOutOfRangeException {
		return ((UnsignedByte) value).doubleValue();
	}

	@Override
	public UnsignedByte transToDevice(Variant value) {
		double in = value.doubleValue();
		in = Math.min(in, Integer.MAX_VALUE);
		in = Math.min(in, Integer.MAX_VALUE);
		return new UnsignedByte((int) in);
	}

	@Override
	public UnsignedByte transToDevice(Object value) {
		double in = ((Double) value).doubleValue();
		in = Math.min(in, Integer.MAX_VALUE);
		in = Math.min(in, Integer.MAX_VALUE);
		return new UnsignedByte((int) in);
	}

	public Object[] createInternArray(int arraylenght) {
		return new Double[arraylenght];
	}
}
