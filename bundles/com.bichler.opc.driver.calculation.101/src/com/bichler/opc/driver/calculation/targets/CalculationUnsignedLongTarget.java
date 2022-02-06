package com.bichler.opc.driver.calculation.targets;

import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.DateTime;
import org.opcfoundation.ua.builtintypes.StatusCode;
import org.opcfoundation.ua.builtintypes.UnsignedLong;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.core.StatusCodes;

public class CalculationUnsignedLongTarget extends CalculationTarget
{
  public CalculationUnsignedLongTarget()
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
        v = new UnsignedLong[] { (UnsignedLong) v };
      dv.setValue(new Variant(v));
    }
    catch (IllegalArgumentException ex)
    {
      // we have an value out of range exception
      Object v = UnsignedLong.MIN_VALUE;
      // do we have an array, so change return value to array
      if (index >= 0 && v != null)
        v = new UnsignedLong[] { (UnsignedLong) v };
      dv.setValue(new Variant(v));
      dv.setStatusCode(new StatusCode(StatusCodes.Bad_OutOfRange));
    }
    return dv;
  }

  /**
   * change java script value into UnsignedLong representation possible java
   * scirpt values are Double, Integer, Boolean, String
   * 
   * @param val
   *          java script value
   * @return OPC UA UnsignedLong value
   */
  private UnsignedLong getValue(Object val) throws IllegalArgumentException
  {
    UnsignedLong ret = null;
    if (val == null)
    {
      return null;
    }
    else if (val instanceof Double)
    {
      ret = new UnsignedLong(((Double) val).longValue());
    }
    else if (val instanceof Float)
    {
      ret = new UnsignedLong(((Float) val).longValue());
    }
    else if (val instanceof Long)
    {
      ret = new UnsignedLong(((Long) val).longValue());
    }
    else if (val instanceof Integer)
    {
      ret = new UnsignedLong(((Integer) val).intValue());
    }
    else if (val instanceof Short)
    {
      ret = new UnsignedLong(((Short) val).longValue());
    }
    else if (val instanceof Byte)
    {
      ret = new UnsignedLong(((Byte) val).longValue());
    }
    else if (val instanceof Boolean)
    {
      if ((Boolean) val)
        ret = new UnsignedLong(1);
      else
        ret = new UnsignedLong(0);
    }
    else if (val instanceof String)
    {
      return null;
    }
    return ret;
  }
}
