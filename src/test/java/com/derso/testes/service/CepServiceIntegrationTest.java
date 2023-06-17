package com.derso.testes.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.derso.testes.service.CepService.Localidade;

@SpringBootTest
public class CepServiceIntegrationTest {
	
	@Autowired
	private CepService servico;
	
	@Test
	public void consultaServicoExterno() {
		Localidade localidade = servico.consultar("30493180");
		assertEquals("Rua José Hemetério Andrade", localidade.logradouro());
		assertEquals("Buritis", localidade.bairro());
		assertEquals("Belo Horizonte", localidade.localidade());
	}

}
