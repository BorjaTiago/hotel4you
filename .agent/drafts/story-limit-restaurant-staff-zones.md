---
type: story
summary: Limitar o restaurante à cozinha e ao restaurante
parent: epic-staff-management-access
requirements: [RF-25, RF-01]
points: 3
points_suggested: 3
status: approved
jira_key:
labels: [access-control]
sprint:
---

# Título: Limitar o restaurante à cozinha e ao restaurante

Como funcionário do restaurante,
Eu quero aceder apenas à cozinha e ao restaurante,
Para que o meu acesso corresponda à minha função na empresa externa.

## Critérios de aceite

### Cenário AC-1: Acesso à cozinha e ao restaurante
  Dado que o funcionário do restaurante iniciou sessão com nome de utilizador e palavra-passe
  Quando abrir a cozinha ou o restaurante
  Então o sistema permite o acesso

### Cenário AC-2: Outro local
  Dado que o funcionário do restaurante tem sessão iniciada
  Quando tentar abrir qualquer outro local ou serviço
  Então o sistema recusa o acesso
  E mostra uma mensagem de acesso não permitido

### Cenário AC-3: Reservas
  Dado que o funcionário do restaurante tem sessão iniciada
  Quando tentar apagar ou criar uma reserva
  Então o sistema recusa a ação

## Notas
Login com nome de utilizador e palavra-passe (Q-23). Enunciado, secção 2. RF-25. Fase: Q-25.

points_suggested: 3
reference: Registar um cliente (3)
compared: equal
reasoning: Regras de permissão por perfil aplicadas aos menus e às ações de reservas. Sem dados novos, mas toca vários ecrãs. Depende do login e do mecanismo de papéis.
split_needed: no

ready: true
dor_failed: []
questions: []

AI use: story-writer, acceptance-criteria, Claude Code (claude-sonnet-5-5), 2026-10-16; refiner, Claude Code (claude-sonnet-5-5), 2026-10-16; estimator, Claude Code (claude-sonnet-5-5), 2026-10-16. Reviewed by: pending.
