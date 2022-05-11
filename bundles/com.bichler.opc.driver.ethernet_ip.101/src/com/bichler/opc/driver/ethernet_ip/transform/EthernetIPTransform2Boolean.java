package com.bichler.opc.driver.ethernet_ip.transform;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.opcfoundation.ua.builtintypes.Variant;
import com.bichler.opc.comdrv.utils.ValueOutOfRangeException;
import etherip.types.CIPData;

public abstract class EthernetIPTransform2Boolean implements EthernetIPTransformation {

	protected Logger logger = Logger.getLogger(getClass().getName());
	
	@Override
	public Object[] createInternArray(int arrayLength) {
		return new Boolean[arrayLength];
	}

	@Override
	public Variant getDefaultValue() {
		return new Variant(Boolean.FALSE);
	}

	@Override
	public Object transToIntern(CIPData value, int index) throws ValueOutOfRangeException {
		Number val = null;
		try {
			val = value.getNumber(index);
		} catch (Exception e) {
			logger.log(Level.SEVERE, "{0} | transform index {1}",
					new String[] { e.getMessage(), Integer.toString(index) });
			return false;
		}
		logger.log(Level.FINE, "Transform to Bool - value: " + val);
		return val.longValue() == 1;
	}
}
