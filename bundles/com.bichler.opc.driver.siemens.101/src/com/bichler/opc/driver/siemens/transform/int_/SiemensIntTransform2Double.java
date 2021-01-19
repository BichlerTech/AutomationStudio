package com.bichler.opc.driver.siemens.transform.int_;

import com.bichler.opc.comdrv.utils.ComByteMessage;
import com.bichler.opc.driver.siemens.transform.SiemensTransform2Double;

public class SiemensIntTransform2Double extends SiemensTransform2Double {
	@Override
	public Object transToIntern(ComByteMessage value) {
		if (value == null || value.getBuffer() == null || value.getBuffer().length < 2)
			return null;
		double i = value.getBuffer()[0];
		double j = value.getBuffer()[1];
		value.deleteFirstBytes(2);
		if (j < 0)
			j += 256;
		i = i * 256 + j;
		return i;
	}

	@Override
	public byte[] transToDevice(Object value) {
		byte[] data = new byte[2];
		long l = ((Double) value).longValue();
		data[1] = (byte) (l & 0xff);
		l = l >> 8;
		data[0] = (byte) (l & 0xff);
		return data;
	}
}
