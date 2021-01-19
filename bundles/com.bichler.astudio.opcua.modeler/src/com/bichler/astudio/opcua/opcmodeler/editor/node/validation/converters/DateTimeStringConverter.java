package com.bichler.astudio.opcua.opcmodeler.editor.node.validation.converters;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;

import org.opcfoundation.ua.builtintypes.DateTime;

import com.richclientgui.toolbox.validation.converter.IContentsStringConverter;

public class DateTimeStringConverter implements IContentsStringConverter<DateTime> {
	@Override
	public DateTime convertFromString(String value) {
		String newValue = value.replace(" GMT", "");
		DateFormat formatter =
				// DateFormat.getDateTimeInstance(
				// DateFormat.MEDIUM, DateFormat.MEDIUM);
				new SimpleDateFormat("MM/d/yy k:m:s.S");
		DateTime datetime = null;
		try {
			Date date = formatter.parse(newValue);
			long time = date.getTime();
			datetime = new DateTime(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return datetime;
		/**
		 * OPC UA try { // [yyyy-mm-dd]T[hh:mm:ss] String date = value.split("T")[0];
		 * int year = Integer.parseInt(date.split("-")[0]); int month =
		 * Integer.parseInt(date.split("-")[1]); int day =
		 * Integer.parseInt(date.split("-")[2]); date = value.split("T")[1]; int hours =
		 * Integer.parseInt(date.split(":")[0]); int minutes =
		 * Integer.parseInt(date.split(":")[1]); int seconds =
		 * Integer.parseInt(date.split(":")[2]); DateTime dateTime = new DateTime(year,
		 * month, day, hours, minutes, seconds, 0); return dateTime; } catch
		 * (NumberFormatException npe) { npe.printStackTrace(); return null; //
		 * npe.printStackTrace(); }
		 */
	}

	@Override
	public String convertToString(DateTime short_) {
		return short_.toString();
	}
}
