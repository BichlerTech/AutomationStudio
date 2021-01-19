package com.bichler.opc.driver.siemens.transform.timer;

import com.bichler.opc.comdrv.utils.ComByteMessage;
import com.bichler.opc.driver.siemens.transform.SiemensTransform2Byte;

public class SiemensTimerTransform2Byte extends SiemensTransform2Byte {
	@Override
	public Object transToIntern(ComByteMessage value) {
		int i = value.getBuffer()[0];
		int j = value.getBuffer()[1];
		if (i < 0)
			i += 256;
		if (j < 0)
			j += 256;
		i = i * 256 + j;
		i = Math.max(i, Byte.MIN_VALUE);
		i = Math.min(i, Byte.MAX_VALUE);
		return (byte) i;
	}

	@Override
	public byte[] transToDevice(Object value) {
		byte[] data = new byte[2];
		data[0] = 0;
		data[1] = ((Byte) value).byteValue();
		return data;
	}
}
