package com.devlib.devlib.dto.update;

import jakarta.validation.constraints.NotBlank;

public class CategoriaUpdateDTO {
	
	@NotBlank(message = "O título deve ser preenchido")
	private String titulo;
	@NotBlank(message = "A descrição deve ser preenchida")
	private String descricao;
	
	public CategoriaUpdateDTO() {
		
	}
	public CategoriaUpdateDTO(String titulo, String descricao) {
		this.titulo = titulo;
		this.descricao = descricao;
	}
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}
