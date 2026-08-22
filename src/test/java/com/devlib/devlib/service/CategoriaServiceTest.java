package com.devlib.devlib.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.devlib.devlib.dto.insert.CategoriaInsertDTO;
import com.devlib.devlib.dto.response.CategoriaResponseDTO;
import com.devlib.devlib.dto.update.CategoriaUpdateDTO;
import com.devlib.devlib.entites.Categoria;
import com.devlib.devlib.repositories.CategoriaRepository;
import com.devlib.devlib.repositories.LivroRepository;
import com.devlib.devlib.services.CategoriaService;
import com.devlib.devlib.services.exceptions.categoria.CategoriaDeletionException;
import com.devlib.devlib.services.exceptions.categoria.CategoriaNotFoundException;

@ExtendWith(MockitoExtension.class)
public class CategoriaServiceTest {

	@Mock
	LivroRepository livroRepository;
	
	@Mock
	CategoriaRepository repository;
	
	@InjectMocks
	CategoriaService service;
	
	private Long idExistente;
	private Long idInexistente;
	private Categoria categoria;
	private Categoria categoria2;
	private List<Categoria> categorias;
	private List<Categoria> categoriasVazia;
	private CategoriaInsertDTO categoriaInsert;
	private CategoriaUpdateDTO categoriaUpdate;
	
	@BeforeEach
	void setUp() {
		categoria = new Categoria(1L, "Terror", "Obras criadas para provocar medo, tensão e suspense.");
		categoria2 = new Categoria(2L, "Ficção científica", "Histórias baseadas em ciência, tecnologia, espaço ou futuros possíveis.");
		categoriaInsert = new CategoriaInsertDTO("Terror", "Obras criadas para provocar medo, tensão e suspense.");
		categoriaUpdate = new CategoriaUpdateDTO("Terror", "Obras criadas para provocar medo, tensão e suspense.");
		idExistente = 1L;
		idInexistente = 100L;
		categoriasVazia = List.of();
		categorias = List.of(categoria, categoria2);
	}
	
	@Test
	public void findAll_DeveRetornarUmaListaCategoriaResponseDTO() {
		when(repository.findAll()).thenReturn(categorias);
		
		List<CategoriaResponseDTO> resultado = service.findAll();
		
		assertNotNull(resultado);
		assertFalse(resultado.isEmpty());
		assertEquals(categoria.getId(), resultado.get(0).getId());
		assertEquals(categoria.getTitulo(), resultado.get(0).getTitulo());
		assertEquals(categoria.getDescricao(), resultado.get(0).getDescricao());
		assertEquals(categoria2.getId(), resultado.get(1).getId());
		assertEquals(categoria2.getTitulo(), resultado.get(1).getTitulo());
		assertEquals(categoria2.getDescricao(), resultado.get(1).getDescricao());
	}
	
	@Test
	public void findAll_DeveRetornarUmaListaCategoriaResponseDTOVazia_QuandoNaoExistirCategoriasAdicionados() {
		when(repository.findAll()).thenReturn(categoriasVazia);
		
		List<CategoriaResponseDTO> resultado = service.findAll();
		
		assertNotNull(resultado);
		assertTrue(resultado.isEmpty());
	}
	
	@Test
	public void findById_DeveRetornarCategoriaResponseDTO_QuandoIdExistirNoBanco() {
		when(repository.findById(1L)).thenReturn(Optional.of(categoria));
		
		CategoriaResponseDTO resultado = service.findById(1L);
		
		assertNotNull(resultado);
		assertEquals(categoria.getId(), resultado.getId());
		assertEquals(categoria.getTitulo(), resultado.getTitulo());
		assertEquals(categoria.getDescricao(), resultado.getDescricao());
	}
	
	@Test
	public void findById_DeveRetornarCategoriaNotFoundException_SeIdNaoExistirNoBanco() {
		when(repository.findById(idInexistente)).thenReturn(Optional.empty());
		
		assertThrows(CategoriaNotFoundException.class, () -> {
			service.findById(idInexistente);
		});
	}
	
	@Test
	public void insert_DeveRetornarCategoriaResponseDTO() {
		when(repository.save(any(Categoria.class))).thenReturn(categoria);
		
		CategoriaResponseDTO resultado = service.insert(categoriaInsert);
		
		assertNotNull(resultado);
		assertNotNull(resultado.getId());
		assertEquals(categoria.getId(), resultado.getId());
		assertEquals(categoria.getTitulo(), resultado.getTitulo());
		assertEquals(categoria.getDescricao(), resultado.getDescricao());
	}
	
	@Test
	public void update_DeveRetornarCategoriaResponseDTO_QuandoIdExistirNoBanco() {
		when(repository.findById(idExistente)).thenReturn(Optional.of(categoria2));
		when(repository.save(any(Categoria.class))).thenAnswer(invocation -> invocation.getArgument(0));
		
		CategoriaResponseDTO resultado = service.update(idExistente, categoriaUpdate);
		
		assertNotNull(resultado);
		assertEquals(categoria2.getId(), resultado.getId());
		assertEquals(categoriaUpdate.getTitulo(), resultado.getTitulo());
		assertEquals(categoriaUpdate.getDescricao(), resultado.getDescricao());
	}
	
	@Test
	public void update_DeveRetornarCategoriaNotFoundException_SeIdNaoExistirNoBanco() {
		when(repository.findById(idInexistente)).thenReturn(Optional.empty());
		
		assertThrows(CategoriaNotFoundException.class, () -> {
			service.update(idInexistente, categoriaUpdate);
		});
	}
	
	@Test
	public void delete_DeveDeletarCategoria_QuandoIdExistir() {
		when(repository.existsById(idExistente)).thenReturn(true);
		when(livroRepository.existsByCategoriasId(idExistente)).thenReturn(false);
		doNothing().when(repository).deleteById(idExistente);
		
		assertDoesNotThrow(() -> {
			service.delete(idExistente);
		});
	}
	
	@Test
	public void delete_DeveRetornarCategoriaNotFoundException_SeIdNaoExistir() {
		when(repository.existsById(idInexistente)).thenReturn(false);
		
		assertThrows(CategoriaNotFoundException.class, () -> {
			service.delete(idInexistente);
		});
	}
	
	@Test
	public void delete_DeveRetornarCategoriaDeletionException_SeCategoriaEstiverVinculadoAUmLivro() {
		when(repository.existsById(idExistente)).thenReturn(true);
		when(livroRepository.existsByCategoriasId(idExistente)).thenReturn(true);
		
		assertThrows(CategoriaDeletionException.class, () -> {
			service.delete(idExistente);
		});
	}
}
