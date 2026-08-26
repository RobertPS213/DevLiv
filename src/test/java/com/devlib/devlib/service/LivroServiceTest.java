package com.devlib.devlib.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.devlib.devlib.dto.insert.LivroInsertDTO;
import com.devlib.devlib.dto.response.LivroResponseDTO;
import com.devlib.devlib.dto.update.LivroUpdateDTO;
import com.devlib.devlib.entites.Autor;
import com.devlib.devlib.entites.Categoria;
import com.devlib.devlib.entites.Editora;
import com.devlib.devlib.entites.Estante;
import com.devlib.devlib.entites.Livro;
import com.devlib.devlib.repositories.AutorRepository;
import com.devlib.devlib.repositories.CategoriaRepository;
import com.devlib.devlib.repositories.EditoraRepository;
import com.devlib.devlib.repositories.EstanteRepository;
import com.devlib.devlib.repositories.LivroRepository;
import com.devlib.devlib.services.LivroService;
import com.devlib.devlib.services.exceptions.autor.AutorAlreadyAssociatedException;
import com.devlib.devlib.services.exceptions.autor.AutorNotAssociatedException;
import com.devlib.devlib.services.exceptions.autor.AutorNotFoundException;
import com.devlib.devlib.services.exceptions.autor.IdAutoresNotFoundException;
import com.devlib.devlib.services.exceptions.categoria.CategoriaAlreadyAssociatedException;
import com.devlib.devlib.services.exceptions.categoria.CategoriaNotAssociatedException;
import com.devlib.devlib.services.exceptions.categoria.CategoriaNotFoundException;
import com.devlib.devlib.services.exceptions.categoria.IdCategoriasNotFoundException;
import com.devlib.devlib.services.exceptions.editora.EditoraNotFoundException;
import com.devlib.devlib.services.exceptions.estante.EstanteNotFoundException;
import com.devlib.devlib.services.exceptions.livro.LivroNotFoundException;

@ExtendWith(MockitoExtension.class)
public class LivroServiceTest {

	@Mock
	EditoraRepository editoraRepository;
	
	@Mock
	EstanteRepository estanteRepository;
	
	@Mock
	AutorRepository autorRepository;
	
	@Mock
	CategoriaRepository categoriaRepository;
	
	@Mock
	LivroRepository repository;
	
	@InjectMocks
	LivroService service;
	
	private Autor autor;
	private Autor autor2;
	private Livro livro;
	private Livro livro2;
	private Editora editora;
	private Estante estante;
	private Categoria categoria;
	private Categoria categoria2;
	private List<Livro> livros;
	private List<Livro> livrosVazio;
	private List<Autor> autores;
	private List<Categoria> categorias;
	private Long idExistente;
	private Long idInexistente;
	private LivroInsertDTO livroInsert;
	private LivroUpdateDTO livroUpdate;
	
	@BeforeEach
	void setUp() {
		// simulando ID´s
		idExistente = 1L;
		idInexistente = 100L;
		
		// complemento para o construtor Livro
		editora = new Editora(1L, "Editora Rocco Ltda.", "42.444.703/0001-59", "rocco@rocco.com.br");
		estante = new Estante(1L, "EST-001", "Biblioteca - Sala A - Parede Norte", 100);
		autor = new Autor(1L, "Paulo Coelho", "Brasileiro");
		categoria = new Categoria(1L, "Terror", "Obras criadas para provocar medo, tensão e suspense.");
		
		// construtores de Livro
		livro = new Livro(1L, "O Hobbit", "9788595084742", 1937, 336, editora, estante, new HashSet<>(Set.of(autor)), new HashSet<>(Set.of(categoria)));
		livro2 = new Livro(2L, "O Silmarillion", "9788533613409", 1977, 480, editora, estante, Set.of(autor), Set.of(categoria));
		
		// findAll
		livros = List.of(livro, livro2);
		livrosVazio = List.of();
		
		// insert
		livroInsert = new LivroInsertDTO("O Hobbit", "9788595084742", 1937, 336, 1L, 1L, Set.of(1L), Set.of(1L));
		autores = List.of(autor);
		categorias = List.of(categoria);
		
		// update
		livroUpdate = new LivroUpdateDTO("O Hobbit", "9788595084742", 1937, 336, 1L, 1L);
		
		// adicionarAutor
		autor2 =  new Autor(2L, "J.K. Rowling", "Britânica");
		
		// adicionarCategoria
		categoria2 = new Categoria(2L, "Romance", "Livros que abordam histórias de amor e relacionamentos");
	}

