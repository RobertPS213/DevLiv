package com.devlib.devlib.dto.response;

import java.util.ArrayList;
import java.util.List;

import com.devlib.devlib.entites.Editora;

public class EditoraResponseDTO {
	
	private Long id;
	private String nome;
	private String cnpj;
	private String email;
	
	public EditoraResponseDTO() {
		
	}

	public EditoraResponseDTO(Long id, String nome, String cnpj, String email) {
		super();
		this.id = id;
		this.nome = nome;
		this.cnpj = cnpj;
		this.email = email;
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
	public static EditoraResponseDTO toResponseDTO(Editora editora) {
		EditoraResponseDTO dto = new EditoraResponseDTO();
		dto.setId(editora.getId());
		dto.setNome(editora.getNome());
		dto.setCnpj(editora.getCnpj());
		dto.setEmail(editora.getEmail());
		return dto;
	}
	public static List<EditoraResponseDTO> toResponseDTOList(List<Editora> editoras){
		List<EditoraResponseDTO> editorasDTO = new ArrayList<>();
		for(Editora ed : editoras) {
			editorasDTO.add(toResponseDTO(ed));
		}
		return editorasDTO;
	}
}
