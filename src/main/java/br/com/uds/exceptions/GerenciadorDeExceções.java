
package br.com.uds.exceptions;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GerenciadorDeExceções extends ResponseEntityExceptionHandler
{
	@ExceptionHandler(value = { EntityExistsException.class, EntityNotFoundException.class })
	protected ResponseEntity<Object> resolverBadRequests(PersistenceException exception, WebRequest request)
	{
		return criarResponseEntity(exception.getMessage(), exception, HttpStatus.BAD_REQUEST, request);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception, 
																  HttpHeaders headers, 
																  HttpStatus status,
																  WebRequest request)
	{
		return criarResponseEntityDeErroDeValidação(exception, headers, request);
	}

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException exception, 
																  HttpHeaders headers, 
																  HttpStatus status,
																  WebRequest request)
	{
		return criarResponseEntity(exception.getMessage(), exception, HttpStatus.BAD_REQUEST, request);
	}
	
	private ResponseEntity<Object> criarResponseEntityDeErroDeValidação(MethodArgumentNotValidException exception, HttpHeaders headers, WebRequest request)
	{
		FieldError fieldError = exception.getBindingResult().getFieldError();
		String mensagem = fieldError.getField() + " " + fieldError.getDefaultMessage();
		DetalhesDaExceção detalhes = new DetalhesDaExceção(mensagem, HttpStatus.BAD_REQUEST);
		return handleExceptionInternal(exception, detalhes, headers, HttpStatus.BAD_REQUEST, request);
	}
	
	private ResponseEntity<Object> criarResponseEntity(String mensagem, RuntimeException exception, HttpStatus status, WebRequest request)
	{
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		DetalhesDaExceção detalhes = new DetalhesDaExceção(mensagem, status);

		return handleExceptionInternal(exception, detalhes, headers, HttpStatus.BAD_REQUEST, request);
	}
}
