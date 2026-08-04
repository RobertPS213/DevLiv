package com.devlib.devlib.services.exceptions.categoria;

import com.devlib.devlib.services.exceptions.base.ExceptionBase;

public class IdCategoriasNotFoundException extends ExceptionBase {
	private static final long serialVersionUID = 1L;

	public IdCategoriasNotFoundException() {
		super("Um ou mais IDs de categorias não existem no banco de dados.");
	}
}
