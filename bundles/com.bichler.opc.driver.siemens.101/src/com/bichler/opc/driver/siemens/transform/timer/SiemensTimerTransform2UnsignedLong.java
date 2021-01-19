package com.bichler.opc.driver.siemens.transform.timer;

import org.opcfoundation.ua.builtintypes.UnsignedLong;

import com.bichler.opc.comdrv.utils.ComByteMessage;
import com.bichler.opc.driver.siemens.transform.SiemensTransformation;

public class SiemensTimerTransform2UnsignedLong implements SiemensTransformation {
	@Override
	public Object transToIntern(ComByteMessage value) {
		int i = value.getBuffer()[0];
		int j = value.getBuffer()[1];
		if (i < 0)
			i += 256;
		if (j < 0)
			j += 256;
		i = i * 256 + j;
		return new UnsignedLong(i);
	}

	@Override
	public byte[] transToDevice(Object value) {
		byte[] data = new byte[2];
		int i = ((UnsignedLong) value).intValue();
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
