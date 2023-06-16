package com.derso.testes.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import com.derso.testes.domain.RegistroCaixa;
import com.derso.testes.domain.RegistroCaixa.Tipo;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ActiveProfiles({"test"})
public class RegistroCaixaRepositoryTest {
	
	// Já ficam umas lições sobre manipular LocalDate e LocalDateTime
	private LocalDate sexta = LocalDate.of(2023, 6, 16);
	private LocalDate sabado = LocalDate.of(2023, 6, 17);
	private LocalDate domingo = LocalDate.of(2023, 6, 18);
	private LocalDateTime sexta12h = sexta.atTime(12, 0, 0);
	private LocalDateTime sexta13h = sexta.atTime(13, 0, 0);
	private LocalDateTime sabado14h = sabado.atTime(14, 0, 0);
	private LocalDateTime sabado15h = sabado.atTime(15, 0, 0);
	private LocalDateTime domingo16h = domingo.atTime(16, 0, 0);
	
	// Este é o cara que usamos para manipular a base de dados:
	// - montar cenários
	// - conferir o conteúdo
	// Exemplos de uso do EntityManager em:
	// https://github.com/EdyKnopfler/spring-boot-udemy-spring-data-entity-manager
	@Autowired
    private TestEntityManager entityManager;
	
	@Autowired
	private RegistroCaixaRepository repositorio;
	
	@BeforeEach
	public void criarRegistros() {
		List<RegistroCaixa> registros = Arrays.asList(
				new RegistroCaixa(Tipo.CREDITO, "Exemplo 1", BigDecimal.valueOf(1)),
				new RegistroCaixa(Tipo.CREDITO, "Exemplo 2", BigDecimal.valueOf(2)),
				new RegistroCaixa(Tipo.CREDITO, "Exemplo 3", BigDecimal.valueOf(3)),
				new RegistroCaixa(Tipo.CREDITO, "Exemplo 4", BigDecimal.valueOf(4)),
				new RegistroCaixa(Tipo.CREDITO, "Exemplo 5", BigDecimal.valueOf(5))
		);
		
		registros.get(0).setDataHora(sexta12h);
		registros.get(1).setDataHora(sexta13h);
		registros.get(2).setDataHora(sabado14h);
		registros.get(3).setDataHora(sabado15h);
		registros.get(4).setDataHora(domingo16h);
		
		for (RegistroCaixa registro: registros ) {
			entityManager.persist(registro);
		}
		
		entityManager.flush();
	}
	
	@Test
	public void buscaPorPeriodoPadraoDaJPA() {
		// Usando o BETWEEN padrão do SQL/JPQL temos que fazer um malabarismo para
		// não perder nenhum registro...
		// https://docs.oracle.com/cd/E17904_01/apirefs.1111/e13946/ejb3_langref.html#ejb3_langref_between
		List<RegistroCaixa> registros = repositorio.findByDataHoraBetween(
				sabado.atTime(0, 0, 0),
				domingo.atTime(23, 59, 59, 999999999));
		assertEquals(3, registros.size());
		assertEquals(sabado14h, registros.get(0).getDataHora());
		assertEquals(sabado15h, registros.get(1).getDataHora());
		assertEquals(domingo16h,registros.get(2).getDataHora());
	}
	
	@Test
	public void buscaPorDataEspecifica() {
		List<RegistroCaixa> registros = repositorio.buscarPorData(sexta);
		assertEquals(2, registros.size());
		assertEquals(sexta.atTime(12, 0, 0), registros.get(0).getDataHora());
		assertEquals(sexta.atTime(13, 0, 0), registros.get(1).getDataHora());
	}

}
