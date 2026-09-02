package com.devlib.devlib.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.devlib.devlib.dto.insert.EstanteInsertDTO;
import com.devlib.devlib.dto.response.EstanteResponseDTO;
import com.devlib.devlib.dto.update.EstanteUpdateDTO;
import com.devlib.devlib.services.EstanteService;
import com.devlib.devlib.services.exceptions.estante.EstanteDeletionException;
import com.devlib.devlib.services.exceptions.estante.EstanteNotFoundException;

import tools.jackson.databind.ObjectMapper;

@WebMvcTest(EstanteController.class)
public class EstanteControllerTest {
 
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@MockitoBean
	private EstanteService service;
	
	private EstanteResponseDTO estante1;
	private EstanteResponseDTO estante2;
	private EstanteInsertDTO estanteInsert;
	private EstanteUpdateDTO estanteUpdate;
	private List<EstanteResponseDTO> estantes;
	
	@BeforeEach
	void setUp() {
		estante1 = new EstanteResponseDTO(1L, "A-01", "Corredor 1, Prateleira A", 50);
		estante2 = new EstanteResponseDTO(2L, "B-02", "Corredor 2, Prateleira B", 80);
		estantes = List.of(estante1, estante2);
		estanteInsert = new EstanteInsertDTO("A-01", "Corredor 1, Prateleira A", 50);
		estanteUpdate = new EstanteUpdateDTO("B-02", "Corredor 2, Prateleira B", 80);
	}
	
	@Test
	public void findAll_DeveRetornarStatus200OkComListaDeEstante() throws Exception {
		when(service.findAll()).thenReturn(estantes);
		
		mockMvc.perform(get("/estantes"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].id").value(1))
				.andExpect(jsonPath("$[0].codigo").value("A-01"))
				.andExpect(jsonPath("$[0].localizacao").value("Corredor 1, Prateleira A"))
				.andExpect(jsonPath("$[0].capacidade").value(50))
				.andExpect(jsonPath("$[1].id").value(2))
				.andExpect(jsonPath("$[1].codigo").value("B-02"))
				.andExpect(jsonPath("$[1].localizacao").value("Corredor 2, Prateleira B"))
				.andExpect(jsonPath("$[1].capacidade").value(80));
	}
	
	@Test
	public void findAll_DeveRetornarStatus200OkComListaDeEstanteResponseDTOVazia_QuandoNaoHouverEstantesAdicionadas() throws Exception {
		when(service.findAll()).thenReturn(List.of());
		
		mockMvc.perform(get("/estantes"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(0));
	}
	
	@Test
	public void findById_DeveRetornarStatus200OkComEstanteResponseDTO_QuandoIdExistir() throws Exception {
		when(service.findById(1L)).thenReturn(estante1);
		
		mockMvc.perform(get("/estantes/{id}", 1L))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$.codigo").value("A-01"))
				.andExpect(jsonPath("$.localizacao").value("Corredor 1, Prateleira A"))
				.andExpect(jsonPath("$.capacidade").value(50));
	}
	
	@Test
	public void findById_DeveRetornarStatus404NotfoundComEstanteNotFoundException_QuandoIdNaoExistir() throws Exception {
		when(service.findById(99L)).thenThrow(new EstanteNotFoundException(99L));
		
		mockMvc.perform(get("/estantes/{id}", 99L))
				.andExpect(status().isNotFound());
	}
	
	@Test
	public void insert_DeveRetornarStatus201CreatedComEstanteResponseDTO() throws Exception {
		String jsonBody = objectMapper.writeValueAsString(estanteInsert);
		
		when(service.insert(any(EstanteInsertDTO.class))).thenReturn(estante1);
		
		mockMvc.perform(post("/estantes")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonBody)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$.codigo").value("A-01"))
				.andExpect(jsonPath("$.localizacao").value("Corredor 1, Prateleira A"))
				.andExpect(jsonPath("$.capacidade").value(50));
	}
	
	@Test
	public void update_DeveRetornarStatus200OkComEstanteResponseDTO_QuandoIdExistir() throws Exception {
		String jsonBody = objectMapper.writeValueAsString(estanteUpdate);
		
		when(service.update(eq(2L), any(EstanteUpdateDTO.class))).thenReturn(estante2);
		
		mockMvc.perform(put("/estantes/{id}", 2L)
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonBody)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(2))
				.andExpect(jsonPath("$.codigo").value("B-02"))
				.andExpect(jsonPath("$.localizacao").value("Corredor 2, Prateleira B"))
				.andExpect(jsonPath("$.capacidade").value(80));
	}
	
	@Test
	public void update_DeveRetornarStatus404NotFoundComEstanteNotFoundException_QuandoIdNaoExistir() throws Exception {
		String jsonBody = objectMapper.writeValueAsString(estanteUpdate);
		
		when(service.update(eq(99L), any(EstanteUpdateDTO.class))).thenThrow(new EstanteNotFoundException(99L));
		
		mockMvc.perform(put("/estantes/{id}", 99L)
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonBody)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}
	
	@Test
	public void delete_DeveRetornarStatus204NoContent_QuandoIdExistir() throws Exception {
		mockMvc.perform(delete("/estantes/{id}", 1L))
				.andExpect(status().isNoContent());
	}
	
	@Test
	public void delete_DeveRetornarStatus404NotfoundComEstanteNotFoundException_QuandoIdNaoExistir() throws Exception {
		doThrow(new EstanteNotFoundException(99L)).when(service).delete(99L);
		
		mockMvc.perform(delete("/estantes/{id}", 99L))
				.andExpect(status().isNotFound());
	}
	
	@Test
	public void delete_DeveRetornarStatus409ConflictComEstanteDeletionException_SeEstanteEstiverVinculadaAUmLivro() throws Exception {
		doThrow(new EstanteDeletionException(1L)).when(service).delete(1L);
		
		mockMvc.perform(delete("/estantes/{id}", 1L))
				.andExpect(status().isConflict());
	}
}
 
