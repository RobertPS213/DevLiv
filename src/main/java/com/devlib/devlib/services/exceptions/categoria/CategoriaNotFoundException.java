package com.devlib.devlib.services.exceptions.categoria;

import com.devlib.devlib.services.exceptions.base.ExceptionBase;

public class CategoriaNotFoundException extends ExceptionBase {

	private static final long serialVersionUID = 1L;

	public CategoriaNotFoundException(Long id) {
		super("Categoria com o ID " + id + " não encontrado");
	}
}
