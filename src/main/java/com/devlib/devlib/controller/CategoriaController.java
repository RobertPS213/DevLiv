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

import com.devlib.devlib.entites.Categoria;
import com.devlib.devlib.services.CategoriaService;

@RestController
@RequestMapping(value = "/categorias")
public class CategoriaController {

	@Autowired
	private CategoriaService service;
	
	@GetMapping
	public ResponseEntity<List<Categoria>> findAll(){
		List<Categoria> categorias = service.findAll();
		return ResponseEntity.ok().body(categorias);
	}
	@GetMapping(value = "/{id}")
	public ResponseEntity<Categoria> findById(@PathVariable Long id){
		Categoria categoria = service.findById(id);
		return ResponseEntity.ok().body(categoria);
	}
	@PostMapping
	public ResponseEntity<Categoria> insert(@RequestBody Categoria entity){
		entity = service.insert(entity);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(entity.getId()).toUri();
		return ResponseEntity.created(uri).body(entity);
	}
	@PutMapping(value = "/{id}")
	public ResponseEntity<Categoria> update(@PathVariable Long id, @RequestBody Categoria entity){
		Categoria categoria = service.update(id, entity);
		return ResponseEntity.ok().body(categoria);
	}
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id){
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
}
