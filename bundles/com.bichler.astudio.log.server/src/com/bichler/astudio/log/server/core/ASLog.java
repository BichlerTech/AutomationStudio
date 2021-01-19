package com.bichler.astudio.log.server.core;

public class ASLog {

	private final String categoryName; // %c
	private final String className; // %C
	private final String date; // %d
	private final String fileName; // %F
	private final String locationInfo; // %l
	private final String lineNumber; // %L
	private final String methodName; // %M
	private final String level; // %p
	private final String ms; // %r
	private final String threadName; // %t
	private final String message; // %m
	private final String throwable; // %throwable
	private final String ndc; // %x
	private final String mdc; // %X{key}

	private ASLog(CometLogBuilder builder) {
		this.categoryName = builder.categoryName;
		this.className = builder.className;
		this.date = builder.date;
		this.fileName = builder.fileName;
		this.locationInfo = builder.locationInfo;
		this.lineNumber = builder.lineNumber;
		this.methodName = builder.methodName;
		this.level = builder.level;
		this.ms = builder.ms;
		this.threadName = builder.threadName;
		this.message = builder.message;
		this.throwable = builder.throwable;
		this.ndc = builder.ndc;
		this.mdc = builder.mdc;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public String getClassName() {
		return className;
	}

	public String getDate() {
		return date;
	}

	public String getFileName() {
		return fileName;
	}

	public String getLocationInfo() {
		return locationInfo;
	}

	public String getLineNumber() {
		return lineNumber;
	}

	public String getMethodName() {
		return methodName;
	}

	public String getLevel() {
		return level;
	}

	public String getMs() {
		return ms;
	}

	public String getThreadName() {
		return threadName;
	}

	public String getMessage() {
		return message;
	}

	public String getThrowable() {
		return throwable;
	}

	public String getNdc() {
		return ndc;
	}

	public String getMdc() {
		return mdc;
	}

	@Override
	public int hashCode() {
		int result = 17;
		result = 31 * result + categoryName.hashCode();
		result = 31 * result + className.hashCode();
		result = 31 * result + date.hashCode();
		result = 31 * result + fileName.hashCode();
		result = 31 * result + locationInfo.hashCode();
		result = 31 * result + lineNumber.hashCode();
		result = 31 * result + methodName.hashCode();
		result = 31 * result + level.hashCode();
		result = 31 * result + ms.hashCode();
		result = 31 * result + threadName.hashCode();
		result = 31 * result + message.hashCode();
		result = 31 * result + throwable.hashCode();
		result = 31 * result + ndc.hashCode();
		result = 31 * result + mdc.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (obj == null) {
			return false;
		} else if (!(obj instanceof ASLog)) {
			return false;
		} else {
			ASLog log = (ASLog) obj;

			if (!categoryName.equals(log.getCategoryName())
					|| !className.equals(log.getClassName())
					|| !date.equals(log.getDate())
					|| !fileName.equals(log.getFileName())
					|| !locationInfo.equals(log.getLocationInfo())
					|| !lineNumber.equals(log.getLineNumber())
					|| !methodName.equals(log.getMethodName())
					|| !level.equals(log.getLevel()) || !ms.equals(log.getMs())
					|| !threadName.equals(log.getThreadName())
					|| !message.equals(log.getMessage())
					|| !throwable.equals(log.getThrowable())
					|| !ndc.equals(log.getNdc()) || !mdc.equals(log.getMdc())) {

				return false;
			}
		}
		return true;
	}

	@Override
	public String toString() {
		return "[categoryName=" + categoryName + "; className=" + className
				+ "; date=" + date + "; fileName=" + fileName
				+ "; locationInfo=" + locationInfo + "; lineNumber="
				+ lineNumber + "; methodName=" + methodName + "; level="
				+ level + "; ms=" + ms + "; threadName=" + threadName
				+ "; message=" + message + "; throwable=" + throwable
				+ "; ndc=" + ndc + "; mdc=" + mdc + "]";
	}

	public static class CometLogBuilder {

		private String categoryName = ""; // %c
		private String className = ""; // %C
		private String date = ""; // %d
		private String fileName = ""; // %F
		private String locationInfo = ""; // %l
		private String lineNumber = ""; // %L
		private String methodName = ""; // %M
		private String level = ""; // %p
		private String ms = ""; // %r
		private String threadName = ""; // %t
		private String message = ""; // %m
		private String throwable = ""; // %throwable
		private String ndc = ""; // %x
		private String mdc = ""; // %X{key}

		public CometLogBuilder categoryName(String name) {
			categoryName = name;
			return this;
		}

		public CometLogBuilder className(String name) {
			className = name;
			return this;
		}

		public CometLogBuilder date(String date) {
			this.date = date;
			return this;
		}

		public CometLogBuilder fileName(String name) {
			fileName = name;
			return this;
		}

		public CometLogBuilder locationInfo(String info) {
			locationInfo = info;
			return this;
		}

		public CometLogBuilder lineNumber(String line) {
			lineNumber = line;
			return this;
		}

		public CometLogBuilder methodName(String name) {
			methodName = name;
			return this;
		}

		public CometLogBuilder level(String level) {
			this.level = level;
			return this;
		}

		public CometLogBuilder ms(String ms) {
			this.ms = ms;
			return this;
		}

		public CometLogBuilder threadName(String name) {
			threadName = name;
			return this;
		}

		public CometLogBuilder message(String msg) {
			message = msg;
			return this;
		}

		public CometLogBuilder throwable(String throwable) {
			this.throwable = throwable;
			return this;
		}

		public CometLogBuilder ndc(String ndc) {
			this.ndc = ndc;
			return this;
		}

		public CometLogBuilder mdc(String mdc) {
			this.mdc = mdc;
			return this;
		}

		public ASLog build() {
			return new ASLog(this);
		}
	}
}
