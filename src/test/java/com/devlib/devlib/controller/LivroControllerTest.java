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
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.devlib.devlib.dto.insert.LivroInsertDTO;
import com.devlib.devlib.dto.response.LivroResponseDTO;
import com.devlib.devlib.dto.update.LivroUpdateDTO;
import com.devlib.devlib.entites.Autor;
import com.devlib.devlib.entites.Categoria;
import com.devlib.devlib.entites.Editora;
import com.devlib.devlib.entites.Estante;
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

import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

@WebMvcTest(LivroController.class)
public class LivroControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@MockitoBean
	private LivroService service;
	
	private LivroResponseDTO livro1;
	private LivroResponseDTO livro2;
	private LivroResponseDTO livroAutorAdicionado;
	private LivroResponseDTO livroAutorRemovido;
	private LivroResponseDTO livroCategoriaAdicionada;
	private LivroResponseDTO livroCategoriaRemovida;
	private Editora editora;
	private Estante estante;
	private Autor autor;
	private Autor autor2;
	private Categoria categoria;
	private Categoria categoria2;
	private LivroInsertDTO livroInsert;
	private LivroUpdateDTO livroUpdate;
	private List<LivroResponseDTO> livros;
	private String jsonBody;
	private String jsonBodyUpdate;
	
	@BeforeEach
	void setUp() throws JacksonException{
		// complemento para o construtor Livro
		editora = new Editora(1L, "Editora Rocco Ltda.", "42.444.703/0001-59", "rocco@rocco.com.br");
		estante = new Estante(1L, "EST-001", "Biblioteca - Sala A - Parede Norte", 100);
		autor = new Autor(1L, "Paulo Coelho", "Brasileiro");
		autor2 = new Autor(2L, "Clarice Lispector", "Brasileira");
		categoria = new Categoria(1L, "Terror", "Obras criadas para provocar medo, tensão e suspense.");
		categoria2 = new Categoria(2L,"Romance", "Obras literárias que abordam histórias de amor, relacionamentos e sentimentos.");
		
		// livros
		livro1 = new LivroResponseDTO(1L, "O Hobbit", "9788595084742", 1937, 336, editora, estante, Set.of(autor), Set.of(categoria));
		livro2 = new LivroResponseDTO(2L, "O Silmarillion", "9788533613409", 1977, 480, editora, estante, Set.of(autor), Set.of(categoria));
		livros = List.of(livro1, livro2);
		
		// livro Insert
		livroInsert = new LivroInsertDTO("O Hobbit", "9788595084742", 1937, 336, 1L, 1L, Set.of(1L), Set.of(1L));
		jsonBody = objectMapper.writeValueAsString(livroInsert);
		
		// livro Update
		livroUpdate = new LivroUpdateDTO("O Silmarillion", "9788533613409", 1977, 480, 1L, 1L);
		jsonBodyUpdate = objectMapper.writeValueAsString(livroUpdate);
		
		// adicionarAutor
		livroAutorAdicionado = new LivroResponseDTO(3L, "O Hobbit", "9788595084742", 1937, 336, editora, estante, Set.of(autor, autor2), Set.of(categoria)); 
		
		// removerAutor
		livroAutorRemovido = new LivroResponseDTO(4L, "O Hobbit", "9788595084742", 1937, 336, editora, estante, Set.of(), Set.of(categoria));
		
		// adicionarCategoria
		livroCategoriaAdicionada = new LivroResponseDTO(5L, "O Hobbit", "9788595084742", 1937, 336, editora, estante, Set.of(autor), Set.of(categoria, categoria2));
		
		// removeCategoria
		livroCategoriaRemovida = new LivroResponseDTO(6L, "O Hobbit", "9788595084742", 1937, 336, editora, estante, Set.of(autor), Set.of());
	}
	
	@Test
	public void findAll_DeveRetornarStatus200OkComListaDeLivroResponseDTO() throws Exception {
		when(service.findAll()).thenReturn(livros);
		
		validationLivroList(livros);
	}
	
	@Test
	public void findAll_DeveRetornarStatus200OkComListaDeLivroResponseDTOVazia_QuandoNaoHouverLivrosAdicionados() throws Exception {
		when(service.findAll()).thenReturn(List.of());
		
		mockMvc.perform(get("/livros"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(0));
	}
	
	@Test
	public void findById_DeveRetornarStatus200OkComLivroResponseDTO_QuandoIdExistir() throws Exception {
		when(service.findById(1L)).thenReturn(livro1);
		
		validationLivro(livro1);
	}
	
	@Test
	public void findById_DeveRetornarStatus404NotfoundComLivroNotFoundException_QuandoIdNaoExistir() throws Exception {
		when(service.findById(99L)).thenThrow(new LivroNotFoundException(99L));
		
		mockMvc.perform(get("/livros/{id}", 99L))
				.andExpect(status().isNotFound());
	}
	
	@Test
	public void insert_DeveRetornarStatus201CreatedComLivroResponseDTO() throws Exception {
		when(service.insert(any(LivroInsertDTO.class))).thenReturn(livro1);
		
		mockMvc.perform(post("/livros")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonBody)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$.titulo").value("O Hobbit"))
				.andExpect(jsonPath("$.isbn").value("9788595084742"))
				.andExpect(jsonPath("$.anoPublicacao").value(1937))
				.andExpect(jsonPath("$.numeroPaginas").value(336))
				.andExpect(jsonPath("$.editora.id").value(1))
				.andExpect(jsonPath("$.estante.id").value(1))
				.andExpect(jsonPath("$.autores[0].id").value(1))
				.andExpect(jsonPath("$.categorias[0].id").value(1));
	}
	
	@Test
	public void insert_DeveRetornarStatus404NotFoundComEditoraNotFoundException_QuandoIdDaEditoraNaoExistir() throws Exception {
		when(service.insert(any(LivroInsertDTO.class))).thenThrow(new EditoraNotFoundException(99L));
		
		MockMvcReturnExceptionNotfound();
	}
	
	@Test
	public void insert_DeveRetornarStatus404NotFoundComEstanteNotFoundException_QuandoIdDaEstanteNaoExistir() throws Exception {
		when(service.insert(any(LivroInsertDTO.class))).thenThrow(new EstanteNotFoundException(99L));
		
		MockMvcReturnExceptionNotfound();
	}
	
	@Test
	public void insert_DeveRetornarStatus404NotFoundComIdAutoresNotFoundException_SeIdsRecebidosNaListaLongNaoExistirNoBancoDeDados() throws Exception {
		when(service.insert(any(LivroInsertDTO.class))).thenThrow(new IdAutoresNotFoundException());
		
		MockMvcReturnExceptionNotfound();
	}
	
	@Test
	public void insert_DeveRetornarStatus404NotFoundComIdCategoriasNotFoundException_SeIdsRecebidosNaListaLongNaoExistirNoBancoDeDados() throws Exception {
		when(service.insert(any(LivroInsertDTO.class))).thenThrow(new IdCategoriasNotFoundException());
		
		MockMvcReturnExceptionNotfound();
	}
	
	@Test
	public void update_DeveRetornarStatus200OkComLivroResponseDTO_QuandoIdExistir() throws Exception {
		when(service.update(eq(2L), any(LivroUpdateDTO.class))).thenReturn(livro2);
		
		mockMvc.perform(put("/livros/{id}", 2L)
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonBodyUpdate)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(2))
				.andExpect(jsonPath("$.titulo").value("O Silmarillion"))
				.andExpect(jsonPath("$.isbn").value("9788533613409"))
				.andExpect(jsonPath("$.anoPublicacao").value(1977))
				.andExpect(jsonPath("$.numeroPaginas").value(480))
				.andExpect(jsonPath("$.editora.id").value(1))
				.andExpect(jsonPath("$.estante.id").value(1));
	}
	
	@Test
	public void update_DeveRetornarStatus404NotFoundComLivroNotFoundException_QuandoIdDoLivroNaoExistir() throws Exception {
		when(service.update(eq(99L), any(LivroUpdateDTO.class))).thenThrow(new LivroNotFoundException(99L));
		
		mockMvc.perform(put("/livros/{id}", 99L)
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonBodyUpdate)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}
	
	@Test
	public void adicionarAutores_DeveRetornarStatus200OkComLivroResponseDTO_QuandoIdsExistirem() throws Exception {
		when(service.adicionarAutor(3L, 2L)).thenReturn(livroAutorAdicionado);
		
		mockMvc.perform(post("/livros/{livroId}/autores/{autorId}", 3L, 2L))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(3))
				.andExpect(jsonPath("$.titulo").value("O Hobbit"))
				.andExpect(jsonPath("$.isbn").value("9788595084742"))
				.andExpect(jsonPath("$.anoPublicacao").value(1937))
				.andExpect(jsonPath("$.numeroPaginas").value(336))
				.andExpect(jsonPath("$.editora.id").value(1))
				.andExpect(jsonPath("$.estante.id").value(1))
				.andExpect(jsonPath("$.autores.length()").value(2))
				.andExpect(jsonPath("$.categorias.length()").value(1));
	}
	
	@Test
	public void adicionarAutores_DeveRetornarStatus404NotFoundComLivroNotFoundException_QuandoIdDoLivroNaoExistir() throws Exception {
		when(service.adicionarAutor(99L, 2L)).thenThrow(new LivroNotFoundException(99L));
		
		mockMvc.perform(post("/livros/{livroId}/autores/{autorId}", 99L, 2L))
			.andExpect(status().isNotFound());
	}
	
	@Test
	public void adicionarAutores_DeveRetornarStatus404NotFoundComAutorNotFoundException_QuandoIdDoAutorNaoExistir() throws Exception {
		when(service.adicionarAutor(3L, 99L)).thenThrow(new AutorNotFoundException(99L));
		
		mockMvc.perform(post("/livros/{livroId}/autores/{autorId}", 3L, 99L))
			.andExpect(status().isNotFound());
	}
	
	
	@Test
	public void adicionarAutores_DeveRetornarStatus404NotFoundComAutorAlreadyAssociatedException_QuandoAutorJaEstiverVinculadoAoLivro() throws Exception {
		when(service.adicionarAutor(3L, 1L)).thenThrow(new AutorAlreadyAssociatedException(1L));
		
		mockMvc.perform(post("/livros/{livroId}/autores/{autorId}", 3L, 1L))
			.andExpect(status().isNotFound());
	}
	
	
	@Test
	public void removerAutores_DeveRetornarStatus200OkComLivroResponseDTO_QuandoIdsExistirem() throws Exception {
		when(service.removerAutor(4L, 1L)).thenReturn(livroAutorRemovido);
		
		mockMvc.perform(delete("/livros/{livroId}/autores/{autorId}", 4L, 1L))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(4))
				.andExpect(jsonPath("$.titulo").value("O Hobbit"))
				.andExpect(jsonPath("$.isbn").value("9788595084742"))
				.andExpect(jsonPath("$.anoPublicacao").value(1937))
				.andExpect(jsonPath("$.numeroPaginas").value(336))
				.andExpect(jsonPath("$.editora.id").value(1))
				.andExpect(jsonPath("$.estante.id").value(1))
				.andExpect(jsonPath("$.autores.length()").value(0))
				.andExpect(jsonPath("$.categorias.length()").value(1));
	}
	
	@Test
	public void removerAutores_DeveRetornarStatus404NotFoundComLivroNotFoundException_QuandoIdDoLivroNaoExistir() throws Exception {
		when(service.removerAutor(99L, 1L)).thenThrow(new LivroNotFoundException(99L));
		
		mockMvc.perform(delete("/livros/{livroId}/autores/{autorId}", 99L, 1L))
			.andExpect(status().isNotFound());
	}
	
	@Test
	public void removerAutores_DeveRetornarStatus404NotFoundComAutorNotFoundException_QuandoIdDoAutorNaoExistir() throws Exception {
		when(service.removerAutor(4L, 99L)).thenThrow(new AutorNotFoundException(99L));
		
		mockMvc.perform(delete("/livros/{livroId}/autores/{autorId}", 4L, 99L))
			.andExpect(status().isNotFound());
	}
	
	
	@Test
	public void removerAutores_DeveRetornarStatus404NotFoundComAutorNotAssociatedException_QuandoAutorNaoEstiverVinculadoAUmLivro() throws Exception {
		when(service.removerAutor(4L, 10L)).thenThrow(new AutorNotAssociatedException());
		
		mockMvc.perform(delete("/livros/{livroId}/autores/{autorId}", 4L, 10L))
			.andExpect(status().isNotFound());
	}
	
	
	
	@Test
	public void adicionarCategoria_DeveRetornarStatus200OkComLivroResponseDTO_QuandoIdsExistirem() throws Exception {
		when(service.adicionarCategoria(5L, 2L)).thenReturn(livroCategoriaAdicionada);
		
		mockMvc.perform(post("/livros/{livroId}/categorias/{categoriaId}", 5L, 2L))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(5))
				.andExpect(jsonPath("$.titulo").value("O Hobbit"))
				.andExpect(jsonPath("$.isbn").value("9788595084742"))
				.andExpect(jsonPath("$.anoPublicacao").value(1937))
				.andExpect(jsonPath("$.numeroPaginas").value(336))
				.andExpect(jsonPath("$.editora.id").value(1))
				.andExpect(jsonPath("$.estante.id").value(1))
				.andExpect(jsonPath("$.autores.length()").value(1))
				.andExpect(jsonPath("$.categorias.length()").value(2));
				
	}
	
	@Test
	public void adicionarCategoria_DeveRetornarStatus404NotFoundComLivroNotFoundException_QuandoIdDoLivroNaoExistir() throws Exception {
		when(service.adicionarCategoria(99L, 2L)).thenThrow(new LivroNotFoundException(99L));
		
		mockMvc.perform(post("/livros/{livroId}/categorias/{categoriaId}", 99L, 2L))
			.andExpect(status().isNotFound());
	}
	
	@Test
	public void adicionarCategoria_DeveRetornarStatus404NotFoundComCategoriaNotFoundException_QuandoIdDaCategoriaNaoExistir() throws Exception {
		when(service.adicionarCategoria(5L, 99L)).thenThrow(new CategoriaNotFoundException(99L));
		
		mockMvc.perform(post("/livros/{livroId}/categorias/{categoriaId}", 5L, 99L))
			.andExpect(status().isNotFound());
	}
	
	@Test
	public void adicionarCategoria_DeveRetornarStatus404NotFoundComCategoriaAlreadyAssociatedException_QuandoCategoriaJaEstiverVinculadaAoLivro() throws Exception {
		when(service.adicionarCategoria(5L, 1L)).thenThrow(new CategoriaAlreadyAssociatedException());
		
		mockMvc.perform(post("/livros/{livroId}/categorias/{categoriaId}", 5L, 1L))
			.andExpect(status().isNotFound());
	}
	
	@Test
	public void removerCategoria_DeveRetornarStatus200OkComLivroResponseDTO_QuandoIdsExistirem() throws Exception {
		when(service.removerCategoria(6L, 1L)).thenReturn(livroCategoriaRemovida);
		
		mockMvc.perform(delete("/livros/{livroId}/categorias/{categoriaId}", 6L, 1L))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(6))
				.andExpect(jsonPath("$.titulo").value("O Hobbit"))
				.andExpect(jsonPath("$.isbn").value("9788595084742"))
				.andExpect(jsonPath("$.anoPublicacao").value(1937))
				.andExpect(jsonPath("$.numeroPaginas").value(336))
				.andExpect(jsonPath("$.editora.id").value(1))
				.andExpect(jsonPath("$.estante.id").value(1))
				.andExpect(jsonPath("$.autores.length()").value(1))
				.andExpect(jsonPath("$.categorias.length()").value(0));
				
	}
	
	@Test
	public void removerCategoria_DeveRetornarStatus404NotFoundComLivroNotFoundException_QuandoIdDoLivroNaoExistir() throws Exception {
		when(service.removerCategoria(99L, 1L)).thenThrow(new LivroNotFoundException(99L));
		
		mockMvc.perform(delete("/livros/{livroId}/categorias/{categoriaId}", 99L, 1L))
			.andExpect(status().isNotFound());
	}
	
	@Test
	public void removerCategoria_DeveRetornarStatus404NotFoundComCategoriaNotFoundException_QuandoIdDaCategoriaNaoExistir() throws Exception {
		when(service.removerCategoria(6L, 99L)).thenThrow(new CategoriaNotFoundException(99L));
		
		mockMvc.perform(delete("/livros/{livroId}/categorias/{categoriaId}", 6L, 99L))
			.andExpect(status().isNotFound());
	}
	
	@Test
	public void removerCategoria_DeveRetornarStatus404NotFoundComCategoriaNotAssociatedException_QuandoCategoriaNaoEstiverVinculadaAUmLivro() throws Exception {
		when(service.removerCategoria(6L, 99L)).thenThrow(new CategoriaNotAssociatedException());
		
		mockMvc.perform(delete("/livros/{livroId}/categorias/{categoriaId}", 6L, 99L))
			.andExpect(status().isNotFound());
	}
	
	@Test
	public void delete_DeveRetornarStatus204NoContent_QuandoIdExistir() throws Exception {
		mockMvc.perform(delete("/livros/{id}", 1L))
				.andExpect(status().isNoContent());
	}
	
	@Test
	public void delete_DeveRetornarStatus404NotFoundComLivroNotFoundException_QuandoIdDoLivroNaoExistir() throws Exception {
		doThrow(new LivroNotFoundException(99L)).when(service).delete(99L);
		
		mockMvc.perform(delete("/livros/{id}", 99L))
				.andExpect(status().isNotFound());
	}
	
	public void MockMvcReturnExceptionNotfound() throws Exception {
		mockMvc.perform(post("/livros")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonBody)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}
	
	public void validationLivroList(List<LivroResponseDTO> livros) throws Exception {
		mockMvc.perform(get("/livros"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].id").value(1))
				.andExpect(jsonPath("$[0].titulo").value("O Hobbit"))
				.andExpect(jsonPath("$[0].isbn").value("9788595084742"))
				.andExpect(jsonPath("$[0].anoPublicacao").value(1937))
				.andExpect(jsonPath("$[0].numeroPaginas").value(336))
				.andExpect(jsonPath("$[0].editora.id").value(1))
				.andExpect(jsonPath("$[0].estante.id").value(1))
				.andExpect(jsonPath("$[0].autores.length()").value(1))
				.andExpect(jsonPath("$[0].autores[0].id").value(1))
				.andExpect(jsonPath("$[0].autores[0].nome").value("Paulo Coelho"))
				.andExpect(jsonPath("$[0].autores[0].nacionalidade").value("Brasileiro"))
				.andExpect(jsonPath("$[0].categorias.length()").value(1))
				.andExpect(jsonPath("$[0].categorias[0].id").value(1))
				.andExpect(jsonPath("$[0].categorias[0].titulo").value("Terror"))
				.andExpect(jsonPath("$[0].categorias[0].descricao").value("Obras criadas para provocar medo, tensão e suspense."))

				.andExpect(jsonPath("$[1].id").value(2))
				.andExpect(jsonPath("$[1].titulo").value("O Silmarillion"))
				.andExpect(jsonPath("$[1].isbn").value("9788533613409"))
				.andExpect(jsonPath("$[1].anoPublicacao").value(1977))
				.andExpect(jsonPath("$[1].numeroPaginas").value(480));
	}
	
	public void validationLivro(LivroResponseDTO livro) throws Exception {
		mockMvc.perform(get("/livros/{id}", 1L))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$.titulo").value("O Hobbit"))
				.andExpect(jsonPath("$.isbn").value("9788595084742"))
				.andExpect(jsonPath("$.anoPublicacao").value(1937))
				.andExpect(jsonPath("$.numeroPaginas").value(336))
				.andExpect(jsonPath("$.editora.id").value(1))
				.andExpect(jsonPath("$.estante.id").value(1))
				.andExpect(jsonPath("$.autores.length()").value(1))
				.andExpect(jsonPath("$.autores[0].id").value(1))
				.andExpect(jsonPath("$.autores[0].nome").value("Paulo Coelho"))
				.andExpect(jsonPath("$.autores[0].nacionalidade").value("Brasileiro"))
				.andExpect(jsonPath("$.categorias.length()").value(1))
				.andExpect(jsonPath("$.categorias[0].id").value(1))
				.andExpect(jsonPath("$.categorias[0].titulo").value("Terror"))
				.andExpect(jsonPath("$.categorias[0].descricao").value("Obras criadas para provocar medo, tensão e suspense."));
	}
	
}
