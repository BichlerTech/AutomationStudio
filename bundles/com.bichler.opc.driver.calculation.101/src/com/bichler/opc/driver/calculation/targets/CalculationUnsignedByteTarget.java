package com.bichler.opc.driver.calculation.targets;

import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.DateTime;
import org.opcfoundation.ua.builtintypes.StatusCode;
import org.opcfoundation.ua.builtintypes.UnsignedByte;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.core.StatusCodes;

public class CalculationUnsignedByteTarget extends CalculationTarget
{
  public CalculationUnsignedByteTarget()
  {
    valueFromNode = ".shortValue()";
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
        v = new UnsignedByte[] { (UnsignedByte) v };
      dv.setValue(new Variant(v));
    }
    catch (IllegalArgumentException ex)
    {
      // we have an value out of range exception
      Object v = UnsignedByte.MIN_VALUE;
      // do we have an array, so change return value to array
      if (index >= 0 && v != null)
        v = new UnsignedByte[] { (UnsignedByte) v };
      dv.setValue(new Variant(v));
      dv.setStatusCode(new StatusCode(StatusCodes.Bad_OutOfRange));
    }
    return dv;
  }

  /**
   * change java script value into UnsignedByte representation possible java
   * scirpt values are Double, Integer, Boolean, String
   * 
   * @param val
   *          java script value
   * @return OPC UA UnsignedByte value
   */
  private UnsignedByte getValue(Object val) throws IllegalArgumentException
  {
    UnsignedByte ret = null;
    if (val == null)
    {
      return null;
    }
    else if (val instanceof Double)
    {
      Double d = (Double) val;
      if (d < UnsignedByte.MIN_VALUE.doubleValue() || d > UnsignedByte.MAX_VALUE.shortValue())
        throw new IllegalArgumentException("Illegal value");
      ret = new UnsignedByte(d.shortValue());
    }
    else if (val instanceof Float)
    {
    	Float d = (Float) val;
      if (d < UnsignedByte.MIN_VALUE.doubleValue() || d > UnsignedByte.MAX_VALUE.shortValue())
        throw new IllegalArgumentException("Illegal value");
      ret = new UnsignedByte(d.shortValue());
    }
    else if (val instanceof Long)
    {
    	Long d = (Long) val;
      if (d < UnsignedByte.MIN_VALUE.intValue() || d > UnsignedByte.MAX_VALUE.shortValue())
        throw new IllegalArgumentException("Illegal value");
      ret = new UnsignedByte(d.shortValue());
    }
    else if (val instanceof Integer)
    {
      Integer d = (Integer) val;
      if (d < UnsignedByte.MIN_VALUE.intValue() || d > UnsignedByte.MAX_VALUE.shortValue())
        throw new IllegalArgumentException("Illegal value");
      ret = new UnsignedByte(d.shortValue());
    }
    else if (val instanceof Short)
    {
    	Short d = (Short) val;
      if (d < UnsignedByte.MIN_VALUE.intValue() || d > UnsignedByte.MAX_VALUE.shortValue())
        throw new IllegalArgumentException("Illegal value");
      ret = new UnsignedByte(d.shortValue());
    }
    else if (val instanceof Byte)
    {
    	Byte d = (Byte) val;
      if (d < UnsignedByte.MIN_VALUE.intValue() || d > UnsignedByte.MAX_VALUE.shortValue())
        throw new IllegalArgumentException("Illegal value");
      ret = new UnsignedByte(d.shortValue());
    }
    else if (val instanceof Boolean)
    {
      if ((Boolean) val)
        ret = new UnsignedByte(1);
      else
        ret = new UnsignedByte(0);
    }
    else if (val instanceof String)
    {
      return null;
    }
    return ret;
  }
}
