package com.bichler.opc.driver.siemens.transform.dword;

import org.opcfoundation.ua.builtintypes.UnsignedShort;

import com.bichler.opc.comdrv.utils.ComByteMessage;
import com.bichler.opc.comdrv.utils.ValueOutOfRangeException;
import com.bichler.opc.driver.siemens.transform.SiemensTransform2UnsignedShort;

public class SiemensDWordTransform2UnsignedShort extends SiemensTransform2UnsignedShort {
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
		if (i < UnsignedShort.L_MIN_VALUE || i > UnsignedShort.L_MAX_VALUE)
			throw new ValueOutOfRangeException("Value from plc is out of OPC UA range!");
		return new UnsignedShort(i);
	}

	@Override
	public byte[] transToDevice(Object value) {
		byte[] data = new byte[] { 0, 0, 0, 0 };
		int i = ((UnsignedShort) value).intValue();
		data[0] = (byte) (i / (256 * 256 * 256));
		data[1] = (byte) (i / (256 * 256));
		data[2] = (byte) (i / (256));
		data[3] = (byte) (i % 256);
		return data;
	}
}
