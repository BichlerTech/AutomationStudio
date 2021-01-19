package com.bichler.astudio.log.server.util.copy;

import java.sql.Timestamp;
import java.util.Map;

import org.apache.log4j.spi.LocationInfo;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.ThrowableInformation;

import com.bichler.astudio.log.server.core.ASLog;

public class ASLogConverter {

	public static ASLog convert(LoggingEvent event) {
		ASLog.CometLogBuilder builder = new ASLog.CometLogBuilder();

		builder.categoryName((event.getLoggerName() == null) ? "" : event
				.getLoggerName());
		builder.threadName((event.getThreadName() == null) ? "" : event
				.getThreadName());
		builder.message((event.getRenderedMessage() == null) ? "" : event
				.getRenderedMessage());
		builder.level((event.getLevel() == null) ? "" : event.getLevel()
				.toString());
		builder.ndc((event.getNDC() == null) ? "" : event.getNDC());
		LocationInfo localInfo = event.getLocationInformation();

		if (localInfo != null) {
			builder.className((localInfo.getClassName() == null) ? ""
					: localInfo.getClassName());
			builder.fileName((localInfo.getFileName() == null) ? "" : localInfo
					.getFileName());
			builder.lineNumber((localInfo.getLineNumber() == null) ? ""
					: localInfo.getLineNumber());
			builder.methodName((localInfo.getMethodName() == null) ? ""
					: localInfo.getMethodName());
			builder.locationInfo((localInfo.fullInfo == null) ? ""
					: localInfo.fullInfo);
		}
		ThrowableInformation throwable = event.getThrowableInformation();

		if (throwable != null && throwable.getThrowableStrRep() != null) {
			StringBuilder strBuilder = new StringBuilder();
			String lineSeparator = "\n";

			for (String exception : throwable.getThrowableStrRep()) {
				strBuilder.append(exception).append(lineSeparator);
			}
			builder.throwable((strBuilder.toString() == null) ? "" : strBuilder
					.toString());
		}
		long time = event.getTimeStamp();

		if (time > 0) {
			builder.ms(String.valueOf(time));
			builder.date(new Timestamp(time).toString());
		}
		Map<?, ?> mdc = event.getProperties();

		if (mdc != null && !mdc.isEmpty()) {
			builder.mdc(mdc.toString());
		} else {
			builder.mdc("");
		}
		ASLog log = builder.build();
		return log;
	}

	public static String getValue(ASLog log, String logField) {
		switch (logField) {
		case ASLogConstants.LEVEL_FIELD_NAME:
			return log.getLevel();
		case ASLogConstants.CATEGORY_FIELD_NAME:
			return log.getCategoryName();
		case ASLogConstants.MESSAGE_FIELD_NAME:
			return log.getMessage();
		case ASLogConstants.LINE_FIELD_NAME:
			return log.getLineNumber();
		case ASLogConstants.DATE_FIELD_NAME:
			return log.getDate();
		case ASLogConstants.THROWABLE_FIELD_NAME:
			return log.getThrowable();
		case ASLogConstants.CLASS_FIELD_NAME:
			return log.getClassName();
		case ASLogConstants.FILE_FIELD_NAME:
			return log.getFileName();
		case ASLogConstants.LOCATION_INFO_FIELD_NAME:
			return log.getLocationInfo();
		case ASLogConstants.METHOD_FIELD_NAME:
			return log.getMethodName();
		case ASLogConstants.MILLISECONDS_FIELD_NAME:
			return log.getMs();
		case ASLogConstants.THREAD_FIELD_NAME:
			return log.getThreadName();
		case ASLogConstants.NDC_FIELD_NAME:
			return log.getNdc();
		case ASLogConstants.MDC_FIELD_NAME:
			return log.getMdc();
		default:
			throw new IllegalArgumentException("No log field with such name: "
					+ logField);
		}
	}
}
