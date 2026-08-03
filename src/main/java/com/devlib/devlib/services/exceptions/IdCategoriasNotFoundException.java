package com.devlib.devlib.services.exceptions;

public class IdCategoriasNotFoundException extends ExceptionBase {
	private static final long serialVersionUID = 1L;

	public IdCategoriasNotFoundException() {
		super("Um ou mais IDs de categorias não existem no banco de dados.");
	}
}
