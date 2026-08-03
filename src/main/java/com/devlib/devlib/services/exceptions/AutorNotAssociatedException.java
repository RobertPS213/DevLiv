package com.devlib.devlib.services.exceptions;

public class AutorNotAssociatedException extends ExceptionBase {
	private static final long serialVersionUID = 1L;

	public AutorNotAssociatedException() {
		super("O autor informado não está vinculado a este livro");
	}
}
