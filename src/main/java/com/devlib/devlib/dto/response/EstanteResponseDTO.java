package com.devlib.devlib.dto.response;

import java.util.ArrayList;
import java.util.List;

import com.devlib.devlib.entites.Estante;

public class EstanteResponseDTO {

	private Long id;
	private String codigo;
	private String localizacao;
	private Integer capacidade;
	
	public EstanteResponseDTO() {
		
	}
	public EstanteResponseDTO(Long id, String codigo, String localizacao, Integer capacidade) {
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
	public static EstanteResponseDTO toResponseDTO(Estante estante) {
		EstanteResponseDTO dto = new EstanteResponseDTO();
		dto.setId(estante.getId());
		dto.setCodigo(estante.getCodigo());
		dto.setLocalizacao(estante.getLocalizacao());
		dto.setCapacidade(estante.getCapacidade());
		return dto;
	}
	public static List<EstanteResponseDTO> toResponseDTOList(List<Estante> estantes){
		List<EstanteResponseDTO> estanteDTO = new ArrayList<>();
		for(Estante et : estantes) {
			estanteDTO.add(toResponseDTO(et));
		}
		return estanteDTO;
	}
}
