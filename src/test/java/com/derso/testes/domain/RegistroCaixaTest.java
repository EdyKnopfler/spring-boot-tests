package com.derso.testes.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.derso.testes.domain.RegistroCaixa.Tipo;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

/*
 * TESTANDO REGRAS DE NEGÓCIO E VALIDAÇÃO
 */

public class RegistroCaixaTest {
	
	private static Validator validator;
	
	private RegistroCaixa registro;
	
	@BeforeAll
    public static void setUp() {
		// Como javeiro gosta desse tipo de sintaxe :P
		validator = Validation
				.buildDefaultValidatorFactory()
				.getValidator();
    }
	
	@BeforeEach
	public void criarRegistroValido() {
		registro = new RegistroCaixa(
				Tipo.CREDITO,
				"Leite de capivara de Barra do Piraí",
				BigDecimal.valueOf(5.50));
	}
	
	@Test
	public void registroEhValidoComDataTipoDescricaoValor() {
		Set<ConstraintViolation<RegistroCaixa>> violacoes = validator.validate(registro);
		assertTrue(violacoes.isEmpty());
	}
	
	@Test
	public void registroEhInvalidoSemTipo() {
		registro.setTipo(null);
		assertFalse(validator.validate(registro).isEmpty());
	}
	
	@Test
	public void registroEhInvalidoSemDataHora() {
		registro.setDataHora(null);
		assertFalse(validator.validate(registro).isEmpty());
	}
	
	@Test
	public void registroEhInvalidoSemDescricao() {
		registro.setDescricao(null);
		assertFalse(validator.validate(registro).isEmpty());
	}
	
	@Test
	public void registroEhInvalidoSemValor() {
		registro.setValor(null);
		assertFalse(validator.validate(registro).isEmpty());
	}
	
	@Test
	public void registroEhInvalidoComValorZero() {
		registro.setValor(BigDecimal.valueOf(0));
		assertFalse(validator.validate(registro).isEmpty());
	}
	
	@Test
	public void registroEhInvalidoComValorNegativo() {
		registro.setValor(BigDecimal.valueOf(-0.01));
		assertFalse(validator.validate(registro).isEmpty());
	}
	
	@Test
	public void registroCreditoValidoPossuiValorComputadoPositivo() {
		assertTrue(BigDecimal.valueOf(5.5).compareTo(registro.getValorComputar()) == 0);
	}
	
	@Test
	public void registroDebitoValidoPossuiValorComputadoNegativo() {
		registro.setTipo(Tipo.DEBITO);
		assertTrue(BigDecimal.valueOf(-5.5).compareTo(registro.getValorComputar()) == 0);
	}

}
