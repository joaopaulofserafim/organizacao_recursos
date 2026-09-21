# Organização de Recursos — PRD (Product Requirements Document)

> Projeto semestral de Qualidade de Software · TADS · Senac · 2026.2 · Prof. Afonso
> Turma STADSCAS4NA · Equipe: _preencher nomes e RA dos integrantes_
> Fonte da especificação: `https://afonsolelis.github.io/aulas_senac/pages/qualidade2/especificacao-projeto.html`

**Convenção de origem** (Aula 04: fato × suposição):

- **[ESPEC]**: fato retirado da especificação oficial do professor.
- **[EQUIPE]**: decisão ou hipótese da equipe. Precisa ser validada com o professor. Se estiver errada, revise o item.
- **[PROF-A04]**: restrição dada pelo professor na Aula 04 (job diário).

---

## 1. Visão do produto

Um sistema web (API REST + interface responsiva) que organiza a **alocação de salas, professores e materiais** de uma instituição de ensino **sem conflitos de horário**. Ele mostra evidências objetivas de qualidade, segurança, rastreabilidade e desempenho. [ESPEC]

## 2. Problema

Hoje as reservas são feitas por planilha, e-mail e conversa de corredor. Por isso: [EQUIPE — contexto hipotético]

- a mesma sala é reservada duas vezes para o mesmo horário;
- um professor fica alocado em duas turmas ao mesmo tempo;
- equipamentos saem do almoxarifado e ninguém sabe com quem estão;
- salas em manutenção continuam sendo reservadas;
- não há histórico confiável de quem mudou o quê;
- a coordenação não sabe a taxa de uso real de cada sala.

## 3. Personas

As três personas vêm dos três perfis obrigatórios da especificação [ESPEC]. Nome, contexto, objetivos e frustrações são elaboração da equipe [EQUIPE].

### P1 — Solicitante: Profª. Marina Campos

| Campo | Descrição |
|---|---|
| Perfil do sistema | **SOLICITANTE** |
| Quem é | Professora de Redes, 38 anos. Leciona em 3 turmas, em dois períodos. |
| Contexto de uso | Usa o sistema principalmente pelo **celular**, no intervalo entre aulas, e às vezes pelo notebook em casa, à noite. |
| Objetivo | Encontrar rapidamente um **laboratório com 30+ computadores** livre no horário da aula e reservar junto um **projetor** e um **kit de roteadores**. |
| Tarefas | Consultar disponibilidade, criar, alterar e cancelar as próprias reservas, e acompanhar se o pedido foi aprovado. |
| Frustrações | Chegou ao laboratório e já havia outra turma. Pediu um projetor que estava "em conserto" e ninguém avisou. Não entende mensagens de erro técnicas. Não sabe se o pedido está pendente ou aprovado. |
| O que ela precisa sentir | "Se o sistema confirmou, a sala é minha." |
| Cenários de qualidade que gera | Sobreposição de horário, dupla reserva simultânea, recurso em manutenção, mensagens compreensíveis e responsividade no celular. |

### P2 — Responsável: Carlos Menezes, coordenador de laboratórios

| Campo | Descrição |
|---|---|
| Perfil do sistema | **RESPONSÁVEL** |
| Quem é | Coordenador de laboratórios e almoxarifado, 45 anos. Responde pelos recursos restritos (laboratórios especiais, equipamentos caros). |
| Contexto de uso | Usa o sistema no desktop da coordenação, durante o dia todo. Recebe muitos pedidos no início de cada semestre. |
| Objetivo | **Aprovar ou rejeitar** pedidos de recursos restritos com critério, validar que o professor alocado tem a competência exigida, e **controlar a retirada e a devolução** de materiais. |
| Tarefas | Ver a fila de pendências, aprovar ou rejeitar com justificativa, registrar retirada e devolução, e marcar "não compareceu". |
| Frustrações | Equipamento "sumido" sem registro de quem retirou. Aprovação feita por quem não tinha autoridade. Não consegue provar, numa auditoria, quem alterou uma reserva. |
| O que ele precisa sentir | "Nada restrito sai sem o meu aval, e tudo fica registrado." |
| Cenários de qualidade que gera | Autorização por perfil, acesso indevido, trilha de auditoria e estados da reserva (máquina de estados). |

### P3 — Administrador: Juliana Rocha, analista de TI acadêmica