	@Test
	public void findAll_DeveRetornarUmaListaLivroResponseDTO() {
		when(repository.findAll()).thenReturn(livros);
		
		List<LivroResponseDTO> resultado = service.findAll();
		
		assertListLivroEqualsDTO(resultado);
	}
	
	@Test
	public void findAll_DeveRetornarUmaListaLivroResponseDTOVazia_QuandoNaoExistirLivrosAdicionados() {
		when(repository.findAll()).thenReturn(livrosVazio);
		
		List<LivroResponseDTO> resultado = service.findAll();
		
		assertNotNull(resultado);
		assertTrue(resultado.isEmpty());
	}
	
	@Test
	public void findById_DeveRetornarLivroResponseDTO_QuandoIdExistirNoBanco() {
		when(repository.findById(idExistente)).thenReturn(Optional.of(livro));
		
		LivroResponseDTO resultado = service.findById(idExistente);
		
		assertLivroEqualsDTO(resultado);
	}
	
	@Test
	public void findById_DeveRetornarLivroNotFoundException_SeIdNaoExistirNoBanco() {
		when(repository.findById(idInexistente)).thenReturn(Optional.empty());
		
		assertThrows(LivroNotFoundException.class, () -> {
			service.findById(idInexistente);
		});
	}
	
	@Test
	public void insert_DeveRetornarLivroResponseDTO() {
		when(editoraRepository.findById(idExistente)).thenReturn(Optional.of(editora));
		when(estanteRepository.findById(idExistente)).thenReturn(Optional.of(estante));
		when(autorRepository.findAllById(anySet())).thenReturn(autores);
		when(categoriaRepository.findAllById(anySet())).thenReturn(categorias);
		when(repository.save(any(Livro.class))).thenReturn(livro);
		
		LivroResponseDTO resultado = service.insert(livroInsert);
		
		assertLivroEqualsDTO(resultado);
	}
	
	@Test
	public void insert_DeveRetornarIdAutoresNotFoundException_SeQtdIdsDivergirDaListaAutores() {
		when(editoraRepository.findById(idExistente)).thenReturn(Optional.of(editora));
		when(estanteRepository.findById(idExistente)).thenReturn(Optional.of(estante));
		when(autorRepository.findAllById(anySet())).thenReturn(List.of());
		
		assertThrows(IdAutoresNotFoundException.class, () -> {
			service.insert(livroInsert);
		});
	}
	
	@Test
	public void insert_DeveRetornarIdCategoriasNotFoundException_SeQtdIdsDivergirDaListaCategorias() {
		when(editoraRepository.findById(idExistente)).thenReturn(Optional.of(editora));
		when(estanteRepository.findById(idExistente)).thenReturn(Optional.of(estante));
		when(autorRepository.findAllById(anySet())).thenReturn(autores);
		when(categoriaRepository.findAllById(anySet())).thenReturn(List.of());
		
		assertThrows(IdCategoriasNotFoundException.class, () -> {
			service.insert(livroInsert);
		});
	}
	
