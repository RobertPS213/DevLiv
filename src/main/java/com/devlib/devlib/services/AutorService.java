package com.devlib.devlib.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devlib.devlib.entites.Autor;
import com.devlib.devlib.repositories.AutorRepository;

@Service
public class AutorService {

	@Autowired
	private AutorRepository repository;
	
	public List<Autor> findAll() {
		return repository.findAll();
	}
	public Autor findById(Long id) {
		Optional<Autor> autor = repository.findById(id);
		return autor.get();
	}
	public Autor insert(Autor autor) {
		return repository.save(autor);
	}
	public Autor update(Long id, Autor entity) {
		Autor autor = repository.getReferenceById(id);
		updateData(autor, entity);
		return repository.save(autor);
	}
	public void updateData(Autor autor, Autor entity) {
		autor.setNome(entity.getNome());
		autor.setNacionalidade(entity.getNacionalidade());
	}
	public void delete(Long id) {
		repository.deleteById(id);
	}
}
