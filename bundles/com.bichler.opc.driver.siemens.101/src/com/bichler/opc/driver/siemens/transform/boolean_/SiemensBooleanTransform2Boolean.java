package com.bichler.opc.driver.siemens.transform.boolean_;

import com.bichler.opc.comdrv.utils.ComByteMessage;
import com.bichler.opc.comdrv.utils.ValueOutOfRangeException;
import com.bichler.opc.driver.siemens.transform.SiemensTransform2Boolean;

/**
 * this class transforms a siemens boolean representation into opc ua boolean
 * representation we take the required bit from byte message
 * 
 * @author applemc207da
 *
 */
public class SiemensBooleanTransform2Boolean extends SiemensTransform2Boolean {
	@Override
	public Object transToIntern(ComByteMessage value) throws ValueOutOfRangeException {
		if (value == null || value.getBuffer() == null || value.getBuffer().length == 0)
			return null;
		boolean trans = value.getBuffer()[0] == 1;
		value.deleteFirstBytes(1);
		return trans;
	}

	@Override
	public byte[] transToDevice(Object value) {
		byte[] data = new byte[1];
		if (((Boolean) value).booleanValue())
			data[0] = 1;
		else
			data[0] = 0;
		return data;
	}
}
