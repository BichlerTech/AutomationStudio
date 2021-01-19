package com.bichler.opc.driver.siemens.transform.real;

import com.bichler.opc.comdrv.utils.ComByteMessage;
import com.bichler.opc.driver.siemens.transform.SiemensTransform2Byte;

public class SiemensRealTransform2Byte extends SiemensTransform2Byte {
	@Override
	public Object transToIntern(ComByteMessage value) {
		if (value == null || value.getBuffer() == null || value.getBuffer().length < 4)
			return null;
		int i = value.getBuffer()[0];
		int j = value.getBuffer()[1];
		int k = value.getBuffer()[2];
		int l = value.getBuffer()[3];
		value.deleteFirstBytes(4);
		// int m = 0;
		if (i < 0)
			i += 256;
		if (j < 0)
			j += 256;
		if (k < 0)
			k += 256;
		if (l < 0)
			l += 256;
		// create int bits
		i <<= 8;
		i |= j;
		i <<= 8;
		i |= k;
		i <<= 8;
		i |= l;
		float f = Float.intBitsToFloat(i);
		i = (int) f;
		i = Math.max(i, Byte.MIN_VALUE);
		i = Math.min(i, Byte.MAX_VALUE);
		return (byte) i;
	}

	@Override
	public byte[] transToDevice(Object value) {
		byte[] data = new byte[4];
		float f = ((Float) value).floatValue();
		int i = Float.floatToIntBits(f);
		data[0] = 0;
		data[1] = (byte) i;
		return data;
	}
}
