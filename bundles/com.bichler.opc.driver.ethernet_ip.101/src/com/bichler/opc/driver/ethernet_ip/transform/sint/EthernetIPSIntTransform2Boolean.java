package com.bichler.opc.driver.ethernet_ip.transform.sint;

import java.util.logging.Level;

import com.bichler.opc.comdrv.utils.ValueOutOfRangeException;
import com.bichler.opc.driver.ethernet_ip.transform.EthernetIPTransform2Boolean;

import etherip.types.CIPData;
import etherip.types.CIPData.Type;

public class EthernetIPSIntTransform2Boolean extends EthernetIPTransform2Boolean {

	@Override
	public void transToDevice(CIPData data, Object value, int index) throws IndexOutOfBoundsException, Exception {
		if ((Boolean) value)
			data.set(index, 1);
		else
			data.set(index, 0);
		return;
	}

	@Override
	public CIPData createCipData(int array) {
		try {
			return new CIPData(Type.SINT, array);
		} catch (Exception e) {
			return null;
		}
	}
	
	@Override
	public Object transToIntern(CIPData value, int index) throws ValueOutOfRangeException {
		Number val = null;
		if (value == null) {
			throw new ValueOutOfRangeException("Value can not be transformed into boolean represenation");
		}
		try {
			val = value.getNumber(index);
		} catch (Exception e) {
			throw new ValueOutOfRangeException("Value can not be transformed into boolean represenation");
		}
		logger.log(Level.FINE, "Transform SINT to Bool - value: " + val.intValue());
		return val.intValue() == 1;
	}
}
