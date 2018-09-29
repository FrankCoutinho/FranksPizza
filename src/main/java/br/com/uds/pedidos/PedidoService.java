
package br.com.uds.pedidos;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.uds.personalizações.Personalização;
import br.com.uds.personalizações.PersonalizaçãoRepository;
import br.com.uds.sabores.Sabor;
import br.com.uds.sabores.SaborRepository;
import br.com.uds.tamanhos.Tamanho;
import br.com.uds.tamanhos.TamanhoRepository;

@Service
public class PedidoService
{
	@Autowired private SaborRepository saborRepository;
	@Autowired private PedidoRepository pedidoRepository;
	@Autowired private TamanhoRepository tamanhoRepository;
	@Autowired private PersonalizaçãoRepository personalizaçãoRepository;

	public List<Pedido> obterTodos()
	{
		List<Pedido> pedidos = pedidoRepository.findAll();
		pedidos.forEach(pedido -> pedido.calcularValorTotal());
		pedidos.forEach(pedido -> pedido.calcularTempoDePreparo());

		return pedidos;
	}

	@Transactional
	public Pedido salvar(Pedido pedido)
	{
		Optional<Sabor> saborResultado = saborRepository.findByNomeIgnoreCase(pedido.getSabor().getNome());
		
		Optional<Tamanho> tamanhoResultado = tamanhoRepository.findByNomeIgnoreCase(pedido.getTamanho().getNome());

		if (!saborResultado.isPresent())
			throw new EntityNotFoundException("O sabor de pizza não foi encontrado");

		if (!tamanhoResultado.isPresent())
			throw new EntityNotFoundException("O tamanho de pizza não foi encontrado");

		Set<Personalização> personalizações;
		try
		{
			personalizações = pedido.getPersonalizações()
									.stream()
									.map(p -> personalizaçãoRepository.findByDescriçãoIgnoreCase(p.getDescrição()))
									.map(personalização -> personalização.get())
									.collect(Collectors.toSet());
		}
		catch (NoSuchElementException exception)
		{
			throw new EntityNotFoundException("Personalização não foi encontrada");
		}

		Pedido novoPedido = new Pedido(saborResultado.get(), tamanhoResultado.get(), personalizações.toArray(new Personalização[0]));

		pedidoRepository.save(novoPedido);

		novoPedido.calcularValorTotal();
		novoPedido.calcularTempoDePreparo();

		return novoPedido;
	}

	public void excluir(Pedido pedido)
	{
		Optional<Pedido> resultado = pedidoRepository.findById(pedido.getId());
		
		if (!resultado.isPresent())
			throw new EntityNotFoundException("O pedido não foi encontrado");
		
		pedidoRepository.delete(resultado.get());
	}
}
