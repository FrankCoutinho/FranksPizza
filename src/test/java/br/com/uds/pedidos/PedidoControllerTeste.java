
package br.com.uds.pedidos;

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

import java.util.Arrays;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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

import br.com.uds.personalizações.Personalização;
import br.com.uds.personalizações.PersonalizaçãoRepository;
import br.com.uds.sabores.Sabor;
import br.com.uds.sabores.SaborRepository;
import br.com.uds.tamanhos.Tamanho;
import br.com.uds.tamanhos.TamanhoRepository;

@ComponentScan
@SpringJUnitConfig
@AutoConfigureDataJpa
@WebMvcTest(PedidoController.class)
public class PedidoControllerTeste
{
	@Autowired ObjectMapper mapper;
	@Autowired MockMvc mockMVC;

	private static final String ENDEREÇO_DE_SALVAR = "/api/pedidos/salvar";
	private static final String ENDEREÇO_DE_EXCLUIR = "/api/pedidos/excluir";
	private static final String ENDEREÇO_DE_OBTER_TODOS = "/api/pedidos/todos";

	@Autowired private SaborRepository saborRepository;
	@Autowired private PedidoRepository pedidoRepository;
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
		if (tamanhoRepository != null)
			tamanhoRepository.saveAll(Arrays.asList(MÉDIA, GRANDE, PEQUENA));
		
		if (saborRepository != null)
			saborRepository.saveAll(Arrays.asList(CALABRESA, MARGUERITA, PORTUGUESA));
		
		if (personalizaçãoRepository != null)
			personalizaçãoRepository.saveAll(Arrays.asList(SEM_CEBOLA, BACON_EXTRA, BORDA_RECHEADA));
	}
	
	@AfterEach
	void limparBancoDeDadosDePedidos()
	{
		pedidoRepository.deleteAll();
	}
	
	@Test
	void excluirPedido() throws JsonProcessingException, Exception
	{
		Pedido pedido = MARGUERITA_PEQUENA_BACON_EXTRA;

		mockMVC	.perform(post(ENDEREÇO_DE_SALVAR).contentType(MediaType.APPLICATION_JSON_UTF8)
												 .content(mapper.writeValueAsString(pedido)))
				.andReturn();

		pedido.setId(1L);

		mockMVC	.perform(delete(ENDEREÇO_DE_EXCLUIR).contentType(MediaType.APPLICATION_JSON_UTF8)
													.content(mapper.writeValueAsString(pedido)))
				.andReturn();

		mockMVC	.perform(get(ENDEREÇO_DE_OBTER_TODOS))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().string(equalToIgnoringWhiteSpace("[]")))
				.andExpect(status().isOk());
	}

	@Test
	void adicionarPedido() throws JsonProcessingException, Exception
	{
		mockMVC	.perform(post(ENDEREÇO_DE_SALVAR).contentType(MediaType.APPLICATION_JSON_UTF8)
												 .content(mapper.writeValueAsString(CALABREA_MÉDIA_SEM_CEBOLA_BORDA_RECHEADA)))
				.andExpect(status().isCreated())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.sabor.nome", equalTo(CALABREA_MÉDIA_SEM_CEBOLA_BORDA_RECHEADA.getSabor().getNome())))
				.andExpect(jsonPath("$.tamanho.nome", equalTo(CALABREA_MÉDIA_SEM_CEBOLA_BORDA_RECHEADA.getTamanho().getNome())));
	}

	@Test
	void obterTodosOsPedidos() throws JsonProcessingException, Exception
	{
		mockMVC	.perform(post(ENDEREÇO_DE_SALVAR).contentType(MediaType.APPLICATION_JSON_UTF8)
												 .content(mapper.writeValueAsString(PORTUGUESA_GRANDE)))
				.andReturn();

		mockMVC	.perform(post(ENDEREÇO_DE_SALVAR).contentType(MediaType.APPLICATION_JSON_UTF8)
												 .content(mapper.writeValueAsString(MARGUERITA_PEQUENA_BACON_EXTRA)))
				.andReturn();

		mockMVC	.perform(post(ENDEREÇO_DE_SALVAR).contentType(MediaType.APPLICATION_JSON_UTF8)
												 .content(mapper.writeValueAsString(CALABREA_MÉDIA_SEM_CEBOLA_BORDA_RECHEADA)))
				.andReturn();

		mockMVC	.perform(get(ENDEREÇO_DE_OBTER_TODOS))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].sabor.nome", equalTo(PORTUGUESA_GRANDE.getSabor().getNome())))
				.andExpect(jsonPath("$[1].sabor.nome", equalTo(MARGUERITA_PEQUENA_BACON_EXTRA.getSabor().getNome())))
				.andExpect(jsonPath("$[2].sabor.nome", equalTo(CALABREA_MÉDIA_SEM_CEBOLA_BORDA_RECHEADA.getSabor().getNome())))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
	}

	@Test
	void adicionarPedidoComSaborInválido() throws JsonProcessingException, Exception
	{
		Pedido pedido = new Pedido(new Sabor("", 0), MÉDIA, SEM_CEBOLA);

		mockMVC	.perform(post(ENDEREÇO_DE_SALVAR).contentType(MediaType.APPLICATION_JSON_UTF8)
												 .content(mapper.writeValueAsString(pedido)))
				.andExpect(status().isBadRequest())
				.andExpect(content().string(not(isEmptyOrNullString())))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
	}

	@Test
	void adicionarPedidoComTamanhoInválido() throws JsonProcessingException, Exception
	{
		Pedido pedido = new Pedido(CALABRESA, new Tamanho("", 0, 0.0), SEM_CEBOLA);

		mockMVC	.perform(post(ENDEREÇO_DE_SALVAR).contentType(MediaType.APPLICATION_JSON_UTF8)
												 .content(mapper.writeValueAsString(pedido)))
				.andExpect(status().isBadRequest())
				.andExpect(content().string(not(isEmptyOrNullString())))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
	}

	@Test
	void adicionarPedidoComPersonalizaçãoInválida() throws JsonProcessingException, Exception
	{
		Pedido pedido = new Pedido(PORTUGUESA, MÉDIA, new Personalização("", 0, 0.0));

		mockMVC	.perform(post(ENDEREÇO_DE_SALVAR).contentType(MediaType.APPLICATION_JSON_UTF8)
												 .content(mapper.writeValueAsString(pedido)))
				.andExpect(status().isBadRequest())
				.andExpect(content().string(not(isEmptyOrNullString())))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
	}
}
