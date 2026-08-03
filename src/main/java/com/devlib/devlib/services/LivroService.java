package com.devlib.devlib.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devlib.devlib.DTO.LivroDTO;
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
import com.devlib.devlib.services.exceptions.AutorAlreadyAssociatedException;
import com.devlib.devlib.services.exceptions.AutorNotAssociatedException;
import com.devlib.devlib.services.exceptions.AutorNotFoundException;
import com.devlib.devlib.services.exceptions.BadRequestException;
import com.devlib.devlib.services.exceptions.CategoriaAlreadyAssociatedException;
import com.devlib.devlib.services.exceptions.CategoriaNotAssociatedException;
import com.devlib.devlib.services.exceptions.CategoriaNotFoundException;
import com.devlib.devlib.services.exceptions.EditoraNotFoundException;
import com.devlib.devlib.services.exceptions.EstanteNotFoundException;
import com.devlib.devlib.services.exceptions.IdAutoresNotFoundException;
import com.devlib.devlib.services.exceptions.IdCategoriasNotFoundException;
import com.devlib.devlib.services.exceptions.LivroNotFoundException;

@Service
public class LivroService {
	
	@Autowired
	private LivroRepository repository;
	
	@Autowired
	private AutorRepository autorRepository;
	
	@Autowired
	private CategoriaRepository categoriaRepository;
	
	@Autowired
	private EditoraRepository editoraRepository;
	
	@Autowired
	private EstanteRepository estanteRepository;
	
	public List<Livro> findAll(){
		return repository.findAll();
	}
	public Livro findById(Long id) {
		Livro livro = repository.findById(id)
				.orElseThrow(() -> new LivroNotFoundException(id));
		return livro;
	}
	public Livro insert(LivroDTO livroDTO) {
		badRequestId(livroDTO);
		Livro livro = new Livro();
		livro.setTitulo(livroDTO.getTitulo());
		livro.setIsbn(livroDTO.getIsbn());
		livro.setAnoPublicacao(livroDTO.getAnoPublicacao());
		livro.setNumeroPaginas(livroDTO.getNumeroPaginas());
		Editora editora = editoraRepository.findById(livroDTO.getEditoraId())
				.orElseThrow(() -> new EditoraNotFoundException(livroDTO.getEditoraId()));
		livro.setEditora(editora);
		Estante estante = estanteRepository.findById(livroDTO.getEstanteId())
				.orElseThrow(() -> new EstanteNotFoundException(livroDTO.getEstanteId()));
		livro.setEstante(estante);
		List<Long> idsRecebidos = new ArrayList<>(livroDTO.getAutoresId());
		List<Autor> listaAutores = autorRepository.findAllById(livroDTO.getAutoresId());
		validationIdAutores(idsRecebidos, listaAutores);
		livro.getAutores().addAll(listaAutores);
		List<Long> idsCatRecebidos = new ArrayList<>(livroDTO.getCategoriasId());
		List<Categoria> listaCategoria = categoriaRepository.findAllById(livroDTO.getCategoriasId());
		validationIdCategorias(idsCatRecebidos, listaCategoria);
		livro.getCategorias().addAll(listaCategoria);
		return repository.save(livro);
	}
	public Livro update(Long id, Livro entity) {
		Livro livro = repository.findById(id)
				.orElseThrow(() -> new LivroNotFoundException(id));
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
		Livro livro = repository.findById(livroId)
	            .orElseThrow(() -> new LivroNotFoundException(livroId));
	    Autor autor = autorRepository.findById(autorId)
	            .orElseThrow(() -> new AutorNotFoundException(autorId));
	    if (livro.getAutores().contains(autor)) {
	        throw new AutorAlreadyAssociatedException(livroId);
	    }
	    livro.getAutores().add(autor);
	    return repository.save(livro);
	}
	public Livro removerAutor(Long livroId, Long autorId) {
		Livro livro = repository.findById(livroId)
	            .orElseThrow(() -> new LivroNotFoundException(livroId));

	    Autor autor = autorRepository.findById(autorId)
	            .orElseThrow(() -> new AutorNotFoundException(autorId));
	    if (!livro.getAutores().contains(autor)) {
	        throw new AutorNotAssociatedException();
	    }
	    livro.getAutores().remove(autor);
	    return repository.save(livro);
	}
	public Livro adicionarCategoria(Long livroId, Long categoriaId) {
		Livro livro = repository.findById(livroId)
	            .orElseThrow(() -> new LivroNotFoundException(livroId));
		Categoria categoria = categoriaRepository.findById(categoriaId)
	            .orElseThrow(() -> new CategoriaNotFoundException(categoriaId));
		if(livro.getCategorias().contains(categoria)) {
			throw new CategoriaAlreadyAssociatedException();
		}
	    livro.getCategorias().add(categoria);
	    return repository.save(livro);
	}
	public Livro removerCategoria(Long livroId, Long categoriaId) {
		Livro livro = repository.findById(livroId)
	            .orElseThrow(() -> new LivroNotFoundException(livroId));
	    Categoria categoria = categoriaRepository.findById(categoriaId)
	            .orElseThrow(() -> new CategoriaNotFoundException(categoriaId));
	    if (!livro.getCategorias().contains(categoria)) {
	        throw new CategoriaNotAssociatedException();
	    }
	    livro.getCategorias().remove(categoria);
	    return repository.save(livro);
	}
	public void delete(Long id) {
		if(!repository.existsById(id)) throw new LivroNotFoundException(id);
		repository.deleteById(id);
	}
	public void badRequestId(LivroDTO livroDTO) {
		if (livroDTO.getId() != null) {
	        throw new BadRequestException();
	    }
	}
	public void validationIdAutores(List<Long> listaIds, List<Autor> listaAutores) {
		if(listaIds.size() != listaAutores.size()) {
			throw new IdAutoresNotFoundException();
		}
	}
	public void validationIdCategorias(List<Long> listaIds, List<Categoria> listaCategorias) {
		if(listaIds.size() != listaCategorias.size()) {
			throw new IdCategoriasNotFoundException();
		}
	}
}
