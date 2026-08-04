package com.devlib.devlib.services.exceptions.categoria;

import com.devlib.devlib.services.exceptions.base.ExceptionBase;

public class CategoriaAlreadyAssociatedException extends ExceptionBase {
	private static final long serialVersionUID = 1L;

	public CategoriaAlreadyAssociatedException() {
		super("A categoria informada já está vinculado a este livro");
	}
}
