package com.bichler.opc.driver.siemens.transform.byte_;

import com.bichler.opc.comdrv.utils.ComByteMessage;
import com.bichler.opc.comdrv.utils.ValueOutOfRangeException;
import com.bichler.opc.driver.siemens.transform.SiemensTransform2Boolean;

public class SiemensByteTransform2Boolean extends SiemensTransform2Boolean {
	@Override
	public Object transToIntern(ComByteMessage value) throws ValueOutOfRangeException {
		if (value == null || value.getBuffer() == null || value.getBuffer().length == 0)
			return null;
		int i = value.getBuffer()[0];
		value.deleteFirstBytes(1);
		if (i < 0)
			i += 256;
		if (i == 1)
			return true;
		else if (i == 0)
			return false;
		throw new ValueOutOfRangeException("");
	}

	@Override
	public byte[] transToDevice(Object value) {
		byte[] data = new byte[1];
		boolean b = ((Boolean) value).booleanValue();
		if (b)
			data[0] = 1;
		else
			data[0] = 0;
		return data;
	}
}
