package com.bichler.opc.driver.siemens.transform.dateandtime;

import org.opcfoundation.ua.builtintypes.DataValue;

import com.bichler.opc.comdrv.utils.ComByteMessage;
import com.bichler.opc.driver.siemens.transform.SiemensTransform2Float;

public class SiemensDateAndTimeTransform2Float extends SiemensTransform2Float {
	@Override
	public DataValue transToIntern(ComByteMessage value) {
		value.deleteFirstBytes(8);
		throw new ClassCastException();
	}

	@Override
	public byte[] transToDevice(Object value) {
		byte[] data = new byte[2];
		float f = ((Float) value).floatValue();
		data[0] = (byte) ((f / 256) % 256);
		data[1] = (byte) (f % 256);
		return data;
	}
}
