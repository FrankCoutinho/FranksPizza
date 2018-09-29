package br.com.uds.tamanhos;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@DataJpaTest
@ComponentScan
@SpringJUnitConfig
public class TamanhoServiceTeste
{
	@Autowired TamanhoService service;
	
	private static final Tamanho MÉDIA = new Tamanho("Médio", 20, 30.0);
	private static final Tamanho GRANDE = new Tamanho("Grande", 25, 40.0);
	private static final Tamanho PEQUENA = new Tamanho("Pequena", 15, 20.0);
	
	@Test
	void excluirTamanho()
	{
		service.salvar(PEQUENA);
		service.excluir(PEQUENA);
		assertTrue(service.obterTodos().isEmpty());
	}
	
	@Test
	void salvarNovoTamanho()
	{
		service.salvar(GRANDE);
		assertEquals(GRANDE, service.obterTodos().get(0));
	}
	
	@Test
	void obterTodosOsTamanhos()
	{
		service.salvar(MÉDIA);
		service.salvar(GRANDE);
		service.salvar(PEQUENA);
		assertEquals(Arrays.asList(MÉDIA, GRANDE, PEQUENA), service.obterTodos());
	}
	
	@Test
	void salvarTamanhoExistente()
	{
		service.salvar(GRANDE);
		assertThrows(EntityExistsException.class, () -> service.salvar(GRANDE));
	}
	
	@Test
	void excluirTamanhoInexistente()
	{
		assertThrows(EntityNotFoundException.class, () -> service.excluir(GRANDE));
	}
}
