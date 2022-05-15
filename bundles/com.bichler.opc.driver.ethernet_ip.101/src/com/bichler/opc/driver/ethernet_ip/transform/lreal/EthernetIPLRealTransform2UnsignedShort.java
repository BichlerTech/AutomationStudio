package com.bichler.opc.driver.ethernet_ip.transform.lreal;

import org.opcfoundation.ua.builtintypes.UnsignedShort;

import com.bichler.opc.driver.ethernet_ip.transform.EthernetIPTransform2UnsignedShort;

import etherip.types.CIPData;
import etherip.types.CIPData.Type;

public class EthernetIPLRealTransform2UnsignedShort extends EthernetIPTransform2UnsignedShort {

	@Override
	public void transToDevice(CIPData data, Object value, int index) throws IndexOutOfBoundsException, Exception {
		data.set(index, new Double(((UnsignedShort) value).doubleValue()));
		return;
	}

	@Override
	public CIPData createCipData(int array) {
		try {
			return new CIPData(Type.LREAL, array);
		} catch (Exception e) {
			return null;
		}
	}
}
