package com.derso.testes.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.derso.testes.domain.RegistroCaixa;
import com.derso.testes.repository.RegistroCaixaRepository;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/caixa")
public class RegistroCaixaController {
	
	@Autowired
	private RegistroCaixaRepository repositorio;
	
	@GetMapping("/{data}")
	public List<RegistroCaixa> listar(@PathVariable LocalDate data) {
		return repositorio.findByDataHoraBetween(
				data.atTime(0, 0, 0),
				data.atTime(23, 59, 59, 999999999));
	}
	
	@PostMapping
	@Transactional
	public RegistroCaixa novo(@Valid @RequestBody RegistroCaixa registro) {
		repositorio.save(registro);
		return registro;
	}

}
