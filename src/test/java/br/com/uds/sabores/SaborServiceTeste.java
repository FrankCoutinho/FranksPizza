package br.com.uds.sabores;

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
public class SaborServiceTeste
{
	@Autowired
	SaborService service;
	
	private static final Sabor CALABRESA = new Sabor("Calabresa", 0);
	private static final Sabor MARGUERITA = new Sabor("Marguerita", 0);
	private static final Sabor PORTUGUESA = new Sabor("Portuguesa", 5);
	
	@Test
	void excluirSabor()
	{
		service.salvar(PORTUGUESA);
		service.excluir(PORTUGUESA);
		assertTrue(service.obterTodos().isEmpty());
	}
	
	@Test
	void salvarSabor()
	{
		service.salvar(MARGUERITA);
		assertEquals(MARGUERITA, service.obterTodos().get(0));
	}
	
	@Test
	void obterTodosOsSabores()
	{
		service.salvar(CALABRESA);
		service.salvar(MARGUERITA);
		service.salvar(PORTUGUESA);
		assertEquals(Arrays.asList(CALABRESA, MARGUERITA, PORTUGUESA), service.obterTodos());
	}
	
	@Test
	void salvarSaborExistente()
	{
		service.salvar(MARGUERITA);
		assertThrows(EntityExistsException.class, () -> service.salvar(MARGUERITA));
	}
	
	@Test
	void excluirSaborInexistente()
	{
		assertThrows(EntityNotFoundException.class, () -> service.excluir(MARGUERITA));
	}
}
