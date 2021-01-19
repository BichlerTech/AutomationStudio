package com.bichler.opc.driver.siemens.transform.lreal;

import org.opcfoundation.ua.builtintypes.UnsignedShort;

import com.bichler.opc.comdrv.utils.ComByteMessage;
import com.bichler.opc.driver.siemens.transform.SiemensTransform2UnsignedShort;

public class SiemensLRealTransform2UnsignedShort extends SiemensTransform2UnsignedShort {
	@Override
	public Object transToIntern(ComByteMessage value) {
		if (value == null || value.getBuffer() == null || value.getBuffer().length < 4)
			return null;
		int i = value.getBuffer()[0];
		int j = value.getBuffer()[1];
		int k = value.getBuffer()[2];
		int l = value.getBuffer()[3];
		value.deleteFirstBytes(8);
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
		i = Math.max(i, UnsignedShort.MIN_VALUE.intValue());
		i = Math.min(i, UnsignedShort.MAX_VALUE.intValue());
		return new UnsignedShort(i);
	}

	@Override
	public byte[] transToDevice(Object value) {
		byte[] data = new byte[4];
		int i = ((UnsignedShort) value).intValue();
		data[0] = (byte) (i / 256);
		data[1] = (byte) (i % 256);
		return data;
	}
}
