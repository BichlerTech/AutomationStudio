package com.bichler.opc.driver.siemens.transform.byte_;

import org.opcfoundation.ua.builtintypes.UnsignedLong;

import com.bichler.opc.comdrv.utils.ComByteMessage;
import com.bichler.opc.driver.siemens.transform.SiemensTransform2UnsignedLong;

public class SiemensByteTransform2UnsignedLong extends SiemensTransform2UnsignedLong {
	@Override
	public Object transToIntern(ComByteMessage value) {
		if (value == null || value.getBuffer() == null || value.getBuffer().length == 0)
			return null;
		int val = value.getBuffer()[0];
		value.deleteFirstBytes(1);
		if (val < 0)
			val += 256;
		return new UnsignedLong(val);
	}

	@Override
	public byte[] transToDevice(Object value) {
		byte[] data = new byte[1];
		data[0] = (byte) ((UnsignedLong) value).intValue();
		return data;
	}
}
