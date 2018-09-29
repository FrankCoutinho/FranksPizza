package br.com.uds.pedidos;

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
@RequestMapping(value = "api/pedidos")
public class PedidoController
{
	@Autowired
	private PedidoService service;
	
	@GetMapping("/todos")
	public List<Pedido> obterTodos()
	{
		return service.obterTodos();
	}
	
	@PostMapping("/salvar")
	@ResponseStatus(HttpStatus.CREATED)
	public Pedido salvar(@Valid @RequestBody Pedido pedido)
	{
		return service.salvar(pedido);
	}
	
	@DeleteMapping("/excluir")
	public void excluir(@Valid @RequestBody Pedido pedido)
	{
		service.excluir(pedido);
	}
}
