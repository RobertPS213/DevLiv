package com.devlib.devlib.services.exceptions.estante;

import com.devlib.devlib.services.exceptions.base.ExceptionBaseDeletion;

public class EstanteDeletionException extends ExceptionBaseDeletion {
	private static final long serialVersionUID = 1L;

	public EstanteDeletionException(Long id) {
		super("Impossivel deletar a estante com o ID " + id + " porque está vinculado a um livro");
	}
}
