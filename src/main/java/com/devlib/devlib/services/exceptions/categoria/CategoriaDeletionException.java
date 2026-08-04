package com.devlib.devlib.services.exceptions.categoria;

import com.devlib.devlib.services.exceptions.base.ExceptionBaseDeletion;

public class CategoriaDeletionException extends ExceptionBaseDeletion {
	private static final long serialVersionUID = 1L;

	public CategoriaDeletionException(Long id) {
		super("Impossivel deletar a categoria com o ID " + id + " porque está vinculado a um livro");
	}
}
