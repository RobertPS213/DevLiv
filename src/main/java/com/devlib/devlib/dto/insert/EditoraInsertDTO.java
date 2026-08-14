package com.devlib.devlib.dto.insert;

import jakarta.validation.constraints.NotBlank;

public class EditoraInsertDTO {

	@NotBlank(message = "O nome deve ser preenchido")
	private String nome;
	@NotBlank(message = "O CNPJ deve ser preenchido")
	private String cnpj;
	@NotBlank(message = "O email deve ser preenchido")
	private String email;
	
	public EditoraInsertDTO() {
		
	}
	public EditoraInsertDTO(String nome, String cnpj, String email) {
		this.nome = nome;
		this.cnpj = cnpj;
		this.email = email;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getCnpj() {
		return cnpj;
	}
	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
}
