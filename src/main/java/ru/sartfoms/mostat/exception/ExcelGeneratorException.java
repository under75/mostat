package ru.sartfoms.mostat.exception;

public class ExcelGeneratorException extends Exception {
	private static final long serialVersionUID = 1L;

	public ExcelGeneratorException() {
	}

	public ExcelGeneratorException(String message) {
		super(message);
	}

	public ExcelGeneratorException(String message, Throwable cause) {
		super(message, cause);
	}

	public ExcelGeneratorException(Throwable cause) {
		super(cause);
	}

	public ExcelGeneratorException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
