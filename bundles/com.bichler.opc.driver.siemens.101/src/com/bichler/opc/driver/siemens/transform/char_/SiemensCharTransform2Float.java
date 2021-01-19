package com.bichler.opc.driver.siemens.transform.char_;

import com.bichler.opc.comdrv.utils.ComByteMessage;
import com.bichler.opc.driver.siemens.transform.SiemensTransform2Float;

public class SiemensCharTransform2Float extends SiemensTransform2Float {
	@Override
	public Object transToIntern(ComByteMessage value) {
		if (value == null || value.getBuffer() == null || value.getBuffer().length == 0)
			return null;
		float f = value.getBuffer()[0];
		value.deleteFirstBytes(1);
		if (f < 0)
			f += 256;
		return f;
	}

	@Override
	public byte[] transToDevice(Object value) {
		byte[] data = new byte[1];
		float f = ((Float) value).floatValue();
		data[0] = (byte) (f % 256);
		return data;
	}
}
