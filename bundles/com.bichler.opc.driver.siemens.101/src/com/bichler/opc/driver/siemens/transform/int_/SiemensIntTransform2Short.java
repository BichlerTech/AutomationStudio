package com.bichler.opc.driver.siemens.transform.int_;

import com.bichler.opc.comdrv.utils.ComByteMessage;
import com.bichler.opc.driver.siemens.transform.SiemensTransform2Short;

public class SiemensIntTransform2Short extends SiemensTransform2Short {
	@Override
	public Object transToIntern(ComByteMessage value) {
		if (value == null || value.getBuffer() == null || value.getBuffer().length < 2)
			return null;
		int i = value.getBuffer()[0];
		int j = value.getBuffer()[1];
		value.deleteFirstBytes(2);
		if (j < 0)
			j += 256;
		i = i * 256 + j;
		i = Math.max(i, Short.MIN_VALUE);
		i = Math.min(i, Short.MAX_VALUE);
		return (short) i;
	}

	@Override
	public byte[] transToDevice(Object value) {
		byte[] data = new byte[2];
		int i = ((Short) value).intValue();
		data[1] = (byte) (i & 0xff);
		i = i >> 8;
		data[0] = (byte) (i & 0xff);
		return data;
	}
}
