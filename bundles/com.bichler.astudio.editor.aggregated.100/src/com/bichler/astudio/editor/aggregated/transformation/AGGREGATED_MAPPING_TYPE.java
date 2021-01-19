package com.bichler.astudio.editor.aggregated.transformation;

/**
 * enumeration specifies mapping between opc ua representation and xml da
 * representation
 * 
 * @author applemc207da
 *
 */
public enum AGGREGATED_MAPPING_TYPE
{
  /**
   * OPC UA values are scalars
   */
  SCALAR,
  /**
   * OPC UA values are arrays
   */
  ARRAY,
  /**
   * Alarm bit mapping. an alarm byte, word, dword array will be mapped on a OPC
   * UA boolean array variable
   */
  ALARM_MAPPING;
}
