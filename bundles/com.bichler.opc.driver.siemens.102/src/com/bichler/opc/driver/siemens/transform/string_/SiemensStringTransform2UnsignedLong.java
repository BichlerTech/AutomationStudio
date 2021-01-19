package com.bichler.opc.driver.siemens.transform.string_;

import org.opcfoundation.ua.builtintypes.UnsignedLong;

import com.bichler.opc.comdrv.utils.ComByteMessage;
import com.bichler.opc.driver.siemens.transform.SiemensTransform2UnsignedLong;

public class SiemensStringTransform2UnsignedLong extends SiemensTransform2UnsignedLong {
	@Override
	public Object transToIntern(ComByteMessage value) {
		return "";
	}

	@Override
	public byte[] transToDevice(Object value) {
		byte[] data = new byte[2];
		int i = ((UnsignedLong) value).intValue();
		data[0] = (byte) (i / 256);
		data[1] = (byte) (i % 256);
		return data;
	}
}
