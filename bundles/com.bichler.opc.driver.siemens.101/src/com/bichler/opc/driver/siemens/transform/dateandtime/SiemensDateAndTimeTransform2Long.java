package com.bichler.opc.driver.siemens.transform.dateandtime;

import com.bichler.opc.comdrv.utils.ComByteMessage;
import com.bichler.opc.driver.siemens.transform.SiemensTransform2Long;

public class SiemensDateAndTimeTransform2Long extends SiemensTransform2Long {
	@Override
	public Object transToIntern(ComByteMessage value) {
		value.deleteFirstBytes(8);
		throw new ClassCastException();
	}

	@Override
	public byte[] transToDevice(Object value) {
		byte[] data = new byte[2];
		long i = ((Long) value).longValue();
		data[0] = (byte) ((i / 256) % 256);
		data[1] = (byte) (i % 256);
		return data;
	}
}
