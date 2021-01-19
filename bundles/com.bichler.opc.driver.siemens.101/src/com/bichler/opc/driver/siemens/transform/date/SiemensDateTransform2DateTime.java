package com.bichler.opc.driver.siemens.transform.date;

import java.util.Calendar;

import org.opcfoundation.ua.builtintypes.DateTime;

import com.bichler.opc.comdrv.utils.ComByteMessage;
import com.bichler.opc.comdrv.utils.ValueOutOfRangeException;
import com.bichler.opc.driver.siemens.transform.SiemensTransform2DateTime;

public class SiemensDateTransform2DateTime extends SiemensTransform2DateTime {
	@Override
	public Object transToIntern(ComByteMessage value) {
		if (value == null || value.getBuffer() == null || value.getBuffer().length < 8)
			return null;
		int year = value.getBuffer()[0];
		if (year < 0) {
			year = 256 + year;
			year = 1900 + (year / 16) * 10 + year % 16;
		} else {
			year = 2000 + (year / 16) * 10 + year % 16;
		}
		int month = value.getBuffer()[1];
		month = (month / 16) * 10 + month % 16 - 1;
		int day = value.getBuffer()[2];
		day = (day / 16) * 10 + day % 16 - 1;
		DateTime dt = new DateTime(year, month, day, 0, 0, 0, 0);
		value.deleteFirstBytes(8);
		return dt;
	}

	@Override
	public byte[] transToDevice(Object value) throws ValueOutOfRangeException {
		int year = ((DateTime) value).getLocalCalendar().get(Calendar.YEAR);
		int month = ((DateTime) value).getLocalCalendar().get(Calendar.MONTH) + 1;
		int day = ((DateTime) value).getLocalCalendar().get(Calendar.DAY_OF_MONTH) + 1;
		int dayofweek = (byte) (((DateTime) value).getLocalCalendar().get(Calendar.DAY_OF_WEEK));
		;
		if (year < 1990 || year > 2089)
			throw new ValueOutOfRangeException("");
		if (year >= 2000) {
			year = year - 2000;
		}
		year = year / 10 * 16 + year % 10;
		month = month / 10 * 16 + month % 10;
		day = day / 10 * 16 + day % 10;
		// dayofweek ++;
		if (dayofweek == 8)
			dayofweek = 1;
		byte[] data = new byte[4];
		data[0] = (byte) (year);
		data[1] = (byte) (month);
		data[2] = (byte) (day);
		data[3] = (byte) dayofweek;
		return data;
	}
}
