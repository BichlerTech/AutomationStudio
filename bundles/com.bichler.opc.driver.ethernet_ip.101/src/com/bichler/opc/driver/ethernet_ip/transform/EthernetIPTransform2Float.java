package com.bichler.opc.driver.ethernet_ip.transform;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.opcfoundation.ua.builtintypes.Variant;

import com.bichler.opc.comdrv.utils.ValueOutOfRangeException;
import com.bichler.opc.driver.ethernet_ip.transform.EthernetIPTransformation;

import etherip.types.CIPData;

public abstract class EthernetIPTransform2Float implements EthernetIPTransformation {
	private Logger logger = Logger.getLogger(getClass().getName());

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
//    logger.log(Level.INFO, "Translate cip data to opc ua: {0}, index: {1}",
//        new String[] { value.toString(), Integer.toString(index) });
		Number val = null;
		try {
			val = value.getNumber(index);
//      logger.log(Level.INFO, "Value from cip: {0}", Float.toString(val.floatValue()));
		} catch (Exception e) {
			logger.log(Level.SEVERE, "{0} | transform index {1}",
					new String[] { e.getMessage(), Integer.toString(index) });
		}
		if (val == null || val.floatValue() > Float.MAX_VALUE) {
			logger.log(Level.INFO, "Value out of range exception {0}", Float.toString(val.floatValue()));
			throw new ValueOutOfRangeException("Value from plc is out of OPC UA range!");
		}
		return val.floatValue();
	}
}
