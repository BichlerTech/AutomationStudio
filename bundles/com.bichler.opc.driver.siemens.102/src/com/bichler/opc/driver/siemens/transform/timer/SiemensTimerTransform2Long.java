package com.bichler.opc.driver.siemens.transform.timer;

import com.bichler.opc.comdrv.utils.ComByteMessage;
import com.bichler.opc.driver.siemens.transform.SiemensTransformation;

public class SiemensTimerTransform2Long implements SiemensTransformation {
	@Override
	public Object transToIntern(ComByteMessage value) {
		long i = value.getBuffer()[0];
		long j = value.getBuffer()[1];
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
		long i = ((Long) value).longValue();
		data[0] = (byte) ((i / 256) % 256);
		data[1] = (byte) (i % 256);
		return data;
	}

	@Override
	public Object[] createInternArray(int arrayLength) {
		// TODO Auto-generated method stub
		return null;
	}
}
