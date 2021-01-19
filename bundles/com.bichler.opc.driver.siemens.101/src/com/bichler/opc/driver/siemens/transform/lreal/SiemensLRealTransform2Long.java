package com.bichler.opc.driver.siemens.transform.lreal;

import com.bichler.opc.comdrv.utils.ComByteMessage;
import com.bichler.opc.driver.siemens.transform.SiemensTransform2Long;

public class SiemensLRealTransform2Long extends SiemensTransform2Long {
	@Override
	public Object transToIntern(ComByteMessage value) {
		if (value == null || value.getBuffer() == null || value.getBuffer().length < 4)
			return null;
		int i = value.getBuffer()[0];
		int j = value.getBuffer()[1];
		// int k = value.getBuffer()[2];
		// int l = value.getBuffer()[3];
		value.deleteFirstBytes(8);
		if (i < 0)
			i += 256;
		if (j < 0)
			j += 256;
		i = i * 256 + j;
		return i;
	}

	@Override
	public byte[] transToDevice(Object value) {
		byte[] data = new byte[2];
		long i = ((Long) value).longValue();
		data[0] = (byte) ((i / 256) % 256);
		data[1] = (byte) (i % 256);
		return data;
	}
}
