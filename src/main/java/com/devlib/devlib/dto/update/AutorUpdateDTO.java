package com.devlib.devlib.dto.update;

import jakarta.validation.constraints.NotBlank;

public class AutorUpdateDTO {

	@NotBlank(message = "O nome deve ser preenchido")
	private String nome;
	@NotBlank(message = "A nacionalidade deve ser preenchida")
	private String nacionalidade;
	
	public AutorUpdateDTO() {
		
	}
	public AutorUpdateDTO(String nome, String nacionalidade) {
		this.nome = nome;
		this.nacionalidade = nacionalidade;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getNacionalidade() {
		return nacionalidade;
	}
	public void setNacionalidade(String nacionalidade) {
		this.nacionalidade = nacionalidade;
	}
}
