package com.devlib.devlib.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devlib.devlib.repositories.LivroRepository;

@Service
public class LivroService {
	
	@Autowired
	private LivroRepository repository;
}
