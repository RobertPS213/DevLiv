package com.devlib.devlib.config;

import java.util.Arrays;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

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

@Configuration
@Profile("test")
public class TestConfig implements CommandLineRunner{

	@Autowired
	private AutorRepository autorRepository;
	
	@Autowired
	private CategoriaRepository categoriaRepository;
	
	@Autowired
	private EditoraRepository editoraRepository;
	
	@Autowired
	private EstanteRepository estanteRepository;

	@Autowired
	private LivroRepository livroRepository;
	
	@Override
	public void run(String... args) throws Exception {
	    Autor a1 = new Autor(null, "Machado de Assis", "Brasileira");
	    Autor a2 = new Autor(null, "Gabriel García Márquez", "Colombiana");
	    Autor a3 = new Autor(null, "J.K. Rowling", "Britânica");
	    Autor a4 = new Autor(null, "Terry Pratchett", "Britânica");
	    Autor a5 = new Autor(null, "Neil Gaiman", "Britânica");

	    // Criação de um novo Autor para o teste do DELETE e evitar a violação de integridade referencial
	    
	    Autor a6 = new Autor(null, "Clarice Lispector", "Brasileira");
	    
	    autorRepository.saveAll(Arrays.asList(a1, a2, a3, a4, a5, a6));

	    Categoria c1 = new Categoria(null, "Fantasia", "Livros com elementos mágicos, mundos imaginários e criaturas fantásticas.");
	    Categoria c2 = new Categoria(null, "Romance", "Histórias com foco em relacionamentos amorosos e emoções humanas.");
	    Categoria c3 = new Categoria(null, "Literatura Brasileira", "Obras de autores nacionais, clássicas e contemporâneas.");

	 // Criação de uma nova Categoria para o teste do DELETE e evitar a violação de integridade referencial

	    Categoria c4 = new Categoria(null, "Ficção Científica", "Livros que exploram tecnologias futuristas, viagens espaciais e sociedades avançadas.");
	    
	    categoriaRepository.saveAll(Arrays.asList(c1, c2, c3, c4));

	    Editora e1 = new Editora(null, "Companhia das Letras", "60.643.313/0001-30", "contato@companhiadasletras.com.br");
	    Editora e2 = new Editora(null, "Editora Rocco", "33.518.897/0001-80", "atendimento@rocco.com.br");
	    Editora e3 = new Editora(null, "Penguin Random House", "12.345.678/0001-99", "contact@penguinrandomhouse.com");

	    editoraRepository.saveAll(Arrays.asList(e1, e2, e3));

	    Estante es1 = new Estante(null, "A1", "Corredor 1 - Prateleira Superior", 50);
	    Estante es2 = new Estante(null, "B2", "Corredor 2 - Prateleira Inferior", 30);
	    Estante es3 = new Estante(null, "C3", "Corredor 3 - Setor de Fantasia", 40);

	    estanteRepository.saveAll(Arrays.asList(es1, es2, es3));

	    Livro l1 = new Livro(null, "Dom Casmurro", "978-85-359-0277-5", 1899, 256, e1, es1, Set.of(a1), Set.of(c3));
	    Livro l2 = new Livro(null, "Cem Anos de Solidão", "978-85-01-01234-0", 1967, 448, e2, es2, Set.of(a2), Set.of(c2));
	    Livro l3 = new Livro(null, "Harry Potter e a Pedra Filosofal", "978-85-325-1101-0", 1997, 264, e3, es3, Set.of(a3), Set.of(c1));
	    Livro l4 = new Livro(null, "Memórias Póstumas de Brás Cubas", "978-85-359-0298-0", 1881, 208, e1, es1, Set.of(a1), Set.of(c3, c2));
	    Livro l5 = new Livro(null, "Belas Maldições", "978-85-325-1180-5", 1990, 412, e3, es3, Set.of(a4, a5), Set.of(c1));

	    livroRepository.saveAll(Arrays.asList(l1, l2, l3, l4, l5));
	}
}
