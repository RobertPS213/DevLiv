package com.devlib.devlib.services.exceptions.autor;

import com.devlib.devlib.services.exceptions.base.ExceptionBase;

public class AutorAlreadyAssociatedException extends ExceptionBase {
	private static final long serialVersionUID = 1L;

	public AutorAlreadyAssociatedException(Long id) {
		super("O autor informado já está vinculado a este livro");
	}
}
