package com.devlib.devlib.services.exceptions.autor;

import com.devlib.devlib.services.exceptions.base.ExceptionBase;

public class IdAutoresNotFoundException extends ExceptionBase {
	private static final long serialVersionUID = 1L;

	public IdAutoresNotFoundException() {
		super("Um ou mais IDs de autores não existem no banco de dados.");
	}
}
