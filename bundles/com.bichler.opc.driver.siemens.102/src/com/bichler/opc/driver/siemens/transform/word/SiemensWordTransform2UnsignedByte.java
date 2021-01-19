package com.bichler.opc.driver.siemens.transform.word;

import org.opcfoundation.ua.builtintypes.UnsignedByte;

import com.bichler.opc.comdrv.utils.ComByteMessage;
import com.bichler.opc.driver.siemens.transform.SiemensTransform2UnsignedByte;

public class SiemensWordTransform2UnsignedByte extends SiemensTransform2UnsignedByte {
	@Override
	public Object transToIntern(ComByteMessage value) {
		if (value == null || value.getBuffer() == null || value.getBuffer().length < 2)
			return null;
		int i = value.getBuffer()[0];
		int j = value.getBuffer()[1];
		value.deleteFirstBytes(2);
		if (i < 0)
			i += 256;
		if (j < 0)
			j += 256;
		i = i * 256 + j;
		return new UnsignedByte(i);
	}

	@Override
	public byte[] transToDevice(Object value) {
		byte[] data = new byte[2];
		short i = ((UnsignedByte) value).shortValue();
		data[0] = (byte) (i / 256);
		data[1] = (byte) (i % 256);
		return data;
	}
}
