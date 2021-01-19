package com.bichler.opc.driver.siemens.transform.byte_;

import org.opcfoundation.ua.builtintypes.UnsignedShort;

import com.bichler.opc.comdrv.utils.ComByteMessage;
import com.bichler.opc.driver.siemens.transform.SiemensTransform2UnsignedShort;

public class SiemensByteTransform2UnsignedShort extends SiemensTransform2UnsignedShort {
	@Override
	public Object transToIntern(ComByteMessage value) {
		if (value == null || value.getBuffer() == null || value.getBuffer().length == 0)
			return null;
		int val = value.getBuffer()[0];
		value.deleteFirstBytes(1);
		if (val < 0)
			val += 256;
		return new UnsignedShort(val);
	}

	@Override
	public byte[] transToDevice(Object value) {
		byte[] data = new byte[1];
		data[0] = (byte) ((UnsignedShort) value).intValue();
		return data;
	}
}
