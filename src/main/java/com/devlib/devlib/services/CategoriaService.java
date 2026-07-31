package com.devlib.devlib.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devlib.devlib.entites.Categoria;
import com.devlib.devlib.repositories.CategoriaRepository;
import com.devlib.devlib.services.exceptions.BadRequestException;
import com.devlib.devlib.services.exceptions.CategoriaNotFoundException;

@Service
public class CategoriaService {

	@Autowired
	private CategoriaRepository repository;
	
	public List<Categoria> findAll(){
		return repository.findAll();
	}
	public Categoria findById(Long id) {
		validationCategoriaId(id);
		Optional<Categoria> categoria = repository.findById(id);
		return categoria.get();
	}
	public Categoria insert(Categoria categoria) {
		badRequestId(categoria);
		return repository.save(categoria);
	}
	public Categoria update(Long id, Categoria entity) {
		validationCategoriaId(id);
		Categoria categoria = repository.getReferenceById(id);
		updateData(categoria, entity);
		return repository.save(categoria);
	}
	public void updateData(Categoria categoria, Categoria entity) {
		categoria.setTitulo(entity.getTitulo());
		categoria.setDescricao(entity.getDescricao());
	}
	public void delete(Long id) {
		validationCategoriaId(id);
		repository.deleteById(id);
	}
	public void validationCategoriaId(Long id){
		if(!repository.existsById(id)) {
			throw new CategoriaNotFoundException(id);
		}
	}
	public void badRequestId(Categoria categoria) {
		if (categoria.getId() != null) {
	        throw new BadRequestException();
	    }
	}
}
