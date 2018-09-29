
package br.com.uds.tamanhos;

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
@WebMvcTest(TamanhoController.class)
public class TamanhoControllerTeste
{
	@Autowired MockMvc mockMVC;
	@Autowired ObjectMapper mapper;
	@Autowired private TamanhoRepository repository;
	
	private static final String ENDEREÇO_DE_SALVAR = "/api/tamanhos/salvar";
	private static final String ENDEREÇO_DE_EXCLUIR = "/api/tamanhos/excluir";
	private static final String ENDEREÇO_DE_OBTER_TODOS = "/api/tamanhos/todos";

	private static final Tamanho MÉDIA = new Tamanho("Médio", 20, 30.0);
	private static final Tamanho GRANDE = new Tamanho("Grande", 25, 40.0);
	private static final Tamanho PEQUENA = new Tamanho("Pequena", 15, 20.0);

	@AfterEach
	void limparBancoDeDadosDasTamanhos()
	{
		repository.deleteAll();
	}
	
	@Test
	void obterTodos() throws JsonProcessingException, Exception
	{
		mockMVC	.perform(post(ENDEREÇO_DE_SALVAR).contentType(MediaType.APPLICATION_JSON_UTF8)
												 .content(mapper.writeValueAsString(MÉDIA)))
				.andReturn();

		mockMVC	.perform(post(ENDEREÇO_DE_SALVAR).contentType(MediaType.APPLICATION_JSON_UTF8)
												 .content(mapper.writeValueAsString(GRANDE)))
				.andReturn();

		mockMVC	.perform(post(ENDEREÇO_DE_SALVAR).contentType(MediaType.APPLICATION_JSON_UTF8)
												 .content(mapper.writeValueAsString(PEQUENA)))
				.andReturn();

		mockMVC	.perform(get(ENDEREÇO_DE_OBTER_TODOS))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].nome", equalTo(MÉDIA.getNome())))
				.andExpect(jsonPath("$[1].nome", equalTo(GRANDE.getNome())))
				.andExpect(jsonPath("$[2].nome", equalTo(PEQUENA.getNome())))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
	}

	@Test
	void excluirTamanho() throws JsonProcessingException, Exception
	{
		mockMVC	.perform(post(ENDEREÇO_DE_SALVAR).contentType(MediaType.APPLICATION_JSON_UTF8)
												 .content(mapper.writeValueAsString(PEQUENA)))
				.andReturn();

		mockMVC	.perform(delete(ENDEREÇO_DE_EXCLUIR).contentType(MediaType.APPLICATION_JSON_UTF8)
													.content(mapper.writeValueAsString(PEQUENA)))
				.andReturn();

		mockMVC	.perform(get(ENDEREÇO_DE_OBTER_TODOS))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().string(equalToIgnoringWhiteSpace("[]")))
				.andExpect(status().isOk());
	}

	@Test
	void adicionarTamanho() throws JsonProcessingException, Exception
	{
		mockMVC	.perform(post(ENDEREÇO_DE_SALVAR).contentType(MediaType.APPLICATION_JSON_UTF8)
												 .content(mapper.writeValueAsString(PEQUENA)))
				.andExpect(status().isCreated())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.nome", equalTo(PEQUENA.getNome())))
				.andExpect(jsonPath("$.custo", equalTo(PEQUENA.getCusto())))
				.andExpect(jsonPath("$.tempoDePreparo", equalTo(PEQUENA.getTempoDePreparo())));
	}
	
	@Test
	void adicionarTamanhoExistente() throws JsonProcessingException, Exception
	{
		mockMVC	.perform(post(ENDEREÇO_DE_SALVAR).contentType(MediaType.APPLICATION_JSON_UTF8)
												 .content(mapper.writeValueAsString(MÉDIA)))
				.andReturn();

		mockMVC	.perform(post(ENDEREÇO_DE_SALVAR).contentType(MediaType.APPLICATION_JSON_UTF8)
												 .content(mapper.writeValueAsString(MÉDIA)))
				.andExpect(status().isBadRequest())
				.andExpect(content().string(not(isEmptyOrNullString())))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
	}
	
	@Test
	void excluirTamanhoInexistente() throws JsonProcessingException, Exception
	{
		mockMVC	.perform(delete(ENDEREÇO_DE_EXCLUIR).contentType(MediaType.APPLICATION_JSON_UTF8)
													.content(mapper.writeValueAsString(MÉDIA)))
				.andExpect(status().isBadRequest())
				.andExpect(content().string(not(isEmptyOrNullString())))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
	}

	@Test
	void adicionarTamanhoComNomeVazio() throws JsonProcessingException, Exception
	{
		Tamanho tamanho = new Tamanho("", 20, 30.0);
		
		mockMVC	.perform(post(ENDEREÇO_DE_SALVAR).contentType(MediaType.APPLICATION_JSON_UTF8)
												 .content(mapper.writeValueAsString(tamanho)))
				.andExpect(status().isBadRequest())
				.andExpect(content().string(not(isEmptyOrNullString())))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
	}
	
	@Test
	void adicionarTamanhoComNomeNulo() throws JsonProcessingException, Exception
	{
		Tamanho tamanho = new Tamanho(null, 20, 30.0);
		
		mockMVC	.perform(post(ENDEREÇO_DE_SALVAR).contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(mapper.writeValueAsString(tamanho)))
		.andExpect(status().isBadRequest())
		.andExpect(content().string(not(isEmptyOrNullString())))
		.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
	}
	
	@Test
	void adicionarTamanhoComCustoNegativo() throws JsonProcessingException, Exception
	{
		Tamanho tamanho = new Tamanho("Tamanho", 20, -1.0);
		
		mockMVC	.perform(post(ENDEREÇO_DE_SALVAR).contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(mapper.writeValueAsString(tamanho)))
		.andExpect(status().isBadRequest())
		.andExpect(content().string(not(isEmptyOrNullString())))
		.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
	}

	@Test
	void adicionarTamanhoComTempoDePreparoNegativo() throws JsonProcessingException, Exception
	{
		Tamanho tamanho = new Tamanho("Tamanho", -1, 30.0);
		
		mockMVC	.perform(post(ENDEREÇO_DE_SALVAR).contentType(MediaType.APPLICATION_JSON_UTF8)
												 .content(mapper.writeValueAsString(tamanho)))
				.andExpect(status().isBadRequest())
				.andExpect(content().string(not(isEmptyOrNullString())))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
	}
}
