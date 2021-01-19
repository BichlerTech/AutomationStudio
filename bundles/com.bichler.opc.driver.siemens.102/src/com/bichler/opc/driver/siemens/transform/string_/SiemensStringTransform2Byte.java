package com.bichler.opc.driver.siemens.transform.string_;

import com.bichler.opc.comdrv.utils.ComByteMessage;
import com.bichler.opc.driver.siemens.transform.SiemensTransform2Byte;

public class SiemensStringTransform2Byte extends SiemensTransform2Byte {
	@Override
	public Object transToIntern(ComByteMessage value) {
		return "";
	}

	@Override
	public byte[] transToDevice(Object value) {
		byte[] data = new byte[2];
		data[0] = 0;
		data[1] = ((Byte) value).byteValue();
		return data;
	}
}
