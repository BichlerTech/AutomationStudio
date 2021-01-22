package com.bichler.opc.driver.siemens.transform.date;

import org.opcfoundation.ua.builtintypes.UnsignedByte;

import com.bichler.opc.comdrv.utils.ComByteMessage;
import com.bichler.opc.comdrv.utils.ValueOutOfRangeException;
import com.bichler.opc.driver.siemens.transform.SiemensTransform2UnsignedByte;

public class SiemensDateTransform2UnsignedByte extends SiemensTransform2UnsignedByte {
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
		if (i > UnsignedByte.L_MAX_VALUE || i < UnsignedByte.L_MIN_VALUE) {
			throw new ValueOutOfRangeException(
					"Couldn't transfor Siemens value to OPC UA Value, only values between " + UnsignedByte.L_MIN_VALUE
							+ " and " + UnsignedByte.L_MAX_VALUE + " are allowed, but we got, " + i + ".");
		}
		return (short) i;
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