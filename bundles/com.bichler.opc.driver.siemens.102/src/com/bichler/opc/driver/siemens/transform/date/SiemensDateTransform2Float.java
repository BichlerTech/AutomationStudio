package com.bichler.opc.driver.siemens.transform.date;

import com.bichler.opc.comdrv.utils.ComByteMessage;
import com.bichler.opc.comdrv.utils.ValueOutOfRangeException;
import com.bichler.opc.driver.siemens.transform.SiemensTransform2Float;

public class SiemensDateTransform2Float extends SiemensTransform2Float {
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
		if (i > Float.MAX_VALUE || i < Float.MIN_VALUE) {
			throw new ValueOutOfRangeException("Couldn't transfor Siemens value to OPC UA Value, only values between "
					+ Float.MIN_VALUE + " and " + Float.MAX_VALUE + " are allowed, but we got, " + i + ".");
		}
		return (float) i;
	}

	@Override
	public byte[] transToDevice(Object value) {
		byte[] data = new byte[2];
		float f = ((Float) value).floatValue();
		data[0] = (byte) ((f / 256) % 256);
		data[1] = (byte) (f % 256);
		return data;
	}
}
