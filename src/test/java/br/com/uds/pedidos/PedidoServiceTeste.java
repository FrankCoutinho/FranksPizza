package br.com.uds.pedidos;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import br.com.uds.personalizações.Personalização;
import br.com.uds.personalizações.PersonalizaçãoRepository;
import br.com.uds.sabores.Sabor;
import br.com.uds.sabores.SaborRepository;
import br.com.uds.tamanhos.Tamanho;
import br.com.uds.tamanhos.TamanhoRepository;

@DataJpaTest
@ComponentScan
@SpringJUnitConfig
public class PedidoServiceTeste
{
	@Autowired private PedidoService service;
	
	@Autowired private SaborRepository saborRepository;
	@Autowired private TamanhoRepository tamanhoRepository;
	@Autowired private PersonalizaçãoRepository personalizaçãoRepository;
	
	private static final Sabor CALABRESA = new Sabor("Calabresa", 0);
	private static final Sabor MARGUERITA = new Sabor("Marguerita", 0);
	private static final Sabor PORTUGUESA = new Sabor("Portuguesa", 5);
	
	private static final Tamanho MÉDIA = new Tamanho("Médio", 20, 30.0);
	private static final Tamanho GRANDE = new Tamanho("Grande", 25, 40.0);
	private static final Tamanho PEQUENA = new Tamanho("Pequena", 15, 20.0);
	
	private static final Personalização SEM_CEBOLA = new Personalização("Sem Cebola", 0, 0.0);
	private static final Personalização BACON_EXTRA = new Personalização("Bacon Extra", 0, 3.0);
	private static final Personalização BORDA_RECHEADA = new Personalização("Borda Recheada", 5, 5.0);
	
	private static final Pedido PORTUGUESA_GRANDE = new Pedido(PORTUGUESA, GRANDE);
	private static final Pedido MARGUERITA_PEQUENA_BACON_EXTRA = new Pedido(MARGUERITA, PEQUENA, BACON_EXTRA);
	private static final Pedido CALABREA_MÉDIA_SEM_CEBOLA_BORDA_RECHEADA = new Pedido(CALABRESA, MÉDIA, SEM_CEBOLA, BORDA_RECHEADA);
	
	@BeforeEach
	void inicializarBancoDeDados()
	{
		tamanhoRepository.saveAll(Arrays.asList(MÉDIA, GRANDE, PEQUENA));
		saborRepository.saveAll(Arrays.asList(CALABRESA, MARGUERITA, PORTUGUESA));
		personalizaçãoRepository.saveAll(Arrays.asList(SEM_CEBOLA, BACON_EXTRA, BORDA_RECHEADA));
	}

	@Test
	void excluirPedido()
	{
		Pedido pedido = service.salvar(CALABREA_MÉDIA_SEM_CEBOLA_BORDA_RECHEADA);
		service.excluir(pedido);
		
		assertTrue(service.obterTodos().isEmpty());
	}
	
	@Test
	void salvarNovoPedido()
	{
		Pedido pedido = service.salvar(MARGUERITA_PEQUENA_BACON_EXTRA);
		assertEquals(pedido, service.obterTodos().get(0));
	}
	
	@Test
	void obterTodosOsPedidos()
	{
		Pedido pedido1 = service.salvar(PORTUGUESA_GRANDE);
		Pedido pedido2 = service.salvar(MARGUERITA_PEQUENA_BACON_EXTRA);
		Pedido pedido3 = service.salvar(CALABREA_MÉDIA_SEM_CEBOLA_BORDA_RECHEADA);
		
		List<Pedido> pedidos = Arrays.asList(pedido1, pedido2, pedido3);
		
		assertEquals(pedidos, service.obterTodos());
	}
	
	@Test
	void excluirPedidoInexistente()
	{
		Pedido pedido = PORTUGUESA_GRANDE;
		pedido.setId(1L);
		assertThrows(EntityNotFoundException.class, () -> service.excluir(pedido));
	}

	@Test
	void salvarPedidoComSaborInexistente()
	{
		assertThrows(EntityNotFoundException.class, () -> service.salvar(new Pedido(new Sabor("Inexistente", 0), MÉDIA)));
	}
	
	@Test
	void salvarPedidoComTamanhoInexistente()
	{
		assertThrows(EntityNotFoundException.class, () -> service.salvar(new Pedido(PORTUGUESA, new Tamanho("Inexistente", 0, 0))));
	}
	
	@Test
	void verificarAChamadaDoCálculoDoValorTotal()
	{
		Pedido pedido = service.salvar(CALABREA_MÉDIA_SEM_CEBOLA_BORDA_RECHEADA);
		assertTrue(pedido.getValorTotal() > 0);
	}
	
	@Test
	void salvarPedidoComPersonalizaçõesInexistentes()
	{
		Personalização p1 = new Personalização("Inexistente 1", 0, 0.0);
		Personalização p2 = new Personalização("Inexistente 2", 0, 0.0);
		assertThrows(EntityNotFoundException.class, () -> service.salvar(new Pedido(CALABRESA, PEQUENA, p1, p2)));
	}

	@Test
	void verificarAChamadaDoCálculoDoTempoDePreparo()
	{
		Pedido pedido = service.salvar(MARGUERITA_PEQUENA_BACON_EXTRA);
		assertTrue(pedido.getTempoDePreparo() > 0);
	}
}
