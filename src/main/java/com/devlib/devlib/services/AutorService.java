package com.devlib.devlib.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devlib.devlib.repositories.AutorRepository;

@Service
public class AutorService {

	@Autowired
	private AutorRepository autorRepository;
}
