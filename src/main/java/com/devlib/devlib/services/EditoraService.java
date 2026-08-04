package com.devlib.devlib.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devlib.devlib.DTO.EditoraInsertDTO;
import com.devlib.devlib.DTO.EditoraUpdateDTO;
import com.devlib.devlib.entites.Editora;
import com.devlib.devlib.repositories.EditoraRepository;
import com.devlib.devlib.repositories.LivroRepository;
import com.devlib.devlib.services.exceptions.EditoraDeletionException;
import com.devlib.devlib.services.exceptions.EditoraNotFoundException;

@Service
public class EditoraService {

	@Autowired
	private EditoraRepository repository;
	
	@Autowired
	private LivroRepository livroRepository;
	
	public List<Editora> findAll(){
		return repository.findAll();
	}
	public Editora findById(Long id) {
		Editora editora = repository.findById(id)
				.orElseThrow(() -> new EditoraNotFoundException(id));
		return editora;
	}
	public Editora insert(EditoraInsertDTO editoraDTO) {
		Editora editora = new Editora();
		editora.setNome(editoraDTO.getNome());
		editora.setCnpj(editoraDTO.getCnpj());
		editora.setEmail(editoraDTO.getEmail());
		return repository.save(editora);
	}
	public Editora update(Long id, EditoraUpdateDTO entity) {
		Editora editora = repository.findById(id)
				.orElseThrow(() -> new EditoraNotFoundException(id));
		updateData(editora, entity);
		return repository.save(editora);
	}
	public void updateData(Editora editora, EditoraUpdateDTO entity) {
		editora.setNome(entity.getNome());
		editora.setCnpj(entity.getCnpj());
		editora.setEmail(entity.getEmail());
	}
	public void delete(Long id) {
		if(!repository.existsById(id)) throw new EditoraNotFoundException(id);
		if(livroRepository.existsByEditoraId(id)) throw new EditoraDeletionException(id);
		repository.deleteById(id);
	}
}
