package com.bichler.opc.driver.siemens.transform.byte_;

import com.bichler.opc.comdrv.utils.ComByteMessage;
import com.bichler.opc.driver.siemens.transform.SiemensTransform2Float;

public class SiemensByteTransform2Float extends SiemensTransform2Float {
	@Override
	public Object transToIntern(ComByteMessage value) {
		if (value == null || value.getBuffer() == null || value.getBuffer().length == 0)
			return null;
		float val = value.getBuffer()[0];
		value.deleteFirstBytes(1);
		if (val < 0)
			val += 256;
		return val;
	}

	@Override
	public byte[] transToDevice(Object value) {
		byte[] data = new byte[1];
		data[0] = (byte) ((Float) value).floatValue();
		return data;
	}
}
