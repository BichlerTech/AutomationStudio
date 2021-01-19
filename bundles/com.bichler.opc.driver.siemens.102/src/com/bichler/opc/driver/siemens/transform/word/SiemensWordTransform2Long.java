package com.bichler.opc.driver.siemens.transform.word;

import com.bichler.opc.comdrv.utils.ComByteMessage;
import com.bichler.opc.driver.siemens.transform.SiemensTransform2Long;

public class SiemensWordTransform2Long extends SiemensTransform2Long {
	@Override
	public Object transToIntern(ComByteMessage value) {
		if (value == null || value.getBuffer() == null || value.getBuffer().length < 2)
			return null;
		long i = value.getBuffer()[0];
		long j = value.getBuffer()[1];
		value.deleteFirstBytes(2);
		if (i < 0)
			i += 256;
		if (j < 0)
			j += 256;
		i = i * 256 + j;
		return (long) i;
	}

	@Override
	public byte[] transToDevice(Object value) {
		byte[] data = new byte[2];
		long i = (Long) value;
		data[0] = (byte) ((i / 256) % 256);
		data[1] = (byte) (i % 256);
		return data;
	}
}
