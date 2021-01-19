package com.bichler.opc.driver.siemens.transform.time;

import com.bichler.opc.comdrv.utils.ComByteMessage;
import com.bichler.opc.comdrv.utils.ValueOutOfRangeException;
import com.bichler.opc.driver.siemens.transform.SiemensTransform2Double;

public class SiemensTimeTransform2Double extends SiemensTransform2Double {
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
		if (i > Double.MAX_VALUE || i < Double.MIN_VALUE) {
			throw new ValueOutOfRangeException("Couldn't transfor Siemens value to OPC UA Value, only values between "
					+ Double.MIN_VALUE + " and " + Double.MAX_VALUE + " are allowed, but we got, " + i + ".");
		}
		return (double) i;
	}

	@Override
	public byte[] transToDevice(Object value) {
		byte[] data = new byte[2];
		double d = ((Double) value).doubleValue();
		data[0] = (byte) ((d / 256) % 256);
		data[1] = (byte) (d % 256);
		return data;
	}
}
