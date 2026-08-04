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

import com.devlib.devlib.DTO.EditoraInsertDTO;
import com.devlib.devlib.DTO.EditoraUpdateDTO;
import com.devlib.devlib.entites.Editora;
import com.devlib.devlib.services.EditoraService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/editoras")
public class EditoraController {

	@Autowired
	private EditoraService service;
	
	@GetMapping
	public ResponseEntity<List<Editora>> findAll(){
		List<Editora> editoras = service.findAll();
		return ResponseEntity.ok().body(editoras);
	}
	@GetMapping(value = "/{id}")
	public ResponseEntity<Editora> findById(@PathVariable Long id){
		Editora editora = service.findById(id);
		return ResponseEntity.ok().body(editora);
	}
	@PostMapping
	public ResponseEntity<Editora> insert(@Valid @RequestBody EditoraInsertDTO editoraDTO){
		Editora editora = service.insert(editoraDTO);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(editora.getId()).toUri();
		return ResponseEntity.created(uri).body(editora);
	}
	@PutMapping(value = "/{id}")
	public ResponseEntity<Editora> update(@PathVariable Long id, @Valid @RequestBody EditoraUpdateDTO entity){
		Editora editora = service.update(id, entity);
		return ResponseEntity.ok().body(editora);
	}
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id){
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
}
