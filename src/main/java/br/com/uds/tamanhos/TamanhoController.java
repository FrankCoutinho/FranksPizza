package br.com.uds.tamanhos;

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
@RequestMapping(value = "api/tamanhos")
public class TamanhoController
{
	@Autowired
	private TamanhoService service;
	
	@GetMapping("/todos")
	public List<Tamanho> obterTodos()
	{
		return service.obterTodos();
	}
	
	@PostMapping("/salvar")
	@ResponseStatus(HttpStatus.CREATED)
	public Tamanho salvar(@Valid @RequestBody Tamanho tamanho)
	{
		return service.salvar(tamanho);
	}
	
	@DeleteMapping("/excluir")
	public void excluir(@Valid @RequestBody Tamanho tamanho)
	{
		service.excluir(tamanho);
	}
}
