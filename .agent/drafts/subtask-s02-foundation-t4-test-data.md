---
type: subtask
summary: T4 - Mecanismo de dados de teste por história e verificação das camadas (T1)
parent: H4Y-11
requirements: [RNF-07, RNF-08, RNF-10]
points:
points_suggested:
status: replaced
jira_key: H4Y-34
labels: [foundation, tests, 1st-delivery]
sprint: 02
branch: feat-034-test-data-layer-check
---

# T4 e T1: Mecanismo de dados de teste e verificação das camadas

Substituído por H4Y-32. O cartão H4Y-34 foi apagado no Jira. Estado: `replaced`.

Origem: `epic-foundation-architecture.md`, tarefas T4 e T1. Precisa de T0a. Este cartão faz com que cada história se teste sozinha, por isso nenhuma história bloqueia outra. Cada história seguinte acrescenta o seu próprio ficheiro de dados.
Responsável proposto: José Cruz (a equipa decide).

## Checklist

- [ ] Um carregador simples coloca dados de teste nos repositórios em memória.
- [ ] O mesmo carregador funciona numa base de dados SQL Server de teste. Nunca corre na base de produção.
- [ ] Cada história tem o seu próprio ficheiro de dados. Uma história nunca edita o ficheiro de outra.
- [ ] Os dados de teste não têm palavras-passe reais. Os utilizadores de teste usam um hash falso do `PasswordHasher`.
- [ ] Existem os ficheiros de dados do sprint 2: utilizadores (gestor, rececionista, restaurante), para as 4 histórias.
- [ ] T1: uma regra de negócio corre por JUnit sem a GUI.
- [ ] T1: uma nova função não quebra os testes das funções antigas.
- [ ] T1: quando uma regra recusa, a GUI mostra o motivo e não decide a regra.

Created by: BorjaTiago. Updated by: BorjaTiago. AI use: sprint-planner, Claude Code (claude-sonnet-5-5), 2026-10-16. Portuguese translation by general-purpose agent, Claude Code (claude-sonnet-5-5), 2026-10-16; doc-updater, Claude Code (claude-sonnet-5-5), 2026-10-19. Reviewed by: pending.
