package com.devlib.devlib.services.exceptions;

public class CategoriaNotFoundException extends ExceptionBase {

	private static final long serialVersionUID = 1L;

	public CategoriaNotFoundException(Long id) {
		super("Categoria com o ID " + id + " não encontrado");
	}
}
