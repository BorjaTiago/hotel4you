---
type: story
summary: Fazer check-in de uma reserva
parent: epic-check-in-out
requirements: [RF-13]
points: 3
points_suggested: 3
status: approved
jira_key:
labels: [check-in, 1st-delivery]
sprint:
---

# Título: Fazer check-in de uma reserva

Como rececionista,
Eu quero fazer o check-in de um cliente com reserva ativa,
Para que o cliente fique instalado no quarto reservado.

## Critérios de aceite

### Cenário AC-1: Check-in de reserva ativa
  Dado que o rececionista está autenticado
  E existe uma reserva ativa sem check-in
  Quando o rececionista faz o check-in da reserva
  Então o estado da reserva passa a "check-in efetuado"

### Cenário AC-2: Check-in duplicado
  Dado que a reserva já tem check-in efetuado
  Quando o rececionista tenta fazer de novo o check-in
  Então o sistema recusa a ação
  E o estado mantém-se

### Cenário AC-3: Reserva cancelada
  Dado que a reserva está cancelada
  Quando o rececionista tenta fazer o check-in
  Então o sistema recusa o check-in
  E informa que a reserva está cancelada

### Cenário AC-4: Reserva inexistente
  Dado que não existe reserva com o identificador indicado
  Quando o rececionista tenta fazer o check-in
  Então o sistema informa que a reserva não existe

## Critérios de aceite (checklist)

- [ ] AC-1: O check-in de uma reserva ativa é registado.
- [ ] AC-2: Um check-in duplicado é recusado.
- [ ] AC-3: O check-in de uma reserva cancelada é recusado.
- [ ] AC-4: O check-in de uma reserva inexistente devolve uma resposta de não encontrado.

## Notas
Entrega 1 (Q-06). A entrega do cartão magnético (RF-13, RF-16) fica na entrega final (epic-magnetic-card-access). Depois do check-in o gestor deixa de poder apagar a reserva (RF-22). Dependências (auditoria 2026-10-16): nenhuma forte. Construir e testar sozinha: reservas de teste próprias (ativa, cancelada, com check-in) (foundation T4).

points_suggested: 3
reference: Realizar check-in com cartão magnético (5)
compared: smaller
reasoning: Delivery 1 has no card. Only the state change with guards (duplicate, cancelled, missing) and a simple GUI.
split_needed: no

ready: true
dor_failed: []
questions: []

AI use: story-writer, acceptance-criteria, Claude Code (claude-sonnet-5-5), 2026-10-16; refiner, Claude Code (claude-sonnet-5-5), 2026-10-16; Dependências note by backlog-prioritizer, Claude Code (claude-opus-5-5), 2026-10-16; checklist by story-writer, Claude Code (claude-sonnet-5-5), 2026-10-16. Reviewed by: pending.
