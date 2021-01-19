package com.bichler.opc.driver.siemens.transform.timer;

import com.bichler.opc.comdrv.utils.ComByteMessage;
import com.bichler.opc.driver.siemens.transform.SiemensTransformation;

public class SiemensTimerTransform2Float implements SiemensTransformation {
	@Override
	public Object transToIntern(ComByteMessage value) {
		float i = value.getBuffer()[0];
		float j = value.getBuffer()[1];
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
		float f = ((Float) value).floatValue();
		data[0] = (byte) ((f / 256) % 256);
		data[1] = (byte) (f % 256);
		return data;
	}

	@Override
	public Object[] createInternArray(int arrayLength) {
		// TODO Auto-generated method stub
		return null;
	}
}
