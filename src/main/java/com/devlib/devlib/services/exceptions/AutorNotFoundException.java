package com.devlib.devlib.services.exceptions;

public class AutorNotFoundException extends ExceptionBase {
	private static final long serialVersionUID = 1L;

	public AutorNotFoundException(Long id) {
		super("Autor com o ID " + id + " não encontrado");
	}
}
