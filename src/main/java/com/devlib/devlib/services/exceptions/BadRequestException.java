package com.devlib.devlib.services.exceptions;

public class BadRequestException extends ExceptionBase{
	private static final long serialVersionUID = 1L;

	public BadRequestException() {
		super("O ID não deve ser informado no cadastro.");
	}
}
