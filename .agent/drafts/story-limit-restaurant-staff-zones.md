---
type: story
summary: Limitar o restaurante à cozinha e ao restaurante
parent: epic-staff-management-access
requirements: [RF-25, RF-01]
points: 3
points_suggested: 3
status: merged
merged_into: regra-acesso-por-local
jira_key:
labels: [access-control]
sprint:
---

Junta-se a `story-regra-acesso-por-local` (decisão do utilizador, 2026-11-13). Esta história não entra em nenhum sprint. O AC-3 (o restaurante não cria nem apaga reservas) já está feito: `ReservationService.create` exige `Permission.MANAGE_RESERVATIONS` e `ReservationService.delete` exige `Permission.DELETE_RESERVATIONS`. Testes: `onCreate_throw_whenActorIsNotReceptionistOrManager`, `onDelete_throwAccessDenied_whenUserIsNotManager`, `onAllows_returnFalse_whenReceptionistDeletesReservations`.

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

## Critérios de aceite (checklist)

- [ ] AC-1: O funcionário do restaurante consegue abrir a cozinha e o restaurante.
- [ ] AC-2: Qualquer outro local ou serviço é recusado com uma mensagem de acesso não permitido.
- [ ] AC-3: O funcionário do restaurante não consegue criar nem apagar reservas.

## Notas
Login com nome de utilizador e palavra-passe (Q-23). Enunciado, secção 2. RF-25. Fase: Q-25.
Dependências (auditoria 2026-10-16): nenhuma forte. Construir e testar sozinha: conta de restaurante e zonas "cozinha" e "restaurante" de teste (foundation T4), sessão simulada (T0). O piso da cozinha não está no enunciado: na demo o gestor cria a cozinha com story-criar-zona (sprint 7). O AC-3 usa o mapa de permissões de story-funcoes-por-papel (sprint 3).

points_suggested: 3
reference: Registar um cliente (3)
compared: equal
reasoning: Regras de permissão por perfil aplicadas aos menus e às ações de reservas. Sem dados novos, mas toca vários ecrãs. Depende do login e do mecanismo de papéis.
split_needed: no

ready: true
dor_failed: []
questions: []

AI use: story-writer, acceptance-criteria, Claude Code (claude-sonnet-5-5), 2026-10-16; refiner, Claude Code (claude-sonnet-5-5), 2026-10-16; estimator, Claude Code (claude-sonnet-5-5), 2026-10-16; Dependências note by backlog-prioritizer, Claude Code (claude-opus-5-5), 2026-10-16; checklist by story-writer, Claude Code (claude-sonnet-5-5), 2026-10-16; sprint-planner, Claude Code (claude-sonnet-5-5), 2026-11-13. Created by: BorjaTiago. Updated by: BorjaTiago. Reviewed by: pending.
