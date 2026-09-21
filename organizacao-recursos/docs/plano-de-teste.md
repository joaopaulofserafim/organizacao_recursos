# Organização de Recursos — Plano de Teste

> Baseado na ISO/IEC/IEEE 29119-3 (Aula 02). Rastreia `docs/prd.md` (RF, RNF, RN).

## 1. Estratégia

- **Caixa preta** (API pelo contrato): regras de negócio RN-01 a RN-16, autorização por perfil e mensagens de erro.
- **Caixa branca** (unidade + cobertura JaCoCo): serviço de reservas, detector de sobreposição e máquina de estados.
- **Caixa cinza** (API + checagem no banco): concorrência e auditoria.
- Técnicas de projeto de teste: **partição de equivalência**, **valor limite** (fronteiras de horário) e **tabela de decisão** (perfil × restrito × estado).
- Regra da casa: **"apague a regra, rode a suíte"**. Se nenhum teste ficar vermelho, o teste não protege nada.

## 2. Níveis de teste

| Nível | Ferramenta | Foco |
|---|---|---|
| Unidade | JUnit 5, `@ParameterizedTest`, Mockito (só isolados) | RN-01, RN-02, RN-03, RN-10 (máquina de estados) |
| Integração | Spring Boot Test + **Testcontainers (PostgreSQL)** | Persistência, constraint anti-sobreposição, auditoria |
| API (caixa preta) | MockMvc / RestAssured | Contratos, 400/401/403/409, mensagens |
| Integração externa | **WireMock** | Notificação (RF-21) |
| Concorrência | JUnit + `ExecutorService` + Testcontainers | RN-04 / RNF-03 |
| Carga | **JMeter** | RNF-01, RNF-02, RNF-03 sob carga |
| E2E | _a definir (ex.: Playwright)_ | Fluxo reservar → aprovar → retirar → devolver |

## 3. Critérios de entrada, saída e suspensão

- **Entrada:** build verde (`./mvnw verify`), banco sobe com Testcontainers, requisito com ID no PRD.
- **Saída:** cobertura ≥ 80% linhas / ≥ 70% branches, Quality Gate do SonarCloud aprovado, 100% dos requisitos críticos com teste passando na RTM e 0 defeitos críticos abertos.
- **Suspensão:** o pipeline de CI quebrado por mais de 1 dia, ou o ambiente de banco indisponível.

## 4. Metas de cobertura

80% linhas · 70% branches (JaCoCo) · 100% das regras RN-01 a RN-10 com teste que fica vermelho quando a regra é removida.

## 5. Ambiente de teste

Java 21 · Spring Boot 3.x · PostgreSQL em Testcontainers · WireMock · GitHub Actions · SonarCloud. Dados **sintéticos** (e-mails `@example.invalid`).

## 6. Itens que NÃO serão testados (decisão registrada)

| Item | Motivo |
|---|---|
| Envio real de e-mail | Fora de escopo. A notificação é validada via WireMock. |
| Compatibilidade com navegadores antigos (IE) | Fora do público-alvo. |
| Acurácia estatística do modelo de previsão (RF-27) | O foco é a robustez do pipeline, não a ciência de dados. |

---

## 7. Utility tree (Aula 02 — ATAM)

(I, D) = Importância para o negócio × Dificuldade · A = Alta, M = Média, B = Baixa

| Atributo | Refinamento | Cenário (folha) | (I, D) | Justificativa |
|---|---|---|---|---|
| Confiabilidade | Integridade sob concorrência | CEN-01: 100 pedidos simultâneos para a mesma sala e horário → exatamente 1 aceita, 0 duplicatas. | **(A, A)** | Dupla reserva é **limitador de nota** e o problema central do produto. Concorrência é difícil de testar. |
| Segurança | Autorização | CEN-02: Solicitante tenta aprovar a própria reserva de laboratório restrito → 403 em 100% das tentativas, estado inalterado. | **(A, A)** | Falha de autorização também é **limitador de nota**. A matriz perfil × endpoint é extensa. |
| Adequação funcional | Correção | CEN-03: reserva que se sobrepõe em 1 minuto a outra (sala ou professor) → recusada com 409 `RESERVA_SOBREPOSTA`. | **(A, A)** | É a regra central. As fronteiras de horário escondem bugs (`<` vs `<=`). |
| Eficiência de Performance | Tempo de resposta | CEN-04: 50 usuários consultando disponibilidade → p95 < 800 ms. | (A, M) | Importante no início do semestre. O JMeter é conhecido. |
| Segurança | Responsabilização (auditoria) | CEN-05: toda aprovação gera registro com usuário, data, estado anterior e novo. | (A, B) | Fácil com listener/serviço de auditoria, e importante para P2. |
| Capacidade de Interação | Mensagens | CEN-06: erro de regra mostra código + texto em português, sem stack trace. | (M, B) | Barato de garantir com um `@ControllerAdvice`. |
| Manutenibilidade | Modularidade | CEN-07: trocar o provedor de notificação altera só 1 adaptador. | (M, M) | Arquitetura hexagonal simples resolve. |

## 8. Cenários (A, A) nas 6 partes

