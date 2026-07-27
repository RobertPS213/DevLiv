package com.devlib.devlib.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devlib.devlib.entites.Estante;
import com.devlib.devlib.repositories.EstanteRepository;

@Service
public class EstanteService {

	@Autowired
	private EstanteRepository repository;
}
