package com.devlib.devlib.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devlib.devlib.entites.Autor;
import com.devlib.devlib.entites.Categoria;
import com.devlib.devlib.entites.Livro;
import com.devlib.devlib.repositories.AutorRepository;
import com.devlib.devlib.repositories.CategoriaRepository;
import com.devlib.devlib.repositories.LivroRepository;

@Service
public class LivroService {
	
	@Autowired
	private LivroRepository repository;
	
	@Autowired
	private AutorRepository autorRepository;
	
	@Autowired
	private CategoriaRepository categoriaRepository;
	
	public List<Livro> findAll(){
		return repository.findAll();
	}
	public Livro findById(Long id) {
		Optional<Livro> livro = repository.findById(id);
		return livro.get();
	}
	public Livro insert(Livro livro) {
		return repository.save(livro);
	}
	public Livro update(Long id, Livro entity) {
		Livro livro = repository.getReferenceById(id);
		updateData(livro, entity);
		return repository.save(livro);
	}
	public void updateData(Livro livro, Livro entity) {
		livro.setTitulo(entity.getTitulo());
		livro.setIsbn(entity.getIsbn());
		livro.setAnoPublicacao(entity.getAnoPublicacao());
		livro.setNumeroPaginas(entity.getNumeroPaginas());
		livro.setEditora(entity.getEditora());
		livro.setEstante(entity.getEstante());
	}
	public Livro adicionarAutor(Long livroId, Long autorId) {
		Livro livro = repository.getReferenceById(livroId);
		Autor autor = autorRepository.getReferenceById(autorId);
		livro.getAutores().add(autor);
		repository.save(livro);
		return livro;
	}
	public Livro removerAutor(Long livroId, Long autorId) {
		Livro livro = repository.getReferenceById(livroId);
		Autor autor = autorRepository.getReferenceById(autorId);
		livro.getAutores().remove(autor);
		return repository.save(livro);
	}
	public Livro adicionarCategoria(Long livroId, Long categoriaId) {
		Livro livro = repository.getReferenceById(livroId);
		Categoria categoria = categoriaRepository.getReferenceById(categoriaId);
		livro.getCategorias().add(categoria);
		return repository.save(livro);
	}
	public Livro removerCategoria(Long livroId, Long categoriaId) {
		Livro livro = repository.getReferenceById(livroId);
		Categoria categoria = categoriaRepository.getReferenceById(categoriaId);
		livro.getCategorias().remove(categoria);
		return repository.save(livro);
	}
	public void delete(Long id) {
		repository.deleteById(id);
	}
}
