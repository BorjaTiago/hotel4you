---
type: subtask
summary: Configurar JUnit e a geração de Javadoc com um comando cada
parent: H4Y-18
requirements: [RNF-09, RNF-10]
points:
points_suggested:
status: published
jira_key: H4Y-35
labels: [quality, tests, docs, 1st-delivery]
sprint: 02
branch: feat-035-junit-javadoc-setup
---

# Qualidade de engenharia T1 e T2: configuração de Javadoc e JUnit

Origem: `epic-engineering-quality.md`, tarefas T1 e T2. Precisa do ficheiro de build de T0a. Começa depois do dia 1.
Responsável proposto: Pedro Costa (a equipa decide).

## Checklist

- [ ] Um comando gera o Javadoc sem erros.
- [ ] Uma classe pública sem Javadoc faz a verificação falhar e indica a classe.
- [ ] Um comando corre todos os testes JUnit.
- [ ] Um teste a falhar termina com erro e indica o teste.
- [ ] Regra escrita em `docs/`: cada teste tem a tag `@Tag("H4Y-NN-AC-n")`.
- [ ] Uma verificação ou script curto lista qualquer AC-n sem teste.
- [ ] Qualquer biblioteca nova está listada em `docs/libraries.md`.

Created by: BorjaTiago. Updated by: BorjaTiago. AI use: sprint-planner, Claude Code (claude-sonnet-5-5), 2026-10-16. Portuguese translation by general-purpose agent, Claude Code (claude-sonnet-5-5), 2026-10-16; doc-updater, Claude Code (claude-haiku-5-5), 2026-10-17. Reviewed by: pending.
