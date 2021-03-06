package opc.sdk.core.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Pattern;

import org.opcfoundation.ua.builtintypes.DateTime;
import org.opcfoundation.ua.builtintypes.Variant;

/**
 * Util to convert datetime formats.
 * 
 * @author Thomas Z&ouml;bauer
 *
 */
public class OPCDateFormat {
	private static Pattern datetimePattern = Pattern.compile("MM/dd/yy HH:mm:ss.SS");
	private static Pattern javaPattern = Pattern.compile("yyyy-MM-dd'T'HH:mm:ss");

	/**
	 * Converts a java.util.Date Object to an OPC DateTime.
	 * 
	 * @param Date Java Date Object.
	 * @return OPC DateTime.
	 */
	private static DateTime convertDateToDateTime(Date date) {
		Calendar instance = GregorianCalendar.getInstance();
		instance.setTime(date);
		DateTime dateTime = new DateTime(instance.get(Calendar.YEAR), instance.get(Calendar.MONTH),
				instance.get(Calendar.DAY_OF_MONTH), instance.get(Calendar.HOUR_OF_DAY), instance.get(Calendar.MINUTE),
				instance.get(Calendar.SECOND), instance.get(Calendar.MILLISECOND));
		return dateTime;
	}

	/**
	 * Formats an UTC String time to an OPC DateTime. <br>
	 * Format: "MM/dd/yy HH:mm:ss"
	 * 
	 * @param DateString Stringvalue of a Date.
	 * @return OPC DateTime
	 */
	public static DateTime parseDateTime(String dateString) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(datetimePattern.pattern());
		try {
			Date date = dateFormat.parse(dateString);
			DateTime dateTime = convertDateToDateTime(date);
			return dateTime;
		} catch (ParseException e) {
			dateFormat = new SimpleDateFormat(javaPattern.pattern());
			try {
				Date date1 = dateFormat.parse(dateString);
				DateTime dateTime = convertDateToDateTime(date1);
				return dateTime;
			} catch (ParseException e1) {
				return null;
			}
		}
	}

	/**
	 * Formats an UTC String time to an Variant containing a Datetime. <br>
	 * Format: "MM/dd/yy HH:mm:ss"
	 * 
	 * @param DateString Stringvalue of a Date.
	 * @return OPC DateTime
	 */
	public static Variant parseAsVariantDateTime(String dateString) {
		DateTime time = parseDateTime(dateString);
		if (time != null) {
			return new Variant(time);
		}
		return Variant.NULL;
	}
}
