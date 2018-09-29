package br.com.uds.personalizações;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api/personalizacoes")
public class PersonalizaçãoController
{
	@Autowired
	private PersonalizaçãoService service;
	
	@GetMapping("/todos")
	public List<Personalização> obterTodos()
	{
		return service.obterTodos();
	}
	
	@PostMapping("/salvar")
	@ResponseStatus(HttpStatus.CREATED)
	public Personalização salvar(@Valid @RequestBody Personalização personalização)
	{
		return service.salvar(personalização);
	}
	
	@DeleteMapping("/excluir")
	public void excluir(@Valid @RequestBody Personalização personalização)
	{
		service.excluir(personalização);
	}
}
