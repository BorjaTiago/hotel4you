---
type: story
summary: Iniciar sessão como funcionário do restaurante
parent: epic-authentication-users
requirements: [RF-01, RF-02]
points: 2
points_suggested: 2
status: published
jira_key: H4Y-21
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

## Critérios de aceite (checklist)

- [ ] AC-1: O funcionário do restaurante com credenciais válidas inicia sessão com sucesso.
- [ ] AC-2: Credenciais inválidas são recusadas sem iniciar sessão.
- [ ] AC-3: Um utilizador desconhecido é recusado com a mesma resposta das credenciais inválidas.

## Notas
Apagada do Jira pelo Team Leader em 2026-10-23: o login do restaurante já funciona desde o sprint 2 (H4Y-22 e H4Y-27). A regra "o restaurante só vê as suas funções" fica em H4Y-23.
Fase: 1.ª entrega (login, Q-06). Início de sessão do restaurante: Q-23 (decisão da equipa). A limitação a cozinha e restaurante (RF-25) fica noutra épica (epic-staff-management-access).
Dependências (auditoria 2026-10-16): sprint 3. A conta do funcionário do restaurante vem de "Criar uma conta de funcionário" (create-staff-account). Reutiliza o mecanismo de início de sessão do rececionista.

points_suggested: 2
reference: Listar os quartos por tipo (2)
compared: equal
reasoning: Reuses the login mechanism. Only adds the restaurant staff role.
split_needed: no

ready: true
dor_failed: []
questions: []

Created by: BorjaTiago. Updated by: lukassperandio. AI use: story-writer, acceptance-criteria, Claude Code (claude-sonnet-5-5), 2026-10-16; refiner, Claude Code (claude-sonnet-5-5), 2026-10-16; checklist by story-writer, Claude Code (claude-sonnet-5-5), 2026-10-16; Dependências note by jira-publisher, Claude Code (claude-sonnet-5-5), 2026-10-16; sprint field by sprint-planner, Claude Code (claude-opus-5-5), 2026-10-23; deletion note by sprint-planner, Claude Code (claude-opus-5-5), 2026-10-23. Reviewed by: pending.
