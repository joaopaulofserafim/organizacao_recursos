# Plano TDD — Inventário de regras de negócio (Aula 05, fase RED)

> Fonte: `docs/prd.md` §7 (RN-01 a RN-16), `docs/arquitetura.md` (ADR-002, ADR-003).
> Objetivo: **cada regra tem ao menos um teste que FALHA** antes da implementação.
> Convenção de nome do teste: `RNxx_descricao_resultadoEsperado`.

## Inventário

| Regra | Caso | Tipo | Entrada | Esperado | Teste (JUnit 5) | Status |
|---|---|---|---|---|---|---|
| RN-01 | Término depois do início | Caminho feliz | 08:00–10:00 | Válido | `RN01_terminoDepoisDoInicio_aceita` | a escrever |
| RN-01 | Término = início | **Limite** | 08:00–08:00 | 400 `PERIODO_INVALIDO` | `RN01_terminoIgualInicio_rejeita` | a escrever |
| RN-01 | Término antes do início | Inválido | 10:00–08:00 | 400 `PERIODO_INVALIDO` | `RN01_terminoAntesDoInicio_rejeita` | a escrever |
| RN-01 | Início ou término nulo | Inválido | null | 400 | `RN01_datasNulas_rejeita` | a escrever |
| RN-02 | Sem conflito | Feliz | Sala vazia | 201 | `RN02_salaLivre_criaReserva` | a escrever |
| RN-02 | Sobrepõe 1 min | **Limite** | 09:59–11:00 vs 08:00–10:00 | 409 `RESERVA_SOBREPOSTA` | `RN02_sobreposicaoDeUmMinuto_rejeita` | a escrever |
| RN-02 | Encosta no fim | **Limite** | 10:00–11:00 vs 08:00–10:00 | 201 | `RN02_intervaloAdjacente_aceita` | a escrever |
| RN-02 | Contida dentro de outra | Conflito | 08:30–09:00 | 409 | `RN02_reservaContida_rejeita` | a escrever |
| RN-02 | Envolve outra | Conflito | 07:00–11:00 | 409 | `RN02_reservaQueEnvolve_rejeita` | a escrever |
| RN-02 | Mesmo horário, outra sala | Partição | Sala B | 201 | `RN02_outraSalaMesmoHorario_aceita` | a escrever |
| RN-02 | Material já reservado | Conflito | Projetor P1 | 409 | `RN02_materialSobreposto_rejeita` | a escrever |
| RN-03 | Professor livre | Feliz | — | 201 | `RN03_professorLivre_aceita` | a escrever |
| RN-03 | Professor em outra sala no horário | Conflito | Sala B, mesmo prof. | 409 `PROFESSOR_INDISPONIVEL` | `RN03_professorSobreposto_rejeita` | a escrever |
| RN-04 | 100 pedidos simultâneos | Concorrência | 100 threads | 1× 201, 99× 409, 1 linha | `RN04_pedidosSimultaneos_umaUnicaReserva` | a escrever |
| RN-05 | Recurso em manutenção | Estado proibido | Manut. 08–12, pedido 09–10 | 409 `RECURSO_EM_MANUTENCAO` | `RN05_recursoEmManutencao_rejeita` | a escrever |
| RN-05 | Pedido logo após a manutenção | **Limite** | Manut. até 12:00, pedido 12:00 | 201 | `RN05_aposFimDaManutencao_aceita` | a escrever |
| RN-06 | Recurso restrito | Feliz | Lab restrito | Estado SOLICITADA | `RN06_recursoRestrito_ficaSolicitada` | a escrever |
| RN-06 | Recurso comum | Partição | Sala comum | Estado APROVADA | `RN06_recursoComum_aprovadaAutomaticamente` | a escrever |
| RN-07 | Responsável aprova | Feliz | RESPONSAVEL | 200, APROVADA | `RN07_responsavelAprova_aceita` | a escrever |
| RN-07 | Solicitante aprova | Proibido | SOLICITANTE | 403 | `RN07_solicitanteAprova_negado` | a escrever |
| RN-07 | Administrador aprova | Proibido | ADMINISTRADOR | 403 | `RN07_administradorAprova_negado` | a escrever |
| RN-08 | Excluir reserva EM_USO | Estado proibido | EM_USO | 409 `RESERVA_INICIADA` | `RN08_excluirReservaEmUso_rejeita` | a escrever |
| RN-08 | Alterar reserva CONCLUIDA | Estado proibido | CONCLUIDA | 409 | `RN08_alterarConcluida_rejeita` | a escrever |
| RN-09 | Aprovação gera auditoria | Feliz | Aprovar | 1 registro com antes/depois | `RN09_mudancaDeEstado_geraAuditoria` | a escrever |
| RN-10 | Transições permitidas (7) | Tabela de decisão | Parametrizado | Sucesso | `RN10_transicaoPermitida_aceita` | a escrever |
| RN-10 | Transições proibidas (ex.: CONCLUIDA→APROVADA) | Tabela de decisão | Parametrizado | 409 `TRANSICAO_INVALIDA` | `RN10_transicaoProibida_rejeita` | a escrever |
| RN-11 | Reserva cancelada libera horário | Feliz | CANCELADA no horário | 201 | `RN11_reservaCanceladaNaoBloqueia_aceita` | a escrever |
| RN-12 | Cancelar reserva de outro | Proibido | Outro solicitante | 403 | `RN12_cancelarReservaAlheia_negado` | a escrever |
| RN-13 | Retirada com reserva SOLICITADA | Estado proibido | SOLICITADA | 409 | `RN13_retiradaSemAprovacao_rejeita` | a escrever |
| RN-13 | Devolver material não retirado | Estado proibido | — | 409 | `RN13_devolucaoSemRetirada_rejeita` | a escrever |
| RN-14 | Professor sem competência | Inválido | Lab. química, prof. de redes | 409 `COMPETENCIA_INCOMPATIVEL` | `RN14_professorSemCompetencia_rejeita` | a escrever |
| RN-15 | Participantes = capacidade | **Limite** | 30 em sala de 30 | 201 | `RN15_participantesIgualCapacidade_aceita` | a escrever |
| RN-15 | Participantes > capacidade | **Limite** | 31 em sala de 30 | 400 `CAPACIDADE_EXCEDIDA` | `RN15_participantesAcimaCapacidade_rejeita` | a escrever |
| RN-16 | Início no passado | Inválido | Ontem | 400 `DATA_NO_PASSADO` | `RN16_inicioNoPassado_rejeita` | a escrever |

## Ambiguidades e perguntas

- RN-06: recurso comum é aprovado automaticamente? (hipótese da equipe)
- RN-10: NAO_COMPARECEU pode ser marcado a partir de quanto tempo após o início? (lacuna)
- RN-14: onde a competência exigida é cadastrada, na sala ou na reserva? (lacuna)
- RN-16: pode alterar uma reserva cujo início já passou, mas que ainda não foi iniciada? (lacuna)

## Como fica a matriz após a fase RED

Depois de rodar o Prompt 2 da Aula 05, troque o `Status` de cada linha para **vermelho (falha esperada)** e anote o motivo da falha (ex.: `NotImplemented`, `expected 409 but was 201`). Se algum teste já **passar**, registre abaixo e investigue.

| Teste que passou sem implementação | Motivo investigado |
|---|---|
| _(vazio)_ | |
