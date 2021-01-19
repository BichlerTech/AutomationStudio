package com.bichler.opc.driver.siemens.transform.lreal;

import org.opcfoundation.ua.builtintypes.UnsignedShort;

import com.bichler.opc.comdrv.utils.ComByteMessage;
import com.bichler.opc.comdrv.utils.ValueOutOfRangeException;
import com.bichler.opc.driver.siemens.transform.SiemensTransform2Boolean;

public class SiemensLRealTransform2Boolean extends SiemensTransform2Boolean {
	@Override
	public Object transToIntern(ComByteMessage value) throws ValueOutOfRangeException {
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
		return i == 1;
	}

	@Override
	public byte[] transToDevice(Object value) {
		byte[] data = new byte[4];
		boolean b = ((Boolean) value).booleanValue();
		data[0] = 0;
		if (b)
			data[1] = 1;
		else
			data[1] = 0;
		return data;
	}
}
