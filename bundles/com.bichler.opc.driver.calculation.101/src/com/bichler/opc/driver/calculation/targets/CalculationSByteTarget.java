package com.bichler.opc.driver.calculation.targets;

import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.DateTime;
import org.opcfoundation.ua.builtintypes.StatusCode;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.core.StatusCodes;

public class CalculationSByteTarget extends CalculationTarget {
	public CalculationSByteTarget() {
		valueFromNode = ".byteValue()";
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
				v = new Byte[] { (Byte) v };
			dv.setValue(new Variant(v));
		} catch (IllegalArgumentException ex) {
			// we have an value out of range exception
			Object v = Byte.MIN_VALUE;
			if (index >= 0 && v != null)
				v = new Byte[] { (Byte) v };
			dv.setValue(new Variant(v));
			dv.setStatusCode(new StatusCode(StatusCodes.Bad_OutOfRange));
		}
		return dv;
	}

	/**
	 * change java script value into Byte representation possible java scirpt values
	 * are Double, Integer, Boolean, String
	 * 
	 * @param val java script value
	 * @return OPC UA Byte value
	 */
	private Byte getValue(Object val) throws IllegalArgumentException {
		Byte ret = null;
		if (val == null) {
			return null;
		} else if (val instanceof Double) {
			Double d = (Double) val;
			if (d < Byte.MIN_VALUE || d > Byte.MAX_VALUE)
				throw new IllegalArgumentException("Illegal value");
			ret = d.byteValue();
		} else if (val instanceof Float) {
			Float d = (Float) val;
			if (d < Byte.MIN_VALUE || d > Byte.MAX_VALUE)
				throw new IllegalArgumentException("Illegal value");
			ret = d.byteValue();
		} else if (val instanceof Long) {
			Long i = (Long) val;
			if (i < Byte.MIN_VALUE || i > Byte.MAX_VALUE)
				throw new IllegalArgumentException("Illegal value");
			ret = i.byteValue();
		} else if (val instanceof Integer) {
			Integer i = (Integer) val;
			if (i < Byte.MIN_VALUE || i > Byte.MAX_VALUE)
				throw new IllegalArgumentException("Illegal value");
			ret = i.byteValue();
		} else if (val instanceof Short) {
			Short i = (Short) val;
			if (i < Byte.MIN_VALUE || i > Byte.MAX_VALUE)
				throw new IllegalArgumentException("Illegal value");
			ret = i.byteValue();
		} else if (val instanceof Byte) {
			Byte i = (Byte) val;
			if (i < Byte.MIN_VALUE || i > Byte.MAX_VALUE)
				throw new IllegalArgumentException("Illegal value");
			ret = i.byteValue();
		} else if (val instanceof Boolean) {
			if ((Boolean) val)
				ret = 1;
			else
				ret = 0;
		} else if (val instanceof String) {
			return null;
		}
		return ret;
	}
}
