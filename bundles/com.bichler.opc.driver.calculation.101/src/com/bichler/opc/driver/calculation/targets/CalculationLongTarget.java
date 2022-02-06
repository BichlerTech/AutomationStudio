package com.bichler.opc.driver.calculation.targets;

import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.DateTime;
import org.opcfoundation.ua.builtintypes.StatusCode;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.core.StatusCodes;

public class CalculationLongTarget extends CalculationTarget
{
  public CalculationLongTarget()
  {
    valueFromNode = ".longValue()";
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
        v = new Long[] { (Long) v };
      dv.setValue(new Variant(v));
    }
    catch (IllegalArgumentException ex)
    {
      // we have an value out of range exception
      Object v = Long.MIN_VALUE;
      // do we have an array, so change return value to array
      if (index >= 0 && v != null)
        v = new Long[] { (Long) v };
      dv.setValue(new Variant(v));
      dv.setStatusCode(new StatusCode(StatusCodes.Bad_OutOfRange));
    }
    return dv;
  }

  /**
   * change java script value into Long representation possible java scirpt
   * values are Double, Integer, Boolean, String
   * 
   * @param val
   *          java script value
   * @return OPC UA Long value
   */
  private Long getValue(Object val) throws IllegalArgumentException
  {
    Long ret = null;
    if (val == null)
    {
      return null;
    }
    else if (val instanceof Double)
    {
      ret = ((Double) val).longValue();
    }
    else if (val instanceof Float)
    {
      ret = ((Float) val).longValue();
    }
    else if (val instanceof Long)
    {
      ret = ((Long) val).longValue();
    }
    else if (val instanceof Integer)
    {
      ret = ((Integer) val).longValue();
    }
    else if (val instanceof Short)
    {
      ret = ((Short) val).longValue();
    }
    else if (val instanceof Byte)
    {
      ret = ((Byte) val).longValue();
    }
    else if (val instanceof Boolean)
    {
      if ((Boolean) val)
        ret = 1l;
      else
        ret = 0l;
    }
    else if (val instanceof String)
    {
      return null;
    }
    return ret;
  }
}
