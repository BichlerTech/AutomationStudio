package com.bichler.opc.driver.xml_da.transform;

import org.opcfoundation.ua.builtintypes.Variant;

import com.bichler.opc.comdrv.utils.ValueOutOfRangeException;

public interface XML_DA_Transformation {
	/**
	 * transforms a device byte array into internal opc ua data value
	 * 
	 * @param value
	 * @return
	 */
	public abstract Object transToIntern(Object value) throws ClassCastException, ValueOutOfRangeException;

	/**
	 * transforms an internal opc ua data value into device byte array
	 * 
	 * @param value
	 * @return
	 */
	public abstract Object transToDevice(Variant value);

	/**
	 * transforms an internal opc ua data value into device byte array
	 * 
	 * @param value
	 * @return
	 */
	public abstract Object transToDevice(Object value);

	public Object[] createInternArray(int arraylenght);
}
