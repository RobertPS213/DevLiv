package com.devlib.devlib.services.exceptions.livro;

import com.devlib.devlib.services.exceptions.base.ExceptionBase;

public class LivroNotFoundException extends ExceptionBase {
	private static final long serialVersionUID = 1L;

	public LivroNotFoundException(Long id) {
		super("Livro com o ID " + id + " não encontrado");
	}
}
