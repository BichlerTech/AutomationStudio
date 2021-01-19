package com.bichler.opc.driver.siemens.transform.char_;

import org.opcfoundation.ua.builtintypes.UnsignedLong;

import com.bichler.opc.comdrv.utils.ComByteMessage;
import com.bichler.opc.driver.siemens.transform.SiemensTransform2UnsignedLong;

public class SiemensCharTransform2UnsignedLong extends SiemensTransform2UnsignedLong {
	@Override
	public Object transToIntern(ComByteMessage value) {
		if (value == null || value.getBuffer() == null || value.getBuffer().length == 0)
			return null;
		int i = value.getBuffer()[0];
		value.deleteFirstBytes(1);
		if (i < 0)
			i += 256;
		return new UnsignedLong(i);
	}

	@Override
	public byte[] transToDevice(Object value) {
		byte[] data = new byte[1];
		long i = ((UnsignedLong) value).longValue();
		data[0] = (byte) (i % 256);
		return data;
	}
}
