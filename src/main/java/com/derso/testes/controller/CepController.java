package com.derso.testes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.derso.testes.service.CepService;
import com.derso.testes.service.CepService.Localidade;

/*
 * ACESSANDO SERVIÇO EXTERNO!
 */

@RestController
@RequestMapping("/cep")
public class CepController {
	
	@Autowired
	private CepService servico;
	
	@GetMapping("/{cep}")
	public Localidade consultar(@PathVariable String cep) {
		cep = cep.replaceAll("\\D+", "");
		return servico.consultar(cep);
	}

}
