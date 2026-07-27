package com.devlib.devlib.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devlib.devlib.repositories.EstanteRepository;

@Service
public class EstanteService {

	@Autowired
	private EstanteRepository repository;
}