	@Test
	public void update_DeveRetornarLivroResponseDTO_QuandoIdsExistiremNoBanco() {
		when(repository.findById(idExistente)).thenReturn(Optional.of(livro2));
		when(editoraRepository.findById(idExistente)).thenReturn(Optional.of(editora));
		when(estanteRepository.findById(idExistente)).thenReturn(Optional.of(estante));
		when(repository.save(any(Livro.class))).thenAnswer(invocation -> invocation.getArgument(0));
		
		LivroResponseDTO resultado = service.update(idExistente, livroUpdate);
		
		assertNotNull(resultado);
		assertEquals(livro2.getId(), resultado.getId());
		assertEquals(livro.getTitulo(), resultado.getTitulo());
		assertEquals(livro.getIsbn(), resultado.getIsbn());
		assertEquals(livro.getAnoPublicacao(), resultado.getAnoPublicacao());
		assertEquals(livro.getNumeroPaginas(), resultado.getNumeroPaginas());
		assertEquals(livro.getEditora(), resultado.getEditora());
		assertEquals(livro.getEstante(), resultado.getEstante());
	}
	
	@Test
	public void update_DeveRetornarLivroNotFoundException_SeIdNaoExistirNoBanco() {
		when(repository.findById(idInexistente)).thenReturn(Optional.empty());
		
		assertThrows(LivroNotFoundException.class, () -> {
			service.update(idInexistente, livroUpdate);
		});
	}
	
	@Test
	public void update_DeveRetornarEditoraNotFoundException_SeIdNaoExistirNoBanco() {
		when(repository.findById(idExistente)).thenReturn(Optional.of(livro));
		when(editoraRepository.findById(1L)).thenReturn(Optional.empty());
		
		assertThrows(EditoraNotFoundException.class, () -> {
			service.update(idExistente, livroUpdate);
		});
	}
	
	@Test
	public void update_DeveRetornarEstanteNotFoundException_SeIdNaoExistirNoBanco() {
		when(repository.findById(idExistente)).thenReturn(Optional.of(livro));
		when(editoraRepository.findById(1L)).thenReturn(Optional.of(editora));
		when(estanteRepository.findById(1L)).thenReturn(Optional.empty());
		
		assertThrows(EstanteNotFoundException.class, () -> {
			service.update(idExistente, livroUpdate);
		});
	}
	
	@Test
	public void adicionarAutor_DeveRetornarLivroResponseDTO_QuandoOsIdsExistiremNoBanco() {
		when(repository.findById(idExistente)).thenReturn(Optional.of(livro));
		when(autorRepository.findById(2L)).thenReturn(Optional.of(autor2));
		when(repository.save(any(Livro.class))).thenAnswer(invocation -> invocation.getArgument(0));
		
		LivroResponseDTO resultado = service.adicionarAutor(idExistente, 2L);
		
		assertNotNull(resultado);
		assertEquals(livro.getId(), resultado.getId());
		assertEquals(livro.getTitulo(), resultado.getTitulo());
		assertEquals(livro.getIsbn(), resultado.getIsbn());
		assertEquals(livro.getAnoPublicacao(), resultado.getAnoPublicacao());
		assertEquals(livro.getNumeroPaginas(), resultado.getNumeroPaginas());
		assertEquals(livro.getEditora(), resultado.getEditora());
		assertEquals(livro.getEstante(), resultado.getEstante());
		for(Autor autor : resultado.getAutores()) {
			if(autor.getId() == 2L) {
				assertEquals(autor2.getId(), autor.getId());
				assertEquals(autor2.getNome(), autor.getNome());
				assertEquals(autor2.getNacionalidade(), autor.getNacionalidade());
			}
		}
		assertEquals(livro.getCategorias(), resultado.getCategorias());
	}
	
	@Test
	public void adicionarAutor_DeveRetornarLivroNotFoundException_SeIdNaoExistirNoBanco() {
		when(repository.findById(idInexistente)).thenReturn(Optional.empty());
		
		assertThrows(LivroNotFoundException.class, () -> {
			service.adicionarAutor(idInexistente, idExistente);
		});
	}
	
