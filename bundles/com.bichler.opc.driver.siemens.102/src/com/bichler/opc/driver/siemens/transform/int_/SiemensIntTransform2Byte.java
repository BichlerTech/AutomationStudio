package com.bichler.opc.driver.siemens.transform.int_;

import com.bichler.opc.comdrv.utils.ComByteMessage;
import com.bichler.opc.comdrv.utils.ValueOutOfRangeException;
import com.bichler.opc.driver.siemens.transform.SiemensTransform2Byte;

public class SiemensIntTransform2Byte extends SiemensTransform2Byte {
	@Override
	public Object transToIntern(ComByteMessage value) throws ValueOutOfRangeException {
		if (value == null || value.getBuffer() == null || value.getBuffer().length < 2)
			return null;
		int i = value.getBuffer()[0];
		int j = value.getBuffer()[1];
		value.deleteFirstBytes(2);
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
		data[1] = (Byte) value;
		return data;
	}
}
