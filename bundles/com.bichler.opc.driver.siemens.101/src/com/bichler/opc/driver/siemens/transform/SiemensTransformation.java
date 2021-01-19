package com.bichler.opc.driver.siemens.transform;

import com.bichler.opc.comdrv.utils.ComByteMessage;
import com.bichler.opc.comdrv.utils.ValueOutOfRangeException;

public interface SiemensTransformation {
	/**
	 * transforms a device byte array into internal opc ua value as object
	 * 
	 * @param value
	 * @return
	 */
	public abstract Object transToIntern(ComByteMessage value) throws ValueOutOfRangeException;

	/**
	 * transforms an internal opc ua data value into device byte array
	 * 
	 * @param value
	 * @return
	 */
	public abstract byte[] transToDevice(Object value) throws ValueOutOfRangeException;

	/**
	 * create value array for opc ua internal use
	 * 
	 * @param arrayLength
	 * @return
	 */
	public abstract Object[] createInternArray(int arrayLength);
	// public abstract boolean verifyValueRange(Object value);
}
