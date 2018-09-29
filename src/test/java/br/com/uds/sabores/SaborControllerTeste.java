
package br.com.uds.sabores;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.equalToIgnoringWhiteSpace;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@ComponentScan
@SpringJUnitConfig
@AutoConfigureDataJpa
@WebMvcTest(SaborController.class)
public class SaborControllerTeste
{
	@Autowired MockMvc mockMVC;
	@Autowired ObjectMapper mapper;
	@Autowired private SaborRepository repository;

	private static final String ENDEREÇO_DE_SALVAR = "/api/sabores/salvar";
	private static final String ENDEREÇO_DE_EXCLUIR = "/api/sabores/excluir";
	private static final String ENDEREÇO_DE_OBTER_TODOS = "/api/sabores/todos";

	private static final Sabor CALABRESA = new Sabor("Calabresa", 0);
	private static final Sabor MARGUERITA = new Sabor("Marguerita", 0);
	private static final Sabor PORTUGUESA = new Sabor("Portuguesa", 5);

	@AfterEach
	void limparBancoDeDadosDasSabores()
	{
		repository.deleteAll();
	}

	@Test
	void excluirSabor() throws JsonProcessingException, Exception
	{
		mockMVC	.perform(post(ENDEREÇO_DE_SALVAR).contentType(MediaType.APPLICATION_JSON_UTF8)
												 .content(mapper.writeValueAsString(PORTUGUESA)))
				.andReturn();

		mockMVC	.perform(delete(ENDEREÇO_DE_EXCLUIR).contentType(MediaType.APPLICATION_JSON_UTF8)
													.content(mapper.writeValueAsString(PORTUGUESA)))
				.andReturn();

		mockMVC	.perform(get(ENDEREÇO_DE_OBTER_TODOS))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().string(equalToIgnoringWhiteSpace("[]")))
				.andExpect(status().isOk());
	}

	@Test
	void adicionarSabor() throws JsonProcessingException, Exception
	{
		mockMVC	.perform(post(ENDEREÇO_DE_SALVAR).contentType(MediaType.APPLICATION_JSON_UTF8)
												 .content(mapper.writeValueAsString(PORTUGUESA)))
				.andExpect(status().isCreated())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.nome", equalTo(PORTUGUESA.getNome())))
				.andExpect(jsonPath("$.tempoAdicional", equalTo(PORTUGUESA.getTempoAdicional())));
	}
	
	@Test
	void obterTodosOsSabores() throws JsonProcessingException, Exception
	{
		mockMVC	.perform(post(ENDEREÇO_DE_SALVAR).contentType(MediaType.APPLICATION_JSON_UTF8)
												 .content(mapper.writeValueAsString(CALABRESA)))
				.andReturn();

		mockMVC	.perform(post(ENDEREÇO_DE_SALVAR).contentType(MediaType.APPLICATION_JSON_UTF8)
												 .content(mapper.writeValueAsString(MARGUERITA)))
				.andReturn();

		mockMVC	.perform(post(ENDEREÇO_DE_SALVAR).contentType(MediaType.APPLICATION_JSON_UTF8)
												 .content(mapper.writeValueAsString(PORTUGUESA)))
				.andReturn();

		mockMVC	.perform(get(ENDEREÇO_DE_OBTER_TODOS))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].nome", equalTo(CALABRESA.getNome())))
				.andExpect(jsonPath("$[1].nome", equalTo(MARGUERITA.getNome())))
				.andExpect(jsonPath("$[2].nome", equalTo(PORTUGUESA.getNome())))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
	}
	
	@Test
	void adicionarSaborExistente() throws JsonProcessingException, Exception
	{
		mockMVC	.perform(post(ENDEREÇO_DE_SALVAR).contentType(MediaType.APPLICATION_JSON_UTF8)
												 .content(mapper.writeValueAsString(CALABRESA)))
				.andReturn();

		mockMVC	.perform(post(ENDEREÇO_DE_SALVAR).contentType(MediaType.APPLICATION_JSON_UTF8)
												 .content(mapper.writeValueAsString(CALABRESA)))
				.andExpect(status().isBadRequest())
				.andExpect(content().string(not(isEmptyOrNullString())))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
	}
	
	@Test
	void excluirSaborInexistente() throws JsonProcessingException, Exception
	{
		mockMVC	.perform(delete(ENDEREÇO_DE_EXCLUIR).contentType(MediaType.APPLICATION_JSON_UTF8)
													.content(mapper.writeValueAsString(CALABRESA)))
				.andExpect(status().isBadRequest())
				.andExpect(content().string(not(isEmptyOrNullString())))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
	}

	@Test
	void adicionarSaborComNomeVazio() throws JsonProcessingException, Exception
	{
		Sabor sabor = new Sabor("", 0);
		
		mockMVC	.perform(post(ENDEREÇO_DE_SALVAR).contentType(MediaType.APPLICATION_JSON_UTF8)
												 .content(mapper.writeValueAsString(sabor)))
				.andExpect(status().isBadRequest())
				.andExpect(content().string(not(isEmptyOrNullString())))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
	}
	
	@Test
	void adicionarSaborComNomeNulo() throws JsonProcessingException, Exception
	{
		Sabor sabor = new Sabor(null, 0);
		
		mockMVC	.perform(post(ENDEREÇO_DE_SALVAR).contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(mapper.writeValueAsString(sabor)))
		.andExpect(status().isBadRequest())
		.andExpect(content().string(not(isEmptyOrNullString())))
		.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
	}
	
	@Test
	void adicionarSaborComTempoAdicionalNegativo() throws JsonProcessingException, Exception
	{
		Sabor sabor = new Sabor("Sabor", -1);
		
		mockMVC	.perform(post(ENDEREÇO_DE_SALVAR).contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(mapper.writeValueAsString(sabor)))
		.andExpect(status().isBadRequest())
		.andExpect(content().string(not(isEmptyOrNullString())))
		.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
	}
}
