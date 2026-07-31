package com.devlib.devlib.services.exceptions;

public class ExceptionBase extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public ExceptionBase(String msg) {
		super(msg);
	}
}
