---
type: subtask
summary: Serviço: regra canAccess(location, actor) por papel e piso, com testes em tabela e sem ecrã
parent: H4Y-87
requirements: [RF-24, RF-25, RF-01]
points:
points_suggested:
status: published
jira_key: H4Y-94
labels: [access-control, 1st-delivery]
sprint: 06
branch: feat/H4Y-94-regra-acesso-por-local
---

# regra-acesso-por-local: a regra e os testes em tabela

História pai: regra-acesso-por-local (H4Y-87). Só a regra e os testes. Sem ecrã e sem script SQL. Uma pessoa faz tudo.
Responsável: a definir na planning do sprint 6.

## Checklist

- [ ] Escrever primeiro os testes com @Tag("H4Y-87-AC-n") e ver que falham (commit de testes antes do código).
- [ ] Antes do código: o diagrama não tem o método de regra de acesso. Acordar o nome (proposta: `LocationService.canAccess(location, actor)`) e o modo de saber o piso de um local (`Location` não tem o número do piso: ler `Floor` e `docs/uml/`). Acrescentar a `docs/uml/` no mesmo PR (regra 11 de `AGENTS.md`).
- [ ] AC-1: o rececionista acede a locais dos pisos 0 e 1.
- [ ] AC-2: o rececionista é recusado em locais de outros pisos (inclui quartos de hóspede).
- [ ] AC-3: o funcionário do restaurante acede só à cozinha e ao restaurante.
- [ ] AC-4: o funcionário do restaurante é recusado em qualquer outro local.
- [ ] AC-5: o gestor acede a locais de qualquer piso, sem limite.
- [ ] A regra usa o papel do utilizador (`Role`, `actor`) e não cria uma regra de papéis nova em `Permission`. A recusa devolve falso ou lança `AccessDeniedException` com "Acesso não permitido" (decidir no diagrama).
- [ ] Testes em tabela (`@ParameterizedTest`): papel, local, piso, resultado. Dados de teste próprios: locais dos pisos -1, 0, 1, 2 e 3, e uma cozinha de teste (foundation T4). Testes só de lógica, sem testes de texto SQL.
- [ ] Antigo AC-3 de restaurante (criar e apagar reservas): já feito. Não escrever teste novo. Testes existentes: `onCreate_throw_whenActorIsNotReceptionistOrManager`, `onDelete_throwAccessDenied_whenUserIsNotManager`, `onAllows_returnFalse_whenReceptionistDeletesReservations`.
- [ ] Javadoc no método novo. O Javadoc compila.

## Interface (UI)

Sem ecrã nesta subtask. O ecrã fica para as histórias de cartões.

Created by: BorjaTiago. Updated by: BorjaTiago. AI use: sprint-planner, Claude Code (claude-sonnet-5-5), 2026-11-13; jira-publisher, Claude Code (claude-sonnet-5-5), 2026-11-13. Reviewed by: pending.
