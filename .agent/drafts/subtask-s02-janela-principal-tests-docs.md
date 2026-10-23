---
type: subtask
summary: Testes, Javadoc e atualização do diagrama da janela principal
parent: H4Y-24
requirements: [RNF-06, RNF-07]
points:
points_suggested:
status: published
jira_key: H4Y-43
labels: [foundation, tests, docs, 1st-delivery]
sprint: 02
branch: feat-024-main-window-tests-docs
---

# janela-principal: testes, Javadoc, diagrama

História pai H4Y-24. A chave 024 do ramo é a chave da história.
Responsável proposto: Pedro Costa (a equipa decide).

## Checklist

- [ ] AC-3: testes JUnit de `canCreateStaff` com a tag `@Tag("H4Y-24-AC-3")` (gestor sim; rececionista, funcionário do restaurante e `null` não). Ficheiros: `StaffServiceTest`, `HotelFacadeTest`.
- [ ] AC-1, AC-2 e AC-4: são ecrãs. A equipa não usa TestFX, por isso verificam-se à mão. Registar no PR o nome, a data e o resultado de cada verificação.
- [ ] Javadoc nas novas classes públicas. O Javadoc compila.
- [ ] Diagrama de classes atualizado com `MainController` e com a referência de uso de IA. Não há `Session` nem registo de navegação: não existem no diagrama.
- [ ] Nenhuma biblioteca nova, ou está em `docs/libraries.md`.

Created by: BorjaTiago. Updated by: lukassperandio. AI use: sprint-planner, Claude Code (claude-sonnet-5-5), 2026-10-16. Portuguese translation by general-purpose agent, Claude Code (claude-sonnet-5-5), 2026-10-16; doc-updater, Claude Code (claude-haiku-5-5), 2026-10-17; doc-updater, Claude Code (claude-opus-5-5), 2026-10-23. Reviewed by: pending.
