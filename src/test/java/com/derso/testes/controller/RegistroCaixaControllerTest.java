package com.derso.testes.controller;


import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
// Atenção para esta classe...
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
// ... e para esta
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.PlatformTransactionManager;

import com.derso.testes.domain.RegistroCaixa;
import com.derso.testes.domain.RegistroCaixa.Tipo;
import com.derso.testes.repository.RegistroCaixaRepository;

/*
 * TESTE DE ENDPOINTS!
 */

@WebMvcTest(controllers = RegistroCaixaController.class)
public class RegistroCaixaControllerTest {
	
	// Mock de requisições 
	// (já populadas pelo Spring, prontas para chegar no controller)
	@Autowired
	private MockMvc mvc;
	
	// Fornece os mocks necessários (usando Mockito :))
	@MockBean
	private RegistroCaixaRepository repositorio;
	
	// Necessário para testar os métodos Transactional :P
	@MockBean
	private PlatformTransactionManager tm;
	
	@BeforeEach
	public void configurarMock() {
		List<RegistroCaixa> registros = Arrays.asList(
			new RegistroCaixa(Tipo.CREDITO, "A", BigDecimal.valueOf(10)),
			new RegistroCaixa(Tipo.CREDITO, "B", BigDecimal.valueOf(15)),
			new RegistroCaixa(Tipo.DEBITO, "C", BigDecimal.valueOf(5))
		);
		
		// Mockitando :)
		when(repositorio.findByDataHoraBetween(any(), any()))
				.thenReturn(registros);
	}
	
	@Test
	public void consultaPorDataDeveConsiderarAteOUltimoNanossegundoDoDia() throws Exception {
		// Continuo odiando fluent interfaces e essas APIs todas pulverizadas 
		mvc
			.perform(get("/caixa/2023-06-15").contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(3)))
			.andExpect(jsonPath("$[0].valorComputar", equalTo(10)))
			.andExpect(jsonPath("$[1].valorComputar", equalTo(15)))
			.andExpect(jsonPath("$[2].valorComputar", equalTo(-5.0)));
		
		LocalDateTime inicioDoDia = LocalDateTime.of(2023, 6, 15, 0, 0, 0, 0);
		LocalDateTime fimDoDia = LocalDateTime.of(2023, 6, 15, 23, 59, 59, 999999999);
		
		// Mockitando :)
		verify(repositorio).findByDataHoraBetween(inicioDoDia, fimDoDia);
	}
	
	@Test
	public void lancandoUmRegistroValido() throws Exception {
		mvc
			.perform(
				post("/caixa")
					.contentType(MediaType.APPLICATION_JSON)
					// Strings multilinha a partir do Java 15!
					.content("""
						{
							"dataHora": "2023-06-15T20:10:00",
							"tipo": "CREDITO",
							"valor": 100.0,
							"descricao": "Descrevo-me a ti como descrevo teu olhar"
						}
					""")
			)
			.andExpect(status().isOk());

		// Capturando o argumento usado na chamada
		ArgumentCaptor<RegistroCaixa> argumento = ArgumentCaptor.forClass(RegistroCaixa.class);
		verify(repositorio).save(argumento.capture());
		RegistroCaixa registro = argumento.getValue();
		
		assertEquals(LocalDateTime.of(2023, 6, 15, 20, 10), registro.getDataHora());
		assertEquals("Descrevo-me a ti como descrevo teu olhar", registro.getDescricao());
		assertEquals(Tipo.CREDITO, registro.getTipo());
		assertEquals(100, registro.getValor().doubleValue(), 0.001);
	}

}
