---
type: subtask
summary: T0b - Janela JavaFX vazia, registo de navegação, sessão simulada e primeiro diagrama de classes
parent: H4Y-11
requirements: [RNF-05, RNF-06, RNF-07, RNF-08]
points:
points_suggested:
status: replaced
jira_key: H4Y-31
labels: [foundation, gui, diagram, 1st-delivery]
sprint: 02
branch: feat-031-gui-shell-class-diagram
---

# T0b: Janela JavaFX vazia, registo de navegação, sessão simulada, diagrama de classes

Substituído por H4Y-32. O cartão H4Y-31 foi apagado no Jira. Estado: `replaced`.

Origem: `epic-foundation-architecture.md`, tarefa T0, ponto 4 e passo (e). Começa com as interfaces base de T0a. Os stubs deixam os cartões das histórias começar sem esperar pelo login.
Responsável proposto: Pedro Costa (a equipa decide).

## Checklist

- [ ] Uma janela JavaFX vazia arranca a partir do pacote `gui`.
- [ ] Existe um registo de navegação. Uma função regista um ecrã com os papéis que o podem ver.
- [ ] Uma `Session` simulada (stub) pode ser definida como rececionista ou gestor para testes e demo. O login real substitui-a mais tarde.
- [ ] Um `PermissionPolicy` stub devolve as funções por papel (RF-02). `funcoes-por-papel` (sprint 3) substitui-o.
- [ ] A GUI só chama serviços. Nenhuma regra de negócio em `gui` (RNF-07).
- [ ] Diagrama de classes inicial (domínio, serviços, repositórios) em `docs/`, com a referência de uso de IA (RNF-25).
- [ ] A ordem de criação das classes está escrita em `docs/`. A equipa revê-a na planning.

Created by: BorjaTiago. Updated by: BorjaTiago. AI use: sprint-planner, Claude Code (claude-sonnet-5-5), 2026-10-16. Portuguese translation by general-purpose agent, Claude Code (claude-sonnet-5-5), 2026-10-16; doc-updater, Claude Code (claude-sonnet-5-5), 2026-10-19. Reviewed by: pending.
