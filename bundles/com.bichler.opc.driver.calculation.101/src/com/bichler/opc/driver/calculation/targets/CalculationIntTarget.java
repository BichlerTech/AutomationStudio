package com.bichler.opc.driver.calculation.targets;

import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.DateTime;
import org.opcfoundation.ua.builtintypes.StatusCode;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.core.StatusCodes;

public class CalculationIntTarget extends CalculationTarget
{
  public CalculationIntTarget()
  {
    valueFromNode = ".intValue()";
  }

  @Override
  public DataValue getTargetValue(Object val, int index)
  {
    DataValue dv = new DataValue();
    dv.setSourceTimestamp(new DateTime());
    dv.setStatusCode(StatusCode.GOOD);
    try
    {
      Object v = getValue(val);
      // do we have an array, so change return value to array
      if (index >= 0 && v != null)
        v = new Integer[] { (Integer) v };
      dv.setValue(new Variant(v));
    }
    catch (IllegalArgumentException ex)
    {
      // we have an value out of range exception
      Object v = Integer.MIN_VALUE;
      // do we have an array, so change return value to array
      if (index >= 0 && v != null)
        v = new Integer[] { (Integer) v };
      dv.setValue(new Variant(v));
      dv.setStatusCode(new StatusCode(StatusCodes.Bad_OutOfRange));
    }
    return dv;
  }

  /**
   * change java script value into Integer representation possible java scirpt
   * values are Double, Integer, Boolean, String
   * 
   * @param val
   *          java script value
   * @return OPC UA Integer value
   */
  private Integer getValue(Object val) throws IllegalArgumentException
  {
    Integer ret = null;
    if (val == null)
    {
      return null;
    }
    else if (val instanceof Double)
    {
      Double d = (Double) val;
      if (d < Integer.MIN_VALUE || d > Integer.MAX_VALUE)
        throw new IllegalArgumentException("Illegal value");
      ret = ((Double) val).intValue();
    }
    else if (val instanceof Integer)
    {
      ret = ((Integer) val).intValue();
    }
    else if (val instanceof Boolean)
    {
      if ((Boolean) val)
        ret = 1;
      else
        ret = 0;
    }
    else if (val instanceof String)
    {
      return null;
    }
    return ret;
  }
}
