package com.bichler.opc.driver.ethernet_ip.transform;

import org.opcfoundation.ua.builtintypes.Variant;

import com.bichler.opc.comdrv.utils.ValueOutOfRangeException;
import com.bichler.opc.driver.ethernet_ip.transform.EthernetIPTransformation;

import etherip.types.CIPData;

public abstract class EthernetIPTransform2Long implements EthernetIPTransformation {

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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (val.longValue() > Long.MAX_VALUE || val.longValue() < Long.MIN_VALUE)
			throw new ValueOutOfRangeException("Value from plc is out of OPC UA range!");

		return val.longValue();
	}
}
