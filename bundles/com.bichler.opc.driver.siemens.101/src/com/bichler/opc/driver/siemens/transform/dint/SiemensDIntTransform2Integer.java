package com.bichler.opc.driver.siemens.transform.dint;

import com.bichler.opc.comdrv.utils.ComByteMessage;
import com.bichler.opc.driver.siemens.transform.SiemensTransform2Integer;

public class SiemensDIntTransform2Integer extends SiemensTransform2Integer {
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
		return (int) i;
	}

	@Override
	public byte[] transToDevice(Object value) {
		byte[] b = new byte[4];
		int i = (Integer) value;
		b[3] = (byte) (i & 0xff);
		i = i >> 8;
		b[2] = (byte) (i & 0xff);
		i = i >> 8;
		b[1] = (byte) (i & 0xff);
		i = i >> 8;
		b[0] = (byte) (i & 0xff);
		return b;
	}
}
