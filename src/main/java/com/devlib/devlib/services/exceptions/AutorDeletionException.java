package com.devlib.devlib.services.exceptions;

public class AutorDeletionException extends ExceptionBaseDeletion {
	private static final long serialVersionUID = 1L;

	public AutorDeletionException(Long id) {
		super("Impossivel deletar o autor com o ID " + id + " porque está vinculado a um livro");
	}
}
