---
type: story
summary: Iniciar sessão como funcionário do restaurante
parent: epic-authentication-users
requirements: [RF-01, RF-02]
points: 2
points_suggested: 2
status: approved
jira_key:
labels: [security, login, 1st-delivery]
sprint:
---

# Título: Iniciar sessão como funcionário do restaurante

Como funcionário do restaurante,
Eu quero iniciar sessão com nome de utilizador e palavra-passe,
Para que o sistema me reconheça como funcionário da empresa externa do restaurante.

## Critérios de aceite

### Cenário AC-1: Credenciais válidas
  Dado que existe um funcionário do restaurante com nome de utilizador e palavra-passe
  Quando o funcionário introduz as credenciais corretas
  Então o sistema inicia a sessão
  E identifica o utilizador como funcionário do restaurante

### Cenário AC-2: Credenciais inválidas
  Dado que existe um funcionário do restaurante com nome de utilizador
  Quando o funcionário introduz uma palavra-passe errada
  Então o sistema recusa o acesso
  E mostra a mensagem genérica "Credenciais inválidas"

### Cenário AC-3: Utilizador desconhecido
  Dado que o nome de utilizador não existe
  Quando alguém tenta iniciar sessão
  Então o sistema recusa o acesso
  E mostra a mesma mensagem do AC-2

## Notas
Fase: 1.ª entrega (login, Q-06). Início de sessão do restaurante: Q-23 (decisão da equipa). A limitação a cozinha e restaurante (RF-25) fica noutra épica (epic-staff-management-access).

points_suggested: 2
reference: Listar os quartos por tipo (2)
compared: equal
reasoning: Reuses the login mechanism. Only adds the restaurant staff role.
split_needed: no

ready: true
dor_failed: []
questions: []

AI use: story-writer, acceptance-criteria, Claude Code (claude-sonnet-5-5), 2026-10-16; refiner, Claude Code (claude-sonnet-5-5), 2026-10-16. Reviewed by: pending.
