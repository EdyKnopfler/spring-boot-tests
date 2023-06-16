package com.derso.testes.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.derso.testes.domain.RegistroCaixa;

public interface RegistroCaixaRepository extends JpaRepository<RegistroCaixa, Long> {
	
	@Query(
		nativeQuery = true,
		value = "SELECT * FROM registro_caixa WHERE DATE(data_hora) = :data")
	List<RegistroCaixa> findByDate(@Param("data") LocalDate data);
	
	List<RegistroCaixa> findByDataHoraBetween(
			LocalDateTime inicio, LocalDateTime fim);

}
