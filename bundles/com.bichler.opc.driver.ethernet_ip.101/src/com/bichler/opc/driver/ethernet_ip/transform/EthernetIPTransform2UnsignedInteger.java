package com.bichler.opc.driver.ethernet_ip.transform;

import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.builtintypes.Variant;

import com.bichler.opc.comdrv.utils.ValueOutOfRangeException;
import com.bichler.opc.driver.ethernet_ip.transform.EthernetIPTransformation;

import etherip.types.CIPData;

public abstract class EthernetIPTransform2UnsignedInteger implements EthernetIPTransformation {

	@Override
	public Object[] createInternArray(int arrayLength) {
		return new UnsignedInteger[arrayLength];
	}

	@Override
	public Variant getDefaultValue() {
		return new Variant(UnsignedInteger.MIN_VALUE);
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

		if (val.longValue() > UnsignedInteger.L_MAX_VALUE || val.longValue() < UnsignedInteger.L_MIN_VALUE)
			throw new ValueOutOfRangeException("Value from plc is out of OPC UA range!");

		return new UnsignedInteger(val.longValue());
	}
}
