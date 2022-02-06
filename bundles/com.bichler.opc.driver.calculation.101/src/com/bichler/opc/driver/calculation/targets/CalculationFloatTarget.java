package com.bichler.opc.driver.calculation.targets;

import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.DateTime;
import org.opcfoundation.ua.builtintypes.StatusCode;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.core.StatusCodes;

public class CalculationFloatTarget extends CalculationTarget {
	public CalculationFloatTarget() {
		valueFromNode = ".floatValue()";
	}

	@Override
	public DataValue getTargetValue(Object val, int index) {
		DataValue dv = new DataValue();
		dv.setSourceTimestamp(new DateTime());
		dv.setStatusCode(StatusCode.GOOD);
		try {
			// do we have an array, so change return value to array
			Object v = getValue(val);
			if (index >= 0 && v != null)
				v = new Float[] { (Float) v };
			dv.setValue(new Variant(v));
		} catch (IllegalArgumentException ex) {
			// we have an value out of range exception
			Object v = Float.MIN_VALUE;
			if (index >= 0 && v != null)
				v = new Float[] { (Float) v };
			dv.setValue(new Variant(v));
			dv.setStatusCode(new StatusCode(StatusCodes.Bad_OutOfRange));
		}
		return dv;
	}

	/**
	 * change java script value into Float representation possible java scirpt
	 * values are Double, Integer, Boolean, String
	 * 
	 * @param val java script value
	 * @return OPC UA Float value
	 */
	private Float getValue(Object val) throws IllegalArgumentException {
		Float ret = null;
		if (val == null) {
			return null;
		} else if (val instanceof Double) {
			Double d = (Double) val;
			if (d < -Float.MAX_VALUE || d > Float.MAX_VALUE)
				throw new IllegalArgumentException("Illegal value");
			ret = d.floatValue();
		} else if (val instanceof Float) {
			ret = (Float) val;
		} else if (val instanceof Long) {
			Long d = (Long) val;
			if (d < -Float.MAX_VALUE || d > Float.MAX_VALUE)
				throw new IllegalArgumentException("Illegal value");
			ret = d.floatValue();
		} else if (val instanceof Integer) {
			Integer d = (Integer) val;
			if (d < -Float.MAX_VALUE || d > Float.MAX_VALUE)
				throw new IllegalArgumentException("Illegal value");
			ret = d.floatValue();
		} else if (val instanceof Short) {
			ret = ((Short) val).floatValue();
		}  else if (val instanceof Byte) {
			ret = ((Byte) val).floatValue();
		}else if (val instanceof Boolean) {
			if ((Boolean) val)
				ret = 1.0f;
			else
				ret = 0.0f;
		} else if (val instanceof String) {
			return null;
		}
		return ret;
	}
}
