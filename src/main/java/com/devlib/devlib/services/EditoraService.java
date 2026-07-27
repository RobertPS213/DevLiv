package com.devlib.devlib.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devlib.devlib.entites.Editora;
import com.devlib.devlib.repositories.EditoraRepository;

@Service
public class EditoraService {

	@Autowired
	private EditoraRepository repository;
	
	public List<Editora> findAll(){
		return repository.findAll();
	}
	public Editora findById(Long id) {
		Optional<Editora> editora = repository.findById(id);
		return editora.get();
	}
	public Editora insert(Editora editora) {
		return repository.save(editora);
	}
	public Editora update(Long id, Editora entity) {
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
		repository.deleteById(id);
	}
}
