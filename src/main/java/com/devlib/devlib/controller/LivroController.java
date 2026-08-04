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

import com.devlib.devlib.DTO.LivroDTO;
import com.devlib.devlib.DTO.LivroUpdateDTO;
import com.devlib.devlib.entites.Livro;
import com.devlib.devlib.services.LivroService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/livros")
public class LivroController {

	@Autowired
	private LivroService service;
	
	@GetMapping
	public ResponseEntity<List<Livro>> findAll(){
		List<Livro> livros = service.findAll();
		return ResponseEntity.ok().body(livros);
	}
	@GetMapping(value = "/{id}")
	public ResponseEntity<Livro> findById(@PathVariable Long id){
		Livro livro = service.findById(id);
		return ResponseEntity.ok().body(livro);
	}
	@PostMapping
	public ResponseEntity<Livro> insert(@Valid @RequestBody LivroDTO livroDTO){
		Livro livroRecebido = service.insert(livroDTO);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(livroRecebido.getId()).toUri();
		return ResponseEntity.created(uri).body(livroRecebido);
	}
	@PutMapping(value = "/{id}")
	public ResponseEntity<Livro> update(@PathVariable Long id, @Valid @RequestBody LivroUpdateDTO entity){
		Livro livro = service.update(id, entity);
		return ResponseEntity.ok().body(livro);
	}
	@PostMapping(value = "/{livroId}/autores/{autorId}")
	public ResponseEntity<Livro> adicionarAutores(@PathVariable Long livroId, @PathVariable Long autorId){
		Livro livro = service.adicionarAutor(livroId, autorId);
		return ResponseEntity.ok().body(livro);
	}
	@DeleteMapping(value = "/{livroId}/autores/{autorId}")
	public ResponseEntity<Livro> removerAutores(@PathVariable Long livroId, @PathVariable Long autorId){
		Livro livro = service.removerAutor(livroId, autorId);
		return ResponseEntity.ok().body(livro);
	}
	@PostMapping(value = "/{livroId}/categorias/{categoriaId}")
	public ResponseEntity<Livro> adicionarCategoria(@PathVariable Long livroId, @PathVariable Long categoriaId){
		Livro livro = service.adicionarCategoria(livroId, categoriaId);
		return ResponseEntity.ok().body(livro);
	}
	@DeleteMapping(value = "/{livroId}/categorias/{categoriaId}")
	public ResponseEntity<Livro> removeCategoria(@PathVariable Long livroId, @PathVariable Long categoriaId){
		Livro livro = service.removerCategoria(livroId, categoriaId);
		return ResponseEntity.ok().body(livro);
	}
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id){
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
}

