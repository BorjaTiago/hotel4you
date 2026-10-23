---
type: story
summary: Cancelar uma reserva mantendo o histórico
parent: epic-reservations
requirements: [RF-12]
points:
points_suggested: 3
status: published
jira_key: H4Y-55
labels: [reservations, 1st-delivery]
sprint: 04
---

# Título: Cancelar uma reserva mantendo o histórico

Como rececionista,
Eu quero cancelar uma reserva de um cliente,
Para que o quarto fique livre e o registo da reserva se mantenha no histórico.

## Critérios de aceite

### Cenário AC-1: Cancelar reserva ativa
  Dado que existe uma reserva ativa sem check-in
  Quando o rececionista cancela a reserva
  Então o estado da reserva passa a "cancelada"
  E a reserva continua visível no histórico

### Cenário AC-2: Quarto volta a ficar livre
  Dado que a reserva do quarto 201 foi cancelada
  Quando o rececionista consulta quartos livres para o mesmo período
  Então o quarto 201 é oferecido

### Cenário AC-3: Reserva já cancelada
  Dado que a reserva já está cancelada
  Quando o rececionista tenta cancelar de novo
  Então o sistema recusa a ação
  E o estado mantém-se "cancelada"

### Cenário AC-4: Reserva com check-in efetuado
  Dado que a reserva tem check-in efetuado
  Quando o rececionista tenta cancelar a reserva
  Então o sistema recusa a ação
  E o estado da reserva não muda

## Critérios de aceite (checklist)

- [ ] AC-1: Uma reserva ativa pode ser cancelada.
- [ ] AC-2: Depois de cancelar a reserva, o quarto volta a ficar livre.
- [ ] AC-3: Cancelar uma reserva já cancelada é recusado.
- [ ] AC-4: Cancelar uma reserva com check-in efetuado é recusado.

## Notas
Decisão da equipa (2026-10-16): cancelar não é possível depois do check-in (AC-4).
Q-05 (decisão da equipa, não confirmada): cancelar é mudança de estado, fica no histórico. Apagar é só do gestor (RF-22). Cancelar uma reserva com check-in efetuado não está definido no enunciado e fica fora desta história (cenário removido pelo refinador). Libertar o quarto ao cancelar é o efeito normal do cancelamento (RF-12). Dependências (auditoria 2026-10-16): nenhuma forte. Construir e testar sozinha: reservas de teste próprias (ativa, cancelada, com check-in) (foundation T4). O AC-2 verifica com a consulta de quartos livres (sprint 4, já feita).

points_suggested: 3
reference: Registar um cliente (validação de dados e gravação) (3)
compared: equal
reasoning: State change with guards (already cancelled, checked in) and history kept. Room release comes from the free-room query.
split_needed: no

ready: true
dor_failed: []
questions: []

Created by: BorjaTiago. Updated by: lukassperandio. AI use: story-writer, acceptance-criteria, Claude Code (claude-sonnet-5-5), 2026-10-16; refiner, Claude Code (claude-sonnet-5-5), 2026-10-16; Dependências note by backlog-prioritizer, Claude Code (claude-opus-5-5), 2026-10-16; checklist by story-writer, Claude Code (claude-sonnet-5-5), 2026-10-16; sprint field by sprint-planner, Claude Code (claude-opus-5-5), 2026-10-23. Reviewed by: pending.
