package com.devlib.devlib.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.devlib.devlib.entites.Estante;
import com.devlib.devlib.services.EstanteService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/estantes")
public class EstanteController {
	
	@Autowired
	private EstanteService service;
	
	@GetMapping
	public ResponseEntity<List<Estante>> findAll(){
		List<Estante> estante = service.findAll();
		return ResponseEntity.ok().body(estante);
	}
	@GetMapping(value = "/{id}")
	public ResponseEntity<Estante> findById(@PathVariable Long id){
		Estante estante = service.findById(id);
		return ResponseEntity.ok().body(estante);
	}
	@PostMapping
	public ResponseEntity<Estante> insert(@Valid @RequestBody Estante estante){
		estante = service.insert(estante);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(estante.getId()).toUri();
		return ResponseEntity.created(uri).body(estante);
	}
	@PutMapping(value = "/{id}")
	public ResponseEntity<Estante> update(@PathVariable Long id, @Valid @RequestBody Estante entity){
		Estante estante = service.update(id, entity);
		return ResponseEntity.ok().body(estante);
	}
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id){
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
}
