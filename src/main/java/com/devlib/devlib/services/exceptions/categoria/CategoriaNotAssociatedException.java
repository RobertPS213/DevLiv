package com.devlib.devlib.services.exceptions.categoria;

import com.devlib.devlib.services.exceptions.base.ExceptionBase;

public class CategoriaNotAssociatedException extends ExceptionBase {
	private static final long serialVersionUID = 1L;

	public CategoriaNotAssociatedException() {
		super("A categoria informada não está vinculado a este livro");
	}
}
