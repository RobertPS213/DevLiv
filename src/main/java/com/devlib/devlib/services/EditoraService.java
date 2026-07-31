package com.devlib.devlib.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devlib.devlib.entites.Editora;
import com.devlib.devlib.repositories.EditoraRepository;
import com.devlib.devlib.services.exceptions.BadRequestException;
import com.devlib.devlib.services.exceptions.EditoraNotFoundException;

@Service
public class EditoraService {

	@Autowired
	private EditoraRepository repository;
	
	public List<Editora> findAll(){
		return repository.findAll();
	}
	public Editora findById(Long id) {
		validationEditoraId(id);
		Optional<Editora> editora = repository.findById(id);
		return editora.get();
	}
	public Editora insert(Editora editora) {
		badRequestId(editora);
		return repository.save(editora);
	}
	public Editora update(Long id, Editora entity) {
		validationEditoraId(id);
		Editora editora = repository.getReferenceById(id);
		updateData(editora, entity);
		return repository.save(editora);
	}
	public void updateData(Editora editora, Editora entity) {
		editora.setNome(entity.getNome());
		editora.setCnpj(entity.getCnpj());
		editora.setEmail(entity.getEmail());
	}
	public void delete(Long id) {
		validationEditoraId(id);
		repository.deleteById(id);
	}
	public void validationEditoraId(Long id){
		if(!repository.existsById(id)) {
			throw new EditoraNotFoundException(id);
		}
	}
	public void badRequestId(Editora editora) {
		if (editora.getId() != null) {
	        throw new BadRequestException();
	    }
	}
}
