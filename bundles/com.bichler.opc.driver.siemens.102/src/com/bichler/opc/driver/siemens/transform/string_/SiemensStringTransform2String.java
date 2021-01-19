package com.bichler.opc.driver.siemens.transform.string_;

import com.bichler.opc.comdrv.utils.ComByteMessage;
import com.bichler.opc.driver.siemens.transform.SiemensTransform2String;

public class SiemensStringTransform2String extends SiemensTransform2String {
	@Override
	public Object transToIntern(ComByteMessage value) {
		return "";
	}

	@Override
	public byte[] transToDevice(Object value) {
		byte[] data = new byte[2];
		short i = ((Short) value).shortValue();
		data[0] = (byte) (i / 256);
		data[1] = (byte) (i % 256);
		return data;
	}
}
