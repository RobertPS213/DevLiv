package com.devlib.devlib.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devlib.devlib.entites.Estante;
import com.devlib.devlib.services.EstanteService;

@RestController
@RequestMapping(value = "/estante")
public class EstanteController {
	
	@Autowired
	private EstanteService service;
}
