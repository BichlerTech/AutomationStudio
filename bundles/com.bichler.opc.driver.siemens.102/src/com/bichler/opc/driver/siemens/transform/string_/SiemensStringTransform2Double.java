package com.bichler.opc.driver.siemens.transform.string_;

import com.bichler.opc.comdrv.utils.ComByteMessage;
import com.bichler.opc.driver.siemens.transform.SiemensTransform2Double;

public class SiemensStringTransform2Double extends SiemensTransform2Double {
	@Override
	public Object transToIntern(ComByteMessage value) {
		return "";
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
