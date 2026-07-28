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

import com.devlib.devlib.entites.Livro;
import com.devlib.devlib.services.LivroService;

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
	public ResponseEntity<Livro> insert(@RequestBody Livro livro){
		livro = service.insert(livro);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(livro.getId()).toUri();
		return ResponseEntity.created(uri).body(livro);
	}
	@PutMapping(value = "/{id}")
	public ResponseEntity<Livro> update(@PathVariable Long id, @RequestBody Livro entity){
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

