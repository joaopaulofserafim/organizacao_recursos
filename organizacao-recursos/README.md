# Organização de Recursos

Sistema para reservar **salas, professores e materiais sem conflito de horário**.
É o projeto avaliado da disciplina **Qualidade de Software** (TADS · Senac · 2026.2 · Prof. Afonso).

**Turma:** STADSCAS4NA · **Equipe:** _Nome completo (RA), Nome completo (RA), Nome completo (RA)_

---

## 1. O que tem neste repositório

| Arquivo ou pasta | Para que serve | Aula |
|---|---|---|
| `docs/prd.md` | **O que** o sistema faz: personas, requisitos funcionais (RF), requisitos de qualidade medíveis (RNF) e regras de negócio (RN). | 02 e 03 |
| `docs/plano-de-teste.md` | **Como** vamos testar: estratégia, utility tree, cenários de qualidade e casos de teste. | 02 |
| `docs/arquitetura.md` | **Como o sistema é montado e por quê**: diagramas C4, análise ATAM, riscos, trade-offs e decisões (ADRs). | 04 |
| `docs/taticas-arquiteturais-len-bass.md` | Catálogo de táticas do professor, guardado ao lado da arquitetura como ele pediu. | 04 |
| `docs/testes/plano-tdd.md` | Lista de todas as regras de negócio e dos testes que cada uma precisa ter. | 05 |
| `RTM.md` | Matriz de rastreabilidade: liga cada requisito ao risco, ao teste e à evidência. | 02 em diante |
| `.github/ISSUE_TEMPLATE/` | Modelo para registrar bugs na aba **Issues** do GitHub. | 05 |
| `pom.xml`, `mvnw`, `src/` | O código Java (Spring Boot). Por enquanto só o esqueleto e um teste. | 03 |

### Como ler os documentos

- **IDs:** todo item tem um identificador (`RF-09`, `RNF-03`, `RN-02`, `CT-001`, `ADR-002`). Ao citar algo em commit, issue ou teste, use o ID.
- **Origem de cada informação:**
  - `[ESPEC]` é fato da [especificação do professor](https://afonsolelis.github.io/aulas_senac/pages/qualidade2/especificacao-projeto.html).
  - `[EQUIPE]` é hipótese nossa: pode mudar e precisa ser validada com o professor.
  - `[PROF-A04]` é restrição dada pelo professor na Aula 04 (o job diário).

## 2. Tecnologias

Java 21 · Spring Boot 3.5.16 · Maven · PostgreSQL · JUnit 5 · Testcontainers.
Nas próximas aulas entram: GitHub Actions, JaCoCo, SonarCloud, WireMock e JMeter.

> **Por que Spring Boot 3.5.16?** A especificação pede Spring Boot 3.x, mas hoje o site start.spring.io só oferece a versão 4.x: a linha 3.5 saiu do suporte gratuito em 30/06/2026, e a 3.5.16 é a última versão 3.x. A pergunta está registrada no `docs/prd.md` para confirmar com o professor.

## 3. Como rodar

Os testes sobem um PostgreSQL temporário dentro do **Docker**. Por isso é preciso ter Docker, ou usar o Codespaces, que já vem com ele.

### Opção A (recomendada): GitHub Codespaces

É o mesmo ambiente que o professor usa. Não precisa instalar nada no seu computador.

1. Na página do repositório no GitHub, clique em **Code → Codespaces → Create codespace on main**.
2. Espere o editor abrir e use o **Terminal** que aparece embaixo:

```bash
java -version                  # precisa mostrar 21 ou maior
docker ps                      # precisa responder sem erro
chmod +x mvnw                  # só na primeira vez
./mvnw test                    # roda os testes
./mvnw spring-boot:test-run    # sobe a aplicação
```

### Opção B: no seu computador com Windows (Prompt de Comando)

Pré-requisitos: JDK 21 ou maior, variável `JAVA_HOME` apontando para a pasta do JDK e Docker Desktop aberto.

```cmd
cd C:\caminho\para\organizacao-recursos
mvnw.cmd test
mvnw.cmd spring-boot:test-run
```

No Windows o comando é `mvnw.cmd`. O `./mvnw` só funciona em Linux, macOS e Codespaces. Se você tem o Maven instalado, `mvn test` também funciona.

### O que você deve ver

| Comando | Resultado esperado |
|---|---|
| `test` | `Tests run: 1, Failures: 0, Errors: 0` e `BUILD SUCCESS`. Na primeira vez demora alguns minutos, porque baixa as dependências e a imagem do PostgreSQL. |
| `spring-boot:test-run` | Uma linha `Started ...` no log. Em `http://localhost:8080` o navegador pede login: o usuário é `user` e a senha aparece no log, na linha `Using generated security password`. Depois do login aparece um erro 404, e isso é normal: ainda não existe nenhuma tela. |

Para parar a aplicação: **Ctrl + C** no terminal.

### Problemas comuns

| Mensagem de erro | Causa | Como resolver |
|---|---|---|
| `JAVA_HOME not found in your environment` | A variável não está configurada | Aponte `JAVA_HOME` para a pasta do JDK (sem o `\bin` no final) e abra um **novo** terminal |
| `'.' não é reconhecido como um comando interno` | Comando de Linux usado no Windows | Use `mvnw.cmd` |
| `'mvnw.cmd' não é reconhecido` | O terminal não está na pasta do projeto | Entre na pasta com `cd` (deve existir um `pom.xml` nela) |
| `Could not find a valid Docker environment` | O Docker não está rodando | Abra o Docker Desktop ou use o Codespaces |
| `Failed to configure a DataSource` | Usou `spring-boot:run`, que não sobe banco | Use `spring-boot:test-run` |

## 4. Como a equipe trabalha

1. **Ninguém faz commit direto na `main`.** Crie uma branch: `git switch -c docs/ajusta-rnf`.
2. Envie a branch e abra um **Pull Request**. Outra pessoa da equipe revisa e aprova, e só então fazemos o merge. Esse histórico de revisão faz parte da nota.
3. Achou um bug? Abra uma **Issue** com o modelo "Relato de defeito". Nunca cole senha, token ou dado pessoal real.
4. Mudou um requisito ou um teste? Atualize o `RTM.md` no mesmo Pull Request.

## 5. Links

- [Especificação do projeto](https://afonsolelis.github.io/aulas_senac/pages/qualidade2/especificacao-projeto.html)
- [Cronograma e materiais da disciplina](https://afonsolelis.github.io/aulas_senac/pages/home_qualidade_2026_2.html)
- [Orientações de entrega final](https://afonsolelis.github.io/aulas_senac/pages/qualidade2/material/material_entrega-projeto-final.html)
