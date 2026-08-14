package com.devlib.devlib.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devlib.devlib.dto.insert.CategoriaInsertDTO;
import com.devlib.devlib.dto.response.CategoriaResponseDTO;
import com.devlib.devlib.dto.update.CategoriaUpdateDTO;
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
	
	public List<CategoriaResponseDTO> findAll(){
		return CategoriaResponseDTO.toResponseDTOList(repository.findAll());
	}
	public CategoriaResponseDTO findById(Long id) {
		Categoria categoria = repository.findById(id)
				.orElseThrow(() -> new CategoriaNotFoundException(id));
		CategoriaResponseDTO categoriaReturn = CategoriaResponseDTO.toResponseDTO(categoria);
		return categoriaReturn;
	}
	public CategoriaResponseDTO insert(CategoriaInsertDTO categoriaDTO) {
		Categoria categoria = new Categoria();
		categoria.setTitulo(categoriaDTO.getTitulo());
		categoria.setDescricao(categoriaDTO.getDescricao());
		repository.save(categoria);
		CategoriaResponseDTO categoriaReturn = CategoriaResponseDTO.toResponseDTO(categoria);
		return categoriaReturn;
	}
	public CategoriaResponseDTO update(Long id, CategoriaUpdateDTO entity) {
		Categoria categoria = repository.findById(id)
				.orElseThrow(() -> new CategoriaNotFoundException(id));
		updateData(categoria, entity);
		repository.save(categoria);
		CategoriaResponseDTO categoriaReturn = CategoriaResponseDTO.toResponseDTO(categoria);
		return categoriaReturn;
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
