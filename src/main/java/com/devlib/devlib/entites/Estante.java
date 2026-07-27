package com.devlib.devlib.entites;

import java.io.Serializable;
import java.util.Objects;

public class Estante implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private String codigo;
	private String localizacao;
	private Integer capacidade;
	
	public Estante() {
		
	}
	public Estante(Long id, String codigo, String localizacao, Integer capacidade) {
		this.id = id;
		this.codigo = codigo;
		this.localizacao = localizacao;
		this.capacidade = capacidade;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Estante other = (Estante) obj;
		return Objects.equals(id, other.id);
	}
}
