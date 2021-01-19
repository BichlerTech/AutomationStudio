package com.bichler.opc.driver.siemens.transform.time;

import com.bichler.opc.comdrv.utils.ComByteMessage;
import com.bichler.opc.comdrv.utils.ValueOutOfRangeException;
import com.bichler.opc.driver.siemens.transform.SiemensTransform2Float;

public class SiemensTimeTransform2Float extends SiemensTransform2Float {
	@Override
	public Object transToIntern(ComByteMessage value) throws ValueOutOfRangeException {
		int i = value.getBuffer()[0];
		int j = value.getBuffer()[1];
		int k = value.getBuffer()[2];
		int l = value.getBuffer()[3];
		value.deleteFirstBytes(4);
		if (j < 0)
			j += 256;
		if (k < 0)
			k += 256;
		if (l < 0)
			l += 256;
		i = k * 256 + l + 65535 * (i * 256 + j);
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