### CEN-01 — Dupla reserva simultânea (RF-13, RN-04, RNF-03)

| Parte | Conteúdo |
|---|---|
| Fonte | 100 solicitantes autenticados |
| Estímulo | Enviam `POST /reservas` para a **mesma sala, mesmo horário**, ao mesmo tempo |
| Ambiente | Operação normal, banco PostgreSQL real (Testcontainers), agenda da sala vazia |
| Artefato | API de reservas + banco de dados |
| Resposta | Aceita uma reserva e recusa as demais com mensagem clara |
| Medida | **Exatamente 1** HTTP 201, **99** HTTP 409 `RESERVA_SOBREPOSTA`, **1** linha na tabela, **0** HTTP 500 |

### CEN-02 — Aprovação indevida de recurso restrito (RF-02, RF-15, RN-07, RNF-05)

| Parte | Conteúdo |
|---|---|
| Fonte | Usuário com perfil SOLICITANTE (ou ADMINISTRADOR) |
| Estímulo | `POST /reservas/{id}/aprovar` numa reserva SOLICITADA de sala restrita |
| Ambiente | Operação normal, token válido |
| Artefato | Camada de autorização + serviço de aprovação |
| Resposta | Nega a operação sem alterar o estado e registra a tentativa |
| Medida | HTTP **403** em **100%** das tentativas; estado continua SOLICITADA; **0** registros de aprovação criados |

### CEN-03 — Sobreposição na fronteira de horário (RF-12, RN-02, RN-03)

| Parte | Conteúdo |
|---|---|
| Fonte | Solicitante |
| Estímulo | Tenta reservar a sala A das 09:59 às 11:00, sendo que já existe reserva APROVADA 08:00–10:00 |
| Ambiente | Operação normal |
| Artefato | Detector de sobreposição |
| Resposta | Recusa 09:59; aceita 10:00 (fronteira semiaberta) |
| Medida | 09:59 → 409 `RESERVA_SOBREPOSTA`; 10:00 → 201; o mesmo vale para a agenda do professor |

## 9. Casos de teste

### CT-001 — Concorrência: 100 pedidos, 1 reserva (→ CEN-01, RF-13)

- **Pré-condições:** banco limpo via Testcontainers; sala `LAB-101` cadastrada, não restrita, sem reservas; 100 usuários SOLICITANTE `sol01@example.invalid` … `sol100@example.invalid` com token válido.
- **Dados:** sala `LAB-101`; início `2026-11-10T08:00-03:00`; término `2026-11-10T10:00-03:00`; professor `PROF-007`.
- **Passos:** 1. Criar um `CountDownLatch(1)`. 2. Submeter 100 tarefas a um pool de 100 threads, cada uma com o `POST /reservas`. 3. Liberar o latch. 4. Aguardar todas terminarem. 5. Contar os status HTTP e as linhas da tabela `reserva`.
- **Esperado:** 1× 201; 99× 409 com código `RESERVA_SOBREPOSTA`; 0× 500; `SELECT count(*) FROM reserva WHERE sala='LAB-101'` = 1.
- **Rastreabilidade:** RF-13, RN-04, RNF-03, CEN-01.

### CT-002 — Solicitante não aprova restrito (→ CEN-02, RN-07)

- **Pré-condições:** sala `LAB-QUIM` com `restrita=true`; reserva R1 SOLICITADA criada por `marina@example.invalid` (SOLICITANTE).
- **Dados:** token de `marina@example.invalid`; `POST /reservas/{R1}/aprovar`.
- **Passos:** 1. Enviar a requisição. 2. Consultar `GET /reservas/{R1}` com token de Responsável.
- **Esperado:** 403 `ACESSO_NEGADO`; R1 continua SOLICITADA.
- **Rastreabilidade:** RF-02, RF-15, RN-07, RNF-05, CEN-02.

### CT-003 — Fronteira de sobreposição (→ CEN-03, RN-02)

- **Pré-condições:** sala `SALA-201` com reserva APROVADA 08:00–10:00 em 2026-11-10.
- **Dados (parametrizado):** início 09:59 → 409; 10:00 → 201; 10:01 → 201; término 08:00 com início 07:00 → 201.
- **Passos:** enviar `POST /reservas` para cada linha de dados, com o banco reiniciado entre os casos.
- **Esperado:** conforme a tabela de dados.
- **Rastreabilidade:** RF-12, RN-02, CEN-03.

## 10. Achados ATAM do plano

- **Ponto de sensibilidade:** o **nível de isolamento/estratégia de trava** do banco na criação de reserva define se CEN-01 passa. Com `READ COMMITTED` e verificação apenas na aplicação ("consulta e depois insere"), duas threads passam juntas.
- **Ponto de trade-off:** **trava pessimista (`SELECT … FOR UPDATE`) na agenda do recurso**: melhora a integridade (CEN-01), mas piora a eficiência de performance (CEN-04), porque serializa pedidos do mesmo recurso. A decisão está no ADR-002: constraint `EXCLUDE` no banco.
- **Não-risco:** relatórios lidos direto do banco principal atendem o p95, **desde que** o volume fique abaixo de ~100 mil reservas por semestre (premissa a monitorar).