| Campo | Descrição |
|---|---|
| Perfil do sistema | **ADMINISTRADOR** |
| Quem é | Analista de sistemas da secretaria acadêmica, 30 anos. Mantém os cadastros e responde por segurança e dados. |
| Contexto de uso | Desktop. Faz cargas de cadastro no início do semestre e consulta relatórios mensalmente para a diretoria. |
| Objetivo | Manter atualizados os cadastros de **salas, professores, materiais e usuários**, registrar **bloqueios e períodos de manutenção** e gerar **relatórios de utilização**. |
| Tarefas | Fazer o CRUD dos recursos e usuários, atribuir perfis, bloquear recurso para manutenção e emitir relatórios de uso, carga horária e conflitos evitados. |
| Frustrações | Relatórios montados à mão em planilha. Usuário com perfil errado vendo o que não devia. Dados pessoais espalhados sem controle (LGPD). |
| O que ela precisa sentir | "Os dados estão corretos, protegidos e eu consigo provar isso." |
| Cenários de qualidade que gera | Segurança/LGPD, gestão de perfis, relatórios corretos e desempenho de consultas. |

## 4. Escopos

| Escopo | Nome | Do que trata | Personas |
|---|---|---|---|
| **E1** | Identidade & Acesso | Login, sessão, perfis (Solicitante, Responsável, Administrador), gestão de usuários | P1, P2, P3 |
| **E2** | Cadastro de Recursos | Salas, professores (competências), materiais, pesquisa e disponibilidade | P3, P1 |
| **E3** | Reservas & Agenda | Criar, alterar e cancelar reservas, detectar sobreposição, concorrência | P1 |
| **E4** | Aprovação & Operação | Aprovação de restritos, manutenção/bloqueio, retirada/devolução, estados | P2, P3 |
| **E5** | Auditoria, Relatórios & Integrações | Histórico auditável, relatórios, notificação, documentação de API e job diário | P2, P3 |

## 5. Requisitos funcionais (RF)

