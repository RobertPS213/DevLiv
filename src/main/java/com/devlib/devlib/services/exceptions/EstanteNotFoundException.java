package com.devlib.devlib.services.exceptions;

public class EstanteNotFoundException extends ExceptionBase {
	private static final long serialVersionUID = 1L;

	public EstanteNotFoundException(Long id) {
		super("Estante com o ID " + id + " não encontrado");
	}
}
