package com.bichler.opc.driver.siemens.transform.int_;

import com.bichler.opc.comdrv.utils.ComByteMessage;
import com.bichler.opc.comdrv.utils.ValueOutOfRangeException;
import com.bichler.opc.driver.siemens.transform.SiemensTransform2Boolean;

public class SiemensIntTransform2Boolean extends SiemensTransform2Boolean {
	@Override
	public Object transToIntern(ComByteMessage value) throws ValueOutOfRangeException {
		if (value == null || value.getBuffer() == null || value.getBuffer().length < 2)
			return null;
		int i = value.getBuffer()[0];
		int j = value.getBuffer()[1];
		value.deleteFirstBytes(2);
		if (j < 0)
			j += 256;
		i = i * 256 + j;
		if (i != 0 && i != 1)
			throw new ValueOutOfRangeException("Value from plc is out of OPC UA range!");
		return i == 1;
	}

	@Override
	public byte[] transToDevice(Object value) {
		byte[] data = new byte[2];
		boolean b = (Boolean) value;
		data[0] = 0;
		if (b)
			data[1] = 1;
		else
			data[1] = 0;
		return data;
	}
}
