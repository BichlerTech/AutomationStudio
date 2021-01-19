package com.bichler.astudio.licensemanagement.exception;

public class ASStudioLicenseException extends Exception {

	/**
	 * TODO: Replace with IllegalAccessException
	 */
	private static final long serialVersionUID = -3496719998440449085L;

	public ASStudioLicenseException() {
		super();
	}

	public ASStudioLicenseException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ASStudioLicenseException(String message, Throwable cause) {
		super(message, cause);
	}

	public ASStudioLicenseException(String message) {
		super(message);
	}

	public ASStudioLicenseException(Throwable cause) {
		super(cause);
	}

}
