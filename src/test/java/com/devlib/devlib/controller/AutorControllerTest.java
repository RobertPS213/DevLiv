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

import com.devlib.devlib.dto.insert.AutorInsertDTO;
import com.devlib.devlib.dto.response.AutorResponseDTO;
import com.devlib.devlib.dto.update.AutorUpdateDTO;
import com.devlib.devlib.services.AutorService;
import com.devlib.devlib.services.exceptions.autor.AutorDeletionException;
import com.devlib.devlib.services.exceptions.autor.AutorNotFoundException;

import tools.jackson.databind.ObjectMapper;

@WebMvcTest(AutorController.class)
public class AutorControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@MockitoBean
	private AutorService service;
	
	private AutorResponseDTO autor1;
	private AutorResponseDTO autor2;
	private AutorInsertDTO autorInsert;
	private AutorUpdateDTO autorUpdate;
	private List<AutorResponseDTO> autores;
	
	@BeforeEach
	void setUp() {
		autor1 = new AutorResponseDTO(1L, "Machado de Assis", "Brasileira");
		autor2 = new AutorResponseDTO(2L, "J.K. Rowling", "Britânica");
		autores = List.of(autor1, autor2);
		autorInsert = new AutorInsertDTO("Machado de Assis", "Brasileira");
		autorUpdate = new AutorUpdateDTO("J.K. Rowling", "Britânica");
	}
	
	@Test
	public void findAll_DeveRetornarStatus200OkComListaDeAutorResponseDTO() throws Exception {
		when(service.findAll()).thenReturn(autores);
		
		mockMvc.perform(get("/autores"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.length()").value(2))
			.andExpect(jsonPath("$[0].id").value(1))
			.andExpect(jsonPath("$[0].nome").value("Machado de Assis"))
			.andExpect(jsonPath("$[1].id").value(2))
			.andExpect(jsonPath("$[1].nome").value("J.K. Rowling"));
	}
	
	@Test
	public void findAll_DeveRetornarStatus200OkComListaDeAutorResponseDTOVazia_QuandoNaoHouverAutoresAdicionados() throws Exception {
		when(service.findAll()).thenReturn(List.of());
		
		mockMvc.perform(get("/autores"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.length()").value(0));
	}
	
	@Test
	public void findById_DeveRetornarStatus200OkComAutorResponseDTO_QuandoIdExistir() throws Exception{
		when(service.findById(1L)).thenReturn(autor1);
		
		mockMvc.perform(get("/autores/1"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value(1))
			.andExpect(jsonPath("$.nome").value("Machado de Assis"))
			.andExpect(jsonPath("$.nacionalidade").value("Brasileira"));
	}
	
	@Test
	public void findById_DeveRetornarStatus404ComAutorNotFoundException_QuandoIdNaoExistir() throws Exception {
		when(service.findById(99L)).thenThrow(new AutorNotFoundException(99L));
		
		mockMvc.perform(get("/autores/99"))
			.andExpect(status().isNotFound());
	}
	
	@Test
	public void insert_DeveRetornarStatus201CreatedComAutorResponseDTO() throws Exception {
		String jsonBody = objectMapper.writeValueAsString(autorInsert);
		
		when(service.insert(any(AutorInsertDTO.class))).thenReturn(autor1);
		
		mockMvc.perform(post("/autores")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonBody)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$.nome").value("Machado de Assis"))
				.andExpect(jsonPath("$.nacionalidade").value("Brasileira"));
	}
	
	@Test
	public void update_DeveRetornarStatus200OkComAutorResponseDTO_QuandoIdExistir() throws Exception{
		String jsonBody = objectMapper.writeValueAsString(autorUpdate);
		
		when(service.update(eq(2L), any(AutorUpdateDTO.class))).thenReturn(autor2);
		
		mockMvc.perform(put("/autores/{id}", 2L)
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonBody)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(2))
				.andExpect(jsonPath("$.nome").value("J.K. Rowling"))
				.andExpect(jsonPath("$.nacionalidade").value("Britânica"));
	}
	
	@Test
	public void update_DeveRetornarStatus404NotFoundComAutorNotFoundException_QuandoIdNaoExistir() throws Exception {
		String jsonBody = objectMapper.writeValueAsString(autorUpdate);
		
		when(service.update(eq(99L), any(AutorUpdateDTO.class))).thenThrow(new AutorNotFoundException(99L));
		
		mockMvc.perform(put("/autores/{id}", 99L)
	            .contentType(MediaType.APPLICATION_JSON) 
	            .content(jsonBody))                     
	            .andExpect(status().isNotFound());
	}
	
	@Test
	public void delete_DeveRetornarStatus204NoContent_QuandoIdExistir() throws Exception {
		mockMvc.perform(delete("/autores/{id}", 1L))
				.andExpect(status().isNoContent());
	}
	
	@Test
	public void delete_DeveRetornarStatus404NotFoundComAutorNotFoundException_QuandoIdNaoExistir() throws Exception {
		doThrow(new AutorNotFoundException(99L)).when(service).delete(99L);
		
		mockMvc.perform(delete("/autores/{id}", 99L))
				.andExpect(status().isNotFound());
	}
	
	@Test
	public void delete_DeveRetornarStatus409ConflictComAutorDeletionException_SeAutorEstiverVinculadoAUmLivro() throws Exception {
		doThrow(new AutorDeletionException(1L)).when(service).delete(1L);
		
		mockMvc.perform(delete("/autores/{id}", 1L))
				.andExpect(status().isConflict());
	}
}

