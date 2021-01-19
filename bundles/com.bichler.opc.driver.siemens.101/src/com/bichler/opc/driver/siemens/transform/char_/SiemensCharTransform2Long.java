package com.bichler.opc.driver.siemens.transform.char_;

import com.bichler.opc.comdrv.utils.ComByteMessage;
import com.bichler.opc.driver.siemens.transform.SiemensTransform2Long;

public class SiemensCharTransform2Long extends SiemensTransform2Long {
	@Override
	public Object transToIntern(ComByteMessage value) {
		if (value == null || value.getBuffer() == null || value.getBuffer().length == 0)
			return null;
		long l = value.getBuffer()[0];
		value.deleteFirstBytes(1);
		if (l < 0)
			l += 256;
		return l;
	}

	@Override
	public byte[] transToDevice(Object value) {
		byte[] data = new byte[1];
		long i = ((Long) value).longValue();
		data[0] = (byte) (i % 256);
		return data;
	}
}
