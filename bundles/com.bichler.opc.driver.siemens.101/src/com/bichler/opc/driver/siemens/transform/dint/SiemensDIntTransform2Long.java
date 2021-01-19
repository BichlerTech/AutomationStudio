package com.bichler.opc.driver.siemens.transform.dint;

import com.bichler.opc.comdrv.utils.ComByteMessage;
import com.bichler.opc.driver.siemens.transform.SiemensTransform2Long;

public class SiemensDIntTransform2Long extends SiemensTransform2Long {
	@Override
	public Object transToIntern(ComByteMessage value) {
		if (value == null || value.getBuffer() == null || value.getBuffer().length < 4)
			return null;
		long i = value.getBuffer()[0];
		long j = value.getBuffer()[1];
		long k = value.getBuffer()[2];
		long l = value.getBuffer()[3];
		value.deleteFirstBytes(4);
		if (j < 0)
			j += 256;
		if (k < 0)
			k += 256;
		if (l < 0)
			l += 256;
		i = (i * 256 + j) * 256 * 256 + (k * 256 + l);
		return (long) i;
	}

	@Override
	public byte[] transToDevice(Object value) {
		byte[] data = new byte[4];
		long i = (Long) value;
		data[3] = (byte) (i & 0xff);
		i = i >> 8;
		data[2] = (byte) (i & 0xff);
		i = i >> 8;
		data[1] = (byte) (i & 0xff);
		i = i >> 8;
		data[0] = (byte) (i & 0xff);
		return data;
	}
}
