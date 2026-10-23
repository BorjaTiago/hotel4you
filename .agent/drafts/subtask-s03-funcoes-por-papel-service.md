---
type: subtask
summary: 23.1 Serviço: regra de funções por papel no HotelFacade, login do restaurante e testes
parent: H4Y-23
requirements: [RF-01, RF-02]
points:
points_suggested:
status: published
jira_key: H4Y-60
labels: [security, 1st-delivery]
sprint: 03
branch: feat/H4Y-60-role-permissions-service
---

# funcoes-por-papel: regra de permissões

História pai H4Y-23 (3 pontos). Cria uma regra única por papel na camada de serviço. A GUI só pergunta (RNF-07). Esta subtask também fecha H4Y-21 (login do restaurante, 2 pontos): o login já existe, falta só o papel `RESTAURANT` e os testes. H4Y-21 não tem subtask própria (decisão do Team Leader, 2026-10-23: nada de subtasks só de testes). Hoje existe `canCreateStaff`; a regra nova substitui ou generaliza esta verificação.
Responsável proposto: Lukas Sperandio (a equipa decide).

## Checklist

- [ ] Antes do código: acrescentar o método da regra em `docs/uml/class-diagram.md` e `docs/uml/classes.md` (regra 11 do AGENTS.md).
- [ ] Escrever primeiro os testes e ver que falham, com `@Tag("H4Y-23-AC-n")`.
- [ ] AC-1: o rececionista tem só as funções do seu papel.
- [ ] AC-2: uma função exclusiva do gestor lança `AccessDeniedException` para o rececionista, e não executa nada.
- [ ] AC-3: sem utilizador autenticado, qualquer função protegida é recusada.
- [ ] H4Y-21: testes com `@Tag("H4Y-21-AC-1")` a `@Tag("H4Y-21-AC-3")`. O funcionário do restaurante inicia sessão com o papel `RESTAURANT`; palavra-passe errada e utilizador desconhecido dão "Credenciais inválidas".
- [ ] H4Y-21: o funcionário do restaurante vê só as funções do seu papel.
- [ ] Tabela papel -> funções em `docs/uml/guide.md` (só as funções que já existem e as do sprint 3).

Created by: lukassperandio. Updated by: lukassperandio. AI use: sprint-planner, Claude Code (claude-opus-5-5), 2026-10-23. Reviewed by: pending.
