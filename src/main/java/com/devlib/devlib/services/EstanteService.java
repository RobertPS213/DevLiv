package com.devlib.devlib.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devlib.devlib.entites.Estante;
import com.devlib.devlib.repositories.EstanteRepository;
import com.devlib.devlib.services.exceptions.BadRequestException;
import com.devlib.devlib.services.exceptions.EstanteNotFoundException;

@Service
public class EstanteService {

	@Autowired
	private EstanteRepository repository;
	
	public List<Estante> findAll(){
		return repository.findAll();
	}
	public Estante findById(Long id) {
		validationEstanteId(id);
		Optional<Estante> estante = repository.findById(id);
		return estante.get();
	}
	public Estante insert(Estante estante) {
		badRequestId(estante);
		return repository.save(estante);
	}
	public Estante update(Long id, Estante entity) {
		validationEstanteId(id);
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
		validationEstanteId(id);
		repository.deleteById(id);
	}
	public void validationEstanteId(Long id){
		if(!repository.existsById(id)) {
			throw new EstanteNotFoundException(id);
		}
	}
	public void badRequestId(Estante estante) {
		if (estante.getId() != null) {
	        throw new BadRequestException();
	    }
	}
}
