package com.devlib.devlib.dto.response;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.devlib.devlib.entites.Autor;

public class AutorResponseDTO {
	
	private Long id;
	private String nome;
	private String nacionalidade;
	
	public AutorResponseDTO() {
		
	}
	public AutorResponseDTO(Long id, String nome, String nacionalidade) {
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
	public static AutorResponseDTO toResponseDTO(Autor autor) {
		AutorResponseDTO dto = new AutorResponseDTO();
		dto.setId(autor.getId());
		dto.setNome(autor.getNome());
		dto.setNacionalidade(autor.getNacionalidade());
		return dto;
	}
	// Necessario para repository.findAll()
	public static List<AutorResponseDTO> toResponseDTOList(List<Autor> autores){
		List<AutorResponseDTO> autoresDTO = new ArrayList<>();
		for(Autor at : autores) {
			autoresDTO.add(toResponseDTO(at));
		}
		return autoresDTO;
	}
	// Necessario para classe LivroResponseDTO
	public static Set<AutorResponseDTO> toResponseDTOSet(Set<Autor> autores){
		Set<AutorResponseDTO> autoresDTO = new HashSet<>();
		for(Autor at : autores) {
			autoresDTO.add(toResponseDTO(at));
		}
		return autoresDTO;
	}
}