| ID | Requisito | Escopo | Persona | Origem | Prioridade |
|---|---|---|---|---|---|
| RF-01 | O sistema deve autenticar usuários por e-mail e senha e emitir um token de acesso. | E1 | P1, P2, P3 | [ESPEC] | Alta |
| RF-02 | O sistema deve autorizar cada operação conforme o perfil do usuário (SOLICITANTE, RESPONSAVEL, ADMINISTRADOR). | E1 | P1, P2, P3 | [ESPEC] | Alta |
| RF-03 | O Administrador deve poder cadastrar, editar, desativar usuários e atribuir perfis. | E1 | P3 | [ESPEC] | Média |
| RF-04 | O Administrador deve poder cadastrar, editar e consultar **salas** (nome, tipo, capacidade, localização/bloco, se é restrita). | E2 | P3 | [ESPEC] | Alta |
| RF-05 | O Administrador deve poder cadastrar, editar e consultar **professores** (nome, competências/disciplinas, carga horária máxima). | E2 | P3 | [ESPEC] | Alta |
| RF-06 | O Administrador deve poder cadastrar, editar e consultar **materiais** (nome, tipo, patrimônio, quantidade, se é restrito). | E2 | P3 | [ESPEC] | Alta |
| RF-07 | Qualquer usuário autenticado deve poder **pesquisar** recursos por tipo, capacidade, localização, competência e disponibilidade em um intervalo de datas. | E2 | P1 | [ESPEC] | Alta |
| RF-08 | O Solicitante deve poder consultar a **agenda de disponibilidade** de um recurso em um período. | E3 | P1 | [ESPEC] | Alta |
| RF-09 | O Solicitante deve poder **criar reserva** informando recurso(s), professor, data/hora de início e de término e finalidade. | E3 | P1 | [ESPEC] | Alta |
| RF-10 | O Solicitante deve poder **alterar** uma reserva própria que ainda não foi iniciada. | E3 | P1 | [ESPEC] | Alta |
| RF-11 | O Solicitante deve poder **cancelar** uma reserva própria que ainda não foi iniciada. | E3 | P1 | [ESPEC] | Alta |
| RF-12 | O sistema deve **detectar e recusar sobreposição** de horário para o mesmo recurso (sala, material) **e na agenda do professor**. | E3 | P1 | [ESPEC] | Crítica |
| RF-13 | O sistema deve garantir que **solicitações simultâneas** para o mesmo recurso e horário produzam **uma única reserva aceita**. | E3 | P1 | [ESPEC] | Crítica |
| RF-14 | Reservas de **recursos restritos** devem ficar SOLICITADA até **aprovação** de um Responsável. Recursos não restritos são aprovados automaticamente. | E4 | P2 | [ESPEC] + [EQUIPE: aprovação automática dos não restritos] | Crítica |
| RF-15 | O Responsável deve poder **aprovar ou rejeitar** reservas pendentes, informando justificativa na rejeição. | E4 | P2 | [ESPEC] | Alta |
| RF-16 | O Administrador deve poder **bloquear um recurso** por um período de manutenção. Nesse período o recurso não pode ser reservado. | E4 | P3 | [ESPEC] | Crítica |
| RF-17 | O Responsável deve poder **registrar a retirada** de material/equipamento, vinculada a uma reserva aprovada. | E4 | P2 | [ESPEC] | Alta |
| RF-18 | O Responsável deve poder **registrar a devolução** de material/equipamento, com condição (ok/danificado). | E4 | P2 | [ESPEC] + [EQUIPE: campo condição] | Alta |
| RF-19 | O sistema deve controlar o **ciclo de vida da reserva**: SOLICITADA → APROVADA → EM_USO → CONCLUIDA, com os estados alternativos REJEITADA, CANCELADA e NAO_COMPARECEU. | E4 | P2 | [ESPEC] | Crítica |
| RF-20 | O sistema deve manter **histórico auditável** de toda mudança (quem, quando, o quê, valor anterior e novo). | E5 | P2, P3 | [ESPEC] | Crítica |
| RF-21 | O sistema deve enviar **notificação** (simulada ou via API externa) ao Solicitante quando a reserva for aprovada, rejeitada ou cancelada. | E5 | P1 | [ESPEC] | Média |
| RF-22 | O Administrador deve poder gerar **relatório de utilização por recurso** (horas reservadas ÷ horas disponíveis) em um período. | E5 | P3 | [ESPEC] | Média |
| RF-23 | O Administrador deve poder gerar **relatório de carga horária alocada** por professor em um período. | E5 | P3 | [ESPEC] | Média |
| RF-24 | O Administrador deve poder gerar **relatório de conflitos evitados** (tentativas recusadas por sobreposição, manutenção ou concorrência). | E5 | P3 | [ESPEC] | Média |
| RF-25 | O sistema deve apresentar **mensagens de erro compreensíveis**, com código e texto em português, sem detalhes técnicos internos. | E1–E5 | P1 | [ESPEC] | Alta |
| RF-26 | O sistema deve publicar **documentação da API** (OpenAPI/Swagger) com todos os endpoints públicos. | E5 | P3 | [ESPEC] | Média |
| RF-27 | Um **job diário**, ao final do dia, deve treinar/retreinar um **modelo de previsão de demanda** de recursos usando **somente as reservas novas** desde a última execução. O modelo apoia a sugestão de salas. | E5 | P3 | [PROF-A04] | Média |

## 6. Requisitos não funcionais (RNF)

> Regra da Aula 03: **"RNF só existe se for medível"**. Todo número marcado [EQUIPE] é hipótese e fica com **validação pendente** com o professor.
>
> Nomes das características seguem a **ISO/IEC 25010 revisão 2023** (Aula 01): *Usabilidade* agora é **Capacidade de Interação** e *Portabilidade* agora é **Flexibilidade**.

