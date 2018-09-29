
package br.com.uds;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.uds.pedidos.Pedido;
import br.com.uds.personalizações.Personalização;
import br.com.uds.personalizações.PersonalizaçãoRepository;
import br.com.uds.sabores.Sabor;
import br.com.uds.sabores.SaborRepository;
import br.com.uds.tamanhos.Tamanho;
import br.com.uds.tamanhos.TamanhoRepository;

@SpringBootTest
@SpringJUnitConfig
@AutoConfigureMockMvc
public class TesteDeIntegração
{
	@Autowired
	ObjectMapper mapper;
	@Autowired
	MockMvc mockMVC;

	private static final String ENDEREÇO_DE_SALVAR = "/api/pedidos/salvar";

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
	
	@Test
	void pizzaSemPersonalização() throws JsonProcessingException, Exception
	{
		Pedido pedido = new Pedido(PORTUGUESA, PEQUENA);
		
		mockMVC	.perform(post(ENDEREÇO_DE_SALVAR).contentType(MediaType.APPLICATION_JSON_UTF8)
												 .content(mapper.writeValueAsString(pedido)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.valorTotal", equalTo(pedido.calcularValorTotal())))
				.andExpect(jsonPath("$.tempoDePreparo", equalTo(pedido.calcularTempoDePreparo())))

				.andExpect(jsonPath("$.sabor.nome", equalTo(pedido.getSabor().getNome())))
				.andExpect(jsonPath("$.sabor.tempoAdicional", equalTo(pedido.getSabor().getTempoAdicional())))
				
				.andExpect(jsonPath("$.tamanho.nome", equalTo(pedido.getTamanho().getNome())))
				.andExpect(jsonPath("$.tamanho.custo", equalTo(pedido.getTamanho().getCusto())))
				.andExpect(jsonPath("$.tamanho.tempoDePreparo", equalTo(pedido.getTamanho().getTempoDePreparo())))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
	}
	
	@Test
	void pizzaComUmaPersonalização() throws JsonProcessingException, Exception
	{
		Pedido pedido = new Pedido(MARGUERITA, GRANDE, SEM_CEBOLA);
		
		mockMVC	.perform(post(ENDEREÇO_DE_SALVAR).contentType(MediaType.APPLICATION_JSON_UTF8)
												 .content(mapper.writeValueAsString(pedido)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.valorTotal", equalTo(pedido.calcularValorTotal())))
				.andExpect(jsonPath("$.tempoDePreparo", equalTo(pedido.calcularTempoDePreparo())))

				.andExpect(jsonPath("$.sabor.nome", equalTo(pedido.getSabor().getNome())))
				.andExpect(jsonPath("$.sabor.tempoAdicional", equalTo(pedido.getSabor().getTempoAdicional())))
				
				.andExpect(jsonPath("$.tamanho.nome", equalTo(pedido.getTamanho().getNome())))
				.andExpect(jsonPath("$.tamanho.custo", equalTo(pedido.getTamanho().getCusto())))
				.andExpect(jsonPath("$.tamanho.tempoDePreparo", equalTo(pedido.getTamanho().getTempoDePreparo())))
				
				.andExpect(jsonPath("$.personalizações[0].descrição", equalTo(SEM_CEBOLA.getDescrição())))
				.andExpect(jsonPath("$.personalizações[0].tempoAdicional", equalTo(SEM_CEBOLA.getTempoAdicional())))
				.andExpect(jsonPath("$.personalizações[0].custoAdicional", equalTo(SEM_CEBOLA.getCustoAdicional())))
				
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
	}
	
	@Test
	void pizzaComVáriasPersonalizações() throws JsonProcessingException, Exception
	{
		Pedido pedido = new Pedido(CALABRESA, MÉDIA, SEM_CEBOLA, BACON_EXTRA);
		
		mockMVC	.perform(post(ENDEREÇO_DE_SALVAR).contentType(MediaType.APPLICATION_JSON_UTF8)
												 .content(mapper.writeValueAsString(pedido)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.valorTotal", equalTo(pedido.calcularValorTotal())))
				.andExpect(jsonPath("$.tempoDePreparo", equalTo(pedido.calcularTempoDePreparo())))

				.andExpect(jsonPath("$.sabor.nome", equalTo(pedido.getSabor().getNome())))
				.andExpect(jsonPath("$.sabor.tempoAdicional", equalTo(pedido.getSabor().getTempoAdicional())))
				
				.andExpect(jsonPath("$.tamanho.nome", equalTo(pedido.getTamanho().getNome())))
				.andExpect(jsonPath("$.tamanho.custo", equalTo(pedido.getTamanho().getCusto())))
				.andExpect(jsonPath("$.tamanho.tempoDePreparo", equalTo(pedido.getTamanho().getTempoDePreparo())))
				
				.andExpect(jsonPath("$.personalizações[0].descrição", equalTo(SEM_CEBOLA.getDescrição())))
				.andExpect(jsonPath("$.personalizações[0].tempoAdicional", equalTo(SEM_CEBOLA.getTempoAdicional())))
				.andExpect(jsonPath("$.personalizações[0].custoAdicional", equalTo(SEM_CEBOLA.getCustoAdicional())))
				
				.andExpect(jsonPath("$.personalizações[1].descrição", equalTo(BACON_EXTRA.getDescrição())))
				.andExpect(jsonPath("$.personalizações[1].tempoAdicional", equalTo(BACON_EXTRA.getTempoAdicional())))
				.andExpect(jsonPath("$.personalizações[1].custoAdicional", equalTo(BACON_EXTRA.getCustoAdicional())))
				
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
	}
}
