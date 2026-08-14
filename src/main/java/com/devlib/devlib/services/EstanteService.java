package com.devlib.devlib.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devlib.devlib.dto.insert.EstanteInsertDTO;
import com.devlib.devlib.dto.update.EstanteUpdateDTO;
import com.devlib.devlib.entites.Estante;
import com.devlib.devlib.repositories.EstanteRepository;
import com.devlib.devlib.repositories.LivroRepository;
import com.devlib.devlib.services.exceptions.estante.EstanteDeletionException;
import com.devlib.devlib.services.exceptions.estante.EstanteNotFoundException;

@Service
public class EstanteService {

	@Autowired
	private EstanteRepository repository;
	
	@Autowired
	private LivroRepository livroRepository;
	
	public List<Estante> findAll(){
		return repository.findAll();
	}
	public Estante findById(Long id) {
		Estante estante = repository.findById(id)
				.orElseThrow(() -> new EstanteNotFoundException(id));
		return estante;
	}
	public Estante insert(EstanteInsertDTO estanteDTO) {
		Estante estante = new Estante();
		estante.setCodigo(estanteDTO.getCodigo());
		estante.setLocalizacao(estanteDTO.getLocalizacao());
		estante.setCapacidade(estanteDTO.getCapacidade());
		return repository.save(estante);
	}
	public Estante update(Long id, EstanteUpdateDTO entity) {
		Estante estante = repository.findById(id)
				.orElseThrow(() -> new EstanteNotFoundException(id));
		updateData(estante, entity);
		return repository.save(estante);
	}
	public void updateData(Estante estante, EstanteUpdateDTO entity) {
		estante.setCodigo(entity.getCodigo());
		estante.setLocalizacao(entity.getLocalizacao());
		estante.setCapacidade(entity.getCapacidade());
	}
	public void delete(Long id) {
		if(livroRepository.existsByEstanteId(id)) throw new EstanteDeletionException(id);
		if(!repository.existsById(id)) throw new EstanteNotFoundException(id);
		repository.deleteById(id);
	}
}
