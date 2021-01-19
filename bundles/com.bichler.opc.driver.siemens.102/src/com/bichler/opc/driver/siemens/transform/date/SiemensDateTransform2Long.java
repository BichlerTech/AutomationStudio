package com.bichler.opc.driver.siemens.transform.date;

import com.bichler.opc.comdrv.utils.ComByteMessage;
import com.bichler.opc.comdrv.utils.ValueOutOfRangeException;
import com.bichler.opc.driver.siemens.transform.SiemensTransform2Long;

public class SiemensDateTransform2Long extends SiemensTransform2Long {
	@Override
	public Object transToIntern(ComByteMessage value) throws ValueOutOfRangeException {
		int i = value.getBuffer()[0];
		int j = value.getBuffer()[1];
		value.deleteFirstBytes(2);
		if (i < 0)
			i += 256;
		if (j < 0)
			j += 256;
		i = i * 256 + j;
		if (i > Long.MAX_VALUE || i < Long.MIN_VALUE) {
			throw new ValueOutOfRangeException("Couldn't transfor Siemens value to OPC UA Value, only values between "
					+ Long.MIN_VALUE + " and " + Long.MAX_VALUE + " are allowed, but we got, " + i + ".");
		}
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
}
