package com.devlib.devlib.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class LivroUpdateDTO {

	@NotBlank(message = "O titulo deve ser preenchido")
	private String titulo;
	@NotBlank(message = "O ISBN deve ser preenchido")
	private String isbn;
	@NotNull(message = "O ano da publicação não pode ser nulo")
	private Integer anoPublicacao;
	@NotNull(message = "O número de páginas não pode ser nulo")
	private Integer numeroPaginas;
	@NotNull(message = "O ID da editora não pode ser nulo")
	private Long editoraId;
	@NotNull(message = "O ID da estante não pode ser nulo")
	private Long estanteId;
	
	public LivroUpdateDTO() {
		
	}
	public LivroUpdateDTO(String titulo, String isbn, Integer anoPublicacao, Integer numeroPaginas, Long editoraId,
			Long estanteId) {
		this.titulo = titulo;
		this.isbn = isbn;
		this.anoPublicacao = anoPublicacao;
		this.numeroPaginas = numeroPaginas;
		this.editoraId = editoraId;
		this.estanteId = estanteId;
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
	public Long getEditoraId() {
		return editoraId;
	}
	public void setEditoraId(Long editoraId) {
		this.editoraId = editoraId;
	}
	public Long getEstanteId() {
		return estanteId;
	}
	public void setEstanteId(Long estanteId) {
		this.estanteId = estanteId;
	}
}
