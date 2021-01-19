package com.bichler.opc.driver.xml_da.transform.unsignedshort;

import org.opcfoundation.ua.builtintypes.UnsignedShort;
import org.opcfoundation.ua.builtintypes.Variant;

import com.bichler.opc.comdrv.utils.ValueOutOfRangeException;
import com.bichler.opc.driver.xml_da.transform.XML_DA_Transformation;

/**
 * this class transforms from UnsignedShort (XML-DA) to Double (OPC UA) and vice
 * versa
 * 
 * @company HB-Softsolution e.U.
 * @author hannes bichler
 * @since 18.12.2013
 * @version 1.0
 * 
 */
public class XML_DA_TransformUnsignedShort2Double implements XML_DA_Transformation {
	@Override
	public Object transToIntern(Object value) throws ClassCastException, ValueOutOfRangeException {
		return ((UnsignedShort) value).doubleValue();
	}

	@Override
	public UnsignedShort transToDevice(Variant value) {
		double in = value.doubleValue();
		in = Math.min(in, Integer.MAX_VALUE);
		in = Math.min(in, Integer.MAX_VALUE);
		return new UnsignedShort((int) in);
	}

	@Override
	public UnsignedShort transToDevice(Object value) {
		double in = ((Double) value).doubleValue();
		in = Math.min(in, Integer.MAX_VALUE);
		in = Math.min(in, Integer.MAX_VALUE);
		return new UnsignedShort((int) in);
	}

	public Object[] createInternArray(int arraylenght) {
		return new Double[arraylenght];
	}
}
