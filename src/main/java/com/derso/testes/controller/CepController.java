package com.derso.testes.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/*
 * ACESSANDO SERVIÇO EXTERNO!
 */

@RestController
@RequestMapping("/cep")
public class CepController {
	
	@JsonIgnoreProperties(ignoreUnknown = true)
	public record Localidade(
			String logradouro, 
			String bairro, 
			String localidade) {
	}
	
	@GetMapping("/{cep}")
	public Localidade consultar(@PathVariable String cep) {
		cep = cep.replaceAll("\\D+", "");
		String uri = "https://viacep.com.br/ws/" + cep + "/json/";
		RestTemplate rest = new RestTemplate();
		Localidade localidade = rest.getForObject(uri, Localidade.class);
		return localidade;
	}

}
