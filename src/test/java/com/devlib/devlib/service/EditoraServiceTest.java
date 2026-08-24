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

import com.devlib.devlib.dto.insert.EditoraInsertDTO;
import com.devlib.devlib.dto.response.EditoraResponseDTO;
import com.devlib.devlib.dto.update.EditoraUpdateDTO;
import com.devlib.devlib.entites.Editora;
import com.devlib.devlib.repositories.EditoraRepository;
import com.devlib.devlib.repositories.LivroRepository;
import com.devlib.devlib.services.EditoraService;
import com.devlib.devlib.services.exceptions.editora.EditoraDeletionException;
import com.devlib.devlib.services.exceptions.editora.EditoraNotFoundException;

@ExtendWith(MockitoExtension.class)
public class EditoraServiceTest {

	@Mock
	LivroRepository livroRepository;
	
	@Mock
	EditoraRepository repository;
	
	@InjectMocks
	EditoraService service;
	
	private Editora editora;
	private Editora editora2;
	private Long idExistente;
	private Long idInexistente;
	private List<Editora> editoras;
	private List<Editora> editorasVazia;
	private EditoraInsertDTO editoraInsert;
	private EditoraUpdateDTO editoraUpdate;
	
	@BeforeEach
	void setUp() {
		editora = new Editora(1L, "Editora Rocco Ltda.", "42.444.703/0001-59", "rocco@rocco.com.br");
		editora2 = new Editora(2L, "Editora Planeta do Brasil Ltda.", "05.764.236/0001-18", "faleconosco@editoraplaneta.com.br");
		editoraInsert = new EditoraInsertDTO("Editora Rocco Ltda.", "42.444.703/0001-59", "rocco@rocco.com.br");
		editoraUpdate = new EditoraUpdateDTO("Editora Rocco Ltda.", "42.444.703/0001-59", "rocco@rocco.com.br");
		editorasVazia = List.of();
		editoras = List.of(editora, editora2);
		idExistente = 1L;
		idInexistente = 100L;
	}
	
	@Test
	public void findAll_DeveRetornarUmaListaEditoraResponseDTO() {
		when(repository.findAll()).thenReturn(editoras);
		
		List<EditoraResponseDTO> resultado = service.findAll();
		
		assertNotNull(resultado);
		assertFalse(resultado.isEmpty());
		assertEquals(editora.getId(), resultado.get(0).getId());
		assertEquals(editora.getNome(), resultado.get(0).getNome());
		assertEquals(editora.getCnpj(), resultado.get(0).getCnpj());
		assertEquals(editora.getEmail(), resultado.get(0).getEmail());
		assertEquals(editora2.getId(), resultado.get(1).getId());
		assertEquals(editora2.getNome(), resultado.get(1).getNome());
		assertEquals(editora2.getCnpj(), resultado.get(1).getCnpj());
		assertEquals(editora2.getEmail(), resultado.get(1).getEmail());
	}
	
	@Test
	public void findAll_DeveRetornarUmaListaEditoraResponseDTOVazia_QuandoNaoExistirEditorasAdicionados() {
		when(repository.findAll()).thenReturn(editorasVazia);
		
		List<EditoraResponseDTO> resultado = service.findAll();
		
		assertNotNull(resultado);
		assertTrue(resultado.isEmpty());
	}
	
	@Test
	public void findById_DeveRetornarEditoraResponseDTO_QuandoIdExistirNoBanco() {
		when(repository.findById(idExistente)).thenReturn(Optional.of(editora));
		
		EditoraResponseDTO resultado = service.findById(idExistente);
		
		assertNotNull(resultado);
		assertEquals(editora.getId(), resultado.getId());
		assertEquals(editora.getNome(), resultado.getNome());
		assertEquals(editora.getCnpj(), resultado.getCnpj());
		assertEquals(editora.getEmail(), resultado.getEmail());
	}
	
	@Test
	public void findById_DeveRetornarEditoraNotFoundException_SeIdNaoExistirNoBanco() {
		when(repository.findById(idInexistente)).thenReturn(Optional.empty());
		
		assertThrows(EditoraNotFoundException.class, () -> {
			service.findById(idInexistente);
		});
	}
	
	@Test
	public void insert_DeveRetornarEditoraResponseDTO() {
		when(repository.save(any(Editora.class))).thenReturn(editora);
		
		EditoraResponseDTO resultado = service.insert(editoraInsert);
		
		assertNotNull(resultado);
		assertNotNull(resultado.getId());
		assertEquals(editora.getId(), resultado.getId());
		assertEquals(editora.getNome(), resultado.getNome());
		assertEquals(editora.getCnpj(), resultado.getCnpj());
		assertEquals(editora.getEmail(), resultado.getEmail());
	}
	
	@Test
	public void update_DeveRetornarEditoraResponseDTO_QuandoIdExistirNoBanco() {
		when(repository.findById(idExistente)).thenReturn(Optional.of(editora2));
		when(repository.save(any(Editora.class))).thenAnswer(invocation -> invocation.getArgument(0));
		
		EditoraResponseDTO resultado = service.update(idExistente, editoraUpdate);
		
		assertNotNull(resultado);
		assertEquals(editora2.getId(), resultado.getId());
		assertEquals(editoraUpdate.getNome(), resultado.getNome());
		assertEquals(editoraUpdate.getCnpj(), resultado.getCnpj());
		assertEquals(editoraUpdate.getEmail(), resultado.getEmail());
	}
	
	@Test
	public void update_DeveRetornarEditoraNotFoundException_SeIdNaoExistirNoBanco() {
		when(repository.findById(idInexistente)).thenReturn(Optional.empty());
		
		assertThrows(EditoraNotFoundException.class, () -> {
			service.update(idInexistente, editoraUpdate);
		});
	}
	
	@Test
	public void delete_DeveDeletarEditora_QuandoIdExistir() {
		when(repository.existsById(idExistente)).thenReturn(true);
		when(livroRepository.existsByEditoraId(idExistente)).thenReturn(false);
		doNothing().when(repository).deleteById(idExistente);
		
		assertDoesNotThrow(() -> {
			service.delete(idExistente);
		});
	}
	
	@Test
	public void delete_DeveRetornarEditoraNotFoundException_SeIdNaoExistir() {
		when(repository.existsById(idInexistente)).thenReturn(false);
		
		assertThrows(EditoraNotFoundException.class, () -> {
			service.delete(idInexistente);
		});
	}
	
	@Test
	public void delete_DeveRetornarEditoraDeletionException_SeEditoraEstiverVinculadoAUmLivro() {
		when(repository.existsById(idExistente)).thenReturn(true);
		when(livroRepository.existsByEditoraId(idExistente)).thenReturn(true);
		
		assertThrows(EditoraDeletionException.class, () -> {
			service.delete(idExistente);
		});
	}
}
