package com.devlib.devlib.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devlib.devlib.services.EditoraService;

@RestController
@RequestMapping(value = "/editora")
public class EditoraController {

	@Autowired
	private EditoraService editoraService;
}
