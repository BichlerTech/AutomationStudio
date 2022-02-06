package com.bichler.opc.driver.calculation.targets;

import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.DateTime;
import org.opcfoundation.ua.builtintypes.StatusCode;
import org.opcfoundation.ua.builtintypes.Variant;

public class CalculationBooleanTarget extends CalculationTarget
{
  public CalculationBooleanTarget()
  {
    valueFromNode = ".booleanValue()";
  }

  @Override
  public DataValue getTargetValue(Object val, int index)
  {
    DataValue dv = new DataValue();
    dv.setSourceTimestamp(new DateTime());
    dv.setStatusCode(StatusCode.GOOD);
    dv.setValue(new Variant(getValue(val, index)));
    return dv;
  }

  /**
   * change java script value into Boolean representation possible java scirpt
   * values are Double, Integer, Boolean, String
   * 
   * @param val
   *          java script value
   * @return OPC UA Boolean value
   */
  private Object getValue(Object val, int index) throws IllegalArgumentException
  {
    Object ret = null;
    if (val == null)
    {
      return null;
    }
    else if (val instanceof Double)
    {
      if (((Double) val) == 1)
        ret = true;
      else
        ret = false;
    }
    else if (val instanceof Float)
    {
      if (((Float) val) == 1)
        ret = true;
      else
        ret = false;
    }
    else if (val instanceof Long)
    {
      if (((Long) val) == 1)
        ret = true;
      else
        ret = false;
    }
    else if (val instanceof Integer)
    {
      if (((Integer) val) == 1)
        ret = true;
      else
        ret = false;
    }
    else if (val instanceof Short)
    {
      if (((Short) val) == 1)
        ret = true;
      else
        ret = false;
    }
    else if (val instanceof Byte)
    {
      if (((Byte) val) == 1)
        ret = true;
      else
        ret = false;
    }
    else if (val instanceof Boolean)
    {
      ret = (Boolean) val;
    }
    else if (val instanceof String)
    {
      return null;
    }
    // do we have an array, so change return value to array
    if (index >= 0 && ret != null)
      ret = new Boolean[] { (Boolean) ret };
    return ret;
  }
}
