package com.bichler.opc.driver.siemens.transform.char_;

import com.bichler.opc.comdrv.utils.ComByteMessage;
import com.bichler.opc.driver.siemens.transform.SiemensTransform2Short;

public class SiemensCharTransform2Short extends SiemensTransform2Short {
	@Override
	public Object transToIntern(ComByteMessage value) {
		if (value == null || value.getBuffer() == null || value.getBuffer().length == 0)
			return null;
		short s = value.getBuffer()[0];
		value.deleteFirstBytes(1);
		if (s < 0)
			s += 256;
		return s;
	}

	@Override
	public byte[] transToDevice(Object value) {
		byte[] data = new byte[1];
		short i = ((Short) value).shortValue();
		data[0] = (byte) (i % 256);
		return data;
	}
}
