package com.bichler.opc.driver.siemens.transform.counter;

import com.bichler.opc.comdrv.utils.ComByteMessage;
import com.bichler.opc.driver.siemens.transform.SiemensTransform2Double;

public class SiemensCounterTransform2Double extends SiemensTransform2Double {
	@Override
	public Object transToIntern(ComByteMessage value) {
		double i = value.getBuffer()[0];
		double j = value.getBuffer()[1];
		value.deleteFirstBytes(2);
		if (i < 0)
			i += 256;
		if (j < 0)
			j += 256;
		i = i * 256 + j;
		return i;
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
