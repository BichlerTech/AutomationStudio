package com.bichler.opc.driver.ethernet_ip.transform;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.opcfoundation.ua.builtintypes.Variant;
import com.bichler.opc.comdrv.utils.ValueOutOfRangeException;
import etherip.types.CIPData;

public abstract class EthernetIPTransform2Long implements EthernetIPTransformation {
	protected Logger logger = Logger.getLogger(getClass().getName());
	
	@Override
	public Object[] createInternArray(int arrayLength) {
		return new Long[arrayLength];
	}

	@Override
	public Variant getDefaultValue() {
		return new Variant(Long.MIN_VALUE);
	}

	@Override
	public Object transToIntern(CIPData value, int index) throws ValueOutOfRangeException {
		Number val = null;
		try {
			val = value.getNumber(0);
		} catch (Exception e) {
			logger.log(Level.SEVERE, "{0} | transform index {1}",
					new String[] { e.getMessage(), Integer.toString(index) });
			return 0l;
		}

		if (val.longValue() > Long.MAX_VALUE || val.longValue() < Long.MIN_VALUE)
			throw new ValueOutOfRangeException("Value from plc is out of OPC UA range!");

		logger.log(Level.FINE, "Transform to Long - value: " + val);
		return val.longValue();
	}
}
