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

import com.devlib.devlib.dto.insert.LivroInsertDTO;
import com.devlib.devlib.dto.response.LivroResponseDTO;
import com.devlib.devlib.dto.update.LivroUpdateDTO;
import com.devlib.devlib.services.LivroService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/livros")
public class LivroController {

	@Autowired
	private LivroService service;
	
	@GetMapping
	public ResponseEntity<List<LivroResponseDTO>> findAll(){
		List<LivroResponseDTO> livros = service.findAll();
		return ResponseEntity.ok().body(livros);
	}
	@GetMapping(value = "/{id}")
	public ResponseEntity<LivroResponseDTO> findById(@PathVariable Long id){
		LivroResponseDTO livro = service.findById(id);
		return ResponseEntity.ok().body(livro);
	}
	@PostMapping
	public ResponseEntity<LivroResponseDTO> insert(@Valid @RequestBody LivroInsertDTO livroDTO){
		LivroResponseDTO livroRecebido = service.insert(livroDTO);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(livroRecebido.getId()).toUri();
		return ResponseEntity.created(uri).body(livroRecebido);
	}
	@PutMapping(value = "/{id}")
	public ResponseEntity<LivroResponseDTO> update(@PathVariable Long id, @Valid @RequestBody LivroUpdateDTO entity){
		LivroResponseDTO livro = service.update(id, entity);
		return ResponseEntity.ok().body(livro);
	}
	@PostMapping(value = "/{livroId}/autores/{autorId}")
	public ResponseEntity<LivroResponseDTO> adicionarAutores(@PathVariable Long livroId, @PathVariable Long autorId){
		LivroResponseDTO livro = service.adicionarAutor(livroId, autorId);
		return ResponseEntity.ok().body(livro);
	}
	@DeleteMapping(value = "/{livroId}/autores/{autorId}")
	public ResponseEntity<LivroResponseDTO> removerAutores(@PathVariable Long livroId, @PathVariable Long autorId){
		LivroResponseDTO livro = service.removerAutor(livroId, autorId);
		return ResponseEntity.ok().body(livro);
	}
	@PostMapping(value = "/{livroId}/categorias/{categoriaId}")
	public ResponseEntity<LivroResponseDTO> adicionarCategoria(@PathVariable Long livroId, @PathVariable Long categoriaId){
		LivroResponseDTO livro = service.adicionarCategoria(livroId, categoriaId);
		return ResponseEntity.ok().body(livro);
	}
	@DeleteMapping(value = "/{livroId}/categorias/{categoriaId}")
	public ResponseEntity<LivroResponseDTO> removeCategoria(@PathVariable Long livroId, @PathVariable Long categoriaId){
		LivroResponseDTO livro = service.removerCategoria(livroId, categoriaId);
		return ResponseEntity.ok().body(livro);
	}
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id){
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
}

