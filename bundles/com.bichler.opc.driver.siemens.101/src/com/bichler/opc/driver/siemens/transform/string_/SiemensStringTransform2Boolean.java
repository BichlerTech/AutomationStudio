package com.bichler.opc.driver.siemens.transform.string_;

import com.bichler.opc.comdrv.utils.ComByteMessage;
import com.bichler.opc.driver.siemens.transform.SiemensTransform2Boolean;

public class SiemensStringTransform2Boolean extends SiemensTransform2Boolean {
	@Override
	public Object transToIntern(ComByteMessage value) {
		return "";
	}

	@Override
	public byte[] transToDevice(Object value) {
		byte[] data = new byte[2];
		boolean b = ((Boolean) value).booleanValue();
		data[0] = 0;
		if (b)
			data[1] = 1;
		else
			data[1] = 0;
		return data;
	}
}
