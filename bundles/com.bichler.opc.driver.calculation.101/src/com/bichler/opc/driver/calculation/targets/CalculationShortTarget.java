package com.bichler.opc.driver.calculation.targets;

import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.DateTime;
import org.opcfoundation.ua.builtintypes.StatusCode;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.core.StatusCodes;

public class CalculationShortTarget extends CalculationTarget {
	public CalculationShortTarget() {
		valueFromNode = ".shortValue()";
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
				v = new Short[] { (Short) v };
			dv.setValue(new Variant(v));
		} catch (IllegalArgumentException ex) {
			// we have an value out of range exception
			Object v = Short.MIN_VALUE;
			// do we have an array, so change return value to array
			if (index >= 0 && v != null)
				v = new Short[] { (Short) v };
			dv.setValue(new Variant(v));
			dv.setStatusCode(new StatusCode(StatusCodes.Bad_OutOfRange));
		}
		return dv;
	}

	/**
	 * change java script value into Short representation possible java scirpt
	 * values are Double, Integer, Boolean, String
	 * 
	 * @param val java script value
	 * @return OPC UA Short value
	 */
	private Short getValue(Object val) throws IllegalArgumentException {
		Short ret = null;
		if (val == null) {
			return null;
		} else if (val instanceof Float) {
			Float d = (Float) val;
			if (d < Short.MIN_VALUE || d > Short.MAX_VALUE)
				throw new IllegalArgumentException("Illegal value");
			ret = d.shortValue();
		} else if (val instanceof Double) {
			Double d = (Double) val;
			if (d < Short.MIN_VALUE || d > Short.MAX_VALUE)
				throw new IllegalArgumentException("Illegal value");
			ret = d.shortValue();
		} else if (val instanceof Integer) {
			Integer d = (Integer) val;
			if (d < Short.MIN_VALUE || d > Short.MAX_VALUE)
				throw new IllegalArgumentException("Illegal value");
			ret = d.shortValue();
		} else if (val instanceof Long) {
			Long d = (Long) val;
			if (d < Short.MIN_VALUE || d > Short.MAX_VALUE)
				throw new IllegalArgumentException("Illegal value");
			ret = d.shortValue();
		} else if (val instanceof Boolean) {
			if ((Boolean) val)
				ret = 1;
			else
				ret = 0;
		} else if (val instanceof String) {
			return null;
		} else if (val instanceof Short) {
			return (Short) val;
		} else if (val instanceof Byte) {
			return ((Byte) val).shortValue();
		}
		return ret;
	}
}
