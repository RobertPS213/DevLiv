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

import com.devlib.devlib.dto.insert.AutorInsertDTO;
import com.devlib.devlib.dto.response.AutorResponseDTO;
import com.devlib.devlib.dto.update.AutorUpdateDTO;
import com.devlib.devlib.services.AutorService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/autores")
public class AutorController {

	@Autowired
	private AutorService service;
	
	@GetMapping
	public ResponseEntity<List<AutorResponseDTO>> findAll(){
		List<AutorResponseDTO> autoresDTO = service.findAll();
		return ResponseEntity.ok().body(autoresDTO);
	}
	@GetMapping(value = "/{id}")
	public ResponseEntity<AutorResponseDTO> findById(@PathVariable Long id){
		AutorResponseDTO autorDTO = service.findById(id);
		return ResponseEntity.ok().body(autorDTO);
	}
	@PostMapping
	public ResponseEntity<AutorResponseDTO> insert(@Valid @RequestBody AutorInsertDTO autorDTO){
		AutorResponseDTO autor = service.insert(autorDTO);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(autor.getId()).toUri();
		return ResponseEntity.created(uri).body(autor);
	}
	@PutMapping(value = "/{id}")
	public ResponseEntity<AutorResponseDTO> update(@PathVariable Long id, @Valid @RequestBody AutorUpdateDTO entity){
		AutorResponseDTO autor = service.update(id, entity);
		return ResponseEntity.ok().body(autor);
	}
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id){
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
}
