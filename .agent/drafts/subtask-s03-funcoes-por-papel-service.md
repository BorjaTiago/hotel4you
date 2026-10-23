---
type: subtask
summary: 23.1 Serviço: regra de funções por papel no HotelFacade e testes
parent: H4Y-23
requirements: [RF-02]
points:
points_suggested:
status: published
jira_key: H4Y-60
labels: [security, 1st-delivery]
sprint: 03
branch: feat/H4Y-60-role-permissions-service
---

# funcoes-por-papel: regra de permissões

História pai H4Y-23 (3 pontos). Cria uma regra única por papel na camada de serviço. A GUI só pergunta (RNF-07). Hoje existe `canCreateStaff`; a regra nova substitui ou generaliza esta verificação.
Responsável proposto: Lukas Sperandio (a equipa decide).

## Checklist

- [ ] Antes do código: acrescentar o método da regra em `docs/uml/class-diagram.md` e `docs/uml/classes.md` (regra 11 do AGENTS.md).
- [ ] Escrever primeiro os testes e ver que falham, com `@Tag("H4Y-23-AC-n")`.
- [ ] AC-1: o rececionista tem só as funções do seu papel. O gestor e o funcionário do restaurante também só têm as suas.
- [ ] AC-2: uma função exclusiva do gestor lança `AccessDeniedException` para o rececionista, e não executa nada.
- [ ] AC-3: sem utilizador autenticado, qualquer função protegida é recusada.
- [ ] Tabela papel -> funções em `docs/uml/guide.md` (só as funções que já existem e as do sprint 3).

## Interface (UI)

Sem ecrã nesta subtask. O menu que usa esta regra está em H4Y-61.

Created by: lukassperandio. Updated by: lukassperandio. AI use: sprint-planner, Claude Code (claude-opus-5-5), 2026-10-23; UI section by sprint-planner, Claude Code (claude-opus-5-5), 2026-10-23. Reviewed by: pending.