	@Test
	public void adicionarAutor_DeveRetornarAutorNotFoundException_SeIdNaoExistirNoBanco() {
		when(repository.findById(idExistente)).thenReturn(Optional.of(livro));
		when(autorRepository.findById(idInexistente)).thenReturn(Optional.empty());
		
		assertThrows(AutorNotFoundException.class, () -> {
			service.adicionarAutor(idExistente, idInexistente);
		});
	}
	
	@Test
	public void adicionarAutor_DeveRetornarAutorAlreadyAssociatedException_SeAutorJaEstaVinculadoAoLivro() {
		when(repository.findById(idExistente)).thenReturn(Optional.of(livro));
		when(autorRepository.findById(idExistente)).thenReturn(Optional.of(autor));
		
		assertThrows(AutorAlreadyAssociatedException.class, () -> {
			service.adicionarAutor(idExistente, idExistente);
		});
	}
	
	@Test
	public void removerAutor_DeveRetornarLivroResponseDTO_QuandoOsIdsExistiremNoBanco() {
		when(repository.findById(idExistente)).thenReturn(Optional.of(livro));
		when(autorRepository.findById(idExistente)).thenReturn(Optional.of(autor));
		when(repository.save(any(Livro.class))).thenAnswer(invocation -> invocation.getArgument(0));
		
		LivroResponseDTO resultado = service.removerAutor(idExistente, idExistente);
		
		assertNotNull(resultado);
		assertEquals(livro.getId(), resultado.getId());
		assertEquals(livro.getTitulo(), resultado.getTitulo());
		assertEquals(livro.getIsbn(), resultado.getIsbn());
		assertEquals(livro.getAnoPublicacao(), resultado.getAnoPublicacao());
		assertEquals(livro.getNumeroPaginas(), resultado.getNumeroPaginas());
		assertEquals(livro.getEditora(), resultado.getEditora());
		assertEquals(livro.getEstante(), resultado.getEstante());
		assertTrue(resultado.getAutores().isEmpty());
		assertEquals(livro.getCategorias(), resultado.getCategorias());
	}
	
	@Test
	public void removerAutor_DeveRetornarLivroNotFoundException_SeIdNaoExistirNoBanco() {
		when(repository.findById(idInexistente)).thenReturn(Optional.empty());
		
		assertThrows(LivroNotFoundException.class, () -> {
			service.removerAutor(idInexistente, idExistente);
		});
	}
	
	@Test
	public void removerAutor_DeveRetornarAutorNotFoundException_SeIdNaoExistirNoBanco() {
		when(repository.findById(idExistente)).thenReturn(Optional.of(livro));
		when(autorRepository.findById(idInexistente)).thenReturn(Optional.empty());
		
		assertThrows(AutorNotFoundException.class, () -> {
			service.removerAutor(idExistente, idInexistente);
		});
	}
	
	@Test
	public void removerAutor_DeveRetornarAutorNotAssociatedException_SeAutorNaoEstiverVinculadoAoLivro() {
		when(repository.findById(idExistente)).thenReturn(Optional.of(livro));
		when(autorRepository.findById(2L)).thenReturn(Optional.of(autor2));
		
		assertThrows(AutorNotAssociatedException.class, () -> {
			service.removerAutor(idExistente, 2L);
		});
	}
	
