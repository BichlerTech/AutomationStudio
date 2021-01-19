package com.bichler.opc.driver.siemens.transform.boolean_;

import org.opcfoundation.ua.builtintypes.UnsignedLong;

import com.bichler.opc.comdrv.utils.ComByteMessage;
import com.bichler.opc.driver.siemens.transform.SiemensTransform2UnsignedLong;

public class SiemensBooleanTransform2UnsignedLong extends SiemensTransform2UnsignedLong {
	@Override
	public Object transToIntern(ComByteMessage value) {
		if (value == null || value.getBuffer() == null || value.getBuffer().length == 0)
			return null;
		short trans = value.getBuffer()[0];
		value.deleteFirstBytes(1);
		return new UnsignedLong(trans);
	}

	@Override
	public byte[] transToDevice(Object value) {
		byte[] data = new byte[1];
		if (((UnsignedLong) value).intValue() == 1)
			data[0] = 1;
		else
			data[0] = 0;
		return data;
	}
}
