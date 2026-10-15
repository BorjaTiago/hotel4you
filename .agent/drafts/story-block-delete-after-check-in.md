---
type: story
summary: Impedir apagar reservas após check-in
parent: epic-check-in-out
requirements: [RF-13, RF-22]
points: 2
points_suggested: 2
status: approved
jira_key:
labels: [check-in, reservations, 1st-delivery]
sprint:
---

# Título: Impedir apagar reservas após check-in

Como gestor,
Eu quero que o sistema bloqueie o apagar de reservas com check-in efetuado,
Para que o registo de uma estadia em curso ou concluída nunca se perca.

## Critérios de aceite

### Cenário AC-1: Apagar após check-in é recusado
  Dado que o gestor está autenticado
  E a reserva tem check-in efetuado
  Quando o gestor tenta apagar a reserva
  Então o sistema recusa a ação
  E a reserva mantém-se com o estado "check-in efetuado"

### Cenário AC-2: Apagar após check-out é recusado
  Dado que a reserva tem check-out efetuado
  Quando o gestor tenta apagar a reserva
  Então o sistema recusa a ação

### Cenário AC-3: Apagar antes do check-in é permitido
  Dado que a reserva não tem check-in
  Quando o gestor apaga a reserva
  Então a reserva é removida

## Notas
RF-22: só se apaga se o cliente não fez check-in. Decisão (2026-10-16): esta história é a única dona da recusa de apagar após check-in; o AC-3 de story-delete-reservation foi removido para não duplicar. Apagar antes do check-in está em story-delete-reservation. AC-2: uma reserva com check-out efetuado já teve check-in, logo RF-22 também impede apagá-la. Depende de story-check-in.

points_suggested: 2
reference: Listar os quartos por tipo (2)
compared: equal
reasoning: One state guard on delete for two states. One layer, simple rule.
split_needed: no

ready: true
dor_failed: []
questions: []

AI use: story-writer, acceptance-criteria, Claude Code (claude-sonnet-5-5), 2026-10-16; refiner, Claude Code (claude-sonnet-5-5), 2026-10-16. Reviewed by: pending.
