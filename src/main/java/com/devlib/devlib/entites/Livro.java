package com.devlib.devlib.entites;

import java.io.Serializable;
import java.util.Objects;

public class Livro implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private String titulo;
	private String isbn;
	private Integer anoPublicacao;
	private Integer numeroPaginas;
	
	public Livro() {
		
	}
	public Livro(Long id, String titulo, String isbn, Integer anoPublicacao, Integer numeroPaginas) {
		this.id = id;
		this.titulo = titulo;
		this.isbn = isbn;
		this.anoPublicacao = anoPublicacao;
		this.numeroPaginas = numeroPaginas;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public String getIsbn() {
		return isbn;
	}
	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}
	public Integer getAnoPublicacao() {
		return anoPublicacao;
	}
	public void setAnoPublicacao(Integer anoPublicacao) {
		this.anoPublicacao = anoPublicacao;
	}
	public Integer getNumeroPaginas() {
		return numeroPaginas;
	}
	public void setNumeroPaginas(Integer numeroPaginas) {
		this.numeroPaginas = numeroPaginas;
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
		Livro other = (Livro) obj;
		return Objects.equals(id, other.id);
	}
}
