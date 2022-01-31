package com.bichler.opc.driver.calculation.targets;

import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.DateTime;
import org.opcfoundation.ua.builtintypes.StatusCode;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.core.StatusCodes;

public class CalculationUnsignedIntTarget extends CalculationTarget
{
  public CalculationUnsignedIntTarget()
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
        v = new UnsignedInteger[] { (UnsignedInteger) v };
      dv.setValue(new Variant(v));
    }
    catch (IllegalArgumentException ex)
    {
      // we have an value out of range exception
      Object v = UnsignedInteger.MIN_VALUE;
      // do we have an array, so change return value to array
      if (index >= 0 && v != null)
        v = new UnsignedInteger[] { (UnsignedInteger) v };
      dv.setValue(new Variant(v));
      dv.setStatusCode(new StatusCode(StatusCodes.Bad_OutOfRange));
    }
    return dv;
  }

  /**
   * change java script value into UnsignedInteger representation possible java
   * scirpt values are Double, Integer, Boolean, String
   * 
   * @param val
   *          java script value
   * @return OPC UA UnsignedInteger value
   */
  private UnsignedInteger getValue(Object val) throws IllegalArgumentException
  {
    UnsignedInteger ret = null;
    if (val == null)
    {
      return null;
    }
    else if (val instanceof Double)
    {
      Double d = (Double) val;
      if (d < UnsignedInteger.MIN_VALUE.doubleValue() || d > UnsignedInteger.MAX_VALUE.doubleValue())
        throw new IllegalArgumentException("Illegal value");
      ret = new UnsignedInteger(d.longValue());
    }
    else if (val instanceof Integer)
    {
      Integer d = (Integer) val;
      if (d < UnsignedInteger.MIN_VALUE.intValue() || d > UnsignedInteger.MAX_VALUE.intValue())
        throw new IllegalArgumentException("Illegal value");
      ret = new UnsignedInteger(d.longValue());
    }
    else if (val instanceof Boolean)
    {
      if ((Boolean) val)
        ret = new UnsignedInteger(1);
      else
        ret = new UnsignedInteger(0);
    }
    else if (val instanceof String)
    {
      return null;
    }
    return ret;
  }
}
