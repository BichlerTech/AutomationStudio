package com.bichler.astudio.editor.xml_da.xml;

/**
 * enumeration specifies mapping between opc ua representation and xml da
 * representation
 * 
 * @author applemc207da
 *
 */
public enum XML_DA_MAPPING_TYPE
{
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
   * OPC UA Alarm values
   */
  ALARM;
}
