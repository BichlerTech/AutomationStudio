package com.bichler.opc.driver.siemens.transform.time;

import com.bichler.opc.comdrv.utils.ComByteMessage;
import com.bichler.opc.driver.siemens.transform.SiemensTransform2String;

public class SiemensTimeTransform2String extends SiemensTransform2String {
	@Override
	public Object transToIntern(ComByteMessage value) {
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
		return "T#" + i + "ms";
	}

	@Override
	public byte[] transToDevice(Object value) {
		byte[] data = new byte[2];
		// String i = (String)value;
		return data;
	}
}
