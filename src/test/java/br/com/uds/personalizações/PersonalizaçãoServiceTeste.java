package br.com.uds.personalizações;

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
public class PersonalizaçãoServiceTeste
{
	@Autowired private PersonalizaçãoService service;
	
	private static final Personalização SEM_CEBOLA = new Personalização("Sem Cebola", 0, 0.0);
	private static final Personalização BACON_EXTRA = new Personalização("Bacon Extra", 0, 3.0);
	private static final Personalização BORDA_RECHEADA = new Personalização("Borda Recheada", 5, 5.0);
	
	@Test
	void excluirTamanho()
	{
		service.salvar(BORDA_RECHEADA);
		service.excluir(BORDA_RECHEADA);
		assertTrue(service.obterTodos().isEmpty());
	}
	
	@Test
	void salvarNovoTamanho()
	{
		service.salvar(BACON_EXTRA);
		assertEquals(BACON_EXTRA, service.obterTodos().get(0));
	}
	
	@Test
	void obterTodosOsTamanhos()
	{
		service.salvar(SEM_CEBOLA);
		service.salvar(BACON_EXTRA);
		service.salvar(BORDA_RECHEADA);
		assertEquals(Arrays.asList(SEM_CEBOLA, BACON_EXTRA, BORDA_RECHEADA), service.obterTodos());
	}
	
	@Test
	void salvarTamanhoExistente()
	{
		service.salvar(BACON_EXTRA);
		assertThrows(EntityExistsException.class, () -> service.salvar(BACON_EXTRA));
	}
	
	@Test
	void excluirTamanhoInexistente()
	{
		assertThrows(EntityNotFoundException.class, () -> service.excluir(BACON_EXTRA));
	}
}
