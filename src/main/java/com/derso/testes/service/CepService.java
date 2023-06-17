package com.derso.testes.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Service
public class CepService {

	private RestTemplate rest = new RestTemplate();
	
	@JsonIgnoreProperties(ignoreUnknown = true)
	public record Localidade(
			String logradouro, 
			String bairro, 
			String localidade) {
	}
	
	public Localidade consultar(String cep) {
		cep = cep.replaceAll("\\D+", "");
		String uri = "https://viacep.com.br/ws/" + cep + "/json/";
		Localidade localidade = rest.getForObject(uri, Localidade.class);
		return localidade;
	}

}
