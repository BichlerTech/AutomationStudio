package com.bichler.astudio.editor.allenbradley.xml;

/**
 * enumeration specifies mapping between opc ua representation and siemens representation
 * @author applemc207da
 *
 */
public enum ALLENBRADLEY_MAPPING_TYPE {
	/**
	 * OPC UA and XML-DA values are scalars
	 */
	SCALAR//,
	/**
	 * OPC UA is a scalar XML-DA is an array
	 */
	//SCALAR_ARRAY,
	/**
	 * OPC UA is an array XML-DA is a scalar
	 */
	//ARRAY_SCALAR,
	/**
	 * OPC UA and XML-DA values are arrays
	 */
	//ARRAY_ARRAY,
	/**
	 * OPC UA Alarm values
	 */
	// ALARM;			not supported anymore
}
