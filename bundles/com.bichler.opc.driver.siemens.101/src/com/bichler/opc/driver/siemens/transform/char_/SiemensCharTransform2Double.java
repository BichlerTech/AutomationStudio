package com.bichler.opc.driver.siemens.transform.char_;

import com.bichler.opc.comdrv.utils.ComByteMessage;
import com.bichler.opc.driver.siemens.transform.SiemensTransform2Double;

public class SiemensCharTransform2Double extends SiemensTransform2Double {
	@Override
	public Object transToIntern(ComByteMessage value) {
		if (value == null || value.getBuffer() == null || value.getBuffer().length == 0)
			return null;
		double d = value.getBuffer()[0];
		value.deleteFirstBytes(1);
		if (d < 0)
			d += 256;
		return d;
	}

	@Override
	public byte[] transToDevice(Object value) {
		byte[] data = new byte[1];
		double d = ((Double) value).doubleValue();
		data[0] = (byte) (d % 256);
		return data;
	}
}
