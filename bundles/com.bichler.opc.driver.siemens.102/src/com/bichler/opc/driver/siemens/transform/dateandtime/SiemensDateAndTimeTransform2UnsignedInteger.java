package com.bichler.opc.driver.siemens.transform.dateandtime;

import org.opcfoundation.ua.builtintypes.UnsignedInteger;

import com.bichler.opc.comdrv.utils.ComByteMessage;
import com.bichler.opc.driver.siemens.transform.SiemensTransform2UnsignedInteger;

public class SiemensDateAndTimeTransform2UnsignedInteger extends SiemensTransform2UnsignedInteger {
	@Override
	public Object transToIntern(ComByteMessage value) {
		value.deleteFirstBytes(8);
		throw new ClassCastException("");
	}

	@Override
	public byte[] transToDevice(Object value) {
		byte[] data = new byte[2];
		int i = ((UnsignedInteger) value).intValue();
		data[0] = (byte) (i / 256);
		data[1] = (byte) (i % 256);
		return data;
	}
}
