package com.derso.testes.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Entity
public class RegistroCaixa {
	
	public enum Tipo {
		DEBITO,
		CREDITO
	}
	
	public RegistroCaixa() {
	}
	
	public RegistroCaixa(Tipo tipo, String descricao, BigDecimal valor) {
		this.dataHora = LocalDateTime.now();
		this.tipo = tipo;
		this.descricao = descricao;
		this.valor = valor;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;
	
	@NotNull
	private LocalDateTime dataHora;
	
	@NotEmpty
	private String descricao;
	
	@NotNull
	private Tipo tipo;
	
	@NotNull
	@DecimalMin(value = "0.0", inclusive = false)
	private BigDecimal valor;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDateTime getDataHora() {
		return dataHora;
	}

	public void setDataHora(LocalDateTime dataHora) {
		this.dataHora = dataHora;
	}
	
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Tipo getTipo() {
		return tipo;
	}

	public void setTipo(Tipo tipo) {
		this.tipo = tipo;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
	
	public BigDecimal getValorComputar() {
		return tipo == Tipo.CREDITO
				? valor
				: valor.multiply(BigDecimal.valueOf(-1.0));
	}
	
	

}