	@Test
	public void adicionarCategoria_DeveRetornarLivroResponseDTO_QuandoOsIdsExistiremNoBanco() {
		when(repository.findById(idExistente)).thenReturn(Optional.of(livro));
		when(categoriaRepository.findById(2L)).thenReturn(Optional.of(categoria2));
		when(repository.save(any(Livro.class))).thenAnswer(invocation -> invocation.getArgument(0));
		
		LivroResponseDTO resultado = service.adicionarCategoria(idExistente, 2L);
		
		assertNotNull(resultado);
		assertEquals(livro.getId(), resultado.getId());
		assertEquals(livro.getTitulo(), resultado.getTitulo());
		assertEquals(livro.getIsbn(), resultado.getIsbn());
		assertEquals(livro.getAnoPublicacao(), resultado.getAnoPublicacao());
		assertEquals(livro.getNumeroPaginas(), resultado.getNumeroPaginas());
		assertEquals(livro.getEditora(), resultado.getEditora());
		assertEquals(livro.getEstante(), resultado.getEstante());
		assertEquals(livro.getAutores(), resultado.getAutores());
		for(Categoria categoriaAdicionada : resultado.getCategorias()) {
			if(categoriaAdicionada.getId() == 2) {
				assertEquals(categoria2.getId(), categoriaAdicionada.getId());
				assertEquals(categoria2.getTitulo(), categoriaAdicionada.getTitulo());
				assertEquals(categoria2.getDescricao(), categoriaAdicionada.getDescricao());
			}
		}
	}
	
	@Test
	public void adicionarCategoria_DeveRetornarLivroNotFoundException_SeIdNaoExistirNoBanco() {
		when(repository.findById(idInexistente)).thenReturn(Optional.empty());
		
		assertThrows(LivroNotFoundException.class, () -> {
			service.adicionarCategoria(idInexistente, idExistente);
		});
	}
	
	@Test
	public void adicionarCategoria_DeveRetornarCategoriaNotFoundException_SeIdNaoExistirNoBanco() {
		when(repository.findById(idExistente)).thenReturn(Optional.of(livro));
		when(categoriaRepository.findById(idInexistente)).thenReturn(Optional.empty());
		
		assertThrows(CategoriaNotFoundException.class, () -> {
			service.adicionarCategoria(idExistente, idInexistente);
		});
	}
	
	@Test
	public void adicionarCategoria_DeveRetornarCategoriaAlreadyAssociatedException_SeCategoriaJaEstaVinculadoAoLivro() {
		when(repository.findById(idExistente)).thenReturn(Optional.of(livro));
		when(categoriaRepository.findById(idExistente)).thenReturn(Optional.of(categoria));
		
		assertThrows(CategoriaAlreadyAssociatedException.class, () -> {
			service.adicionarCategoria(idExistente, idExistente);
		});
	}
	
	@Test
	public void removerCategoria_DeveRetornarLivroResponseDTO_QuandoOsIdsExistiremNoBanco() {
		when(repository.findById(idExistente)).thenReturn(Optional.of(livro));
		when(categoriaRepository.findById(idExistente)).thenReturn(Optional.of(categoria));
		when(repository.save(any(Livro.class))).thenAnswer(invocation -> invocation.getArgument(0));
		
		LivroResponseDTO resultado = service.removerCategoria(idExistente, idExistente);
		
		assertNotNull(resultado);
		assertEquals(livro.getId(), resultado.getId());
		assertEquals(livro.getTitulo(), resultado.getTitulo());
		assertEquals(livro.getIsbn(), resultado.getIsbn());
		assertEquals(livro.getAnoPublicacao(), resultado.getAnoPublicacao());
		assertEquals(livro.getNumeroPaginas(), resultado.getNumeroPaginas());
		assertEquals(livro.getEditora(), resultado.getEditora());
		assertEquals(livro.getEstante(), resultado.getEstante());
		assertEquals(livro.getAutores(), resultado.getAutores());
		assertTrue(resultado.getCategorias().isEmpty());
	}
	
	@Test
	public void removerCategoria_DeveRetornarLivroNotFoundException_SeIdNaoExistirNoBanco() {
		when(repository.findById(idInexistente)).thenReturn(Optional.empty());
		
		assertThrows(LivroNotFoundException.class, () -> {
			service.removerCategoria(idInexistente, idExistente);
		});
	}
	
