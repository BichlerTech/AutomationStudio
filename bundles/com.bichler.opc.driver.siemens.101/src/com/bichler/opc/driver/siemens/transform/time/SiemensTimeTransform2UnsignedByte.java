package com.bichler.opc.driver.siemens.transform.time;

import org.opcfoundation.ua.builtintypes.UnsignedByte;

import com.bichler.opc.comdrv.utils.ComByteMessage;
import com.bichler.opc.comdrv.utils.ValueOutOfRangeException;
import com.bichler.opc.driver.siemens.transform.SiemensTransform2UnsignedByte;

public class SiemensTimeTransform2UnsignedByte extends SiemensTransform2UnsignedByte {
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
		if (i > UnsignedByte.L_MAX_VALUE || i < UnsignedByte.L_MIN_VALUE) {
			throw new ValueOutOfRangeException(
					"Couldn't transfor Siemens value to OPC UA Value, only values between " + UnsignedByte.L_MIN_VALUE
							+ " and " + UnsignedByte.L_MAX_VALUE + " are allowed, but we got, " + i + ".");
		}
		return new UnsignedByte(i);
	}

	@Override
	public byte[] transToDevice(Object value) {
		byte[] data = new byte[2];
		int i = ((UnsignedByte) value).intValue();
		data[0] = (byte) (i / 256);
		data[1] = (byte) (i % 256);
		return data;
	}
}
