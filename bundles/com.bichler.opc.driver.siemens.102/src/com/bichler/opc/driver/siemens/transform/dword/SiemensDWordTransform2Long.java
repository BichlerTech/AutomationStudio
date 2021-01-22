package com.bichler.opc.driver.siemens.transform.dword;

import com.bichler.opc.comdrv.utils.ComByteMessage;
import com.bichler.opc.driver.siemens.transform.SiemensTransform2Long;

public class SiemensDWordTransform2Long extends SiemensTransform2Long {
	@Override
	public Object transToIntern(ComByteMessage value) {
		if (value == null || value.getBuffer() == null || value.getBuffer().length < 4)
			return null;
		long i = value.getBuffer()[0];
		long j = value.getBuffer()[1];
		long k = value.getBuffer()[2];
		long l = value.getBuffer()[3];
		value.deleteFirstBytes(4);
		if (i < 0)
			i += 256;
		if (j < 0)
			j += 256;
		if (k < 0)
			k += 256;
		if (l < 0)
			l += 256;
		i = (256 * k + l) + 65536L * (256 * i + j);
		return (long) i;
	}

	@Override
	public byte[] transToDevice(Object value) {
		byte[] data = new byte[] { 0, 0, 0, 0 };
		long i = (Long) value;
		data[0] = (byte) (i / (256 * 256 * 256));
		data[1] = (byte) (i / (256 * 256));
		data[2] = (byte) (i / (256));
		data[3] = (byte) (i % 256);
		return data;
	}
}