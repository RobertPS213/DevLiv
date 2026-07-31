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

import com.devlib.devlib.entites.Autor;
import com.devlib.devlib.services.AutorService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/autores")
public class AutorController {

	@Autowired
	private AutorService service;
	
	@GetMapping
	public ResponseEntity<List<Autor>> findAll(){
		List<Autor> autores = service.findAll();
		return ResponseEntity.ok().body(autores);
	}
	@GetMapping(value = "/{id}")
	public ResponseEntity<Autor> findById(@PathVariable Long id){
		Autor autor = service.findById(id);
		return ResponseEntity.ok().body(autor);
	}
	@PostMapping
	public ResponseEntity<Autor> insert(@Valid @RequestBody Autor autor){
		autor = service.insert(autor);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(autor.getId()).toUri();
		return ResponseEntity.created(uri).body(autor);
	}
	@PutMapping(value = "/{id}")
	public ResponseEntity<Autor> update(@PathVariable Long id, @Valid @RequestBody Autor entity){
		Autor autor = service.update(id, entity);
		return ResponseEntity.ok().body(autor);
	}
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id){
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
}
