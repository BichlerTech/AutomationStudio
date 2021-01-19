package com.bichler.opc.driver.ethernet_ip.transform;

import org.opcfoundation.ua.builtintypes.UnsignedShort;
import org.opcfoundation.ua.builtintypes.Variant;

import com.bichler.opc.comdrv.utils.ValueOutOfRangeException;
import com.bichler.opc.driver.ethernet_ip.transform.EthernetIPTransformation;

import etherip.types.CIPData;

public abstract class EthernetIPTransform2UnsignedShort implements EthernetIPTransformation {

	@Override
	public Object[] createInternArray(int arrayLength) {
		return new UnsignedShort[arrayLength];
	}

	@Override
	public Variant getDefaultValue() {
		return new Variant(UnsignedShort.MIN_VALUE);
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

		if (val.intValue() > UnsignedShort.L_MAX_VALUE || val.intValue() < UnsignedShort.L_MIN_VALUE)
			throw new ValueOutOfRangeException("Value from plc is out of OPC UA range!");

		return new UnsignedShort(val.intValue());
	}
}
