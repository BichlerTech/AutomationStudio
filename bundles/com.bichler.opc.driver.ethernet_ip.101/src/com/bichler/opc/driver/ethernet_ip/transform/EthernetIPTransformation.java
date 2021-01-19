package com.bichler.opc.driver.ethernet_ip.transform;

import org.opcfoundation.ua.builtintypes.Variant;

import com.bichler.opc.comdrv.utils.ValueOutOfRangeException;

import etherip.types.CIPData;

public interface EthernetIPTransformation {

	public abstract Variant getDefaultValue();

	/**
	 * transforms a device byte array into internal opc ua value as object
	 * 
	 * @param value
	 * @return
	 */
	public abstract Object transToIntern(CIPData value, int index) throws ValueOutOfRangeException;

	/**
	 * transforms an internal opc ua data value into device byte array
	 * 
	 * @param value
	 * @return
	 */
	public abstract void transToDevice(CIPData data, Object value, int index)
			throws ValueOutOfRangeException, IndexOutOfBoundsException, Exception;

	/**
	 * create value array for opc ua internal use
	 * 
	 * @param arrayLength
	 * @return
	 */
	public abstract Object[] createInternArray(int arrayLength);
	// public abstract boolean verifyValueRange(Object value);

	public abstract CIPData createCipData(int array);
}
