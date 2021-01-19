package com.bichler.opc.driver.siemens.transform.string_;

import org.opcfoundation.ua.builtintypes.UnsignedShort;

import com.bichler.opc.comdrv.utils.ComByteMessage;
import com.bichler.opc.driver.siemens.transform.SiemensTransform2UnsignedShort;

public class SiemensStringTransform2UnsignedShort extends SiemensTransform2UnsignedShort {
	@Override
	public Object transToIntern(ComByteMessage value) {
		return "";
	}

	@Override
	public byte[] transToDevice(Object value) {
		byte[] data = new byte[2];
		int i = ((UnsignedShort) value).intValue();
		data[0] = (byte) (i / 256);
		data[1] = (byte) (i % 256);
		return data;
	}
}
