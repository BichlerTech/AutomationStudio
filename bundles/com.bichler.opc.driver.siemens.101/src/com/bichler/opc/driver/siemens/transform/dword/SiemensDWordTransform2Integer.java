package com.bichler.opc.driver.siemens.transform.dword;

import com.bichler.opc.comdrv.utils.ComByteMessage;
import com.bichler.opc.comdrv.utils.ValueOutOfRangeException;
import com.bichler.opc.driver.siemens.transform.SiemensTransform2Integer;

public class SiemensDWordTransform2Integer extends SiemensTransform2Integer {
	@Override
	public Object transToIntern(ComByteMessage value) throws ValueOutOfRangeException {
		if (value == null || value.getBuffer() == null || value.getBuffer().length < 4)
			return null;
		long i = value.getBuffer()[0];
		long j = value.getBuffer()[1];
		long k = value.getBuffer()[2];
		long l = value.getBuffer()[3];
		value.deleteFirstBytes(4);
		if (i < 0)
			i += 256;
		if (j < 0)
			j += 256;
		if (k < 0)
			k += 256;
		if (l < 0)
			l += 256;
		i = (256 * k + l) + 65536L * (256 * i + j);
		if (i < Integer.MIN_VALUE || i > Integer.MAX_VALUE)
			throw new ValueOutOfRangeException("Value from plc is out of OPC UA range!");
		return (int) i;
	}

	@Override
	public byte[] transToDevice(Object value) throws ValueOutOfRangeException {
		int i = (Integer) value;
		if (i < 0) {
			throw new ValueOutOfRangeException("Value from OPC UA is out of plc range!");
		}
		byte[] data = new byte[] { 0, 0, 0, 0 };
		data[0] = (byte) (i / (256 * 256 * 256));
		data[1] = (byte) (i / (256 * 256));
		data[2] = (byte) (i / (256));
		data[3] = (byte) (i % 256);
		return data;
	}
}
