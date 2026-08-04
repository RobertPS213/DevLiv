package com.devlib.devlib.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class EstanteUpdateDTO {

	@NotBlank(message = "O codigo deve ser preenchido")
	private String codigo;
	@NotBlank(message = "A localização deve ser preenchido")
	private String localizacao;
	@NotNull(message = "A capacidade não pode ser nula")
	private Integer capacidade;
	
	public EstanteUpdateDTO() {
		
	}
	public EstanteUpdateDTO(String codigo,String localizacao,Integer capacidade) {
		this.codigo = codigo;
		this.localizacao = localizacao;
		this.capacidade = capacidade;
	}
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getLocalizacao() {
		return localizacao;
	}
	public void setLocalizacao(String localizacao) {
		this.localizacao = localizacao;
	}
	public Integer getCapacidade() {
		return capacidade;
	}
	public void setCapacidade(Integer capacidade) {
		this.capacidade = capacidade;
	}
}