| ID | Atributo (ISO/IEC 25010) | Requisito medível | Escopo | Origem |
|---|---|---|---|---|
| RNF-01 | Eficiência de Performance: tempo de resposta | Consulta de disponibilidade (RF-07/RF-08) com **50 usuários simultâneos**: **p95 < 800 ms** e nenhuma requisição > 3 s. | E2, E3 | [EQUIPE] |
| RNF-02 | Eficiência de Performance: tempo de resposta | Criação de reserva (RF-09) com 50 usuários simultâneos: **p95 < 1,5 s**. | E3 | [EQUIPE] |
| RNF-03 | Confiabilidade: integridade sob concorrência | **100 requisições simultâneas** para o mesmo recurso e horário resultam em **exatamente 1 reserva criada (HTTP 201)** e **99 recusas (HTTP 409)**, com **0 duplicatas** no banco. | E3 | [ESPEC] + [EQUIPE: 100 req] |
| RNF-04 | Segurança: autenticidade | **100%** dos endpoints, exceto `/auth/login`, documentação e health check, exigem token válido. Sem token → HTTP 401. | E1 | [ESPEC] |
| RNF-05 | Segurança: confidencialidade/autorização | Qualquer operação fora do perfil retorna **HTTP 403** em **100%** dos casos da matriz perfil × endpoint (testada automaticamente). | E1 | [ESPEC] |
| RNF-06 | Segurança: sessão | Token de acesso expira em **30 min** [EQUIPE]. Token expirado → HTTP 401. | E1 | [EQUIPE] |
| RNF-07 | Segurança: credenciais | Senhas armazenadas com **BCrypt (custo ≥ 10)**. **0** segredos no repositório (verificado no SonarCloud/secret scanning). | E1 | [EQUIPE] |
| RNF-08 | Segurança: validação de entrada | **100%** dos campos de entrada validados. Entrada inválida → HTTP 400 com lista de campos inválidos. | E1–E5 | [ESPEC] |
| RNF-09 | Segurança: tratamento seguro de erros | **0** respostas de erro com stack trace, SQL ou nome de classe interna (verificado por teste de API). | E1–E5 | [ESPEC] |
| RNF-10 | Segurança: responsabilização (auditoria) | **100%** das mudanças de estado e alterações de cadastro geram registro de auditoria com usuário, data/hora (UTC), entidade, valor anterior e novo. Registros de auditoria são **somente inserção** (0 updates/deletes). | E5 | [ESPEC] |
| RNF-11 | Manutenibilidade: testabilidade | Cobertura **≥ 80% de linhas** e **≥ 70% de branches** (JaCoCo). | Todos | [ESPEC] |
| RNF-12 | Manutenibilidade: análise estática | **0 bugs e 0 vulnerabilidades críticas/bloqueantes** no SonarCloud. Quality Gate aprovado em todo PR. | Todos | [ESPEC] |
| RNF-13 | Processo: rastreabilidade (meta da especificação, não é característica ISO de produto) | **100%** dos requisitos críticos presentes no `RTM.md` com teste e evidência. | Todos | [ESPEC] |
| RNF-14 | Capacidade de Interação: operabilidade em telas pequenas | Telas de consulta e reserva utilizáveis em largura **≥ 360 px**, sem rolagem horizontal. | E3 | [ESPEC] + [EQUIPE: 360 px] |
| RNF-15 | Capacidade de Interação: mensagens de erro | **100%** das mensagens de erro de regra de negócio têm código (ex.: `RESERVA_SOBREPOSTA`) e texto em português que diz o que fazer. | Todos | [ESPEC] |
| RNF-16 | Flexibilidade: instalabilidade | Um colega que não montou o ambiente sobe o sistema seguindo o README em **≤ 15 min**, num Codespace novo, com os comandos documentados (`./mvnw spring-boot:test-run`). | Todos | [EQUIPE] |
| RNF-17 | Confiabilidade: disponibilidade (isolamento batch/online) | Durante o job diário (RF-27), o p95 da consulta de disponibilidade **não piora mais que 10%**. | E5 | [PROF-A04] + [EQUIPE: 10%] |
| RNF-18 | Confiabilidade: recuperabilidade do job diário | Falha do job **não avança o checkpoint** e **não altera o modelo ativo**. Reexecução não duplica registros (0 duplicatas). | E5 | [PROF-A04] |
| RNF-19 | Segurança: confidencialidade (LGPD) | O job de treino usa **somente dados não pessoais** (recurso, data, hora, duração). **0** campos de nome/e-mail no dataset de treino. | E5 | [EQUIPE] |
| RNF-20 | Adequação Funcional: correção (fuso horário) | Todas as datas são armazenadas em UTC e exibidas em `America/Sao_Paulo`. | E3 | [EQUIPE] |

## 7. Regras de negócio (RN)

