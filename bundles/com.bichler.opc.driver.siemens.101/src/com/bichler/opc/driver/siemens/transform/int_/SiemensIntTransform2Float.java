package com.bichler.opc.driver.siemens.transform.int_;

import com.bichler.opc.comdrv.utils.ComByteMessage;
import com.bichler.opc.driver.siemens.transform.SiemensTransform2Float;

public class SiemensIntTransform2Float extends SiemensTransform2Float {
	@Override
	public Object transToIntern(ComByteMessage value) {
		if (value == null || value.getBuffer() == null || value.getBuffer().length < 2)
			return null;
		float i = value.getBuffer()[0];
		float j = value.getBuffer()[1];
		value.deleteFirstBytes(2);
		if (j < 0)
			j += 256;
		i = i * 256 + j;
		return i;
	}

	@Override
	public byte[] transToDevice(Object value) {
		byte[] data = new byte[2];
		int i = ((Float) value).intValue();
		data[1] = (byte) (i & 0xff);
		i = i >> 8;
		data[0] = (byte) (i & 0xff);
		return data;
	}
}
