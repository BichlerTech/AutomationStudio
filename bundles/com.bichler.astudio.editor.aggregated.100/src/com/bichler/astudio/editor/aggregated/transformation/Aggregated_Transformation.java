package com.bichler.astudio.editor.aggregated.transformation;

import com.bichler.opc.comdrv.utils.ValueOutOfRangeException;

public interface Aggregated_Transformation
{
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
  public abstract Object transToDevice(Object value) throws ClassCastException, ValueOutOfRangeException;

  public Object[] createInternArray(int arraylength);
}