| ID | Regra | Relacionada a | Origem |
|---|---|---|---|
| RN-01 | O **término deve ser posterior ao início** (término > início). Término = início é inválido. | RF-09, RF-10 | [ESPEC] |
| RN-02 | Reservas do **mesmo recurso** (sala ou material) **não podem se sobrepor**. Intervalos são semiabertos `[início, fim)`: uma reserva 08:00–10:00 **não** conflita com 10:00–12:00. | RF-12 | [ESPEC] + [EQUIPE: intervalo semiaberto] |
| RN-03 | O **mesmo professor** não pode estar em duas reservas sobrepostas. | RF-12 | [ESPEC] |
| RN-04 | **Duas solicitações simultâneas** para o mesmo recurso e horário produzem **uma única reserva aceita**. | RF-13 | [ESPEC] |
| RN-05 | Recurso **em manutenção** no intervalo solicitado **não pode ser reservado**. | RF-16 | [ESPEC] |
| RN-06 | Reserva de **recurso restrito** nasce como SOLICITADA e só vai para APROVADA por ação de um **RESPONSAVEL**. | RF-14, RF-15 | [ESPEC] |
| RN-07 | **Somente RESPONSAVEL** aprova ou rejeita recurso restrito. Solicitante e Administrador recebem 403. | RF-15 | [ESPEC] |
| RN-08 | Reservas **iniciadas** (EM_USO ou CONCLUIDA) **não podem ser apagadas** nem alteradas. | RF-10, RF-11 | [ESPEC] |
| RN-09 | **Toda mudança de estado** gera registro de auditoria. | RF-20 | [ESPEC] |
| RN-10 | Transições permitidas: SOLICITADA→APROVADA, SOLICITADA→REJEITADA, SOLICITADA→CANCELADA, APROVADA→EM_USO, APROVADA→CANCELADA, APROVADA→NAO_COMPARECEU, EM_USO→CONCLUIDA. **Qualquer outra transição é proibida.** | RF-19 | [ESPEC] + [EQUIPE: transições exatas] |
| RN-11 | Reservas REJEITADA, CANCELADA e NAO_COMPARECEU **não bloqueiam agenda** (não contam para sobreposição). | RF-12 | [EQUIPE] |
| RN-12 | Solicitante só altera ou cancela **as próprias** reservas. | RF-10, RF-11 | [EQUIPE] |
| RN-13 | Retirada de material só pode ser registrada para reserva **APROVADA**. Devolução só para material **retirado**. | RF-17, RF-18 | [EQUIPE] |
| RN-14 | O professor alocado deve ter a **competência** exigida pela sala, quando a sala exigir competência (ex.: laboratório de química). | RF-05, RF-15 | [ESPEC: "valida a alocação de docentes"] + [EQUIPE] |
| RN-15 | Capacidade da sala ≥ número de participantes informado. | RF-09 | [EQUIPE] |
| RN-16 | Não é permitido criar reserva com início no passado. | RF-09 | [EQUIPE] |

## 8. Fora de escopo

- Pagamento ou cobrança por uso de recurso.
- Aplicativo mobile nativo (a interface web responsiva atende o celular).
- Integração com o sistema acadêmico real do Senac.
- Envio real de e-mail/SMS. A notificação é simulada ou feita via API externa mockada com WireMock.
- Login com SSO/Google.

## 9. Métricas de sucesso

| Métrica | Meta | Origem |
|---|---|---|
| Dupla reserva em produção/teste de carga | 0 | [ESPEC] |
| Cobertura de linhas / branches | ≥ 80% / ≥ 70% | [ESPEC] |
| Requisitos críticos na RTM | 100% | [ESPEC] |
| Bugs/vulnerabilidades críticas no SonarCloud | 0 | [ESPEC] |
| Conflitos evitados registrados no relatório (RF-24) | 100% das recusas registradas | [EQUIPE] |

## 10. Perguntas abertas (validar com o professor)

1. Os limites numéricos de RNF-01, RNF-02, RNF-06 e RNF-16 são aceitáveis?
2. Recurso não restrito deve ser aprovado automaticamente (RF-14)?
3. Existe interface web obrigatória ou basta API + Swagger? A especificação pede "interface responsiva".
4. O job diário (RF-27) é obrigatório no projeto de recursos ou foi só no case da aula? Sugerimos "previsão de demanda".
5. Uma reserva pode ter vários recursos (sala + materiais) ao mesmo tempo, ou é uma reserva por recurso?
6. A especificação pede Spring Boot 3.x, mas hoje o start.spring.io só oferece 4.x (a linha 3.5 saiu do suporte gratuito em 30/06/2026). Usamos a **3.5.16**, a última 3.x. Está certo, ou podemos usar 4.x?
