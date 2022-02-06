package com.bichler.opc.driver.calculation.targets;

import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.DateTime;
import org.opcfoundation.ua.builtintypes.StatusCode;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.core.StatusCodes;

public class CalculationDoubleTarget extends CalculationTarget {
	public CalculationDoubleTarget() {
		valueFromNode = ".doubleValue()";
	}

	@Override
	public DataValue getTargetValue(Object val, int index) {
		DataValue dv = new DataValue();
		dv.setSourceTimestamp(new DateTime());
		dv.setStatusCode(StatusCode.GOOD);
		try {
			Object v = getValue(val);
			// do we have an array, so change return value to array
			if (index >= 0 && v != null)
				v = new Double[] { (Double) v };
			dv.setValue(new Variant(v));
		} catch (IllegalArgumentException ex) {
			// we have an value out of range exception
			Object v = Double.MIN_VALUE;
			if (index >= 0 && v != null)
				v = new Double[] { (Double) v };
			dv.setValue(new Variant(v));
			dv.setStatusCode(new StatusCode(StatusCodes.Bad_OutOfRange));
		}
		return dv;
	}

	/**
	 * change java script value into Double representation possible java scirpt
	 * values are Double, Integer, Boolean, String
	 * 
	 * @param val java script value
	 * @return OPC UA Double value
	 */
	private Double getValue(Object val) throws IllegalArgumentException {
		Double ret = null;
		if (val == null) {
			ret = null;
		} else if (val instanceof Double) {
			ret = (Double) val;
		} else if (val instanceof Float) {
			ret = ((Float) val).doubleValue();
		} else if (val instanceof Long) {
			Long d = (Long) val;
			if (d < -Double.MAX_VALUE || d > Double.MAX_VALUE)
				throw new IllegalArgumentException("Illegal value");
			ret = d.doubleValue();
		} else if (val instanceof Integer) {
			ret = ((Integer) val).doubleValue();
		} else if (val instanceof Short) {
			ret = ((Short) val).doubleValue();
		} else if (val instanceof Byte) {
			ret = ((Byte) val).doubleValue();
		} else if (val instanceof Boolean) {
			if ((Boolean) val)
				ret = 1.0;
			else
				ret = 0.0;
		} else if (val instanceof String) {
			return null;
		}
		return ret;
	}
}
