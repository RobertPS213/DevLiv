package com.devlib.devlib.services.exceptions;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ResourceExceptionHandler {

	LocalDateTime date = LocalDateTime.now();
	DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
	
	@ExceptionHandler(ExceptionBase.class)
	public ResponseEntity<StandardError> exceptions(ExceptionBase e, HttpServletRequest request){
		HttpStatus status  = HttpStatus.NOT_FOUND;
		StandardError err = new StandardError(
				date.format(fmt),
				status.value(),
				e.getMessage(),
				request.getRequestURI()
		);
		return ResponseEntity.status(status).body(err);
	}
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<StandardError> validation(MethodArgumentNotValidException e, HttpServletRequest request){
		HttpStatus status = HttpStatus.BAD_REQUEST;
		String mensagemDeErro = e.getBindingResult().getFieldError().getDefaultMessage();
		StandardError err = new StandardError(
				date.format(fmt),
				status.value(),
				mensagemDeErro,
				request.getRequestURI()
		);
		return ResponseEntity.status(status).body(err);
	}
}
