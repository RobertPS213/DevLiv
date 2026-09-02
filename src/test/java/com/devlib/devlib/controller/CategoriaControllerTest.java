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

import com.devlib.devlib.dto.insert.CategoriaInsertDTO;
import com.devlib.devlib.dto.response.CategoriaResponseDTO;
import com.devlib.devlib.dto.update.CategoriaUpdateDTO;
import com.devlib.devlib.services.CategoriaService;
import com.devlib.devlib.services.exceptions.categoria.CategoriaDeletionException;
import com.devlib.devlib.services.exceptions.categoria.CategoriaNotFoundException;

import tools.jackson.databind.ObjectMapper;

@WebMvcTest(CategoriaController.class)
public class CategoriaControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@MockitoBean
	private CategoriaService service;
	
	private CategoriaResponseDTO categoria1;
	private CategoriaResponseDTO categoria2;
	private CategoriaInsertDTO categoriaInsert;
	private CategoriaUpdateDTO categoriaUpdate;
	private List<CategoriaResponseDTO> categorias;
	
	@BeforeEach
	void setUp() {
		categoria1 = new CategoriaResponseDTO(1L, "Ficção", "Livros que apresentam histórias imaginárias ou fictícias.");
		categoria2 = new CategoriaResponseDTO(2L, "Romance", "Livros que exploram histórias de amor e relacionamentos.");
		categorias = List.of(categoria1, categoria2);
		categoriaInsert = new CategoriaInsertDTO("Ficção", "Livros que apresentam histórias imaginárias ou fictícias.");
		categoriaUpdate = new CategoriaUpdateDTO("Romance", "Livros que exploram histórias de amor e relacionamentos.");
	}
	
	@Test
	public void findAll_DeveRetornarStatus200OkComListaDeCategoria() throws Exception {
		when(service.findAll()).thenReturn(categorias);
		
		mockMvc.perform(get("/categorias"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].id").value(1))
				.andExpect(jsonPath("$[0].titulo").value("Ficção"))
				.andExpect(jsonPath("$[0].descricao").value("Livros que apresentam histórias imaginárias ou fictícias."))
				.andExpect(jsonPath("$[1].id").value(2))
				.andExpect(jsonPath("$[1].titulo").value("Romance"))
				.andExpect(jsonPath("$[1].descricao").value("Livros que exploram histórias de amor e relacionamentos."));
	}
	
	@Test
	public void findAll_DeveRetornarStatus200OkComListaDeCategoriaResponseDTOVazia_QuandoNaoHouverCategoriasAdicionadas() throws Exception {
		when(service.findAll()).thenReturn(List.of());
		
		mockMvc.perform(get("/categorias"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(0));
	}
	
	@Test
	public void findById_DeveRetornarStatus200OkComCategoriaResponseDTO_QuandoIdExistir() throws Exception {
		when(service.findById(1L)).thenReturn(categoria1);
		
		mockMvc.perform(get("/categorias/{id}", 1L))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$.titulo").value("Ficção"))
				.andExpect(jsonPath("$.descricao").value("Livros que apresentam histórias imaginárias ou fictícias."));
	}
	
	@Test
	public void findById_DeveRetornarStatus404NotfoundComCategoriaNotFoundException_QuandoIdNaoExistir() throws Exception {
		when(service.findById(99L)).thenThrow(new CategoriaNotFoundException(99L));
		
		mockMvc.perform(get("/categorias/{id}", 99L))
				.andExpect(status().isNotFound());
	}
	
	@Test
	public void insert_DeveRetornarStatus201CreatedComCategoriaResponseDTO() throws Exception {
		String jsonBody = objectMapper.writeValueAsString(categoriaInsert);
		
		when(service.insert(any(CategoriaInsertDTO.class))).thenReturn(categoria1);
		
		mockMvc.perform(post("/categorias")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonBody)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$.titulo").value("Ficção"))
				.andExpect(jsonPath("$.descricao").value("Livros que apresentam histórias imaginárias ou fictícias."));
	}
	
	@Test
	public void update_DeveRetornarStatus200OkComCategoriaResponseDTO_QuandoIdExistir() throws Exception {
		String jsonBody = objectMapper.writeValueAsString(categoriaUpdate);
		
		when(service.update(eq(2L), any(CategoriaUpdateDTO.class))).thenReturn(categoria2);
		
		mockMvc.perform(put("/categorias/{id}", 2L)
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonBody)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(2))
				.andExpect(jsonPath("$.titulo").value("Romance"))
				.andExpect(jsonPath("$.descricao").value("Livros que exploram histórias de amor e relacionamentos."));
	}
	
	@Test
	public void update_DeveRetornarStatus404NotFoundComCategoriaNotFoundException_QuandoIdNaoExistir() throws Exception {
		String jsonBody = objectMapper.writeValueAsString(categoriaUpdate);
		
		when(service.update(eq(99L), any(CategoriaUpdateDTO.class))).thenThrow(new CategoriaNotFoundException(99L));
		
		mockMvc.perform(put("/categorias/{id}", 99L)
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonBody)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}
	
	@Test
	public void delete_DeveRetornarStatus204NoContent_QuandoIdExistir() throws Exception {
		mockMvc.perform(delete("/categorias/{id}", 1L))
				.andExpect(status().isNoContent());
	}
	
	@Test
	public void delete_DeveRetornarStatus404NotfoundComCategoriaNotFoundException_QuandoIdNaoExistir() throws Exception {
		doThrow(new CategoriaNotFoundException(99L)).when(service).delete(99L);
		
		mockMvc.perform(delete("/categorias/{id}", 99L))
				.andExpect(status().isNotFound());
	}
	
	@Test
	public void delete_DeveRetornarStatus409ConflictComCategoriaDeletionException_SeCategoriaEstiverVinculadoAUmLivro() throws Exception {
		doThrow(new CategoriaDeletionException(1L)).when(service).delete(1L);
		
		mockMvc.perform(delete("/categorias/{id}", 1L))
				.andExpect(status().isConflict());
	}
}
