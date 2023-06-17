package com.derso.testes.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.derso.testes.service.CepService;
import com.derso.testes.service.CepService.Localidade;

@WebMvcTest(controllers = CepController.class)
public class CepControllerTest {
	
	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private CepService servico;
	
	@Test
	public void testeChamada() throws Exception {
		Localidade local = new Localidade("Rua da Amargura", "Bairro dos Bêbados", "Boston City");
		when(servico.consultar(anyString())).thenReturn(local);
		mvc.perform(get("/cep/11111-111"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.logradouro", equalTo(local.logradouro())))
			.andExpect(jsonPath("$.bairro", equalTo(local.bairro())))
			.andExpect(jsonPath("$.localidade", equalTo(local.localidade())));
		verify(servico).consultar("11111111");
	}

}
