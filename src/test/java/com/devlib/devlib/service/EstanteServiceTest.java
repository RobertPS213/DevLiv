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

import com.devlib.devlib.dto.insert.EstanteInsertDTO;
import com.devlib.devlib.dto.response.EstanteResponseDTO;
import com.devlib.devlib.dto.update.EstanteUpdateDTO;
import com.devlib.devlib.entites.Estante;
import com.devlib.devlib.repositories.EstanteRepository;
import com.devlib.devlib.repositories.LivroRepository;
import com.devlib.devlib.services.EstanteService;
import com.devlib.devlib.services.exceptions.estante.EstanteDeletionException;
import com.devlib.devlib.services.exceptions.estante.EstanteNotFoundException;

@ExtendWith(MockitoExtension.class)
public class EstanteServiceTest {

	@Mock
	LivroRepository livroRepository;
	
	@Mock
	EstanteRepository repository;
	
	@InjectMocks
	EstanteService service;
	
	private Estante estante;
	private Estante estante2;
	private Long idExistente;
	private Long idInexistente;
	private List<Estante> estantes;
	private List<Estante> estantesVazia;
	private EstanteInsertDTO estanteInsert;
	private EstanteUpdateDTO estanteUpdate;
	
	@BeforeEach
	void setUp() {
		estante = new Estante(1L, "EST-001", "Biblioteca - Sala A - Parede Norte", 100);
		estante2 = new Estante(2L, "EST-002", "Biblioteca - Sala B - Parede Sul", 150);
		estanteInsert = new EstanteInsertDTO("EST-001", "Biblioteca - Sala A - Parede Norte", 100);
		estanteUpdate = new EstanteUpdateDTO("EST-001", "Biblioteca - Sala A - Parede Norte", 100);
		idExistente = 1L;
		idInexistente = 100L;
		estantesVazia = List.of();
		estantes = List.of(estante, estante2);
	}
	
	@Test
	public void findAll_DeveRetornarUmaListaEstanteResponseDTO() {
		when(repository.findAll()).thenReturn(estantes);
		
		List<EstanteResponseDTO> resultado = service.findAll();
		
		assertNotNull(resultado);
		assertFalse(resultado.isEmpty());
		assertEquals(estante.getId(), resultado.get(0).getId());
		assertEquals(estante.getCodigo(), resultado.get(0).getCodigo());
		assertEquals(estante.getLocalizacao(), resultado.get(0).getLocalizacao());
		assertEquals(estante.getCapacidade(), resultado.get(0).getCapacidade());
		assertEquals(estante2.getId(), resultado.get(1).getId());
		assertEquals(estante2.getCodigo(), resultado.get(1).getCodigo());
		assertEquals(estante2.getLocalizacao(), resultado.get(1).getLocalizacao());
		assertEquals(estante2.getCapacidade(), resultado.get(1).getCapacidade());
	}
	
	@Test
	public void findAll_DeveRetornarUmaListaEstanteResponseDTOVazia_QuandoNaoExistirEstantesAdicionados() {
		when(repository.findAll()).thenReturn(estantesVazia);
		
		List<EstanteResponseDTO> resultado = service.findAll();
		
		assertNotNull(resultado);
		assertTrue(resultado.isEmpty());
	}
	
	@Test
	public void findById_DeveRetornarEstanteResponseDTO_QuandoIdExistirNoBanco() {
		when(repository.findById(idExistente)).thenReturn(Optional.of(estante));
		
		EstanteResponseDTO resultado = service.findById(idExistente);
		
		assertNotNull(resultado);
		assertEquals(estante.getId(), resultado.getId());
		assertEquals(estante.getCodigo(), resultado.getCodigo());
		assertEquals(estante.getLocalizacao(), resultado.getLocalizacao());
		assertEquals(estante.getCapacidade(), resultado.getCapacidade());
	}
	
	@Test
	public void findById_DeveRetornarEstanteNotFoundException_SeIdNaoExistirNoBanco() {
		when(repository.findById(idInexistente)).thenReturn(Optional.empty());
		
		assertThrows(EstanteNotFoundException.class, () -> {
			service.findById(idInexistente);
		});
	}
	
	@Test
	public void insert_DeveRetornarEstanteResponseDTO() {
		when(repository.save(any(Estante.class))).thenReturn(estante);
		
		EstanteResponseDTO resultado = service.insert(estanteInsert);
		
		assertNotNull(resultado);
		assertNotNull(resultado.getId());
		assertEquals(estante.getId(), resultado.getId());
		assertEquals(estante.getCodigo(), resultado.getCodigo());
		assertEquals(estante.getLocalizacao(), resultado.getLocalizacao());
		assertEquals(estante.getCapacidade(), resultado.getCapacidade());
	}
	
	@Test
	public void update_DeveRetornarEstanteResponseDTO_QuandoIdExistirNoBanco() {
		when(repository.findById(idExistente)).thenReturn(Optional.of(estante2));
		when(repository.save(any(Estante.class))).thenAnswer(invocation -> invocation.getArgument(0));
		
		EstanteResponseDTO resultado = service.update(idExistente, estanteUpdate);
		
		assertNotNull(resultado);
		assertEquals(estante2.getId(), resultado.getId());
		assertEquals(estanteUpdate.getCodigo(), resultado.getCodigo());
		assertEquals(estante2.getLocalizacao(), resultado.getLocalizacao());
		assertEquals(estante2.getCapacidade(), resultado.getCapacidade());
	}
	
	@Test
	public void update_DeveRetornarEstanteNotFoundException_SeIdNaoExistirNoBanco() {
		when(repository.findById(idInexistente)).thenReturn(Optional.empty());
		
		assertThrows(EstanteNotFoundException.class, () -> {
			service.update(idInexistente, estanteUpdate);
		});
	}
	
	@Test
	public void delete_DeveDeletarEstante_QuandoIdExistir() {
		when(repository.existsById(idExistente)).thenReturn(true);
		when(livroRepository.existsByEstanteId(idExistente)).thenReturn(false);
		doNothing().when(repository).deleteById(idExistente);
		
		assertDoesNotThrow(() -> {
			service.delete(idExistente);
		});
	}
	
	@Test
	public void delete_DeveRetornarEstanteNotFoundException_SeIdNaoExistir() {
		when(repository.existsById(idInexistente)).thenReturn(false);
		
		assertThrows(EstanteNotFoundException.class, () -> {
			service.delete(idInexistente);
		});
	}
	
	@Test
	public void delete_DeveRetornarEstanteDeletionException_SeEstanteEstiverVinculadoAUmLivro() {
		when(repository.existsById(idExistente)).thenReturn(true);
		when(livroRepository.existsByEstanteId(idExistente)).thenReturn(true);
		
		assertThrows(EstanteDeletionException.class, () -> {
			service.delete(idExistente);
		});
	}
}
