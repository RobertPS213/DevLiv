package com.devlib.devlib.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devlib.devlib.DTO.AutorInsertDTO;
import com.devlib.devlib.DTO.AutorUpdateDTO;
import com.devlib.devlib.entites.Autor;
import com.devlib.devlib.repositories.AutorRepository;
import com.devlib.devlib.repositories.LivroRepository;
import com.devlib.devlib.services.exceptions.AutorDeletionException;
import com.devlib.devlib.services.exceptions.AutorNotFoundException;

@Service
public class AutorService {

	@Autowired
	private AutorRepository repository;
	
	@Autowired
	private LivroRepository livroRepository;
	
	public List<Autor> findAll() {
		return repository.findAll();
	}
	public Autor findById(Long id) {
		Autor autor = repository.findById(id)
				.orElseThrow(() -> new AutorNotFoundException(id));
		return autor;
	}
	public Autor insert(AutorInsertDTO autorDTO) {
		Autor autor = new Autor();
		autor.setNome(autorDTO.getNome());
		autor.setNacionalidade(autorDTO.getNacionalidade());
	    return repository.save(autor);
	}
	public Autor update(Long id, AutorUpdateDTO entity) {
		Autor autor = repository.findById(id)
				.orElseThrow(() -> new AutorNotFoundException(id));
		updateData(autor, entity);
		return repository.save(autor);
	}
	public void updateData(Autor autor, AutorUpdateDTO entity) {
		autor.setNome(entity.getNome());
		autor.setNacionalidade(entity.getNacionalidade());
	}
	public void delete(Long id) {
		if(!repository.existsById(id)) throw new AutorNotFoundException(id);
		if(livroRepository.existsByAutoresId(id)) throw new AutorDeletionException(id);
		repository.deleteById(id);
	}
}
