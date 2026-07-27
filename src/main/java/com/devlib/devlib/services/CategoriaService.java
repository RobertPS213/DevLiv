package com.devlib.devlib.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devlib.devlib.entites.Categoria;
import com.devlib.devlib.repositories.CategoriaRepository;

@Service
public class CategoriaService {

	@Autowired
	private CategoriaRepository repository;
	
	public List<Categoria> findAll(){
		return repository.findAll();
	}
	public Categoria findById(Long id) {
		Optional<Categoria> categoria = repository.findById(id);
		return categoria.get();
	}
	public Categoria insert(Categoria categoria) {
		return repository.save(categoria);
	}
	public Categoria update(Long id, Categoria entity) {
		Categoria categoria = repository.getReferenceById(id);
		updateData(categoria, entity);
		return repository.save(categoria);
	}
	public void updateData(Categoria categoria, Categoria entity) {
		categoria.setTitulo(entity.getTitulo());
		categoria.setDescricao(entity.getDescricao());
	}
	public void delete(Long id) {
		repository.deleteById(id);
	}
}
