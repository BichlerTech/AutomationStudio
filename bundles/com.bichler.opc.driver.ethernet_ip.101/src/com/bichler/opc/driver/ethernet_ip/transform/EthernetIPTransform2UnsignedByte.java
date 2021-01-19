package com.bichler.opc.driver.ethernet_ip.transform;

import org.opcfoundation.ua.builtintypes.UnsignedByte;
import org.opcfoundation.ua.builtintypes.Variant;

import com.bichler.opc.comdrv.utils.ValueOutOfRangeException;
import com.bichler.opc.driver.ethernet_ip.transform.EthernetIPTransformation;

import etherip.types.CIPData;

public abstract class EthernetIPTransform2UnsignedByte implements EthernetIPTransformation {

	@Override
	public Object[] createInternArray(int arrayLength) {
		return new UnsignedByte[arrayLength];
	}

	@Override
	public Variant getDefaultValue() {
		return new Variant(UnsignedByte.MIN_VALUE);
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

		if (val.intValue() > UnsignedByte.L_MAX_VALUE || val.intValue() < UnsignedByte.L_MIN_VALUE)
			throw new ValueOutOfRangeException("Value from plc is out of OPC UA range!");

		return new UnsignedByte(val.intValue());
	}
}
