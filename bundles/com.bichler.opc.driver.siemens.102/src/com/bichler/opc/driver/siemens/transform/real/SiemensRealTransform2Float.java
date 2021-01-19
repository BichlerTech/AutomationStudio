package com.bichler.opc.driver.siemens.transform.real;

import com.bichler.opc.comdrv.utils.ComByteMessage;
import com.bichler.opc.driver.siemens.transform.SiemensTransform2Float;

public class SiemensRealTransform2Float extends SiemensTransform2Float {
	@Override
	public Object transToIntern(ComByteMessage value) {
		if (value == null || value.getBuffer() == null || value.getBuffer().length < 4)
			return null;
		int i = value.getBuffer()[0];
		int j = value.getBuffer()[1];
		int k = value.getBuffer()[2];
		int l = value.getBuffer()[3];
		value.deleteFirstBytes(4);
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
		return Float.intBitsToFloat(i);
	}

	@Override
	public byte[] transToDevice(Object value) {
		byte[] data = new byte[4];
		float f = ((Float) value).floatValue();
		int a = Float.floatToIntBits(f);
		data[3] = (byte) (a & 0xff);
		a = a >> 8;
		data[2] = (byte) (a & 0xff);
		a = a >> 8;
		data[1] = (byte) (a & 0xff);
		a = a >> 8;
		data[0] = (byte) (a & 0xff);
		return data;
	}
}
