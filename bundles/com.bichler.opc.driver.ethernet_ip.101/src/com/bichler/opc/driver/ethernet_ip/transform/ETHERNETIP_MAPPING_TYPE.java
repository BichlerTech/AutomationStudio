package com.bichler.opc.driver.ethernet_ip.transform;

/**
 * enumeration specifies mapping between opc ua representation and siemens
 * representation
 * 
 * @author applemc207da
 *
 */
public enum ETHERNETIP_MAPPING_TYPE {
	/**
	 * OPC UA and XML-DA values are scalars
	 */
	SCALAR,
	/**
	 * OPC UA is a scalar XML-DA is an array
	 */
	SCALAR_ARRAY,
	/**
	 * OPC UA is an array XML-DA is a scalar
	 */
	ARRAY_SCALAR,
	/**
	 * OPC UA and XML-DA values are arrays
	 */
	ARRAY_ARRAY,
	/**
	 * Alarm bit mapping. an alarm byte, word, dword arry will be mapped on a OPC UA
	 * boolean array variable
	 */
	ALARM;
}
