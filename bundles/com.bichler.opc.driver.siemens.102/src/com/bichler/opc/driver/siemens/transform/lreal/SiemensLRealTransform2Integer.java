package com.bichler.opc.driver.siemens.transform.lreal;

import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.DateTime;
import org.opcfoundation.ua.builtintypes.StatusCode;
import org.opcfoundation.ua.builtintypes.Variant;

import com.bichler.opc.comdrv.utils.ComByteMessage;
import com.bichler.opc.driver.siemens.transform.SiemensTransform2Integer;

public class SiemensLRealTransform2Integer extends SiemensTransform2Integer {
	@Override
	public DataValue transToIntern(ComByteMessage value) {
		if (value == null || value.getBuffer() == null || value.getBuffer().length < 4)
			return null;
		int i = value.getBuffer()[0];
		int j = value.getBuffer()[1];
		// int k = value.getBuffer()[2];
		// int l = value.getBuffer()[3];
		value.deleteFirstBytes(8);
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
