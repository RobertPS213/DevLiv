package com.devlib.devlib.services.exceptions.editora;

import com.devlib.devlib.services.exceptions.base.ExceptionBaseDeletion;

public class EditoraDeletionException extends ExceptionBaseDeletion {
	private static final long serialVersionUID = 1L;

	public EditoraDeletionException(Long id) {
		super("Impossivel deletar a editora com o ID " + id + " porque está vinculado a um livro");
	}
}
