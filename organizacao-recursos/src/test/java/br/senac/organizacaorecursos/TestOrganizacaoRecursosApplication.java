package br.senac.organizacaorecursos;

import org.springframework.boot.SpringApplication;

/**
 * Sobe a aplicação com o banco do Testcontainers.
 * Use: ./mvnw spring-boot:test-run   (Windows: mvnw.cmd spring-boot:test-run)
 */
public class TestOrganizacaoRecursosApplication {

	public static void main(String[] args) {
		SpringApplication.from(OrganizacaoRecursosApplication::main)
				.with(TestcontainersConfiguration.class)
				.run(args);
	}

}
