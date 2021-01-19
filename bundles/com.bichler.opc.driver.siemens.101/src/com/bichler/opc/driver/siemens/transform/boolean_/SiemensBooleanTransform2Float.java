package com.bichler.opc.driver.siemens.transform.boolean_;

import com.bichler.opc.comdrv.utils.ComByteMessage;
import com.bichler.opc.driver.siemens.transform.SiemensTransform2Float;

public class SiemensBooleanTransform2Float extends SiemensTransform2Float {
	@Override
	public Object transToIntern(ComByteMessage value) {
		if (value == null || value.getBuffer() == null || value.getBuffer().length == 0)
			return null;
		float trans = value.getBuffer()[0];
		value.deleteFirstBytes(1);
		return trans;
	}

	@Override
	public byte[] transToDevice(Object value) {
		byte[] data = new byte[1];
		if (((Float) value).floatValue() == 1)
			data[0] = 1;
		else
			data[0] = 0;
		return data;
	}
}
