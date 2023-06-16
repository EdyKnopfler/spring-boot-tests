package com.derso.testes.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.derso.testes.domain.RegistroCaixa;

public interface RegistroCaixaRepository extends JpaRepository<RegistroCaixa, Long> {
	
	// Não é o jeito mais eficiente de fazer isto porém é apenas para demonstração
	// da classe de testes
	@Query(
		nativeQuery = true,
		value = "SELECT * FROM registro_caixa WHERE CAST(data_hora AS DATE) = :data")
	List<RegistroCaixa> buscarPorData(@Param("data") LocalDate data);
	
	// O ideal é ter uma classe de negócio/serviço que pega o LocalDate e gera dois
	// LocalDateTime, com a meia-noite e o último segundo (nanossegundo?) do dia.
	// Ver RegistroCaixaRepositoryTest
	List<RegistroCaixa> findByDataHoraBetween(
			LocalDateTime inicio, LocalDateTime fim);

}
