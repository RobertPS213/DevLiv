package com.devlib.devlib.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devlib.devlib.dto.insert.AutorInsertDTO;
import com.devlib.devlib.dto.response.AutorResponseDTO;
import com.devlib.devlib.dto.update.AutorUpdateDTO;
import com.devlib.devlib.entites.Autor;
import com.devlib.devlib.repositories.AutorRepository;
import com.devlib.devlib.repositories.LivroRepository;
import com.devlib.devlib.services.exceptions.autor.AutorDeletionException;
import com.devlib.devlib.services.exceptions.autor.AutorNotFoundException;

@Service
public class AutorService {

	@Autowired
	private AutorRepository repository;
	
	@Autowired
	private LivroRepository livroRepository;
	
	public List<AutorResponseDTO> findAll() {
		return AutorResponseDTO.toResponseDTOList(repository.findAll());
	}
	public AutorResponseDTO findById(Long id) {
		Autor autor = repository.findById(id)
				.orElseThrow(() -> new AutorNotFoundException(id));
		AutorResponseDTO autorReturn = AutorResponseDTO.toResponseDTO(autor);
		return autorReturn;
	}
	public AutorResponseDTO insert(AutorInsertDTO autorDTO) {
		Autor autor = new Autor();
		autor.setNome(autorDTO.getNome());
		autor.setNacionalidade(autorDTO.getNacionalidade());
	    repository.save(autor);
	    AutorResponseDTO autorReturn = AutorResponseDTO.toResponseDTO(autor);
	    return autorReturn;
	}
	public AutorResponseDTO update(Long id, AutorUpdateDTO entity) {
		Autor autor = repository.findById(id)
				.orElseThrow(() -> new AutorNotFoundException(id));
		updateData(autor, entity);
		repository.save(autor);
		AutorResponseDTO autorReturn = AutorResponseDTO.toResponseDTO(autor);
		return autorReturn;
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
