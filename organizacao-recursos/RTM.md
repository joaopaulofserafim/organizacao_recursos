# RTM — Matriz de Rastreabilidade

> Liga **requisito → risco → caso de teste → evidência**. Atualize a coluna Evidência a cada PR (link do GitHub Actions, commit ou relatório).
> Status: ⬜ sem teste · 🟥 teste vermelho (TDD red) · 🟩 passou · ⚠️ risco aceito

| Requisito | Crítico? | Risco coberto | Caso(s) de teste | Evidência | Status |
|---|---|---|---|---|---|
| RF-01 Autenticação | Sim | Acesso sem credencial | CT-API-auth-01..03 | _pendente_ | ⬜ |
| RF-02 Autorização por perfil | Sim | Perfil errado executa ação | CT-002, matriz perfil×endpoint | _pendente_ | ⬜ |
| RF-09 Criar reserva | Sim | Dados inválidos aceitos | RN01_*, RN15_*, RN16_* | _pendente_ | ⬜ |
| RF-10/RF-11 Alterar/cancelar | Sim | Alterar reserva iniciada/alheia | RN08_*, RN12_* | _pendente_ | ⬜ |
| RF-12 Sobreposição | Sim | Dupla ocupação sala/professor | CT-003, RN02_*, RN03_* | _pendente_ | ⬜ |
| RF-13 Concorrência | Sim | **Dupla reserva (limitador de nota)** | CT-001, RN04_* | _pendente_ | ⬜ |
| RF-14/RF-15 Aprovação | Sim | Restrito sem aval | RN06_*, RN07_* | _pendente_ | ⬜ |
| RF-16 Manutenção | Sim | Reserva de recurso quebrado | RN05_* | _pendente_ | ⬜ |
| RF-17/RF-18 Retirada/devolução | Não | Material sem controle | RN13_* | _pendente_ | ⬜ |
| RF-19 Estados | Sim | Transição proibida | RN10_* | _pendente_ | ⬜ |
| RF-20 Auditoria | Sim | Mudança sem rastro | RN09_* | _pendente_ | ⬜ |
| RF-21 Notificação | Não | Falha externa derruba a reserva | CT-WM-01 (WireMock) | _pendente_ | ⬜ |
| RF-22..24 Relatórios | Não | Número errado | CT-REL-01..03 | _pendente_ | ⬜ |
| RF-25 Mensagens de erro | Não | Stack trace exposto | CT-ERR-01 | _pendente_ | ⬜ |
| RF-26 Documentação API | Não | Contrato desatualizado | Swagger gerado no build | _pendente_ | ⬜ |
| RF-27 Job diário | Não | Checkpoint avança sem sucesso | CT-JOB-01..03 | _pendente_ | ⬜ |
| RNF-01/02 Desempenho | Não | Lentidão no pico | CT-PERF-01..02 (JMeter) | _pendente_ | ⬜ |
| RNF-03 Concorrência sob carga | Sim | Dupla reserva sob carga | CT-PERF-03 (JMeter) | _pendente_ | ⬜ |
| RNF-11/12 Cobertura e Sonar | Sim | Código sem teste | JaCoCo + SonarCloud no CI | _pendente_ | ⬜ |
