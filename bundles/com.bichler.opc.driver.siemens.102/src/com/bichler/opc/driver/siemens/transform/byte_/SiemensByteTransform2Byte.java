package com.bichler.opc.driver.siemens.transform.byte_;

import com.bichler.opc.comdrv.utils.ComByteMessage;
import com.bichler.opc.driver.siemens.transform.SiemensTransform2Byte;

public class SiemensByteTransform2Byte extends SiemensTransform2Byte {
	@Override
	public Object transToIntern(ComByteMessage value) {
		if (value == null || value.getBuffer() == null || value.getBuffer().length == 0)
			return null;
		int i = value.getBuffer()[0];
		value.deleteFirstBytes(1);
		// check ranges because opc ua has range from -128 to 127
		if (i < 0)
			i += 256;
		if (i > Byte.MAX_VALUE)
			return Byte.MAX_VALUE;
		return i;
	}

	@Override
	public byte[] transToDevice(Object value) {
		byte[] data = new byte[1];
		data[0] = ((Byte) value).byteValue();
		return data;
	}
}
