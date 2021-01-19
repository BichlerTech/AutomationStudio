package com.bichler.opc.driver.siemens.transform.dint;

import com.bichler.opc.comdrv.utils.ComByteMessage;
import com.bichler.opc.driver.siemens.transform.SiemensTransform2Float;

public class SiemensDIntTransform2Float extends SiemensTransform2Float {
	@Override
	public Object transToIntern(ComByteMessage value) {
		if (value == null || value.getBuffer() == null || value.getBuffer().length < 4)
			return null;
		float i = value.getBuffer()[0];
		float j = value.getBuffer()[1];
		float k = value.getBuffer()[2];
		float l = value.getBuffer()[3];
		value.deleteFirstBytes(4);
		if (j < 0)
			j += 256;
		if (k < 0)
			k += 256;
		if (l < 0)
			l += 256;
		i = (i * 256 + j) * 256 * 256 + (k * 256 + l);
		return (float) i;
	}

	@Override
	public byte[] transToDevice(Object value) {
		byte[] data = new byte[4];
		int i = ((Float) value).intValue();
		data[3] = (byte) (i & 0xff);
		i = i >> 8;
		data[2] = (byte) (i & 0xff);
		i = i >> 8;
		data[1] = (byte) (i & 0xff);
		i = i >> 8;
		data[0] = (byte) (i & 0xff);
		return data;
	}
}
