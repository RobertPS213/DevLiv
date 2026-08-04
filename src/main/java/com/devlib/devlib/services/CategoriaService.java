package com.devlib.devlib.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devlib.devlib.DTO.CategoriaInsertDTO;
import com.devlib.devlib.DTO.CategoriaUpdateDTO;
import com.devlib.devlib.entites.Categoria;
import com.devlib.devlib.repositories.CategoriaRepository;
import com.devlib.devlib.repositories.LivroRepository;
import com.devlib.devlib.services.exceptions.categoria.CategoriaDeletionException;
import com.devlib.devlib.services.exceptions.categoria.CategoriaNotFoundException;

@Service
public class CategoriaService {

	@Autowired
	private CategoriaRepository repository;
	
	@Autowired
	private LivroRepository livroRepository;
	
	public List<Categoria> findAll(){
		return repository.findAll();
	}
	public Categoria findById(Long id) {
		Categoria categoria = repository.findById(id)
				.orElseThrow(() -> new CategoriaNotFoundException(id));
		return categoria;
	}
	public Categoria insert(CategoriaInsertDTO categoriaDTO) {
		Categoria categoria = new Categoria();
		categoria.setTitulo(categoriaDTO.getTitulo());
		categoria.setDescricao(categoriaDTO.getDescricao());
		return repository.save(categoria);
	}
	public Categoria update(Long id, CategoriaUpdateDTO entity) {
		Categoria categoria = repository.findById(id)
				.orElseThrow(() -> new CategoriaNotFoundException(id));
		updateData(categoria, entity);
		return repository.save(categoria);
	}
	public void updateData(Categoria categoria, CategoriaUpdateDTO entity) {
		categoria.setTitulo(entity.getTitulo());
		categoria.setDescricao(entity.getDescricao());
	}
	public void delete(Long id) {
		if(livroRepository.existsByCategoriasId(id)) throw new CategoriaDeletionException(id);
		if(!repository.existsById(id)) throw new CategoriaNotFoundException(id);
		repository.deleteById(id);
	}
}
