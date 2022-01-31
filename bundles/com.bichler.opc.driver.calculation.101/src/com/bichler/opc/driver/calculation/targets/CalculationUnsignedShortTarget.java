package com.bichler.opc.driver.calculation.targets;

import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.DateTime;
import org.opcfoundation.ua.builtintypes.StatusCode;
import org.opcfoundation.ua.builtintypes.UnsignedShort;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.core.StatusCodes;

public class CalculationUnsignedShortTarget extends CalculationTarget
{
  public CalculationUnsignedShortTarget()
  {
    valueFromNode = ".byteValue()";
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
        v = new UnsignedShort[] { (UnsignedShort) v };
      dv.setValue(new Variant(v));
    }
    catch (IllegalArgumentException ex)
    {
      // we have an value out of range exception
      Object v = UnsignedShort.MIN_VALUE;
      // do we have an array, so change return value to array
      if (index >= 0 && v != null)
        v = new UnsignedShort[] { (UnsignedShort) v };
      dv.setValue(new Variant(v));
      dv.setStatusCode(new StatusCode(StatusCodes.Bad_OutOfRange));
    }
    return dv;
  }

  /**
   * change java script value into UnsignedShort representation possible java
   * scirpt values are Double, Integer, Boolean, String
   * 
   * @param val
   *          java script value
   * @return OPC UA UnsignedShort value
   */
  private UnsignedShort getValue(Object val) throws IllegalArgumentException
  {
    UnsignedShort ret = null;
    if (val == null)
    {
      return null;
    }
    else if (val instanceof Double)
    {
      Double d = (Double) val;
      if (d < UnsignedShort.MIN_VALUE.doubleValue() || d > UnsignedShort.MAX_VALUE.doubleValue())
        throw new IllegalArgumentException("Illegal value");
      ret = new UnsignedShort(d.intValue());
    }
    else if (val instanceof Integer)
    {
      Integer d = (Integer) val;
      if (d < UnsignedShort.MIN_VALUE.intValue() || d > UnsignedShort.MAX_VALUE.intValue())
        throw new IllegalArgumentException("Illegal value");
      ret = new UnsignedShort(d.intValue());
    }
    else if (val instanceof Boolean)
    {
      if ((Boolean) val)
        return new UnsignedShort(1);
      ret = new UnsignedShort(0);
    }
    else if (val instanceof String)
    {
      return null;
    }
    return ret;
  }
}
