package com.devlib.devlib.services.exceptions.editora;

import com.devlib.devlib.services.exceptions.base.ExceptionBase;

public class EditoraNotFoundException extends ExceptionBase {
	private static final long serialVersionUID = 1L;

	public EditoraNotFoundException(Long id) {
		super("Editora com o ID " + id + " não encontrado");
	}
}
