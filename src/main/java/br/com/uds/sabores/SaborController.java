package br.com.uds.sabores;

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
@RequestMapping(value = "api/sabores")
public class SaborController
{
	@Autowired
	private SaborService service;
	
	@GetMapping("/todos")
	public List<Sabor> obterTodos()
	{
		return service.obterTodos();
	}
	
	@PostMapping("/salvar")
	@ResponseStatus(HttpStatus.CREATED)
	public Sabor salvar(@Valid @RequestBody Sabor sabor)
	{
		return service.salvar(sabor);
	}
	
	@DeleteMapping("/excluir")
	public void excluir(@Valid @RequestBody Sabor sabor)
	{
		service.excluir(sabor);
	}
}
