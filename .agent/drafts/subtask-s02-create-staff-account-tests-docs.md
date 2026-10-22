---
type: subtask
summary: Testes, Javadoc e atualização do diagrama de criar conta de pessoal
parent: H4Y-27
requirements: [RF-26, RF-01, RNF-02]
points:
points_suggested:
status: published
jira_key: H4Y-41
labels: [staff, tests, docs, 1st-delivery]
sprint: 02
branch: feat-027-create-staff-tests-docs
---

# create-staff-account: testes, Javadoc, diagrama

História pai H4Y-27. A chave 027 do ramo é a chave da história.
Responsável proposto: Tiago Borja (a equipa decide).

## Checklist

- [ ] Um teste JUnit por cada AC-1 a AC-4. Os nomes citam "H4Y-27 AC-n".
- [ ] Um teste mostra que a palavra-passe guardada difere da introduzida (AC-1).
- [ ] Dados de teste: base vazia e um utilizador existente "ana".
- [ ] Javadoc nas novas classes públicas. O Javadoc compila.
- [ ] Diagrama de classes atualizado (`StaffService`), com a referência de uso de IA.
- [ ] Verificação manual: o gestor cria uma conta de rececionista e uma do restaurante que iniciam sessão (o login-restaurante do sprint 3 usa-a).

Created by: BorjaTiago. Updated by: BorjaTiago. AI use: sprint-planner, Claude Code (claude-sonnet-5-5), 2026-10-16. Portuguese translation by general-purpose agent, Claude Code (claude-sonnet-5-5), 2026-10-16; doc-updater, Claude Code (claude-haiku-5-5), 2026-10-17; doc-updater, Claude Code (claude-sonnet-5-5), 2026-10-19. Reviewed by: pending.
