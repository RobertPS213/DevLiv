package com.devlib.devlib.dto.response;

import java.util.ArrayList;
import java.util.List;

import com.devlib.devlib.entites.Categoria;

public class CategoriaResponseDTO {
	
	private Long id;
	private String titulo;
	private String descricao;
	
	public CategoriaResponseDTO() {
		
	}
	public CategoriaResponseDTO(Long id, String titulo, String descricao) {
		super();
		this.id = id;
		this.titulo = titulo;
		this.descricao = descricao;
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
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public static CategoriaResponseDTO toResponseDTO(Categoria categoria) {
		CategoriaResponseDTO dto = new CategoriaResponseDTO();
		dto.setId(categoria.getId());
		dto.setTitulo(categoria.getTitulo());
		dto.setDescricao(categoria.getDescricao());
		return dto;
	}
	public static List<CategoriaResponseDTO> toResponseDTOList(List<Categoria> categorias){
		List<CategoriaResponseDTO> categoriasDTO = new ArrayList<>();
		for(Categoria cat : categorias) {
			categoriasDTO.add(toResponseDTO(cat));
		}
		return categoriasDTO;
	}
}
