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

import com.devlib.devlib.dto.insert.AutorInsertDTO;
import com.devlib.devlib.dto.response.AutorResponseDTO;
import com.devlib.devlib.dto.update.AutorUpdateDTO;
import com.devlib.devlib.entites.Autor;
import com.devlib.devlib.repositories.AutorRepository;
import com.devlib.devlib.repositories.LivroRepository;
import com.devlib.devlib.services.AutorService;
import com.devlib.devlib.services.exceptions.autor.AutorDeletionException;
import com.devlib.devlib.services.exceptions.autor.AutorNotFoundException;

@ExtendWith(MockitoExtension.class)
public class AutorServiceTest {

	@Mock
	LivroRepository livroRepository;
	
	@Mock
	AutorRepository repository;
	
	@InjectMocks
	AutorService service;
	
	private Autor autor;
	private Autor autor2;
	private Long idExistente;
	private Long idInexistente;
	private AutorInsertDTO autorInsertDTO;
	private AutorUpdateDTO autorUpdateDTO;
	
	@BeforeEach
	void setUp() {
		autor = new Autor(1L, "Paulo Coelho", "Brasileiro");
		autor2 = new Autor(2L, "George Orwell", "Indiano");
		autorInsertDTO  = new AutorInsertDTO("Paulo Coelho", "Brasileiro");
		autorUpdateDTO = new AutorUpdateDTO("George Orwell", "Indiano");
		idExistente = 1L;
		idInexistente = 100L;
	}
	
	@Test
	public void findAll_DeveRetornarUmaListaDeAutorResponseDTO() {
		List<Autor> autores = List.of(autor, autor2);
		when(repository.findAll()).thenReturn(autores);
		List<AutorResponseDTO> resultado = service.findAll();
		
		assertNotNull(resultado);
		assertFalse(resultado.isEmpty());
		assertEquals(autores.size(), resultado.size());
		assertEquals("Paulo Coelho", resultado.get(0).getNome());
		assertEquals("Brasileiro", resultado.get(0).getNacionalidade());
		assertEquals("George Orwell", resultado.get(1).getNome());
		assertEquals("Indiano", resultado.get(1).getNacionalidade());
	}
	
	@Test
	public void findAll_DeveRetornarUmaListaDeAutorResponseDTOVazia_QuandoNaoExistirAutoresAdicionados() {
		List<Autor> autores = List.of();
		when(repository.findAll()).thenReturn(autores);
		List<AutorResponseDTO> resultado = service.findAll();
		
		assertNotNull(resultado);
		assertTrue(resultado.isEmpty());
	}
	
	@Test
	public void findById_DeveRetornarUmAutorResponseDTO_QuandoIdExistirNoBanco() {
		when(repository.findById(1L)).thenReturn(Optional.of(autor));
		
		AutorResponseDTO resultado = service.findById(1L);
		
		assertNotNull(resultado);
		assertEquals(autor.getId(), resultado.getId());
		assertEquals(autor.getNome(), resultado.getNome());
		assertEquals(autor.getNacionalidade(), resultado.getNacionalidade());
	}
	
	@Test
	public void findById_DeveRetornarAutorNotFoundException_SeIdNaoExistirNoBanco() {
		when(repository.findById(idInexistente)).thenReturn(Optional.empty());
		
		assertThrows(AutorNotFoundException.class, () -> {
			service.findById(idInexistente);
		});
	}
	
	@Test
	public void insert_DeveRetornarUmAutorResponseDTO() {
		when(repository.save(any(Autor.class))).thenReturn(autor);
		
		AutorResponseDTO resultado = service.insert(autorInsertDTO);
		
		assertNotNull(resultado);
		assertNotNull(resultado.getId());
	    assertEquals(autor.getId(), resultado.getId());
		assertEquals(autor.getNome(), resultado.getNome());
		assertEquals(autor.getNacionalidade(), resultado.getNacionalidade());
	}
	
	@Test
	public void update_DeveRetornarUmAutorResponseDTO_QuandoIdExistirNoBanco() {
		when(repository.findById(1L)).thenReturn(Optional.of(autor));
		when(repository.save(any(Autor.class))).thenAnswer(invocation -> invocation.getArgument(0));
		
		AutorResponseDTO resultado = service.update(1L, autorUpdateDTO);
		
		assertNotNull(resultado);
		assertEquals(autor.getId(), resultado.getId());
		assertEquals(autorUpdateDTO.getNome(), resultado.getNome());
		assertEquals(autorUpdateDTO.getNacionalidade(), resultado.getNacionalidade());
	}
	
	@Test
	public void update_DeveRetornarAutorNotFoundException_SeIdNaoExistirNoBanco() {	
		when(repository.findById(idInexistente)).thenReturn(Optional.empty());
		
		assertThrows(AutorNotFoundException.class, () -> {
			service.update(idInexistente, autorUpdateDTO);
		});
	}
	
	@Test
	public void delete_DeveDeletarAutor_QuandoIdExistir() {
		when(repository.existsById(idExistente)).thenReturn(true);
		when(livroRepository.existsByAutoresId(idExistente)).thenReturn(false);
		doNothing().when(repository).deleteById(idExistente);
		
		assertDoesNotThrow(() -> {
			service.delete(idExistente);
		});
	}
	
	@Test
	public void delete_DeveRetornarAutorNotFoundException_SeIdNaoExistir() {
		when(repository.existsById(idInexistente)).thenReturn(false);
		
		assertThrows(AutorNotFoundException.class, () -> {
			service.delete(idInexistente);
		});
	}
	
	@Test
	public void delete_DeveRetornarAutorDeletionException_SeAutorEstiverVinculadoAUmLivro() {
		when(repository.existsById(idExistente)).thenReturn(true);
		when(livroRepository.existsByAutoresId(idExistente)).thenReturn(true);
		
		assertThrows(AutorDeletionException.class, () -> {
			service.delete(idExistente);
		});
	}
}
