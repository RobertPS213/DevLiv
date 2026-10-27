package com.devlib.devlib.dto.response;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.devlib.devlib.entites.Livro;

public class LivroResponseDTO {

	private Long id;
	private String titulo;
	private String isbn;
	private Integer anoPublicacao;
	private Integer numeroPaginas;
	private EditoraResponseDTO editora;
	private EstanteResponseDTO estante;
	private Set<AutorResponseDTO> autores = new HashSet<>();
	private Set<CategoriaResponseDTO> categorias = new HashSet<>();
	
	public LivroResponseDTO() {
		
	}
	public LivroResponseDTO(Long id, String titulo, String isbn, Integer anoPublicacao, Integer numeroPaginas,
			EditoraResponseDTO editora, EstanteResponseDTO estante, Set<AutorResponseDTO> autores, Set<CategoriaResponseDTO> categorias) {
		this.id = id;
		this.titulo = titulo;
		this.isbn = isbn;
		this.anoPublicacao = anoPublicacao;
		this.numeroPaginas = numeroPaginas;
		this.editora = editora;
		this.estante = estante;
		this.autores = autores;
		this.categorias = categorias;
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
	public EditoraResponseDTO getEditora() {
		return editora;
	}
	public void setEditora(EditoraResponseDTO editora) {
		this.editora = editora;
	}
	public EstanteResponseDTO getEstante() {
		return estante;
	}
	public void setEstante(EstanteResponseDTO estante) {
		this.estante = estante;
	}
	public Set<AutorResponseDTO> getAutores() {
		return autores;
	}
	public void setAutores(Set<AutorResponseDTO> autores) {
		this.autores = autores;
	}
	public Set<CategoriaResponseDTO> getCategorias() {
		return categorias;
	}
	public void setCategorias(Set<CategoriaResponseDTO> categorias) {
		this.categorias = categorias;
	}
	public static LivroResponseDTO toResponseDTO(Livro livro) {
		LivroResponseDTO dto = new LivroResponseDTO();
		dto.setId(livro.getId());
		dto.setTitulo(livro.getTitulo());
		dto.setIsbn(livro.getIsbn());
		dto.setAnoPublicacao(livro.getAnoPublicacao());
		dto.setNumeroPaginas(livro.getNumeroPaginas());
		dto.setEditora(EditoraResponseDTO.toResponseDTO(livro.getEditora()));
		dto.setEstante(EstanteResponseDTO.toResponseDTO(livro.getEstante()));
		dto.setAutores(AutorResponseDTO.toResponseDTOSet(livro.getAutores()));
		dto.setCategorias(CategoriaResponseDTO.toResponseDTOSet(livro.getCategorias()));
		return dto;
	}
	public static List<LivroResponseDTO> toResponseDTOList(List<Livro> livros){
		List<LivroResponseDTO> livrosDTO = new ArrayList<>();
		for(Livro lv : livros) {
			livrosDTO.add(toResponseDTO(lv));
		}
		return livrosDTO;
	}
}
