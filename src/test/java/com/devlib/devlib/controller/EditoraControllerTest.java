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

import com.devlib.devlib.dto.insert.EditoraInsertDTO;
import com.devlib.devlib.dto.response.EditoraResponseDTO;
import com.devlib.devlib.dto.update.EditoraUpdateDTO;
import com.devlib.devlib.services.EditoraService;
import com.devlib.devlib.services.exceptions.editora.EditoraDeletionException;
import com.devlib.devlib.services.exceptions.editora.EditoraNotFoundException;

import tools.jackson.databind.ObjectMapper;

@WebMvcTest(EditoraController.class)
public class EditoraControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@MockitoBean
	private EditoraService service;
	
	private EditoraResponseDTO editora1;
	private EditoraResponseDTO editora2;
	private EditoraInsertDTO editoraInsert;
	private EditoraUpdateDTO editoraUpdate;
	private List<EditoraResponseDTO> editoras;
	
	@BeforeEach
	void setUp() {
		editora1 = new EditoraResponseDTO(1L, "Companhia das Letras", "60.643.909/0001-70", "contato@companhiadasletras.com.br");
		editora2 = new EditoraResponseDTO(2L, "Editora Rocco", "33.756.216/0001-08", "contato@rocco.com.br");
		editoras = List.of(editora1, editora2);
		editoraInsert = new EditoraInsertDTO("Companhia das Letras", "60.643.909/0001-70", "contato@companhiadasletras.com.br");
		editoraUpdate = new EditoraUpdateDTO("Editora Rocco", "33.756.216/0001-08", "contato@rocco.com.br");
	}
	
	@Test
	public void findAll_DeveRetornarStatus200OkComListaDeEditora() throws Exception {
		when(service.findAll()).thenReturn(editoras);
		
		mockMvc.perform(get("/editoras"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].id").value(1))
				.andExpect(jsonPath("$[0].nome").value("Companhia das Letras"))
				.andExpect(jsonPath("$[0].cnpj").value("60.643.909/0001-70"))
				.andExpect(jsonPath("$[0].email").value("contato@companhiadasletras.com.br"))
				.andExpect(jsonPath("$[1].id").value(2))
				.andExpect(jsonPath("$[1].nome").value("Editora Rocco"))
				.andExpect(jsonPath("$[1].cnpj").value("33.756.216/0001-08"))
				.andExpect(jsonPath("$[1].email").value("contato@rocco.com.br"));
	}
	
	@Test
	public void findAll_DeveRetornarStatus200OkComListaDeEditoraResponseDTOVazia_QuandoNaoHouverEditorasAdicionadas() throws Exception {
		when(service.findAll()).thenReturn(List.of());
		
		mockMvc.perform(get("/editoras"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(0));
	}
	
	@Test
	public void findById_DeveRetornarStatus200OkComEditoraResponseDTO_QuandoIdExistir() throws Exception {
		when(service.findById(1L)).thenReturn(editora1);
		
		mockMvc.perform(get("/editoras/{id}", 1L))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$.nome").value("Companhia das Letras"))
				.andExpect(jsonPath("$.cnpj").value("60.643.909/0001-70"))
				.andExpect(jsonPath("$.email").value("contato@companhiadasletras.com.br"));
	}
	
	@Test
	public void findById_DeveRetornarStatus404NotfoundComEditoraNotFoundException_QuandoIdNaoExistir() throws Exception {
		when(service.findById(99L)).thenThrow(new EditoraNotFoundException(99L));
		
		mockMvc.perform(get("/editoras/{id}", 99L))
				.andExpect(status().isNotFound());
	}
	
	@Test
	public void insert_DeveRetornarStatus201CreatedComEditoraResponseDTO() throws Exception {
		String jsonBody = objectMapper.writeValueAsString(editoraInsert);
		
		when(service.insert(any(EditoraInsertDTO.class))).thenReturn(editora1);
		
		mockMvc.perform(post("/editoras")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonBody)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$.nome").value("Companhia das Letras"))
				.andExpect(jsonPath("$.cnpj").value("60.643.909/0001-70"))
				.andExpect(jsonPath("$.email").value("contato@companhiadasletras.com.br"));
	}
	
	@Test
	public void update_DeveRetornarStatus200OkComEditoraResponseDTO_QuandoIdExistir() throws Exception {
		String jsonBody = objectMapper.writeValueAsString(editoraUpdate);
		
		when(service.update(eq(2L), any(EditoraUpdateDTO.class))).thenReturn(editora2);
		
		mockMvc.perform(put("/editoras/{id}", 2L)
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonBody)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(2))
				.andExpect(jsonPath("$.nome").value("Editora Rocco"))
				.andExpect(jsonPath("$.cnpj").value("33.756.216/0001-08"))
				.andExpect(jsonPath("$.email").value("contato@rocco.com.br"));
	}
	
	@Test
	public void update_DeveRetornarStatus404NotFoundComEditoraNotFoundException_QuandoIdNaoExistir() throws Exception {
		String jsonBody = objectMapper.writeValueAsString(editoraUpdate);
		
		when(service.update(eq(99L), any(EditoraUpdateDTO.class))).thenThrow(new EditoraNotFoundException(99L));
		
		mockMvc.perform(put("/editoras/{id}", 99L)
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonBody)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}
	
	@Test
	public void delete_DeveRetornarStatus204NoContent_QuandoIdExistir() throws Exception {
		mockMvc.perform(delete("/editoras/{id}", 1L))
				.andExpect(status().isNoContent());
	}
	
	@Test
	public void delete_DeveRetornarStatus404NotfoundComEditoraNotFoundException_QuandoIdNaoExistir() throws Exception {
		doThrow(new EditoraNotFoundException(99L)).when(service).delete(99L);
		
		mockMvc.perform(delete("/editoras/{id}", 99L))
				.andExpect(status().isNotFound());
	}
	
	@Test
	public void delete_DeveRetornarStatus409ConflictComEditoraDeletionException_SeEditoraEstiverVinculadaAUmLivro() throws Exception {
		doThrow(new EditoraDeletionException(1L)).when(service).delete(1L);
		
		mockMvc.perform(delete("/editoras/{id}", 1L))
				.andExpect(status().isConflict());
	}
}