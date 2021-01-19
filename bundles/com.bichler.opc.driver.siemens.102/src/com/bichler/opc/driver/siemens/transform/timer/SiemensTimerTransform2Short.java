package com.bichler.opc.driver.siemens.transform.timer;

import com.bichler.opc.comdrv.utils.ComByteMessage;
import com.bichler.opc.driver.siemens.transform.SiemensTransformation;

public class SiemensTimerTransform2Short implements SiemensTransformation {
	@Override
	public Object transToIntern(ComByteMessage value) {
		int i = value.getBuffer()[0];
		int j = value.getBuffer()[1];
		if (i < 0)
			i += 256;
		if (j < 0)
			j += 256;
		i = i * 256 + j;
		i = Math.max(i, Short.MIN_VALUE);
		i = Math.min(i, Short.MAX_VALUE);
		return (short) i;
	}

	@Override
	public byte[] transToDevice(Object value) {
		byte[] data = new byte[2];
		short i = ((Short) value).shortValue();
		data[0] = (byte) (i / 256);
		data[1] = (byte) (i % 256);
		return data;
	}

	@Override
	public Object[] createInternArray(int arrayLength) {
		// TODO Auto-generated method stub
		return null;
	}
}
