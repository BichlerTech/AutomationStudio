package com.bichler.opc.driver.ethernet_ip.transform;

import org.opcfoundation.ua.builtintypes.Variant;

import com.bichler.opc.comdrv.utils.ValueOutOfRangeException;
import com.bichler.opc.driver.ethernet_ip.transform.EthernetIPTransformation;

import etherip.types.CIPData;

public abstract class EthernetIPTransform2Byte implements EthernetIPTransformation {

	@Override
	public Object[] createInternArray(int arrayLength) {
		return new Byte[arrayLength];
	}

	@Override
	public Variant getDefaultValue() {
		return new Variant(Byte.MIN_VALUE);
	}

	@Override
	public Object transToIntern(CIPData value, int index) throws ValueOutOfRangeException {
		Number val = null;
		try {
			val = value.getNumber(index);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (val.intValue() > Byte.MAX_VALUE || val.intValue() < Byte.MIN_VALUE)
			throw new ValueOutOfRangeException("Value from plc is out of OPC UA range!");

		return val.byteValue();
	}
}
