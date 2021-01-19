package com.bichler.opc.driver.siemens.transform.boolean_;

import com.bichler.opc.comdrv.utils.ComByteMessage;
import com.bichler.opc.driver.siemens.transform.SiemensTransform2Double;

public class SiemensBooleanTransform2Double extends SiemensTransform2Double {
	@Override
	public Object transToIntern(ComByteMessage value) {
		if (value == null || value.getBuffer() == null || value.getBuffer().length == 0)
			return null;
		double trans = value.getBuffer()[0];
		value.deleteFirstBytes(1);
		return trans;
	}

	@Override
	public byte[] transToDevice(Object value) {
		byte[] data = new byte[1];
		if (((Double) value).doubleValue() == 1)
			data[0] = 1;
		else
			data[0] = 0;
		return data;
	}
}