	@Test
	public void removerCategoria_DeveRetornarCategoriaNotFoundException_SeIdNaoExistirNoBanco() {
		when(repository.findById(idExistente)).thenReturn(Optional.of(livro));
		when(categoriaRepository.findById(idInexistente)).thenReturn(Optional.empty());
		
		assertThrows(CategoriaNotFoundException.class, () -> {
			service.removerCategoria(idExistente, idInexistente);
		});
	}
	
	@Test
	public void removerCategoria_DeveRetornarCategoriaNotAssociatedException_SeCategoriaNaoEstiverVinculadoAoLivro() {
		when(repository.findById(idExistente)).thenReturn(Optional.of(livro));
		when(categoriaRepository.findById(idExistente)).thenReturn(Optional.of(categoria2));
		
		assertThrows(CategoriaNotAssociatedException.class, () -> {
			service.removerCategoria(idExistente, idExistente);
		});
	}
	
	@Test
	public void delete_DeveDeletarLivro_QuandoIdExistir() {
		when(repository.existsById(idExistente)).thenReturn(true);
		
		doNothing().when(repository).deleteById(idExistente);
		
		assertDoesNotThrow(() -> {
			service.delete(idExistente);
		});
	}
	
	@Test
	public void delete_DeveRetornarLivroNotFoundException_SeIdNaoExistirNoBanco() {
		when(repository.existsById(idInexistente)).thenReturn(false);
		
		assertThrows(LivroNotFoundException.class, () -> {
			service.delete(idInexistente);
		});
	}
	
	public void assertLivroEqualsDTO(LivroResponseDTO resultado) {
		assertNotNull(resultado);
		assertEquals(livro.getId(), resultado.getId());
		assertEquals(livro.getTitulo(), resultado.getTitulo());
		assertEquals(livro.getIsbn(), resultado.getIsbn());
		assertEquals(livro.getAnoPublicacao(), resultado.getAnoPublicacao());
		assertEquals(livro.getNumeroPaginas(), resultado.getNumeroPaginas());
		assertEquals(livro.getEditora(), resultado.getEditora());
		assertEquals(livro.getEstante(), resultado.getEstante());
		assertEquals(livro.getAutores(), resultado.getAutores());
		assertEquals(livro.getCategorias(), resultado.getCategorias());
	}
	
	public void assertListLivroEqualsDTO(List<LivroResponseDTO> resultado) {
		assertNotNull(resultado);
		assertEquals(livros.size(), resultado.size());
		assertEquals(livro.getId(), resultado.get(0).getId());
		assertEquals(livro.getTitulo(), resultado.get(0).getTitulo());
		assertEquals(livro.getIsbn(), resultado.get(0).getIsbn());
		assertEquals(livro.getAnoPublicacao(), resultado.get(0).getAnoPublicacao());
		assertEquals(livro.getNumeroPaginas(), resultado.get(0).getNumeroPaginas());
		assertEquals(livro.getEditora(), resultado.get(0).getEditora());
		assertEquals(livro.getEstante(), resultado.get(0).getEstante());
		assertEquals(livro.getAutores(), resultado.get(0).getAutores());
		assertEquals(livro.getCategorias(), resultado.get(0).getCategorias());
		assertEquals(livro2.getId(), resultado.get(1).getId());
		assertEquals(livro2.getTitulo(), resultado.get(1).getTitulo());
		assertEquals(livro2.getIsbn(), resultado.get(1).getIsbn());
		assertEquals(livro2.getAnoPublicacao(), resultado.get(1).getAnoPublicacao());
		assertEquals(livro2.getNumeroPaginas(), resultado.get(1).getNumeroPaginas());
		assertEquals(livro2.getEditora(), resultado.get(1).getEditora());
		assertEquals(livro2.getEstante(), resultado.get(1).getEstante());
		assertEquals(livro2.getAutores(), resultado.get(1).getAutores());
		assertEquals(livro2.getCategorias(), resultado.get(1).getCategorias());
	}
}
