package com.bichler.opc.driver.ethernet_ip.transform.string;

import com.bichler.opc.comdrv.utils.ValueOutOfRangeException;
import com.bichler.opc.driver.ethernet_ip.transform.EthernetIPTransform2Boolean;

import etherip.types.CIPData;
import etherip.types.CIPData.Type;

public class EthernetIPStringTransform2String extends EthernetIPTransform2Boolean {

	@Override
	public Object transToIntern(CIPData value, int index) throws ValueOutOfRangeException {
		try {
			return value.getString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public CIPData createCipData(int array) {
		try {
			return new CIPData(Type.STRUCT, array);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public void transToDevice(CIPData data, Object value, int index) throws IndexOutOfBoundsException, Exception {
		data.setString(index, value.toString());
	}
}
