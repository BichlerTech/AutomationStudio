package com.bichler.opc.driver.siemens.transform.lreal;

import com.bichler.opc.comdrv.utils.ComByteMessage;
import com.bichler.opc.driver.siemens.transform.SiemensTransform2Float;

public class SiemensLRealTransform2Float extends SiemensTransform2Float {
	@Override
	public Object transToIntern(ComByteMessage value) {
		if (value == null || value.getBuffer() == null || value.getBuffer().length < 8)
			return null;
		long i = value.getBuffer()[0];
		long j = value.getBuffer()[1];
		long k = value.getBuffer()[2];
		long l = value.getBuffer()[3];
		long m = value.getBuffer()[4];
		long n = value.getBuffer()[5];
		long o = value.getBuffer()[6];
		long p = value.getBuffer()[7];
		value.deleteFirstBytes(8);
		if (i < 0)
			i += 256;
		if (j < 0)
			j += 256;
		if (k < 0)
			k += 256;
		if (l < 0)
			l += 256;
		if (m < 0)
			m += 256;
		if (n < 0)
			n += 256;
		if (o < 0)
			o += 256;
		if (p < 0)
			p += 256;
		// create int bits
		i <<= 8;
		i |= j;
		i <<= 8;
		i |= k;
		i <<= 8;
		i |= l;
		i <<= 8;
		i |= m;
		i <<= 8;
		i |= n;
		i <<= 8;
		i |= o;
		i <<= 8;
		i |= p;
		return (float) Double.longBitsToDouble(i);
	}

	@Override
	public byte[] transToDevice(Object value) {
		byte[] data = new byte[8];
		double f = ((Float) value).floatValue();
		long a = Double.doubleToLongBits(f);
		data[7] = (byte) (a & 0xff);
		a = a >> 8;
		data[6] = (byte) (a & 0xff);
		a = a >> 8;
		data[5] = (byte) (a & 0xff);
		a = a >> 8;
		data[4] = (byte) (a & 0xff);
		a = a >> 8;
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
