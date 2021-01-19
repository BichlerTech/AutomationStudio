package com.bichler.opc.driver.siemens.transform.boolean_;

import org.opcfoundation.ua.builtintypes.UnsignedByte;

import com.bichler.opc.comdrv.utils.ComByteMessage;
import com.bichler.opc.driver.siemens.transform.SiemensTransform2UnsignedByte;

public class SiemensBooleanTransform2UnsignedByte extends SiemensTransform2UnsignedByte {
	@Override
	public Object transToIntern(ComByteMessage value) {
		if (value == null || value.getBuffer() == null || value.getBuffer().length == 0)
			return null;
		short trans = value.getBuffer()[0];
		value.deleteFirstBytes(1);
		return new UnsignedByte(trans);
	}

	@Override
	public byte[] transToDevice(Object value) {
		byte[] data = new byte[1];
		if (((UnsignedByte) value).shortValue() == 1)
			data[0] = 1;
		else
			data[0] = 0;
		return data;
	}
}
