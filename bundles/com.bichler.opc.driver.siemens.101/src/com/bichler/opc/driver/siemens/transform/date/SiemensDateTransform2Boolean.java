package com.bichler.opc.driver.siemens.transform.date;

import com.bichler.opc.comdrv.utils.ComByteMessage;
import com.bichler.opc.driver.siemens.transform.SiemensTransform2Boolean;

public class SiemensDateTransform2Boolean extends SiemensTransform2Boolean {
	@Override
	public Object transToIntern(ComByteMessage value) {
		value.deleteFirstBytes(2);
		throw new ClassCastException();
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
