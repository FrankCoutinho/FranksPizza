
package br.com.uds.personalizações;

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
@WebMvcTest(PersonalizaçãoController.class)
public class PersonalizaçãoControllerTeste
{
	@Autowired MockMvc mockMVC;
	@Autowired ObjectMapper mapper;
	@Autowired private PersonalizaçãoRepository repository;

	private static final String ENDEREÇO_DE_SALVAR = "/api/personalizacoes/salvar";
	private static final String ENDEREÇO_DE_EXCLUIR = "/api/personalizacoes/excluir";
	private static final String ENDEREÇO_DE_OBTER_TODOS = "/api/personalizacoes/todos";

	private static final Personalização SEM_CEBOLA = new Personalização("Sem Cebola", 0, 0.0);
	private static final Personalização BACON_EXTRA = new Personalização("Bacon Extra", 0, 3.0);
	private static final Personalização BORDA_RECHEADA = new Personalização("Borda Recheada", 5, 5.0);
	
	@AfterEach
	void limparBancoDeDadosDasPersonalizações()
	{
		repository.deleteAll();
	}
	
	@Test
	void obterTodasAsPersonalizações() throws JsonProcessingException, Exception
	{
		mockMVC	.perform(post(ENDEREÇO_DE_SALVAR).contentType(MediaType.APPLICATION_JSON_UTF8)
												 .content(mapper.writeValueAsString(SEM_CEBOLA)))
				.andReturn();

		mockMVC	.perform(post(ENDEREÇO_DE_SALVAR).contentType(MediaType.APPLICATION_JSON_UTF8)
												 .content(mapper.writeValueAsString(BACON_EXTRA)))
				.andReturn();

		mockMVC	.perform(post(ENDEREÇO_DE_SALVAR).contentType(MediaType.APPLICATION_JSON_UTF8)
												 .content(mapper.writeValueAsString(BORDA_RECHEADA)))
				.andReturn();

		mockMVC	.perform(get(ENDEREÇO_DE_OBTER_TODOS))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].descrição", equalTo(SEM_CEBOLA.getDescrição())))
				.andExpect(jsonPath("$[1].descrição", equalTo(BACON_EXTRA.getDescrição())))
				.andExpect(jsonPath("$[2].descrição", equalTo(BORDA_RECHEADA.getDescrição())))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
	}

	@Test
	void excluirPersonalização() throws JsonProcessingException, Exception
	{
		mockMVC	.perform(post(ENDEREÇO_DE_SALVAR).contentType(MediaType.APPLICATION_JSON_UTF8)
												 .content(mapper.writeValueAsString(BORDA_RECHEADA)))
				.andReturn();

		mockMVC	.perform(delete(ENDEREÇO_DE_EXCLUIR).contentType(MediaType.APPLICATION_JSON_UTF8)
													.content(mapper.writeValueAsString(BORDA_RECHEADA)))
				.andReturn();

		mockMVC	.perform(get(ENDEREÇO_DE_OBTER_TODOS))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().string(equalToIgnoringWhiteSpace("[]")))
				.andExpect(status().isOk());
	}

	@Test
	void adicionarPersonalização() throws JsonProcessingException, Exception
	{
		mockMVC	.perform(post(ENDEREÇO_DE_SALVAR).contentType(MediaType.APPLICATION_JSON_UTF8)
												 .content(mapper.writeValueAsString(BORDA_RECHEADA)))
				.andExpect(status().isCreated())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.descrição", equalTo(BORDA_RECHEADA.getDescrição())))
				.andExpect(jsonPath("$.tempoAdicional", equalTo(BORDA_RECHEADA.getTempoAdicional())))
				.andExpect(jsonPath("$.custoAdicional", equalTo(BORDA_RECHEADA.getCustoAdicional())));
	}
	
	@Test
	void adicionarPersonalizaçãoExistente() throws JsonProcessingException, Exception
	{
		mockMVC	.perform(post(ENDEREÇO_DE_SALVAR).contentType(MediaType.APPLICATION_JSON_UTF8)
												 .content(mapper.writeValueAsString(SEM_CEBOLA)))
				.andReturn();

		mockMVC	.perform(post(ENDEREÇO_DE_SALVAR).contentType(MediaType.APPLICATION_JSON_UTF8)
												 .content(mapper.writeValueAsString(SEM_CEBOLA)))
				.andExpect(status().isBadRequest())
				.andExpect(content().string(not(isEmptyOrNullString())))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
	}
	
	@Test
	void excluirPersonalizaçãoInexistente() throws JsonProcessingException, Exception
	{
		mockMVC	.perform(delete(ENDEREÇO_DE_EXCLUIR).contentType(MediaType.APPLICATION_JSON_UTF8)
													.content(mapper.writeValueAsString(SEM_CEBOLA)))
				.andExpect(status().isBadRequest())
				.andExpect(content().string(not(isEmptyOrNullString())))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
	}

	@Test
	void adicionarPersonalizaçãoComDescriçãoVazia() throws JsonProcessingException, Exception
	{
		Personalização personalização = new Personalização("", 0, 0.0);
		
		mockMVC	.perform(post(ENDEREÇO_DE_SALVAR).contentType(MediaType.APPLICATION_JSON_UTF8)
												 .content(mapper.writeValueAsString(personalização)))
				.andExpect(status().isBadRequest())
				.andExpect(content().string(not(isEmptyOrNullString())))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
	}
	
	@Test
	void adicionarPersonalizaçãoComDescriçãoNula() throws JsonProcessingException, Exception
	{
		Personalização personalização = new Personalização(null, 0, 0.0);
		
		mockMVC	.perform(post(ENDEREÇO_DE_SALVAR).contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(mapper.writeValueAsString(personalização)))
				.andExpect(status().isBadRequest())
				.andExpect(content().string(not(isEmptyOrNullString())))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
	}
	
	@Test
	void adicionarPersonalizaçãoComTempoAdicionalNegativo() throws JsonProcessingException, Exception
	{
		Personalização personalização = new Personalização("Personalização", -1, 0.0);
		
		mockMVC	.perform(post(ENDEREÇO_DE_SALVAR).contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(mapper.writeValueAsString(personalização)))
				.andExpect(status().isBadRequest())
				.andExpect(content().string(not(isEmptyOrNullString())))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
	}

	@Test
	void adicionarPersonalizaçãoComCustoAdicionalNegativo() throws JsonProcessingException, Exception
	{
		Personalização personalização = new Personalização("Personalização", 0, -1.0);
		
		mockMVC	.perform(post(ENDEREÇO_DE_SALVAR).contentType(MediaType.APPLICATION_JSON_UTF8)
												 .content(mapper.writeValueAsString(personalização)))
				.andExpect(status().isBadRequest())
				.andExpect(content().string(not(isEmptyOrNullString())))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
	}
}
