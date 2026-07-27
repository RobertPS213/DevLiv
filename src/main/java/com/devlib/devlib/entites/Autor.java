package com.devlib.devlib.entites;

import java.io.Serializable;
import java.util.Objects;

public class Autor implements Serializable{
	private static final long serialVersionUID = 1L;

	private Long id;
	private String nome;
	private String nacionalidade;
	
	public Autor() {
		
	}
	public Autor(Long id, String nome, String nacionalidade) {
		this.id = id;
		this.nome = nome;
		this.nacionalidade = nacionalidade;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
		Autor other = (Autor) obj;
		return Objects.equals(id, other.id);
	}
}
