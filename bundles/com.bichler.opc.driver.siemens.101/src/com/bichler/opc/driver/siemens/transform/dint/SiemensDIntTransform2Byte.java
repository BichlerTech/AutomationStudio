package com.bichler.opc.driver.siemens.transform.dint;

import com.bichler.opc.comdrv.utils.ComByteMessage;
import com.bichler.opc.comdrv.utils.ValueOutOfRangeException;
import com.bichler.opc.driver.siemens.transform.SiemensTransform2Byte;

public class SiemensDIntTransform2Byte extends SiemensTransform2Byte {
	@Override
	public Object transToIntern(ComByteMessage value) throws ValueOutOfRangeException {
		if (value == null || value.getBuffer() == null || value.getBuffer().length < 4)
			return null;
		long i = value.getBuffer()[0];
		long j = value.getBuffer()[1];
		long k = value.getBuffer()[2];
		long l = value.getBuffer()[3];
		value.deleteFirstBytes(4);
		if (j < 0)
			j += 256;
		if (k < 0)
			k += 256;
		if (l < 0)
			l += 256;
		i = (i * 256 + j) * 256 * 256 + (k * 256 + l);
		if (i > Byte.MAX_VALUE || i < Byte.MIN_VALUE)
			throw new ValueOutOfRangeException("Value from plc is out of OPC UA range!");
		// i = Math.max(i, Byte.MIN_VALUE);
		// i = Math.min(i, Byte.MAX_VALUE);
		return (byte) i;
	}

	@Override
	public byte[] transToDevice(Object value) {
		byte[] data = new byte[4];
		data[0] = 0;
		data[1] = 0;
		data[2] = 0;
		data[3] = (Byte) value;
		return data;
	}
}
