package com.bichler.opc.driver.siemens.transform.counter;

import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.DateTime;
import org.opcfoundation.ua.builtintypes.StatusCode;
import org.opcfoundation.ua.builtintypes.Variant;

import com.bichler.opc.comdrv.utils.ComByteMessage;
import com.bichler.opc.driver.siemens.transform.SiemensTransform2Integer;

public class SiemensCounterTransform2Integer extends SiemensTransform2Integer {
	@Override
	public DataValue transToIntern(ComByteMessage value) {
		int i = value.getBuffer()[0];
		int j = value.getBuffer()[1];
		value.deleteFirstBytes(2);
		if (i < 0)
			i += 256;
		if (j < 0)
			j += 256;
		i = i * 256 + j;
		DataValue dv = new DataValue(new Variant(i));
		dv.setSourceTimestamp(new DateTime());
		dv.setStatusCode(StatusCode.GOOD);
		// TODO Auto-generated method stub
		return dv;
	}

	@Override
	public byte[] transToDevice(Object value) {
		byte[] data = new byte[2];
		int i = ((Integer) value).intValue();
		data[0] = (byte) (i / 256);
		data[1] = (byte) (i % 256);
		return data;
	}
}