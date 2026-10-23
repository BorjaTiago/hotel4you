---
type: subtask
summary: Serviço de criar conta de pessoal - nome único, palavra-passe com hash, só gestor
parent: H4Y-27
requirements: [RF-26, RF-01, RNF-02]
points:
points_suggested:
status: published
jira_key: H4Y-40
labels: [staff, backend, 1st-delivery]
sprint: 02
branch: feat-027-create-staff-service
---

# create-staff-account: serviço

História pai H4Y-27 (3 pontos). Precisa de H4Y-32. H4Y-32 deixa `StaffService.createStaff` como esqueleto que lança `UnsupportedOperationException`; este cartão escreve a lógica. Os testes usam um gestor simulado como `actor` e uma base de testes vazia. Não espera pelo login. A chave 027 do ramo é a chave da história.
Responsável proposto: Tiago Borja (a equipa decide).

## Checklist

- [ ] AC-1: `StaffService.createStaff(name, username, password, role, actor)` guarda o nome de utilizador, o papel e a palavra-passe com hash. Papéis: rececionista, pessoal do restaurante, gestor.
- [ ] AC-2: Um nome de utilizador repetido é recusado com uma mensagem de que o nome já existe.
- [ ] AC-3: Uma palavra-passe em falta (ou nome de utilizador ou papel) é recusada.
- [ ] AC-4: Um chamador sem o papel de gestor é recusado (usa `StaffService.canCreateStaff()`).
- [ ] Nenhuma palavra-passe em texto simples é guardada ou registada em log.

Created by: BorjaTiago. Updated by: BorjaTiago. AI use: sprint-planner, Claude Code (claude-sonnet-5-5), 2026-10-16. Portuguese translation by general-purpose agent, Claude Code (claude-sonnet-5-5), 2026-10-16; doc-updater, Claude Code (claude-sonnet-5-5), 2026-10-19. Reviewed by: pending.
