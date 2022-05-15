package com.bichler.opc.driver.ethernet_ip.transform;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.opcfoundation.ua.builtintypes.Variant;
import com.bichler.opc.comdrv.utils.ValueOutOfRangeException;
import etherip.types.CIPData;

public abstract class EthernetIPTransform2Float implements EthernetIPTransformation {
	protected Logger logger = Logger.getLogger(getClass().getName());

	@Override
	public Object[] createInternArray(int arrayLength) {
		return new Float[arrayLength];
	}

	@Override
	public Variant getDefaultValue() {
		return new Variant(Float.MIN_VALUE);
	}

	@Override
	public Object transToIntern(CIPData value, int index) throws ValueOutOfRangeException {
		Number val = null;
		try {
			val = value.getNumber(index);
		} catch (Exception e) {
			logger.log(Level.SEVERE, "{0} | transform index {1}",
					new String[] { e.getMessage(), Integer.toString(index) });
			return 0.0f;
		}
		
		if (val == null || val.floatValue() > Float.MAX_VALUE) {
			throw new ValueOutOfRangeException("Value from plc ('" + val + "') is out of opc range ('"+Float.MIN_VALUE+"|" + Float.MAX_VALUE + "')!");
		}
		
		logger.log(Level.FINE, "Transform to Float - value: " + val);
		return val.floatValue();
	}
}
