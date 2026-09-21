package br.senac.organizacaorecursos;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

/**
 * Marco zero da Aula 03: prova apenas que a aplicação sobe com o banco.
 * Não testa nenhuma regra de negócio (os testes das regras nascem na Aula 05).
 */
@Import(TestcontainersConfiguration.class)
@SpringBootTest
class OrganizacaoRecursosApplicationTests {

	@Test
	void contextLoads() {
	}

}
