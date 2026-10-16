---
type: story
summary: Emitir cartão com número ao fazer check-in
parent: epic-magnetic-card-access
requirements: [RF-13, RF-16]
points: 3
points_suggested: 3
status: approved
jira_key:
labels: [access-control, final-delivery]
sprint:
---

# Título: Emitir cartão com número ao fazer check-in

Como rececionista,
Eu quero emitir um cartão magnético com número ao efetuar o check-in de uma reserva,
Para que o cliente receba o cartão que lhe dá acesso aos locais da reserva.

## Critérios de aceite

### Cenário AC-1: Cartão emitido no check-in
  Dado que existe uma reserva ativa sem check-in efetuado e sem cartão
  Quando o rececionista efetua o check-in
  Então o sistema emite um cartão com um número
  E o cartão fica associado a essa reserva

### Cenário AC-2: Números de cartão distintos
  Dado que já existe um cartão emitido com o número N
  Quando o rececionista efetua o check-in de outra reserva
  Então o novo cartão tem um número diferente de N

### Cenário AC-3: Reserva já tem cartão
  Dado que a reserva já tem um cartão associado
  Quando o rececionista tenta emitir outro cartão para a mesma reserva
  Então o sistema recusa a emissão
  E mostra uma mensagem a indicar que a reserva já tem cartão

### Cenário AC-4: Check-in sem reserva ativa
  Dado que a reserva está cancelada ou não existe
  Quando o rececionista tenta efetuar o check-in
  Então o sistema recusa o check-in
  E não emite nenhum cartão

## Critérios de aceite (checklist)

- [ ] AC-1: O check-in de uma reserva ativa emite um cartão com número, associado a essa reserva.
- [ ] AC-2: Cada novo cartão emitido tem um número diferente dos já emitidos.
- [ ] AC-3: Emitir um segundo cartão para a mesma reserva é recusado com mensagem a indicar que já tem cartão.
- [ ] AC-4: O check-in de uma reserva cancelada ou inexistente é recusado e nenhum cartão é emitido.

## Notas
Um cartão por reserva (Q-09, decisão da equipa). Entrega final (Q-06). Dependências (auditoria 2026-10-16): uma forte, story-check-in (o cartão é emitido dentro do mesmo fluxo; sprint 6 antes do sprint 9). Número do cartão: `CardNumberGenerator` (foundation T6). Testar com reservas de teste com e sem cartão (T4).
Decisão da equipa (2026-10-16): no check-out o cartão deixa de funcionar e o registo fica guardado; o cenário está em story-check-out (AC-4). O cartão emitido aqui só vale durante a estadia.

points_suggested: 3
reference: Registar um cliente (3)
compared: equal
reasoning: Gera número único e liga o cartão à reserva dentro do check-in já existente. Regras: um cartão por reserva, reserva ativa. Menor que o check-in completo (5).
split_needed: no

ready: true
dor_failed: []
questions: []

AI use: story-writer, acceptance-criteria, Claude Code (claude-sonnet-5-5), 2026-10-16; refiner, Claude Code (claude-sonnet-5-5), 2026-10-16; estimator, Claude Code (claude-sonnet-5-5), 2026-10-16; Dependências note by backlog-prioritizer, Claude Code (claude-opus-5-5), 2026-10-16; checklist by story-writer, Claude Code (claude-sonnet-5-5), 2026-10-16. Reviewed by: pending.
