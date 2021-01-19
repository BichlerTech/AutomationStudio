package com.bichler.opc.driver.siemens.transform.dint;

import org.opcfoundation.ua.builtintypes.UnsignedLong;

import com.bichler.opc.comdrv.utils.ComByteMessage;
import com.bichler.opc.comdrv.utils.ValueOutOfRangeException;
import com.bichler.opc.driver.siemens.transform.SiemensTransform2UnsignedLong;

public class SiemensDIntTransform2UnsignedLong extends SiemensTransform2UnsignedLong {
	@Override
	public Object transToIntern(ComByteMessage value) throws ValueOutOfRangeException {
		if (value == null || value.getBuffer() == null || value.getBuffer().length < 4)
			return null;
		int i = value.getBuffer()[0];
		int j = value.getBuffer()[1];
		// int k = value.getBuffer()[2];
		// int l = value.getBuffer()[3];
		value.deleteFirstBytes(4);
		if (j < 0)
			j += 256;
		i = i * 256 + j;
		if (i < UnsignedLong.MIN_VALUE.intValue())
			throw new ValueOutOfRangeException("Value from plc is out of OPC UA range!");
		return new UnsignedLong(i);
	}

	@Override
	public byte[] transToDevice(Object value) {
		byte[] data = new byte[2];
		long i = ((UnsignedLong) value).longValue();
		data[1] = (byte) (i & 0xff);
		i = i >> 8;
		data[0] = (byte) (i & 0xff);
		return data;
	}
}
