package com.devlib.devlib.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devlib.devlib.entites.Estante;
import com.devlib.devlib.repositories.EstanteRepository;

@Service
public class EstanteService {

	@Autowired
	private EstanteRepository repository;
	
	public List<Estante> findAll(){
		return repository.findAll();
	}
	public Estante findById(Long id) {
		Optional<Estante> estante = repository.findById(id);
		return estante.get();
	}
	public Estante insert(Estante estante) {
		return repository.save(estante);
	}
	public Estante update(Long id, Estante entity) {
		Estante estante = repository.getReferenceById(id);
		updateData(estante, entity);
		return repository.save(estante);
	}
	public void updateData(Estante estante, Estante entity) {
		estante.setCodigo(entity.getCodigo());
		estante.setLocalizacao(entity.getLocalizacao());
		estante.setCapacidade(entity.getCapacidade());
	}
	public void delete(Long id) {
		repository.deleteById(id);
	}
}
