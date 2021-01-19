package com.bichler.opc.driver.ethernet_ip.transform.real;

import org.opcfoundation.ua.builtintypes.UnsignedByte;

import com.bichler.opc.driver.ethernet_ip.transform.EthernetIPTransform2UnsignedByte;

import etherip.types.CIPData;
import etherip.types.CIPData.Type;

public class EthernetIPRealTransform2UnsignedByte extends EthernetIPTransform2UnsignedByte {

	@Override
	public void transToDevice(CIPData data, Object value, int index) throws IndexOutOfBoundsException, Exception {
		data.set(index, new Float(((UnsignedByte) value).floatValue()));
		return;
	}

	@Override
	public CIPData createCipData(int array) {
		try {
			return new CIPData(Type.REAL, array);
		} catch (Exception e) {
			return null;
		}
	}
}
